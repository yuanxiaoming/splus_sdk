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

    public static final String DSID = "dsid";

    private String dsid;

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

    public static final String MONEY = "money";

    private String money;

    public static final String ROLE = "role";

    private String role;

    public static final String OUTORDERID = "outorderid";

    private String outorderid;

    public static final String PEXT = "pext";

    private String pext;

    public static final String TIME = "time";

    private Long time;

    public static final String PASSPORT = "passport";

    private String passport;


    public static final String SIGN = "sign";

    private String sign;

    /**
     * @Title: RechargeModel
     * @Description:(非卡类充值模型)
     * @param: @param gameid
     * @param: @param dsid
     * @param: @param deviceno
     * @param: @param partner
     * @param: @param referer
     * @param: @param uid
     * @param: @param money
     * @param: @param payway
     * @param: @param role
     * @param: @param time
     * @param: @param passport
     * @param: @param outorderid
     * @param: @param pext
     * @param: @param debug
     * @param: @param sign
     * @throws
     */

    public RechargeModel(Integer gameid, String dsid, String deviceno, String partner,
            String referer, Integer uid, Integer type, Float money, String role, Long time,
            String passport, String outorderid, String pext, String sign) {
        super("");
        this.put(GAMEID, gameid == null ? 0 : gameid);
        this.put(DSID, dsid == null ? "" : dsid);
        this.put(DEVICENO, deviceno == null ? "" : deviceno);
        this.put(PARTNER, partner == null ? "" : partner);
        this.put(REFERER, referer == null ? "" : referer);
        this.put(UID, uid == null ? 0 : uid);
        this.put(TYPE, type == null ? 0 : type);
        this.put(MONEY, money == null ? 0.0 : money);
        this.put(ROLE, role == null ? "" : role);
        this.put(TIME, time == null ? 0 : time);
        this.put(PASSPORT, passport == null ? "" : passport);
        this.put(OUTORDERID, outorderid == null ? "" : outorderid);
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
