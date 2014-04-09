/**
 * @Title: SmsReceiver.java
 * @Package: com.sanqi.android.sdk.widget
 * @Description: TODO(用一句话描述该文件做什么)
 * Copyright: Copyright (c) 2013
 * Company: 上海三七玩网络科技有限公司
 * @author xiaoming.yuan
 * @date 2013年10月15日 下午1:00:31
 * @version 1.0
 */

package com.android.splus.sdk.widget;

import com.android.splus.sdk.utils.log.LogHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

/**
 * @ClassName:SmsReceiver
 * @author xiaoming.yuan
 * @date 2013年10月15日 下午1:00:31
 */
public class SmsReceiver extends BroadcastReceiver {
    public static final String TAG = "SmsReceiver";

    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    public SmsListener listener;

    private String phone;

    public SmsReceiver(String phone, SmsListener listener) {

        this.phone = phone;
        this.listener = listener;
    }

    /*
     * (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
     * android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        if (SMS_RECEIVED.equals(intent.getAction())) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus"); // 接收数据
            for (Object pdu : pdus) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
                // 获取短信的正文内容
                String content = message.getMessageBody();
                // 获取短信的发送者
                String address = message.getOriginatingAddress();

                LogHelper.i(TAG, "信息内容:" + content);
                LogHelper.i(TAG, "发送者:" + address);
                if (address.equals(phone) && listener != null) {
                    listener.onSmsReceive(content);
                }
            }
        }
    }

    public interface SmsListener {
        public void onSmsReceive(String content);
    }
}
