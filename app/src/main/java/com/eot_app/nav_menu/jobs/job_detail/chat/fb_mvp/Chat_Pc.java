package com.eot_app.nav_menu.jobs.job_detail.chat.fb_mvp;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.eot_app.nav_menu.jobs.job_controller.ChatController;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.chat.ChatFragment;
import com.eot_app.nav_menu.jobs.job_detail.chat.fire_Base_Model.Chat_Send_Msg_Model;
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


public class Chat_Pc implements Chat_Pi {

    private final Chat_View chat_view;
    private DatabaseReference databaseReference;


    public Chat_Pc(Chat_View chat_view) {
        this.chat_view = chat_view;
    }


    //send text msg
    @Override
    public void sendMessages(final Chat_Send_Msg_Model chat_send_Msg_model) {
        if (AppUtility.isInternetConnected()) {
            FirebaseFirestore.getInstance().collection(ChatFragment.PATH)
                    .add(chat_send_Msg_model)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.e("Message Send", documentReference.getId());
                            /**
                             *update read count for all fieldworkers except me***/
                            ChatController.getInstance().increseUnreadCountforAll(chat_send_Msg_model.getJobCode(), chat_send_Msg_model.getJobId());
                            /**
                             * ** function call for offline user push notification.**/
                            ChatController.getInstance().getAllUserOffLineDataList(chat_send_Msg_model);
                            /**
                             *function call for desktop notification**/
                            ChatController.getInstance().sendNotificationToAdmins(chat_send_Msg_model);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Msg Not Send", e.getMessage());
                        }
                    });
        } else {
            netWrkError();
        }

    }

    //after upload image get image uri from firebase storage & than send msg
    public void getmeUrl(String image_url, final String type, final Job jobData) {
        if (AppUtility.isInternetConnected()) {
            FirebaseStorage.getInstance().getReference().child(image_url).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Chat_Send_Msg_Model fileModel = new Chat_Send_Msg_Model
                                    (
                                            "", uri.toString(),
                                            AppUtility.getDateByMiliseconds(),
                                            jobData.getLabel(),
                                            jobData.getJobId(), type);
                            String string = ChatFragment.PATH.replace(".", "-");
                            databaseReference = FirebaseDatabase.getInstance().getReference(string);
                            //databaseReference = FirebaseDatabase.getInstance().getReference(ChatFragment.PATH);
                            databaseReference.child(databaseReference.push().getKey()).push().setValue(fileModel);
                            sendMessages(fileModel);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("", "");
                }
            });
        } else {
            netWrkError();
        }

    }

    //upload image & store in firebase storage
    @Override
    public void uploadActualImageOnFireStore(Uri uri, final Job jobData) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(((Fragment) chat_view).getActivity());
            try {
                final String im_url = ChatFragment.PATH + System.currentTimeMillis();
                FirebaseStorage.getInstance().getReference().child(im_url).
                        putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
            netWrkError();
        }
    }

    public void netWrkError() {
        AppUtility.alertDialog(((Fragment) chat_view).getActivity(), LanguageController.getInstance().
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
