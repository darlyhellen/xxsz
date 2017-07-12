/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xiangxun.xacity.chart;

import org.xclcharts.common.DensityUtil;
import org.xclcharts.view.ChartView;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @package: com.xiangxun.xacity.chart
 * @ClassName: ObjectView.java
 * @Description: 各个例子ChartView的基类
 * @author: HanGJ
 * @date: 2016-3-14 下午2:22:48
 */
public class ObjectView extends ChartView {

	public ObjectView(Context context) {
		super(context);
	}

	public ObjectView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public ObjectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	// Demo中bar chart所使用的默认偏移值。
	// 偏移出来的空间用于显示tick,axistitle....
	protected int[] getBarLnDefaultSpadding() {
		int[] ltrb = new int[4];
		ltrb[0] = DensityUtil.dip2px(getContext(), 20); // left
		ltrb[1] = DensityUtil.dip2px(getContext(), 60); // top
		ltrb[2] = DensityUtil.dip2px(getContext(), 10); // right
		ltrb[3] = DensityUtil.dip2px(getContext(), 20); // bottom
		return ltrb;
	}

	protected int[] getPieDefaultSpadding() {
		int[] ltrb = new int[4];
		ltrb[0] = DensityUtil.dip2px(getContext(), 20); // left
		ltrb[1] = DensityUtil.dip2px(getContext(), 65); // top
		ltrb[2] = DensityUtil.dip2px(getContext(), 20); // right
		ltrb[3] = DensityUtil.dip2px(getContext(), 20); // bottom
		return ltrb;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

	}

}
