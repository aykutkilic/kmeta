package com.kilic.kmeta.core.meta;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.syntax.ISyntaxExpr;

public class MClass {
	String name;
	
	MPackage pack;
	Set<MClass> superClasses = new HashSet<>();
	Set<MClass> subClasses = new HashSet<>();
	Set<MStructuralFeature> features = new HashSet<>();

	ISyntaxExpr syntax;
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public MPackage getPackage() { return pack; }
	public void setPackage(MPackage pack) {
		this.pack = pack;
		pack.addClass(this);
	}
	
	public void addSuperClass(MClass superClass) { 
		superClasses.add(superClass);
		superClass.subClasses.add(this);
	}
	
	public MClass[] getSuperClasses() { return (MClass[]) superClasses.toArray(); }
	public MClass[] getSubClasses() { return (MClass[]) subClasses.toArray(); }
	
	public void addFeature(MStructuralFeature feature) { features.add(feature); }
	public MStructuralFeature[] getFeatures() { return (MStructuralFeature[]) features.toArray(); }

	public ISyntaxExpr getSyntax() { return syntax; }
	public void setSyntax(ISyntaxExpr syntax) { this.syntax = syntax; }
}
