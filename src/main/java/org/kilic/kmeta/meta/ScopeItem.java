package org.kilic.kmeta.meta;

public interface ScopeItem {
    Scope  getParentScope();
    String getName();
    String getFQN();
}
