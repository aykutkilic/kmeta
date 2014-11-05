package deneme.editor

import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.eclipse.core.resources.IMarker
import org.eclipse.jface.text.ITextViewer
import org.eclipse.jface.text.contentassist.CompletionProposal
import org.eclipse.jface.text.contentassist.IContentAssistProcessor
import org.eclipse.ui.texteditor.MarkerUtilities
import org.antlr.v4.runtime.tree.ParseTreeListener
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.TerminalNode
import org.antlr.v4.runtime.misc.IntervalSet

class DenemeCAProcessor implements IContentAssistProcessor {

	override computeCompletionProposals(ITextViewer viewer, int offset) {
		val text = viewer.document.get
		var lexer = new DenemeLexer(new ANTLRInputStream(text))
		var tokens = new CommonTokenStream(lexer);
		val parser = new DenemeParser(tokens);
		parser.setBuildParseTree(true);
		
		var	IntervalSet expectedTokens = null
		
		parser.addParseListener(new ParseTreeListener() {
			
			override enterEveryRule(ParserRuleContext ctx) { 
							}
			
			override exitEveryRule(ParserRuleContext arg0) {}
			
			override visitErrorNode(ErrorNode arg0) { }
			
			override visitTerminal(TerminalNode arg0) { }
			
		})
		
		parser.program();

		#[
			new CompletionProposal(parser.currentToken.toString, offset, 0, offset),
			new CompletionProposal('aykut', offset, 0, offset),
			new CompletionProposal('atilla', offset, 0, offset),
			new CompletionProposal('serkan', offset, 0, offset),
			new CompletionProposal('burcu', offset, 0, offset)
		]
	}

	override getCompletionProposalAutoActivationCharacters() { #['.'] }

	override computeContextInformation(ITextViewer viewer, int offset) { null }

	override getContextInformationAutoActivationCharacters() { null }

	override getContextInformationValidator() { null }

	override getErrorMessage() { null }
}

class DenemeCAParseTreeListener implements ParseTreeListener {
	int offset
	DenemeParser parser
	IntervalSet expectedTokens

	override enterEveryRule(ParserRuleContext ctx) {
		if( ctx.sourceInterval.a<=offset && ctx.sourceInterval.b>=offset ) {
			expectedTokens = parser.expectedTokensWithinCurrentRule
		}
	}
	
	override exitEveryRule(ParserRuleContext arg0) {
	}
	
	override visitErrorNode(ErrorNode arg0) {
	}
	
	override visitTerminal(TerminalNode arg0) {
	}
}
