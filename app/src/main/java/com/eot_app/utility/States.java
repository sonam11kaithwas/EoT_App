package com.eot_app.utility;

/**
 * Created by ubuntu on 30/6/18.
 */

/**
 * Created by ubuntu on 25/6/18.
 */

public class States implements DropdownListBean {
    String id;
    String name;
    String country_id;

    public States(String id, String name, String country_id) {
        this.id = id;
        this.name = name;
        this.country_id = country_id;
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

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }
}
