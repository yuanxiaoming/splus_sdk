/**
 * @Title: FloatToolBar.java
 * @Package: com.sanqi.android.sdk.ui
 * @Description: TODO(用一句话描述该文件做什么)
 * Copyright: Copyright (c) 2013
 * Company: 上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013年10月30日 下午11:15:56
 * @version 1.0
 */

package com.android.splus.sdk.ui;

import android.app.Activity;

/**
 * @ClassName:FloatToolBar
 * @author xiaoming.yuan
 * @date 2013年10月30日 下午11:15:56
 */
public class FloatToolBar {
    public static final String TAG = "FloatToolBar";

    private static FloatWindow mFloatButton;

    private FloatToolBar(Activity activity, boolean showlasttime, FloatToolBarAlign align, float position) {
        if(mFloatButton==null){
            mFloatButton = new FloatWindow(activity, showlasttime, align, position);
        }
    }

    static FloatToolBar getFloatToolBar(Activity activity, boolean showlasttime, FloatToolBarAlign align, float position) {
        return new FloatToolBar(activity, showlasttime, align, position);
    }

    /**
     * 隐藏
     *
     * @author xiaoming.yuan
     * @date 2013年10月30日 下午11:19:07
     */
    static void hide() {
        if (mFloatButton != null) {
            mFloatButton.hide();
        }
    }

    /**
     * 显示
     *
     * @author xiaoming.yuan
     * @date 2013年10月30日 下午11:19:45
     */
    static void show() {
        if (mFloatButton != null && !isShowing()) {
            mFloatButton.show();
        }
    };

    /**
     * @author xiaoming.yuan
     * @date 2013年10月30日 下午11:20:15
     */
    static void recycle() {
        if (mFloatButton != null) {
            mFloatButton.hide() ;
            mFloatButton.recycle();
            mFloatButton=null;
        }
    };

    /**
     * @author xiaoming.yuan
     * @date 2013年10月30日 下午11:21:01
     * @return
     */
    private static boolean isShowing() {
        if (mFloatButton != null) {
            return mFloatButton.isShowing();
        }
        return false;
    };

    /**
     * 位置
     *
     * @ClassName:FloatToolBarPlace
     * @author xiaoming.yuan
     * @date 2013年10月25日 下午1:49:56
     */
    public enum FloatToolBarAlign {
        /**
         * 显示在左边
         */
        Left,
        /**
         * 显示在右边
         */
        Right
    }
}
