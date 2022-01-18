package com.eot_app.nav_menu.admin_fw_chat_pkg.chatmember_list_pkg;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.user_chat_controller.UserToUserChatController;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.ChatMsgDataModel;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sonam-11 on 2020-03-07.
 */
public class ChatUsersListAdpter extends RecyclerView.Adapter<ChatUsersListAdpter.MyViewHolder> {
    private final Context context;
    private final ChatMemberCallback chatMemberCallback;
    private List<ChatMsgDataModel> chatMembersList;

    public ChatUsersListAdpter(List<ChatMsgDataModel> chatMemberList, ChatMemberCallback callback, Context context) {
        this.chatMembersList = chatMemberList;
        this.context = context;
        this.chatMemberCallback = callback;
    }

    public void updateUserChatList(List<ChatMsgDataModel> chatMembersList) {
        this.chatMembersList = chatMembersList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chatmember_adpterlayout, null);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Log.e("", "");
        final ChatMsgDataModel memberModel = chatMembersList.get(i);

        /**
         * Shuflling problem***/
        myViewHolder.item_title_name.setText("");
        myViewHolder.userLastMsg.setCompoundDrawablesWithIntrinsicBounds(
                0, 0, 0, 0);
        myViewHolder.userLastMsg.setText("");
        myViewHolder.userLastMsgTime.setText("");
        myViewHolder.badge_count.setText("");


        myViewHolder.item_title_name.setText(memberModel.getUserChatModel().getFnm() + " " +
                memberModel.getUserChatModel().getLnm());
        myViewHolder.chatcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatMemberCallback.getChatMember(memberModel);
            }
        });
        //*** User Msg notification Batch count ***//*
        int batchCount = UserToUserChatController.getInstance().getCountList(memberModel.getUserChatModel().getUsrId());
        if (batchCount > 0) {
            myViewHolder.badge_count.setVisibility(View.VISIBLE);
            myViewHolder.badge_count.setText(String.valueOf(batchCount));
        } else {
            myViewHolder.badge_count.setVisibility(View.GONE);
        }
        /***user image/profile set***/
        if (chatMembersList.get(i).getUserChatModel().getIsTeam() != null && chatMembersList.get(i).getUserChatModel().getIsTeam().equals("1")
                && chatMembersList.get(i).getUserChatModel().getTeamMemId() != null && chatMembersList.get(i).getUserChatModel().getTeamMemId().size() > 0) {
            Picasso.with(context).load(R.drawable.grp_chat).into(myViewHolder.userImg);

        } else {
            if (chatMembersList.get(i).getUserChatModel().getImg() != null
                    && !chatMembersList.get(i).getUserChatModel().getImg().equals("")) {
                Picasso.with(context).load(App_preference.getSharedprefInstance().getBaseURL() +
                        chatMembersList.get(i).getUserChatModel().getImg()).
                        placeholder(R.drawable.chat_user_ic).error(R.drawable.chat_user_ic)
                        .into(myViewHolder.userImg);
            } else {
                Picasso.with(context).load(R.drawable.chat_user_ic).into(myViewHolder.userImg);
            }
        }
        /**** *//*******
         * *user online/offline status ***/
        if (chatMembersList.get(i).getUserChatModel().getIsTeam() != null && chatMembersList.get(i).getUserChatModel().getIsTeam() != null && chatMembersList.get(i).getUserChatModel().getIsTeam().equals("1")
                && chatMembersList.get(i).getUserChatModel().getTeamMemId() != null && chatMembersList.get(i).getUserChatModel().getTeamMemId().size() > 0) {
            myViewHolder.batchIcon.setVisibility(View.GONE);

        } else {
            myViewHolder.batchIcon.setVisibility(View.VISIBLE);
            if (memberModel.getUserStatusModel().getIsOnline().equals("1") || memberModel.getUserStatusModel().getIsOnline().equals("3")) {
                myViewHolder.batchIcon.setBackground(context.getResources().getDrawable(R.drawable.user_online_batch));
            } else {
                myViewHolder.batchIcon.setBackground(context.getResources().getDrawable(R.drawable.user_offline_batch));
            }
        }


        /***last msg***/
        if (memberModel.getMsgModel() != null) {
            if (memberModel.getMsgModel().getContent() != null && !memberModel.getMsgModel().getContent().equals("")) {
                myViewHolder.userLastMsg.setText("");
                myViewHolder.userLastMsg.setText(memberModel.getMsgModel().getContent());
            } else if (memberModel.getMsgModel().getDoc() != null && !memberModel.getMsgModel().getDoc().equals("")) {
                String ps = memberModel.getMsgModel().getDoc().substring(0, memberModel.getMsgModel().getDoc().lastIndexOf('.'));
                String[] parts = ps.split("/");
                String filename = parts[parts.length - 1];
                String extension = "";
                int ext = memberModel.getMsgModel().getDoc().lastIndexOf('.');
                if (ext > 0) {
                    extension = memberModel.getMsgModel().getDoc().substring(i + 1);
                }
                String ex = "";
                int j = memberModel.getMsgModel().getDoc().lastIndexOf('.');
                if (j > 0) {
                    ex = memberModel.getMsgModel().getDoc().substring(j + 1);
                }
                Log.e("j", ex);
                if (extension.contains(".jpg") || extension.contains(".png") || extension.contains(".jpeg")) {
                    myViewHolder.userLastMsg.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_insert_photo_black_24dp, 0, 0, 0);
                    myViewHolder.userLastMsg.setText("photo");
                } else if (extension.contains(".pdf") || extension.contains(".doc") || extension.contains(".docx")
                        || extension.contains(".xlsx") || extension.contains(".csv") || extension.contains(".xls")) {
                    myViewHolder.userLastMsg.setText(filename + "." + ex);
                } else {
                    myViewHolder.userLastMsg.setText("Start new chat here");
                }
            }
        } else {
            myViewHolder.userLastMsg.setText("Start new chat here");
        }

        /***last MSG's time***/
        if (memberModel.getMsgModel().getCreatedAt() != null && !memberModel.getMsgModel().getCreatedAt().equals("")) {
            String todaydatetime = AppUtility.getDateByFormat(AppUtility.dateTimeByAmPmFormate("dd/MM/yyyy hh:mm a",
                    "dd/MM/yyyy kk:mm"));
            String[] todayDateTime = todaydatetime.split(" ");
            String yesterdaydatetime = AppUtility.getYesterDayDate(
                    AppUtility.dateTimeByAmPmFormate("dd/MM/yyyy hh:mm a", "dd/MM/yyyy kk:mm"), -1);
            String[] yesterDayDateTime = yesterdaydatetime.split(" ");
            String msgdatetime = AppUtility.getDate(Long.parseLong(memberModel.getMsgModel().getCreatedAt()),
                    AppUtility.dateTimeByAmPmFormate("dd/MM/yyyy hh:mm a",
                            "dd/MM/yyyy kk:mm"));
            String[] msgDateTime = msgdatetime.split(" ");
            if (todayDateTime[0].equals(msgDateTime[0])) {
                try {
                    if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                            App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0"))
                        myViewHolder.userLastMsgTime.setText(msgDateTime[1] + " " + msgDateTime[2]);
                    else myViewHolder.userLastMsgTime.setText(msgDateTime[1] + "");

                } catch (Exception e) {
                    e.getMessage();
                }
            } else if (yesterDayDateTime[0].equals(msgDateTime[0])) {
                myViewHolder.userLastMsgTime.setText("Yesterday");
            } else {
                myViewHolder.userLastMsgTime.setText(msgDateTime[0]);
            }
        } else {
            myViewHolder.userLastMsgTime.setText("");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return chatMembersList.size();
    }


    interface ChatMemberCallback {
        void getChatMember(ChatMsgDataModel chatMember);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item_title_name, badge_count, userLastMsgTime, userLastMsg;
        CardView chatcard;
        ImageView userImg, batchIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            chatcard = itemView.findViewById(R.id.chatcard);
            item_title_name = itemView.findViewById(R.id.item_title_name);
            badge_count = itemView.findViewById(R.id.badge_count);
            userImg = itemView.findViewById(R.id.userImg);
            batchIcon = itemView.findViewById(R.id.batchIcon);
            userLastMsgTime = itemView.findViewById(R.id.userLastMsgTime);
            userLastMsg = itemView.findViewById(R.id.userLastMsg);
        }
    }
}
