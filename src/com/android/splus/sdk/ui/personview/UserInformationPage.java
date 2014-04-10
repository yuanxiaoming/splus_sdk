package com.android.splus.sdk.ui.personview;

import com.android.splus.sdk.data.UserInfoData;
import com.android.splus.sdk.model.UserRequestInfoModel;
import com.android.splus.sdk.parse.UserInfoParser;
import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.IdcardUtil;
import com.android.splus.sdk.utils.date.DateUtil;
import com.android.splus.sdk.utils.http.BaseParser;
import com.android.splus.sdk.utils.http.NetHttpUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil.DataCallback;
import com.android.splus.sdk.utils.http.RequestModel;
import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.md5.MD5Util;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.utils.toast.ToastUtil;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;

/**
 * 个人资料页面
 * @author yuanxiaoming
 *
 */
public class UserInformationPage extends ScrollView implements RadioGroup.OnCheckedChangeListener, OnFocusChangeListener, View.OnClickListener{

    private static final String TAG = "ActiveParser";
    private TextView mTvWelcome, mTvRealName, mTvSex, mTvIdCard, mTvQQ;
    private EditText mEtRealName, mEtIdCard, mEtQQ;
    private RadioGroup mRgSex;
    private RadioButton mRbSecret, mRbBoy, mRbGirl;
    private LinearLayout mLlayout_realname, mLlayout_idcard, mLlayout_qq;

    private Button mBtnComplete;
    private TextView mTvTips;

    private Activity mActivity;
    private String mPassport;
    private Integer mUid;
    private String mServerName;
    private String mDeviceno;
    private String mPartner;
    private String mReferer;
    private Integer mGameid;
    private String mAppkey;

    /**
     * 0为保密，1为男，2为女
     */
    private int mGenderType = 0;

    /**
     * 是否为横屏
     */
    private boolean mLandscape = true;

    /**
     * 字体颜色
     */
    private static final String COLORFE5F2E = "#fe5f2e";

    public UserInformationPage(Activity activity, String passport, Integer uid, String serverName,
            String deviceno, String partner, String referer, Integer gameid, String appkey,int orientation ) {
        super(activity);
        this.mActivity = activity;
        this.mPassport = passport;
        this.mUid = uid;
        this.mServerName = serverName;
        this.mDeviceno = deviceno;
        this.mPartner = partner;
        this.mReferer = referer;
        this.mGameid = gameid;
        this.mAppkey = appkey;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            mLandscape = true;

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            mLandscape = false;
        }
        init();

    }

    private void init(){
        inflate(mActivity, ResourceUtil.getLayoutId(mActivity, KR.layout.splus_person_userinformation), this);
        findViews();
        initViews();

    }

    private void findViews(){
        mTvWelcome = (TextView) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_person_center_welcome_text));
        setUserName();
        mTvRealName = (TextView) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_person_center_userinformation_realname_tv));
        mEtRealName = (EditText) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_person_center_userinformation_realname_et));

        mTvSex = (TextView) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_person_center_userinformation_sex_tv));
        mRgSex = (RadioGroup) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_person_center_userinformation_sex_rg));
        mRbSecret = (RadioButton) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_person_center_userinformation_sex_secret_rb));
        mRbBoy = (RadioButton) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_person_center_userinformation_sex_boy_rb));
        mRbGirl = (RadioButton) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_person_center_userinformation_sex_girl_rb));

        mTvIdCard = (TextView) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_person_center_userinformation_idcard_tv));
        mEtIdCard = (EditText) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_person_center_userinformation_idcard_et));

        mTvQQ = (TextView) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_person_center_userinformation_qq_tv));
        mEtQQ = (EditText) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_person_center_userinformation_qq_et));

        mBtnComplete = (Button) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_person_center_userinformation_complete_btn));
        mBtnComplete.setTextColor(Color.WHITE);
        mTvTips = (TextView) findViewById(ResourceUtil.getId(
                mActivity, KR.id.splus_person_center_userinformation_tips_tv));

        mLlayout_realname = (LinearLayout) mEtRealName.getParent();
        mLlayout_idcard = (LinearLayout) mEtIdCard.getParent();
        mLlayout_qq = (LinearLayout) mEtQQ.getParent();
    }

    private void initViews(){
        mTvRealName.setText(KR.string.splus_person_center_userinformation_realname_text);
        mTvSex.setText(KR.string.splus_person_center_userinformation_sex_text);
        mRbSecret.setText(KR.string.splus_person_center_userinformation_sex_rb_secret_text);
        mRbSecret.setChecked(true);
        mRbBoy.setText(KR.string.splus_person_center_userinformation_sex_rb_boy_text);
        mRbGirl.setText(KR.string.splus_person_center_userinformation_sex_rb_girl_text);
        mTvIdCard.setText(KR.string.splus_person_center_userinformation_idcard_text);
        mTvQQ.setText(KR.string.splus_person_center_userinformation_qq_text);
        mBtnComplete.setText(KR.string.splus_person_center_userinformation_edit_text);
        mTvTips.setText(KR.string.splus_person_center_userinformation_tips_text);
        getUserInfoFromServer();

        mBtnComplete.setOnClickListener(this);
        mLlayout_realname.setOnFocusChangeListener(this);
        mLlayout_idcard.setOnFocusChangeListener(this);
        mLlayout_qq.setOnFocusChangeListener(this);
        mRgSex.setOnCheckedChangeListener(this);
        setEnabledCompons(false);
    }

    private void setUserName() {
        String replace;
        if (mLandscape) {
            replace = KR.string.splus_person_center_userinformation_welcone_text_user.replace("%s", mPassport);
        } else {
            replace = KR.string.splus_person_center_userinformation_welcone_text_user_portrait.replace("%s", mPassport);
        }
        mTvWelcome.setText(replace);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(mTvWelcome.getText());
        spannableStringBuilder.setSpan
        (new ForegroundColorSpan(Color.parseColor(COLORFE5F2E)), replace.indexOf(mPassport),
                replace.indexOf(mPassport) + mPassport.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mTvWelcome.setText(spannableStringBuilder);
    }


    /**
     * @Title: setEnabledCompons(点击动作完成，恢复控件功能)
     * @author xiaoming.yuan
     * @data 2014-3-3 下午3:51:19 void 返回类型
     */
    private void setEnabledCompons(boolean flag) {
        if (mEtRealName != null) {
            mEtRealName.setEnabled(flag);
            mEtRealName.setClickable(flag);
            mEtRealName.setFocusable(flag);
            mEtRealName.setFocusableInTouchMode(flag);
        }
        if (mEtIdCard != null) {
            mEtIdCard.setEnabled(flag);
            mEtIdCard.setClickable(flag);
            mEtIdCard.setFocusable(flag);
            mEtIdCard.setFocusableInTouchMode(flag);
        }
        if (mEtQQ != null) {
            mEtQQ.setEnabled(flag);
            mEtQQ.setClickable(flag);
            mEtQQ.setFocusable(flag);
            mEtQQ.setFocusableInTouchMode(flag);
        }
        if (mRgSex != null) {
            mRgSex.setEnabled(flag);
            mRgSex.setClickable(flag);
            mRgSex.setFocusable(flag);
            mRgSex.setFocusableInTouchMode(flag);
        }
        if (mRbSecret != null) {
            mRbSecret.setEnabled(flag);
            mRbSecret.setClickable(flag);
            mRbSecret.setFocusable(flag);
            mRbSecret.setFocusableInTouchMode(flag);
        }
        if (mRbBoy != null) {
            mRbBoy.setEnabled(flag);
            mRbBoy.setClickable(flag);
            mRbBoy.setFocusable(flag);
            mRbBoy.setFocusableInTouchMode(flag);
        }
        if (mRbGirl != null) {
            mRbGirl.setEnabled(flag);
            mRbGirl.setClickable(flag);
            mRbGirl.setFocusable(flag);
            mRbGirl.setFocusableInTouchMode(flag);
        }
    }

    /**
     * 获取用户填写的个人资料
     */
    private void getUserInfoFromServer() {
        Long time = DateUtil.getUnixTime();
        String keyString = mGameid + mDeviceno + mReferer + mPartner + mUid + mPassport + time+mAppkey;
        UserRequestInfoModel mCheckUserRequestInfoData = new UserRequestInfoModel(mUid, mServerName,
                mGameid, MD5Util.getMd5toLowerCase(keyString), time, mDeviceno, mPartner, mReferer,mPassport);

   //     LogHelper.i("UserInformationPage", "url---"+ NetHttpUtil.hashMapTOgetParams(mCheckUserRequestInfoData, Constant.USERINFO_URL));

        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.USERINFO_URL, mActivity,
                mCheckUserRequestInfoData, new UserInfoParser()), userInfoCallBack);
    }

    private DataCallback<HashMap<String, Object>> userInfoCallBack = new DataCallback<HashMap<String, Object>>() {

        @Override
        public void callbackSuccess(HashMap<String, Object> paramObject) {
            UserInfoData  userInfoData =(UserInfoData) paramObject.get(BaseParser.DATA);
            if(userInfoData!=null){
                mEtRealName.setText(userInfoData.getRealname());
                if (TextUtils.isEmpty(userInfoData.getGendertype())) {
                    mRbSecret.setChecked(true);
                } else if (userInfoData.getGendertype().equals("0")) {
                    mRbSecret.setChecked(true);
                } else if (userInfoData.getGendertype().equals("1")) {
                    mRbBoy.setChecked(true);
                } else if (userInfoData.getGendertype().equals("2")) {
                    mRbGirl.setChecked(true);
                }
                mEtIdCard.setText(userInfoData.getIdcard());
                mEtQQ.setText(userInfoData.getQq());

            }else{
                String msg = paramObject.get(BaseParser.MSG).toString();
                LogHelper.i(TAG, msg);
                ToastUtil.showToast(mActivity, msg);
            }
            //做一些成功后的操作
            mBtnComplete.setText(KR.string.splus_person_center_userinformation_edit_text);
            setEnabledCompons(false);

        }

        @Override
        public void callbackError(String error) {
            //请求数据失败
            ToastUtil.showToast(mActivity, error);
        }

    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.equals(mRgSex)) {
            if (checkedId == mRbSecret.getId()) {
                mGenderType = 0;
            } else if (checkedId == mRbBoy.getId()) {
                mGenderType = 1;
            } else if (checkedId == mRbGirl.getId()) {
                mGenderType = 2;
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.equals(mLlayout_realname)) {
            CommonUtil.showInput(mActivity, mEtRealName);
            return;
        }

        if (v.equals(mLlayout_idcard)) {
            CommonUtil.showInput(mActivity, mEtIdCard);
        }

        if (v.equals(mLlayout_qq)) {
            CommonUtil.showInput(mActivity, mEtQQ);
            return;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBtnComplete)) {
            if (mBtnComplete.getText().toString().equals(KR.string.splus_person_center_userinformation_complete_text)) {
                String text = mEtIdCard.getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    String msg = IdcardUtil.IDCardValidate(text);
                    if (!TextUtils.isEmpty(msg)) {
                        mEtIdCard.requestFocus();
                        mEtIdCard.setError("该身份证号码不正确，请重新输入");
                        return;
                    }
                }
                sendUserInfoToServer(mEtRealName.getText().toString().trim(), mGenderType,
                        mEtIdCard.getText().toString().trim(), mEtQQ.getText().toString().trim());
            } else {
                mBtnComplete.setText(KR.string.splus_person_center_userinformation_complete_text);
                setEnabledCompons(true);
            }
        }
    }

    /**
     * 发送个人资料到服务器
     */
    private void sendUserInfoToServer(String realName, int genderType, String idCard, String qq){
        Long time = DateUtil.getUnixTime();
        String keyString = mGameid  + mDeviceno + mReferer + mPartner + mUid + mPassport + time+mAppkey;
        UserRequestInfoModel mSendUserRequestInfoData = new UserRequestInfoModel(mUid, mServerName,
                mGameid, MD5Util.getMd5toLowerCase(keyString), time, mDeviceno, mPartner,
                mReferer, mPassport, realName, genderType, idCard, qq);

    //    LogHelper.i("UserInformationPage", "url---"+ NetHttpUtil.hashMapTOgetParams(mSendUserRequestInfoData, Constant.USERINFO_URL));
        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.USERINFO_URL, mActivity,
                mSendUserRequestInfoData, new UserInfoParser()), sendUserInfoCallBack);
    }

    private DataCallback<HashMap<String, Object>> sendUserInfoCallBack = new DataCallback<HashMap<String, Object>>() {

        @Override
        public void callbackSuccess(HashMap<String, Object> paramObject) {
            UserInfoData  userInfoData =(UserInfoData) paramObject.get(BaseParser.DATA);
            if(userInfoData!=null){
                mEtRealName.setText(userInfoData.getRealname());
                if (userInfoData.getGendertype().equals("")) {
                    mRbSecret.setChecked(true);
                } else if (userInfoData.getGendertype().equals("0")) {
                    mRbSecret.setChecked(true);
                } else if (userInfoData.getGendertype().equals("1")) {
                    mRbBoy.setChecked(true);
                } else if (userInfoData.getGendertype().equals("2")) {
                    mRbGirl.setChecked(true);
                }
                mEtIdCard.setText(userInfoData.getIdcard());
                mEtQQ.setText(userInfoData.getQq());

                ToastUtil.showToast(mActivity, "更新资料成功");
            }else{
                String msg = paramObject.get(BaseParser.MSG).toString();
                LogHelper.i(TAG, msg);
                ToastUtil.showToast(mActivity, msg);
            }
            //做一些成功后的操作
            mBtnComplete.setText(KR.string.splus_person_center_userinformation_edit_text);
            setEnabledCompons(false);
        }

        @Override
        public void callbackError(String error) {
            //发送数据失败
            mBtnComplete.setText(KR.string.splus_person_center_userinformation_edit_text);
            setEnabledCompons(false);
            ToastUtil.showToast(mActivity, "更新资料失败");
        }

    };

}
