/**
 * @Title: RechargeAlipayHtmlPage.java
 * @Package com.android.splus.sdk.ui.rechargeview
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-3-12 下午1:27:02
 * @version V1.0
 */

package com.android.splus.sdk.ui.rechargeview;

import com.android.splus.sdk.model.RechargeModel;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.ui.JSplugin;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.date.DateUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil;
import com.android.splus.sdk.utils.md5.MD5Util;
import com.android.splus.sdk.widget.CustomWebChromeClient;
import com.android.splus.sdk.widget.CustomWebView;
import com.android.splus.sdk.widget.CustomWebViewClient;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.LinearLayout;

/**
 * @ClassName: RechargeAlipayHtmlPage
 * @author xiaoming.yuan
 * @date 2014-3-12 下午1:27:02
 */

public class RechargeAlipayHtmlPage extends LinearLayout {
    private static final String TAG = "RechargeAlipayHtmlPage";

    private Activity mActivity;

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

    private String mPayway;

    private float mRenminbi = 0; // 人民币

    private CustomWebView mCustomWebView;

    private RechargeModel mRechargeModel;

    public RechargeAlipayHtmlPage(UserModel userModel, Activity activity, String deviceno, String appKey, Integer gameid, String partner, String referer, Integer serverId, Integer roleId, String roleName, String serverName, String outOrderid, String pext, Integer type,
                    String payway, float renminbi) {
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
        this.mRenminbi = renminbi;

        long time = DateUtil.getUnixTime();
        String keyString = mGameid + mServerName + mDeviceno + mReferer + mPartner + mUserModel.getUid() + mRenminbi + mPayway + time + mAppKey;
        mRechargeModel = new RechargeModel(mGameid, mServerId, mServerName, mDeviceno, mPartner, mReferer, mUserModel.getUid(), mRenminbi, mType, mPayway, mRoleId, mRoleName, time, mUserModel.getPassport(), mOutOrderid, mPext, MD5Util.getMd5toLowerCase(keyString));

        mCustomWebView = new CustomWebView(activity);
        mCustomWebView.setWebChromeClient(new CustomWebChromeClient(activity, new WebChromeClient()));
        mCustomWebView.setWebViewClient(new CustomWebViewClient(activity));
        mCustomWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        mCustomWebView.clearCache(true);
        mCustomWebView.requestFocus();
        WebSettings webSettings = mCustomWebView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setJavaScriptEnabled(true);
        mCustomWebView.addJavascriptInterface(new JSplugin(activity), JSplugin.ANDROIDJSPLUG);

        String data = NetHttpUtil.hashMapTOgetParams(mRechargeModel);
        mCustomWebView.postUrl(Constant.HTMLWAPPAY_URL, EncodingUtils.getBytes(data, "UTF-8"));
        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addView(mCustomWebView, lps);

    }

    public boolean goBack() {
        if (mCustomWebView != null && mCustomWebView.canGoBack()) {
            mCustomWebView.goBack();
            return true;
        } else {
            return false;
        }
    }

}
