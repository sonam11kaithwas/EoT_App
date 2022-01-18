package com.eot_app.nav_menu.audit.audit_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.R;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.audit.audit_list.details.AuditDetailsFragment;
import com.eot_app.nav_menu.audit.audit_list.details.MyFragmentPagerAdapter;
import com.eot_app.nav_menu.audit.audit_list.documents.DocumentsFragment;
import com.eot_app.nav_menu.audit.audit_list.equipment.AuditEquipmentFragment;
import com.eot_app.nav_menu.audit.audit_list.report.AuditReportFragment;
import com.eot_app.nav_menu.audit.audit_list.scanbarcode.AuditScanbarcodeFragment;
import com.eot_app.nav_menu.jobs.job_detail.MenuItemsModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.CustomViewPagerState;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mahendra Dabi on 6/11/19.
 */
public class AuditDetails extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {//, AuditObserverNotify

    private final int ID_DETAILS = 102;
    private final int ID_EQUIPMENT = 103;
    private final int ID_SCAN_BARCOE = 104;
    private final int ID_REPORT = 105;
    private final int ID_DOCUMENT = 106;
    private final List<MenuItemsModel> menu_items_ilist = new ArrayList<>();
    public BottomNavigationView navigation;
    DocumentsFragment documentsFragment;
    AuditEquipmentFragment auditEquipmentFragment;
    //ArrayList<String> statusList = new ArrayList<>();
    private AuditList_Res auditList_res;
    private CustomViewPagerState viewPager;
    private MyFragmentPagerAdapter pagerAdapter;
    private int position = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_details);

        navigation = findViewById(R.id.navigation);
        viewPager = findViewById(R.id.jobdetail_pager);


        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_audit));
        setBottomMenus();

        navigation.setOnNavigationItemSelectedListener(this);
//        statusList.add("1");
//        statusList.add("8");
//        statusList.add("7");
//        statusList.add("9");

        Intent intent = getIntent();
        if (intent.hasExtra("audit"))
            this.auditList_res = getIntent().getParcelableExtra("audit");

        if (intent.hasExtra("auditstr")) {
            String str = getIntent().getExtras().getString("auditstr");
            auditList_res = new Gson().fromJson(str, AuditList_Res.class);
        }
        if (intent.hasExtra("position"))
            position = getIntent().getIntExtra("position", -1);


        try {
            if (auditList_res == null) {
                return;
            }

            if (auditList_res.getStatus() != null && !AppUtility.auditStatusList().contains(auditList_res.getStatus())
                    && auditList_res.getAudId() != null) {
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().deletAuditById(auditList_res.getAudId());
                this.finish();
            }

        } catch (Exception e) {
            e.getMessage();
        }


        documentsFragment = DocumentsFragment.newInstance("", this.auditList_res.getAudId() + "");
        //init pager adapter
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addNewFragment(AuditDetailsFragment.getInstance(this.auditList_res, position), LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_audit));
        if (isEquipmentPermissionEnabled()) {
            auditEquipmentFragment = AuditEquipmentFragment.getInstance(this.auditList_res.getAudId());
            pagerAdapter.addNewFragment(auditEquipmentFragment, LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_equipment));
            pagerAdapter.addNewFragment(AuditScanbarcodeFragment.getInstance(this.auditList_res.getAudId() + ""), LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_scan_barcode));
        }
        pagerAdapter.addNewFragment(AuditReportFragment.getInstance(this.auditList_res.getAudId() + ""), LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_report));
        pagerAdapter.addNewFragment(documentsFragment, LanguageController.getInstance().getMobileMsgByKey(AppConstant.document));

        viewPager.setAdapter(pagerAdapter);
    }

    /*setting bottomnavigation menu items*/
    private void setBottomMenus() {
        initializeMenuItemList();

        Menu menu = navigation.getMenu();
        if (menu_items_ilist.size() > 0) {
            for (MenuItemsModel item : menu_items_ilist) {
                menu.add(Menu.NONE, item.getMenu_item_id(), Menu.NONE, item.getMenu_title()).setIcon(item.getMenu_icon());
                item.setIsalreadySet(true);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            AppUtility.hideSoftKeyboard(AuditDetails.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeMenuItemList() {
        if (menu_items_ilist.size() > 0) {
            menu_items_ilist.clear();
        }

        /********      add all menu items to arraylist ************/
        menu_items_ilist.add(new MenuItemsModel(ID_DETAILS, LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_audit), R.drawable.ic_detail));
        if (isEquipmentPermissionEnabled()) {
            menu_items_ilist.add(new MenuItemsModel(ID_EQUIPMENT, LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_equipment), R.drawable.ic_equipement));
            menu_items_ilist.add(new MenuItemsModel(ID_SCAN_BARCOE, LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_scan_barcode), R.drawable.ic_qr_code));
        }
        menu_items_ilist.add(new MenuItemsModel(ID_REPORT, LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_report), R.drawable.ic_report));
        menu_items_ilist.add(new MenuItemsModel(ID_DOCUMENT, LanguageController.getInstance().getMobileMsgByKey(AppConstant.document), R.drawable.ic_attach_document));

    }

    private boolean isEquipmentPermissionEnabled() {
        boolean b = false;
        if (App_preference.getSharedprefInstance().getLoginRes().getIsEquipmentEnable() != null)
            b = App_preference.getSharedprefInstance().getLoginRes().getIsEquipmentEnable().equals("1");

        return b;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case ID_DETAILS:
                setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_audit));
                viewPager.setCurrentItem(0, false);
                break;
            case ID_EQUIPMENT:
                setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_equipment));
                viewPager.setCurrentItem(1, false);
                if (auditEquipmentFragment != null) {
                    auditEquipmentFragment.refreshListOnEquipmentTabSelection();
                }
                break;
            case ID_SCAN_BARCOE:
                setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_scan_barcode));
                if (isEquipmentPermissionEnabled())
                    viewPager.setCurrentItem(2, false);
                break;
            case ID_REPORT:
                setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_report));
                if (isEquipmentPermissionEnabled())
                    viewPager.setCurrentItem(3, false);
                else
                    viewPager.setCurrentItem(1, false);
                break;
            case ID_DOCUMENT:
                setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.document));
                if (isEquipmentPermissionEnabled())
                    viewPager.setCurrentItem(4, false);
                else viewPager.setCurrentItem(2, false);
                break;
        }
        return true;
    }


    /*  private void updateFragments(Fragment fragment) {
          getSupportFragmentManager().beginTransaction()
                  .replace(R.id.fl_audit_details, fragment).commit();
      }
  */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (viewPager != null && viewPager.getAdapter() != null &&
                viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(0, false);
            navigation.setSelectedItemId(ID_DETAILS);

        } else
            super.onBackPressed();
        // EotApp.getAppinstance().notifyApiObserver(Service_apis.addAppointment);
    }

    public void refreshDocuments() {
        if (documentsFragment != null && documentsFragment.isVisible()) {
            documentsFragment.refreshDocuments();
        }
    }
}
