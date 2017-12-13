package com.dlbase.pickutil;

import com.dlbase.base.DLBaseModel;

public class DLPickerItemModel extends DLBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3613250520498757750L;
	
	private String pickId;
	private String pickIndex;
	private String pickName;

	public String getPickId() {
		return pickId;
	}

	public void setPickId(String pickId) {
		this.pickId = pickId;
	}

	public String getPickIndex() {
		return pickIndex;
	}

	public void setPickIndex(String pickIndex) {
		this.pickIndex = pickIndex;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPickName() {
		return pickName;
	}

	public void setPickName(String pick_name) {
		this.pickName = pick_name;
	}

	@Override
	public String toString() {
		return "pickItem [id=" + pickId + ", pick_name=" + pickName +", index ="+ pickIndex +"]";
	}

}
