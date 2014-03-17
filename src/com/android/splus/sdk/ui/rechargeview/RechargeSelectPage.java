/**
 * @Title: RechargeSelect.java
 * @Package com.android.splus.sdk.ui.recharge
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-3-10 下午5:18:52
 * @version V1.0
 */

package com.android.splus.sdk.ui.rechargeview;

import com.android.splus.sdk.adapter.RechargeTypeAdapter;
import com.android.splus.sdk.model.RechargeTypeModel;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.phone.Phoneuitl;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.widget.CustomGridView;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.text.Html;
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

    private float mRenminbi = 0; // 人民币

    private Integer mType;

    private RechargeTypeAdapter mRechargeTypeAdapter;

    private ArrayList<RechargeTypeModel> mRechargeTypeArrayList;
    private RechargeTypeModel rechargeTypeModel = null;


    private RechargeItemClick mRechargeItemClick;

    /**
     * @Title: RechargeSelect
     * @Description:(这里用一句话描述这个方法的作用)
     * @param context
     * @throws
     */

    public RechargeSelectPage(Activity activity, String passport,Integer type,Float money) {
        super(activity);
        this.mActivity = activity;
        this.mPassport = passport;
        this.mRenminbi=money;
        this.mType=type;
        inflate(activity,ResourceUtil.getLayoutId(activity, KR.layout.splus_recharge_select_layout), this);
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
        recharge_username = (TextView) findViewById(ResourceUtil.getId(mActivity,KR.id.splus_recharge_select_username));
        recharge_select_head_tips = (TextView) findViewById(ResourceUtil.getId(mActivity,KR.id.splus_recharge_select_head_tips));
        recharge_type_gv = (CustomGridView) findViewById(ResourceUtil.getId(mActivity,KR.id.splus_recharge_type_gridview_select));

        recharge_username.setGravity(Gravity.CENTER);
        recharge_username.setFocusable(true);
        recharge_username.setFocusableInTouchMode(true);
        recharge_username.requestFocus();
        recharge_username.setTextColor(ResourceUtil.getDrawableId(mActivity,
                KR.drawable.splus_recharge_type_textcolor_selector));

        recharge_username.setTextColor(Color.BLACK);
        recharge_username.setText(Html.fromHtml("欢迎您: <font color=#FE8E35>"+mPassport+"</font>"));
        recharge_select_head_tips.setText(KR.string.splus_recharge_select_head_tips);
    }

    /**
     * @Title: initViews(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-10 下午5:42:20 void 返回类型
     */

    private void initViews() {
        mRechargeTypeArrayList = new ArrayList<RechargeTypeModel>();

        if(mType==Constant.RECHARGE_BY_QUATO){
            showRechargeType(mRenminbi);

        }else if(mType==Constant.RECHARGE_BY_NO_QUATO){
            for (int i = 0; i < Constant.IMG_ICON.length; i++) {
                rechargeTypeModel = new RechargeTypeModel(Constant.IMG_ICON[i],Constant.RECHARGE_TYPE[i],Constant.PAYWAY_TYPE[i]);
                mRechargeTypeArrayList.add(rechargeTypeModel);
            }
        }

        int orientation = Phoneuitl.getOrientation(mActivity);
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            recharge_type_gv.setNumColumns(4);
            recharge_type_gv.setColumnWidth(40);
            recharge_type_gv.setGravity(Gravity.CENTER);
            recharge_type_gv.setLayoutParams(new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER));
            recharge_type_gv.setVerticalSpacing(40);
            recharge_type_gv.setHorizontalSpacing(20);

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            recharge_type_gv.setNumColumns(3);
            recharge_type_gv.setLayoutParams(new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER));
            recharge_type_gv.setPadding(5, 50, 5, 50);
            recharge_type_gv.setVerticalSpacing(40);
            recharge_type_gv.setHorizontalSpacing(20);

        }
        recharge_type_gv.setGravity(Gravity.CENTER_HORIZONTAL);
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

    /**
     * 定额支付
     *
     * @param userMoney
     */
    private void showRechargeType(Float userMoney) {

        // 定额支付
        for (int i = 0; i <=Constant.UNION_PAY; i++) {
            if ((userMoney > 500&i==Constant.ALIPAY_CREDIT)||(userMoney > 500&i==Constant.ALIPAY_DEPOSIT)) {
                continue;
            }
            rechargeTypeModel = new RechargeTypeModel(Constant.IMG_ICON[i],Constant.RECHARGE_TYPE[i],Constant.PAYWAY_TYPE[i]);
            mRechargeTypeArrayList.add(rechargeTypeModel);
        }


        for (int i = 0; i < Constant.CHINA_MOBILE_MONEY.length; i++) {
            if (userMoney == Constant.CHINA_MOBILE_MONEY[i]) {
                rechargeTypeModel = new RechargeTypeModel(Constant.IMG_ICON[Constant.CHAIN_CMM],Constant.RECHARGE_TYPE[Constant.CHAIN_CMM],Constant.PAYWAY_TYPE[Constant.CHAIN_CMM]);
                mRechargeTypeArrayList.add(rechargeTypeModel);
                break;
            }
        }
        for (int i = 0; i < Constant.CHINA_UNICOM_MONEY.length; i++) {
            if (userMoney == Constant.CHINA_UNICOM_MONEY[i]) {
                rechargeTypeModel = new RechargeTypeModel(Constant.IMG_ICON[Constant.CHAIN_UNC],Constant.RECHARGE_TYPE[Constant.CHAIN_UNC],Constant.PAYWAY_TYPE[Constant.CHAIN_UNC]);
                mRechargeTypeArrayList.add(rechargeTypeModel);
                break;
            }
        }
        for (int i = 0; i < Constant.CHINA_SDCOMM_ONEY.length; i++) {
            if (userMoney == Constant.CHINA_SDCOMM_ONEY[i]) {
                rechargeTypeModel = new RechargeTypeModel(Constant.IMG_ICON[Constant.CHAIN_SD],Constant.RECHARGE_TYPE[Constant.CHAIN_SD],Constant.PAYWAY_TYPE[Constant.CHAIN_SD]);
                mRechargeTypeArrayList.add(rechargeTypeModel);
                break;
            }
        }

        rechargeTypeModel = new RechargeTypeModel(Constant.IMG_ICON[Constant.PERSON],Constant.RECHARGE_TYPE[Constant.PERSON],Constant.PAYWAY_TYPE[Constant.PERSON]);
        mRechargeTypeArrayList.add(rechargeTypeModel);
    }


}
