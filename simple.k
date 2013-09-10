package org.kilic.kmeta

concept execution_unit {
	expr * expressions
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