package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.user_chat_controller;

import androidx.annotation.NonNull;

import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserOflineOnlineModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Sonam-11 on 15/4/20.
 */
public class ChatUserStatusListner {
    private final ValueEventListener valueEventListener;
    private final String uid;

    public ChatUserStatusListner(final String uId) {
        this.uid = uId;
        valueEventListener =
                FirebaseDatabase.getInstance().getReference(
                        UserToUserChatController.getInstance().getUserStatuspath(uid)
                ).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            String isonline = "", isInactive = "";
                            if (dataSnapshot != null && (dataSnapshot.child("isOnline").getValue()) != null
                                    && dataSnapshot.child("isInactive").getValue() != null) {
                                isonline = (dataSnapshot.child("isOnline").getValue()).toString();
                                isInactive = (dataSnapshot.child("isInactive").getValue()).toString();
                                UserToUserChatController.getInstance().set_user_Status_Model_List(uId, new UserOflineOnlineModel((dataSnapshot.child("isOnline").getValue()).toString()
                                        , (dataSnapshot.child("isInactive").getValue()).toString()));
                            } else if (dataSnapshot != null && (dataSnapshot.child("isOnline").getValue()) != null) {
                                isonline = (dataSnapshot.child("isOnline").getValue()).toString();
                                UserToUserChatController.getInstance().set_user_Status_Model_List(uId, new UserOflineOnlineModel((dataSnapshot.child("isOnline").getValue()).toString()
                                        , "0"));
                            } else if (dataSnapshot != null && (dataSnapshot.child("isInactive").getValue()) != null) {
                                isInactive = (dataSnapshot.child("isInactive").getValue()).toString();
                                UserToUserChatController.getInstance().set_user_Status_Model_List(uId, new UserOflineOnlineModel("0"
                                        , (dataSnapshot.child("isInactive").getValue()).toString()));
                            } else {
                                if ((dataSnapshot.child("isOnline").getValue()) == null) {
                                    isonline = "0";
                                }
                                if (dataSnapshot.child("isInactive").getValue() == null) {
                                    isInactive = "0";
                                }
                                UserToUserChatController.getInstance().set_user_Status_Model_List(uId, new UserOflineOnlineModel(isonline
                                        , isInactive));
                            }


                            /****update user states & user Active/Inactive state's on chat window****/
                            if (UserToUserChatController.getInstance().getChatScreenState(1).equals(getUid())
                                    && UserToUserChatController.getInstance().getUserToUserChatActivity() != null) {
                                UserToUserChatController.getInstance().getUserToUserChatActivity().
                                        updateUserStatus(new UserOflineOnlineModel(isonline, isInactive
                                        ));
                            }
                            /***update states on user list list screen***/
                            if (UserToUserChatController.getInstance().getChatUsersListFragment() != null &&
                                    UserToUserChatController.getInstance().isState()) {
                                UserToUserChatController.getInstance().getChatUsersListFragment().updateListUserStatus((new UserOflineOnlineModel(
                                        isonline, isInactive
                                )), uid);
                            }
                        } catch (Exception ex) {
                            ex.getMessage();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    public String getUid() {
        return uid;
    }

    public ValueEventListener getValueEventListener() {
        return valueEventListener;
    }
}
