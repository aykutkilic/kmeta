package org.kilic.kmeta;

import java.util.List;

public class Concept {
    private String        fqn;
    private boolean       isAbstract;
    private List<Concept> parents;
    private List<Concept> children;
    private String        syntax;

    public String getFQN() {
        return fqn;
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
