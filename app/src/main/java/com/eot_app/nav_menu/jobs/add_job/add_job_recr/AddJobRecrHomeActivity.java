package com.eot_app.nav_menu.jobs.add_job.add_job_recr;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.DailyRecrFragment;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.montly_recr_pkg.MontlyRecrFragment;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.weekly_recr_pkg.WeeklyRecrFragment;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.language_support.LanguageController;
import com.google.android.material.tabs.TabLayout;

public class AddJobRecrHomeActivity extends AppCompatActivity {
    private String addJobScdlStartTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job_recr_home);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.recuring_job));//recuring_job


        Bundle bundle = getIntent().getExtras();
        if (getIntent().hasExtra("AddJobScdlStartTime")) {
            addJobScdlStartTime = bundle.getString("AddJobScdlStartTime");
        }


        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);

        setupViewPager(viewPager);

        // Give the TabLayout the ViewPager
        final TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        Bundle bundle = new Bundle();
        bundle.putString("dateTime", addJobScdlStartTime);
        DailyRecrFragment dailyRecrFragment = new DailyRecrFragment();
        WeeklyRecrFragment weeklyRecrFragment = new WeeklyRecrFragment();
        MontlyRecrFragment montlyRecrFragment = new MontlyRecrFragment();

        dailyRecrFragment.setArguments(bundle);
        weeklyRecrFragment.setArguments(bundle);
        montlyRecrFragment.setArguments(bundle);

        SampleFragmentPagerAdapter pagerAdapter =
                new SampleFragmentPagerAdapter(getSupportFragmentManager(), AddJobRecrHomeActivity.this);
        pagerAdapter.addFragment(dailyRecrFragment, (LanguageController.getInstance().getMobileMsgByKey(AppConstant.daily)));
        pagerAdapter.addFragment(weeklyRecrFragment, LanguageController.getInstance().getMobileMsgByKey(AppConstant.weekly));
        pagerAdapter.addFragment(montlyRecrFragment, LanguageController.getInstance().getMobileMsgByKey(AppConstant.monthly));

        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}