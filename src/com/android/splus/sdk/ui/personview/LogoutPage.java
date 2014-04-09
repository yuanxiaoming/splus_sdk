
package com.android.splus.sdk.ui.personview;

import com.android.splus.sdk.ui.PersonActivity;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class LogoutPage extends ScrollView {

    private static final String TAG = "LogoutPage";

    /**
     * 字体颜色
     */
    private static final String COLORFE5F2E = "#fe5f2e";

    private Activity mActivity;

    private TextView tv_welcom, tv_playgame, tv_gamehint, tv_gamename, tv_verhint, tv_vername;

    private Button btn_logout;

    private ImageView img_gameicon;

    private ApplicationInfo mApplicationInfo;

    private PackageInfo mPackageInfo;

    private String mPassport;

    public LogoutPage(Activity activity, String passport) {
        super(activity);
        this.mActivity = activity;
        this.mPassport = passport;

        mApplicationInfo = activity.getApplicationInfo();
        try {
            mPackageInfo = activity.getPackageManager()
                    .getPackageInfo(activity.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        inflate(activity, ResourceUtil.getLayoutId(activity, KR.layout.splus_person_logout), this);
        findViews();
        initViews();
        setListener();

    }

    private void findViews() {

        tv_welcom = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_person_logout_account_tv));
        tv_playgame = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_person_logout_game_tv));
        tv_gamehint = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_person_logout_game_name_hint));
        tv_gamename = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_person_logout_game_name_text));
        tv_verhint = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_person_logout_game_version_hint));
        tv_vername = (TextView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_person_logout_game_version_text));
        btn_logout = (Button) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_person_logout_btn));
        btn_logout.setTextColor(Color.WHITE);
        img_gameicon = (ImageView) findViewById(ResourceUtil.getId(mActivity,
                KR.id.splus_person_logout_game_ic));
    }

    private void initViews() {
        tv_welcom.setText(KR.string.splus_person_logout_account_tv);
        tv_playgame.setText(KR.string.splus_person_logout_game_tv);
        tv_gamehint.setText(KR.string.splus_person_logout_game_name_hint);
        tv_verhint.setText(KR.string.splus_person_logout_game_version_hint);
        btn_logout.setText(KR.string.splus_person_logout);
        setUserName();
        tv_gamename.setSingleLine();
        tv_gamename.setText(getAppName());
        tv_vername.setText(getAppVersion());
        img_gameicon.setImageResource(getAppIcon());

    }

    private void setListener() {

        btn_logout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mActivity instanceof PersonActivity) {
                    ((PersonActivity) mActivity).logout_in_person();
                }

            }
        });

    }

    private void setUserName() {

        String replaceWelcom = KR.string.splus_person_logout_account_tv.replace("%s", mPassport);
        tv_welcom.setText(replaceWelcom);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(
                tv_welcom.getText());
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor(COLORFE5F2E)),
                replaceWelcom.indexOf(mPassport), replaceWelcom.length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv_welcom.setText(spannableStringBuilder);

    }

    /**
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午8:50:55
     * @return
     */
    private String getAppName() {
        return mApplicationInfo.loadLabel(mActivity.getPackageManager()).toString();
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午8:50:59
     * @return
     */
    private String getAppVersion() {
        if (mPackageInfo != null) {
            return mPackageInfo.versionName;
        }
        return "无版本号";
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年9月29日 下午8:51:46
     * @return
     */
    private int getAppIcon() {
        return mApplicationInfo.icon;
    }

}
