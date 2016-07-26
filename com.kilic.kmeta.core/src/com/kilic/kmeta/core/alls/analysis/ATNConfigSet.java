package com.kilic.kmeta.core.alls.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNCharSetEdge;
import com.kilic.kmeta.core.alls.atn.ATNState;
import com.kilic.kmeta.core.alls.atn.ATNStringEdge;
import com.kilic.kmeta.core.alls.atn.ATNTokenEdge;
import com.kilic.kmeta.core.alls.atn.IATNEdge;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.util.CharSet;

public class ATNConfigSet extends HashSet<ATNConfig> {
	private static final long serialVersionUID = -4055740663032286654L;

	public ATNConfigSet() {
	}

	public ATNConfigSet move(IStream input) {
		ATNConfigSet result = new ATNConfigSet();

		Iterator<ATNConfig> i = iterator();
		while (i.hasNext()) {
			ATNConfig c = i.next();

			if (input.hasEnded() && c.getState().isFinalState()) {
				result.add(c);
			} else {
				for (IATNEdge next : c.getState().move(input)) {
					result.add(new ATNConfig(next.getTo(), c.getAlternative(), c.getCallStack()));
				}
			}
		}

		return result;
	}

	public Map<CharSet, Set<IATNEdge>> getNextDistinctCharSets() {
		Map<CharSet, IATNEdge> csToState = new HashMap<>();

		Iterator<ATNConfig> i = iterator();
		while (i.hasNext()) {
			ATNConfig c = i.next();
			ATNState state = c.getState();
			if (!state.hasNext())
				continue;
			IATNEdge edge = state.nextEdge();
			if (edge instanceof ATNCharSetEdge) {
				CharSet cs = ((ATNCharSetEdge) edge).getCharSet();
				csToState.put(cs, edge);
			}
		}

		Map<CharSet, Set<IATNEdge>> result = new HashMap();
		Set<CharSet> dcss = CharSet.getDistinctCharSets(csToState.keySet());
		for (CharSet dcs : dcss) {
			for (Entry<CharSet, IATNEdge> cs2s : csToState.entrySet()) {
				if (cs2s.getKey().intersects(dcs)) {
					if (!result.containsKey(dcs)) {
						Set<IATNEdge> as = new HashSet<>();
						as.add(cs2s.getValue());
						result.put(dcs, as);
					} else {
						result.get(dcs).add(cs2s.getValue());
					}
				}
			}
		}

		return result;
	}

	public ATNConfigSet moveByEdges(Set<IATNEdge> edgesToMove) {
		ATNConfigSet result = new ATNConfigSet();

		Iterator<ATNConfig> i = iterator();
		while (i.hasNext()) {
			ATNConfig c = i.next();
			ATNState state = c.getState();
			if (!state.hasNext())
				continue;
			IATNEdge edge = state.nextEdge();
			if (edgesToMove.contains(edge)) {
				ATNConfig newConfig = new ATNConfig(edge.getTo(), c.alternative, c.callStack);
				result.add(newConfig);
			} else
				result.add(c);
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