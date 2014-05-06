/**
 * @Title: BindPhoneWindow.java
 * @Package: com.android.splus.sdk.ui
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013年11月18日 上午9:50:26
 * @version 1.0
 */

package com.android.splus.sdk.ui;

import com.android.splus.sdk.apiinterface.LoginCallBack;
import com.android.splus.sdk.manager.ExitAppUtils;
import com.android.splus.sdk.model.BindPhoneModel;
import com.android.splus.sdk.model.CheckPhoneModel;
import com.android.splus.sdk.model.GetCodeModel;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.parse.LoginParser;
import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.date.DateUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil.DataCallback;
import com.android.splus.sdk.utils.http.RequestModel;
import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.md5.MD5Util;
import com.android.splus.sdk.utils.phone.Phoneuitl;
import com.android.splus.sdk.utils.progressDialog.ProgressDialogUtil;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.utils.sharedPreferences.SharedPreferencesHelper;
import com.android.splus.sdk.utils.toast.ToastUtil;
import com.android.splus.sdk.widget.CustomProgressDialog;
import com.android.splus.sdk.widget.SmsReceiver;
import com.android.splus.sdk.widget.SmsReceiver.SmsListener;
import com.nd.commplatform.d.c.sp;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName:BindPhoneWindow
 * @author xiaoming.yuan
 * @date 2013年11月18日 上午9:50:26
 */
@SuppressLint("HandlerLeak")
public class BindPhoneDialog extends AlertDialog {
    public static final String TAG = "BindPhoneWindow";

    private static final int NO_BINDING = 1;

    private static final int SUCCESS = 1;

    private static BindPhoneDialog mWindow;

    private static final byte[] lock = new byte[0];

    private static Activity mActivity;

    private ProgressDialog mDialog;

    private LoginCallBack mLoginCallBack;

    private UserModel mUserModel;

    private AlertDialog mLoginRegDialog;

    private EditText mPhone_et;// 输入手机号

    private Button mBtnCode;// 提交

    private Button mBtnLater;// 下次再说

    private ImageView mCloseWindow;

    private String mPassport;

    private static final int SECOND = 1000;

    private static final int WAIT_SECOND = 60;

    private int mSecond;

    private TimerTask mTaskClock;

    private Timer mTimerClock;

    private TextView bind_phone_tv_firstline, bind_phone_tv_secondline;

    private String mPhoneNumber;

    private String mPhoneCode;

    private boolean mGetCode = false;

    private View mCurrentView;

    private View mBindPhoneView;

    private Integer mGameid;

    private String mServerName;

    private Integer mServerId;

    private Integer mRoleId;

    private String mRoleName;

    private String mDeviceno;

    private String mReferer;

    private String mPartner;

    private Integer mUid;

    private String mAppkey;

    private SplusPayManager mSplusPayManager;

    private SmsReceiver mSmsReceiver;

    /**
     * 平台号码
     */
    public static final String PHONE = "106550200589133839";

    private BindPhoneDialog(Activity activity) {
        super(activity);
        mActivity = activity;
        mSplusPayManager = SplusPayManager.getInstance();
        mLoginCallBack = mSplusPayManager.getLoginCallBack();
        mGameid = mSplusPayManager.getGameid();
        mServerName = mSplusPayManager.getServerName();
        mServerId=mSplusPayManager.getServerId();
        mRoleId=mSplusPayManager.getRoleId();
        mRoleName=mSplusPayManager.getRoleName();
        mDeviceno = SharedPreferencesHelper.getInstance().getdevicenoPreferences(mActivity);
        mReferer = mSplusPayManager.getReferer();
        mPartner = mSplusPayManager.getPartner();
        mAppkey = mSplusPayManager.getAppkey();
        mUserModel = mSplusPayManager.getUserModel();
        mUid = mUserModel.getUid();
        mPassport = mUserModel.getPassport();
    }

    public static BindPhoneDialog getInstance(Activity activity) {
        if (mWindow == null || mActivity == null || mActivity.isFinishing()) {
            synchronized (lock) {
                if (mWindow == null || mActivity == null || mActivity.isFinishing()) {
                    mWindow = new BindPhoneDialog(activity);
                }
            }
        }
        return mWindow;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBindPhoneView = LayoutInflater.from(mActivity).inflate(ResourceUtil.getLayoutId(mActivity, KR.layout.splus_login_bindphone_layout), null);
        mCurrentView = mBindPhoneView;
        setContentView(mCurrentView, CommonUtil.getFrameLayoutParams(mActivity, 120, 120, 25, 25, Gravity.CENTER));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initViews();
        setListener();
    }

    public void show() {
        super.show();
        initWindow();
        viewInit();

    }

    private void initWindow() {
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = 0.0f;
        if (mSplusPayManager.getOrientation() == Phoneuitl.getOrientation(mActivity)) {
            lp.width = mSplusPayManager.getWidth();
            lp.height = mSplusPayManager.getHeight();
        } else {
            int height = Phoneuitl.getHpixels(mActivity);
            int width = Phoneuitl.getWpixels(mActivity);
            if (Phoneuitl.getOrientation(mActivity) == Configuration.ORIENTATION_LANDSCAPE) {
                // 横屏方向，高<宽
                if (height > width) {
                    int temp = width;
                    width = height;
                    height = temp;
                }
            } else {
                // 竖屏方向 高>宽
                if (height < width) {
                    int temp = width;
                    width = height;
                    height = temp;
                }
            }
            lp.height = height;
            lp.width = width;
        }
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = android.R.style.Animation;
        window.setAttributes(lp);
    }

    private void initViews() {
        mPhone_et = (EditText) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_login_bindphone_num));
        mBtnCode = (Button) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_login_bindphone_get_code));
        mBtnCode.setTextColor(Color.WHITE);
        mBtnLater = (Button) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_login_bindphone_later));
        mBtnLater.setTextColor(Color.WHITE);
        mCloseWindow = (ImageView) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_login_iv_close));

        mPhone_et.setHint(KR.string.splus_person_account_phone_unrelated_hint);
        mBtnCode.setText(KR.string.splus_person_account_phone_unrelated_getcode_btn);
        mBtnLater.setText(KR.string.splus_login_bindphone_submit);

        bind_phone_tv_firstline = (TextView) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_login_bindphone_tv_firstline));
        bind_phone_tv_firstline.setText("您的账号很危险!");
        bind_phone_tv_firstline.setTextColor(Color.parseColor("#fe792e"));
        bind_phone_tv_firstline.setSingleLine();
        bind_phone_tv_secondline = (TextView) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_login_bindphone_tv_secondline));
        bind_phone_tv_secondline.setText("请尽快绑定手机,忘记密码后可及时通过手机找回");
        ImageView iv_title = (ImageView) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_login_iv_title));
        iv_title.setImageResource(ResourceUtil.getDrawableId(mActivity, KR.drawable.splus_login_bindphone_safe));
    }

    private void setListener() {

        mBtnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mBtnCode.getText().toString();
                String submit = KR.string.splus_person_pwd_submit_btn_text;
                String getcode_text = KR.string.splus_person_account_phone_unrelated_getcode_btn;
                if (text.equals(submit)) {
                    // 提交
                    submit();
                } else if (text.equals(getcode_text)) {
                    // 获取验证码
                    getCode(mPhone_et.getText().toString().trim());
                }
            }
        });
        mBtnLater.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text = mBtnLater.getText().toString();
                String getCode = KR.string.splus_person_account_phone_unrelated_getcode_btn;
                String later = KR.string.splus_login_bindphone_submit;
                if (text.startsWith(getCode)) {
                    // 获得验证码
                    getCode(mPhoneNumber);
                } else if (text.equals(later)) {
                    // 下次再说
                    dismiss();
                    // 写入sharepreference，记录一次
                    String passport = mUserModel.getPassport();
                    if (passport != null) {
                        SharedPreferencesHelper.getInstance().addAccountRejectBindPhoneTimes(passport, mActivity);
                    }
                    loginSuccess();
                }

            }
        });

        mCloseWindow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // 当输入框中为空的是时候，禁用按钮 mBtnCode
        // mPhone.addTextChangedListener(new TextWatcher() {
        //
        // @Override
        // public void onTextChanged(CharSequence s, int start, int before, int
        // count) {
        // }
        //
        // @Override
        // public void beforeTextChanged(CharSequence s, int start, int count,
        // int after) {
        // }
        //
        // @Override
        // public void afterTextChanged(Editable s) {
        // String input = s.toString();
        // if (TextUtils.isEmpty(input)) {
        // mBtnCode.setEnabled(false);
        // } else {
        // mBtnCode.setEnabled(true);
        // }
        // }
        // });

    }

    /**
     * 获取用户绑定状态
     *
     * @author xiaoming.yuan
     * @date 2013年10月11日 下午2:25:54
     */
    public void getBindStatusFromServer(CustomProgressDialog dialog, AlertDialog alert) {
        this.mDialog = dialog;
        this.mLoginRegDialog = alert;
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
        long time = DateUtil.getUnixTime();
        String keyString = mGameid + mServerName + mDeviceno + mReferer + mPartner + mUid + mPassport + time + mAppkey;
        CheckPhoneModel checkPhoneModel = new CheckPhoneModel(mGameid, mDeviceno, mPartner, mReferer, mUid, mServerId,mRoleId,mServerName,mRoleName, mPassport, time, MD5Util.getMd5toLowerCase(keyString));
        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.BINDMOBILE_URL, checkPhoneModel, new LoginParser()), mCheckCallBack);
    }

    /**
     * 检查用户是否绑定了手机号码回调
     */
    private DataCallback<JSONObject> mCheckCallBack = new DataCallback<JSONObject>() {

        @Override
        public void callbackSuccess(JSONObject paramObject) {
            LogHelper.d(TAG, paramObject.toString());
            closeDialog(mDialog);
            closeDialog(mLoginRegDialog);
            int code = paramObject.optInt("code");
            SharedPreferencesHelper.getInstance().setLoginStatusPreferences(mActivity, mSplusPayManager.getAppkey(), true);
            if (code == NO_BINDING) {
                // 可绑定手机
                if (!mActivity.isFinishing()) {
                    // 读取该账户中 对于 下次再说的次数 超过5次则不显示
                    int times = SharedPreferencesHelper.getInstance().getAccountRejectBindPhoneTimes(mUserModel.getPassport(), mActivity);
                    if (times < 5) {
                        BindPhoneDialog.this.show();
                    }
                    return;
                } else {
                    loginSuccess();
                }
            }
            loginSuccess();
        }

        @Override
        public void callbackError(String error) {
            LogHelper.d(TAG, error);
            closeDialog(mDialog);
            closeDialog(mLoginRegDialog);
            ToastUtil.showToast(mActivity, "查询失败！");
        }
    };

    /**
     * 获取短信验证码
     *
     * @author xiaoming.yuan
     * @date 2013年10月11日 下午12:10:59
     */
    private void getCode(String phone) {
        if (TextUtils.isEmpty(phone)) {
            mPhone_et.setError(Html.fromHtml("<font color=#000000> 手机号码不能为空！</font>"));
            mPhone_et.requestFocus();
            return;
        }
        mGetCode = true;
        mPhoneNumber = phone;
        long time = DateUtil.getUnixTime();
        String keyString = mGameid + mServerName + mDeviceno + mReferer + mPartner + mUid + mPassport + time + mAppkey;
        GetCodeModel getCodeModel = new GetCodeModel(mUid, mServerId,mRoleId,mServerName,mRoleName, mGameid, MD5Util.getMd5toLowerCase(keyString), time, mDeviceno, mReferer, mReferer, mPassport, mPhoneNumber);
        mDialog = ProgressDialogUtil.showProgress(mActivity, "加载中...", null, false, false);
        registerReceiver();
        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.BINDMOBILE_URL, getCodeModel, new LoginParser()), mCodeCallBack);
    }

    /**
     * 获取短信验证码回调
     */
    private DataCallback<JSONObject> mCodeCallBack = new DataCallback<JSONObject>() {

        @Override
        public void callbackSuccess(JSONObject paramObject) {
            closeDialog(mDialog);
            LogHelper.d(TAG, paramObject.toString());
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
                        // 获取验证码成功
                        mPhone_et.setText("");
                        mPhone_et.setHint(KR.string.splus_person_account_phone_unrelated_code);
                        mBtnCode.setText(KR.string.splus_person_pwd_submit_btn_text);// 提交
                        mBtnLater.setText(KR.string.splus_login_bindphone_resend);// 重新发送验证码
                        mBtnLater.setEnabled(false);
                        doTimerTask();
                        ToastUtil.showToast(mActivity, msg);
                        break;
                    default:
                        msg = data.optString("content");
                        mPhone_et.setError(Html.fromHtml("<font color=#000000> " + msg + "</font>"));
                        break;
                }
            }
            if (TextUtils.isEmpty(msg)) {
                msg = "获取验证码失败";
                mPhone_et.setError(Html.fromHtml("<font color=#000000> 获取验证码失败！</font>"));
                return;
            }

        }

        @Override
        public void callbackError(String error) {
            LogHelper.d(TAG, error);
            closeDialog(mDialog);
            mPhone_et.setError(Html.fromHtml("<font color=#000000> 短信验证码获取失败！</font>"));
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
        mSmsReceiver = new SmsReceiver(PHONE, mSmsListener);
        IntentFilter filter = new IntentFilter(SmsReceiver.SMS_RECEIVED);
        filter.setPriority(1000);
        return mActivity.registerReceiver(mSmsReceiver, new IntentFilter(SmsReceiver.SMS_RECEIVED));
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
            mPhone_et.setText(code);

        }
    };

    /**
     * 绑定手机号码
     *
     * @author xiaoming.yuan
     * @date 2013年10月11日 下午2:25:32
     */
    private void submit() {
        // 点击提交的时候，禁用提交按钮，直到返回结果，或者错误。
        mBtnCode.setEnabled(false);
        String text = mPhone_et.getText().toString();
        if (TextUtils.isEmpty(text)) {
            mPhone_et.setError(Html.fromHtml("<font color=#000000> 验证码不能为空！</font>"));
            mPhone_et.requestFocus();
            mBtnCode.setEnabled(true);
            return;
        } else if (text.length() != 6) {
            mPhone_et.setError(Html.fromHtml("<font color=#000000> 请输入6位验证码！</font>"));
            mPhone_et.requestFocus();
            mBtnCode.setEnabled(true);
            return;
        }

        if (!mGetCode) {
            ToastUtil.showToast(mActivity, "请先获取验证码！");
            mBtnCode.setEnabled(true);
            return;
        }
        if (mPhoneCode == null) {
            mPhoneCode = mPhone_et.getText().toString();
        }

        long time = DateUtil.getUnixTime();
        String keyString = mGameid + mServerName + mDeviceno + mReferer + mPartner + mUid + mPassport + time + mAppkey;
        BindPhoneModel bindPhoneModel = new BindPhoneModel(mUid, mServerId,mRoleId,mServerName,mRoleName, mGameid, MD5Util.getMd5toLowerCase(keyString), time, mDeviceno, mPartner, mReferer, mPassport, mPhoneCode, mPhoneNumber);

        mDialog = ProgressDialogUtil.showProgress(mActivity, "加载中...", null, false, false);
        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.BINDMOBILE_URL, bindPhoneModel, new LoginParser()), mBindCallBack);
    }

    /**
     * 绑定手机号码回调
     */
    private DataCallback<JSONObject> mBindCallBack = new DataCallback<JSONObject>() {

        @Override
        public void callbackSuccess(JSONObject paramObject) {
            closeDialog(mDialog);
            mBtnCode.setEnabled(true);
            LogHelper.d(TAG, paramObject.toString());
            String titleStr = "";
            String contentStr = "";
            /**
             * code：-5 请求超时, -4 加密签名错误, -3 参数不全, -2 手机验证码错误,, -1 手机验证码不符合规则, 0
             * 系统繁忙, 1 验证成功, 2 验证超过当天最大次数5次, 3 帐号已参加过活动, 4 手机号已参加活动, 5
             * 兑换超过领取激活码时30分钟, 6 短信未发出，这人瞎猜的
             */
            int code = paramObject.optInt("code");
            JSONObject data = paramObject.optJSONObject("data");
            if (data != null) {
                switch (code) {
                    case SUCCESS:
                        titleStr = data.optString("title");
                        contentStr = data.optString("content");
                        // setContentView(new BindPhoneSuccessView(mActivity,
                        // BindPhoneDialog.this,
                        // mPassport, mPhoneNumber));
                        ToastUtil.showToast(mActivity, contentStr);
                        break;
                    default:
                        titleStr = data.optString("title");
                        contentStr = data.optString("content");
                        mPhone_et.setError(Html.fromHtml("<font color=#000000> " + contentStr + "</font>"));
                        break;
                }
            } else {
                mPhone_et.setError(Html.fromHtml("<font color=#000000> 手机号码绑定失败！</font>"));
            }

        }

        @Override
        public void callbackError(String error) {
            LogHelper.d(TAG, error);
            closeDialog(mDialog);
            mBtnCode.setEnabled(true);
            mPhone_et.setError(Html.fromHtml("<font color=#000000> 手机号码绑定失败！</font>"));
        }
    };

    /**
     * 倒计时
     *
     * @author xiaoming.yuan
     * @date 2013年10月11日 上午11:36:25
     */
    private void doTimerTask() {
        mSecond = WAIT_SECOND;
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
        mTimerClock.schedule(mTaskClock, 0, SECOND);
    }

    /**
     * 倒计时Handler
     */
    private Handler mClockHandler = new Handler() {
        public void handleMessage(Message msg) {
            // 显示倒数秒数
            if (mSecond <= 0) {
                mBtnCode.setText(KR.string.splus_person_pwd_submit_btn_text);
                mBtnLater.setText(KR.string.splus_login_bindphone_resend);
                mBtnLater.setEnabled(true);
                mSecond = WAIT_SECOND;
                if (mTimerClock != null) {
                    mTimerClock.cancel();
                    mTimerClock = null;
                }
            } else {
                mBtnLater.setText(KR.string.splus_login_bindphone_resend + "(" + mSecond + "秒)");
                mBtnLater.setEnabled(false);
            }
            mSecond--;
        }
    };

    protected void loginSuccess() {
        SharedPreferencesHelper.getInstance().setLoginStatusPreferences(mActivity, mSplusPayManager.getAppkey(), true);
        if (mLoginCallBack != null) {
            mLoginCallBack.loginSuccess(mUserModel);
        }
        ExitAppUtils.getInstance().exit();
        // 登陆成功，弹出悬浮欢迎框
        ToastUtil.showPassportToast(mActivity, mPassport);
    }

    private void closeDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                LogHelper.d(TAG, e.getMessage());
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        loginSuccess();
    }

    public void viewInit() {
        mPhone_et.setText("");
        mPhone_et.setHint(KR.string.splus_person_account_phone_unrelated_hint);
        mGetCode = false;
        mPhoneNumber = null;
        mBtnCode.setText(KR.string.splus_person_account_phone_unrelated_getcode_btn);
        mSecond = WAIT_SECOND;
        if (mTimerClock != null) {
            mTimerClock.cancel();
            mTimerClock = null;
        }
        mBtnLater.setEnabled(true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            initWindow();
        }
        super.onWindowFocusChanged(hasFocus);

    }

    public void changeCurrentView() {
        mCurrentView = mBindPhoneView;
        // 同时将相关的值，切成以前的状态。
        mBtnCode.setText(KR.string.splus_person_account_phone_unrelated_getcode_btn);
        mBtnLater.setText(KR.string.splus_person_pwd_submit_btn_text);
        mPhone_et.setText("");
        mPhone_et.setHint(KR.string.splus_person_account_phone_unrelated_hint);
        setContentView(mCurrentView, CommonUtil.getFrameLayoutParams(mActivity, 120, 120, 25, 25, Gravity.CENTER));

    }

}
