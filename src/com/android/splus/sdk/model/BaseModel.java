
package com.android.splus.sdk.model;

import com.android.splus.sdk.ui.SplusPayManager;
import com.android.splus.sdk.utils.md5.MD5Util;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * @ClassName: BaseData
 * @author xiaoming.yuan
 * @date 2014-2-19 上午10:48:31
 */
public abstract class BaseModel extends HashMap<String, Object> {

    /**
     * @Fields serialVersionUID :（用一句话描述这个变量表示什么）
     */

    private static final long serialVersionUID = -4829792410212552384L;

    /**
     * 保存文件路径
     */
    private String mPath;

    private String mFileName;

    public static final String VERSION = "version";

    /** 开启后会返回服务器加密字符串/耗时/内存 **/
    public static final String DEBUG = "debug";

    private String debug;

    public BaseModel(String jsonObject) {
        this.put(VERSION, SplusPayManager.SDK_VERSION);
        // this.put(DEBUG, "1");
    }

    /**
     * 设置文件名称
     */
    protected void setPath(String path, String fileName) {
        this.mPath = path;
        this.mFileName = fileName;
    }

    /**
     * 变量转换为json字符串
     * 
     * @return
     */
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        JSONObject Json = new JSONObject();
        Iterator<Entry<String, Object>> it = entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                Json.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return Json.toString();
    }

    /**
     * json字符串转化为数组对象
     * 
     * @return
     */
    public byte[] getBytes() {
        String str = toString();
        if (!TextUtils.isEmpty(str)) {
            return str.getBytes();
        }
        return null;
    }

    /**
     * 获取保存文件名
     * 
     * @return
     */
    public String getPath() {
        return this.mPath + MD5Util.getMd5(mFileName).toLowerCase(Locale.getDefault());
    }

    /**
     * 初始化Map
     */
    protected abstract void initMap();
}
