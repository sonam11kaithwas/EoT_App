package com.eot_app.nav_menu.audit.audit_list.documents.fileattach_mvp;

import com.eot_app.nav_menu.audit.audit_list.documents.doc_model.GetFileList_Res;

import java.util.ArrayList;

/**
 * Created by ubuntu on 8/10/18.
 */

public interface Doc_Attch_View {
    void selectFile();

    void setList(ArrayList<GetFileList_Res> getFileList_res);

    void addView();

    void onSessionExpire(String msg);

    void fileExtensionNotSupport(String msg);
}
