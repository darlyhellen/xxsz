package com.xiangxun.xacity.bean;

public class UpdateResult {
	String resCode;
	String resDesc;
	UpdateData result;

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResDesc() {
		return resDesc;
	}

	public void setResDesc(String resDesc) {
		this.resDesc = resDesc;
	}

	public UpdateData getDetailed() {
		return result;
	}

	public void setDetailed(UpdateData result) {
		this.result = result;
	}

}
