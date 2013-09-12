package org.kilic.kmeta;

import java.util.HashSet;
import java.util.Set;

public class Concept extends BaseScope {
    private ExecutionUnit executionUnit;
    private boolean isAbstract;
    private Set<Concept> parents;
    private Set<Concept> children;
    private String syntax;

    public Concept(String name, ExecutionUnit container) {
        super(name,container);

        executionUnit = container;
        parents = new HashSet<>();
        children = new HashSet<>();
    }

    public String getSyntax() {
        return syntax;
    }

    public Set<Concept> getChildren() {
        return children;
    }

    public Set<Concept> getParents() {
        return parents;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public ExecutionUnit getExecutionUnit() {
        return executionUnit;
    }
}
