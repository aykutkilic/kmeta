grammar Deneme;
 
@header
{
package deneme.editor;
}

stat: ( BIN | INT | HEX | REAL )+;

INT	: [0-9]+;
BIN : '0b' [0-9a-fA-F]+ | [0-9a-fA-F]+ 'b';
HEX : '0x' [0-9a-fA-F]+ | [0-9a-fA-F]+ 'h';
REAL: [0-9]+ '.' [0-9]* ('e' [+-]? [0-9]+)?
	  | '.' [0-9]* ('e' [+-]? [0-9]+)?;

WS	: [ \t\r\n]+ -> skip;