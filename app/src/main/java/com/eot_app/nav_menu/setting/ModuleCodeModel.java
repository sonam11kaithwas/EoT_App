package com.eot_app.nav_menu.setting;

/**
 * Created by Mahendra Dabi on 01-09-2020.
 */


public class ModuleCodeModel implements ModuleCode {

    private final int code;
    private final String text;

    public ModuleCodeModel(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }


}
