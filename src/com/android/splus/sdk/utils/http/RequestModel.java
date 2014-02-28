
package com.android.splus.sdk.utils.http;

import android.content.Context;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA. Copyright: Copyright (c) 2013
 *  User: xiaoming.yuan Email: lijianfeng@37wan.com Date: 13-9-17
 * Time: 上午10:46 To change this template use File | Settings | File Templates.
 */
public class RequestModel {
    public String mRequestUrl;

    public Context mContext;

    public HashMap<String, Object> mParams;

    public BaseParser<?> mBaseParser;

    /**
     * @param requestUrl
     * @param context
     * @param params
     * @param baseParser
     */
    public RequestModel(String requestUrl, Context context, HashMap<String, Object> params,
            BaseParser<?> baseParser) {
        super();
        this.mRequestUrl = requestUrl;
        this.mContext = context;
        this.mParams = params;
        this.mBaseParser = baseParser;
    }

    public String getRequestUrl() {
        return mRequestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.mRequestUrl = requestUrl;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
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
