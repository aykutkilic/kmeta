package deneme.editor

import org.eclipse.jface.text.IDocument
import org.eclipse.jface.text.TextAttribute
import org.eclipse.jface.text.presentation.PresentationReconciler
import org.eclipse.jface.text.rules.DefaultDamagerRepairer
import org.eclipse.jface.text.rules.IWhitespaceDetector
import org.eclipse.jface.text.rules.RuleBasedScanner
import org.eclipse.jface.text.rules.SingleLineRule
import org.eclipse.jface.text.rules.Token
import org.eclipse.jface.text.rules.WhitespaceRule
import org.eclipse.jface.text.source.ISourceViewer
import org.eclipse.jface.text.source.SourceViewerConfiguration
import org.eclipse.jface.text.contentassist.ContentAssistant
import org.eclipse.jface.text.rules.WordRule
import org.eclipse.jface.text.rules.IWordDetector

class DenemeSourceViewerConfiguration extends SourceViewerConfiguration {
	ColorManager		   colorManager = new ColorManager
	PresentationReconciler reconciler
	
	override getPresentationReconciler(ISourceViewer sourceViewer) {
		if( reconciler != null) return reconciler
		
		reconciler = new PresentationReconciler
		
		val keywordToken = new Token(
			new TextAttribute(colorManager.getColor(IDenemeColorConstants.KEYWORD))
		)

		var stringToken = new Token(
			new TextAttribute( colorManager.getColor( IDenemeColorConstants.STRING ) )
		)
		
		val defaultToken = new Token(
			new TextAttribute( colorManager.getColor(IDenemeColorConstants.DEFAULT) )
		)
		
		var whiteSpaceDetector = new IWhitespaceDetector() {
			override isWhitespace(char c) {
				c==' ' || c=='\t' || c=='\n' || c=='\r'
			}
		}
		
		var scanner = new RuleBasedScanner
		scanner.rules = #[
			new SingleLineRule('"','"', stringToken, '\\'),
			new WhitespaceRule(whiteSpaceDetector)
		]
		scanner.defaultReturnToken = defaultToken		
		var dr = new DefaultDamagerRepairer(scanner)
		
		reconciler.setDamager(dr, DenemePartitionScanner.DNM_STRING)
		reconciler.setRepairer(dr, DenemePartitionScanner.DNM_STRING)
		
		val keywordRule = new WordRule(new IWordDetector() {
			override isWordStart(char c) { Character.isLetter(c) }			
			override isWordPart(char c) { Character.isLetterOrDigit(c) }
		})
		
		#[ 
		  'fn', 'struct',
		  'var', 'val', 'it',
		  'switch', 'default',
		  'if', 'else', 'else if',  
		  'return', 'break', 'skip',
		  'int', 'uint',
		  'real', 'f32', 'f64',
		  'u8', 'u16', 'u32', 'u64', 'u128',
		  's8', 's16', 's32', 's64', 's128',
		  'bool', 'string'
	    ].forEach[keywordRule.addWord(it, keywordToken)]
		
		scanner = new RuleBasedScanner
		scanner.rules = #[
			keywordRule
		]
		scanner.defaultReturnToken = new Token(new TextAttribute(colorManager.getColor(IDenemeColorConstants.DEFAULT)))
		dr = new DefaultDamagerRepairer(scanner)
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE)
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE)
		
		reconciler
	}

	override getContentAssistant(ISourceViewer sourceViewer) {
		var assistant = new ContentAssistant
		var processor = new DenemeCAProcessor
		
		assistant.setContentAssistProcessor( processor, IDocument.DEFAULT_CONTENT_TYPE )
		
		assistant.enableAutoActivation( true )
		assistant.autoActivationDelay = 500
		
		assistant
	}
	
}