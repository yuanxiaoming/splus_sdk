/**
 * @Title: RechargeAlipayPage.java
 * @Package com.android.splus.sdk.ui.recharge
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-3-11 下午1:37:06
 * @version V1.0
 */

package com.android.splus.sdk.ui.recharge;

import com.android.splus.sdk.adapter.MoneyGridViewAdapter;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.utils.date.DateUtil;
import com.android.splus.sdk.utils.phone.Phoneuitl;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.utils.toast.ToastUtil;
import com.android.splus.sdk.widget.CustomGridView;

import android.app.Activity;
import android.content.res.Configuration;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName: RechargeAlipayPage
 * @author xiaoming.yuan
 * @date 2014-3-11 下午1:37:06
 */

public class RechargeAlipayPage extends LinearLayout {
    private static final String TAG = "RechargeAlipayPage";

    private UserModel mUserModel;

    private Activity mActivity;

    private TextView recharge_money_tips;

    private TextView recharge_money_ratio_tv;

    private Button recharge_comfirm_btn;

    private EditText recharge_money_custom_et;

    private CustomGridView recharge_money_gridview_select;

    private MoneyGridViewAdapter mMoneyGridViewAdapter;

    private static final int[] ALIPAY_MONEY = { 10, 20, 30, 50, 100, 200, 300, 500, 1000, 2000, 3000, 5000 };



    public RechargeAlipayPage(UserModel userModel, Activity activity) {
        super(activity);
        this.mUserModel = userModel;
        this.mActivity = activity;
        inflate(activity,
                ResourceUtil.getLayoutId(activity, KR.layout.splus_recharge_alipay_layout), this);
        findViews();
        initViews();
        setlistener();

    }

    /**
     * @Title: findViews(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午1:42:16 void 返回类型
     */

    private void findViews() {
        recharge_money_tips = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_tips));
        recharge_money_custom_et = (EditText) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_custom_et));
        recharge_comfirm_btn = (Button) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_comfirm_btn));
        recharge_comfirm_btn.setText(KR.string.splus_recharge_comfirm_tips);
        recharge_money_ratio_tv = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_ratio_tv));
        recharge_money_gridview_select = (CustomGridView) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_recharge_money_gridview_select));

        recharge_money_tips.setText(KR.string.splus_recharge_select_head_tips);
        recharge_money_custom_et.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
        recharge_money_custom_et.setHint(KR.string.splus_recharge_custom_et_tips);
        recharge_money_ratio_tv.setHint(KR.string.splus_recharge_ratio_tv_tips);
    }

    /**
     * @Title: initViews(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午1:42:09 void 返回类型
     */

    private void initViews() {


        int orientation = Phoneuitl.getOrientation(mActivity);
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            recharge_money_gridview_select.setNumColumns(6);
            recharge_money_gridview_select.setColumnWidth(40);
            recharge_money_gridview_select.setGravity(Gravity.CENTER);
            recharge_money_gridview_select.setLayoutParams(new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER));
            recharge_money_gridview_select.setVerticalSpacing(30);
            recharge_money_gridview_select.setHorizontalSpacing(20);

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            recharge_money_gridview_select.setNumColumns(3);
            recharge_money_gridview_select.setLayoutParams(new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER));
            recharge_money_gridview_select.setPadding(5, 50, 5, 50);
            recharge_money_gridview_select.setVerticalSpacing(40);
            recharge_money_gridview_select.setHorizontalSpacing(20);

        }
        recharge_money_gridview_select.setGravity(Gravity.CENTER_HORIZONTAL);
        recharge_money_gridview_select.setSelector(android.R.color.transparent);

        mMoneyGridViewAdapter=new MoneyGridViewAdapter(ALIPAY_MONEY, mActivity);

        recharge_money_gridview_select.setAdapter(mMoneyGridViewAdapter);





    }

    /**
     * @Title: setlistener(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午1:42:12 void 返回类型
     */

    private void setlistener() {

        recharge_money_gridview_select.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mMoneyGridViewAdapter.setMoneyIndex(position);
                        ToastUtil.showToast(mActivity, position+"");

                    }
        });
    }


    /**
    *
    * @Title: getRatio(请求兑换比率和显示名称)
    * @author xiaoming.yuan
    * @data 2013-10-2 下午11:22:24 void 返回类型
    */
    private void getRatio() {
        long time = DateUtil.getUnixTime();

    }
}
