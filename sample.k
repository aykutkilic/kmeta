concept expr {}

concept ID {
	string name
	text --> ALPHA ALNUM*
}

text => ID {
	syntax ALPHA ALNUM* -> name
}


concept binary_expr : expr {
	expr left, right
}

text => model binary_expr {
	EBNF<<<expr op expr>>> -> left skip right
}

concept sum_expr : binary_expr {
	op = ('+'|'-')
}

concept mul_expr : binary_expr {
	op = ('*'|'/')
}