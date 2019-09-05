package com.zzamphong.slfood.zzamphong;

/**
 * Created by wonbokyu on 2017-08-28.
 */

public class InputCash {

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String date;
    public String amount;
    public String remark;
    public String user;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public InputCash() {
    }

    public InputCash(String date, String amount, String remark,String user) {
        this.date = date;
        this.amount = amount;
        this.remark = remark;
        this.user = user;
    }
}

