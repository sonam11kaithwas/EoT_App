package com.eot_app.api_call_controller;


import com.google.gson.JsonObject;

public interface ApiCallCallBack {
    void getSuccessResponce(JsonObject jsonObject);

    void getApiErrorResponce(Throwable error);

    void getApiCallComplete();

    //void disableSwipeRefresh();
}
