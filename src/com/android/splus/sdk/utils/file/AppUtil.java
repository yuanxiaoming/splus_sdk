
package com.android.splus.sdk.utils.file;

import com.android.splus.sdk.model.BaseModel;
import com.android.splus.sdk.model.UserModel;
import com.android.splus.sdk.utils.md5.MD5Util;

import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具类集合
 *
 * @author xiaoming.yuan
 */
public class AppUtil {

    /**
     * SD卡目录
     */
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath();

    /**
     * 应用根目录
     */
    public static final String ROOT_PATH = SDCARD_PATH + "/Android/data/splus/";

    /**
     * 用户数据目录
     */
    public static final String USER_DATA_PATH = ROOT_PATH + "/UserData/";

    /**
     * 下载目录
     */
    public static final String DOWNLOAD_PATH =  ROOT_PATH + "/downs/";

    /**
     * 配置目录
     */
    public static final String CONFIG_PATH = ROOT_PATH + "/Config/";

    /**
     * 配置文件路径
     */
    public static final String CONFIG_FILE = CONFIG_PATH + "config.cfg";

    /**
     * @Title: getDownsPath(获取下载目录)
     * @author xiaoming.yuan
     * @data 2013年9月4日 下午3:04:38
     * @return String 返回类型
     */
    public static String getDownsPath() {
        if (!FileUtil.isSDCardAvailable()) {
            return "";
        }
        if (FileUtil.createFolder(DOWNLOAD_PATH, FileUtil.MODE_UNCOVER)) {
            return DOWNLOAD_PATH;
        }
        return "";
    }

    /**
     * 保存数据
     *
     * @param baseData
     */
    public static void saveDatatoFile(BaseModel baseData) {
        if (baseData == null) {
            return;
        }
        if (!FileUtil.isSDCardAvailable()) {
            return;
        }
        String filePath = baseData.getPath();
        if (!FileUtil.isExist(filePath)) {
            FileUtil.createFile(filePath, FileUtil.MODE_COVER);
        }
        byte[] data = baseData.getBytes();
        // 先加密
        encrypt(data);
        FileUtil.rewriteData(filePath, data);
        // 保存 用户路径
        saveLatestUserDataFilePath(filePath);
    }

    /**
     * 删除数据
     */
    public static void deleteDataFile(BaseModel baseData) {
        if (baseData == null) {
            return;
        }
        if (!FileUtil.isSDCardAvailable()) {
            return;
        }
        String filePath = baseData.getPath();
        if (FileUtil.isExist(filePath)) {
            FileUtil.deleteFile(filePath);
        }
    }

    /**
     * 获取数据
     *
     * @return
     */
    public static UserModel getUserData() {
        if (!FileUtil.isSDCardAvailable()) {
            return null;
        }
        String latestUserDataFilePath = getLatestUserDataFilePath();
        if (FileUtil.isExist(latestUserDataFilePath)) {
            byte[] data = FileUtil.getFileData(latestUserDataFilePath);
            if (data != null && data.length > 0) {
                // 先解密
                unEncrypt(data);
                String json = new String(data);
                if (!TextUtils.isEmpty(json)) {
                    return new UserModel(json);
                }
            }
        } else {
            ArrayList<?> list = getDatasFromFilePath(USER_DATA_PATH, UserModel.class);
            if (list != null && list.size() > 0) {
                return (UserModel) list.get(0);
            }
        }
        return null;
    }

    /***
     * @Title: getUserData(这里用一句话描述这个方法的作用)
     * @author xiaoming.yuan
     * @data 2013-9-26 下午5:57:33
     * @return UserData 返回类型
     */
    public static ArrayList<UserModel> getAllUserData() {
        return (ArrayList<UserModel>) getDatasFromFilePath(USER_DATA_PATH, UserModel.class);
    }

    /**
     * 获取最新登录的用户数据文件路径
     *
     * @return 用户数据文件路径
     */
    public static String getLatestUserDataFilePath() {
        if (!FileUtil.isSDCardAvailable()) {
            return null;
        }
        if (FileUtil.isExist(CONFIG_FILE)) {
            byte[] data = FileUtil.getFileData(CONFIG_FILE);
            if (data != null && data.length > 0) {
                // 先解密
                unEncrypt(data);
                String json = new String(data);
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject jsonObj = new JSONObject(json);
                        String userDataFilePath = jsonObj.getString("userDataFilePath");
                        return userDataFilePath;
                    } catch (Exception e) {
                    }
                }
            }
        }
        return null;
    }

    /**
     * 保存最新使用的用户数据文件路径
     *
     * @param userDataFilePath 用户数据文件路径
     */
    public static void saveLatestUserDataFilePath(String userDataFilePath) {
        if (TextUtils.isEmpty(userDataFilePath)) {
            return;
        }
        if (!FileUtil.isSDCardAvailable()) {
            return;
        }
        if (!FileUtil.isExist(CONFIG_FILE)) {
            FileUtil.createFile(CONFIG_FILE, FileUtil.MODE_COVER);
        }
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userDataFilePath", userDataFilePath);
            byte[] data = jsonObj.toString().getBytes();
            // 先加密
            encrypt(data);
            FileUtil.rewriteData(CONFIG_FILE, data);
        } catch (Exception e) {
        }
    }

    private final static byte[] encrypt_key = new byte[] {
            0x1a, 0x2b, 0x3c, 0x4d, 0x5e, 0x6f
    };

    /**
     * 加密
     *
     * @param source
     */
    public static void encrypt(byte[] source) {
        if (source != null && source.length > 0) {
            int length = source.length;
            int keyLen = encrypt_key.length;
            for (int i = 0; i < length; i++) {
                source[i] ^= encrypt_key[i % keyLen];
            }
        }
    }

    /**
     * 解密
     *
     * @param source
     */
    public static void unEncrypt(byte[] source) {
        if (source != null && source.length > 0) {
            int length = source.length;
            int keyLen = encrypt_key.length;
            for (int i = 0; i < length; i++) {
                source[i] ^= encrypt_key[i % keyLen];
            }
        }
    }

    /**
     * 获取路径下的所有数据对象,根据修改时间排序
     * <p>
     * 调用方法:getDatasFromFilePath(USER_DATA_PATH, UserData.class)
     * </p>
     *
     * @param path 路径
     * @param cls 对象类名
     * @return
     */

    public static ArrayList<? extends BaseModel> getDatasFromFilePath(String path,
            Class<? extends BaseModel> cls) {
        /**
         * 读取所有账号
         */
        ArrayList<BaseModel> datas = new ArrayList<BaseModel>();
        if (!FileUtil.isSDCardAvailable()) {
            return datas;
        }
        if (!FileUtil.isExist(path)) {
            return datas;
        }

        File[] files = FileUtil.listFiles(path);
        int len = files.length;
        for (int i = 0; i < len; ++i) {
            for (int j = 0; j < len - i - 1; j++) {
                File temp = null;
                if (files[j].lastModified() < files[j + 1].lastModified()) {
                    temp = files[j + 1];
                    files[j + 1] = files[j];
                    files[j] = temp;
                }
            }

        }
        for (int i = 0; i < len; i++) {
            byte[] data = FileUtil.getFileData(files[i].getAbsolutePath());
            if (data == null || data.length <= 0) {
                continue;
            }
            // 先解密
            unEncrypt(data);
            String json = new String(data);
            if (TextUtils.isEmpty(json)) {
                continue;
            }

            try {
                // 获取cls只有一个参数并且参数为String的构成函数
                Constructor cons = cls.getDeclaredConstructor(new Class[] {
                    String.class
                });
                Object obj = cons.newInstance(json);
                datas.add(cls.cast(obj));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return datas;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return datas;
            } catch (InstantiationException e) {
                e.printStackTrace();
                return datas;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return datas;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return datas;
            }
        }
        return datas;
    }

    /**
     * 根据文件名获取数据类对象
     * <p>
     * 调用方法:getDataFromFile(USER_DATA_PATH, UserData.class)
     * </p>
     *
     * @param fileName 文件名（包含路径）
     * @param cls 对象类名
     * @return
     */
    public static BaseModel getDataFromFile(String fileName, Class<? extends BaseModel> cls) {
        if (!FileUtil.isSDCardAvailable()) {
            return null;
        }
        if (!FileUtil.isExist(fileName)) {
            return null;
        }
        if (!cls.getClass().getSuperclass().equals(BaseModel.class)) {
            return null;
        }
        byte[] data = FileUtil.getFileData(new File(fileName).getAbsolutePath());
        if (data == null || data.length <= 0) {
            return null;
        }
        // 先解密
        unEncrypt(data);
        String json = new String(data);
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        try {
            // 获取cls只有一个参数并且参数为String的构成函数
            Constructor cons = cls.getDeclaredConstructor(new Class[] {
                String.class
            });
            Object obj = cons.newInstance(json);
            return cls.cast(obj);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace(); // To change body of catch statement use File |
                                 // Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace(); // To change body of catch statement use File |
                                 // Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // To change body of catch statement use File |
                                 // Settings | File Templates.
        }
        return null;
    }

//    /**
//     * 启动APK下载
//     *
//     * @param activity
//     * @param packUrl
//     */
//    public static void startDownloadApkService(Activity activity, String gameName,
//            String downloadApkUrl) {
//        Intent intent = new Intent(activity, DownloadApkService.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("gameName", gameName);
//        bundle.putString("downloadApkUrl", downloadApkUrl);
//        intent.putExtras(bundle);
//        activity.startService(intent);
//    }

    /**
     * 获取Apk下载目录
     *
     * @return
     */
    public static String getDownloadApkPath(Context context) {
        String md5Folder = getMd5Folder(context);
        if (!FileUtil.isSDCardAvailable()) {
            return "";
        }
        if (FileUtil.createFolder(DOWNLOAD_PATH + md5Folder, FileUtil.MODE_UNCOVER)) {
            return DOWNLOAD_PATH + md5Folder;
        }
        return "";
    }

    /**
     * 获取md5文件夹名称
     *
     * @param context
     * @return
     */
    public static String getMd5Folder(Context context) {
        return MD5Util.getMd5(context.getPackageName()) + "/";
    }

    /**
     * 启动应用
     *
     * @param context
     * @param packageName 要启动的应用的包名
     */
    public static void runApp(Context context, String packageName) {
        PackageInfo pi;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.setPackage(pi.packageName);
            PackageManager pManager = context.getPackageManager();
            List apps = pManager.queryIntentActivities(resolveIntent, 0);

            ResolveInfo ri = (ResolveInfo) apps.iterator().next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cn = new ComponentName(packageName, className);
                intent.setComponent(cn);
                context.startActivity(intent);
            }
        } catch (NameNotFoundException e) {
        }
    }

    /**
     * 安装应用APK
     *
     * @param cachePath
     */
    public static void installAPP(Context context, String downloadPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + downloadPath),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
