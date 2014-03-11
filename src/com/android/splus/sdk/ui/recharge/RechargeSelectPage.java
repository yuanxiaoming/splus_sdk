/**
 * @Title: RechargeSelect.java
 * @Package com.android.splus.sdk.ui.recharge
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-3-10 下午5:18:52
 * @version V1.0
 */

package com.android.splus.sdk.ui.recharge;

import com.android.splus.sdk.R;
import com.android.splus.sdk.adapter.RechargeTypeAdapter;
import com.android.splus.sdk.model.RechargeTypeModel;
import com.android.splus.sdk.ui.SplusPayManager;
import com.android.splus.sdk.utils.phone.Phoneuitl;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.widget.CustomGridView;
import com.canhe.android.sdk.person.PersonCenter.PersonClickListener;
import com.sanqi.android.sdk.model.UserData;
import com.sanqi.android.sdk.ui._37WanPayManager;
import com.sanqi.android.sdk.util.AppUtil;
import com.sanqi.android.sdk.util.CommonUtil;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @ClassName: RechargeSelect
 * @author xiaoming.yuan
 * @date 2014-3-10 下午5:18:52
 */

public class RechargeSelectPage extends LinearLayout {
    private static final String TAG = "RechargeSelect";

    private TextView recharge_username;

    private TextView recharge_select_head_tips;

    private CustomGridView recharge_type_gv;

    private String mPassport;

    private Activity mActivity;

    private RechargeTypeAdapter mRechargeTypeAdapter;

    private ArrayList<RechargeTypeModel> mRechargeTypeArrayList;

    private String mImgICON[] = new String[] {
            KR.drawable.splus_recharge_by_alipay_fast_normal,
            KR.drawable.splus_recharge_by_alipay_fast_normal,
            KR.drawable.splus_recharge_by_alipay_fast_normal,
            KR.drawable.splus_recharge_by_alipay_fast_normal,
            KR.drawable.splus_recharge_by_alipay_fast_normal,
            KR.drawable.splus_recharge_by_alipay_fast_normal,
            KR.drawable.splus_recharge_by_alipay_fast_normal,
            KR.drawable.splus_recharge_by_alipay_fast_normal
    };

    private Integer mRechargeType[] = new Integer[] {
            0, 1, 2, 3, 4, 5, 6, 7
    };

    private RechargeItemClick mRechargeItemClick;

    /**
     * @Title: RechargeSelect
     * @Description:(这里用一句话描述这个方法的作用)
     * @param context
     * @throws
     */

    public RechargeSelectPage(Activity activity, String passport) {
        super(activity);
        this.mActivity = activity;
        this.mPassport = passport;
        inflate(activity,
                ResourceUtil.getLayoutId(activity, KR.layout.splus_recharge_select_layout), this);
        findViews();
        initViews();
        setlistener();

    }

    /**
     * @Title: findViews(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-10 下午5:44:00 void 返回类型
     */

    private void findViews() {
        recharge_username = (TextView) findViewById(R.id.splus_recharge_select_username);
        recharge_select_head_tips = (TextView) findViewById(R.id.splus_recharge_select_head_tips);
        recharge_type_gv = (CustomGridView) findViewById(R.id.splus_recharge_type_gridview_select);

        recharge_username.setGravity(Gravity.CENTER);
        recharge_username.setTextColor(ResourceUtil.getDrawableId(mActivity,
                KR.drawable.splus_recharge_type_textcolor_selector));

        recharge_username.setTextColor(Color.BLACK);
        recharge_username.setText(mPassport);
        recharge_select_head_tips.setText(KR.string.splus_recharge_select_head_tips);
    }

    /**
     * @Title: initViews(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-10 下午5:42:20 void 返回类型
     */

    private void initViews() {
        mRechargeTypeArrayList = new ArrayList<RechargeTypeModel>();
        RechargeTypeModel rechargeTypeModel = null;
        for (int i = 0; i < mImgICON.length; i++) {
            rechargeTypeModel = new RechargeTypeModel(mImgICON[i], mRechargeType[i]);
            mRechargeTypeArrayList.add(rechargeTypeModel);
        }

        int orientation = Phoneuitl.getOrientation(mActivity);
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            recharge_type_gv.setNumColumns(4);
            recharge_type_gv.setGravity(Gravity.CENTER);
            recharge_type_gv.setLayoutParams(new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER));
            recharge_type_gv.setVerticalSpacing(20);

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            recharge_type_gv.setNumColumns(3);
            recharge_type_gv.setLayoutParams(new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER));
            recharge_type_gv.setPadding(5, 50, 5, 50);
            recharge_type_gv.setVerticalSpacing(40);

        }
        recharge_type_gv.setGravity(Gravity.CENTER);
        recharge_type_gv.setSelector(android.R.color.transparent);
        mRechargeTypeAdapter = new RechargeTypeAdapter(mRechargeTypeArrayList, mActivity);
        recharge_type_gv.setAdapter(mRechargeTypeAdapter);

    }

    /**
     * @Title: setlistener(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 上午11:17:31 void 返回类型
     */

    private void setlistener() {

        recharge_type_gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RechargeTypeModel item = mRechargeTypeAdapter.getItem(position);
                mRechargeItemClick.onRechargeItemClick(view, item);
            }
        });
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午4:28:56
     * @param listener
     */
    public void setOnRechargeItemClick(RechargeItemClick listener) {
        this.mRechargeItemClick = listener;
    }

    public interface RechargeItemClick {
        public void onRechargeItemClick(View v, RechargeTypeModel rechargeTypeModel);

    }

}
