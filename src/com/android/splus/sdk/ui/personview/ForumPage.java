
package com.android.splus.sdk.ui.personview;

import com.android.splus.sdk.model.SQModel;
import com.android.splus.sdk.ui.JSplugin;
import com.android.splus.sdk.utils.Constant;
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

public class ForumPage extends LinearLayout {

    private static final String TAG = "ForumPage";

    private Integer mGameid;

    private String mPartner;

    private String mReferer;

    private String mDeviceno;

    private Activity mActivity;

    private Integer mServerId;

    private Integer mRoleId;

    private String mRoleName;

    private String mServerName;

    private SQModel mSqModel;

    private CustomWebView mCustomWebView;

    private Integer mUid;

    private String mPassport;

    private String mPassword;

    private String mSign;

    private String mAppkey;

    public ForumPage(Activity activity, String deviceno, Integer gameid, String partner, String referer, Integer uid, String passport, String password,Integer serverId, Integer roleId, String roleName, String serverName,String appkey) {

        super(activity);
        this.mActivity = activity;
        this.mDeviceno = deviceno;
        this.mGameid = gameid;
        this.mPartner = partner;
        this.mReferer = referer;
        this.mUid = uid;
        this.mPassword = password;
        this.mPassport = passport;
        this.mRoleName = roleName;
        this.mServerName = serverName;
        this.mServerId=serverId;
        this.mRoleId=roleId;
        this.mAppkey=appkey;
        String keyString=mGameid+mDeviceno+ mReferer+mPartner+mUid+mAppkey;
        this.mSign=MD5Util.getMd5toLowerCase(keyString);
        mSqModel = new SQModel(mGameid, mDeviceno, mReferer, mPartner, mUid, mPassport, mPassword,mServerId,mRoleId, mRoleName, mServerName,mSign);
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
        String data = NetHttpUtil.hashMapTOgetParams(mSqModel);
        System.out.println(NetHttpUtil.hashMapTOgetParams(mSqModel, Constant.FORUMPAGE_URL));
        mCustomWebView.postUrl(Constant.FORUMPAGE_URL, EncodingUtils.getBytes(data, "UTF-8"));
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
