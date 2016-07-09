package com.kilic.kmeta.core.predictiondfa;

import com.kilic.kmeta.core.tn.IEdge;
import com.kilic.kmeta.core.tn.INode;

public abstract class DFAEdgeBase<K> implements IEdge<K> {
	INode<K> from, to;
	
	@Override
	public INode<K> getFrom() {
		return from;
	}
	
	@Override
	public INode<K> getTo() {
		return to;
	}
}
