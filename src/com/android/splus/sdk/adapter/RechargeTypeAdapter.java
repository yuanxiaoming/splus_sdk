/**
 * @Title: RechargeTypeAdapter.java
 * @Package com.android.splus.sdk.adapter
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-3-11 上午10:48:05
 * @version V1.0
 */

package com.android.splus.sdk.adapter;

import com.android.splus.sdk.model.RechargeTypeModel;
import com.android.splus.sdk.utils.r.ResourceUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * @ClassName: RechargeTypeAdapter
 * @author xiaoming.yuan
 * @date 2014-3-11 上午10:48:05
 */

public class RechargeTypeAdapter extends BaseAdapter {
    private ArrayList<RechargeTypeModel> mRechargeTypeArrayList;

    private Context mContext;

    public RechargeTypeAdapter(ArrayList<RechargeTypeModel> rechargeTypeArrayList, Context context) {
        super();
        this.mRechargeTypeArrayList = rechargeTypeArrayList;
        this.mContext = context;
    }

    /**
     * Title: getCount Description:
     * 
     * @return
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return mRechargeTypeArrayList != null ? mRechargeTypeArrayList.size() : 0;
    }

    /**
     * Title: getItem Description:
     * 
     * @param position
     * @return
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public RechargeTypeModel getItem(int position) {
        return mRechargeTypeArrayList.get(position);
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
        ImageView mImageView;
        if (convertView == null) {
            mImageView = new ImageView(mContext);
            // mImageView.setLayoutParams(new GridView.LayoutParams(240,
            // 175));//设置ImageView对象布局
            mImageView.setAdjustViewBounds(false);// 设置边界对齐
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);// 设置刻度的类型
            mImageView.setPadding(8, 8, 8, 8);// 设置间距
            convertView = mImageView;
        } else {
            mImageView = (ImageView) convertView;
        }
        mImageView.setBackgroundDrawable(mContext.getResources().getDrawable(ResourceUtil.getDrawableId(mContext, mRechargeTypeArrayList.get(position).getImgIcon())));

        return convertView;

    }

}
