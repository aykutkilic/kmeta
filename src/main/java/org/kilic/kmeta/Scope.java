package org.kilic.kmeta;

import java.util.Collection;

public interface Scope {
    String getShortName();
    String getFQN();

    Scope getParentScope();
}
