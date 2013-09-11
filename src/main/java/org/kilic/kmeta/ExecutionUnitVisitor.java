package org.kilic.kmeta;

import org.antlr.v4.runtime.misc.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Aykut
 * Date: 09/09/13
 * Time: 00:19
 * To change this template use File | Settings | File Templates.
 */
public class ExecutionUnitVisitor extends KMetaBaseVisitor {
    @Override
    public Object visitConceptStatement(@NotNull KMetaParser.ConceptStatementContext ctx) {
        return super.visitConceptStatement(ctx);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
