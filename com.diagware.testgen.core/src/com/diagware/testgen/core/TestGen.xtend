package com.diagware.testgen.core

import java.util.ArrayList
import java.util.List
import org.eclipse.cdt.core.dom.ast.ASTVisitor
import org.eclipse.cdt.core.dom.ast.IASTDeclaration
import org.eclipse.cdt.core.dom.ast.IASTExpression
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition
import org.eclipse.cdt.core.dom.ast.IASTIdExpression
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit
import org.eclipse.cdt.core.model.CoreModel
import org.eclipse.cdt.core.model.ITranslationUnit
import org.eclipse.cdt.core.parser.util.ASTPrinter
import org.eclipse.cdt.internal.core.dom.parser.c.CNodeFactory
import org.eclipse.cdt.internal.core.dom.rewrite.astwriter.MyASTWriterVisitor
import org.eclipse.core.runtime.IPath
import org.eclipse.core.runtime.Path
import org.junit.Test

class TestGen {

	@Test
	def deneme() {
		val factory = CNodeFactory.getDefault

		var p = Path.fromPortableString("/A/a.c")
		var tu = getTranslationUnit(p)
		var wrappedAST = tu.AST.copy

		ASTPrinter::print(wrappedAST)
		System.out.println

		var fns = wrappedAST.functionDefinitions

		val newName = factory.newName(("deneme").toCharArray)

		fns.forEach [
			System.out.println(it.declarator.name.toString)
			it.referredFunctions.forEach [
				var id = it.functionNameExpression as IASTIdExpression
				//System.out.println("calls " + id.name.toString)
				var x = id.name.resolveBinding
				//System.out.println(id.name.toString + " -> " + x.name)
				if (id.name.toString.equals("fnB")) {
				}
				id.name = newName
			]
		]

		var writerVisitor = new MyASTWriterVisitor
		wrappedAST.accept(writerVisitor)
		var code = writerVisitor.scribe.toString

		System.out.println(code)
	}

	def getTranslationUnit(IPath cFilePath) {
		var x = CoreModel::getDefault
		x.create(cFilePath) as ITranslationUnit;
	}

	def getFunctionDefinitions(IASTTranslationUnit ast) {
		var v = new FunctionDefinitionVisitor
		ast.accept(v)
		return v.elements
	}

	def getReferredFunctions(IASTFunctionDefinition fn) {
		var v = new FunctionCallVisitor
		fn.body.accept(v);
		return v.elements
	}
}

class FunctionCallVisitor extends ASTVisitor {
	var List<IASTFunctionCallExpression> elements = new ArrayList

	new() {
		shouldVisitExpressions = true
	}

	override visit(IASTExpression e) {
		if (e instanceof IASTFunctionCallExpression) {
			elements.add(e)
		}

		PROCESS_CONTINUE
	}

	def List<IASTFunctionCallExpression> getElements() { return elements; }
}

class FunctionDefinitionVisitor extends ASTVisitor {
	var List<IASTFunctionDefinition> elements = new ArrayList

	new() {
		shouldVisitDeclarations = true
	}

	override visit(IASTDeclaration declaration) {
		if (declaration instanceof IASTFunctionDefinition) {
			elements.add(declaration)
		}

		PROCESS_SKIP
	}

	def List<IASTFunctionDefinition> getElements() { return elements; }
}
