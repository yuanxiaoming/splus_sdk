
package com.android.splus.sdk.parse;

import com.android.splus.sdk.data.ActiveData;
import com.android.splus.sdk.utils.http.BaseParser;
import com.android.splus.sdk.utils.log.LogHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA. Copyright: Copyright (c) 2013 User: xiaoming.yuan
 * template use File | Settings | File Templates.
 */
public class ActiveParser extends BaseParser<HashMap<String, Object>> {

    private static final String TAG = "ActiveParser";

    private ActiveData mActiveData = null;

    private HashMap<String, Object> mHashMap;

    private String msg;

    @Override
    public HashMap<String, Object> parseJSON(String paramString) throws JSONException {
        JSONObject paramObject = new JSONObject(paramString);
        mHashMap = new HashMap<String, Object>();
        if (paramObject != null && paramObject.optInt(CODE) == SUCCESS) {
            JSONObject jsonObject = paramObject.optJSONObject(DATA);
            if (jsonObject != null) {
                String deviceno = jsonObject.optString(ActiveData.DEVICENO);
                int updatetype = jsonObject.optInt(ActiveData.UPDATETYPE);
                String sdkVersion = jsonObject.optString(ActiveData.SDKVER);
                String gameVersion = jsonObject.optString(ActiveData.GAMEVER);
                String updateurl = jsonObject.optString(ActiveData.UPDATEURL);
                String updatecontent = jsonObject.optString(ActiveData.UPDATECONTENT);
                String easyRegisterPassport = jsonObject.optString(ActiveData.PASSPORT);
                String relationships = jsonObject.optString(ActiveData.RELATIONSHIPS);
                mActiveData = new ActiveData(deviceno, updatetype, sdkVersion, gameVersion, updateurl, updatecontent, easyRegisterPassport, relationships);
            }
        } else {
            msg = paramObject.getString(MSG);
            LogHelper.i(TAG, msg);
        }
        mHashMap.put(DATA, mActiveData);
        mHashMap.put(MSG, msg);
        return mHashMap;
    }
}
