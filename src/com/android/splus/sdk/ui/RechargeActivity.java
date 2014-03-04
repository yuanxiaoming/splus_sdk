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
import com.android.splus.sdk.widget.CustomWebChromeClient;
import com.android.splus.sdk.widget.CustomWebView;
import com.android.splus.sdk.widget.CustomWebViewClient;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
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

    private String mType;

    private RechargeCallBack mRechargeCallBack;

    private Activity mActivity;

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
        mType = getIntent().getStringExtra(RechargeActivity.class.getName());
        if (TextUtils.isEmpty(mType)) {
            mType = Constant.RECHARGE_BY_NO_QUATO;
        }
    }

    /**
     * Title: processLogic
     *
     * @see com.canhe.android.sdk.ui.BaseActivity#processLogic()
     */
    @Override
    protected void processLogic() {
        mRechargeCallBack = SplusPayManager.getInstance().getRechargeCallBack();
        mPassport = getPassport();
        if (TextUtils.isEmpty(mPassport)) {
            return;
        }
        mTime = DateUtil.getUnixTime();
        mUid = getUid();
        mMoney=SplusPayManager.getInstance().getMoney();
        mOutOrderid = SplusPayManager.getInstance().getOutorderid();
        mPext = SplusPayManager.getInstance().getPext();

        String keyString = SplusPayManager.getInstance().getGameid()
                + SplusPayManager.getInstance().getServerName() + getDeviceno()
                + SplusPayManager.getInstance().getReferer()
                + SplusPayManager.getInstance().getPartner()
                + mUid
                + mTime;
        mRechargeModel = new RechargeModel(SplusPayManager.getInstance().getGameid(),
                SplusPayManager.getInstance().getServerName(), getDeviceno(), SplusPayManager
                        .getInstance().getPartner(), SplusPayManager.getInstance().getReferer(),
                mUid, mType, mMoney, SplusPayManager
                        .getInstance().getRoleName(), mTime, mPassport, mOutOrderid, mPext,
                MD5Util.getMd5toLowerCase(keyString + SplusPayManager.getInstance().getAppkey()));

        mCustomWebView.setWebChromeClient(new CustomWebChromeClient(this, new WebChromeClient()));
        mCustomWebView.setWebViewClient(new CustomWebViewClient(this));
        mCustomWebView.clearCache(true);
        mCustomWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        mCustomWebView.requestFocus();
        WebSettings webSettings = mCustomWebView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setJavaScriptEnabled(true);
        mCustomWebView.addJavascriptInterface(new JSplugin(mActivity),JSplugin.ANDROIDJSPLUG);
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



}
