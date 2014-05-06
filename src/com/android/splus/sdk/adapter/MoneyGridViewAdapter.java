/**
 * @Title: MoneyGridViewAdapter.java
 * @Package com.android.splus.sdk.parse
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-3-11 下午5:07:42
 * @version V1.0
 */

package com.android.splus.sdk.adapter;

import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @ClassName: MoneyGridViewAdapter
 * @author xiaoming.yuan
 * @date 2014-3-11 下午5:07:42
 */

public class MoneyGridViewAdapter extends BaseAdapter {
    private static final String TAG = "MoneyGridViewAdapter";

    private int[] mMoneyArray;

    private Context mContext;

    private int mMoneyIndex = 0;

    public MoneyGridViewAdapter(int[] moneyArray, Context context) {
        this.mMoneyArray = moneyArray;
        this.mContext = context;
    }

    public void changeMoneyType(int[] moneyArray) {
        this.mMoneyArray = moneyArray;
    }

    public int[] getMoneyArray() {
        return this.mMoneyArray;
    }

    @Override
    public int getCount() {
        return mMoneyArray != null ? mMoneyArray.length : 0;
    }

    @Override
    public Object getItem(int position) {
        return mMoneyArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Title: getView Description:
     * 
     * @param position
     * @param convertView
     * @param parent
     * @return
     * @see android.widget.Adapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = null;
        if (convertView == null) {
            textView = new TextView(mContext);
            textView.setBackgroundDrawable(mContext.getResources().getDrawable(ResourceUtil.getDrawableId(mContext, KR.drawable.splus_recharge_money_item_selector)));
            textView.setMaxWidth(75);
            textView.setMinHeight(75);
            textView.setGravity(Gravity.CENTER);
            convertView = textView;
        } else {
            textView = (TextView) convertView;
        }
        textView.setText(mMoneyArray[position] + "元");
        int selectorId = ResourceUtil.getDrawableId(mContext, KR.drawable.splus_recharge_money_item_press);
        int unselectorId = ResourceUtil.getDrawableId(mContext, KR.drawable.splus_recharge_money_item_normal);

        if (mMoneyIndex == position) {
            Drawable dw = mContext.getResources().getDrawable(selectorId);
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundDrawable(dw);
        } else {
            Drawable dw = mContext.getResources().getDrawable(unselectorId);
            textView.setTextColor(Color.BLACK);
            textView.setBackgroundDrawable(dw);
        }

        return convertView;

    }

    public void setMoneyIndex(int position) {
        this.mMoneyIndex = position;
        notifyDataSetChanged();
    }

}
