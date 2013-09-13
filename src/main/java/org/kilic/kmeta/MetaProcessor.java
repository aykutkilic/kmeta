package org.kilic.kmeta;

import com.google.inject.Inject;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MetaProcessor {
    private ExecutionUnit executionUnit;
    private Map<String, Concept> concepts;
    private Linker<Concept> conceptsLinker;

    @Inject
    public MetaProcessor() {
        concepts = new HashMap<>();
        conceptsLinker = new Linker<>();
    }

    public void process(ANTLRFileStream fileStream) throws IOException {
        KMetaLexer lexer = new KMetaLexer(fileStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        KMetaParser parser = new KMetaParser(tokenStream);

        parser.setBuildParseTree(true);
        conceptsLinker.reset();
        processExecutionUnit(parser.executionUnit());
        Collection<String> unresolvedItems = conceptsLinker.resolve();

        String currentGrammar = computeCurrentGrammar();

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("simple.g4"), Charset.defaultCharset())) {
            writer.write(currentGrammar, 0, currentGrammar.length());
        }
    }

    public String computeCurrentGrammar() {
        StringBuilder result = new StringBuilder();

        concepts.values().forEach(c -> {
            String grammar = c.getCurrentGrammar();
            if( grammar != null )
                result.append(c.getCurrentGrammar());
        });

        // dumping the footer.
        result.append("INT        : NUM+;\n");
        result.append("REAL       : NUM* '.' NUM*;\n");
        result.append("ID         : ALPHA (ALNUM | '_')*;");
        result.append("STRING     : '\"'  (  ~('\"'|'\\\\'|'\\n'|'\\r') )*  '\"'\n");
        result.append("{\n");
        result.append("    setText( org.antlr.v4.misc.CharSupport.getStringFromGrammarStringLiteral( getText() ) );\n");
        result.append("};\n");
        result.append("\n");
        result.append("fragment ALNUM      : ALPHA | [0-9];\n");
        result.append("fragment ALPHA      : [a-zA-Z];\n");
        result.append("fragment NUM        : [0-9];\n");
        result.append("fragment NEWLINE    : [\\r\\n]+;\n");
        result.append("fragment HEX_DIGIT  : [0-9a-fA-F];\n");

        return result.toString();
    }

    private void processExecutionUnit(KMetaParser.ExecutionUnitContext ctx) {
        String fqn = ctx.packageStatement().fullyQualifiedName().getText();

        ExecutionUnit newExecutionUnit = new ExecutionUnit(fqn);
        ctx.conceptStatement().forEach(c -> processConcept(newExecutionUnit, c));

        this.executionUnit = newExecutionUnit;
    }

    public void processConcept(ExecutionUnit unit, KMetaParser.ConceptStatementContext ctx) {
        String shortName = ctx.ID().getText();
        Concept newConcept = new Concept(shortName, unit);

        ctx.definition().forEach(c -> processDefinition(newConcept, c));
        //ctx.definitionWithInitExpr().forEach();
        if (ctx.listOfIds() != null)
            ctx.listOfIds().ID().forEach(id -> {
                conceptsLinker.registerListener(id.getText(),
                        item -> {
                            newConcept.getParentConcepts().add(item);
                            item.getSubConcepts().add(newConcept);
                        });
            });

        ctx.syntaxDefinition().stream().findFirst().ifPresent(e -> newConcept.setSyntax(getSyntax(newConcept, e)));

        concepts.put(newConcept.getFQN(), newConcept);
        conceptsLinker.linkFQNToObject(newConcept.getShortName(), newConcept);
    }

    private void processDefinition(Concept newConcept, KMetaParser.DefinitionContext c) {
        Multiplicity mul = new Multiplicity();
        mul.createFromMultiplicityContext(c.multiplicity());
        String name = c.ID(1).toString();
        Definition newDefinition = new Definition(newConcept);
        newDefinition.setMultiplicity(mul);
        newDefinition.setName(name);
        newConcept.getContainedConcepts().put(name, newDefinition);
        conceptsLinker.registerListener(c.ID(0).toString(), item -> newDefinition.setType(item));
    }

    private SyntaxDefinition getSyntax(Concept newConcept, KMetaParser.SyntaxDefinitionContext ctx) {
        SyntaxDefinition result = new SyntaxDefinition(newConcept);

        if (ctx.getChild(1).getText().equals("lr"))
            result.setIfLeftRecursive(true);

        String syntaxSection = ctx.META().getText();
        result.setEBNF(syntaxSection.substring(1, syntaxSection.length() - 1));
        return result;
    }
}
