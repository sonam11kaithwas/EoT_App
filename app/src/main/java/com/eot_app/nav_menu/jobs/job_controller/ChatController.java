package com.eot_app.nav_menu.jobs.job_controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.eot_app.R;
import com.eot_app.login_next.FireStoreUserOnlineModel;
import com.eot_app.nav_menu.client_chat_pkg.client_chat_model.ClientChatReqModel;
import com.eot_app.nav_menu.client_chat_pkg.client_chat_model.SendCLientAdminNotifica;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.JobDetailActivity;
import com.eot_app.nav_menu.jobs.job_detail.chat.fire_Base_Model.Chat_Send_Msg_Model;
import com.eot_app.nav_menu.jobs.job_detail.chat.fire_Base_Model.FirestoreFieldWorkerModel;
import com.eot_app.nav_menu.jobs.job_list.JobList;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.NotificationUtils;
import com.eot_app.utility.db.AppDataBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aplite_pc302 on 1/8/19.
 */

public class ChatController {
    private static ChatController chatController;
    private final String TAG = "ChatController";
    private final String CHAT_TYPE_IMAGE = "image";
    private final String FILE_SHARED = "Shared a File";
    private final String fwKeyCode = "usr-";
    private final List<ChatListnersContainer> collRefList = new ArrayList<>(); // list of event listeners.
    private final Set<Job> pathlist = new HashSet<>(); // for unique path
    /****
     * chatScreenStateArray is used for Manage Notification and show and hide counter budge
     * chatScreenStateArray[0] position specify JobDetailActivity.
     * chatScreenStateArray[1] position specify ChatFragment.
     * chatScreenStateArray[2] position specify ChatFragment states.
     * */
    private final String[] chatScreenStateArray = new String[]{"", "", "", ""};
    private final String[] clientChatStateArray = new String[]{"", "", ""};
    private NotificationUtils nNotificationUtils;
    private JobList joblistListener;
    private JobDetailActivity jobdetailListener;

    public static ChatController getInstance() {
        if (chatController == null) {
            chatController = new ChatController();
        }
        return chatController;
    }

    /***set job chat state's***/
    public void setChatScreenState(int index, String value) {
        chatScreenStateArray[index] = value;
    }


    /*** set client chat state'S ****/
    public void setClientChatState(int clientChatState, String value) {
        clientChatStateArray[clientChatState] = value;
    }

    /***set job chat screen state for Fragment ****/
    public String getChatScreenState(int index) {
        return chatScreenStateArray[index];
    }

    /*** get client chat state for Fragment ***/
    public String getClientChatFragState(int position) {
        return clientChatStateArray[position];
    }

    public JobDetailActivity getJobdetailListener() {
        return jobdetailListener;
    }


    public void setJobdetailListener(JobDetailActivity jobdetailListener) {
        this.jobdetailListener = jobdetailListener;
    }

    public JobList getJoblistListener() {
        return joblistListener;
    }

    public void setJoblistListener(JobList joblistListener) {
        this.joblistListener = joblistListener;
    }

    /*** Unread count for Admin msg ****/
    public void setUnreadCountZeroByJobId(String jobCode, final String jobId, int clUnread) {
        FirestoreFieldWorkerModel model = new FirestoreFieldWorkerModel();
        model.setUnread(0);
        model.setCltunread(clUnread);
        FirebaseFirestore.getInstance().document(getFieldworkerpath(jobCode, jobId)).
                set(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "set read 0");
            }
        });
    }


    /**** Count update for Client *****/
    public void setClientUnreadCountZeroByJobId(String jobCode, final String jobId, int inreadBatch) {
        FirestoreFieldWorkerModel model = new FirestoreFieldWorkerModel();
        model.setUnread(inreadBatch);
        model.setCltunread(0);
        FirebaseFirestore.getInstance().document(getFieldworkerpath(jobCode, jobId)).
                set(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "set read 0");
            }
        });
    }

    public void initializeUnreadFromJobID(String jobCode, final String jobId, List<String> userList) {
        FirestoreFieldWorkerModel model = new FirestoreFieldWorkerModel();
        model.setUnread(0);
        //  model.setCltunread(0);
        WriteBatch batch = FirebaseFirestore.getInstance().batch();
        for (String fwId : userList) {
            DocumentReference fw_ref = FirebaseFirestore.getInstance().document(getFieldworkerpathById(jobCode, jobId, fwId));
            batch.set(fw_ref, model);
        }
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "=>> committed");
            }
        });
    }

    /****/
    private String getFieldworkerpathById(String jobCode, String jobId, String fwId) {
        String fwPath = "";
        //**this path for new job**//*
        if (Integer.valueOf(jobId) >= App_preference.getSharedprefInstance().getLoginRes().getStaticJobId()) {
            fwPath = App_preference.getSharedprefInstance().getRegion()
                    + "/comp-"
                    + App_preference.getSharedprefInstance().getLoginRes().getCompId()
                    + "/job/"
                    + jobId
                    + "/users/usr-" + fwId;
        } else {
            fwPath = App_preference.getSharedprefInstance().getRegion()
                    + "/comp-"
                    + App_preference.getSharedprefInstance().getLoginRes().getCompId()
                    + "/job/"
                    + jobCode
                    + "-"
                    + jobId
                    + "/users/usr-" + fwId;

        }
        return fwPath;
    }

    /**
     * READ UNREAD COUNT FOR ALL FW for Job Chat
     ***/
    public void increseUnreadCountforAll(final String jobCode, final String jobId) {
        FirebaseFirestore.getInstance().collection(getFieldworkersCollectionPath(jobCode, jobId))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Get a new write batch
                            WriteBatch batch = FirebaseFirestore.getInstance().batch();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (!document.getId().equals(fwKeyCode + App_preference.getSharedprefInstance().getLoginRes().getUsrId())) {
                                    FirestoreFieldWorkerModel fwmodel = document.toObject(FirestoreFieldWorkerModel.class);
                                    /*** set job Unread Count ****/
                                    fwmodel.setUnread(fwmodel.getUnread() + 1);
                                    // fwmodel.setCltunread(fwmodel.getCltunread() + 1);

                                    batch.set(document.getReference(), fwmodel);
                                }
                            }
                            // Commit the batch
                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "=>> committed");
                                }
                            });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    /**
     * job unread count & craete notification for Job
     ***/
    void getUnreadMessagesByJobId(String jobcode, String jobId, final int limit) {
        FirebaseFirestore.getInstance().collection(getChatPath(jobcode, jobId)).orderBy("time",
                Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() > 0) {
                    final Chat_Send_Msg_Model noti_model = queryDocumentSnapshots.getDocuments().
                            get(queryDocumentSnapshots.size() - 1).toObject(Chat_Send_Msg_Model.class);
                    if (noti_model != null) {

                        //  if (!ChatFragment.NOTIFISSUE.equals("ChatFragment")) {
                        /**** Create Job Chat Notificatio ***/
                        ExecutorService service = Executors.newSingleThreadExecutor();
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                createNotificationforChat(noti_model);
                            }
                        });
                        //}
                    }
                }
            }
        });
    }

    /**
     * job unread count & craete notification for Client Chat Job
     ***/

    void getUnreadMessagesByClientJobFwId(String jobId) {
        FirebaseFirestore.getInstance().collection(getClientChatPath(jobId)).orderBy("time",
                Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener
                (new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() > 0) {
                            ClientChatReqModel noti_model = queryDocumentSnapshots.getDocuments().
                                    get(queryDocumentSnapshots.size() - 1).toObject(ClientChatReqModel.class);
                            if (noti_model != null) {
                                /**** Create Job Chat Notificatio ***/
                                createClientChatNotificationforChat(noti_model);
                            }
                        }
                    }
                });
    }


    /**
     * create notification for only chat
     **/
    public void createNotificationforChat(Chat_Send_Msg_Model model) {
        Log.e("", model.getMsg() + model.getUsrid());
        int notIdEmergency = Integer.valueOf(model.getJobId());
        Intent intent = new Intent(EotApp.getAppinstance(), JobDetailActivity.class);
        Log.e("set parceble", "");
        String str = new Gson().toJson(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(model.getJobId()));
        intent.putExtra("CHAT_JOB", str);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        /**add action for samsung device **/
        intent.setAction("" + notIdEmergency);
        intent.setType("ADMINCHAT");
        //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent resultIntent = PendingIntent.getActivity(EotApp.getAppinstance(), notIdEmergency, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (nNotificationUtils == null) {
            nNotificationUtils = new NotificationUtils(EotApp.getAppinstance());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder nb = null;

            if (model.getType().equals("1")) { //for text msgs
                nb = nNotificationUtils.getAndroidChannelNotification(notificationSoundURI, R.mipmap.app_icon, 0, "1000",
                        model.getUsrnm(), model.getMsg(), resultIntent).setWhen(0);
                nNotificationUtils.getManager().notify(notIdEmergency, nb.build());
            } else if (model.getType().startsWith(CHAT_TYPE_IMAGE)) {  //for images
                Notification notification = nNotificationUtils.getNotificationImageExpand(model.getUsrnm(), model.getFile(), resultIntent);
                NotificationManager notificationManager = (NotificationManager) EotApp.getAppinstance().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notIdEmergency, notification);
            } else {
                nb = nNotificationUtils.getAndroidChannelNotification(notificationSoundURI, R.mipmap.app_icon, 0, "1000",
                        model.getUsrnm(), FILE_SHARED, resultIntent).setWhen(0);
                nNotificationUtils.getManager().notify(notIdEmergency, nb.build());
            }

        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Notification notification = null;
            if (model.getType().equals("1")) {
                notification = nNotificationUtils.getNotificationManager(R.mipmap.app_icon, model.getUsrnm(), model.getMsg(), resultIntent);
            } else if (model.getType().startsWith(CHAT_TYPE_IMAGE)) {
                notification = nNotificationUtils.getNotificationImageExpand(model.getUsrnm(), model.getFile(), resultIntent);
            } else {
                notification = nNotificationUtils.getNotificationManager(R.mipmap.app_icon, model.getUsrnm(), FILE_SHARED, resultIntent);
            }
            NotificationManager notificationManager = (NotificationManager) EotApp.getAppinstance().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notIdEmergency, notification);
        }
    }


    /**
     * create notification for only chat
     **/
    public void createClientChatNotificationforChat(ClientChatReqModel model) {
        Log.e("", model.getMsg() + model.getUsrid());
        int notIdEmergency = Integer.valueOf(model.getJobId());
        Intent intent = new Intent(EotApp.getAppinstance(), JobDetailActivity.class);
        //   intent.putExtra("CLIENT_CHAT", AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(model.getJobId()));
        String str = new Gson().toJson(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(model.getJobId()));
        intent.putExtra("CLIENT_CHAT", str);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        /**add action for samsung device **/
        intent.setAction("" + notIdEmergency);
        intent.setType("CLIENTCHAT");
        PendingIntent resultIntent = PendingIntent.getActivity(EotApp.getAppinstance(), notIdEmergency, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (nNotificationUtils == null) {
            nNotificationUtils = new NotificationUtils(EotApp.getAppinstance());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder nb = null;

            if (model.getType().equals("1")) { //for text msgs
                nb = nNotificationUtils.getAndroidChannelNotification(notificationSoundURI, R.mipmap.app_icon, 0, "1000",
                        model.getUsrnm(), model.getMsg(), resultIntent).setWhen(0);
                nNotificationUtils.getManager().notify(notIdEmergency, nb.build());
            } else if (model.getType().startsWith(CHAT_TYPE_IMAGE)) {  //for images
                Notification notification = nNotificationUtils.getNotificationImageExpand(model.getUsrnm(), model.getFile(), resultIntent);
                NotificationManager notificationManager = (NotificationManager) EotApp.getAppinstance().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notIdEmergency, notification);
            } else {
                nb = nNotificationUtils.getAndroidChannelNotification(notificationSoundURI, R.mipmap.app_icon, 0, "1000",
                        model.getUsrnm(), FILE_SHARED, resultIntent).setWhen(0);
                nNotificationUtils.getManager().notify(notIdEmergency, nb.build());
            }
//            nNotificationUtils.getManager().notify(notIdEmergency, nb.build());

        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Notification notification = null;
            if (model.getType().equals("1")) {
                notification = nNotificationUtils.getNotificationManager(R.mipmap.app_icon, model.getUsrnm(), model.getMsg(), resultIntent);
            } else if (model.getType().startsWith(CHAT_TYPE_IMAGE)) {
                notification = nNotificationUtils.getNotificationImageExpand(model.getUsrnm(), model.getFile(), resultIntent);
            } else {
                notification = nNotificationUtils.getNotificationManager(R.mipmap.app_icon, model.getUsrnm(), FILE_SHARED, resultIntent);
            }
            NotificationManager notificationManager = (NotificationManager) EotApp.getAppinstance().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notIdEmergency, notification);
        }
        //}
    }


    /**
     * job chat path
     ***/
    public String getFieldworkerpath(String jobCode, String jobId) {
        String fwPath = "";
        try {
            /**this path for new job**/
            if (String.valueOf(App_preference.getSharedprefInstance().getLoginRes().getStaticJobId()) != null
                    && Integer.parseInt(jobId) >= App_preference.getSharedprefInstance().getLoginRes().getStaticJobId()) {
                fwPath = App_preference.getSharedprefInstance().getRegion()
                        + "/comp-"
                        + App_preference.getSharedprefInstance().getLoginRes().getCompId()
                        + "/job/"
                        + jobId
                        + "/users/usr-" + App_preference.getSharedprefInstance().getLoginRes().getUsrId();
            } else {
                fwPath = App_preference.getSharedprefInstance().getRegion()
                        + "/comp-"
                        + App_preference.getSharedprefInstance().getLoginRes().getCompId()
                        + "/job/"
                        + jobCode
                        + "-"
                        + jobId
                        + "/users/usr-" + App_preference.getSharedprefInstance().getLoginRes().getUsrId();

            }
        } catch (Exception ex) {
            ex.getMessage();
        }

        return fwPath;
    }

    /**
     * get job fw path
     **/
    public String getFieldworkersCollectionPath(String jobCode, String jobId) {
        String fwPath = "";
        /**this path for new job**/
        if (Integer.parseInt(jobId) >= App_preference.getSharedprefInstance().getLoginRes().getStaticJobId()) {
            fwPath = App_preference.getSharedprefInstance().getRegion()
                    + "/comp-"
                    + App_preference.getSharedprefInstance().getLoginRes().getCompId()
                    + "/job/"
                    + jobId
                    + "/users/";
        } else {
            fwPath = App_preference.getSharedprefInstance().getRegion()
                    + "/comp-"
                    + App_preference.getSharedprefInstance().getLoginRes().getCompId()
                    + "/job/"
                    + jobCode
                    + "-"
                    + jobId
                    + "/users/";

        }
        return fwPath;

    }


    /**
     * get job user path for Company
     **/
    public String getUserCollectionPath() {
        String userpath = App_preference.getSharedprefInstance().getRegion() +
                "/comp-"
                + App_preference.getSharedprefInstance().getLoginRes().getCompId() + "/users";
        return userpath;
    }


    public void setListOfListener(List<Job> data) {
        if (!collRefList.isEmpty()) {
            removeAllListner();
        }

//        set all path to one list for activate listener.
//        addAllCollectionListnerMessage(data);
    }

    /**
     * CHAT PATH FOR GROUP CHATING admin-2-FW
     **/
    public String getChatPath(String jobCode, String jobId) {
        String fwPath = "";
        try {
            /**this path for new job**/
            if (Integer.parseInt(jobId) >= App_preference.getSharedprefInstance().getLoginRes().getStaticJobId()) {
                fwPath = App_preference.getSharedprefInstance().getRegion()
                        + "/comp-"
                        + App_preference.getSharedprefInstance().getLoginRes().getCompId()
                        + "/job/"
                        + jobId
                        + "/messages/";
            } else {
                fwPath = App_preference.getSharedprefInstance().getRegion()
                        + "/comp-"
                        + App_preference.getSharedprefInstance().getLoginRes().getCompId()
                        + "/job/"
                        + jobCode
                        + "-"
                        + jobId
                        + "/messages/";
            }
        } catch (Exception ex) {
            ex.getMessage();
        }

        return fwPath;
    }


    /**
     * CHAT PATH FOR GROUP CHATING Client-2-FW
     **/
    public String getClientChatPath(String jobId) {
        String fwPath = "";
        fwPath = App_preference.getSharedprefInstance().getRegion()
                + "/comp-"
                + App_preference.getSharedprefInstance().getLoginRes().getCompId()
                + "/job/"
                + jobId
                + "/cltfwmessages/";
        return fwPath;
    }


    /**
     * CHAT PATH FOR USER online/offline
     ***/
    public String getAppUserOnlinePath() {
        String chatPath = "";
        chatPath = App_preference.getSharedprefInstance().getRegion()
                + "/comp-"
                + App_preference.getSharedprefInstance().getLoginRes().getCompId()
                + "/users/usr-" + App_preference.getSharedprefInstance().getLoginRes().getUsrId();
        return chatPath;
    }

    public void setAppUserOnline(final int online) {
        Log.e("onDestroy", "" + online);
        FireStoreUserOnlineModel model = new FireStoreUserOnlineModel();
        model.setOnline(online);
        FirebaseFirestore.getInstance().document(ChatController.getInstance()
                .getAppUserOnlinePath()).set(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("User Offline/Online:", online + "");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("User Offline/Online:", online + "");
            }
        });
    }

    public void removeAllListner() {
        try {
            Log.e("S::", "" + collRefList.size());
            if (!collRefList.isEmpty()) {
                for (ChatListnersContainer item : collRefList) {
                    item.getListenerRegistration().remove();
                    Log.e("", "");
                }
                pathlist.clear();
                collRefList.clear();
            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void removeListnerByJobID(String jobId) {
        try {
            if (!collRefList.isEmpty()) {
                for (ChatListnersContainer item : collRefList) {
                    if (item.getJobId().equals(jobId)) {
                        item.getListenerRegistration().remove();
                        pathlist.remove(jobId);
                        //collRefList.remove(jobId);
                    }
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }

    }

    /**
     * get client batch Count
     ***/
    public int getbatchCount(String jobId) {
        try {
            if (collRefList != null) {
                for (ChatListnersContainer item : collRefList) {
                    if (item.getJobId().equals(jobId)) {
                        return item.getBatchCount();
                    }
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return 0;
    }


    public int getClientChatBatchCount(String jobId) {
        try {
            for (ChatListnersContainer item : collRefList) {
                if (item.getJobId().equals(jobId)) {
                    return item.getCltbatChCount();
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return 0;
    }


    /**
     * PUSH NOTIFICATION FOR OFFLINE USER'S
     ***/
    public void getAllUserOffLineDataList(final Chat_Send_Msg_Model chat_send_Msg_model) {//String jobCode, String jobId
        final ArrayList<String> userList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection(getFieldworkersCollectionPath(chat_send_Msg_model.getJobCode(),
                chat_send_Msg_model.getJobId()))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                try {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (!document.getId().equals("usr-" + App_preference.getSharedprefInstance().getLoginRes().
                                getUsrId())) {
                            Log.e("Get UserList ====", document.getId());
                            userList.add(document.getId());
                        }
                    }
                    getUserOffLineList(userList, chat_send_Msg_model);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.e("TAG EXCEPTION :", ex.getMessage());
                }
            }
        });

    }


    /**
     * PUSH NOTIFICATION FOR OFFLINE USER'S from client chat
     ***/
    public void getAllUserOffLineDataListFromClientChat(final ClientChatReqModel chat_send_Msg_model) {//String jobCode, String jobId
        final ArrayList<String> userList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection(getFieldworkersCollectionPath(chat_send_Msg_model.getJobCode(),
                chat_send_Msg_model.getJobId()))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                try {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (!document.getId().equals("usr-" + App_preference.getSharedprefInstance().getLoginRes().
                                getUsrId())) {
                            Log.e("Get UserList ====", document.getId());
                            userList.add(document.getId());
                        }
                    }
                    getUserOffLineListSendNoti(userList, chat_send_Msg_model);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.e("TAG EXCEPTION :", ex.getMessage());
                }
            }
        });

    }

    /**
     * job admin FW offline chat
     **/
    private void getUserOffLineListSendNoti(final ArrayList<String> userJobList, final ClientChatReqModel
            chat_send_Msg_model) {

        List<String> adminIdsList = App_preference.getSharedprefInstance().getLoginRes().getAdminIds();

        List<String> rmUsrKeyList = new ArrayList<>();
        for (String adIds : userJobList) {
            rmUsrKeyList.add(adIds.replace("usr-", ""));
        }
        /**
         *get admin list from App perefence ***
         * ***/
        final List<String> commonAdminList = new ArrayList<>(adminIdsList);
        commonAdminList.retainAll(rmUsrKeyList);

        //fw list
        final ArrayList<String> fwIdsList = new ArrayList<>();
        for (String uIds : rmUsrKeyList) {
            if (!commonAdminList.contains(uIds)) {
                fwIdsList.add("usr-" + uIds);
            }
        }
        final ArrayList<String> offline_User_List = new ArrayList<>();
        FirebaseFirestore.getInstance().collection(getUserCollectionPath()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Log.e("", "");
                            if (!documentSnapshot.getId().contains("ad-")) {
                                if (!documentSnapshot.getId().contains("undefined"))
                                /** 0 for Offline user's
                                 * & 1 For online User's**/
                                    if (documentSnapshot.getData().get("online") != null)
                                        if (documentSnapshot.getData().get("online").toString().equals("0")) {
                                            if (fwIdsList.contains(documentSnapshot.getId())) {
                                                offline_User_List.add(documentSnapshot.getId());
                                            }
                                        }
                                Log.e("Offline User List", offline_User_List.toString());
                            }
                        }
                        ArrayList<String> list = new ArrayList<>();
                        for (String adIds : offline_User_List) {
                            list.add(adIds.replace("usr-", ""));
                        }
                        sendOfflineUserNotificationForClientChat(list, chat_send_Msg_model);

                    }
                });
    }


    /**
     * job admin FW offline chat
     **/
    private void getUserOffLineList(final ArrayList<String> userJobList, final Chat_Send_Msg_Model
            chat_send_Msg_model) {

        List<String> adminIdsList = App_preference.getSharedprefInstance().getLoginRes().getAdminIds();

        List<String> rmUsrKeyList = new ArrayList<>();
        for (String adIds : userJobList) {
            rmUsrKeyList.add(adIds.replace("usr-", ""));
        }
        /**
         *get admin list from App perefence ***
         * ***/
        final List<String> commonAdminList = new ArrayList<>(adminIdsList);
        commonAdminList.retainAll(rmUsrKeyList);

        //fw list
        final ArrayList<String> fwIdsList = new ArrayList<>();
        for (String uIds : rmUsrKeyList) {
            if (!commonAdminList.contains(uIds)) {
                fwIdsList.add("usr-" + uIds);
            }
        }
        final ArrayList<String> offline_User_List = new ArrayList<>();
        FirebaseFirestore.getInstance().collection(getUserCollectionPath()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Log.e("", "");
                            if (!documentSnapshot.getId().contains("ad-")) {
                                if (!documentSnapshot.getId().contains("undefined"))
                                /** 0 for Offline user's
                                 * & 1 For online User's**/
                                    if (documentSnapshot.getData().get("online") != null)
                                        if (documentSnapshot.getData().get("online").toString().equals("0")) {
                                            if (fwIdsList.contains(documentSnapshot.getId())) {
                                                offline_User_List.add(documentSnapshot.getId());
                                            }
                                        }
                                Log.e("Offline User List", offline_User_List.toString());
                            }
                        }
                        ArrayList<String> list = new ArrayList<>();
                        for (String adIds : offline_User_List) {
                            list.add(adIds.replace("usr-", ""));
                        }
                        sendOfflineUserNotification(list, chat_send_Msg_model);

                    }
                });
    }

    public void sendNotCpClient(String cltId, ClientChatReqModel chatReqModel) {
        Chat_Send_Msg_Model model = new Chat_Send_Msg_Model(chatReqModel.getMsg(), chatReqModel.getFile()
                , chatReqModel.getTime(), chatReqModel.getJobCode(), chatReqModel.getJobId(), chatReqModel.getType());
        Chat_Bck_Notification_Model bck_noti_model = new Chat_Bck_Notification_Model(cltId, model, "clientChat");
        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(bck_noti_model));

        ApiClient.getservices().eotServiceCall(Service_apis.sendNotificationToCpClient,
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
     * server Request for Client chat
     **/
    private void sendOfflineUserNotificationForClientChat(ArrayList<String> offline_user_list,
                                                          ClientChatReqModel chat_send_Msg_model) {
        Chat_Send_Msg_Model model = new Chat_Send_Msg_Model(chat_send_Msg_model.getMsg(), chat_send_Msg_model.getFile()
                , chat_send_Msg_model.getTime(), chat_send_Msg_model.getJobCode(), chat_send_Msg_model.getJobId(), chat_send_Msg_model.getType());
        Chat_Bck_Notification_Model bck_noti_model = new Chat_Bck_Notification_Model(offline_user_list, model, "clientChat");
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
     * server Request for job chat
     **/
    private void sendOfflineUserNotification(ArrayList<String> offline_user_list,
                                             Chat_Send_Msg_Model chat_send_Msg_model) {

        Chat_Bck_Notification_Model bck_noti_model = new Chat_Bck_Notification_Model(offline_user_list, chat_send_Msg_model);
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
     * SEND ADMIN NOTIFICATION
     ****/
    public void sendNotificationToAdmins(Chat_Send_Msg_Model chat_send_msg_model) {
        Map<String, String> admin_noti_model = new HashMap();
        admin_noti_model.put("jobCode", chat_send_msg_model.getJobCode());
        admin_noti_model.put("jobId", chat_send_msg_model.getJobId());
        if (chat_send_msg_model.getType().equals("1")) {
            admin_noti_model.put("msg", chat_send_msg_model.getMsg());
        } else {
            admin_noti_model.put("msg", chat_send_msg_model.getUsrnm() + " sent a file.");
        }
        admin_noti_model.put("time", chat_send_msg_model.getTime());
        admin_noti_model.put("usrnm", chat_send_msg_model.getUsrnm());
        for (String id : App_preference.getSharedprefInstance().getLoginRes().getAdminIds()) {
            FirebaseFirestore.getInstance().collection(getAdminNotificationDocPath(id)).add(admin_noti_model);
        }
    }

    /**
     * SEND ADMIN NOTIFICATION
     ****/
    public void sendNotificationToClientAdminChat(SendCLientAdminNotifica chat_send_msg_model, String cltId) {
        if (chat_send_msg_model.getType().equals("1")) {
            chat_send_msg_model.setMsg(chat_send_msg_model.getMsg());
        } else {
            chat_send_msg_model.setMsg(chat_send_msg_model.getUsrnm() + " sent a file.");
        }
        chat_send_msg_model.setIsClientChat(true);
        chat_send_msg_model.isClientChat();
        for (String id : App_preference.getSharedprefInstance().getLoginRes().getAdminIds()) {
            FirebaseFirestore.getInstance().collection(getAdminNotificationForClientChatforAdmin(id)).add(chat_send_msg_model);
        }
        FirebaseFirestore.getInstance().collection(getAdminNotificationForClientChat(cltId))
                .add(chat_send_msg_model);
    }


    private String getAdminNotificationDocPath(String adminid) {
        String chatPath = App_preference.getSharedprefInstance().getRegion()
                + "/comp-"
                + App_preference.getSharedprefInstance().getLoginRes().getCompId()
                + "/users/ad-" + adminid + "/notification/";
        return chatPath;
    }


    private String getAdminNotificationForClientChat(String adminid) {
        String chatPath = App_preference.getSharedprefInstance().getRegion()
                + "/comp-"
                + App_preference.getSharedprefInstance().getLoginRes().getCompId()
                + "/users/clt-" + adminid + "/notification/";
        return chatPath;
    }

    private String getAdminNotificationForClientChatforAdmin(String adminid) {
        String chatPath = App_preference.getSharedprefInstance().getRegion()
                + "/comp-"
                + App_preference.getSharedprefInstance().getLoginRes().getCompId()
                + "/users/ad-" + adminid + "/notification/";
        return chatPath;
    }

    /***craete all job listner for latest message ****/
    public void registerChatListner(Job item) {
        try {
            boolean isavailableAlready = false;
            for (ChatListnersContainer listner_item : collRefList) {
                if (listner_item.getJobId().equals(item.getJobId())) {
                    isavailableAlready = true;
                }
            }
            if (!isavailableAlready) {
                collRefList.add(new ChatListnersContainer(item.getLabel(), item.getJobId()));
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    //refresh adapter after all item atted collrefList
    public void refreshAdapter() {
        if (joblistListener != null && joblistListener.getAdapter() != null) {
            joblistListener.getAdapter().notifyDataSetChanged();
        }
    }

    public void getFirebaseAuthentication(String usrId, String region) {
        final String emailId = createFirebaseEmailId(usrId, region);//username + "@eyeontask.com";
        final String password = "123456";
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailId, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            creatuserFirstTime(emailId, password);
                        }
                    }
                });
    }

    private String createFirebaseEmailId(String usrId, String region) {
        return region + "_" + usrId + "@eyeontask.com";
    }

    public void signOutUserFromFirebase() {
        FirebaseAuth.getInstance().signOut();
    }


    /**
     * Register user
     **/
    private void creatuserFirstTime(String emailId, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailId, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });

    }

    /*** UNREAD COUNT FOR ALL FIELDWORK'S client-fw CHAT ****/
    public void increseUnreadCountforAllFW(final String jobCode, final String jobId) {
        FirebaseFirestore.getInstance().collection(getFieldworkersCollectionPath(jobCode, jobId))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Get a new write batch
                            WriteBatch batch = FirebaseFirestore.getInstance().batch();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (!document.getId().equals(fwKeyCode + App_preference.getSharedprefInstance().getLoginRes().getUsrId())) {
                                    FirestoreFieldWorkerModel fwmodel = document.toObject(FirestoreFieldWorkerModel.class);
                                    fwmodel.setCltunread(fwmodel.getCltunread() + 1);
                                    batch.set(document.getReference(), fwmodel);
                                }
                            }
                            // Commit the batch
                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "=>> committed");
                                }
                            });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void notifyWeBforNew(String type, String action, String jobId, String msg, String statusId) {
        try {
            if (statusId == null)
                statusId = "";
            NotifyWebNewModel model = new NotifyWebNewModel(type, action, jobId, statusId);
            model.setMsg(msg);
            FirebaseFirestore.getInstance().collection(getCollectionPathForWeb()).add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.e("", "");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("", "");
                }
            }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    Log.e("", "");

                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public String getCollectionPathForWeb() {
        try {
            for (String id : App_preference.getSharedprefInstance().getLoginRes().getAdminIds()) {
                String userpath = App_preference.getSharedprefInstance().getRegion()
                        + "/comp-"
                        + App_preference.getSharedprefInstance().getLoginRes().getCompId()
                        + "/notifyWeb/ad-" + id + "/newAssignNotify/";
                return userpath;
            }
            return "";
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "";

    }

}
