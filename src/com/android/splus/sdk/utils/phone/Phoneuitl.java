/**
 * @Title: Phoneuitls.java
 * @Package com.android.cansh.sdk.utils.phone
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-2-26 下午3:19:43
 * @version V1.0
 */

package com.android.splus.sdk.utils.phone;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * @ClassName: PhoneUitl
 * @author xiaoming.yuan
 * @date 2014-2-26 下午3:19:43
 */

public class Phoneuitl {
    private static final String TAG = "Phoneuitls";

    // 手机常量
    public static final String MODE = android.os.Build.MODEL;

    public static final String OS = "android";

    public static final String OSVER = "android+" + android.os.Build.VERSION.RELEASE;

    /**
     * getNumber(获取手机号。注：有些手机卡获取不到手机号) (这里描述这个方法适用条件 – 可选)
     * 
     * @param context
     * @return String
     * @exception
     * @since 1.0.0xiaoming.yuan
     */
    public static String getNumber(Context context) {
        String number = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        number = tm.getLine1Number();
        if (!TextUtils.isEmpty(number) && number.startsWith("+86")) {
            number = number.substring(3);
        }
        return number;
    }

    /**
     * Role:Telecom service providers获取手机服务商信息 <BR>
     * 需要加入权限<uses-permission
     * android:name="android.permission.READ_PHONE_STATE"/> <BR>
     * Date:2012-3-12 <BR>
     * 
     * @author CODYY)yuanxiaoming
     */
    public String getProvidersName(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String ProvidersName = null;
        // 返回唯一的用户ID;就是这张卡的编号神马的
        String IMSI = telephonyManager.getSubscriberId();
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        }
        return ProvidersName;
    }

    /**
     * getManufacture(获取制造厂商) (这里描述这个方法适用条件 – 可选)
     * 
     * @return String
     * @exception
     * @since 1.0.0 xiaoming.yuan
     */
    public static String getBrand() {
        return Build.MODEL;
    }

    /**
     * @Title: getLocalMacAddress(取得手机的mac)
     * @author xiaoming.yuan
     * @data 2013-8-10 下午3:40:46
     * @param: mContext
     * @return
     * @return String 返回类型
     */
    public static String getLocalMacAddress(Context mContext) {

        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();

        return info.getMacAddress() == null ? "" : info.getMacAddress();
    }

    /**
     * @Title: getIMEI(设备的IMEI,IMSI)
     * @author xiaoming.yuan
     * @data 2013-8-10 下午3:41:05
     * @param: @param mContext
     * @param: @return
     * @return String 返回类型
     */
    public static String getIMEI(Context mContext) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            if (TextUtils.isEmpty(imei)) {
                imei = telephonyManager.getSubscriberId();
                if (TextUtils.isEmpty(imei)) {
                    imei = "000000000000000";
                }
            }
            return imei;
        } catch (Exception e) {
            return "000000000000000";
        }
    }

    /**
     * @Title: getDisplayScreenResolution(手机的分辨率)
     * @author xiaoming.yuan
     * @data 2013-8-10 下午3:41:29
     * @param: @param mContext
     * @param: @return
     * @return String 返回类型
     */
    private static String getDisplayScreenResolution(Context mContext) {
        int screen_w = 0;
        int screen_h = 0;
        try {
            screen_h = mContext.getResources().getDisplayMetrics().heightPixels;
            screen_w = mContext.getResources().getDisplayMetrics().widthPixels;
        } catch (Exception e) {
            return screen_w + "*" + screen_h;
        }
        return screen_w + "*" + screen_h;
    }

    /**
     * @Title: getWpixels(横屏的像素)
     * @author xiaoming.yuan
     * @data 2013-8-10 下午3:41:57
     * @param: @param mContext
     * @param: @return
     * @return int 返回类型
     */
    public static int getWpixels(Context mContext) {

        String screenResolution = getDisplayScreenResolution(mContext);
        String wpixels = screenResolution.substring(0, screenResolution.indexOf("*"));
        return Integer.valueOf(wpixels);
    }

    /**
     * @Title: getHpixels(竖屏的像素)
     * @author xiaoming.yuan
     * @data 2013-8-10 下午3:42:23
     * @param: @param mContext
     * @param: @return
     * @return int 返回类型
     */
    public static int getHpixels(Context mContext) {
        String screenResolution = getDisplayScreenResolution(mContext);
        String hpixels = screenResolution.substring(screenResolution.indexOf("*") + 1);
        return Integer.valueOf(hpixels);
    }

    /**
     * getOrientation(获取屏幕方向) (这里描述这个方法适用条件 – 可选)
     * 
     * @param context
     * @return int
     * @exception
     * @since 1.0.0 xiaoming.yuan
     */
    public static int getOrientation(Activity context) {
        Configuration config = context.getResources().getConfiguration();
        // if (config.orientation == Configuration.ORIENTATION_LANDSCAPE){
        // //横屏，比如 480x320
        // }else if(config.orientation == Configuration.ORIENTATION_PORTRAIT){
        // //竖屏 ，标准模式 320x480
        // }else if(config.hardKeyboardHidden ==
        // Configuration.KEYBOARDHIDDEN_NO){
        // //横屏，物理键盘滑出了
        // }else if(config.hardKeyboardHidden ==
        // Configuration.KEYBOARDHIDDEN_YES){
        // //竖屏，键盘隐藏了
        // }
        return config.orientation;
    }
}
