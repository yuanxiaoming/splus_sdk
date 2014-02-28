
package com.android.splus.sdk.model;

import com.android.splus.sdk.apiinterface.UserAccount;
import com.android.splus.sdk.utils.file.AppUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA. Copyright: Copyright (c) 2013
 * User: xiaoming.yuan
 * Time: 下午4:34 To change this template use File | Settings | File Templates.
 */
public class UserModel extends BaseModel implements UserAccount {

    /**
     * @Fields serialVersionUID （用一句话描述这个变量表示什么）
     */

    private static final long serialVersionUID = 1L;

    private static final String TAG = "UserData";

    private String passport;

    public final static String PASSPORT = "passport";

    private String password;

    public static String PASSWORD = "password";

    private Integer time;

    public final static String TIME = "time";

    private Integer uid;

    public final static String UID = "uid";

    private final static String CHECKED = "checked";

    public Boolean checked;

    private String sessionid;

    public final static String SESSIONID = "sessionid";

    public UserModel(String jsonObject) {
        super(jsonObject);
        try {
            JSONObject data = new JSONObject(jsonObject);
            this.uid = data.optInt(UID);
            this.passport = data.optString(PASSPORT);
            this.password = data.optString(PASSWORD);
            this.sessionid = data.optString(SESSIONID);
            this.time = data.optInt(TIME);
            this.checked = data.optBoolean(CHECKED);
            initMap();
        } catch (JSONException e) {
            e.printStackTrace();
            // To change body of catch statement use File |
            // Settings | File Templates.
        }
        setPath(AppUtil.USER_DATA_PATH, passport);
    }

    public UserModel(Integer uid, String passport, String password, String sessionid, Integer time,
            Boolean checked) {
        super("");
        this.uid = uid;
        this.passport = passport;
        this.password = password;
        this.sessionid = sessionid;
        this.time = time;
        this.checked = checked;

        initMap();
        setPath(AppUtil.USER_DATA_PATH, passport);
    }

    @Override
    protected void initMap() {
        // To change body of implemented methods use File | Settings | File
        // Templates.
        this.put(UID, uid == null ? 0 : uid);
        this.put(PASSPORT, passport == null ? "" : passport);
        this.put(PASSWORD, password == null ? "" : password);
        this.put(SESSIONID, sessionid == null ? "" : sessionid);
        this.put(TIME, time == null ? 0 : time);
        this.put(CHECKED, checked == null ? true : checked);
    }

    /**
     * getter method
     *
     * @return the checked
     */

    public Boolean getChecked() {
        return (Boolean) this.get(CHECKED);
    }

    /**
     * setter method
     *
     * @param checked the checked to set
     */

    public void setChecked(Boolean checked) {
        this.put(CHECKED, checked == null ? true : checked);
    }

    public String getPassport() {
        return this.get(PASSPORT).toString();
    }

    public void setPassport(String passport) {
        this.put(PASSPORT, passport == null ? "" : passport);
    }

    public String getPassword() {
        return this.get(PASSWORD).toString();
    }

    public void setPassword(String password) {
        this.put(PASSWORD, password == null ? "" : password);

    }

    public Integer getTime() {
        return (Integer) this.get(TIME);
    }

    public void setTime(Integer time) {

        this.put(TIME, time == null ? 0 : time);
    }

    public Integer getUid() {
        return (Integer) this.get(UID);
    }

    public void setUid(Integer uid) {
        this.put(UID, uid == null ? 0 : uid);
    }

    public String getSessionid() {
        return this.get(SESSIONID).toString();
    }

    public void setSessionid(String sessionid) {
        this.put(SESSIONID, sessionid == null ? "" : sessionid);
    }


    /**
     * Title: getSession Description:
     *
     * @return
     * @see com.canhe.android.sdk.apinterface.UserAccount#getSession()
     */
    @Override
    public String getSession() {

        return this.get(SESSIONID).toString();

    }

    /**
     * Title: getUserUid Description:
     *
     * @return
     * @see com.canhe.android.sdk.apinterface.UserAccount#getUserUid()
     */
    @Override
    public Integer getUserUid() {

        return (Integer) this.get(UID);

    }

    /**
     * Title: getUserName Description:
     *
     * @return
     * @see com.canhe.android.sdk.apinterface.UserAccount#getUserName()
     */
    public String getUserName() {
        return this.get(PASSPORT).toString();
    }

}
