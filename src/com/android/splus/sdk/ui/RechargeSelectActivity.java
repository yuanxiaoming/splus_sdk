
 /**
 * @Title: RechargeSelectActivity.java
 * @Package com.android.splus.sdk.ui
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-3-10 下午3:23:32
 * @version V1.0
 */

 package com.android.splus.sdk.ui;

import com.android.splus.sdk.manager.ExitAppUtils;
import com.android.splus.sdk.model.RechargeTypeModel;
import com.android.splus.sdk.ui.recharge.RechargeSelectPage;
import com.android.splus.sdk.ui.recharge.RechargeSelectPage.RechargeItemClick;
import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.toast.ToastUtil;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;


/**
 * @ClassName: RechargeSelectActivity
 * @author xiaoming.yuan
 * @date 2014-3-10 下午3:23:32
 */

public class RechargeSelectActivity extends BaseActivity  {
    private static final String TAG = "RechargeSelectActivity";

    private ImageButton recharge_title_left_backbtn;

    private ImageButton recharge_title_right_btn;

    private TextView recharge_titlr_middle_text;

    /**
     * 页面切换动画
     */
    private Animation anim_in_into, anim_out_into, anim_in_back, anim_out_back;

    private ViewFlipper vf_recharge_center;


    public int anim_time = 500;

    /**
     * 当前的页面
     */
    private String currentPage = RechargeSelectPage.class.getName();

    private RechargeSelectPage mRechargeSelectPage;



    /**
     * Title: loadViewLayout
     * Description:
     * @see com.android.splus.sdk.ui.BaseActivity#loadViewLayout()
     */
    @Override
    protected void loadViewLayout() {
        setContentView(KR.layout.splus_recharge_activity);

    }

    /**
     * Title: findViewById
     * Description:
     * @see com.android.splus.sdk.ui.BaseActivity#findViewById()
     */
    @Override
    protected void findViewById() {

        recharge_title_left_backbtn = (ImageButton) findViewById(KR.id.splus_recharge_title_bar_left_button);
        recharge_title_right_btn = (ImageButton) findViewById(KR.id.splus_recharge_title_bar_right_button);
        recharge_titlr_middle_text = (TextView) findViewById(KR.id.splus_recharge_title_bar_middle_title);
        recharge_titlr_middle_text.setTextColor(Color.WHITE);
        recharge_titlr_middle_text.setTextSize(CommonUtil.dip2px(this, 10));
        recharge_titlr_middle_text.setText(KR.string.splus_recharge_title_bar_middle_tips);
        vf_recharge_center = (ViewFlipper) findViewById(KR.id.splus_recharge_center_views);


        mRechargeSelectPage = new RechargeSelectPage(this, getPassport());
        addView(mRechargeSelectPage, RechargeSelectPage.class.getName());
        mRechargeSelectPage.setOnRechargeItemClick(new RechargeItemClick() {

            @Override
            public void onRechargeItemClick(View v, RechargeTypeModel rechargeTypeModel) {
               ToastUtil.showToast(mContext, rechargeTypeModel.getImgIcon()+rechargeTypeModel.getRechargeType());

            }
        });




        anim_in_into = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        anim_in_into.setDuration(anim_time);
        anim_out_into = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -1, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        anim_out_into.setDuration(anim_time);

        anim_in_back = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1,
                Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        anim_in_back.setDuration(anim_time);
        anim_out_back = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        anim_out_back.setDuration(anim_time);
    }




    /**
     * Title: setListener
     * Description:
     * @see com.android.splus.sdk.ui.BaseActivity#setListener()
     */
    @Override
    protected void setListener() {
        recharge_title_left_backbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recharge_title_right_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });


    }

    /**
     * Title: processLogic
     * Description:
     * @see com.android.splus.sdk.ui.BaseActivity#processLogic()
     */
    @Override
    protected void processLogic() {
    }


    /**
     * 页面添加
     *
     * @author xiaoming.yuan
     * @date 2013年9月30日 下午6:08:35
     * @param v
     * @param tag
     */
    private void addView(View v, String tag) {
        View view = vf_recharge_center.getCurrentView();
        if (v.equals(view)) {
            currentPage = tag;
            return;
        }

        int count = vf_recharge_center.getChildCount();
        boolean hasView = false;
        for (int i = 0; i < count; i++) {
            view = vf_recharge_center.getChildAt(i);
            if (view.equals(v)) {
                hasView = true;
                break;
            }
        }
        if (hasView) {
            vf_recharge_center.removeView(v);
        }

        vf_recharge_center.addView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        currentPage = tag;
        showNextPage();
    }


    /**
     * 显示下一个页面
     *
     * @author xiaoming.yuan
     * @date 2013年9月30日 下午5:20:09
     */
    private void showNextPage() {
        vf_recharge_center.setInAnimation(anim_in_into);
        vf_recharge_center.setOutAnimation(anim_out_into);
        vf_recharge_center.showNext();
        hideSoftInput(this);
    }

    /**
     * Title: finish Description:
     *
     * @see android.app.Activity#finish()
     */
    @Override
    public void finish() {
        super.finish();
        ExitAppUtils.getInstance().exit();

    }
}

