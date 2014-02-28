
package com.android.splus.sdk.utils.sharedPreferences;

import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.md5.MD5Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Title: GetSharedPreferencesHelper.java
 * @Package com.kugou.pay.android.utils
 * @Description: SharedPreferences帮助类
 * @author xiaoming.yuan
 * @date 2013-3-30 下午4:34:34
 * @version V1.0
 */
public class SharedPreferencesHelper {
    private static SharedPreferencesHelper instance;

    private static byte[] lock = new byte[0];

    private SharedPreferencesHelper() {
    }

    public static SharedPreferencesHelper getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new SharedPreferencesHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 获取登陆状态
     *
     * @author xiaoming.yuan
     * @date 2013年10月31日 上午12:05:03
     * @param context
     * @param appkey
     * @return
     */
    public boolean getLoginStatusPreferences(Context context, String appkey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.LOGIN_STATUS_FILE,
                Context.MODE_PRIVATE);
        String status = sharedPreferences.getString(Constant.LOGIN_STATUS, "");
        String sign = MD5Util.getMd5(true + appkey);
        if (sign.equals(status)) {
            return true;
        }
        return false;
    }

    /**
     * 保存登陆状态
     *
     * @author xiaoming.yuan
     * @date 2013年10月31日 上午12:05:25
     * @param context
     * @param appkey
     * @param status
     */
    public void setLoginStatusPreferences(Context context, String appkey, boolean status) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.LOGIN_STATUS_FILE,
                Context.MODE_PRIVATE);
        String sign = MD5Util.getMd5(status + appkey);
        sharedPreferences.edit().putString(Constant.LOGIN_STATUS, sign).commit();
    }

    public boolean setCommonPreferences(Context context, int MODE, String fileName, String key,
            String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, MODE);
        Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        boolean commit = edit.commit();
        return commit;
    }

    public String getCommonPreferences(Context context, int MODE, String fileName, String key,
            String defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, MODE);
        return sharedPreferences.getString(key, defValue);

    }

    /**
     * @Title: setDevicenoPreferences(保存服务器生成的设备唯一值)
     * @author xiaoming.yuan
     * @data 2013-12-16 下午4:45:25
     * @param context
     * @param value
     * @return boolean 返回类型
     */
    public boolean setDevicenoPreferences(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.DEVICENO_FILE,
                Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
        Editor edit = sharedPreferences.edit();
        edit.putString(Constant.DEVICENO, value);
        boolean commit = edit.commit();
        return commit;
    }

    /**
     * @Title: getdevicenoPreferences(获取保存服务器生成的设备唯一值)
     * @author xiaoming.yuan
     * @data 2013-12-16 下午4:45:32
     * @param context
     * @return String 返回类型
     */
    public String getdevicenoPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.DEVICENO_FILE,
                Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
        return sharedPreferences.getString(Constant.DEVICENO, "");

    }

    /**
     * @Title: getAccountRejectBindPhoneTimes(获取当前账户下 点击不绑定手机的 次数)
     * @author xiaoming.yuan
     * @data 2013-12-17 下午7:37:23
     * @param account
     * @param context
     * @return int 返回类型
     */
    public int getAccountRejectBindPhoneTimes(String account, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(account,
                Context.MODE_PRIVATE);
        int times = 0;
        times = sharedPreferences.getInt("times", 0);
        String dateNow = getDate();
        String dateLastTime = sharedPreferences.getString("date", 0 + "");// 默认为0
        if (!dateNow.equals(dateLastTime)) {
            // 不相等 则说明日期不同，
            times = 0;
        }
        return times;
    }

    /**
     * @Title: addAccountRejectBindPhoneTimes(增加计数)
     * @author xiaoming.yuan
     * @data 2013-12-17 下午7:36:47
     * @param account
     * @param context
     * @return boolean 返回类型
     */
    public boolean addAccountRejectBindPhoneTimes(String account, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(account,
                Context.MODE_PRIVATE);
        String dateNow = getDate();
        int times = getAccountRejectBindPhoneTimes(account, context);
        Editor edit = sharedPreferences.edit();
        edit.putString("date", dateNow);
        edit.putInt("times", ++times);

        return edit.commit();
    }

    /**
     * @Title: getDate(获取当前系统时间)
     * @author xiaoming.yuan
     * @data 2013-12-17 下午7:23:08
     * @return String 返回类型
     */
    private String getDate() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String str = df.format(date);
        return str;
    }


}
