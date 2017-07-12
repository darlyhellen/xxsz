/*
 * Created by Storm Zhang, Feb 11, 2014.
 */

package com.xiangxun.xacity.common;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.xiangxun.xacity.utils.HttpHeaderIgnoreParser;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.volley.AuthFailureError;
import com.xiangxun.xacity.volley.NetworkResponse;
import com.xiangxun.xacity.volley.ParseError;
import com.xiangxun.xacity.volley.Request;
import com.xiangxun.xacity.volley.Response;
import com.xiangxun.xacity.volley.Response.ErrorListener;
import com.xiangxun.xacity.volley.Response.Listener;
import com.xiangxun.xacity.volley.toolbox.HttpHeaderParser;

public class GsonRequest<T> extends Request<T> {
	private final Gson mGson = new Gson();
	private final Class<T> mClazz;
	private final Listener<T> mListener;
	private final Map<String, String> mHeaders;
	private boolean isNeedCache;

	public GsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
		this(Method.GET, url, clazz, null, listener, errorListener);
		Logger.e(url);
	}

	public GsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener, boolean isCache) {
		this(Method.GET, url, clazz, null, listener, errorListener);
		Logger.e(url);
		this.isNeedCache = isCache;
	}

	public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, Listener<T> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		Logger.e(url);
		this.mClazz = clazz;
		this.mHeaders = headers;
		this.mListener = listener;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeaders != null ? mHeaders : super.getHeaders();
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(mGson.fromJson(json, mClazz),
					isNeedCache ? HttpHeaderIgnoreParser.parseIgnoreCacheHeaders(response) : HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
}
