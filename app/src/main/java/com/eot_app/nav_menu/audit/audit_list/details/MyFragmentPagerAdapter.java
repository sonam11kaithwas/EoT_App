package com.eot_app.nav_menu.audit.audit_list.details;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahendra Dabi on 7/11/19.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> titles = new ArrayList<>();

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addNewFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titles.add(title);
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
