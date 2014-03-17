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

import com.android.splus.sdk.model.RatioModel;
import com.android.splus.sdk.model.RechargeModel;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.parse.LoginParser;
import com.android.splus.sdk.ui.RechargeResultActivity;
import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.date.DateUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil.DataCallback;
import com.android.splus.sdk.utils.http.RequestModel;
import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.md5.MD5Util;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.utils.toast.ToastUtil;
import com.android.splus.sdk.widget.CustomProgressDialog;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: RechargeAlipayPage
 * @author xiaoming.yuan
 * @date 2014-3-11 下午1:37:06
 */

public class RechargeCardQuotaPage extends LinearLayout {
    private static final String TAG = "RechargeAlipayPage";

    private Activity mActivity;

    private TextView recharge_money_tips;

    private TextView recharge_money_ratio_tv;

    private Button recharge_comfirm_btn;

    private ImageButton recharge_card_explian_ibtn;

    private EditText recharge_money_cardpassport_edit;

    private EditText recharge_money_cardpassword_edit;

    private float mRenminbi = 0; // 人民币

    private String mCoin_name = "金币";

    private int mRatio = 9;

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


    private View mDialogClauseView;// 条款视图

    private Dialog mDialogClause;// 条款对话框

    private FrameLayout.LayoutParams mDialogClauseparams;// 条款对话框参数


    public RechargeCardQuotaPage(UserModel userModel, Activity activity, String deviceno, String appKey,
            Integer gamid, String partner, String referer, String roleName, String serverName,
            String outOrderid, String pext, Integer type, String payway,Float money) {
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
        this.mRenminbi=money;
        inflate(activity,
                ResourceUtil.getLayoutId(activity, KR.layout.splus_recharge_card_quota_layout), this);
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
        recharge_money_cardpassport_edit = (EditText) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_cardpassport_edit));
        recharge_money_cardpassword_edit = (EditText) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_money_cardpassword_edit));
        recharge_card_explian_ibtn=(ImageButton) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_recharge_card_explian_ibtn));

    }

    /**
     * @Title: initViews(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午1:42:09 void 返回类型
     */

    private void initViews() {

        recharge_money_tips.setText(Html.fromHtml("<font color=#FE8E35>充值金额: "+ mRenminbi+"元</font>"));
        recharge_money_ratio_tv.setHint(KR.string.splus_recharge_ratio_tv_tips);
        recharge_money_cardpassport_edit.setHint("请输入卡号");
        recharge_money_cardpassword_edit.setHint("请输入密码");

    }

    /**
     * @Title: setlistener(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-11 下午1:42:12 void 返回类型
     */

    private void setlistener() {



        recharge_comfirm_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                payQuest();
            }
        });
        recharge_card_explian_ibtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPayway.equals(Constant.CHAIN_CMM_PAYWAY)) {
                    show_card_explian("移动卡充值说明", KR.string.splus_recharge_long_cmm_tips, "确定");
                } else if (mPayway.equals(Constant.CHAIN_UNC_PAYWAY)) {
                    show_card_explian("联通卡充值说明", KR.string.splus_recharge_long_unc_tips, "确定");
                } else if (mPayway.equals(Constant.CHAIN_SD_PAYWAY)) {
                    show_card_explian("盛大卡充值说明", KR.string.splus_recharge_long_sd_tips, "确定");
                }
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



    private void show_card_explian(String title,String content,String okBtnStr) {
        mDialogClauseparams = getMParames();
        if (mDialogClause == null) {
            mDialogClause = new Dialog(mActivity, android.R.style.Theme_Panel);
            mDialogClause.addContentView(getmDialogView(title,content,okBtnStr), mDialogClauseparams);
        }
        if (!mDialogClause.isShowing()) {
            mDialogClause.show();
        }

    }

    /**
     *
     * @return View
     * @exception
     * @since 1.0.0 xiaoming.yuan
     */
    private View getmDialogView(String title,String content,String okBtnStr) {

        mDialogClauseView = CommonUtil.createCustomView(mActivity,
                KR.layout.splus_recharge_card_explian_dialog);
        TextView mContentTextView = (TextView) mDialogClauseView.findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_register_clause_dialog_tv_content));
        mContentTextView.setText(Html.fromHtml(content));
        TextView mTitleTextView = (TextView) mDialogClauseView.findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_register_clause_dialog_iv_title));
        mTitleTextView.setText(title);
        Button button = (Button) mDialogClauseView.findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_register_clause_dialog_btn_agree));
        button.setText(okBtnStr);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDialogClause != null && mDialogClause.isShowing()) {
                    mDialogClause.dismiss();
                }
            }
        });
        ImageView splus_login_clause_dialog_iv_close = (ImageView) mDialogClauseView
                .findViewById(ResourceUtil.getId(mActivity, KR.id.splus_login_clause_iv_close));
        splus_login_clause_dialog_iv_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDialogClause != null && mDialogClause.isShowing()) {
                    mDialogClause.dismiss();
                }

            }
        });

        return mDialogClauseView;
    }
    /**
     * getMParames(生成窗口参数)
     *
     * @return LayoutParams
     * @exception
     * @since 1.0.0 xiaoming.yuan
     */
    private FrameLayout.LayoutParams getMParames() {
        if (mDialogClauseparams == null) {
            return CommonUtil.getFrameLayoutParams(mActivity, 140, 140, 65, 65, Gravity.CENTER);
        }
        return mDialogClauseparams;
    }

}
