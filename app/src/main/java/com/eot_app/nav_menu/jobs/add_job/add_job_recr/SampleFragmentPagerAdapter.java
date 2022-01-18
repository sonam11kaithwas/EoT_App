package com.eot_app.nav_menu.jobs.add_job.add_job_recr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.eot_app.R;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.language_support.LanguageController;

import java.util.ArrayList;
import java.util.List;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    private final String[] tabTitles = new String[]{(LanguageController.getInstance().getMobileMsgByKey(AppConstant.daily)),
            (LanguageController.getInstance().getMobileMsgByKey(AppConstant.weekly)),
            (LanguageController.getInstance().getMobileMsgByKey(AppConstant.monthly))};
    private final FragmentManager fm;
    private final Context context;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fm = fm;
        this.context = context;
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab_layouts, null);
        TextView tv = v.findViewById(R.id.textView);
        tv.setText(tabTitles[position]);
        return v;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
