package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.user_chat_controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.eot_app.R;
import com.eot_app.home_screens.MainActivity;
import com.eot_app.nav_menu.admin_fw_chat_pkg.chatmember_list_pkg.ChatUsersListFragment;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.UserToUserChatActivity;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.MsgModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.NotModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserChatModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserOflineOnlineModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.offlineNotiModel;
import com.eot_app.services.ApiClient;
import com.eot_app.services.ForegroundService2;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.NotificationUtils;
import com.eot_app.utility.db.AppDataBase;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sonam-11 on 7/4/20.
 */
public class UserToUserChatController {
    public static boolean USERSTATUPDATE = false;
    private static UserToUserChatController userChatController;
    private final String FILE_SHARED = "Shared a File";
    private final String[] chatScreenStateArray = new String[]{"", ""};
    private final HashMap<String, UserOflineOnlineModel> user_Status_Model_List = new LinkedHashMap<>();
    List<ChatUserStatusListner> chatUserStatusListnersList = new ArrayList<>();
    HashMap<String, MsgModel> userLastmsgList = new LinkedHashMap<>();
    HashMap<String, Integer> countList = new LinkedHashMap<String, Integer>();
    private UsrTousrChatControListner usrTousrChatControListner;
    private ChatUsersListFragment chatUsersListFragment;
    private MainActivity mainActivity;
    private NotificationUtils nNotificationUtils;
    private boolean state;
    private UserToUserChatActivity userToUserChatActivity;

    public static UserToUserChatController getInstance() {
        if (userChatController == null) {
            userChatController = new UserToUserChatController();
        }
        return userChatController;
    }

    public void set_user_Status_Model_List(String uid, UserOflineOnlineModel userModel) {
        if (user_Status_Model_List.containsKey(uid)) {
            user_Status_Model_List.remove(uid);
            user_Status_Model_List.put(uid, userModel);
        } else {
            user_Status_Model_List.put(uid, userModel);
        }

        try {
            if (uid.equals(App_preference.getSharedprefInstance().getLoginRes().getUsrId()) &&
                    userModel.getIsInactive().equals("1")) {
                if (getMainActivity() != null)
                    getMainActivity().setusersChatListnerS();
            } else if (uid.equals(App_preference.getSharedprefInstance().getLoginRes().getUsrId())

                    && userModel.getIsOnline().equals("0") && !EotApp.getAppinstance().isMyServiceRunning(ForegroundService2.class)
                    && !MainActivity.USERLOGOUT) {
                getMainActivity().setOnlineOfflineToOnline();
            }
        } catch (Exception ex) {
            ex.getMessage();
        }

    }

    public UserOflineOnlineModel get_user_Status_Model_List(String uid) {
        if (user_Status_Model_List.containsKey(uid)) {
            return user_Status_Model_List.get(uid);
        }
        return new UserOflineOnlineModel("", "");
    }

    public MsgModel getUserLastmsgList(String uid) {
        if (userLastmsgList.containsKey(uid)) {
            return userLastmsgList.get(uid);
        }
        return new MsgModel("", "", "");
    }

    public void setUserLastmsgList(String uid, MsgModel msgModel) {
        if (userLastmsgList.containsKey(uid)) {
            userLastmsgList.remove(uid);
            userLastmsgList.put(uid, msgModel);
        } else {
            userLastmsgList.put(uid, msgModel);
        }
    }

    public void clearAllList() {
        userLastmsgList.clear();
        countList.clear();
        user_Status_Model_List.clear();
    }

    public int getCountList(String uid) {
        if (countList.containsKey(uid)) {
            return countList.get(uid).intValue();
        }
        return 0;
    }

    public void setCountList(String uid, int count) {
        if (countList.containsKey(uid)) {
            countList.remove(uid);
            countList.put(uid, count);
        } else {
            countList.put(uid, count);
        }
    }

    public UserToUserChatActivity getUserToUserChatActivity() {
        return userToUserChatActivity;
    }

    /**
     * set for user online/offline status
     **/
    public void setUserToUserChatActivity(UserToUserChatActivity userToUserChatActivity) {
        this.userToUserChatActivity = userToUserChatActivity;
    }

    /**
     * add chat user listner's
     ***/
    public void addUserStatusListner(String uid) {
        boolean isavailableAlready = false;
        for (ChatUserStatusListner listner_item : chatUserStatusListnersList) {
            if (listner_item.getUid().equals(uid)) {
                isavailableAlready = true;
            }
        }
        if (!isavailableAlready) {
            chatUserStatusListnersList.add(new ChatUserStatusListner(uid));
        }
    }


    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /**
     * remove all chat user listner's
     ***/
    public void removeAllChatUserListner() {
        try {
            if (!chatUserStatusListnersList.isEmpty()) {
                for (ChatUserStatusListner item : chatUserStatusListnersList) {
                    FirebaseDatabase.getInstance().getReference().removeEventListener(item.getValueEventListener());
                }
                chatUserStatusListnersList.clear();
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        if (chatUserStatusListnersList != null)
            chatUserStatusListnersList.clear();
    }

    /**
     * remove chat Document listners
     **/
    public void removeChatListner() {
        try {
            if (usrTousrChatControListner != null) {
                List<UsrTousrChatControListner> list = usrTousrChatControListner.getCollRefList();
                for (UsrTousrChatControListner obj : list) {
                    obj.getDocumentListner().remove();
                }
                usrTousrChatControListner.getCollRefList().clear();
            }
        } catch (Exception ex) {
            ex.getMessage();
        }

    }


    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void registerChatUserListner() {
        usrTousrChatControListner =
                new UsrTousrChatControListner();
    }

    public void setChatScreenState(int index, String value) {
        chatScreenStateArray[index] = value;
    }

    public String getChatScreenState(int index) {
        return chatScreenStateArray[index];
    }


    /**
     * get Chat path
     ***/
    public String getChatpath() {
        String path = "";
        path = App_preference.getSharedprefInstance().getRegion()
                + "/comp-"
                + App_preference.getSharedprefInstance().getLoginRes().getCompId() +
                "/chats/";
        return path;
    }

    /****get user path**
     *
     */
    public String getUserpath() {
        String path = "";
        path = App_preference.getSharedprefInstance().getRegion()
                + "/comp-"
                + App_preference.getSharedprefInstance().getLoginRes().getCompId() +
                "/users/";
        return path;
    }


    public void updateCount(String uid) {
        try {
            if (countList.containsKey(uid)) {
                countList.remove(uid);
                countList.put(uid, 0);
            } else {
                countList.put(uid, 0);
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
            e.getMessage();
        }
        if (chatUsersListFragment != null) {
            chatUsersListFragment.getAdapter().notifyDataSetChanged();
        }
        if (UserToUserChatController.getInstance().getMainActivity() != null) {
            UserToUserChatController.getInstance().getMainActivity().setChatbatchCount();
        }
    }

    public ChatUsersListFragment getChatUsersListFragment() {
        return chatUsersListFragment;
    }

    public void setChatUsersListFragment(ChatUsersListFragment chatUsersListFragment) {
        this.chatUsersListFragment = chatUsersListFragment;
    }

    public void updateDrawer() {
        UserToUserChatController.getInstance().getMainActivity().setChatbatchCount();
    }

    public int getTotalCount() {
        int count = 0;
        try {
            if (countList != null) {
                for (Integer i : countList.values()) {
                    i = Integer.valueOf(i.intValue()).intValue();
                    Log.e("", i + "");
                    count = count + i;
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return count;
    }

    /**
     * get user last msgs & time
     ****/

    /****get user online/offline path***/
    public String getUserStatuspath(String uid) {
        String path = App_preference.getSharedprefInstance().getRegion()
                + "/cmp" +
                App_preference.getSharedprefInstance().getLoginRes().getCompId().toLowerCase() + "/users/"
                + "usr" + uid;
        Log.e("error", path);
        return path;
    }

    /***get from firebase meesagging ***/
    public void createChatNotifications(MsgModel model) {

        Log.e("", model.getContent() + model.getSenderId());
        int notIdEmergency = Integer.valueOf(model.getSenderId());
        Intent intent = new Intent(EotApp.getAppinstance(), UserToUserChatActivity.class);
        intent.putExtra("UserCHAT", AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                .userChatModel().getUserById(model.getSenderId()));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        /**add action for samsung device **/
        intent.setAction("" + notIdEmergency);
        intent.setType("ADMINCHAT");
        PendingIntent resultIntent = PendingIntent.getActivity(EotApp.getAppinstance(), notIdEmergency, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (nNotificationUtils == null) {
            nNotificationUtils = new NotificationUtils(EotApp.getAppinstance());
        }

        UserChatModel userChatModel = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).userChatModel().
                getUserById(model.getSenderId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder nb = null;
            if (userChatModel != null) {
                if (model.getContent() != null && !model.getContent().equals("")) {
                    nb = nNotificationUtils.getAndroidChannelNotification(notificationSoundURI, R.mipmap.app_icon, 0, "1000",
                            userChatModel.getFnm(),
                            model.getContent(), resultIntent).setWhen(0);
                    nNotificationUtils.getManager().notify(notIdEmergency, nb.build());
                } else if (model.getDoc() != null && model.getDoc().contains(".jpg") ||
                        model.getDoc().contains(".png") || model.getDoc().contains(".jpeg")) {
                    Notification notification = nNotificationUtils.getNotificationImageExpand(userChatModel.getFnm(),
                            App_preference.getSharedprefInstance().getBaseURL() + model.getDoc(), resultIntent);
                    NotificationManager notificationManager = (NotificationManager) EotApp.getAppinstance().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(notIdEmergency, notification);
                } else {
                    nb = nNotificationUtils.getAndroidChannelNotification(notificationSoundURI, R.mipmap.app_icon, 0, "1000",
                            userChatModel.getFnm(), FILE_SHARED, resultIntent).setWhen(0);
                    nNotificationUtils.getManager().notify(notIdEmergency, nb.build());
                }

            }
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Notification notification = null;
            if (model.getContent() != null && !model.getContent().equals("")) {
                notification = nNotificationUtils.getNotificationManager(R.mipmap.app_icon, userChatModel.getFnm(),
                        model.getContent(), resultIntent);
            } else if (model.getDoc() != null && model.getDoc().contains(".jpg") ||
                    model.getDoc().contains(".png") || model.getDoc().contains(".jpeg")) {
                notification = nNotificationUtils.getNotificationImageExpand(userChatModel.getFnm(),
                        App_preference.getSharedprefInstance().getBaseURL() + model.getDoc(), resultIntent);
            } else {
                notification = nNotificationUtils.getNotificationManager(R.mipmap.app_icon, userChatModel.getFnm(),
                        FILE_SHARED, resultIntent);
            }
            NotificationManager notificationManager = (NotificationManager) EotApp.getAppinstance().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notIdEmergency, notification);
        }
    }

    public void getAllUserOffLineDataList(String msgModel, String noti_recvrid, String docurl) {
        ArrayList<String> userList = new ArrayList<>();
        if (user_Status_Model_List.containsKey(noti_recvrid)) {
            UserOflineOnlineModel userStatus = user_Status_Model_List.get(noti_recvrid);
            if (userStatus.getIsOnline().equals("0")) {
                userList.add(noti_recvrid);
                getUserOffLineList(userList, msgModel, docurl);
            }
        }

    }

    private void getUserOffLineList(final ArrayList<String> userId, final String
            chat_send_Msg_model, String docurl) {

        offlineNotiModel bck_noti_model = new offlineNotiModel(chat_send_Msg_model, docurl, userId);
        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(bck_noti_model));
        ApiClient.getservices().eotServiceCall(Service_apis.sendNotificationToUser,
                AppUtility.getApiHeaders(), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        if (jsonObject.get("success").getAsBoolean()) {
                            Log.e("", "" + jsonObject.toString());
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        Log.e("", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * create offline user notification's
     * *
     *
     * @param model
     */
    public void createofflineChatNotifications(NotModel model) {
        USERSTATUPDATE = true;
        int notIdEmergency = Integer.valueOf(model.getSenderid());
        Intent intent = new Intent(EotApp.getAppinstance(), UserToUserChatActivity.class);
        intent.putExtra("UserCHAT", AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                .userChatModel().getUserById(model.getSenderid()));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        /**add action for samsung device **/
        intent.setAction("" + notIdEmergency);
        intent.setType("ADMINCHAT");
        //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent resultIntent = PendingIntent.getActivity(EotApp.getAppinstance(), notIdEmergency, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (nNotificationUtils == null) {
            nNotificationUtils = new NotificationUtils(EotApp.getAppinstance());
        }

        UserChatModel userChatModel = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).userChatModel().
                getUserById(model.getSenderid());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder nb = null;
            if (userChatModel != null) {
                if (model.getMsg() != null && model.getMsg().equals("📷 Photo")) {
                    Notification notification = nNotificationUtils.getNotificationImageExpand(userChatModel.getFnm(),
                            App_preference.getSharedprefInstance().getBaseURL() + model.getMsgUrl(), resultIntent);
                    NotificationManager notificationManager = (NotificationManager) EotApp.getAppinstance().
                            getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(notIdEmergency, notification);
                } else if (model.getMsg() != null && model.getMsg().equals("📎 Attachment")) {
                    nb = nNotificationUtils.getAndroidChannelNotification(notificationSoundURI, R.mipmap.app_icon, 0, "1000",
                            userChatModel.getFnm(), FILE_SHARED, resultIntent).setWhen(0);
                    nNotificationUtils.getManager().notify(notIdEmergency, nb.build());
                } else {
                    nb = nNotificationUtils.getAndroidChannelNotification(notificationSoundURI, R.mipmap.app_icon, 0, "1000",
                            userChatModel.getFnm(),
                            model.getMsg(), resultIntent).setWhen(0);
                    nNotificationUtils.getManager().notify(notIdEmergency, nb.build());
                }
            }
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Notification notification = null;
            if (model.getMsg() != null && model.getMsg().equals("📷 Photo")) {
                notification = nNotificationUtils.getNotificationImageExpand(userChatModel.getFnm(),
                        App_preference.getSharedprefInstance().getBaseURL() + model.getMsgUrl(), resultIntent);
            } else if (model.getMsg() != null && model.getMsg().equals("📎 Attachment")) {
                notification = nNotificationUtils.getNotificationManager(R.mipmap.app_icon, userChatModel.getFnm(),
                        FILE_SHARED, resultIntent);
            } else {
                notification = nNotificationUtils.getNotificationManager(R.mipmap.app_icon, userChatModel.getFnm(),
                        model.getMsg(), resultIntent);
            }
            NotificationManager notificationManager = (NotificationManager) EotApp.getAppinstance().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notIdEmergency, notification);
        }


    }
}
