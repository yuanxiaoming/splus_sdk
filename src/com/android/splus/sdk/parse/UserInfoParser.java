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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * CheckUserInfoParser
 * @author 0794
 *
 */
public class UserInfoParser extends BaseParser<UserInfoData> {

    @Override
    public UserInfoData parseJSON(String paramString) throws JSONException {
        UserInfoData userInfoData=null;
        JSONObject paramObject = new JSONObject(paramString);
        if (paramObject != null && paramObject.optInt("code") == 1) {
            JSONArray GameInfoArray = paramObject.optJSONArray("data");
            for (int i = 0; i < GameInfoArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) GameInfoArray.get(i);
                String realname = jsonObject.optString(UserInfoData.REALNAME);
                String gendertype = jsonObject.optString(UserInfoData.GENDERTYPE);
                String idcard = jsonObject.optString(UserInfoData.IDCARD);
                String qq = jsonObject.optString(UserInfoData.QQ);
                userInfoData = new UserInfoData(realname, gendertype, idcard, qq);
            }
        }
        return userInfoData;
    }

}
