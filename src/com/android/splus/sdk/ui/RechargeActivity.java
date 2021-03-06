/**
 * @Title: RechargeSelectActivity.java
 * @Package com.sanqi.sdk.ui
 * Copyright: Copyright (c) 2013
 * @author xiaoming.yuan
 * @date 2013-8-5 下午8:03:37
 * @version V1.0
 */

package com.android.splus.sdk.ui;

import com.android.splus.sdk.apiinterface.RechargeCallBack;
import com.android.splus.sdk.model.RechargeModel;
import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.date.DateUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil;
import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.md5.MD5Util;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.toast.ToastUtil;
import com.android.splus.sdk.widget.CustomWebChromeClient;
import com.android.splus.sdk.widget.CustomWebView;
import com.android.splus.sdk.widget.CustomWebViewClient;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @ClassName: RechargeSelectActivity
 * @author xiaoming.yuan
 * @date 2013-8-5 下午8:03:37
 */

public class RechargeActivity extends BaseActivity {
    private static final String TAG = "RechargeWebViewActivity";

    private ImageButton recharge_title_left_backbtn;

    private ImageButton recharge_title_right_btn;

    private TextView recharge_titlr_middle_text;

    private static CustomWebView mCustomWebView;

    private String mOutOrderid;

    private String mPext;

    private String mPassport;

    private Integer mUid;

    private Long mTime;

    private RechargeModel mRechargeModel;

    private Integer mType;

    private RechargeCallBack mRechargeCallBack;

    private Activity mActivity;

    private SplusPayManager mSplusPayManager;

    private Float mMoney;

    public static final String TIME = "time";

    public static final String MONEY = "money";


    /**
     * Title: findViewById
     *
     * @see com.canhe.android.sdk.ui.BaseActivity#findViewById()
     */
    @Override
    protected void findViewById() {
        recharge_title_left_backbtn = (ImageButton) findViewById(KR.id.splus_recharge_title_bar_left_button);
        recharge_title_right_btn = (ImageButton) findViewById(KR.id.splus_recharge_title_bar_right_button);
        recharge_titlr_middle_text = (TextView) findViewById(KR.id.splus_recharge_title_bar_middle_title);
        recharge_titlr_middle_text.setTextColor(Color.WHITE);
        recharge_titlr_middle_text.setTextSize(CommonUtil.dip2px(mActivity, 10));
        recharge_titlr_middle_text.setText(KR.string.splus_recharge_title_bar_middle_tips);
        mCustomWebView = (CustomWebView) findViewById(KR.id.splus_recharge_webview);
        mSplusPayManager = SplusPayManager.getInstance();
    }

    /**
     * Title: loadViewLayout
     *
     * @see com.canhe.android.sdk.ui.BaseActivity#loadViewLayout()
     */
    @Override
    protected void loadViewLayout() {
        setContentView(KR.layout.splus_recharge_select_activity);
        mActivity = this;
        mType = getIntent().getIntExtra(RechargeActivity.class.getName(), 0);
    }

    /**
     * Title: processLogic
     *
     * @see com.canhe.android.sdk.ui.BaseActivity#processLogic()
     */
    @Override
    protected void processLogic() {
        mRechargeCallBack = mSplusPayManager.getRechargeCallBack();
        mPassport = getPassport();
        if (TextUtils.isEmpty(mPassport)) {
            ToastUtil.showToast(mActivity, "账号为空");
            return;
        }
        mTime = DateUtil.getUnixTime();
        mUid = getUid();
        mMoney = mSplusPayManager.getMoney();
        mOutOrderid = mSplusPayManager.getOutorderid();
        mPext = mSplusPayManager.getPext();

        String keyString = mSplusPayManager.getGameid() + mSplusPayManager.getServerName() + getDeviceno() + mSplusPayManager.getReferer() + mSplusPayManager.getPartner() + mUid + mTime;
        mRechargeModel = new RechargeModel(mSplusPayManager.getGameid(),mSplusPayManager.getServerId(), mSplusPayManager.getServerName(), getDeviceno(), mSplusPayManager.getPartner(), mSplusPayManager.getReferer(), mUid, mMoney, mType,
                             mSplusPayManager.getRoleId(),SplusPayManager.getInstance().getRoleName(), mTime, mPassport, mOutOrderid, mPext, MD5Util.getMd5toLowerCase(keyString + mSplusPayManager.getAppkey()));

        mCustomWebView.setWebChromeClient(new CustomWebChromeClient(this, new WebChromeClient()));
        mCustomWebView.setWebViewClient(new CustomWebViewClient(this));
        mCustomWebView.clearCache(true);
        mCustomWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        mCustomWebView.requestFocus();
        WebSettings webSettings = mCustomWebView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setJavaScriptEnabled(true);
        mCustomWebView.addJavascriptInterface(new JSplugin(mActivity), JSplugin.ANDROIDJSPLUG);
        String data = NetHttpUtil.hashMapTOgetParams(mRechargeModel);
        mCustomWebView.postUrl(Constant.HTMLWAPPAY_URL, EncodingUtils.getBytes(data, "UTF-8"));

    }

    /**
     * Title: setListener
     *
     * @see com.canhe.android.sdk.ui.BaseActivity#setListener()
     */
    @Override
    protected void setListener() {
        recharge_title_left_backbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recharge_title_right_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 密码找回界面
                Intent intent = new Intent(mActivity, PersonActivity.class);
                intent.putExtra(PersonActivity.INTENT_TYPE, PersonActivity.INTENT_SQ);
                mActivity.startActivity(intent);

            }
        });
    }

    /**
     * Title: onBackPressed Description:
     *
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        if (mCustomWebView != null && mCustomWebView.canGoBack()) {
            mCustomWebView.goBack(); // goBack()表示返回WebView的上一页面
        } else {
            if (mRechargeCallBack != null) {
                mRechargeCallBack.backKey("选择充值方式界面手机返回键和back监听");
                LogHelper.i(TAG, "选择充值方式界面手机返回键和back监听");
            }
            super.onBackPressed();
        }
    }

    /**
     * Title: onDestroy Description:
     *
     * @see com.canhe.android.sdk.ui.BaseActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogHelper.i(TAG, "onDestroy");
        if (mCustomWebView != null) {
            mCustomWebView.removeAllViews();
        }
        mCustomWebView = null;
        mOutOrderid = null;
        mPext = null;
        mPassport = null;
        mUid = null;
        mTime = null;
        mRechargeModel = null;
        mType = null;
        mActivity = null;
    }

    public static CustomWebView getCustomWebView() {
        return mCustomWebView;
    }

    /**
     * Title: onActivityResult Description:
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @see android.app.Activity#onActivityResult(int, int,
     *      android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            /*************************************************
             * 处理银联手机支付控件返回的支付结果 支付控件返回字符串:success、fail、cancel
             * 分别代表支付成功，支付失败，支付取消
             ************************************************/
            String str = data.getExtras().getString("pay_result");
            if (str.equalsIgnoreCase("success")) {
                result_intent(Constant.RECHARGE_RESULT_SUCCESS_TIPS);
            } else if (str.equalsIgnoreCase("fail")) {
                result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
            } else if (str.equalsIgnoreCase("cancel")) {
                result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
            }
        }
    }

    public void result_intent(String rechage_type) {
        Intent intent = new Intent();
        intent.setClass(mActivity, RechargeResultActivity.class);
        intent.putExtra(Constant.RECHARGE_RESULT_TIPS, rechage_type);
        intent.putExtra(Constant.MONEY, JSplugin.sMoney);
        mActivity.startActivity(intent);

    }

}
