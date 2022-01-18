package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eot_app.R;
import com.eot_app.UploadDocumentActivity;
import com.eot_app.home_screens.MainActivity;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.user_chat_controller.UserToUserChatController;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.ChatMsgDataModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.GrpUsrlistStatus;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.GrpUsrlistStatusAdpter;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.MsgModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.TeamMemIdModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserChatModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserMsgSend;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserOflineOnlineModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UsersModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UsersModelForgrp;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_mvp.UserToUserChat_PC;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_mvp.UserToUserChat_PI;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_mvp.UserToUserChat_View;
import com.eot_app.nav_menu.jobs.job_detail.documents.PathUtils;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.Callable;

import javax.annotation.Nullable;

/**
 * Created by Sonam-11 on 2020-03-06.
 */
public class UserToUserChatActivity extends UploadDocumentActivity implements
        UserToUserChat_View, View.OnClickListener {

    public RecyclerView recycler_chats;
    private String documentId = "";
    private ChatMsgDataModel chatMember;
    private int count = 0;
    // TODO: Rename and change types of parameters
    private TextView nolist_txt, usr_txt;
    private EditText msg_send_edt;
    private ImageView imag_upload, send_msg, file_upload;
    private UserChatMsgsAdpter myChatAdapter;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private LinearLayout expanded_image_bg;
    private View grp_chat_drp_parent_lay;
    private ImageView expandedImageView;
    private LinearLayoutManager mLayoutManager;
    private Dialog enterFieldDialog;
    private ImageView chat_im_zoom;
    private UserToUserChat_PI userToUserChatPi;
    private Spinner usr_stats_dp;
//    private View user_list_layoutt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_to_user_chat);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (getIntent().hasExtra("chatMember")) {
                String convertObject = bundle.getString("chatMember");
                chatMember = new Gson().fromJson(convertObject, ChatMsgDataModel.class);
            } else {
                if (UserToUserChatController.USERSTATUPDATE) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    /*** set online status when app launch****/
                    hashMap.put("isOnline", 1);
                    setOnlineOffline(hashMap);
                }

                if (getIntent().hasExtra("UserCHAT")) {
                    /*** user chat notification for all devices**/
                    UserChatModel userChatModel = (UserChatModel) bundle.getSerializable("UserCHAT");
                    if (userChatModel == null) {
                        startActivity(new Intent(this, MainActivity.class));
                        this.finish();
                    }
                    UserToUserChatController.getInstance().addUserStatusListner(userChatModel.getUsrId());
                    chatMember = new ChatMsgDataModel();
                    chatMember.setUserChatModel(userChatModel);
                } else if (getIntent().getType().equals("ADMINCHAT")) {
                    /****This only for SamSung**/
                    UserChatModel userChatModel = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                            .userChatModel().getUserById(getIntent().getAction());
                    if (userChatModel == null) {
                        startActivity(new Intent(this, MainActivity.class));
                        this.finish();
                    }
                    UserToUserChatController.getInstance().addUserStatusListner(userChatModel.getUsrId());
                    chatMember = new ChatMsgDataModel();
                    chatMember.setUserChatModel(userChatModel);
                } else {
                    this.finish();
                }

            }
        }

        UserOflineOnlineModel userStatus = UserToUserChatController.getInstance().get_user_Status_Model_List(chatMember.getUserChatModel()
                .getUsrId());
        if (userStatus != null) {
            if (userStatus.getIsInactive().equals("1")) {
                this.finish();
            }
            if (chatMember.getUserChatModel().getIsTeam() != null && chatMember.getUserChatModel().getIsTeam().equals("1")) {
                setTitle(chatMember.getUserChatModel().getFnm() + " " + chatMember.getUserChatModel().getLnm());
            } else {
                if (userStatus.getIsOnline().equals("1") || userStatus.getIsOnline().equals("3")) {
                    setTitle(chatMember.getUserChatModel().getFnm() + " " + chatMember.getUserChatModel().getLnm() + "\n"
                            + "Online");
                } else {
                    setTitle(chatMember.getUserChatModel().getFnm() + " " + chatMember.getUserChatModel().getLnm() + "\n"
                            + "Offline");
                }
            }
        }
        initializeView();
        if (chatMember.getUserChatModel().getIsTeam() != null && chatMember.getUserChatModel().getIsTeam().equals("1")
                && chatMember.getUserChatModel().getTeamMemId() != null && chatMember.getUserChatModel().getTeamMemId().size() > 0) {

            userToUserChatPi.getUserListsByIds(chatMember.getUserChatModel().getTeamMemId());
            groupsUserChat();
        } else {
            getDocumentid();
        }
        if (!documentId.isEmpty())
            FirebaseFirestore.getInstance().collection(UserToUserChatController.getInstance().getChatpath())
                    .document(documentId)
                    .addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            Log.e("", "");
                        }
                    });


    }


    @Override
    public void setUserListInGrp(ArrayList<GrpUsrlistStatus> ids) {
        // AppUtility.spinnerPopUpWindow(usr_stats_dp);
        if (ids.size() > 0) {
            GrpUsrlistStatusAdpter adpter = new GrpUsrlistStatusAdpter(this, ids);
            usr_stats_dp.setAdapter(adpter);

        }
    }

    /******set user online status when tab on notifaction ***/
    private void setOnlineOffline(HashMap<String, Object> hashMap) {
        try {
            FirebaseDatabase.getInstance()
                    .getReference().child(App_preference.getSharedprefInstance().getRegion()
                    + "/cmp" +
                    App_preference.getSharedprefInstance().getLoginRes().getCompId().toLowerCase() + "/users/usr"
                    + App_preference.getSharedprefInstance().getLoginRes().getUsrId()).updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("ForegroundService2", "init firebase success realtime");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("ForegroundService2", "init firebase fail realtime");

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getDocumentid() {
        final ArrayList<String> usersList = new ArrayList<>();
        usersList.add(chatMember.getUserChatModel().getUsrId());
        usersList.add(App_preference.getSharedprefInstance().getLoginRes().getUsrId());

        final ArrayList<UsersModel> userModelList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection(UserToUserChatController.getInstance().getUserpath())
                .whereArrayContains("users", App_preference.getSharedprefInstance().getLoginRes().getUsrId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        try {
                            if (queryDocumentSnapshots != null) {
                                if (queryDocumentSnapshots.size() != 0) {
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        Log.d("TAG", document.getId() + " => " + document.getData());
                                        UsersModel userModel = document.toObject(UsersModel.class);
                                        if (userModel.getUsers().contains(App_preference.getSharedprefInstance().getLoginRes().getUsrId())
                                                && ((userModel.getUsers().
                                                contains(chatMember.getUserChatModel().getUsrId())) ||
                                                (userModel.getUsers().
                                                        contains("grp-" + chatMember.getUserChatModel().getUsrId())))) {
                                            documentId = document.getId();
                                            getUserMsgs();
                                            userModelList.clear();
                                            break;
                                        } else {
                                            userModelList.add(userModel);
                                        }
                                    }
                                    if (userModelList.size() > 0) {
                                        sentMsgFirstTimeForDOuc(usersList);
                                    }
                                } else {
                                    sentMsgFirstTimeForDOuc(usersList);

                                }
                            }
                        } catch (Exception ex) {
                            ex.getMessage();
                        }
                    }
                });

    }


    private void sentMsgFirstTimeForDOuc(ArrayList<String> usersList) {
        ArrayList<MsgModel> msgList = new ArrayList<MsgModel>();
        msgList.add(new MsgModel("", "", ""));
        UsersModel model = new UsersModel(usersList);
        userToUserChatPi.createUsers(model, (new UserMsgSend(
                new ArrayList<MsgModel>()
        )));
    }

    private void sentMsgFirstTimeForgrp(UsersModelForgrp usersList) {
        ArrayList<MsgModel> msgList = new ArrayList<MsgModel>();
        msgList.add(new MsgModel("", "", ""));
        userToUserChatPi.createUsersForgrp(usersList, (new UserMsgSend(
                new ArrayList<MsgModel>()
        )));
    }

    /******Craete node and get DOCUMENT id for Group chat*******/
    private void groupsUserChat() {
        ArrayList<Object> tempusersList = new ArrayList<>();
        for (TeamMemIdModel m : chatMember.getUserChatModel().getTeamMemId()) {
            tempusersList.add(m.getUsrId());
        }

        final HashMap<String, String> grpId = new HashMap<>();
        grpId.put("grpId", "grp-" + chatMember.getUserChatModel().getUsrId());

        tempusersList.add(grpId);

        final UsersModelForgrp groups = new UsersModelForgrp(tempusersList);

        Log.e("", "");
        final ArrayList<UsersModelForgrp> userModelList = new ArrayList<>();

        FirebaseFirestore.getInstance().collection(UserToUserChatController.getInstance().getUserpath())
                .whereArrayContains("groups", App_preference.getSharedprefInstance().getLoginRes().getUsrId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        try {
                            if (queryDocumentSnapshots != null) {
                                if (queryDocumentSnapshots.size() != 0) {
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        Log.d("TAG", document.getId() + " => " + document.getData());
                                        UsersModelForgrp userModel = document.toObject(UsersModelForgrp.class);
                                        Log.e("", "");
                                        if (userModel != null && userModel.getGroups().contains(App_preference.getSharedprefInstance().getLoginRes().getUsrId())
                                                &&
                                                (userModel.getGroups().
                                                        contains(grpId))) {
                                            documentId = document.getId();
                                            getUserMsgs();
                                            userModelList.clear();
                                            break;
                                        } else {
                                            userModelList.add(userModel);
                                        }
                                    }
                                    if (userModelList.size() > 0) {
                                        sentMsgFirstTimeForgrp(groups);
                                    }
                                } else {
                                    Log.e("", "");
                                    sentMsgFirstTimeForgrp(groups);
                                }
                            }
                        } catch (Exception ex) {
                            ex.getMessage();
                        }
                    }
                });

    }

    private void getUserMsgs() {
        FirebaseFirestore.getInstance()
                .collection(UserToUserChatController.getInstance().getChatpath()).
                document(documentId).addSnapshotListener(
                new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        Log.e("", "");
                        try {
                            if (documentSnapshot != null) {
                                ArrayList<MsgModel> data = new ArrayList<>();
                                UserMsgSend fireBase_req_model2 = documentSnapshot.toObject(UserMsgSend.class);
                                if (fireBase_req_model2 != null) {
                                    data.addAll(fireBase_req_model2.getMessages());

                                    count = fireBase_req_model2.getMessages().size();
                                    setAdpterData(data);
                                }
                            }
                        } catch (Exception ex) {
                            ex.getMessage();
                        }

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserToUserChatController.getInstance().updateCount(chatMember.getUserChatModel().getUsrId());
        UserToUserChatController.getInstance().setChatScreenState(0, chatMember.getUserChatModel().getUsrId());
        UserToUserChatController.getInstance().setChatScreenState(1, chatMember.getUserChatModel().getUsrId());
        UserToUserChatController.getInstance().setUserToUserChatActivity(this);
    }

    public void updateUserStatus(UserOflineOnlineModel status) {
        if (status.getIsInactive().equals("1")) {
            this.finish();
        }
        if (status.getIsOnline().equals("1") || status.getIsOnline().equals("3"))
            setTitle(chatMember.getUserChatModel().getFnm() + " " + chatMember.getUserChatModel().getLnm() + "\n"
                    + "Online");
        else
            setTitle(chatMember.getUserChatModel().getFnm() + " " + chatMember.getUserChatModel().getLnm() + "\n"
                    + "Offline");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Ee", "onPause");
        UserToUserChatController.getInstance().setChatScreenState(0, "");
        UserToUserChatController.getInstance().setChatScreenState(1, "");
        UserToUserChatController.getInstance().setUserToUserChatActivity(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Ee", "onDestroy");
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).userChatModel().updateUserCount(chatMember.getUserChatModel()
                .getUsrId(), count);
        Log.e("", "");
        if (UserToUserChatController.USERSTATUPDATE) {
            HashMap<String, Object> hashMap = new HashMap<>();
            /*** set online status when app launch****/
            hashMap.put("isOnline", 0);
            setOnlineOffline(hashMap);
            UserToUserChatController.USERSTATUPDATE = false;
            UserToUserChatActivity.this.finish();
        }
    }


    /***update Read count in App Prefeence****/

    private void setAdpterData(ArrayList<MsgModel> msgList) {
        /***filter msg's by date**/

        Collections.sort(msgList, new Comparator<MsgModel>() {
            @Override
            public int compare(MsgModel o1, MsgModel o2) {
                return o1.getCreatedAt().compareTo(o2.getCreatedAt());
            }
        });
        if (msgList.size() > 0)
            recycler_chats.smoothScrollToPosition(msgList.size() - 1);
        myChatAdapter.updateMyAdpter(msgList);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initializeView() {
        /**get chat path**/
        Log.e("SenderId:", chatMember.getUserChatModel().getUsrId());
        recycler_chats = findViewById(R.id.recycler_client_chats);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        recycler_chats.setLayoutManager(mLayoutManager);
        recycler_chats.setItemAnimator(new DefaultItemAnimator());
        myChatAdapter = new UserChatMsgsAdpter(this, new ArrayList<MsgModel>());

        myChatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recycler_chats.scrollToPosition(recycler_chats.getAdapter().getItemCount() - 1);
            }
        });

        recycler_chats.setAdapter(myChatAdapter);
        recycler_chats.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtility.hideSoftKeyboard(UserToUserChatActivity.this);
                return false;
            }
        });


        imag_upload = findViewById(R.id.imag_upload_Img);
        imag_upload.setOnClickListener(this);

        msg_send_edt = findViewById(R.id.msg_send_edt);
        msg_send_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.chat_msg_hint));
        send_msg = findViewById(R.id.send_msg_Img);

        send_msg.setOnClickListener(this);

        nolist_txt = findViewById(R.id.nolist_txt);

        file_upload = findViewById(R.id.file_upload_Img);
        file_upload.setOnClickListener(this);
        userToUserChatPi = new UserToUserChat_PC(this, chatMember.getUserChatModel().getUsrId());

        usr_txt = findViewById(R.id.usr_txt);
        grp_chat_drp_parent_lay = findViewById(R.id.grp_chat_drp_parent_lay);
        grp_chat_drp_parent_lay.setOnClickListener(this);
        usr_stats_dp = findViewById(R.id.usr_stats_dp);

        if (chatMember.getUserChatModel().getIsTeam() != null && chatMember.getUserChatModel().getIsTeam().equals("1")
                && chatMember.getUserChatModel().getTeamMemId() != null && chatMember.getUserChatModel().getTeamMemId().size() > 0) {
            grp_chat_drp_parent_lay.setVisibility(View.VISIBLE);
            usr_txt.setText(chatMember.getUserChatModel().getTeamMemId().size() + " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.users));
        } else {
            grp_chat_drp_parent_lay.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * call from adpter
     **/
    @Override
    public void openImage(View thumbView, Drawable bmp, String img_url) {
        getChatImageZoomDialog(thumbView, bmp, img_url);
    }


    @Override
    public void onSessionExpire(String msg) {
        AppUtility.alertDialog(this, LanguageController.getInstance()
                        .getMobileMsgByKey(AppConstant.dialog_error_title), msg, LanguageController.getInstance()
                        .getMobileMsgByKey(AppConstant.ok), "",
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        EotApp.getAppinstance().sessionExpired();
                        return null;
                    }
                });
    }

    @Override
    public void userDialog(String str) {
        showDoalig(str);
    }

    private void showDoalig(String msg) {
        AppUtility.alertDialog(this, LanguageController.getInstance()
                        .getMobileMsgByKey(AppConstant.dialog_error_title), msg, LanguageController.getInstance()
                        .getMobileMsgByKey(AppConstant.ok), "",
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {

                        return null;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }


    @Override
    public void onDocumentSelected(String path, boolean isImage) {
        userToUserChatPi.uploadDoc(path, documentId);
    }

    @Override
    public void onClickContinuarEvent(Uri uri) {
        Log.e("", "");
        String path = "";
        path = PathUtils.getRealPath(this, uri);
        if (!path.isEmpty()) {
            userToUserChatPi.uploadDoc(path, documentId);
        }
        Log.e("Path:::", path);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void openUriOnBroWser(String serverUri, String type) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse(serverUri), type);
        try {
            startActivity(i);
        } catch (Exception e) {
            Intent openAnyType = new Intent(Intent.ACTION_VIEW);
            openAnyType.setData(Uri.parse(serverUri));
            startActivity(openAnyType);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.grp_chat_drp_parent_lay:
                usr_stats_dp.performClick();
                break;
            case R.id.imag_upload_Img:
                selectFile(true);
                break;
            case R.id.send_msg_Img:
                if (msg_send_edt.getText().toString().trim().length() > 0) {
                    userToUserChatPi.sendMsgs(new MsgModel(App_preference.getSharedprefInstance().getLoginRes().getUsrId(),
                            msg_send_edt.getText().toString().trim()), documentId);
                    msg_send_edt.setText("");
                }
                break;
            case R.id.file_upload_Img:
                getDocumentsFromGallery();
                break;
        }
    }


    private void getChatImageZoomDialog(View thumbView, Drawable bmp, String img_url) {

        enterFieldDialog = new Dialog(this);
        enterFieldDialog.setCancelable(false);
        enterFieldDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        enterFieldDialog.setContentView(R.layout.chat_image_zoom_out_in_layout);

        Window window = enterFieldDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);

        expanded_image_bg = enterFieldDialog.findViewById(R.id.expanded_image_bg);
        expandedImageView = enterFieldDialog.findViewById(R.id.expanded_image);
        chat_im_zoom = enterFieldDialog.findViewById(R.id.chat_im_zoom);
        enterFieldDialog.show();
        zoomImageFromThumb(thumbView, bmp, img_url);

    }

    private void zoomImageFromThumb(final View thumbView, Drawable imageResId, String img_url) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.

        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.

        Glide.with(this).load(img_url)
                .placeholder(imageResId)
                .override(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//220
                .into(expandedImageView);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expanded_image_bg.setVisibility(View.VISIBLE);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                    //enterFieldDialog.dismiss();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expanded_image_bg.setVisibility(View.GONE);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                        enterFieldDialog.dismiss();//dialog close
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expanded_image_bg.setVisibility(View.GONE);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });


        chat_im_zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thumbView.setAlpha(1f);
                expanded_image_bg.setVisibility(View.GONE);
                expandedImageView.setVisibility(View.GONE);
                mCurrentAnimator = null;
                enterFieldDialog.dismiss();
            }
        });


    }


}
