
package com.android.splus.sdk.apiinterface;

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

    public void init(Activity activity, String appkey, InitCallBack initCallBack, boolean useUpdate,
            int screenType);

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
     * 統計选服接口
     */
    public void sendServerStatics(Activity activity, String serverName);

    /**
     * 統計角色接口
     */
    public void sendRoleStatics(Activity activity, String roleName);


    /**
     * 注销游戏接口
     *
     * @author xiaoming.yuan
     * @date 2013年10月12日 上午11:38:34
     */
    public void logout(LogoutCallBack logoutCallBack);


    /**
     * 控制日志DUBG
     */
    public void setDBUG(boolean logDbug);



}
