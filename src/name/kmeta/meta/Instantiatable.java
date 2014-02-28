package name.kmeta.meta;

import java.util.Set;

public interface Instantiatable extends Scope {
    Set<Containment> getContainments();
}
