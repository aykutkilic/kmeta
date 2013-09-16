package org.kilic.kmeta.meta;

import java.util.Set;

public interface Instantiatable extends Scope {
    Set<Containment> getContainments();
}
