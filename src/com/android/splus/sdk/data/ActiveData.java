/**
 * @Title: ActiveData.java
 * @Package com.android.cansh.sdk.data
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-2-26 下午5:03:21
 * @version V1.0
 */

package com.android.splus.sdk.data;

import com.android.splus.sdk.ui.SplusPayManager;
import com.android.splus.sdk.utils.log.LogHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ClassName: ActiveData
 * @author xiaoming.yuan
 * @date 2014-2-26 下午5:03:21
 */

public class ActiveData {

    private static final String TAG = "ActiveData";

    public static final String DEVICENO = "deviceno";

    private String deviceno;

    public static final String UPDATETYPE = "updatetype";

    private Integer updatetype;

    public static final String SDKVER = "sdkver";

    private String sdkver;

    public static final String GAMEVER = "gamever";

    private String gamever;

    public static final String UPDATEURL = "updateurl";

    private String updateurl;

    public static final String UPDATECONTENT = "updatecontent";

    private String updatecontent;

    public static final String PASSPORT = "passport";

    private String passport;

    public static final String RELATIONSHIPS = "relationships";

    private String relationships;

    public JSONObject updateInfo;

    public ActiveData(String deviceno, Integer updatetype, String sdkver, String gamever,
            String updateurl, String updatecontent, String passport, String relationships) {
        super();
        this.deviceno = deviceno==null?"": deviceno;
        this.updatetype = updatetype==null?0:updatetype;
        this.sdkver = sdkver==null?"":sdkver;
        this.gamever = gamever==null?"":gamever;
        this.updateurl = updateurl==null?"":updateurl;
        this.updatecontent = updatecontent==null?"":updatecontent;
        this.passport = passport;
        this.relationships = relationships;
        updateInfo = new JSONObject();
        try {
            updateInfo.put(SplusPayManager.UPDATETYPE, this.updatetype);
            updateInfo.put(SplusPayManager.SDKVERSION, this.sdkver);
            updateInfo.put(SplusPayManager.GAMEVERSION, this.gamever);
            updateInfo.put(SplusPayManager.UPDATEURL, this.updateurl);
            updateInfo.put(SplusPayManager.UPDATECONTENT, this.updatecontent);
        } catch (JSONException e) {
            LogHelper.e(TAG, "", e);

        }
    }

    /**
     * getter method
     *
     * @return the deviceno
     */

    public String getDeviceno() {
        return this.deviceno;
    }

    /**
     * setter method
     *
     * @param deviceno the deviceno to set
     */

    public void setDeviceno(String deviceno) {
        this.deviceno = deviceno;
    }

    /**
     * getter method
     *
     * @return the updatetype
     */

    public Integer getUpdatetype() {
        return this.updatetype;
    }

    /**
     * setter method
     *
     * @param updatetype the updatetype to set
     */

    public void setUpdatetype(Integer updatetype) {
        this.updatetype = updatetype;
    }

    /**
     * getter method
     *
     * @return the sdkver
     */

    public String getSdkver() {
        return this.sdkver;
    }

    /**
     * setter method
     *
     * @param sdkver the sdkver to set
     */

    public void setSdkver(String sdkver) {
        this.sdkver = sdkver;
    }

    /**
     * getter method
     *
     * @return the gamever
     */

    public String getGamever() {
        return this.gamever;
    }

    /**
     * setter method
     *
     * @param gamever the gamever to set
     */

    public void setGamever(String gamever) {
        this.gamever = gamever;
    }

    /**
     * getter method
     *
     * @return the updateurl
     */

    public String getUpdateurl() {
        return this.updateurl;
    }

    /**
     * setter method
     *
     * @param updateurl the updateurl to set
     */

    public void setUpdateurl(String updateurl) {
        this.updateurl = updateurl;
    }

    /**
     * getter method
     *
     * @return the updatecontent
     */

    public String getUpdatecontent() {
        return this.updatecontent;
    }

    /**
     * setter method
     *
     * @param updatecontent the updatecontent to set
     */

    public void setUpdatecontent(String updatecontent) {
        this.updatecontent = updatecontent;
    }

    /**
     * getter method
     *
     * @return the passport
     */

    public String getPassport() {
        return this.passport;
    }

    /**
     * setter method
     *
     * @param passport the passport to set
     */

    public void setPassport(String passport) {
        this.passport = passport;
    }

    /**
     * getter method
     *
     * @return the relationships
     */

    public String getRelationships() {
        return this.relationships;
    }

    /**
     * setter method
     *
     * @param relationships the relationships to set
     */

    public void setRelationships(String relationships) {
        this.relationships = relationships;
    }

    /**
     * getter method
     *
     * @return the mJSONObject
     */

    public JSONObject getUpdateInfo() {
        return this.updateInfo;
    }

    /**
     * setter method
     *
     * @param mJSONObject the mJSONObject to set
     */

    public void setUpdateInfo(JSONObject updateInfo) {
        this.updateInfo = updateInfo;
    }

}
