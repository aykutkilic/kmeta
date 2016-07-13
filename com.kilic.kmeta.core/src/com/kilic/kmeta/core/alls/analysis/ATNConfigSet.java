package com.kilic.kmeta.core.alls.analysis;

import java.util.HashSet;
import java.util.Iterator;

import com.kilic.kmeta.core.alls.atn.IATNEdge;
import com.kilic.kmeta.core.alls.stream.IStream;

public class ATNConfigSet extends HashSet<ATNConfig> {
	private static final long serialVersionUID = -4055740663032286654L;

	public ATNConfigSet() {}
	
	public ATNConfigSet move(IStream input) {
		ATNConfigSet result = new ATNConfigSet();
		
		Iterator<ATNConfig> i = iterator();
		while(i.hasNext()) {
			ATNConfig c = i.next();

			for(IATNEdge next : c.getState().move(input) ) {
				result.add(
					new ATNConfig(
						next,
						c.getAlternative(),
						c.getCallStack()
					));
			}
		}
		
		return result;
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
