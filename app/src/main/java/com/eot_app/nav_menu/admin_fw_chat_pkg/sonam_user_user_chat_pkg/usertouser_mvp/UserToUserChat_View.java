package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_mvp;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.GrpUsrlistStatus;

import java.util.ArrayList;

/**
 * Created by Sonam-11 on 7/4/20.
 */
public interface UserToUserChat_View {
    void onSessionExpire(String str);

    void openImage(View thumbView, Drawable bmp, String img_url);

    void userDialog(String str);

    void setUserListInGrp(ArrayList<GrpUsrlistStatus> ids);
}
