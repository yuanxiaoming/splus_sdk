
 /**
 * @Title: ProgressDialogUtil.java
 * @Package com.android.cansh.sdk.utils.progress
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-2-26 下午2:15:08
 * @version V1.0
 */

 package com.android.splus.sdk.utils.progressDialog;

import com.android.splus.sdk.widget.CustomProgressDialog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

/**
 * @ClassName: ProgressDialogUtil
 * @author xiaoming.yuan
 * @date 2014-2-26 下午2:15:08
 */

public class ProgressDialogUtil {
    private static final String TAG = "ProgressDialogUtil";
    /**
     * @Title: showProgress(这显示进度条)
     * @author xiaoming.yuan
     * @data 2013-8-10 下午3:47:56
     * @param context 环境
     * @param title 标题
     * @param message 信息
     * @param indeterminate 确定性
     * @param cancelable cancelable 可撤销
     * @return ProgressDialog 返回类型
     */
    public static ProgressDialog showProgress(Context context, CharSequence title,
            CharSequence message, boolean indeterminate, boolean cancelable) {
        CustomProgressDialog dialog = new CustomProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.show();
        return dialog;
    }

    /**
     * @Title: showInfoDialog(一般的对话框)
     * @author xiaoming.yuan
     * @data 2013年9月4日 下午1:15:22
     * @param context
     * @param strTitle
     * @param message
     * @param icon
     * @param onClickOKListener
     * @param okStr
     * @param onClickCancleListener
     * @param cancleStr
     * @param cancle void 返回类型
     */
    public static void showInfoDialog(Context context, String strTitle, String message,
            Integer icon, DialogInterface.OnClickListener onClickOKListener, String okStr,
            DialogInterface.OnClickListener onClickCancleListener, String cancleStr, boolean cancle) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
        if (icon != null) {
            localBuilder.setIcon(icon);
        }
        localBuilder.setTitle(strTitle);
        localBuilder.setMessage(message);
        okStr = TextUtils.isEmpty(okStr) ? "YES" : okStr;
        localBuilder.setPositiveButton(okStr, onClickOKListener);
        cancleStr = TextUtils.isEmpty(cancleStr) ? "NO" : cancleStr;
        if (onClickCancleListener != null) {
            localBuilder.setNegativeButton(cancleStr, onClickCancleListener);
        }
        localBuilder.setCancelable(cancle);
        AlertDialog dialog = localBuilder.create();
        dialog.show();
    }

    /**
     * @Title: showInfoDialog(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2013-8-10 下午3:46:11
     * @param: context
     * @param: strTitle
     * @param: message
     * @param: icon
     * @param: onClickListener
     * @return void 返回类型
     */
    public static void showInfoDialog(Context context, String strTitle, String message, int icon,
            DialogInterface.OnClickListener onClickOKListener,
            DialogInterface.OnClickListener onClickCancleListener, boolean cancle) {

        AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
        if (icon != 0) {
            localBuilder.setIcon(icon);
        }
        localBuilder.setTitle(strTitle);
        localBuilder.setMessage(message);
        localBuilder.setPositiveButton("YES", onClickOKListener);
        if (onClickCancleListener != null) {
            localBuilder.setNegativeButton("NO", onClickCancleListener);
        }
        localBuilder.setCancelable(cancle);
        AlertDialog dialog = localBuilder.create();
        dialog.show();

    }

}

