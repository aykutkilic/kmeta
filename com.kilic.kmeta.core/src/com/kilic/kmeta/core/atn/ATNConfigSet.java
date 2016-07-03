package com.kilic.kmeta.core.atn;

import java.util.HashSet;
import com.kilic.kmeta.core.stream.IStream;

public class ATNConfigSet extends HashSet<ATNConfig> {
	private static final long serialVersionUID = -4055740663032286654L;

	public ATNConfigSet() {}
	
	public ATNConfigSet move(IStream input) {
		return null;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	} 
	
	@Override
	public String toString() {
		return super.toString();
	}
}
