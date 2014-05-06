/**
 * @Title: IdcardUtil.java
 * @Package: com.sanqi.android.sdk.util
 * @Description: TODO(用一句话描述该文件做什么)
 * Copyright: Copyright (c) 2013
 * Company: 上海三七玩网络科技有限公司
 * @author 李剑锋
 * @date 2013年10月6日 上午11:39:11
 * @version 1.0
 */

package com.android.splus.sdk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 身份证验证的工具（支持5位或18位省份证）<br/>
 * 身份证号码结构：
 * <ol>
 * <li>17位数字和1位校验码：6位地址码数字，8位生日数字，3位出生时间顺序号，1位校验码。</li>
 * <li>地址码（前6位）：表示对象常住户口所在县（市、镇、区）的行政区划代码，按GB/T2260的规定执行。</li>
 * <li>出生日期码，（第七位 至十四位）：表示编码对象出生年、月、日，按GB按GB/T7408的规定执行，年、月、日代码之间不用分隔符。</li>
 * <li>顺序码（第十五位至十七位）：表示在同一地址码所标示的区域范围内，对同年、同月、同日出生的人编订的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。
 * </li>
 * <li>校验码（第十八位数）：<br/>
 * <ul>
 * <li>十七位数字本体码加权求和公式 s = sum(Ai*Wi), i = 0,,16，先对前17位数字的权求和；
 * Ai:表示第i位置上的身份证号码数字值.Wi:表示第i位置上的加权因.Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2；</li>
 * <li>计算模 Y = mod(S, 11)</li>
 * <li>通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2</li>
 * </ul>
 * </li>
 * </ol>
 * 
 * @ClassName:IdcardUtil
 * @author xiaoming.yuan
 * @date 2013年10月6日 上午11:39:11
 */
public class IdcardUtil {
    private static HashMap<String, String> GetAreaCode() {
        HashMap<String, String> hashmap = new HashMap<String, String>();
        hashmap.put("11", "北京");
        hashmap.put("12", "天津");
        hashmap.put("13", "河北");
        hashmap.put("14", "山西");
        hashmap.put("15", "内蒙古");
        hashmap.put("21", "辽宁");
        hashmap.put("22", "吉林");
        hashmap.put("23", "黑龙江");
        hashmap.put("31", "上海");
        hashmap.put("32", "江苏");
        hashmap.put("33", "浙江");
        hashmap.put("34", "安徽");
        hashmap.put("35", "福建");
        hashmap.put("36", "江西");
        hashmap.put("37", "山东");
        hashmap.put("41", "河南");
        hashmap.put("42", "湖北");
        hashmap.put("43", "湖南");
        hashmap.put("44", "广东");
        hashmap.put("45", "广西");
        hashmap.put("46", "海南");
        hashmap.put("50", "重庆");
        hashmap.put("51", "四川");
        hashmap.put("52", "贵州");
        hashmap.put("53", "云南");
        hashmap.put("54", "西藏");
        hashmap.put("61", "陕西");
        hashmap.put("62", "甘肃");
        hashmap.put("63", "青海");
        hashmap.put("64", "宁夏");
        hashmap.put("65", "新疆");
        hashmap.put("71", "台湾");
        hashmap.put("81", "香港");
        hashmap.put("82", "澳门");
        hashmap.put("91", "国外");
        return hashmap;
    }

    private final static String[] ValCodeArr = {
                    "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"
    };

    private final static String[] Wi = {
                    "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"
    };

    /**
     * 功能：身份证的有效验证
     * 
     * @param IDStr 身份证号
     * @return 有效：返回"" 无效：返回String信息
     */
    public static String IDCardValidate(String IDStr) {
        String errorInfo = "";// 记录错误信息
        String Ai = "";

        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                return errorInfo;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            errorInfo = "身份证生日格式有误。";
            return errorInfo;
        } catch (ParseException e) {
            e.printStackTrace();
            errorInfo = "身份证生日格式有误。";
            return errorInfo;
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        HashMap<String, String> h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo;
            }
        } else {
            return "";
        }
        // =====================(end)=====================
        return "";
    }

    /**
     * 功能：判断字符串是否为数字
     * 
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：判断字符串是否为日期格式
     * 
     * @param str
     * @return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                        .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
