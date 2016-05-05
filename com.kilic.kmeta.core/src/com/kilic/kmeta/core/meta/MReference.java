package com.kilic.kmeta.core.meta;

public class MReference extends MStructuralFeature {
	boolean isContainment;
	MClass type;
	MReference opposite;
	
	public void setContainment(boolean isContainment) { this.isContainment = isContainment; }
	public void setType(MClass type) { this.type = type; }
	
	public void setOpposite(MReference opposite) { 
		this.opposite = opposite;
		
		if(opposite.getOpposite()!= this) { 
			opposite.setOpposite(this);
		}
	}
	
	public boolean isContainment() { return isContainment; }
	public MClass getType() { return type; }
	public MReference getOpposite() { return opposite; }
}
