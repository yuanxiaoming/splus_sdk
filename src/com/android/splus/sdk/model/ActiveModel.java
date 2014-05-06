
package com.android.splus.sdk.model;

import java.util.Locale;

/**
 * Created with IntelliJ IDEA. Copyright: Copyright (c) 2013 User: xiaoming.yuan
 * Date: 13-9-17 Time: 上午11:02 To change this template use File | Settings |
 * File Templates.
 */
public class ActiveModel extends BaseModel {

    private static final long serialVersionUID = 1L;

    /** 游戏ID **/
    public static final String GAMEID = "gameid";

    private Integer gameid;

    /** 联运商 **/
    public static final String PARTNER = "partner";

    private String partner;

    /** 渠道号 **/
    public static final String REFERER = "referer";

    private String referer;

    /** Mac地址 **/
    public static final String MAC = "mac";

    private String mac;

    /** 手机一般17位imei **/
    public static final String IMEI = "imei";

    private String imei;

    /** 分辨率宽 **/
    public static final String WPIXELS = "wpixels";

    private Integer wpixels;

    /** 分辨率高 **/
    public static final String HPIXELS = "hpixels";

    private Integer hpixels;

    /** 型号(nexus 4) **/
    public static final String MODE = "mode";

    private String mode;

    /** 操作系统:(android/ios) **/
    public static final String OS = "os";

    private String os;

    /** 系统版本 **/
    public static final String OSVER = "osver";

    private String osver;

    /** 请求时间戳 **/
    public static final String TIME = "time";

    private Long time;

    /** 加密验证 **/
    public static final String SIGN = "sign";

    private String sign;

    public ActiveModel(Integer gameid, String partner, String referer, String mac, String imei, Integer wpixels, Integer hpixels, String mode, String os, String osver, Long time, String sign) {
        super("");
        this.put(GAMEID, gameid == null ? 0 : gameid);
        this.put(PARTNER, partner == null ? "" : partner);
        this.put(REFERER, referer == null ? "" : referer);
        this.put(MAC, mac == null ? "" : mac);
        this.put(IMEI, imei == null ? "" : imei);
        this.put(WPIXELS, wpixels == null ? 0 : wpixels);
        this.put(HPIXELS, hpixels == null ? 0 : hpixels);
        this.put(MODE, mode == null ? "" : mode);
        this.put(OS, os == null ? "" : os);
        this.put(OSVER, osver == null ? "" : osver);
        this.put(TIME, time == null ? 0 : time);
        this.put(SIGN, sign == null ? "" : sign.toLowerCase(Locale.getDefault()));

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

    public String geReferer() {
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
     * @return the mac
     */

    public String getMac() {
        return get(MAC).toString();
    }

    /**
     * setter method
     * 
     * @param mac the mac to set
     */

    public void setMac(String mac) {
        this.put(MAC, mac == null ? "" : mac);

    }

    /**
     * getter method
     * 
     * @return the imei
     */

    public String getImei() {
        return get(IMEI).toString();

    }

    /**
     * setter method
     * 
     * @param imei the imei to set
     */

    public void setImei(String imei) {
        this.put(IMEI, imei == null ? "" : imei);

    }

    /**
     * getter method
     * 
     * @return the wpixels
     */

    public Integer getWpixels() {
        return (Integer) get(GAMEID);
    }

    /**
     * setter method
     * 
     * @param wpixels the wpixels to set
     */

    public void setWpixels(Integer wpixels) {
        this.put(WPIXELS, wpixels == null ? 0 : wpixels);

    }

    /**
     * getter method
     * 
     * @return the hpixels
     */

    public Integer getHpixels() {
        return (Integer) get(HPIXELS);

    }

    /**
     * setter method
     * 
     * @param hpixels the hpixels to set
     */

    public void setHpixels(Integer hpixels) {
        this.put(HPIXELS, hpixels == null ? 0 : hpixels);

    }

    /**
     * getter method
     * 
     * @return the mode
     */

    public String getMode() {
        return get(MODE).toString();
    }

    /**
     * setter method
     * 
     * @param mode the mode to set
     */

    public void setMode(String mode) {
        this.put(MODE, mode == null ? "" : mode);

    }

    /**
     * getter method
     * 
     * @return the os
     */

    public String getOs() {
        return get(OS).toString();
    }

    /**
     * setter method
     * 
     * @param os the os to set
     */

    public void setOs(String os) {
        this.put(OS, os == null ? "" : os);

    }

    /**
     * getter method
     * 
     * @return the osver
     */

    public String getOsver() {
        return get(OSVER).toString();
    }

    /**
     * setter method
     * 
     * @param osver the osver to set
     */

    public void setOsver(String osver) {
        this.put(OSVER, osver);
        this.put(OSVER, osver == null ? "" : osver);

    }

    /**
     * getter method
     * 
     * @return the time
     */

    public Integer getTime() {
        return (Integer) get(TIME);
    }

    /**
     * setter method
     * 
     * @param time the time to set
     */

    public void setTime(Integer time) {
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
        this.put(SIGN, sign == null ? "" : sign.toLowerCase(Locale.getDefault()));
    }

    /**
     * Title: initMap Description:
     * 
     * @see com.BaseModel.android.sdk.model.BaseData#initMap()
     */
    @Override
    protected void initMap() {

    }

}
