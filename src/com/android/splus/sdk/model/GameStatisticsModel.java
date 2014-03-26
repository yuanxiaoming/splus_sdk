/**
 * @Title: StaticModel.java
 * @Package com.sanqi.android.sdk.model
 * Copyright: Copyright (c) 2013
 * Company:上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013-9-24 下午7:33:56
 * @version V1.0
 */

package com.android.splus.sdk.model;

/**
 * @ClassName: StaticModel
 * @author xiaoming.yuan
 * @date 2013-9-24 下午7:33:56
 */

public class GameStatisticsModel extends BaseModel {

    /**
     * @Fields serialVersionUID : （统计类参数）
     */

    private static final long serialVersionUID = 1L;

    private static final String TAG = "StaticModel";

    public static final String GAMEID = "gameid";

    private Integer gameid;

    public static final String DEVICENO = "deviceno";

    private String deviceno;

    public static final String PARTNER = "partner";

    private String partner;

    public static final String REFERER = "referer";

    private String referer;

    public static final String UID = "uid";

    private String uid;

    public static final String PASSPORT = "passport";

    private String passport;

    public static final String TIME = "time";

    private Long time;

    public static final String SIGN = "sign";

    private String sign;

    public static final String ROLENAME = "roleName";

    private String roleName;

    public static final String SERVERNAME = "serverName";

    private String serverName;

    public static final String LEVEL = "level";

    private String level;

    public static final String ONLINETIME = "onlinetime";

    private String onlinetime;

    public static final String ONLINETIMEEND = "onlinetimeend";

    private long onlinetimeend;

    public static final String ONLINETIMESTART = "onlinetimestart";

    private long onlinetimestart;

    /**
     * @Title: StatisticsData
     * @Description:(选服的构造方法)
     * @param gameid
     * @param deviceno
     * @param partner
     * @param referer
     * @param uid
     * @param passport
     * @param time
     * @param dsid
     * @param debug
     * @param sign
     * @throws
     */
    public GameStatisticsModel(Integer gameid, String deviceno, String partner, String referer,
            String uid, String passport, Long time, String serverName,  String sign) {
        super("");
        this.put(GAMEID, gameid == null ? 0 : gameid);
        this.put(DEVICENO, deviceno == null ? "" : deviceno);
        this.put(PARTNER, partner == null ? "" : partner);
        this.put(REFERER, referer == null ? "" : referer);
        this.put(UID, uid == null ? "" : uid);
        this.put(PASSPORT, passport == null ? "" : passport);
        this.put(TIME, time == 0 ? 0 : time);
        this.put(SERVERNAME, serverName == null ? "" : serverName);
        this.put(SIGN, sign == null ? "" : sign.toLowerCase());
    }

    /**
     * @Title: StatisticsData
     * @Description:(选角色构造方法)
     * @param gameid
     * @param deviceno
     * @param partner
     * @param referer
     * @param uid
     * @param passport
     * @param roleName
     * @param time
     * @param debug
     * @param sign
     * @throws
     */
    public GameStatisticsModel(Integer gameid, String deviceno, String partner, String referer,
            String uid, String passport, String roleName, Long time, String sign) {
        super("");
        this.put(GAMEID, gameid == null ? 0 : gameid);
        this.put(DEVICENO, deviceno == null ? "" : deviceno);
        this.put(PARTNER, partner == null ? "" : partner);
        this.put(REFERER, referer == null ? "" : referer);
        this.put(UID, uid == null ? "" : uid);
        this.put(PASSPORT, passport == null ? "" : passport);
        this.put(TIME, time == 0 ? 0 : time);
        this.put(ROLENAME, roleName == null ? "" : roleName);
        this.put(SIGN, sign == null ? "" : sign.toLowerCase());
    }

    /**
     * @Title: StatisticsData
     * @Description:(选角色等级构造方法)
     * @param gameid
     * @param deviceno
     * @param partner
     * @param referer
     * @param uid
     * @param passport
     * @param roleName
     * @param level
     * @param dsid
     * @param time
     * @param debug
     * @param sign
     * @throws
     */
    public GameStatisticsModel(Integer gameid, String deviceno, String partner, String referer,
            String uid, String passport, String roleName, String level, String serverName, Long time,String sign) {
        super("");
        this.put(GAMEID, gameid == null ? 0 : gameid);
        this.put(DEVICENO, deviceno == null ? "" : deviceno);
        this.put(PARTNER, partner == null ? "" : partner);
        this.put(REFERER, referer == null ? "" : referer);
        this.put(UID, uid == null ? "" : uid);
        this.put(PASSPORT, passport == null ? "" : passport);
        this.put(TIME, time == 0 ? 0 : time);
        this.put(ROLENAME, roleName == null ? "" : roleName);
        this.put(LEVEL, level == null ? "" : level);
        this.put(SERVERNAME, serverName == null ? "" : serverName);
        this.put(SIGN, sign == null ? "" : sign.toLowerCase());
    }

    /**
     * @Title: StatisticsData
     * @Description:(统计在线实在构造)
     * @param gameid
     * @param deviceno
     * @param partner
     * @param referer
     * @param uid
     * @param passport
     * @param roleName
     * @param level
     * @param dsid
     * @param onLineTime
     * @param time
     * @param debug
     * @param sign
     * @throws
     */
    public GameStatisticsModel(Integer gameid, String deviceno, String partner, String referer,
            String uid, String passport, String roleName, String level, String serverName,
            Long onLineTimeStart, Long onLineTimeEnd, Long onLineTime, Long time,String sign) {
        super("");
        this.put(GAMEID, gameid == null ? 0 : gameid);
        this.put(DEVICENO, deviceno == null ? "" : deviceno);
        this.put(PARTNER, partner == null ? "" : partner);
        this.put(REFERER, referer == null ? "" : referer);
        this.put(UID, uid == null ? "" : uid);
        this.put(PASSPORT, passport == null ? "" : passport);
        this.put(ROLENAME, roleName == null ? "" : roleName);
        this.put(LEVEL, level == null ? "" : level);
        this.put(SERVERNAME, serverName == null ? "" : serverName);
        this.put(ONLINETIME, onLineTime == null ? 0 : onLineTime);
        this.put(ONLINETIMEEND, onLineTimeEnd == 0 ? 0 : onLineTimeEnd);
        this.put(ONLINETIMESTART, onLineTimeStart == 0 ? 0 : onLineTimeStart);
        this.put(TIME, time == 0 ? 0 : time);
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
