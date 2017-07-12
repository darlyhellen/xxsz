package com.xiangxun.xacity.bean;

import java.io.Serializable;
import java.util.List;

public class LoginData {
	private Login login;
	private List<Menu> menu;
	private List<UserData> user;

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public List<Menu> getMenu() {
		return menu;
	}

	public void setMenu(List<Menu> menu) {
		this.menu = menu;
	}

	public List<UserData> getUser() {
		return user;
	}

	public void setUser(List<UserData> user) {
		this.user = user;
	}

	public class Login {
		private String res;

		public String getRes() {
			return res;
		}

		public void setRes(String res) {
			this.res = res;
		}
	}

	public class Menu implements Serializable {
		private static final long serialVersionUID = 4529107390381386282L;
		private String id;
		private String name;
		private String url;
		private List<ChildrenRoot> children;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public List<ChildrenRoot> getChildren() {
			return children;
		}

		public void setChildren(List<ChildrenRoot> children) {
			this.children = children;
		}

	}

	public class ChildrenRoot implements Serializable {
		private static final long serialVersionUID = -5329369543132758744L;
		private String id;
		private String name;
		private List<Children> children;

		public List<Children> getChildren() {
			return children;
		}

		public void setChildren(List<Children> children) {
			this.children = children;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public class Children implements Serializable {
		private static final long serialVersionUID = 1656738930593067327L;
		private String id;
		private String name;
		private List<Roles> roles;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<Roles> getRoles() {
			return roles;
		}

		public void setRoles(List<Roles> roles) {
			this.roles = roles;
		}
	}

	public class Roles implements Serializable {
		private static final long serialVersionUID = -3405789993526529830L;
		private String id;
		private String name;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public class UserData implements Serializable {
		private static final long serialVersionUID = 534887745347912919L;
		private String id;
		private String account;
		private String pwd;
		private String name;
		private String disabled;
		private String deptid;
		private String dutyorgcode;
		private String mobile;
		private String mobileRolelds;
		private String mobileRoles;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public String getPwd() {
			return pwd;
		}

		public void setPwd(String pwd) {
			this.pwd = pwd;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDisabled() {
			return disabled;
		}

		public void setDisabled(String disabled) {
			this.disabled = disabled;
		}

		public String getDeptid() {
			return deptid;
		}

		public void setDeptid(String deptid) {
			this.deptid = deptid;
		}

		public String getDutyorgcode() {
			return dutyorgcode;
		}

		public void setDutyorgcode(String dutyorgcode) {
			this.dutyorgcode = dutyorgcode;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getMobileRolelds() {
			return mobileRolelds;
		}

		public void setMobileRolelds(String mobileRolelds) {
			this.mobileRolelds = mobileRolelds;
		}

		public String getMobileRoles() {
			return mobileRoles;
		}

		public void setMobileRoles(String mobileRoles) {
			this.mobileRoles = mobileRoles;
		}

	}

}
