package com.xiangxun.xacity.bean;

import java.util.List;

/**
 * @package: com.xiangxun.xacity.bean
 * @ClassName: DevicePurchaseListSuperItem.java
 * @Description: 设备采购实体类
 * @author: HanGJ
 * @date: 2016-2-2 上午8:32:53
 */
public class DeviceListSuperItem {
	private String purchasename = null;
	private List<DeviceListChildItem> purchases = null;

	public DeviceListSuperItem(String purchasename, List<DeviceListChildItem> purchases) {
		super();
		this.purchasename = purchasename;
		this.purchases = purchases;
	}

	public String getPurchasename() {
		return purchasename;
	}

	public void setPurchasename(String purchasename) {
		this.purchasename = purchasename;
	}

	public List<DeviceListChildItem> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<DeviceListChildItem> purchases) {
		this.purchases = purchases;
	}

}
