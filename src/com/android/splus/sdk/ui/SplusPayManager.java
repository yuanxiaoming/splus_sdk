/**
 * @Title: SplusPayManager.java
 * @Package com.android.cansh.sdk.ui
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-2-26 下午2:28:10
 * @version V1.0
 */

package com.android.splus.sdk.ui;

import com.android.splus.sdk.api.InitBean;
import com.android.splus.sdk.apiinterface.IPayManager;
import com.android.splus.sdk.apiinterface.InitCallBack;
import com.android.splus.sdk.apiinterface.LoginCallBack;
import com.android.splus.sdk.apiinterface.LogoutCallBack;
import com.android.splus.sdk.apiinterface.RechargeCallBack;
import com.android.splus.sdk.data.ActiveData;
import com.android.splus.sdk.manager.AccountObservable;
import com.android.splus.sdk.manager.ExitAppUtils;
import com.android.splus.sdk.model.ActiveModel;
import com.android.splus.sdk.model.GameStatisticsModel;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.parse.ActiveParser;
import com.android.splus.sdk.parse.LoginParser;
import com.android.splus.sdk.ui.FloatToolBar.FloatToolBarAlign;
import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.Constant;
import com.android.splus.sdk.utils.date.DateUtil;
import com.android.splus.sdk.utils.file.AppUtil;
import com.android.splus.sdk.utils.http.BaseParser;
import com.android.splus.sdk.utils.http.NetHttpUtil;
import com.android.splus.sdk.utils.http.NetHttpUtil.DataCallback;
import com.android.splus.sdk.utils.http.NetWorkUtil;
import com.android.splus.sdk.utils.http.RequestModel;
import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.md5.MD5Util;
import com.android.splus.sdk.utils.phone.Phoneuitl;
import com.android.splus.sdk.utils.progressDialog.ProgressDialogUtil;
import com.android.splus.sdk.utils.sharedPreferences.SharedPreferencesHelper;
import com.android.splus.sdk.utils.toast.ToastUtil;
import com.android.splus.sdk.widget.SplashPage;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Properties;

/**
 * @ClassName: SplusPayManager
 * @author xiaoming.yuan
 * @date 2014-2-26 下午2:28:10
 */

public class SplusPayManager implements IPayManager {
    private static final String TAG = "SplusPayManager";

    private static final String CONFIG_FILENAME="splusconfig";

    public static final String SDK_VERSION = "1.0";

    private static SplusPayManager mManager;

    private static byte[] lock = new byte[0];

    private static byte[] lock1 = new byte[0];

    private static final int INIT_SUCCESS = 0;

    private static final int INIT_START = 1;

    private static final int LOGIN_SUCCESS = 2;

    private static final int REGISTER_SUCCESS = 3;

    private Activity mActivity = null;

    private int mHeight;

    private int mWidth;

    private int mOrientation;

    private String mAppkey;

    private boolean mUseUpdate = false;

    private InitCallBack mInitCallBack;

    /** mActivity的跟视图 **/
    private ViewGroup mDecorView;

    /** loading界面 */
    private View mView;

    /** 初始化开始时间 **/
    private long mStartTime;

    /** 初始化成功时间 **/
    private long mEndTime;

    /** 初始化最短等待时间 **/
    private final int mWaitTime = 3500;

    private Integer mGameid;

    private String mPartner;

    private String mReferer;

    public static final String UPDATETYPE = "updatetype";

    public static final String SDKVERSION = "sdkVersion";

    public static final String GAMEVERSION = "gameVersion";

    public static final String UPDATEURL = "updateurl";

    public static final String UPDATECONTENT = "updatecontent";

    private boolean mNewDevice;// 一键注册获取的用户名是否是新设备

    private String mEasyRegisterPassport;// 一键注册获取的用户名

    private static boolean mInited = false;

    private JSONObject mUpdateInfo;

    private final String RELATIONSHIPS = "0";// 0标示新设备，非0标示已注册的账号

    static final String INTENT_TYPE = "intent_type";

    static final String INTENT_LOGOUT = "intent_logout";

    static final String INTENT_UPDATE = "intent_update";

    public static final int NO_UPDATE = 0;

    public static final int MUST_UPDATE = 1;

    public static final int ORDINARY_UPDATE = 2;

    private UserModel mUserModel;

    private LogoutCallBack mLogoutCallBack;

    private LoginCallBack mLoginCallBack;

    private RechargeCallBack mRechargeCallBack;

    private String mRoleName;

    private String mServerName;

    private String mLevel;

    private String mOutOrderid;

    private String mPext;

    private Float mMoney;// 定额充值金额

    private LoginDialog mLoginDialog;

    /**
     * @Title: CHPayManager
     * @Description:( 将构造函数私有化)
     * @throws
     */
    private SplusPayManager() {

    }

    /**
     * @Title: getInstance(获取实例)
     * @author xiaoming.yuan
     * @data 2014-2-26 下午2:30:02
     * @return CHPayManager 返回类型
     */
    public static SplusPayManager getInstance() {

        if (mManager == null) {
            synchronized (lock) {
                if (mManager == null) {
                    mManager = new SplusPayManager();
                }
            }
        }
        return mManager;
    }

    @Override
    public void setInitBean(InitBean bean) {
    }

    /**
     * Title: init Description:
     *
     * @param mBaseActivity
     * @param appkey
     * @param callBack
     * @param useupdate
     * @param screenType
     * @see com.android.splus.sdk.apiinterface.IPayManager#init(android.app.Activity,
     *      java.lang.String, com.android.splus.sdk.apiinterface.InitCallBack,
     *      boolean, int)
     */
    @Override
    public void init(Activity activity, String appkey, InitCallBack initCallBack,
            boolean useUpdate, int orientation) {
        mStartTime = DateUtil.getCurrentTimestamp();
        if (initCallBack == null) {
            LogHelper.i(TAG, "InitCallBack参数不能为空");
            return;
        }
        if (activity == null) {
            LogHelper.i(TAG, "Activity参数不能为空");
            initCallBack.initFaile("Activity参数不能为空");
            return;
        } else {
            if (!(activity instanceof Activity)) {
                LogHelper.i(TAG, "参数Activity不是一个Activity的实例");
                initCallBack.initFaile("参数Activity不是一个Activity的实例");
                return;
            }
        }
        this.mActivity = activity;
        // 加入Loading页面
        mDecorView = (ViewGroup) activity.getWindow().getDecorView();
        mView = new SplashPage(getContext());
        mDecorView.addView(mView);
        mView.requestFocus();
        if (TextUtils.isEmpty(appkey)) {
            LogHelper.i(TAG, "appkey参数不能为空");
            initCallBack.initFaile("appkey参数不能为空");
            return;
        }
        // 初始化获取屏幕高度和宽度
        mHeight = Phoneuitl.getHpixels(activity);
        mWidth = Phoneuitl.getWpixels(activity);
        mOrientation = orientation;
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏方向，高<宽
            if (mHeight > mWidth) {
                int temp = mWidth;
                mWidth = mHeight;
                mHeight = temp;
            }
        } else {
            // 竖屏方向 高>宽
            if (mHeight < mWidth) {
                int temp = mWidth;
                mWidth = mHeight;
                mHeight = temp;
            }
        }
        this.mAppkey = appkey;
        this.mInitCallBack = initCallBack;
        this.mUseUpdate = useUpdate;
        this.mLoginDialog = null;
        SharedPreferencesHelper.getInstance().setLoginStatusPreferences(activity, appkey, false);
        getGameConfig(activity, initCallBack);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 初始化账号
                AccountObservable.getInstance().init(AppUtil.getAllUserData());
                // initImageLoader(getContext());
                payManagerHandler.sendEmptyMessage(INIT_START);
            }
        }).start();

    }

    /**
     * @Title: getGameInfo(获取游戏配置文件信息)
     * @author xiaoming.yuan
     * @data 2013-12-16 上午10:43:58 void 返回类型
     */
    private void getGameConfig(Activity activity, InitCallBack initCallBack) {

        AssetManager assetManager = activity.getAssets();
        if (assetManager == null) {
            LogHelper.i(TAG, "Activity参数不能为空");
            if (initCallBack != null) {
                initCallBack.initFaile("Activity参数不能为空");
            }
            return;
        }
        try {
            // 获取文件输入流
            InputStream in = assetManager.open(CONFIG_FILENAME);
            Properties prop = new Properties();
            prop.load(in);
            mGameid = Integer.parseInt(prop.getProperty("gameid").trim());
            mPartner = prop.getProperty("partner").trim();
            mReferer = prop.getProperty("referer").trim();
        } catch (IOException e) {
            LogHelper.i(TAG, e.getLocalizedMessage(), e);
            LogHelper.i(TAG, CONFIG_FILENAME+"文件配置错误");
            initCallBack.initFaile(CONFIG_FILENAME+"文件配置错误");

        }

    }

    /**
     * 初始化完成回调
     */
    private Handler payManagerHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case INIT_SUCCESS:
                    removeInitView();
                    if (mInitCallBack != null) {
                        mInitCallBack.initSuccess("初始化成功！", mUpdateInfo);
                    }
                    break;
                case INIT_START:
                    repeatInit();
                    break;

                case LOGIN_SUCCESS:
                    showLoginDialog();
                    break;

                case REGISTER_SUCCESS:
                    showRegisterDialog();
                    break;
                default:
                    break;
            }

        };
    };

    /**
     * @Title: repeatInit(无网络时提示)
     * @author xiaoming.yuan
     * @data 2013-12-16 上午10:45:44 void 返回类型
     */
    private synchronized void repeatInit() {

        if (!NetWorkUtil.isNetworkAvailable(getContext())) {

            ProgressDialogUtil.showInfoDialog(getContext(), "提示", "当前网络不稳定,请检查您的网络设置！", 0,
                    new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            repeatInit();

                        }
                    }, "确定", null, null, false);

        } else {
            requestInit();
        }

    }

    /**
     * @Title: requestInit(SDK初始化请求)
     * @author xiaoming.yuan
     * @data 2013-12-16 上午10:45:00 void 返回类型
     */
    private synchronized void requestInit() {
        long time = DateUtil.getUnixTime();
        String mac = Phoneuitl.getLocalMacAddress(getContext());
        String imei = Phoneuitl.getIMEI(getContext());
        String keyString = mGameid + mReferer + mPartner + mac + imei + time;
        String sign = MD5Util.getMd5toLowerCase(keyString + mAppkey);
        ActiveModel mActiveMode = new ActiveModel(mGameid, mPartner, mReferer, mac, imei, mWidth,
                mHeight, Phoneuitl.MODE, Phoneuitl.OS, Phoneuitl.OSVER, time, sign);
        NetHttpUtil.getDataFromServerPOST(getContext(), new RequestModel(Constant.ACTIVE_URL,
                mActiveMode, new ActiveParser()), onActiveCallBack);
        // LogHelper.i("requestInit",
        // NetHttpUtil.hashMapTOgetParams(mActiveMode, Constant.ACTIVE_URL));

    }

    protected DataCallback<HashMap<String, Object>> onActiveCallBack = new DataCallback<HashMap<String, Object>>() {

        @Override
        public void callbackSuccess(HashMap<String, Object> paramObject) {
            ActiveData activeData = (ActiveData) paramObject.get(BaseParser.DATA);
            if (activeData != null) {
                String deviceno = activeData.getDeviceno();

                if (!TextUtils.isEmpty(deviceno)) {
                    SharedPreferencesHelper.getInstance().setDevicenoPreferences(getContext(),
                            deviceno);
                }
                mEasyRegisterPassport = activeData.getPassport();

                String relationships = activeData.getRelationships();
                if (relationships.equals(RELATIONSHIPS)) {
                    // 0标示新设备，非0标示已注册的账号
                    mNewDevice = true;
                } else {
                    mNewDevice = false;
                }
                mUpdateInfo = activeData.getUpdateInfo();
                setInitStatus(true);
                if (mUseUpdate) {
                    Intent intent = new Intent(getContext(), UpdateActivity.class);
                    intent.putExtra(INTENT_TYPE, INTENT_UPDATE);
                    getContext().startActivity(intent);
                } else {
                    onInitSuccess();
                }
            } else {
                String msg = paramObject.get(BaseParser.MSG).toString();
                LogHelper.i(TAG, msg);
                ToastUtil.showToast(mActivity, msg);
                repeatInit();
            }
        }

        @Override
        public void callbackError(String error) {

            LogHelper.i(TAG, error);
            ToastUtil.showToast(mActivity, error);
            ProgressDialogUtil.showInfoDialog(getContext(), "提示", error, 0, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    repeatInit();

                }
            }, "确定", null, null, false);

        }
    };

    /**
     * 初始化成功
     *
     * @author xiaoming.yuan
     * @date 2013年10月9日 下午9:20:06
     */
    void onInitSuccess() {
        mEndTime = System.currentTimeMillis();
        long temp = mEndTime - mStartTime;
        temp = mWaitTime - temp;
        temp = temp <= 0 ? 0 : temp;
        payManagerHandler.sendEmptyMessageDelayed(INIT_SUCCESS, temp);
    }

    /**
     * @author xiaoming.yuan
     * @param contentView the contentView to set
     */
    private void removeInitView() {
        try {
            mDecorView.removeView(mView);
        } catch (Exception e) {
            LogHelper.i(TAG, e.toString());
        }

    }

    /**
     * Title: login Description:
     *
     * @param activity
     * @param loginCallBack
     * @see com.android.splus.sdk.apiinterface.IPayManager#login(android.app.Activity,
     *      com.android.splus.sdk.apiinterface.LoginCallBack)
     */
    @Override
    public void login(Activity activity, LoginCallBack loginCallBack) {

        if (loginCallBack == null) {
            LogHelper.i(TAG, "LoginCallBack参数不能为空");
            return;
        }
        if (activity == null) {
            LogHelper.i(TAG, "Activity参数不能为空");
            loginCallBack.loginFaile("参数Activity参数不能为空");
            return;
        } else {
            if (!(activity instanceof Activity)) {
                LogHelper.i(TAG, "参数Activity不是一个Activity的实例");
                loginCallBack.loginFaile("参数Activity不是一个Activity的实例");
                return;
            }
        }
        this.mActivity = activity;
        this.mLoginCallBack = loginCallBack;
        new Thread() {
            public void run() {
                while (!mInited) {
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        LogHelper.i(TAG, e.getLocalizedMessage());
                        repeatInit();
                    }

                }
                if (mInited) {
                    // 初始化成功，自动选择一键注册界面或者是注册界面。
                    if (AccountObservable.getInstance().getAllUserData().size() > 0) {
                        payManagerHandler.sendEmptyMessage(LOGIN_SUCCESS);
                    } else {
                        if (mNewDevice) {
                            payManagerHandler.sendEmptyMessage(REGISTER_SUCCESS);
                        } else {
                            payManagerHandler.sendEmptyMessage(LOGIN_SUCCESS);
                        }
                    }

                } else {
                    repeatInit();
                }
            };
        }.start();

    }

    /**
     * @Title: showLoginDialog(显示登录框)
     * @author xiaoming.yuan
     * @data 2013-12-16 上午10:37:25 void 返回类型
     */
    private void showLoginDialog() {
        if (mLoginDialog != null && mLoginDialog.isShowing()) {
            return;
        } else {
            synchronized (lock1) {
                if (getContext() != null) {
                    mLoginDialog = new LoginDialog(getContext(), LoginView.class.getSimpleName());
                    mLoginDialog.show();
                }
            }

        }
    }

    /**
     * @Title: showRegisterDialog(显示注册对话框)
     * @author xiaoming.yuan
     * @data 2013-12-16 上午10:37:51 void 返回类型
     */
    private void showRegisterDialog() {
        if (mLoginDialog != null && mLoginDialog.isShowing()) {
            return;
        } else {
            synchronized (lock1) {
                if (getContext() != null) {
                    mLoginDialog = new LoginDialog(getContext(), RegisterView.class.getSimpleName());
                    mLoginDialog.show();
                }
            }

        }

    }

    /**
     * Title: recharge Description:
     *
     * @param activity
     * @param serverName
     * @param roleName
     * @param outOrderid
     * @param pext
     * @param rechargeCallBack
     * @see com.android.splus.sdk.apiinterface.IPayManager#recharge(android.app.Activity,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String,
     *      com.android.splus.sdk.apiinterface.RechargeCallBack)
     */
    @Override
    public void recharge(Activity activity, String serverName, String roleName, String outOrderid,
            String pext, RechargeCallBack rechargeCallBack) {
        if (rechargeCallBack == null) {
            LogHelper.i(TAG, "RechargeCallBack参数不能为空");
            return;
        }
        if (activity == null) {
            LogHelper.i(TAG, "Activity参数不能为空");
            rechargeCallBack.rechargeFaile("参数Activity参数不能为空");
            return;
        } else {
            if (!(activity instanceof Activity)) {
                LogHelper.i(TAG, "参数Activity不是一个Activity的实例");
                rechargeCallBack.rechargeFaile("参数Activity不是一个Activity的实例");
                return;
            }
        }
        if (!SharedPreferencesHelper.getInstance()
                .getLoginStatusPreferences(mActivity, getAppkey())) {
            String msg = "您还没有登录或登录失效，不能进行充值，以免造成损失，请重新登陆。";
            LogHelper.i(TAG, msg);
            ToastUtil.showToast(activity, msg);
            rechargeCallBack.rechargeFaile(msg);
            return;
        }
        this.mActivity = activity;
        this.mRechargeCallBack = rechargeCallBack;
        this.mOutOrderid = outOrderid;
        this.mPext = pext;
        this.mServerName = serverName;
        this.mRoleName = roleName;
        this.mMoney = null;
        Intent intent = new Intent(activity, RechargeSelectActivity.class);
        intent.putExtra(RechargeSelectActivity.class.getName(), Constant.RECHARGE_BY_NO_QUATO);
        activity.startActivity(intent);

    }

    /**
     * Title: rechargeByQuota Description:
     *
     * @param activity
     * @param serverName
     * @param roleName
     * @param outOrderid
     * @param pext
     * @param money
     * @param rechargeCallBack
     * @see com.android.splus.sdk.apiinterface.IPayManager#rechargeByQuota(android.app.Activity,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String, java.lang.Float,
     *      com.android.splus.sdk.apiinterface.RechargeCallBack)
     */
    @Override
    public void rechargeByQuota(Activity activity, String serverName, String roleName,
            String outOrderid, String pext, Float money, RechargeCallBack rechargeCallBack) {
        if (rechargeCallBack == null) {
            LogHelper.i(TAG, "RechargeCallBack参数不能为空");
            return;
        }
        if (activity == null) {
            LogHelper.i(TAG, "Activity参数不能为空");
            rechargeCallBack.rechargeFaile("参数Activity参数不能为空");
            return;
        } else {
            if (!(activity instanceof Activity)) {
                LogHelper.i(TAG, "参数Activity不是一个Activity的实例");
                rechargeCallBack.rechargeFaile("参数Activity不是一个Activity的实例");
                return;
            }
        }
        if (!SharedPreferencesHelper.getInstance()
                .getLoginStatusPreferences(mActivity, getAppkey())) {
            String msg = "您还没有登录或登录失效，不能进行充值，以免造成损失，请重新登陆。";
            LogHelper.i(TAG, msg);
            ToastUtil.showToast(activity, msg);
            rechargeCallBack.rechargeFaile(msg);
            return;
        }
        if (null == money) {
            money = 0.0f;
        }
        if (money >= 1000000) {
            ToastUtil.showToast(activity, "请输入金额小于1000000元");
            return;

        }

        if (money >= 0.01 && money < 10) {
            // 测试账号
            if (!getUserModel().getPassport().equalsIgnoreCase(Constant.TEST_PASSPROT)) {
                ToastUtil.showToast(activity, "请输入金额大于10元");
                return;
            }
        }
        if (money > 0 && money < 0.01) {
            // 测试账号
            if (getUserModel().getPassport().equalsIgnoreCase(Constant.TEST_PASSPROT)) {
                ToastUtil.showToast(activity, "请输入金额大于0.01元");
                return;
            } else {
                ToastUtil.showToast(activity, "请输入金额大于10元");
                return;
            }
        }

        this.mActivity = activity;
        this.mRechargeCallBack = rechargeCallBack;
        this.mOutOrderid = outOrderid;
        this.mPext = pext;
        this.mServerName = serverName;
        this.mRoleName = roleName;
        this.mMoney = money;
        Intent intent = new Intent(activity, RechargeSelectActivity.class);
        intent.putExtra(RechargeSelectActivity.class.getName(), Constant.RECHARGE_BY_QUATO);
        activity.startActivity(intent);

    }

    /**
     * Title: exitSDK Description:
     *
     * @see com.android.splus.sdk.apiinterface.IPayManager#exitSDK()
     */
    @Override
    public void exitSDK() {
        ExitAppUtils.getInstance().exit();
    }

    /**
     * Title: exitGame Description:
     *
     * @param context
     * @see com.android.splus.sdk.apiinterface.IPayManager#exitGame(android.content.Context)
     */
    @Override
    public void exitGame(Context context) {
        if (context == null) {
            return;
        }
        ExitAppUtils.getInstance().exit();
        // 如果上面没关闭好自己，或者没填写任何东西，就我们sdk来关闭进程。
        CommonUtil.killSDK(context);
    }

    /**
     * Title: logout Description:
     *
     * @param logoutCallBack
     * @see com.android.splus.sdk.apiinterface.IPayManager#logout(com.android.splus.sdk.apiinterface.LogoutCallBack)
     */
    @Override
    public void logout(Activity activity, LogoutCallBack logoutCallBack) {
        if (logoutCallBack == null) {
            LogHelper.i(TAG, "LogoutCallBack参数不能为空");
        }
        this.mLogoutCallBack = logoutCallBack;
        if (activity == null) {
            LogHelper.i(TAG, "Activity参数不能为空");
            return;
        } else {
            if (!(activity instanceof Activity)) {
                LogHelper.i(TAG, "参数Activity不是一个Activity的实例");
                return;
            }
        }
        this.mActivity = activity;
        SharedPreferencesHelper.getInstance().setLoginStatusPreferences(activity,
                SplusPayManager.getInstance().getAppkey(), false);

        ExitAppUtils.getInstance().exit();
        mLogoutCallBack.logoutCallBack();
    }

    /**
     * Title: setDBUG Description:
     *
     * @param logDbug
     * @see com.android.splus.sdk.apiinterface.IPayManager#setDBUG(boolean)
     */
    @Override
    public void setDBUG(boolean logDbug) {
        LogHelper.setLOGDBUG(logDbug);
    }

    /**
     * Title: enterUserCenter (个人中心) Description:
     *
     * @param mActivity
     * @param mLogoutCallBack
     * @see com.android.splus.sdk.apiinterface.IPayManager#enterUserCenter(android.app.Activity,
     *      com.android.splus.sdk.apiinterface.LogoutCallBack)
     */
    @Override
    public void enterUserCenter(Activity activity, LogoutCallBack mLogoutCallBack) {

        if (mLogoutCallBack == null) {
            LogHelper.i(TAG, "LogoutCallBack参数不能为空");
            // return;
        }
        if (activity == null) {
            LogHelper.i(TAG, "Activity参数不能为空");
            return;
        } else {
            if (!(activity instanceof Activity)) {
                LogHelper.i(TAG, "参数Activity不是一个Activity的实例");
                return;
            }
        }
        if (!SharedPreferencesHelper.getInstance().getLoginStatusPreferences(activity, getAppkey())) {
            String msg = "您还没有登录或者登陆失效，不能个人中心，请重新登陆。";
            LogHelper.i(TAG, msg);
            ToastUtil.showToast(activity, msg);
            return;
        }
        this.mLogoutCallBack = mLogoutCallBack;
        this.mActivity = activity;
        activity.startActivity(new Intent(activity, PersonActivity.class));

    }

    /**
     * Title: sendGameStatics 统计游戏数据 Description:
     *
     * @param mActivity
     * @param roleName
     * @param level
     * @param serverName
     * @see com.android.splus.sdk.apiinterface.IPayManager#sendGameStatics(android.app.Activity,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void sendGameStatics(Activity activity, String roleName, String level, String serverName) {

        if (activity == null) {
            LogHelper.i(TAG, "Activity参数不能为空");
            return;
        }
        if (!SharedPreferencesHelper.getInstance()
                .getLoginStatusPreferences(mActivity, getAppkey())) {
            String msg = "您还没有登录或者登陆失效，请重新登陆。";
            LogHelper.i(TAG, "统计区服角色等级失败" + msg);
            return;
        }

        if (TextUtils.isEmpty(roleName)) {
            roleName = getRoleName();
        }
        if (TextUtils.isEmpty(level)) {
            level = getLevel();
        }

        if (TextUtils.isEmpty(serverName)) {
            serverName = getServerName();
        }

        this.mLevel = level;
        String passport;
        String uid;
        if (mUserModel == null) {
            passport = AppUtil.getUserData().getUserName();
            uid = AppUtil.getUserData().getUid().toString();
        } else {
            passport = mUserModel.getUserName();
            uid = mUserModel.getUid().toString();
        }
        long time = DateUtil.getUnixTime();
        String deviceno = SharedPreferencesHelper.getInstance().getdevicenoPreferences(activity);
        String keyString = getGameid() + deviceno + getReferer() + getPartner() + uid + passport
                + serverName + roleName + level + time + getAppkey();
        GameStatisticsModel mGameStatisticsModel = new GameStatisticsModel(getGameid(), deviceno,
                getPartner(), getReferer(), uid, passport, roleName, level, serverName, time,
                MD5Util.getMd5toLowerCase(keyString));
        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(Constant.STATISTICS_GAME_URL,
                mGameStatisticsModel, new LoginParser()), new DataCallback<JSONObject>() {
            @Override
            public void callbackSuccess(JSONObject paramObject) {

                try {
                    if (paramObject != null && paramObject.getInt("code") == 1) {
                        // LogHelper.i(TAG, paramObject.toString());
                        LogHelper.i(TAG, "统计区服角色等级成功");
                    } else {
                        LogHelper.i(TAG, "统计区服角色等级失败");
                    }
                } catch (JSONException e) {
                    LogHelper.i(TAG, e.getLocalizedMessage());
                    LogHelper.i(TAG, "统计区服角色等级失败");
                }
            }

            @Override
            public void callbackError(String error) {
                LogHelper.i(TAG, error);
                LogHelper.i(TAG, "统计区服角色等级失败");
            }
        });

    }

    /**
     * Title: enterBBS BBS Description:
     *
     * @param mActivity
     * @see com.android.splus.sdk.apiinterface.IPayManager#enterBBS(android.app.Activity)
     */
    @Override
    public void enterBBS(Activity activity) {

        if (activity == null) {
            LogHelper.i(TAG, "Activity参数不能为空");
            return;
        } else {
            if (!(activity instanceof Activity)) {
                LogHelper.i(TAG, "参数Activity不是一个Activity的实例");
                return;
            }
        }
        if (!SharedPreferencesHelper.getInstance()
                .getLoginStatusPreferences(mActivity, getAppkey())) {
            String msg = "您还没有登录或者登陆失效，不能进行论坛，请重新登陆。";
            LogHelper.i(TAG, msg);
            ToastUtil.showToast(activity, msg);
            return;
        }
        this.mActivity = activity;
        Intent intent = new Intent(getContext(), PersonActivity.class);
        intent.putExtra(PersonActivity.INTENT_TYPE, PersonActivity.INTENT_FORUM);
        activity.startActivity(intent);
    }

    /**
     * Title: onResume Description:
     *
     * @param mActivity
     * @see com.android.splus.sdk.apiinterface.IPayManager#onResume(android.app.Activity)
     */
    @Override
    public void onResume(Activity activity) {
        if (SharedPreferencesHelper.getInstance().getOnlineTimeEedPreferences(mActivity) != 0) {
            sendOnLineTimeStatics(activity, getRoleName(), getLevel(), getServerName());
        }
        LogHelper.i(TAG, "统计在线时长开始------ " + "onResume");
        SharedPreferencesHelper.getInstance().setOnlineTimeStartPreferences(mActivity,
                DateUtil.getUnixTime());

    }

    /**
     * Title: onPause Description:
     *
     * @param mActivity
     * @see com.android.splus.sdk.apiinterface.IPayManager#onPause(android.app.Activity)
     */
    @Override
    public void onPause(Activity activity) {
        LogHelper.i(TAG, "统计在线时长结束------ " + "onPause");
        sendOnLineTimeStatics(activity, getRoleName(), getLevel(), getServerName());
    }

    /**
     * Title: sendOnLineTimeStatics 统计在线时长 Description:
     *
     * @param mActivity
     * @param rolename
     * @param serverName
     * @param level
     * @see com.canhe.android.sdk.apinterface.IPayManager#sendOnLineTimeStatics(android.app.Activity,
     *      java.lang.String, java.lang.String, java.lang.String)
     */

    void sendOnLineTimeStatics(final Activity activity, String roleName, String level,
            String serverName) {
        if (activity == null) {
            LogHelper.i(TAG, "Activity参数不能为空");
            return;
        }
        if (!SharedPreferencesHelper.getInstance()
                .getLoginStatusPreferences(mActivity, getAppkey())) {
            String msg = "您还没有登录或者登陆失效，请重新登陆。";
            LogHelper.i(TAG, "统计在线时长失败------ " + msg);
            return;
        }
        String passport;
        String uid;
        if (mUserModel == null) {
            passport = AppUtil.getUserData().getUserName();
            uid = AppUtil.getUserData().getUid().toString();
        } else {
            passport = mUserModel.getUserName();
            uid = mUserModel.getUid().toString();
        }
        final long time = DateUtil.getUnixTime();
        long onLineTimeEnd = time;
        long onLineTimeStart = SharedPreferencesHelper.getInstance().getOnlineTimeStartPreferences(
                mActivity);
        long onLineTime = onLineTimeEnd - onLineTimeStart;
        String deviceno = SharedPreferencesHelper.getInstance().getdevicenoPreferences(mActivity);
        String keyString = getGameid() + deviceno + getReferer() + getPartner() + uid + passport
                + time + getAppkey();
        GameStatisticsModel mGameStatisticsModel = new GameStatisticsModel(getGameid(), deviceno,
                getPartner(), getReferer(), uid, passport, roleName, level, serverName,
                onLineTimeStart, onLineTimeEnd, onLineTime, time,
                MD5Util.getMd5toLowerCase(keyString));
        // LogHelper.i(TAG, NetHttpUtil.hashMapTOgetParams(mGameStatisticsModel,
        // Constant.STATISTICS_ONLINETIME_URL));

        NetHttpUtil.getDataFromServerPOST(mActivity, new RequestModel(
                Constant.STATISTICS_ONLINETIME_URL, mGameStatisticsModel, new LoginParser()),
                new DataCallback<JSONObject>() {
                    @Override
                    public void callbackSuccess(JSONObject paramObject) {
                        // LogHelper.i(TAG,"paramObject---"+paramObject.toString());
                        try {
                            if (paramObject != null && paramObject.getInt("code") == 1) {
                                SharedPreferencesHelper.getInstance().setOnlineTimeEndPreferences(
                                        mActivity, 0L);
                                LogHelper.i(TAG, "统计在线时长成功");
                            } else {
                                LogHelper.i(TAG, "统计在线时失败");
                            }
                        } catch (JSONException e) {
                            SharedPreferencesHelper.getInstance().setOnlineTimeEndPreferences(
                                    mActivity, time);
                            LogHelper.i(TAG, e.getLocalizedMessage());
                            LogHelper.i(TAG, "统计在线时失败");
                        }
                    }

                    @Override
                    public void callbackError(String error) {
                        SharedPreferencesHelper.getInstance().setOnlineTimeEndPreferences(
                                mActivity, time);
                        LogHelper.i(TAG, error);
                        LogHelper.i(TAG, "统计在线时失败");
                    }
                });

    }

    /**
     * @Title: getContext(获取上下文)
     * @author xiaoming.yuan
     * @data 2014-2-26 下午4:14:27
     * @return Activity 返回类型
     */
    Activity getContext() {
        return this.mActivity;
    }

    String getAppkey() {
        return this.mAppkey == null ? "" : this.mAppkey;
    }

    Integer getGameid() {
        return this.mGameid == null ? 0 : this.mGameid;
    }

    String getPartner() {
        return this.mPartner == null ? "" : this.mPartner;
    }

    String getReferer() {
        return this.mReferer == null ? "" : this.mReferer;
    }

    JSONObject getUpdateInfo() {
        return this.mUpdateInfo;
    }

    /**
     * getter method
     *
     * @return the mHeight
     */

    int getHeight() {
        return this.mHeight;
    }

    /**
     * getter method
     *
     * @return the mWidth
     */

    int getWidth() {
        return this.mWidth;
    }

    /**
     * @Title: getOrientation(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2014-3-31 下午12:13:02
     * @return int 返回类型
     */
    int getOrientation() {
        return this.mOrientation;
    }

    /**
     * getter method
     *
     * @return the mUserModel
     */

    UserModel getUserModel() {
        return this.mUserModel;
    }

    /**
     * setter method
     *
     * @param mUserData
     */

    void setUserModel(UserModel userModel) {
        this.mUserModel = userModel;
        if (userModel != null) {
            // 保存用户数据
            AppUtil.saveDatatoFile(userModel);

        }
    }

    /**
     * easyRegisterUserName
     *
     * @return the easyRegisterUserName
     * @since 1.0.0 xilin.chen
     */

    String getEasyRegisterPassport() {
        return mEasyRegisterPassport;
    }

    /**
     * isNewDevice
     *
     * @return the mNewDevice 设备的是否绑定 @ xiaoming.yuan
     */

    boolean isNewDevice() {
        return mNewDevice;
    }

    /**
     * @param isNewDevice the isNewDevice to set
     */
    void setNewDevice(boolean isNewDevice) {
        this.mNewDevice = isNewDevice;
    }

    void setInitStatus(boolean status) {
        mInited = status;
    }

    LoginCallBack getLoginCallBack() {
        return mLoginCallBack;
    }

    RechargeCallBack getRechargeCallBack() {
        return mRechargeCallBack;
    }

    InitCallBack getInitCallBack() {
        return mInitCallBack;
    }

    LogoutCallBack getLogoutCallBack() {
        return this.mLogoutCallBack;
    }

    String getRoleName() {
        return this.mRoleName == null ? "" : this.mRoleName;
    }

    String getServerName() {
        return this.mServerName == null ? "" : this.mServerName;
    }

    String getLevel() {

        return this.mLevel == null ? "" : this.mLevel;

    }

    String getOutorderid() {
        return this.mOutOrderid == null ? "" : this.mOutOrderid;
    }

    String getPext() {
        return this.mPext == null ? "" : this.mPext;
    }

    /**
     * getter method
     *
     * @return the money
     */

    Float getMoney() {
        if (this.mMoney == null) {
            BigDecimal b = new BigDecimal(0.00);
            return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        } else {
            BigDecimal b = new BigDecimal(this.mMoney);
            return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }
    }

    @Override
    public FloatToolBar creatFloatButton(Activity mActivity, boolean showlasttime,
            FloatToolBarAlign align, float position) {
        return FloatToolBar.getFloatToolBar(mActivity, showlasttime, align, position);

    }

}
