/**
 * @Title: RechargeTypeModel.java
 * @Package com.android.splus.sdk.model
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-3-11 上午10:19:47
 * @version V1.0
 */

package com.android.splus.sdk.model;

import java.util.HashMap;

/**
 * @ClassName: RechargeTypeModel
 * @author xiaoming.yuan
 * @date 2014-3-11 上午10:19:47
 */

public class RechargeTypeModel extends HashMap<String, Object> {

    /**
     * @Fields serialVersionUID :（用一句话描述这个变量表示什么）
     */

    private static final long serialVersionUID = 1L;

    private static final String TAG = "RechargeTypeModel";

    private String imgIcon;

    public static final String IMGICON = "imgIcon";

    private Integer rechargeType;

    public static final String RECHARGETYPE = "rechargeType";

    private String payway;

    public static final String PAYWAY = "payway";

    public RechargeTypeModel(String imgIcon, Integer rechargeType, String payway) {
        super();
        this.put(IMGICON, imgIcon == null ? 0 : imgIcon);
        this.put(RECHARGETYPE, rechargeType == null ? "" : rechargeType);
        this.put(PAYWAY, payway == null ? "" : payway);
    }

    /**
     * getter method
     * 
     * @return the imgIcon
     */

    public String getPayway() {
        return (String) this.get(PAYWAY);
    }

    /**
     * setter method
     * 
     * @param imgIcon the imgIcon to set
     */

    public void setPayway(String payway) {
        this.put(PAYWAY, payway == null ? 0 : payway);
    }

    /**
     * getter method
     * 
     * @return the imgIcon
     */

    public String getImgIcon() {
        return (String) this.get(IMGICON);
    }

    /**
     * setter method
     * 
     * @param imgIcon the imgIcon to set
     */

    public void setImgIcon(String imgIcon) {
        this.put(IMGICON, imgIcon == null ? 0 : imgIcon);
    }

    /**
     * getter method
     * 
     * @return the rechargeType
     */

    public Integer getRechargeType() {
        return (Integer) this.get(RECHARGETYPE);
    }

    /**
     * setter method
     * 
     * @param rechargeType the rechargeType to set
     */

    public void setRechargeType(Integer rechargeType) {
        this.put(RECHARGETYPE, rechargeType == null ? "" : rechargeType);
    }

}
