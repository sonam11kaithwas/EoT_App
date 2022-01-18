package com.eot_app.nav_menu.admin_fw_chat_pkg.chat_single_pkg;

import com.google.firebase.firestore.ListenerRegistration;

/**
 * Created by Sonam-11 on 2020-03-09.
 */
public class AdminChatListner {
    ListenerRegistration listenerRegistration;
    private String userId;

    public AdminChatListner(String userId) {
//        listenerRegistration = FirebaseFirestore.getInstance().document(AdminChatController.getAdminChatInstance()
//                .getAdminGrpChatPath(userId)).
//                addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                        Log.e("", "");
//                    }
//                });
    }

    public String getUserId() {
        return userId;
    }
}
