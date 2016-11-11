package com.kilic.kmeta.core.meta;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MModel {
	private String name;
	private Set<MPackage> packages = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void addPackage(final MPackage pack) {
		if (!packages.contains(pack))
			packages.add(pack);
		if (pack.getModel() != this)
			pack.setModel(this);
	}

	public Collection<MPackage> getPackages() {
		return packages;
	}
}
