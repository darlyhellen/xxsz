package com.xiangxun.xacity.constant;

import java.util.HashMap;
import java.util.Map;

public class ConstantType {
	/*********************** 性别 ****************************/
	private static String[] sex = null;
	/*********************** 文化程度 ****************************/
	private static String[] degree = null;
	/*********************** 燃油类型 ****************************/
	private static String[] oilType = null;
	/*********************** 资金来源 ****************************/
	private static String[] sourceMoney = null;
	/*********************** 环保标志 ****************************/
	private static String[] enviTyoe = null;
	/*********************** 财务对应 ****************************/
	private static String[] financeTyoe = null;
	/*********************** 财务对应 ****************************/
	private static Map<String, Integer> projectType = null;
	private static Map<String, Integer> projectSex = null;
	private static Map<String, Integer> projectDegree = null;
	private static Map<String, Integer> projectOilType = null;
	private static Map<String, Integer> projectEnviType = null;
	private static Map<String, Integer> projectSourceMoney = null;
	private static Map<String, Integer> projectFinanceType = null;

	public static void init() {
		sex = new String[] { "男", "女" };
		degree = new String[] { "小学", "初中", "中专", "高中", "大专", "本科", "研究生", "博士" };
		oilType = new String[] { "汽油", "柴油", "天热气" };
		sourceMoney = new String[] { "单位自购", "三产资金", "上级调拨", "政府采购" };
		enviTyoe = new String[] { "无标", "绿标", "黄标" };
		financeTyoe = new String[] { "有帐有物", "有帐无物", "有物无帐" };
		projectType = new HashMap<String, Integer>();
		projectSex = new HashMap<String, Integer>();
		projectDegree = new HashMap<String, Integer>();
		projectOilType = new HashMap<String, Integer>();
		projectEnviType = new HashMap<String, Integer>();
		projectSourceMoney = new HashMap<String, Integer>();
		projectFinanceType = new HashMap<String, Integer>();
		projectType.put("挖掘", 1);
		projectType.put("占用", 2);
		projectType.put("街具", 3);
		projectSex.put("男", 1);
		projectSex.put("女", 2);

		projectDegree.put("小学", 1);
		projectDegree.put("初中", 2);
		projectDegree.put("中专", 3);
		projectDegree.put("高中", 4);
		projectDegree.put("大专", 5);
		projectDegree.put("本科", 6);
		projectDegree.put("研究生", 7);
		projectDegree.put("博士", 8);

		projectOilType.put("汽油", 1);
		projectOilType.put("柴油", 2);
		projectOilType.put("天热气", 3);

		projectEnviType.put("无标", 1);
		projectEnviType.put("绿标", 2);
		projectEnviType.put("黄标", 3);

		projectFinanceType.put("有帐有物", 1);
		projectFinanceType.put("有帐无物", 2);
		projectFinanceType.put("有物无帐", 3);

		projectSourceMoney.put("单位自购", 1);
		projectSourceMoney.put("三产资金", 2);
		projectSourceMoney.put("上级调拨", 3);
		projectSourceMoney.put("政府采购", 4);
	}

	public static String[] getSex() {
		if (sex == null) {
			init();
		}
		return sex;
	}

	public static String[] getDegree() {
		if (degree == null) {
			init();
		}
		return degree;
	}

	public static String[] getOilType() {
		if (oilType == null) {
			init();
		}
		return oilType;
	}

	public static String[] getSourceMoney() {
		if (sourceMoney == null) {
			init();
		}
		return sourceMoney;
	}

	public static String[] getEnviTyoe() {
		if (enviTyoe == null) {
			init();
		}
		return enviTyoe;
	}

	public static String[] getFinanceTyoe() {
		if (financeTyoe == null) {
			init();
		}
		return financeTyoe;
	}

	public static int getProjectTyoe(String item) {
		if (projectType == null) {
			init();
		}
		return projectType.get(item);
	}

	public static int getProjectSex(String item) {
		if (projectSex == null) {
			init();
		}
		return projectSex.get(item);
	}

	public static int getProjectDegree(String item) {
		if (projectDegree == null) {
			init();
		}
		return projectDegree.get(item);
	}

	public static int getProjectOilType(String item) {
		if (projectOilType == null) {
			init();
		}
		return projectOilType.get(item);
	}

	public static int getProjectEnviTyoe(String item) {
		if (projectEnviType == null) {
			init();
		}
		return projectEnviType.get(item);
	}

	public static int getProjectSourceMoney(String item) {
		if (projectSourceMoney == null) {
			init();
		}
		return projectSourceMoney.get(item);
	}

	public static int getProjectFinanceType(String item) {
		if (projectFinanceType == null) {
			init();
		}
		return projectFinanceType.get(item);
	}

}
