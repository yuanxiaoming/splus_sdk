
package com.android.splus.sdk.api;

import cn.uc.gamesdk.UCCallbackListener;
import cn.uc.gamesdk.UCCallbackListenerNullException;
import cn.uc.gamesdk.UCGameSDK;
import cn.uc.gamesdk.UCGameSDKStatusCode;
import cn.uc.gamesdk.UCLogLevel;
import cn.uc.gamesdk.UCLoginFaceType;
import cn.uc.gamesdk.UCOrientation;
import cn.uc.gamesdk.info.FeatureSwitch;
import cn.uc.gamesdk.info.GameParamInfo;

import com.android.splus.sdk.apiinterface.IPayManager;
import com.android.splus.sdk.apiinterface.InitCallBack;
import com.android.splus.sdk.apiinterface.LoginCallBack;
import com.android.splus.sdk.apiinterface.LogoutCallBack;
import com.android.splus.sdk.apiinterface.RechargeCallBack;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.ui.FloatToolBar;
import com.android.splus.sdk.ui.FloatToolBar.FloatToolBarAlign;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import java.util.Properties;

public class _UC implements IPayManager {

    private static final String TAG = "_UC";
    private static _UC _mUC;

    // 平台参数
    private Properties mProperties;

    private String mAppId;

    private String mGameId;

    private String mServerId;

    private InitBean mInitBean;

    private InitCallBack mInitCallBack;

    private Activity mActivity;

    private LoginCallBack mLoginCallBack;

    private RechargeCallBack mRechargeCallBack;

    private LogoutCallBack  mLogoutCallBack;

    // 下面参数仅在测试时用
    private  UserModel userModel;

    private int mUid = 0;

    private String mPassport = "";

    private String mSessionid = "";


    /**
     * @Title: _360
     * @Description:( 将构造函数私有化)
     */
    private _UC() {

    }

    /**
     * @Title: getInstance(获取实例)
     * @author xiaoming.yuan
     * @data 2014-2-26 下午2:30:02
     * @return CHPayManager 返回类型
     */
    public static _UC getInstance() {

        if (_mUC == null) {
            synchronized (_UC.class) {
                if (_mUC == null) {
                    _mUC = new _UC();
                }
            }
        }
        return _mUC;
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

        if (mProperties != null) {
            mAppId = mProperties.getProperty("uc_appid") == null ? "0" : mProperties.getProperty("91_appid");
            mGameId = mProperties.getProperty("uc_gameId") == null ? "0" : mProperties.getProperty("uc_gameId");
            mServerId = mProperties.getProperty("uc_serverId") == null ? "0" : mProperties.getProperty("uc_serverId");
        }

        try {
            UCGameSDK.defaultSDK().setLogoutNotifyListener(mLogoutNotify);
            GameParamInfo gpi = new GameParamInfo();// 下面的值仅供参考
            gpi.setCpId(Integer.valueOf(mAppId));
            gpi.setGameId(Integer.valueOf(mGameId));
            gpi.setServerId(Integer.valueOf(mServerId));
            // gpi.setChannelId(2); // 渠道号统一处理，已不需设置，此参数已废弃，服务端此参数请设置值为2

            // 在九游社区设置显示查询充值历史和显示切换账号按钮，
            // 在不设置的情况下，默认情况情况下，生产环境显示查询充值历史记录按钮，不显示切换账户按钮
            // 测试环境设置无效
            gpi.setFeatureSwitch(new FeatureSwitch(true, true));
            if (mInitBean.getOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
                // 设置SDK登录界面为横屏，个人中心及充值页面默认为强制竖屏，无法修改
                UCGameSDK.defaultSDK().setOrientation(UCOrientation.LANDSCAPE);
            } else {
                // 设置SDK登录界面为竖屏
                UCGameSDK.defaultSDK().setOrientation(UCOrientation.PORTRAIT);
            }

            // 设置登录界面：
            // USE_WIDGET - 简版登录界面
            // USE_STANDARD - 标准版登录界面
            UCGameSDK.defaultSDK().setLoginUISwitch(UCLoginFaceType.USE_WIDGET);
            // setUIStyle已过时，不需调用。
            // UCGameSDK.defaultSDK().setUIStyle(UCUIStyle.STANDARD);

            UCGameSDK.defaultSDK().initSDK(activity, UCLogLevel.DEBUG, true, gpi,mInitCallback);
        } catch (UCCallbackListenerNullException e) {
            e.printStackTrace();
            mInitCallBack.initFaile(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            mInitCallBack.initFaile(e.getLocalizedMessage());
        }


    }



    UCCallbackListener<String> mInitCallback=new UCCallbackListener<String>(){

        @Override
        public void callback(int code, String msg) {
            Log.e("UCGameSDK", "UCGameSDK初始化接口返回数据 msg:" + msg+ ",code:" + code );
            switch (code) {
                // 初始化成功,可以执行后续的登录充值操作
                case UCGameSDKStatusCode.SUCCESS:
                    // 调用sdk登录接口
                    mInitCallBack.initSuccess("初始化完成", null);
                    break;

                    // 初始化失败
                case UCGameSDKStatusCode.INIT_FAIL:
                    // 调用sdk初始化接口
                    mInitCallBack.initFaile("初始化失败");
                    break;
            }
        }

    };

    UCCallbackListener<String> mLogoutNotify=  new UCCallbackListener<String>() {
        @Override
        public void callback(int statuscode, String msg) {
            //  此处需要游戏客户端注销当前已经登录的游戏角色信息
            String s = "游戏接收到用户退出通知。" + msg + statuscode;
            Log.e("UCGameSDK", s);
            // 未成功初始化
            if (statuscode == UCGameSDKStatusCode.NO_INIT) {
                // 调用SDK初始化接口
            }
            // 未登录成功
            if (statuscode == UCGameSDKStatusCode.NO_LOGIN) {
                // 调用SDK登录接口
            }
            // 退出账号成功
            if (statuscode == UCGameSDKStatusCode.SUCCESS) {
                // 执行销毁悬浮按钮接口
            }
            // 退出账号失败
            if (statuscode == UCGameSDKStatusCode.FAIL) {
                // 调用SDK退出当前账号接口

            }
        }



    };


    @Override
    public void login(Activity activity, LoginCallBack loginCallBack) {
        this.mActivity = activity;
        this.mLoginCallBack=loginCallBack;
        try {
            UCGameSDK.defaultSDK().login(activity, loginCallbackListener);

        } catch (UCCallbackListenerNullException e) {
            e.printStackTrace();
        }
    }

    // 登录接口回调。从这里可以获取登录结果。
    UCCallbackListener<String> loginCallbackListener = new UCCallbackListener<String>() {

        @Override
        public void callback(int code, String msg) {
            Log.e("UCGameSDK", "UCGameSdk登录接口返回数据:code=" + code
                    + ",msg=" + msg);

            // 登录成功。此时可以获取sid。并使用sid进行游戏的登录逻辑。
            // 客户端无法直接获取UCID
            if (code == UCGameSDKStatusCode.SUCCESS) {
                // 获取sid。（注：ucid需要使用sid作为身份标识去SDK的服务器获取）
                UCGameSDK.defaultSDK().getSid();

            }

            // 登录失败。应该先执行初始化成功后再进行登录调用。
            if (code == UCGameSDKStatusCode.NO_INIT) {
                // 没有初始化就进行登录调用，需要游戏调用SDK初始化方法
            }

            // 登录退出。该回调会在登录界面退出时执行。
            if (code == UCGameSDKStatusCode.LOGIN_EXIT) {
                // 登录界面关闭，游戏需判断此时是否已登录成功进行相应操作
            }
        }



    };

    @Override
    public void recharge(Activity activity, Integer serverId,String serverName, Integer roleId,String roleName, String outOrderid,
            String pext, RechargeCallBack rechargeCallBack) {
        rechargeByQuota( activity, serverId,serverName, roleId, roleName, outOrderid,  pext,  0f, rechargeCallBack);
    }

    @Override
    public void rechargeByQuota(Activity activity,Integer serverId,String serverName, Integer roleId,String roleName,
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
    public void sendGameStatics(Activity activity,Integer serverId,String serverName, Integer roleId,String roleName, String level) {
        UCGameSDK.defaultSDK().notifyZone(serverName, serverName, roleName);
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

    @Override
    public void onDestroy(Activity activity) {
    }
}

