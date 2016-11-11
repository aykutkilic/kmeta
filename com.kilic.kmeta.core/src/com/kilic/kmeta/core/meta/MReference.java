package com.kilic.kmeta.core.meta;

public class MReference extends MFeature {
	private boolean isContainment;
	private MClass targetClass;
	private MReference opposite;

	public void setContainment(final boolean isContainment) {
		this.isContainment = isContainment;
	}

	public void setTargetClass(final MClass type) {
		this.targetClass = type;
	}

	public void setOpposite(final MReference opposite) {
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
