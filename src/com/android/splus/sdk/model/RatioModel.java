
 /**
 * @Title: RatioModel.java
 * @Package com.android.splus.sdk.model
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-3-11 下午9:29:11
 * @version V1.0
 */

 package com.android.splus.sdk.model;


/**
 * @ClassName: RatioModel
 * @author xiaoming.yuan
 * @date 2014-3-11 下午9:29:11
 */

public class RatioModel extends BaseModel {
    private static final String TAG = "RatioModel";
    private static final long serialVersionUID = 1L;

    public  static final String GAMEID="gameid";
    private  Integer gameid;

    public static final String PAYWAY="payway";
    private  String payway ;


    public static final String TIME="time";
    private  Long time;;

    public static final String SIGN="sign";
    private  String sign;

    public static final String PAY_WAY_RATIO_KEY="**yxm~!!!OOxx";


    public RatioModel(Integer gameid, String payway, Long time, String sign) {
        super("");
        this.put(GAMEID, gameid == null? 0 : gameid);
        this.put(PAYWAY, payway == null ? "" : payway);
        this.put(TIME, time == null ? 0 : time);
        this.put(SIGN, sign == null ? "" : sign);


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
     * @param gameid
     *            the gameid to set
     */

    public void setGameid(Integer gameid) {
        this.put(GAMEID, gameid == null ? 0 : gameid);

    }


    /**
     * getter method
     * @return the payway
     */

    public String getPayway() {
        return get(PAYWAY).toString();
    }

    /**
     * setter method
     * @param payway
     * the payway to set
     */

    public void setPayway(String payway) {
        this.put(PAYWAY, payway == null ? "" : payway);


    }

    /**
     * getter method
     * @return the time
     */

    public Long getTime() {
        return (Long) get(TIME);
    }

    /**
     * setter method
     * @param time
     * the time to set
     */

    public void setTime(Long time) {
        this.put(TIME, time == null? 0 : time);

    }

    /**
     * getter method
     * @return the sign
     */

    public String getSign() {
        return get(SIGN).toString();
    }

    /**
     * setter method
     * @param sign
     * the sign to set
     */

    public void setSign(String sign) {
        this.put(SIGN, sign == null ? "" : sign);

    }

    /**
     * Title: initMap
     * Description:
     * @see BaseData#initMap()
     */
    @Override
    protected void initMap() {


    }


}

