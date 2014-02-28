
package com.android.splus.sdk.utils.http;

import org.json.JSONException;

/**
 * @ClassName: BaseParser
 * @author xiaoming.yuan
 * @date 2013-5-29 上午10:12:26
 */
public abstract class BaseParser<T> {
    public static final String CODE = "code";
    public static final String DATA = "data";
    public static final String MSG = "msg";
    public static final int SUCCESS = 1;

    public abstract T parseJSON(String paramString) throws JSONException;

    /*    *//**
     * @Title: checkResponse(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2013-7-24 下午5:33:50
     * @return String 返回类型
     */
    /*
     * public String checkResponse(String paramString) throws JSONException { if
     * (paramString == null) { return null; } else { JSONObject jsonObject = new
     * JSONObject(paramString); return jsonObject } }
     */
}
