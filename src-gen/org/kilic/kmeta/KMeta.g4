grammar KMeta;

executionUnit:	packageStatement
                importStatement*
                (conceptStatement|compositionStatement|transformationStatement)*;

packageStatement : KW_PACKAGE fullyQualifiedName;
importStatement  : KW_IMPORT fullyQualifiedPath;
conceptStatement :
    KW_CONCEPT ID (':' listOfIds)? '{'
        (definition|definitionWithInitExpr|syntaxDefinition)*
    '}';

compositionStatement :
    KW_COMPOSITION ID '{'
        (port|binding)*
    '}';

transformationStatement:
    domain ('=>'|'<='|'<=>') domain '{'
    '}';

domain: 'text' ('('ID')')
        | ID;

port :
    ('in'|'out'|'inout') ID ID;

binding:
    ID ('='|'<='|'=>'|'<>') expression;

definition:
    ID multiplicity? ID;

definitionWithInitExpr:
    Definition '=' expression;

multiplicity:
    '?'|'+'|'*'|('['NUM']');

syntaxDefinition: 'syntax' 'lr'? META;

expression:
    expression ('*'|'/') expression # mulExpression
    |	expression ('+'|'-') expression # addExpression
    |   ('+'|'-') expression  # unaryExpression
    |	INT # intNumber
    |   REAL # realNumber
    |   STRING # string
    |   ID # reference
    |   FnCallExpressionRule # fnCallExpression
    |   MatrixExpressionRule # matricExpression
    |   LambdaExpressionRule # lambdaExpression
    |	'(' expression ')' # parenthesisExpression
    |   expression ':' expression # rangeExpression
    ;

fnCallExpressionRule :  ID '(' listOfExpressions? ')';
matrixExpressionRule  :  '[' listOfExpressions (';' listOfExpressions)* ']';
lambdaExpressionRule  :  '(' listOfIds ')' '->' expression;

listOfIds :
    ID (',' ID)*;

listOfExpressions :
    expression (',' expression)*;

fullyQualifiedName : ID ('.' ID)*;
fullyQualifiedPath : ID ('.' ID)* ('.' '*')?;

KW_CONCEPT : 'concept';
KW_COMPOSITION : 'composition';
KW_IMPORT  : 'import';
KW_PACKAGE : 'package';

INT        : NUM+;
REAL       : NUM* '.' NUM*;
ID         : ALPHA (ALNUM | '_')*;
META       : '`' ~('`')* '`';
STRING     : '"'  (  ~('"'|'\\'|'\n'|'\r') )*  '"'
             {
                  setText( org.antlr.v4.misc.CharSupport.getStringFromGrammarStringLiteral( getText() ) );
             };

fragment ALNUM      : ALPHA | [0-9];
fragment ALPHA      : [a-zA-Z];
fragment NUM        : [0-9];
fragment NEWLINE    : [\r\n]+;
fragment HEX_DIGIT  : [0-9a-fA-F];