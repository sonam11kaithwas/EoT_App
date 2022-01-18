package com.eot_app.nav_menu.admin_fw_chat_pkg.chatmember_list_pkg;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.nav_menu.admin_fw_chat_pkg.chatmember_list_pkg.chatmember_mvp.ChatUsersList_PC;
import com.eot_app.nav_menu.admin_fw_chat_pkg.chatmember_list_pkg.chatmember_mvp.ChatUsersList_PI;
import com.eot_app.nav_menu.admin_fw_chat_pkg.chatmember_list_pkg.chatmember_mvp.ChatUsersList_View;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.UserToUserChatActivity;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.user_chat_controller.UserToUserChatController;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.ChatMsgDataModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.MsgModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserChatModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserOflineOnlineModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatmemberListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatmemberListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * Created by Sonam-11 on 2020-03-07.
 */
public class ChatUsersListFragment extends Fragment implements
        ChatUsersListAdpter.ChatMemberCallback, ChatUsersList_View, View.OnClickListener, TextWatcher {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<ChatMsgDataModel> chatMsgDataModelList = new ArrayList<>();
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerview_UserList;
    private LinearLayoutManager layoutManager;
    private ChatUsersListAdpter chatMemberAdpter;
    private ChatUsersList_PI memberChatPi;
    private LinearLayout searchLayout;
    private EditText edtSearch;
    private ImageView search_btn;
    //    private boolean searchTxt;
    private SwipeRefreshLayout swiperefresh;
    private LinearLayout nolist_linear, linearlayout;
    private TextView nolist_txt;

    public ChatUsersListFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatmemberListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatUsersListFragment newInstance(String param1, String param2) {
        ChatUsersListFragment fragment = new ChatUsersListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_singlechat, container, false);
        initializeMyViews(view);
        return view;
    }

    /***initialize My view's***/
    private void initializeMyViews(View view) {
        recyclerview_UserList = view.findViewById(R.id.recyclerview_UserList);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerview_UserList.setLayoutManager(layoutManager);
        recyclerview_UserList.setHasFixedSize(true);
        recyclerview_UserList.setItemAnimator(new DefaultItemAnimator());
        recyclerview_UserList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtility.hideSoftKeyboard(getActivity());
                return false;
            }
        });

        chatMemberAdpter = new ChatUsersListAdpter(new ArrayList<ChatMsgDataModel>(), this, getActivity());
        recyclerview_UserList.setAdapter(chatMemberAdpter);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        searchLayout = view.findViewById(R.id.searchLayout);
        edtSearch = view.findViewById(R.id.edtSearch);
        edtSearch.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.search_by_username));
        edtSearch.addTextChangedListener(this);
        search_btn = view.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(this);
        memberChatPi = new ChatUsersList_PC(this);
        UserToUserChatController.getInstance().setChatUsersListFragment(this);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiperefresh.setRefreshing(true);
                memberChatPi.callApiGetUserList();
            }
        });

        nolist_linear = view.findViewById(R.id.nolist_linear);
        linearlayout = view.findViewById(R.id.linearlayout);
        nolist_txt = view.findViewById(R.id.nolist_txt);
    }

    @Override
    public void disableRefersh() {
        if (swiperefresh.isRefreshing())
            swiperefresh.setRefreshing(false);
    }

    public RecyclerView.Adapter getAdapter() {
        return chatMemberAdpter;
    }

    @Override
    public void setChatUserS(List<UserChatModel> chatUserList) {
        List<ChatMsgDataModel> updateList = getLastmessage(chatUserList);
        List<ChatMsgDataModel> lastmsgList = new ArrayList<>();
        for (ChatMsgDataModel msgModel : updateList) {
            /**filter by msg's list show on Top**/
            if (msgModel.getMsgModel().getContent() != null && msgModel.getMsgModel().getContent().length() >= 1
                    || msgModel.getMsgModel().getDoc() != null) {
                lastmsgList.add(msgModel);
            }
        }

        Collections.sort(lastmsgList, new Comparator<ChatMsgDataModel>() {
            @Override
            public int compare(ChatMsgDataModel o1, ChatMsgDataModel o2) {
                return o2.getMsgModel().getCreatedAt().compareTo(o1.getMsgModel().getCreatedAt());
            }
        });

        Log.e("", "");
        List<ChatMsgDataModel> msgList = new ArrayList<>();
        updateList.removeAll(lastmsgList);
        msgList.addAll(lastmsgList);
        msgList.addAll(updateList);
        this.chatMsgDataModelList = msgList;
        chatMemberAdpter.updateUserChatList(msgList);
        disableRefersh();
        if (chatUserList.size() <= 0) {
            nolist_linear.setVisibility(View.VISIBLE);
            linearlayout.setVisibility(View.GONE);
            nolist_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.user_not_found));
        } else {
            nolist_linear.setVisibility(View.GONE);
            linearlayout.setVisibility(View.VISIBLE);
            nolist_txt.setText("");
        }
    }

    public void updateListItemByLastMsg(String uid) {
        for (ChatMsgDataModel model : chatMsgDataModelList) {
            if (model.getUserChatModel().getUsrId().equals(uid)) {
                MsgModel msgModel = UserToUserChatController.getInstance().getUserLastmsgList(uid);
                model.setMsgModel(msgModel);
                chatMemberAdpter.updateUserChatList(chatMsgDataModelList);
                break;
            }
        }
    }

    /***update user  online/offline & active/inactive status
     * @param uid***/
    public void updateListUserStatus(UserOflineOnlineModel userStatusModel, String uid) {
        if (userStatusModel.getIsInactive().equals("1") &&
                !uid.equals(App_preference.getSharedprefInstance().getLoginRes().getUsrId())) {
            EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(
                    AppConstant.trying_to_chat));
        }
        memberChatPi.getChatUserListFromDB();
    }

    private List<ChatMsgDataModel> getLastmessage(List<UserChatModel> chatUserList) {
        MsgModel msgModel = null;
        ChatMsgDataModel chatMsgDataModel;
        List<ChatMsgDataModel> chatMsgslist = new ArrayList<>();
        for (UserChatModel userModel : chatUserList) {
            chatMsgDataModel = new ChatMsgDataModel();
            /**get user last msg**/
            msgModel = UserToUserChatController.getInstance().getUserLastmsgList(userModel.getUsrId());
            chatMsgDataModel.setUserChatModel(userModel);
            chatMsgDataModel.setMsgModel(msgModel);
            /****get user status***/
            UserOflineOnlineModel userStatus = UserToUserChatController.getInstance().get_user_Status_Model_List(userModel.getUsrId());
            chatMsgDataModel.setUserStatusModel(UserToUserChatController.getInstance().get_user_Status_Model_List(userModel.getUsrId()));
            if (!userStatus.getIsInactive().equals("1")) {
                chatMsgslist.add(chatMsgDataModel);
            }

        }
        UserToUserChatController.getInstance().getTotalCount();
        UserToUserChatController.getInstance().updateDrawer();
        return chatMsgslist;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            /***get all users list without search****/
            //   AppUtility.hideSoftKeyboard(getActivity());
            memberChatPi.getChatUserListFromDB();
        } else if (s.length() > 2) {
            searchGrpUser();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public void onPause() {
        edtSearch.setText("");
        // getAdapter().notifyDataSetChanged();
        UserToUserChatController.getInstance().setState(false);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        memberChatPi.getChatUserListFromDB();
        UserToUserChatController.getInstance().setState(true);
        super.onResume();
    }

    @Override
    public void setSearchVisibility(boolean b) {
        searchLayout.setVisibility(b ? View.VISIBLE : View.GONE);
    }


    /****chat with selected user**/
    @Override
    public void getChatMember(ChatMsgDataModel chatMember) {
        Intent intent = new Intent(getActivity(), UserToUserChatActivity.class);
        intent.putExtra("chatMember", new Gson().toJson(chatMember));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn:
                searchGrpUser();
                AppUtility.hideSoftKeyboard(getActivity());
                break;
        }
    }

    private void searchGrpUser() {
        if (!TextUtils.isEmpty(edtSearch.getText().toString())) {
            //   AppUtility.hideSoftKeyboard(getActivity());
            memberChatPi.getChatUserByName(edtSearch.getText().toString().trim());
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}




