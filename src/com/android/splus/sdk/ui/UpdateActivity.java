
 /**
 * @Title: UpdateActivity.java
 * @Package com.android.cansh.sdk.ui
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-2-27 上午10:29:51
 * @version V1.0
 */

 package com.android.splus.sdk.ui;

import com.android.splus.sdk.manager.ExitAppUtils;
import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.progressDialog.ProgressDialogUtil;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Window;


/**
 * @ClassName: UpdateActivity
 * @author xiaoming.yuan
 * @date 2014-2-27 上午10:29:51
 */

public class UpdateActivity extends BaseActivity {
    private static final String TAG = "UpdateActivity";

    private int mUptype = 0;
    private  static final String UPTYPE="uptype";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

    }

    /**
     * Title: findViewById
     * Description:
     * @see com.android.splus.sdk.ui.BaseActivity#findViewById()
     */
    @Override
    protected void findViewById() {
    }

    /**
     * Title: loadViewLayout
     * Description:
     * @see com.android.splus.sdk.ui.BaseActivity#loadViewLayout()
     */
    @Override
    protected void loadViewLayout() {
    }

    /**
     * Title: processLogic
     * Description:
     * @see com.android.splus.sdk.ui.BaseActivity#processLogic()
     */
    @Override
    protected void processLogic() {
        Intent intent = getIntent();
        String type = intent.getStringExtra(SplusPayManager.INTENT_TYPE);
        if (SplusPayManager.INTENT_LOGOUT.equals(type)) {
            finish();
            ExitAppUtils.getInstance().exit();
            SplusPayManager.getInstance().destroy();
            intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (SplusPayManager.INTENT_UPDATE.equals(type)) {
            String localAppVer = "1.0.0";
            try {
                PackageInfo info = mBaseActivity.getPackageManager().getPackageInfo(mBaseActivity.getPackageName(), 0);
                localAppVer = info.versionName;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            JSONObject mJSONObject = SplusPayManager.getInstance().getUpdateInfo();
            try {
                mUptype = mJSONObject.optInt(SplusPayManager.UPDATETYPE);
                String remoteAppVer = mJSONObject.optString(SplusPayManager.GAMEVERSION);
                String updatecontent = mJSONObject.optString(SplusPayManager.UPDATECONTENT);
                String remoteSDKVer = mJSONObject.optString(SplusPayManager.SDKVERSION);
                update(localAppVer, remoteAppVer, SplusPayManager.SDK_VERSION, remoteSDKVer, mUptype,
                        updatecontent);
            } catch (Exception e) {
                LogHelper.i(TAG, e.getLocalizedMessage());
                finish(true);
            }
        }
    }

    /**
     * Title: setListener
     * Description:
     * @see com.android.splus.sdk.ui.BaseActivity#setListener()
     */
    @Override
    protected void setListener() {
    }

    /**
     * 比较游戏版本号和游戏SDK版本升级
     *
     * @author xiaoming.yuan
     * @date 2013年10月9日 下午2:57:22
     * @param localAppVer 本地游戏包版本
     * @param remoteAppVer 远程游戏包版本
     * @param localSDKVer 本地游戏SDK版本
     * @param remoteSDKVer 远程游戏SDK版本
     * @param updatetype 是否强制更新
     * @param updatecontent 更新内容
     */
    private void update(String localAppVer, String remoteAppVer, String localSDKVer,
            String remoteSDKVer, int updatetype, String updatecontent) {
        if (CommonUtil.compareVersion(localAppVer.trim(), remoteAppVer.trim())
                || (localAppVer.trim().equals(remoteAppVer.trim()) && CommonUtil.compareVersion(
                        localSDKVer.trim(), remoteSDKVer.trim()))) {
            switch (updatetype) {
                case SplusPayManager.MUST_UPDATE:
                    ProgressDialogUtil.showInfoDialog(UpdateActivity.this, "版本更新", updatecontent, 0,
                            mUpdateListener, "更新", null, null, false);
                    break;
                case SplusPayManager.NO_UPDATE:
                    finish(true);
                    break;
                case SplusPayManager.ORDINARY_UPDATE:
                    ProgressDialogUtil.showInfoDialog(UpdateActivity.this, "版本更新", updatecontent, 0,
                            mUpdateListener, "更新", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    finish(true);
                                }
                            }, "取消", false);
                    break;
                default:
                    finish(true);
                    break;
            }
        } else {
            finish(true);
        }

    }

    private DialogInterface.OnClickListener mUpdateListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish(false);
            DownLoadActivity.CALLINSTALL = false;
            Intent intent = new Intent(mBaseActivity, DownLoadActivity.class);
            intent.putExtra(UPTYPE, mUptype);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mBaseActivity.startActivity(intent);
        }
    };

    public void finish(boolean show) {
        if (show) {
            SplusPayManager.getInstance().onInitSuccess();
        }
        super.finish();
    }
}

