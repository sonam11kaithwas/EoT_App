package com.eot_app.nav_menu.admin_fw_chat_pkg.chat_pkg.chat_mvp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.eot_app.nav_menu.admin_fw_chat_pkg.chat_pkg.model_pkg.SingleChatModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.chat_single_pkg.AdminChatController;
import com.eot_app.nav_menu.jobs.job_detail.chat.ChatFragment;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
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

/**
 * Created by Sonam-11 on 2020-03-07.
 */
public class SingleChat_PC implements SingleChat_PI {
    private final SingleChat_View singleChatView;
    private final String PATH;
    String rcvrId;
    private DatabaseReference databaseReference;

    public SingleChat_PC(SingleChat_View singleChatView, String PATH, String rcvrId) {
        this.singleChatView = singleChatView;
        this.PATH = PATH;
        this.rcvrId = rcvrId;
    }

    @Override
    public void sendChatMessages(SingleChatModel chatModel) {

        FirebaseFirestore.getInstance().collection(PATH)
                .add(chatModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e("Message Send", documentReference.getId());
                        AdminChatController.getAdminChatInstance().setUserMgsCount(rcvrId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Msg Not Send", e.getMessage());
                    }
                });

    }

    @Override
    public void uploadActualImageOnFireStore(Uri uri) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(((Context) singleChatView));
            try {
                final String im_url = ChatFragment.PATH + System.currentTimeMillis();
                FirebaseStorage.getInstance().getReference().child(im_url).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Log.e("TAG", "image upload");
                        getmeUrl(im_url, task.getResult().getMetadata().getContentType());
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
            EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network));
        }
    }


    //after upload image get image uri from firebase storage & than send msg
    public void getmeUrl(String image_url, final String type) {
        FirebaseStorage.getInstance().getReference().child(image_url).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        SingleChatModel fileModel = new SingleChatModel
                                ("", uri.toString(), AppUtility.getDateByMiliseconds(), type);
                        String string = PATH.replace(".", "-");
                        databaseReference = FirebaseDatabase.getInstance().getReference(string);
                        databaseReference.child(databaseReference.push().getKey()).push().setValue(fileModel);
                        sendChatMessages(fileModel);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("", "");
            }
        });

    }
}
