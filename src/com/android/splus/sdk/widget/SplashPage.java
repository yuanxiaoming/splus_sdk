/**
 * @Title: SplashPage.java
 * @Package: com.sanqi.android.sdk.widget
 * Copyright: Copyright (c) 2013
 * @author xiaoming.yuan
 * @date 2013年10月9日 下午10:03:07
 * @version 1.0
 */

package com.android.splus.sdk.widget;

import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @ClassName:WelcomeView
 * @author xiaoming.yuan
 * @date 2013年10月9日 下午10:03:07
 */
public class SplashPage extends LinearLayout {
    private ImageView iv_logo;

    private CustomProgressBar mBar;

    private Activity activity;

    public SplashPage(Activity activity) {
        super(activity);
        this.activity = activity;
        init();
        mBar = new CustomProgressBar(activity);
        iv_logo = new ImageView(activity);
        iv_logo.setImageResource(getDrawableId(KR.drawable.splus_login_success_toast_logo));
        iv_logo.setPadding(0, 0, 0, (int) dpTopx(10));
        addView(iv_logo);
        addView(mBar);

    }

    private void init() {
        setBackgroundColor(Color.parseColor("#FFF2F2F2"));
       // setBackgroundResource(getDrawableId(KR.drawable.splus_splash_background));
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        setFocusable(true);
        setFocusableInTouchMode(true);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    /**
     * dp转px
     *
     * @author xiaoming.yuan
     * @date 2013年10月9日 下午10:45:10
     * @param dp
     * @return
     */
    private float dpTopx(float dp) {
        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources()
                .getDisplayMetrics());
        return size;

    }

    /**
     * 获取图片id
     *
     * @author xiaoming.yuan
     * @date 2013年10月9日 下午10:45:55
     * @param resName
     * @return
     */
    private Integer getDrawableId(String resName) {
        Integer resId = ResourceUtil.getDrawableId(activity, resName);
        return resId;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return true;
    }

}
