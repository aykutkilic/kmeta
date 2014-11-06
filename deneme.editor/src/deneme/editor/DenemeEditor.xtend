package deneme.editor

import java.util.BitSet
import org.antlr.v4.runtime.ANTLRErrorListener
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import org.eclipse.core.resources.IResource
import org.eclipse.core.runtime.CoreException
import org.eclipse.jface.text.IDocument
import org.eclipse.ui.IEditorInput
import org.eclipse.ui.editors.text.TextEditor
import org.eclipse.ui.ide.ResourceUtil
import org.eclipse.ui.texteditor.MarkerUtilities
import org.eclipse.core.resources.IMarker
import org.antlr.v4.runtime.CommonToken

class DenemeEditor extends TextEditor {
	static val DENEME_SYNTAX_ERROR_MARKER_ID = "deneme.markers.syntaxerror"

	IDocument document
	IResource resource

	new() {
		super()

		sourceViewerConfiguration = new DenemeSourceViewerConfiguration
		documentProvider = new DenemeDocumentProvider
	}

	override protected doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input)
		document = documentProvider.getDocument(input)
		resource = ResourceUtil::getResource(input)
	}

	override protected editorSaved() {
		super.editorSaved()

		resource.deleteMarkers(DENEME_SYNTAX_ERROR_MARKER_ID, true, IResource.DEPTH_ZERO);

		val text = document.get
		var lexer = new DenemeLexer(new ANTLRInputStream(text))
		var tokens = new CommonTokenStream(lexer);
		var parser = new DenemeParser(tokens);
		parser.setBuildParseTree(true);

		parser.addErrorListener(
			new ANTLRErrorListener() {
				override reportAmbiguity(Parser arg0, DFA arg1, int arg2, int arg3, boolean arg4, BitSet arg5,
					ATNConfigSet arg6) {
				}

				override reportAttemptingFullContext(Parser arg0, DFA arg1, int arg2, int arg3, BitSet arg4,
					ATNConfigSet arg5) {
				}

				override reportContextSensitivity(Parser arg0, DFA arg1, int arg2, int arg3, int arg4, ATNConfigSet arg5) {
				}

				override syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
					int charPositionInLine, String msg, RecognitionException e) {
					addMarker(line, offendingSymbol as CommonToken, msg)
				}
			})

		parser.program();
	}

	def addMarker(int lineNumber, CommonToken token, String msg) {
		var map = newHashMap()
		
		MarkerUtilities::setMessage(map, msg)
		MarkerUtilities::setLineNumber(map, lineNumber)
		MarkerUtilities::setCharStart(map, token.startIndex)
		MarkerUtilities::setCharEnd(map,  token.stopIndex+1 )
		
		map.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_ERROR));

		MarkerUtilities::createMarker(resource, map, DENEME_SYNTAX_ERROR_MARKER_ID);
	}

}
