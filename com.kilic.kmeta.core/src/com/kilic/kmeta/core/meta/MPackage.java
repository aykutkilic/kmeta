package com.kilic.kmeta.core.meta;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MPackage {
	private String name;
	private final Set<MClass> classes = new HashSet<>();
	private MModel model;
	
	public String getName() { return name; }
	public void setName(final String name) { this.name = name; }

	public void addClass(final MClass clazz) { 
		if(!classes.contains(clazz)) {
			classes.add(clazz);
			clazz.setPackage(this);
		}
	}
	public Collection<MClass> getClasses() { return classes; }
	
	public void setModel(final MModel model) { 
		if(this.model!=model) {
			this.model = model;
			model.addPackage(this);
		}
	}
	
	public MModel getModel() { return model; }
}
