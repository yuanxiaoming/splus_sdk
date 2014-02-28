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
import com.android.splus.sdk.model.RegisterModel;
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

    private FrameLayout.LayoutParams mDialogClauseparams;

    private EditText et_userName, et_userPwd;

    private ImageView iv_close;

    private Button btn_login;

    private ScrollForeverTextView tv_agreeClause;

    private CheckBox cb_agreeClause;

    private ImageView ch_login_back;

    private LoginCallBack mRegisterCallBack;

    private String mPassport;

    private String mPassword;

    private RegisterModel mRegisterModel;

    protected String TAG = this.getClass().getSimpleName();

    protected CustomProgressDialog mProgressDialog;

    private Activity mActivity;

    long mClickTime = 0;

    private LoginDialog mAlertDialog;

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
                ResourceUtil.getLayoutId(mActivity, KR.layout.ch_register_activity), null),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        findViewById();
        setListener();
        processLogic();
    }

    private void findViewById() {
        // To change body of implemented methods use File | Settings | File
        // Templates.
        // 返回
        ch_login_back = (ImageView) findViewById(ResourceUtil.getId(mActivity, KR.id.ch_login_back));
        ch_login_back.setVisibility(View.VISIBLE);
        et_userName = (EditText) findViewById(ResourceUtil.getId(mActivity,
                KR.id.ch_login_et_username));
        et_userPwd = (EditText) findViewById(ResourceUtil.getId(mActivity,
                KR.id.ch_login_et_userPwd));
        iv_close = (ImageView) findViewById(ResourceUtil.getId(mActivity, KR.id.ch_login_iv_close));
        tv_agreeClause = (ScrollForeverTextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.ch_login_tv_agreeClause));
        tv_agreeClause.setText(Html.fromHtml("同意<u>" + KR.string.ch_login_tv_agreeClause_text
                + "</u>"));// 同意条款
        btn_login = (Button) findViewById(ResourceUtil.getId(mActivity, KR.id.ch_login_btnLogin));
        btn_login.setText(KR.string.ch_register_btn_login_text);
        cb_agreeClause = (CheckBox) findViewById(ResourceUtil.getId(mActivity,
                KR.id.ch_login_cb_agreeClause));

        et_userName.setHint(KR.string.ch_login_et_username_register);
        et_userName.setHintTextColor(Color.parseColor("#c1c1c1"));
        et_userPwd.setHint(KR.string.ch_register_et_userPwd);
        et_userPwd.setHintTextColor(Color.parseColor("#c1c1c1"));

        ImageView iv_more = (ImageView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.ch_login_iv_more));
        iv_more.setVisibility(View.GONE);
        ImageView iv_title = (ImageView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.ch_login_iv_title));
        iv_title.setImageResource(ResourceUtil.getDrawableId(mActivity, KR.drawable.ch_register));
        // 输入框
        TextView ch_bindphone_account = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.ch_bindphone_account));
        ch_bindphone_account.setText(KR.string.ch_bind_phone_account);
        ch_bindphone_account.setTextColor(Color.parseColor("#9a9a9a"));
        TextView ch_bindphone_password = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.ch_bindphone_password));
        ch_bindphone_password.setText(KR.string.ch_bind_phone_password);
        ch_bindphone_password.setSingleLine();
        ch_bindphone_password.setTextColor(Color.parseColor("#9a9a9a"));
    }

    private void setListener() {
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isQuickClick()) {
                    return;
                }
                disenableCompon();
                register();

            }
        });

        tv_agreeClause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isQuickClick()) {
                    return;
                }
                disenableCompon();
                showClause();

            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isQuickClick()) {
                    return;
                }
                disenableCompon();
                mAlertDialog.dismiss();
                clickActionedEnableCompons();
                onBackPressed();
            }
        });

        ch_login_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isQuickClick()) {
                    return;
                }
                disenableCompon();
                //  切换回登陆界面
                (mAlertDialog).changeView(LoginView.class.getSimpleName());
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
        et_userPwd.addTextChangedListener(new TextWatcher() {

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
                        et_userPwd.setText(text.subSequence(0, lenght - 1));
                    }
                }
            }
        });
    }

    private void processLogic() {
        // 如果是新设备，则显示为 一键注册的账号
        if (CHPayManager.getInstance().isNewDevice()) {
            et_userName.setText(CHPayManager.getInstance().getEasyRegisterUserName());
        }
        mRegisterCallBack = CHPayManager.getInstance().getLoginCallBack();
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
        mPassword = et_userPwd.getText().toString().trim();
        if (!cb_agreeClause.isChecked()) {
            clickActionedEnableCompons();
            ToastUtil.showToast(mActivity, "请勾选“37wan用户服务条款”");
            return;
        }
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
        String deviceno = getDeviceno();
        Long time = DateUtil.getUnixTime();
        String keyString = CHPayManager.getInstance().getGameid() + deviceno
                + CHPayManager.getInstance().getReferer() + CHPayManager.getInstance().getPartner()
                + mPassport + mPassword + time;
        if (CHPayManager.getInstance().isNewDevice()
                && mPassport.equals(CHPayManager.getInstance().getEasyRegisterUserName())) {
            mRegisterModel = new RegisterModel(CHPayManager.getInstance().getGameid(), deviceno,
                    CHPayManager.getInstance().getPartner(), CHPayManager.getInstance().getReferer(),
                    mPassport, mPassword, time, CommonUtil.getDebug(), MD5Util.getMd5toLowerCase(keyString
                            + CHPayManager.getInstance().getAppkey()), "1");// 一键注册
        } else {
            mRegisterModel = new RegisterModel(CHPayManager.getInstance().getGameid(), deviceno,
                    CHPayManager.getInstance().getPartner(), CHPayManager.getInstance().getReferer(),
                    mPassport, mPassword, time, CommonUtil.getDebug(), MD5Util.getMd5toLowerCase(keyString
                            + CHPayManager.getInstance().getAppkey()), "0");// 普通注册
        }
        SharedPreferencesHelper.getInstance().setLoginStatusPreferences(mActivity,
                CHPayManager.getInstance().getAppkey(), false);
        // 一键注册 时注册接口
        getDataFromServer(new RequestModel(Constant.REGISTER_URL, mActivity, mRegisterModel,
                new LoginParser()), onRegisterCallBack);
    }

    private DataCallback<JSONObject> onRegisterCallBack = new DataCallback<JSONObject>() {

        @Override
        public void callbackSuccess(JSONObject paramObject) {
            try {
                closeProgressDialog();
                clickActionedEnableCompons();
                String msg = paramObject.getString("msg");
                if (paramObject != null && paramObject.getInt("code") == 1) {

                    String sessionid = paramObject.getJSONObject("data").getString("sessionid");

                    int uid = paramObject.getJSONObject("data").getInt("uid");

                    int time = paramObject.getJSONObject("data").getInt("time");

                    String sign = paramObject.getJSONObject("data").getString("sign");

                    if (sign.equals(MD5Util.getMd5toLowerCase(sessionid + uid + time
                            + CHPayManager.getInstance().getAppkey()))) {

                        UserModel userModel = new UserModel(uid, mPassport, mPassword, sessionid, time,
                                Boolean.valueOf(true));
                        // 保存用户数据
                        CHPayManager.getInstance().setUserData(userModel);
                        AccountObservable.getInstance().addUser(userModel);
                        LogHelper.i(TAG, "注册成功");
                        // 登陆成功，默认不再是新设备。
                        CHPayManager.getInstance().setNewDevice(false);
//                        BindPhoneDialog.getInstance(mActivity).getBindStatusFromServer(
//                                mProgressDialog, mAlertDialog);
                    } else {
                        LogHelper.i(TAG, msg);
                        ToastUtil.showToast(mActivity, "注册失败");
                    }
                } else if (paramObject != null && paramObject.getInt("code") == 18) {
                    ToastUtil.showToast(mActivity, msg);
                    // 该账号已经存在，用户名 清空，密码清空。
                    if (et_userName != null) {
                        et_userName.setText(null);
                    }
                    if (et_userPwd != null) {
                        et_userPwd.setText(null);
                    }
                } else if (paramObject != null && paramObject.getInt("code") == 9) {
                    ToastUtil.showToast(mActivity, msg);
                    // 该账号已经存在，用户名 清空，密码清空。
                    if (et_userName != null) {
                        et_userName.setText(null);
                    }
                    if (et_userPwd != null) {
                        et_userPwd.setText(null);
                    }
                } else if (paramObject != null && paramObject.getInt("code") == 16) {
                    ToastUtil.showToast(mActivity, msg);
                    // 该账号已经存在，用户名 清空，密码清空。
                    if (et_userName != null) {
                        et_userName.setText(null);
                    }
                    if (et_userPwd != null) {
                        et_userPwd.setText(null);
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

    protected void getDataFromServer(RequestModel mRequestModel, DataCallback<JSONObject> callBack) {

        if (setIsShowProgressDialog()) {
            if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                showProgressDialog();
            }
        }
        NetHttpUtil.getDataFromServerPOST(mActivity, mRequestModel, callBack);
    }

    /**
     * isQuickClick(低于400ms，判断是快速点击) (这里描述这个方法适用条件 – 可选)
     *
     * @return boolean
     * @exception
     * @since 1.0.0 xiaoming.yuan
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

    private void disenableCompon() {
        iv_close.setEnabled(false);
        tv_agreeClause.setEnabled(false);
        btn_login.setEnabled(false);
        ch_login_back.setEnabled(false);
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
     * getmDialogView(生成条款视图) (这里描述这个方法适用条件 – 可选)
     *
     * @return View
     * @exception
     * @since 1.0.0 xiaoming.yuan
     */
    private View getmDialogView() {

        mDialogClauseView = CommonUtil.createCustomView(mActivity, KR.layout.ch_loginclause_dialog);
        TextView content = (TextView) mDialogClauseView.findViewById(ResourceUtil.getId(mActivity,
                KR.id.ch_login_clause_dialog_tv_content));
        content.setText(Html.fromHtml(KR.string.ch_reg_clause_tips));
        TextView ch_login_clause_dialog_btn_agree = (TextView) mDialogClauseView
                .findViewById(ResourceUtil.getId(mActivity, KR.id.ch_login_clause_dialog_btn_agree));
        ch_login_clause_dialog_btn_agree.setText(KR.string.ch_login_clause_dialog_btn_agree);
        Button button = (Button) mDialogClauseView.findViewById(ResourceUtil.getId(mActivity,
                KR.id.ch_login_clause_dialog_btn_agree));
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
        ImageView ch_login_clause_dialog_iv_close = (ImageView) mDialogClauseView
                .findViewById(ResourceUtil.getId(mActivity, KR.id.ch_login_clause_dialog_iv_close));
        ch_login_clause_dialog_iv_close.setOnClickListener(new View.OnClickListener() {

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
     * getMParames(生成窗口参数) (这里描述这个方法适用条件 – 可选)
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

    /**
     * clickActionedEnableCompons(点击动作完成，恢复控件功能) (这里描述这个方法适用条件 – 可选) void
     *
     * @exception
     * @since 1.0.0 xiaoming.yuan
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
        if (ch_login_back != null) {
            ch_login_back.setEnabled(true);
        }
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

    /**
     * @return String 返回类型
     * @Title: password(获取用户密码)
     * @data 2013-8-14 下午12:33:26
     */
    protected String getPassword() {
        UserModel userModel = CHPayManager.getInstance().getUserData();
        if (userModel == null) {
            userModel = AppUtil.getUserData();
        }
        if (null != userModel && null != userModel.getPassword()) {
            return userModel.getPassword();
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

    private void onBackPressed() {
        if (mRegisterCallBack != null) {
            mRegisterCallBack.backKey("登陆界面返回键退出");
            LogHelper.i(TAG, "注册界面返回键退出");
        }
    }

    @Override
    public void recoveryState() {
        processLogic();
    }
}
