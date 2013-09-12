package org.kilic.kmeta;

public abstract class BaseScope implements Scope {
    private String shortName;
    private String fqn;
    private Scope parentScope;

    protected BaseScope(String shortName, Scope parent) {
        this.shortName = shortName;
        this.parentScope = parent;
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
}
