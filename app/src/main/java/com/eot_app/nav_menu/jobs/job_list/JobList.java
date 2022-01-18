package com.eot_app.nav_menu.jobs.job_list;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.home_screens.MainActivity;
import com.eot_app.nav_menu.jobs.job_controller.ChatController;
import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.JobDetailActivity;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.job_equ_remrk.JobEquRemarkRemarkActivity;
import com.eot_app.nav_menu.jobs.job_filter.filter_model.JobPrioty;
import com.eot_app.nav_menu.jobs.job_list.customselecter.CustomSelecter;
import com.eot_app.nav_menu.jobs.job_list.job_presenter.JobList_pc;
import com.eot_app.nav_menu.jobs.job_list.job_presenter.JobList_pi;
import com.eot_app.nav_menu.jobs.job_list.job_presenter.Joblist_view;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.DropdownListBean;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.setting_db.TagData;
import com.eot_app.utility.util_interfaces.MyListItemSelected;
import com.eot_app.utility.util_interfaces.OnFragmentInteractionListener;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JobList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobList extends Fragment implements MyListItemSelected<Job>, Joblist_view, View.OnClickListener {
    public static final int UPDATE = 2;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView joblist;
    LinearLayoutManager layoutManager;
    JobListAdapter adapter;
    SwipeRefreshLayout swiperefresh;
    // CardView cv_filter;
    ImageView drop_down_arrow;
    LinearLayout lin;
    // RelativeLayout filter_layout;
    LinearLayout filter_layout;
    List<Job> jobList_Chat = new ArrayList<>();
    //  presenter setup
    JobList_pi jobListPi;
    private final BroadcastReceiver myJobListfrefreshForNotif = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /****Job list update after notification craete****/
            Log.d("JobList", "refreshed called");
            if (jobListPi != null)
                jobListPi.loadFromServer();
        }
    };
    CustomSelecter newtask, accepted, pending, completed;
    boolean filter_dropdown = false;
    ArrayList<DropdownListBean> listBeans = new ArrayList<>();
    ChipGroup chipGroup;
    LinearLayout ll_in_progress;
    LinearLayout ll_today_task;
    int today_pos = -1;
    int inprogress_pos;
    int nearToday_pos = -1;
    boolean searchStopNear;
    LinearLayout lin_layout;
    private boolean isListSortByStartDate;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private LinearLayout nojobs_linear;
    private Button filter_list_btn;
    private boolean isAutoScrolled;
    private FloatingActionButton fab_refresh;
    private CoordinatorLayout cl;
    private TextView tv_in_progress, tv_today_task;
    private TextView newTask, tv_accepted, tv_on_hold, tv_completed, tv_no_job_found;
    private ImageView img_new, img_accepted, img_hold, img_completed;
    private ImageView img_close_new, img_close_accpeted, img_close_hold, img_close_completed;
    //arrow
    private ImageView img_arrow_in_progress;
    private ImageView img_arrow_today;
    //    private Switch sort_joblist;
    private TextView txt_sort;
    private LinearLayout quotes_search_view;
    private EditText edtSearch;
    private JobFilterModel jobFilterModel;
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //Set Changed Text for search filter
            jobListPi.setSearchOnTextChange(s.toString());
            if (s.toString().isEmpty()) {
                jobFilterModel = new JobFilterModel();
                jobListPi.getFilterJobList(jobFilterModel, 1);
            }
        }
    };
    private Activity myActivity;
    private Set<String> statusList;
    private boolean isFromScan;
    /**
     * filter list to show
     * Today Job and than unscehedule jobs and so on in DESC order
     */
    private int unScheduleHeaderPos = -1;

    public JobList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JobList.
     */
    // TODO: Rename and change types and number of parameters
    public static JobList newInstance(String param1, String param2) {
        JobList fragment = new JobList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static JobList newInstance(boolean isFromScan, List<Job> jobList, String barcode) {
        JobList fragment = new JobList();
        Bundle args = new Bundle();
        args.putBoolean("isscan", isFromScan);
        args.putString("barcode", barcode);
        String str = new Gson().toJson(jobList);
        args.putString("joblist", str);
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshJobList() {
        if (jobListPi != null) {
            jobListPi.getJobList();
        }
    }

    public void showSiteName(boolean isShow) {
        if (adapter != null) {
            adapter.setFromShowSiteName(isShow);
            adapter.notifyDataSetChanged();
        }
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
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
    public void onResume() {
        super.onResume();
        ChatController.getInstance().setJoblistListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job_list, container, false);
//      View Initialization
        if (getArguments() != null) {
            isFromScan = getArguments().getBoolean("isscan", false);
        }
        myActivity = getActivity();

        initializelables(view);
        initializeViews(view);
        if (getArguments() != null) {
            List<Job> jobList = new ArrayList<>();
            try {
                jobList = new ArrayList<>();
                Type listType = new TypeToken<List<Job>>() {
                }.getType();
                String str = getArguments().getString("joblist");
                jobList = new Gson().fromJson(str, listType);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //  getUpdatedJob();

            if (jobList != null && jobList.size() > 0)
                setdata(jobList, 0);
            if (isFromScan) {
                swiperefresh.setEnabled(false);
                fab_refresh.hide();
                lin_layout.setVisibility(View.GONE);
            }

        }

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(myJobListfrefreshForNotif,
                new IntentFilter("job_refresh"));

        return view;
    }

    @Override
    public void onDestroyView() {
        try {
            if (myJobListfrefreshForNotif != null)
                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(myJobListfrefreshForNotif);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroyView();

    }

    private void initializelables(View view) {
        cl = view.findViewById(R.id.cl);
        ll_in_progress = view.findViewById(R.id.ll_in_progress);
        ll_today_task = view.findViewById(R.id.ll_today_task);
        lin_layout = view.findViewById(R.id.lin_layout);

        ll_in_progress.setOnClickListener(this);
        ll_today_task.setOnClickListener(this);

        fab_refresh = view.findViewById(R.id.fab_refresh);

        newtask = view.findViewById(R.id.newtask);
        accepted = view.findViewById(R.id.accepted);
        pending = view.findViewById(R.id.pending);
        completed = view.findViewById(R.id.completed);

        tv_in_progress = view.findViewById(R.id.tv_in_progress);
        tv_today_task = view.findViewById(R.id.tv_today_task);


        tv_today_task.setText(LanguageController.getInstance().getMobileMsgByKey(
                AppConstant.today_task
        ));
        tv_in_progress.setText(LanguageController.getInstance().getMobileMsgByKey(
                AppConstant.In_progress
        ));

        img_new = view.findViewById(R.id.img_new);
        img_accepted = view.findViewById(R.id.img_accepted);
        img_hold = view.findViewById(R.id.img_hold);
        img_completed = view.findViewById(R.id.img_completed);

        img_arrow_in_progress = view.findViewById(R.id.img_arrow_in_progress);
        img_arrow_today = view.findViewById(R.id.img_arrow_today);


        img_close_new = view.findViewById(R.id.img_close_new);
        img_close_accpeted = view.findViewById(R.id.img_close_accpeted);
        img_close_hold = view.findViewById(R.id.img_close_hold);
        img_close_completed = view.findViewById(R.id.img_close_completed);


        lin = view.findViewById(R.id.lin);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        nojobs_linear = view.findViewById(R.id.nojobs_linear);
        txt_sort = view.findViewById(R.id.txt_sort);

        filter_layout = view.findViewById(R.id.filter_tags_linear);//filterlayoutview
        chipGroup = view.findViewById(R.id.chipGroup);
        filter_list_btn = view.findViewById(R.id.filter_list_btn);
        filter_list_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reset));

        //  cv_filter = view.findViewById(R.id.cv_filter);

        drop_down_arrow = view.findViewById(R.id.drop_down_arrow);
        newTask = view.findViewById(R.id.newTask);
        newTask.setText(AppConstant.status_new_key);
        newTask.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.status_new));

        joblist = view.findViewById(R.id.joblist);

        tv_accepted = view.findViewById(R.id.tv_accepted);
        tv_accepted.setText(AppConstant.status_acc);
        tv_accepted.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.accepted));

        tv_on_hold = view.findViewById(R.id.tv_on_hold);
        tv_on_hold.setText(AppConstant.status_onhold);
        //  tv_on_hold.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.on_hold));
        tv_on_hold.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.new_on_hold));

        tv_completed = view.findViewById(R.id.tv_completed);
        tv_completed.setText(AppConstant.status_com);
        tv_completed.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.completed));

        tv_no_job_found = view.findViewById(R.id.tv_no_job_found);
        tv_no_job_found.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_no_jobs_found));

        quotes_search_view = view.findViewById(R.id.quotes_search_view);

        edtSearch = view.findViewById(R.id.edtSearch);
        edtSearch.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.search_job_code_client_name_address));

        fab_refresh.setOnClickListener(this);
        isAutoScrolled = false;
    }

    private void initializeViews(View view) {
        jobFilterModel = new JobFilterModel();
        view.findViewById(R.id.search_btn).setOnClickListener(this);
        newtask.setOnClickListener(this);

        accepted.setOnClickListener(this);
        pending.setOnClickListener(this);

        completed.setOnClickListener(this);
        //change color of text color of sorting text

        txt_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isListSortByStartDate) { // schedule start
                    getSpannableFormat(LanguageController.getInstance().getMobileMsgByKey(AppConstant.sort_by_date), LanguageController.getInstance().getMobileMsgByKey(AppConstant.date));
                } else {    // // schedule recent
                    getSpannableFormat(LanguageController.getInstance().getMobileMsgByKey(AppConstant.sort_by_recent), LanguageController.getInstance().getMobileMsgByKey(AppConstant.recent));
                }
                jobListPi.setSoringByDate(isListSortByStartDate);
                isListSortByStartDate = !isListSortByStartDate;
            }
        });
        getSpannableFormat(LanguageController.getInstance().getMobileMsgByKey(AppConstant.sort_by_date), LanguageController.getInstance().getMobileMsgByKey(AppConstant.date));


        filter_list_btn.setOnClickListener(this);
        jobListPi = new JobList_pc(this, isFromScan);
        List<Job> jobsdata = new ArrayList<>();
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isAutoScrolled = false;
                swiperefresh.setRefreshing(true);
                jobListPi.loadFromServer();
            }
        });

        layoutManager = new LinearLayoutManager(getActivity());
        joblist.setLayoutManager(layoutManager);


        adapter = new JobListAdapter(getActivity(), this, jobsdata);
        adapter.setFromShowSiteName(App_preference.getSharedprefInstance().getSiteNameShowInSetting());
        adapter.setOnListScrolling(new JobListAdapter.OnListScrolling() {
            @Override
            public void currentVisiblePos(int pos) {
                hideShowBottomOptions(pos);
            }
        });
        adapter.updateRecords(Collections.<Job>emptyList());
        joblist.setAdapter(adapter);
//        load data from data base

        jobListPi.getFilterJobList(jobFilterModel, 0);

        LinearLayout filter_dropdown = view.findViewById(R.id.filter_dropdown);
        filter_dropdown.setOnClickListener(this);


    }

    /**
     * <P>reset all views to normal state</P>
     */
    private void resetStatusViews() {
        newTask.setTextColor(getResources().getColor(R.color.txt_color));
        img_close_new.setVisibility(View.GONE);
        img_new.setColorFilter(null);

        tv_accepted.setTextColor(getResources().getColor(R.color.txt_color));
        img_close_accpeted.setVisibility(View.GONE);
        img_accepted.setColorFilter(null);

        tv_on_hold.setTextColor(getResources().getColor(R.color.txt_color));
        img_close_hold.setVisibility(View.GONE);
        img_hold.setColorFilter(null);

        tv_completed.setTextColor(getResources().getColor(R.color.txt_color));
        img_close_completed.setVisibility(View.GONE);
        img_completed.setColorFilter(null);
    }

    /**
     * <p>Updating color and background color of the selected status</p>
     *
     * @Param selecter the customview
     * @Param tv the status label
     * @Param statusICon status iconstatus
     * @Param close close icon
     */

    private void updateStatuViews(CustomSelecter selecter, TextView tv, ImageView statusIcon, ImageView close) {
        try {
            selecter.setSeleted(!selecter.isSeleted());
            TransitionManager.beginDelayedTransition(cl);
            if (selecter.isSeleted()) {
                tv.setTextColor(Color.WHITE);
                statusIcon.setColorFilter(Color.WHITE);
                close.setVisibility(View.VISIBLE);
            } else {
                statusIcon.setColorFilter(null);
                tv.setTextColor(getResources().getColor(R.color.txt_color));
                close.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * <P>on scroll hide the today and in progress views</P>
     */
    private void hideShowBottomOptions(int pos) {
        int first = layoutManager.findFirstVisibleItemPosition();
        int last = layoutManager.findLastVisibleItemPosition();
        if (first == -1) return;

        //hide show for in progress task
        if (inprogress_pos > -1) {
            if (pos == inprogress_pos)
                ll_in_progress.setVisibility(View.GONE);
            else if (inprogress_pos >= first && inprogress_pos <= last) {
                ll_in_progress.setVisibility(View.GONE);
            } else {
                if (inprogress_pos < pos)
                    img_arrow_in_progress.setRotation(180);
                else img_arrow_in_progress.setRotation(0);
                ll_in_progress.setVisibility(View.VISIBLE);
            }

            if (first == 0 && last == 0) if (inprogress_pos > -1 && inprogress_pos < 4) {
                ll_in_progress.setVisibility(View.GONE);
            }
        } else ll_in_progress.setVisibility(View.GONE);


        //hide show for today task
        if (today_pos > -1) {
            if (pos == today_pos)
                ll_today_task.setVisibility(View.GONE);
            else if (today_pos >= first && today_pos <= last) {
                ll_today_task.setVisibility(View.GONE);
            } else {
                if (today_pos < pos)
                    img_arrow_today.setRotation(180);
                else img_arrow_today.setRotation(0);
                ll_today_task.setVisibility(View.VISIBLE);
            }

            if (first == 0 && last == 0) if (today_pos > -1 && today_pos < 4) {
                ll_today_task.setVisibility(View.GONE);
            }
        } else ll_today_task.setVisibility(View.GONE);


    }

    private void hideFilterView() {
        quotes_search_view.setVisibility(View.GONE);
    }

    private void getSpannableFormat(String full_str, String value_date) {
        String s = full_str;
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#00848d"));
        SpannableString ss = new SpannableString(s);
        String[] array = s.split(" ");

        int currIndex = 0;
        for (String word : array) {
            if (word.equals(value_date)) {
                ss.setSpan(span, currIndex, currIndex + word.length(), 0);
                ss.setSpan(new UnderlineSpan(), currIndex, currIndex + word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            currIndex += (word.length() + 1);
        }
        txt_sort.setText(ss);
    }

    private void removeFilters() {
        newtask.setSeleted(false);
        accepted.setSeleted(false);
        pending.setSeleted(false);
        completed.setSeleted(false);
        resetStatusViews();
        jobListPi.clearfiletrlist();
        if (statusList != null) {
            statusList.clear();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setdata(List<Job> data, int visibilityFlag) {
        if (data != null)
            backGroundJobListListner(visibilityFlag, data);
    }

    /**
     * Visibility Flag Detail
     * 1 : Search view (quotes_search_view) Visible , Main Filter (lin) and Filter Chip layout (filter_layout) not visible
     * 2 : Main Filter (lin) Visible , Search view (quotes_search_view) and Filter Chip layout (filter_layout) not visible
     * 3 : Filter Chip layout (filter_layout) visible , Main Filter (lin) Visible and  Search view (quotes_search_view) not visible
     * 0 : Filter as it is , no changes
     */
    private void setUpViewVisibility(int visibilityFlag) {
        if (visibilityFlag == 1) {
            quotes_search_view.setVisibility(View.VISIBLE);
            lin.setVisibility(View.GONE);
            filter_layout.setVisibility(View.GONE);
        } else if (visibilityFlag == 2) {
            edtSearch.removeTextChangedListener(textWatcher);
            edtSearch.setText("");
            quotes_search_view.setVisibility(View.GONE);
            lin.setVisibility(View.VISIBLE);
            filter_layout.setVisibility(View.GONE);
        } else if (visibilityFlag == 3) {
            edtSearch.removeTextChangedListener(textWatcher);
            edtSearch.setText("");
            quotes_search_view.setVisibility(View.GONE);
            lin.setVisibility(View.GONE);
            filter_layout.setVisibility(View.VISIBLE);
        }
    }

    public void getUpdatedJob() {
        if (jobListPi != null)
            jobListPi.loadFromServer();
    }

    @Override
    public void updateFromApiObserver() {
        if (jobListPi != null) {
            jobListPi.getFilterJobList(jobFilterModel, 0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn://serach by name or job id with status & tag & prioty
                AppUtility.hideSoftKeyboard(getActivity());
                jobFilterModel.setSearch(edtSearch.getText().toString());
                // jobListPi.getJobByName(jobFilterModel);
                jobListPi.getFilterJobList(jobFilterModel, 1);
                break;
            case R.id.newtask:

                updateStatuViews(newtask, newTask, img_new, img_close_new);
                //jobListPi.filterDataFromStatus("1", newtask.isSeleted());
                prepareStatusListByCode("1", newtask.isSeleted());

                break;
            case R.id.accepted:

                updateStatuViews(accepted, tv_accepted, img_accepted, img_close_accpeted);
                // jobListPi.filterDataFromStatus("2", accepted.isSeleted());
                prepareStatusListByCode("2", accepted.isSeleted());

                break;
            case R.id.pending:

                updateStatuViews(pending, tv_on_hold, img_hold, img_close_hold);
//                jobListPi.filterDataFromStatus("8", pending.isSeleted());
                //  prepareStatusListByCode("8", pending.isSeleted());
                prepareStatusListByCode("12", pending.isSeleted());

                break;
            case R.id.completed:
                updateStatuViews(completed, tv_completed, img_completed, img_close_completed);
//                jobListPi.filterDataFromStatus("9", completed.isSeleted());
                prepareStatusListByCode("9", completed.isSeleted());

                break;
            case R.id.filter_dropdown:
                if (filter_dropdown) {
                    //    jobListPi.expand(cv_filter, 500, ((int) getResources().getDimension(R.dimen.tab_height)));
                    jobListPi.rorateClockwise(drop_down_arrow);
                } else {
                    //  jobListPi.collapse(cv_filter, 500, 0);
                    jobListPi.rorateAntiClockwise(drop_down_arrow);
                }
                filter_dropdown = !filter_dropdown;
                break;

            case R.id.filter_list_btn:
                listBeans = new ArrayList<>();
                hideFilterView();

                jobFilterModel = new JobFilterModel();
                jobListPi.getFilterJobList(jobFilterModel, 2);
                break;

            case R.id.fab_refresh:
                isAutoScrolled = false;
                swiperefresh.setRefreshing(true);
                jobListPi.loadFromServer();
                break;

            case R.id.ll_today_task:
                if (adapter != null && today_pos > -1 && adapter.getItemCount() > today_pos) {
                    joblist.scrollToPosition(today_pos);
                    ll_today_task.setVisibility(View.GONE);
                }

                break;

            case R.id.ll_in_progress:
                if (adapter != null && inprogress_pos > -1 && adapter.getItemCount() > inprogress_pos) {
                    joblist.scrollToPosition(inprogress_pos);
                    ll_in_progress.setVisibility(View.GONE);

                }
                break;
        }
    }

    private void prepareStatusListByCode(String status, boolean check) {
        if (statusList == null) {
            statusList = new HashSet<>();
        }
        if (status.equals(AppConstant.Accepted)) {
            if (check) {
                statusList.add(AppConstant.Travelling);
                statusList.add(AppConstant.Break);
            } else {
                statusList.remove(AppConstant.Travelling);
                statusList.remove(AppConstant.Break);
            }
        }

        if (check) {
            statusList.add(status);
        } else statusList.remove(status);

        jobFilterModel = new JobFilterModel();
        jobFilterModel.setStatusModelsList(statusList);
        jobListPi.getFilterJobList(jobFilterModel, 2);
        Log.d("status list size", "" + statusList.size());


    }

    @Override
    public void onMyListitemSeleted(Job job) {
        if (isFromScan) {
            String barcode = getArguments().getString("barcode");
            if (barcode != null) {
                jobListPi.searchEquipment(job.getJobId(), barcode);
            }
        } else {
            /*** Change BY Me For Imvoice ***/
            Intent intentJobDeatis = new Intent(getActivity(), JobDetailActivity.class);
            intentJobDeatis.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            //intentJobDeatis.putExtra("JOBS", job);
            String strjob = new Gson().toJson(job);
            intentJobDeatis.putExtra("JOBS", strjob);
            getActivity().startActivityForResult(intentJobDeatis, UPDATE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && requestCode == UPDATE) {
            if (data.hasExtra("JobID")) {
                data.getStringExtra("JobID");

                jobListPi.getFilterJobList(jobFilterModel, 0);
                newtask.setSeleted(false);
                accepted.setSeleted(false);
                pending.setSeleted(false);
                completed.setSeleted(false);
                resetStatusViews();
                jobListPi.clearfiletrlist();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        for remove all listener
        ChatController.getInstance().setJoblistListener(null);
    }

    @Override
    public void onSessionExpired(String msg) {
        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title),
                msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        EotApp.getAppinstance().sessionExpired();
                        return null;
                    }
                });
    }

    @Override
    public void filterListByChip(ArrayList<DropdownListBean> listBean, int visibilityFlag) {//job list filter by Advance filter
        edtSearch.removeTextChangedListener(textWatcher);
        this.listBeans = listBean;
        Set<String> statusModelsList = new HashSet<>();
        ArrayList<TagData> tagDataList = new ArrayList<>();
        Set<String> jobPriotiesList = new HashSet<>();

        for (DropdownListBean objList : listBeans) {
            if (objList.getClass() == TagData.class) {
                if (!tagDataList.contains(objList))
                    tagDataList.add((TagData) objList);
            } else if (objList.getClass() == JobStatusModel.class) {
                statusModelsList.add(objList.getKey());
            } else if (objList.getClass() == JobPrioty.class) {
                jobPriotiesList.add(objList.getKey());
            }
        }

        jobFilterModel.setJobPriotiesList(jobPriotiesList);
        jobFilterModel.setStatusModelsList(statusModelsList);
        jobFilterModel.setTagDataList(tagDataList);
        jobFilterModel.setSearch("");
        jobListPi.getFilterJobList(jobFilterModel, visibilityFlag);
        removeFilters();
        //  jobListPi.getFilterListByStatus_Prity(tagDataList, statusModelsList, jobPriotiesList);
    }

    @Override
    public void chipAdd(ArrayList<DropdownListBean> listBeans) {
        chipGroup.removeAllViews();
        for (DropdownListBean obj : listBeans)
            addFilterName(obj);
    }

    @Override
    public void refreshchangesFromLocalDB() {
        if (jobListPi != null) {
            jobListPi.getFilterJobList(jobFilterModel, 0);
        }
    }

    private void addFilterName(final DropdownListBean chipItem) {
        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.chip_layout, null);
        TextView textView = v.findViewById(R.id.chip_txt);
        textView.setText(chipItem.getName());
        ImageView deleteMember = v.findViewById(R.id.deleteChip);
        deleteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipGroup.removeView((LinearLayout) view.getParent());
                listBeans.remove(chipItem);

                if (listBeans.size() > 0)
                    filterListByChip(listBeans, 3);
                else
                    filterListByChip(listBeans, 2);
            }
        });
        chipGroup.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void setSearchVisibility(boolean b) {
        quotes_search_view.setVisibility(b ? View.VISIBLE : View.GONE);
        lin.setVisibility(b ? View.GONE : View.VISIBLE);
        jobFilterModel = new JobFilterModel();
        if (b) {
            edtSearch.addTextChangedListener(textWatcher);
            removeFilters();
            jobListPi.getFilterJobList(jobFilterModel, 1);
        } else {
            jobListPi.getFilterJobList(jobFilterModel, 2);
            edtSearch.removeTextChangedListener(textWatcher);
            edtSearch.setText("");
        }
    }

    /**
     * <P>nearby index find last index need to find header position in multiple jobs in same
     * future date</P>
     */
    private int findNearFutureHeaderJOb(List<Job> jobs, int nearToday_pos) {
        int newPosition = nearToday_pos;
        try {
            Job job1 = jobs.get(nearToday_pos);
            Date date1 = new Date(Long.parseLong(job1.getSchdlStart()) * 1000);

            Job job = jobs.get(nearToday_pos - 1);
            long l = Long.parseLong(job.getSchdlStart()) * 1000;
            Date date2 = new Date(l);

            if (AppUtility.isSameDay(date1, date2)) {
                newPosition = newPosition - 1;
                findNearFutureHeaderJOb(jobs, newPosition);
            } else return newPosition;

        } catch (Exception ex) {
            return newPosition;
        }

        return newPosition;
    }

    /**
     * <P>auto scroll the list on today task position</P>
     */
    private void autoScrollToTodayTask(int today_pos) {
        if (!isAutoScrolled && adapter != null && today_pos > -1 && adapter.getItemCount() > today_pos) {
            isAutoScrolled = true;
            joblist.smoothScrollToPosition(today_pos);
            ll_today_task.setVisibility(View.GONE);
        }
    }

    @Override
    public void setRefereshPullOff() {
        if (swiperefresh != null)
            swiperefresh.setRefreshing(false);
    }

    @Override
    public void onEquipmentFound(EquArrayModel equipment) {
        if (equipment != null) {
            String strEqu = new Gson().toJson(equipment);
            startActivity(new Intent(getActivity(), JobEquRemarkRemarkActivity.class).putExtra("equipment", strEqu));
        }
    }

    private List<Job> filterList(List<Job> jobdata) {
        int today_pos = -1;
        int unschedule = -1;

        for (int i = 0; i < jobdata.size(); i++) {
            Job item = jobdata.get(i);
            if (!TextUtils.isEmpty(item.getSchdlStart())) {
                Long datelong = Long.parseLong(item.getSchdlStart()) * 1000;
                if (AppUtility.isToday(new Date(datelong))) {
                    {
                        today_pos = i;
                    }
                }
            } else if (unschedule == -1) {
                unschedule = i;
                break;
            }
        }
        if (unschedule > 0) {
            List<Job> jobs = jobdata.subList(unschedule, jobdata.size());
            List<Job> jobs1 = jobdata.subList(0, unschedule);
            if (today_pos > -1) {
                unScheduleHeaderPos = today_pos + 1;
                jobs1.addAll(today_pos + 1, jobs);
            } else {
                unScheduleHeaderPos = 0;
                jobs1.addAll(0, jobs);
            }
            return jobs1;
        } else return jobdata;
    }

    private void backGroundJobListListner(final int visibilityFlag, final List<Job>... lists) {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                final List<Job> dataList = new ArrayList<>();
                myActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        inprogress_pos = -1;
                        today_pos = -1;
                        nearToday_pos = -1;
                        searchStopNear = false;
                    }
                });


                if (lists != null) {
                    unScheduleHeaderPos = -1;
                    final List<Job> data = filterList(lists[0]);
                    for (int i = 0; i < data.size(); i++) {
                        Job item = data.get(i);
                        try {
                            if (item != null && item.getStatus() != null && !AppUtility.getJobStatusList().contains(item.getStatus())) {
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().deleteJobById(item.getJobId());
                            } else {
                                if (!item.getTempId().equals(item.getJobId())) {

                                    if (item.getIsdelete() != null && item.getIsdelete().equals("0")
                                            || item.getStatus().equals(AppConstant.Cancel)
                                            || item.getStatus().equals(AppConstant.Closed)
                                            || item.getStatus().equals(AppConstant.Reject)) {
                                        ChatController.getInstance().removeListnerByJobID(item.getJobId());
                                    } else {

                                        if (item.getStatus().equals(AppConstant.In_Progress)) {
                                            inprogress_pos = i;
                                        }

                                        try {
                                            if (!TextUtils.isEmpty(item.getSchdlStart()) && today_pos == -1) {
                                                Long datelong = Long.parseLong(item.getSchdlStart()) * 1000;
                                                if (AppUtility.isToday(new Date(datelong))) {
                                                    {
                                                        today_pos = i;
                                                        nearToday_pos = -1;
                                                    }
                                                } else if (today_pos == -1) {


                                                    Date date1 = new Date(System.currentTimeMillis());
                                                    Date date2 = new Date(datelong);
                                                    if (AppUtility.isAfterDay(date2, date1))
                                                        nearToday_pos = i;

                                                }
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }

                                        /***
                                         *
                                         * CREATE Job Chat watch count & Listner's**/
                                        ChatController.getInstance().registerChatListner(item);
                                    }
                                }
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }

                    if (nearToday_pos > 0) {
                        nearToday_pos = findNearFutureHeaderJOb(data, nearToday_pos);
                    }
                    /****added by ***/
                    dataList.addAll(data);
                }


                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ChatController.getInstance().refreshAdapter();
                        jobList_Chat = dataList;
                        setUpViewVisibility(visibilityFlag);
                        swiperefresh.setRefreshing(false);
                        if (today_pos == -1) ll_today_task.setVisibility(View.GONE);
                        if (inprogress_pos == -1) ll_in_progress.setVisibility(View.GONE);


                        adapter.setUnScheduleHeaderPos(unScheduleHeaderPos);
                        adapter.updateRecords(dataList);
                        if (adapter.getItemCount() <= 0) {
                            nojobs_linear.setVisibility(View.VISIBLE);

                        } else {
                            hideShowBottomOptions(0);
                            if (nearToday_pos > -1)
                                autoScrollToTodayTask(nearToday_pos);
                            else
                                autoScrollToTodayTask(today_pos);
                            nojobs_linear.setVisibility(View.GONE);

                            try {
                                if (myActivity instanceof MainActivity) {
                                    String notificationDataId = ((MainActivity) myActivity).getNotificationDataId();
                                    ((MainActivity) myActivity).setNotificationDataId(null);
                                    if (!TextUtils.isEmpty(notificationDataId)) {
                                        Job jobsById = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                                                .jobModel().getJobsById(notificationDataId);
                                        if (jobsById != null) {
                                            Intent intentJobDeatis = new Intent(myActivity, JobDetailActivity.class);
                                            intentJobDeatis.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                            String strjob = new Gson().toJson(jobsById);
                                            intentJobDeatis.putExtra("JOBS", strjob);
                                            myActivity.startActivityForResult(intentJobDeatis, UPDATE);
                                        }
                                    }
                                }
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
                });

            }
        });

    }


}
