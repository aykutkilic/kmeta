package org.kilic.kmeta.meta;

import java.util.Set;

public interface Scope extends ScopeItem {
    Set<ScopeItemImport> getImports();
    Set<ScopeItem>       getItems();
}
