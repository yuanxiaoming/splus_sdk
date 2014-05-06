/**
 * @Title: RechargeResultActivity.java
 * @Package com.sanqi.android.sdk.ui
 * Copyright: Copyright (c) 2013
 * Company:上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013-11-18 下午3:41:28
 * @version V1.0
 */

package com.android.splus.sdk.ui;

import com.android.splus.sdk.apiinterface.RechargeCallBack;
import com.android.splus.sdk.manager.ExitAppUtils;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.file.AppUtil;
import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @ClassName: RechargeResultActivity
 * @author xiaoming.yuan
 * @date 2013-11-18 下午3:41:28
 */

public class RechargeResultActivity extends BaseActivity {
    private static final String TAG = "RechargeResultActivity";

    private ImageButton recharge_title_left_backbtn;

    private TextView recharge_titlr_middle_text;

    private ImageButton recharge_title_right_btn;

    private Button rechage_result_comfirm_btn;

    private TextView tv_rechage_result_tips;

    private TextView tv_rechage_result_bottom_tips;

    private RechargeCallBack mRechargeCallBack;

    private String mRecharge_Type;

    private String mMoney;

    private Activity mActivity;

    /**
     * Title: findViewById Description:
     * 
     * @see com.canhe.android.sdk.ui.BaseActivity#findViewById()
     */
    @Override
    protected void findViewById() {

        tv_rechage_result_tips = (TextView) findViewById(KR.id.splus_rechage_result_tips);
        tv_rechage_result_tips.setTextColor(0xff747474);
        rechage_result_comfirm_btn = (Button) findViewById(KR.id.splus_rechage_result_btn_comfirm);
        rechage_result_comfirm_btn.setTextColor(Color.WHITE);
        rechage_result_comfirm_btn.setTextSize(CommonUtil.dip2px(this, 9));
        rechage_result_comfirm_btn.setText(KR.string.splus_recharge_result_comfirm_tips);
        tv_rechage_result_bottom_tips = (TextView) findViewById(KR.id.splus_rechage_result_bottom_tips);
        tv_rechage_result_bottom_tips.setText(KR.string.splus_recharge_select_tips);
        tv_rechage_result_bottom_tips.setTextColor(0xff747474);
        recharge_title_left_backbtn = (ImageButton) findViewById(KR.id.splus_recharge_title_bar_left_button);
        recharge_title_left_backbtn.setVisibility(View.INVISIBLE);
        recharge_title_right_btn = (ImageButton) findViewById(KR.id.splus_recharge_title_bar_right_button);
        recharge_title_right_btn.setVisibility(View.INVISIBLE);
        recharge_titlr_middle_text = (TextView) findViewById(KR.id.splus_recharge_title_bar_middle_title);
        recharge_titlr_middle_text.setTextColor(Color.WHITE);
        recharge_titlr_middle_text.setTextSize(CommonUtil.dip2px(this, 10));

    }

    /**
     * Title: loadViewLayout Description:
     * 
     * @see com.canhe.android.sdk.ui.BaseActivity#loadViewLayout()
     */
    @Override
    protected void loadViewLayout() {

        setContentView(KR.layout.splus_recharge_result_activity);
        mActivity = this;
    }

    /**
     * Title: processLogic Description:
     * 
     * @see com.canhe.android.sdk.ui.BaseActivity#processLogic()
     */
    @Override
    protected void processLogic() {

        Intent intent = getIntent();
        mRecharge_Type = intent.getStringExtra(Constant.RECHARGE_RESULT_TIPS);
        mMoney = intent.getStringExtra(Constant.MONEY);
        LogHelper.i(TAG, mMoney);
        if (mRecharge_Type.equals(Constant.RECHARGE_RESULT_SUCCESS_TIPS)) {
            // tv_rechage_result_tips.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(
            // ResourceUtil.getDrawableId(this,KR.drawable.splus_recharge_result_success_icon)),
            // null, null,null);
            tv_rechage_result_tips.setText(KR.string.splus_recharge_success_result_tips.replace("%s", mMoney));
            recharge_titlr_middle_text.setText(KR.string.splus_recharge_success_result_head_tips);
        } else if (mRecharge_Type.equals(Constant.RECHARGE_RESULT_FAIL_TIPS)) {
            tv_rechage_result_tips.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(ResourceUtil.getDrawableId(this, KR.drawable.splus_recharge_result__fail_icon)), null, null, null);
            tv_rechage_result_tips.setText(KR.string.splus_recharge_fail_result_tips);
            recharge_titlr_middle_text.setText(KR.string.splus_recharge_fail_result_head_tips);
        }
        mRechargeCallBack = SplusPayManager.getInstance().getRechargeCallBack();
    }

    /**
     * Title: setListener Description:
     * 
     * @see com.canhe.android.sdk.ui.BaseActivity#setListener()
     */
    @Override
    protected void setListener() {

        rechage_result_comfirm_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                rechageCallBack();
            }
        });

        recharge_title_left_backbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                rechageCallBack();
            }
        });
        recharge_title_right_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 密码找回界面
                if (mRecharge_Type.equals(Constant.RECHARGE_RESULT_SUCCESS_TIPS)) {
                    if (mRechargeCallBack != null) {
                        UserModel userModel = SplusPayManager.getInstance().getUserModel();
                        if (userModel == null) {
                            userModel = AppUtil.getUserData();
                        }
                        mRechargeCallBack.rechargeSuccess(userModel);
                        LogHelper.i(TAG, "充值成功");
                    }

                } else if (mRecharge_Type.equals(Constant.RECHARGE_RESULT_FAIL_TIPS)) {
                    if (mRechargeCallBack != null) {
                        mRechargeCallBack.rechargeFaile("充值失败");
                        LogHelper.i(TAG, "充值失败");
                    }

                }
                Intent intent = new Intent(mActivity, PersonActivity.class);
                intent.putExtra(PersonActivity.INTENT_TYPE, PersonActivity.INTENT_SQ);
                mActivity.startActivity(intent);
                finish();
            }
        });

    }

    /**
     * Title: onBackPressed Description:
     * 
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        rechageCallBack();

    }

    // 充值结果回调
    private void rechageCallBack() {

        if (mRecharge_Type.equals(Constant.RECHARGE_RESULT_SUCCESS_TIPS)) {
            if (mRechargeCallBack != null) {
                UserModel userModel = SplusPayManager.getInstance().getUserModel();
                if (userModel == null) {
                    userModel = AppUtil.getUserData();
                }
                mRechargeCallBack.rechargeSuccess(userModel);
                LogHelper.i(TAG, "充值成功");
            }

        } else if (mRecharge_Type.equals(Constant.RECHARGE_RESULT_FAIL_TIPS)) {
            if (mRechargeCallBack != null) {
                mRechargeCallBack.rechargeFaile("充值失败");
                LogHelper.i(TAG, "充值失败");
            }

        }
        this.finish();
        ExitAppUtils.getInstance().exit();

    }

    /**
     * Title: onDestroy Description:
     * 
     * @see com.canhe.android.sdk.ui.BaseActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        tv_rechage_result_tips = null;
        rechage_result_comfirm_btn = null;
        tv_rechage_result_bottom_tips = null;
        mRechargeCallBack = null;
        mRecharge_Type = null;
        recharge_title_left_backbtn = null;
        recharge_title_right_btn = null;

    }
}
