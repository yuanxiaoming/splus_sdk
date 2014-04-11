
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

}
