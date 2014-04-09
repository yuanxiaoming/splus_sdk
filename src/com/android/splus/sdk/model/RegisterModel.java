/**
 * @Title: RegisterModel.java
 * @Package com.sanqi.sdk.model
 * Copyright: Copyright (c) 2013
 * Company:上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013-8-7 下午5:29:52
 * @version V1.0
 */

package com.android.splus.sdk.model;

/**
 * @ClassName: RegisterModel
 * @author xiaoming.yuan
 * @date 2013-8-7 下午5:29:52
 */

public class RegisterModel extends BaseModel {

    /**
     * @Fields serialVersionUID : （用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = 1L;

    private static final String TAG = "RegisterModel";

    public static final String GAMEID = "gameid";

    private int gameid;

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

    public static final String FASTREG = "fastreg";

    private String fastreg;

    public RegisterModel(Integer gameid, String deviceno, String partner, String referer,
            String passport, String password, Long time, String sign,
            String fastreg) {
        super("");
        this.put(GAMEID, gameid == null ? 0 : gameid);
        this.put(DEVICENO, deviceno == null ? "" : deviceno);
        this.put(PARTNER, partner == null ? "" : partner);
        this.put(REFERER, referer == null ? "" : referer);
        this.put(PASSPORT, passport == null ? "" : passport);
        this.put(PASSWORD, password == null ? "" : password);
        this.put(TIME, time == null ? 0 : time);
        this.put(SIGN, sign == null ? "" : sign.toLowerCase());
        this.put(FASTREG, fastreg == null ? "0" : fastreg);
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
     * fastreg
     *
     * @return the fastreg
     * @since 1.0.0 xilin.chen
     */

    public String getFastreg() {
        return get(FASTREG).toString();
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
     * @param fastreg the fastreg to set
     */
    public void setFastreg(String fastreg) {
        this.put(FASTREG, fastreg == null ? "1" : fastreg);
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
