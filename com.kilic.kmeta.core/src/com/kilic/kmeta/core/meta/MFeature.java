package com.kilic.kmeta.core.meta;

public abstract class MFeature {
	protected String name;
	MClass containerClass;
	Multiplicity mult = Multiplicity.ONE;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMult(Multiplicity mult) {
		this.mult = mult;
	}

	public Multiplicity getMult() {
		return mult;
	}

	public void setContainerClass(MClass containerClass) {
		this.containerClass = containerClass;
		this.containerClass.addFeature(this);
	}

	public MClass getContainerClass() {
		return containerClass;
	}
}
