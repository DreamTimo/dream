package com.lykj.model;

import com.dlbase.base.DLBaseModel;

/**
 * @author luyz
 * 项目信息
 */
public class LYProjectModel extends DLBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7354941694583366630L;

	private String projectId;
	
	private String projectName;

	private String projectLogo;

	public LYProjectModel(){

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public String getProjectLogo() {
		return projectLogo;
	}

	public void setProjectLogo(String projectLogo) {
		this.projectLogo = projectLogo;
	}

}
