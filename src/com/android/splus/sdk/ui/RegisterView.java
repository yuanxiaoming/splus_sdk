/**
 * @Title: RegisterWindow.java
 * @Package: com.sanqi.android.sdk.ui
 * @Description: (注册对话框)
 * Copyright: Copyright (c) 2013
 * Company: 上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013年10月23日 下午3:07:30
 * @version 1.0
 */

package com.android.splus.sdk.ui;

import com.android.splus.sdk.apiinterface.LoginCallBack;
import com.android.splus.sdk.manager.AccountObservable;
import com.android.splus.sdk.manager.ExitAppUtils;
import com.android.splus.sdk.model.RegisterModel;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.parse.LoginParser;
import com.android.splus.sdk.ui.LoginDialog.ViewRecoveryState;
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
import com.android.splus.sdk.utils.sharedPreferences.SharedPreferencesHelper;
import com.android.splus.sdk.utils.toast.ToastUtil;
import com.android.splus.sdk.widget.CustomProgressDialog;
import com.android.splus.sdk.widget.ScrollForeverTextView;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterView extends LinearLayout implements ViewRecoveryState {

    private View mDialogClauseView;// 条款视图

    private Dialog mDialogClause;// 条款对话框

    private FrameLayout.LayoutParams mDialogClauseparams;// 条款对话框参数

    private EditText et_userName, et_password;

    private ImageView iv_close, iv_more, iv_title;

    private Button btn_login;

    private ScrollForeverTextView tv_agreeClause;

    private CheckBox cb_agreeClause;

    private ImageView splus_login_back;

    private LoginCallBack mRegisterCallBack;

    private String mPassport;

    private String mPassword;

    private RegisterModel mRegisterModel;

    protected String TAG = this.getClass().getSimpleName();

    protected CustomProgressDialog mProgressDialog;

    private Activity mActivity;

    private long mClickTime = 0;

    private LoginDialog mAlertDialog;

    private static final String EASY_REGISTER = "1";

    private static final String COMMON_REGISTER = "0";

    /**
     * 创建一个新的实例 RegisterView.
     *
     * @param context
     */
    public RegisterView(Activity activity, LoginDialog alertDialog) {
        super(activity);
        this.mActivity = activity;
        this.mAlertDialog = alertDialog;
        addView(inflate(mActivity,
                ResourceUtil.getLayoutId(mActivity, KR.layout.splus_register_activity), null),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        findViewById();
        setListener();
        processLogic();
    }

    private void findViewById() {
        // 返回
        splus_login_back = (ImageView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_back));
        splus_login_back.setVisibility(View.VISIBLE);
        et_userName = (EditText) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_et_username));
        et_password = (EditText) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_et_userpassword));
        iv_close = (ImageView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_iv_close));
        tv_agreeClause = (ScrollForeverTextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_register_tv_agreeClause));
        tv_agreeClause.setText(Html.fromHtml("同意<u>" + KR.string.splus_register_tv_agreeClause_text
                + "</u>"));// 同意条款
        btn_login = (Button) findViewById(ResourceUtil
                .getId(mActivity, KR.id.splus_login_btn_login));
        btn_login.setText(KR.string.splus_login_btn_text);
        cb_agreeClause = (CheckBox) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_register_agreeClause_checkbox));

        et_userName.setHint(KR.string.splus_register_et_username_hint);
        et_userName.setHintTextColor(Color.parseColor("#c1c1c1"));
        et_password.setHint(KR.string.splus_login_et_userpassword_hint);
        et_password.setHintTextColor(Color.parseColor("#c1c1c1"));

        iv_more = (ImageView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_account_iv_more));
        iv_more.setVisibility(View.GONE);
        iv_title = (ImageView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_iv_title));
        iv_title.setImageResource(ResourceUtil.getDrawableId(mActivity,
                KR.drawable.splus_register_title));
        // 输入框
        TextView splus_login_account_title = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_account_title));
        splus_login_account_title.setText(KR.string.splus_login_account_title);
        splus_login_account_title.setTextColor(Color.parseColor("#9a9a9a"));
        TextView splus_login_password_title = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_login_password_title));
        splus_login_password_title.setText(KR.string.splus_login_password_title);
        splus_login_password_title.setSingleLine();
        splus_login_password_title.setTextColor(Color.parseColor("#9a9a9a"));
    }

    private void setListener() {
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isQuickClick()) {
                    return;
                }
                clickDisableCompons();
                register();

            }
        });

        tv_agreeClause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isQuickClick()) {
                    return;
                }
                clickDisableCompons();
                showClause();

            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isQuickClick()) {
                    return;
                }
                clickDisableCompons();
                closeDialog(mAlertDialog);
                clickActionedEnableCompons();
                if (mRegisterCallBack != null) {
                    mRegisterCallBack.backKey("取消登录操作");
                }
            }
        });

        splus_login_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isQuickClick()) {
                    return;
                }
                clickDisableCompons();
                mAlertDialog.changeView(LoginView.class.getSimpleName());
                requestFocus();
                // 隐藏键盘
                View focus = RegisterView.this.findFocus();
                ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(focus.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                clickActionedEnableCompons();
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
                String text = s.toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    int lenght = text.length();
                    if (lenght >= 21) {
                        et_password.setText(text.subSequence(0, lenght - 1));
                    }
                }
            }
        });
    }

    private void processLogic() {
        // 如果是新设备，则显示为 一键注册的账号
        if (SplusPayManager.getInstance().isNewDevice()) {
            et_userName.setText(SplusPayManager.getInstance().getEasyRegisterPassport());
        }
        if (!TextUtils.isEmpty(et_userName.getText().toString())) {
            et_userName.setSelection(et_userName.getText().toString().length());
        }
        mRegisterCallBack = SplusPayManager.getInstance().getLoginCallBack();
        if (cb_agreeClause != null) {
            cb_agreeClause.setChecked(true);
        }
    }

    /**
     * register(完成注册) void
     *
     * @exception
     * @since 1.0.0 xiaoming.yuan
     */
    private void register() {
        mPassport = et_userName.getText().toString().trim();
        mPassword = et_password.getText().toString().trim();
        if (!cb_agreeClause.isChecked()) {
            clickActionedEnableCompons();
            ToastUtil.showToast(mActivity, "请勾选“用户服务条款”");
            return;
        }
        if (TextUtils.isEmpty(mPassport)) {
            clickActionedEnableCompons();
            ToastUtil.showToast(mActivity, "账号不能为空!!!");
            return;
        }
        if (TextUtils.isEmpty(mPassword)) {
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
        String deviceno = getDeviceno();
        Long time = DateUtil.getUnixTime();
        String keyString = SplusPayManager.getInstance().getGameid() + deviceno
                + SplusPayManager.getInstance().getReferer()
                + SplusPayManager.getInstance().getPartner() + mPassport + mPassword + time;
        if (SplusPayManager.getInstance().isNewDevice()
                && mPassport.equals(SplusPayManager.getInstance().getEasyRegisterPassport())) {
            mRegisterModel = new RegisterModel(
                    SplusPayManager.getInstance().getGameid(),
                    deviceno,
                    SplusPayManager.getInstance().getPartner(),
                    SplusPayManager.getInstance().getReferer(),
                    mPassport,
                    mPassword,
                    time,
                    MD5Util.getMd5toLowerCase(keyString + SplusPayManager.getInstance().getAppkey()),
                    EASY_REGISTER);// 一键注册
        } else {
            mRegisterModel = new RegisterModel(
                    SplusPayManager.getInstance().getGameid(),
                    deviceno,
                    SplusPayManager.getInstance().getPartner(),
                    SplusPayManager.getInstance().getReferer(),
                    mPassport,
                    mPassword,
                    time,
                    MD5Util.getMd5toLowerCase(keyString + SplusPayManager.getInstance().getAppkey()),
                    COMMON_REGISTER);// 普通注册
        }
        SharedPreferencesHelper.getInstance().setLoginStatusPreferences(mActivity,
                SplusPayManager.getInstance().getAppkey(), false);
        // 一键注册 时注册接口
        getDataFromServer(new RequestModel(Constant.REGISTER_URL, mActivity, mRegisterModel,
                new LoginParser()), onRegisterCallBack);
    }

    private DataCallback<JSONObject> onRegisterCallBack = new DataCallback<JSONObject>() {

        @Override
        public void callbackSuccess(JSONObject paramObject) {
            try {
                SharedPreferencesHelper.getInstance().setLoginStatusPreferences(mActivity,
                        SplusPayManager.getInstance().getAppkey(), false);
                closeProgressDialog();
                clickActionedEnableCompons();
                String msg = paramObject.optString("msg");
                if (paramObject != null && paramObject.optInt("code") == 1) {
                    JSONObject jsonObject = paramObject.optJSONObject("data");
                    String sessionid = jsonObject.optString("sessionid");

                    int uid = jsonObject.optInt("uid");

                    int time = jsonObject.optInt("time");

                    String sign = jsonObject.optString("sign");

                    if (sign.equals(MD5Util.getMd5toLowerCase(sessionid + uid + time
                            + SplusPayManager.getInstance().getAppkey()))) {

                        UserModel userModel = new UserModel(uid, mPassport, mPassword, sessionid,
                                time, Boolean.valueOf(true));
                        // 保存用户数据
                        SplusPayManager.getInstance().setUserModel(userModel);
                        AccountObservable.getInstance().addUser(userModel);
                        // 登陆成功，默认不再是新设备。
                        SplusPayManager.getInstance().setNewDevice(false);
                        // 登录状态
                        SharedPreferencesHelper.getInstance().setLoginStatusPreferences(mActivity,
                                SplusPayManager.getInstance().getAppkey(), true);
                        closeDialog(mAlertDialog);
                        // 登陆成功，弹出悬浮欢迎框
                        ToastUtil.showPassportToast(mActivity, mPassport);
                        ExitAppUtils.getInstance().exit();

                        if (mRegisterCallBack != null) {
                            LogHelper.i(TAG, "注册成功");
                            mRegisterCallBack.loginSuccess(userModel);
                            // BindPhoneDialog.getInstance(mActivity).getBindStatusFromServer(
                            // mProgressDialog, mAlertDialog);
                        }
                    } else {
                        if (mRegisterCallBack != null) {
                            mRegisterCallBack.loginFaile(msg);
                        }
                        LogHelper.i(TAG, msg);
                        ToastUtil.showToast(mActivity, "注册失败");
                    }
                } else if (paramObject != null && paramObject.getInt("code") == 18) {
                    ToastUtil.showToast(mActivity, msg);
                    // 该账号已经存在，用户名 清空，密码清空。
                    if (et_userName != null) {
                        et_userName.setText(null);
                    }
                    if (et_password != null) {
                        et_password.setText(null);
                    }
                    if (mRegisterCallBack != null) {
                        mRegisterCallBack.loginFaile(msg);
                    }
                } else if (paramObject != null && paramObject.getInt("code") == 9) {
                    ToastUtil.showToast(mActivity, msg);
                    // 该账号已经存在，用户名 清空，密码清空。
                    if (et_userName != null) {
                        et_userName.setText(null);
                    }
                    if (et_password != null) {
                        et_password.setText(null);
                    }
                    if (mRegisterCallBack != null) {
                        mRegisterCallBack.loginFaile(msg);
                    }
                } else if (paramObject != null && paramObject.getInt("code") == 16) {
                    ToastUtil.showToast(mActivity, msg);
                    // 该账号已经存在，用户名 清空，密码清空。
                    if (et_userName != null) {
                        et_userName.setText(null);
                    }
                    if (et_password != null) {
                        et_password.setText(null);
                    }
                    if (mRegisterCallBack != null) {
                        mRegisterCallBack.loginFaile(msg);
                    }
                } else {
                    // 回调 注册失败
                    if (mRegisterCallBack != null) {
                        mRegisterCallBack.loginFaile(msg);
                    }
                    LogHelper.i(TAG, msg);
                    ToastUtil.showToast(mActivity, "注册失败");

                }

            } catch (JSONException e) {
                e.printStackTrace();
                LogHelper.i(TAG, "JSONException");
                clickActionedEnableCompons();
                // 回调 注册失败
                if (mRegisterCallBack != null) {
                    mRegisterCallBack.loginFaile(e.getLocalizedMessage());
                }
                ToastUtil.showToast(mActivity, "注册失败");
            }
        }

        @Override
        public void callbackError(String error) {
            LogHelper.i(TAG, error);
            ToastUtil.showToast(mActivity, "注册失败");
            closeProgressDialog();
            clickActionedEnableCompons();
            if (mRegisterCallBack != null) {
                mRegisterCallBack.loginFaile(error);
            }
        }

    };

    protected <T> void getDataFromServer(RequestModel mRequestModel, DataCallback<T> callBack) {

        if (setIsShowProgressDialog()) {
            if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                showProgressDialog();
            }
        }
        NetHttpUtil.getDataFromServerPOST(mActivity, mRequestModel, callBack);
    }

    /**
     * 显示服务条款 showClause(显示服务条款) (这里描述这个方法适用条件 – 可选) void
     *
     * @exception
     * @since 1.0.0 修改
     */
    private void showClause() {
        mDialogClauseparams = getMParames();

        if (mDialogClause == null) {
            mDialogClause = new Dialog(mActivity, android.R.style.Theme_Panel);
            mDialogClause.addContentView(getmDialogView(), mDialogClauseparams);
            mDialogClause.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    clickActionedEnableCompons();
                }
            });
        }

        if (!mDialogClause.isShowing()) {
            mDialogClause.show();
        }

    }

    /**
     * getmDialogView(生成条款视图)
     *
     * @return View
     * @exception
     * @since 1.0.0 xiaoming.yuan
     */
    private View getmDialogView() {

        mDialogClauseView = CommonUtil.createCustomView(mActivity,
                KR.layout.splus_register_clause_dialog);
        TextView content = (TextView) mDialogClauseView.findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_register_clause_dialog_tv_content));
        content.setText(Html.fromHtml(KR.string.splus_register_clause_tips));
        Button button = (Button) mDialogClauseView.findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_register_clause_dialog_btn_agree));
        button.setText(KR.string.splus_register_clause_dialog_btn_agree);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDialogClause != null && mDialogClause.isShowing()) {
                    mDialogClause.dismiss();
                }
                if (cb_agreeClause != null) {
                    cb_agreeClause.setChecked(true);
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
                    if (cb_agreeClause != null) {
                        cb_agreeClause.setChecked(true);
                    }
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
            return CommonUtil.getFrameLayoutParams(mActivity, 120, 120, 25, 25, Gravity.CENTER);
        }
        return mDialogClauseparams;
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

    /***
     * @Title: setIsShowProgressDialog(是否显示对话框)
     * @data 2013-10-3 下午4:09:53
     * @return boolean 返回类型
     */
    public boolean setIsShowProgressDialog() {
        return true;
    }

    protected void closeProgressDialog() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing())
            this.mProgressDialog.dismiss();
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

    @Override
    public void recoveryState() {
        processLogic();
    }

    /**
     * @Title: closeDialog(关闭对话框)
     * @author xiaoming.yuan
     * @data 2014-3-3 下午3:46:11
     * @param dialog void 返回类型
     */
    private void closeDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                LogHelper.d(TAG, e.getMessage());
            }

        }
    }

    /**
     * isQuickClick(低于400ms，判断是快速点击) (这里描述这个方法适用条件 – 可选)
     *
     * @return boolean
     * @exception
     * @since 1.0.0 xilin.chen
     */
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
     * @Title: disEnableCompon(组件不可用)
     * @author xiaoming.yuan
     * @data 2014-3-3 下午3:48:39 void 返回类型
     */
    private void clickDisableCompons() {
        iv_close.setEnabled(false);
        tv_agreeClause.setEnabled(false);
        btn_login.setEnabled(false);
        splus_login_back.setEnabled(false);
    }

    /**
     * @Title: clickActionedEnableCompons(点击动作完成，恢复控件功能)
     * @author xiaoming.yuan
     * @data 2014-3-3 下午3:51:19 void 返回类型
     */
    private void clickActionedEnableCompons() {
        if (btn_login != null) {
            btn_login.setEnabled(true);
        }
        if (iv_close != null) {

            iv_close.setEnabled(true);
        }
        if (tv_agreeClause != null) {

            tv_agreeClause.setEnabled(true);
        }
        if (splus_login_back != null) {
            splus_login_back.setEnabled(true);
        }
    }

}
