/**
 * @Title: RechargeAlipayPage.java
 * @Package com.android.splus.sdk.ui.recharge
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-3-11 下午1:37:06
 * @version V1.0
 */

package com.android.splus.sdk.ui.recharge;

import com.android.splus.sdk.adapter.MoneyGridViewAdapter;
import com.android.splus.sdk.alipay.AlixId;
import com.android.splus.sdk.alipay.MobileSecurePayHelper;
import com.android.splus.sdk.alipay.MobileSecurePayer;
import com.android.splus.sdk.manager.ExitAppUtils;
import com.android.splus.sdk.model.RatioModel;
import com.android.splus.sdk.model.RechargeModel;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.parse.LoginParser;
import com.android.splus.sdk.ui.RechargeActivity;
import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.date.DateUtil;
import com.android.splus.sdk.utils.file.AppUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil.DataCallback;
import com.android.splus.sdk.utils.http.RequestModel;
import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.md5.MD5Util;
import com.android.splus.sdk.utils.phone.Phoneuitl;
import com.android.splus.sdk.utils.progressDialog.ProgressDialogUtil;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.utils.toast.ToastUtil;
import com.android.splus.sdk.widget.CustomGridView;
import com.android.splus.sdk.widget.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName: RechargeAlipayPage
 * @author xiaoming.yuan
 * @date 2014-3-11 下午1:37:06
 */

public class RechargeAlipayPage extends LinearLayout {
    private static final String TAG = "RechargeAlipayPage";

    private Activity mActivity;

    private TextView recharge_money_tips;

    private TextView recharge_money_ratio_tv;

    private Button recharge_comfirm_btn;

    private EditText recharge_money_custom_et;

    private CustomGridView recharge_money_gridview_select;

    private MoneyGridViewAdapter mMoneyGridViewAdapter;

    private static final int[] ALIPAY_MONEY = {
        10, 20, 30, 50, 100, 200, 300, 500, 1000, 2000, 3000, 5000
    };

    private float mRenminbi = 0; // 人民币

    private String mCoin_name = "金币";

    private int mRatio = 10;

    private int mMoneyIndex = 0;

    private String mPayway;

    private UserModel mUserModel;

    private Integer mGameid;

    private String mPartner;

    private String mReferer;

    private String mRoleName;

    private String mServerName;

    private String mOutOrderid;

    private String mPext;

    private String mAppKey;

    private String mDeviceno;

    protected CustomProgressDialog mProgressDialog;

    private int iconID = android.R.drawable.ic_dialog_info;

    public RechargeAlipayPage(UserModel userModel, Activity activity, String deviceno,
            String appKey, Integer gamid, String partner, String referer, String roleName,
            String serverName, String outOrderid, String pext) {
        super(activity);
        this.mUserModel = userModel;
        this.mActivity = activity;
        this.mDeviceno = deviceno;
        this.mAppKey = appKey;
        this.mGameid = gamid;
        this.mPartner = partner;
        this.mReferer = referer;
        this.mRoleName = roleName;
        this.mServerName = serverName;
        this.mOutOrderid = outOrderid;
        this.mPext = pext;
        inflate(activity,
                ResourceUtil.getLayoutId(activity, KR.layout.splus_recharge_alipay_layout), this);
        findViews();
        initViews();
        setlistener();
        processLogic();

    }

    /**
     * @Title: findViews(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午1:42:16 void 返回类型
     */

    private void findViews() {
        recharge_money_tips = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_tips));
        recharge_money_custom_et = (EditText) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_custom_et));
        recharge_comfirm_btn = (Button) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_comfirm_btn));
        recharge_comfirm_btn.setText(KR.string.splus_recharge_comfirm_tips);
        recharge_money_ratio_tv = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_ratio_tv));
        recharge_money_gridview_select = (CustomGridView) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_recharge_money_gridview_select));

        recharge_money_tips.setText(KR.string.splus_recharge_select_head_tips);
        recharge_money_custom_et.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(6)
        });
        recharge_money_custom_et.setHint(KR.string.splus_recharge_custom_et_tips);
        recharge_money_ratio_tv.setHint(KR.string.splus_recharge_ratio_tv_tips);
    }

    /**
     * @Title: initViews(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午1:42:09 void 返回类型
     */

    private void initViews() {

        int orientation = Phoneuitl.getOrientation(mActivity);
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            recharge_money_gridview_select.setNumColumns(6);
            recharge_money_gridview_select.setColumnWidth(40);
            recharge_money_gridview_select.setGravity(Gravity.CENTER);
            recharge_money_gridview_select.setLayoutParams(new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER));
            recharge_money_gridview_select.setVerticalSpacing(30);
            recharge_money_gridview_select.setHorizontalSpacing(20);

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            recharge_money_gridview_select.setNumColumns(3);
            recharge_money_gridview_select.setLayoutParams(new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER));
            recharge_money_gridview_select.setPadding(5, 50, 5, 50);
            recharge_money_gridview_select.setVerticalSpacing(40);
            recharge_money_gridview_select.setHorizontalSpacing(20);

        }
        recharge_money_gridview_select.setGravity(Gravity.CENTER_HORIZONTAL);
        recharge_money_gridview_select.setSelector(android.R.color.transparent);
        mMoneyGridViewAdapter = new MoneyGridViewAdapter(ALIPAY_MONEY, mActivity);
        recharge_money_gridview_select.setAdapter(mMoneyGridViewAdapter);
        mRenminbi = mMoneyGridViewAdapter.getMoneyArray()[0];

    }

    /**
     * @Title: setlistener(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午1:42:12 void 返回类型
     */

    private void setlistener() {

        recharge_money_gridview_select.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMoneyIndex = position;
                mMoneyGridViewAdapter.setMoneyIndex(position);
                mRenminbi = mMoneyGridViewAdapter.getMoneyArray()[position];
                ToastUtil.showToast(mActivity, mRenminbi + "YUAN");

            }
        });

        recharge_comfirm_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                payQuest();
            }
        });
        recharge_money_custom_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 清除GIRDVIEW中的选择
                if (mMoneyIndex != -1) {
                    mMoneyIndex = -1;
                    recharge_money_ratio_tv.setText("");
                    mMoneyGridViewAdapter.setMoneyIndex(mMoneyIndex);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString().trim();
                if (temp.contains(".")) {
                    if (temp.length() == 1) {
                        recharge_money_custom_et.setText(null);
                    }
                    int posDot = temp.indexOf(".");
                    if (posDot <= 0) {
                        return;
                    }

                    if (temp.length() - posDot - 1 > 2) {
                        s.delete(posDot + 3, posDot + 4);
                    }
                }
                String str = s.toString().trim();
                if (str.length() > 0) {
                    if (!TextUtils.isEmpty(str)
                            && !str.subSequence(str.length() - 1, str.length()).equals(".")) {
                        mRenminbi = Float.valueOf(str);
                        setGetMoneyTextPure(mRenminbi);
                    }
                }
            }
        });
    }

    /**
     * @Title: processLogic(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午9:10:35 void 返回类型
     */

    private void processLogic() {
        getRatio();
    }

    /**
     * @Title: getRatio(请求兑换比率和显示名称)
     * @author xiaoming.yuan
     * @data 2013-10-2 下午11:22:24 void 返回类型
     */
    private void getRatio() {
        long time = DateUtil.getUnixTime();
        String keyString = mGameid + mPayway + time + mAppKey;
        RatioModel mRatioModel = new RatioModel(mGameid, mPayway, time,
                MD5Util.getMd5toLowerCase(keyString));
        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.RATIO_URL,
                mActivity, mRatioModel, new LoginParser()), onRatioebyCardCallBack);
    }

    private DataCallback<JSONObject> onRatioebyCardCallBack = new DataCallback<JSONObject>() {

        @Override
        public void callbackSuccess(JSONObject paramObject) {
            if (paramObject != null && paramObject.optInt("code") == 1) {
                mCoin_name = paramObject.optJSONObject("data").optString("coin_name");
                mRatio = paramObject.optJSONObject("data").optInt("ratio");
                setGetMoneyTextPure(mRenminbi);
            } else {
                String msg = paramObject.optJSONObject("data").optString("msg");
                LogHelper.d(TAG, msg);
            }
        }

        @Override
        public void callbackError(String error) {
            LogHelper.d(TAG, error);
        }

    };

    /**
     * 根据元宝数，显示相应提示（纯色文字）
     *
     * @param money
     */
    private void setGetMoneyTextPure(Float money) {

        if (money == 0) {
            recharge_money_ratio_tv.setText("");
        } else {
            recharge_money_ratio_tv.setText(mRenminbi + "元可兑换" + mRenminbi * mRatio + mCoin_name);
        }

    }

    private void payQuest() {
        // check to see if the MobileSecurePay is already installed.
        // 检测安全支付服务是否安装
        MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(mActivity);
        boolean isMobile_spExist = mspHelper.detectMobile_sp();
        if (!isMobile_spExist) {
            return;
        }
        long time = DateUtil.getUnixTime();
        String keyString = mGameid + mServerName + mDeviceno + mReferer + mPartner
                + mUserModel.getUid() + mRenminbi + mPayway + time + mAppKey;
        RechargeModel rechargeModel = new RechargeModel(mGameid, mServerName, mDeviceno, mPartner,
                mReferer, mUserModel.getUid(),  mRenminbi,mPayway, mRoleName, time,
                mUserModel.getPassport(), mOutOrderid, mPext, MD5Util.getMd5toLowerCase(keyString));
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            showProgressDialog();
        }
        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.HTMLWAPPAY_URL,
                mActivity, rechargeModel, new LoginParser()), onRechargeCallBack);

    }

    private DataCallback<JSONObject> onRechargeCallBack = new DataCallback<JSONObject>() {

        @Override
        public void callbackSuccess(JSONObject paramObject) {

            closeProgressDialog();
            if (paramObject != null
                    && (paramObject.optInt("code") == 24 || paramObject.optInt("code") == 1)) {
                String orderid = paramObject.optJSONObject("data").optString("orderid");
                int time = paramObject.optJSONObject("data").optInt("time");
                String orderinfo = paramObject.optJSONObject("data").optString("orderinfo");
                String sign = paramObject.optJSONObject("data").optString("sign");
                if (sign.equals(MD5Util.getMd5toLowerCase(orderid + time + mAppKey))) {
                    // 支付
                    try {
                        MobileSecurePayer msp = new MobileSecurePayer();
                        boolean bRet = msp.pay(orderinfo, mHandler, AlixId.RQF_PAY, mActivity);
                        if (bRet) {
                            showProgressDialog();
                        }
                    } catch (Exception e) {
                        ToastUtil.showToast(mActivity, "支付失败");
                    }
                } else {
                    LogHelper.d(TAG, "签名错误!!！");
                }

            } else {
                String msg = paramObject.optString("msg");
                LogHelper.d(TAG, msg);
            }

        }

        @Override
        public void callbackError(String error) {
            closeProgressDialog();
            LogHelper.d(TAG, error);
        }

    };

    // 这里接收支付结果，支付宝手机端同步通知
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            try {
                String strRet = (String) msg.obj;
                LogHelper.e(TAG, strRet); // strRet范例：resultStatus={9000};memo={};result={partner="2088201564809153"&seller="2088201564809153"&out_trade_no="050917083121576"&subject="123456"&body="2010新款NIKE 耐克902第三代板鞋 耐克男女鞋 386201 白红"&total_fee="0.01"&notify_url="http://notify.java.jpxx.org/index.jsp"&success="true"&sign_type="RSA"&sign="d9pdkfy75G997NiPS1yZoYNCmtRbdOP0usZIMmKCCMVqbSG1P44ohvqMYRztrB6ErgEecIiPj9UldV5nSy9CrBVjV54rBGoT6VSUF/ufjJeCSuL510JwaRpHtRPeURS1LXnSrbwtdkDOktXubQKnIMg2W0PreT1mRXDSaeEECzc="}
                switch (msg.what) {
                    case AlixId.RQF_PAY: {
                        closeProgressDialog();
                        LogHelper.d(TAG, strRet);
                        // 处理交易结果
                        try {
                            // 获取交易状态码，具体状态代码请参看文档
                            String tradeStatus = "resultStatus={";
                            int imemoStart = strRet.indexOf("resultStatus=");
                            imemoStart += tradeStatus.length();
                            int imemoEnd = strRet.indexOf("};memo=");
                            tradeStatus = strRet.substring(imemoStart, imemoEnd);
                            // 判断交易状态码，只有9000表示交易成功
                            if (tradeStatus.equals("9000")) {
                                ProgressDialogUtil.showInfoDialog(mActivity, "提示",
                                        "支付成功,预计到账时间1-10分钟", iconID,
                                        new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ExitAppUtils.getInstance().exit();
                                        //Todo
                                    }
                                }, null, false);
                            } else {
                                ProgressDialogUtil.showInfoDialog(mActivity, "提示", "支付失败",
                                        iconID, null, null, false);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                            LogHelper.d(TAG, strRet);

                        }
                    }
                    break;
                }

                super.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    protected void showProgressDialog() {

        if (this.mProgressDialog == null) {
            this.mProgressDialog = new CustomProgressDialog(mActivity);
        }
        // 设置ProgressDialog 的进度条style
        this.mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // this.mProgressDialog.setTitle("提示");
        this.mProgressDialog.setMessage("加载中...");
        // 设置ProgressDialog 的进度条是否不明确
        this.mProgressDialog.setIndeterminate(false);
        // 设置ProgressDialog 的进度条是否不明确
        this.mProgressDialog.setCancelable(false);
        this.mProgressDialog.show();
    }

    protected void closeProgressDialog() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing())
            this.mProgressDialog.dismiss();
    }

}
