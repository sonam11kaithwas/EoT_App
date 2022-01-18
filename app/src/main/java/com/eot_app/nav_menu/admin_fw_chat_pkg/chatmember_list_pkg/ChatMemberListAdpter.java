package com.eot_app.nav_menu.admin_fw_chat_pkg.chatmember_list_pkg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.admin_fw_chat_pkg.chat_single_pkg.AdminChatController;
import com.eot_app.nav_menu.admin_fw_chat_pkg.chatmember_list_pkg.chatmember_model.ChatUserListResModel;
import com.eot_app.utility.App_preference;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sonam-11 on 2020-03-07.
 */
public class ChatMemberListAdpter extends RecyclerView.Adapter<ChatMemberListAdpter.MyViewHolder> {
    private final List<ChatUserListResModel> chatMembersList;
    private final Context context;
    private final ChatMemberCallback chatMemberCallback;
    private ChatMemberCallback ChatMemberCallback;

    public ChatMemberListAdpter(List<ChatUserListResModel> chatMemberList, ChatMemberCallback callback, Context context) {
        this.chatMembersList = chatMemberList;
        this.context = context;
        this.chatMemberCallback = callback;
    }

    public void updateUserChatList(List<ChatUserListResModel> chatUserList) {
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chatmember_adpterlayout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final ChatUserListResModel memberModel = chatMembersList.get(i);
        myViewHolder.item_title_name.setText(memberModel.getFnm());
        myViewHolder.chatcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatMemberCallback.getChatMember(memberModel);
            }
        });
        /*** Job notification Batch count ***/

        int batchCount = AdminChatController.getAdminChatInstance().getUserChatBatchCount();
        if (batchCount > 0) {
            myViewHolder.badge_count.setVisibility(View.VISIBLE);
            myViewHolder.badge_count.setText(String.valueOf(batchCount));

        } else {
            myViewHolder.badge_count.setVisibility(View.GONE);
        }
        if (!memberModel.getImg().isEmpty()) {
            Picasso.with(context).load(App_preference.getSharedprefInstance().getBaseURL() +
                    memberModel.getImg()).placeholder(R.drawable.ic_profile).error(R.drawable.ic_profile)
                    .into(myViewHolder.userImg);

        } else {
            myViewHolder.userImg.setBackground(context.getResources().getDrawable(R.drawable.crcl_img));
        }

        if (memberModel.getUserStatus() != null && memberModel.getUserStatus().equals("0")) {
            myViewHolder.batchIcon.setVisibility(View.VISIBLE);
            myViewHolder.batchIcon.setBackground(context.getResources().getDrawable(R.drawable.user_offline_batch));
        } else if (memberModel.getUserStatus() != null && memberModel.getUserStatus().equals("1")) {
            myViewHolder.batchIcon.setVisibility(View.VISIBLE);
            myViewHolder.batchIcon.setBackground(context.getResources().getDrawable(R.drawable.user_online_batch));
        } else {
            myViewHolder.batchIcon.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return chatMembersList.size();
    }

    interface ChatMemberCallback {
        void getChatMember(ChatUserListResModel chatMember);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item_title_name, badge_count;
        CardView chatcard;
        ImageView userImg, batchIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            chatcard = itemView.findViewById(R.id.chatcard);
            item_title_name = itemView.findViewById(R.id.item_title_name);
            badge_count = itemView.findViewById(R.id.badge_count);
            userImg = itemView.findViewById(R.id.userImg);
            batchIcon = itemView.findViewById(R.id.batchIcon);
        }
    }
}
