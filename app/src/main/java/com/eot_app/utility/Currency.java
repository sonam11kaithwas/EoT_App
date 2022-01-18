package com.eot_app.utility;

public class Currency implements DropdownListBean {
    String symbol;
    String text;
    String id;

    public Currency(String text, String id) {
        this.text = text;
        this.id = id;
    }

    @Override
    public String getKey() {
        return id;
    }

    @Override
    public String getName() {
        return text;
    }

    public void setName(String text) {
        this.text = text;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "name='" + text + '\'' +
                '}';
    }
}
