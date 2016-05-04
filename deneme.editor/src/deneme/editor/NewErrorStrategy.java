package deneme.editor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.atn.ATNState;
import org.antlr.v4.runtime.atn.Transition;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.eclipse.jface.text.contentassist.CompletionProposal;

public class NewErrorStrategy extends DefaultErrorStrategy {
	Set<CompletionProposal> completionProposalList = new HashSet<CompletionProposal>();

	public Set<CompletionProposal> getCompletionProposalList() {
		return completionProposalList;
	}
	
	@Override
	protected void reportUnwantedToken(Parser recognizer) {
		//completionProposalList.clear();
		Vocabulary v = recognizer.getVocabulary();
		
		for( int t : recognizer.getExpectedTokens().toList() ) {
			String token = v.getDisplayName(t);
			completionProposalList.add(new CompletionProposal(token, 0, token.length(), 0));
		}
		
		super.reportUnwantedToken(recognizer);
	}
	
	@Override
	protected void reportInputMismatch(Parser recognizer, InputMismatchException e) {
		//completionProposalList.clear();
		Vocabulary v = recognizer.getVocabulary();
		
		for( int t : e.getExpectedTokens().toList() ) {
			String token = v.getDisplayName(t);
			completionProposalList.add(new CompletionProposal(token, 0, token.length(), 0));
		}
		
		super.reportInputMismatch(recognizer, e);
	}
	
	@Override
	protected void reportNoViableAlternative(Parser recognizer, NoViableAltException e) {
		IntervalSet tokens = e.getExpectedTokens();
		ATNConfigSet cfgs = e.getDeadEndConfigs();
		Vocabulary v = recognizer.getVocabulary();
		
		//completionProposalList.clear();
		
		for( ATNState state : cfgs.getStates() ) {
			for(Transition t : state.getTransitions()) {
				for(Interval interval : t.label().getIntervals()) {
					for( int i=interval.a; i<=interval.b; i++ ) {
						String token = v.getDisplayName(i);
						completionProposalList.add(new CompletionProposal(token, 0, token.length(), 0));
					}
				}
			}
		}
		
		super.reportNoViableAlternative(recognizer, e);
	}
}
