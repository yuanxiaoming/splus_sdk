/**
 * @Title: CustomProgressBar.java
 * @Package: com.sanqi.android.sdk.widget
 * Copyright: Copyright (c) 2013
 * Company: 上海三七玩网络科技有限公司
 * @author 李剑锋
 * @date 2013年10月11日 下午1:33:43
 * @version 1.0
 */

package com.android.splus.sdk.widget;

import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * @ClassName:xiaoming.yuan
 * @author 李剑锋
 * @date 2013年10月11日 下午1:33:43
 */
public class CustomProgressBar extends ImageView {
    private Activity activity;

    private AnimationDrawable loadingAnimationDrawable;

    private Bitmap loadingBitmap;

    private final int duration = 100;

    public CustomProgressBar(Activity activity) {
        super(activity);
        this.activity = activity;
        setFocusable(true);
        setFocusableInTouchMode(true);

        loadingBitmap = BitmapFactory.decodeResource(getResources(), getDrawableId(KR.drawable.splus_splash_loading));

        loadingAnimationDrawable = new AnimationDrawable();
        int orientationDegree = 0;
        while (orientationDegree <= 360) {
            loadingAnimationDrawable.addFrame(adjustDrawableRotation(orientationDegree), duration);
            orientationDegree += 30;
        }

        setImageDrawable(loadingAnimationDrawable);
        loadingAnimationDrawable.setOneShot(false);
        loadingAnimationDrawable.start();
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年10月10日 上午11:54:40
     * @param orientationDegree
     * @return
     */
    private Drawable adjustDrawableRotation(int orientationDegree) {
        // Canvas抗锯齿
        PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        Matrix matrix = new Matrix();
        matrix.setRotate(orientationDegree, (float) loadingBitmap.getWidth() / 2, (float) loadingBitmap.getHeight() / 2);
        Bitmap bitmap = Bitmap.createBitmap(loadingBitmap.getHeight(), loadingBitmap.getWidth(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        // Paint抗锯齿
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(bitmap);
        canvas.setDrawFilter(pfd);
        canvas.drawBitmap(loadingBitmap, matrix, paint);

        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        return drawable;
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

}
