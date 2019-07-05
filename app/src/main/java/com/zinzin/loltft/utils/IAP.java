package com.zinzin.loltft.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IAP {
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("purchaseToken")
    @Expose
    private String purchaseToken;
    @SerializedName("purchaseTime")
    @Expose
    private float purchaseTime;
    @SerializedName("developerPayload")
    @Expose
    private Object developerPayload;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }

    public float getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(float purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public Object getDeveloperPayload() {
        return developerPayload;
    }

    public void setDeveloperPayload(Object developerPayload) {
        this.developerPayload = developerPayload;
    }

}
