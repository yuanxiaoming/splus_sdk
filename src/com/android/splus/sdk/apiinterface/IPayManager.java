
package com.android.splus.sdk.apiinterface;

import com.android.splus.sdk.ui.FloatToolBar;
import com.android.splus.sdk.ui.FloatToolBar.FloatToolBarAlign;

import android.app.Activity;
import android.content.Context;

/**
 * 游戏充值管理接口（对外api接口）
 *
 * @author xiaoming.yuan
 * @ClassName: IPayManager
 * @date 2013-8-9 下午1:03:54
 */
public interface IPayManager {

    /**
     * @param context
     * @param appkey
     * @param callBack 初始化回调
     * @param useupdate 是否使用SDK平台的更新机制
     */

    public void init(Activity activity, String appkey, InitCallBack initCallBack, boolean useUpdate);

    /**
     * 进入SDK登录
     */
    public void login(Activity activity, LoginCallBack loginCallBack);

    /**
     * 充值
     */
    public void recharge(Activity activity, String serverName, String roleName, String outOrderid,
            String pext, RechargeCallBack rechargeCallBack);

    /**
     * 定额充值
     */
    public void rechargeByQuota(Activity activity, String serverName, String roleName,
            String outOrderid, String pext, Float money, RechargeCallBack rechargeCallBack);

    /**
     * 退出sdk
     */
    public void exitSDK();

    /**
     * 退出游戏
     */
    public void exitGame(Context context);

    /**
     * 注销游戏接口
     *
     * @author xiaoming.yuan
     * @date 2013年10月12日 上午11:38:34
     */
    public void logout(Activity activity,LogoutCallBack logoutCallBack);


    /**
     * 控制日志DUBG
     */
    public void setDBUG(boolean logDbug);

    /**
     * 进入个人中心
     *
     * @author xiaoming.yuan
     * @date 2013年10月14日 上午10:27:05
     */
    public void enterUserCenter(Activity activity, LogoutCallBack mLogoutCallBack);


    /**
     * 統計区服角色等级接口
     */
    public void sendGameStatics(Activity activity, String roleName, String level,String serverName);


    /**
     * 论坛
     */
    public void enterBBS(Activity activity);


   /**
    * 悬浮按钮
    */

   public FloatToolBar creatFloatButton(Activity mActivity, boolean showlasttime,
           FloatToolBarAlign align, float position);

   /**
    * 在线时长统计开始
    */

   public void onResume(Activity activity);

   /**
    * 在线时长统计结束
    */
   public void onPause(Activity activity);


}
