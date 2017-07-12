package com.xiangxun.xacity.bean;

import java.io.Serializable;
import java.util.List;

public class ResponseResultBeans {

	/**
	 * 通知公告列表信息
	 */
	public static class NoticeBean implements Serializable {
		private static final long serialVersionUID = 4858499390805278042L;
		public String noticeId;// 公告ID
		public String noticeTitle;// 公告标题
		public String noticeType;// 公告类型
		public String noticeKeyword;// 公告关键词
		public String noticeInstancy;// 公告紧急程度
		public String releaseUserId;// 公告发布人
		public String releaseUserName;// 公告发布人
		public String releaseOrgId;// 公告发布人机构
		public String releaseOrgName;// 公告发布人机构
		public String noticeBegin;// 公告发布日期
		public String noticeEnd;// 公告失效日期
		public String noticeIsTop;// 是否置顶
		public String noticeStatus;// 公告状态
		public String createTime;// 创建时间
		public String createUserId;// 创建人
		public String createOrgId;// 创建人机构
		public String updateTime;// 最后修改人
		public String updateUserId;// 最后修改时间
		public String noticeRange;// 公告发布范围
		public String noticeContent;// 公告内容
	}

	public static class SupplierBean implements Serializable {
		private static final long serialVersionUID = 7815242824117351235L;
		public String id;
		public String remark;
		/********** 合格供方名册 *************/
		public String name;
		public String product;
		public String dateTime;
		public String linkman;
		public String telephone;
		public String evaluateUnit;
		public String evaluateMan;
		/********** 采购租赁申请 *************/
		public String deviceName;
		public String code;
		public String num;
		public String applyDate;
		public String status;
		public String model;
		public String applicant;
		public String orgId;
		public String orgName;
		public int budgetFund;
		public String statusHtml;
		public String ispurchase;
		/********** 外租设备台账 *************/
		public String deviceSupplierName;
		public String deviceSupplierId;
		public String endDateTime;
		public String price;
		public String way;
		public String unit;
		/********** 设备验收 *************/
		public String manufacturer;
		public String levaeFactoryCode;
		public String levaeFactoryDate;
		public String aogDateTime;
		public String usingCompany;
		public String address;
		public String checkRecord;
		public String surveyor;
		public String usingCompanyName;
		public String photo;
		public String place;
		public String tempcode;
		/********** 合同台账 *************/
		public String purchaseDeparment;
		public String content;
		public String purchaseDeparmentId;
		public String cooperationDepartment;
		public String operator;
		public String condition;
		public String registerDate;
		public String result;
	}

	public static class ContractDetail {
		public String orgName;
		public String purchaseDeparment;
		public String id;
		public String code;
		public String orgId;
		public String content;
		public String purchaseDeparmentId;
		public String dateTime;
		public String name;
		public String cooperationDepartment;
		public String operator;
		public String contractFile;
		public String registerDate;
		public String money;
		public String schedule;
		public String condition;
	}

	public static class DeviceManageBean implements Serializable {
		private static final long serialVersionUID = -105671652942169606L;
		/********** 操作人员台账 *************/
		public String id;
		public String name;
		public String sex;
		public String birthdate;
		public String standardcultre;
		public String station;
		public String certificateDate;
		public String certificateCode;
		public String checkDate;
		public String orgId;
		public String orgName;
		public String remark;
		public String telephone;
		public String photo;
		/********** 设备台账 *************/
		public String typeName;
		public String stypeName;
		public String stateName;
		public String startUsingDate;
		public String capitalName;
		public String curtainName;
		public String belongtoCompanyName;
		public String usingCompanyName;
		public String operatorName;
		public String code;
		public String brand;
		public String model;
		public String num;
		public String original;
		public String buyDate;
		public String checkReport;
		public String operator;
		public int oilPoint;
		/********** 安全保护装置 *************/
		public String mdId;
		public String deviceCode;
		public String deviceName;
		public String deviceSafety;
		public String explain;
		/********** 设备派遣 *************/
		public String deviceModel;
		public String dateTime;
		public String reason;
		public String address;
		public String affirmPerson;
		public String usingTime;
		public String taibanNum;
		/********** 使用记录 *************/
		public String mdeUsingCompanyName;
		public String mdePropertyName;
		public String mdCode;
		public String mdName;
		public String mdModel;
		public String mdeBelongtoCompany;
		public String mdeBelongtoCompanyName;
		public String belongtoCompany;
		public String mdeStartUsingDate;
		public String mdeState;
		public String usingMan;
		public String usingAddress;
		/********** 日常检查记录 *************/
		public String plate;
		public String deviceType;
		public String constructionAddress;
		public String principal;
		public String operatorCondition;
		public String significantProblem;
		public String dynamicPapt;
		public String chassisPart;
		public String minglePart;
		public String authenticateCorrect;
		public String identificationManagement;
		public String periodInspect;
		/********** 完好利用率 *************/
		public String intactRegime;
		public String intact;
		public String intactRatio;
		public String useRegime;
		public String utilizeRegime;
		public String reality;
		public String overtime;
		public String useRatio;
		public String month;
		/********** 重点设备月报表 *************/
		public String workTime;
		public String totalTime;
		public String deviceState;
		public String completeContent;
		public String maintainContent;
		public String manager;
		public String company;
		public String companyName;
		/********** 保险管理 *************/
		public String premium;
		public String startDate;
		public String insureCompany;
		public String belongToCompany;
		public String bcompanyName;
		public String businessPremium;
		public String shipPremium;
		public String premiumSum;
		public int warningDays;
	}

	public static class MaintainRecord implements Serializable {
		private static final long serialVersionUID = 7216738459706541635L;
		public String id;
		public String parentid;
		public String maintainName;
		public String stuffCost;
		public String taskCost;
		public String costTotal;
		public String totalCount;
	}

	public static class DeviceMaintainBean implements Serializable {
		private static final long serialVersionUID = 1461587993228015860L;
		/********** 设备维修计划 *************/
		public String useDateTime;
		public String belongtoCompany;
		public String belongtoCompanyName;
		public String status;
		public String lastTime;
		public String nextTime;
		public String operator;
		public String content;
		/********** 维修工单 *************/
		public String repairFactory;
		public String pepairAddress;
		public String maintainCost;
		public String sendRepair;
		public String entranceDateTime;
		public String overhaulItme;
		public String recipient;
		public String runKm;
		/********** 维修记录 *************/
		public String id;// 主键ID
		public String mdId;// 设备Id
		public String deviceName;// 设备名称
		public String deviceCode;// 资产编号
		public String dateTime;// 维修时间
		public String usingCompany;// 使用单位编号
		public String usingCompanyName;// 使用单位名称
		public String cost;// 维修费用
		public String address;// 维修地点
		public String project;// 维修项目
		public String plate;// 牌照号码
		public String model;// 设备型号
		/********** 事故报告记录 *************/
		public String code;// 事故单号
		public String insureCompany;//
		public String dealTrafficPolice;//
		public String principal;// 负责人
		public String loss;// 经济损失
		public String describe;// 事故描述
		public String dispose;// 事故处理报告
		public String photo;//
		public String dutyDivide;//
		public List<MaintainRecord> maintainList;
		public List<ImageBean> picList;
	}

	public static class DeviceScrapBean implements Serializable {
		private static final long serialVersionUID = -5835855134428275856L;
		public String scrapCompanyName;
		public String id;
		public String code;
		public String scrapCompany;
		public String mdId;
		public String deviceName;
		public String deviceModel;
		public String deviceCode;
		public String reason;
		public String appraisalResult;
		public String manufacturers;
		public String proposer;
		public String affirmPerson;
		public String status;
		public String dateTime;
		public String buyDateTime;
		public int usingCount;
		public String statusHtml;
		public String plate;
		public String original;
		public String photo;
		public String attach;
	}

	public static class DeviceSpareOilBean implements Serializable {
		private static final long serialVersionUID = 215944356532029093L;
		/********** 入库台账记录 *************/
		public String id;// 主键ID
		public String name;// 名称 - 材料名称 - 设备名称
		public String model;// 型号
		public String quantity;// 数量
		public String organization;// 单位
		public String oname;// 单位名称
		public String sellOrganization;// 供应单位
		public String proveCode;// 质量证明编号
		public String checkStatus;// 检查状态
		public String checkContent;// 检查内容
		public String inspectPerson;// 检查人
		public String price;// 单价
		public String unit;// 计量单位
		public String buyer;// 采购人
		public String dateTime;// 采购日期 - 加油时间 - 申请时间
		public String depositAddress;// 存放地点
		/********** 出库台账记录 *************/
		public String userPlace;
		public String getPerson;
		/********** 加油管理 *************/
		public String orgId;// 单位编号
		public String orgName;// 单位名称
		public String driver;// 驾驶员
		public String plate;// 牌照号码
		public String fat;// 油品
		public String measure;// 加油量
		public String money;// 金额
		public String fatModel;// 油号
		/********** 领料申请 *************/
		public String num;// 数量
		public String company;// 单位ID
		public String companyName;// 单位名称
		public String code;// 申请单号
		public String drawPerson;// 申请人
		public String projectName;// 工程名称
		public String remark;// 备注
	}

	public static class MaterielDemandBean implements Serializable {
		private static final long serialVersionUID = -3297686694899809507L;
		/********** 项目管理 *************/
		public String id;
		public String name;
		public String code;
		public String starttime;
		public String planendtime;
		public String place;
		public String principal;
		public String mobile;
		public String dept;
		public String operator;
		public String addtime;
		public String status;
		public String applyflag;
		public String applier;
		public String verifyflag;
		public String verifier;
		public String verifyresult;
		public String disabled;
		public String isApply;
		public String isComplete;
		public String starttimeStr;
		public String planendtimeStr;
		public String statusHtml;
		public int statusNum;
		/********** 需求计划 *************/
		public String projectCode;
		public String projectName;
		public String month;
		public String applytime;
		public String verifytime;
		public String deadtime;
		public String note;
	}

	public static class ProjectInfo implements Serializable {
		private static final long serialVersionUID = 1490513748197807963L;
		/********** 项目计划信息 *************/
		public String id;
		public String projectCode;
		public String type;
		public String specification;
		public String amount;
		/********** 项目审核记录 *************/
		public String resourceId;
		public String status;
		public String applyflag;
		public String applier;
		public String applytime;
		public String verifyflag;
		public String verifytime;
		public String verifyresult;
		public String rebutreason;
		public String verifier;
		public String disabled;
		public String note;
		public String unit;
		public int statusNum;
		public int index;
	}

	public static class PurchaseOrderManageBean implements Serializable {
		private static final long serialVersionUID = -400187057150347033L;
		/********** 需求单管理 *************/
		public String id;
		public String planCode;
		public String type;
		public String specification;
		public String amount;
		public String supplier;
		public String supplierType;
		public String isAssign;
		public String projectCode;
		public String projectName;
		public String month;
		public String monthStr;
		public String status;
		public String applyflag;
		public String applier;
		public String applytime;
		public String verifyflag;
		public String verifier;
		public String verifytime;
		public String verifyresult;
		public String disabled;
		public String operator;
		public String operatorName;
		public String addtime;
		public String statusHtml;
		public int statusNum;
		public int index;
		public String unit;
		public String receiver;
		public String receivetime;
		/********** 采购单管理 *************/
		public String demandformId;
		public int stockTotal;
		public String note;
		/********** 外部供应商管理 *************/
		public String name;
		public String commAddress;
		public String address;
		public String dept;
		public String levelCode;
		public String productName;
		// 补充字段
		public String purchaseformId;
		public String requireAmount;
		public String receiveAmount;
		public String deliverAmount;
		public String product;
		public String code;
		public String starttime;
		public String planendtime;
		public String place;
		public String principal;
		public String mobile;
		public String isApply;
		public String isComplete;
		public String starttimeStr;
		public String planendtimeStr;
		public String total;
		public String entertime;
		public String rebutreason;
		public String deadtime;
		/********** 出入库管理 *************/
		public String pid;
		public String phone;
		public String remark;
	}

	/**
	 * @package: com.xiangxun.xacity.bean
	 * @ClassName: ResponseResultBeans.java
	 * @Description: 查询管理
	 * @author: HanGJ
	 * @date: 2016-3-2 下午3:05:55
	 */
	public static class OccupySearchBean implements Serializable {
		private static final long serialVersionUID = -867799539448999444L;
		public String id;
		public String pid;
		public String builderId;
		public String roadId;
		public String occupyType;
		public String operator;
		public String applyTime;
		public String deadtime;
		public String reason;
		public String picPath;
		public String applicantId;
		public String type;
		public String note;
		public String begintime;
		public String applyTimeStr;
		public String begintimeStr;
		public String deadtimeStr;
		public String reasonName;
		public String applicantName;
		public String principal;
		public String mobile;
		public String pname;
		//
		public String inspector;
		public String inspectTime;
		public String illustration;
		public String picType;
		public String opratorTime;
		public String managearea;
		public String remark;
		public String inspectTimeStr;
		public String illustrationName;
		//
		public String beforeStatus;
		public String afterStatus;
		public String updateTime;
		public String pcode;
		public String roadname;
		public String startworkDate;
		public List<ImageBean> picList;
		public String builderName;// 建设/施工单位
		public String builderSignDate;// 建设/施工单位签字时间
		public String detachmentName;// 中队签字
		public String detachmentSignDate;// 中队签字时间
		public String dataSource;
		public String aleadingOfficial;// 建设单位 联系人
		public String bleadingOfficial;// 施工单位 联系人
		public String completeDate; // 完工时间
		public String finishDate; // 竣工时间
		public String transferDate; // 移交恢复路面时间
		public String maintainName; // 维护公司签字
		public String maintainSignDate; // 维护公司签字时间
		public String startworkExplain;// 开工情况说明
		public String completeExplain;// 完工情况说明
		public String delayExplain;// 延迟情况说明
	}

	public static class ImageBean implements Serializable {
		private static final long serialVersionUID = 3254266620186766010L;
		public String id;
		public String inspectId;
		public String img;
		public String path;
	}

	/**
	 * @package: com.xiangxun.xacity.bean
	 * @ClassName: ResponseResultBeans.java
	 * @Description: 单位档案
	 * @author: HanGJ
	 * @date: 2016-3-3 上午9:45:13
	 */
	public static class OccupyArchiveBean implements Serializable {
		private static final long serialVersionUID = -2617470484136731008L;
		public String id;
		public String name;
		public String leadingOfficial;
		public String insertTime;
		public String operatorId;
		public String disabled;
		public String isblack;
		public String leadingOfficialMobile;
		public String levelCode;
		public String unitAttribute;
	}

	/**
	 * @package: com.xiangxun.xacity.bean
	 * @ClassName: ResponseResultBeans.java
	 * @Description: 挖占管理
	 * @author: HanGJ
	 * @date: 2016-3-3 下午2:31:22
	 */
	public static class OccupyManageBean implements Serializable {
		private static final long serialVersionUID = 6647174861987664211L;
		public String id;
		public String projectName;
		public String projectCode;
		public String applicantId;
		public String applyTime;
		public String builderId;
		public String type;
		public String lengths;
		public String widths;
		public String proportion;
		public String approveEndtime;
		public String longitude;
		public String latitude;
		public String roadType;
		public String isCrossroads;
		public String isNearbyhas;
		public String isBlocking;
		public String isMainroad;
		public String isprofession;
		public String isremind;
		public String iconType;
		public String status;
		public String isDelay;
		public String railProportion;
		public String railLengths;
		public String railWidths;
		public String starttime;
		public String endtime;
		public String roadname;
		public String principal;
		public String mobile;
		public String manager;
		public String managearea;
		public String occupytype;
		public String statusBefore;
		public String starttimeStr;
		public String endtimeStr;
		public double recordSum;
		public double totalSum;
		public double wrecordSum;
		public double zrecordSum;
		public double reallySum;
		public double wreallySum;
		public double zreallySum;
		public String applicantName;
		public String builderName;
		public double toexpireNum;
		public double beyondedNum;
		public double maintainNum;
		public String statusHtml;
		public String typeName;
		public String webprojectId;
		public String roadStyle;
		public String delayFlag;
		public String applyStarttime;
		public String applyCode;
		public String municipalLeader;
		public String approveTime;
		public String approveEndtimeStr;
		public String remark;

	}

	public static class OccupyAreaBean {
		public String id;
		public String projectId;
		public String faceType;
		public String lengths;
		public String widths;
		public String proportion;
		public String analysisLengths;
		public String analysisProportion;
		public String type;
		public String note;
		public String delayFlag;
	}

	/**
	 * @package: com.xiangxun.xacity.bean
	 * @ClassName: ResponseResultBeans.java
	 * @Description: 工单查询实体类
	 * @author: HanGJ
	 * @date: 2016-3-10 上午10:28:10
	 */
	public static class WorkOrderBean implements Serializable {
		private static final long serialVersionUID = -6026372674276599168L;
		public int workDays;
		public String serveBackText;
		public String dutyOrgCode;
		public String complaintAddress;
		public String workAssess;
		public String updateId;
		public String workOrderText;
		public String backVisitId;
		public String employeeName;
		public String issuedRegName;
		public int annexNumber;
		public String orgName;
		public String siteDescription;
		public String workEventType;
		public String disposePhotoId;
		public int localPhotoNumber;
		public String localPhotoId;
		public String dutyOrgType;
		public String workEventPointLatlon;
		public String workOrderId;
		public String satisfactionDegree;
		public String workEventPointExplain;
		public int workIsChange;
		public String checkDescription;
		public String workOrderCode;
		public String backOrgId;
		public String eventBackText;
		public String workOrderRemark;
		public String workEventSource;
		public String complaintTel;
		public String orgId;
		public String checkStatus;
		public String dealLimit;
		public String orgCode;
		public String complaintName;
		public String backVisitName;
		public String complaintEmail;
		public String localPhotoExplain;
		public String workEndTime;
		public String backOrgCode;
		public String dutyOrgName;
		public String workEventState;
		public String workIsNodus;
		public String dutyOrgId;
		public String employeeId;
		public String updateTime;
		public String workIsIssued;
		public String workBackExplain;
		public String backOrgName;
		public String sceneAnnex;
		public String workIsBack;
		public String workReportType;
		public String createTime;
		public String workOrderName;
		public String workDisposeRequest;
		public String issuedRegId;
		public String workBeginTime;
	}

	public static class OccupyImageBean {
		public String id;
		public String pid;
		public String type;
		public String path;
		public String operator;
		public String uploadTime;
		public String typeName;
		public String uploadTimeStr;
	}

	public static class GetNiticeBeanList {
		public GetNoticeList result;
	}

	public static class GetNoticeList {
		public List<NoticeBean> result;
	}

	public static class GetNoticeDetail {
		public NoticeBean result;
	}

	public static class SupplierList {
		public String count;
		public List<SupplierBean> supplierList;
	}

	public static class LeaseApplyList {
		public String count;
		public List<SupplierBean> purchaseList;
	}

	public static class InspectionList {
		public String count;
		public List<SupplierBean> inspectionList;
	}

	public static class RentLedgerList {
		public String count;
		public List<SupplierBean> rentList;
	}

	public static class ContractLedgerList {
		public String count;
		public List<SupplierBean> contractList;
	}

	public static class GetContractDetail {
		public ContractDetail contract;
	}

	public static class GetDeviceManageList {
		public List<DeviceManageBean> operatorList;
	}

	public static class GetDeviceLedgerList {
		public String count;
		public List<DeviceManageBean> mainshowList;
	}

	public static class GetSafetyProtectionList {
		public String count;
		public List<DeviceManageBean> safetyList;
	}

	public static class GetDeviceDispatchList {
		public String count;
		public List<DeviceManageBean> dispatchList;
	}

	public static class GetDeviceUseRecordList {
		public String count;
		public List<DeviceManageBean> data;
	}

	public static class GetDeviceVerifyList {
		public String count;
		public List<DeviceManageBean> checkingList;
	}

	public static class GetInspectionRecordList {
		public String count;
		public List<DeviceManageBean> everyinspectList;
	}

	public static class GetUtilizationRateList {
		public String count;
		public List<DeviceManageBean> intactList;
	}

	public static class GetMonthlyRreportList {
		public String count;
		public List<DeviceManageBean> emphasisList;
	}

	public static class GetInsuranceManageList {
		public String count;
		public List<DeviceManageBean> insureList;
	}

	public static class GetMaintenancePlanList {
		public String count;
		public List<DeviceMaintainBean> maintainplanList;
	}

	public static class GetMaintenanceOrderList {
		public String count;
		public List<DeviceMaintainBean> overhaulList;
	}

	public static class GetMaintenanceRecordList {
		public String count;
		public List<DeviceMaintainBean> maintainrecordList;
	}

	public static class GetAccidentReportRecordList {
		public String count;
		public List<DeviceMaintainBean> accidentList;
	}

	public static class GetDeviceScrapBeanList {
		public String count;
		public List<DeviceScrapBean> scrapList;
	}

	public static class GetInLedgerRecordList {
		public String count;
		public List<DeviceSpareOilBean> enterstockList;
	}

	public static class GetOutLedgerRecordList {
		public String count;
		public List<DeviceSpareOilBean> outstockList;
	}

	public static class GetPetrolManageList {
		public String count;
		public List<DeviceSpareOilBean> refuelList;
	}

	public static class GetMaterialApplyList {
		public String count;
		public List<DeviceSpareOilBean> drawapplyList;
	}

	public static class GetMaterialPMList {
		public String count;
		public List<MaterielDemandBean> projectList;
	}

	public static class GetMaterialDemanPlanist {
		public String count;
		public List<MaterielDemandBean> planList;
	}

	public static class GetProjectInfoist {
		public String count;
		public List<ProjectInfo> projectResourceList;
	}

	public static class GetReProjectInfoist {
		public String count;
		public List<ProjectInfo> demandFormList;
	}

	public static class GetProjectRecodeist {
		public String count;
		public List<ProjectInfo> verifyList;
	}

	public static class GetReProjectRecodeist {
		public String count;
		public List<ProjectInfo> demandplanVerifyList;
	}

	public static class GetMaterialDemanOrderManageList {
		public String count;
		public List<PurchaseOrderManageBean> demandformList;
	}

	public static class GetPurchaseOrderManageList {
		public String count;
		public List<PurchaseOrderManageBean> purchaseList;
	}

	public static class GetMaterialSupplierManageList {
		public String count;
		public List<PurchaseOrderManageBean> supplierBList;
	}

	public static class GetMaterialDemanOrderDetailList {
		public String count;
		public List<PurchaseOrderManageBean> demandViewList;
	}

	public static class GetMaterialDemanOrderDetail {
		public PurchaseOrderManageBean demandPlanInfo;
	}

	public static class GetPurchaseOrderDetailList {
		public String count;
		public List<PurchaseOrderManageBean> deliverFormList;
	}

	public static class GetMaterialSupplierDetailList {
		public String count;
		public List<PurchaseOrderManageBean> verifyList;
	}

	public static class GetMaterialProjectList {
		public String count;
		public List<PurchaseOrderManageBean> projectList;
	}

	public static class GetMaterialSupplierPList {
		public String count;
		public List<PurchaseOrderManageBean> supplierList;
	}

	public static class GetPurchaseformList {
		public String count;
		public List<PurchaseOrderManageBean> purchaseformList;
	}

	public static class GetDeliverformList {
		public String count;
		public List<PurchaseOrderManageBean> deliverformList;
	}

	public static class GetMaterialStockList {
		public String count;
		public List<PurchaseOrderManageBean> stockList;
	}

	public static class GetMaterialStorageInList {
		public String count;
		public List<PurchaseOrderManageBean> storageputinList;
	}

	public static class GetMaterialStorageOutList {
		public String count;
		public List<PurchaseOrderManageBean> storageputoutList;
	}

	public static class GetDeliverformVerifyList {
		public String count;
		public List<PurchaseOrderManageBean> deliverformVerify;
	}

	public static class GetMaterialStockVerifyList {
		public String count;
		public List<PurchaseOrderManageBean> stockVerify;
	}

	public static class GetMaterialStorageInVerifyList {
		public String count;
		public List<PurchaseOrderManageBean> storageputinVerify;
	}

	public static class GetMaterialStorageOutVerifyList {
		public String count;
		public List<PurchaseOrderManageBean> storageputoutVerify;
	}

	public static class GetOccupyDelaySearchList {
		public String count;
		public List<OccupySearchBean> delayList;
	}

	public static class GetOccupyInspectSearchList {
		public String count;
		public List<OccupySearchBean> inspectList;
	}

	public static class GetOccupyStateSearchList {
		public String count;
		public List<OccupySearchBean> logList;
	}

	public static class GetOccupyApplyArchiveList {
		public String count;
		public List<OccupyArchiveBean> applicantList;
	}

	public static class GetOccupyBuilderArchiveList {
		public String count;
		public List<OccupyArchiveBean> builderList;
	}

	public static class GetOccupyManageList {
		public String ztotalSum;
		public String totalSum;
		public String count;
		public String wtotalSum;
		public List<OccupyManageBean> infoList;
	}

	public static class GetOccupyManageDetail {
		public OccupyManageBean occupyInfo;
	}

	public static class GetOccupyList {
		public List<OccupyAreaBean> occupyList;
	}

	public static class GetOccupyStartAreaList {
		public String ztotalSum;
		public String totalSum;
		public String count;
		public String wtotalSum;
		public String wgiveSum;
		public List<OccupyManageBean> sumproportionList;
	}

	public static class GetOccupyGiveAreaList {
		public String ztotalSum;
		public String totalSum;
		public String count;
		public String wtotalSum;
		public String wgiveSum;
		public List<OccupyManageBean> giveproportionList;
	}

	public static class GetWorkOrderBeanList {
		public int pageCount;
		public String resDesc;
		public String resCode;
		public int pageSize;
		public int rowCount;
		public List<WorkOrderBean> result;
	}

	public static class DisposeBean {
		public String employeeName;
		public String orgName;
		public int disposeAnnex;
		public int disposeAfterDtate;
		public String acceptTime;
		public String operaterName;
		public String acceptName;
		public String processResult;
		public String employeeId;
		public String acceptId;
		public String disposeId;
		public String orgId;
		public String operaterTime;
		public String disposeText;
		public String disposeTime;
		public String orgCode;
		public String operaterId;
		public int disposeBeforeState;
		public String workOrderId;
		public String disposeType;
	}

	public static class AttBean {
		public int mediaSize;
		public String bussinessId;
		public String bussinessType;
		public String mediaUrl;
		public String mediaTime;
		public String mediaType;
		public String mediaId;
		public String mediaDetail;
		public String mediaName;
		public String mediaPath;
	}

	public static class LocationConfig {
		public String locateCycleType;
		public String locateCycleNumber;
		public String locateFrequencyDay;
		public String locateBegin;
		public String locateEnd;
		public String locateSpaceUnit;
		public String locateSpace;
		public String locateEffectBegin;
		public String locateEffectEnd;
		public String locateIsEnd;

		@Override
		public String toString() {
			String toString = locateCycleType + ":" + locateCycleNumber + ":" + locateFrequencyDay + ":" + locateFrequencyDay + ":" + locateIsEnd;
			return toString;
		}
	}

	public static class WorkOrder {
		public List<DisposeBean> disposes;
		public List<AttBean> files;
		public WorkOrderBean order;
	}

	public static class GetWorkOrderDetail {
		public String resDesc;
		public String resCode;
		public WorkOrder result;
	}

	public static class LocationConfigResult {
		public String resDesc;
		public String resCode;
		public LocationConfig result;
	}

	public static class GetOccupyInfoList {
		public String count;
		public List<OccupyManageBean> occupyInfoList;
	}

	public static class GetOccupyImageList {
		public List<OccupyImageBean> picView;
	}

	public static class GetOccupyStatusList {
		public String count;
		public List<OccupyManageBean> occupyStatusList;
	}

	public static class LoginDataResult {
		public LoginData data;
	}

	public static class DeviceScrapVerify {
		// 审核人
		public String verifier;
		// 审核人角色
		public String verifierRole;
		// 审核时间
		public String verifytime;
		// 审核结果
		public String verifyresult;
		// 审核意见
		public String suggestion;
		// 驳回原因
		public String rebutreason;
	}

	public static class DeviceScrapVerifyList {
		public List<DeviceScrapVerify> verifyList;
	}
}
