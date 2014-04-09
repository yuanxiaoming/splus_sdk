/**
 * @Title: Password.java
 * @Package: com.sanqi.android.sdk.person
 * Copyright: Copyright (c) 2013
 * Company: 上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013年9月29日 下午3:47:34
 * @version 1.0
 */

package com.android.splus.sdk.ui.personview;

import com.android.splus.sdk.model.PasswordModel;
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
import com.android.splus.sdk.utils.phone.Phoneuitl;
import com.android.splus.sdk.utils.progressDialog.ProgressDialogUtil;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.utils.toast.ToastUtil;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 修改密码页面
 *
 * @ClassName:Password
 * @author xiaoming.yuan
 * @date 2013年9月29日 下午3:47:34
 */
public class PasswordPage extends ScrollView implements View.OnFocusChangeListener {

    public static String TAG = "PasswordPage";

    private Activity mActivity;

    private TextView tv_oldpwd, tv_newpwd, tv_newpwd_repeat;

    private EditText et_oldpwd, et_newpwd, et_newpwd_repeat;

    private LinearLayout llayout_oldpwd, llayout_newpwd, llayout_newpwd_repeat;

    private ProgressDialog mDialog;

    private String mPassport;

    private Integer mUid;

    private String mServerName;

    private String mDeviceno;

    private String mPartner;

    private String mReferer;

    private Integer mGameid;

    private String mAppkey;

    private Handler mHandler;

    private Button mBtnSubmit;

    /**
     * 是否为横屏
     */
    private boolean mLandscape = true;

    public PasswordPage(Activity activity, String passport, Integer uid, String serverName,
            String deviceno, String partner, String referer, Integer gameid,String appkey, Handler handler,int orientation ) {
        super(activity);
        this.mActivity = activity;
        this.mPassport = passport;
        this.mUid = uid;
        this.mServerName = serverName;
        this.mDeviceno = deviceno;
        this.mPartner = partner;
        this.mGameid = gameid;
        this.mReferer = referer;
        this.mAppkey = appkey;
        this.mHandler = handler;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            mLandscape = true;

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            mLandscape = false;
        }

        inflate(activity, ResourceUtil.getLayoutId(activity, KR.layout.splus_person_pwd), this);
        findViews();
        initViews();
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午3:50:58
     */
    private void findViews() {
        tv_oldpwd = (TextView) findViewById(ResourceUtil
                .getId(mActivity, KR.id.splus_person_pwd_old_tv));
        tv_newpwd = (TextView) findViewById(ResourceUtil
                .getId(mActivity, KR.id.splus_person_pwd_new_tv));
        tv_newpwd_repeat = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_person_pwd_new_repeat_tv));

        et_oldpwd = (EditText) findViewById(ResourceUtil
                .getId(mActivity, KR.id.splus_person_pwd_old_et));
        et_newpwd = (EditText) findViewById(ResourceUtil
                .getId(mActivity, KR.id.splus_person_pwd_new_et));
        et_newpwd_repeat = (EditText) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_person_pwd_new_repeat_et));

        llayout_oldpwd = (LinearLayout) et_oldpwd.getParent();
        llayout_newpwd = (LinearLayout) et_newpwd.getParent();
        llayout_newpwd_repeat = (LinearLayout) et_newpwd_repeat.getParent();

        mBtnSubmit = (Button) findViewById(ResourceUtil
                .getId(mActivity, KR.id.splus_person_pwd_submit_btn));
        mBtnSubmit.setTextColor(Color.WHITE);

    }

    /**
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午3:51:20
     */
    private void initViews() {
        tv_oldpwd.setText(KR.string.splus_person_pwd_old_tv);
        tv_newpwd.setText(KR.string.splus_person_pwd_new_tv);
        tv_newpwd_repeat.setText(KR.string.splus_person_pwd_new_repeat_tv);

        et_oldpwd.setHint(KR.string.splus_person_pwd_old_et);
        et_newpwd_repeat.setHint(KR.string.splus_person_pwd_new_repeat_et);

        if (mLandscape) {
            et_newpwd.setHint(KR.string.splus_person_pwd_new_et);
        } else {
            et_newpwd.setHint(KR.string.splus_person_pwd_new_et_portrait);
        }

        llayout_oldpwd.setOnFocusChangeListener(this);
        llayout_newpwd.setOnFocusChangeListener(this);
        llayout_newpwd_repeat.setOnFocusChangeListener(this);

        et_newpwd_repeat.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isNewPwdSame()) {
                    et_newpwd_repeat.setError(Html.fromHtml("<font color=#000000>两次密码输入不一致，请重新输入</font>"));
                }
            }
        });

        mBtnSubmit.setText(KR.string.splus_person_pwd_submit_btn_text);
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年10月10日 下午4:02:33
     * @param mPassport
     * @param mUid
     * @param mServerName
     * @param mDeviceno
     * @param mPartner
     * @param pext
     * @param mReferer
     * @param aoshi_gameid
     * @param mAppkey
     */
    public void submit() {
        if (getOldPwd().length() < 6) {
            et_oldpwd.setError(Html.fromHtml("<font color=#000000>旧密码不能少于6位</font>"));
            et_oldpwd.requestFocus();
            return;
        }

        if (getNewPwd().length() < 6) {
            et_newpwd.setError(Html.fromHtml("<font color=#000000>新密码不能少于6位</font>"));
            et_newpwd.requestFocus();
            return;
        }
        if (!isNewPwdSame()) {
            et_newpwd_repeat.setError(Html.fromHtml("<font color=#000000>两次密码输入不一致，请重新输入</font>"));
            et_newpwd_repeat.requestFocus();
            return;
        }
        long time = DateUtil.getUnixTime();
        String keyString = mGameid + mServerName + mDeviceno + mReferer + mPartner + mUid + mPassport + time
                + getOldPwd() + getNewPwd()+mAppkey;
        PasswordModel passwordModel = new PasswordModel(mUid, mServerName, mGameid,
                MD5Util.getMd5toLowerCase(keyString), time, mDeviceno, mPartner,  mReferer,
                mPassport, getOldPwd(), getNewPwd());

        mDialog = ProgressDialogUtil.showProgress(mActivity, "加载中...", null, false, false);

        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.PASSWORD_URL, mActivity,
                passwordModel, new LoginParser()), mPwdDataCallback);

    }

    private DataCallback<JSONObject> mPwdDataCallback = new DataCallback<JSONObject>() {

        @Override
        public void callbackSuccess(JSONObject paramObject) {
            hideDialog();
            LogHelper.d(TAG, paramObject.toString());
            String msg = "";
            /**
             * code：-3 用户不存在或旧密码不正确 -2 参数不全 -1 加密校验错误 0 系统繁忙 1 成功
             */
            int code = paramObject.optInt("code");
            switch (code) {
                case 1:
                    msg = "密码修改成功！";
                    if (mHandler != null) {
                        Message message = new Message();
                        message.obj = getNewPwd();
                        message.what = PersonActivity.PASSWORDPAGE;
                        mHandler.sendMessage(message);
                    }
                    break;
                default:
                    msg = "密码修改失败！";
                    break;
            }
            ToastUtil.showToast(mActivity, msg);
        }

        @Override
        public void callbackError(String error) {
            LogHelper.d(TAG, error);
            hideDialog();
            ToastUtil.showToast(mActivity, "密码修改失败！");
        }
    };

    /**
     * @author xiaoming.yuan
     * @date 2013年10月11日 下午12:27:54
     */
    private void hideDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午4:22:14
     * @return
     */
    private String getOldPwd() {
        return this.et_oldpwd.getText().toString();
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午4:22:59
     * @return
     */
    private String getNewPwd() {
        return this.et_newpwd.getText().toString();
    }

    /**
     * 两次新密码时候相同
     *
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午4:24:32
     * @return
     */
    private boolean isNewPwdSame() {
        String pwd = et_newpwd.getText().toString().trim();
        String re_pwd = et_newpwd_repeat.getText().toString().trim();
        return pwd.equals(re_pwd);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            return;
        }

        if (v.equals(llayout_newpwd)) {
            CommonUtil.showInput(mActivity, et_newpwd);
            return;
        }
        if (v.equals(llayout_oldpwd)) {
            CommonUtil.showInput(mActivity, et_oldpwd);
            return;
        }
        if (v.equals(llayout_newpwd_repeat)) {
            CommonUtil.showInput(mActivity, et_newpwd_repeat);
            return;
        }
    }

}
