package org.kilic.kmeta;

import java.util.HashSet;
import java.util.Set;

public class Concept {
    private ExecutionUnit executionUnit;
    private String fqn;
    private boolean isAbstract;
    private Set<Concept> parents;
    private Set<Concept> children;
    private String syntax;

    void Concept() {
        parents = new HashSet<>();
        children = new HashSet<>();
    }

    public String getFQN() {
        return fqn;
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

    public void setFqn(String fqn) {
        this.fqn = fqn;
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

    public void setExecutionUnit(ExecutionUnit executionUnit) {
        this.executionUnit = executionUnit;
    }
}
