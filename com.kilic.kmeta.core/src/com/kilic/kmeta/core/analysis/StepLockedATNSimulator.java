package com.kilic.kmeta.core.analysis;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.ATNCallEdge;
import com.kilic.kmeta.core.atn.ATNConfig;
import com.kilic.kmeta.core.atn.ATNConfigSet;
import com.kilic.kmeta.core.atn.ATNState;
import com.kilic.kmeta.core.atn.GSS;
import com.kilic.kmeta.core.atn.GSSNode;
import com.kilic.kmeta.core.dfa.DFAState;
import com.kilic.kmeta.core.stream.IStream;

/**
 * Algorithm from:
 * 
 * PARR T., HARWELL S., FISHER K. 
 * Adaptive LL(*) Parsing: The Power of Dynamic Analysis
 */
public class StepLockedATNSimulator {
	ATNState startState;
	GSS gss;
	IStream input;
	
	public StepLockedATNSimulator(ATNState startState, GSS gss, IStream input) {
	}

	// returns the start state of the predicted alternative
	public ATNState adaptivePredict() {
		return null;
	}
	
	void step() {
	}

	Set<ATNConfig> closure(ATNConfig config, Set<ATNConfig> history) {
		if(history == null)
			history = new HashSet<>();
		else if(history.contains(config)) 
			return null;
		
		Set<ATNConfig> result = new HashSet<>();
		result.add(config);
		
		if( config.getState().isFinal() ) {
			// stack is SLL wildcard
			if( config.getCallStack().isAny() ) {
				// get all returns of call for this type
				ATN atn = config.getState().getATN();
				for(ATNCallEdge edge : atn.getAllCallers() ){
					result.addAll(
						closure(
							new ATNConfig(
								edge.getTo(), 
								config.getAlternative(),
								config.getCallStack()),
							history
						)
					);
				}
			} else {
				// nonempty SLL or LL stack
				ATNState popped = null;
				GSSNode poppedStack = null;
				
				result.addAll(
					closure(
						new ATNConfig(
							popped,
							config.getAlternative(),
							poppedStack
						),
						history
					)
				);
			}
			
			return result;
		}
		
		return result;
	}
	
	DFAState target(DFAState d) {
		return null;
	}
	
	void move() {
	}

	int sllPredict(ATNState atnState, DFAState d0, GSSNode g, int offset) {
		DFAState d = d0;
		while(true) {
			DFAState next = d.move(input);
			if(next==null) next = target(d);
			if(next.isErrorState())
				return -1; // error
			boolean isStackSensitive = false;
			if(isStackSensitive)
				return llPredict(atnState, g, offset);
			
			if(d.isFinalState())
				return 1; // i
			
			d = next;
			input.nextChar();
		}
	}
	
	int llPredict(ATNState atnState, GSSNode g, int offset) {
		ATNConfigSet d = startState(atnState, g);
		while(true) {
			ATNConfigSet mv = d.move(input);
			
			Set<ATNConfig> newConfigSet = new HashSet<>();
			
			for( ATNConfig config : mv )
				newConfigSet.addAll( closure(config,null) );
			
			if(newConfigSet.isEmpty())
				return -1;

			Set<Set<Integer>> altSets = getConflictSetsPerLoc(newConfigSet);
			// if ambiguity report return min(x)
		}
	}
	
	Set<Set<Integer>> getConflictSetsPerLoc(Set<ATNConfig> configSet) {
		return null;
	}
	

	ATNConfigSet startState(ATNState atnState, GSSNode g) {
		return null;
	}
	
}
