package com.kilic.kmeta.core.discriminator;

import java.util.LinkedList;

import com.kilic.kmeta.core.meta.MClass;
import com.kilic.kmeta.core.syntax.ISyntaxExpr;

public class Discriminator {
	MClass clazz;
	Discriminator parent;
	LinkedList<Discriminator> children;
	PredictionTreeNode root = new PredictionTreeNode(null);
	
	
	public Discriminator(MClass clazz) {
		this.clazz = clazz;
		
		for(MClass subclass : clazz.getSubClasses() )
			addChild(new Discriminator(subclass));
		
		ISyntaxExpr syntax = clazz.getSyntax();
		if( syntax!=null ) appendSyntax(syntax);
	}
	
	
	private void appendSyntax(ISyntaxExpr syntax) {
		//for(PredictionTreeNode child : root.getChildren() ) {
			// check if overlaps. create child nodes accordingly.
		//}
	}

	public MClass getMClass() { return clazz; }
	public Discriminator getParent() { return parent; }
	public Discriminator[] getChildren() { return (Discriminator[]) children.toArray(); }
	
	protected void addChild(Discriminator child) {
		this.children.add(child);
		child.parent = this;		
	}
}
