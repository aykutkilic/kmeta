package com.kilic.kmeta.core.meta;

import java.util.HashSet;
import java.util.Set;

public class MModel {
	String name;
	Set<MPackage> packages = new HashSet<>();
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public void addPackage(MPackage pack) {
		if(!packages.contains(pack)) packages.add(pack);
		if(pack.getModel()!=this) pack.setModel(this);
	}
	
	public MPackage[] getPackages() { return (MPackage[]) packages.toArray(); }
}
