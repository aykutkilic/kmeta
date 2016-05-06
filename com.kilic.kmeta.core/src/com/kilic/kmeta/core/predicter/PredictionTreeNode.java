package com.kilic.kmeta.core.predicter;

import java.util.LinkedList;

import com.kilic.kmeta.core.meta.MClass;
import com.kilic.kmeta.core.syntax.ISyntaxExpr;

public class PredictionTreeNode {
	ISyntaxExpr syntax;
	PredictionTreeNode parent;
	LinkedList<PredictionTreeNode> children;
	MClass predictedClass;
	
	public PredictionTreeNode(ISyntaxExpr syntax) {
		this.syntax = syntax;
	}
	
	public PredictionTreeNode getParent() { return parent; }
	public void setParent(PredictionTreeNode parent) { this.parent = parent; }
	
	public ISyntaxExpr getSyntax() { return syntax; }
	public void setSyntax(ISyntaxExpr syntax) { this.syntax = syntax; }
	
	public void addChild(PredictionTreeNode node) { children.add(node); }
	public PredictionTreeNode[] getChildren() { return (PredictionTreeNode[]) children.toArray(); }
	
	public MClass getPredictedClass() { return predictedClass; }
	public void setPredictedClass(MClass predictedClass) { this.predictedClass = predictedClass; }
}
