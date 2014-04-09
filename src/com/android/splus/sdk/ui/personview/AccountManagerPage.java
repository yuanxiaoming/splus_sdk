
package com.android.splus.sdk.ui.personview;

import com.android.splus.sdk.model.CheckPhoneModel;
import com.android.splus.sdk.parse.LoginParser;
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

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class AccountManagerPage extends ScrollView {

    private static final String TAG = "AccountManager";

    private Integer mGameid;

    private String mPartner;

    private String mReferer;

    private String mDeviceno;

    private Activity mActivity;

    private String mServerName;

    private Integer mUid;

    private String mPassport;

    private Body mBody;

    private boolean mLandscape;

    private ProgressDialog mDialog;

    private String mAppkey;

    private boolean mHasBindPhone = false;

    public AccountManagerPage(Activity activity, String passport, Integer uid, String serverName,
            String deviceno, String partner, String referer, Integer gameid, String appkey,int orientation) {
        super(activity);
        this.mActivity = activity;
        this.mDeviceno = deviceno;
        this.mGameid = gameid;
        this.mPartner = partner;
        this.mReferer = referer;
        this.mUid = uid;
        this.mPassport = passport;
        this.mServerName = serverName;
        this.mAppkey = appkey;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            mLandscape = true;

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            mLandscape = false;
        }

        mBody = new Body(activity, passport, mLandscape);
        mBody.setOrientation(LinearLayout.VERTICAL);
        addView(mBody);
        getBindStatusFromServer();
    }

    /**
     * 获取用户绑定状态
     *
     * @author xiaoming.yuan
     * @date 2013年10月11日 下午2:25:54
     */
    private void getBindStatusFromServer() {
        mDialog = ProgressDialogUtil.showProgress(mActivity, "加载中...", null, false, false);
        long time = DateUtil.getUnixTime();
        String keyString = mGameid + mServerName + mDeviceno + mReferer + mPartner + mUid
                + mPassport + time + mAppkey;
        CheckPhoneModel checkPhoneModel = new CheckPhoneModel(mGameid, mDeviceno, mPartner,
                mReferer, mUid, mServerName, mPassport, time, MD5Util.getMd5toLowerCase(keyString));
        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.BINDMOBILE_URL,
                mActivity, checkPhoneModel, new LoginParser()), mCheckCallBack);
    }

    /**
     * 检查用户是否绑定了手机号码回调
     */
    private DataCallback<JSONObject> mCheckCallBack = new DataCallback<JSONObject>() {

        @Override
        public void callbackSuccess(JSONObject paramObject) {
            hideDialog();
            LogHelper.d(TAG, paramObject.toString());
            /**
             * code：-3 已绑定 1 可绑定
             */
            int code = paramObject.optInt("code");
            switch (code) {
                case 1:
                    // 可绑定手机
                    mHasBindPhone = false;
                    break;
                case -3:
                    // 已绑定手机
                    mHasBindPhone = true;
                    break;
                default:
                    mHasBindPhone = false;
                    break;
            }
        }

        @Override
        public void callbackError(String error) {
            LogHelper.d(TAG, error);
            hideDialog();
            mHasBindPhone = false;
        }
    };

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
     * @author xiaoming.yuan
     * @date 2013年10月15日 上午9:56:45
     * @return
     */
    public boolean isBinded() {
        return this.mHasBindPhone;
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年10月15日 上午9:56:49
     * @param bindStatus
     */
    public void setBindStatus(boolean bindStatus) {
        this.mHasBindPhone = bindStatus;
    }

    /**
     * @ClassName:AccountClickListener
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午4:27:06
     */
    public interface AccountClickListener {
        public void onUserInformationClick(View v);

        public void onPasswordClick(View v);

        public void onPhoneClick(View v);
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午4:28:24
     * @param listener
     */
    public void setOnAccountClickListener(AccountClickListener listener) {
        mBody.setOnAccountClickListener(listener);

    }

    /**
     * @ClassName:Body
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午4:26:42
     */
    private class Body extends LinearLayout {

        private View mWelcomeView, btn_userinformation, btn_pwd, btn_phone;

        private TextView tv_welcom, mTvUserinformationText, mTvPwd, mTvPhone;

        /**
         * 分隔线
         */
        private ImageView mLine1, mLine2;

        private static final String COLORFE5F2E = "#fe5f2e";

        private AccountClickListener mListener;

        public Body(Activity activity, String passport, Boolean isLandscape) {
            super(activity);
            mWelcomeView = inflate(activity,
                    ResourceUtil.getLayoutId(activity, KR.layout.splus_person_center_welcome), null);
            btn_userinformation = inflate(activity,
                    ResourceUtil.getLayoutId(activity, KR.layout.splus_person_center_top_item),
                    null);
            btn_pwd = inflate(activity,
                    ResourceUtil.getLayoutId(activity, KR.layout.splus_person_center_item), null);

            btn_phone = inflate(activity,
                    ResourceUtil.getLayoutId(activity, KR.layout.splus_person_center_bottom_item),
                    null);
            LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(50, 0, 50, 0);

            LayoutParams params1 = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
            params1.setMargins(50, 0, 50, 0);

            LayoutParams params2 = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params2.setMargins(50, 0, 50, 50);
            LayoutParams mWelcomeViewParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mWelcomeViewParams.setMargins(0, 10, 0, 10);

            addView(mWelcomeView,mWelcomeViewParams);

            mLine1 = new ImageView(activity);
            mLine1.setBackgroundColor(0xfff7f7f7);
            mLine1.setScaleType(ScaleType.FIT_XY);
            mLine1.setImageResource(ResourceUtil.getDrawableId(activity,
                    KR.drawable.splus_login_bg_devider));
            mLine2 = new ImageView(activity);
            mLine2.setBackgroundColor(0xfff7f7f7);
            mLine2.setScaleType(ScaleType.FIT_XY);
            mLine2.setImageResource(ResourceUtil.getDrawableId(activity,
                    KR.drawable.splus_login_bg_devider));
            int padding = 0;
            if (isLandscape) {
                padding = 20;
            } else {
                padding = 0;
            }
            mLine1.setPadding(padding, 0, padding, 0);
            mLine2.setPadding(padding, 0, padding, 0);
            addView(btn_userinformation, params);
            addView(mLine1, params1);

            addView(btn_pwd, params);
            addView(mLine2, params1);

            addView(btn_phone, params2);

            intViews(activity, passport, isLandscape);
            setListener();

        }

        private void intViews(Activity activity, String passport, Boolean isLandscape) {

            tv_welcom = (TextView) mWelcomeView.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_welcome_text));

            setUserName(isLandscape, passport);

            ((ImageView) (btn_userinformation.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_top_ic_left))))
                    .setImageResource(ResourceUtil.getDrawableId(activity,
                            KR.drawable.splus_person_center_account_icon_selector));
            ((ImageView) (btn_userinformation.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_top_ic_right)))).setImageResource(ResourceUtil
                            .getDrawableId(activity, KR.drawable.splus_person_center_arrow_icon_selector));
            mTvUserinformationText =  (TextView) btn_userinformation.findViewById(
                    ResourceUtil.getId(activity, KR.id.splus_person_center_item_top_tv));
            mTvUserinformationText.setText(KR.string.splus_person_account_user_information);
            mTvUserinformationText.setTextColor(createColorStateList(0xff747474, 0xffffffff,
                    0xffffffff, 0xffffffff));

            ((ImageView) (btn_pwd.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_ic_left)))).setImageResource(ResourceUtil
                            .getDrawableId(activity, KR.drawable.splus_person_account_modify_pwd_selector));
            ((ImageView) (btn_pwd.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_ic_right)))).setImageResource(ResourceUtil
                            .getDrawableId(activity, KR.drawable.splus_person_center_arrow_icon_selector));
            mTvPwd = (TextView) btn_pwd.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_tv));
            mTvPwd.setText(KR.string.splus_person_account_modify_pwd);
            mTvPwd.setTextColor(createColorStateList(0xff747474, 0xffffffff, 0xffffffff, 0xffffffff));

            ((ImageView) (btn_phone.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_bottom_item_ic_left)))).setImageResource(ResourceUtil
                            .getDrawableId(activity,
                                    KR.drawable.splus_person_account_binding_phone_selector));
            ((ImageView) (btn_phone.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_bottom_item_ic_right))))
                    .setImageResource(ResourceUtil.getDrawableId(activity,
                            KR.drawable.splus_person_center_arrow_icon_selector));
            mTvPhone = (TextView) btn_phone.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_bottom_item_tv));
            mTvPhone.setText(KR.string.splus_person_account_binding_phone);
            mTvPhone.setTextColor(createColorStateList(0xff747474, 0xffffffff, 0xffffffff,
                    0xffffffff));

        }

        private void setListener() {

            btn_userinformation.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mListener.onUserInformationClick(v);
                }
            });
            btn_pwd.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mHasBindPhone) {
                        mListener.onPasswordClick(v);
                    } else {
                        ToastUtil.showToast(mActivity, "您还没有绑定手机号码，暂时不能修改密码，请先绑定手机号码后再修改密码！");
                        mListener.onPasswordClick(v);
                    }

                }
            });

            btn_phone.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mListener.onPhoneClick(v);
                }
            });

        }

        /**
         * 对TextView设置不同状态时其文字颜色
         */
        private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
            int[] colors = new int[] {
                    pressed, focused, normal, focused, unable, normal
            };
            int[][] states = new int[6][];
            states[0] = new int[] {
                    android.R.attr.state_pressed, android.R.attr.state_enabled
            };
            states[1] = new int[] {
                    android.R.attr.state_enabled, android.R.attr.state_focused
            };
            states[2] = new int[] {
                    android.R.attr.state_enabled
            };
            states[3] = new int[] {
                    android.R.attr.state_focused
            };
            states[4] = new int[] {
                    android.R.attr.state_window_focused
            };
            states[5] = new int[] {};
            ColorStateList colorList = new ColorStateList(states, colors);
            return colorList;
        }

        /**
         * @author xiaoming.yuan
         * @date 2013年9月29日 下午4:26:47
         */
        private void setUserName(Boolean isLandscape, String passport) {
            String replace;
            if (isLandscape) {
                replace = KR.string.splus_person_account_welcome_text.replace("%s", passport);
            } else {
                replace = KR.string.splus_person_account_welcome_text_portrait.replace("%s",
                        passport);
            }
            tv_welcom.setText(replace);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(
                    tv_welcom.getText());
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor(COLORFE5F2E)),
                    replace.indexOf(passport), replace.indexOf(passport) + passport.length(),
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            tv_welcom.setText(spannableStringBuilder);
        }

        /**
         * @author xiaoming.yuan
         * @date 2013年9月29日 下午4:28:24
         * @param listener
         */
        public void setOnAccountClickListener(AccountClickListener listener) {
            this.mListener = listener;

        }

    }

}
