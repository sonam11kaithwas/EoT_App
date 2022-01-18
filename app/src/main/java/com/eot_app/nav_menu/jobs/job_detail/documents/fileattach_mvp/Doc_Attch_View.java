package com.eot_app.nav_menu.jobs.job_detail.documents.fileattach_mvp;

import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;

import java.util.ArrayList;

/**
 * Created by ubuntu on 8/10/18.
 */

public interface Doc_Attch_View {
    void selectFile();

    void setList(ArrayList<GetFileList_Res> getFileList_res, String isAttachCompletionNotes);

    void addView();

    void onSessionExpire(String msg);

    void fileExtensionNotSupport(String msg);

    void onDocumentUpdate(String msg, boolean isSuccess);

    void hideProgressBar();

    //   void setUploadNewDocList(ArrayList<GetFileList_Res> getFileList_res);
}
