/**
 * @Title: DownLoadActivity.java
 * @Package com.android.cansh.sdk.ui
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-2-27 上午10:50:37
 * @version V1.0
 */

package com.android.splus.sdk.ui;

import com.android.splus.sdk.download.DownloadTask;
import com.android.splus.sdk.download.DownloadTaskListener;
import com.android.splus.sdk.utils.CommonUtil;
import com.android.splus.sdk.utils.file.AppUtil;
import com.android.splus.sdk.utils.http.NetWorkUtil;
import com.android.splus.sdk.utils.progressDialog.ProgressDialogUtil;
import com.android.splus.sdk.utils.r.KR;
import com.android.splus.sdk.utils.r.ResourceUtil;
import com.android.splus.sdk.utils.toast.ToastUtil;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.io.File;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.UUID;

/**
 * @ClassName: DownLoadActivity
 * @author xiaoming.yuan
 * @date 2014-2-27 上午10:50:37
 */

public class DownLoadActivity extends BaseActivity {
    private static final String TAG = "DownLoadActivity";

    private ProgressBar mPBar;

    private TextView mText;

    private JSONObject mUpdateInfo;

    private String mDownloadUrl = "";

    private String mDownloadDir = "";

    private NotificationManager mNotificationManager = null;

    private RemoteViews mNotifyView = null;

    private Notification mNotification = new Notification();

    private boolean mInFront = true;

    private int mMaxLength = 100;

    static boolean CALLINSTALL = true;

    private int mIcon = 0;

    private String mAppName = "";

    private DownloadTask mDownloadTasks;

    private boolean mKillSelf = false;

    private long mPercent = 0;

    /**
     * Title: findViewById Description:
     *
     * @see com.canhe.android.sdk.ui.BaseActivity#findViewById()
     */
    @Override
    protected void findViewById() {

        mPBar = (ProgressBar) findViewById(ResourceUtil.getId(this, KR.id.splus_download_pbar));
        mText = (TextView) findViewById(ResourceUtil.getId(this, KR.id.splus_download_txt));
        mNotifyView = new RemoteViews(getPackageName(), ResourceUtil.getLayoutId(this,
                KR.layout.splus_download_notify));
    }

    /**
     * Title: loadViewLayout Description:
     *
     * @see com.canhe.android.sdk.ui.BaseActivity#loadViewLayout()
     */
    @Override
    protected void loadViewLayout() {

        setContentView(ResourceUtil.getLayoutId(this, KR.layout.splus_download_activity));
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // 清除下载完成通知
        mNotificationManager.cancel(ResourceUtil.getLayoutId(this, KR.layout.splus_download_activity));
    }

    /**
     * Title: processLogic Description:
     *
     * @see com.canhe.android.sdk.ui.BaseActivity#processLogic()
     */
    @Override
    protected void processLogic() {

        mText.setText("正在连接网络……");
        ApplicationInfo appi = getApplication().getApplicationInfo();
        mIcon = appi.icon;
        mAppName = getString(appi.labelRes);
        Intent intent = new Intent(this, DownLoadActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.contentView = mNotifyView;
        mNotification.contentIntent = pIntent;
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
        // 通知的图标必须设置(其他属性为可选设置),否则通知无法显示
        mNotification.icon = mIcon;
        mNotifyView.setImageViewResource(ResourceUtil.getId(this, KR.id.splus_download_notify_ic),
                mIcon);// 起一个线程用来更新progress
        mNotifyView.setTextViewText(ResourceUtil.getId(this, KR.id.splus_download_notify_app_name),
                mAppName);
        String networkType = NetWorkUtil.getNetworkType(this);
        if (NetWorkUtil.NetworkType.UNKNOWN.equals(networkType)) {
            ProgressDialogUtil.showInfoDialog(this, "更新提示", "无网络连接，无法下载！", null,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CommonUtil.killSDK(DownLoadActivity.this);
                        }
                    }, "退出", null, null, false);
            return;
        }
        if (NetWorkUtil.NetworkType.NET_3G.equals(networkType)) {
            ProgressDialogUtil.showInfoDialog(this, "更新提示", "当前为3G网络，是否进行下载？", null,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startDownload();

                        }
                    }, "下载", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            CommonUtil.killSDK(DownLoadActivity.this);
                        }
                    }, "取消", false);
        }
        if (NetWorkUtil.NetworkType.NET_2G.equals(networkType)) {
            ProgressDialogUtil.showInfoDialog(this, "更新提示", "当前为2G网络，是否进行下载？", null,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startDownload();

                        }
                    }, "下载", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            CommonUtil.killSDK(DownLoadActivity.this);
                        }
                    }, "取消", false);
        }
        if (NetWorkUtil.NetworkType.WIFI.equals(networkType)) {
            ProgressDialogUtil.showInfoDialog(this, "更新提示", "当前为WIFI网络，是否进行下载？", null,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startDownload();

                        }
                    }, "下载", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            CommonUtil.killSDK(DownLoadActivity.this);
                        }
                    }, "取消", false);
        }

    }

    /**
     * Title: setListener Description:
     *
     * @see com.canhe.android.sdk.ui.BaseActivity#setListener()
     */
    @Override
    protected void setListener() {

    }

    /**
     * 下载监听
     */
    private DownloadTaskListener downloadTaskListener = new DownloadTaskListener() {

        @Override
        public void updateProcess(DownloadTask mgr) {
            updateDownload(mgr);
        }

        @Override
        public void preDownload() {
        }

        @Override
        public void finishDownload(DownloadTask mgr) {
            File file = new File(new File(mDownloadDir), getFileNameFromUrl());
            finishNotify(file.getAbsolutePath());
            showInstallAPP(file.getAbsolutePath());
        }

        @Override
        public void errorDownload(int error) {
            String title = null;
            switch (error) {
                case DownloadTask.ERROR_SD_NO_MEMORY:

                    title = "手机内存储卡容量不够，请清理内存";
                    break;
                case DownloadTask.ERROR_BLOCK_INTERNET:

                    title = "网络不稳定或异常";
                    break;
                case DownloadTask.ERROR_UNKONW:

                    title = "服务器繁忙或异常";
                    break;
            }
            ProgressDialogUtil.showInfoDialog(DownLoadActivity.this, "更新提示", title, null,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startDownload();
                        }
                    }, "重新下载", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            CommonUtil.killSDK(DownLoadActivity.this);
                        }
                    }, "退出", true);
        }
    };

    /**
     * 更新进度
     *
     * @author xiaoming.yuan
     * @date 2013年10月8日 下午2:08:43
     * @param mgr
     */
    public void updateDownload(DownloadTask mgr) {
        mPBar.setProgress((int) mgr.getDownloadPercent());
        String txt = "" + (int) mgr.getDownloadPercent() + "%" + " " + mgr.getDownloadSpeed()
                + "kbps" + " " + size(mgr.getDownloadSize()) + "/" + size(mgr.getTotalSize());
        mText.setText(txt);
        if (!mInFront) {
            if (mgr.getDownloadPercent() - mPercent >= 1) {
                mPercent = mgr.getDownloadPercent();
                showNotify(size(mgr.getDownloadSize()) + "/" + size(mgr.getTotalSize()),
                        (int) mgr.getDownloadPercent());
            }
        } else {
            hideNotify();
        }

    }

    /**
     * 文件大小转换
     *
     * @author xiaoming.yuan
     * @date 2013年10月8日 下午1:05:56
     * @param size
     * @return
     */
    private String size(long size) {
        if (size / (1024 * 1024) > 0) {
            float tmpSize = (float) (size) / (float) (1024 * 1024);
            DecimalFormat df = new DecimalFormat("#.##");
            return "" + df.format(tmpSize) + "MB";
        } else if (size / 1024 > 0) {
            return "" + (size / (1024)) + "KB";
        } else
            return "" + size + "B";
    }

    /**
     * 获取文件名
     *
     * @author xiaoming.yuan
     * @date 2013年10月8日 下午12:02:52
     * @return
     */
    private String getFileNameFromUrl() {
        // 通过 ‘？’ 和 ‘/’ 判断文件名
        int index = mDownloadUrl.lastIndexOf('?');
        String filename;
        if (index > 1) {
            filename = mDownloadUrl.substring(mDownloadUrl.lastIndexOf('/') + 1, index);
        } else {
            filename = mDownloadUrl.substring(mDownloadUrl.lastIndexOf('/') + 1);
        }

        if (filename == null || "".equals(filename.trim())) {// 如果获取不到文件名称
            filename = UUID.randomUUID() + ".apk";// 默认取一个文件名
        }
        return filename;
    }

    /**
     * 开始下载
     *
     * @author xiaoming.yuan
     * @date 2013年10月8日 上午11:43:26
     */
    private synchronized void startDownload() {
        mText.setVisibility(View.VISIBLE);
        mPBar.setVisibility(View.VISIBLE);
        mUpdateInfo = CHPayManager.getInstance().getUpdateInfo();
        try {
            mDownloadUrl = mUpdateInfo.optString(CHPayManager.UPDATEURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDownloadDir = AppUtil.getDownsPath();
        if (TextUtils.isEmpty(mDownloadDir)) {
            ToastUtil.showToast(this, "无外置内存卡，创建下载目录失败，无法进行下载！");
            CommonUtil.killSDK(DownLoadActivity.this);
            return;
        }
        if (TextUtils.isEmpty(mDownloadUrl)) {
            ToastUtil.showToast(this, "下载地址为空，无法进行下载！");
            CommonUtil.killSDK(DownLoadActivity.this);
            return;
        }

        File file = new File(mDownloadDir + getFileNameFromUrl());
        if (file.exists())
            file.delete();
        try {
            mDownloadTasks = new DownloadTask(this, mDownloadUrl, mDownloadDir,
                    downloadTaskListener);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mDownloadTasks.execute();

    }

    /**
     * 停止下载
     *
     * @author xiaoming.yuan
     * @date 2013年10月8日 下午12:10:34
     */
    private synchronized void stopDownload() {
        File file = new File(mDownloadDir + getFileNameFromUrl());
        if (file.exists())
            file.delete();

        if (mDownloadTasks != null) {
            // tasks.pause();
            mDownloadTasks.cancel(true);
        }

        mDownloadTasks = null;
    }

    /**
     * @Title: showInstallAPP
     * @Description: 安装APP应用
     * @param @param cachePath 设定文件
     * @return void 返回类型
     * @throws
     * @date 2013-6-4 下午4:12:26
     */
    private void showInstallAPP(String cachePath) {
        // 清除下载完成通知
        mNotificationManager.cancel(ResourceUtil.getLayoutId(this, KR.layout.splus_download_activity));

        if (mKillSelf) {
            return;
        }

        CALLINSTALL = true;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + cachePath),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /**
     * Title: onKeyUp Description:
     *
     * @param keyCode
     * @param event
     * @return
     * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (CALLINSTALL) {
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ProgressDialogUtil.showInfoDialog(this, "更新提示", "是否取消下载", null,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 清除下载完成通知
                            stopDownload();
                            mNotificationManager.cancel(ResourceUtil.getLayoutId(
                                    DownLoadActivity.this, KR.layout.splus_download_notify));
                            mNotificationManager.cancel(ResourceUtil.getLayoutId(
                                    DownLoadActivity.this, KR.layout.splus_download_activity));
                            mKillSelf = true;

                            CommonUtil.killSDK(DownLoadActivity.this);

                        }
                    }, "确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }, "取消", true);
            return true;
        }
        return super.onKeyUp(keyCode, event);

    }

    /**
     * @Title: showNotify(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2013年9月4日 下午8:03:31
     * @param context
     * @param progress void 返回类型
     */
    private void showNotify(String context, int progress) {
        if (mNotificationManager != null) {
            mNotifyView.setProgressBar(ResourceUtil.getId(this, KR.id.splus_download_notify_pb),
                    mMaxLength, progress, false);
            mNotifyView.setTextViewText(ResourceUtil.getId(this, KR.id.splus_download_notify_tv),
                    context);
            mNotificationManager.notify(
                    ResourceUtil.getLayoutId(this, KR.layout.splus_download_notify), mNotification);// 关键部分，如果你不重新更新通知，进度条是不会更新的
        }
    }

    /**
     * @Title: hideNotify(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2013年9月4日 下午8:06:27 void 返回类型
     */
    private void hideNotify() {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(ResourceUtil
                    .getLayoutId(this, KR.layout.splus_download_notify));// 清除通知
        }
    }

    private void finishNotify(String cachePath) {
        hideNotify();
        if (mKillSelf) {
            return;
        }
        if (mNotificationManager != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + cachePath),
                    "application/vnd.android.package-archive");
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            mNotification.contentIntent = pIntent;

            mNotifyView.setImageViewResource(ResourceUtil.getId(this, KR.id.splus_download_notify_ic),
                    mIcon);// 起一个线程用来更新progress
            mNotifyView.setProgressBar(ResourceUtil.getId(this, KR.id.splus_download_notify_pb),
                    mMaxLength, mMaxLength, false);
            mNotifyView.setTextViewText(ResourceUtil.getId(this, KR.id.splus_download_notify_tv),
                    "下载已完成，点击安装");
            mNotificationManager.notify(
                    ResourceUtil.getLayoutId(this, KR.layout.splus_download_activity), mNotification);// 关键部分，如果你不重新更新通知，进度条是不会更新的
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
        mInFront = true;
        if (CALLINSTALL) {
            mText.setVisibility(View.GONE);
            mPBar.setVisibility(View.GONE);
            CommonUtil.killSDK(this);
        }
    }

    /**
     * Title: onPause Description:
     *
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        mInFront = false;
    }

    /**
     * Title: onDestroy Description:
     *
     * @see com.canhe.android.sdk.ui.BaseActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {

        super.onDestroy();
        stopDownload();
        hideNotify();
    }
}
