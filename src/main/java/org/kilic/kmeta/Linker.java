package org.kilic.kmeta;

import java.util.*;
import java.util.function.Consumer;

public class Linker {
    interface LinkerListener {
        void itemLinked(String fqn, Object linkedItem) throws RuntimeException;
    }

    Map<String, Object> itemRegistry;
    Map<String, Set<LinkerListener>> listenersRegistry;

    public void Linker() {
        itemRegistry = new HashMap<>();
        listenersRegistry = new HashMap<>();
    }

    public void reset() {
        itemRegistry.clear();
        listenersRegistry.clear();
    }

    public void registerItem(String fqn, Object item) throws RuntimeException {
        if (itemRegistry.containsKey(fqn)) {
            if (itemRegistry.get(fqn) != item)
                throw new RuntimeException("multiple defined item " + fqn);
        }

        itemRegistry.put(fqn, item);
    }

    public void registerFieldByName(String fqn, Object parentObj, String fieldName) {
        registerListener(fqn,
                (ignored, linkedObj)
                        -> {
                    try {
                        parentObj.getClass().getField(fieldName).set(parentObj, linkedObj);
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public void registerListener(String fqn, LinkerListener listener) {
        if (!listenersRegistry.containsKey(fqn)) {
            listenersRegistry.put(fqn, new HashSet<>());
        }
        listenersRegistry.get(fqn).add(listener);
    }

    public Set<String> link() throws RuntimeException {
        Set<String> unresolvedItems = new HashSet<>();

        listenersRegistry.forEach((fqn, actions) -> {
            if (itemRegistry.containsKey(fqn))
                actions.forEach(item -> item.itemLinked(fqn, itemRegistry.get(fqn)));
            else
                unresolvedItems.add(fqn);
        });

        return unresolvedItems;
    }
}
