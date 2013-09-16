package org.kilic.kmeta.meta.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LinkerImpl<T> implements Linker {

    Map<String, T> itemRegistry;
    Map<String, Set<LinkerListener>> listenersRegistry;

    public LinkerImpl() {
        itemRegistry = new HashMap<>();
        listenersRegistry = new HashMap<>();
    }

    public void reset() {
        itemRegistry.clear();
        listenersRegistry.clear();
    }

    public void linkFQNToObject(String fqn, T item) throws RuntimeException {
        if (itemRegistry.containsKey(fqn)) {
            if (itemRegistry.get(fqn) != item)
                throw new RuntimeException("multiple defined item " + fqn);
        }

        itemRegistry.put(fqn, item);
    }

    public void registerListener(String fqn, LinkerListener<T> listener) {
        if (!listenersRegistry.containsKey(fqn)) {
            listenersRegistry.put(fqn, new HashSet<>());
        }
        listenersRegistry.get(fqn).add(listener);
    }

    public Set<String> resolve() throws RuntimeException {
        Set<String> unresolvedItems = new HashSet<>();

        listenersRegistry.forEach((fqn, actions) -> {
            if (itemRegistry.containsKey(fqn))
                actions.forEach(item -> item.itemLinked(itemRegistry.get(fqn)));
            else
                unresolvedItems.add(fqn);
        });

        return unresolvedItems;
    }
}
