package com.kilic.kmeta.core.meta;

public class FixedMult implements IMultiplicity {
	int count;
	
	public FixedMult(int count) {
		this.count = count;
	}
	
	public int getCount() { return count; }
}
