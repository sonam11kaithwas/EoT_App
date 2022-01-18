package com.eot_app.utility;

public class Language implements DropdownListBean {
    private String lngId;
    private String nativeName;
    private String languageCode;

    public Language(String lngId, String nativeName, String languageCode) {
        this.lngId = lngId;
        this.nativeName = nativeName;
        this.languageCode = languageCode;
    }

    public String getLngId() {
        return lngId;
    }

    public void setLngId(String lngId) {
        this.lngId = lngId;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getName() {

        return nativeName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "name='" + nativeName + '\'' +
                '}';
    }
}
