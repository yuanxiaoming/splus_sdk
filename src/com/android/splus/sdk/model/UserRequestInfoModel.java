/**
 * @Title: CheckPhoneData.java
 * @Package: com.sanqi.android.sdk.model
 * Copyright: Copyright (c) 2013
 * Company: 上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013年10月11日 下午2:09:41
 * @version 1.0
 */

package com.android.splus.sdk.model;

/**
 * @ClassName: UserRequestInfoData
 * @author xiaoming.yuan
 * @date 2014-2-19 上午10:57:27
 */
public class UserRequestInfoModel extends BaseModel {
    private static final long serialVersionUID = 1L;

    public static final String UID = "uid";

    private Integer uid;

    public static final String SERVERID = "serverId";
    private Integer serverId;

    public static final String ROLEID = "roleId";
    private Integer roleId;

    public static final String SERVERNAME = "serverName";

    private String serverName;

    public static final String ROLENAME = "roleName";

    private String roleName;

    public static final String GAMEID = "gameid";

    private Integer gameid;

    public static final String SIGN = "sign";

    private String sign;

    public static final String TIME = "time";

    private Long time;

    public static final String DEVICENO = "deviceno";

    private String deviceno;

    public static final String PARTNER = "partner";

    private String partner;

    public static final String REFERER = "referer";

    private String referer;

    public static final String PASSPORT = "passport";

    private String passport;

    public static final String ACTION = "action";

    public static final String REALNAME = "realname";

    private String realname;

    public static final String GENDERTYPE = "gendertype";

    private Integer gendertype;

    public static final String IDCARD = "idcard";

    private String idcard;

    public static final String QQ = "qq";

    private String qq;

    // public UserRequestInfoData(String jsonObject) {
    // super(jsonObject);
    // try {
    // JSONObject json = new JSONObject(jsonObject);
    // this.put(UID, uid == null ? "0" : json.optInt(UID));
    // this.put(DEBUG, debug == null ? "" : json.optString(DEBUG));
    // this.put(DSID, dsid == null ? "" : json.optString(DSID));
    // this.put(GAMEID, gameid == null ? 0 : json.optInt(GAMEID));
    // this.put(SIGN, sign == null ? "" : json.optString(SIGN));
    // this.put(DEVICENO, deviceno == null ? "" : json.optString(DEVICENO));
    // this.put(PARTNER, partner == null ? "" : json.optString(PARTNER));
    // this.put(REFERER, referer == null ? "" : json.optString(REFERER));
    // this.put(PASSPORT, passport == null ? "" : json.optString(PASSPORT));
    // this.put(REALNAME, realname == null ? "" : json.optString(REALNAME));
    // this.put(GENDERTYPE, gendertype == null ? "" : json.optInt(GENDERTYPE));
    // this.put(IDCARD, idcard == null ? "" : json.optString(IDCARD));
    // this.put(QQ, qq == null ? "" : json.optString(QQ));
    // } catch (Exception e) {
    // }
    // }

    public UserRequestInfoModel(Integer uid, Integer serverId, Integer roleId, String serverName, String roleName,Integer gameid, String sign, Long time, String deviceno, String partner, String referer, String passport) {
        super("");

        this.put(UID, uid == null ? 0 : uid);
        this.put(SERVERID, serverId == null ? 0 : serverId);
        this.put(SERVERNAME, serverName == null ? "" : serverName);
        this.put(ROLEID, roleId == null ? 0 : roleId);
        this.put(ROLENAME, roleName == null ? "" : roleName);
        this.put(GAMEID, gameid == null ? 0 : gameid);
        this.put(SIGN, sign == null ? "" : sign);
        this.put(TIME, time == null ? 0 : time);
        this.put(DEVICENO, deviceno == null ? "" : deviceno);
        this.put(PARTNER, partner == null ? "" : partner);
        this.put(REFERER, referer == null ? "" : referer);
        this.put(PASSPORT, passport == null ? "" : passport);
        this.put(ACTION, "checkuserinfo");
    }

    public UserRequestInfoModel(Integer uid, String serverName, Integer gameid, String sign, Long time, String deviceno, String partner, String referer, String passport, String realname, Integer gendertype, String idcard, String qq) {
        super("");

        this.put(UID, uid == null ? 0 : uid);
        this.put(SERVERNAME, serverName == null ? "" : serverName);
        this.put(GAMEID, gameid == null ? 0 : gameid);
        this.put(SIGN, sign == null ? "" : sign);
        this.put(TIME, time == null ? 0 : time);
        this.put(DEVICENO, deviceno == null ? "" : deviceno);
        this.put(PARTNER, partner == null ? "" : partner);
        this.put(REFERER, referer == null ? "" : referer);
        this.put(PASSPORT, passport == null ? "" : passport);
        this.put(REALNAME, realname == null ? "" : realname);
        this.put(GENDERTYPE, gendertype == null ? 0 : gendertype);
        this.put(IDCARD, idcard == null ? "" : idcard);
        this.put(QQ, qq == null ? "" : qq);
        this.put(ACTION, "senduserinfo");
    }

    /*
     * (non-Javadoc)
     * @see com.sanqi.android.sdk.model.BaseData#initMap()
     */
    @Override
    protected void initMap() {

    }

    /**
     * @return the uid
     */
    public Integer getUid() {
        return (Integer) get(UID);
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(Integer uid) {
        this.uid = uid;
        this.put(UID, uid == null ? 0 : uid);
    }

    /**
     * @return the gameid
     */
    public Integer getGameid() {
        return (Integer) get(GAMEID);
    }

    /**
     * @param gameid the gameid to set
     */
    public void setGameid(Integer gameid) {
        this.gameid = gameid;
        this.put(GAMEID, gameid == null ? 0 : gameid);
    }

    /**
     * @return the sign
     */
    public String getSign() {
        return (String) get(SIGN);
    }

    /**
     * @param sign the sign to set
     */
    public void setSign(String sign) {
        this.sign = sign;
        this.put(SIGN, sign == null ? "" : sign);
    }

    /**
     * @return the deviceno
     */
    public String getDeviceno() {
        return (String) get(DEVICENO);
    }

    /**
     * @param deviceno the deviceno to set
     */
    public void setDeviceno(String deviceno) {
        this.deviceno = deviceno;
        this.put(DEVICENO, deviceno == null ? "" : deviceno);
    }

    /**
     * @return the partner
     */
    public String getPartner() {
        return (String) get(PARTNER);

    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(String partner) {
        this.partner = partner;
        this.put(PARTNER, partner == null ? "" : partner);
    }

    /**
     * @return the referer
     */
    public String getReferer() {
        return (String) get(REFERER);
    }

    /**
     * @param referer the referer to set
     */
    public void setReferer(String referer) {
        this.referer = referer;
        this.put(REFERER, referer == null ? "" : referer);
    }

    /**
     * @return the passport
     */
    public String getPassport() {
        return (String) get(PASSPORT);
    }

    /**
     * @param passport the passport to set
     */
    public void setPassport(String passport) {
        this.passport = passport;
        this.put(PASSPORT, passport == null ? "" : passport);
    }

    public String getRealname() {
        return (String) get(REALNAME);
    }

    public void setRealname(String realname) {
        this.realname = realname;
        this.put(REALNAME, realname == null ? "" : realname);
    }

    public Integer getGendertype() {
        return (Integer) get(GENDERTYPE);
    }

    public void setGendertype(Integer gendertype) {
        this.gendertype = gendertype;
        this.put(GENDERTYPE, gendertype == null ? 0 : gendertype);
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
