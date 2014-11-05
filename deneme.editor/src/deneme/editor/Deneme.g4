grammar Deneme;
 
@header
{
package deneme.editor;
}

// PARSER
program : 
	(functiondef|structdef)*;

statement: 
	assignment|switchStatement|ifStatement|loopStatement|loopFlowStatement|returnStatement|vardef|expr;

assignment:
	ID '=' expr;

ifStatement: 
	'if' '(' expr ')' body
	('else if' '(' expr ')' body)*
	('else' body)?;

switchStatement:
	'switch' '(' expr ')' '{'
		(expr* ':' body)*
		('default' ':' body)?
	'}';

loopStatement: 
	expr '->' body;

loopFlowStatement:
	'break' | 'skip';

returnStatement: 
	'return' expr;

body: 
	'{' statement* '}' | statement;

structdef: 		
	'struct' ID '{' (vardef ';')* '}';
	
vardef:
	( ID multiplicity? | 'var' | 'val' ) ID ('=' expr)?;
	
multiplicity:
	'?' | '*' | '+' | '[' INT ']';

functiondef: 'fn' ID  ID '(' (vardef (',' vardef)*)? ')' body; 
 
expr
    : '(' expr ')'                		 		# paren
    | '[' expr '..' expr (':' expr)? ']' 		# rangeExpr
    | '[' expr* ']'						 		# listExpr
    | expr '[' expr '..' expr (':' expr)? ']'   # accessListElems
    | '{' (expr (',' expr)*)? '}' 		 		# structVal
    | expr '->' expr 		 					# map
    | ID '(' (expr (',' expr)*)? ')' 	 		# fnCall
    | expr '.' ID  						 		# memberVarAccess
    | expr '.' ID '(' (expr (',' expr)*)? ')' 	# fnCall
    | ('+'|'-')expr						 		# sign
    | expr ('*'|'/') expr   		 	 		# multOrDiv
    | expr ('+'|'-') expr   		 	 		# addOrSub
    | expr ('++'|'--')					 		# increment
    | '~' expr 					   	     		# not
    | expr ('>>'|'<<') expr		     	 		# bitshift
    | expr ('&'|'|') expr		     	 		# bitwise
    | expr ('&&'|'||') expr	     		 		# logic
    | expr ('>'|'>='|'<'|'<=') expr		 		# comparison
    | expr ('=='|'<>') expr				 		# equality
    | expr ('+='|'-='|'*='|'/=') expr	 		# relative
    | 'it'								 		# itExpr
    | ID 'as' ID								# cast
    | ID 'is' ID								# instanceOf
    | ID                                 		# identifier
    | STRING                             		# stringVal
    | INT                                		# integerVal;
 

// LEXER

STRING : '"' (' '..'~')* '"';
ID     : '^'?('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;
INT    : '0'..'9'+;
WS     : [ \t\n\r]+ -> skip ;