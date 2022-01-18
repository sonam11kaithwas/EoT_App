package com.eot_app.utility.language_support;

public class Language_Model {
    private String lngId;
    private String fileName;
    private String filePath;
    private String nativeName;
    private String version;
    private boolean selected;

    public String getLngId() {
        return lngId;
    }

    public String getVersion() {
        return version;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getNativeName() {
        return nativeName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
