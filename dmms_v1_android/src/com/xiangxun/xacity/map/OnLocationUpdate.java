package com.xiangxun.xacity.map;

import com.baidu.location.BDLocation;

/**
 * @package: com.xiangxun.xacity.map
 * @ClassName: OnLocationUpdate.java
 * @Description: 定位刷新定位返回接口
 * @author: HanGJ
 * @date: 2016-2-18 下午3:04:38
 */
public interface OnLocationUpdate {
	public void update(BDLocation location);
}
