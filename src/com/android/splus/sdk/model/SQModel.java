
package com.android.splus.sdk.model;
public class SQModel extends BaseModel {

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

    public static final String ROLENAME = "roleName";

    private String roleName;

    public static final String SERVERNAME = "serverName";

    private String serverName;



    public SQModel(Integer gameid, String deviceno, String referer,String partner, Integer uid, String passport, String password,String roleName, String serverName) {
        super("");
        this.put(GAMEID, gameid == null ? 0 : gameid);
        this.put(SERVERNAME, serverName == null ? "" : serverName);
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

