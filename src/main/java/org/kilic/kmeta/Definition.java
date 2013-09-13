package org.kilic.kmeta;

public class Definition {
    private String name;
    private Multiplicity multiplicity;
    private Concept defType;
    private Concept container;

    public Definition(Concept container) {
        this.container = container;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Multiplicity getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(Multiplicity multiplicity) {
        this.multiplicity = multiplicity;
    }

    public Concept getType() {
        return defType;
    }

    public void setType(Concept defType) {
        this.defType = defType;
    }

    public Concept getContainer() {
        return container;
    }
}
