package com.kilic.kmeta.core.meta;

public abstract class MStructuralFeature {
	protected String name;
	IMultiplicity mult = new OneMult();
	MClass containerClass;

	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public void setMult(IMultiplicity mult) { this.mult = mult; }
	public IMultiplicity getMult() { return mult; }
	
	public void setContainerClass(MClass containerClass) { 
		this.containerClass = containerClass;
		this.containerClass.addFeature(this);
	}
	
	public MClass getContainerClass() { return containerClass; }
}
