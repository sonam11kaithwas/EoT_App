package com.eot_app.nav_menu.expense.expense_list.model;

import java.io.Serializable;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ExpenseResModel implements Comparable, Serializable {
    private String expId;
    private String name;
    private String amt;
    private String dateTime;
    private String category;
    private String tag;
    private String status;
    private String des;


    public String getExpId() {
        return expId;
    }

    public void setExpId(String expId) {
        this.expId = expId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Override
    public int compareTo(Object compare) {
        try {
            int compareage = Integer.parseInt(((ExpenseResModel) compare).getDateTime());
            //* For Descending order do like this *//*
            return compareage - (Integer.parseInt(this.dateTime));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }


}
