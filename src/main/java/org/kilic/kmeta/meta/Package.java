package org.kilic.kmeta.meta;

import java.util.Set;

public interface Package extends Scope {
    Set<Concept> getConcepts();
}
