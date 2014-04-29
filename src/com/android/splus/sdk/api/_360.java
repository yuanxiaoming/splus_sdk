
package com.android.splus.sdk.api;

import com.android.splus.sdk.apiinterface.IPayManager;
import com.android.splus.sdk.apiinterface.InitCallBack;
import com.android.splus.sdk.apiinterface.LoginCallBack;
import com.android.splus.sdk.apiinterface.LogoutCallBack;
import com.android.splus.sdk.apiinterface.RechargeCallBack;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.parse.LoginParser;
import com.android.splus.sdk.ui.FloatToolBar;
import com.android.splus.sdk.ui.FloatToolBar.FloatToolBarAlign;
import com.android.splus.sdk.utils.http.NetHttpUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil.DataCallback;
import com.android.splus.sdk.utils.http.RequestModel;
import com.android.splus.sdk.utils.md5.MD5Util;
import com.qihoo.gamecenter.sdk.common.IDispatcherCallback;
import com.qihoo.gamecenter.sdk.protocols.pay.ProtocolConfigs;
import com.qihoo.gamecenter.sdk.protocols.pay.ProtocolKeys;
import com.qihoopay.insdk.activity.ContainerActivity;
import com.qihoopay.insdk.matrix.Matrix;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Properties;

public class _360 implements IPayManager  {

    private static final String TAG = "_360";
    private static _360 m_360;
    private static byte[] lock = new byte[0];

    // 平台参数
    private Properties mProperties;

    private String mAppId;

    private String mAppKey;

    private InitBean mInitBean;

    private InitCallBack mInitCallBack;

    private Activity mActivity;

    private LoginCallBack mLoginCallBack;

    private RechargeCallBack mRechargeCallBack;

    // 下面参数仅在测试时用
    private  UserModel userModel;

    private int mUid = 0;

    private String mPassport = "";

    private String mSessionid = "";

    private final String TOKEN = "token"; // 存放360 token
    // 登录响应模式：CODE模式。
    protected static final String RESPONSE_TYPE_CODE = "code";

    private final String PREFS = "prefs";


    /**
     * @Title: _360
     * @Description:( 将构造函数私有化)
     */
    private _360() {

    }

    /**
     * @Title: getInstance(获取实例)
     * @author xiaoming.yuan
     * @data 2014-2-26 下午2:30:02
     * @return CHPayManager 返回类型
     */
    public static _360 getInstance() {

        if (m_360 == null) {
            synchronized (lock) {
                if (m_360 == null) {
                    m_360 = new _360();
                }
            }
        }
        return m_360;
    }

    @Override
    public void setInitBean(InitBean bean) {
        this.mInitBean = bean;
        this.mProperties = mInitBean.getProperties();

    }

    @Override
    public void init(Activity activity, Integer gameid, String appkey, InitCallBack initCallBack,
            boolean useUpdate, Integer orientation) {

        mInitBean.initSplus(activity, initCallBack);
        this.mInitCallBack = initCallBack;
        this.mActivity = activity;

    }

    @Override
    public void login(Activity activity, LoginCallBack loginCallBack) {
        this.mActivity = activity;
        this.mLoginCallBack=loginCallBack;
        boolean  isLandScape=false;
        if( mInitBean.getOrientation() == Configuration.ORIENTATION_PORTRAIT){
            isLandScape=false;
        }else{
            isLandScape=true;
        }

        Bundle bundle = new Bundle();

        // 界面相关参数，360SDK界面是否以横屏显示。
        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);
        // 界面相关参数，360SDK登录界面背景是否透明。
        bundle.putBoolean(ProtocolKeys.IS_LOGIN_BG_TRANSPARENT, true);
        // *** 以下非界面相关参数 ***
        // 必需参数，登录回应模式：CODE模式，即返回Authorization Code的模式。
        bundle.putString(ProtocolKeys.RESPONSE_TYPE, RESPONSE_TYPE_CODE);

        // 必需参数，使用360SDK的登录模块。
        bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_LOGIN);

        Intent intent = new Intent(activity, ContainerActivity.class);
        intent.putExtras(bundle);

        Matrix.invokeActivity(activity, intent, mIDispatcherCallback);

    }


    IDispatcherCallback mIDispatcherCallback =new IDispatcherCallback() {

        @Override
        public void onFinished(String data) {

            Log.d(TAG, "mLoginCallback, data is " + data);
            String authorizationCode = parseAuthorizationCode(data);
            _37WanLogin(authorizationCode);

        }
    };

    /**
     * 从Json字符中获取授权码
     *
     * @param data Json字符串
     * @return 授权码
     */
    private String parseAuthorizationCode(String data) {
        String authorizationCode = null;
        if (!TextUtils.isEmpty(data)) {
            boolean isCallbackParseOk = false;
            try {
                JSONObject json = new JSONObject(data);
                int errCode = json.getInt("errno");
                if (errCode == 0) {
                    // 只支持code登陆模式
                    JSONObject content = json.getJSONObject("data");
                    if (content != null) {
                        // 360SDK登录返回的Authorization Code（授权码，60秒有效）。
                        authorizationCode = content.getString("code");
                        isCallbackParseOk = true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 用于测试数据格式是否异常。
            if (!isCallbackParseOk) {
                Toast.makeText(mActivity, "严重错误！！接口返回数据格式错误！！", Toast.LENGTH_LONG).show();
            }
        }
        Log.d(TAG, "parseAuthorizationCode=" + authorizationCode);
        return authorizationCode;
    }

    private void _37WanLogin(String data) {
        if (data == null) {
            if (mLoginCallBack != null) {
                mLoginCallBack.loginFaile("Cancel login");
            }
            return;
        } else {
            String authorizationCode = parseAuthorizationCode(data);

            if (authorizationCode != null && !"".equals(authorizationCode)) {
                System.out.println("authorizationCode:" + authorizationCode);
                String gameid = mInitBean.getGameid().toString();
                String deviceno = mInitBean.getDeviceNo();
                String referer = mInitBean.getReferer();
                String partner = mInitBean.getPartner();
                String time = String.valueOf(System.currentTimeMillis() / 1000);
                String keyString = gameid + deviceno + referer + partner + time + mInitBean.getAppKey();

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("gameid", gameid);
                params.put("referer", referer);
                params.put("partner", partner);
                params.put("deviceno", deviceno);
                params.put("time", time);
                params.put("version", PayManager.SDK_VERSION);
                params.put("sign", MD5Util.getMd5toLowerCase(keyString));
                params.put("code", authorizationCode);
                NetHttpUtil.getDataFromServerPOST(mActivity,new RequestModel(APIConstants.TS_VERIFY, params, new LoginParser()),mLoginDataCallBack);
            } else {
                mLoginCallBack.loginFaile("登录失败，请稍后再试");
            }

        }
    }

    private DataCallback<JSONObject> mLoginDataCallBack = new DataCallback<JSONObject>() {

        @Override
        public void callbackSuccess(JSONObject paramObject) {

            try {
                if (paramObject != null && paramObject.getInt("code") == 1) {
                    JSONObject data = paramObject.optJSONObject("data");
                    setToken(mActivity, data.optString("refresh_token"));
                    mUid = data.optInt("uid");
                    mPassport = data.optString("passport");
                    mSessionid = data.optString("sessionid");
                    userModel = new UserModel(data.toString());
                    mLoginCallBack.loginSuccess(userModel);

                } else {
                    mLoginCallBack.loginFaile(paramObject.optString("msg"));
                }
            } catch (Exception e) {
                mLoginCallBack.loginFaile(e.getLocalizedMessage());
            }
        }

        @Override
        public void callbackError(String error) {
            mLoginCallBack.loginFaile(error);
        }

    };

    @Override
    public void recharge(Activity activity, String serverName, String roleName, String outOrderid,
            String pext, RechargeCallBack rechargeCallBack) {
    }

    @Override
    public void rechargeByQuota(Activity activity, String serverName, String roleName,
            String outOrderid, String pext, Float money, RechargeCallBack rechargeCallBack) {
    }

    @Override
    public void exitSDK() {
    }

    @Override
    public void exitGame(Context context) {
    }

    @Override
    public void logout(Activity activity, LogoutCallBack logoutCallBack) {
    }

    @Override
    public void setDBUG(boolean logDbug) {
    }

    @Override
    public void enterUserCenter(Activity activity, LogoutCallBack mLogoutCallBack) {
    }

    @Override
    public void sendGameStatics(Activity activity, String roleName, String level, String serverName) {
    }

    @Override
    public void enterBBS(Activity activity) {
    }

    @Override
    public FloatToolBar creatFloatButton(Activity activity, boolean showlasttime,
            FloatToolBarAlign align, float position) {

        return null;

    }

    @Override
    public void onResume(Activity activity) {
    }

    @Override
    public void onPause(Activity activity) {
    }

    @Override
    public void onStop(Activity activity) {
    }



    private void setToken(Context context, String token) {
        SharedPreferences uiState = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        Editor editor = uiState.edit();
        editor.putString(TOKEN, token);
        editor.commit();
    }

    private String getToken(Context context) {
        SharedPreferences uiState = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return uiState.getString(TOKEN, "");
    }



}

