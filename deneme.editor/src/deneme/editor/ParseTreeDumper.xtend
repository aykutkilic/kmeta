package deneme.editor

import org.antlr.v4.runtime.tree.ParseTreeListener
import org.antlr.v4.runtime.ANTLRErrorListener
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.TerminalNode
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.dfa.DFA
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.misc.IntervalSet
import java.util.BitSet

class ParseTreeDumper implements ParseTreeListener, ANTLRErrorListener {
	DenemeParser parser
	int offset
		
	new(DenemeParser parser, int offset) {
		this.parser = parser
		this.offset = offset
		System.out.println(
		'''
		Parse Tree:
		---------------
		offset: «offset»
		
		'''
		)
	}
	
	override enterEveryRule(ParserRuleContext ctx) {
		System.out.println(
		'''
		«indentation(ctx.depth)»> «parser.state»: «ctx.class.simpleName» - «ctx.toString» 
		''')
	}
	
	override exitEveryRule(ParserRuleContext ctx) {
		System.out.println('''«indentation(ctx.depth)»< «parser.state»''')
	}
	
	override visitErrorNode(ErrorNode node) {
		System.out.println(
		'''
		Err - «parser.state»: «node.text»@[«node.symbol.startIndex»-«node.symbol.stopIndex»]
		''')
	}
	
	override visitTerminal(TerminalNode node) {
		System.out.println(	
		'''
		Ter - «parser.state»: «node.text»@[«node.symbol.startIndex»-«node.symbol.stopIndex»]
		''')
	}
	
	override reportAmbiguity(Parser parser, DFA dfa, int arg2, int arg3, boolean arg4, BitSet arg5, ATNConfigSet arg6) {
		System.out.println(	
		'''
		Ambg - «parser.state»: «parser.currentToken»
		''')
	}
	
	override reportAttemptingFullContext(Parser arg0, DFA arg1, int arg2, int arg3, BitSet arg4, ATNConfigSet arg5) {
		System.out.println(	
		'''
		AttFullCtx - «parser.state»: «parser.currentToken»
		''')
	}
	
	override reportContextSensitivity(Parser arg0, DFA arg1, int arg2, int arg3, int arg4, ATNConfigSet arg5) {
		System.out.println(	
		'''
		CtxSens - «parser.state»: «parser.currentToken»
		''')
	}
	
	override syntaxError(Recognizer<?, ?> arg0, Object arg1, int arg2, int arg3, String arg4, RecognitionException arg5) {
		System.out.println(	
		'''
		SynErr - «parser.state»: «parser.currentToken»
		''')
	}
	
	private def indentation(int size) {
		val result = new StringBuilder()
		for (var i=0 ; i<size; i++) result.append(' ')

		result.toString
	}
	
	private def tokenNames(IntervalSet input) {
		input.toList.filter[it>0].map[parser.tokenNames.get(it)]
	}
}