package com.kilic.kmeta.core.alls.predictiondfa;

import com.kilic.kmeta.core.alls.tn.IEdge;
import com.kilic.kmeta.core.alls.tn.INode;

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
