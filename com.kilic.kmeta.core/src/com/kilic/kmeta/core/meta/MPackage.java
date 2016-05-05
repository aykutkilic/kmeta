package com.kilic.kmeta.core.meta;

import java.util.HashSet;
import java.util.Set;

public class MPackage {
	String name;
	Set<MClass> classes = new HashSet<>();
	MModel model;
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public void addClass(MClass clazz) { 
		if(!classes.contains(clazz)) {
			classes.add(clazz);
			clazz.setPackage(this);
		}
	}
	public MClass[] getClasses() { return (MClass[]) classes.toArray(); }
	
	public void setModel(MModel model) { 
		if(this.model!=model) {
			this.model = model;
			model.addPackage(this);
		}
	}
	
	public MModel getModel() { return model; }
}
