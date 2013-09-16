package org.kilic.kmeta.meta;

public interface Containment {
    Instantiatable getContainer();
    Multiplicity   getMultiplicity();
    Type           getType();
    AccessEnum     getAccessType();
}
