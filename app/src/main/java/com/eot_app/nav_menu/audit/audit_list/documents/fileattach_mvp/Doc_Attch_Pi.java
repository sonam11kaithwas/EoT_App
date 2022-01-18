package com.eot_app.nav_menu.audit.audit_list.documents.fileattach_mvp;

/**
 * Created by ubuntu on 8/10/18.
 */

public interface Doc_Attch_Pi {
    void getAttachFileList(String jobId);

    void uploadDocuments(String job_Id, String file, String finalFname);
}
