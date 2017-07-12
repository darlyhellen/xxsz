package com.xiangxun.xacity.ui.materiel;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResultBeans.Type;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.utils.DateMonthPickDialogUtil;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.PublishSelectTypeDialog;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.materiel
 * @ClassName: PurchaseManageSearchActivity.java
 * @Description: 采购管理查询页面
 * @author: HanGJ
 * @date: 2016-1-26 上午11:17:18
 */
public class PurchaseManageSearchActivity extends BaseActivity implements OnClickListener {
	private PurchaseManageSearchActivity context;
	private TitleView titleView;
	private ViewFlipper mVF;
	private Button btn_search;
	/*************** 需求单管理 ****************/
	private TextView tv_materiel_category;
	private LinearLayout ll_materiel_category_click;
	private EditText et_plan_code, et_project_name, et_plan_month;
	/*************** 采购单管理 ****************/
	private TextView tv_materiel_category1;
	private LinearLayout ll_materiel_category_click1;
	private TextView tv_supplier_category;
	private LinearLayout ll_supplier_category_click;
	private EditText et_project_name1, et_plan_month1;
	/*************** 外部供应商管理 ****************/
	private EditText et_supplier_name, et_contact_address, et_person_charge;

	private int childAt;
	private LoadDialog loadDialog;
	private PublishSelectTypeDialog typeDialog;
	private PublishSelectTypeDialog selectTypeDialog;
	private boolean isFlag = false;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.getMaterialListSuccess:
				List<Type> types = (List<Type>) msg.obj;
				if (types != null && types.size() > 0) {
					if (childAt == 0) {
						typeDialog = new PublishSelectTypeDialog(context, types, tv_materiel_category, "请选择物资类别");
					} else {
						typeDialog = new PublishSelectTypeDialog(context, types, tv_materiel_category1, "请选择物资类别");
					}
				}
				break;
			case ConstantStatus.getSupplierListSuccess:
				List<Type> supplierTypes = (List<Type>) msg.obj;
				if (supplierTypes != null && supplierTypes.size() > 0) {
					selectTypeDialog = new PublishSelectTypeDialog(context, supplierTypes, tv_supplier_category, "请选择供应商");
				}
				break;
			case ConstantStatus.getMaterialListFailed:

				break;
			case ConstantStatus.NetWorkError:
				MsgToast.geToast().setMsg("网络异常,请稍后重试!");
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mareriel_purchase_search_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		btn_search = (Button) findViewById(R.id.btn_search);
		// 需求单管理
		tv_materiel_category = (TextView) findViewById(R.id.tv_materiel_category);
		ll_materiel_category_click = (LinearLayout) findViewById(R.id.ll_materiel_category_click);
		et_plan_code = (EditText) findViewById(R.id.et_plan_code);
		et_project_name = (EditText) findViewById(R.id.et_project_name);
		et_plan_month = (EditText) findViewById(R.id.et_plan_month);
		//
		tv_materiel_category1 = (TextView) findViewById(R.id.tv_materiel_category1);
		ll_materiel_category_click1 = (LinearLayout) findViewById(R.id.ll_materiel_category_click1);
		tv_supplier_category = (TextView) findViewById(R.id.tv_supplier_category);
		ll_supplier_category_click = (LinearLayout) findViewById(R.id.ll_supplier_category_click);
		et_project_name1 = (EditText) findViewById(R.id.et_project_name1);
		et_plan_month1 = (EditText) findViewById(R.id.et_plan_month1);
		//
		et_supplier_name = (EditText) findViewById(R.id.et_supplier_name);
		et_contact_address = (EditText) findViewById(R.id.et_contact_address);
		et_person_charge = (EditText) findViewById(R.id.et_person_charge);
		context = this;
	}

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "查询");
		mVF.setDisplayedChild(childAt);
		loadDialog = new LoadDialog(context);
		loadDialog.setTitle("正在加载数据,请稍后...");
		RequestList();
	}

	private void RequestList() {
		if (childAt == 0) {
			loadDialog.show();
			DcNetWorkUtils.getMaterielCategoryList(this, handler, account, password);
		} else if (childAt == 1) {
			loadDialog.show();
			DcNetWorkUtils.getMaterielCategoryList(this, handler, account, password);
			DcNetWorkUtils.getSupplierCategoryList(this, handler, account, password);
		}
	}

	@Override
	public void initListener() {
		et_plan_month.setOnClickListener(context);
		et_plan_month1.setOnClickListener(context);
		ll_materiel_category_click.setOnClickListener(context);
		ll_materiel_category_click1.setOnClickListener(context);
		ll_supplier_category_click.setOnClickListener(context);
		btn_search.setOnClickListener(context);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.et_plan_month:
			new DateMonthPickDialogUtil(this).dateTimePicKDialog(et_plan_month);
			break;
		case R.id.et_plan_month1:
			new DateMonthPickDialogUtil(this).dateTimePicKDialog(et_plan_month1);
			break;
		case R.id.ll_materiel_category_click:
			if (typeDialog != null) {
				typeDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.ll_materiel_category_click1:
			if (typeDialog != null) {
				typeDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.ll_supplier_category_click:
			if (selectTypeDialog != null) {
				selectTypeDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.btn_search:
			if (childAt == 0) {
				String type = tv_materiel_category.getText().toString().trim();
				String code = et_plan_code.getText().toString().trim();
				String projectName = et_project_name.getText().toString().trim();
				String month = et_plan_month.getText().toString().trim();
				if (type.length() > 0) {
					isFlag = true;
					Type typeClass = (Type) tv_materiel_category.getTag();
					type = typeClass.code;
				} else {
					type = "";
				}
				if (code.length() > 0) {
					isFlag = true;
				} else {
					code = "";
				}
				if (projectName.length() > 0) {
					isFlag = true;
				} else {
					projectName = "";
				}
				if (month.length() > 0) {
					isFlag = true;
				} else {
					month = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("type", type);
					intent.putExtra("code", code);
					intent.putExtra("projectName", projectName);
					intent.putExtra("month", month);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			} else if (childAt == 1) {
				String projectName = et_project_name1.getText().toString().trim();
				String month = et_plan_month1.getText().toString().trim();
				String type = tv_materiel_category1.getText().toString().trim();
				String supplier = tv_supplier_category.getText().toString().trim();
				if (type.length() > 0) {
					isFlag = true;
					Type typeClass = (Type) tv_materiel_category1.getTag();
					type = typeClass.code;
				} else {
					type = "";
				}
				if (supplier.length() > 0) {
					isFlag = true;
					Type typeClass = (Type) tv_supplier_category.getTag();
					supplier = typeClass.id;
				} else {
					supplier = "";
				}
				if (projectName.length() > 0) {
					isFlag = true;
				} else {
					projectName = "";
				}
				if (month.length() > 0) {
					isFlag = true;
				} else {
					month = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("type", type);
					intent.putExtra("supplier", supplier);
					intent.putExtra("projectName", projectName);
					intent.putExtra("month", month);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			} else {
				String name = et_supplier_name.getText().toString().trim();
				String commAddress = et_contact_address.getText().toString().trim();
				String principal = et_person_charge.getText().toString().trim();
				if (name.length() > 0) {
					isFlag = true;
				} else {
					name = "";
				}
				if (commAddress.length() > 0) {
					isFlag = true;
				} else {
					commAddress = "";
				}
				if (principal.length() > 0) {
					isFlag = true;
				} else {
					principal = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("name", name);
					intent.putExtra("commAddress", commAddress);
					intent.putExtra("principal", principal);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			}
			break;
		}
	}

}
