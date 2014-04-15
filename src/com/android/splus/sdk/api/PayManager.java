
package com.android.splus.sdk.api;

import com.android.splus.sdk.apiinterface.IPayManager;
import com.android.splus.sdk.apiinterface.InitCallBack;
import com.android.splus.sdk.apiinterface.LoginCallBack;
import com.android.splus.sdk.apiinterface.LogoutCallBack;
import com.android.splus.sdk.apiinterface.RechargeCallBack;
import com.android.splus.sdk.ui.FloatToolBar;
import com.android.splus.sdk.ui.FloatToolBar.FloatToolBarAlign;
import com.android.splus.sdk.ui.SplusPayManager;

import android.app.Activity;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PayManager {
    private static final String TAG = "PayManager";

    private Activity mActivity;

    private InitBean mInitBean;

    private IPayManager mIPayManager;

    private static PayManager mPayManager;

    private static byte[] lock = new byte[0];

    public static final String SDK_VERSION = "1.0";

    /**
     * @Title: PayManager
     * @Description:( 将构造函数私有化)
     * @throws
     */
    private PayManager() {

    }

    /**
     * @Title: getInstance(获取实例)
     * @author xiaoming.yuan
     * @data 2014-2-26 下午2:30:02
     * @return CHPayManager 返回类型
     */
    public static PayManager getInstance() {

        if (mPayManager == null) {
            synchronized (lock) {
                if (mPayManager == null) {
                    mPayManager = new PayManager();
                }
            }
        }
        return mPayManager;
    }

    /**
     * Title: init Description:
     *
     * @param mBaseActivity
     * @param appkey
     * @param callBack
     * @param useupdate
     * @param screenType
     * @see com.android.splus.sdk.apiinterface.IPayManager#init(android.app.Activity,
     *      java.lang.String, com.android.splus.sdk.apiinterface.InitCallBack,
     *      boolean, int)
     */
    public synchronized void init(Activity activity, String appkey, InitCallBack initCallBack,
            boolean useUpdate, int orientation) {

        this.mActivity = activity;
        if (mInitBean == null || mInitBean.getUsesdk() != 1) {
            Properties prop = readPropertites(activity, APIConstants.CONFIG_FILENAME);
            mInitBean = InitBean.inflactBean(activity, prop, appkey);
        }
        switch (mInitBean.getUsesdk()) {
            case APIConstants.SPLUS:
                mIPayManager = SplusPayManager.getInstance();
                break;
            case APIConstants.SPLUS_91:
                mIPayManager = _91.getInstance();
                break;
            case APIConstants.SPLUS_UC:

                break;
            case APIConstants.SPLUS_XIAOMI:

                break;
            case APIConstants.SPLUS_JIFENG:

                break;
            case APIConstants.SPLUS_DCN:

                break;
            case APIConstants.SPLUS_DUOKU:

                break;
            case APIConstants.SPLUS_OPPO:

                break;
            case APIConstants.SPLUS__91DJ:

                break;
            case APIConstants.SPLUS__360:

                break;
        }

        mIPayManager.setInitBean(mInitBean);
        mIPayManager.init(mActivity, appkey, initCallBack, useUpdate, orientation);
    }

    /**
     * 进入SDK登录
     */
    public void login(Activity activity, LoginCallBack loginCallBack) {
        if (mIPayManager != null) {
            mIPayManager.login(activity, loginCallBack);
        }
    }

    /**
     * 充值
     */
    public void recharge(Activity activity, String serverName, String roleName, String outOrderid,
            String pext, RechargeCallBack rechargeCallBack) {
        if (mIPayManager != null) {
            mIPayManager.recharge(activity, serverName, roleName, outOrderid, pext,
                    rechargeCallBack);
        }
    }

    /**
     * 定额充值
     */
    public void rechargeByQuota(Activity activity, String serverName, String roleName,
            String outOrderid, String pext, Float money, RechargeCallBack rechargeCallBack) {
        if (mIPayManager != null) {
            mIPayManager.rechargeByQuota(activity, serverName, roleName, outOrderid, pext, money,
                    rechargeCallBack);
        }
    }

    /**
     * 退出sdk
     */
    public void exitSDK() {
        if (mIPayManager != null) {
            mIPayManager.exitSDK();
        }
    }

    /**
     * 退出游戏
     */
    public void exitGame(Context context) {
        if (mIPayManager != null) {
            mIPayManager.exitGame(context);
        }

    }

    /**
     * 注销游戏接口
     *
     * @author xiaoming.yuan
     * @date 2013年10月12日 上午11:38:34
     */
    public void logout(Activity activity, LogoutCallBack logoutCallBack) {
        if (mIPayManager != null) {
            mIPayManager.logout(activity, logoutCallBack);
        }

    }

    /**
     * 控制日志DUBG
     */
    public void setDBUG(boolean logDbug) {
        if (mIPayManager != null) {
            mIPayManager.setDBUG(logDbug);
        }
    }

    /**
     * 进入个人中心
     *
     * @author xiaoming.yuan
     * @date 2013年10月14日 上午10:27:05
     */
    public void enterUserCenter(Activity activity, LogoutCallBack mLogoutCallBack) {
        if (mIPayManager != null) {
            mIPayManager.enterUserCenter(activity, mLogoutCallBack);
        }
    }

    /**
     * 統計区服角色等级接口
     */
    public void sendGameStatics(Activity activity, String roleName, String level, String serverName) {
        if (mIPayManager != null) {
            mIPayManager.sendGameStatics(activity, roleName, level, serverName);
        }
    }

    /**
     * 论坛
     */
    public void enterBBS(Activity activity) {
        if (mIPayManager != null) {
            mIPayManager.enterBBS(activity);
        }
    }

    /**
     * 悬浮按钮
     */

    public FloatToolBar creatFloatButton(Activity mActivity, boolean showlasttime,
            FloatToolBarAlign align, float position) {
        if (mIPayManager != null) {
            return mIPayManager.creatFloatButton(mActivity, showlasttime, align, position);
        }
        return null;
    }

    /**
     * 在线时长统计开始
     */

    public void onResume(Activity activity) {
        if (mIPayManager != null) {
            mIPayManager.onResume(activity);
        }
    }

    /**
     * 在线时长统计结束
     */
    public void onPause(Activity activity) {
        if (mIPayManager != null) {
            mIPayManager.onPause(activity);
        }
    }

    /**
     * 取assets下的配置参数
     *
     * @param context
     * @param file
     * @return
     */
    private Properties readPropertites(Context context, String file) {
        Properties p = null;
        try {
            InputStream in = context.getResources().getAssets().open(file);
            p = new Properties();
            p.load(in);
        } catch (IOException e) {
            p = null;
        }
        return p;
    }

}
