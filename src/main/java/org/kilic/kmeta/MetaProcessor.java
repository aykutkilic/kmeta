package org.kilic.kmeta;

import com.google.inject.Inject;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;

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

    public void process(ANTLRFileStream fileStream) {
        KMetaLexer lexer = new KMetaLexer(fileStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        KMetaParser parser = new KMetaParser(tokenStream);

        parser.setBuildParseTree(true);
        conceptsLinker.reset();
        processExecutionUnit(parser.executionUnit());
        conceptsLinker.resolve();
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

        ctx.metaExpression().stream().findFirst().ifPresent(e -> newConcept.setSyntax(e.getText()));

        concepts.put(newConcept.getFQN(), newConcept);
    }

    private void processDefinition(Concept newConcept, KMetaParser.DefinitionContext c) {
    }
}
