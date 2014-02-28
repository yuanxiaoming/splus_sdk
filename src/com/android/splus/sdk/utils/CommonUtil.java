/**
 * @Title: Common.java
 * @Package com.android.cansh.sdk.utils
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-2-26 下午4:36:39
 * @version V1.0
 */

package com.android.splus.sdk.utils;

import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.phone.Phoneuitl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.text.Editable;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

/**
 * @ClassName: Common
 * @author xiaoming.yuan
 * @date 2014-2-26 下午4:36:39
 */

public class CommonUtil {
    private static final String TAG = "Common";

    /**
     * @Title: deBug(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2013-8-15 下午2:34:47
     * @return String 返回类型
     */
    public static String getDebug() {
        return "0";
    }

    /**
     * @Title: 比较版本
     * @author xiaoming.yuan
     * @data 2013年9月3日 下午8:55:10
     * @param localVer
     * @param remoteVer
     * @return boolean 返回类型 true为远程版本较高
     */
    public static boolean compareVersion(String localVer, String remoteVer) {
        String remoteVers[] = remoteVer.split("\\.");
        String localVers[] = localVer.split("\\.");
        int len = remoteVers.length > localVers.length ? localVers.length : remoteVers.length;
        for (int i = 0; i < len; i++) {
            try {
                if (Integer.valueOf(remoteVers[i]) > Integer.valueOf(localVers[i])) {
                    return true;
                }
            } catch (Exception e) {
                if (remoteVers[i].compareTo(localVers[i]) > 0) {
                    return true;
                }
            }
        }

        return remoteVers.length > localVers.length ? true : false;
    }

    /**
     * @Title: killSDK(杀进程)
     * @author xiaoming.yuan
     * @data 2013-9-26 上午12:27:33
     * @param mContext void 返回类型
     */
    public static void killSDK(final Context mContext) {

        new Thread(new Runnable() {
            public void run() {

                int currentVersion = Build.VERSION.SDK_INT;
                try {
                    Thread.sleep(1000);
                    if (currentVersion > Build.VERSION_CODES.ECLAIR_MR1) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(startMain);
                        System.exit(0);
                    } else {
                        // android2.1
                        ActivityManager am = (ActivityManager) mContext
                                .getSystemService(Context.ACTIVITY_SERVICE);
                        am.restartPackage(mContext.getPackageName());
                    }
                } catch (Exception e) {
                }
            };
        }).start();

    }

    /**
     * setEditTextInputType(限制只能输入数字 大小写 下划线 同时限制最大长度) (这里描述这个方法适用条件 – 可选)
     *
     * @param edt void
     * @exception
     * @since 1.0.0 xiaoming.yuan
     */
    public static void setEditTextInputTypeAndMaxlength(Editable edt, int maxLength) {
        try {
            int lenght = edt.toString().length();
            if (lenght > 0) {
                char mid = edt.charAt(lenght - 1);

                if (mid >= 48 && mid <= 57) {// 数字
                    return;
                }
                if (mid >= 65 && mid <= 90) {// 大写字母
                    return;
                }
                if (mid >= 97 && mid <= 122) {// 小写字母
                    return;
                }
                if (mid == 95) {
                    return;
                }
                if (lenght <= maxLength) {
                    return;
                }
                edt.delete(lenght - 1, lenght);
            }
        } catch (Exception e) {
            LogHelper.i(TAG, "edittext error");
        }
    }

    /**
     * getFraLayoutParams(获取layoutparams参数) (这里描述这个方法适用条件 – 可选)
     *
     * @param context
     * @param widthLandScapeScale 横屏全屏宽度比例
     * @param widthPortScapeScale 竖屏全屏宽度比例
     * @param heightLandScapeScale 横屏全屏高度比例
     * @param heightPortScapeScale 竖屏全屏高度比例
     * @param gravity
     * @return FrameLayout.LayoutParams
     * @exception
     * @since 1.0.0 xiaoming.yuan
     */
    public static FrameLayout.LayoutParams getFrameLayoutParams(Context context,
            double widthLandScapeScale, double widthPortScapeScale, double heightLandScapeScale,
            double heightPortScapeScale, int gravity) {
        int width = (Phoneuitl.getWpixels(context));
        int height = (Phoneuitl.getHpixels(context));
        int orientation = Phoneuitl.getOrientation(context);
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            height = (int) (height * heightLandScapeScale);
            width = (int) (width * widthLandScapeScale);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            height = (int) (height * heightPortScapeScale);
            width = (int) (width * widthPortScapeScale);
        }
        LayoutParams dialogClauseparams = new LayoutParams(width, height);
        dialogClauseparams.gravity = gravity;
        return dialogClauseparams;
    }
}
