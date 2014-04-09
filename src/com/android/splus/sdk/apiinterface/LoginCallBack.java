
package com.android.splus.sdk.apiinterface;

public interface LoginCallBack {
    /**
     * 登录成功
     *
     * @param account 帐号
     */
    public void loginSuccess(UserAccount account);

    /**
     * 登录失败
     *
     * @param errorMsg 出错信息
     */
    public void loginFaile(String errorMsg);

    /**
     * @Title: backKey(登录页面返回键的监理)
     * @author xiaoming.yuan
     * @data 2013-9-5 下午2:24:22
     * @param errorMsg void 返回类型
     */
    public void backKey(String errorMsg);

}
