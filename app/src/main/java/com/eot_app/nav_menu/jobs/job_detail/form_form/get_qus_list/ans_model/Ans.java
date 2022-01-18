package com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model;

/**
 * Created by ubuntu on 19/9/18.
 */

public class Ans {
    private String key;
    private String value;

    public Ans(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
