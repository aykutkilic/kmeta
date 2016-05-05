package com.kilic.kmeta.core.syntax;

import java.util.ArrayList;
import java.util.List;

public class AlternativeExpr implements ISyntaxExpr {
	List<ISyntaxExpr> alternatives = new ArrayList<>();
	
	public void addAlternative(ISyntaxExpr alternative) { alternatives.add(alternative); }
	public ISyntaxExpr[] getAlternatives() { return (ISyntaxExpr[]) alternatives.toArray(); }
}
