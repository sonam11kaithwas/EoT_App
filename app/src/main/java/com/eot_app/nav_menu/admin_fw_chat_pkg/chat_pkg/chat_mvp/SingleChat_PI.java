package com.eot_app.nav_menu.admin_fw_chat_pkg.chat_pkg.chat_mvp;

import android.net.Uri;

import com.eot_app.nav_menu.admin_fw_chat_pkg.chat_pkg.model_pkg.SingleChatModel;

/**
 * Created by Sonam-11 on 2020-03-07.
 */

public interface SingleChat_PI {
    void sendChatMessages(SingleChatModel chatModel);

    void uploadActualImageOnFireStore(Uri uri);
}
