package com.kilic.kmeta.core.meta;

public class MReference extends MFeature {
	boolean isContainment;
	MClass targetClass;
	MReference opposite;

	public void setContainment(boolean isContainment) {
		this.isContainment = isContainment;
	}

	public void setTargetClass(MClass type) {
		this.targetClass = type;
	}

	public void setOpposite(MReference opposite) {
		this.opposite = opposite;

		if (opposite.getOpposite() != this) {
			opposite.setOpposite(this);
		}
	}

	public boolean isContainment() {
		return isContainment;
	}

	public MClass getTargetClass() {
		return targetClass;
	}

	public MReference getOpposite() {
		return opposite;
	}
}
