
package com.android.splus.sdk.api;

import com.android.splus.sdk.api.InitBean.InitBeanSuccess;
import com.android.splus.sdk.apiinterface.IPayManager;
import com.android.splus.sdk.apiinterface.InitCallBack;
import com.android.splus.sdk.apiinterface.LoginCallBack;
import com.android.splus.sdk.apiinterface.LogoutCallBack;
import com.android.splus.sdk.apiinterface.RechargeCallBack;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.ui.FloatToolBar;
import com.android.splus.sdk.ui.FloatToolBar.FloatToolBarAlign;
import com.duoku.platform.DkErrorCode;
import com.duoku.platform.DkPlatform;
import com.duoku.platform.DkPlatformSettings;
import com.duoku.platform.DkPlatformSettings.GameCategory;
import com.duoku.platform.DkProtocolConfig;
import com.duoku.platform.DkProtocolKeys;
import com.duoku.platform.ui.DKContainerActivity;
import com.duoku.platform.ui.DKPaycenterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.Properties;

public class _DuoKu implements IPayManager{

    private static final String TAG = "_DouKu";

    private static _DuoKu _mDuoKu;

    // 平台参数
    private Properties mProperties;

    private String mAppId;

    private String mAppkey;

    private InitBean mInitBean;

    private InitCallBack mInitCallBack;

    private Activity mActivity;

    private LoginCallBack mLoginCallBack;

    private RechargeCallBack mRechargeCallBack;

    private LogoutCallBack mLogoutCallBack;


    // 下面参数仅在测试时用
    private UserModel mUserModel;

    private int mUid = 0;

    private String mPassport = "";

    private String mSessionid = "";



    /**
     * @Title: _DuoKu
     * @Description:( 将构造函数私有化)
     */
    private _DuoKu() {

    }

    /**
     * @Title: getInstance(获取实例)
     * @author xiaoming.yuan
     * @data 2014-2-26 下午2:30:02
     * @return _DuoKu 返回类型
     */
    public static _DuoKu getInstance() {

        if (_mDuoKu == null) {
            synchronized (_UC.class) {
                if (_mDuoKu == null) {
                    _mDuoKu = new _DuoKu();
                }
            }
        }
        return _mDuoKu;
    }

    @Override
    public void setInitBean(InitBean bean) {
        this.mInitBean = bean;
        this.mProperties = mInitBean.getProperties();
        if (mProperties != null) {
            mAppId = mProperties.getProperty("duoku_appid") == null ? "0" : mProperties.getProperty("duoku_appid");
            mAppkey = mProperties.getProperty("duoku_appkey") == null ? "0" : mProperties.getProperty("duoku_appkey");
        }
    }

    @Override
    public void init(Activity activity, Integer gameid, String appkey, InitCallBack initCallBack, boolean useUpdate, Integer orientation) {

        this.mInitCallBack = initCallBack;
        this.mActivity = activity;
        // 特别注意：请在onCreate中进行初始化，在游戏主activity中的onDestory中进行资源释放，详情请参照本activity的onDestory方法，如不释放会出现crash等问题。
        DkPlatformSettings appInfo = new DkPlatformSettings();
        appInfo.setGameCategory(GameCategory.ONLINE_Game);
        appInfo.setAppid(mAppId);// 应用ID，由多酷分配
        appInfo.setAppkey(mAppkey);// 应用Key，由多酷分配
        if (mInitBean.getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            appInfo.setOrient(DkPlatformSettings.SCREEN_ORIENTATION_LANDSCAPE); //横屏
        } else {
            appInfo.setOrient(DkPlatformSettings.SCREEN_ORIENTATION_PORTRAIT);  //竖屏
        }
        DkPlatform.init(mActivity, appInfo);
        mInitBean.initSplus(activity, initCallBack ,new InitBeanSuccess() {
            @Override
            public void initBeaned(boolean initBeanSuccess) {
                mInitCallBack.initSuccess("初始化完成", null);

            }});
    }

    @Override
    public void login(Activity activity, LoginCallBack loginCallBack) {
        this.mActivity=activity;
        this.mLoginCallBack=loginCallBack;
        Bundle bundle = new Bundle();
        bundle.putInt(DkProtocolKeys.FUNCTION_CODE,DkProtocolConfig.FUNCTION_LOGIN);
        Intent intent = new Intent(activity, DKContainerActivity.class);
        intent.putExtras(bundle);
        DkPlatform.invokeActivity(activity,intent , mLoginIdksdkCallBack);

    }
    com.duoku.platform.IDKSDKCallBack mLoginIdksdkCallBack=new com.duoku.platform.IDKSDKCallBack(){

        @Override
        public void onResponse(String paramString) {

            //onResponse()方法中的paramString为返回结果JSON串，例如：登录成功通知内容格式如下：
            //{"user_id":"103256","user_name":"dktest","user_sessionid":"52469875215469875462545698754562","state_code":1021}
            //其中：state_code为状态码，"user_id"为用户id, "user_name"为用户的用户名，"user_sessionid"为用户的sessionid
            int _loginState = 0;
            String _userName = null;
            String _userId;
            String _userSessionId;
            JSONObject jsonObj;
            try {
                jsonObj = new JSONObject(paramString);
                _loginState = jsonObj.getInt(DkProtocolKeys.FUNCTION_STATE_CODE);// 用户登录状态
                _userName = jsonObj.getString(DkProtocolKeys.USER_NAME);// 用户姓名
                _userId = jsonObj.getString(DkProtocolKeys.USER_ID);// 用户id
                _userSessionId = jsonObj.getString(DkProtocolKeys.USER_SESSIONID);// 用户seesionId
            } catch (JSONException e) {
                e.printStackTrace();
                mLoginCallBack.loginFaile("登录失败");
            }

            if (DkErrorCode.DK_LOGIN_SUCCESS == _loginState) {
                //登录成功回调，必须正确处理
                Toast.makeText(mActivity, "登录成功", Toast.LENGTH_LONG).show();
                mLoginCallBack.loginSuccess(null);
            } else if (DkErrorCode.DK_LOGIN_CANCELED == _loginState) {
                //取消登录回调，必须正确处理
                Toast.makeText(mActivity, "用户取消登录", Toast.LENGTH_LONG).show();
                mLoginCallBack.backKey("用户取消登录");
            } else if (DkErrorCode.DK_NEEDLOGIN == _loginState) {
                //用户登录状态失效回调，必须正确处理
                mLoginCallBack.loginFaile("帐号失效");
            }

        }

    };





    @Override
    public void recharge(Activity activity, Integer serverId, String serverName, Integer roleId, String roleName, String outOrderid, String pext, RechargeCallBack rechargeCallBack) {
        rechargeByQuota(activity, serverId, serverName, roleId, roleName, outOrderid, pext, 1.0f, rechargeCallBack);
    }

    @Override
    public void rechargeByQuota(Activity activity, Integer serverId, String serverName, Integer roleId, String roleName, String outOrderid, String pext, Float money, RechargeCallBack rechargeCallBack) {
        this.mActivity = activity;
        this.mRechargeCallBack = rechargeCallBack;


        /**
         * 参数说明
         * @param amount                定额支付消费金额（人民币），如购买1元的物品或者游戏币，则amount为1；若不需要定额支付则传入0
         * @param exchangeRatio 兑换比例，如1酷币兑换100游戏币，则兑换比例为100
         * @param strGamebiName 游戏币名称，如金币、符石、元宝等
         * @param strOrderId            cp生成的订单号，充值结束后，多酷服务器会通知业务服务器该订单号及充值结果
         * @param strPayDesc            支付描述，cp可以在此处自定义字段完成自己的需求，若不需要传入空串，请勿传null
         * @return                          返回Intent对象
         */
        Bundle bundle = new Bundle();
        bundle.putInt(DkProtocolKeys.FUNCTION_CODE , DkProtocolConfig.FUNCTION_Pay);
        bundle.putString(DkProtocolKeys.FUNCTION_AMOUNT, String.valueOf(money));                                  // 金额（转换成String）
        bundle.putString(DkProtocolKeys.FUNCTION_EXCHANGE_RATIO, "100");       // 兑换比例 （转换成String）
        bundle.putString(DkProtocolKeys.FUNCTION_ORDER_ID , outOrderid);                                    // 订单号
        bundle.putString(DkProtocolKeys.FUNCTION_PAY_DESC, "");                                 // 支付描述
        bundle.putString(DkProtocolKeys.FUNCTION_GAMEBI_NAME, "金币") ;                  // 游戏币名称
        Intent intent = new Intent(activity, DKPaycenterActivity.class);
        intent.putExtras(bundle);
        // 参数说明
        // 参数一：进入支付中心的当前activity
        // 参数二：兑换比例，如1酷币兑换100游戏币，则兑换比例为100
        // 参数三：游戏币名称，如金币、符石
        // 参数四：cp生成的订单号，充值结束后，多酷服务器会通知业务服务器该订单号及充值结果
        // 参数五：定额支付消费金额（人民币），如购买1元的物品或者游戏币，则amount为1；若不需要定额支付则传入0
        // 参数六：支付描述，cp可以在此处自定义字段完成自己的需求，若不需要传入空串，请勿传null
        // 参数七：退出支付中心回调接口，cp收到该通知后可根据参数四去自家服务器查询订单是否成功
        // 此处订单号strOrderId在退出支付中心时，将原样返回给开发者
        // 特别注意：此处的参数二和参数三，均需在百度多酷开发者平台去进行配置，若不配置或配置错误，将会导致支付异常
        DkPlatform.invokeActivity(activity, intent, mRechargeIDKSDKCallBack);



    }

    com.duoku.platform.IDKSDKCallBack mRechargeIDKSDKCallBack=new com.duoku.platform.IDKSDKCallBack(){

        @Override
        public void onResponse(String paramString) {
            Log.i(TAG, paramString);
            try {
                JSONObject jsonObject = new JSONObject(paramString);
                String code = jsonObject.optString(DkProtocolKeys.FUNCTION_STATE_CODE); // 状态码
                if(!TextUtils.isEmpty(code)){
                    int  mStateCode=Integer.valueOf(code);
                    String message = jsonObject.optString(DkProtocolKeys.FUNCTION_MESSAGE);        // 信息
                    String orderId =jsonObject.optString(DkProtocolKeys.FUNCTION_ORDER_ID);             // 订单号
                    Log.d(TAG, "订单:"+ orderId + "的状-----message"+message);
                    if(mStateCode == DkErrorCode.DK_ORDER_NEED_CHECK) {                                     // 需要查询订单
                        mRechargeCallBack.rechargeSuccess(null);

                    } else if (mStateCode == DkErrorCode.DK_ORDER_NOT_CHECK) {                          // 不需要查询订单
                        mRechargeCallBack.rechargeFaile("充值失败");
                    }
                }else{
                    mRechargeCallBack.rechargeFaile("充值失败");
                }
            } catch (JSONException e) {
                mRechargeCallBack.rechargeFaile("充值失败");
            }

        }
    };



    @Override
    public void exitSDK() {


    }


    @Override
    public void logout(Activity activity, LogoutCallBack logoutCallBack) {
        this.mLogoutCallBack=logoutCallBack;
        this.mActivity=activity;
        DkPlatform.doDKUserLogout();
        DkPlatform.destroy(activity);
        logoutCallBack.logoutCallBack();

    }






    @Override
    public void setDBUG(boolean logDbug) {
    }

    @Override
    public void enterUserCenter(Activity activity, LogoutCallBack logoutCallBack) {
        this.mActivity=activity;
        this.mLogoutCallBack=logoutCallBack;

    }



    @Override
    public void sendGameStatics(Activity activity, Integer serverId, String serverName, Integer roleId, String roleName, String level) {
    }

    @Override
    public void enterBBS(Activity activity) {
    }

    @Override
    public FloatToolBar creatFloatButton(Activity activity, boolean showlasttime, FloatToolBarAlign align, float position) {
        //2、初始化结束之后，设置悬浮窗回调，目前悬浮窗中仅切换帐号功能支持回调，暂时只需处理这一种情况。不设置该回调或者设置错误回调，将无法通过上线测试。
        this.mActivity=activity;
        DkPlatform.setDKSuspendWindowCallBack(mIdksdkCallBack);
        return null;

    }

    com.duoku.platform.IDKSDKCallBack mIdksdkCallBack=new com.duoku.platform.IDKSDKCallBack(){

        @Override
        public void onResponse(String paramString) {

            //paramString为返回结果JSON串，切换帐号json串示例如下：{"state_code":2005}
            int _stateCode = 0;
            try {
                JSONObject _jsonObj = new JSONObject(paramString);
                _stateCode = _jsonObj.getInt(DkProtocolKeys.FUNCTION_STATE_CODE);
            } catch(Exception e) {
                e.printStackTrace();
            }
            if(_stateCode == DkErrorCode.DK_CHANGE_USER){
                //切换帐号处理逻辑，以下代码仅为示例代码，cp可根据自身需要进行操作，如重新弹出登录界面等
                if(mLogoutCallBack!=null){
                    mLogoutCallBack.logoutCallBack();

                }else{
                    Intent intent =mActivity.getApplicationContext().getPackageManager().getLaunchIntentForPackage(mActivity.getPackageName());
                    if(android.os.Build.VERSION.SDK_INT >= 11){
                        intent.addFlags(32768);
                    }else{
                        intent.addFlags(67108864);
                    }
                    PendingIntent intent1 = PendingIntent.getActivity(mActivity, 223344, intent, 268435456);
                    ((AlarmManager)mActivity.getSystemService("alarm")).set(1, System.currentTimeMillis() + 100L, intent1);
                    System.exit(0);

                }
            }
        }

    };





    @Override
    public void onResume(Activity activity) {


    }

    @Override
    public void onPause(Activity activity) {



    }

    @Override
    public void onStop(Activity activity) {

    }

    @Override
    public void onDestroy(Activity activity) {

  //      DkPlatform.destroy(activity);
    }
}

