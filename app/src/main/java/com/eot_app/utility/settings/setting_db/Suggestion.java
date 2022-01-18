package com.eot_app.utility.settings.setting_db;

public class Suggestion {
    public String suggId;
    public String compId;
    public String jtId;
    public String jtDesSugg;
    public String complNoteSugg;
    public String createdDate;
    public String updateDate;

    public Suggestion() {
    }

    public String getSuggId() {
        return suggId;
    }

    public void setSuggId(String suggId) {
        this.suggId = suggId;
    }


    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getJtId() {
        return jtId;
    }

    public void setJtId(String jtId) {
        this.jtId = jtId;
    }

    public String getJtDesSugg() {
        return jtDesSugg;
    }

    public void setJtDesSugg(String jtDesSugg) {
        this.jtDesSugg = jtDesSugg;
    }

    public String getComplNoteSugg() {
        return complNoteSugg;
    }

    public void setComplNoteSugg(String complNoteSugg) {
        this.complNoteSugg = complNoteSugg;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
