package com.xiangxun.xacity.common;

/**
 * @package: com.xiangxun.util
 * @ClassName: ConstantStatus.java
 * @Description: 结果码
 * @author: HanGJ
 * @date: 2015-7-24 上午10:47:02
 */
public class ConstantStatus {
	// 列表请求的状态 刷新 加载更多
	public static final int listStateRefresh = 1;
	public static final int listStateLoadMore = 2;
	public static final int listStateFirst = 3;
	// 工单上报图片上传地址
	public static final String ORDER = "upload/order";
	// 延期图片上传地址
	public static final String DELAY = "attachfile/occupy/delay";
	// 勘察图片上传地址
	public static final String INSPECT = "attachfile/occupy/inspect";
	// 格式
	public static final String ENCODE = "UTF-8";
	// 登录
	public static final int loadSuccess = 4;
	public static final int loadFailed = 5;
	// 上传本地照片状态码
	public static final int UpLoadSuccess = 6;
	public static final int UpLoadFalse = 7;
	// 从本地加载图片状态
	public static final int GetLocalSuccess = 8;
	public static final int GetLocalFalse = 9;
	// SD卡无空间
	public static final int SD_NOSPACE = 10;
	// 通知公告
	public static final int getNoticeBeanListSuccess = 11;
	public static final int getNoticeBeanListFailed = 12;
	// 合格供方名册
	public static final int getDeviceQualifiedListSuccess = 13;
	public static final int getDeviceQualifiedListFailed = 14;
	// 采购租赁申请
	public static final int getDeviceLeaseApplyListSuccess = 15;
	public static final int getDeviceLeaseApplyListFailed = 16;
	// 设备验收
	public static final int getDeviceAcceptanceListSuccess = 17;
	public static final int getDeviceAcceptanceListFailed = 18;
	// 外租设备台账
	public static final int getDeviceRentLedgerListSuccess = 19;
	public static final int getDeviceRentLedgerListFailed = 20;
	// 合同台账
	public static final int getDeviceContractLedgerListSuccess = 21;
	public static final int getDeviceContractLedgerListFailed = 22;
	// 操作人员台账
	public static final int getOperatorLedgerListSuccess = 23;
	public static final int getOperatorLedgerListFailed = 24;
	// 设备維修
	public static final int getDeviceMaintainListSuccess = 25;
	public static final int getDeviceMaintainListFailed = 26;
	// 设备报废
	public static final int getDeviceScrapListSuccess = 27;
	public static final int getDeviceScrapListFailed = 28;
	// 备件油料
	public static final int getDeviceSpareOilListSuccess = 29;
	public static final int getDeviceSpareOilListFailed = 30;
	// 需求管理
	public static final int getMaterialDemanListSuccess = 31;
	public static final int getMaterialDemanListFailed = 32;
	//
	public static final int getMaterialListSuccess = 33;
	public static final int getMaterialListFailed = 34;
	//
	public static final int getSupplierListSuccess = 35;
	public static final int getSupplierListFailed = 36;
	// 道路挖占--查询管理
	public static final int getOccupySearchListSuccess = 37;
	public static final int getOccupySearchListFailed = 38;
	// 道路挖占
	public static final int getOccupyListSuccess = 39;
	public static final int getOccupyListFailed = 40;
	public static final int getOccupyDetailSuccess = 41;
	public static final int getOccupyDetailStaticSuccess = 42;
	public static final int getOccupyDetailImageSuccess = 43;
	//
	public static final int getOccupyApplicantSuccess = 44;
	public static final int getOccupyBuilderSuccess = 45;
	public static final int getOccupyProgressSuccess = 46;
	public static final int getOccupyAreaSuccess = 47;
	public static final int getOccupyTypeSuccess = 48;
	// 巡视系统--查询管理
	public static final int getPatrolSearchListSuccess = 49;
	public static final int getPatrolSearchListFailed = 50;
	// 审核/驳回
	public static final int VerifySuccess = 51;
	public static final int VerifyFailed = 52;
	// 驳回
	public static final int VerifyResultSuccess = 53;
	public static final int VerifyResultFailed = 54;
	// 勘察说明
	public static final int SurveySuccess = 55;
	public static final int SurveyFailed = 56;
	//
	public static final int OccupyPercentageSuccess = 57;
	public static final int OccupyPercentageFailed = 58;
	//
	public static final int OccupyStatusSuccess = 59;
	public static final int OccupyStatusFailed = 60;
	//
	public static final int OccupyTypeSuccess = 61;
	public static final int OccupyTypeFailed = 62;
	// 审提交核
	public static final int SubmitVerifySuccess = 63;
	public static final int SubmitVerifyFailed = 64;
	// 通讯录
	public static final int ContactGroupSuccess = 65;
	public static final int ContactGroupBookSuccess = 66;
	public static final int ContactGroupBookDetailSuccess = 67;
	public static final int ContactFailed = 68;
	public static final int ContactSearchSuccess = 69;
	public static final int ContactSearchFailed = 70;
	// 机构
	public static final int OrgSuccess = 71;
	public static final int OrgUserSuccess = 72;
	public static final int OrgFailed = 73;
	public static final int OrgSearchSuccess = 74;
	public static final int OrgSearchFailed = 75;
	// 工单保存
	public static final int PublishOrderSuccess = 76;
	public static final int PublishOrderFailed = 77;
	// 获取定位设置
	public static final int LocationConfigSuccess = 78;
	public static final int LocationConfigFailed = 79;
	public static final int LocationConfigNotSet = 80;
	// 人员历史轨迹
	public static final int LocationListSuccess = 81;
	public static final int LocationListFailed = 82;
	//
	public static final int DeviceHomeSuccess = 83;
	public static final int DeviceHomeFailed = 84;
	//
	public static final int getOccupyRoadTypeSuccess = 85;
	// 成功返回码
	public static final int SUCCESS = 200;
	// 网络错误返回码
	public static final int NetWorkError = 400;
	// 缓存大小
	public static final int getCacheSize = 141;
	// 检查更新
	public static final int updateSuccess = 131;
	public static final int updateFalse = 132;
	public static final String FILESELECTORACTIONXML = "com.xiangxun.xml";
	public static final String FILESELECTORACTIONEXCEL = "com.xiangxun.excel";

	public static final String SIZE = "size";
	
}
