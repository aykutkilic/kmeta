package deneme.editor

import java.util.BitSet
import org.antlr.v4.runtime.ANTLRErrorListener
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonToken
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.RuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import org.antlr.v4.runtime.misc.IntervalSet
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTreeListener
import org.antlr.v4.runtime.tree.TerminalNode
import org.eclipse.jface.text.ITextViewer
import org.eclipse.jface.text.contentassist.CompletionProposal
import org.eclipse.jface.text.contentassist.IContentAssistProcessor

class DenemeCAProcessor implements IContentAssistProcessor {

	override computeCompletionProposals(ITextViewer viewer, int offset) {
		val text = viewer.document.get
		var lexer = new DenemeLexer(new ANTLRInputStream(text))
		var tokens = new CommonTokenStream(lexer);
		val parser = new DenemeParser(tokens);
		parser.setBuildParseTree(true);

		var listener = new DenemeCAParseTreeListener(parser, offset)
		parser.addParseListener(listener)
		parser.addErrorListener(listener)

		parser.program();

		println("offset: " + offset)
		listener.completionProposalList
	}

	override getCompletionProposalAutoActivationCharacters() { #['.'] }

	override computeContextInformation(ITextViewer viewer, int offset) { null }

	override getContextInformationAutoActivationCharacters() { null }

	override getContextInformationValidator() { null }

	override getErrorMessage() { null }
}

class DenemeCAParseTreeListener implements ParseTreeListener, ANTLRErrorListener {
	int offset
	DenemeParser parser
	RuleContext ruleContext

	CompletionProposal[] completionProposalList

	new(DenemeParser parser, int offset) {
		this.parser = parser
		this.offset = offset
	}

	override enterEveryRule(ParserRuleContext ctx) {
		if(isTokenUnderCursor(ctx.start)) {
			println(parser.currentToken.toString)
			println(">> " + parser.ruleNames.get(ctx.ruleIndex))
			println(">> " + ctx.start.text + " " + ctx.start.startIndex + ":" + ctx.start.stopIndex)
			println(">> " + parser.expectedTokens.toList.filter[it > 0].map[parser.tokenNames.get(it)])
			ruleContext = ctx
			
			createCompletionList(ctx.start, parser.expectedTokens)	
		}
	}

	override exitEveryRule(ParserRuleContext ctx) {
		var Token token = null
		if(isTokenUnderCursor(parser.currentToken)) token = parser.currentToken
		if(isTokenUnderCursor(ctx.stop)) token = ctx.stop
		if(token!=null) {
			println(parser.currentToken.toString)
			println("<< " + parser.ruleNames.get(ctx.ruleIndex))
			if (ctx.stop != null)
				println("<< " + ctx.stop.text + " " + ctx.stop.startIndex + ":" + ctx.stop.stopIndex)
			println("<< " + parser.expectedTokens.toList.filter[it > 0].map[parser.tokenNames.get(it)])
				
			createCompletionList(token, parser.expectedTokens)	
		}
	}

	override visitErrorNode(ErrorNode node) {
		println("ER ")
	}

	override visitTerminal(TerminalNode node) {
		if(isTokenUnderCursor(node.symbol)) {
			println('TT ' + node.text)
			createCompletionList(node.symbol, parser.expectedTokens)	
		}
		
	}

	override reportAmbiguity(
		Parser recognizer,
		DFA dfa,
		int startIndex,
		int stopIndex,
		boolean exact,
		BitSet ambigAlts,
		ATNConfigSet configs
	) {
		println("reportAmbiguity")
	}

	override reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
		BitSet conflictingAlts, ATNConfigSet configs) {
		println("reportAttemptingFullContext")
	}

	override reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction,
		ATNConfigSet configs) {
		println("reportContextSensitivity")
	}

	override syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
		String msg, RecognitionException e) {
		var token = offendingSymbol as CommonToken
		if(!isTokenUnderCursor(token)) return;

		println("-- " + token.startIndex + " - " + token.stopIndex);

		var IntervalSet expected = null;
		if (e != null) {
			expected = e.expectedTokens
		} else {
			expected = recognizer.ATN.getExpectedTokens(recognizer.state, ruleContext)
		}
		
		
		if (expected != null) {
			System.out.println("-- " + expected.toList.filter[it > 0].map[parser.tokenNames.get(it)])
			if(isTokenUnderCursor(token)) {
				createCompletionList(token, expected)
			}
		}
	}

	def createCompletionList(Token token, IntervalSet expected) {
		if (completionProposalList!=null) return
		
		val tokenNames = expected.toList.filter[it > 0].map[parser.tokenNames.get(it)]
		val tokenLength = if(token.stopIndex>token.startIndex) token.stopIndex - token.startIndex else 0
		completionProposalList =
			tokenNames.map[new CompletionProposal(it, token.startIndex, tokenLength, offset)].toList
		
		//quitListening
	}

	def isTokenUnderCursor(Token token) {
		if(offset == token.startIndex) return true;

		token.startIndex <= offset && token.stopIndex >= offset
	}

	def quitListening() {
		parser.removeParseListener(this)
		parser.removeErrorListener(this)
	}

	def CompletionProposal[] getCompletionProposalList() {
		completionProposalList
	}
}
