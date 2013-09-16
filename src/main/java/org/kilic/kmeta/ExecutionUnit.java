package org.kilic.kmeta;

import org.kilic.kmeta.meta.impl.BaseScope;
import org.kilic.kmeta.meta.impl.ConceptImpl;

import java.util.HashSet;
import java.util.Set;

public class ExecutionUnit extends BaseScope {
    private Set<ConceptImpl> concepts;

    public ExecutionUnit(String name) {
        super(name,null);
        concepts = new HashSet<>();
    }

    public Set<ConceptImpl> getConcepts() {
        return concepts;
    }
}
