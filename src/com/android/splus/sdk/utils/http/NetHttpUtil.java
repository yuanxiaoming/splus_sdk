
package com.android.splus.sdk.utils.http;


import com.android.splus.sdk.utils.log.LogHelper;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoming.yuan
 * @ClassName: NetUtil
 * @date 2013-7-12 下午9:23:52
 */
public class NetHttpUtil {
    private static final String TAG = "NetHttpUtil";

    public final static int FAILED = 1;

    public final static int SUCCESS = 2;

    private final static int TIMEOUT_CONNECTION = 10000;

    private final static int TIMEOUT_SOCKET = 20000;

    private final static  boolean REQUEST_GET=false;

    private final static boolean REQUEST_POST=true;

    private static boolean REQUEST_MODE;

    private static Header[] headers = new BasicHeader[11];
    static {
        headers[0] = new BasicHeader("Appkey", "");
        headers[1] = new BasicHeader("Udid", "");
        headers[2] = new BasicHeader("Os", "");
        headers[3] = new BasicHeader("Osversion", "");
        headers[4] = new BasicHeader("Appversion", "");
        headers[5] = new BasicHeader("Sourceid", "");
        headers[6] = new BasicHeader("Ver", "");
        headers[7] = new BasicHeader("Userid", "");
        headers[8] = new BasicHeader("Usersession", "");
        headers[9] = new BasicHeader("Unique", "");
        headers[10] = new BasicHeader("Cookie", "");

    }

    /**
     *
     * @Title: getDataFromServer(获取网络数据对外接口)
     * @author xiaoming.yuan
     * @data 2014-1-16 上午10:16:09
     * @param context
     * @param mRequestModel
     * @param callBack
     * void 返回类型
     */
    public static <T> void getDataFromServerPOST(Context context, RequestModel mRequestModel,
            DataCallback<T> callBack) {
        REQUEST_MODE=REQUEST_POST;
        BaseHandler handler = new BaseHandler(callBack);
        BaseTask taskThread = new BaseTask(context, mRequestModel, handler);
        ThreadPoolManager.getInstance().addTask(taskThread);
    }

    public static <T> void getDataFromServerGET(Context context, RequestModel mRequestModel,
            DataCallback<T> callBack) {
        REQUEST_MODE=REQUEST_GET;
        BaseHandler handler = new BaseHandler(callBack);
        BaseTask taskThread = new BaseTask(context, mRequestModel, handler);
        ThreadPoolManager.getInstance().addTask(taskThread);
    }

    /**
     * @return Object 返回类型
     * @Title: post(请求模型)
     * @author xiaoming.yuan
     * @data 2013-7-12 下午9:21:28
     */
    public static Object post(RequestModel mRequestModel,Context mContext) {
        Object obj = null;
        try {
            if (mRequestModel.mParams == null || mRequestModel.mParams.size() <= 0
                    || TextUtils.isEmpty(mRequestModel.mRequestUrl)) {
                return obj;
            }
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT_CONNECTION);
            HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_SOCKET);
            //开启重定向
            HttpClientParams.setRedirecting(httpParameters, true);
            // 构造Httpclient实例
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            //设置代理
            if(NetWorkUtil.isCmwap(mContext)){
                // 获取默认代理主机ip
                String host = android.net.Proxy.getDefaultHost();
                // 获取端口
                int port = android.net.Proxy.getDefaultPort();

                HttpHost httpHost = new HttpHost(host, port);
                httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,httpHost);
            }
            // 创建post方法实例
            HttpPost httpPost = new HttpPost(mRequestModel.mRequestUrl);
            // 设置httppost请求参数
            httpPost.setHeaders(headers);
            HttpEntity entity = new UrlEncodedFormEntity(hashMapTOpostParams(mRequestModel.mParams),
                    HTTP.UTF_8);
            httpPost.setEntity(entity);
            // 使用execute方法发送Http post请求并返回Httpresponse对象
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                setCookie(httpResponse);
                String result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                obj = mRequestModel.mBaseParser.parseJSON(result);
            }
            //结束连接
            httpResponse.getEntity().consumeContent();
        } catch (ClientProtocolException e) {
            LogHelper.i(TAG, e.getLocalizedMessage(), e);
            return null;
        } catch (IOException e) {
            LogHelper.i(TAG, e.getLocalizedMessage(), e);
            return null;
        } catch (JSONException e) {
            LogHelper.i(TAG, e.getLocalizedMessage(), e);
            return null;

        }
        return obj;
    }

    /**
     * @return Object 返回类型
     * @Title: get(请求模型)
     * @author xiaoming.yuan
     * @data 2013-7-24 下午6:04:37
     */
    public static Object get(RequestModel mRequestModel,Context mContext) {
        Object obj = null;
        try {
            if (mRequestModel.mParams == null || mRequestModel.mParams.size() <= 0
                    || TextUtils.isEmpty(mRequestModel.mRequestUrl)) {
                return obj;
            }

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT_CONNECTION);
            HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_SOCKET);
            //开启重定向
            HttpClientParams.setRedirecting(httpParameters, true);
            // 构造Httpclient实例
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            //设置代理
            if(NetWorkUtil.isCmwap(mContext)){
                // 获取默认代理主机ip
                String host = android.net.Proxy.getDefaultHost();
                // 获取端口
                int port = android.net.Proxy.getDefaultPort();
                HttpHost httpHost = new HttpHost(host, port);
                httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,httpHost);
            }
            HttpGet httpGet = new HttpGet(hashMapTOgetParams(mRequestModel.mParams,
                    mRequestModel.mRequestUrl));
            httpGet.setHeaders(headers);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                setCookie(httpResponse);
                String result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                obj = mRequestModel.mBaseParser.parseJSON(result);
            }
            //结束连接
            httpResponse.getEntity().consumeContent();
        } catch (ClientProtocolException e) {
            LogHelper.i(TAG, e.getLocalizedMessage(), e);
            return null;
        } catch (IOException e) {
            LogHelper.i(TAG, e.getLocalizedMessage(), e);
            return null;
        } catch (JSONException e) {
            LogHelper.i(TAG, e.getLocalizedMessage(), e);
            return null;
        }
        return obj;
    }

    /**
     * 添加Cookie
     *
     * @param response
     */
    private static void setCookie(HttpResponse response) {
        if (response.getHeaders("Set-Cookie").length > 0) {
            LogHelper.d(TAG, response.getHeaders("Set-Cookie")[0].getValue());
            headers[10] = new BasicHeader("Cookie", response.getHeaders("Set-Cookie")[0].getValue());
        }
    }

    /**
     * @return List<BasicNameValuePair> 返回类型:
     * @Title: hashMapTOpostParams(把params集合转换为post(NameValuePair)请求参数的集合)
     * @author xiaoming.yuan create at 2013-5-29 上午10:27:59
     */
    public static synchronized List<BasicNameValuePair> hashMapTOpostParams(
            HashMap<String, Object> params) {

        List<BasicNameValuePair> result = new ArrayList<BasicNameValuePair>();
        Iterator<String> iterators = params.keySet().iterator();
        while (iterators.hasNext()) {
            String key = iterators.next();
            BasicNameValuePair pair = new BasicNameValuePair(key, params.get(key).toString());
            result.add(pair);
        }
        return result;
    }

    /**
     * @Title: paramsGet(hashmap转化成get请求的参数)
     * @author xiaoming.yuan
     * @data 2013-10-8 下午5:05:30
     * @param params
     * @param url
     * @return String 返回类型
     */
    public static synchronized String hashMapTOgetParams(HashMap<String, Object> params, String url) {
        if (params == null || params.size() <= 0 || TextUtils.isEmpty(url)) {
            return null;
        }
        StringBuilder buf = new StringBuilder(url);
        buf.append("?");
        Iterator<String> iterators = params.keySet().iterator();
        while (iterators.hasNext()) {
            String key = iterators.next();
            try {
                buf.append(key).append("=")
                .append(URLEncoder.encode(params.get(key).toString(), "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                LogHelper.d(TAG, e.getLocalizedMessage());
            }
        }
        buf.deleteCharAt(buf.length() - 1);
        return buf.toString();
    }

    /**
     * @Title: paramsGet(hashmap转化成get请求的参数)
     * @author xiaoming.yuan
     * @data 2013-10-8 下午5:05:30
     * @param params
     * @return String 返回类型
     */
    public static synchronized String hashMapTOgetParams(HashMap<String, Object> params) {
        if (params == null || params.size() <= 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        Iterator<String> iterators = params.keySet().iterator();
        while (iterators.hasNext()) {
            String key = iterators.next();
            try {
                buf.append(key).append("=")
                .append(URLEncoder.encode(params.get(key).toString(), "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                LogHelper.d(TAG, e.getLocalizedMessage());
            }
        }
        buf.deleteCharAt(buf.length() - 1);
        return buf.toString();
    }

    /***
     * @Title: getParamsTOhashMap( 解析出url参数中的键值对 如
     *         "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中)
     * @author xiaoming.yuan
     * @data 2014-1-2 下午4:43:27
     * @param URL
     * @return Map<String,String> 返回类型
     */
    public static Map<String, String> getParamsTOhashMap(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();
        String strUrlParam = truncateUrl(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        // 每个键值为一组
        String[] arrSplit = strUrlParam.split("[&]");
        String[] arrSplitEqual = null;
        for (String strSplit : arrSplit) {
            arrSplitEqual = strSplit.split("[=]");
            // 解析出键值
            if (null != arrSplitEqual && arrSplitEqual.length > 1) {
                // 正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1].replaceAll("\"", ""));
            } else {
                if (arrSplitEqual[0] != "") {
                    // 只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     * @Title: truncateUrl(去掉url中的路径，留下请求参数部分)
     * @author xiaoming.yuan
     * @data 2014-1-2 下午4:43:10
     * @param strURL
     * @return String 返回类型
     */
    public static String truncateUrl(String strURL) {
        String strAllParam = strURL.trim();
        String[] arrSplit = null;
        if (strURL.contains("[?]")) {
            arrSplit = strURL.split("[?]");
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }
        return strAllParam;
    }

    /**
     * @ClassName: BaseHandler
     * @author xiaoming.yuan
     * @date 2014-1-2 下午4:42:36
     */
    private static class BaseHandler extends Handler {
        private DataCallback mCallBack;

        public BaseHandler(DataCallback callBack) {
            this.mCallBack = callBack;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg); // To change body of overridden methods
            // use File | Settings | File Templates.
            if (msg.what == NetHttpUtil.SUCCESS) {
                if (msg.obj == null) {
                    if (mCallBack != null) {
                        mCallBack.callbackError("请求成功，服务器返回参数有误");
                    }
                } else {
                    if (mCallBack != null) {
                        mCallBack.callbackSuccess(msg.obj);
                    }
                }
            } else if (msg.what == NetHttpUtil.FAILED) {
                if (mCallBack != null) {
                    mCallBack.callbackError("当前网络不可用，请先连接Internet！");
                }

            } else {
                if (mCallBack != null) {
                    mCallBack.callbackError("当前网络不可用，请先连接Internet！");
                }
            }
        }
    }

    /**
     * @ClassName: BaseTask
     * @author xiaoming.yuan
     * @date 2014-1-2 下午4:42:42
     */
    private static class BaseTask implements Runnable {
        private Context context;

        private RequestModel mRequestModel;

        private Handler handler;

        public BaseTask(Context context, RequestModel mRequestModel, Handler handler) {
            this.context = context;
            this.mRequestModel = mRequestModel;
            this.handler = handler;
        }

        @Override
        public void run() {
            // To change body of implemented methods use File | Settings | File
            // Templates.
            Object obj = null;
            Message msg = Message.obtain();
            try {
                if (NetWorkUtil.isNetworkAvailable(context)) {
                    if(REQUEST_MODE==REQUEST_GET){
                        obj = NetHttpUtil.get(mRequestModel,context);
                    }
                    if(REQUEST_MODE==REQUEST_POST){
                        obj = NetHttpUtil.post(mRequestModel,context);

                    }
                    msg.what = NetHttpUtil.SUCCESS;
                    msg.obj = obj;
                    handler.sendMessage(msg);
                } else {
                    msg.what = NetHttpUtil.FAILED;
                    msg.obj = obj;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                msg.what = NetHttpUtil.FAILED;
                handler.sendMessage(msg);
            }
        }
    }

    /**
     * @ClassName: DataCallback
     * @author xiaoming.yuan
     * @date 2013-9-26 上午11:10:49
     * @param <T>
     */
    public abstract interface DataCallback<T> {
        /**
         * 回调
         *
         * @param paramObject
         * @param paramBoolean 请求是否成功
         */
        public abstract void callbackSuccess(T paramObject);

        public abstract void callbackError(String error);
    }
}
