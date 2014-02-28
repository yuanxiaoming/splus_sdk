
 /**
 * @Title: MD5.java
 * @Package com.android.cansh.sdk.utils.md5
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-2-26 下午2:32:42
 * @version V1.0
 */

 package com.android.splus.sdk.utils.md5;

import java.security.MessageDigest;
import java.util.Locale;

/**
 * @ClassName: MD5
 * @author xiaoming.yuan
 * @date 2014-2-26 下午2:32:42
 */

public class MD5Util {
    private static final String TAG = "MD5";


    /**
     * 获取MD5
     * @param input 字符串
     * @return
     */
    public static String getMd5(String input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input.getBytes());
            return toHexString(md5.digest());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取MD5
     * @param input 字符串
     * @return
     */
    public static String getMd5toLowerCase(String input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input.getBytes());
            return toHexString(md5.digest()).toLowerCase(Locale.getDefault());
        } catch (Exception e) {
            return "";
        }
    }
    /**
     * 获取MD5
     *
     * @param input 字符串
     * @param encode 编码
     * @return
     */
    public static String getMd5(String input, String encode) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input.getBytes(encode));
            return toHexString(md5.digest());
        } catch (Exception e) {
            return "";
        }
    }

    private static String toHexString(byte[] b) throws Exception {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    private static char[] hexChar = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };


}

