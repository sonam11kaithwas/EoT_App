package com.eot_app.nav_menu.jobs.job_filter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.R;
import com.eot_app.home_screens.MainActivity;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.nav_menu.jobs.job_filter.filter_model.JobPrioty;
import com.eot_app.nav_menu.jobs.job_filter.filter_mvp.Job_Filter_PC;
import com.eot_app.nav_menu.jobs.job_filter.filter_mvp.Job_Filter_Pi;
import com.eot_app.nav_menu.jobs.job_filter.filter_mvp.Job_Filter_View;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.DropdownListBean;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.setting_db.TagData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class JobFilter_Activity extends AppCompatActivity implements View.OnClickListener, Job_Filter_View {
    Spinner fil_status_spinner, fil_prity_spinner, fil_tag_spinner;
    LinearLayout fil_tag_layout, fil_status_layout, fil_prity_layout;
    TextView fil_prity_hint, fil_status_hint, fil_tag_hint;
    TextView prity_name, status_name, tag_name;
    Button filter_btn, reset_btn;

    Job_Filter_Pi job_filter_pi;

    ArrayList<DropdownListBean> listBeans = new ArrayList<>();
    ArrayList<DropdownListBean> prity_SElectes_list = new ArrayList<>();
    ArrayList<DropdownListBean> status_SElectes_list = new ArrayList<>();
    ArrayList<DropdownListBean> tag_SElectes_list = new ArrayList<>();

    List<TagData> tagList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_filter_second);//activity_job_filter
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.filter));
        initializelables();
        intializeViews();
    }

    private void initializelables() {
        fil_status_spinner = findViewById(R.id.fil_status_spinner);
        fil_prity_spinner = findViewById(R.id.fil_prity_spinner);
        fil_tag_spinner = findViewById(R.id.fil_tag_spinner);

        fil_status_layout = findViewById(R.id.fil_status_layout);
        fil_prity_layout = findViewById(R.id.fil_prity_layout);
        fil_tag_layout = findViewById(R.id.fil_tag_layout);

        fil_prity_hint = findViewById(R.id.fil_prity_hint);
        fil_prity_hint.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_priority));
        fil_status_hint = findViewById(R.id.fil_status_hint);
        fil_status_hint.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_status));

        fil_tag_hint = findViewById(R.id.fil_tag_hint);
        fil_tag_hint.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.tags));

        prity_name = findViewById(R.id.prity_name);
        prity_name.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_priority));

        status_name = findViewById(R.id.status_name);
        status_name.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_status));
        tag_name = findViewById(R.id.tag_name);
        tag_name.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.tags));

        filter_btn = findViewById(R.id.filter_btn);
        filter_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.filter));

        reset_btn = findViewById(R.id.reset_btn);
        reset_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reset));
    }

    private void intializeViews() {

        job_filter_pi = new Job_Filter_PC(this);

        fil_status_layout.setOnClickListener(this);
        fil_prity_layout.setOnClickListener(this);
        fil_tag_layout.setOnClickListener(this);
        filter_btn.setOnClickListener(this);
        reset_btn.setOnClickListener(this);

        job_filter_pi.setStatusList();
        job_filter_pi.setJobPrityList();
        job_filter_pi.getTagList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void setJobPriopity(ArrayList<JobPrioty> jobPrioties) {
        AppUtility.spinnerPopUpWindow(fil_prity_spinner);
        FilterListAdapter prityFilterAdapter = new FilterListAdapter(this, jobPrioties, new FilterListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Set<String> data, Set<DropdownListBean> beanList) {
                prity_name.setText(data.toString().replace("[", "").replace("]", ""));
                if (data.size() > 0)
                    fil_prity_hint.setVisibility(View.VISIBLE);
                else fil_prity_hint.setVisibility(View.INVISIBLE);
                prity_SElectes_list.clear();
                prity_SElectes_list.addAll(beanList);
                Log.e("List", listBeans.toString());
            }
        });
        fil_prity_spinner.setAdapter(prityFilterAdapter);
    }


    @Override
    public void setstatus(final ArrayList<JobStatusModel> statusList) {
        AppUtility.spinnerPopUpWindow(fil_status_spinner);
        FilterListAdapter statusFilterAdapter = new FilterListAdapter(this, statusList, new FilterListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Set<String> data, Set<DropdownListBean> beanList) {
                if (data.size() >= 4)
                    status_name.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_selected) + " " + data.size());
                else
                    status_name.setText(data.toString().replace("[", "").replace("]", ""));
                if (data.size() > 0)
                    fil_status_hint.setVisibility(View.VISIBLE);
                else fil_status_hint.setVisibility(View.INVISIBLE);
                status_SElectes_list.clear();
                status_SElectes_list.addAll(beanList);
            }
        });
        fil_status_spinner.setAdapter(statusFilterAdapter);
    }

    @Override
    public void setTagsList(final List<TagData> tagList) {
        this.tagList = tagList;
        AppUtility.spinnerPopUpWindow(fil_tag_spinner);
        FilterListAdapter tagFilterAdapter = new FilterListAdapter(this, tagList, new FilterListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Set<String> data, Set<DropdownListBean> beanList) {
                if (data.size() >= 4)
                    tag_name.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_selected) + " " + data.size());
                else
                    tag_name.setText(data.toString().replace("[", "").replace("]", ""));
                if (data.size() > 0)
                    fil_tag_hint.setVisibility(View.VISIBLE);
                else fil_tag_hint.setVisibility(View.INVISIBLE);
                tag_SElectes_list.clear();
                tag_SElectes_list.addAll(beanList);
            }
        });

        fil_tag_spinner.setAdapter(tagFilterAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fil_prity_layout:
                fil_prity_spinner.performClick();
                break;
            case R.id.fil_status_layout:
                fil_status_spinner.performClick();
                break;
            case R.id.fil_tag_layout:
                if (tagList.size() > 0)
                    fil_tag_spinner.performClick();
                else
                    AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.empty_tag), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return null;
                        }
                    });
                break;
            case R.id.reset_btn:
                job_filter_pi.setStatusList();
                job_filter_pi.setJobPrityList();
                job_filter_pi.getTagList();
                resetButtonClearAll();
                break;
            case R.id.filter_btn:
                listBeans.addAll(prity_SElectes_list);
                listBeans.addAll(status_SElectes_list);
                listBeans.addAll(tag_SElectes_list);
                Set<DropdownListBean> beanSet = new HashSet<>();
                for (int i = 0; i < listBeans.size(); i++) {
                    beanSet.add(listBeans.get(i));
                }
                ArrayList<DropdownListBean> list = new ArrayList<>(beanSet);

                if (!list.isEmpty()) {
                    Intent intent = new Intent();
                    intent.putExtra("filterlist", list);
                    //String str = new Gson().toJson(list);
                    // intent.putExtra("filterlist", str);
                    setResult(MainActivity.FILTERJOBLIST, intent);
                    this.finish();
                } else {
                    job_filter_pi.emptyFilterListDialog();

                }
                break;
        }
    }

    private void resetButtonClearAll() {
        prity_SElectes_list.clear();
        status_SElectes_list.clear();
        tag_SElectes_list.clear();

        tag_name.setText("");
        tag_name.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.tags));
        fil_tag_hint.setVisibility(View.GONE);

        status_name.setText("");
        status_name.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_status));
        fil_status_hint.setVisibility(View.GONE);

        prity_name.setText("");
        prity_name.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_priority));
        fil_prity_hint.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



