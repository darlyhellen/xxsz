package com.xiangxun.xacity.ui.device.detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.ContractDetail;
import com.xiangxun.xacity.bean.ResponseResultBeans.GetContractDetail;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.common.DcHttpClient;
import com.xiangxun.xacity.request.Api;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.volley.toolbox.NetworkImageView;

/**
 * @package: com.xiangxun.xacity.ui.device.detail
 * @ClassName: ContractLedgerDetailActivity.java
 * @Description: 合同台賬詳情頁面
 * @author: HanGJ
 * @date: 2016-2-20 下午4:48:21
 */
public class ContractLedgerDetailActivity extends BaseActivity {
	private LinearLayout mLlLoading = null;
	private TitleView titleView;
	private NetworkImageView mFile;
	private TextView tvcontractName;
	private TextView tvcontractDate;
	private TextView tvcontractOrg;
	private TextView tvMan;
	private TextView tvPurchseOrg;
	private TextView tvOrg;
	private TextView tvDateTime;
	private TextView tvContent;
	private TextView tv_contract_manoy;
	private TextView tv_contract_type;
	private TextView tv_contract_progress;
	private String id;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mLlLoading.setVisibility(View.GONE);
			switch (msg.what) {
			case ConstantStatus.getDeviceContractLedgerListSuccess:
				GetContractDetail contractDetail = (GetContractDetail) msg.obj;
				if (contractDetail != null && contractDetail.contract != null) {
					setData(contractDetail.contract);
				}
				break;

			case ConstantStatus.getDeviceContractLedgerListFailed:
				break;
			case ConstantStatus.NetWorkError:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contract_ledger_detail_layout);
		initView();
		initData();
		initListener();
	}

	protected void setData(ContractDetail contract) {
		tvcontractName.setText(Tools.isEmpty(contract.name));
		tvcontractDate.setText(Tools.isEmpty(contract.dateTime));
		tvcontractOrg.setText(Tools.isEmpty(contract.purchaseDeparment));
		tvMan.setText(Tools.isEmpty(contract.operator));
		tvPurchseOrg.setText(Tools.isEmpty(contract.orgId));
		tvOrg.setText(Tools.isEmpty(contract.cooperationDepartment));
		tvDateTime.setText(Tools.isEmpty(contract.registerDate));
		tvContent.setText(Tools.isEmpty(contract.content));
		tv_contract_manoy.setText(Tools.isEmpty(contract.money));
		tv_contract_type.setText(Tools.isEmpty(contract.condition));
		tv_contract_progress.setText(Tools.isEmpty(contract.schedule));
		if (contract.contractFile != null && contract.contractFile.length() > 0) {
			DcHttpClient.getInstance().getImageForNIView(Api.urlImageContract + contract.contractFile, mFile, R.drawable.view_pager_default);
		} else {
			mFile.setBackgroundResource(R.drawable.view_pager_default);
		}
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mLlLoading = (LinearLayout) findViewById(R.id.ll_loading_layout);
		mFile = (NetworkImageView) findViewById(R.id.iv_file);
		tvcontractName = (TextView) findViewById(R.id.tv_contract_name);
		tvcontractDate = (TextView) findViewById(R.id.tv_contract_date);
		tvcontractOrg = (TextView) findViewById(R.id.tv_contract_org);
		tvMan = (TextView) findViewById(R.id.tv_contract_man);
		tvPurchseOrg = (TextView) findViewById(R.id.tv_purchase_org);
		tvOrg = (TextView) findViewById(R.id.tv_other_org);
		tvDateTime = (TextView) findViewById(R.id.tv_date_time);
		tvContent = (TextView) findViewById(R.id.tv_contract_content);
		tv_contract_manoy = (TextView) findViewById(R.id.tv_contract_manoy);
		tv_contract_type = (TextView) findViewById(R.id.tv_contract_type);
		tv_contract_progress = (TextView) findViewById(R.id.tv_contract_progress);
	}

	@Override
	public void initData() {
		titleView.setTitle("合同台账--详情");
		id = getIntent().getStringExtra("id");
		RequestList();
	}

	private void RequestList() {
		DcNetWorkUtils.getDeviceContractDetail(this, handler, account, password, id);
	}

	@Override
	public void initListener() {
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

}
