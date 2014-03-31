/**
 * 系统项目名称
 * com.sanqi.android.sdk.ui
 * LoginWindow1.java
 *
 */

package com.android.splus.sdk.ui;

import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.phone.Phoneuitl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * LoginWindow xiaoming.yuan2013-12-11 下午4:28:02
 *
 * @version 1.0.0
 */
public class LoginDialog extends AlertDialog {
    private Activity mActivity;

    private LoginView mLoginView;

    private RegisterView mRegisterView;

    private View mCurrentView;

    protected LoginDialog(Activity activity, String tag) {
        super(activity);
        this.mActivity = activity;
        // 创建当前展示的view
        createCurrentView(tag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (mCurrentView != null) {
            setContentView(mCurrentView,
                    CommonUtil.getFrameLayoutParams(mActivity, 120, 120, 25, 25, Gravity.CENTER));
        } else {
            LogHelper.e("loginDialog", "mCurrentView is null");
        }
    }

    /**
     * 调整视图
     */
    public void onWindowFocusChanged(boolean hasFocus) {
//        if (!hasFocus) {
//            initWindow();
//        }
        initWindow();
        setContentView(mCurrentView,
                CommonUtil.getFrameLayoutParams(mActivity, 120, 120, 25, 25, Gravity.CENTER));
        super.onWindowFocusChanged(hasFocus);

    }

    /**
     * changeView(用于根据tag改变对话框中的view) (这里描述这个方法适用条件 – 可选)
     *
     * @param tag void
     * @exception
     * @since 1.0.0 xilin.chen
     */
    public void changeView(String tag) {
        createCurrentView(tag);
        setContentView(mCurrentView,
                CommonUtil.getFrameLayoutParams(mActivity, 120, 120, 25, 25, Gravity.CENTER));
    }

    private void createCurrentView(String tag) {
        if (LoginView.class.getSimpleName().equals(tag)) {
            if (mLoginView == null) {
                mLoginView = new LoginView(mActivity, this);
            }
            // 如果不需要保存上一次操作状态的view，需要调用方法恢复初始化值
            mLoginView.recoveryState();
            mCurrentView = mLoginView;
        } else if (RegisterView.class.getSimpleName().equals(tag)) {
            if (mRegisterView == null) {
                mRegisterView = new RegisterView(mActivity, this);
            }
            // 如果不需要保存上一次操作状态的view，需要调用方法恢复初始化值
            mRegisterView.recoveryState();
            mCurrentView = mRegisterView;
        }
    }

    private void initWindow() {
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = 0.0f;
        if (SplusPayManager.getInstance().getOrientation() == Phoneuitl.getOrientation(mActivity)) {
            lp.width = SplusPayManager.getInstance().getWidth();
            lp.height = SplusPayManager.getInstance().getHeight();
        } else {
            int height = Phoneuitl.getHpixels(mActivity);
            int width = Phoneuitl.getWpixels(mActivity);
            if (Phoneuitl.getOrientation(mActivity) == Configuration.ORIENTATION_LANDSCAPE) {
                // 横屏方向，高<宽
                if (height > width) {
                    int temp = width;
                    width = height;
                    height = temp;
                }
            } else {
                // 竖屏方向 高>宽
                if (height < width) {
                    int temp = width;
                    width = height;
                    height = temp;
                }
            }
            lp.height = height;
            lp.width =  width;
        }
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = android.R.style.Animation;
        window.setAttributes(lp);

    }

    @Override
    public void show() {
        super.show();
        initWindow();
    }

    public interface ViewRecoveryState {
        public void recoveryState();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (SplusPayManager.getInstance().getLoginCallBack() != null) {
            SplusPayManager.getInstance().getLoginCallBack().loginFaile("取消登录操作");
        }

    }

}
