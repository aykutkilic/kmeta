package com.kilic.kmeta.core.meta;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.syntax.ISyntaxExpr;

public class MClass {
	private String name;

	private MPackage pack;
	private final Set<MClass> superClasses = new HashSet<>();
	private final Set<MClass> subClasses = new HashSet<>();
	private final Set<MFeature> features = new HashSet<>();

	private ISyntaxExpr syntax;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public MPackage getPackage() {
		return pack;
	}

	public void setPackage(final MPackage pack) {
		this.pack = pack;
		pack.addClass(this);
	}

	public void addSuperClass(final MClass superClass) {
		superClasses.add(superClass);
		superClass.subClasses.add(this);
	}

	public Collection<MClass> getSuperClasses() {
		return superClasses;
	}

	public Collection<MClass> getSubClasses() {
		return subClasses;
	}

	public void addFeature(MFeature feature) {
		features.add(feature);
	}

	public Collection<MFeature> getFeatures() {
		return features;
	}

	public ISyntaxExpr getSyntax() {
		return syntax;
	}

	public void setSyntax(final ISyntaxExpr syntax) {
		this.syntax = syntax;
	}
}
