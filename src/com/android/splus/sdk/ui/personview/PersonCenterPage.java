package com.android.splus.sdk.ui.personview;

import com.android.splus.sdk.utils.phone.Phoneuitl;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
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
public class PersonCenterPage extends ScrollView {

    private static final String TAG = "PersonCenter";

    private Activity mActivity;

    private String mPassport;

    /**
     * 是否为横屏
     */
    private boolean mLandscape = true;

    private PersonClickListener mPersonClickListener;

    private Body mBody;

    public PersonCenterPage(Activity activity, String passport,int orientation) {
        super(activity);
        this.mActivity = activity;
        this.mPassport = passport;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            mLandscape = true;

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            mLandscape = false;
        }
        mBody = new Body(mActivity, mPassport, mLandscape);
        mBody.setOrientation(LinearLayout.VERTICAL);
        addView(mBody);
    }

    /**
     * @ClassName:Body
     * @author xiaoming.yuan
     * @date 2013年9月28日 下午4:11:08
     */
    private class Body extends LinearLayout {

        private View mWelcomeView;

        private TextView tv_welcom;

        private TextView mTvAccount;

        private TextView mTvSq;

        private TextView mTvForum;

        private TextView mTvAnnouncementsPage;

        private TextView mTvLogout;

        private View btn_account, btn_sq, btn_forum, btn_logout, btn_announcementsPage;

        /**
         * 分隔线
         */
        private ImageView mLine1,mLine2,mLine3,mLine4;

        private PersonClickListener mListener;

        /**
         * 字体颜色
         */
        private static final String COLORFE5F2E = "#fe5f2e";


        public Body(Activity activity, String passport, Boolean isLandscape) {
            super(activity);
            mWelcomeView = inflate(activity,
                    ResourceUtil.getLayoutId(activity, KR.layout.splus_person_center_welcome), null);
            btn_account = inflate(activity,
                    ResourceUtil.getLayoutId(activity, KR.layout.splus_person_center_top_item),
                    null);
            btn_sq = inflate(activity,
                    ResourceUtil.getLayoutId(activity, KR.layout.splus_person_center_item), null);
            btn_forum = inflate(activity,
                    ResourceUtil.getLayoutId(activity, KR.layout.splus_person_center_item), null);
            btn_announcementsPage = inflate(activity,
                    ResourceUtil.getLayoutId(activity, KR.layout.splus_person_center_item), null);
            btn_logout = inflate(activity,
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

            addView(mWelcomeView, mWelcomeViewParams);

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
            mLine3 = new ImageView(activity);
            mLine3.setBackgroundColor(0xfff7f7f7);
            mLine3.setScaleType(ScaleType.FIT_XY);
            mLine3.setImageResource(ResourceUtil.getDrawableId(activity,
                    KR.drawable.splus_login_bg_devider));
            mLine4 = new ImageView(activity);
            mLine4.setBackgroundColor(0xfff7f7f7);
            mLine4.setScaleType(ScaleType.FIT_XY);
            mLine4.setImageResource(ResourceUtil.getDrawableId(activity,
                    KR.drawable.splus_login_bg_devider));
            int padding = 20;
            if (isLandscape) {
                padding = 20;
            } else {
                padding = 0;
            }
            mLine1.setPadding(padding, 0, padding, 0);
            mLine2.setPadding(padding, 0, padding, 0);
            mLine3.setPadding(padding, 0, padding, 0);
            mLine4.setPadding(padding, 0, padding, 0);

            addView(btn_account, params);
            addView(mLine1, params1);

            addView(btn_sq, params);
            addView(mLine2, params1);

            addView(btn_forum, params);
            addView(mLine3, params1);

            addView(btn_announcementsPage, params);
            addView(mLine4, params1);

            addView(btn_logout, params2);

            intViews(activity, passport, isLandscape);
            setListener();

        }

        /**
         * @author xiaoming.yuan
         * @date 2013年9月28日 下午4:10:55
         */
        private void intViews(Activity activity, String passport, Boolean isLandscape) {
            tv_welcom = (TextView) mWelcomeView.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_welcome_text));
            setUserName(isLandscape, passport);

            ((ImageView) (btn_account.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_top_ic_left))))
                    .setImageResource(ResourceUtil.getDrawableId(activity,
                            KR.drawable.splus_person_center_account_icon_selector));
            ((ImageView) (btn_account.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_top_ic_right)))).setImageResource(ResourceUtil
                    .getDrawableId(activity, KR.drawable.splus_person_center_arrow_icon_selector));
            mTvAccount = (TextView) btn_account.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_top_tv));
            mTvAccount.setText(KR.string.splus_person_center_idcard_btn_text);
            mTvAccount.setTextColor(createColorStateList(0xff747474, 0xffffffff, 0xffffffff,
                    0xffffffff));

            ((ImageView) (btn_sq.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_ic_left)))).setImageResource(ResourceUtil
                    .getDrawableId(activity, KR.drawable.splus_person_center_sq_icon_selector));
            ((ImageView) (btn_sq.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_ic_right)))).setImageResource(ResourceUtil
                    .getDrawableId(activity, KR.drawable.splus_person_center_arrow_icon_selector));
            mTvSq = (TextView) btn_sq.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_tv));
            mTvSq.setText(KR.string.splus_person_center_sq_btn_text);
            mTvSq.setTextColor(createColorStateList(0xff747474, 0xffffffff, 0xffffffff, 0xffffffff));

            ((ImageView) (btn_forum.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_ic_left)))).setImageResource(ResourceUtil
                    .getDrawableId(activity, KR.drawable.splus_person_center_forum_icon_selector));
            ((ImageView) (btn_forum.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_ic_right)))).setImageResource(ResourceUtil
                    .getDrawableId(activity, KR.drawable.splus_person_center_arrow_icon_selector));

            mTvForum = (TextView) btn_forum.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_tv));
            mTvForum.setText(KR.string.splus_person_center_forum_btn_text);
            mTvForum.setTextColor(createColorStateList(0xff747474, 0xffffffff, 0xffffffff,
                    0xffffffff));

            ((ImageView) (btn_announcementsPage.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_ic_left)))).setImageResource(ResourceUtil
                    .getDrawableId(activity,
                            KR.drawable.splus_person_center_announcementspage_icon_selector));
            ((ImageView) (btn_announcementsPage.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_ic_right)))).setImageResource(ResourceUtil
                    .getDrawableId(activity, KR.drawable.splus_person_center_arrow_icon_selector));
            mTvAnnouncementsPage = (TextView) btn_announcementsPage.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_item_tv));
            mTvAnnouncementsPage.setText(KR.string.splus_person_center_announcementspage_btn_text);
            mTvAnnouncementsPage.setTextColor(createColorStateList(0xff747474, 0xffffffff,
                    0xffffffff, 0xffffffff));

            ((ImageView) (btn_logout.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_bottom_item_ic_left)))).setImageResource(ResourceUtil
                    .getDrawableId(activity, KR.drawable.splus_person_center_logout_icon_selector));
            ((ImageView) (btn_logout.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_bottom_item_ic_right)))).setImageResource(ResourceUtil
                    .getDrawableId(activity, KR.drawable.splus_person_center_arrow_icon_selector));
            mTvLogout = (TextView) btn_logout.findViewById(ResourceUtil.getId(activity,
                    KR.id.splus_person_center_bottom_item_tv));
            mTvLogout.setText(KR.string.splus_person_center_logout_btn_text);
            mTvLogout.setTextColor(createColorStateList(0xff747474, 0xffffffff, 0xffffffff,
                    0xffffffff));

        }

        /**
         * @Title: setListener(事件监听)
         * @author xiaoming.yuan
         * @data 2014-3-21 下午2:38:30 void 返回类型
         */
        private void setListener() {

            btn_account.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mListener.onAccountClick(v);
                }
            });
            btn_forum.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    mListener.onForumClick(v);
                }
            });
            btn_sq.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mListener.onSQClick(v);

                }
            });
            btn_logout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    mListener.onLogoutClick(v);
                }
            });
            btn_announcementsPage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mListener.onAnnouncement(v);
                }
            });

        }

        /**
         * @author xiaoming.yuan
         * @date 2013年9月29日 下午4:28:56
         * @param listener
         */
        public void setOnPersonClickListener(PersonClickListener listener) {
            this.mListener = listener;
        }

        /**
         * @author xiaoming.yuan
         * @date 2013年9月29日 下午4:28:59
         */
        private void setUserName(Boolean isLandscape, String passport) {
            String replace;
            if (isLandscape) {
                replace = KR.string.splus_person_center_welcome_text.replace("%s", passport);
            } else {
                replace = KR.string.splus_person_center_welcome_text_portrait.replace("%s",
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

    }

    /**
     * @ClassName:PersonClickListener
     * @author xiaoming.yuan
     * @date 2013年9月28日 下午4:11:00
     */
    public interface PersonClickListener {

        public void onAccountClick(View v);

        public void onSQClick(View v);

        public void onForumClick(View v);

        public void onLogoutClick(View v);

        public void onAnnouncement(View v);

        public void onGameRecommendationClick(View v);
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午4:28:56
     * @param listener
     */
    public void setOnPersonClickListener(PersonClickListener listener) {
        this.mPersonClickListener = listener;
        mBody.setOnPersonClickListener(mPersonClickListener);
    }

}
