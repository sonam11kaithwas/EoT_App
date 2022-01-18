package com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model;

import com.eot_app.utility.DropdownListBean;

import java.io.Serializable;

/**
 * Created by aplite_pc302 on 6/23/18.
 */

public class JobStatusModel implements DropdownListBean, Serializable {
    private String status_no = "0";
    private String status_name = "Not_Started";
    private String img;

    public JobStatusModel(String status_no, String status_name) {
        this.status_no = status_no;
        this.status_name = status_name;
    }

    public JobStatusModel(String status_no, String status_name, String img) {
        this.status_no = status_no;
        this.status_name = status_name;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus_no() {
        return status_no;
    }

    public void setStatus_no(String status_no) {
        this.status_no = status_no;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    @Override
    public String getKey() {
        return getStatus_no();
    }

    @Override
    public String getName() {
        return getStatus_name();
    }

   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobStatusModel)) return false;
        JobStatusModel that = (JobStatusModel) o;
        return Objects.equals(getStatus_name(), that.getStatus_name());
    }

    @Override
    public int hashCode() {
        //return Objects.hash(getStatus_no(), getStatus_name(), img);
        int result = getStatus_name().hashCode();
        result = 31 * result + (getStatus_name() != null ? getStatus_name().hashCode() : 0);
        return result;
    }*/
}
