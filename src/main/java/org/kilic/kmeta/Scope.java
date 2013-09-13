package org.kilic.kmeta;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface Scope {
    String getShortName();
    String getFQN();

    Set<String> getImports();
    Scope getParentScope();
}
