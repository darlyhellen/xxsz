package com.xiangxun.xacity.bean;

import java.io.Serializable;
import java.util.List;

public class ResultBeans {

	public static class Type implements Serializable {
		private static final long serialVersionUID = 3884612945322844153L;
		public String code;
		public String name;
		public String type;
		public String id;
		public String product;
		public String dept;
		public String levelCode;
		public String productName;
		public String status;
		public String disabled;
		public String operator;
		public String statusHtml;
	}

	public static class Types {
		public String name;
		public String type;
		public String[] status;
		public String[] num;
		public int sum;
		public int[] percent;
	}

	public static class OccupyType {
		public String statusname;
		public String typename;
		public int occnum;
	}

	public static class OccupyTotal {
		public int[] totalArr;
		public int totals;
		public List<Types> typestatusInfoList;

	}

	public static class OrgType {
		public List<OrgType> child;
		public String orgId;
		public String orgName;
		public boolean hasChild;
	}

	public static class OrgList {
		public String resCode;
		public String resDesc;
		public OrgType result;
	}

	public static class OrgUserType {
		public String id;
		public String account;
		public String name;
		public String disabled;
		public String deptid;
		public String locateRegId;
		public String locateRegName;
		public boolean checked;
	}

	public static class OrgUserList {
		public List<OrgUserType> result;
	}

	public static class TelBookType {
		public String memberPosition;
		public String contactGroupId;
		public String memberOffice;
		public String memberMsidn;
		public String memberName;
		public String contactId;
	}

	public static class TelBookList {
		public List<TelBookType> result;
	}

	public static class ContactGroupType {
		public List<ContactGroupType> child;
		public String hasChild;
		public String groupId;
		public String groupName;
	}

	public static class ContactGroupList {
		public List<ContactGroupType> result;
	}

	public static class GetProjectTypeList {
		public List<Type> stuffStatusType;
	}

	public static class GetMaterielTypeList {
		public List<Type> materialStuffType;
	}

	public static class GetSupplierTypeList {
		public List<Type> supplierList;
	}

	public static class GetDeviceCategoryList {
		public List<Type> municipaldtype;
	}

	public static class GetDeviceBelongList {
		public List<Type> smalldtype;
	}

	public static class GetOcuupyApplicantList {
		public List<Type> applicantinfo;
	}

	public static class GetOcuupyBuilderList {
		public List<Type> builderinfo;
	}

	public static class GetOcuupyProgressList {
		public List<Type> occupystatustype;
	}

	public static class GetOcuupyAreaList {
		public List<Type> suburbtype;
	}

	public static class GetOcuupyTypeList {
		public List<Type> occupystyle;
	}

	public static class GetOcuupyRoadTypeList {
		public List<Type> data;
	}

	public static class GetOcuupyType {
		public List<OccupyType> applyStatusList;
		public List<OccupyType> applyTypeList;
	}

	public static class GetOcuupyLevelCodeList {
		public List<Type> levelcode;
		public List<Type> picturetypeList;
		public List<Type> illustrationtemplate;
		public List<Type> delayreasontemplate;
	}

	public static class GetPatrolTypeList {
		public List<Type> tourOrderType;
		public List<Type> tourOrderState;
		public List<Type> tourDutyType;
		public List<Type> tourKpState;
		public List<Type> tourIsBack;
	}

	public static class GetPatrolTypeResult {
		public GetPatrolTypeList result;
	}

	public static class GetPatrolBulderList {
		public List<Type> type;
	}

	public static class GetPatrolResult {
		public GetPatrolBulderList result;
	}

	public static class UpLoadResule {
		public String fileName;
		public String filePath;
		public String fileSize;
	}

	public static class getUpLoadFile {
		public UpLoadResule result;
		public String resDesc;
		public String resCode;
	}

	public static class PublishOrderData {
		public List<UpLoadResule> files;
		public String workEventType;
		public String workOrderText;
		public String workEventPointLatlon;
		public String workEventPointExplain;
		public String dutyOrgCode;
		public String dutyOrgType;
		//
		public String workOrderCode;
		public String workOrderId;
		public String siteDescription;
		public String issuedRegId;
		public String workIsIssued;
		public String workIsNodus;
		public String workEventState;
		public String workOrderRemark;
		public String checkStatus;
		public String checkDescription;
		public String workBackExplain;
	}

	public static class OrderProgressData {
		public List<UpLoadResule> files;
		public String acceptName;
		public String workOrderId;
		public String disposeText;
		public String disposeType;
	}

	public static class GPSData implements Serializable {
		private static final long serialVersionUID = -6620263730336948012L;
		public String userId;
		public String orgId;
		public String userPhone;
		public String gpsTime;
		public String longitude;
		public String latitude;
		public String gpsStatus;
		public String locateAddress;
	}

	public static class getGPSData {
		public GPSData result;
	}

	public static class getGPSDataList {
		public List<GPSData> result;
	}

	public static class MaterielHome implements Serializable {
		private static final long serialVersionUID = -6620263730336948012L;
		public String plansum;// 需求计划
		public String suppliersum;// 库存信息，
		public String purchasesum;// 采购单，
		public String formsum;// 需求单信息
	}

	public static class DeviceHome implements Serializable {
		private static final long serialVersionUID = -6620263730336948012L;
		public String insuresum;// 保险管理,
		public String maintainplansum;// 维修计划，
		public String accidentsum;// 事故报告，
		public String scrapsum;// 报废申请，
		public String purchasesum;// 采购申请
	}

	public static class OccupyStatistics {
		public double wrecordSum;
		public double zrecordSum;
		public double wrecordLen;
		public double zrecordLen;
		public String name;
	}

	public static class DeviceVehicle {
		/***
		 * gps时间
		 */
		public String posTime;
		/***
		 * ID
		 */
		public String vehicleId;
		/***
		 * 设备名称
		 */
		public String deviceName;
		/***
		 * 纬度
		 */
		public String latitude;
		/***
		 * 经度
		 */
		public String longitude;
		// 车牌号码
		public String vehNoCode;
		// SIM卡号
		public String code;
		// 出厂号
		public String devNo;
		// 车牌颜色
		public String vehNoCol;
		// 车辆类型
		public String useType;
		// 详细地址
		public String location;
		// 操作员
		public String operator;
		// 联系电话
		public String telephone;
		// 行驶速度
		public double speed;
		// 资产编号
		public String devCode;
		// 正反转标示
		public String status;
		public String belongtoCompanyName;
	}

	public static class OccupyStatisticsList {
		public List<OccupyStatistics> data;
	}

	public static class DeviceVehicleList {
		public List<DeviceVehicle> data;
	}

}
