package org.kilic.kmeta;

import com.google.common.base.Joiner;

import java.util.*;

public class Concept extends BaseScope {
    private ExecutionUnit executionUnit;
    private boolean isAbstract;
    private Set<Concept> parents;
    private Map<String, Concept> children;
    private Map<String, Object>  properties;
    private String syntax;

    public Concept(String name, ExecutionUnit container) {
        super(name,container);

        executionUnit = container;
        parents = new HashSet<>();
        children = new HashMap<>();
        properties = new HashMap<>();
    }

    public String getSyntax() {
        return syntax;
    }

    public Map<String, Concept> getChildren() {
        return children;
    }

    public Map<String, Object> getProperties() {
        return properties;
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

    public String getCurrentGrammar() {
        StringBuilder result = new StringBuilder();

        result.append(getShortName()+":\n");
        result.append("  " + Joiner.on(" | ").join(children.values().stream().map( Concept::getShortName).iterator()) + "\n");
        if( syntax != null && !getSyntax().isEmpty())
            result.append("  " + getSyntax() + "\n");

        result.append(";\n\n");

        return result.toString();
    }
}
