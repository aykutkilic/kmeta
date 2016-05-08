grammar Deneme;
 
@header
{
package deneme.editor;
}

// PARSER
program:
	(functiondef|classdef)*;

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

classdef: 		
	'abstract'? 'class' ID ('extends' ID (',' ID)*)? '{' 
		(vardef ';')*
		syntaxdef?
	'}';

vardef:
	( ID multiplicity? | 'var' | 'val' ) ID ('=' expr)?;

syntaxdef:
	'syntax' ':' sExpr ';';
	
sExpr
	: '(' sExpr ')' 			# sParen
	| sExpr ('|' sExpr)+		# sAlt
	| sExpr sExpr+				# sSequence
	| ID '=' sExpr		 		# sAssignment
	| sExpr ('?'|'+'|'*') 		# sMult
	| sExpr '<' sExpr			# sPref
	| sExpr '>' sExpr			# sSuff
	| sExpr '/' sExpr			# sSep
	| '[' ID ']'				# sRef
	| STRING					# sKeyword
	| ID						# sCall
	| sExpr body				# sAction
	| ID '=' (sExpr ':' expr)+  # sSwitch
	;

	
multiplicity:
	'?' | '*' | '+' | '[' INT ']';

functiondef: 'fn' ID  ID '(' (vardef (',' vardef)*)? ')' '{' statement* '}'; 
 
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
    | INT                                		# integerVal
    ;
 

// LEXER

// from antlr4s grammar

/** Allow unicode rule/token names */
ID	:	NameStartChar NameChar*;

fragment
NameChar
	:   NameStartChar
	|   '0'..'9'
	|   '_'
	|   '\u00B7'
	|   '\u0300'..'\u036F'
	|   '\u203F'..'\u2040'
	;

fragment
NameStartChar
	:   'A'..'Z'
	|   'a'..'z'
	|   '\u00C0'..'\u00D6'
	|   '\u00D8'..'\u00F6'
	|   '\u00F8'..'\u02FF'
	|   '\u0370'..'\u037D'
	|   '\u037F'..'\u1FFF'
	|   '\u200C'..'\u200D'
	|   '\u2070'..'\u218F'
	|   '\u2C00'..'\u2FEF'
	|   '\u3001'..'\uD7FF'
	|   '\uF900'..'\uFDCF'
	|   '\uFDF0'..'\uFFFD'
	; // ignores | ['\u10000-'\uEFFFF] ;

INT	: [0-9]+
	;
	
STRING
	:  '\'' (ESC_SEQ | ~['\r\n\\])* '\''
	;

// Any kind of escaped character that we can embed within ANTLR
// literal strings.
fragment
ESC_SEQ
	:	'\\'
		(	// The standard escaped character set such as tab, newline, etc.
			[btnfr"'\\]
		|	// A Java style Unicode escape sequence
			UNICODE_ESC
		|	// Invalid escape
			.
		|	// Invalid escape at end of file
			EOF
		)
	;

fragment
UNICODE_ESC
    :   'u' (HEX_DIGIT (HEX_DIGIT (HEX_DIGIT HEX_DIGIT?)?)?)?
    ;

fragment
HEX_DIGIT : [0-9a-fA-F]	;

WS  :	[ \t\r\n\f]+ -> channel(HIDDEN)	;

DOC_COMMENT
	:	'/**' .*? ('*/' | EOF)
	;

BLOCK_COMMENT
	:	'/*' .*? ('*/' | EOF)  -> channel(HIDDEN)
	;

LINE_COMMENT
	:	'//' ~[\r\n]*  -> channel(HIDDEN)
	;
