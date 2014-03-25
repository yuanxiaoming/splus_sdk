/**
 * @Title: GetCodeData.java
 * @Package: com.sanqi.android.sdk.model
 * @Description: TODO(用一句话描述该文件做什么)
 * Copyright: Copyright (c) 2013
 * Company: 上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013年10月11日 上午9:15:20
 * @version 1.0
 */

package com.android.splus.sdk.model;

import org.json.JSONObject;

/**
 * @ClassName:GetCodeData
 * @author xiaoming.yuan
 * @date 2013年10月11日 上午9:15:20
 */
public class GetCodeModel extends BaseModel {


    /**
     * @Fields serialVersionUID :（用一句话描述这个变量表示什么）
     */

    private static final long serialVersionUID = 1L;

    public static final String UID = "uid";

    private Integer uid;

    public static final String SERVERNAME = "serverName";

    private String serverName;

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

    public static final String PHONE = "phone";

    private String phone;


    public GetCodeModel(Integer uid,  String serverName, Integer gameid, String sign,
            Long time, String deviceno, String partner,  String referer,
            String passport, String phone) {
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
        this.put(ACTION, "getcode");
        this.put(PHONE, phone == null ? "" : phone);
    }


    /*
     * (non-Javadoc)
     * @see com.sanqi.android.sdk.model.BaseData#initMap()
     */
    @Override
    protected void initMap() {}

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
     * @return the debug
     */
    public String getDebug() {
        return (String) get(DEBUG);
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

    /**
     * @return the phone
     */
    public String getPhone() {
        return (String) get(PHONE);
    }

    /**
     * @param phone the phone to set
     */
    public void setNewp(String phone) {
        this.phone = phone;
        this.put(PHONE, phone == null ? "" : phone);
    }
}
