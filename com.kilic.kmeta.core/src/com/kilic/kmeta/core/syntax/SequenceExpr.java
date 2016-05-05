package com.kilic.kmeta.core.syntax;

import java.util.ArrayList;
import java.util.List;

public class SequenceExpr implements ISyntaxExpr {
	List<ISyntaxExpr> seq = new ArrayList<>();
	
	public SequenceExpr( ISyntaxExpr... exprs ) {
		for(ISyntaxExpr e : exprs)
			seq.add(e);
	}
	
	public void appendSeq(ISyntaxExpr elem ) { seq.add(elem); }
	public ISyntaxExpr[] getSequence() { return (ISyntaxExpr[]) seq.toArray(); }
}
