package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.eot_app.R;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.MsgModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserChatModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_mvp.UserToUserChat_View;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;

import java.util.ArrayList;

/**
 * Created by Sonam-11 on 7/4/20.
 */
public class UserChatMsgsAdpter extends RecyclerView.Adapter<UserChatMsgsAdpter.MyViewHolder> {
    private final String TYPE_IMG = "image";
    UserToUserChatActivity context;
    ArrayList<MsgModel> msgList;
    String same_date = "";

    public UserChatMsgsAdpter(UserToUserChatActivity context, ArrayList<MsgModel> msgList) {
        this.context = context;
        this.msgList = msgList;
    }


    @NonNull
    @Override
    public UserChatMsgsAdpter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_adpter_layout, viewGroup, false);
        return new UserChatMsgsAdpter.MyViewHolder(view, context);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {
        final MsgModel model = msgList.get(i);
        String today_date = AppUtility.getDateByFormat(AppUtility.dateTimeByAmPmFormate("dd/MMM/yyyy hh:mm a", "dd/MMM/yyyy kk:mm"));
        String[] today_Date = today_date.split(" ");
        String str = "", usrIDs = "", stringDate = "";

//      /  *get msg Sender name By id**/
        String timeStamp = AppUtility.getDate(Long.parseLong(model.getCreatedAt()));
        String[] new_Date = timeStamp.split(" ");
        if (i != 0) {
            MsgModel chatModel = msgList.get(i - 1);
            String timeStamp1 = AppUtility.getDate(Long.parseLong(chatModel.getCreatedAt()));
            String[] old_Date = timeStamp1.split(" ");
            same_date = old_Date[0];
            if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                    App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0")) {
                str = old_Date[1] + " " + old_Date[2];
            } else {
                str = old_Date[1] + " ";
            }
            usrIDs = chatModel.getSenderId();
        }
        if (i == 0) {
            holder.chat_date.setVisibility(View.VISIBLE);
            if (today_Date[0].equals(new_Date[0])) {
                holder.chat_date.setText("Today");
            } else {
                holder.chat_date.setText(new_Date[0]);
            }
        } else if (!same_date.equals(new_Date[0])) {
            holder.chat_date.setVisibility(View.VISIBLE);
            if (today_Date[0].equals(new_Date[0])) {
                holder.chat_date.setText("Today");
            } else {
                holder.chat_date.setText(new_Date[0]);
            }
        } else {
            holder.chat_date.setVisibility(View.GONE);
            holder.chat_date.setText("");
        }

        if (model.getSenderId().equals(App_preference.getSharedprefInstance().getLoginRes().getUsrId())) {
            holder.sndr_layout2.setVisibility(View.VISIBLE);
            holder.rcv_layout1.setVisibility(View.GONE);

            try {
                if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                        App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0"))
                    stringDate = new_Date[1] + " " + new_Date[2];
                else
                    stringDate = new_Date[1] + "";

                holder.date2.setText(stringDate);

            } catch (Exception e) {
                e.getMessage();
            }

            if (i != 0)
                showHideTimeMsgView(usrIDs, model.getSenderId(), str, stringDate, holder.date2);
            else holder.date2.setVisibility(View.VISIBLE);

            if (model.getContent() != null && !model.getContent().equals("")) {
                holder.sendr_msgs.setVisibility(View.VISIBLE);
                holder.send_img.setVisibility(View.GONE);
                holder.send_all_file_layout.setVisibility(View.GONE);
                holder.sendr_msgs.setText(model.getContent());
                Linkify.addLinks(holder.sendr_msgs, Linkify.WEB_URLS);
                holder.sendr_msgs.setLinkTextColor(Color.BLUE);
                holder.send_usrNm.setText(App_preference.getSharedprefInstance().getLoginRes().getFnm() + " " + App_preference.getSharedprefInstance().getLoginRes().getLnm());
            } else if (model.getDoc() != null && model.getDoc().contains(".jpg") ||
                    model.getDoc().contains(".png") || model.getDoc().contains(".jpeg")
            ) {
                //'jpg, png, jpeg, pdf, doc, docx, xlsx, csv, xls
                holder.setImg_url(App_preference.getSharedprefInstance().getBaseURL() + model.getDoc());
                holder.sendr_msgs.setVisibility(View.GONE);
                holder.send_all_file_layout.setVisibility(View.GONE);
                holder.send_img.setVisibility(View.VISIBLE);
                holder.send_img.setClickable(false);
                holder.send_usrNm.setText((App_preference.getSharedprefInstance().getLoginRes().getFnm() + " " + App_preference.getSharedprefInstance().getLoginRes().getLnm()));
                if (context != null) {
                    try {
                        Glide.with(EotApp.getAppinstance()).load(App_preference.getSharedprefInstance().getBaseURL() + "" +
                                model.getDoc()).addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.send_img.setClickable(true);
                                return false;
                            }
                        }).placeholder(R.drawable.ic_gallery)
                                .centerCrop()
                                .override(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                                .into(holder.send_img);
                    } catch (Exception ex) {
                        ex.getMessage();
                    }

                }
            } else if (model.getDoc() != null && model.getDoc().contains(".pdf")
                    || model.getDoc().contains(".doc") || model.getDoc().contains(".docs")
                    || model.getDoc().contains(".xlsx") || model.getDoc().contains(".csv")
                    || model.getDoc().contains(".xls")) {
                //pdf, doc, docx, xlsx, csv, xls
                holder.send_all_file_layout.setVisibility(View.VISIBLE);
                holder.send_img.setVisibility(View.GONE);
                holder.sendr_msgs.setVisibility(View.GONE);
                holder.all_types_file_send.setText("Open File");//model.getFile()
                holder.send_usrNm.setText((App_preference.getSharedprefInstance().getLoginRes().getFnm() + " " + App_preference.getSharedprefInstance().getLoginRes().getLnm()));
                // holder.all_types_file_send.setTextColor(Color.BLUE);

                holder.send_all_file_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.openUriOnBroWser(App_preference.getSharedprefInstance().getBaseURL() + "" + model.getDoc(),
                                "");
                    }
                });
            }
        } else {
            holder.sndr_layout2.setVisibility(View.GONE);
            holder.rcv_layout1.setVisibility(View.VISIBLE);


            try {
                if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                        App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0")) {
                    stringDate = new_Date[1] + " " + new_Date[2];
                } else {
                    stringDate = new_Date[1] + "";
                }
                holder.date1.setText(stringDate);

            } catch (Exception e) {
                e.getMessage();
            }

            if (i != 0)
                showHideTimeMsgView(usrIDs, model.getSenderId(), str, stringDate, holder.date1);
            else holder.date1.setVisibility(View.VISIBLE);


            UserChatModel nameById = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).userChatModel().
                    getUserById(model.getSenderId());
            if (model.getContent() != null && !model.getContent().equals("")) {
                holder.recv_msgs.setVisibility(View.VISIBLE);
                holder.rcv_img.setVisibility(View.GONE);
                holder.rcv_all_file_layout.setVisibility(View.GONE);
                holder.recv_msgs.setText(model.getContent());
                Linkify.addLinks(holder.recv_msgs, Linkify.WEB_URLS);
                holder.recv_msgs.setLinkTextColor(Color.BLUE);
                holder.rev_usrNm.setText(nameById.getFnm() + " " + nameById.getLnm());
            } else if (model.getDoc() != null && model.getDoc().contains(".jpg") ||
                    model.getDoc().contains(".png") || model.getDoc().contains(".jpeg")) {
                holder.setImg_url(App_preference.getSharedprefInstance().getBaseURL() + "" + model.getDoc());
                holder.recv_msgs.setVisibility(View.GONE);
                holder.rcv_all_file_layout.setVisibility(View.GONE);
                holder.rcv_img.setVisibility(View.VISIBLE);
                holder.rcv_img.setClickable(false);
                holder.rev_usrNm.setText(nameById.getFnm() + " " + nameById.getLnm());
                try {
                    Glide.with(EotApp.getAppinstance()).load(App_preference.getSharedprefInstance().getBaseURL() + "" +
                            model.getDoc()).addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.rcv_img.setClickable(true);
                            return false;
                        }
                    })
                            .placeholder(R.drawable.ic_gallery)
                            .centerCrop()
                            .override(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//220
                            .into(holder.rcv_img);
                } catch (Exception ex) {
                    ex.getMessage();
                }

            } else if (model.getDoc() != null && model.getDoc().contains(".pdf")
                    || model.getDoc().contains(".doc") || model.getDoc().contains(".docs")
                    || model.getDoc().contains(".xlsx") || model.getDoc().contains(".csv")
                    || model.getDoc().contains(".xls")) {
                holder.rcv_all_file_layout.setVisibility(View.VISIBLE);
                holder.rcv_img.setVisibility(View.GONE);
                holder.recv_msgs.setVisibility(View.GONE);
                holder.all_types_file_rcv.setText("Open File");//model.getFile()
                holder.rev_usrNm.setText(nameById.getFnm() + " " + nameById.getLnm());
                // holder.all_types_file_rcv.setTextColor(Color.BLUE);

                holder.rcv_all_file_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.openUriOnBroWser(App_preference.getSharedprefInstance().getBaseURL() + "" + model.getDoc(), "");
                    }
                });
            }
            /* */
        }
    }

    private void showHideTimeMsgView(String usrIDs, String usrid, String str, String stringDate, TextView date1) {
        try {
            if (usrIDs.equals(usrid)) {
                if (!str.equals(stringDate)) {
                    date1.setVisibility(View.VISIBLE);
                } else {
                    date1.setVisibility(View.GONE);
                }
            } else {
                date1.setVisibility(View.VISIBLE);

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public void updateMyAdpter(ArrayList<MsgModel> msgList) {
        this.msgList = msgList;
        notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView recv_msgs;
        private final TextView sendr_msgs;
        private final TextView chat_date;
        private final TextView date2;
        private final TextView date1;
        private final TextView rev_usrNm;
        private final TextView send_usrNm;
        private final LinearLayout rcv_layout1;
        private final LinearLayout sndr_layout2;
        private final ImageView send_img;
        private final ImageView rcv_img;
        private final UserToUserChat_View mlistener;
        private final TextView all_types_file_send;
        private final TextView all_types_file_rcv;
        private final LinearLayout send_all_file_layout;
        private final LinearLayout rcv_all_file_layout;
        private String img_url = "";

        public MyViewHolder(@NonNull View itemView, UserToUserChatActivity mlistener) {
            super(itemView);
            this.mlistener = mlistener;
            recv_msgs = itemView.findViewById(R.id.recv_msgs);
            sendr_msgs = itemView.findViewById(R.id.sendr_msgs);

            rcv_layout1 = itemView.findViewById(R.id.rcv_layout1);
            sndr_layout2 = itemView.findViewById(R.id.sndr_layout2);

            chat_date = itemView.findViewById(R.id.chat_date);

            date2 = itemView.findViewById(R.id.time2);
            date1 = itemView.findViewById(R.id.time1);

            send_img = itemView.findViewById(R.id.send_img);
            rcv_img = itemView.findViewById(R.id.rcv_img);

            send_img.setOnClickListener(this);
            rcv_img.setOnClickListener(this);

            rev_usrNm = itemView.findViewById(R.id.rev_usrNm);
            send_usrNm = itemView.findViewById(R.id.send_usrNm);

            all_types_file_rcv = itemView.findViewById(R.id.all_types_file_rcv);
            all_types_file_send = itemView.findViewById(R.id.all_types_file_send);

            send_all_file_layout = itemView.findViewById(R.id.send_all_file_layout);
            rcv_all_file_layout = itemView.findViewById(R.id.rcv_all_file_layout);

//            all_types_file_rcv.setOnClickListener(this);
//            all_types_file_send.setOnClickListener(this);
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.send_img:
                    mlistener.openImage(v, ((ImageView) v).getDrawable(), getImg_url());
                    break;
                case R.id.rcv_img:
                    mlistener.openImage(v, ((ImageView) v).getDrawable(), getImg_url());
                    break;
                case R.id.all_types_file_send:
                    //context.openUriOnBroWser();
                    break;
                case R.id.all_types_file_rcv:
                    break;
            }


        }


    }
}
