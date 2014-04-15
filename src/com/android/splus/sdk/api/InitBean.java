
package com.android.splus.sdk.api;

import com.android.splus.sdk.apiinterface.InitCallBack;
import com.android.splus.sdk.model.ActiveModel;
import com.android.splus.sdk.parse.ActiveParser;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.date.DateUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil.DataCallback;
import com.android.splus.sdk.utils.http.NetWorkUtil;
import com.android.splus.sdk.utils.http.RequestModel;
import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.md5.MD5Util;
import com.android.splus.sdk.utils.phone.Phoneuitl;
import com.android.splus.sdk.utils.progressDialog.ProgressDialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InitBean {

    private static final String TAG = "InitBean";

    public int mUseSDK = 1;

    public int mOrientation; // 是否横屏显示 1-横屏；

    public int mFixed; // 是否定额，1-定额

    private Properties mProperties;

    private String mAppkey;

    private Integer mGameid;

    private String mPartner;

    private String mReferer;

    private String mDeviceNo;

    private Activity mActivity;

    private int mHeight;

    private int mWidth;

    public static InitBean inflactBean(Activity activity, Properties prop, String appkey) {
        InitBean bean = new InitBean();
        if (prop != null && !TextUtils.isEmpty(appkey)) {
            bean.setProperties(prop);
            bean.getGameConfig(activity, null);
            bean.setOrientation(Integer.parseInt(prop.getProperty("orientation") == null ? "1"
                    : prop.getProperty("orientation")));
            bean.setFixed(Integer.parseInt(prop.getProperty("quota") == null ? "1" : prop
                    .getProperty("quota"))); // 默认是1-定额
            bean.mAppkey = appkey;
        }
        return bean;
    }

    public void initSplus(final Activity activity, final InitCallBack initCallBack) {
        this.mActivity = activity;
        if (mUseSDK != APIConstants.SPLUS) {
            init(activity, mAppkey, initCallBack,mOrientation == 1 ? Configuration.ORIENTATION_LANDSCAPE: Configuration.ORIENTATION_PORTRAIT);
        }
    }

    public synchronized void init(Activity mActivity, String appkey, InitCallBack initCallBack,
            int orientation) {
        if (initCallBack == null) {
            LogHelper.d(TAG, "InitCallBack参数不能为空");
            return;
        }
        if (mActivity == null) {
            LogHelper.d(TAG, "Activity参数不能为空");
            initCallBack.initFaile("Activity参数不能为空");
            return;
        } else {
            if (!(mActivity instanceof Activity)) {
                LogHelper.d(TAG, "参数Activity不是一个Activity的实例");
                initCallBack.initFaile("参数Activity不是一个Activity的实例");
                return;
            }
        }
        if (TextUtils.isEmpty(appkey)) {
            LogHelper.d(TAG, "appkey参数不能为空");
            initCallBack.initFaile("appkey参数不能为空");
            return;
        }
        // 初始化获取屏幕高度和宽度
        mHeight = Phoneuitl.getHpixels(mActivity);
        mWidth = Phoneuitl.getWpixels(mActivity);
        mOrientation = orientation;
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏方向，高<宽
            if (mHeight > mWidth) {
                int temp = mWidth;
                mWidth = mHeight;
                mHeight = temp;
            }
        } else {
            // 竖屏方向 高>宽
            if (mHeight < mWidth) {
                int temp = mWidth;
                mWidth = mHeight;
                mHeight = temp;
            }
        }
        getGameConfig(mActivity, initCallBack);
        repeatInit(mActivity);

    }

    private synchronized void requestInit(Activity activity) {
        long time = DateUtil.getUnixTime();
        String mac = Phoneuitl.getLocalMacAddress(activity);
        String imei = Phoneuitl.getIMEI(activity);
        String keyString = mGameid + mReferer + mPartner + mac + imei + time;
        String sign = MD5Util.getMd5toLowerCase(keyString + mAppkey);
        ActiveModel mActiveMode = new ActiveModel(mGameid, mPartner, mReferer, mac, imei, mWidth,
                mHeight, Phoneuitl.MODE, Phoneuitl.OS, Phoneuitl.OSVER, time, sign);
        NetHttpUtil.getDataFromServerPOST(activity, new RequestModel(Constant.ACTIVE_URL,
                mActiveMode, new ActiveParser()), onActiveCallBack);
        // LogHelper.i("requestInit",
        // NetHttpUtil.hashMapTOgetParams(mActiveMode, Constant.ACTIVE_URL));

    }

    /**
     * @Title: repeatInit(无网络时提示)
     * @author xiaoming.yuan
     * @data 2013-12-16 上午10:45:44 void 返回类型
     */
    private synchronized void repeatInit(final Activity activity) {

        if (!NetWorkUtil.isNetworkAvailable(activity)) {

            ProgressDialogUtil.showInfoDialog(activity, "提示", "当前网络不稳定,请检查您的网络设置！", 0,
                    new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            repeatInit(activity);

                        }
                    }, "确定", null, null, false);

        } else {
            requestInit(activity);
        }

    }

    /**
     * 初始化回调
     */
    protected DataCallback<JSONObject> onActiveCallBack = new DataCallback<JSONObject>() {
        @Override
        public void callbackSuccess(JSONObject paramObject) {
            try {
                if (paramObject != null && paramObject.optInt("code") == 1) {
                    mDeviceNo = paramObject.optJSONObject("data").optString("deviceno");
                } else {
                    String msg = paramObject.getString("msg");
                    LogHelper.d(TAG, msg);
                    repeatInit(mActivity);
                }
            } catch (JSONException e) {
                LogHelper.d(TAG, e.getLocalizedMessage());
                repeatInit(mActivity);
            }
        }

        @Override
        public void callbackError(String error) {
            LogHelper.d(TAG, error);
            repeatInit(mActivity);
        }
    };

    /**
     * @Title: getGameInfo(获取游戏配置文件信息)
     * @author xiaoming.yuan
     * @data 2013-12-16 上午10:43:58 void 返回类型
     */
    private void getGameConfig(Activity activity, InitCallBack initCallBack) {
        // // 从资产中读取XMl文件
        // // 获取资产管理器
        // AssetManager assetManager = activity.getAssets();
        // if (assetManager == null) {
        // LogHelper.i(TAG, "Activity参数不能为空");
        // if (initCallBack != null) {
        // initCallBack.initFaile("Activity参数不能为空");
        // }
        // return;
        // }
        // try {
        // // 获取文件输入流
        // InputStream is = assetManager.open("splus_config.xml");
        // if (is == null) {
        // LogHelper.i(TAG, "splus_config.xml文件不存在");
        // if (initCallBack != null) {
        // initCallBack.initFaile("splus_config.xml文件不存在");
        // }
        // return;
        // }
        // // 创建构建XMLPull分析器工厂
        // XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
        // // 创建XMLPull分析器
        // XmlPullParser xpp = xppf.newPullParser();
        // // 设置分析器的输入流
        // xpp.setInput(is, "utf-8");
        // // 得到下一个事件
        // int eventType;
        // // 得到当前的事件
        // eventType = xpp.getEventType();
        // // 循环事件
        // while (eventType != XmlPullParser.END_DOCUMENT) {
        // switch (eventType) {
        // case XmlPullParser.START_TAG:
        // if (xpp.getName().equals("gameid")) {
        // mGameid = Integer.parseInt(xpp.nextText());
        // }
        // if (xpp.getName().equals("partner")) {
        // mPartner = xpp.nextText().trim();
        // }
        // if (xpp.getName().equals("referer")) {
        // mReferer = xpp.nextText().trim();
        // }
        // break;
        // default:
        // break;
        // }
        // // 获取下一个事件
        // eventType = xpp.next();
        // }
        //
        // } catch (Exception e) {
        // LogHelper.i(TAG, e.getLocalizedMessage(), e);
        // LogHelper.i(TAG, "splus_config.xml文件配置错误");
        // if (initCallBack != null) {
        // initCallBack.initFaile("splus_config.xml文件配置错误");
        // }
        // return;
        // }
        // mUseSDK = PayManager.SPLUS;
        // try {
        // mUseSDK = Integer.parseInt(mPartner);
        // } catch (NumberFormatException e) {
        // e.printStackTrace();
        //
        // }

        AssetManager assetManager = activity.getAssets();
        if (assetManager == null) {
            LogHelper.i(TAG, "Activity参数不能为空");
            if (initCallBack != null) {
                initCallBack.initFaile("Activity参数不能为空");
            }
            return;
        }
        try {
            // 获取文件输入流
            InputStream in = assetManager.open(APIConstants.CONFIG_FILENAME);
            Properties prop = new Properties();
            prop.load(in);
            mGameid = Integer.parseInt(prop.getProperty("gameid"));
            mPartner = prop.getProperty("partner");
            mReferer = prop.getProperty("referer");
        } catch (IOException e) {
            LogHelper.i(TAG, e.getLocalizedMessage(), e);
            LogHelper.i(TAG, APIConstants.CONFIG_FILENAME + "文件配置错误");
            initCallBack.initFaile(APIConstants.CONFIG_FILENAME + "文件配置错误");
        }

        mUseSDK = APIConstants.SPLUS;
        try {
            mUseSDK = Integer.parseInt(mPartner);
        } catch (NumberFormatException e) {
            e.printStackTrace();

        }
    }

    public String getAppKey() {
        return this.mAppkey;
    }

    public Integer getGameid() {
        return this.mGameid;
    }

    public String getPartner() {
        return this.mPartner;
    }

    public String getReferer() {
        return this.mReferer;
    }

    public String getDeviceNo() {
        return this.mDeviceNo;
    }

    public void setProperties(Properties prop) {
        this.mProperties = prop;
    }

    public Properties getProperties() {
        return this.mProperties;
    }

    public int getUsesdk() {
        return mUseSDK;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
    }

    public int getFixed() {
        return mFixed;
    }

    public void setFixed(int fixed) {
        this.mFixed = fixed;
    }

}
