
package com.android.splus.sdk.apiinterface;

/**
 * 帐户信息
 *
 * @ClassName: UserAccount
 * @author xiaoming.yuan
 * @date 2013-8-1 下午3:04:20
 */
public interface UserAccount {

    /**
     * @Title: getUserName(用户名)
     * @author xiaoming.yuan
     * @data 2013-8-8 下午3:56:46
     * @param: @return
     * @return String 返回类型
     */
    public String getUserName();


    /**
     * @Title: getSession(获取会话)
     * @author xiaoming.yuan
     * @data 2013-8-6 下午2:18:08
     * @param: @return
     * @return String 返回类型
     */

    public String getSession();

    /**
     * @Title: getUid(获取用户的id)
     * @author xiaoming.yuan
     * @data 2013-8-16 上午10:00:10
     * @return String 返回类型
     */
    public Integer getUserUid();

}
