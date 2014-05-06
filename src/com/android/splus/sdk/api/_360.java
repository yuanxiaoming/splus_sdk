
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
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Properties;

public class _360 implements IPayManager {

    private static final String TAG = "_360";

    private static _360 m_360;

    // 平台参数
    private Properties mProperties;

    private String mAppId;

    private String mAppKey;

    private InitBean mInitBean;

    private InitCallBack mInitCallBack;

    private Activity mActivity;

    private LoginCallBack mLoginCallBack;

    private RechargeCallBack mRechargeCallBack;

    private LogoutCallBack mLogoutCallBack;

    // 下面参数仅在测试时用
    private UserModel userModel;

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
            synchronized (_360.class) {
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
    public void init(Activity activity, Integer gameid, String appkey, InitCallBack initCallBack, boolean useUpdate, Integer orientation) {

        mInitBean.initSplus(activity, initCallBack);
        this.mInitCallBack = initCallBack;
        this.mActivity = activity;
        Matrix.init(activity, false, mIinitCallback);

    }

    IDispatcherCallback mIinitCallback = new IDispatcherCallback() {

        @Override
        public void onFinished(String data) {
            Log.d(TAG, "matrix startup callback,result is " + data);
            mInitCallBack.initSuccess("初始化完成", null);
        }

    };

    @Override
    public void login(Activity activity, LoginCallBack loginCallBack) {
        this.mActivity = activity;
        this.mLoginCallBack = loginCallBack;
        boolean isLandScape = false;
        if (mInitBean.getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            isLandScape = false;
        } else {
            isLandScape = true;
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

        Matrix.invokeActivity(activity, intent, mILoginCallback);

    }

    IDispatcherCallback mILoginCallback = new IDispatcherCallback() {

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
                NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(APIConstants.TS_VERIFY, params, new LoginParser()), mLoginDataCallBack);
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
    public void recharge(Activity activity, Integer serverId, String serverName, Integer roleId, String roleName, String outOrderid, String pext, RechargeCallBack rechargeCallBack) {
        rechargeByQuota(activity, serverId, serverName, roleId, roleName, outOrderid, pext, 0f, rechargeCallBack);
    }

    @Override
    public void rechargeByQuota(Activity activity, Integer serverId, String serverName, Integer roleId, String roleName, String outOrderid, String pext, Float money, RechargeCallBack rechargeCallBack) {
        this.mActivity = activity;
        this.mRechargeCallBack = rechargeCallBack;
        boolean isLandScape = false;
        if (mInitBean.getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            isLandScape = false;
        } else {
            isLandScape = true;
        }

        Bundle bundle = new Bundle();

        // 界面相关参数，360SDK界面是否以横屏显示。
        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);

        // *** 以下非界面相关参数 ***//

        // 设置PayRechargeBean中的参数。

        PayRechargeBean payRechargeBean = new PayRechargeBean("");
        // 必需参数，用户access token，要使用注意过期和刷新问题，最大64字符。
        bundle.putString(ProtocolKeys.ACCESS_TOKEN, payRechargeBean.getAccessToken());

        // 必需参数，360账号id，整数。
        bundle.putString(ProtocolKeys.QIHOO_USER_ID, payRechargeBean.getQihooUserId());

        // 必需参数，所购买商品金额, 以分为单位。金额大于等于100分，360SDK运行定额支付流程； 金额数为0，360SDK运行不定额支付流程。
        bundle.putString(ProtocolKeys.AMOUNT, payRechargeBean.getMoneyAmount());

        // 必需参数，人民币与游戏充值币的默认比例，例如2，代表1元人民币可以兑换2个游戏币，整数。
        bundle.putString(ProtocolKeys.RATE, payRechargeBean.getExchangeRate());

        // 必需参数，所购买商品名称，应用指定，建议中文，最大10个中文字。
        bundle.putString(ProtocolKeys.PRODUCT_NAME, payRechargeBean.getProductName());

        // 必需参数，购买商品的商品id，应用指定，最大16字符。
        bundle.putString(ProtocolKeys.PRODUCT_ID, payRechargeBean.getProductId());

        // 必需参数，应用方提供的支付结果通知uri，最大255字符。360服务器将把支付接口回调给该uri，具体协议请查看文档中，支付结果通知接口–应用服务器提供接口。
        bundle.putString(ProtocolKeys.NOTIFY_URI, payRechargeBean.getNotifyUrl());

        // 必需参数，游戏或应用名称，最大16中文字。
        bundle.putString(ProtocolKeys.APP_NAME, payRechargeBean.getAppName());

        // 必需参数，应用内的用户名，如游戏角色名。 若应用内绑定360账号和应用账号，则可用360用户名，最大16中文字。（充值不分区服，
        // 充到统一的用户账户，各区服角色均可使用）。
        bundle.putString(ProtocolKeys.APP_USER_NAME, payRechargeBean.getAppUserName());

        // 必需参数，应用内的用户id。
        // 若应用内绑定360账号和应用账号，充值不分区服，充到统一的用户账户，各区服角色均可使用，则可用360用户ID最大32字符。
        bundle.putString(ProtocolKeys.APP_USER_ID, payRechargeBean.getAppUserId());

        // 可选参数，应用扩展信息1，原样返回，最大255字符。
        bundle.putString(ProtocolKeys.APP_EXT_1, payRechargeBean.getAppExt1());

        // 可选参数，应用扩展信息2，原样返回，最大255字符。
        bundle.putString(ProtocolKeys.APP_EXT_2, payRechargeBean.getAppExt2());

        // 可选参数，应用订单号，应用内必须唯一，最大32字符。
        bundle.putString(ProtocolKeys.APP_ORDER_ID, payRechargeBean.getAppOrderId());

        Intent intent = new Intent(activity, ContainerActivity.class);

        // 必需参数，使用360SDK的支付模块。
        intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_PAY);
        // 界面相关参数，360SDK登录界面背景是否透明。
        intent.putExtra(ProtocolKeys.IS_LOGIN_BG_TRANSPARENT, true);
        intent.putExtras(bundle);

    }

    @Override
    public void exitSDK() {

    }

    @Override
    public void exitGame(final Context context) {
        // 如果上面没关闭好自己，或者没填写任何东西，就我们sdk来关闭进程。
        new Thread(new Runnable() {
            public void run() {

                int currentVersion = Build.VERSION.SDK_INT;
                try {
                    Thread.sleep(1000);
                    if (currentVersion > Build.VERSION_CODES.ECLAIR_MR1) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(startMain);
                        System.exit(0);
                    } else {
                        // android2.1
                        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                        am.restartPackage(context.getPackageName());
                    }
                } catch (Exception e) {
                }
            };
        }).start();

    }

    @Override
    public void logout(Activity activity, LogoutCallBack logoutCallBack) {
        this.mActivity = activity;
        this.mLogoutCallBack = logoutCallBack;
        boolean isLandScape = false;
        if (mInitBean.getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            isLandScape = false;
        } else {
            isLandScape = true;
        }
        Bundle bundle = new Bundle();
        // 界面相关参数，360SDK界面是否以横屏显示。
        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);
        // 必需参数，使用360SDK的退出模块。
        bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_QUIT);
        Intent intent = new Intent(activity, ContainerActivity.class);
        intent.putExtras(bundle);
        Matrix.invokeActivity(activity, intent, mQuitCallback);

    }

    // 退出的回调
    private IDispatcherCallback mQuitCallback = new IDispatcherCallback() {
        @Override
        public void onFinished(String data) {
            Log.d(TAG, "mQuitCallback, data is " + data);
            mLogoutCallBack.logoutCallBack();
        }

    };

    @Override
    public void setDBUG(boolean logDbug) {
    }

    @Override
    public void enterUserCenter(Activity activity, LogoutCallBack mLogoutCallBack) {
    }

    @Override
    public void sendGameStatics(Activity activity, Integer serverId, String serverName, Integer roleId, String roleName, String level) {

    }

    @Override
    public void enterBBS(Activity activity) {
        this.mActivity = activity;
        boolean isLandScape = false;
        if (mInitBean.getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            isLandScape = false;
        } else {
            isLandScape = true;
        }
        Bundle bundle = new Bundle();
        // 界面相关参数，360SDK界面是否以横屏显示。
        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);
        // 必需参数，使用360SDK的论坛模块。
        bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_BBS);
        Intent intent = new Intent(activity, ContainerActivity.class);
        intent.putExtras(bundle);
        Matrix.invokeActivity(mActivity, intent, null);

    }

    @Override
    public FloatToolBar creatFloatButton(Activity activity, boolean showlasttime, FloatToolBarAlign align, float position) {
        this.mActivity = activity;
        boolean isLandScape = false;
        if (mInitBean.getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            isLandScape = false;
        } else {
            isLandScape = true;
        }
        Bundle bundle = new Bundle();
        // 界面相关参数，360SDK界面是否以横屏显示。
        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);
        bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_SETTINGS);
        Intent intent = new Intent(activity, ContainerActivity.class);
        intent.putExtras(bundle);
        Matrix.execute(activity, intent, new IDispatcherCallback() {
            @Override
            public void onFinished(String data) {
                Log.d(TAG, data);

            }

        });
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

    @Override
    public void onDestroy(Activity activity) {

        Matrix.destroy(activity);
    }

}
