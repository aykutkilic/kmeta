package deneme.editor.antlr

import org.junit.Test
import org.antlr.v4.tool.Grammar
import org.antlr.v4.tool.ast.GrammarASTVisitor
import org.antlr.v4.tool.ast.GrammarAST
import org.antlr.v4.tool.ast.GrammarRootAST
import org.antlr.v4.tool.ast.RuleAST
import org.antlr.v4.tool.ast.BlockAST
import org.antlr.v4.tool.ast.OptionalBlockAST
import org.antlr.v4.tool.ast.PlusBlockAST
import org.antlr.v4.tool.ast.StarBlockAST
import org.antlr.v4.tool.ast.AltAST
import org.antlr.v4.tool.ast.NotAST
import org.antlr.v4.tool.ast.PredAST
import org.antlr.v4.tool.ast.RangeAST
import org.antlr.v4.tool.ast.SetAST
import org.antlr.v4.tool.ast.RuleRefAST
import org.antlr.v4.tool.ast.TerminalAST

public class Antlr4Test {
	val someGrammar = '''
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
	'''
	
	@Test
	public def void deneme() {
		val g = new Grammar(someGrammar)
		
		g.ast.visit(new GrammarASTVisitor() {
			override visit(GrammarAST node) {
				node.children?.forEach[
					if(it instanceof RuleAST) 					visit(it as RuleAST)
					else if (it instanceof BlockAST)  			visit(it as BlockAST)
					else if (it instanceof OptionalBlockAST)	visit(it as OptionalBlockAST)
					else if (it instanceof PlusBlockAST)  		visit(it as PlusBlockAST)
					else if (it instanceof StarBlockAST)  		visit(it as StarBlockAST)
					else if (it instanceof AltAST)  			visit(it as AltAST)
					else if (it instanceof NotAST)  			visit(it as NotAST)
					else if (it instanceof PredAST)  			visit(it as PredAST)
					else if (it instanceof RangeAST)  			visit(it as RangeAST)
					else if (it instanceof SetAST)  			visit(it as SetAST)
					else if (it instanceof RuleRefAST)  		visit(it as RuleRefAST)
					else if (it instanceof TerminalAST)  		visit(it as TerminalAST)
					
					visit(it as GrammarAST)
				]
				node
			}
			
			override visit(GrammarRootAST node) {
				node.children?.forEach[this.visit(it as GrammarAST)]
				node
			}
			
			override visit(RuleAST node) {
				System::out.println(node.ruleName)
				node
			}
			
			override visit(BlockAST node) {
				node.children?.forEach[this.visit(it as GrammarAST)]
				node
			}
			
			override visit(OptionalBlockAST node) {}
			override visit(PlusBlockAST node) {}
			override visit(StarBlockAST node) {}
			
			override visit(AltAST node) {
				System::out.println(node.text)
				node
			}
			
			override visit(NotAST node) {}
			override visit(PredAST node) {}
			override visit(RangeAST node) {}
			override visit(SetAST node) {}
			
			override visit(RuleRefAST node) {
				System::out.println(node.text)
				node
			}
			
			override visit(TerminalAST node) {
				System::out.println(node.text)
				node
			}
		})
	}	
}