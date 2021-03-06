
package com.android.splus.sdk.ui;

import com.android.splus.sdk.apiinterface.LogoutCallBack;
import com.android.splus.sdk.manager.AccountObservable;
import com.android.splus.sdk.manager.ExitAppUtils;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.ui.personview.AccountManagerPage;
import com.android.splus.sdk.ui.personview.AnnouncementsPage;
import com.android.splus.sdk.ui.personview.ForumPage;
import com.android.splus.sdk.ui.personview.LogoutPage;
import com.android.splus.sdk.ui.personview.PasswordPage;
import com.android.splus.sdk.ui.personview.PersonCenterPage;
import com.android.splus.sdk.ui.personview.PhoneBindPage;
import com.android.splus.sdk.ui.personview.SQPage;
import com.android.splus.sdk.ui.personview.UserInformationPage;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.file.AppUtil;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.sharedPreferences.SharedPreferencesHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class PersonActivity extends BaseActivity {

    private static final String TAG = "PersonActivity";

    private Activity mActivity;

    private SplusPayManager mSplusPayManager;

    private LogoutCallBack mLogoutCallBack;

    public static final String INTENT_TYPE = "intent_type";

    public static final String INTENT_SQ = "intent_sq";

    public static final String INTENT_ANNOUNCEMENTS = "intent_announcements";

    public static final String INTENT_FORUM = "intent_forum";

    public static final int PASSWORDPAGE = 0;

    public static final int PHONEBIND = 1;

    public static final int LOGOUT = 2;

    /**
     * 页面切换动画
     */
    private Animation anim_in_into, anim_out_into, anim_in_back, anim_out_back;

    public int mAnim_time = 500;

    private ViewFlipper vf_person_center;

    private ImageButton ibtn_back, ibtn_submit;

    private TextView mTvTitleBarCenter;

    /**
     * 当前的页面
     */
    private String mCurrentPage = PersonCenterPage.class.getName();

    /**
     * 个人中心页面
     */
    private PersonCenterPage mPersonCenterPage;

    /**
     * 注销页面
     */
    private LogoutPage mLogoutPage;

    /**
     * 客服中心
     */
    private SQPage mSqPage;

    /**
     * 论坛
     */
    private ForumPage mForumPage;

    /**
     * 活动页面
     */
    private AnnouncementsPage mAnnouncementsPage;

    /**
     * 安全信息页面
     */
    private AccountManagerPage mAccountManagerPage;

    /**
     * 个人资料页面
     */
    private UserInformationPage mUserInformationPage;

    /**
     * 密码修改页面
     */
    private PasswordPage mPasswordPage;

    /**
     * 绑定手机页面
     */
    private PhoneBindPage mPhoneBindPage;

    @Override
    protected void loadViewLayout() {
        setContentView(KR.layout.splus_person_center_activity);
        this.mActivity = this;
        mSplusPayManager = SplusPayManager.getInstance();
        mLogoutCallBack = mSplusPayManager.getLogoutCallBack();
    }

    @Override
    protected void findViewById() {
        Intent intent = getIntent();
        String type = intent.getStringExtra(INTENT_TYPE);

        ibtn_back = (ImageButton) findViewById(KR.id.splus_person_title_bar_left_button);
        ibtn_submit = (ImageButton) findViewById(KR.id.splus_person_title_bar_right_button);
        mTvTitleBarCenter = (TextView) findViewById(KR.id.splus_person_title_bar_middle_title);
        vf_person_center = (ViewFlipper) findViewById(KR.id.splus_person_center_views);

        if (INTENT_SQ.equals(type)) {
            String passport = intent.getStringExtra(Constant.LOGIN_INTENT_USERNAME);
            Integer uid = intent.getIntExtra(Constant.LOGIN_INTENT_USERID, 0);
            String password = intent.getStringExtra(Constant.LOGIN_INTENT_USERNAME);

            if (TextUtils.isEmpty(passport)) {
                passport = getPassport();
                uid = getUid();
                password = getPassword();
            }

            // 进入客服中心页面
            if (mSqPage == null) {
                mSqPage = new SQPage(mActivity, getDeviceno(), mSplusPayManager.getGameid(), mSplusPayManager.getPartner(), mSplusPayManager.getReferer(), uid, passport, password,mSplusPayManager.getServerId(),mSplusPayManager.getRoleId(), mSplusPayManager.getRoleName(), mSplusPayManager.getServerName(),mSplusPayManager.getAppkey());
            }
            mTvTitleBarCenter.setText(KR.string.splus_person_center_sq_btn_text);
            addView(mSqPage, SQPage.class.getName());

        } else if (INTENT_ANNOUNCEMENTS.equals(type)) {
            String passport = intent.getStringExtra(Constant.LOGIN_INTENT_USERNAME);
            Integer uid = intent.getIntExtra(Constant.LOGIN_INTENT_USERID, 0);
            String password = intent.getStringExtra(Constant.LOGIN_INTENT_USERNAME);
            if (TextUtils.isEmpty(passport)) {
                passport = getPassport();
                uid = getUid();
                password = getPassword();
            }
            // 进入活动页面
            if (mAnnouncementsPage == null) {
                mAnnouncementsPage = new AnnouncementsPage(mActivity, getDeviceno(), mSplusPayManager.getGameid(), mSplusPayManager.getPartner(), mSplusPayManager.getReferer(), uid, passport, password, mSplusPayManager.getServerId(),mSplusPayManager.getRoleId(),mSplusPayManager.getRoleName(), mSplusPayManager.getServerName(),mSplusPayManager.getAppkey());
            }
            mTvTitleBarCenter.setText(KR.string.splus_person_center_announcementspage_btn_text);
            // 把活动页面显示在前台
            addView(mAnnouncementsPage, AnnouncementsPage.class.getName());

        } else if (INTENT_FORUM.equals(type)) {
            String passport = intent.getStringExtra(Constant.LOGIN_INTENT_USERNAME);
            Integer uid = intent.getIntExtra(Constant.LOGIN_INTENT_USERID, 0);
            String password = intent.getStringExtra(Constant.LOGIN_INTENT_USERNAME);

            if (TextUtils.isEmpty(passport)) {
                passport = getPassport();
                uid = getUid();
                password = getPassword();
            }
            // 进入论坛页面看
            if (mForumPage == null) {
                mForumPage = new ForumPage(mActivity, getDeviceno(), mSplusPayManager.getGameid(), mSplusPayManager.getPartner(), mSplusPayManager.getReferer(), uid, passport, password, mSplusPayManager.getServerId(),mSplusPayManager.getRoleId(),mSplusPayManager.getRoleName(), mSplusPayManager.getServerName(),mSplusPayManager.getAppkey());
            }
            mTvTitleBarCenter.setText(KR.string.splus_person_center_forum_btn_text);
            // 把论坛页面显示在前台
            addView(mForumPage, ForumPage.class.getName());

        } else {
            mPersonCenterPage = new PersonCenterPage(this, getPassport(), mOrientation);
            mTvTitleBarCenter.setText(KR.string.splus_person_center_text);
            addView(mPersonCenterPage, PersonCenterPage.class.getName());
        }

        anim_in_into = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        anim_in_into.setDuration(mAnim_time);
        anim_out_into = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        anim_out_into.setDuration(mAnim_time);

        anim_in_back = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        anim_in_back.setDuration(mAnim_time);
        anim_out_back = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        anim_out_back.setDuration(mAnim_time);
    }

    @Override
    protected void setListener() {

        if (mPersonCenterPage != null) {
            mPersonCenterPage.setOnPersonClickListener(mPersonClickListener);
        }
        ibtn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goBack();

            }
        });
        ibtn_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void processLogic() {
    }

    /**
     * 个人中心页面点击事件
     */
    private PersonCenterPage.PersonClickListener mPersonClickListener = new PersonCenterPage.PersonClickListener() {

        @Override
        public void onAccountClick(View v) {
            // 进入账号安全页面
            if (mAccountManagerPage == null) {
                mAccountManagerPage = new AccountManagerPage(mActivity, getPassport(), getUid(), mSplusPayManager.getServerId(),mSplusPayManager.getRoleId(),mSplusPayManager.getServerName(),mSplusPayManager.getRoleName(), getDeviceno(), mSplusPayManager.getPartner(), mSplusPayManager.getReferer(), mSplusPayManager.getGameid(), mSplusPayManager.getAppkey(),
                                mOrientation);
                mAccountManagerPage.setOnAccountClickListener(mAccountClickListener);
            }
            mTvTitleBarCenter.setText(KR.string.splus_person_center_idcard_btn_text);
            // 把通行证管理页面显示在前台
            addView(mAccountManagerPage, AccountManagerPage.class.getName());

        }

        @Override
        public void onSQClick(View v) {
            // 进入客服中心页面
            if (mSqPage == null) {
                mSqPage = new SQPage(mActivity, getDeviceno(), mSplusPayManager.getGameid(), mSplusPayManager.getPartner(), mSplusPayManager.getReferer(), getUid(), getPassport(), getPassword(), mSplusPayManager.getServerId(),mSplusPayManager.getRoleId(),mSplusPayManager.getRoleName(), mSplusPayManager.getServerName(),mSplusPayManager.getAppkey());
            }
            mTvTitleBarCenter.setText(KR.string.splus_person_center_sq_btn_text);
            addView(mSqPage, SQPage.class.getName());
        }

        @Override
        public void onForumClick(View v) {
            // 进入论坛页面看
            if (mForumPage == null) {
                mForumPage = new ForumPage(mActivity, getDeviceno(), mSplusPayManager.getGameid(), mSplusPayManager.getPartner(), mSplusPayManager.getReferer(), getUid(), getPassport(), getPassword(), mSplusPayManager.getServerId(),mSplusPayManager.getRoleId(),mSplusPayManager.getRoleName(), mSplusPayManager.getServerName(),mSplusPayManager.getAppkey());
            }
            mTvTitleBarCenter.setText(KR.string.splus_person_center_forum_btn_text);
            // 把论坛页面显示在前台
            addView(mForumPage, ForumPage.class.getName());
        }

        @Override
        public void onLogoutClick(View v) {
            // 进入注销页面
            if (mLogoutPage == null) {
                mLogoutPage = new LogoutPage(mActivity, getPassport());
            }
            mTvTitleBarCenter.setText(KR.string.splus_person_center_logout_btn_text);
            // 把注销页面显示在前台
            addView(mLogoutPage, LogoutPage.class.getName());

        }

        @Override
        public void onAnnouncement(View v) {
            // 进入活动页面
            if (mAnnouncementsPage == null) {
                mAnnouncementsPage = new AnnouncementsPage(mActivity, getDeviceno(), mSplusPayManager.getGameid(), mSplusPayManager.getPartner(), mSplusPayManager.getReferer(), getUid(), getPassport(), getPassword(),mSplusPayManager.getServerId(),mSplusPayManager.getRoleId(), mSplusPayManager.getRoleName(),
                                mSplusPayManager.getServerName(),mSplusPayManager.getAppkey());
            }
            mTvTitleBarCenter.setText(KR.string.splus_person_center_announcementspage_btn_text);
            // 把活动页面显示在前台
            addView(mAnnouncementsPage, AnnouncementsPage.class.getName());

        }

        @Override
        public void onGameRecommendationClick(View v) {

        }
    };

    /**
     * 安全信息页面点击事件
     */
    private AccountManagerPage.AccountClickListener mAccountClickListener = new AccountManagerPage.AccountClickListener() {

        @Override
        public void onUserInformationClick(View v) {

            // 进入个人资料页面
            if (mUserInformationPage == null) {
                mUserInformationPage = new UserInformationPage(PersonActivity.this, getPassport(), getUid(), mSplusPayManager.getServerId(),mSplusPayManager.getRoleId(),mSplusPayManager.getServerName(),mSplusPayManager.getRoleName(), getDeviceno(), mSplusPayManager.getPartner(), mSplusPayManager.getReferer(), mSplusPayManager.getGameid(), mSplusPayManager.getAppkey(),
                                mOrientation);
            }
            // 把个人资料页面显示在前台
            mTvTitleBarCenter.setText(KR.string.splus_person_account_user_information);
            addView(mUserInformationPage, UserInformationPage.class.getName());

        }

        @Override
        public void onPasswordClick(View v) {
            // 进入修改密码页面
            if (mPasswordPage == null) {
                mPasswordPage = new PasswordPage(PersonActivity.this, getPassport(), getUid(), mSplusPayManager.getServerId(),mSplusPayManager.getRoleId(),mSplusPayManager.getServerName(),mSplusPayManager.getRoleName(), getDeviceno(), mSplusPayManager.getPartner(), mSplusPayManager.getReferer(), mSplusPayManager.getGameid(), mSplusPayManager.getAppkey(), mHandler,
                                mOrientation);
            }
            mTvTitleBarCenter.setText(KR.string.splus_person_account_modify_pwd);
            // 把修改密码页面显示在前台
            addView(mPasswordPage, PasswordPage.class.getName());

        }

        @Override
        public void onPhoneClick(View v) {
            // 进入绑定手机页面
            if (mPhoneBindPage == null) {
                mPhoneBindPage = new PhoneBindPage(PersonActivity.this, getPassport(), getUid(), mSplusPayManager.getServerId(),mSplusPayManager.getRoleId(),mSplusPayManager.getServerName(),mSplusPayManager.getRoleName(), getDeviceno(), mSplusPayManager.getPartner(), mSplusPayManager.getReferer(), mSplusPayManager.getGameid(), mSplusPayManager.getAppkey(),
                                mAccountManagerPage.isBinded(), mHandler, mOrientation);
            }
            mPhoneBindPage.setBindedStatus();
            mTvTitleBarCenter.setText(KR.string.splus_person_account_binding_phone);
            // 把绑定手机页面显示在前台
            addView(mPhoneBindPage, PhoneBindPage.class.getName());

        }

    };

    /**
     * 页面添加
     *
     * @author xiaoming.yuan
     * @date 2013年9月30日 下午6:08:35
     * @param v
     * @param tag
     */
    private void addView(View v, String tag) {
        View view = vf_person_center.getCurrentView();
        if (v.equals(view)) {
            mCurrentPage = tag;
            return;
        }

        int count = vf_person_center.getChildCount();
        boolean hasView = false;
        for (int i = 0; i < count; i++) {
            view = vf_person_center.getChildAt(i);
            if (view.equals(v)) {
                hasView = true;
                break;
            }
        }
        if (hasView) {
            vf_person_center.removeView(v);
        }
        vf_person_center.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mCurrentPage = tag;
        showNextPage();
    }

    /**
     * 显示下一个页面
     *
     * @author xiaoming.yuan
     * @date 2013年9月30日 下午5:20:09
     */
    private void showNextPage() {
        vf_person_center.setInAnimation(anim_in_into);
        vf_person_center.setOutAnimation(anim_out_into);
        vf_person_center.showNext();
        hideSoftInput(mActivity);
    }

    /**
     * 页面返回事件
     *
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午2:24:30
     * @return
     */
    private void goBack() {
        hideSoftInput(PersonActivity.this);
        /**
         * 正在切换中
         */
        if (vf_person_center.isFlipping()) {
            return;
        }

        if (ForumPage.class.getName().equals(mCurrentPage) && mForumPage != null) {
            if (mForumPage.goBack()) {
                mTvTitleBarCenter.setText(KR.string.splus_person_center_forum_btn_text);
                return;
            }
        }

        if (SQPage.class.getName().equals(mCurrentPage) && mSqPage != null) {
            if (mSqPage.goBack()) {
                mTvTitleBarCenter.setText(KR.string.splus_person_center_sq_btn_text);
                return;
            }
        }
        if (AnnouncementsPage.class.getName().equals(mCurrentPage) && mAnnouncementsPage != null) {
            if (mAnnouncementsPage.goBack()) {
                mTvTitleBarCenter.setText(KR.string.splus_person_center_announcementspage_btn_text);
                return;
            }
        }
        int count = vf_person_center.getChildCount();
        if (count == 1) {
            // 第一个视图,结束当前activity
            finish();
            mCurrentPage = "";
        } else {
            vf_person_center.setInAnimation(anim_in_back);
            vf_person_center.setOutAnimation(anim_out_back);
            vf_person_center.showPrevious();
            vf_person_center.removeViewAt(count - 1);
            mCurrentPage = vf_person_center.getChildAt(count - 2).getClass().getName();
        }

        if (PersonCenterPage.class.getName().equals(mCurrentPage) && mPersonCenterPage != null) {
            mTvTitleBarCenter.setText(KR.string.splus_person_center_text);
        }

        if (AccountManagerPage.class.getName().equals(mCurrentPage) && mAccountManagerPage != null) {
            mTvTitleBarCenter.setText(KR.string.splus_person_center_idcard_btn_text);
        }

        if (UserInformationPage.class.getName().equals(mCurrentPage) && mUserInformationPage != null) {
            mTvTitleBarCenter.setText(KR.string.splus_person_account_user_information);
        }

        if (PasswordPage.class.getName().equals(mCurrentPage) && mPasswordPage != null) {
            mTvTitleBarCenter.setText(KR.string.splus_person_account_modify_pwd);
        }

        if (PhoneBindPage.class.getName().equals(mCurrentPage) && mPhoneBindPage != null) {
            mPhoneBindPage.unregisterReceiver();
            mTvTitleBarCenter.setText(KR.string.splus_person_account_binding_phone);
        }
    }

    /**
     * @Title: logout_in_person(个人中心注销)
     * @author xiaoming.yuan
     * @data 2014-3-24 上午10:48:42 void 返回类型
     */
    public void logout_in_person() {
        SharedPreferencesHelper.getInstance().setLoginStatusPreferences(mActivity, mSplusPayManager.getAppkey(), false);
        finish();
        ExitAppUtils.getInstance().exit();
        if (mLogoutCallBack != null) {
            mLogoutCallBack.logoutCallBack();

        } else {
            Intent intent =getApplicationContext().getPackageManager().getLaunchIntentForPackage(mActivity.getPackageName());
            if(android.os.Build.VERSION.SDK_INT >= 11){
                intent.addFlags(32768);
            }else{
                intent.addFlags(67108864);
            }
            PendingIntent intent1 = PendingIntent.getActivity(mActivity, 223344, intent, 268435456);
            ((AlarmManager)getSystemService("alarm")).set(1, System.currentTimeMillis() + 100L, intent1);
            System.exit(0);
        }

        //        else {
        //            Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        //            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //            startActivity(i);
        //        }

    }

    /**
     * 网络请求成功回调
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PASSWORDPAGE:
                    String newPwd = (String) msg.obj;
                    savaNewPwd(newPwd);
                    goBack();
                    break;
                case PHONEBIND:
                    if (mAccountManagerPage != null) {
                        mAccountManagerPage.setBindStatus(true);
                    }
                    goBack();
                    break;
                case LOGOUT:
                    logout_in_person();
                    break;
                default:
                    break;
            }

        };
    };

    /**
     * 保存新密码
     *
     * @author xiaoming.yuan
     * @date 2013年10月10日 下午11:03:42
     * @param newPwd
     */
    private void savaNewPwd(String newPwd) {
        UserModel userModel = mSplusPayManager.getUserModel();
        if (userModel == null) {
            userModel = AppUtil.getUserData();
        }
        if (userModel == null) {
            return;
        }
        userModel.setPassword(newPwd);
        mSplusPayManager.setUserModel(userModel);
        AccountObservable.getInstance().modifyUser(userModel);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPhoneBindPage != null) {
            mPhoneBindPage.unregisterReceiver();
        }
        mPersonCenterPage = null;
        mLogoutPage = null;
        mSqPage = null;
        mForumPage = null;
        mAnnouncementsPage = null;
        mAccountManagerPage = null;
        mUserInformationPage = null;
        mPasswordPage = null;
        mPhoneBindPage = null;
    }

    /**
     * Title: finish Description:
     *
     * @see android.app.Activity#finish()
     */
    @Override
    public void finish() {
        super.finish();
        ExitAppUtils.getInstance().exit();
    }

}
