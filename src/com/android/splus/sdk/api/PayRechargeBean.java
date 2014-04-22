
package com.android.splus.sdk.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PayRechargeBean extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    private static final String TAG = "PayRechargeBean";

    private  String orderid; // 订单号；360、91、anzhi、duoku、jifeng、xiaomi、oppo
    public static final String ORDERID = "orderid";

    private String productId; // 商品ID；360、91
    public static final String PRODUCTID = "productId";

    private String productName; // 商品名称；360、91、dcn、duoku、oppo
    public static final String PRODUCTNAME = "productName";

    private String productDescription; // 商品描述；91、anzhi、duoku、oppo
    public static final String PRODUCTDESCRIPTION = "productDescription";

    private double productPrice;
    public static final String PRODUCTPRICE = "productPrice";

    private double productOrginalPrice;
    public static final String PRODUCTORGINALPRICE = "productOrginalPrice";

    private int count;
    public static final String COUNT = "count";

    private String notifyUrl; // 充值回调地址;360、OPPO
    public static final String NOTIFYURL = "notifyUrl";



    public PayRechargeBean(String jsonObject) {
        try {
            JSONObject data = new JSONObject(jsonObject);
            this.orderid = data.optString(ORDERID)==null? "" : data.optString(ORDERID);
            this.productId = data.optString(PRODUCTID)==null? "" : data.optString(PRODUCTID);
            this.productName = data.optString(PRODUCTNAME)==null? "" : data.optString(PRODUCTNAME);
            this.productDescription = data.optString(PRODUCTDESCRIPTION)==null? "" : data.optString(PRODUCTDESCRIPTION);
            this.notifyUrl = data.optString(NOTIFYURL)==null? "" : data.optString(NOTIFYURL);
            this.productPrice = data.optDouble(PRODUCTPRICE)==0.0d? 0.0d : data.optDouble(PRODUCTPRICE);
            this.productOrginalPrice = data.optDouble(PRODUCTORGINALPRICE)==0.0d? 0.0d : data.optDouble(PRODUCTORGINALPRICE);
            this.count = data.optInt(COUNT)==0? 1 : data.optInt(COUNT);
            initMap();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initMap() {
        this.put(ORDERID, orderid);
        this.put(PRODUCTID, productId);
        this.put(PRODUCTNAME, productName );
        this.put(PRODUCTDESCRIPTION, productDescription);
        this.put(NOTIFYURL, notifyUrl);
        this.put(PRODUCTPRICE, productPrice);
        this.put(PRODUCTORGINALPRICE, productOrginalPrice);
        this.put(COUNT, count);
    }


    public int getCount() {

        return this.count;
    }

    public void setCount(int count) {

        this.count = count;
    }

    public double getProductPrice() {

        return this.productPrice;
    }

    public void setProductOrginalPrice(double productOrginalPrice) {

        this.productOrginalPrice = productOrginalPrice;
    }

    public String getOrderid() {

        return this.orderid;
    }

    public void setOrderid(String orderid) {

        this.orderid = orderid;
    }

    public String getProductId() {

        return this.productId;
    }

    public void setProductId(String productId) {

        this.productId = productId;
    }

    public String getProductName() {

        return this.productName;
    }

    public void setProductName(String productName) {

        this.productName = productName;
    }

    public String getProductDescription() {

        return this.productDescription;
    }

    public void setProductDescription(String productDescription) {

        this.productDescription = productDescription;
    }


    public void setProductPrice(double productPrice)
    {
        this.productPrice = productPrice;
    }

    public double getProductOrginalPrice()
    {
        return productOrginalPrice;
    }

    public String getNotifyUrl() {

        return this.notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {

        this.notifyUrl = notifyUrl;
    }


}

