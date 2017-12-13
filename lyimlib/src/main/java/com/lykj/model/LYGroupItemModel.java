package com.lykj.model;

import java.util.ArrayList;

import com.dlbase.base.DLBaseModel;

public class LYGroupItemModel extends DLBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3886452812750416381L;

	private String groupId;
	private String groupName;
	private String groupAvatar;

	public LYGroupItemModel(){
		
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupAvatar() {
		return groupAvatar;
	}

	public void setGroupAvatar(String groupAvatar) {
		this.groupAvatar = groupAvatar;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
