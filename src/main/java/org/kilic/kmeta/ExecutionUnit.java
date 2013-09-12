package org.kilic.kmeta;

import java.util.HashSet;
import java.util.Set;

public class ExecutionUnit extends BaseScope {
    private Set<Concept> concepts;

    public ExecutionUnit(String name) {
        super(name,null);
        concepts = new HashSet<>();
    }

    public Set<Concept> getConcepts() {
        return concepts;
    }
}
