
package com.android.splus.sdk.apiinterface;

public interface RechargeCallBack {
    /**
     * 充值成功
     *
     * @param account 帐号
     */
    public void rechargeSuccess(UserAccount account);

    /**
     * 充值失败
     *
     * @param errorMsg 出错信息
     */
    public void rechargeFaile(String errorMsg);

    /**
     * @Title: backKey(充值界面back键回调)
     * @author xiaoming.yuan
     * @data 2013-10-3 下午6:29:21
     * @param errorMsg void 返回类型
     */
    public void backKey(String errorMsg);

}
