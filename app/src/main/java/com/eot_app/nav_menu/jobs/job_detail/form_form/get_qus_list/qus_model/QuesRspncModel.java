package com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ubuntu on 18/9/18.
 */

public class QuesRspncModel implements Serializable {
    private String queId;
    private String parentId;
    private String parentAnsId;
    private String frmId;
    private String des;
    private String type;
    private String mandatory;
    private boolean flag = false;
    private List<OptionModel> opt = null;
    private List<AnswerModel> ans = null;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getQueId() {
        return queId;
    }

    public void setQueId(String queId) {
        this.queId = queId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentAnsId() {
        return parentAnsId;
    }

    public void setParentAnsId(String parentAnsId) {
        this.parentAnsId = parentAnsId;
    }

    public String getFrmId() {
        return frmId;
    }

    public void setFrmId(String frmId) {
        this.frmId = frmId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public List<OptionModel> getOpt() {
        return opt;
    }

    public void setOpt(List<OptionModel> opt) {
        this.opt = opt;
    }

//    public AnswerModel getAns() {
//        return ans;
//    }

//    public void setAns(AnswerModel ans) {
//        this.ans = ans;
//    }
//

    public List<AnswerModel> getAns() {
        return ans;
    }

    public void setAns(List<AnswerModel> ans) {
        this.ans = ans;
    }
}
