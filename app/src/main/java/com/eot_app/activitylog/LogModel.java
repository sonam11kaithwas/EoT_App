package com.eot_app.activitylog;

public class LogModel {
    int device = 4;
    String module;
    String msg;

    public LogModel(String module, String msg) {
        this.module = module;
        this.msg = msg;
    }
}
