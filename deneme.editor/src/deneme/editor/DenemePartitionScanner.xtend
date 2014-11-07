package deneme.editor

import org.eclipse.jface.text.rules.MultiLineRule
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner
import org.eclipse.jface.text.rules.Token

class DenemePartitionScanner extends RuleBasedPartitionScanner {
	public static val DNM_STRING  = "DNM_STRING"

	new() {
		var string = new Token(DNM_STRING)
		
		predicateRules = #[
			new MultiLineRule("'", "'", string)
		]
	}
}