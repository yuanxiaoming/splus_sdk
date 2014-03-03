/**
 * 系统项目名称
 * com.sanqi.android.sdk.login
 * LoginView.java
 * xiaoming.yuan
 *
 */

package com.android.splus.sdk.ui;

import com.android.splus.sdk.apiinterface.LoginCallBack;
import com.android.splus.sdk.manager.AccountObservable;
import com.android.splus.sdk.manager.ExitAppUtils;
import com.android.splus.sdk.model.LoginModel;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.parse.LoginParser;
import com.android.splus.sdk.ui.LoginDialog.ViewRecoveryState;
import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.date.DateUtil;
import com.android.splus.sdk.utils.file.AppUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil.DataCallback;
import com.android.splus.sdk.utils.http.RequestModel;
import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.md5.MD5Util;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.utils.sharedPreferences.SharedPreferencesHelper;
import com.android.splus.sdk.utils.toast.ToastUtil;
import com.android.splus.sdk.widget.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * LoginView xiaoming.yuan 2013-12-11 下午2:52:20
 *
 * @version 1.0.0
 */
public class LoginView extends LinearLayout implements ViewRecoveryState, Observer {
    protected final String TAG = "LoginView";

    private EditText et_userName, et_password;

    private ImageView iv_more, iv_close, iv_back;

    private Button btn_register, btn_login;

    private TextView tv_fortgetPwd;

    private CheckBox cb_remember_pwd;

    private LoginModel mLoginData;

    private LoginCallBack mLoginCallBack;

    private ArrayList<UserModel> mAllUsers;

    private PopupWindow popView;

    private LoginAdapter dropDownAdapter;

    private String mPassport;

    private String mPassword;

    public static final String ISFROMEASYLOGIN = "isFromEasyLogin";

    protected CustomProgressDialog mProgressDialog;

    private Activity mActivity;

    private long mClickTime = 0;

    private LoginDialog mAlertDialog;

    /**
     * 创建一个新的实例 LoginView.
     *
     * @param context
     */
    public LoginView(Activity activity, LoginDialog alertDialog) {
        super(activity);
        this.mActivity = activity;
        this.mAlertDialog = alertDialog;
        addView(inflate(mActivity,
                ResourceUtil.getLayoutId(mActivity, KR.layout.splus_login_activity), null),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        AccountObservable.getInstance().addObserver(LoginView.this);
        findViewById();
        setListener();
        processLogic();
        initPopView();

    }

    private void findViewById() {
        et_userName = (EditText) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_et_username));
        et_userName.setHint(KR.string.splus_login_et_username_hint);
        et_password = (EditText) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_et_userpassword));
        et_password.setHint(KR.string.splus_login_et_userpassword_hint);
        iv_more = (ImageView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_account_iv_more));
        btn_register = (Button) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_btn_register));
        btn_login = (Button) findViewById(ResourceUtil
                .getId(mActivity, KR.id.splus_login_btn_login));

        btn_login.setText(KR.string.splus_login_btn_text);
        tv_fortgetPwd = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_tv_forgetpassword));
        tv_fortgetPwd.setText((Html.fromHtml("<u>" + "忘记密码" + "</u>")));
        cb_remember_pwd = (CheckBox) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_remember_checkbox));
        cb_remember_pwd.setText(KR.string.splus_login_remember_password_text);// checkBox
        iv_close = (ImageView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_iv_close));

        iv_back = (ImageView) findViewById(ResourceUtil.getId(mActivity, KR.id.splus_login_back));
        iv_back.setVisibility(View.GONE);
        // 输入框
        TextView splus_login_account_title = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_account_title));
        splus_login_account_title.setTextColor(Color.parseColor("#9a9a9a"));
        splus_login_account_title.setText(KR.string.splus_login_account_title);
        TextView splus_login_password_title = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_password_title));
        splus_login_password_title.setTextColor(Color.parseColor("#9a9a9a"));
        splus_login_password_title.setText(KR.string.splus_login_password_title);
        splus_login_password_title.setSingleLine();

    }

    private void setListener() {

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isQuickClick()) {
                    return;
                }
                clickDisableCompons();
                login();

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isQuickClick()) {
                    return;
                }
                clickDisableCompons();
                mAlertDialog.changeView(RegisterView.class.getSimpleName());
                clickActionedEnableCompons();
                // 发送一键注册 请求

            }
        });

        tv_fortgetPwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_userName.getText().toString())) {
                    ToastUtil.showToast(mActivity, "请先输入您的帐号");
                    return;
                }
                if (isQuickClick()) {
                    return;
                }
                // 密码找回界面
                // Intent intent = new Intent(mActivity, PersonActivity.class);
                // intent.putExtra(PersonActivity.INTENT_TYPE,
                // PersonActivity.INTENT_SQ);
                // intent.putExtra(Constant.LOGIN_INTENT_USERNAME,
                // et_userName.getText().toString());
                // mActivity.startActivity(intent);

            }
        });
        et_userName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                et_password.setText(null);
                CommonUtil.setEditTextInputTypeAndMaxlength(s, 20);
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CommonUtil.setEditTextInputTypeAndMaxlength(s, 20);

            }
        });
        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isQuickClick()) {
                    return;
                } else if (popView != null) {
                    if (!popView.isShowing() && (mAllUsers != null && mAllUsers.size() > 0)) {
                        // 如果有已经登录过账号
                        initPopView();
                        popView.showAsDropDown(et_userName, 0, 1);
                    } else {
                        popView.dismiss();
                    }
                }
            }
        });
        iv_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isQuickClick()) {
                    return;
                }
                clickDisableCompons();
                mAlertDialog.dismiss();
                clickActionedEnableCompons();
                clickActionedEnableCompons();
            }
        });

    }

    private void processLogic() {
        // 如果是新设备，则一键注册显示为 注册
        if (SplusPayManager.getInstance().isNewDevice()) {
            btn_register.setText("一键注册");
        } else {
            btn_register.setText("注册");
        }
        mAllUsers = AccountObservable.getInstance().getAllUserData();
        if (mAllUsers == null || (mAllUsers != null && mAllUsers.size() <= 1)) {
            iv_more.setVisibility(View.GONE);
        }
        mLoginCallBack = SplusPayManager.getInstance().getLoginCallBack();

        if (null == AccountObservable.getInstance().getAllUserData()
                || AccountObservable.getInstance().getAllUserData().size() <= 0) {
            if (!SplusPayManager.getInstance().isNewDevice()) {
                et_userName.setText(SplusPayManager.getInstance().getEasyRegisterUserName());
            }
        } else {
            et_userName.setText(getPassport());
        }
        UserModel UserMode = SplusPayManager.getInstance().getUserData();
        if (UserMode == null) {
            UserMode = AppUtil.getUserData();
        }
        if (null != UserMode && null != UserMode.getUserName()) {
            if (UserMode.getChecked()) {
                // 对应的账户点击了 保存密码，将用户密码自动填写到密码框
                et_password.setText(getPassword());
                cb_remember_pwd.setChecked(true);
            } else {
                cb_remember_pwd.setChecked(false);
            }
        }

    }

    /**
     * login(登陆) (这里描述这个方法适用条件 – 可选) void
     *
     * @exception
     * @since 1.0.0 xiaoming.yuan
     */
    private void login() {
        mPassport = et_userName.getText().toString().trim();
        mPassword = et_password.getText().toString().trim();
        if (mPassport.equals("") || null == mPassport) {
            clickActionedEnableCompons();
            ToastUtil.showToast(mActivity, "账号不能为空!!!");
            return;
        }
        if (mPassword.equals("") || null == mPassword) {
            clickActionedEnableCompons();
            ToastUtil.showToast(mActivity, "密码不能为空!!!");
            return;
        }

        if (!isUserNameCorrect(mPassport)) {
            clickActionedEnableCompons();
            ToastUtil.showToast(mActivity, "账号格式不对!!!");
            return;
        }
        if (!isPasswordCorrect(mPassword)) {
            clickActionedEnableCompons();
            ToastUtil.showToast(mActivity, "密码格式不对!!!");
            return;
        }
        if (mPassword.equals(mPassport)) {
            clickActionedEnableCompons();
            ToastUtil.showToast(mActivity, "账号和密码不能一样!!!");

            return;
        }
        SharedPreferencesHelper.getInstance().setLoginStatusPreferences(mActivity,
                SplusPayManager.getInstance().getAppkey(), false);
        String deviceno = getDeviceno();
        Long time = DateUtil.getUnixTime();

        String keyString = SplusPayManager.getInstance().getGameid() + deviceno
                + SplusPayManager.getInstance().getReferer()
                + SplusPayManager.getInstance().getPartner() + mPassport + mPassword + time;

        mLoginData = new LoginModel(SplusPayManager.getInstance().getGameid(), deviceno,
                SplusPayManager.getInstance().getPartner(), SplusPayManager.getInstance()
                        .getReferer(), mPassport, mPassword, time, CommonUtil.getDebug(),
                MD5Util.getMd5toLowerCase(keyString + SplusPayManager.getInstance().getAppkey()));
        getDataFromServer(new RequestModel(Constant.LOGIN_URL, mActivity, mLoginData,
                new LoginParser()), onLoginCallBack);
    }

    protected <T> void getDataFromServer(RequestModel mRequestModel, DataCallback<T> callBack) {

        if (setIsShowProgressDialog()) {
            if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                showProgressDialog();
            }
        }
        NetHttpUtil.getDataFromServerPOST(mActivity, mRequestModel, callBack);
    }

    private DataCallback<JSONObject> onLoginCallBack = new DataCallback<JSONObject>() {

        @Override
        public void callbackSuccess(JSONObject paramObject) {
            try {
                String msg = paramObject.optString("msg");
                closeProgressDialog();
                clickActionedEnableCompons();
                if (paramObject != null && paramObject.optInt("code") == 1) {
                    JSONObject jsonObject = paramObject.optJSONObject("data");
                    String sessionid = jsonObject.optString("sessionid");
                    int uid = jsonObject.getInt("uid");
                    int time = jsonObject.getInt("time");
                    String sign = jsonObject.getString("sign");
                    if (sign.equals(MD5Util.getMd5toLowerCase(sessionid + uid + time
                            + SplusPayManager.getInstance().getAppkey()))) {

                        UserModel UserMode = new UserModel(uid, mPassport, mPassword, sessionid,
                                time, Boolean.valueOf(cb_remember_pwd.isChecked()));
                        // 保存用户数据
                        SplusPayManager.getInstance().setUserData(UserMode);
                        AccountObservable.getInstance().modifyUser(UserMode);
                        //登录状态
                        SharedPreferencesHelper.getInstance().setLoginStatusPreferences(mActivity,
                                SplusPayManager.getInstance().getAppkey(), true);
                        ToastUtil.showPassportToast(mActivity, mPassport);
                        ExitAppUtils.getInstance().exit();
                        if (mLoginCallBack != null) {
                            LogHelper.i(TAG, "登录成功");
                            mLoginCallBack.loginSuccess(UserMode);
                        }
                        // BindPhoneDialog.getInstance(mActivity).getBindStatusFromServer(
                        // mProgressDialog, mAlertDialog);
                    } else {
                        LogHelper.i(TAG, msg);
                        ToastUtil.showToast(mActivity, "登录失败");
                    }
                } else if (paramObject.getInt("code") == 17) {
                    LogHelper.i(TAG, msg);
                    ToastUtil.showToast(mActivity, msg);
                    if (mLoginCallBack != null) {
                        mLoginCallBack.loginFaile(msg);
                    }

                } else if (paramObject.getInt("code") == 16) {
                    LogHelper.i(TAG, msg);
                    ToastUtil.showToast(mActivity, msg);
                    if (mLoginCallBack != null) {
                        mLoginCallBack.loginFaile(msg);
                    }

                } else if (paramObject.getInt("code") == 10) {
                    LogHelper.i(TAG, msg);
                    ToastUtil.showToast(mActivity, msg);
                    if (mLoginCallBack != null) {
                        mLoginCallBack.loginFaile(msg);
                    }

                } else {
                    ToastUtil.showToast(mActivity, "登录失败");
                    LogHelper.i(TAG, msg);
                    if (mLoginCallBack != null) {
                        mLoginCallBack.loginFaile(msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                clickActionedEnableCompons();
                if (mLoginCallBack != null) {
                    mLoginCallBack.loginFaile(e.getLocalizedMessage());
                }
                ToastUtil.showToast(mActivity, "登录失败");
                LogHelper.i(TAG, "JSONException");
            }
        }

        @Override
        public void callbackError(String error) {
            closeProgressDialog();
            clickActionedEnableCompons();
            LogHelper.i(TAG, error);
            ToastUtil.showToast(mActivity, "登录失败");
            if (mLoginCallBack != null) {
                mLoginCallBack.loginFaile(error);
            }
        }
    };

    /***
     * @Title: setIsShowProgressDialog(是否显示对话框)
     * @data 2013-10-3 下午4:09:53
     * @return boolean 返回类型
     */
    public boolean setIsShowProgressDialog() {
        return true;
    }

    private synchronized boolean isQuickClick() {
        long current = System.currentTimeMillis();
        if (current - mClickTime < 400) {
            mClickTime = current;
            return true;
        }
        mClickTime = current;
        return false;
    }

    /**
     * clickDisableCompons(当点击动作发生，禁用其他控件功能，防止频繁点击) (这里描述这个方法适用条件 – 可选) void
     *
     * @exception
     * @since 1.0.0 xiaoming.yuan
     */

    private void clickDisableCompons() {
        btn_login.setEnabled(false);
        btn_register.setEnabled(false);
        tv_fortgetPwd.setEnabled(false);
        iv_more.setEnabled(false);
    }

    /**
     * clickActionedEnableCompons(当点击动作完成了恢复控件功能) (这里描述这个方法适用条件 – 可选) void
     *
     * @exception
     * @since 1.0.0 xiaoming.yuan
     */
    private void clickActionedEnableCompons() {
        if (btn_register != null) {
            btn_register.setEnabled(true);
        }
        if (tv_fortgetPwd != null) {
            tv_fortgetPwd.setEnabled(true);
        }
        if (iv_more != null) {
            iv_more.setEnabled(true);
        }
        if (btn_login != null) {
            btn_login.setEnabled(true);
        }
    }

    private void initPopView() {
        ListView listView = new ListView(mActivity);
        listView.setCacheColorHint(0xffffffff);
        listView.setDividerHeight(0);
        listView.setFocusable(false);
        listView.setPadding(1, 1, 1, 1);
        popView = new PopupWindow(listView, et_userName.getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popView.setFocusable(true);
        popView.setOutsideTouchable(true);
        popView.setBackgroundDrawable(mActivity.getResources().getDrawable(
                ResourceUtil.getDrawableId(mActivity, KR.drawable.splus_login_input_background)));
        dropDownAdapter = new LoginAdapter(mActivity, mAllUsers, et_userName, et_password,
                cb_remember_pwd, popView);
        listView.setAdapter(dropDownAdapter);
        dropDownAdapter.notifyDataSetChanged();
    }

    /**
     * @return String 返回类型
     * @Title: password(获取用户密码)
     * @data 2013-8-14 下午12:33:26
     */
    protected String getPassword() {
        UserModel mUserData = SplusPayManager.getInstance().getUserData();
        if (mUserData == null) {
            mUserData = AppUtil.getUserData();
        }
        if (null != mUserData && null != mUserData.getPassword()) {
            return mUserData.getPassword();
        }
        return "";
    }

    // 验证密码是否格式良好
    protected boolean isPasswordCorrect(String password) {
        if (!TextUtils.isEmpty(password)) {
            Pattern pattern = Pattern.compile("^[0-9a-zA-Z_]{6,20}$");
            Matcher matcher = pattern.matcher(password);
            return matcher.find();
        }
        return false;
    }

    // 用户是否格式良好
    protected boolean isUserNameCorrect(String userName) {
        if (!TextUtils.isEmpty(userName)) {
            Pattern pattern = Pattern.compile("^[0-9a-zA-Z_]{4,20}$");
            Matcher matcher = pattern.matcher(userName);
            return matcher.find();
        }
        return false;
    }

    /**
     * @return String 返回类型
     * @Title: passport(获取用户名)
     * @data 2013-8-14 下午12:33:59
     */
    protected String getPassport() {
        UserModel mUserData = SplusPayManager.getInstance().getUserData();
        if (mUserData == null) {
            mUserData = AppUtil.getUserData();
        }
        if (null != mUserData && null != mUserData.getUserName()) {
            return mUserData.getUserName();
        }
        return "";
    }

    /**
     * @return String 返回类型
     * @Title: getDeviceno(获取设备标识)
     * @author xiaoming.yuan
     * @data 2013-8-10 下午4:55:01
     */
    protected String getDeviceno() {
        return SharedPreferencesHelper.getInstance().getdevicenoPreferences(mActivity);
    }

    protected void showProgressDialog() {
        if (!(mAlertDialog != null && mAlertDialog.isShowing())) {
            return;
        }
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
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing())
            this.mProgressDialog.dismiss();
    }

    /**
     * Title: recoveryState Description:
     *
     * @see com.canhe.android.sdk.ui.LoginDialog.ViewRecoveryState#recoveryState()
     */

    @Override
    public void recoveryState() {
        // 将视图中的值重新恢复
        processLogic();
    }

    /**
     * Title: update Description:
     *
     * @param observable
     * @param data
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable observable, Object data) {

        mAllUsers = AccountObservable.getInstance().getAllUserData();
        dropDownAdapter.notifyDataSetChanged();
        if (mAllUsers.size() > 1) {
            iv_more.setVisibility(View.VISIBLE);
        } else {
            iv_more.setVisibility(View.GONE);
            if (popView != null && popView.isShowing()) {
                popView.dismiss();
            }
        }
    }

}
