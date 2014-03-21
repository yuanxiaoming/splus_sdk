
package com.android.splus.sdk.ui;

import com.android.splus.sdk.apiinterface.LogoutCallBack;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.ui.person.PersonCenter;
import com.android.splus.sdk.utils.file.AppUtil;
import com.android.splus.sdk.utils.r.KR;

import android.app.Activity;
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
    private String mCurrentPage = PersonCenter.class.getName();

    /**
     * 个人中心页面
     */
    private PersonCenter mPersonCenter;


    @Override
    protected void loadViewLayout() {
        setContentView(KR.layout.splus_person_center_views);
        this.mActivity = this;
        mSplusPayManager = SplusPayManager.getInstance();
        mLogoutCallBack = mSplusPayManager.getLogoutCallBack();
    }

    @Override
    protected void findViewById() {
        ibtn_back = (ImageButton) findViewById(KR.id.splus_person_title_bar_left_button);
        ibtn_submit = (ImageButton) findViewById(KR.id.splus_person_title_bar_right_button);
        mTvTitleBarCenter = (TextView) findViewById(KR.id.splus_person_title_bar_middle_title);
        vf_person_center = (ViewFlipper) findViewById(KR.id.splus_person_center_views);


        mPersonCenter = new PersonCenter(this, getPassport());
        mTvTitleBarCenter.setText("用户中心");
        addView(mPersonCenter, PersonCenter.class.getName());



        anim_in_into = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        anim_in_into.setDuration(mAnim_time);
        anim_out_into = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -1, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        anim_out_into.setDuration(mAnim_time);

        anim_in_back = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1,
                Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        anim_in_back.setDuration(mAnim_time);
        anim_out_back = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        anim_out_back.setDuration(mAnim_time);
    }

    @Override
    protected void setListener() {
        if (mPersonCenter != null) {
          //  mPersonCenter.setOnPersonClickListener(mPersonClickListener);
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

            }
        });



    }

    @Override
    protected void processLogic() {



    }


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

        vf_person_center.addView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
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
        hideSoftInput(this);
    }



    /**
     * @return String 返回类型
     * @Title: passport(获取用户名)
     * @author xiaoming.yuan
     * @data 2013-8-14 下午12:33:59
     */
    protected String getPassport() {
        UserModel userModel = getUserData();
        if (null != userModel && null != userModel.getUserName()) {
            return userModel.getUserName();
        }
        return "";
    }

    /**
     * @return Integer 返回类型
     * @Title: uid(获取用户名的唯一标示)
     * @author xiaoming.yuan
     * @data 2013-8-14 下午12:33:59
     */
    protected Integer getUid() {
        UserModel userModel = getUserData();
        if (null != userModel && null != userModel.getUserName()) {
            return userModel.getUid();
        }
        return null;
    }

    /**
     * @return String 返回类型
     * @Title: password(获取用户密码)
     * @author xiaoming.yuan
     * @data 2013-8-14 下午12:33:26
     */
    protected String getPassword() {
        UserModel userModel = getUserData();
        if (null != userModel && null != userModel.getPassword()) {
            return userModel.getPassword();
        }
        return "";
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年10月18日 下午6:58:56
     * @return
     */
    protected UserModel getUserData() {
        UserModel userModel = SplusPayManager.getInstance().getUserData();
        if (userModel == null) {
            userModel = AppUtil.getUserData();
        }
        return userModel;
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

    }

}

