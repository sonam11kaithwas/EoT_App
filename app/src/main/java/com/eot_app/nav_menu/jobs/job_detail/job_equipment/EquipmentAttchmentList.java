package com.eot_app.nav_menu.jobs.job_detail.job_equipment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.model.EqipAttchAdpter;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EquipmentAttchmentList extends AppCompatActivity {
    EqipAttchAdpter attchAdpter;
    RecyclerView.LayoutManager layoutManager;
    private List<GetFileList_Res> attachments = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_attchment_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            if (getIntent().hasExtra("list")) {
                String str = getIntent().getExtras().get("list").toString();
                Type listType = new TypeToken<List<GetFileList_Res>>() {
                }.getType();
                attachments = new Gson().fromJson(str, listType);
            }
        } catch (Exception e) {
            e.getMessage();
        }

        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_documents));
        intializeViews();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void intializeViews() {
        recyclerView = findViewById(R.id.fileupload_rc);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        attchAdpter = new EqipAttchAdpter(attachments, this);
        recyclerView.setAdapter(attchAdpter);
    }
}