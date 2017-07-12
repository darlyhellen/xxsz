package com.xiangxun.xacity.request;

import android.content.Context;

import com.xiangxun.xacity.utils.EncodeTools;
import com.xiangxun.xacity.utils.ShareDataUtils;
import com.xiangxun.xacity.utils.Tools;

/**
 * @package: com.xiangxun.xacity.request
 * @ClassName: ApiUrl.java
 * @Description: 所有请求方法url拼接
 * @author: HanGJ
 * @date: 2016-2-19 上午8:57:44
 */
public class ApiUrl {

	// 登录
	public static String login() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("login/login/authority/?callback=JSON_CALLBACK").toString()));
	}

	// 公告列表
	public static String getNoticeList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("notice/info/query/?callback=JSON_CALLBACK").toString()));
	}

	// 公告詳情
	public static String getNoticeDetail() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("notice/info/view/?callback=JSON_CALLBACK").toString()));
	}

	/*********************** 设备采购接口地址 ****************************/
	// 合格供方名册
	public static String getDeviceQualifiedList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/purchase/supplier/list/?callback=JSON_CALLBACK").toString()));
	}

	// 采购租赁申请
	public static String getDeviceLeaseApplyList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/purchase/purchase/list/?callback=JSON_CALLBACK").toString()));
	}

	// 设备验收
	public static String getDeviceAcceptanceList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/purchase/inspection/list/?callback=JSON_CALLBACK").toString()));
	}

	// 外租设备台账
	public static String getDeviceRentLedgerList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/purchase/rent/list/?callback=JSON_CALLBACK").toString()));
	}

	// 合同台账
	public static String getDeviceContractLedgerList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/purchase/contract/list/?callback=JSON_CALLBACK").toString()));
	}

	// 合同台账详情
	public static String getDeviceContractDetail() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/purchase/goViewContract/?callback=JSON_CALLBACK").toString()));
	}

	/*********************** 设备管理接口地址 ****************************/
	// 操作人员台账
	public static String getOperatorLedgerList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/manage/operator/list/?callback=JSON_CALLBACK").toString()));
	}

	// 设备台账
	public static String getDeviceLedgerList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/manage/mainshow/list/?callback=JSON_CALLBACK").toString()));
	}

	// 安全保护装置
	public static String getSafetyProtectionList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/manage/safety/list/?callback=JSON_CALLBACK").toString()));
	}

	// 设备派遣
	public static String getDeviceDispatchList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/manage/dispatch/list/?callback=JSON_CALLBACK").toString()));
	}

	// 使用记录
	public static String getDeviceUseRecordList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/manage/exercise/list/?callback=JSON_CALLBACK").toString()));
	}

	// 设备到场验证
	public static String getDeviceVerifyList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/manage/checking/list/?callback=JSON_CALLBACK").toString()));
	}

	// 日常检查记录
	public static String getDailyInspectionRecordList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/manage/everyinspect/list/?callback=JSON_CALLBACK").toString()));
	}

	// 完好利用率
	public static String getUtilizationRateList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/manage/intact/list/?callback=JSON_CALLBACK").toString()));
	}

	// 重点设备月报表
	public static String getMonthlyRreportList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/manage/emphasis/list/?callback=JSON_CALLBACK").toString()));
	}

	// 保险管理
	public static String getInsuranceManageList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/manage/insure/list/?callback=JSON_CALLBACK").toString()));
	}

	/*********************** 设备維修接口地址 ****************************/
	// 设备维修计划
	public static String getMaintenancePlanList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/repair/maintainplan/list/?callback=JSON_CALLBACK").toString()));
	}

	// 维修工单
	public static String getMaintenanceOrderList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/repair/overhaul/list/?callback=JSON_CALLBACK").toString()));
	}

	// 维修记录
	public static String getMaintenanceRecordList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/repair/maintainrecord/list/?callback=JSON_CALLBACK").toString()));
	}

	// 事故报告记录
	public static String getAccidentReportRecordList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/repair/accident/list/?callback=JSON_CALLBACK").toString()));
	}

	/*********************** 设备报废接口地址 ****************************/
	// 设备报废
	public static String getDeviceScrapApplyList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/scrap/scrap/list/?callback=JSON_CALLBACK").toString()));
	}

	/*********************** 备件油料接口地址 ****************************/
	// 入库台账记录
	public static String getInLedgerRecordsList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/partoil/enterstock/list/?callback=JSON_CALLBACK").toString()));
	}

	// 出库台账记录
	public static String getOutLedgerRecordsList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/partoil/outstock/list/?callback=JSON_CALLBACK").toString()));
	}

	// 加油管理
	public static String getOilPetrolManageList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/partoil/refuel/list/?callback=JSON_CALLBACK").toString()));
	}

	// 领料申请
	public static String getMaterialApplyList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/partoil/drawapply/list/?callback=JSON_CALLBACK").toString()));
	}

	/***************************************************** end ******************************************************/
	/*********************** 需求管理接口地址 ****************************/
	// 项目管理
	public static String getMaterielDemandPMList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/demand/project/list/?callback=JSON_CALLBACK").toString()));
	}

	//
	public static String getMaterielProjectInfoList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/demand/projectResource/list/?callback=JSON_CALLBACK").toString()));
	}

	//
	public static String getMaterielProjectRecordList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/demand/verify/list/?callback=JSON_CALLBACK").toString()));
	}

	// 需求计划
	public static String getMaterielDemandPlanList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/demand/plan/list/?callback=JSON_CALLBACK").toString()));
	}

	// 需求计划下的需求单信息
	public static String getMaterielDemandFormListList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/demand/demandFormList/list/?callback=JSON_CALLBACK").toString()));
	}

	// 需求计划下的需求审核记录信息
	public static String getMaterielDemandRecordListList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/demand/demandplan/verify/list/?callback=JSON_CALLBACK").toString()));
	}

	/*********************** 采购管理接口地址 ****************************/
	// 需求单管理
	public static String getMaterielDemanPurchasesList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/purchase/demandform/list/?callback=JSON_CALLBACK").toString()));
	}

	// 需求单信息
	public static String getDemanOrderDetailList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/purchase/demandView/list/?callback=JSON_CALLBACK").toString()));
	}

	// 采购单管理
	public static String getPurchasesOrderManageList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/purchase/purchaseform/list/?callback=JSON_CALLBACK").toString()));
	}

	// 采购单发货信息
	public static String getPurchaseOrderDeliverList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/purchase/deliverForm/list/?callback=JSON_CALLBACK").toString()));
	}

	// 采购单审核记录
	public static String getPurchaseOrderVerifyList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/purchase/verify/list/?callback=JSON_CALLBACK").toString()));
	}

	// 外部供应商管理
	public static String getMaterielSupplierManageList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/purchase/supplier/B/list/?callback=JSON_CALLBACK").toString()));
	}

	/*********************** 工程监管接口地址 ****************************/
	// 项目管理
	public static String getMaterielProjectManageList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/supervise/project/list/?callback=JSON_CALLBACK").toString()));
	}

	// 供应商监管
	public static String getMaterielSupplierList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/supervise/supplier/list/?callback=JSON_CALLBACK").toString()));
	}

	/*********************** 供应商管理接口地址 ****************************/
	// 采购单管理
	public static String getSupplierPurchaseManageList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/supply/purchaseform/list/?callback=JSON_CALLBACK").toString()));
	}

	// 发货单管理
	public static String getSupplierDeliverManageList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/supply/deliverform/list/?callback=JSON_CALLBACK").toString()));
	}

	// 发货单审核记录
	public static String getSupplierDeliverVerifyList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/supply/deliverform/verify/list/?callback=JSON_CALLBACK").toString()));
	}

	// 库存管理
	public static String getSupplierStoreManageList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/supply/stock/list/?callback=JSON_CALLBACK").toString()));
	}

	// 库存审核记录
	public static String getSupplierStoreVerifyList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/supply/stock/verify/list/?callback=JSON_CALLBACK").toString()));
	}

	// 入库管理
	public static String getSupplierInStoreManageList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/supply/storageputin/list/?callback=JSON_CALLBACK").toString()));
	}

	// 入库审核记录
	public static String getSupplierInStoreVerifyList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/supply/storageputin/verify/list/?callback=JSON_CALLBACK").toString()));
	}

	// 出库管理
	public static String getSupplierOutStoreManageList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/supply/storageputout/list/?callback=JSON_CALLBACK").toString()));
	}

	// 出库审核记录
	public static String getSupplierOutStoreVerifyList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/supply/storageputout/verify/list/?callback=JSON_CALLBACK").toString()));
	}

	/***************************************************** end ******************************************************/
	// 项目状态
	public static String getProjectStatusList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/demand//dic/stuffStatusType/?callback=JSON_CALLBACK").toString()));
	}

	// 物资类别
	public static String getMaterielCategoryList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/purchase/dic/materialStuffType/?callback=JSON_CALLBACK").toString()));
	}

	// 供应商
	public static String getSupplierCategoryList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/purchase/supplierList/list/?callback=JSON_CALLBACK").toString()));
	}

	// 设备分类
	public static String getDeviceCategoryList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/manage/dic/municipaldtype/?callback=JSON_CALLBACK").toString()));
	}

	// 设备归属
	public static String getDeviceBelongList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/manage/dic/smalldtype/?callback=JSON_CALLBACK").toString()));
	}

	/***************************************************** start 道路挖占 ******************************************************/
	// 道路挖掘管理(occupytype=1)/道路占用管理(occupytype=2)/占道街具管理(occupytype=3)
	public static String getOccupyRoadPmList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/info/list/?callback=JSON_CALLBACK").toString()));
	}

	// 查询项目详细信息
	public static String getOccupySearchDetailList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/info/view/?callback=JSON_CALLBACK").toString()));
	}

	// 查询项目统计信息
	public static String getOccupyStatisticsDetailList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/info/showProportion/?callback=JSON_CALLBACK").toString()));
	}

	// 查询项目图片
	public static String getOccupyDetailImageList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/info/view/pic/?callback=JSON_CALLBACK").toString()));
	}

	// 延期项目查询
	public static String getOccupyRoadSpendList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/query/delay/list/?callback=JSON_CALLBACK").toString()));
	}

	// 勘察信息查询
	public static String getOccupyRoadInspectList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/query/inspect/list/?callback=JSON_CALLBACK").toString()));
	}

	// 更改状态查询
	public static String getOccupyStateSearchList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/query/log/list/?callback=JSON_CALLBACK").toString()));
	}

	// 申请单位档案
	public static String getOccupyApplyLedgeList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/company/applicant/list/?callback=JSON_CALLBACK").toString()));
	}

	// 施工单位档案
	public static String getOccupyConstructLedgeList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/company/builder/list/?callback=JSON_CALLBACK").toString()));
	}

	// 挖占统计
	public static String getOccupyStatisticsList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/analysis/static/list/?callback=JSON_CALLBACK").toString()));
	}

	// 退让面积统计
	public static String getOccupyConcessionList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/analysis/giveproportion/list/?callback=JSON_CALLBACK").toString()));
	}

	// 建设单位
	public static String getOccupyApplicantInfoList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/applicantinfo/list/?callback=JSON_CALLBACK").toString()));
	}

	// 施工单位
	public static String getOccupyBuilderInfoList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/builderinfo/list/?callback=JSON_CALLBACK").toString()));
	}

	// 施工进度
	public static String getOccupyProgressInfoList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/dic/occupystatustype/?callback=JSON_CALLBACK").toString()));
	}

	// 辖区
	public static String getOccupyAreaInfoList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/suburbtype/list/?callback=JSON_CALLBACK").toString()));
	}

	// 信用等级
	public static String getOccupyLevelCodeList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/company/dic/levelcode/?callback=JSON_CALLBACK").toString()));
	}

	// 施工类别(occupytype=1)/占用类别(occupytype=2)/街具类别(occupytype=3)
	public static String getOccupyTypeList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/dic/occupystyle/?callback=JSON_CALLBACK").toString()));
	}
	
	// 路面种类
	public static String getOccupyRoadTypeList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/dic/occupyroadtype/?callback=JSON_CALLBACK").toString()));
	}

	/***************************************************** end 道路挖占 ******************************************************/
	// 版本更新
	public static String getVersion() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("apk/detail/?callback=JSON_CALLBACK").toString()));
	}

	// 工单查询
	public static String getWorkOrderList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("tournew/order/query/?callback=JSON_CALLBACK").toString()));
	}

	// 工单详情
	public static String getWorkOrderDetail(String workOrderId) {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("tournew/order/orderDetail/?callback=JSON_CALLBACK&workOrderId=").append(workOrderId).toString()));
	}

	// 物资管理---项目提交审核
	public static String getProjectSubmitVerify() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/verify/doVerify/?callback=JSON_CALLBACK").toString()));
	}

	// 物资管理---项目审核
	public static String getProjectVerify() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/verify/doVerifyPass/?callback=JSON_CALLBACK").toString()));
	}

	// 物资管理---项目驳回
	public static String getProjectVerifyRebut() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/verify/doVerifyRebut/?callback=JSON_CALLBACK").toString()));
	}

	// 挖占管理---挖占百分比
	public static String getProjectResultMap() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/main/getResultMap/?callback=JSON_CALLBACK").toString()));
	}

	// 挖占管理---施工进度统计
	public static String getProjectOccupyStatus() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/main/pie/?callback=JSON_CALLBACK").toString()));
	}

	// 挖占管理---施工类别统计
	public static String getProjectOccupyInfos() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/main/column/?callback=JSON_CALLBACK").toString()));
	}

	// 挖占管理---延期上报
	public static String getOccupyDelay() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/doDelay/?callback=JSON_CALLBACK").toString()));
	}

	// 挖占管理---勘察上报
	public static String getOccupySurVey() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/doInspect/?callback=JSON_CALLBACK").toString()));
	}

	// 挖占管理---状态更改
	public static String getOccupyChange() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/doChange/?callback=JSON_CALLBACK").toString()));
	}

	// 挖占管理---标记
	public static String getOccupyMark() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/doMark/?callback=JSON_CALLBACK").toString()));
	}

	// 挖占管理---更改信用等级
	public static String getOccupyLevelChange() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/company/doChange/?callback=JSON_CALLBACK").toString()));
	}

	// 挖占管理---图片类型
	public static String getOccupyPictureTypeList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/dic/picturetypeList/?callback=JSON_CALLBACK").toString()));
	}

	// 挖占管理---勘察说明
	public static String getOccupySurVeyDescript() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/dic/illustrationtemplate/?callback=JSON_CALLBACK").toString()));
	}

	// 挖占管理---延期原因
	public static String getOccupyDelayDescript() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/dic/delayreasontemplate/?callback=JSON_CALLBACK").toString()));
	}

	// 挖占管理---勘察上报
	public static String getOccupySurVeyUpload() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/doInspect/?callback=JSON_CALLBACK").toString()));
	}

	// 挖占管理---勘察上报图片上传
	public static String getOccupySurVeyUploadImage(String account, String password, String id, String inspectTimeStr) {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/doSaveInspectPic/?callback=JSON_CALLBACK&loginName=").append(account).append("&password=").append(password).append("&id=").append(id).append("&inspectTimeStr=").append(inspectTimeStr).toString()));
	}

	// 挖占管理---延期上报
	public static String getOccupyDelayUpload() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/doDelay/?callback=JSON_CALLBACK").toString()));
	}

	// 挖占管理---延期上报图片上传
	public static String getOccupyDelayUploadImage(String account, String password, String id, String begintime, String deadtime) {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("occupy/manage/doSaveDelayPic/?callback=JSON_CALLBACK&loginName=").append(account).append("&password=").append(password).append("&id=").append(id).append("&begintime=").append(begintime).append("&deadtime=").append(deadtime).toString()));
	}

	// 巡视系统---获取字典项
	public static String getPatrolBaseData() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("dic/query/?callback=JSON_CALLBACK").toString()));
	}

	// 巡视系统---机构用户数据
	public static String getPatrolUserData() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("tour/locate/getOrgUser/?callback=JSON_CALLBACK").toString()));
	}

	// 巡视系统---机构数据
	public static String getPatrolOrgData() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("info/getOrg/?callback=JSON_CALLBACK").toString()));
	}

	// 巡视系统---所属机构用户数据
	public static String getPatrolOrgUserData() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("info/getUser/?callback=JSON_CALLBACK").toString()));
	}

	// 巡视系统---查询机构用户
	public static String getPatrolSearchOrgUser() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("info/queryUser/?callback=JSON_CALLBACK").toString()));
	}

	// 巡视系统---获取通讯群组
	public static String getPatrolContactGroup() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("tournew/contact/getContactGroup/?callback=JSON_CALLBACK").toString()));
	}

	// 巡视系统---获取群组通讯录
	public static String getPatrolContactGroupBook() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("tournew/contact/getContactBook/?callback=JSON_CALLBACK").toString()));
	}

	// 巡视系统---获取通讯录详情
	public static String getPatrolContactGroupBookDetail() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("tournew/contact/getDetail/?callback=JSON_CALLBACK").toString()));
	}

	// 巡视系统---查找通讯录
	public static String getPatrolContactSearchPerson() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("tournew/contact/query/?callback=JSON_CALLBACK").toString()));
	}

	// 巡视系统---查找群组通讯录
	public static String getPatrolContactSearchGroupPerson() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("tournew/contact/getContactBook/?callback=JSON_CALLBACK").toString()));
	}

	// 上传图片
	public static String upLoadImageUrl(Context context) {
		String account = ShareDataUtils.getSharedStringData(context, "loginName");
		String password = ShareDataUtils.getSharedStringData(context, "password");
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.upLoadImageHead).append("&loginName=").append(account).append("&password=").append(password).append("&keyValue=").append(DcNetWorkUtils.makeValidate(account, password)).toString()));
	}

	// 工单保存
	public static String publishOrderUrl(String... args) {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("tournew/order/saveOrder/?callback=JSON_CALLBACK").append("&loginName=").append(args[0]).append("&password=").append(args[1]).append("&keyValue=").append(DcNetWorkUtils.makeValidate(args[0], args[1])).toString()));
	}

	// 工单进展
	public static String publishOrderProgressUrl(String... args) {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("tournew/order/addDispose/?callback=JSON_CALLBACK").append("&loginName=").append(args[0]).append("&password=").append(args[1]).append("&keyValue=").append(DcNetWorkUtils.makeValidate(args[0], args[1])).toString()));
	}

	// 人员实时定位
	public static String locationPosition(Context context) {
		String account = ShareDataUtils.getSharedStringData(context, "loginName");
		String password = ShareDataUtils.getSharedStringData(context, "password");
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("tour/locate/uploadLocation/?callback=JSON_CALLBACK").append("&loginName=").append(account).append("&password=").append(password).append("&keyValue=").append(DcNetWorkUtils.makeValidate(account, password)).toString()));
	}

	// 获取定位设置
	public static String locationConfig() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("tour/locate/getLocateSet/?callback=JSON_CALLBACK").toString()));
	}

	// 人员实时定位
	public static String getUserLocation() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("tour/locate/getUserLocation/?callback=JSON_CALLBACK").toString()));
	}

	// 人员历史轨迹
	public static String getUserLocationList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("tour/locate/queryUserLocation/?callback=JSON_CALLBACK").toString()));
	}

	// 物资首页数据统计
	public static String getMaterielHome() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/main/index/?callback=JSON_CALLBACK").toString()));
	}

	// 设备首页数据统计
	public static String getDevicelHome() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/main/index/?callback=JSON_CALLBACK").toString()));
	}
	
	// 车辆信息
	public static String getDevicelVehicleList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/gpsinfo/curr/listByorgId/?callback=JSON_CALLBACK").toString()));
	}
	
	// 需求计划详情
	public static String getMaterielDetail() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("material/purchase/demandplan/view/?callback=JSON_CALLBACK").toString()));
	}
	
	// 报废审核记录列表
	public static String getDeviceScrapVerifyList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/scrap/verifyRecord/?callback=JSON_CALLBACK").toString()));
	}
	
	// 采购租赁审核记录列表
	public static String getDevicePurchaseVerifyList() {
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(Api.urlHeadMobile).append("device/purchase/verifyRecord/?callback=JSON_CALLBACK").toString()));
	}

}
