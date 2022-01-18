package com.eot_app.frgt_pass.frgt_mvp;

/**
 * Created by ubuntu on 17/7/18.
 */

public interface Frgt_View {
    void setFrgtEmail(String msg);

    void setKey();

    void newPassViewSet();

    void passwordError(String error);

    void frgtDialogFinish();
}
