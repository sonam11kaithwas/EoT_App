package com.eot_app.nav_menu.client.clientlist.client_detail.work_history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.SampleFragmentPagerAdapter;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.language_support.LanguageController;
import com.google.android.material.tabs.TabLayout;

/**
 * Created by Mahendra Dabi on 12/3/21.
 */
public class WorkHistoryFragment extends Fragment {

    String cltId;
    ClientWorkHistoryList clientWorkHistoryList;

    public static WorkHistoryFragment newInstance(String cltId) {
        WorkHistoryFragment fragment = new WorkHistoryFragment();
        Bundle args = new Bundle();
        args.putString("cltId", cltId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.fragment_client_work_hitory, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.cltId = bundle.getString("cltId");

        }


        ViewPager viewPager = view.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        if (cltId != null)
            setupViewPager(viewPager, cltId);

        // Give the TabLayout the ViewPager
        final TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }


    private void setupViewPager(ViewPager viewPager, String cltId) {

        ClientWorkHistoryList fragmentForJob = ClientWorkHistoryList.newInstance(ClientWorkHistoryList.FragmentTypes.JOB, cltId);
        ClientWorkHistoryList fragmentForAudit = ClientWorkHistoryList.newInstance(ClientWorkHistoryList.FragmentTypes.AUDIT, cltId);
        ClientWorkHistoryList fragmentForAppointment = ClientWorkHistoryList.newInstance(ClientWorkHistoryList.FragmentTypes.APPOINTMENT, cltId);

        SampleFragmentPagerAdapter pagerAdapter =
                new SampleFragmentPagerAdapter(getChildFragmentManager(), getActivity());
        pagerAdapter.addFragment(fragmentForJob, LanguageController.getInstance().getMobileMsgByKey(AppConstant.job));
        pagerAdapter.addFragment(fragmentForAudit, LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_nav));
        pagerAdapter.addFragment(fragmentForAppointment, LanguageController.getInstance().getMobileMsgByKey(AppConstant.appointment));
        viewPager.setAdapter(pagerAdapter);
    }


}
