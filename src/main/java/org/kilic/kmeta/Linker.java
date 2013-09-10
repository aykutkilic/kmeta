package org.kilic.kmeta;

import java.util.*;
import java.util.function.Consumer;

public class Linker {
    interface LinkerListener {
        void itemLinked(String fqn, Object linkedItem) throws Exception;
    }

    Map<String, Object> itemRegistry;
    Map<String, Set<Consumer<Object>>> listenersRegistry;

    public void Linker() {
        itemRegistry = new HashMap<>();
        listenersRegistry = new HashMap<>();
    }

    public void reset() {
        itemRegistry.clear();
        listenersRegistry.clear();
    }

    public void registerItem(String fqn, Object item) throws Exception {
        if(itemRegistry.containsKey(fqn)) {
            if( itemRegistry.get(fqn) != item )
                throw new Exception("multiple defined item " + fqn);
        }

        itemRegistry.put(fqn, item);
    }

    public void registerFieldByName(String fqn, Object parentObj, String fieldName ) {
        registerListener(fqn, (_, linkedObj) -> parentObj.getClass().getField(fieldName).set(parentObj, linkedObj));
    }

    public void registerListener(String fqn, LinkerListener listener) {
        if(!listenersRegistry.containsKey(fqn)) {
            listenersRegistry.put(fqn, new HashSet<>());
        }
        listenersRegistry.get(fqn).add(consumer);
    }

    public Set<String> link() {
       Set<String> unresolvedItems = new HashSet<>();

       listenersRegistry.forEach( (fqn, actions) -> {
           if( itemRegistry.containsKey(fqn) )
               actions.forEach( fn -> fn.accept(itemRegistry.get(fqn)) );
           else
               unresolvedItems.add(fqn);
       });

       return unresolvedItems;
    }
}
