package com.eot_app.nav_menu.jobs.job_detail.chat.fb_mvp;

import android.net.Uri;

import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.chat.fire_Base_Model.Chat_Send_Msg_Model;

public interface Chat_Pi {
    void sendMessages(Chat_Send_Msg_Model chat_send_Msg_model);

    void getmeUrl(String image_url, String type, Job jobData);

    void uploadActualImageOnFireStore(Uri uri, Job jobData);

}
