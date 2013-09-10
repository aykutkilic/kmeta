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
    private final ConceptFactory conceptFactory;

    @Inject
    public MetaProcessor(ConceptFactory conceptFactory) {
        this.conceptFactory = conceptFactory;

        concepts = new HashMap<>();
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
        return super.visitConceptStatement(ctx);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
