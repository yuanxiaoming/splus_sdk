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

import com.android.splus.sdk.apiinterface.RechargeCallBack;
import com.android.splus.sdk.manager.ExitAppUtils;
import com.android.splus.sdk.model.RechargeTypeModel;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.ui.rechargeview.RechargeAlipayHtmlPage;
import com.android.splus.sdk.ui.rechargeview.RechargeAlipayPage;
import com.android.splus.sdk.ui.rechargeview.RechargeAlipayPage.AlipayHtmlClick;
import com.android.splus.sdk.ui.rechargeview.RechargeAlipayQuotaPage;
import com.android.splus.sdk.ui.rechargeview.RechargeAlipayQuotaPage.AlipayQuotaHtmlClick;
import com.android.splus.sdk.ui.rechargeview.RechargeCardPage;
import com.android.splus.sdk.ui.rechargeview.RechargeCardQuotaPage;
import com.android.splus.sdk.ui.rechargeview.RechargePersonPage;
import com.android.splus.sdk.ui.rechargeview.RechargeSelectPage;
import com.android.splus.sdk.ui.rechargeview.RechargeSelectPage.RechargeItemClick;
import com.android.splus.sdk.ui.rechargeview.RechargeUnionPayPage;
import com.android.splus.sdk.ui.rechargeview.RechargeUnionQuotaPayPage;
import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.phone.Phoneuitl;
import com.android.splus.sdk.utils.r.KR;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
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

public class RechargeSelectActivity extends BaseActivity {
    private static final String TAG = "RechargeSelectActivity";

    private Activity mActivity;

    private Integer mType = 0;

    private SplusPayManager mSplusPayManager;

    private RechargeCallBack mRechargeCallBack;

    private ImageButton recharge_title_left_backbtn;

    private ImageButton recharge_title_right_btn;

    private TextView recharge_titlr_middle_text;

    private String mPayway;

    private Float mMoney;

    /**
     * 页面切换动画
     */
    private Animation anim_in_into, anim_out_into, anim_in_back, anim_out_back;

    private ViewFlipper vf_recharge_center;

    public int mAnim_time = 500;

    private RechargeTypeModel mRechargeTypeModel;

    /**
     * 当前的页面
     */
    private String mCurrentPage = RechargeSelectPage.class.getName();

    private RechargeSelectPage mRechargeSelectPage;

    private RechargeAlipayPage mRechargeAlipayPage;

    private RechargeAlipayHtmlPage mRechargeAlipayHtmlPage;

    private RechargeCardPage mRechargeCardPage;

    private RechargePersonPage mRechargePersonPage;

    private RechargeUnionPayPage mRechargeUnionPayPage;

    private RechargeAlipayQuotaPage mRechargeAlipayQuotaPage;

    private RechargeCardQuotaPage mRechargeCardQuotaPage;

    private RechargeUnionQuotaPayPage mRechargeUnionQuotaPayPage;

    /**
     * Title: loadViewLayout Description:
     *
     * @see com.android.splus.sdk.ui.BaseActivity#loadViewLayout()
     */
    @Override
    protected void loadViewLayout() {
        setContentView(KR.layout.splus_recharge_activity);
        this.mActivity = this;
        mSplusPayManager = SplusPayManager.getInstance();
        mRechargeCallBack = mSplusPayManager.getRechargeCallBack();

    }

    /**
     * Title: findViewById Description:
     *
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

    }

    /**
     * Title: setListener Description:
     *
     * @see com.android.splus.sdk.ui.BaseActivity#setListener()
     */
    @Override
    protected void setListener() {
        recharge_title_left_backbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                goBack();
            }
        });
        recharge_title_right_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 密码找回界面
                Intent intent = new Intent(mActivity, PersonActivity.class);
                intent.putExtra(PersonActivity.INTENT_TYPE, PersonActivity.INTENT_SQ);
                mActivity.startActivity(intent);
            }
        });

    }

    /**
     * Title: processLogic Description:
     *
     * @see com.android.splus.sdk.ui.BaseActivity#processLogic()
     */
    @Override
    protected void processLogic() {
        mType = getIntent().getIntExtra(RechargeSelectActivity.class.getName(), 0);
        mMoney = mSplusPayManager.getMoney();
        if (mMoney == 0) {
            mType = Constant.RECHARGE_BY_NO_QUATO;
        }
        mRechargeSelectPage = new RechargeSelectPage(mActivity, getPassport(), mType, mMoney,mOrientation);
        addView(mRechargeSelectPage, RechargeSelectPage.class.getName());
        mRechargeSelectPage.setOnRechargeItemClick(mRechargeItemClick);

        anim_in_into = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        anim_in_into.setDuration(mAnim_time);
        anim_out_into = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -1, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        anim_out_into.setDuration(mAnim_time);

        anim_in_back = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1,
                Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        anim_in_back.setDuration(mAnim_time);
        anim_out_back = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        anim_out_back.setDuration(mAnim_time);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 页面返回事件
     *
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午2:24:30
     * @return
     */
    private void goBack() {
        hideSoftInput(mActivity);
        /**
         * 正在切换中
         */
        if (vf_recharge_center.isFlipping()) {
            return;
        }

        if (RechargeAlipayHtmlPage.class.getName().equals(mCurrentPage)
                && mRechargeAlipayHtmlPage != null) {
            if (mRechargeAlipayHtmlPage.goBack()) {
                return;
            }
        }

        int count = vf_recharge_center.getChildCount();
        if (count == 1) {
            // 第一个视图,结束当前activity
            mCurrentPage = "";
            onBackPressed();
        } else {
            vf_recharge_center.setInAnimation(anim_in_back);
            vf_recharge_center.setOutAnimation(anim_out_back);
            vf_recharge_center.showPrevious();
            vf_recharge_center.removeViewAt(count - 1);
            mCurrentPage = vf_recharge_center.getChildAt(count - 2).getClass().getName();
        }

        if ((RechargeAlipayPage.class.getName().equals(mCurrentPage) && mRechargeAlipayPage != null)
                || (RechargeAlipayQuotaPage.class.getName().equals(mCurrentPage) && mRechargeAlipayQuotaPage != null)) {

            switch (mRechargeTypeModel.getRechargeType()) {
                case Constant.ALIPAY_FAST:
                    recharge_titlr_middle_text.setText("支付宝快捷");
                    break;
                case Constant.ALIPAY_WAP:
                    recharge_titlr_middle_text.setText("支付宝网页");
                    break;
                case Constant.ALIPAY_DEPOSIT:
                    recharge_titlr_middle_text.setText("信用卡");
                    break;
                case Constant.ALIPAY_CREDIT:
                    recharge_titlr_middle_text.setText("储蓄卡");
                    break;
            }
        }

        if ((RechargeCardPage.class.getName().equals(mCurrentPage) && mRechargeCardPage != null)
                || (RechargeCardQuotaPage.class.getName().equals(mCurrentPage) && mRechargeCardQuotaPage != null)) {

            switch (mRechargeTypeModel.getRechargeType()) {
                case Constant.CHAIN_CMM:
                    recharge_titlr_middle_text.setText("移动卡");
                    break;
                case Constant.CHAIN_UNC:
                    recharge_titlr_middle_text.setText("联通卡");
                    break;
                case Constant.CHAIN_SD:
                    recharge_titlr_middle_text.setText("盛大卡");
                    break;
            }
        }

        if (RechargeSelectPage.class.getName().equals(mCurrentPage) && mRechargeSelectPage != null) {

            recharge_titlr_middle_text.setText(KR.string.splus_recharge_title_bar_middle_tips);
        }

        if (RechargePersonPage.class.getName().equals(mCurrentPage) && mRechargePersonPage != null) {

            recharge_titlr_middle_text.setText("人工充值");
        }

        if ((RechargeUnionPayPage.class.getName().equals(mCurrentPage) && mRechargeUnionPayPage != null)
                || (RechargeUnionQuotaPayPage.class.getName().equals(mCurrentPage) && mRechargeUnionQuotaPayPage != null)) {

            recharge_titlr_middle_text.setText("银联");
        }

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
            mCurrentPage = tag;
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
        mCurrentPage = tag;
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

    private RechargeItemClick mRechargeItemClick = new RechargeItemClick() {

        @Override
        public void onRechargeItemClick(View v, RechargeTypeModel rechargeTypeModel) {
            mPayway = rechargeTypeModel.getPayway();
            mRechargeTypeModel = rechargeTypeModel;
            switch (rechargeTypeModel.getRechargeType()) {
                case Constant.ALIPAY_FAST:
                    recharge_titlr_middle_text.setText("支付宝快捷");
                    if (mType == Constant.RECHARGE_BY_NO_QUATO) {
                        // 进入支付宝快捷页面
                        mRechargeAlipayPage = new RechargeAlipayPage(getUserData(), mActivity,
                                getDeviceno(), mSplusPayManager.getAppkey(),
                                mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                                mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                                mSplusPayManager.getPext(), mType, mPayway,mOrientation);
                        addView(mRechargeAlipayPage, RechargeAlipayPage.class.getName());
                        mRechargeAlipayPage.setOnAlipayHtmlClick(mAlipayHtmlClick);
                    } else if (mType == Constant.RECHARGE_BY_QUATO) {
                        mRechargeAlipayQuotaPage = new RechargeAlipayQuotaPage(getUserData(),
                                mActivity, getDeviceno(), mSplusPayManager.getAppkey(),
                                mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                                mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                                mSplusPayManager.getPext(), mType, mPayway, mMoney);
                        addView(mRechargeAlipayQuotaPage, RechargeAlipayQuotaPage.class.getName());
                        mRechargeAlipayQuotaPage.setOnAlipayQuotaHtmlClick(mAlipayQuotaHtmlClick);
                    }
                    break;
                case Constant.ALIPAY_WAP:
                    recharge_titlr_middle_text.setText("支付宝网页");
                    // 进入支付宝网页页面
                    if (mType == Constant.RECHARGE_BY_NO_QUATO) {
                        mRechargeAlipayPage = new RechargeAlipayPage(getUserData(), mActivity,
                                getDeviceno(), mSplusPayManager.getAppkey(),
                                mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                                mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                                mSplusPayManager.getPext(), mType, mPayway,mOrientation);
                        addView(mRechargeAlipayPage, RechargeAlipayPage.class.getName());
                        mRechargeAlipayPage.setOnAlipayHtmlClick(mAlipayHtmlClick);
                    } else if (mType == Constant.RECHARGE_BY_QUATO) {
                        mRechargeAlipayQuotaPage = new RechargeAlipayQuotaPage(getUserData(),
                                mActivity, getDeviceno(), mSplusPayManager.getAppkey(),
                                mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                                mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                                mSplusPayManager.getPext(), mType, mPayway, mMoney);
                        addView(mRechargeAlipayQuotaPage, RechargeAlipayQuotaPage.class.getName());
                        mRechargeAlipayQuotaPage.setOnAlipayQuotaHtmlClick(mAlipayQuotaHtmlClick);
                    }
                    break;
                case Constant.ALIPAY_DEPOSIT:
                    recharge_titlr_middle_text.setText("信用卡");
                    // 进入支付宝支付信用卡
                    if (mType == Constant.RECHARGE_BY_NO_QUATO) {
                        mRechargeAlipayPage = new RechargeAlipayPage(getUserData(), mActivity,
                                getDeviceno(), mSplusPayManager.getAppkey(),
                                mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                                mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                                mSplusPayManager.getPext(), mType, mPayway,mOrientation);
                        addView(mRechargeAlipayPage, RechargeAlipayPage.class.getName());
                        mRechargeAlipayPage.setOnAlipayHtmlClick(mAlipayHtmlClick);
                    } else if (mType == Constant.RECHARGE_BY_QUATO) {
                        mRechargeAlipayQuotaPage = new RechargeAlipayQuotaPage(getUserData(),
                                mActivity, getDeviceno(), mSplusPayManager.getAppkey(),
                                mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                                mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                                mSplusPayManager.getPext(), mType, mPayway, mMoney);
                        addView(mRechargeAlipayQuotaPage, RechargeAlipayQuotaPage.class.getName());
                        mRechargeAlipayQuotaPage.setOnAlipayQuotaHtmlClick(mAlipayQuotaHtmlClick);
                    }
                    break;
                case Constant.ALIPAY_CREDIT:
                    recharge_titlr_middle_text.setText("储蓄卡");
                    // 进入支付宝储蓄卡页面
                    if (mType == Constant.RECHARGE_BY_NO_QUATO) {
                        mRechargeAlipayPage = new RechargeAlipayPage(getUserData(), mActivity,
                                getDeviceno(), mSplusPayManager.getAppkey(),
                                mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                                mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                                mSplusPayManager.getPext(), mType, mPayway,mOrientation);
                        addView(mRechargeAlipayPage, RechargeAlipayPage.class.getName());
                        mRechargeAlipayPage.setOnAlipayHtmlClick(mAlipayHtmlClick);
                    } else if (mType == Constant.RECHARGE_BY_QUATO) {
                        mRechargeAlipayQuotaPage = new RechargeAlipayQuotaPage(getUserData(),
                                mActivity, getDeviceno(), mSplusPayManager.getAppkey(),
                                mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                                mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                                mSplusPayManager.getPext(), mType, mPayway, mMoney);
                        addView(mRechargeAlipayQuotaPage, RechargeAlipayQuotaPage.class.getName());
                        mRechargeAlipayQuotaPage.setOnAlipayQuotaHtmlClick(mAlipayQuotaHtmlClick);
                    }
                    break;
                case Constant.UNION_PAY:
                    recharge_titlr_middle_text.setText("银联");
                    // 进入银联页面
                    if (mType == Constant.RECHARGE_BY_NO_QUATO) {
                        mRechargeUnionPayPage = new RechargeUnionPayPage(getUserData(), mActivity,
                                getDeviceno(), mSplusPayManager.getAppkey(),
                                mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                                mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                                mSplusPayManager.getPext(), mType, mPayway,mOrientation);
                        addView(mRechargeUnionPayPage, RechargeUnionPayPage.class.getName());
                    } else if (mType == Constant.RECHARGE_BY_QUATO) {
                        mRechargeUnionQuotaPayPage = new RechargeUnionQuotaPayPage(getUserData(),
                                mActivity, getDeviceno(), mSplusPayManager.getAppkey(),
                                mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                                mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                                mSplusPayManager.getPext(), mType, mPayway, mMoney);
                        addView(mRechargeUnionQuotaPayPage,
                                RechargeUnionQuotaPayPage.class.getName());
                    }
                    break;
                case Constant.CHAIN_CMM:
                    recharge_titlr_middle_text.setText("移动卡");
                    // 进入移动卡页面
                    if (mType == Constant.RECHARGE_BY_NO_QUATO) {
                        mRechargeCardPage = new RechargeCardPage(getUserData(), mActivity,
                                getDeviceno(), mSplusPayManager.getAppkey(),
                                mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                                mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                                mSplusPayManager.getPext(), mType, mPayway,mOrientation);
                        addView(mRechargeCardPage, RechargeCardPage.class.getName());
                    } else if (mType == Constant.RECHARGE_BY_QUATO) {
                        mRechargeCardQuotaPage = new RechargeCardQuotaPage(getUserData(),
                                mActivity, getDeviceno(), mSplusPayManager.getAppkey(),
                                mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                                mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                                mSplusPayManager.getPext(), mType, mPayway, mMoney);
                        addView(mRechargeCardQuotaPage, RechargeCardQuotaPage.class.getName());
                    }
                    break;
                case Constant.CHAIN_UNC:
                    recharge_titlr_middle_text.setText("联通卡");
                    // 进入联通卡页面
                    if (mType == Constant.RECHARGE_BY_NO_QUATO) {
                        mRechargeCardPage = new RechargeCardPage(getUserData(), mActivity,
                                getDeviceno(), mSplusPayManager.getAppkey(),
                                mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                                mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                                mSplusPayManager.getPext(), mType, mPayway,mOrientation);
                        addView(mRechargeCardPage, RechargeCardPage.class.getName());
                    } else if (mType == Constant.RECHARGE_BY_QUATO) {
                        mRechargeCardQuotaPage = new RechargeCardQuotaPage(getUserData(),
                                mActivity, getDeviceno(), mSplusPayManager.getAppkey(),
                                mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                                mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                                mSplusPayManager.getPext(), mType, mPayway, mMoney);
                        addView(mRechargeCardQuotaPage, RechargeCardQuotaPage.class.getName());
                    }
                    break;
                case Constant.CHAIN_SD:
                    recharge_titlr_middle_text.setText("盛大卡");
                    // 进入支盛大卡页面
                    if (mType == Constant.RECHARGE_BY_NO_QUATO) {
                        mRechargeCardPage = new RechargeCardPage(getUserData(), mActivity,
                                getDeviceno(), mSplusPayManager.getAppkey(),
                                mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                                mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                                mSplusPayManager.getPext(), mType, mPayway,mOrientation);
                        addView(mRechargeCardPage, RechargeCardPage.class.getName());
                    } else if (mType == Constant.RECHARGE_BY_QUATO) {
                        mRechargeCardQuotaPage = new RechargeCardQuotaPage(getUserData(),
                                mActivity, getDeviceno(), mSplusPayManager.getAppkey(),
                                mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                                mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                                mSplusPayManager.getPext(), mType, mPayway, mMoney);
                        addView(mRechargeCardQuotaPage, RechargeCardQuotaPage.class.getName());
                    }
                    break;

                case Constant.PERSON:
                    recharge_titlr_middle_text.setText("人工充值");
                    mRechargePersonPage = new RechargePersonPage(getUserData(), mActivity,
                            getDeviceno(), mSplusPayManager.getAppkey(),
                            mSplusPayManager.getGameid(), mSplusPayManager.getPartner(),
                            mSplusPayManager.getReferer(), mSplusPayManager.getRoleName(),
                            mSplusPayManager.getServerName(), mSplusPayManager.getOutorderid(),
                            mSplusPayManager.getPext(), mType, mPayway,mOrientation);
                    addView(mRechargePersonPage, RechargePersonPage.class.getName());
                    break;
            }

        }

    };

    /**
     * 支付宝HTML页面支付
     */
    private AlipayHtmlClick mAlipayHtmlClick = new AlipayHtmlClick() {

        @Override
        public void onAlipayHtmlClick(UserModel userModel, Activity activity, String deviceno,
                String appKey, Integer gamid, String partner, String referer, String roleName,
                String serverName, String outOrderid, String pext, Integer type, String payway,
                float renminbi) {

            mRechargeAlipayHtmlPage = new RechargeAlipayHtmlPage(userModel, activity, deviceno,
                    appKey, gamid, partner, referer, roleName, serverName, outOrderid, pext, type,
                    payway, renminbi);
            addView(mRechargeAlipayHtmlPage, RechargeAlipayHtmlPage.class.getName());
        }

    };

    /**
     * 支付宝HTML页面支付
     */
    private AlipayQuotaHtmlClick mAlipayQuotaHtmlClick = new AlipayQuotaHtmlClick() {

        @Override
        public void onAlipayQuotaHtmlClick(UserModel userModel, Activity activity, String deviceno,
                String appKey, Integer gamid, String partner, String referer, String roleName,
                String serverName, String outOrderid, String pext, Integer type, String payway,
                float renminbi) {
            mRechargeAlipayHtmlPage = new RechargeAlipayHtmlPage(userModel, activity, deviceno,
                    appKey, gamid, partner, referer, roleName, serverName, outOrderid, pext, type,
                    payway, renminbi);
            addView(mRechargeAlipayHtmlPage, RechargeAlipayHtmlPage.class.getName());
        }

    };

    protected void onDestroy() {
        super.onDestroy();
        mRechargeSelectPage = null;

        mRechargeSelectPage = null;

        mRechargeAlipayPage = null;

        mRechargeAlipayHtmlPage = null;

        mRechargeCardPage = null;

        mRechargePersonPage = null;

        mRechargeUnionPayPage = null;

        mRechargeAlipayQuotaPage = null;

        mRechargeCardQuotaPage = null;

        mRechargeUnionQuotaPayPage = null;

    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mRechargeCallBack != null) {
            mRechargeCallBack.backKey("取消充值");
        }
        ExitAppUtils.getInstance().exit();
    }

    /**
     * Title: onActivityResult Description:
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @see android.app.Activity#onActivityResult(int, int,
     *      android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            /*************************************************
             * 处理银联手机支付控件返回的支付结果 支付控件返回字符串:success、fail、cancel
             * 分别代表支付成功，支付失败，支付取消
             ************************************************/
            if (mRechargeUnionPayPage == null && mRechargeUnionQuotaPayPage == null) {
                return;
            }
            String str = data.getExtras().getString("pay_result");
            if (str.equalsIgnoreCase("success")) {
                if (mType == Constant.RECHARGE_BY_NO_QUATO) {
                    if (mRechargeUnionPayPage != null) {
                        mRechargeUnionPayPage.result_intent(Constant.RECHARGE_RESULT_SUCCESS_TIPS);
                    }
                } else if (mType == Constant.RECHARGE_BY_QUATO) {
                    if (mRechargeUnionQuotaPayPage != null) {
                        mRechargeUnionQuotaPayPage.result_intent(Constant.RECHARGE_RESULT_SUCCESS_TIPS);
                    }
                }

            } else if (str.equalsIgnoreCase("fail")) {
                if (mType == Constant.RECHARGE_BY_NO_QUATO) {
                    if (mRechargeUnionPayPage != null) {
                        mRechargeUnionPayPage.result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
                    }
                } else if (mType == Constant.RECHARGE_BY_QUATO) {
                    if (mRechargeUnionQuotaPayPage != null) {
                        mRechargeUnionQuotaPayPage.result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
                    }
                }

            } else if (str.equalsIgnoreCase("cancel")) {
                if (mType == Constant.RECHARGE_BY_NO_QUATO) {
                    if (mRechargeUnionPayPage != null) {
                        mRechargeUnionPayPage.result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
                    }
                } else if (mType == Constant.RECHARGE_BY_QUATO) {
                    if (mRechargeUnionQuotaPayPage != null) {
                        mRechargeUnionQuotaPayPage.result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
                    }
                }
            }
        }

    }

}
