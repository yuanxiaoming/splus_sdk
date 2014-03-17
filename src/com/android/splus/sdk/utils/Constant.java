
package com.android.splus.sdk.utils;

import com.android.splus.sdk.utils.r.KR;

public class Constant {

    /**
     * url
     */

    //    public static final String URL="http://121.201.96.82/api.splusgame.com/sdk/";
    //
    //    public static final String ACTIVE_URL = URL+"active.php";
    //
    //    public static final String LOGIN_URL = URL+"login.php";
    //
    //    public static final String REGISTER_URL = URL+"reg.php";
    //
    //    public static final String HTMLWAPPAY_URL = "http://sy.api.37wan.cn/htmlWapPay_test/payIndex.php";
    //
    //    public static final String RATIO_URL = "http://sy.api.37wan.cn/sdk/ext/pay_way_ratio.php";
    //

    public static final String ACTIVE_URL = "http://sy.api.37wan.cn/sdk/active/active.php";

    public static final String LOGIN_URL = "http://sy.api.37wan.cn/sdk/login/";

    public static final String REGISTER_URL = "http://sy.api.37wan.cn/sdk/reg/reg.php";

    public static final String HTMLWAPPAY_URL = "http://sy.api.37wan.cn/htmlWapPay_test/payIndex.php";

    public static final String RATIO_URL = "http://sy.api.37wan.cn/sdk/ext/pay_way_ratio.php";


    //充值：
    public static final int[] CHINA_MOBILE_MONEY = {
            10, 20, 30, 50, 100, 300, 500
    };

    public static final int[] CHINA_UNICOM_MONEY = {
        20, 30, 50, 100, 300, 500
    };

    public static final int[] CHINA_SDCOMM_ONEY = {
        10, 30, 35, 45, 100, 350, 1000
    };

    public static final int[] ALIPAY_MONEY = {
        10, 20, 30, 50, 100, 200, 300, 500, 1000, 2000, 3000, 5000
    };

    public static final int RECHARGE_BY_NO_QUATO = 0;

    public static final int RECHARGE_BY_QUATO = 1;

    public final static int ALIPAY_FAST = 0;

    public final static int ALIPAY_WAP = 1;

    public final static int ALIPAY_DEPOSIT = 2;

    public final static int ALIPAY_CREDIT = 3;

    public final static int UNION_PAY = 4;

    public final static int CHAIN_CMM = 5;

    public final static int CHAIN_UNC = 6;

    public final static int CHAIN_SD = 7;

    public final static int PERSON = 8;

    public static final Integer RECHARGE_TYPE[] = {
        ALIPAY_FAST, ALIPAY_WAP, ALIPAY_DEPOSIT, ALIPAY_CREDIT, UNION_PAY,CHAIN_CMM, CHAIN_UNC, CHAIN_SD,
        PERSON
    };

    public static final String IMG_ICON[] = {
        KR.drawable.splus_recharge_by_alipay_fast_normal,
        KR.drawable.splus_recharge_by_alipay_fast_normal,
        KR.drawable.splus_recharge_by_alipay_fast_normal,
        KR.drawable.splus_recharge_by_alipay_fast_normal,
        KR.drawable.splus_recharge_by_alipay_fast_normal,
        KR.drawable.splus_recharge_by_alipay_fast_normal,
        KR.drawable.splus_recharge_by_alipay_fast_normal,
        KR.drawable.splus_recharge_by_alipay_fast_normal,
        KR.drawable.splus_recharge_by_alipay_fast_normal
    };

    /**
     * 充值类型
     */

    public final static String ALIPAY_FAST_PAYWAY = "alipay_fast";

    public final static String ALIPAY_WAP_PAYWAY = "alipay_way";

    public final static String ALIPAY_DEPOSIT_PAYWAY = "alipay_deposit";

    public final static String ALIPAY_CREDIT_PAYWAY = "alipay_credit";

    public final static String UNION_PAYWAY = "union_pay";

    public final static String CHAIN_CMM_PAYWAY = "szx";

    public final static String CHAIN_UNC_PAYWAY = "unicom";

    public final static String CHAIN_SD_PAYWAY = "sndacard";

    public final static String PERSON_PAYWAY = "";

    public static final String[] PAYWAY_TYPE = {
        ALIPAY_FAST_PAYWAY, ALIPAY_WAP_PAYWAY, ALIPAY_DEPOSIT_PAYWAY, ALIPAY_CREDIT_PAYWAY,UNION_PAYWAY,
        CHAIN_CMM_PAYWAY, CHAIN_UNC_PAYWAY, CHAIN_SD_PAYWAY, PERSON_PAYWAY
    };

    /** ******************************************************************************************/
    /** sharespreferences 设备唯一标示* */
    public static final String DEVICENO_FILE = "deviceno_file";

    public static final String DEVICENO = "deviceno";

    public static final String LOGIN_STATUS_FILE = "login_status_file";

    public static final String LOGIN_STATUS = "login_status";

    /** ******************************************************************************************/
    public static String RECHARGE_RESULT_TIPS = "recharge_result_tips";

    public static String RECHARGE_RESULT_SUCCESS_TIPS = "recharge_result_success_tips";

    public static String RECHARGE_RESULT_FAIL_TIPS = "recharge_result_fail_tips";

    public static final String MONEY = "money";

}
