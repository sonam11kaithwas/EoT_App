package com.eot_app.nav_menu.client.clientlist.client_detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.eot_app.R;
import com.eot_app.login_next.login_next_model.CompPermission;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.client_overview.Client_Overview;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.Client_Contact_List;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.edit_contact.editmodel.Edit_Add_Contact_Activity;
import com.eot_app.nav_menu.client.clientlist.client_detail.edit_overview.OverViewEditActivity;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.Client_Site_List;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.EditSiteActivity;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.WorkHistoryFragment;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.ApiContactSiteObserver;
import com.eot_app.utility.util_interfaces.OnFragmentInteractionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ClientDetail extends AppCompatActivity implements OnFragmentInteractionListener, ViewPager.OnPageChangeListener, ApiContactSiteObserver {
    public static final int ClientEDIT = 12;
    private BottomNavigationView navigation;
    private Client_Contact_List client_contact;
    private ViewPager viewPager;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.client_overview:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.client_contact:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.client_site:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.work_history:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };
    private Client_Overview clientOverview;
    private Client_Site_List clientSite;
    private WorkHistoryFragment workHistoryFragment;
    private Client data;
    private Menu menu;
    private MypagerAdapter mypagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_detail);

//        set observer for callback
        EotApp.getAppinstance().setApiCon_SiteObserver(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            data = (Client) bundle.get("data");
        }
        viewPager = findViewById(R.id.clientdetail_pager);
        setUpViewPager();
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setTitle(mypagerAdapter.getPageTitle(0));
        navigation.getMenu().getItem(0).setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.overview));
        navigation.getMenu().getItem(1).setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.contacts_screen_title));
        navigation.getMenu().getItem(2).setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.sites_screen_title));
        CompPermission compPermission = App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0);
        if (compPermission.getCltWorkHistory() != null) {
            if (compPermission.getCltWorkHistory().equals("0"))
                navigation.getMenu().getItem(3).setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.work_history));
            else
                navigation.getMenu().getItem(3).setVisible(false);
        }
    }

    @Override
    protected void onDestroy() {
        EotApp.getAppinstance().setApiCon_SiteObserver(null);
        super.onDestroy();
    }

    private void setUpViewPager() {
        mypagerAdapter = new MypagerAdapter(getSupportFragmentManager());
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(mypagerAdapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTitle(mypagerAdapter.getPageTitle(position));
        navigation.getMenu().getItem(position).setChecked(true);
        switch (position) {
            case 0:
                navigation.getMenu().getItem(position).setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.overview));
                try {
                    menu.setGroupVisible(R.id.clt_grp_ovrvw, true);
                    menu.setGroupVisible(R.id.clt_grp_contact, false);
                    menu.setGroupVisible(R.id.clt_grp_site, false);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                if (clientOverview != null && data != null) {
                    clientOverview.refreshUI(data.getCltId());
                }
                break;
            case 1:
                navigation.getMenu().getItem(position).setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.contacts_screen_title));
                try {
                    menu.setGroupVisible(R.id.clt_grp_ovrvw, false);
                    menu.setGroupVisible(R.id.clt_grp_contact, true);
                    menu.setGroupVisible(R.id.clt_grp_site, false);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            case 2:
                navigation.getMenu().getItem(position).setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.sites_screen_title));
                /**************** at com.eot_app.nav_menu.client.clientlist.client_detail.ClientDetail.onPageSelected (ClientDetail.java:140)*************/
                try {
                    menu.setGroupVisible(R.id.clt_grp_ovrvw, false);
                    menu.setGroupVisible(R.id.clt_grp_contact, false);
                    menu.setGroupVisible(R.id.clt_grp_site, true);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;

            case 3:
                navigation.getMenu().getItem(position).setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.work_history));
                try {
                    menu.setGroupVisible(R.id.clt_grp_ovrvw, false);
                    menu.setGroupVisible(R.id.clt_grp_contact, false);
                    menu.setGroupVisible(R.id.clt_grp_site, false);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.client_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clt_edit:
                Intent intent1 = new Intent(this, OverViewEditActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent1.putExtra("Overview", data);
                startActivityForResult(intent1, ClientEDIT);
                return true;
            case R.id.clt_add_contact:
                Intent in = new Intent(ClientDetail.this, Edit_Add_Contact_Activity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                in.putExtra("contactAdd", data.getCltId());
                startActivityForResult(in, Client_Contact_List.ADDCONTACT);
                return true;

            case R.id.clt_add_site:
                Intent intent = new Intent(ClientDetail.this, EditSiteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("siteAdd", data.getCltId());
                startActivityForResult(intent, Client_Site_List.SITE_ADD);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            switch (resultCode) {
                case 12:
                    if (data.hasExtra("clientId")) {
                        clientOverview.refreshUI(data.getStringExtra("clientId"));
                    }
                    break;
                case 21:
                    String Id = data.getExtras().getString("conId");
                    client_contact.contactUpdate(AppConstant.ForAdd, Id);
                    break;
                case 22:
                    String ciId = data.getExtras().getString("conId");
                    client_contact.contactUpdate(AppConstant.ForEdit, ciId);
                case 31:
                    if (data.hasExtra("siteId")) {
                        clientSite.updateAdapter(AppConstant.ForAdd, data.getStringExtra("siteId"));
                    }
                    break;
                case 32:
                    if (data.hasExtra("siteId")) {
                        clientSite.updateAdapter(AppConstant.ForEdit, data.getStringExtra("siteId"));
                    }
                    break;
            }
        }

    }

    @Override
    public void onObserveCallBack(String api_name) {
        Log.e("TAG", api_name);
        switch (api_name) {
            //    case Service_apis.
            case Service_apis.addClientContact:
                if (client_contact != null) {
                    client_contact.updateFromObserver();
                }
                break;
            case Service_apis.updateClientSite:
            case Service_apis.addClientSite:
                if (clientSite != null) {
                    clientSite.updateFromObserver();
                    client_contact.getUpdateConbtact();
                }
                break;
        }
    }

    class MypagerAdapter extends FragmentPagerAdapter {
        private final int ITEM_COUNT = 4;

        public MypagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (clientOverview == null) {
                        clientOverview = Client_Overview.newInstance(data);
                    }
                    return clientOverview;

                case 1:
                    if (client_contact == null) {
                        client_contact = Client_Contact_List.newInstance(data.getCltId());
                    }
                    return client_contact;

                case 2:
                    if (clientSite == null) {
                        clientSite = Client_Site_List.newInstance(data.getCltId(), "0");
                    }
                    return clientSite;

                case 3:
                    if (workHistoryFragment == null) {
                        workHistoryFragment = WorkHistoryFragment.newInstance(data.getCltId());
                    }
                    return workHistoryFragment;

                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return LanguageController.getInstance().getMobileMsgByKey(AppConstant.overview);
                case 1:
                    return LanguageController.getInstance().getMobileMsgByKey(AppConstant.contacts_screen_title);
                case 2:
                    return LanguageController.getInstance().getMobileMsgByKey(AppConstant.sites_screen_title);
                case 3:
                    return LanguageController.getInstance().getMobileMsgByKey(AppConstant.work_history);
                default:
                    return "";

            }
        }

        @Override
        public int getCount() {
            CompPermission compPermission = App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0);
            if (compPermission.getCltWorkHistory() != null) {
                if (compPermission.getCltWorkHistory().equals("0"))
                    return ITEM_COUNT;
                else
                    return ITEM_COUNT - 1;
            }
            return ITEM_COUNT;
        }
    }
}
