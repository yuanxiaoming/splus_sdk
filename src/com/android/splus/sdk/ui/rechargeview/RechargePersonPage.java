/**
 * @Title: RechargeAlipayPage.java
 * @Package com.android.splus.sdk.ui.recharge
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-3-11 下午1:37:06
 * @version V1.0
 */

package com.android.splus.sdk.ui.rechargeview;

import com.android.splus.sdk.adapter.BankGridViewAdapter;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.utils.phone.Phoneuitl;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.widget.CustomGridView;

import android.app.Activity;
import android.content.res.Configuration;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @ClassName: RechargeAlipayPage
 * @author xiaoming.yuan
 * @date 2014-3-11 下午1:37:06
 */

public class RechargePersonPage extends LinearLayout {
    private static final String TAG = "RechargeAlipayPage";

    private String[] BANK_DATA_NAME={"工商银行","招商银行","建设银行","农行银行"};

    private String[] BANK_DATA_NAME_TIPS={
            "开户地:广州 \n开户行:工商银行广州工业园支行 \n帐户:灿和网络科技有限公司 \n卡号:44001 47051 30530 06982",
            "开户地:广州 \n开户行:招商银行广州工业园支行 \n帐户:灿和网络科技有限公司 \n卡号:44001 47051 30530 06982",
            "开户地:广州 \n开户行:建设银行广州工业园支行 \n帐户:灿和网络科技有限公司 \n卡号:44001 47051 30530 06982",
    "开户地:广州 \n开户行:农行银行广州工业园支行 \n帐户:灿和网络科技有限公司 \n卡号:44001 47051 30530 06982"};

    private Activity mActivity;
    private BankGridViewAdapter mBankGridViewAdapter;

    private TextView recharge_person_tips;

    private CustomGridView recharge_person_gridview_select;

    private TextView recharge_person_bank_tips;

    private ImageButton recharge_card_explian_ibtn;

    private TextView recharge_persontel_tips;

    private TextView recharge_personqq_tips;

    private TextView recharge_personweixin_tips;

    private  ScrollView recharge_person_explian_scrollview;

    private TextView recharge_person_explian_tips;

    private LinearLayout recharge_person_linearlayout;




    private int mMoneyIndex = 0;

    private String mPayway;

    private UserModel mUserModel;

    private Integer mGameid;

    private String mPartner;

    private String mReferer;

    private String mRoleName;

    private String mServerName;

    private String mOutOrderid;

    private String mPext;

    private String mAppKey;

    private String mDeviceno;

    private Integer mType;




    public RechargePersonPage(UserModel userModel, Activity activity, String deviceno, String appKey,
            Integer gameid, String partner, String referer, String roleName, String serverName,
            String outOrderid, String pext, Integer type, String payway) {
        super(activity);
        this.mUserModel = userModel;
        this.mActivity = activity;
        this.mDeviceno = deviceno;
        this.mAppKey = appKey;
        this.mGameid = gameid;
        this.mPartner = partner;
        this.mReferer = referer;
        this.mRoleName = roleName;
        this.mServerName = serverName;
        this.mOutOrderid = outOrderid;
        this.mPext = pext;
        this.mType = type;
        this.mPayway = payway;
        inflate(activity,
                ResourceUtil.getLayoutId(activity, KR.layout.splus_recharge_person_layout), this);
        findViews();
        initViews();
        setlistener();
        processLogic();

    }



    /**
     * @Title: findViews(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午1:42:16 void 返回类型
     */

    private void findViews() {
        recharge_person_tips = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_person_tips));
        recharge_person_tips.setText(Html.fromHtml("<font color=#FE8E35>请选择对应的开户行</font>"));
        recharge_person_bank_tips = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_person_bank_tips));
        recharge_person_bank_tips.setPadding(0, 0, 0, 0);
        recharge_person_bank_tips.setText(BANK_DATA_NAME_TIPS[0]);
        recharge_persontel_tips = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_persontel_tips));
        recharge_persontel_tips.setText(KR.string.splus_recharge_persontel_tips);
        recharge_personqq_tips = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_personqq_tips));
        recharge_personqq_tips.setText(KR.string.splus_recharge_personqq_tips);

        recharge_personweixin_tips = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_personweixin_tips));
        recharge_personweixin_tips.setText(KR.string.splus_recharge_personweixin_tips);

        recharge_person_gridview_select = (CustomGridView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_person_gridview_select));

        recharge_card_explian_ibtn = (ImageButton) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_card_explian_ibtn));

        recharge_person_explian_tips = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_person_explian_tips));
        recharge_person_explian_tips.setText(Html.fromHtml(KR.string.splus_recharge_person_explian_tips));

        recharge_person_explian_scrollview = (ScrollView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_person_explian_scrollview));

        recharge_person_explian_scrollview.setVisibility(View.GONE);

        recharge_person_linearlayout = (LinearLayout) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_person_linearlayout));



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
            recharge_person_gridview_select.setNumColumns(4);
            recharge_person_gridview_select.setColumnWidth(40);
            recharge_person_gridview_select.setGravity(Gravity.CENTER);
            recharge_person_gridview_select.setLayoutParams(new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER));
            recharge_person_gridview_select.setVerticalSpacing(10);
            recharge_person_gridview_select.setHorizontalSpacing(2);

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
             recharge_person_gridview_select.setNumColumns(4);
             recharge_person_gridview_select.setLayoutParams(new
             LinearLayout.LayoutParams(
             FrameLayout.LayoutParams.MATCH_PARENT,
             FrameLayout.LayoutParams.MATCH_PARENT,
             Gravity.CENTER));
             recharge_person_gridview_select.setPadding(5, 20, 5, 20);
             recharge_person_gridview_select.setVerticalSpacing(10);
             recharge_person_gridview_select.setHorizontalSpacing(10);
        }
        recharge_person_gridview_select.setGravity(Gravity.CENTER_HORIZONTAL);
        recharge_person_gridview_select.setSelector(android.R.color.transparent);
        mBankGridViewAdapter = new BankGridViewAdapter(BANK_DATA_NAME, mActivity);
        recharge_person_gridview_select.setAdapter(mBankGridViewAdapter);

    }

    /**
     * @Title: setlistener(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午1:42:12 void 返回类型
     */

    private void setlistener() {

        recharge_person_gridview_select.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBankGridViewAdapter.setIndex(position);
                recharge_person_bank_tips.setText(BANK_DATA_NAME_TIPS[position]);
            }
        });
        recharge_card_explian_ibtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                recharge_person_linearlayout.setVisibility(View.GONE);
                recharge_person_explian_scrollview.setVisibility(VISIBLE);

            }
        });

    }


    /**
     * @Title: processLogic(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-13 下午5:36:14
     * void 返回类型
     */

    private void processLogic() {
    }

}
