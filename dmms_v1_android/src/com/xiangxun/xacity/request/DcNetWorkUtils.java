package com.xiangxun.xacity.request;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.xiangxun.xacity.bean.InfoCache;
import com.xiangxun.xacity.bean.LoginData;
import com.xiangxun.xacity.bean.ResponseResultBeans;
import com.xiangxun.xacity.bean.ResponseResultBeans.ContractLedgerList;
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceMaintainBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceManageBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceScrapBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceScrapVerify;
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceSpareOilBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.GetContractDetail;
import com.xiangxun.xacity.bean.ResponseResultBeans.GetNoticeList;
import com.xiangxun.xacity.bean.ResponseResultBeans.GetWorkOrderDetail;
import com.xiangxun.xacity.bean.ResponseResultBeans.InspectionList;
import com.xiangxun.xacity.bean.ResponseResultBeans.LeaseApplyList;
import com.xiangxun.xacity.bean.ResponseResultBeans.LocationConfig;
import com.xiangxun.xacity.bean.ResponseResultBeans.MaterielDemandBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.NoticeBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupyArchiveBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupyAreaBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupyImageBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupyManageBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupySearchBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.ProjectInfo;
import com.xiangxun.xacity.bean.ResponseResultBeans.PurchaseOrderManageBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.RentLedgerList;
import com.xiangxun.xacity.bean.ResponseResultBeans.SupplierList;
import com.xiangxun.xacity.bean.ResponseResultBeans.WorkOrderBean;
import com.xiangxun.xacity.bean.ResultBeans;
import com.xiangxun.xacity.bean.ResultBeans.ContactGroupType;
import com.xiangxun.xacity.bean.ResultBeans.DeviceHome;
import com.xiangxun.xacity.bean.ResultBeans.DeviceVehicle;
import com.xiangxun.xacity.bean.ResultBeans.GPSData;
import com.xiangxun.xacity.bean.ResultBeans.GetPatrolBulderList;
import com.xiangxun.xacity.bean.ResultBeans.GetPatrolTypeList;
import com.xiangxun.xacity.bean.ResultBeans.MaterielHome;
import com.xiangxun.xacity.bean.ResultBeans.OccupyStatistics;
import com.xiangxun.xacity.bean.ResultBeans.OccupyTotal;
import com.xiangxun.xacity.bean.ResultBeans.OccupyType;
import com.xiangxun.xacity.bean.ResultBeans.OrderProgressData;
import com.xiangxun.xacity.bean.ResultBeans.OrgType;
import com.xiangxun.xacity.bean.ResultBeans.OrgUserType;
import com.xiangxun.xacity.bean.ResultBeans.PublishOrderData;
import com.xiangxun.xacity.bean.ResultBeans.TelBookType;
import com.xiangxun.xacity.bean.ResultBeans.Type;
import com.xiangxun.xacity.bean.ResultBeans.UpLoadResule;
import com.xiangxun.xacity.bean.UpdateResult;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.common.DcHttpClient;
import com.xiangxun.xacity.utils.DeEncryptUtil;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.utils.ShareDataUtils;
import com.xiangxun.xacity.utils.SharedPreferencesKeys;
import com.xiangxun.xacity.volley.Response;
import com.xiangxun.xacity.volley.Response.ErrorListener;
import com.xiangxun.xacity.volley.Response.Listener;
import com.xiangxun.xacity.volley.VolleyError;

/**
 * 
 * @package: com.huatek.api.request
 * @ClassName: DcNetWorkUtils
 * @author: aaron_han
 * @Description: 网络请求统一类
 * @date: 2015年01月14日 上午10:50:06
 */

public class DcNetWorkUtils {
	/**
	 * 登录
	 * 
	 * @param context
	 * @param usetName
	 * @param passWord
	 * @param handler
	 */
	public static void login(Context context, String usetName, String passWord, final Handler handler) {
		String url = ApiUrl.login();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", usetName);
		params.put("password", passWord);
		params.put("keyValue", makeValidate(usetName, passWord));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject object) {
				String json = object.toString();
				json = "{\"data\":" + json + "}";
				try {
					object = new JSONObject(json);
					if (null != object && !object.equals("网络异常")) {
						try {
							LoginData loginData = getGson().fromJson(object.toString(), ResponseResultBeans.LoginDataResult.class).data;
							handler.sendMessage(createMsg(ConstantStatus.loadSuccess, loginData));
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
							handler.sendMessage(createMsg(ConstantStatus.loadFailed, null));
						}
					} else {
						handler.sendMessage(createMsg(ConstantStatus.loadFailed, null));
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	public static void getNoticeList(Context context, String usetName, String passWord, int currentPage, int PageSize, final Handler handler) {
		String url = ApiUrl.getNoticeList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", usetName);
		params.put("password", passWord);
		params.put("keyValue", makeValidate(usetName, passWord));
		params.put("pageNo", currentPage + "");
		params.put("pageSize", PageSize + "");
		DcHttpClient.getInstance().postWithURL(context, url, params, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject object) {
				if (object.optString("resCode").equals("1000")) {
					try {
						GetNoticeList noticeList = getGson().fromJson(object.toString(), ResponseResultBeans.GetNoticeList.class);
						handler.sendMessage(createMsg(ConstantStatus.getNoticeBeanListSuccess, noticeList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getNoticeBeanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	public static void getNoticeDetail(Context context, String usetName, String passWord, String noteId, final Handler handler) {
		String url = ApiUrl.getNoticeDetail();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", usetName);
		params.put("password", passWord);
		params.put("keyValue", makeValidate(usetName, passWord));
		params.put("noticeId", noteId);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (object.optString("resCode").equals("1000")) {
					try {
						NoticeBean noticeBean = getGson().fromJson(object.toString(), ResponseResultBeans.GetNoticeDetail.class).result;
						handler.sendMessage(createMsg(ConstantStatus.getNoticeBeanListSuccess, noticeBean));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getNoticeBeanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getNoticeBeanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 合格供方名册接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDeviceQualifiedList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDeviceQualifiedList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("name", args[4]);
		params.put("linkman", args[5]);
		params.put("product", args[6]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						SupplierList supplierBeans = getGson().fromJson(object.toString(), ResponseResultBeans.SupplierList.class);
						handler.sendMessage(createMsg(ConstantStatus.getDeviceQualifiedListSuccess, supplierBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getDeviceQualifiedListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getDeviceQualifiedListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 采购租赁申请接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDeviceLeaseApplyList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDeviceLeaseApplyList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("code", args[4]);
		params.put("deviceName", args[5]);
		params.put("model", args[6]);
		params.put("startDate", args[7]);
		params.put("endDate", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						LeaseApplyList purchaseList = getGson().fromJson(object.toString(), ResponseResultBeans.LeaseApplyList.class);
						handler.sendMessage(createMsg(ConstantStatus.getDeviceLeaseApplyListSuccess, purchaseList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getDeviceLeaseApplyListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getDeviceLeaseApplyListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 设备验收接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDeviceAcceptanceList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDeviceAcceptanceList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("manufacturer", args[4]);
		params.put("deviceName", args[5]);
		params.put("model", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						InspectionList purchaseList = getGson().fromJson(object.toString(), ResponseResultBeans.InspectionList.class);
						handler.sendMessage(createMsg(ConstantStatus.getDeviceAcceptanceListSuccess, purchaseList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getDeviceAcceptanceListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getDeviceAcceptanceListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 外租设备台账接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDeviceRentLedgerList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDeviceRentLedgerList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("deviceSupplierName", args[4]);
		params.put("deviceName", args[5]);
		params.put("model", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						RentLedgerList rentLedgerList = getGson().fromJson(object.toString(), ResponseResultBeans.RentLedgerList.class);
						handler.sendMessage(createMsg(ConstantStatus.getDeviceRentLedgerListSuccess, rentLedgerList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getDeviceRentLedgerListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getDeviceRentLedgerListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 合同台账接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDeviceContractLedgerList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDeviceContractLedgerList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("name", args[4]);
		params.put("cooperationDepartment", args[5]);
		params.put("purchaseDeparmentId", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						ContractLedgerList contractLedgerList = getGson().fromJson(object.toString(), ResponseResultBeans.ContractLedgerList.class);
						handler.sendMessage(createMsg(ConstantStatus.getDeviceContractLedgerListSuccess, contractLedgerList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getDeviceContractLedgerListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getDeviceContractLedgerListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 合同台账詳情接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDeviceContractDetail(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDeviceContractDetail();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						GetContractDetail contractDetail = getGson().fromJson(object.toString(), ResponseResultBeans.GetContractDetail.class);
						handler.sendMessage(createMsg(ConstantStatus.getDeviceContractLedgerListSuccess, contractDetail));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getDeviceContractLedgerListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getDeviceContractLedgerListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 操作人员台账接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOperatorLedgerList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOperatorLedgerList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("name", args[4]);
		params.put("sex", args[5]);
		params.put("standardcultre", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceManageBean> operatorList = getGson().fromJson(object.toString(), ResponseResultBeans.GetDeviceManageList.class).operatorList;
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListSuccess, operatorList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 设备台账接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDeviceLedgerList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDeviceLedgerList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("type", args[4]);
		params.put("name", args[5]);
		params.put("usingCompany", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		params.put("smallType", args[9]);
		params.put("model", args[10]);
		params.put("brand", args[11]);
		params.put("plates", args[12]);
		params.put("fueltype", args[13]);
		params.put("surroundingSign", args[14]);
		params.put("driver", args[15]);
		params.put("manufacturers", args[16]);
		params.put("capotalSource", args[17]);
		params.put("curtain", args[18]);
		params.put("belongtoCompany", args[19]);
		params.put("operator", args[20]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceManageBean> operatorList = getGson().fromJson(object.toString(), ResponseResultBeans.GetDeviceLedgerList.class).mainshowList;
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListSuccess, operatorList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 安全保护装置接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getSafetyProtectionList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getSafetyProtectionList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("deviceCode", args[4]);
		params.put("deviceName", args[5]);
		params.put("deviceSafety", args[6]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceManageBean> operatorList = getGson().fromJson(object.toString(), ResponseResultBeans.GetSafetyProtectionList.class).safetyList;
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListSuccess, operatorList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 设备派遣接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDeviceDispatchList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDeviceDispatchList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("deviceName", args[4]);
		params.put("usingCompany", args[5]);
		params.put("deviceModel", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceManageBean> operatorList = getGson().fromJson(object.toString(), ResponseResultBeans.GetDeviceDispatchList.class).dispatchList;
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListSuccess, operatorList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 使用记录接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDeviceUseRecordList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDeviceUseRecordList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("mdeUsingCompanyName", args[4]);
		params.put("mdName", args[5]);
		params.put("mdModel", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceManageBean> operatorList = getGson().fromJson(object.toString(), ResponseResultBeans.GetDeviceUseRecordList.class).data;
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListSuccess, operatorList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 设备到场验证接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDeviceVerifyList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDeviceVerifyList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("deviceCode", args[4]);
		params.put("deviceName", args[5]);
		params.put("model", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceManageBean> operatorList = getGson().fromJson(object.toString(), ResponseResultBeans.GetDeviceVerifyList.class).checkingList;
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListSuccess, operatorList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 日常检查记录接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDailyInspectionRecordList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDailyInspectionRecordList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("code", args[4]);
		params.put("deviceName", args[5]);
		params.put("plate", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceManageBean> operatorList = getGson().fromJson(object.toString(), ResponseResultBeans.GetInspectionRecordList.class).everyinspectList;
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListSuccess, operatorList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 完好利用率接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getUtilizationRateList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getUtilizationRateList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("usingCompanyName", args[4]);
		params.put("deviceName", args[5]);
		params.put("deviceModel", args[6]);
		params.put("month", args[7]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceManageBean> operatorList = getGson().fromJson(object.toString(), ResponseResultBeans.GetUtilizationRateList.class).intactList;
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListSuccess, operatorList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 重点设备月报表接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMonthlyRreportList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getMonthlyRreportList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("deviceName", args[4]);
		params.put("company", args[5]);
		params.put("deviceModel", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceManageBean> operatorList = getGson().fromJson(object.toString(), ResponseResultBeans.GetMonthlyRreportList.class).emphasisList;
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListSuccess, operatorList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 保险管理接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getInsuranceManageList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getInsuranceManageList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("deviceName", args[4]);
		params.put("name", args[5]);
		params.put("deviceModel", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceManageBean> operatorList = getGson().fromJson(object.toString(), ResponseResultBeans.GetInsuranceManageList.class).insureList;
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListSuccess, operatorList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 设备维修计划接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaintenancePlanList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getMaintenancePlanList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("deviceName", args[4]);
		params.put("deviceCode", args[5]);
		params.put("plate", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceMaintainBean> deviceMaintainBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaintenancePlanList.class).maintainplanList;
						handler.sendMessage(createMsg(ConstantStatus.getDeviceMaintainListSuccess, deviceMaintainBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getDeviceMaintainListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getDeviceMaintainListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 维修工单接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaintenanceOrderList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getMaintenanceOrderList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("code", args[4]);
		params.put("repairFactory", args[5]);
		params.put("deviceName", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceMaintainBean> deviceMaintainBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaintenanceOrderList.class).overhaulList;
						handler.sendMessage(createMsg(ConstantStatus.getDeviceMaintainListSuccess, deviceMaintainBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getDeviceMaintainListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getDeviceMaintainListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 维修记录接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaintenanceRecordList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getMaintenanceRecordList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("deviceName", args[4]);
		params.put("deviceCode", args[5]);
		params.put("cost", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceMaintainBean> deviceMaintainBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaintenanceRecordList.class).maintainrecordList;
						handler.sendMessage(createMsg(ConstantStatus.getDeviceMaintainListSuccess, deviceMaintainBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getDeviceMaintainListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getDeviceMaintainListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 事故报告记录接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getAccidentReportRecordList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getAccidentReportRecordList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("code", args[4]);
		params.put("deviceName", args[5]);
		params.put("deviceCode", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceMaintainBean> deviceMaintainBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetAccidentReportRecordList.class).accidentList;
						handler.sendMessage(createMsg(ConstantStatus.getDeviceMaintainListSuccess, deviceMaintainBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getDeviceMaintainListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getDeviceMaintainListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 维修报废接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDeviceScrapApplyList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDeviceScrapApplyList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("code", args[4]);
		params.put("deviceName", args[5]);
		params.put("deviceCode", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceScrapBean> deviceScrapBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetDeviceScrapBeanList.class).scrapList;
						handler.sendMessage(createMsg(ConstantStatus.getDeviceScrapListSuccess, deviceScrapBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getDeviceScrapListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getDeviceScrapListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 入库台账记录接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getInLedgerRecordsList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getInLedgerRecordsList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("name", args[4]);
		params.put("oname", args[5]);
		params.put("model", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceSpareOilBean> deviceScrapBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetInLedgerRecordList.class).enterstockList;
						handler.sendMessage(createMsg(ConstantStatus.getDeviceSpareOilListSuccess, deviceScrapBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getDeviceSpareOilListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getDeviceSpareOilListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 出库台账记录接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOutLedgerRecordsList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOutLedgerRecordsList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("name", args[4]);
		params.put("oname", args[5]);
		params.put("code", args[6]);
		params.put("getPerson", args[7]);
		params.put("projectName", args[8]);
		params.put("startDateTime", args[9]);
		params.put("endDateTime", args[10]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceSpareOilBean> deviceScrapBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetOutLedgerRecordList.class).outstockList;
						handler.sendMessage(createMsg(ConstantStatus.getDeviceSpareOilListSuccess, deviceScrapBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getDeviceSpareOilListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getDeviceSpareOilListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 加油管理接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOilPetrolManageList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOilPetrolManageList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("name", args[4]);
		params.put("orgName", args[5]);
		params.put("plate", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceSpareOilBean> deviceScrapBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetPetrolManageList.class).refuelList;
						handler.sendMessage(createMsg(ConstantStatus.getDeviceSpareOilListSuccess, deviceScrapBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getDeviceSpareOilListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getDeviceSpareOilListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 领料申请接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaterialApplyList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getMaterialApplyList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("name", args[4]);
		params.put("company", args[5]);
		params.put("model", args[6]);
		params.put("startDateTime", args[7]);
		params.put("endDateTime", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceSpareOilBean> deviceScrapBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaterialApplyList.class).drawapplyList;
						handler.sendMessage(createMsg(ConstantStatus.getDeviceSpareOilListSuccess, deviceScrapBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getDeviceSpareOilListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getDeviceSpareOilListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 项目管理接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaterielDemandPMList(Context context, final Handler handler, int disabled, String... args) {
		String url = ApiUrl.getMaterielDemandPMList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("disabled", disabled + "");
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("menuid", args[4]);
		params.put("code", args[5]);
		params.put("name", args[6]);
		params.put("starttime", args[7]);
		params.put("place", args[8]);
		params.put("status", args[9]);
		params.put("planendtime", args[10]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<MaterielDemandBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaterialPMList.class).projectList;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 需求计划接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaterielDemandPlanList(Context context, final Handler handler, int disabled, String... args) {
		String url = ApiUrl.getMaterielDemandPlanList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("disabled", disabled + "");
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("menuid", args[4]);
		params.put("code", args[5]);
		params.put("projectName", args[6]);
		params.put("month", args[7]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<MaterielDemandBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaterialDemanPlanist.class).planList;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 项目计划信息接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaterielProjectInfoList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getMaterielProjectInfoList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("code", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<ProjectInfo> projectInfos = getGson().fromJson(object.toString(), ResponseResultBeans.GetProjectInfoist.class).projectResourceList;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, projectInfos));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 项目审核记录接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaterielProjectRecordList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getMaterielProjectRecordList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<ProjectInfo> projectInfos = getGson().fromJson(object.toString(), ResponseResultBeans.GetProjectRecodeist.class).verifyList;
						handler.sendMessage(createMsg(ConstantStatus.GetLocalSuccess, projectInfos));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 需求计划下的需求单信息接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaterielDemandFormListList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getMaterielDemandFormListList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("planCode", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<ProjectInfo> projectInfos = getGson().fromJson(object.toString(), ResponseResultBeans.GetReProjectInfoist.class).demandFormList;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, projectInfos));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 需求计划下的需求审核记录接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaterielDemandRecordListList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getMaterielDemandRecordListList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<ProjectInfo> projectInfos = getGson().fromJson(object.toString(), ResponseResultBeans.GetReProjectRecodeist.class).demandplanVerifyList;
						handler.sendMessage(createMsg(ConstantStatus.GetLocalSuccess, projectInfos));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 需求单管理接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaterielDemanPurchasesList(Context context, final Handler handler, int disabled, String... args) {
		String url = ApiUrl.getMaterielDemanPurchasesList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("disabled", disabled + "");
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("menuid", args[4]);
		params.put("planCode", args[5]);
		params.put("projectName", args[6]);
		params.put("month", args[7]);
		params.put("type", args[8]);
		params.put("supplier", "");
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaterialDemanOrderManageList.class).demandformList;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 采购单管理接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getPurchasesOrderManageList(Context context, final Handler handler, int disabled, String... args) {
		String url = ApiUrl.getPurchasesOrderManageList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("disabled", disabled + "");
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("menuid", args[4]);
		params.put("projectName", args[5]);
		params.put("month", args[6]);
		params.put("type", args[7]);
		params.put("supplier", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetPurchaseOrderManageList.class).purchaseList;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 外部供应商管理接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaterielSupplierManageList(Context context, final Handler handler, int disabled, String... args) {
		String url = ApiUrl.getMaterielSupplierManageList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("disabled", disabled + "");
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("menuid", args[4]);
		params.put("name", args[5]);
		params.put("commAddress", args[6]);
		params.put("principal", args[7]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaterialSupplierManageList.class).supplierBList;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 采购管理详情需求单信息接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDemanOrderDetailList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDemanOrderDetailList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("menuid", args[2]);
		params.put("id", args[3]);
		params.put("disabled", args[4]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaterialDemanOrderDetailList.class).demandViewList;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 采购单发货信息接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getPurchaseOrderDeliverList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getPurchaseOrderDeliverList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		params.put("disabled", args[3]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetPurchaseOrderDetailList.class).deliverFormList;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 采购单审核记录接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getPurchaseOrderVerifyList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getPurchaseOrderVerifyList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		params.put("disabled", args[3]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaterialSupplierDetailList.class).verifyList;
						handler.sendMessage(createMsg(ConstantStatus.GetLocalSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 项目管理接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaterielProjectManageList(Context context, final Handler handler, int disabled, String... args) {
		String url = ApiUrl.getMaterielProjectManageList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("disabled", disabled + "");
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("menuid", args[4]);
		params.put("code", args[5]);
		params.put("name", args[6]);
		params.put("starttime", args[7]);
		params.put("place", args[8]);
		params.put("status", args[9]);
		params.put("planendtime", args[10]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaterialProjectList.class).projectList;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 供应商监管接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaterielSupplierList(Context context, final Handler handler, int disabled, String... args) {
		String url = ApiUrl.getMaterielSupplierList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("disabled", disabled + "");
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("menuid", args[4]);
		params.put("name", args[5]);
		params.put("commAddress", args[6]);
		params.put("principal", args[7]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaterialSupplierPList.class).supplierList;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 供应商管理---采购单管理接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getSupplierPurchaseManageList(Context context, final Handler handler, int disabled, String... args) {
		String url = ApiUrl.getSupplierPurchaseManageList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("disabled", disabled + "");
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("menuid", args[4]);
		params.put("projectName", args[5]);
		params.put("type", args[6]);
		params.put("supplier", args[7]);
		params.put("month", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetPurchaseformList.class).purchaseformList;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 供应商管理---发货单管理接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getSupplierDeliverManageList(Context context, final Handler handler, int disabled, String... args) {
		String url = ApiUrl.getSupplierDeliverManageList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("disabled", disabled + "");
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("menuid", args[4]);
		params.put("projectName", args[5]);
		params.put("type", args[6]);
		params.put("supplier", args[7]);
		params.put("month", args[8]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetDeliverformList.class).deliverformList;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 供应商管理---库存管理接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getSupplierStoreManageList(Context context, final Handler handler, int disabled, String... args) {
		String url = ApiUrl.getSupplierStoreManageList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("disabled", disabled + "");
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("menuid", args[4]);
		params.put("name", args[5]);
		params.put("type", args[6]);
		params.put("supplier", args[7]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaterialStockList.class).stockList;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 供应商管理---入库管理接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getSupplierInStoreManageList(Context context, final Handler handler, int disabled, String... args) {
		String url = ApiUrl.getSupplierInStoreManageList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("disabled", disabled + "");
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("menuid", args[4]);
		params.put("name", args[5]);
		params.put("type", args[6]);
		params.put("supplier", args[7]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaterialStorageInList.class).storageputinList;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 供应商管理---出库管理接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getSupplierOutStoreManageList(Context context, final Handler handler, int disabled, String... args) {
		String url = ApiUrl.getSupplierOutStoreManageList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("disabled", disabled + "");
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("menuid", args[4]);
		params.put("name", args[5]);
		params.put("type", args[6]);
		params.put("supplier", args[7]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaterialStorageOutList.class).storageputoutList;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 供应商管理---发货单审核记录
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getSupplierDeliverVerifyList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getSupplierDeliverVerifyList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetDeliverformVerifyList.class).deliverformVerify;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 供应商管理---库存审核记录
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getSupplierStoreVerifyList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getSupplierStoreVerifyList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaterialStockVerifyList.class).stockVerify;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 供应商管理---入库审核记录
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getSupplierInStoreVerifyList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getSupplierInStoreVerifyList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaterialStorageInVerifyList.class).storageputinVerify;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 供应商管理---出库审核记录
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getSupplierOutStoreVerifyList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getSupplierOutStoreVerifyList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<PurchaseOrderManageBean> materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaterialStorageOutVerifyList.class).storageputoutVerify;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 物资管理---项目状态
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getProjectStatusList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getProjectStatusList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<Type> types = getGson().fromJson(object.toString(), ResultBeans.GetProjectTypeList.class).stuffStatusType;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialListSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 物资管理---物资类别
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaterielCategoryList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getMaterielCategoryList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<Type> types = getGson().fromJson(object.toString(), ResultBeans.GetMaterielTypeList.class).materialStuffType;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialListSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 物资管理---供应商
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getSupplierCategoryList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getSupplierCategoryList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<Type> types = getGson().fromJson(object.toString(), ResultBeans.GetSupplierTypeList.class).supplierList;
						handler.sendMessage(createMsg(ConstantStatus.getSupplierListSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getSupplierListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getSupplierListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 设备管理---设备分类
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDeviceCategoryList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDeviceCategoryList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<Type> types = getGson().fromJson(object.toString(), ResultBeans.GetDeviceCategoryList.class).municipaldtype;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialListSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 设备管理---设备归属
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDeviceBelongList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDeviceBelongList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<Type> types = getGson().fromJson(object.toString(), ResultBeans.GetDeviceBelongList.class).smalldtype;
						handler.sendMessage(createMsg(ConstantStatus.getSupplierListSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getSupplierListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getSupplierListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 道路挖占---延期项目查询
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyRoadSpendList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyRoadSpendList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("pname", args[4]);
		params.put("applicantId", args[5]);
		params.put("builderId", args[6]);
		params.put("starttime", args[7]);
		params.put("endtime", args[8]);
		params.put("roadId", args[9]);
		params.put("operator", args[10]);
		params.put("type", args[11]);
		params.put("occupyType", args[12]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<OccupySearchBean> occupySearchBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetOccupyDelaySearchList.class).delayList;
						handler.sendMessage(createMsg(ConstantStatus.getOccupySearchListSuccess, occupySearchBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupySearchListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupySearchListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 道路挖占---勘察信息查询
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyRoadInspectList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyRoadInspectList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("pname", args[4]);
		params.put("applicantId", args[5]);
		params.put("builderId", args[6]);
		params.put("starttime", args[7]);
		params.put("endtime", args[8]);
		params.put("roadId", args[9]);
		params.put("inspector", args[10]);
		params.put("type", args[11]);
		params.put("occupyType", args[12]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<OccupySearchBean> occupySearchBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetOccupyInspectSearchList.class).inspectList;
						handler.sendMessage(createMsg(ConstantStatus.getOccupySearchListSuccess, occupySearchBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupySearchListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupySearchListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 道路挖占---更改状态查询
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyStateSearchList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyStateSearchList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("pname", args[4]);
		params.put("roadname", args[5]);
		params.put("applicantId", args[6]);
		params.put("beforeStatus", args[7]);
		params.put("starttime", args[8]);
		params.put("endtime", args[9]);
		params.put("note", args[10]);
		params.put("operator", args[11]);
		params.put("type", args[12]);
		params.put("builderId", args[13]);
		params.put("afterStatus", args[14]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<OccupySearchBean> occupySearchBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetOccupyStateSearchList.class).logList;
						handler.sendMessage(createMsg(ConstantStatus.getOccupySearchListSuccess, occupySearchBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupySearchListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupySearchListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 道路挖占---申请单位档案
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyApplyLedgeList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyApplyLedgeList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("name", args[4]);
		params.put("unitAttribute", args[5]);
		params.put("levelCode", args[6]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<OccupyArchiveBean> occupySearchBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetOccupyApplyArchiveList.class).applicantList;
						handler.sendMessage(createMsg(ConstantStatus.getOccupySearchListSuccess, occupySearchBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupySearchListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupySearchListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 道路挖占---施工单位档案
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyConstructLedgeList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyConstructLedgeList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("name", args[4]);
		params.put("levelCode", args[5]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<OccupyArchiveBean> occupySearchBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetOccupyBuilderArchiveList.class).builderList;
						handler.sendMessage(createMsg(ConstantStatus.getOccupySearchListSuccess, occupySearchBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupySearchListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupySearchListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 道路挖占---道路挖掘管理(occupytype=1)/道路占用管理(occupytype=2)/占道街具管理(occupytype=3)
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyRoadPmList(Context context, final Handler handler, String occupytype, String... args) {
		String url = ApiUrl.getOccupyRoadPmList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("occupytype", occupytype);
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("projectName", args[4]);
		params.put("applicantId", args[5]);
		params.put("builderId", args[6]);
		params.put("starttime", args[7]);
		params.put("endtime", args[8]);
		params.put("roadname", args[9]);
		params.put("status", args[10]);
		params.put("principal", args[11]);
		params.put("managearea", args[12]);
		params.put("type", args[13]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<OccupyManageBean> manageBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetOccupyManageList.class).infoList;
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListSuccess, manageBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 道路挖占---查询项目详细信息
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupySearchDetailList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupySearchDetailList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						OccupyManageBean manageBean = getGson().fromJson(object.toString(), ResponseResultBeans.GetOccupyManageDetail.class).occupyInfo;
						handler.sendMessage(createMsg(ConstantStatus.getOccupyDetailSuccess, manageBean));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 道路挖占---查询项目统计信息
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyStatisticsDetailList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyStatisticsDetailList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<OccupyAreaBean> occupyList = getGson().fromJson(object.toString(), ResponseResultBeans.GetOccupyList.class).occupyList;
						handler.sendMessage(createMsg(ConstantStatus.getOccupyDetailStaticSuccess, occupyList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 道路挖占---查询项目图片
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyDetailImageList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyDetailImageList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<OccupyImageBean> manageBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetOccupyImageList.class).picView;
						handler.sendMessage(createMsg(ConstantStatus.getOccupyDetailImageSuccess, manageBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 道路挖占---挖占统计
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyStatisticsList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyStatisticsList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("queryType", args[2]);
		params.put("faceType", args[3]);
		params.put("startTime", args[4]);
		params.put("endTime", args[5]);
		params.put("managearea", args[6]);
		params.put("type", args[7]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<OccupyStatistics> occupyStatistics = getGson().fromJson(object.toString(), ResultBeans.OccupyStatisticsList.class).data;
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListSuccess, occupyStatistics));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 道路挖占---退让面积统计
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyConcessionList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyConcessionList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("start", args[2]);
		params.put("limit", args[3]);
		params.put("startTime", args[4]);
		params.put("endTime", args[5]);
		params.put("managearea", args[6]);
		params.put("roadname", args[7]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<OccupyManageBean> manageBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetOccupyGiveAreaList.class).giveproportionList;
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListSuccess, manageBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 挖占管理---建设单位
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyApplicantInfoList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyApplicantInfoList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<Type> types = getGson().fromJson(object.toString(), ResultBeans.GetOcuupyApplicantList.class).applicantinfo;
						handler.sendMessage(createMsg(ConstantStatus.getOccupyApplicantSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 挖占管理---施工单位
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyBuilderInfoList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyBuilderInfoList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<Type> types = getGson().fromJson(object.toString(), ResultBeans.GetOcuupyBuilderList.class).builderinfo;
						handler.sendMessage(createMsg(ConstantStatus.getOccupyBuilderSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 挖占管理---施工进度
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyProgressInfoList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyProgressInfoList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<Type> types = getGson().fromJson(object.toString(), ResultBeans.GetOcuupyProgressList.class).occupystatustype;
						handler.sendMessage(createMsg(ConstantStatus.getOccupyProgressSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 挖占管理---辖区
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyAreaInfoList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyAreaInfoList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<Type> types = getGson().fromJson(object.toString(), ResultBeans.GetOcuupyAreaList.class).suburbtype;
						handler.sendMessage(createMsg(ConstantStatus.getOccupyAreaSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 挖占管理---施工类别(occupytype=1)/占用类别(occupytype=2)/街具类别(occupytype=3)
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyTypeList(Context context, final Handler handler, String occupytype, String... args) {
		String url = ApiUrl.getOccupyTypeList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("occupytype", occupytype);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<Type> types = getGson().fromJson(object.toString(), ResultBeans.GetOcuupyTypeList.class).occupystyle;
						handler.sendMessage(createMsg(ConstantStatus.getOccupyTypeSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 挖占管理---路面种类
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyRoadTypeList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyRoadTypeList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<Type> types = getGson().fromJson(object.toString(), ResultBeans.GetOcuupyRoadTypeList.class).data;
						handler.sendMessage(createMsg(ConstantStatus.getOccupyRoadTypeSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 挖占管理---信用等级
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyLevelCodeList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyLevelCodeList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<Type> types = getGson().fromJson(object.toString(), ResultBeans.GetOcuupyLevelCodeList.class).levelcode;
						handler.sendMessage(createMsg(ConstantStatus.getOccupyTypeSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 版本更新
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getVersion(final boolean showFalse, final Context context, final Handler handler, String... args) {
		String url = ApiUrl.getVersion();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (object != null && object.optString("resCode").equals("1000")) {
					InfoCache.getInstance().setmData(((UpdateResult) getGson().fromJson(object.toString(), UpdateResult.class)).getDetailed());
					InfoCache.getInstance().setNewVer(InfoCache.getInstance().getAppVersionName(context).compareTo(InfoCache.getInstance().getmData().getVersionName()) < 0);
					if (InfoCache.getInstance().isNewVer()) {
						handler.sendMessage(createMsg(ConstantStatus.updateSuccess, InfoCache.getInstance().getmData()));
					} else {
						if (showFalse) {
							handler.sendMessage(createMsg(ConstantStatus.updateFalse, null));
						}
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getWorkOrderList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getWorkOrderList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("pageNo", args[2]);
		params.put("pageSize", args[3]);
		params.put("workOrderCode", args[4]);
		params.put("employeeName", args[5]);
		params.put("complaintName", args[6]);
		params.put("workEventType", args[7]);
		params.put("workEventStatus", args[8]);
		params.put("workIsBack", args[9]);
		params.put("dutyOrgType", args[10]);
		params.put("dutyOrgId", args[11]);
		params.put("dealLimit", args[12]);
		params.put("satisfactionDegree", args[13]);
		params.put("workEventSource", args[14]);
		params.put("workAssess", args[15]);
		params.put("workOrderText", args[16]);
		params.put("workOrderTimeBegin", args[17]);
		params.put("workOrderTimeEnd", args[18]);
		params.put("complaintTel", args[19]);
		params.put("closeBegin", args[20]);
		params.put("closeEnd", args[21]);
		params.put("workIsNodus", args[22]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (object.optString("resCode").equals("1000")) {
					try {
						List<WorkOrderBean> workOrderBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetWorkOrderBeanList.class).result;
						handler.sendMessage(createMsg(ConstantStatus.getPatrolSearchListSuccess, workOrderBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getPatrolSearchListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 工单详情
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getWorkOrderDetail(final Context context, final Handler handler, String... args) {
		String url = ApiUrl.getWorkOrderDetail(args[2]);
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("workOrderId", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (object.optString("resCode").equals("1000")) {
					try {
						GetWorkOrderDetail workOrderBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetWorkOrderDetail.class);
						handler.sendMessage(createMsg(ConstantStatus.getPatrolSearchListSuccess, workOrderBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getPatrolSearchListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 项目审核
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getProjectVerify(final Context context, final Handler handler, String type, String... args) {
		String url = ApiUrl.getProjectVerify();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("ids", args[2]);
		params.put("type", type);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				try {
					JSONObject obj = new JSONObject(object.optString("message"));
					if (obj.optString("res").contains("审核通过")) {
						handler.sendMessage(createMsg(ConstantStatus.VerifySuccess, null));
					} else {
						handler.sendMessage(createMsg(ConstantStatus.VerifyFailed, null));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 项目提交审核
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getProjectSubmitVerify(final Context context, final Handler handler, String type, String... args) {
		String url = ApiUrl.getProjectSubmitVerify();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("ids", args[2]);
		params.put("type", type);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				try {
					JSONObject obj = new JSONObject(object.optString("message"));
					if (obj.optString("res").contains("success")) {
						handler.sendMessage(createMsg(ConstantStatus.SubmitVerifySuccess, null));
					} else {
						handler.sendMessage(createMsg(ConstantStatus.SubmitVerifyFailed, null));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 项目驳回
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getProjectVerifyRebut(final Context context, final Handler handler, String type, String... args) {
		String url = ApiUrl.getProjectVerifyRebut();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("ids", args[2]);
		params.put("reason", args[3]);
		params.put("type", type);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				try {
					JSONObject obj = new JSONObject(object.optString("message"));
					if (obj.optString("res").contains("审核驳回成功")) {
						handler.sendMessage(createMsg(ConstantStatus.VerifyResultSuccess, null));
					} else {
						handler.sendMessage(createMsg(ConstantStatus.VerifyFailed, null));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 施工进度统计
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getProjectOccupyStatus(final Context context, final Handler handler, String... args) {
		String url = ApiUrl.getProjectOccupyStatus();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<OccupyType> applyStatusList = getGson().fromJson(object.toString(), ResultBeans.GetOcuupyType.class).applyStatusList;
						handler.sendMessage(createMsg(ConstantStatus.OccupyStatusSuccess, applyStatusList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.OccupyStatusFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 施工类别统计
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getProjectOccupyInfos(final Context context, final Handler handler, String... args) {
		String url = ApiUrl.getProjectOccupyInfos();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<OccupyType> applyTypeList = getGson().fromJson(object.toString(), ResultBeans.GetOcuupyType.class).applyTypeList;
						handler.sendMessage(createMsg(ConstantStatus.OccupyTypeSuccess, applyTypeList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.OccupyTypeFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 挖占管理---标记
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyMark(final Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyMark();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("ids", args[2]);
		params.put("content", args[3]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				try {
					JSONObject obj = new JSONObject(object.optString("message"));
					if (obj.optString("res").contains("项目标记完成")) {
						handler.sendMessage(createMsg(ConstantStatus.VerifySuccess, null));
					} else {
						handler.sendMessage(createMsg(ConstantStatus.VerifyFailed, null));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 挖占管理---状态更改
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyChange(final Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyChange();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("ids", args[2]);
		params.put("status", args[3]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				try {
					JSONObject obj = new JSONObject(object.optString("message"));
					if (obj.optString("res").contains("更改状态完成")) {
						handler.sendMessage(createMsg(ConstantStatus.VerifyResultSuccess, null));
					} else {
						handler.sendMessage(createMsg(ConstantStatus.VerifyResultFailed, null));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 挖占管理---更改信用等级
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyLevelChange(final Context context, final Handler handler, String type, String... args) {
		String url = ApiUrl.getOccupyLevelChange();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		params.put("level", args[3]);
		params.put("type", type);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				try {
					JSONObject obj = new JSONObject(object.optString("message"));
					if (obj.optString("res").contains("更改状态完成")) {
						handler.sendMessage(createMsg(ConstantStatus.VerifyResultSuccess, null));
					} else {
						handler.sendMessage(createMsg(ConstantStatus.VerifyResultFailed, null));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 挖占管理---图片类型
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyPictureTypeList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyPictureTypeList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<Type> types = getGson().fromJson(object.toString(), ResultBeans.GetOcuupyLevelCodeList.class).picturetypeList;
						handler.sendMessage(createMsg(ConstantStatus.getOccupyTypeSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOccupyListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 挖占管理---勘察说明
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupySurVeyDescript(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupySurVeyDescript();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<Type> types = getGson().fromJson(object.toString(), ResultBeans.GetOcuupyLevelCodeList.class).illustrationtemplate;
						handler.sendMessage(createMsg(ConstantStatus.SurveySuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.SurveyFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.SurveyFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 挖占管理---勘察上报
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupySurVeyUpload(Context context, final Handler handler, List<UpLoadResule> files, String... args) {
		String url = ApiUrl.getOccupySurVeyUpload();
		Map<String, String> params = new HashMap<String, String>();
		Map<String, List<UpLoadResule>> fileInfo = new HashMap<String, List<UpLoadResule>>();
		fileInfo.put("files", files);
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		params.put("inspectTimeStr", args[3]);
		params.put("illustration", args[4]);
		params.put("remark", args[5]);
		params.put("picType", args[6]);
		params.put("files", getGson().toJson(fileInfo));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				try {
					JSONObject obj = new JSONObject(object.optString("message"));
					if (obj.optString("res").contains("success")) {
						handler.sendMessage(createMsg(ConstantStatus.VerifyResultSuccess, null));
					} else {
						handler.sendMessage(createMsg(ConstantStatus.VerifyResultFailed, null));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 挖占管理---延期原因
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyDelayDescript(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getOccupyDelayDescript();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<Type> types = getGson().fromJson(object.toString(), ResultBeans.GetOcuupyLevelCodeList.class).delayreasontemplate;
						handler.sendMessage(createMsg(ConstantStatus.SurveySuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.SurveyFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.SurveyFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 挖占管理---延期上报
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getOccupyDelayUpload(Context context, final Handler handler, List<UpLoadResule> files, String... args) {
		String url = ApiUrl.getOccupyDelayUpload();
		Map<String, String> params = new HashMap<String, String>();
		Map<String, List<UpLoadResule>> fileInfo = new HashMap<String, List<UpLoadResule>>();
		fileInfo.put("files", files);
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		params.put("begintime", args[3]);
		params.put("deadtime", args[4]);
		params.put("reason", args[5]);
		params.put("note", args[6]);
		params.put("files", getGson().toJson(fileInfo));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				try {
					JSONObject obj = new JSONObject(object.optString("message"));
					if (obj.optString("res").contains("success")) {
						handler.sendMessage(createMsg(ConstantStatus.VerifyResultSuccess, null));
					} else {
						handler.sendMessage(createMsg(ConstantStatus.VerifyResultFailed, null));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 挖占管理---挖占百分比
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getProjectResultMap(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getProjectResultMap();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						OccupyTotal types = getGson().fromJson(object.toString(), ResultBeans.OccupyTotal.class);
						handler.sendMessage(createMsg(ConstantStatus.OccupyPercentageSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.OccupyPercentageFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.OccupyPercentageFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 巡视系统---获取字典项
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getPatrolBaseData(Context context, final Handler handler, final int type, final String... args) {
		String url = ApiUrl.getPatrolBaseData();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("dicTypes", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && object.optString("resCode").equals("1000")) {
					try {
						if (type == 0) {
							GetPatrolTypeList patrolTypeList = getGson().fromJson(object.toString(), ResultBeans.GetPatrolTypeResult.class).result;
							handler.sendMessage(createMsg(ConstantStatus.OccupyTypeSuccess, patrolTypeList));
						} else {
							JSONObject json = (JSONObject) object.get("result");
							JSONArray temp = (JSONArray) json.get(args[2]);
							String temp1 = temp.toString();
							temp1 = "{\"type\":" + temp1 + "}";
							JSONObject jsonObject = new JSONObject(temp1);
							GetPatrolBulderList patrolTypeList = getGson().fromJson(jsonObject.toString(), ResultBeans.GetPatrolBulderList.class);
							handler.sendMessage(createMsg(ConstantStatus.OccupyStatusSuccess, patrolTypeList));
						}
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.OccupyTypeFailed, null));
					} catch (JSONException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.OccupyTypeFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 巡视系统---机构用户数据
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getPatrolUserData(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getPatrolUserData();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (object != null && object.optString("resCode").equals("1000")) {
					try {
						OccupyTotal types = getGson().fromJson(object.toString(), ResultBeans.OccupyTotal.class);
						handler.sendMessage(createMsg(ConstantStatus.OccupyPercentageSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.OccupyPercentageFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.OccupyPercentageFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 巡视系统---机构数据
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getPatrolOrgData(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getPatrolOrgData();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && object.optString("resCode").equals("1000")) {
					try {
						OrgType types = getGson().fromJson(object.toString(), ResultBeans.OrgList.class).result;
						handler.sendMessage(createMsg(ConstantStatus.OrgSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.OrgFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.OrgFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 巡视系统---机构用户数据
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getPatrolOrgUserData(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getPatrolOrgUserData();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("orgId", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (object != null && object.optString("resCode").equals("1000")) {
					try {
						List<OrgUserType> types = getGson().fromJson(object.toString(), ResultBeans.OrgUserList.class).result;
						handler.sendMessage(createMsg(ConstantStatus.OrgUserSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.OrgFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.OrgFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 巡视系统---机构查找用户
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getPatrolSearchOrgUser(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getPatrolSearchOrgUser();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("userName", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (object != null && object.optString("resCode").equals("1000")) {
					try {
						List<OrgUserType> types = getGson().fromJson(object.toString(), ResultBeans.OrgUserList.class).result;
						handler.sendMessage(createMsg(ConstantStatus.OrgSearchSuccess, types));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.OrgSearchFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.OrgSearchFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 巡视系统---获取通讯群组
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getPatrolContactGroup(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getPatrolContactGroup();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (object != null && object.optString("resCode").equals("1000")) {
					try {
						List<ContactGroupType> contactGroupTypes = getGson().fromJson(object.toString(), ResultBeans.ContactGroupList.class).result;
						handler.sendMessage(createMsg(ConstantStatus.ContactGroupSuccess, contactGroupTypes));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.ContactFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.ContactFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 巡视系统---获取群组通讯录
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getPatrolContactGroupBook(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getPatrolContactGroupBook();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("contactGroupId", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (object != null && object.optString("resCode").equals("1000")) {
					try {
						List<TelBookType> telBookTypes = getGson().fromJson(object.toString(), ResultBeans.TelBookList.class).result;
						handler.sendMessage(createMsg(ConstantStatus.ContactGroupBookSuccess, telBookTypes));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.ContactFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.ContactFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 巡视系统---获取群组通讯录
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getPatrolContactSearchGroupPerson(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getPatrolContactSearchGroupPerson();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("contactGroupId", args[2]);
		params.put("contactName", args[3]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (object != null && object.optString("resCode").equals("1000")) {
					try {
						List<TelBookType> telBookTypes = getGson().fromJson(object.toString(), ResultBeans.TelBookList.class).result;
						handler.sendMessage(createMsg(ConstantStatus.ContactGroupBookSuccess, telBookTypes));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.ContactFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.ContactFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 巡视系统---获取群组通讯录
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getPatrolContactSearchPerson(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getPatrolContactSearchPerson();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("contactName", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (object != null && object.optString("resCode").equals("1000")) {
					try {
						List<TelBookType> telBookTypes = getGson().fromJson(object.toString(), ResultBeans.TelBookList.class).result;
						handler.sendMessage(createMsg(ConstantStatus.ContactSearchSuccess, telBookTypes));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.ContactSearchFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.ContactSearchFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 工单保存
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void publishOrderUrl(final Context context, final Handler handler, PublishOrderData orderData, String... args) {
		String url = ApiUrl.publishOrderUrl(args);
		Map<String, String> params = new HashMap<String, String>();
		params.put("jsondata", getGson().toJson(orderData));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (object != null && object.optString("resCode").equals("1000")) {
					try {
						GetWorkOrderDetail workOrderBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetWorkOrderDetail.class);
						handler.sendMessage(createMsg(ConstantStatus.PublishOrderSuccess, workOrderBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.PublishOrderFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.PublishOrderFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 工单进展
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void publishOrderProgressUrl(final Context context, final Handler handler, OrderProgressData progressData, String... args) {
		String url = ApiUrl.publishOrderProgressUrl(args);
		Map<String, String> params = new HashMap<String, String>();
		params.put("jsondata", getGson().toJson(progressData));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (object != null && object.optString("resCode").equals("1000")) {
					try {
						GetWorkOrderDetail workOrderBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetWorkOrderDetail.class);
						handler.sendMessage(createMsg(ConstantStatus.PublishOrderSuccess, workOrderBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.PublishOrderFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.PublishOrderFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 上报定位数据
	 * 
	 * @param context
	 * @param data
	 * @param handler
	 */
	public static void locationPosition(Context context, GPSData data) {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("jsondata", getGson().toJson(data));
			String url = ApiUrl.locationPosition(context);
			DcHttpClient.getInstance().postWithURL(context, url, params, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject object) {
					if (null != object && object.optString("resCode").equals("1000")) {
						System.out.println("数据提交成功");
						Logger.e("LocationService-locationPosition ---" + " 定位数据提交成功");
					} else if (null != object && object.optString("resCode").equals("1001")) {
						Logger.e("LocationService-locationPosition ---" + " 未开启定位设置");
					} else {
						System.out.println("数据提交失败");
						Logger.e("LocationService-locationPosition ---" + " 定位数据提交失败");
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError arg0) {
					System.out.println("出错了: " + arg0.getMessage());
					Logger.e("LocationService-locationPosition ---" + "出错了: " + arg0.getMessage());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("定位数据上报异常");
			Logger.e("LocationService-locationPosition --- " + "定位数据上报异常");
		}
	}

	/**
	 * 获取定位设置
	 * 
	 * @param context
	 * @param args
	 * @param handler
	 */
	public static void locationConfig(Context context, final Handler handler, String... args) {
		String url = ApiUrl.locationConfig();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("userId", ShareDataUtils.getSharedStringData(context, SharedPreferencesKeys.USERID));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject object) {
				if (object != null && object.optString("resCode").equals("1000")) {
					try {
						LocationConfig locationConfig = getGson().fromJson(object.toString(), ResponseResultBeans.LocationConfigResult.class).result;
						handler.sendMessage(createMsg(ConstantStatus.LocationConfigSuccess, locationConfig));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.LocationConfigFailed, null));
					}
				} else if (object != null && object.optString("resCode").equals("1002")) {
					handler.sendMessage(createMsg(ConstantStatus.LocationConfigNotSet, null));
				} else {
					handler.sendMessage(createMsg(ConstantStatus.LocationConfigFailed, null));
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				handler.sendMessage(createMsg(ConstantStatus.LocationConfigFailed, null));
			}
		});
	}

	/**
	 * 人员实时定位
	 * 
	 * @param context
	 * @param args
	 * @param handler
	 */
	public static void getUserLocation(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getUserLocation();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("userId", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject object) {
				if (object != null && object.optString("resCode").equals("1000")) {
					try {
						GPSData data = getGson().fromJson(object.toString(), ResultBeans.getGPSData.class).result;
						handler.sendMessage(createMsg(ConstantStatus.LocationConfigSuccess, data));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.LocationConfigFailed, null));
					}
				} else if (object != null && object.optString("resCode").equals("1001")) {
					handler.sendMessage(createMsg(ConstantStatus.LocationConfigSuccess, null));
				} else {
					handler.sendMessage(createMsg(ConstantStatus.LocationConfigFailed, null));
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 人员历史轨迹
	 * 
	 * @param context
	 * @param args
	 * @param handler
	 */
	public static void getUserLocationList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getUserLocationList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("userId", args[2]);
		params.put("beginTime", args[3]);
		params.put("endTime", args[4]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject object) {
				if (object != null && object.optString("resCode").equals("1000")) {
					try {
						List<GPSData> datas = getGson().fromJson(object.toString(), ResultBeans.getGPSDataList.class).result;
						handler.sendMessage(createMsg(ConstantStatus.LocationListSuccess, datas));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.LocationListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.LocationListFailed, null));
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 物资首页数据统计
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaterielHome(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getMaterielHome();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("menuid", args[2]);
		params.put("disabled", "0");
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						MaterielHome materielHome = getGson().fromJson(object.toString(), ResultBeans.MaterielHome.class);
						handler.sendMessage(createMsg(ConstantStatus.getMaterialListSuccess, materielHome));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 设备首页数据统计
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDevicelHome(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDevicelHome();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						DeviceHome deviceHome = getGson().fromJson(object.toString(), ResultBeans.DeviceHome.class);
						handler.sendMessage(createMsg(ConstantStatus.DeviceHomeSuccess, deviceHome));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.DeviceHomeFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.DeviceHomeFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 车辆信息接口
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDeviceVehicleList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDevicelVehicleList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("vehNoCode", args[2]);
		params.put("devCode", args[3]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceVehicle> deviceVehicles = getGson().fromJson(object.toString(), ResultBeans.DeviceVehicleList.class).data;
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListSuccess, deviceVehicles));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getOperatorLedgerListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 需求计划详情
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getMaterielDetail(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getMaterielDetail();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						PurchaseOrderManageBean materielDemandBeans = getGson().fromJson(object.toString(), ResponseResultBeans.GetMaterialDemanOrderDetail.class).demandPlanInfo;
						handler.sendMessage(createMsg(ConstantStatus.getMaterialListSuccess, materielDemandBeans));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.getMaterialDemanListFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 报废审核记录列表
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDeviceScrapVerifyList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDeviceScrapVerifyList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceScrapVerify> verifyList = getGson().fromJson(object.toString(), ResponseResultBeans.DeviceScrapVerifyList.class).verifyList;
						handler.sendMessage(createMsg(ConstantStatus.DeviceHomeSuccess, verifyList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.DeviceHomeFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.DeviceHomeFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 采购租赁审核记录列表
	 * 
	 * @param context
	 * @param handler
	 * @param args
	 */
	public static void getDevicePurchaseVerifyList(Context context, final Handler handler, String... args) {
		String url = ApiUrl.getDevicePurchaseVerifyList();
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", args[0]);
		params.put("password", args[1]);
		params.put("keyValue", makeValidate(args[0], args[1]));
		params.put("id", args[2]);
		DcHttpClient.getInstance().postWithURL(context, url, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject object) {
				if (null != object && !object.equals("网络异常")) {
					try {
						List<DeviceScrapVerify> verifyList = getGson().fromJson(object.toString(), ResponseResultBeans.DeviceScrapVerifyList.class).verifyList;
						handler.sendMessage(createMsg(ConstantStatus.DeviceHomeSuccess, verifyList));
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						handler.sendMessage(createMsg(ConstantStatus.DeviceHomeFailed, null));
					}
				} else {
					handler.sendMessage(createMsg(ConstantStatus.DeviceHomeFailed, null));
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendMessage(createMsg(ConstantStatus.NetWorkError, null));
			}
		});
	}

	/**
	 * 私钥
	 */
	private static final String PRIVATE_KEY = "xiangxun";

	/**
	 * 生成keyValue验证字符
	 * 
	 * @param user
	 * @param pwd
	 * @param key
	 *            私钥
	 */
	@SuppressLint("SimpleDateFormat")
	public static String makeValidate(String user, String pwd) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		StringBuilder str = new StringBuilder();
		str.append(user);
		str.append(pwd);
		str.append(PRIVATE_KEY);
		str.append(sdf.format(new Date()));

		try {
			DeEncryptUtil deu = new DeEncryptUtil();
			return deu.encrypt(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static Message createMsg(int what, Object obj) {
		Message msg = new Message();
		msg.what = what;
		if (obj != null)
			msg.obj = obj;
		return msg;
	}

	private static Gson gson;

	private static Gson getGson() {
		if (gson == null) {
			gson = new Gson();
		}
		return gson;
	}
}