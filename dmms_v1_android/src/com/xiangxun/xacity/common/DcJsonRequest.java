package com.xiangxun.xacity.common;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.xiangxun.xacity.utils.HttpHeaderIgnoreParser;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.volley.AuthFailureError;
import com.xiangxun.xacity.volley.DefaultRetryPolicy;
import com.xiangxun.xacity.volley.NetworkResponse;
import com.xiangxun.xacity.volley.ParseError;
import com.xiangxun.xacity.volley.Request;
import com.xiangxun.xacity.volley.Response;
import com.xiangxun.xacity.volley.Response.ErrorListener;
import com.xiangxun.xacity.volley.VolleyError;
import com.xiangxun.xacity.volley.toolbox.HttpHeaderParser;

/**
 * @package: com.huatek.api.common
 * @ClassName: DcJsonRequest
 * @Description: request请求类
 * @author: aaron_han
 * @data: 2015-1-15 下午4:55:46
 */
public class DcJsonRequest extends Request<JSONObject> {
	private final Response.Listener<JSONObject> mListener;
	private final Map<String, String> mParams;
	private ErrorListener mErrorListener;
	private String mUrl;

	public DcJsonRequest(Context context, int method, String url, Map<String, String> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
		super(method, url, errorListener);
		Logger.e(url);
		mUrl = url;
		mErrorListener = errorListener;
		mListener = listener;
		mParams = params;
		setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));// 设置重试超时10秒
		setTag(context);// 设置url为标记

	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mParams;
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			jsonString = jsonString.replace("JSON_CALLBACK", "");
			jsonString = jsonString.substring(1, jsonString.length() - 1);
			return Response.success(new JSONObject(jsonString), shouldCache() ? HttpHeaderIgnoreParser.parseIgnoreCacheHeaders(response) : HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			Logger.e("UnsupportedEncodingException异常 ---"+ e.getMessage());
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			Logger.e("数据解析异常 ---"+je.getMessage());
			return Response.error(new ParseError(je));
		}
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		if (shouldCache()) {
			if (response.optInt("result") == 0) {
				mListener.onResponse(response);
			}
		} else {
			mListener.onResponse(response);
		}
	}

	@Override
	public void deliverError(VolleyError error) {
		Logger.e(error.getMessage() + "\n" + "**url**" + mUrl);
		if (mErrorListener != null) {
			mErrorListener.onErrorResponse(error);
		}
	}

}
