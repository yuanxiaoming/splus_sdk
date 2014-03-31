
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

import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.phone.Phoneuitl;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
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

    /**
     * showWelcomeToast(账号提示框) (这里描述这个方法适用条件 – 可选)
     *
     * @param context
     * @param userName void
     * @exception
     * @since 1.0.0 xiaoming.yuan
     */
    public static void showPassportToast(Activity context, String userName) {
        Toast toast = new Toast(context);
        FrameLayout view = new FrameLayout(context);
        FrameLayout.LayoutParams framParams = new LayoutParams(LayoutParams.MATCH_PARENT,
               CommonUtil.dip2px(context,45));
        view.setLayoutParams(framParams);
        view.setBackgroundResource(ResourceUtil
                .getDrawableId(context, KR.drawable.splus_login_success_toast_background));
        LayoutParams ivParam = new LayoutParams(CommonUtil.dip2px(context,80),
                CommonUtil.dip2px(context,45));
        ivParam.gravity = Gravity.CENTER_VERTICAL;
        ivParam.bottomMargin = 10;
        ivParam.topMargin = 10;
        ivParam.leftMargin = 10;
        ivParam.rightMargin = 5;
        ImageView iv = new ImageView(context);
        iv.setBackgroundResource(ResourceUtil.getDrawableId(context,
                KR.drawable.splus_login_success_toast_logo));
        iv.setScaleType(ScaleType.FIT_XY);
        view.addView(iv, ivParam);
        TextView tv = new TextView(context);
        tv.setSingleLine();
        tv.setTextSize(14);
        tv.setTextColor(Color.WHITE);
        tv.setText(userName + "," + "欢迎您回来！");
        tv.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams tvParams = new LayoutParams(Phoneuitl.getWpixels(context),
                LayoutParams.MATCH_PARENT);
        tvParams.gravity = Gravity.CENTER;
        view.addView(tv, tvParams);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
        toast.setDuration(5);
        toast.show();
    }

}

