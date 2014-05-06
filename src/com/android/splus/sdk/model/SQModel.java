
package com.android.splus.sdk.model;

public class SQModel extends BaseModel {

    /**
     * @Fields serialVersionUID
     */

    private static final long serialVersionUID = 1L;

    private static final String TAG = "SQModel";

    public static final String GAMEID = "gameid";

    private Integer gameid;

    public static final String DEVICENO = "deviceno";

    private String deviceno;

    public static final String REFERER = "referer";

    private String referer;

    public static final String PARTNER = "partner";

    private String partner;

    public static final String UID = "uid";

    private Integer uid;

    public static final String PASSPORT = "passport";

    private String passport;

    public static final String PASSWORD = "password";

    private String password;

    public static final String SERVERID = "serverId";
    private Integer serverId;

    public static final String ROLEID = "roleId";
    private Integer roleId;


    public static final String ROLENAME = "roleName";

    private String roleName;

    public static final String SERVERNAME = "serverName";

    private String serverName;

    public SQModel(Integer gameid, String deviceno, String referer, String partner, Integer uid, String passport, String password,Integer serverId, Integer roleId, String roleName, String serverName) {
        super("");
        this.put(GAMEID, gameid == null ? 0 : gameid);
        this.put(SERVERID, serverId == null ? 0 : serverId);
        this.put(SERVERNAME, serverName == null ? "" : serverName);
        this.put(ROLEID, roleId == null ? 0 : roleId);
        this.put(ROLENAME, roleName == null ? "" : roleName);
        this.put(DEVICENO, deviceno == null ? "" : deviceno);
        this.put(PARTNER, partner == null ? "" : partner);
        this.put(REFERER, referer == null ? "" : referer);
        this.put(UID, uid == null ? 0 : uid);
        this.put(ROLENAME, roleName == null ? "" : roleName);
        this.put(PASSPORT, passport == null ? "" : passport);
        this.put(PASSWORD, passport == null ? "" : passport);
    }

    @Override
    protected void initMap() {
    }
}
