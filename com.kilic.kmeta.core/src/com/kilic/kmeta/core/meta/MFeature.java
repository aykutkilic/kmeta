package com.kilic.kmeta.core.meta;

public abstract class MFeature {
	protected String name;
	private MClass containerClass;
	private Multiplicity mult = Multiplicity.ONE;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setMult(final Multiplicity mult) {
		this.mult = mult;
	}

	public Multiplicity getMult() {
		return mult;
	}

	public void setContainerClass(final MClass containerClass) {
		this.containerClass = containerClass;
		this.containerClass.addFeature(this);
	}

	public MClass getContainerClass() {
		return containerClass;
	}
}
