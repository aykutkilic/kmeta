grammar Deneme;
 
@header
{
package deneme.editor;
}

@members { boolean enum_is_keyword=true; }

stat: expr '=' expr ';'
	| expr ';';

expr: expr '*' expr
	| expr '+' expr
	| expr '(' expr ')' // f(x)
	| id;
	
id	: ID | {!enum_is_keyword}? 'enum';
ID	: [A-Za-z]+;
WS	: [ \t\r\n]+ -> skip;
