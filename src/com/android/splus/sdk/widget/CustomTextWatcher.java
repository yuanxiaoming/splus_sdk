/**
 * @Title: CustomTextWatcher.java
 * @Package: com.sanqi.android.sdk.person
 * @Description: TODO(用一句话描述该文件做什么)
 * Copyright: Copyright (c) 2013
 * Company: 上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013年10月12日 下午5:47:24
 * @version 1.0
 */

package com.android.splus.sdk.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @ClassName:CustomTextWatcher
 * @author xiaoming.yuan
 * @date 2013年10月12日 下午5:47:24
 */
public class CustomTextWatcher implements TextWatcher {
    private EditText et;

    private String limit = "";

    private int start = 0, end = 0;

    private String text = "";

    private int selection = 0;

    public CustomTextWatcher(EditText et, String limit) {

        this.et = et;
        this.limit = limit;
    }

    /*
     * (non-Javadoc)
     * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
     */
    @Override
    public void afterTextChanged(Editable s) {
        if (start == end) {
            return;
        }
        if (text.equals(s.toString())) {
            return;
        }
        CharSequence cs = s.subSequence(start, end);

        if (!limit.contains(cs)) {
            s.delete(start, end);
        }
        et.setText(s.toString());
        et.setSelection(selection);

    }

    /*
     * (non-Javadoc)
     * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence,
     * int, int, int)
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        text = s.toString();
        selection = et.getSelectionEnd();

    }

    /*
     * (non-Javadoc)
     * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int,
     * int, int)
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.start = start;
        this.end = start + count;

    }

}
