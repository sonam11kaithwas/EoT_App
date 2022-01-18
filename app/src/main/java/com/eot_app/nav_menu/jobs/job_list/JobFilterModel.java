package com.eot_app.nav_menu.jobs.job_list;

import com.eot_app.utility.settings.setting_db.TagData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class JobFilterModel {
    String search;
    Set<String> statusModelsList;
    ArrayList<TagData> tagDataList;
    Set<String> jobPriotiesList;

    public JobFilterModel() {
        this.search = "";
        this.statusModelsList = new HashSet<>();
        this.tagDataList = new ArrayList<>();
        this.jobPriotiesList = new HashSet<>();
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        if (search.equals("")) {
            this.search = search;
        } else {
            this.search = "%" + search + "%";
        }
    }

    public Set<String> getStatusModelsList() {
        return statusModelsList;
    }

    public void setStatusModelsList(Set<String> statusModelsList) {
        this.statusModelsList = statusModelsList;
    }

    public ArrayList<TagData> getTagDataList() {
        return tagDataList;
    }

    public void setTagDataList(ArrayList<TagData> tagDataList) {
        this.tagDataList = tagDataList;
    }

    public Set<String> getJobPriotiesList() {
        return jobPriotiesList;
    }

    public void setJobPriotiesList(Set<String> jobPriotiesList) {
        this.jobPriotiesList = jobPriotiesList;
    }
}
