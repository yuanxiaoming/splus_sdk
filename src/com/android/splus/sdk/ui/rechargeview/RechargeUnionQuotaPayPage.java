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

import com.android.splus.sdk.adapter.MoneyGridViewAdapter;
import com.android.splus.sdk.alipay.NetworkManager;
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
import com.android.splus.sdk.utils.phone.Phoneuitl;
import com.android.splus.sdk.utils.progressDialog.ProgressDialogUtil;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.utils.toast.ToastUtil;
import com.android.splus.sdk.widget.CustomGridView;
import com.android.splus.sdk.widget.CustomProgressDialog;
import com.unionpay.UPPayAssistEx;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

/**
 * @ClassName: RechargeUnionPayPage
 * @author xiaoming.yuan
 * @date 2014-3-11 下午1:37:06
 */

public class RechargeUnionQuotaPayPage extends LinearLayout {
    private static final String TAG = "RechargeUnionPayPage";

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

    private String mRoleName;

    private String mServerName;

    private String mOutOrderid;

    private String mPext;

    private String mAppKey;

    private String mDeviceno;

    private Integer mType;

    protected CustomProgressDialog mProgressDialog;

    private String mMode = "00";// 测试

    public RechargeUnionQuotaPayPage(UserModel userModel, Activity activity, String deviceno,
            String appKey, Integer gamid, String partner, String referer, String roleName,
            String serverName, String outOrderid, String pext, Integer type, String payway,Float money) {
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
        this.mType = type;
        this.mPayway = payway;
        this.mRenminbi=money;
        inflate(activity,
                ResourceUtil.getLayoutId(activity, KR.layout.splus_recharge_alipay_quota_layout), this);
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
        recharge_comfirm_btn = (Button) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_comfirm_btn));
        recharge_comfirm_btn.setText(KR.string.splus_recharge_comfirm_tips);
        recharge_money_ratio_tv = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_ratio_tv));

        recharge_money_tips.setText(KR.string.splus_recharge_select_head_tips);
        recharge_money_ratio_tv.setHint(KR.string.splus_recharge_ratio_tv_tips);
    }

    /**
     * @Title: initViews(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午1:42:09 void 返回类型
     */

    private void initViews() {

        recharge_money_tips.setText(Html.fromHtml("<font color=#FE8E35>充值金额: "+ mRenminbi+"元</font>"));
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
        // getRatio();
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
        long time = DateUtil.getUnixTime();
        String keyString = mGameid + mServerName + mDeviceno + mReferer + mPartner
                + mUserModel.getUid() + mRenminbi + mPayway + time + mAppKey;
        RechargeModel rechargeModel = new RechargeModel(mGameid, mServerName, mDeviceno, mPartner,
                mReferer, mUserModel.getUid(), mRenminbi, mType, mPayway, mRoleName, time,
                mUserModel.getPassport(), mOutOrderid, mPext, MD5Util.getMd5toLowerCase(keyString));
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            showProgressDialog();
        }
        // NetHttpUtil.getDataFromServerPOST(mActivity, new
        // RequestModel(Constant.HTMLWAPPAY_URL,
        // mActivity, rechargeModel, new LoginParser()), onRechargeCallBack);
        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(
                "http://121.201.96.82/upmp/examples/purchase.php", mActivity, rechargeModel,
                new LoginParser()), onRechargeCallBack);

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
//                if (sign.equals(MD5Util.getMd5toLowerCase(orderid + time + mAppKey))) {
                    int startPay = UPPayAssistEx.startPay(mActivity, null, null, orderinfo, mMode);
                    if (startPay == UPPayAssistEx.PLUGIN_NOT_FOUND) {
                        // 需要重新安装控件
                        Log.e(TAG, " plugin not found or need upgrade!!!");
                        ProgressDialogUtil.showInfoDialog(mActivity, "提示", "完成购买需要安装银联支付控件，是否安装？",
                                0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!UPPayAssistEx.installUPPayPlugin(mActivity)) {
                                            showProgressDialog();
                                            new Thread(new Runnable() {
                                                public void run() {
                                                    String cachePath = mActivity.getCacheDir().getAbsolutePath() + "/UPPayPluginEx.apk";
                                                    // 动态下载
                                                    retrieveApkFromNet(
                                                            mActivity,
                                                            "http://mobile.unionpay.com/getclient?platform=android&type=securepayplugin",
                                                            cachePath);
                                                    // 发送结果
                                                    Message msg = new Message();
                                                    msg.what = 1;
                                                    msg.obj = cachePath;
                                                    mHandler.sendMessage(msg);
                                                }
                                            }).start();
                                        }
                                    }
                                }, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }, true);
                    }

//                } else {
//                    result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
//                    String msg = paramObject.optString("msg");
//                    LogHelper.d(TAG, msg);
//                    ToastUtil.showToast(mActivity, msg);
//                }

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

    /**
     *
     * @Title: result_intent(跳转到支付界面)
     * @author xiaoming.yuan
     * @data 2014-3-17 上午11:06:05
     * @param rechage_type
     * void 返回类型
     */
    public void result_intent(String rechage_type) {
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
     * 动态下载apk
     *
     * @param context 上下文环境
     * @param strurl 下载地址
     * @param filename 文件名称
     * @return
     */
    public boolean retrieveApkFromNet(Context context, String strurl, String filename) {
        boolean bRet = false;

        try {
            NetworkManager nM = new NetworkManager(mActivity);
            bRet = nM.urlDownloadToFile(context, strurl, filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bRet;
    }

    // 安装
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1: {
                    closeProgressDialog();
                    String cachePath = (String) msg.obj;
                    showInstallAPK(mActivity, cachePath);
                }
                    break;
            }
        }
    };

    /**
     * 显示确认安装的提示
     *
     * @param context 上下文环境
     * @param cachePath 安装文件路径
     */
    public void showInstallAPK(final Context context, final String cachePath) {

                // 修改apk权限
                try {
                    String command = "chmod " + "777" + " " + cachePath;
                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec(command);
                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtil.showToast(context, "安装失败");
                }
                // 安装安全支付服务APK
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse("file://" + cachePath),
                        "application/vnd.android.package-archive");
                context.startActivity(intent);

    }

}
