package com.kilic.kmeta.core.predicter;

import java.util.LinkedList;

import com.kilic.kmeta.core.meta.MClass;
import com.kilic.kmeta.core.syntax.ISyntaxExpr;

public class Predicter {
	MClass clazz;
	Predicter parent;
	LinkedList<Predicter> children;
	PredictionTreeNode root = new PredictionTreeNode(null);
	
	
	public Predicter(MClass clazz) {
		this.clazz = clazz;
		
		for(MClass subclass : clazz.getSubClasses() )
			addChild(new Predicter(subclass));
		
		ISyntaxExpr syntax = clazz.getSyntax();
		if( syntax!=null ) appendSyntax(syntax);
	}
	
	
	private void appendSyntax(ISyntaxExpr syntax) {
		//for(PredictionTreeNode child : root.getChildren() ) {
			// check if overlaps. create child nodes accordingly.
		//}
	}

	public MClass getMClass() { return clazz; }
	public Predicter getParent() { return parent; }
	public Predicter[] getChildren() { return (Predicter[]) children.toArray(); }
	
	protected void addChild(Predicter child) {
		this.children.add(child);
		child.parent = this;		
	}
}
