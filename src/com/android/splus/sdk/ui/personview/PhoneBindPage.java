/**
 * @Title: PhoneBind.java
 * @Package: com.sanqi.android.sdk.person
 * Copyright: Copyright (c) 2013
 * Company: 上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013年9月29日 下午5:23:29
 * @version 1.0
 */

package com.android.splus.sdk.ui.personview;

import com.android.splus.sdk.model.BindPhoneModel;
import com.android.splus.sdk.model.GetCodeModel;
import com.android.splus.sdk.parse.LoginParser;
import com.android.splus.sdk.ui.PersonActivity;
import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.date.DateUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil.DataCallback;
import com.android.splus.sdk.utils.http.RequestModel;
import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.md5.MD5Util;
import com.android.splus.sdk.utils.progressDialog.ProgressDialogUtil;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.utils.toast.ToastUtil;
import com.android.splus.sdk.widget.CustomTextWatcher;
import com.android.splus.sdk.widget.SmsReceiver;
import com.android.splus.sdk.widget.SmsReceiver.SmsListener;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 绑定手机页面
 *
 * @ClassName:PhoneBind
 * @author xiaoming.yuan
 * @date 2013年9月29日 下午5:23:29
 */
public class PhoneBindPage extends ScrollView implements View.OnFocusChangeListener, View.OnClickListener {
    public static String TAG = "PhoneBind";

    /**
     * 平台号码
     */
    public static final String PHONE = "106550200589133839";

    private Activity mActivity;

    private boolean mHasBinded = false;

    private TextView tv_welcome, tv_relatedText, tv_hint;

    private TextView tv_unrelatedText;

    private EditText et_phone, et_code;

    private Button btn_code, btn_submit;

    private LinearLayout llayout_related, llayout_unrelated, llayout_phone;

    private final int mWait_Second = 30;

    private int mSecond;

    private TimerTask mTaskClock;

    private Timer mTimerClock;

    private ProgressDialog mDialog;

    private String mPassport;

    private Integer mUid;

    private String mServerName;

    private Integer mServerId;

    private Integer mRoleId;

    private String mRoleName;

    private String mDeviceno;

    private String mPartner;

    private String mReferer;

    private Integer mGameid;

    private String mAppkey;

    private Handler mHandler;

    private String mNumber = "0123456789";

    private SmsReceiver mSmsReceiver;

    private String mPhoneNumber;

    private boolean mGetCode = false;

    /**
     * 是否为横屏
     */
    private boolean mLandscape = true;

    private static final int SUCCESS = 1;

    /**
     * 字体颜色
     */
    private static final String COLORFE5F2E = "#fe5f2e";

    public PhoneBindPage(Activity activity, String passport, Integer uid,Integer serverId, Integer roleId,String serverName,String roleName, String deviceno, String partner, String referer, Integer gameid, String appkey, boolean bindstatus, Handler handler, int orientation) {
        super(activity);
        this.mActivity = activity;
        this.mUid = uid;
        this.mServerName = serverName;
        this.mServerName = serverName;
        this.mServerId=serverId;
        this.mRoleId=roleId;
        this.mRoleName=roleName;
        this.mGameid = gameid;
        this.mDeviceno = deviceno;
        this.mPartner = partner;
        this.mReferer = referer;
        this.mPassport = passport;
        this.mAppkey = appkey;
        this.mHasBinded = bindstatus;
        this.mHandler = handler;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            mLandscape = true;

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            mLandscape = false;
        }

        mSmsReceiver = new SmsReceiver(PHONE, mSmsListener);
        init();
    }

    private SmsListener mSmsListener = new SmsListener() {

        @Override
        public void onSmsReceive(String content) {
            LogHelper.i(TAG, content);
            int start = content.lastIndexOf(":");
            int end = content.lastIndexOf("[");
            String code = "";
            if (start >= 0 && end > start) {
                code = content.substring(start + 1, end);
            }
            et_code.setText(code);

        }
    };

    /**
     * 接收验证码
     *
     * @author xiaoming.yuan
     * @date 2013年10月15日 下午2:27:36
     * @return
     */
    private Intent registerReceiver() {
        IntentFilter filter = new IntentFilter(SmsReceiver.SMS_RECEIVED);
        filter.setPriority(1000);
        return mActivity.registerReceiver(mSmsReceiver, new IntentFilter(SmsReceiver.SMS_RECEIVED));
    }

    public void unregisterReceiver() {
        try {
            mActivity.unregisterReceiver(mSmsReceiver);
        } catch (Exception e) {
            LogHelper.i(TAG, e.toString());
        }

    }

    private void init() {
        inflate(mActivity, ResourceUtil.getLayoutId(mActivity, KR.layout.splus_person_account_phone_manager), this);
        findViews();
        initViews();
        setUserName();
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年10月11日 上午10:55:37
     * @param name
     */
    private void setUserName() {
        String replaceWelcome = KR.string.splus_person_account_phone_welcome.replace("%s", mPassport);
        tv_welcome.setText(replaceWelcome);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(tv_welcome.getText());
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor(COLORFE5F2E)), replaceWelcome.indexOf(mPassport), replaceWelcome.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv_welcome.setText(spannableStringBuilder);
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午5:24:58
     */
    private void findViews() {
        llayout_related = (LinearLayout) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_person_account_phone_related));
        llayout_unrelated = (LinearLayout) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_person_account_phone_unrelated));

        tv_welcome = (TextView) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_person_account_phone_welcome));
        tv_relatedText = (TextView) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_person_account_phone_related_text));
        tv_hint = (TextView) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_person_account_phone_hint));

        tv_unrelatedText = (TextView) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_person_account_phone_unrelated_text));
        et_phone = (EditText) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_person_account_phone_unrelated_num));
        et_code = (EditText) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_person_account_phone_unrelated_code));

        btn_code = (Button) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_person_account_phone_unrelated_getcode_btn));
        btn_code.setTextColor(Color.WHITE);

        btn_submit = (Button) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_person_account_phone_unrelated_submit_btn));
        btn_submit.setTextColor(Color.WHITE);

        llayout_phone = (LinearLayout) et_phone.getParent();
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午5:25:16
     */
    private void initViews() {
        et_phone.setSingleLine();
        et_code.setSingleLine();
        tv_hint.setText(KR.string.splus_person_account_phone_hint);
        tv_relatedText.setMovementMethod(LinkMovementMethod.getInstance());
        tv_unrelatedText.setText(KR.string.splus_person_account_phone_unrelated_text);
        et_code.setHint(KR.string.splus_person_account_phone_unrelated_code);
        et_phone.setHint(KR.string.splus_person_account_phone_unrelated_hint);
        btn_code.setText(KR.string.splus_person_account_phone_unrelated_getcode_btn);
        btn_submit.setText(KR.string.splus_person_pwd_submit_btn_text);
        if (mLandscape) {
            tv_relatedText.setText(Html.fromHtml(KR.string.splus_person_account_phone_related_text));
        } else {
            tv_relatedText.setText(Html.fromHtml(KR.string.splus_person_account_phone_related_text_portrait));
        }

        et_phone.addTextChangedListener(new CustomTextWatcher(et_phone, mNumber));
        llayout_phone.setOnFocusChangeListener(this);
        btn_code.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        setBindedStatus();
    }

    public void setBindedStatus() {
        if (mHasBinded) {
            llayout_related.setVisibility(View.VISIBLE);
            llayout_unrelated.setVisibility(View.GONE);
        } else {
            llayout_related.setVisibility(View.GONE);
            llayout_unrelated.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取短信验证码
     *
     * @author xiaoming.yuan
     * @date 2013年10月11日 下午12:10:59
     */
    private void getCode() {
        if (TextUtils.isEmpty(et_phone.getText().toString())) {
            et_phone.setError(Html.fromHtml("<font color=#000000> 手机号码不能为空！</font>"));
            et_phone.requestFocus();
            return;
        }
        mGetCode = true;
        btn_code.setEnabled(false);
        doTimerTask();
        long time = DateUtil.getUnixTime();
        mPhoneNumber = et_phone.getText().toString();
        String keyString = mGameid + mServerName + mDeviceno + mReferer + mPartner + mUid + mPassport + time + mAppkey;
        GetCodeModel getCodeModel = new GetCodeModel(mUid,  mServerId,mRoleId,mServerName,mRoleName, mGameid, MD5Util.getMd5toLowerCase(keyString), time, mDeviceno, mReferer, mReferer, mPassport, mPhoneNumber);
        mDialog = ProgressDialogUtil.showProgress(mActivity, "加载中...", null, false, false);
        registerReceiver();
        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.BINDMOBILE_URL, getCodeModel, new LoginParser()), mCodeCallBack);
    }

    /**
     * 绑定手机号码
     *
     * @author xiaoming.yuan
     * @date 2013年10月11日 下午2:25:32
     */
    public void submit() {
        if (TextUtils.isEmpty(getCodeString())) {
            et_code.setError(Html.fromHtml("<font color=#000000> 短信验证码不能为空！</font>"));
            et_code.requestFocus();
            return;
        }
        if (!mGetCode) {
            ToastUtil.showToast(mActivity, "请先获取验证码！");
            return;
        } else {
            if (!et_phone.getText().toString().trim().equals(mPhoneNumber)) {
                et_phone.setError(Html.fromHtml("<font color=#000000> 手机号码跟获取验证码的手机号不一致！</font>"));
                et_phone.requestFocus();
                return;
            }
        }
        long time = DateUtil.getUnixTime();
        String keyString = mGameid + mServerName + mDeviceno + mReferer + mPartner + mUid + mPassport + time + mAppkey;
        BindPhoneModel bindPhoneModel = new BindPhoneModel(mUid,  mServerId,mRoleId,mServerName,mRoleName, mGameid, MD5Util.getMd5toLowerCase(keyString), time, mDeviceno, mPartner, mReferer, mPassport, getCodeString(), mPhoneNumber);

        mDialog = ProgressDialogUtil.showProgress(mActivity, "加载中...", null, false, false);
        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.BINDMOBILE_URL, bindPhoneModel, new LoginParser()), mBindCallBack);
    }

    /**
     * 获取短信验证码回调
     */
    private DataCallback<JSONObject> mCodeCallBack = new DataCallback<JSONObject>() {

        @Override
        public void callbackSuccess(JSONObject paramObject) {
            hideDialog();
            LogHelper.i(TAG, paramObject.toString());
            String msg = "";
            /**
             * code：-4 请求超时, -3 加密签名错误, -2 参数不全, -1 手机号码不符合规则, 0 系统繁忙, 1
             * 获取成功当天剩余的获取次数, 2 获取超过当天的最大次数3次, 3 帐号已参加过活动, 4 手机号已参加活动, 5 短信发送失败,
             * 6 没有匹配的短信模板 {code:1,msg:(3),data:{content:zzzzzz}}
             */
            int code = paramObject.optInt("code");
            JSONObject data = paramObject.optJSONObject("data");
            if (data != null) {
                switch (code) {
                    case SUCCESS:
                        msg = data.optString("content");

                        break;
                    default:
                        msg = data.optString("content");
                        break;
                }
            }
            if (TextUtils.isEmpty(msg)) {
                msg = "获取验证码失败";
            }
            ToastUtil.showToast(mActivity, msg);
        }

        @Override
        public void callbackError(String error) {
            LogHelper.d(TAG, error);
            ToastUtil.showToast(mActivity, "短信验证码获取失败！");
        }
    };

    /**
     * 绑定手机号码回调
     */
    private DataCallback<JSONObject> mBindCallBack = new DataCallback<JSONObject>() {

        @Override
        public void callbackSuccess(JSONObject paramObject) {
            hideDialog();
            LogHelper.i(TAG, paramObject.toString());
            String msg = "";
            /**
             * code：-5 请求超时, -4 加密签名错误, -3 参数不全, -2 手机验证码错误,, -1 手机验证码不符合规则, 0
             * 系统繁忙, 1 验证成功, 2 验证超过当天最大次数5次, 3 帐号已参加过活动, 4 手机号已参加活动, 5
             * 兑换超过领取激活码时30分钟, 6 短信未发出，这人瞎猜的
             */
            int code = paramObject.optInt("code");
            JSONObject data = paramObject.optJSONObject("data");
            if (data != null) {
                switch (code) {
                    case 1:
                        msg = data.optString("content");
                        mHasBinded = true;
                        if (mHandler != null) {
                            Message message = new Message();
                            message.what = PersonActivity.PHONEBIND;
                            mHandler.sendMessage(message);
                        }
                        break;
                    default:
                        msg = data.optString("content");
                        break;
                }

            } else {
                msg = "手机号码绑定失败！";
            }
            ToastUtil.showToast(mActivity, msg);
        }

        @Override
        public void callbackError(String error) {
            LogHelper.i(TAG, error);
            hideDialog();
            ToastUtil.showToast(mActivity, "手机号码绑定失败！");
        }
    };

    /**
     * 获取输入框的验证码
     *
     * @author xiaoming.yuan
     * @date 2013年10月11日 下午12:35:07
     * @return
     */
    private String getCodeString() {
        String code = et_code.getText().toString().trim();
        return code;
    }

    /**
     * 是否已经绑定手机号码
     *
     * @author xiaoming.yuan
     * @date 2013年10月11日 上午10:42:11
     * @return
     */
    public boolean hasBindPhone() {
        return mHasBinded;
    }

    /**
     * 隐藏loading框
     *
     * @author xiaoming.yuan
     * @date 2013年10月11日 下午12:27:54
     */
    private void hideDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 倒计时Handler
     */
    @SuppressLint("HandlerLeak")
    private Handler mClockHandler = new Handler() {
        public void handleMessage(Message msg) {
            // 显示倒数秒数
            if (mSecond <= 0) {
                btn_code.setEnabled(true);
                et_phone.setEnabled(true);
                btn_code.setText(KR.string.splus_person_account_phone_unrelated_getcode_btn);
                mSecond = mWait_Second;
                if (mTimerClock != null) {
                    mTimerClock.cancel();
                    mTimerClock = null;
                }
            } else {
                if (mLandscape) {
                    btn_code.setText(KR.string.splus_person_account_phone_unrelated_getcode_btn + "\n(" + mSecond + "秒)");
                } else {
                    btn_code.setText(KR.string.splus_person_account_phone_unrelated_getcode_btn + "(" + mSecond + "秒)");
                }
            }
            mSecond--;
        }
    };

    /**
     * 倒计时
     *
     * @author xiaoming.yuan
     * @date 2013年10月11日 上午11:36:25
     */
    private void doTimerTask() {
        et_phone.setEnabled(false);
        mSecond = mWait_Second;
        if (mTimerClock != null) {
            mTimerClock.cancel();
        }
        mTaskClock = new TimerTask() {
            @Override
            public void run() {
                mClockHandler.sendEmptyMessage(0);
            }
        };
        mTimerClock = new Timer();
        mTimerClock.schedule(mTaskClock, 0, 1000);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            return;
        }
        if (v.equals(llayout_phone)) {
            CommonUtil.showInput(mActivity, et_phone);
            return;
        }

    }

    @Override
    public void onClick(View v) {
        if (v.equals(btn_code)) {
            getCode();
        }
        if (v.equals(btn_submit)) {
            submit();
        }
    }

}
