package com.eot_app.nav_menu.appointment.list.common;

public class CommonAppointmentModel implements Comparable {
    private int type;
    private String id;
    private String tempId;
    private String startDateTime;
    private String endDateTime;
    private String title;
    private String des;
    private String nm;
    private String jobId;
    private String status;
    private int jobItemCount = 0;
    private int equipmentCount;
    private int attchmentCount;

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public int getAttchmentCount() {
        return attchmentCount;
    }

    public void setAttchmentCount(int attchmentCount) {
        this.attchmentCount = attchmentCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }


    @Override
    public int compareTo(Object compareTime) {
        /* For Descending order do like this */
        try {
            String startDateTime = ((CommonAppointmentModel) compareTime).getStartDateTime();
            int start = Integer.parseInt(startDateTime);
            int current = Integer.parseInt(this.startDateTime);
            return start - current;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getJobItemCount() {
        return jobItemCount;
    }

    public void setJobItemCount(int jobItemCount) {
        this.jobItemCount = jobItemCount;
    }

    public int getEquipmentCount() {
        return equipmentCount;
    }

    public void setEquipmentCount(int equipmentCount) {
        this.equipmentCount = equipmentCount;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
