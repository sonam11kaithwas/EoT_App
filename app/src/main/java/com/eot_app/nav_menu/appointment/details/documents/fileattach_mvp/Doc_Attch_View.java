package com.eot_app.nav_menu.appointment.details.documents.fileattach_mvp;

import com.eot_app.nav_menu.appointment.details.AppointmentAttachment;

import java.util.ArrayList;

/**
 * Created by ubuntu on 8/10/18.
 */

public interface Doc_Attch_View {
    void selectFile();

    void setList(ArrayList<AppointmentAttachment> getFileList_res);

    void addView();

    void onSessionExpire(String msg);

    void fileExtensionNotSupport(String msg);

    void onDocumentUpdate(String msg, boolean isSuccess);
}
