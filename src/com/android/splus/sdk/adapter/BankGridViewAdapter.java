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

public class BankGridViewAdapter extends BaseAdapter {
    private static final String TAG = "MoneyGridViewAdapter";

    private String[] mBankArray;

    private Context mContext;

    private int mIndex = 0;

    public BankGridViewAdapter(String[] bankArray, Context context) {
        this.mBankArray = bankArray;
        this.mContext = context;
    }

    public void changeMoneyType(String[] bankArray) {
        this.mBankArray = bankArray;
    }

    public String[] getMoneyArray() {
        return this.mBankArray;
    }

    @Override
    public int getCount() {
        return mBankArray != null ? mBankArray.length : 0;
    }

    @Override
    public Object getItem(int position) {
        return mBankArray[position];
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
            textView.setSingleLine();
            textView.setGravity(Gravity.CENTER);
            convertView = textView;
        } else {
            textView = (TextView) convertView;
        }
        textView.setText(mBankArray[position]);
        int selectorId = ResourceUtil.getDrawableId(mContext, KR.drawable.splus_recharge_money_item_press);
        int unselectorId = ResourceUtil.getDrawableId(mContext, KR.drawable.splus_recharge_money_item_normal);

        if (mIndex == position) {
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

    public void setIndex(int position) {
        this.mIndex = position;
        notifyDataSetChanged();
    }

}
