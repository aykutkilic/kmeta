package org.kilic.kmeta

concept execution_unit {
	class * classes
}

concept class {
	string 			name
	class ref 		extends
	interface ref * implements
	
	field *    fields
	property * properties
	method *   methods
	
	syntax <<<
	class ID ('extends' FQN)? ('implements' FQN (',' FQN)*)? '{'
		(field|property|method)*
	'}'
	>>>
}

void fn();

void main() {
	fn();
}

concept method {
	string  name
	field * parameters
}

concept expr {
}

concept add_expr : expr {
	syntax <<< expr ('+'|'-') expr >>>
}

concept mul_expr : expr {
	syntax <<< expr ('*'|'/') expr >>>
}

concept unary_expr : expr {
	syntax <<< ('+'|'-') expr >>>
}

concept id_expr : expr {
	syntax <<< ID >>>
}

concept num_expr : expr {
	syntax <<< NUM >>>
}

concept real_expr : expr {
	syntax <<< REAL >>>
}