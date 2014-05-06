/**
 * @Title: CheckPhoneData.java
 * @Package: com.sanqi.android.sdk.model
 * @Description: TODO(用一句话描述该文件做什么)
 * Copyright: Copyright (c) 2013
 * Company: 上海三七玩网络科技有限公司
 * @author 李剑锋
 * @date 2013年10月11日 下午2:09:41
 * @version 1.0
 */

package com.android.splus.sdk.model;

/**
 * @ClassName:CheckPhoneData
 * @author xiaoming.yuan
 * @date 2013年10月11日 下午2:09:41
 */
public class CheckPhoneModel extends BaseModel {
    private static final long serialVersionUID = 1L;

    public static final String GAMEID = "gameid";

    private Integer gameid;

    public static final String DEVICENO = "deviceno";

    private String deviceno;

    public static final String PARTNER = "partner";

    private String partner;

    public static final String REFERER = "referer";

    private String referer;

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

    public static final String PASSPORT = "passport";

    private String passport;

    public static final String SIGN = "sign";

    private String sign;

    public static final String TIME = "time";

    private Long time;

    public static final String ACTION = "action";

    public CheckPhoneModel(Integer gameid, String deviceno, String partner, String referer, Integer uid, Integer serverId, Integer roleId,String serverName, String roleName, String passport, Long time, String sign) {
        super("");
        this.put(UID, uid == null ? 0 : uid);
        this.put(GAMEID, gameid == null ? 0 : gameid);
        this.put(SERVERID, serverId == null ? 0 : serverId);
        this.put(SERVERNAME, serverName == null ? "" : serverName);
        this.put(ROLEID, roleId == null ? 0 : roleId);
        this.put(ROLENAME, roleName == null ? "" : roleName);
        this.put(SIGN, sign == null ? "" : sign);
        this.put(TIME, time == null ? 0 : time);
        this.put(DEVICENO, deviceno == null ? "" : deviceno);
        this.put(PARTNER, partner == null ? "" : partner);
        this.put(REFERER, referer == null ? "" : referer);
        this.put(PASSPORT, passport == null ? "" : passport);
        this.put(ACTION, "checkphone");
    }

    @Override
    protected void initMap() {

    }

}
