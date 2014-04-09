/**
 * @Title: ScrollForeverTextView.java
 * @Package com.sanqi.android.sdk.widget
 * Copyright: Copyright (c) 2013
 * Company:上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013-11-18 上午11:35:38
 * @version V1.0
 */

package com.android.splus.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @ClassName: ScrollForeverTextView
 * @author xiaoming.yuan
 * @date 2013-11-18 上午11:35:38
 */

public class ScrollForeverTextView extends TextView {
    private static final String TAG = "ScrollForeverTextView";

    /**
     * @Title: ScrollForeverTextView
     * @Description:(这里用一句话描述这个方法的作用)
     * @param context
     * @throws
     */

    public ScrollForeverTextView(Context context) {
        super(context);
    }

    public ScrollForeverTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollForeverTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

}
