
package com.android.splus.sdk.api;

import com.android.splus.sdk.apiinterface.IPayManager;
import com.android.splus.sdk.apiinterface.InitCallBack;
import com.android.splus.sdk.apiinterface.LoginCallBack;
import com.android.splus.sdk.apiinterface.LogoutCallBack;
import com.android.splus.sdk.apiinterface.RechargeCallBack;
import com.android.splus.sdk.ui.FloatToolBar;
import com.android.splus.sdk.ui.FloatToolBar.FloatToolBarAlign;

import android.app.Activity;
import android.content.Context;

import java.util.Properties;

public class _91 implements IPayManager {
    private static final String TAG = "_91";
    private static _91 m_91;
    private static byte[] lock = new byte[0];
    // 平台参数
    private Properties mProperties;
    private String mAppId;
    private String mAppKey;
    private InitBean mInitBean;


    /**
     * @Title: _91
     * @Description:( 将构造函数私有化)
     */
    private _91() {

    }

    /**
     * @Title: getInstance(获取实例)
     * @author xiaoming.yuan
     * @data 2014-2-26 下午2:30:02
     * @return CHPayManager 返回类型
     */
    public static _91 getInstance() {

        if (m_91 == null) {
            synchronized (lock) {
                if (m_91 == null) {
                    m_91 = new _91();
                }
            }
        }
        return m_91;
    }


    @Override
    public void setInitBean(InitBean bean) {
        this.mInitBean=bean;
        this.mProperties=mInitBean.getProperties();
    }

    @Override
    public void init(Activity activity, String appkey, InitCallBack initCallBack,
            boolean useUpdate, int orientation) {
        mInitBean.initSplus(activity, initCallBack);
    }

    @Override
    public void login(Activity activity, LoginCallBack loginCallBack) {
    }

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
    public FloatToolBar creatFloatButton(Activity mActivity, boolean showlasttime,
            FloatToolBarAlign align, float position) {

        return null;

    }

    @Override
    public void onResume(Activity activity) {
    }

    @Override
    public void onPause(Activity activity) {
    }
}

