package com.eot_app.utility;

/**
 * Created by ubuntu on 25/6/18.
 */

public class Country implements DropdownListBean {
    private String id;
    private String name;
    private String timezone;
    private String currency;
    private String languageCode;
    private String currency_format;


    public Country(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * This use for registration form
     ******/
    public Country(String id, String name, String timezone, String currency, String languageCode, String currency_format) {
        this.id = id;
        this.name = name;
        this.timezone = timezone;
        this.currency = currency;
        this.languageCode = languageCode;
        this.currency_format = currency_format;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getTimezone() {
        return timezone;
    }

    public String getCurrency() {
        return currency;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getCurrency_format() {
        return currency_format;
    }
}
