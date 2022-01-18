package com.eot_app.nav_menu.client_chat_pkg.client_chat_mvp;

import android.net.Uri;

import com.eot_app.nav_menu.client_chat_pkg.client_chat_model.ClientChatReqModel;
import com.eot_app.nav_menu.jobs.job_db.Job;

/**
 * Created by Sonam-11 on 2020-01-02.
 */
public interface ClientChat_PI {

    void sendCLientFwMessage(ClientChatReqModel chatReqModel);

    void uploadActualImageOnFireStore(Uri uri, Job jobData);
}
