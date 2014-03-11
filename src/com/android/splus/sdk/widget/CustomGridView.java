
/**
 * @Title: GridViewLayout.java
 * @Package com.sanqi.android.sdk.widget
 * Copyright: Copyright (c) 2013
 * Company:上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013-10-6 下午2:54:17
 * @version V1.0
 */

 package com.android.splus.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @ClassName: GridViewLayout
 * @author xiaoming.yuan
 * @date 2013-10-6 下午2:54:17
 */

public class CustomGridView extends GridView {
    private static final String TAG = "GridViewLayout";

     public CustomGridView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public CustomGridView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CustomGridView(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int expandSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

        }

}

