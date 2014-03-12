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

import com.android.splus.sdk.adapter.MoneyGridViewAdapter;
import com.android.splus.sdk.model.RatioModel;
import com.android.splus.sdk.model.RechargeModel;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.parse.LoginParser;
import com.android.splus.sdk.ui.RechargeResultActivity;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.date.DateUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil.DataCallback;
import com.android.splus.sdk.utils.http.RequestModel;
import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.md5.MD5Util;
import com.android.splus.sdk.utils.phone.Phoneuitl;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.utils.toast.ToastUtil;
import com.android.splus.sdk.widget.CustomGridView;
import com.android.splus.sdk.widget.CustomProgressDialog;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: RechargeAlipayPage
 * @author xiaoming.yuan
 * @date 2014-3-11 下午1:37:06
 */

public class RechargeCardPage extends LinearLayout {
    private static final String TAG = "RechargeAlipayPage";

    private Activity mActivity;

    private TextView recharge_money_tips;

    private TextView recharge_money_ratio_tv;

    private Button recharge_comfirm_btn;

    private EditText recharge_money_cardpassport_edit;

    private EditText recharge_money_cardpassword_edit;

    private CustomGridView recharge_money_gridview_select;

    private MoneyGridViewAdapter mMoneyGridViewAdapter;

    private final int[] CHINA_MOBILE_MONEY = {
            10, 20, 30, 50, 100, 300, 500
    };

    private static final int[] CHINA_UNICOM_MONEY = {
            20, 30, 50, 100, 300, 500
    };

    private static final int[] CHINA_SDCOMM_ONEY = {
            10, 30, 35, 45, 100, 350, 1000
    };

    private float mRenminbi = 0; // 人民币

    private String mCoin_name = "金币";

    private int mRatio = 10;

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

    private String mCardNumber;

    private String mCardPassword;

    protected CustomProgressDialog mProgressDialog;

    public RechargeCardPage(UserModel userModel, Activity activity, String deviceno, String appKey,
            Integer gamid, String partner, String referer, String roleName, String serverName,
            String outOrderid, String pext, Integer type, String payway) {
        super(activity);
        this.mUserModel = userModel;
        this.mActivity = activity;
        this.mDeviceno = deviceno;
        this.mAppKey = appKey;
        this.mGameid = gamid;
        this.mPartner = partner;
        this.mReferer = referer;
        this.mRoleName = roleName;
        this.mServerName = serverName;
        this.mOutOrderid = outOrderid;
        this.mPext = pext;
        this.mType = type;
        this.mPayway = payway;
        inflate(activity,
                ResourceUtil.getLayoutId(activity, KR.layout.splus_recharge_alipay_layout), this);
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
        recharge_money_tips = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_tips));
        recharge_comfirm_btn = (Button) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_comfirm_btn));
        recharge_comfirm_btn.setText(KR.string.splus_recharge_comfirm_tips);
        recharge_money_ratio_tv = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_ratio_tv));
        recharge_money_gridview_select = (CustomGridView) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_recharge_money_gridview_select));
        recharge_money_tips.setText(KR.string.splus_recharge_select_head_tips);
        recharge_money_ratio_tv.setHint(KR.string.splus_recharge_ratio_tv_tips);

        recharge_money_cardpassport_edit = (EditText) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_cardpassport_edit));

        recharge_money_cardpassword_edit = (EditText) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_cardpassword_edit));

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
            // recharge_money_gridview_select.setNumColumns(3);
            // recharge_money_gridview_select.setLayoutParams(new
            // LinearLayout.LayoutParams(
            // FrameLayout.LayoutParams.MATCH_PARENT,
            // FrameLayout.LayoutParams.MATCH_PARENT,
            // Gravity.CENTER));
            // recharge_money_gridview_select.setPadding(5, 50, 5, 50);
            // recharge_money_gridview_select.setVerticalSpacing(40);
            // recharge_money_gridview_select.setHorizontalSpacing(20);

        }
        recharge_money_gridview_select.setGravity(Gravity.CENTER_HORIZONTAL);
        recharge_money_gridview_select.setSelector(android.R.color.transparent);
        if (mPayway.equals(Constant.CHAIN_CMM_PAYWAY)) {
            mMoneyGridViewAdapter = new MoneyGridViewAdapter(CHINA_MOBILE_MONEY, mActivity);
        } else if (mPayway.equals(Constant.CHAIN_UNC_PAYWAY)) {
            mMoneyGridViewAdapter = new MoneyGridViewAdapter(CHINA_UNICOM_MONEY, mActivity);
        } else if (mPayway.equals(Constant.CHAIN_SD_PAYWAY)) {
            mMoneyGridViewAdapter = new MoneyGridViewAdapter(CHINA_SDCOMM_ONEY, mActivity);
        }
        recharge_money_gridview_select.setAdapter(mMoneyGridViewAdapter);
        mRenminbi = mMoneyGridViewAdapter.getMoneyArray()[0];

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
                mMoneyIndex = position;
                mRenminbi = mMoneyGridViewAdapter.getMoneyArray()[mMoneyIndex];
                setGetMoneyTextPure(mRenminbi);
                mMoneyGridViewAdapter.setMoneyIndex(mMoneyIndex);

            }
        });

        recharge_comfirm_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                payQuest();
            }
        });
    }

    /**
     * @Title: processLogic(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午9:10:35 void 返回类型
     */

    private void processLogic() {
        // getRatio();
    }

    /**
     * @Title: getRatio(请求兑换比率和显示名称)
     * @author xiaoming.yuan
     * @data 2013-10-2 下午11:22:24 void 返回类型
     */
    private void getRatio() {
        long time = DateUtil.getUnixTime();
        String keyString = mGameid + mPayway + time + mAppKey;
        RatioModel mRatioModel = new RatioModel(mGameid, mPayway, time,
                MD5Util.getMd5toLowerCase(keyString));
        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.RATIO_URL,
                mActivity, mRatioModel, new LoginParser()), onRatioebyCardCallBack);
    }

    private DataCallback<JSONObject> onRatioebyCardCallBack = new DataCallback<JSONObject>() {

        @Override
        public void callbackSuccess(JSONObject paramObject) {
            if (paramObject != null && paramObject.optInt("code") == 1) {
                mCoin_name = paramObject.optJSONObject("data").optString("coin_name");
                mRatio = paramObject.optJSONObject("data").optInt("ratio");
                setGetMoneyTextPure(mRenminbi);
            } else {
                String msg = paramObject.optJSONObject("data").optString("msg");
                LogHelper.d(TAG, msg);
            }
        }

        @Override
        public void callbackError(String error) {
            LogHelper.d(TAG, error);
        }

    };

    /**
     * 根据元宝数，显示相应提示（纯色文字）
     *
     * @param money
     */
    private void setGetMoneyTextPure(Float money) {

        if (money == 0) {
            recharge_money_ratio_tv.setText("");
        } else {
            recharge_money_ratio_tv.setText(mRenminbi + "元可兑换" + mRenminbi * mRatio + mCoin_name);
        }

    }

    private void payQuest() {

        mCardNumber = recharge_money_cardpassport_edit.getText().toString().trim();
        mCardPassword = recharge_money_cardpassword_edit.getText().toString().trim();
        if (null == mCardNumber || "".equals(mCardNumber) || !isCardNumberCorrect(mCardNumber)) {
            ToastUtil.showToast(mActivity, "卡号不能为空或者格式不对");
            return;
        }
        if (null == mCardPassword || "".equals(mCardPassword) || !isCardPasswordCorrect(mCardPassword)) {
            ToastUtil.showToast(mActivity, "密码不能为空或者格式不对");
            return;
        }
        long time = DateUtil.getUnixTime();
        String keyString = mGameid + mServerName + mDeviceno + mReferer + mPartner+ mUserModel.getUid() + mRenminbi + mPayway + time + mAppKey;
        RechargeModel rechargeModel = new RechargeModel(mGameid, mServerName, mDeviceno,
                mPartner, mReferer, mUserModel.getUid(), mRenminbi, mType, mPayway, mRoleName,
                time, mUserModel.getPassport(),mCardNumber,mCardPassword, String.valueOf(mRenminbi),mOutOrderid, mPext,
                MD5Util.getMd5toLowerCase(keyString));


        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            showProgressDialog();
        }
        NetHttpUtil.getDataFromServerPOST(mActivity,new RequestModel(Constant.HTMLWAPPAY_URL, mActivity,
                rechargeModel, new LoginParser()), onRechargebyCardCallBack);

    }



    private DataCallback<JSONObject> onRechargebyCardCallBack = new DataCallback<JSONObject>() {
        @Override
        public void callbackSuccess(JSONObject paramObject) {
                closeProgressDialog();
                if (paramObject != null && (paramObject.optInt("code") == 24 || paramObject.optInt("code") == 1)) {
                    String orderid = paramObject.optJSONObject("data").optString("orderid");
                    final int time = paramObject.optJSONObject("data").optInt("time");
                    String sign = paramObject.optJSONObject("data").optString("sign");
                    if (sign.equals(MD5Util.getMd5toLowerCase(orderid + time + mAppKey))) {
                        result_intent(Constant.RECHARGE_RESULT_SUCCESS_TIPS);
                    } else {
                        String msg = paramObject.optString("msg");
                        LogHelper.d(TAG, msg);
                        ToastUtil.showToast(mActivity, msg);
                        result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
                    }
                } else if (paramObject != null && (paramObject.optInt("code") == 19 || paramObject.optInt("code") == 20 || paramObject.optInt("code") == 26)) {
                    ToastUtil.showToast(mActivity, "系统故障或繁忙，请稍后再试");
                    String msg = paramObject.optString("msg");
                    LogHelper.d(TAG, msg);
                    result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
                } else {
                    String msg = paramObject.optString("msg");
                    ToastUtil.showToast(mActivity, "充值失败，请稍后再试");
                    LogHelper.d(TAG, msg);
                    result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
                }
        }

        @Override
        public void callbackError(String error) {
            closeProgressDialog();
            LogHelper.d(TAG, error);
            ToastUtil.showToast(mActivity, error);
            result_intent(Constant.RECHARGE_RESULT_FAIL_TIPS);
        }

    };


    private void result_intent(String rechage_type) {
        Intent intent = new Intent();
        intent.setClass(mActivity, RechargeResultActivity.class);
        intent.putExtra(Constant.RECHARGE_RESULT_TIPS, rechage_type);
        intent.putExtra(Constant.MONEY, String.valueOf(mRenminbi));
        mActivity.startActivity(intent);

    }

    /**
     * 检测充值卡密码格式是否正确
     *
     * @param cardNumberpwd
     * @return
     */
    private boolean isCardPasswordCorrect(String cardNumberpwd) {

        if (!TextUtils.isEmpty(cardNumberpwd)) {
            Pattern pattern = null;

            if (mPayway.equals(Constant.CHAIN_CMM_PAYWAY)) {
                pattern = Pattern.compile("^\\d{18}$");
            } else if (mPayway.equals(Constant.CHAIN_UNC_PAYWAY)) {
                pattern = Pattern.compile("^\\d{19}$");
            } else if (mPayway.equals(Constant.CHAIN_SD_PAYWAY)) {
                pattern = Pattern.compile("^\\d{8,9}$");
            }

            if (pattern != null) {
                Matcher matcher = pattern.matcher(cardNumberpwd);
                return matcher.find();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 检测充值卡号格式是否正确
     *
     * @param cardNumber
     * @return
     */
    private boolean isCardNumberCorrect(String cardNumber) {
        if (!TextUtils.isEmpty(cardNumber)) {
            Pattern pattern = null;
            if (mPayway.equals(Constant.CHAIN_CMM_PAYWAY)) {
                pattern = Pattern.compile("^\\d{17}$");
            } else if (mPayway.equals(Constant.CHAIN_UNC_PAYWAY)) {
                pattern = Pattern.compile("^[0-9a-zA-Z]{15}$");
            } else if (mPayway.equals(Constant.CHAIN_SD_PAYWAY)) {
                pattern = Pattern.compile("^[0-9a-zA-Z]{15}$");
            }

            if (pattern != null) {
                Matcher matcher = pattern.matcher(cardNumber);
                return matcher.find();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected void showProgressDialog() {

        if (this.mProgressDialog == null) {
            this.mProgressDialog = new CustomProgressDialog(mActivity);
        }
        // 设置ProgressDialog 的进度条style
        this.mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // this.mProgressDialog.setTitle("提示");
        this.mProgressDialog.setMessage("加载中...");
        // 设置ProgressDialog 的进度条是否不明确
        this.mProgressDialog.setIndeterminate(false);
        // 设置ProgressDialog 的进度条是否不明确
        this.mProgressDialog.setCancelable(false);
        this.mProgressDialog.show();
    }

    protected void closeProgressDialog() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
    }

}
