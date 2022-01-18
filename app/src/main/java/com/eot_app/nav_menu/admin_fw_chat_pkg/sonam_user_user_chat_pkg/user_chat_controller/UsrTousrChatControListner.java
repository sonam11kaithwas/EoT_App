package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.user_chat_controller;

import android.util.Log;

import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.MsgModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserChatModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserMsgSend;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserOflineOnlineModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UsersModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UsersModelForgrp;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.ChatApp_Preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Nullable;

/**
 * Created by Sonam-11 on 10/4/20.
 */

public class UsrTousrChatControListner {
    private final List<UsrTousrChatControListner> collRefList = new ArrayList<>();
    ListenerRegistration documentListner;
    ArrayList<String> usersIdList = new ArrayList<>();
    HashMap<String, String> list = new LinkedHashMap<>();
    String usrIds;
    String docId;

    /*** craete listner for user Node get document id's for Users***/
    public UsrTousrChatControListner() {
        FirebaseFirestore.getInstance()
                .collection(UserToUserChatController.getInstance().getUserpath())
                .whereArrayContains("users", App_preference.getSharedprefInstance().getLoginRes().getUsrId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        Log.e("", "");
                        if (queryDocumentSnapshots != null) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                Log.e("document data", document.getId() + " => " + document.getData());
                                UsersModel usersList = null;
                                try {
                                    usersList = document.toObject(UsersModel.class);
                                } catch (Exception e1) {
                                    e1.getMessage();
                                }
                                if (usersList != null) {
                                    for (String uId : usersList.getUsers()) {
                                        if (!uId.equals(App_preference.getSharedprefInstance().getLoginRes().getUsrId())) {
                                            if (!usersIdList.contains(uId)) {
                                                /***fillter from server user's***/
                                                usersIdList.add(uId);
                                            }
                                            /***very IMPORTANT*/
                                            addListnerForDocument(uId, document.getId());
                                            break;
                                        }
                                    }
                                }

                            }
                        }
                        UsrTousrChatControListner();


                    }
                });
        Log.e("", "");
    }


    public UsrTousrChatControListner(String uId, String docid) {
        this.usrIds = uId;
        this.docId = docid;
        documentListner = FirebaseFirestore.getInstance().collection(UserToUserChatController.getInstance()
                .getChatpath())
                .document(docId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        Log.e("", "");
                        UserMsgSend fireBase_req_model2 = null;
                        try {
                            UserOflineOnlineModel userStatusModel = UserToUserChatController.getInstance().get_user_Status_Model_List(usrIds);
                            if (!userStatusModel.getIsInactive().equals("1")) {
                                fireBase_req_model2 = documentSnapshot.toObject(UserMsgSend.class);
                                if (fireBase_req_model2 != null) {
                                    addUsersCount(fireBase_req_model2.getMessages().size(), usrIds, fireBase_req_model2.getMessages());
                                } else {
                                    addUsersCount(0, usrIds, new ArrayList<MsgModel>());
                                }
                            }
                        } catch (Exception ex) {
                            Log.e("FireBase ", "" + ex.getMessage());
                        }
                    }
                });
    }


    /******Craete node and get DOCUMENT id for Group chat*******/
    public void UsrTousrChatControListner() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore.getInstance()
                        .collection(UserToUserChatController.getInstance().getUserpath())
                        .whereArrayContains("groups", App_preference.getSharedprefInstance().getLoginRes().getUsrId())
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                Log.e("", "");
                                try {
                                    if (queryDocumentSnapshots != null) {
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            Log.e("document data", document.getId() + " => " + document.getData());
                                            UsersModelForgrp usersList = null;
                                            try {
                                                usersList = document.toObject(UsersModelForgrp.class);
                                            } catch (Exception e1) {
                                                e1.getMessage();
                                            }
                                            if (usersList != null) {
                                                for (Object uId : usersList.getGroups()) {
                                                    if (!uId.equals(App_preference.getSharedprefInstance().getLoginRes().getUsrId())) {
                                                        if (!usersIdList.contains(uId)) {
                                                            /***fillter from server user's***/
                                                            usersIdList.add((String) uId);
                                                            Log.e("", "");
                                                        }
                                                        /***very IMPORTANT*/
                                                        Log.e("", "");
                                                        if (((HashMap) usersList.getGroups().get(usersList.getGroups().size() - 1)).containsKey("grpId")) {
                                                            Object s = ((HashMap) usersList.getGroups().get(usersList.getGroups().size() - 1)).get("grpId");
                                                            String[] strArray = ((String) s).split("grp-");
                                                            addListnerForDocument(strArray[1], document.getId());
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });


        Log.e("", "");
    }

    private void addListnerForDocument(String uId, String docId) {
        boolean isavailableAlready = false;
        for (UsrTousrChatControListner listner_item : collRefList) {
            if (listner_item.getUsrIds().equals(uId)) {
                isavailableAlready = true;
            }
        }
        if (!isavailableAlready) {
            collRefList.add(new UsrTousrChatControListner(uId, docId));
        }
    }

    public List<UsrTousrChatControListner> getCollRefList() {
        return collRefList;
    }


    public String getUsrIds() {
        return usrIds;
    }

    public void setUsrIds(String usrIds) {
        this.usrIds = usrIds;
    }

    public HashMap<String, String> getList() {
        return list;
    }

    public void setList(HashMap<String, String> list) {
        this.list = list;
    }

    public ListenerRegistration getDocumentListner() {
        return documentListner;
    }

    private void addUsersCount(int msgSize, final String uIds, final List<MsgModel> messagesList) {
        boolean lastTwoDays = ChatApp_Preference.getSharedprefInstance().getChatData();
        final UserChatModel userChatModel = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).userChatModel().getUserById(uIds);
        /**first login execute***/
        if (!lastTwoDays) {
            /***get only last two Day's Mgs's on First time Appp luanch****/
            int count = 0;
            String befaoreTwoDaysDate = AppUtility.getYesterDayDate("dd-MM-yyyy hh:mm a", -2);
            for (MsgModel msgModel : messagesList) {
                String msgDate = AppUtility.getDate(Long.parseLong(msgModel.getCreatedAt()), "dd-MM-yyyy hh:mm a");
                if (AppUtility.compareTwoDates(befaoreTwoDaysDate, msgDate)) {
                    if (!msgModel.getSenderId().equals(App_preference.getSharedprefInstance().getLoginRes().getUsrId())) {
                        count = count + 1;
                    }
                }
            }
            msgSize = count;
            if (!UserToUserChatController.getInstance().getChatScreenState(0).equals(uIds)) {
                UserToUserChatController.getInstance().setCountList(uIds, msgSize);
            }
        } else {
            if (userChatModel != null) {
                if (userChatModel.getReadCount() == 0) {
                    /***filter for sender count ***/

                    int count = 0;
                    String befaoreTwoDaysDate = AppUtility.getYesterDayDate("dd-MM-yyyy hh:mm a", -2);
                    for (MsgModel msgModel : messagesList) {
                        String msgDate = AppUtility.getDate(Long.parseLong(msgModel.getCreatedAt()),
                                "dd-MM-yyyy hh:mm a");
                        if (AppUtility.compareTwoDates(befaoreTwoDaysDate, msgDate)) {
                            if (!msgModel.getSenderId().equals(App_preference.getSharedprefInstance()
                                    .getLoginRes().getUsrId())) {
                                count = count + 1;
                            }
                        }
                    }
                    msgSize = count;
                } else {
                    msgSize = msgSize - Integer.valueOf(userChatModel.getReadCount());
                }
            }
            if (!UserToUserChatController.getInstance().getChatScreenState(0).equals(uIds)) {
                UserToUserChatController.getInstance().setCountList(uIds, msgSize);
            }
        }

        /***
         * Add last msg in List
         * *****/
        if (messagesList.size() > 0) {
            UserToUserChatController.getInstance().setUserLastmsgList(uIds, messagesList.get(messagesList.size() - 1));
        }

        /*** **craete notification's***/


        try {
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        int COUNT = 0;
                        if (!UserToUserChatController.getInstance().getChatScreenState(0).equals(uIds)) {
                            if (userChatModel != null) {
                                if (messagesList.size() >= 1) {
                                    for (int i = messagesList.size(); i > userChatModel.getReadCount(); i--) {
                                        if (!messagesList.get(i - 1).getSenderId().
                                                equals(App_preference.getSharedprefInstance().getLoginRes().
                                                        getUsrId())) {
                                            if (COUNT == 1) {
                                                break;
                                            } else {
                                                String befaoreTwoDaysDate = AppUtility.getYesterDayDate("dd-MM-yyyy hh:mm a", -2);
                                                String msgDate = AppUtility.getDate(Long.parseLong(messagesList.get(i - 1).getCreatedAt()),
                                                        "dd-MM-yyyy hh:mm a");
                                                if (AppUtility.compareTwoDates(befaoreTwoDaysDate, msgDate)) {
                                                    UserToUserChatController.getInstance().
                                                            createChatNotifications(messagesList.get(messagesList.size() - 1));
                                                    COUNT++;
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });
        } catch (Exception exception) {
            exception.getMessage();
        }


        /**update count & Lat msg when user on User chalist ****/
        if (UserToUserChatController.getInstance().getChatUsersListFragment() != null && UserToUserChatController.getInstance().isState()) {
            UserToUserChatController.getInstance().getChatUsersListFragment().updateListItemByLastMsg(uIds);
        }
        if (UserToUserChatController.getInstance().getMainActivity() != null) {
            UserToUserChatController.getInstance().getMainActivity().setChatbatchCount();
        }

    }

}


