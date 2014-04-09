
package com.android.splus.sdk.parse;

import com.android.splus.sdk.utils.http.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA. Copyright: Copyright (c) 2013
 * Company:上海三七玩网络科技有限公司 User: xiaoming.yuan Email: lijianfeng@37wan.com Date: 13-9-17
 * Time: 下午4:42 To change this template use File | Settings | File Templates.
 */
public class LoginParser extends BaseParser<JSONObject> {
    @Override
    public JSONObject parseJSON(String paramString) throws JSONException {
        JSONObject jsonObject = new JSONObject(paramString);
        return jsonObject;
    }
}
