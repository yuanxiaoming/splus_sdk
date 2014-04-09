
package com.android.splus.sdk.apiinterface;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA. Copyright: Copyright (c) 2013 User: xiaoming.yuan
 * Time: 下午3:02 To change this template use File | Settings | File Templates.
 */
public interface InitCallBack {
    public void initSuccess(String msg, JSONObject apkverJson);

    public void initFaile(String errorMsg);
}
