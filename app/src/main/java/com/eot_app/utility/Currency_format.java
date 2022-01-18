package com.eot_app.utility;

public class Currency_format implements DropdownListBean {
    private String id;
    private String text;

    public Currency_format(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getName() {
        return text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Currency_format{" +
                "name='" + text + '\'' +
                '}';
    }
}
