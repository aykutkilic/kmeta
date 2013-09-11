package org.kilic.kmeta;

import com.google.inject.Inject;
import com.sun.istack.internal.NotNull;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.HashMap;
import java.util.Map;

public class MetaProcessor extends KMetaBaseVisitor {
    private Map<String, Concept> concepts;
    private Linker               conceptsLinker;

    @Inject
    public MetaProcessor() {
        concepts = new HashMap<>();
        conceptsLinker = new Linker();
    }

    public void process(ANTLRFileStream fileStream) {
        KMetaLexer lexer = new KMetaLexer(fileStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        KMetaParser parser = new KMetaParser(tokenStream);

        parser.setBuildParseTree(true);
        ParseTree tree = parser.executionUnit();

        visit(tree);
    }

    @Override
    public Object visitExecutionUnit(@NotNull org.kilic.kmeta.KMetaParser.ExecutionUnitContext ctx) {
        return super.visitExecutionUnit(ctx);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public Object visitConceptStatement(@NotNull org.kilic.kmeta.KMetaParser.ConceptStatementContext ctx) {
        Concept newConcept = new Concept();
        String fqn = ctx.children.get(1).getText();
        this.concepts.put(fqn, newConcept);

        newConcept.setFqn(fqn);
        return super.visitConceptStatement(ctx);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
