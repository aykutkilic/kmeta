package name.kmeta.meta;

import java.util.HashSet;
import java.util.Set;

public interface Linker {
    interface LinkerListener<T> {
        void itemLinked(T linkedItem) throws RuntimeException;
    }

    void reset();
    void linkScopeItemToObject(ScopeItem scopeItem, T item) throws RuntimeException;
    void registerListener(String fqn, LinkerListener<T> listener);
    Set<String> resolve() throws RuntimeException;
}
