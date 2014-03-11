
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

import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.widget.CustomGridView;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName: RechargeAlipayPage
 * @author xiaoming.yuan
 * @date 2014-3-11 下午1:37:06
 */

public class RechargeAlipayPage extends LinearLayout{
    private static final String TAG = "RechargeAlipayPage";

    private  UserModel mUserModel;

    private Activity mActivity;

    private TextView recharge_money_tips;

    private TextView recharge_money_ratio_tv;

    private Button recharge_comfirm_btn;

    private EditText  recharge_money_custom_et;

    private CustomGridView  recharge_money_gridview_select;


    public RechargeAlipayPage(UserModel userModel, Activity activity) {
        super(activity);
        this.mUserModel = userModel;
        this.mActivity = activity;
        inflate(activity,ResourceUtil.getLayoutId(activity, KR.layout.splus_recharge_alipay_layout), this);
        findViews();
        initViews();
        setlistener();

    }




    /**
     * @Title: findViews(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午1:42:16
     * void 返回类型
     */

    private void findViews() {
        recharge_money_tips=(TextView) findViewById(ResourceUtil.getId(mActivity,KR.id.splus_recharge_money_tips));
        recharge_money_custom_et=(EditText) findViewById(ResourceUtil.getId(mActivity,KR.id.splus_recharge_money_custom_et));
        recharge_comfirm_btn=(Button) findViewById(ResourceUtil.getId(mActivity,KR.id.splus_recharge_money_comfirm_btn));
        recharge_money_ratio_tv=(TextView) findViewById(ResourceUtil.getId(mActivity,KR.id.splus_recharge_money_ratio_tv));
        recharge_money_gridview_select=(CustomGridView) findViewById(ResourceUtil.getId(mActivity,KR.id.splus_recharge_money_gridview_select));


    }


    /**
     * @Title: initViews(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午1:42:09
     * void 返回类型
     */

    private void initViews() {
    }

    /**
     * @Title: setlistener(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午1:42:12
     * void 返回类型
     */

    private void setlistener() {
    }


}

