
package com.android.splus.sdk.utils.r;

import android.content.Context;

/**
 * 资源读取类
 *
 * @ClassName: ResourceUtil
 * @author xiaoming.yuan
 * @date 2013-8-5 下午8:07:18
 */
public class ResourceUtil {

    /**
     * 获取layout
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getLayoutId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "layout", context.getPackageName());
    }

    /**
     * 获取string
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getStringId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "string", context.getPackageName());
    }

    /**
     * 获取drawable
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getDrawableId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
    }

    /**
     * 获取style
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getStyleId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "style", context.getPackageName());
    }

    /**
     * 获取id
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getId(Context context, String resName) {

        return context.getResources().getIdentifier(resName, "id", context.getPackageName());
    }

    /**
     * 获取color
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getColorId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "color", context.getPackageName());
    }

    /**
     * 获取getStyle
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getValuesId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "style", context.getPackageName());
    }
}
