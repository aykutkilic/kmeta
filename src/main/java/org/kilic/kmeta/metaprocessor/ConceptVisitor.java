package org.kilic.kmeta.metaprocessor;

import org.antlr.v4.runtime.misc.NotNull;
import org.kilic.kmeta.Concept;
import org.kilic.kmeta.KMetaBaseVisitor;
import org.kilic.kmeta.KMetaParser;

/**
 * Created with IntelliJ IDEA.
 * User: Aykut
 * Date: 09/09/13
 * Time: 23:18
 * To change this template use File | Settings | File Templates.
 */
public class ConceptVisitor extends KMetaBaseVisitor {
    MetaProcessor processor;

    public ConceptVisitor(MetaProcessor metaProcessor) {
        this.processor = metaProcessor;
    }

    @Override
    public Object visitConceptStatement(@NotNull KMetaParser.ConceptStatementContext ctx) {
        Concept newConcept = new Concept(KMetaParser.ConceptStatementContext ctx);
        ctx.children
        return super.visitConceptStatement(ctx);
    }
}
