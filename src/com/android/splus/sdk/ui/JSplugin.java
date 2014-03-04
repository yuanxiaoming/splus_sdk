/**
 * @Title: JSplugin.java
 * @Package com.sanqi.android.sdk.apinterface
 * Copyright: Copyright (c) 2013
 * @author xiaoming.yuan
 * @date 2013-10-9 上午11:20:57
 * @version V1.0
 */

package com.android.splus.sdk.ui;

import com.android.splus.sdk.alipay.AlixId;
import com.android.splus.sdk.alipay.MobileSecurePayHelper;
import com.android.splus.sdk.alipay.MobileSecurePayer;
import com.android.splus.sdk.manager.ExitAppUtils;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.file.AppUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil;
import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.progressDialog.ProgressDialogUtil;
import com.android.splus.sdk.utils.toast.ToastUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.util.Map;

/**
 * @ClassName: JSplugin
 * @author xiaoming.yuan
 * @date 2013-10-9 上午11:20:57
 */

public class JSplugin {

    private static final String TAG = "JSplugin";

    public static final String ANDROIDJSPLUG = "androidJSPlug";

    private static final int SUCCESS_BACK_RECHARGE = 1;

    private static final int ERROR_BACK_RECHARGE = 2;

    private static final int ALIPAY_RECHARGE = 3;

    private static final int CALL_PHONE = 4;

    private  ProgressDialog mProgress = null;

    private Activity mActivity;

    private  String mMoney;

    public  JSplugin(Activity activity){
        this.mActivity=activity;

    }

    /**
     * wap成功支付回到游戏
     */

    @JavascriptInterface
    public void successBackGame() {

        ExitAppUtils.getInstance().exit();
        UserModel userModel = SplusPayManager.getInstance().getUserData();
        if (null == userModel) {
            userModel = AppUtil.getUserData();
        } else {
            if (null != SplusPayManager.getInstance().getRechargeCallBack()) {
                SplusPayManager.getInstance().getRechargeCallBack().rechargeSuccess(userModel);
            }
        }

        LogHelper.i(TAG, "successBackGame()");
    }

    /**
     * wap成功支付回到充值界面
     */
    @JavascriptInterface
    public void successBackRacharge() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                mJSHandler.sendEmptyMessage(JSplugin.SUCCESS_BACK_RECHARGE);

            }
        }).start();

        LogHelper.i(TAG, "successBackRacharge()");
    }

    /**
     * wap支付失败回到游戏
     */
    @JavascriptInterface
    public void errorBackGame() {
        ExitAppUtils.getInstance().exit();
        if (null != SplusPayManager.getInstance().getRechargeCallBack()) {
            SplusPayManager.getInstance().getRechargeCallBack().rechargeFaile("充值失败");
            LogHelper.i(TAG, "充值失败");
            LogHelper.i(TAG, "errorBackGame()");
        }
    }

    /**
     * wap支付失败回到充值界面
     */
    @JavascriptInterface
    public void errorBackRecharge() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                mJSHandler.sendEmptyMessage(JSplugin.ERROR_BACK_RECHARGE);

            }
        }).start();

        LogHelper.i(TAG, "errorBackRecharge()");
    }

    // 快捷支付
    @JavascriptInterface
    public void alipay(final String orderinfo) {
        Log.i("orderinfo", orderinfo);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = JSplugin.ALIPAY_RECHARGE;
                message.obj = orderinfo;
                mJSHandler.sendMessage(message);
            }
        }).start();
    }

    /**
     * wap页面拨打电话号码
     */
    @JavascriptInterface
    public void callphone(final String phoneNumber) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = JSplugin.CALL_PHONE;
                message.obj = phoneNumber;
                mJSHandler.sendMessage(message);
            }
        }).start();

    }

    private Handler mJSHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case JSplugin.SUCCESS_BACK_RECHARGE:
                    Activity mActivity = ExitAppUtils.getInstance().getRunActivity();
                    if (null != mActivity) {
                        if (RechargeActivity.class.getSimpleName().equals(
                                mActivity.getClass().getSimpleName())) {
                            if (RechargeActivity.getCustomWebView() != null) {
                                while (RechargeActivity.getCustomWebView().canGoBack()) {
                                    RechargeActivity.getCustomWebView().goBack();
                                }
                                UserModel userModel = SplusPayManager.getInstance().getUserData();
                                if (null == userModel) {
                                    userModel = AppUtil.getUserData();
                                }else {
                                    if (null != SplusPayManager.getInstance().getRechargeCallBack()) {
                                        SplusPayManager.getInstance().getRechargeCallBack()
                                        .rechargeSuccess(userModel);
                                    }
                                }
                            }
                        }
                    }

                    break;
                case JSplugin.ERROR_BACK_RECHARGE:
                    Activity mActivity2 = ExitAppUtils.getInstance().getRunActivity();
                    if (null != mActivity2) {
                        if (RechargeActivity.class.getSimpleName().equals(
                                mActivity2.getClass().getSimpleName())) {
                            if (null != RechargeActivity.getCustomWebView()) {
                                while (RechargeActivity.getCustomWebView().canGoBack()) {
                                    RechargeActivity.getCustomWebView().goBack();
                                }
                                if (null != SplusPayManager.getInstance().getRechargeCallBack()) {
                                    SplusPayManager.getInstance().getRechargeCallBack().rechargeFaile("充值失败");
                                    LogHelper.i(TAG, "充值失败");
                                }
                            }
                        }
                    }

                    break;
                case JSplugin.ALIPAY_RECHARGE:
                    String orderinfo = (String) msg.obj;
                    alipay(orderinfo);
                    break;

                case JSplugin.CALL_PHONE:
                    String phoneNumber = (String) msg.obj;
                    if (!TextUtils.isEmpty(phoneNumber)) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://"
                                + phoneNumber));
                        // 去调用那些可以处理拨号行为的Activity
                        if (null != ExitAppUtils.getInstance().getRunActivity()) {
                            ExitAppUtils.getInstance().getRunActivity().startActivity(intent);
                        } else if (null != SplusPayManager.getInstance().getContext()) {
                            SplusPayManager.getInstance().getContext().startActivity(intent);
                        }
                    }
                    break;
            }
        }
    };


    public  void alipay_ss(String orderinfo) {
        if (mActivity == null) {
            return;
        }
        // 检测安全支付服务是否安装
        MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(mActivity);
        boolean isMobile_spExist = mspHelper.detectMobile_sp();
        if (!isMobile_spExist) {
            return;
        }
        Map<String, String> urltoMap = NetHttpUtil.getParamsTOhashMap(orderinfo);
        String money = urltoMap.get("total_fee");
        // 支付
        try {
            MobileSecurePayer msp = new MobileSecurePayer();
            boolean bRet = msp.pay(orderinfo, mHandler, AlixId.RQF_PAY, mActivity);
            if (bRet) {
                // 显示“正在支付”进度条
                mProgress = ProgressDialogUtil.showProgress(mActivity, null, "正在支付", false, true);
            }
        } catch (Exception e) {
            ToastUtil.showToast(mActivity, "支付失败");
        }
    }

    // the handler use to receive the pay result.
    // 这里接收支付结果，支付宝手机端同步通知
    private  Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            closeProgress();
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

    // close the progress bar
    // 关闭进度框
    private  void closeProgress() {
        try {
            if (mProgress != null) {
                mProgress.dismiss();
                mProgress = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  void result_intent(String rechage_type) {
        Intent intent = new Intent();
    //    intent.setClass(mActivity, RechargeResultActivity.class);
        intent.putExtra(Constant.RECHARGE_RESULT_TIPS, rechage_type);
        intent.putExtra(Constant.MONEY, mMoney);
        mActivity.startActivity(intent);

    }

}
