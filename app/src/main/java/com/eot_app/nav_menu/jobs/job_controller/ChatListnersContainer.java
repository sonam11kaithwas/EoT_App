package com.eot_app.nav_menu.jobs.job_controller;

import android.util.Log;

import com.eot_app.nav_menu.jobs.job_detail.chat.fire_Base_Model.FirestoreFieldWorkerModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Objects;

import javax.annotation.Nullable;


/**
 * Created by aplite_pc302 icon 1/14/19.
 */

public class ChatListnersContainer {
    private static ChatListnersContainer ChatListnerInstance;
    String jobId;
    String jobCode;
    int batchCount;
    int cltbatChCount;
    ListenerRegistration listenerRegistration;

    /*** Job Chat event ***/
    public ChatListnersContainer(final String jobCode, final String jobId) {
        this.jobId = jobId;
        this.jobCode = jobCode;
        listenerRegistration = FirebaseFirestore.getInstance().document
                (ChatController.getInstance().getFieldworkerpath(jobCode, jobId)).
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                        @Nullable FirebaseFirestoreException e) {

                        if (documentSnapshot != null) {
                            FirestoreFieldWorkerModel fwitem = null;

                            try {
                                //Log.e("GetCollection")
                                fwitem = documentSnapshot.toObject(FirestoreFieldWorkerModel.class);
                            } catch (Exception ex) {
                                Log.e("FireBase ", "" + ex.getMessage());
                            }

                            /**** Event For Job Chat ***/
                            if (fwitem != null) {
                                ChatListnersContainer.this.setBatchCount(fwitem.getUnread());
                                ChatListnersContainer.this.setCltbatChCount(fwitem.getCltunread());

                                /***Appear on chat window***/
                                if (fwitem.getUnread() > 0 && ChatListnersContainer.this.getJobId()
                                        .equals(ChatController.getInstance().getChatScreenState(3))) {
                                    ChatController.getInstance().setUnreadCountZeroByJobId(
                                            ChatListnersContainer.this.jobCode,
                                            ChatListnersContainer.this.jobId, fwitem.getCltunread());
                                } else
                                /********FIRST-1 get msg's first time*/
                                    if (fwitem.getUnread() > 0 && !ChatListnersContainer.this.getJobId().
                                            equals(ChatController.getInstance().getChatScreenState(0))
                                    ) {
                                        /*****Create Notification's  when User on Job Details**/
                                        ChatController.getInstance().getUnreadMessagesByJobId
                                                (ChatListnersContainer.this.jobCode,
                                                        ChatListnersContainer.this.jobId, fwitem.getUnread());
                                        //        notify job list adapter
                                        if (ChatController.getInstance().getJoblistListener() != null &&
                                                ChatController.getInstance().getJoblistListener().getAdapter()
                                                        != null) {
                                            ChatController.getInstance().getJoblistListener().getAdapter().
                                                    notifyDataSetChanged();
                                        }

                                    } else  /** SECOND-2  craete notification***/
                                        if (fwitem.getUnread() > 0 && ChatListnersContainer.this.getJobId()
                                                .equals(ChatController.getInstance().getChatScreenState(0))
                                                && !ChatListnersContainer.this.getJobId().equals(
                                                ChatController.getInstance().getChatScreenState(1))) {

                                            ChatController.getInstance().getUnreadMessagesByJobId(ChatListnersContainer.this.jobCode, ChatListnersContainer.this.jobId, fwitem.getUnread());
                                            //        notify job list adapter
                                            if (ChatController.getInstance().getJoblistListener() != null && ChatController.getInstance().getJoblistListener().getAdapter() != null) {
                                                ChatController.getInstance().getJoblistListener().getAdapter().notifyDataSetChanged();
                                            }
                                            if (ChatController.getInstance().getJobdetailListener() != null) {
                                                ChatController.getInstance().getJobdetailListener().
                                                        clientBatch(fwitem.getUnread());

                                                ChatController.getInstance().getJobdetailListener().showBadge
                                                        (fwitem.getUnread());
                                            }

                                        } else/*** THIRD-3  fw read message from chat fragment ****/
                                            if (fwitem.getUnread() > 0 &&
                                                    ChatListnersContainer.this.getJobId().
                                                            equals(ChatController.getInstance().
                                                                    getChatScreenState(0))
                                                    && ChatListnersContainer.this.getJobId().
                                                    equals(ChatController.getInstance().
                                                            getChatScreenState(1)) &&
                                                    ChatListnersContainer.this.getJobId().
                                                            equals(ChatController.getInstance().
                                                                    getChatScreenState(2))) {
                                                ChatController.getInstance().setUnreadCountZeroByJobId(
                                                        ChatListnersContainer.this.jobCode,
                                                        ChatListnersContainer.this.jobId, fwitem.getCltunread());
                                                if (ChatController.getInstance().getJobdetailListener() != null) {
                                                    ChatController.getInstance().getJobdetailListener().
                                                            clientBatch(fwitem.getUnread());

                                                    ChatController.getInstance().getJobdetailListener().showBadge
                                                            (fwitem.getUnread());
                                                }
                                            }
                                            /****Don't know why this use*****/
                                            else if (fwitem.getUnread() > 0 && ChatListnersContainer.this.getJobId().
                                                    equals(ChatController.getInstance().
                                                            getChatScreenState(0)) &&
                                                    ChatListnersContainer.this.getJobId().
                                                            equals(ChatController.getInstance().
                                                                    getChatScreenState(1)) &&
                                                    !ChatListnersContainer.this.getJobId().equals
                                                            (ChatController.getInstance().
                                                                    getChatScreenState(2))) {
                                                ChatController.getInstance().getUnreadMessagesByJobId(ChatListnersContainer.this.jobCode, ChatListnersContainer.this.jobId, fwitem.getUnread());
                                                //        notify job list adapter
                                                if (ChatController.getInstance().getJoblistListener() != null &&
                                                        ChatController.getInstance().getJoblistListener().getAdapter() != null) {
                                                    ChatController.getInstance().getJoblistListener().getAdapter().notifyDataSetChanged();
                                                }
                                            } else if (fwitem.getUnread() == 0 && fwitem.getCltunread() == 0) {
                                                if (ChatController.getInstance().getJoblistListener() != null && ChatController.getInstance().getJoblistListener().getAdapter() != null) {
                                                    ChatController.getInstance().getJoblistListener().getAdapter().notifyDataSetChanged();
                                                }
                                                //        notify job detail page
                                                if (ChatController.getInstance().getJobdetailListener() != null) {

                                                    ChatController.getInstance().getJobdetailListener().
                                                            clientBatch(fwitem.getUnread());

                                                    ChatController.getInstance().getJobdetailListener().showBadge(fwitem.getUnread());
                                                }
                                            } else/***Case for Client/Admin Msg admin count Update ****/
                                                if (fwitem.getUnread() == 0) {
                                                    if (ChatController.getInstance().getJobdetailListener() != null) {
                                                        ChatController.getInstance().getJobdetailListener().
                                                                clientBatch(fwitem.getUnread());
                                                        ChatController.getInstance().getJobdetailListener().showBadge(fwitem.getUnread());
                                                    }
                                                }


                                /**** Event For Client-FW Chat ***/
                                /*****FIRST-1***/

                                /*** Create Notification ****/
                                if (fwitem.getCltunread() > 0 &&
                                        !ChatListnersContainer.this.getJobId().
                                                equals(ChatController.getInstance().
                                                        getClientChatFragState(0))) {
                                    if (ChatController.getInstance().getJobdetailListener() != null && !ChatController.getInstance().getJobdetailListener().CLIENTCHATWINDOW)
                                        ChatController.getInstance().getUnreadMessagesByClientJobFwId(ChatListnersContainer.this.jobId);
                                    if (ChatController.getInstance().getJoblistListener() != null && ChatController.getInstance().getJoblistListener().getAdapter() != null) {
                                        ChatController.getInstance().getJoblistListener().getAdapter().
                                                notifyDataSetChanged();
                                    }
                                } else {/**  * no need for notification craete only update count  ***/
                                    if (fwitem.getCltunread() > 0
                                            &&
                                            ChatListnersContainer.this.getJobId().
                                                    equals(ChatController.getInstance().
                                                            getClientChatFragState(2))) {
                                        ChatController.getInstance().setClientUnreadCountZeroByJobId(
                                                ChatListnersContainer.this.jobCode,
                                                ChatListnersContainer.this.jobId, ChatController.getInstance().getbatchCount(jobId));
                                        if (ChatController.getInstance().getJoblistListener() != null && ChatController.getInstance().getJoblistListener().getAdapter() != null) {
                                            ChatController.getInstance().getJoblistListener().getAdapter().
                                                    notifyDataSetChanged();
                                            /****/
                                            if (ChatController.getInstance().getJobdetailListener() != null)
                                                ChatController.getInstance().getJobdetailListener().showBadgeForClientChat(fwitem.getCltunread());
                                        }

                                    } else {
                                        try {
                                            if (ChatController.getInstance() != null)
                                                if (ChatController.getInstance().getJobdetailListener() != null) {
                                                    ChatController.getInstance().getJobdetailListener().
                                                            clientBatch(fwitem.getCltunread());
                                                    /****/
                                                    ChatController.getInstance().getJobdetailListener().showBadgeForClientChat(fwitem.getCltunread());
                                                }

                                            if (fwitem.getCltunread() > 0)
                                                ChatController.getInstance().getUnreadMessagesByClientJobFwId(ChatListnersContainer.this.jobId);
                                            if (ChatController.getInstance().getJoblistListener() != null && ChatController.getInstance().getJoblistListener().getAdapter() != null) {
                                                ChatController.getInstance().getJoblistListener().getAdapter().
                                                        notifyDataSetChanged();
                                            }

                                        } catch (Exception ex) {
                                            ex.getMessage();
                                        }
                                    }


                                }


                            } else {
//                     set fire base field worker path unread 0 first time
                                FirestoreFieldWorkerModel model = new FirestoreFieldWorkerModel();
                                model.setUnread(0);
                                model.setCltunread(0);
                                FirebaseFirestore.getInstance().document(ChatController.getInstance().getFieldworkerpath(ChatListnersContainer.this.jobCode, ChatListnersContainer.this.jobId)).set(model);
                            }


                        }
                    }
                });
    }

    public ChatListnersContainer() {

    }

    public static ChatListnersContainer getChatListnerInstance() {
        if (ChatListnerInstance == null) {
            ChatListnerInstance = new ChatListnersContainer();
        }
        return ChatListnerInstance;
    }

    /**
     * this used for client count set 0
     ***/
    public void getCltRemove(String jobCode, String jobId) {
        ChatController.getInstance().setClientUnreadCountZeroByJobId(
                jobCode,
                jobId, ChatController.getInstance().getbatchCount(jobId));
    }


    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public int getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(int batchCount) {
        this.batchCount = batchCount;
    }

    public int getCltbatChCount() {
        return cltbatChCount;
    }

    public void setCltbatChCount(int cltbatChCount) {
        this.cltbatChCount = cltbatChCount;
    }

    public ListenerRegistration getListenerRegistration() {
        return listenerRegistration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatListnersContainer)) return false;
        ChatListnersContainer that = (ChatListnersContainer) o;
        return Objects.equals(getJobId(), that.getJobId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJobId());
    }
}
