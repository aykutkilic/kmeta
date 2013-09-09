package org.kilic.kmeta;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Aykut
 * Date: 09/09/13
 * Time: 23:58
 * To change this template use File | Settings | File Templates.
 */
public class Concept {
    private String  id;
    private boolean isAbstract;
    private List<Concept> parents;
    private List<Concept> children;

    private String        syntax;

    public Concept(KMetaParser.ConceptStatementContext ctx) {
    }

    public String getSyntax() {
        return syntax;
    }

    public List<Concept> getChildren() {
        return children;
    }

    public List<Concept> getParents() {
        return parents;
    }

    public boolean isAbstract() {
        return isAbstract;
    }
}
