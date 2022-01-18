package com.eot_app.nav_menu.jobs.job_detail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.eot_app.R;
import com.eot_app.home_screens.MainActivity;
import com.eot_app.login_next.FooterMenu;
import com.eot_app.login_next.login_next_model.CompPermission;
import com.eot_app.nav_menu.client_chat_pkg.ClientChatFragment;
import com.eot_app.nav_menu.jobs.job_controller.ChatController;
import com.eot_app.nav_menu.jobs.job_controller.ChatListnersContainer;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_db.JtId;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.nav_menu.jobs.job_detail.chat.ChatFragment;
import com.eot_app.nav_menu.jobs.job_detail.customform.ButtomBarAdapter;
import com.eot_app.nav_menu.jobs.job_detail.customform.CustomFormListFragment;
import com.eot_app.nav_menu.jobs.job_detail.customform.cstm_form_model.CustomFormList_Res;
import com.eot_app.nav_menu.jobs.job_detail.customform.cstm_form_mvp.Cstm_Form_Pc;
import com.eot_app.nav_menu.jobs.job_detail.detail.DetailFragment;
import com.eot_app.nav_menu.jobs.job_detail.documents.DocumentsFragment;
import com.eot_app.nav_menu.jobs.job_detail.feedback.FeedbackFragment;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.FormQueAns_Activity;
import com.eot_app.nav_menu.jobs.job_detail.generate_invoice.Generate_Invoice_Activity;
import com.eot_app.nav_menu.jobs.job_detail.history.HistoryFragment;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg.Invoice_Email_Activity;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_payment_pkg.Payment_Activity;
import com.eot_app.nav_menu.jobs.job_detail.invoice2list.JoBInvoiceItemList2Activity;
import com.eot_app.nav_menu.jobs.job_detail.job_detail_activity_presenter.Job_Detail_Activity_View;
import com.eot_app.nav_menu.jobs.job_detail.job_detail_activity_presenter.Job_Detail_Activity_pc;
import com.eot_app.nav_menu.jobs.job_detail.job_detail_activity_presenter.Job_Detail_Activity_pi;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.JobEquipmentActivity;
import com.eot_app.services.GpsUtils;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.CustomViewPagerState;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.SignatureView;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.MyListItemSelected;
import com.eot_app.utility.util_interfaces.NotificationObserver;
import com.eot_app.utility.util_interfaces.OnFragmentInteractionListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.hypertrack.hyperlog.HyperLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class JobDetailActivity extends AppCompatActivity implements
        OnFragmentInteractionListener,
        ViewPager.OnPageChangeListener,
        NotificationObserver, Job_Detail_Activity_View, ResultCallback<LocationSettingsResult> {
    private static final int CUSTOM_FORM_REQUESTCALL = 1010;
    private static final String TAG = JobDetailActivity.class.getSimpleName();
    private final int DETAIL_VIEW = 0;
    private final int DOCUMENT_VIEW = 1;
    private final int CHAT_VIEW = 2;
    private final int FEEDBACK_VIEW = 3;
    private final int HISTORY_VIEW = 4;
    private final int CLINET_CHAT_VIEW = 5;
    private final int CUSTOM_FORM_VIEW = 8;
    private final int ID_MORE = 100;
    private final int ID_JOB_DETAIL = 101;
    private final int ID_ITEM = 102;
    private final int ID_DOCUMENTS = 103;
    private final int ID_CHAT = 104;
    private final int ID_FEEDBACK = 105;
    private final int ID_HISTORY = 106;
    private final int ID_PAYMENT = 107;
    private final int ID_GENERATE_INVOICE = 108;
    private final int ID_CLIENT_CHAT = 110;
    private final int ID_CUSTOM_FORM = 10;
    private final int ID_JOB_EQUIPMENT = 11;
    private final List<MenuItemsModel> menu_items_ilist = new ArrayList<>();
    public BottomNavigationView navigation;
    public boolean CLIENTCHATWINDOW;
    Bundle bundle;
    Job dataJob;
    Cstm_Form_Pc cstm_form_pc;
    ArrayList<CustomFormList_Res> customFormLists = new ArrayList<>();
    private MypagerAdapter mypagerAdapter;
    private DetailFragment detailFragment;
    private DocumentsFragment documentsFragment;
    private CustomViewPagerState viewPager;
    private Job_Detail_Activity_pi detail_activity_pi;
    private TextView text, clientChatTextView;
    private BottomSheetDialog mBottomSheetDialog;
    private ButtomBarAdapter buttomBarAdapter;
    private String appId;
    private boolean DOCUMENTSELECT = false;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case ID_JOB_DETAIL:
                    ChatController.getInstance().setClientChatState(2, "");
                    // ChatController.getInstance().setChatScreenState(2, "");
                    resetBottomMenuSelection();
                    viewPager.setCurrentItem(DETAIL_VIEW, false);
                    CLIENTCHATWINDOW = false;
                    //   COMMNENTWINDOW = false;
                    return true;
                case ID_ITEM:
                    ChatController.getInstance().setClientChatState(2, "");
                    // ChatController.getInstance().setChatScreenState(2, "");
                    CLIENTCHATWINDOW = false;
                    // COMMNENTWINDOW = false;
                    resetBottomMenuSelection();
                    Intent intent = new Intent(JobDetailActivity.this, JoBInvoiceItemList2Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("JobId", dataJob.getJobId());
                    startActivity(intent);
                    return false;
                case ID_CHAT:
                    ChatController.getInstance().setClientChatState(2, "");
                    //ChatController.getInstance().setChatScreenState(2, dataJob.getJobId());
                    // ChatController.getInstance().setChatScreenState(0, "");
//                    ChatController.getInstance().setUnreadCountZeroByJobId(dataJob.getLabel(), dataJob.getJobId(),
//                            ChatController.getInstance().getClientChatBatchCount(dataJob.getJobId()));
                    CLIENTCHATWINDOW = false;
                    // COMMNENTWINDOW = true;
                    resetBottomMenuSelection();
                    viewPager.setCurrentItem(CHAT_VIEW, false);
                    return true;
                case ID_CLIENT_CHAT:
                    ChatController.getInstance().setClientChatState(2, dataJob.getJobId());
                    // ChatController.getInstance().setChatScreenState(2, "");

                    ChatListnersContainer.getChatListnerInstance().getCltRemove(dataJob.getLabel(), dataJob.getJobId());
                    CLIENTCHATWINDOW = true;
                    // COMMNENTWINDOW = false;
                    resetBottomMenuSelection();
                    viewPager.setCurrentItem(CLINET_CHAT_VIEW, false);
                    return true;
                case ID_DOCUMENTS:
                    //  ChatController.getInstance().setClientChatState(2, "");
                    ChatController.getInstance().setChatScreenState(2, "");
                    CLIENTCHATWINDOW = false;
                    DOCUMENTSELECT = true;
                    //  COMMNENTWINDOW = false;
                    resetBottomMenuSelection();
                    viewPager.setCurrentItem(DOCUMENT_VIEW, false);
                    if (documentsFragment != null) {
                        documentsFragment.updateDocList();
                    }
                    return true;
                case ID_HISTORY:
                    ChatController.getInstance().setClientChatState(2, "");
                    //   ChatController.getInstance().setChatScreenState(2, "");
                    CLIENTCHATWINDOW = false;
                    //  COMMNENTWINDOW = false;
                    resetBottomMenuSelection();
                    viewPager.setCurrentItem(HISTORY_VIEW, false);
                    return true;
                case ID_FEEDBACK:
                    ChatController.getInstance().setClientChatState(2, "");
                    //   ChatController.getInstance().setChatScreenState(2, "");
                    CLIENTCHATWINDOW = false;
                    // COMMNENTWINDOW = false;
                    resetBottomMenuSelection();
                    viewPager.setCurrentItem(FEEDBACK_VIEW, false);
                    return true;
                case ID_PAYMENT:
                    ChatController.getInstance().setClientChatState(2, "");
                    // ChatController.getInstance().setChatScreenState(2, "");
                    CLIENTCHATWINDOW = false;
                    //  COMMNENTWINDOW = false;
                    resetBottomMenuSelection();
                    Intent paymentIntent = new Intent(JobDetailActivity.this, Payment_Activity.class);
                    paymentIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    paymentIntent.putExtra("JobId", dataJob.getJobId());
                    startActivity(paymentIntent);
                    return false;
                case ID_CUSTOM_FORM:
                    ChatController.getInstance().setClientChatState(2, "");
                    //ChatController.getInstance().setChatScreenState(2, "");
                    CLIENTCHATWINDOW = false;
                    // COMMNENTWINDOW = false;
                    resetBottomMenuSelection();
                    viewPager.setCurrentItem(CUSTOM_FORM_VIEW, false);
                    return true;
                case ID_GENERATE_INVOICE:
                    /***** *//**
                 * canInvoiceCreated =0   Do not create  invoice /1 create invoice
                 * check permission for the job is allow to generate invoice
                 * **//****/

                    ChatController.getInstance().setClientChatState(2, "");
                    //ChatController.getInstance().setChatScreenState(2, "");
                    CLIENTCHATWINDOW = false;
                    //   COMMNENTWINDOW = false;
                    if (dataJob != null && dataJob.getCanInvoiceCreated() != null &&
                            dataJob.getCanInvoiceCreated().equals("0")) {
                        AppUtility.alertDialog(JobDetailActivity.this,
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_invoice),
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.invoice_can_not_generate_msg), null,
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), null);
                    } else
                        detail_activity_pi.getItemFromServer(dataJob.getJobId());
                    return false;
                case ID_JOB_EQUIPMENT:
                    ChatController.getInstance().setClientChatState(2, "");
                    // ChatController.getInstance().setChatScreenState(2, "");
                    CLIENTCHATWINDOW = false;
                    //  COMMNENTWINDOW = false;
                    resetBottomMenuSelection();
                    Intent intent1 = new Intent(JobDetailActivity.this, JobEquipmentActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent1.putExtra("JobId", dataJob.getJobId());
                    intent1.putExtra("cltId", dataJob.getCltId());
                    intent1.putExtra("contrId", dataJob.getContrId());
                    intent1.putExtra("appId", appId);
                    if (dataJob.getEquArray() != null && dataJob.getEquArray().size() > 0)
                        intent1.putExtra("auditid", dataJob.getEquArray().get(0).getAudId());
                    else intent1.putExtra("auditid", "0");
                    startActivityForResult(intent1, 300);
                    return false;
                case ID_MORE:
                    CLIENTCHATWINDOW = false;
                    // COMMNENTWINDOW = false;
                    ChatController.getInstance().setClientChatState(2, "");
                    // ChatController.getInstance().setChatScreenState(2, "");
                    addButtomCustomFormWindow();
                    if (mBottomSheetDialog != null) {
                        mBottomSheetDialog.show();
                    }
                    return true;
            }
            if (mBottomSheetDialog != null && buttomBarAdapter != null)
                buttomBarAdapter.updateSelectedMenu(0);
            return false;
        }
    };
    private boolean keyboardListenersAttached = false;
    private ViewGroup rootLayout;
    private final ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
            int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();

            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(JobDetailActivity.this);

            if (heightDiff <= contentViewTop) {
                onHideKeyboard();

                Intent intent = new Intent("KeyboardWillHide");
                broadcastManager.sendBroadcast(intent);
            } else {
                int keyboardHeight = heightDiff - contentViewTop;
                onShowKeyboard(keyboardHeight);
                Intent intent = new Intent("KeyboardWillShow");
                intent.putExtra("KeyboardHeight", keyboardHeight);
                broadcastManager.sendBroadcast(intent);
            }
        }
    };

    private void selectBottomMenu(int ID) {
        if (buttomBarAdapter != null)
            buttomBarAdapter.updateSelectedMenu(ID);
    }

    private void resetBottomMenuSelection() {
        if (mBottomSheetDialog != null && buttomBarAdapter != null)
            buttomBarAdapter.updateSelectedMenu(0);
    }

    @Override
    protected void onDestroy() {
        EotApp.getAppinstance().setObserver(null);
        ChatController.getInstance().setJobdetailListener(null);

        ChatController.getInstance().setChatScreenState(0, "");
        ChatController.getInstance().setChatScreenState(1, "");

        super.onDestroy();

        if (keyboardListenersAttached) {
            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(keyboardLayoutListener);
        }
    }

    /*****show Client Chat Total count for msg's for Bottom navigation***/
    public void showBadgeForClientChat(int value) {
        try {
            if (clientChatTextView != null && !CLIENTCHATWINDOW) {
                if (value == 0) {
                    clientChatTextView.setVisibility(View.GONE);
                    clientChatTextView.setText("");
                } else {
                    clientChatTextView.setVisibility(View.VISIBLE);
                    clientChatTextView.setText(String.valueOf(value));
                }
            } else {
                if (clientChatTextView != null) {
                    clientChatTextView.setVisibility(View.GONE);
                    clientChatTextView.setText("");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /********/
    public void showBadge(int value) {
        try {
            if (text != null) {
                if (value == 0) {
                    text.setVisibility(View.GONE);
                    text.setText("");
                } else {
                    text.setVisibility(View.VISIBLE);
                    text.setText(String.valueOf(value));
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void clientBatch(int count) {
//        if ((buttomBarAdapter != null) && (count > 0)) {
//            buttomBarAdapter.setCount(count);
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        /***Add bottom navigation Option's *****/
        initializeMenuItemList();

        /*****add current(roottask) activity when app at background*/
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
            attachKeyboardListeners();
        }

        viewPager = findViewById(R.id.jobdetail_pager);
        setUpViewPager(viewPager);
        navigation = findViewById(R.id.navigation);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_details));
        Menu menu = navigation.getMenu();
        if (menu_items_ilist.size() > 0) {
            int count_menu = 0;
            for (MenuItemsModel item : menu_items_ilist) {
                if (count_menu == 4) {
                    break;
                }
                menu.add(Menu.NONE, item.getMenu_item_id(), Menu.NONE, item.getMenu_title()).setIcon(item.getMenu_icon());
                item.setIsalreadySet(true);
                count_menu++;
            }
//            more option is always stay on the place.
            if (menu_items_ilist.size() > 4)
                menu.add(Menu.NONE, ID_MORE, Menu.NONE, LanguageController.getInstance().getMobileMsgByKey(AppConstant.more)).setIcon(R.drawable.ic_more_horiz_black_24dp);
        }


        /**   add job chat batch count on tab ***/
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < bottomNavigationMenuView.getChildCount(); i++) {
            int chatId = bottomNavigationMenuView.getChildAt(i).getId();
            if (chatId == ID_CHAT) {
                View v = bottomNavigationMenuView.getChildAt(i);
                BottomNavigationItemView itemView = (BottomNavigationItemView) v;
                /*** add view for chat batch icon ***/
                View badge = LayoutInflater.from(this).inflate(R.layout.chat_batch_count_layout, navigation, false);
                text = badge.findViewById(R.id.badge_text_view);
                itemView.addView(badge);
            } else if (chatId == ID_CLIENT_CHAT) {
                View v = bottomNavigationMenuView.getChildAt(i);
                BottomNavigationItemView itemView = (BottomNavigationItemView) v;
                /*** add view for chat batch icon ***/
                View badge = LayoutInflater.from(this).inflate(R.layout.chat_batch_count_layout, navigation, false);
                clientChatTextView = badge.findViewById(R.id.badge_text_view);
                itemView.addView(badge);
            }
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /** *intent data for Notifications*****/

        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (getIntent().hasExtra("JOBS")) {
                try {
                    String str = getIntent().getExtras().getString("JOBS");
                    dataJob = new Gson().fromJson(str, Job.class);
                    appId = bundle.getString("appId");
                    EotApp.getAppinstance().setObserver(this); // use to check remove field worker notification.
                } catch (Exception e) {
                    startActivity(new Intent(this, MainActivity.class));
                    this.finish();
                    e.getMessage();
                }

            } else
            /*** Job Chat notification   ***/
                if (getIntent().hasExtra("CHAT_JOB")) {
                    try {
                        String str = getIntent().getExtras().getString("CHAT_JOB");
                        dataJob = new Gson().fromJson(str, Job.class);
                        // dataJob = bundle.getParcelable("CHAT_JOB");
                        Job tempJobData = AppDataBase.getInMemoryDatabase(this).jobModel().getJobsById(dataJob.getJobId());
                        if ((dataJob != null && tempJobData != null) && dataJob.getJobId().equals(tempJobData.getJobId())) {
                            if (navigation.getMenu().findItem(ID_CHAT) != null) {
                                navigation.setSelectedItemId(ID_CHAT);
                            } else {
                                navigation.getMenu().findItem(ID_MORE).setChecked(true);
                                if (mBottomSheetDialog == null)
                                    addButtomCustomFormWindow();
                                if (mBottomSheetDialog != null) {
                                    if (buttomBarAdapter != null) {
                                        mBottomSheetDialog.dismiss();
                                        buttomBarAdapter.updateSelectedMenu(ID_CHAT);
                                    }
                                }
                                viewPager.setCurrentItem(CHAT_VIEW, false);
                            }
                        } else {
                            startActivity(new Intent(this, MainActivity.class));
                            this.finish();
                        }
                    } catch (Exception exception) {
                        exception.getMessage();
                        startActivity(new Intent(this, MainActivity.class));
                        this.finish();
                    }

                } else if (getIntent().hasExtra("CLIENT_CHAT")) {
                    try {
                        String str = getIntent().getExtras().getString("CLIENT_CHAT");
                        dataJob = new Gson().fromJson(str, Job.class);
                        Job tempJobData = AppDataBase.getInMemoryDatabase(this).jobModel().getJobsById(dataJob.getJobId());
                        if ((dataJob != null && tempJobData != null) && dataJob.getJobId().equals(tempJobData.getJobId())) {
                            Log.e("", "");

//                            if (navigation.getMenu().findItem(ID_CLIENT_CHAT) != null) {
//                                navigation.setSelectedItemId(ID_CLIENT_CHAT);
//                            } else {
//                                viewPager.setCurrentItem(CLINET_CHAT_VIEW, false);
//                            }
                            if (navigation.getMenu().findItem(ID_CLIENT_CHAT) != null) {
                                navigation.setSelectedItemId(ID_CLIENT_CHAT);
                            } else {
                                navigation.getMenu().findItem(ID_MORE).setChecked(true);
                                if (mBottomSheetDialog == null)
                                    addButtomCustomFormWindow();
                                if (mBottomSheetDialog != null) {
                                    if (buttomBarAdapter != null) {
                                        mBottomSheetDialog.dismiss();
                                        buttomBarAdapter.updateSelectedMenu(ID_CLIENT_CHAT);
                                    }
                                }
                                viewPager.setCurrentItem(CLINET_CHAT_VIEW, false);
                            }

                        } else {
                            startActivity(new Intent(this, MainActivity.class));
                            this.finish();
                        }
                    } catch (Exception exception) {
                        exception.getMessage();
                        startActivity(new Intent(this, MainActivity.class));
                        this.finish();
                    }

                }

                /**when bundle data not found **/
                else if (getIntent().getAction() != null) {
                    if (getIntent().getType().equals("CLIENTCHAT")) {
                        try {
                            dataJob = AppDataBase.getInMemoryDatabase(this).jobModel().getJobsById(getIntent().getAction());
                            if (navigation.getMenu().findItem(ID_CLIENT_CHAT) != null) {
                                navigation.setSelectedItemId(ID_CLIENT_CHAT);
                            } else {
                                viewPager.setCurrentItem(CLINET_CHAT_VIEW, false);
                            }
                        } catch (Exception ex) {
                            ex.getMessage();
                            startActivity(new Intent(this, MainActivity.class));
                            this.finish();
                        }

                    } else if (getIntent().getType().equals("ADMINCHAT")) {
                        try {
                            dataJob = AppDataBase.getInMemoryDatabase(this).jobModel().getJobsById(getIntent().getAction());
                            if (navigation.getMenu().findItem(ID_CHAT) != null) {
                                navigation.setSelectedItemId(ID_CHAT);
                            } else {
                                viewPager.setCurrentItem(CHAT_VIEW, false);
                            }
                        } catch (Exception exception) {
                            exception.getMessage();
                        }

                    }

                } else {
                    navigation.setSelectedItemId(ID_JOB_DETAIL);
                    return;
                }
        }

        cstm_form_pc = new Cstm_Form_Pc(this);
        ArrayList<String> jTitleId = null;
        try {
            jTitleId = new ArrayList<>();
            for (JtId jobTitleId : dataJob.getJtId()) {
                jTitleId.add(jobTitleId.getJtId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dataJob != null && dataJob.getJobId() != null) {
            cstm_form_pc.callApiGetFormlist(dataJob.getJobId(), jTitleId);
        }

        try {
            if (dataJob == null) {
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        detail_activity_pi = new Job_Detail_Activity_pc(this);
        detail_activity_pi.getInvoiceItemList();


        ChatController.getInstance().setJobdetailListener(this);

        try {
            if (dataJob != null && dataJob.getJobId() != null) {
                /**
                 * *
                 *  create batch count for JOB **/
                showBadge(ChatController.getInstance().getbatchCount(dataJob.getJobId()));

                /**
                 * * client chat batch count ***/
                showBadgeForClientChat(ChatController.getInstance().getClientChatBatchCount(dataJob.getJobId()));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        addButtomCustomFormWindow();
    }


    /*****arrange footer menu option by Admin sequence******/
    private void initializeMenuItemList() {
        if (menu_items_ilist.size() > 0) {
            menu_items_ilist.clear();
        }

        for (FooterMenu serverList : App_preference.getSharedprefInstance().getLoginRes().getFooterMenu()) {
            if (serverList.isEnable.equals("1"))
                switch (serverList.getMenuField()) {
                    case "set_jobDetailMenuOdrNo":
                        menu_items_ilist.add(new MenuItemsModel(ID_JOB_DETAIL, LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_detail), R.drawable.ic_detail));
                        break;
                    case "set_itemMenuOdrNo":
                        if (App_preference.getSharedprefInstance().getLoginRes().getRights().get(0).getIsItemVisible() == 0) {
                            menu_items_ilist.add(new MenuItemsModel(ID_ITEM, LanguageController.getInstance().getMobileMsgByKey(AppConstant.item), R.drawable.ic_item));
                        }
                        break;
                    case "set_attachmentMenuOdrNo":
                        menu_items_ilist.add(new MenuItemsModel(ID_DOCUMENTS,
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_documents), R.drawable.ic_file_upload));
                        break;
                    case "set_commentsMenuOdrNo":
                        menu_items_ilist.add(new MenuItemsModel(ID_CHAT, LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_chat), R.drawable.chat_tab_icon));
                        break;
                    case "set_feedbackMenuOdrNo":
                        menu_items_ilist.add(new MenuItemsModel(ID_FEEDBACK, LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_feedback), R.drawable.ic_feedback));
                        break;
                    case "set_historyMenuOdrNo":
                        menu_items_ilist.add(new MenuItemsModel(ID_HISTORY, LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_history), R.drawable.ic_history));
                        break;
                    case "set_paymentMenuOdrNo":
                        if (App_preference.getSharedprefInstance().getLoginRes().getRights().get(0).getIsPaymentVisible() == 0) {
                            menu_items_ilist.add(new MenuItemsModel(ID_PAYMENT, LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_payment), R.drawable.ic_american_dollar_symbol));
                        }
                        break;
                    case "set_invoiceMenuOdrNo":
                        if (App_preference.getSharedprefInstance().getLoginRes().getRights().get(0).getIsInvoiceVisible() == 0) {
                            menu_items_ilist.add(new MenuItemsModel(ID_GENERATE_INVOICE,
                                    LanguageController.getInstance().getMobileMsgByKey
                                            (AppConstant.title_invoice), R.drawable.ic_invoice_icon));
                        }
                        break;
                    case "set_clientChatMenuOdrNo":
                        /*** add client chat tab in more tab**/
                        if (App_preference.getSharedprefInstance().getLoginRes().getIsClientEnable().equals("1")) {
                            Log.e("", "");
                            menu_items_ilist.add(new MenuItemsModel(ID_CLIENT_CHAT,
                                    LanguageController.getInstance().getMobileMsgByKey
                                            (AppConstant.client_fw_chat),
                                    R.drawable.ic_client_chat));
                        }
                        break;
                    case "set_customFormMenuOdrNo":
                        /*** add Custom form permission from Super Addmin**/
                        if (App_preference.getSharedprefInstance().getLoginRes().getCustomFormEnable().equals("1")) {
                            menu_items_ilist.add(new MenuItemsModel(ID_CUSTOM_FORM, LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_cutomform), R.drawable.ic_form_black_24dp));
                        }
                        break;
                    case "set_equipmentMenuOrdrNo":
                        /*** add Job equipment's permission from Super Addmin**/
                        try {
                            if (App_preference.getSharedprefInstance().getLoginRes().getIsEquipmentEnable() != null)
                                if (App_preference.getSharedprefInstance().getLoginRes().getIsEquipmentEnable().equals("1"))
                                    menu_items_ilist.add(new MenuItemsModel(ID_JOB_EQUIPMENT, LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_equipment), R.drawable.ic_equipement));

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;

                }
        }

    }


    private void setUpViewPager(ViewPager viewPager) {
        viewPager.setOffscreenPageLimit(0);
        mypagerAdapter = new MypagerAdapter(getSupportFragmentManager());
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(mypagerAdapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        viewPager.setCurrentItem(DETAIL_VIEW, false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.e("", "");
    }

    public void setList(ArrayList<CustomFormList_Res> customFormLists) {
        for (CustomFormList_Res item : customFormLists) {
            if (!item.getTotalQues().equals("0") && !item.getTab().equals("0")) {
                this.customFormLists.add(item);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        setTitle(mypagerAdapter.getPageTitle(position));
        int currentItem = viewPager.getCurrentItem();
        /**** this check for chat window notification issue****/
        if (position == 1) {
            ChatController.getInstance().setChatScreenState(3, dataJob.getJobId());
        } else {
            ChatController.getInstance().setChatScreenState(3, "");
        }

        if (position < navigation.getMenu().size() && mypagerAdapter.getPageTitle(position).
                equals(LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_chat))) {
            ChatController.getInstance().setChatScreenState(1, dataJob.getJobId());
            ChatController.getInstance().setUnreadCountZeroByJobId(dataJob.getLabel(), dataJob.getJobId(),
                    ChatController.getInstance().getClientChatBatchCount(dataJob.getJobId()));
        } else {
            ChatController.getInstance().setChatScreenState(1, "");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.e("", "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            AppUtility.hideSoftKeyboard(JobDetailActivity.this);
            return true;
        } else if (item.getItemId() == R.id.menu_send_job_card_email) {

            if (dataJob != null) {
                Intent intent = new Intent(this, Invoice_Email_Activity.class);
                intent.putExtra("jobId", dataJob.getJobId());
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        } else if (item.getItemId() == R.id.menu_print_job_card) {
            if (detail_activity_pi != null && dataJob != null)
                detail_activity_pi.printJobCard(dataJob.getJobId());
        } else if (item.getItemId() == R.id.menu_customer_sign) {
            if (detail_activity_pi != null && dataJob != null) {

                if (TextUtils.isEmpty(dataJob.getSignature())) {
                    openCustomSignatureDialog();
                } else {
                    AppUtility.alertDialog(JobDetailActivity.this,
                            "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.sign_uploaded), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", null);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        switch (requestCode) {
            case 300:
                if (documentsFragment != null) {
                    documentsFragment.updateDocList();
                }
                break;
            case MainActivity.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        break;

                }
                break;
            case CUSTOM_FORM_REQUESTCALL:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (detailFragment != null) {
                            detailFragment.onFormSuccess();
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        if (detailFragment != null) {
                            detailFragment.onFormError();
                        }
                        break;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(DETAIL_VIEW, false);
            navigation.getMenu().getItem(0).setChecked(true);
        } else {
            finishActivityWithSetResult();
        }
    }

    public RecyclerView.Adapter getAdpter() {
        return buttomBarAdapter;
    }

    //add dynamically buttom tabs for more option
    public void addButtomCustomFormWindow() {
        mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        mBottomSheetDialog.setContentView(sheetView);
        final RecyclerView recyclerView = mBottomSheetDialog.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        List<MenuItemsModel> more_item_list = new ArrayList<>();
        for (MenuItemsModel more_itme : menu_items_ilist) {
            if (!more_itme.isIsalreadySet()) {
                more_item_list.add(more_itme);
            }
        }

        buttomBarAdapter = new ButtomBarAdapter(more_item_list,
                new MyListItemSelected<MenuItemsModel>() {
                    @Override
                    public void onMyListitemSeleted(MenuItemsModel detailMenuItemsModel) {
                        setMenuPages(detailMenuItemsModel.getMenu_item_id());
                        mBottomSheetDialog.dismiss();
                    }
                }, dataJob.getJobId());
        recyclerView.setAdapter(buttomBarAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void setMenuPages(int menu_item_id) {
        switch (menu_item_id) {
            case ID_FEEDBACK:
                selectBottomMenu(ID_FEEDBACK);
                viewPager.setCurrentItem(FEEDBACK_VIEW, false);
                break;
            case ID_HISTORY:
                selectBottomMenu(ID_HISTORY);
                viewPager.setCurrentItem(HISTORY_VIEW, false);
                break;
            case ID_JOB_DETAIL:
                selectBottomMenu(ID_JOB_DETAIL);
                viewPager.setCurrentItem(DETAIL_VIEW, false);
            case ID_ITEM:
                Intent intent = new Intent(JobDetailActivity.this, JoBInvoiceItemList2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("JobId", dataJob.getJobId());
                startActivity(intent);
                break;
            case ID_CHAT:
                selectBottomMenu(ID_CHAT);
                viewPager.setCurrentItem(CHAT_VIEW, false);
                break;
            case ID_DOCUMENTS:
                DOCUMENTSELECT = false;
                selectBottomMenu(ID_DOCUMENTS);
                viewPager.setCurrentItem(DOCUMENT_VIEW, false);
                break;
            case ID_PAYMENT:
                Intent paymentIntent = new Intent(JobDetailActivity.this, Payment_Activity.class);
                paymentIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                paymentIntent.putExtra("JobId", dataJob.getJobId());
                startActivity(paymentIntent);
                break;
            case ID_CUSTOM_FORM:
                selectBottomMenu(ID_CUSTOM_FORM);
                viewPager.setCurrentItem(CUSTOM_FORM_VIEW, false);
                break;
            case ID_GENERATE_INVOICE:
                /**
                 * canInvoiceCreated =0   Do not create  invoice /1 create invoice
                 * check permission for the job is allow to generate invoice
                 * **/
                if (dataJob != null && dataJob.getCanInvoiceCreated() != null &&
                        dataJob.getCanInvoiceCreated().equals("0")) {
                    AppUtility.alertDialog(JobDetailActivity.this,
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_invoice),
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.invoice_can_not_generate_msg), null,
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), null);
                } else
                    detail_activity_pi.getItemFromServer(dataJob.getJobId());
                break;
            case ID_CLIENT_CHAT:
                selectBottomMenu(ID_CLIENT_CHAT);
                viewPager.setCurrentItem(CLINET_CHAT_VIEW, false);
                break;
            case ID_JOB_EQUIPMENT:
                Intent intent1 = new Intent(JobDetailActivity.this, JobEquipmentActivity.class);
                intent1.putExtra("JobId", dataJob.getJobId());
                intent1.putExtra("cltId", dataJob.getCltId());
                intent1.putExtra("contrId", dataJob.getContrId());
                intent1.putExtra("appId", appId);
                if (dataJob.getEquArray() != null && dataJob.getEquArray().size() > 0)
                    intent1.putExtra("auditid", dataJob.getEquArray().get(0).getAudId());
                else intent1.putExtra("auditid", "0");
                startActivityForResult(intent1, 300);
                break;
        }
    }

    @Override
    public void setInvoiceDetails() {

    }


    //public
    public void getAttchmentFragment() {
        Log.e("", "");
        navigation.setSelectedItemId(ID_DOCUMENTS);
        if (DOCUMENTSELECT) {
            resetBottomMenuSelection();
            viewPager.setCurrentItem(DOCUMENT_VIEW, false);
            if (documentsFragment != null) {
                documentsFragment.updateDocList();
            }
        } else {
            selectBottomMenu(ID_DOCUMENTS);
            viewPager.setCurrentItem(DOCUMENT_VIEW, false);
        }
    }

    public void openFormForEvent(String eventId) {
        HyperLog.i(TAG, "openFormForEvent(M) check custom form permission");
        if (App_preference.getSharedprefInstance().getLoginRes().getCustomFormEnable().equals("1")) {
            HyperLog.i(TAG, "openFormForEvent(M) Permission Enabled");
            try {
                if (AppUtility.isInternetConnected()) {
                    if (customFormLists != null && customFormLists.size() > 0) {
                        int i = 0;
                        for (CustomFormList_Res item : customFormLists) {
                            if (item.getEvent() != null
                                    && item.getEvent().equals(eventId)
                                    && item.getTotalQues() != null
                                    && !item.getTotalQues().equals("")
                                    && !item.getTotalQues().equals("0")) {

                                Intent intent = new Intent(JobDetailActivity.this, FormQueAns_Activity.class);
                                intent.putExtra("formId", item);
                                intent.putExtra("jobId", dataJob.getJobId());
                                startActivityForResult(intent, CUSTOM_FORM_REQUESTCALL + i);
                                i++;
                            }
                        }
                        if (i == 0) {
                            if (detailFragment != null) {
                                HyperLog.i(TAG, "openFormForEvent(M) : Custom form Answer Submit than status change");
                                detailFragment.onFormSuccess();
                            } else {
                                HyperLog.i(TAG, "openFormForEvent(M) : detailFragment NULL......");
                            }
                        } else {
                            HyperLog.i(TAG, "openFormForEvent(M) : Custom FORM nhi h ............");
                        }
                    } else {
                        if (detailFragment != null) {
                            detailFragment.onFormSuccess();
                            HyperLog.i(TAG, "openFormForEvent(M) :" + "Custom Form List Not Found");
                        } else {
                            HyperLog.i(TAG, "openFormForEvent(M) : detailFragment NULL......");
                        }
                    }
                } else {
                    HyperLog.i(TAG, "openFormForEvent(M) :" + "Status Update WithOut NEtwork");
                    if (detailFragment != null) {
                        detailFragment.onFormSuccess();
                    } else {
                        HyperLog.i(TAG, "openFormForEvent(M) : detailFragment NULL......");
                    }
                }
            } catch (Exception ex) {
                ex.getMessage();
                HyperLog.i(TAG, "openFormForEvent(M) :" + ex.toString());
                if (detailFragment != null) {
                    detailFragment.onFormSuccess();
                } else {
                    HyperLog.i(TAG, "openFormForEvent(M) : detailFragment NULL......");
                }
            }
        } else {
            HyperLog.i(TAG, "openFormForEvent(M) :" + "Custom form permission Disable");
            if (detailFragment != null) {
                detailFragment.onFormSuccess();
            } else {
                HyperLog.i(TAG, "openFormForEvent(M) : detailFragment NULL......");
            }
        }

    }

    @Override
    public void onNotifyListner(String key, final String msg) {
        if (key.equals("complition")) {
            if (!TextUtils.isEmpty(msg))
                dataJob.setComplNote(msg);
            if (detailFragment != null)
                detailFragment.setCompletionNotes(msg);
        } else {
            if (dataJob.getJobId().equals(key)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppUtility.alertDialog(JobDetailActivity.this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                finish();
                                return null;
                            }
                        });
                    }
                });
            }

        }
    }


    public void onSessionExpire(String msg) {
        showDialog(msg);
    }

    @Override
    public void moreInvoiceOption(List<InvoiceItemDataModel> data) {
        if (data.size() > 0) {
            Intent generateInvoiceIntent = new Intent(JobDetailActivity.this,
                    Generate_Invoice_Activity.class);
            generateInvoiceIntent.putExtra("JobId", dataJob.getJobId());
            startActivity(generateInvoiceIntent);
        } else {
            AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_Item_generate_inv), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return null;
                }
            });
        }
    }

    @Override
    public void onGetPdfPath(String pdfPath) {
        String path = App_preference.getSharedprefInstance().getBaseURL() + pdfPath;
        Intent openAnyType = new Intent(Intent.ACTION_VIEW);
        openAnyType.setData(Uri.parse(path));
        try {
            startActivity(openAnyType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSignatureUpload(String signaturePath, String msg) {
        if (dataJob != null) {
            dataJob.setSignature(signaturePath);
            if (detailFragment != null) {
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().updateJob(dataJob);
                detailFragment.notifyCustomberSign();
            }
        }
        AppUtility.alertDialog(JobDetailActivity.this, "", msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });

    }

    private void showDialog(String message) {
        AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), message, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }

    @Override
    public void finishActivityWithSetResult() {
        if (getIntent().hasExtra("JOBS")) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("key", "value");
            setResult(RESULT_OK, intent);
            finish();
        } else if (getIntent().hasExtra("CHAT_JOB")) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    protected void onShowKeyboard(int keyboardHeight) {
        if (navigation.getVisibility() == View.VISIBLE) {
            navigation.setVisibility(View.GONE);
        }
    }

    protected void onHideKeyboard() {
        if (navigation.getVisibility() == View.GONE) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigation.setVisibility(View.VISIBLE);
                }
            }, 10);
        }
    }

    protected void attachKeyboardListeners() {
        if (keyboardListenersAttached) {
            return;
        }
        rootLayout = findViewById(R.id.container);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);

        keyboardListenersAttached = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChatController.getInstance().setChatScreenState(0, dataJob.getJobId());
        ChatController.getInstance().setClientChatState(0, dataJob.getJobId());
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  GPS turned off, Show the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(this, GpsUtils.REQUEST_CHECK_SETTINGS_LOCATION);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show dialog
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_job_details, menu);
//        menu.getItem(0).setVisible
//                (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsJobCardEnableMobile().equals("0"));
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_send_job_card_email).setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.email_job_card));
        menu.findItem(R.id.menu_print_job_card).setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.print_job_card));
        menu.findItem(R.id.menu_customer_sign).setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.menu_upload_sign));
        menu.findItem(R.id.menu_send_job_card_email).setVisible
                (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsJobCardEnableMobile()
                        != null && App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).
                        getIsJobCardEnableMobile().equals("0"));
        menu.findItem(R.id.menu_print_job_card).setVisible
                (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsJobCardEnableMobile()
                        != null && App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).
                        getIsJobCardEnableMobile().equals("0"));


        try {
            CompPermission compPermission = App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0);
            if (compPermission != null)
                menu.findItem(R.id.menu_customer_sign).setVisible(!compPermission.getIsJcSignEnable().equals("1"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onPrepareOptionsMenu(menu);
    }

    private void openCustomSignatureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.item_customer_signature, null);


        final String getPath;
        File mypath;


        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir(getResources().getString(R.string.external_dir), Context.MODE_PRIVATE);
        final String current = "eot_" + AppUtility.getDateByMiliseconds() + ".png";
        mypath = new File(directory, current);
        getPath = mypath.getAbsolutePath();


        AppCompatTextView tv_heading = view.findViewById(R.id.tv_heading);
        tv_heading.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.customer_sign_head));
        LinearLayout mContent = view.findViewById(R.id.signt);
        TextView clear_txt = view.findViewById(R.id.clear_txt);
        clear_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.clear));

        Button btn_close = view.findViewById(R.id.btn_close);
        Button btn_upload = view.findViewById(R.id.btn_upload);

        btn_upload.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_upload));
        btn_close.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.close));


        final SignatureView mSignature = new SignatureView(this);
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        clear_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignature.clear();
            }
        });


        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSignature.isSignatureEmpty()) {
                    AppUtility.alertDialog(JobDetailActivity.this,
                            "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.customer_sign_required),
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                            "", null);
                    return;
                }

                File mfile = mSignature.exportFile(getPath, current);
                if (mSignature.isSignatureEmpty()) {
                    mfile = null;
                }
                detail_activity_pi.uploadCustomerSign(
                        dataJob.getJobId(),
                        mfile);

                alertDialog.dismiss();
            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    class MypagerAdapter extends FragmentStatePagerAdapter {
        private final int ITEM_COUNT = 7;

        public MypagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (detailFragment == null) {
                        detailFragment = DetailFragment.newInstance("No", dataJob.getJobId());
                    }
                    return detailFragment;
                case 1:
                    if (documentsFragment == null) {
                        documentsFragment = DocumentsFragment.newInstance("No", dataJob.getJobId());
                    }
                    return documentsFragment;
                case 2:
                    return ChatFragment.newInstance("No", dataJob.getJobId());
                case 3:
                    return FeedbackFragment.newInstance("No", dataJob.getJobId());
                case 4:
                    return HistoryFragment.newInstance("No", dataJob.getJobId());
                case 5:
                    return ClientChatFragment.newInstance("NO", dataJob.getJobId());
                case 6:
                    return CustomFormListFragment.newInstance(customFormLists, dataJob.getJobId());
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_details);
                case 1:
                    return LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_documents);
                case 2:
                    return LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_chat);
                case 3:
                    return LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_feedback);
                case 4:
                    return LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_history);
                case 5:
                    return LanguageController.getInstance().getMobileMsgByKey(AppConstant.client_fw_chat);
                case 6:
                    return LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_cutomform);
                case 7:
                    return LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_invoice);
            }
            return "";
        }

        @Override
        public int getCount() {
            return ITEM_COUNT;
        }
    }
}
