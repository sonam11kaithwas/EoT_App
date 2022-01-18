package com.eot_app.nav_menu.userleave_list_pkg;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.databinding.FragmentUserLeaveListBinding;
import com.eot_app.nav_menu.jobs.job_list.customselecter.CustomSelecter;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserLeaveListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserLeaveListFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    UserleaveViewModel userleaveViewModel;
    FragmentUserLeaveListBinding binding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private UserleaveAdpter userleaveAdpter;

    public UserLeaveListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserLeaveListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserLeaveListFragment newInstance(String param1, String param2) {
        UserLeaveListFragment fragment = new UserLeaveListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newtask:
                updateStatuViews(binding.newtask, binding.newTask, binding.imgNew, binding.imgCloseNew);
                prepareStatusListByCode();

                break;
            case R.id.accepted:
                updateStatuViews(binding.accepted, binding.tvAccepted, binding.imgAccepted, binding.imgCloseAccpeted);
                prepareStatusListByCode();

                break;
            case R.id.reject:
                updateStatuViews(binding.reject, binding.rejectTv, binding.imgReject, binding.imgRejectCloss);
                prepareStatusListByCode();

                break;
        }
    }

    private void prepareStatusListByCode() {

        StringBuilder builder = new StringBuilder();
        if (binding.newtask.isSeleted())
            builder.append(AppConstant.NEW_LEAVE_STATUS);
        if (binding.accepted.isSeleted())
            builder.append(" " + AppConstant.APPRO_LEAVE_STATUS);
        if (binding.reject.isSeleted())
            builder.append(" " + AppConstant.REJECT_LEAVE_STATUS);


        if (!TextUtils.isEmpty(builder.toString()) && userleaveAdpter != null)
            userleaveAdpter.filterByStatus(builder.toString());
        else if (userleaveAdpter != null) {
            userleaveAdpter.UpdateAllDataList();
        }


    }

    private void updateStatuViews(CustomSelecter selecter, TextView tv, ImageView statusIcon, ImageView close) {
        try {
            selecter.setSeleted(!selecter.isSeleted());
            TransitionManager.beginDelayedTransition(binding.parentLayout);
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


    public void refreshAppointmentList() {
        if (userleaveViewModel != null) {
            userleaveViewModel.getUserLeaveStatusList();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        userleaveViewModel = ViewModelProviders.of(getActivity()).get(UserleaveViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_leave_list, container, false);
        setUiLables();

        return binding.getRoot();
    }

    private void setUiLables() {
        binding.newtask.setOnClickListener(this);
        binding.accepted.setOnClickListener(this);
        binding.reject.setOnClickListener(this);

        binding.newTask.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.status_new));

        binding.tvAccepted.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.accepted));
        binding.rejectTv.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reject));


        userleaveViewModel.setContext(getActivity());


        userleaveAdpter = new UserleaveAdpter(getActivity(), new ArrayList<UserLeaveResModel>());

        binding.list.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.list.setAdapter(userleaveAdpter);


        userleaveViewModel.getUserLeaveList();


        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (userleaveViewModel != null)
                    if (AppUtility.isInternetConnected())
                        userleaveViewModel.getUserLeaveList();
                    else {
                        binding.swiperefresh.setRefreshing(false);
                        AppUtility.alertDialog(getActivity(),
                                "",
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network),
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), null, null);
                    }
            }
        });


        userleaveViewModel.getUserLeaveStatusList().observe(getActivity(), new Observer<List<UserLeaveResModel>>() {
            @Override
            public void onChanged(List<UserLeaveResModel> userLeaveResModels) {
                binding.swiperefresh.setRefreshing(false);
                if (userleaveAdpter != null) {
                    Collections.sort(userLeaveResModels, new Comparator<UserLeaveResModel>() {
                        @Override
                        public int compare(UserLeaveResModel o1, UserLeaveResModel o2) {
                            return o2.getStartDateTime().compareTo(o1.getStartDateTime());
                        }
                    });
                    userleaveAdpter.UpdateList(userLeaveResModels);
                }
            }
        });

    }

}