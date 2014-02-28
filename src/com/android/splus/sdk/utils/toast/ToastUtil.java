
/**
 * @Title: ToastUtil.java
 * @Package com.android.cansh.sdk.utils.toast
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-2-27 上午11:29:48
 * @version V1.0
 */

package com.android.splus.sdk.utils.toast;

import com.android.splus.sdk.utils.log.LogHelper;

import android.content.Context;
import android.widget.Toast;

/**
 * @ClassName: ToastUtil
 * @author xiaoming.yuan
 * @date 2014-2-27 上午11:29:48
 */

public class ToastUtil {
    private static final String TAG = "ToastUtil";

    /**
     * @Title: showToast(Toast 提示)
     * @author xiaoming.yuan
     * @data 2013-8-10 下午3:30:38
     * @param: context
     * @param: message
     * @return void 返回类型
     */
    public static void showToast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }
}

