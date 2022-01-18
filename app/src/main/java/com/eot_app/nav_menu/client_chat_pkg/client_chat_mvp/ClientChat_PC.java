package com.eot_app.nav_menu.client_chat_pkg.client_chat_mvp;

import static com.eot_app.nav_menu.client_chat_pkg.ClientChatFragment.CHATFRAGMENTTAG;
import static com.eot_app.nav_menu.client_chat_pkg.ClientChatFragment.CLIENTPATH;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.eot_app.nav_menu.client_chat_pkg.client_chat_model.ClientChatReqModel;
import com.eot_app.nav_menu.client_chat_pkg.client_chat_model.SendCLientAdminNotifica;
import com.eot_app.nav_menu.jobs.job_controller.ChatController;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.Callable;

/**
 * Created by Sonam-11 on 2020-01-02.
 */
public class ClientChat_PC implements ClientChat_PI {
    private final ClientChat_View clientChatView;
    private final FirebaseFirestore firebaseFirestore;
    private DatabaseReference databaseReference;
    private String cltId = "", conId = "";

    public ClientChat_PC(ClientChat_View clientChatView, String cltId, String conId) {
        this.clientChatView = clientChatView;
        firebaseFirestore = FirebaseFirestore.getInstance();
        this.cltId = cltId;
        this.conId = conId;

    }

    @Override
    public void sendCLientFwMessage(final ClientChatReqModel chatReqModel) {
        if (AppUtility.isInternetConnected()) {
            firebaseFirestore.collection(CLIENTPATH).add(chatReqModel)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.e(CHATFRAGMENTTAG, "onSuccess");
                            /**
                             *update read count for all fieldworkers except me***/
                            ChatController.getInstance().increseUnreadCountforAllFW(chatReqModel.getJobCode(),
                                    chatReqModel.getJobId());
                            /****work for Piyush sir****/
                            /**
                             *function call for desktop notification**/
                            SendCLientAdminNotifica model = new SendCLientAdminNotifica(chatReqModel.getJobCode(), chatReqModel.getJobId(), chatReqModel.getMsg(), chatReqModel.getTime(), chatReqModel.getUsrnm(), chatReqModel.getType());
                            ChatController.getInstance().sendNotificationToClientAdminChat(model, cltId);


                            /****Send notification for Customber portal****/
                            ChatController.getInstance().sendNotCpClient(conId, chatReqModel);


                            /**
                             * ** function call for offline user push notification.**/
                            /****Not call this because action not giving from BACKEND****/
                            ChatController.getInstance().getAllUserOffLineDataListFromClientChat(chatReqModel);
                            /**
                             /**
                             * ** function call for offline user push notification.**/
//                        ChatController.getInstance().sendNotificationToAdmins(chat_send_Msg_model);
                            //ChatController.getInstance().getClientChatUserOffline(chatReqModel);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(CHATFRAGMENTTAG, "Error" + e.getMessage());
                }
            });
        } else {
            networkError();
        }
    }


    /***
     * *upload image & store in firebase storage*/
    @Override
    public void uploadActualImageOnFireStore(Uri uri, final Job jobData) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(((Fragment) clientChatView).getActivity());
            try {
                final String im_url = CLIENTPATH + System.currentTimeMillis();
                FirebaseStorage.getInstance().getReference().child(im_url).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Log.e("TAG", "image upload");
                        getmeUrl(im_url, task.getResult().getMetadata().getContentType(), jobData);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "fail");
                        AppUtility.progressBarDissMiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e("TAG", "Success");
                        AppUtility.progressBarDissMiss();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            networkError();
        }
    }

    private void networkError() {
        AppUtility.alertDialog(((Fragment) clientChatView).getActivity(), LanguageController.getInstance().
                        getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey
                        (AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    /***
     **
     * after upload image get image uri from firebase storage & than send msg */

    public void getmeUrl(String image_url, final String type, final Job jobData) {
        FirebaseStorage.getInstance().getReference().child(image_url).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        try {
                            ClientChatReqModel fileModel = new ClientChatReqModel
                                    ("", uri.toString(),
                                            jobData.getLabel(), jobData.getJobId(), type);
                            if (CLIENTPATH != null) {
                                String string = CLIENTPATH.replace(".", "-");
                                databaseReference = FirebaseDatabase.getInstance().getReference(string);
                                databaseReference.child(databaseReference.push().getKey()).push().setValue(fileModel);
                            }

                            sendCLientFwMessage(fileModel);
                        } catch (Exception ex) {
                            ex.getMessage();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(CHATFRAGMENTTAG, "Exception" + exception.getMessage());
            }
        });
    }

}
