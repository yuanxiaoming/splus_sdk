/**
 *
 */

package com.android.splus.sdk.alipay;

import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.progressDialog.ProgressDialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 检测安全支付服务是否正确安装，如果没有安装进行本地安装，或者下载安装， 检测安全支付服务版本，有新版本时进行下载。
 * 
 * @author xiaoming.yuan
 */
public class MobileSecurePayHelper {

    // 支付宝安全支付服务apk的名称，必须与assets目录下的apk名称一致
    private final String ALIPAY_PLUGIN_NAME = "Alipay_msp_2.5.2_0426.apk";

    /**
     * 支付宝服务下载地址
     */
    public final String ALIPAY_SERVER_URL = "https://msp.alipay.com/x.htm";

    private final String TAG = "MobileSecurePayHelper";

    private ProgressDialog mProgress = null;

    Context mContext = null;

    public MobileSecurePayHelper(Context context) {
        this.mContext = context;
    }

    /**
     * 检测安全支付服务是否安装
     * 
     * @return
     */
    public boolean detectMobile_sp() {
        boolean isMobile_spExist = isMobile_spExist();
        if (!isMobile_spExist) {
            // get the cacheDir.
            // 获取系统缓冲绝对路径 获取/data/data//cache目录
            File cacheDir = mContext.getCacheDir();
            final String cachePath = cacheDir.getAbsolutePath() + "/temp.apk";
            // 捆绑安装
            boolean ret = retrieveApkFromAssets(mContext, ALIPAY_PLUGIN_NAME, cachePath);
            if (ret) {
                mProgress = ProgressDialogUtil.showProgress(mContext, null, "正在检测安全支付服务版本", false, true);
                // 实例新线程检测是否有新版本进行下载
                new Thread(new Runnable() {
                    public void run() {
                        // 检测是否有新的版本。
                        PackageInfo apkInfo = getApkInfo(mContext, cachePath);
                        String newApkdlUrl = checkNewUpdate(apkInfo);
                        // 动态下载
                        if (newApkdlUrl != null) {
                            retrieveApkFromNet(mContext, newApkdlUrl, cachePath);
                        }
                        // 发送结果
                        Message msg = new Message();
                        msg.what = AlixId.RQF_INSTALL_CHECK;
                        msg.obj = cachePath;
                        mHandler.sendMessage(msg);
                    }
                }).start();
            } else {
                mProgress = ProgressDialogUtil.showProgress(mContext, null, "正在检测安全支付服务版本", false, true);
                // 实例新线程检测本地assets文件夹下是否有apk是进行下载
                new Thread(new Runnable() {
                    public void run() {
                        String newApkdlUrl = checkNewUpdate();
                        if (newApkdlUrl != null) {
                            retrieveApkFromNet(mContext, newApkdlUrl, cachePath);
                        }
                        // 发送结果
                        Message msg = new Message();
                        msg.what = AlixId.RQF_INSTALL_CHECK;
                        msg.obj = cachePath;
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        }
        return isMobile_spExist;
    }

    /**
     * 显示确认安装的提示
     * 
     * @param context 上下文环境
     * @param cachePath 安装文件路径
     */
    public void showInstallConfirmDialog(final Context context, final String cachePath) {
        AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
        tDialog.setIcon(android.R.drawable.ic_dialog_info);
        tDialog.setTitle("安装提示");
        tDialog.setMessage("为保证您的交易安全，需要您安装支付宝安全支付服务，才能进行付款。\n\n点击确定，立即安装。");

        tDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 修改apk权限
                chmod("777", cachePath);
                // 安装安全支付服务APK
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse("file://" + cachePath), "application/vnd.android.package-archive");
                context.startActivity(intent);
            }
        });

        tDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        tDialog.show();
    }

    /**
     * 遍历程序列表，判断是否安装安全支付服务
     * 
     * @return
     */
    public boolean isMobile_spExist() {
        PackageManager manager = mContext.getPackageManager();
        List<PackageInfo> pkgList = manager.getInstalledPackages(0);
        for (int i = 0; i < pkgList.size(); i++) {
            PackageInfo pI = pkgList.get(i);
            if (pI.packageName.equalsIgnoreCase("com.alipay.android.app"))
                return true;
        }

        return false;
    }

    /**
     * 安装安全支付服务，安装assets文件夹下的apk
     * 
     * @param context 上下文环境
     * @param fileName apk名称
     * @param path 安装路径
     * @return
     */
    public boolean retrieveApkFromAssets(Context context, String fileName, String path) {
        boolean bRet = false;

        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);

            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }

            fos.close();
            is.close();

            bRet = true;

        } catch (IOException e) {
            e.printStackTrace();
            return bRet;
        }

        return bRet;
    }

    /**
     * 获取未安装的APK信息
     * 
     * @param context
     * @param archiveFilePath APK文件的路径。如：/sdcard/download/XX.apk
     */
    public static PackageInfo getApkInfo(Context context, String archiveFilePath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo apkInfo = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_META_DATA);
        return apkInfo;
    }

    /**
     * 检查是否有新版本，如果有，返回apk下载地址
     * 
     * @param packageInfo {@link android.content.pm.PackageInfo}
     * @return
     */
    public String checkNewUpdate() {
        String url = null;

        try {
            // JSONObject resp = sendCheckNewUpdate(packageInfo.versionName);
            JSONObject resp = sendCheckNewUpdate("1.0.0");
            if (resp.getString("needUpdate").equalsIgnoreCase("true")) {
                url = resp.getString("updateUrl");
            }
            // else ok.
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * 检查是否有新版本，如果有，返回apk下载地址
     * 
     * @param packageInfo {@link android.content.pm.PackageInfo}
     * @return
     */
    public String checkNewUpdate(PackageInfo packageInfo) {
        String url = null;

        try {
            JSONObject resp = sendCheckNewUpdate(packageInfo.versionName);
            // JSONObject resp = sendCheckNewUpdate("1.0.0");
            if (resp.getString("needUpdate").equalsIgnoreCase("true")) {
                url = resp.getString("updateUrl");
            }
            // else ok.
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * 发送当前版本信息，返回是否需要升级 如果需要升级返回更新apk地址
     * 
     * @param versionName 当前版本号
     * @return
     */
    public JSONObject sendCheckNewUpdate(String versionName) {
        JSONObject objResp = null;
        try {
            JSONObject req = new JSONObject();
            req.put(AlixDefine.action, AlixDefine.actionUpdate);

            JSONObject data = new JSONObject();
            data.put(AlixDefine.platform, "android");
            data.put(AlixDefine.VERSION, versionName);
            data.put(AlixDefine.partner, "");

            req.put(AlixDefine.data, data);

            objResp = sendRequest(req.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return objResp;
    }

    /**
     * 发送json数据
     * 
     * @param content
     * @return
     */
    public JSONObject sendRequest(final String content) {
        NetworkManager nM = new NetworkManager(this.mContext);

        //
        JSONObject jsonResponse = null;
        try {
            String response = null;

            synchronized (nM) {
                //
                response = nM.SendAndWaitResponse(content, ALIPAY_SERVER_URL);
            }

            jsonResponse = new JSONObject(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonResponse != null)
            LogHelper.i(TAG, jsonResponse.toString());

        return jsonResponse;
    }

    /**
     * 动态下载apk
     * 
     * @param context 上下文环境
     * @param strurl 下载地址
     * @param filename 文件名称
     * @return
     */
    public boolean retrieveApkFromNet(Context context, String strurl, String filename) {
        boolean bRet = false;

        try {
            NetworkManager nM = new NetworkManager(this.mContext);
            bRet = nM.urlDownloadToFile(context, strurl, filename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bRet;
    }

    // close the progress bar
    void closeProgress() {
        try {
            if (mProgress != null) {
                mProgress.dismiss();
                mProgress = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 此处接收安装检测结果
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case AlixId.RQF_INSTALL_CHECK: {
                        //
                        closeProgress();
                        String cachePath = (String) msg.obj;
                        showInstallConfirmDialog(mContext, cachePath);
                    }
                        break;
                }

                super.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * @Title: chmod(这获取权限)
     * @author xiaoming.yuan
     * @data 2013-8-10 下午3:45:11
     * @param permission 权限
     * @param path 路径
     * @return void 返回类型
     */
    public void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
