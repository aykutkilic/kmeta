package org.kilic.kmeta.meta;

import java.util.Set;

public interface Generalizable extends Instantiatable {
    Set<Generalizable> getSuperTypes();
    Set<Generalizable> getSubTypes();

    Set<Containment> getContainments(boolean includeInherited);
}
