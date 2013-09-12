package org.kilic.kmeta;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MetaProcessor {
    private ExecutionUnit executionUnit;
    private Map<String, Concept> concepts;
    private Linker<Concept> conceptsLinker;

    @Inject
    public MetaProcessor() {
        concepts = new HashMap<>();
        conceptsLinker = new Linker<>();
    }

    public void process(ANTLRFileStream fileStream) {
        KMetaLexer lexer = new KMetaLexer(fileStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        KMetaParser parser = new KMetaParser(tokenStream);

        parser.setBuildParseTree(true);
        conceptsLinker.reset();
        processExecutionUnit(parser.executionUnit());
        Collection<String> unresolvedItems = conceptsLinker.resolve();

        String currentGrammar = computeCurrentGrammar();
    }

    public String computeCurrentGrammar() {
        StringBuilder result = new StringBuilder();

        concepts.values().forEach(c-> result.append(c.getCurrentGrammar()));

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
        ctx.conceptStatement().forEach( c -> processConcept(newExecutionUnit, c));

        this.executionUnit = newExecutionUnit;
    }

    public void processConcept(ExecutionUnit unit, KMetaParser.ConceptStatementContext ctx) {
        String shortName = ctx.ID().getText();
        Concept newConcept = new Concept(shortName, unit);

        ctx.definition().forEach(c -> processDefinition(newConcept, c));
        //ctx.definitionWithInitExpr().forEach();
        if(ctx.listOfIds()!=null)
            ctx.listOfIds().ID().forEach(id -> {
                conceptsLinker.registerListener(id.getText(), item -> newConcept.getParents().add(item));
            });

        ctx.metaExpression().stream().findFirst().ifPresent(e -> newConcept.setSyntax(getSyntax(e)));

        concepts.put(newConcept.getFQN(), newConcept);
        conceptsLinker.linkFQNToObject(newConcept.getShortName(), newConcept);
    }

    private void processDefinition(Concept newConcept, KMetaParser.DefinitionContext c) {
    }

    private String getSyntax(KMetaParser.MetaExpressionContext ctx) {
        return Joiner.on("").join(ctx.children.subList(2,ctx.children.size()-1));
    }
}
