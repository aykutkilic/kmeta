package com.kilic.kmeta.core.alls.analysis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.kilic.kmeta.core.alls.atn.ATNCharSetEdge;
import com.kilic.kmeta.core.alls.atn.ATNStringEdge;
import com.kilic.kmeta.core.alls.atn.ATNTokenEdge;
import com.kilic.kmeta.core.alls.atn.IATNEdge;
import com.kilic.kmeta.core.alls.stream.IStream;

public class ATNConfigSet extends HashSet<ATNConfig> {
	private static final long serialVersionUID = -4055740663032286654L;

	public ATNConfigSet() {}

	public ATNConfigSet move(IStream input) {
		ATNConfigSet result = new ATNConfigSet();

		Iterator<ATNConfig> i = iterator();
		while (i.hasNext()) {
			ATNConfig c = i.next();

			for (IATNEdge next : c.getState().move(input)) {
				result.add(new ATNConfig(next.getTo(), c.getAlternative(), c.getCallStack()));
			}
		}

		return result;
	}

	// we assume that the set contains the whole epsilon closure
	public Set<IATNEdge> getNextTerminalEdges() {
		Set<IATNEdge> result = new HashSet<>();
		Iterator<ATNConfig> i = iterator();
		while (i.hasNext()) {
			ATNConfig c = i.next();
			if (!c.getState().hasNext())
				continue;
			IATNEdge edge = c.getState().nextEdge();
			if (edge instanceof ATNCharSetEdge || edge instanceof ATNStringEdge || edge instanceof ATNTokenEdge)
				result.add(edge);
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