/**
 * @Title: LonginModel.java
 * @Package com.sanqi.sdk.model
 * Copyright: Copyright (c) 2013
 * Company:上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013-8-7 上午10:24:32
 * @version V1.0
 */

package com.android.splus.sdk.model;

/**
 * @ClassName: LonginModel
 * @author xiaoming.yuan
 * @date 2013-8-7 上午10:24:32
 */

public class LoginModel extends BaseModel {

    /**
     * @Fields serialVersionUID
     */

    private static final long serialVersionUID = 1L;

    private static final String TAG = "LonginModel";

    public static final String GAMEID = "gameid";

    private Integer gameid;

    public static final String DEVICENO = "deviceno";

    private String deviceno;

    public static final String PARTNER = "partner";

    private String partner;

    public static final String REFERER = "referer";

    private String referer;

    public static final String PASSPORT = "passport";

    private String passport;

    public static final String PASSWORD = "password";

    private String password;

    public static final String TIME = "time";

    private Long time;

    public static final String SIGN = "sign";

    private String sign;

    public LoginModel(Integer gameid, String deviceno, String partner, String referer, String passport, String password, Long time, String sign) {
        super("");

        this.put(GAMEID, gameid == null ? 0 : gameid);
        this.put(DEVICENO, deviceno == null ? "" : deviceno);
        this.put(PARTNER, partner == null ? "" : partner);
        this.put(REFERER, referer == null ? "" : referer);
        this.put(PASSPORT, passport == null ? "" : passport);
        this.put(PASSWORD, password == null ? "" : password);
        this.put(TIME, time == null ? 0 : time);
        this.put(SIGN, sign == null ? "" : sign.toLowerCase());
    }

    /**
     * getter method
     * 
     * @return the gameid
     */

    public Integer getGameid() {
        return (Integer) get(GAMEID);
    }

    /**
     * setter method
     * 
     * @param gameid the gameid to set
     */

    public void setGameid(Integer gameid) {
        this.put(GAMEID, gameid == null ? 0 : gameid);

    }

    /**
     * getter method
     * 
     * @return the deviceno
     */

    public String getDeviceno() {
        return get(DEVICENO).toString();
    }

    /**
     * setter method
     * 
     * @param deviceno the deviceno to set
     */

    public void setDeviceno(String deviceno) {
        this.put(DEVICENO, deviceno == null ? "" : deviceno);

    }

    /**
     * getter method
     * 
     * @return the partner
     */

    public String getPartner() {
        return get(PARTNER).toString();
    }

    /**
     * setter method
     * 
     * @param otta the partner to set
     */

    public void setPartner(String partner) {
        this.put(PARTNER, partner == null ? "" : partner);

    }

    /**
     * getter method
     * 
     * @return the partner
     */

    public String getReferer() {
        return get(REFERER).toString();

    }

    /**
     * setter method
     * 
     * @param partner the partner to set
     */

    public void setReferer(String referer) {
        this.put(REFERER, referer == null ? "" : referer);

    }

    /**
     * getter method
     * 
     * @return the passport
     */

    public String getPassport() {
        return get(PASSPORT).toString();

    }

    /**
     * setter method
     * 
     * @param passport the passport to set
     */

    public void setPassport(String passport) {
        this.put(PASSPORT, passport == null ? "" : passport);

    }

    /**
     * getter method
     * 
     * @return the password
     */

    public String getPassword() {
        return get(PASSWORD).toString();
    }

    /**
     * setter method
     * 
     * @param password the password to set
     */

    public void setPassword(String password) {
        this.put(PASSWORD, password == null ? "" : password);

    }

    /**
     * getter method
     * 
     * @return the time
     */

    public Long getTime() {
        return (Long) get(TIME);
    }

    /**
     * setter method
     * 
     * @param time the time to set
     */

    public void setTime(Long time) {
        this.put(TIME, time == null ? 0 : time);

    }

    /**
     * getter method
     * 
     * @return the sign
     */

    public String getSign() {
        return get(SIGN).toString();
    }

    /**
     * setter method
     * 
     * @param sign the sign to set
     */

    public void setSign(String sign) {
        this.put(SIGN, sign == null ? "" : sign.toLowerCase());
    }

    /**
     * Title: initMap Description:
     * 
     * @see com.canhe.android.sdk.model.BaseData#initMap()
     */
    @Override
    protected void initMap() {

    }

}
