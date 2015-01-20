package com.diagware.testgen.core

import java.util.ArrayList
import java.util.List
import org.eclipse.cdt.core.dom.ast.ASTVisitor
import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement
import org.eclipse.cdt.core.dom.ast.IASTDeclaration
import org.eclipse.cdt.core.model.CoreModel
import org.eclipse.cdt.core.model.ICElement
import org.eclipse.cdt.core.model.ITranslationUnit
import org.eclipse.core.runtime.IPath
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition
import org.junit.Test
import org.eclipse.core.runtime.Path
import org.eclipse.core.resources.ResourcesPlugin

class TestGen {
	
	@Test
	def deneme() {
		var p = Path.fromPortableString("/bsw/src/Meas/Meas.c")
		var f = ResourcesPlugin::getWorkspace.root.getFile(p);
		var tu = getTranslationUnit(p)
		var fns = tu.functionDefinitions
		fns.forEach[System.out.println(it.toString)]
	}
	
	def getTranslationUnit(IPath cFilePath) {
		var x = CoreModel::getDefault
		x.create(cFilePath) as ITranslationUnit;
	}
		
	def getFunctionDefinitions(ITranslationUnit tu) {
		var v = new FunctionDefinitionVisitor
		tu.AST.accept(v)
		return v.elements
	}
}



class FunctionDefinitionVisitor extends ASTVisitor {
	var List<IASTFunctionDefinition> elements = new ArrayList
	
	new() { shouldVisitDeclarations = true }
	
	override visit(IASTDeclaration declaration) {
		if( declaration instanceof IASTFunctionDefinition ) {
			elements.add(declaration)
		}
		
		PROCESS_SKIP
	}
	
	def List<IASTFunctionDefinition> getElements() { return elements; }
}