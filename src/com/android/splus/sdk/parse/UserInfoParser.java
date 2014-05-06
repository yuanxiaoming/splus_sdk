/**
 * @Title: CheckPhoneParser.java
 * @Package: com.sanqi.android.sdk.parse
 * @Description: TODO(用一句话描述该文件做什么)
 * Copyright: Copyright (c) 2013
 * Company: 上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013年10月11日 下午2:18:21
 * @version 1.0
 */

package com.android.splus.sdk.parse;

import com.android.splus.sdk.data.UserInfoData;
import com.android.splus.sdk.utils.http.BaseParser;
import com.android.splus.sdk.utils.log.LogHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * CheckUserInfoParser
 * 
 * @author yuanxiaoming
 */
public class UserInfoParser extends BaseParser<HashMap<String, Object>> {
    private static final String TAG = "ActiveParser";

    private HashMap<String, Object> mHashMap;

    private UserInfoData userInfoData = null;

    private String msg;

    @Override
    public HashMap<String, Object> parseJSON(String paramString) throws JSONException {
        JSONObject paramObject = new JSONObject(paramString);
        mHashMap = new HashMap<String, Object>();
        if (paramObject != null && paramObject.optInt(CODE) == SUCCESS) {
            JSONObject jsonObject = paramObject.optJSONObject(DATA);
            if (jsonObject != null) {
                String realname = jsonObject.optString(UserInfoData.REALNAME);
                String gendertype = jsonObject.optString(UserInfoData.GENDERTYPE);
                String idcard = jsonObject.optString(UserInfoData.IDCARD);
                String qq = jsonObject.optString(UserInfoData.QQ);
                userInfoData = new UserInfoData(realname, gendertype, idcard, qq);
            }
        } else {
            msg = paramObject.getString(MSG);
            LogHelper.i(TAG, msg);

        }
        mHashMap.put(DATA, userInfoData);
        mHashMap.put(MSG, msg);
        return mHashMap;
    }

}
