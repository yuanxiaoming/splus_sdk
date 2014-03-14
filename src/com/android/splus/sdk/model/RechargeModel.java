/**
 * @Title: AlipayModel.java
 * @Package com.sanqi.sdk.model
 * Copyright: Copyright (c) 2013
 * Company:上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013-8-8 下午2:10:48
 * @version V1.0
 */

package com.android.splus.sdk.model;

/**
 * @ClassName: AlipayModel
 * @author xiaoming.yuan
 * @date 2013-8-8 下午2:10:48
 */

public class RechargeModel extends BaseModel {

    /**
     * @Fields serialVersionUID : （用一句话描述这个变量表示什么）
     */

    private static final long serialVersionUID = 1L;

    private static final String TAG = "RechargeData";

    public static final String GAMEID = "gameid";

    private Integer gameid;

    public static final String SERVERNAME = "serverName";

    private String serverName;

    public static final String DEVICENO = "deviceno";

    private String deviceno;

    public static final String REFERER = "referer";

    private String referer;

    public static final String PARTNER = "partner";

    private String partner;

    public static final String UID = "uid";

    private Integer uid;

    public static final String TYPE = "type";

    private Integer type;

    public static final String PAYWAY = "payway";

    private String payway;

    public static final String MONEY = "money";

    private String money;

    public static final String ROLENAME = "roleName";

    private String roleName;

    public static final String OUTORDERID = "outOrderid";

    private String outOrderid;

    public static final String PEXT = "pext";

    private String pext;

    public static final String TIME = "time";

    private Long time;

    public static final String PASSPORT = "passport";

    private String passport;


    public static final String CARDNUMBER = "cardnumber";

    private String cardnumber;

    public static final String CARDPWD = "cardpwd";

    private String cardpwd;

    public static final String CARDAMT = "cardamt";

    private String cardamt;

    public static final String SIGN = "sign";

    private String sign;

  /**
   *
   * @Title:  RechargeModel
   * @Description:( 网页支付)
   * @param gameid
   * @param serverName
   * @param deviceno
   * @param partner
   * @param referer
   * @param uid
   * @param type
   * @param money
   * @param roleName
   * @param time
   * @param passport
   * @param outOrderid
   * @param pext
   * @param sign
   * @throws
   */

    public RechargeModel(Integer gameid, String serverName, String deviceno, String partner,
            String referer, Integer uid,  Float money,Integer type, String roleName, Long time,
            String passport, String outOrderid, String pext, String sign) {
        super("");
        this.put(GAMEID, gameid == null ? 0 : gameid);
        this.put(SERVERNAME, serverName == null ? "" : serverName);
        this.put(DEVICENO, deviceno == null ? "" : deviceno);
        this.put(PARTNER, partner == null ? "" : partner);
        this.put(REFERER, referer == null ? "" : referer);
        this.put(UID, uid == null ? 0 : uid);
        this.put(MONEY, money == null ? 0.0 : money);
        this.put(TYPE, type == null ? 0 : type);
        this.put(ROLENAME, roleName == null ? "" : roleName);
        this.put(TIME, time == null ? 0 : time);
        this.put(PASSPORT, passport == null ? "" : passport);
        this.put(OUTORDERID, outOrderid == null ? "" : outOrderid);
        this.put(PEXT, pext == null ? "" : pext);
        this.put(SIGN, sign == null ? "" : sign.toLowerCase());
    }


    public RechargeModel(Integer gameid, String serverName, String deviceno, String partner,
            String referer, Integer uid, Float money, Integer type, String payway,String roleName, Long time,
            String passport, String outOrderid, String pext, String sign) {
        super("");
        this.put(GAMEID, gameid == null ? 0 : gameid);
        this.put(SERVERNAME, serverName == null ? "" : serverName);
        this.put(DEVICENO, deviceno == null ? "" : deviceno);
        this.put(PARTNER, partner == null ? "" : partner);
        this.put(REFERER, referer == null ? "" : referer);
        this.put(UID, uid == null ? 0 : uid);
        this.put(MONEY, money == null ? 0.0 : money);
        this.put(TYPE, type == null ? 0 : type);
        this.put(PAYWAY, payway == null ? 0 : payway);
        this.put(ROLENAME, roleName == null ? "" : roleName);
        this.put(TIME, time == null ? 0 : time);
        this.put(PASSPORT, passport == null ? "" : passport);
        this.put(OUTORDERID, outOrderid == null ? "" : outOrderid);
        this.put(PEXT, pext == null ? "" : pext);
        this.put(SIGN, sign == null ? "" : sign.toLowerCase());
    }


    public RechargeModel(Integer gameid, String serverName, String deviceno, String partner,
            String referer, Integer uid,  Float money,Integer type, String payway,String roleName, Long time,
            String passport, String cardnumber,String cardpwd,String cardamt, String outOrderid, String pext, String sign) {
        super("");
        this.put(GAMEID, gameid == null ? 0 : gameid);
        this.put(SERVERNAME, serverName == null ? "" : serverName);
        this.put(DEVICENO, deviceno == null ? "" : deviceno);
        this.put(PARTNER, partner == null ? "" : partner);
        this.put(REFERER, referer == null ? "" : referer);
        this.put(UID, uid == null ? 0 : uid);
        this.put(MONEY, money == null ? 0.0 : money);
        this.put(TYPE, type == null ? 0 : type);
        this.put(PAYWAY, payway == null ? 0 : payway);
        this.put(ROLENAME, roleName == null ? "" : roleName);
        this.put(TIME, time == null ? 0 : time);
        this.put(PASSPORT, passport == null ? "" : passport);
        this.put(CARDNUMBER, cardnumber == null ? "" : cardnumber);
        this.put(CARDPWD, cardpwd == null ? "" : cardpwd);
        this.put(CARDAMT, cardamt == null ? "" : cardamt);
        this.put(OUTORDERID, outOrderid == null ? "" : outOrderid);
        this.put(PEXT, pext == null ? "" : pext);
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
