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

public class SupplierManageSearchActivity extends BaseActivity implements OnClickListener {
	private SupplierManageSearchActivity context;
	private TitleView titleView;
	private ViewFlipper mVF;
	private int childAt;
	private Button btn_search;

	private TextView materielView;
	private TextView supplierView;
	private TextView tv_materiel_category1;
	private LinearLayout ll_materiel_category_click1;
	private TextView tv_supplier_category;
	private LinearLayout ll_supplier_category_click;
	private EditText et_project_name1, et_plan_month1;
	//
	private EditText et_materiel_name;
	private TextView tv_materiel_category;
	private LinearLayout ll_materiel_category_click;
	private TextView tv_supplier_category1;
	private LinearLayout ll_supplier_category_click1;
	
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
					if(childAt > 1){
						materielView = 	tv_materiel_category;
					} else {
						materielView = tv_materiel_category1;
					}
					typeDialog = new PublishSelectTypeDialog(context, types, materielView, "请选择物资类别");
				}
				break;
			case ConstantStatus.getSupplierListSuccess:
				List<Type> supplierTypes = (List<Type>) msg.obj;
				if (supplierTypes != null && supplierTypes.size() > 0) {
					if(childAt > 1){
						supplierView = 	tv_supplier_category1;
					} else {
						supplierView = tv_supplier_category;
					}
					selectTypeDialog = new PublishSelectTypeDialog(context, supplierTypes, supplierView, "请选择供应商");
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
		setContentView(R.layout.mareriel_supplier_search_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		btn_search = (Button) findViewById(R.id.btn_search);
		//
		tv_materiel_category1 = (TextView) findViewById(R.id.tv_materiel_category1);
		ll_materiel_category_click1 = (LinearLayout) findViewById(R.id.ll_materiel_category_click1);
		tv_supplier_category = (TextView) findViewById(R.id.tv_supplier_category);
		ll_supplier_category_click = (LinearLayout) findViewById(R.id.ll_supplier_category_click);
		et_project_name1 = (EditText) findViewById(R.id.et_project_name1);
		et_plan_month1 = (EditText) findViewById(R.id.et_plan_month1);
		//
		et_materiel_name = (EditText) findViewById(R.id.et_materiel_name);
		tv_materiel_category = (TextView) findViewById(R.id.tv_materiel_category);
		tv_supplier_category1 = (TextView) findViewById(R.id.tv_supplier_category1);
		ll_materiel_category_click = (LinearLayout) findViewById(R.id.ll_materiel_category_click);
		ll_supplier_category_click1 = (LinearLayout) findViewById(R.id.ll_supplier_category_click1);
		context = this;
	}

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "查询");
		if (childAt > 1) {
			mVF.setDisplayedChild(1);
		} else {
			mVF.setDisplayedChild(0);
		}
		loadDialog = new LoadDialog(context);
		loadDialog.setTitle("正在加载数据,请稍后...");
		RequestList();
	}

	private void RequestList() {
		loadDialog.show();
		DcNetWorkUtils.getMaterielCategoryList(this, handler, account, password);
		DcNetWorkUtils.getSupplierCategoryList(this, handler, account, password);
	}

	@Override
	public void initListener() {
		et_plan_month1.setOnClickListener(context);
		ll_materiel_category_click1.setOnClickListener(context);
		ll_supplier_category_click.setOnClickListener(context);
		ll_materiel_category_click.setOnClickListener(context);
		ll_supplier_category_click1.setOnClickListener(context);
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
		case R.id.et_plan_month1:
			new DateMonthPickDialogUtil(this).dateTimePicKDialog(et_plan_month1);
			break;
		case R.id.ll_materiel_category_click1:
		case R.id.ll_materiel_category_click:
			if (typeDialog != null) {
				typeDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.ll_supplier_category_click:
		case R.id.ll_supplier_category_click1:
			if (selectTypeDialog != null) {
				selectTypeDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.btn_search:
			if (childAt <= 1) {
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
				String name = et_materiel_name.getText().toString().trim();
				String type = tv_materiel_category.getText().toString().trim();
				String supplier = tv_supplier_category1.getText().toString().trim();
				if (type.length() > 0) {
					isFlag = true;
					Type typeClass = (Type) tv_materiel_category.getTag();
					type = typeClass.code;
				} else {
					type = "";
				}
				if (supplier.length() > 0) {
					isFlag = true;
					Type typeClass = (Type) tv_supplier_category1.getTag();
					supplier = typeClass.id;
				} else {
					supplier = "";
				}
				if (name.length() > 0) {
					isFlag = true;
				} else {
					name = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("type", type);
					intent.putExtra("supplier", supplier);
					intent.putExtra("name", name);
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
