package com.eot_app.utility;

public class Server_location implements DropdownListBean {
    private String apiCode;
    private String text;

    public Server_location(String apiCode, String text) {
        this.apiCode = apiCode;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getApiCode() {
        return apiCode;
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getName() {
        return text;
    }


    @Override
    public String toString() {
        return "Currency{" +
                "name='" + text + '\'' +
                '}';
    }
}
