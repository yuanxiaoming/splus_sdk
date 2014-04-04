package com.android.splus.sdk.ui.personview;

import com.android.splus.sdk.data.UserInfoData;
import com.android.splus.sdk.model.UserRequestInfoModel;
import com.android.splus.sdk.parse.UserInfoParser;
import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.IdcardUtil;
import com.android.splus.sdk.utils.date.DateUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil.DataCallback;
import com.android.splus.sdk.utils.http.RequestModel;
import com.android.splus.sdk.utils.md5.MD5Util;
import com.android.splus.sdk.utils.phone.Phoneuitl;
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

/**
 * 个人资料页面
 * @author yuanxiaoming
 *
 */
public class UserInformationPage extends ScrollView implements RadioGroup.OnCheckedChangeListener, OnFocusChangeListener, View.OnClickListener{

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
            String deviceno, String partner, String referer, Integer gameid, String appkey ) {
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
        int orientation = Phoneuitl.getOrientation(mActivity);
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            mLandscape = true;

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            mLandscape = false;
        }
        init();
        getUserInfoFromServer();
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
        mRgSex.setOnCheckedChangeListener(this);
        mRbSecret.setText(KR.string.splus_person_center_userinformation_sex_rb_secret_text);
        mRbSecret.setChecked(true);
        mRbBoy.setText(KR.string.splus_person_center_userinformation_sex_rb_boy_text);
        mRbGirl.setText(KR.string.splus_person_center_userinformation_sex_rb_girl_text);

        mTvIdCard.setText(KR.string.splus_person_center_userinformation_idcard_text);

        mTvQQ.setText(KR.string.splus_person_center_userinformation_qq_text);

        mBtnComplete.setText(KR.string.splus_person_center_userinformation_complete_text);
        mBtnComplete.setOnClickListener(this);

        mTvTips.setText(KR.string.splus_person_center_userinformation_tips_text);

        mLlayout_realname.setOnFocusChangeListener(this);
        mLlayout_idcard.setOnFocusChangeListener(this);
        mLlayout_qq.setOnFocusChangeListener(this);
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
     * 获取用户填写的个人资料
     */
    private void getUserInfoFromServer() {
        Long time = DateUtil.getUnixTime();
        String keyString = mGameid + mServerName + mDeviceno + mReferer + mPartner + mUid + mPassport + time+mAppkey;
        UserRequestInfoModel mCheckUserRequestInfoData = new UserRequestInfoModel(mUid, mServerName,
                mGameid, MD5Util.getMd5toLowerCase(keyString), time, mDeviceno, mPartner, mReferer,mPassport);
        String hashMapToGetPrrams = NetHttpUtil.hashMapTOgetParams(mCheckUserRequestInfoData, Constant.USERINFO_URL);
        System.out.println("hashMapToGetPrrams-->" + hashMapToGetPrrams);

        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.USERINFO_URL, mActivity,
                mCheckUserRequestInfoData, new UserInfoParser()), userInfoCallBack);
    }

    private DataCallback<UserInfoData> userInfoCallBack = new DataCallback<UserInfoData>() {

        @Override
        public void callbackSuccess(UserInfoData userInfoData) {
            mEtRealName.setText(userInfoData.getRealname());
            if (userInfoData.getGendertype().equals("")) {

            } else if (userInfoData.getGendertype().equals("0")) {
                mRbSecret.setChecked(true);
            } else if (userInfoData.getGendertype().equals("1")) {
                mRbBoy.setChecked(true);
            } else if (mLandscape) {
                mRbGirl.setChecked(true);
            }
            mEtIdCard.setText(userInfoData.getIdcard());
            mEtQQ.setText(userInfoData.getQq());
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
            String text = mEtIdCard.getText().toString().trim();
            if (TextUtils.isEmpty(text)) {

            } else {
                String msg = IdcardUtil.IDCardValidate(text);
                if (TextUtils.isEmpty(msg)) {

                } else {
                    mEtIdCard.requestFocus();
                    mEtIdCard.setError("该身份证号码不正确，请重新输入");
                    return;
                }
            }

            ToastUtil.showToast(mActivity, "提交的数据为："
                    + "\n真实姓名：" + mEtRealName.getText().toString().trim()
                    + "\n性别：" + mGenderType
                    + "\n身份证号：" + mEtIdCard.getText().toString().trim()
                    + "\nQQ：" + mEtQQ.getText().toString().trim());

            sendUserInfoToServer(mEtRealName.getText().toString().trim(), mGenderType,
                    mEtIdCard.getText().toString().trim(), mEtQQ.getText().toString().trim());
        }
    }

    /**
     * 发送个人资料到服务器
     */
    private void sendUserInfoToServer(String realName, int genderType, String idCard, String qq){
        Long time = DateUtil.getUnixTime();


        String keyString = mGameid + mServerName + mDeviceno + mReferer + mPartner + mUid + mPassport + time+mAppkey;
        UserRequestInfoModel mSendUserRequestInfoData = new UserRequestInfoModel(mUid, mServerName,
                mGameid, MD5Util.getMd5toLowerCase(keyString), time, mDeviceno, mPartner,
                mReferer, mPassport, realName, genderType, idCard, qq);

        String hashMapToGetPrrams = NetHttpUtil.hashMapTOgetParams(mSendUserRequestInfoData, Constant.GAME_INFO_URL);
        System.out.println("hashMapToGetPrrams-->" + hashMapToGetPrrams);

        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.USERINFO_URL, mActivity,
                mSendUserRequestInfoData, new UserInfoParser()), sendUserInfoCallBack);
    }

    private DataCallback<UserInfoData> sendUserInfoCallBack = new DataCallback<UserInfoData>() {

        @Override
        public void callbackSuccess(UserInfoData userInfoData) {
            //做一些成功后的操作
        }

        @Override
        public void callbackError(String error) {
            //发送数据失败
        }

    };

}
