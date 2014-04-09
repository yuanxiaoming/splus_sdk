/**
 * @Title: BindPhoneSuccessView.java
 * @Package com.sanqi.android.sdk.ui
 * Copyright: Copyright (c) 2013
 * Company:上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013-12-16 下午8:56:38
 * @version V1.0
 */

package com.android.splus.sdk.ui;

import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName: BindPhoneSuccessView
 * @author xiaoming.yuan
 * @date 2013-12-16 下午8:56:38
 */

public class BindPhoneSuccessView extends LinearLayout {

    private Activity mActivity;

    private String account;

    private String phone;

    private BindPhoneDialog mDialog;

    /**
     * @Title: BindPhoneSuccessView
     * @Description:(这里用一句话描述这个方法的作用)
     * @param mActivity
     * @param account
     * @param phone
     * @throws
     */

    public BindPhoneSuccessView(Activity mActivity, BindPhoneDialog dialog, String account,
            String phone) {
        super(mActivity);
        this.mActivity = mActivity;
        this.account = account;
        this.phone = phone;
        this.mDialog = dialog;
        addView(inflate(mActivity,
                ResourceUtil.getLayoutId(mActivity, KR.layout.splus_login_bindphone_success), null),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        initView();
    }

    private void initView() {
        TextView bind_phone_tv_firstline = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_bindphone_tv_firstline));
        bind_phone_tv_firstline.setTextColor(Color.parseColor("#9a9a9a"));
        bind_phone_tv_firstline.setText("37wan账号:" + account);
        TextView bind_phone_tv_secondline = (TextView) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_login_bindphone_tv_secondline));
        bind_phone_tv_secondline.setTextColor(Color.parseColor("#9a9a9a"));
        bind_phone_tv_secondline.setText("手机号:" + phone);
        ImageView ch_login_iv_close = (ImageView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_iv_close));
        
        ImageView iv_title = (ImageView) findViewById(ResourceUtil.getId(mActivity,KR.id.splus_login_iv_title));
        iv_title.setImageResource(ResourceUtil.getDrawableId(mActivity,KR.drawable.splus_login_bindphone_success));
        
        ch_login_iv_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        Button ch_bindphone_finish = (Button) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_bindphone_later));
        ch_bindphone_finish.setText(KR.string.splus_login_bindphone_success_finished);
        ch_bindphone_finish.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
    }

    /**
     * @Title: dismissDialog(隐藏对话框)
     * @author xilinch
     * @data 2013-12-17 下午7:56:08 void 返回类型
     */
    private void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog.changeCurrentView();
            mDialog.loginSuccess();
        }
    }
}
