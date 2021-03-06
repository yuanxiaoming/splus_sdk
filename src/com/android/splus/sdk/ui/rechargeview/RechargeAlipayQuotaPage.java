/**
 * @Title: RechargeAlipayPage.java
 * @Package com.android.splus.sdk.ui.recharge
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-3-11 下午1:37:06
 * @version V1.0
 */

package com.android.splus.sdk.ui.rechargeview;

import com.android.splus.sdk.alipay.AlixId;
import com.android.splus.sdk.alipay.MobileSecurePayHelper;
import com.android.splus.sdk.alipay.MobileSecurePayer;
import com.android.splus.sdk.model.RatioModel;
import com.android.splus.sdk.model.RechargeModel;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.parse.LoginParser;
import com.android.splus.sdk.ui.RechargeResultActivity;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.date.DateUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil.DataCallback;
import com.android.splus.sdk.utils.http.RequestModel;
import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.md5.MD5Util;
import com.android.splus.sdk.utils.progressDialog.ProgressDialogUtil;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.utils.toast.ToastUtil;
import com.android.splus.sdk.widget.CustomProgressDialog;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName: RechargeAlipayPage
 * @author xiaoming.yuan
 * @date 2014-3-11 下午1:37:06
 */

public class RechargeAlipayQuotaPage extends LinearLayout {
    private static final String TAG = "RechargeAlipayPage";

    private Activity mActivity;

    private TextView recharge_money_tips;

    private TextView recharge_money_ratio_tv;

    private Button recharge_comfirm_btn;

    private float mRenminbi = 0; // 人民币

    private String mCoin_name = "金币";

    private int mRatio = 10;

    private String mPayway;

    private UserModel mUserModel;

    private Integer mGameid;

    private String mPartner;

    private String mReferer;

    private Integer mServerId;

    private String mServerName;

    private Integer mRoleId;

    private String mRoleName;

    private String mOutOrderid;

    private String mPext;

    private String mAppKey;

    private String mDeviceno;

    private Integer mType;

    protected CustomProgressDialog mProgressDialog;

    private AlipayQuotaHtmlClick mAlipayHtmlClick;

    public RechargeAlipayQuotaPage(UserModel userModel, Activity activity, String deviceno, String appKey, Integer gameid, String partner, String referer, Integer serverId, Integer roleId, String roleName, String serverName, String outOrderid, String pext, Integer type,
                    String payway, Float money) {
        super(activity);
        this.mUserModel = userModel;
        this.mActivity = activity;
        this.mDeviceno = deviceno;
        this.mAppKey = appKey;
        this.mGameid = gameid;
        this.mPartner = partner;
        this.mReferer = referer;
        this.mRoleId = roleId;
        this.mServerId = serverId;
        this.mRoleName = roleName;
        this.mServerName = serverName;
        this.mOutOrderid = outOrderid;
        this.mPext = pext;
        this.mType = type;
        this.mPayway = payway;
        this.mRenminbi = money;
        inflate(activity, ResourceUtil.getLayoutId(activity, KR.layout.splus_recharge_alipay_quota_layout), this);
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
        recharge_money_tips = (TextView) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_recharge_money_tips));

        recharge_comfirm_btn = (Button) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_recharge_money_comfirm_btn));
        recharge_comfirm_btn.setText(KR.string.splus_recharge_comfirm_tips);
        recharge_money_ratio_tv = (TextView) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_recharge_money_ratio_tv));
    }

    /**
     * @Title: initViews(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午1:42:09 void 返回类型
     */

    private void initViews() {

        recharge_money_tips.setText(Html.fromHtml("<font color=#FE8E35>充值金额: " + mRenminbi + "元</font>"));
        recharge_money_ratio_tv.setHint(KR.string.splus_recharge_ratio_tv_tips);

    }

    /**
     * @Title: setlistener(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午1:42:12 void 返回类型
     */

    private void setlistener() {

        recharge_comfirm_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                payQuest();
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
        RatioModel mRatioModel = new RatioModel(mGameid, mPayway, time, MD5Util.getMd5toLowerCase(keyString));
        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.RATIO_URL, mRatioModel, new LoginParser()), onRatioebyCardCallBack);
    }

    private DataCallback<JSONObject> onRatioebyCardCallBack = new DataCallback<JSONObject>() {

        @Override
        public void callbackSuccess(JSONObject paramObject) {
            if (paramObject != null && paramObject.optInt("code") == 1) {
                JSONObject optJSONObject = paramObject.optJSONObject("data");
                if (optJSONObject != null) {
                    mCoin_name = optJSONObject.optString("coin_name");
                    mRatio = optJSONObject.optInt("ratio");
                    setGetMoneyTextPure(mRenminbi);
                }

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
        if (mPayway.equals(Constant.ALIPAY_FAST_PAYWAY)) {
            // check to see if the MobileSecurePay is already installed.
            // 检测安全支付服务是否安装
            MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(mActivity);
            boolean isMobile_spExist = mspHelper.detectMobile_sp();
            if (!isMobile_spExist) {
                return;
            }
            long time = DateUtil.getUnixTime();
            String keyString = mGameid + mServerName + mDeviceno + mReferer + mPartner + mUserModel.getUid() + mRenminbi + mPayway + time + mAppKey;
            RechargeModel rechargeModel = new RechargeModel(mGameid, mServerId, mServerName, mDeviceno, mPartner, mReferer, mUserModel.getUid(), mRenminbi, mType, mPayway, mRoleId, mRoleName, time, mUserModel.getPassport(), mOutOrderid, mPext,
                            MD5Util.getMd5toLowerCase(keyString));
            if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                showProgressDialog();
            }
            NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.HTMLWAPPAY_URL, rechargeModel, new LoginParser()), onRechargeCallBack);

        } else {
            String str = "充值金额：" + mRenminbi + "元, 请确认您的支付宝余额大于充值金额,否则请您先充值到余额宝！储蓄卡或信用卡支付，单笔限额500元，每日限额500元，每月限额500元";
            ProgressDialogUtil.showInfoDialog(mActivity, "确认支付", str, android.R.drawable.ic_dialog_info, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAlipayHtmlClick.onAlipayQuotaHtmlClick(mUserModel, mActivity, mDeviceno, mAppKey, mGameid, mPartner, mReferer, mServerId, mRoleId, mRoleName, mServerName, mOutOrderid, mPext, mType, mPayway, mRenminbi);
                }

            }, null, true);
        }

    }

    private DataCallback<JSONObject> onRechargeCallBack = new DataCallback<JSONObject>() {

        @Override
        public void callbackSuccess(JSONObject paramObject) {
            closeProgressDialog();
            if (paramObject != null && (paramObject.optInt("code") == 24 || paramObject.optInt("code") == 1)) {
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
                        result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
                        ToastUtil.showToast(mActivity, "支付失败");
                    }
                } else {
                    result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
                    String msg = paramObject.optString("msg");
                    LogHelper.d(TAG, msg);
                    ToastUtil.showToast(mActivity, msg);
                }

            } else {
                result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
                String msg = paramObject.optString("msg");
                LogHelper.d(TAG, msg);
                ToastUtil.showToast(mActivity, msg);
            }

        }

        @Override
        public void callbackError(String error) {
            closeProgressDialog();
            result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
            LogHelper.d(TAG, error);
            ToastUtil.showToast(mActivity, error);
        }

    };

    // the handler use to receive the pay result.
    // 这里接收支付结果，支付宝手机端同步通知
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            closeProgressDialog();
            try {
                String strRet = (String) msg.obj;
                LogHelper.i(TAG, strRet); // strRet范例：resultStatus={9000};memo={};result={partner="2088201564809153"&seller="2088201564809153"&out_trade_no="050917083121576"&subject="123456"&body="2010新款NIKE 耐克902第三代板鞋 耐克男女鞋 386201 白红"&total_fee="0.01"&notify_url="http://notify.java.jpxx.org/index.jsp"&success="true"&sign_type="RSA"&sign="d9pdkfy75G997NiPS1yZoYNCmtRbdOP0usZIMmKCCMVqbSG1P44ohvqMYRztrB6ErgEecIiPj9UldV5nSy9CrBVjV54rBGoT6VSUF/ufjJeCSuL510JwaRpHtRPeURS1LXnSrbwtdkDOktXubQKnIMg2W0PreT1mRXDSaeEECzc="}
                switch (msg.what) {
                    case AlixId.RQF_PAY: {
                        LogHelper.i(TAG, strRet);
                        // 处理交易结果
                        // 获取交易状态码，具体状态代码请参看文档
                        String tradeStatus = "resultStatus={";
                        int imemoStart = strRet.indexOf(tradeStatus);
                        imemoStart += tradeStatus.length();
                        int imemoEnd = strRet.indexOf("};memo=");
                        tradeStatus = strRet.substring(imemoStart, imemoEnd);
                        // 判断交易状态码，只有9000表示交易成功
                        if (tradeStatus.equals("9000")) {
                            result_intent(Constant.RECHARGE_RESULT_SUCCESS_TIPS);
                        } else {
                            LogHelper.i(TAG, strRet);
                            result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
                        }
                    }
                        break;
                }
                super.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
                LogHelper.i(TAG, e.getLocalizedMessage());
                result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
            }
        }
    };

    private void result_intent(String rechage_type) {
        Intent intent = new Intent();
        intent.setClass(mActivity, RechargeResultActivity.class);
        intent.putExtra(Constant.RECHARGE_RESULT_TIPS, rechage_type);
        intent.putExtra(Constant.MONEY, String.valueOf(mRenminbi));
        mActivity.startActivity(intent);

    }

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
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午4:28:56
     * @param listener
     */
    public void setOnAlipayQuotaHtmlClick(AlipayQuotaHtmlClick listener) {
        this.mAlipayHtmlClick = listener;
    }

    public interface AlipayQuotaHtmlClick {

        public void onAlipayQuotaHtmlClick(UserModel userModel, Activity activity, String deviceno, String appKey, Integer gamid, String partner, String referer, Integer serverId, Integer roleId, String roleName, String serverName, String outOrderid, String pext, Integer type,
                        String payway, float renminbi);

    }

}
