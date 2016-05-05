package com.kilic.kmeta.core.syntax;

import java.util.ArrayList;
import java.util.List;

public class CharRangeGroupExpr {
	List<CharRangeExpr> ranges = new ArrayList<>();
	
	public void addRange( CharRangeExpr range) { this.ranges.add(range); }
	public CharRangeExpr[] getRanges() { return (CharRangeExpr[]) ranges.toArray(); }
}
