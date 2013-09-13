package org.kilic.kmeta

concept executionUnit {
	expression * expressions
}

concept expression {
}

concept addExpression : expression {
	syntax lr `expression ('+'|'-') expression`
}

concept mulExpression : expression {
	syntax lr `expression ('*'|'/') expression`
}

concept unaryExpression : expression {
	syntax `('+'|'-') expression`
}

concept idExpression : expression {
	syntax `ID`
}

concept numExpression : expression {
	syntax `NUM`
}

concept realExpression : expression {
	syntax `REAL`
}