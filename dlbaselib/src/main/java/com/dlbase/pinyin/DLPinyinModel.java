package com.dlbase.pinyin;

import com.dlbase.base.DLBaseModel;

public class DLPinyinModel extends DLBaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5744359783826894041L;

	private String name;
	private String firstP;
	
	public DLPinyinModel(){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstP() {
		return firstP;
	}

	public void setFirstP(String firstP) {
		this.firstP = firstP;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
