
package com.android.splus.sdk.ui;

import com.android.splus.sdk.manager.ExitAppUtils;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.utils.http.NetHttpUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil.DataCallback;
import com.android.splus.sdk.utils.http.RequestModel;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.utils.sharedPreferences.SharedPreferencesHelper;
import com.android.splus.sdk.widget.CustomProgressDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA. Copyright: Copyright (c) 2013
 * User: xiaoming.yuan Email: lijianfeng@37wan.com Date:
 * 13-9-17 Time: 上午10:51 To change this template use File | Settings | File
 * Templates.
 */
public abstract class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";

    protected Context mContext;

    protected CustomProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mContext = this;
        initView();
        ExitAppUtils.getInstance().addActivity(this);

    }

    /**
     * @return void 返回类型
     * @Title: initView(初始化组件)
     * @author xiaoming.yuan
     * @data 2013-7-12 下午9:13:35
     */
    private void initView() {
        loadViewLayout();
        findViewById();
        setListener();
        processLogic();

    }

    /**
     * @author xiaoming.yuan
     * @date 2013年9月28日 下午4:23:48
     * @param layoutResName
     */
    public void setContentView(String layoutResName) {
        super.setContentView(ResourceUtil.getLayoutId(this, layoutResName));
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年9月28日 下午4:25:48
     * @param viewName
     * @return
     */
    public View findViewById(String viewName) {
        return super.findViewById(ResourceUtil.getId(this, viewName));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext = null;
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        ExitAppUtils.getInstance().delActivity(this);
    }

    /**
     * @return String 返回类型
     * @Title: passport(获取用户名)
     * @author xiaoming.yuan
     * @data 2013-8-14 下午12:33:59
     */
    protected String getPassport() {
        return null;
    }

    /**
     * @return Integer 返回类型
     * @Title: uid(获取用户名的唯一标示)
     * @author xiaoming.yuan
     * @data 2013-8-14 下午12:33:59
     */
    protected Integer getUid() {
        return null;
    }

    /**
     * @return String 返回类型
     * @Title: password(获取用户密码)
     * @author xiaoming.yuan
     * @data 2013-8-14 下午12:33:26
     */
    protected String getPassword() {
        return null;
    }

    /**
     * @author xiaoming.yuan
     * @date 2013年10月18日 下午6:58:56
     * @return
     */
    protected UserModel getUserData() {
        return null;
    }


    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput(Context context) {
        InputMethodManager manager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        View view = getCurrentFocus();
        if (view != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Title: onResume Description:
     *
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSoftInput(this);
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

    /**
     * @return void 返回类型
     * @Title: showProgressDialog(设置进度条)
     * @author xiaoming.yuan
     * @data 2013-7-12 下午10:09:36
     */
    protected void showProgressDialog() {
        if ((!isFinishing()) && this.mProgressDialog == null) {
            this.mProgressDialog = new CustomProgressDialog(this);
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

    /**
     * @return void 返回类型
     * @Title: closeProgressDialog(关闭进度条)
     * @author xiaoming.yuan
     * @data 2013-7-12 下午10:09:30
     */
    protected void closeProgressDialog() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing())
            this.mProgressDialog.dismiss();
    }

    /**
     * @return void 返回类型
     * @Title: findViewById(实例化组件)
     * @author xiaoming.yuan
     * @data 2013-8-5 下午10:15:03
     * @param:
     */
    protected abstract void findViewById();

    /**
     * @return void 返回类型
     * @Title: loadViewLayout(设置布局文件)
     * @author xiaoming.yuan
     * @data 2013-7-12 下午10:09:09
     */
    protected abstract void loadViewLayout();

    /**
     * @return void 返回类型
     * @Title: processLogic(处理一些其余的操作)
     * @author xiaoming.yuan
     * @data 2013-7-12 下午10:09:01
     */
    protected abstract void processLogic();

    /**
     * @return void 返回类型
     * @Title: setListener(设置控件监听)
     * @author xiaoming.yuan
     * @data 2013-7-12 下午10:08:51
     */
    protected abstract void setListener();

    protected <T> void getDataFromServer(RequestModel mRequestModel, DataCallback<T> callBack) {

        if (setIsShowProgressDialog()) {
            if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                showProgressDialog();
            }
        }
        NetHttpUtil.getDataFromServerPOST(BaseActivity.this, mRequestModel, callBack);
    }

    /**
     * @Title: isGetDeviceno(是否获取deviceno)
     * @author xiaoming.yuan
     * @data 2013-9-26 上午9:29:22
     * @return boolean 返回类型
     */
    protected boolean isGetDeviceno() {
        return true;
    }

    /***
     * @Title: setIsShowProgressDialog(是否显示对话框)
     * @author xiaoming.yuan
     * @data 2013-10-3 下午4:09:53
     * @return boolean 返回类型
     */
    public boolean setIsShowProgressDialog() {
        return true;
    }

    /**
     * @return String 返回类型
     * @Title: getDeviceno(获取设备标识)
     * @author xiaoming.yuan
     * @data 2013-8-10 下午4:55:01
     */
    protected String getDeviceno() {
        return SharedPreferencesHelper.getInstance().getdevicenoPreferences(mContext);
    }


}
