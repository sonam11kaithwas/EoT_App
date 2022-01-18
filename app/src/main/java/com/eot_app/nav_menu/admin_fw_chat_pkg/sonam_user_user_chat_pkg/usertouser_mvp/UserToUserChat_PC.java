package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_mvp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.user_chat_controller.UserToUserChatController;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.ChatDocResModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.GrpUsrlistStatus;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.MsgModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.TeamMemIdModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserChatModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserMsgSend;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserOflineOnlineModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UsersModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UsersModelForgrp;
import com.eot_app.services.ApiClient;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Sonam-11 on 7/4/20.
 */
public class UserToUserChat_PC implements UserToUserChat_PI {
    private final UserToUserChat_View userToUserChatView;
    private String noti_recvrid = "";

    public UserToUserChat_PC(UserToUserChat_View userToUserChatView, String noti_recvrid) {
        this.userToUserChatView = userToUserChatView;
        this.noti_recvrid = noti_recvrid;
    }

    @Override
    public void getUserListsByIds(List<TeamMemIdModel> tempusersList) {
        ArrayList<GrpUsrlistStatus> ids = new ArrayList<>();
        try {
            GrpUsrlistStatus model = null;
            for (TeamMemIdModel m : tempusersList) {
                if (!m.getUsrId().equals(App_preference.getSharedprefInstance().getLoginRes().getUsrId())) {
                    UserChatModel userChatModel = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).userChatModel().getUserById(m.getUsrId());
                    model = new GrpUsrlistStatus();
                    model.setNm(userChatModel.getFnm() + " " + userChatModel.getLnm());
                    model.setUsrIm(model.getNm().charAt(0) + "");
                    UserOflineOnlineModel userStatus = UserToUserChatController.getInstance().get_user_Status_Model_List(m.getUsrId());
                    model.setStatus(userStatus.getIsOnline());
                } else {
                    model = new GrpUsrlistStatus();
                    model.setNm(App_preference.getSharedprefInstance().getLoginRes().getFnm() + " " + App_preference.getSharedprefInstance().getLoginRes().getLnm());
                    model.setUsrIm(model.getNm().charAt(0) + "");
                    model.setStatus("1");
                }
                ids.add(model);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        userToUserChatView.setUserListInGrp(ids);

    }

    @Override
    public void craeteFirstnode(UserMsgSend msgModel, String documentId) {
        String chatpath = UserToUserChatController.getInstance().getChatpath();
        if (!chatpath.equals("") && !documentId.equals("")) {
            FirebaseFirestore.getInstance().collection(chatpath)
                    .document(documentId)
                    .set(msgModel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e("", "");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("", "");
                        }
                    });
        }
    }


    @Override
    public void createUsersForgrp(UsersModelForgrp userList, final UserMsgSend userMsgSend) {
        FirebaseFirestore.getInstance().collection(UserToUserChatController.getInstance().getUserpath())
                .add(userList)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e("Message Send", documentReference.getId());
                        craeteFirstnode(userMsgSend, documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Msg Not Send", e.getMessage());
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                    }
                });
    }

    @Override
    public void createUsers(UsersModel userList, final UserMsgSend userMsgSend) {
        FirebaseFirestore.getInstance().collection(UserToUserChatController.getInstance().getUserpath())
                .add(userList)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e("Message Send", documentReference.getId());
                        craeteFirstnode(userMsgSend, documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Msg Not Send", e.getMessage());
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                    }
                });
    }

    /**/
    @Override
    public void sendMsgs(final MsgModel msgModel, String documentId) {
        HashMap<String, String> myObject = new HashMap<String, String>();
        myObject.put("content", msgModel.getContent());
        myObject.put("senderId", msgModel.getSenderId());
        myObject.put("createdAt", msgModel.getCreatedAt());
        myObject.put("senderNm", msgModel.getSenderNm());

        String chatpath = UserToUserChatController.getInstance().getChatpath();
        if (!chatpath.equals("") && !documentId.equals("")) {
            FirebaseFirestore.getInstance().collection(chatpath)
                    .document(documentId).
                    update("messages", FieldValue.arrayUnion(myObject))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e("", "");
                            /**
                             * ** function call for offline user push notification.**/
                            UserToUserChatController.getInstance().getAllUserOffLineDataList(msgModel.getContent(), noti_recvrid
                                    , "");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("", "");
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
        }

    }

    /***upload document for chat**/
    @Override
    public void uploadDoc(String fileString1, final String chatDocumentId) {
        Log.e("", "");
        String mimeType = "";
        MultipartBody.Part body = null;
        File file = new File(fileString1);
        if (file != null && file.exists()) {
            mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = URLConnection.guessContentTypeFromName(file.getName());
                if (mimeType == null) {
                    return;
                }
            }
            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("cd", file.getName(), requestFile);
        }

        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow((Context) userToUserChatView);
            ApiClient.getservices().uploadChatDocements(AppUtility.getApiHeaders(), body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("Responce", jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean()) {
                                if (jsonObject.get("data").getAsJsonArray().size() > 0) {
                                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                    Type listType = new TypeToken<List<ChatDocResModel>>() {
                                    }.getType();
                                    ArrayList<ChatDocResModel> getFileList_res = new Gson().fromJson(convert, listType);
                                    sendDocument(new MsgModel(getFileList_res.get(0).getUserId()
                                            , getFileList_res.get(0).getCreatedate(),
                                            getFileList_res.get(0).getAttachFileName()), chatDocumentId);
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                userToUserChatView.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                userToUserChatView.userDialog(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            Log.e("Error", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            Log.e("", "");
                            AppUtility.progressBarDissMiss();
                        }
                    });
        } else {
            networkError();
        }
    }

    private void sendDocument(final MsgModel msgModel, String chatDocumentId) {
        HashMap<String, String> myObject = new HashMap<String, String>();
        myObject.put("doc", msgModel.getDoc());
        myObject.put("createdAt", msgModel.getCreatedAt());
        myObject.put("senderId", msgModel.getSenderId());
        myObject.put("senderNm", msgModel.getSenderNm());

        String chatPath = UserToUserChatController.getInstance().getChatpath();
        if (!chatPath.equals("") && !chatDocumentId.equals("")) {
            FirebaseFirestore.getInstance().collection(chatPath)
                    .document(chatDocumentId).
                    update("messages", FieldValue.arrayUnion(myObject))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e("", "");
                            /**
                             * ** function call for offline user push notification.**/
                            if (msgModel.getDoc().contains(".jpg") || msgModel.getDoc().contains(".png") || msgModel.getDoc().contains(".jpeg"))
                                UserToUserChatController.getInstance().getAllUserOffLineDataList("📷 Photo", noti_recvrid
                                        , msgModel.getDoc());
                            else
                                UserToUserChatController.getInstance().getAllUserOffLineDataList("📎 Attachment", noti_recvrid
                                        , msgModel.getDoc());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("", "");
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
        }

    }

    private void networkError() {
        AppUtility.alertDialog((Context) userToUserChatView, LanguageController.getInstance().
                        getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey
                        (AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }
}