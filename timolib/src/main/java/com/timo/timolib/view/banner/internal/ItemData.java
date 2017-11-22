package com.timo.timolib.view.banner.internal;

public class ItemData {
	 /** 图片地址 */
	private String imgUrl;
    /** 链接操作 */
	private String imgForwardUrl;

	private String imgId;
	private String imgTypeId;
	private String createTime;

	public String getImgForwardUrl() {
		return imgForwardUrl;
	}

	public void setImgForwardUrl(String imgForwardUrl) {
		this.imgForwardUrl = imgForwardUrl;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public String getImgTypeId() {
		return imgTypeId;
	}

	public void setImgTypeId(String imgTypeId) {
		this.imgTypeId = imgTypeId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateByUser() {
		return createByUser;
	}

	public void setCreateByUser(String createByUser) {
		this.createByUser = createByUser;
	}

	private String updateTime;
	private String createByUser;
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getLink() {
		return imgForwardUrl;
	}
	public void setLink(String link) {
		this.imgForwardUrl = link;
	}

}
