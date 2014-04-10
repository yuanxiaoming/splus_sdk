package com.android.splus.sdk.data;

import android.text.TextUtils;

import java.util.HashMap;

/**
 *
 * @ClassName: UserInfoData
 * @author xiaoming.yuan
 * @date 2014-2-19 上午10:57:04
 */
public class UserInfoData extends HashMap<String, Object> {

    private static final long serialVersionUID = 4232299934204933563L;

    public static final String REALNAME = "realname";

    private String realname;

    public static final String GENDERTYPE = "gendertype";

    private String gendertype;

    public static final String IDCARD = "idcard";

    private String idcard;

    public static final String QQ = "qq";

    private String qq;

    public UserInfoData(String realname, String gendertype, String idcard, String qq) {
        this.put(REALNAME, TextUtils.isEmpty(realname) == true ? "" : realname);
        this.put(GENDERTYPE,  TextUtils.isEmpty(gendertype) == true ? "" : gendertype);
        this.put(IDCARD, TextUtils.isEmpty(idcard) == true ? "" : idcard);
        this.put(QQ, TextUtils.isEmpty(qq) == true ? "" : qq);
    }



    public String getRealname() {
        return (String)get(REALNAME);
    }

    public void setRealname(String realname) {
        this.realname = realname;
        this.put(REALNAME, realname == null ? "" : realname);
    }

    public String getGendertype() {
        return (String) get(GENDERTYPE);
    }

    public void setGendertype(String gendertype) {
        this.gendertype = gendertype;
        this.put(GENDERTYPE, gendertype == null ? "" : gendertype);
    }

    public String getIdcard() {
        return (String) get(IDCARD);
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
        this.put(IDCARD, idcard == null ? "" : idcard);
    }

    public String getQq() {
        return (String) get(QQ);
    }

    public void setQq(String qq) {
        this.qq = qq;
        this.put(QQ, qq == null ? "" : qq);
    }

}
