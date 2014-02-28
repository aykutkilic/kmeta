package name.kmeta.meta.factory;

import org.kilic.kmeta.meta.impl.ConceptImpl;
import org.kilic.kmeta.meta.impl.MultiplicityImpl;

public class ContainmentImpl {
    private String name;
    private MultiplicityImpl multiplicity;
    private ConceptImpl defType;
    private ConceptImpl container;

    public ContainmentImpl(ConceptImpl container) {
        this.container = container;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultiplicityImpl getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(MultiplicityImpl multiplicity) {
        this.multiplicity = multiplicity;
    }

    public ConceptImpl getType() {
        return defType;
    }

    public void setType(ConceptImpl defType) {
        this.defType = defType;
    }

    public ConceptImpl getContainer() {
        return container;
    }
}
