
package com.android.splus.sdk.utils.http;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA. Copyright: Copyright (c) 2013
 *  User: xiaoming.yuan
 * Time: 上午10:46
 */
public class RequestModel {
    public String mRequestUrl;

    public HashMap<String, Object> mParams;

    public BaseParser<?> mBaseParser;

    /**
     * @param requestUrl
     * @param context
     * @param params
     * @param baseParser
     */
    public RequestModel(String requestUrl, HashMap<String, Object> params,BaseParser<?> baseParser) {
        super();
        this.mRequestUrl = requestUrl;
        this.mParams = params;
        this.mBaseParser = baseParser;
    }

    public String getRequestUrl() {
        return mRequestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.mRequestUrl = requestUrl;
    }

    public HashMap<String, Object> getParams() {
        return mParams;
    }

    public void setParams(HashMap<String, Object> params) {
        this.mParams = params;
    }

    public BaseParser<?> getBaseParser() {
        return mBaseParser;
    }

    public void setBaseParser(BaseParser<?> baseParser) {
        this.mBaseParser = baseParser;
    }
}
