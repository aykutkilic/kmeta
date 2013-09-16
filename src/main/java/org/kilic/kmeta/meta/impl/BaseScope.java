package org.kilic.kmeta.meta.impl;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseScope implements Scope {
    private String shortName;
    private String fqn;
    private Scope parentScope;
    private Set<String> imports;

    protected BaseScope(String shortName, Scope parent) {
        this.shortName = shortName;
        this.parentScope = parent;
        imports = new HashSet<>();
    }

    protected void setShortName(String newValue) {
        shortName = newValue;
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public String getFQN() {
        if( parentScope == null)
            return shortName;

        return parentScope.getFQN() + "." + shortName;
    }

    @Override
    public Scope getParentScope() {
        return parentScope;
    }

    @Override
    public Set<String> getImports() {
        return imports;
    }
}
