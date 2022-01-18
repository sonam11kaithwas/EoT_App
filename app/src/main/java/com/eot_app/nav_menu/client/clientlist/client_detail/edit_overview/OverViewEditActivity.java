package com.eot_app.nav_menu.client.clientlist.client_detail.edit_overview;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.R;
import com.eot_app.nav_menu.client.add_client.clientadpter.ClientIndustryAdpter;
import com.eot_app.nav_menu.client.add_client.clientadpter.ClientRefrenceAdpter;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.ClientDetail;
import com.eot_app.nav_menu.client.clientlist.client_detail.edit_overview.edit_client_presenter.Edit_client_pc;
import com.eot_app.nav_menu.client.clientlist.client_detail.edit_overview.edit_client_presenter.Edit_client_pi;
import com.eot_app.nav_menu.client.clientlist.client_detail.edit_overview.edit_client_view.Edit_client_view;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.client_refrence_db.ClientRefrenceModel;
import com.eot_app.utility.settings.clientaccount_db.ClientAccountType;
import com.eot_app.utility.settings.clientindustry_db.ClientIndustryModel;
import com.eot_app.utility.util_interfaces.MySpinnerAdapter;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class OverViewEditActivity extends AppCompatActivity implements Edit_client_view, View.OnClickListener, TextWatcher, Spinner.OnItemSelectedListener {

    EditText name, gst_no, tin_no, notes;
    Button update_bt;
    Spinner industry_spinner, account_spinner, referenceDp;
    RadioButton active, inactive;
    Edit_client_pi presenter;
    TextInputLayout layout_client_notes, layout_tin_no, layout_gst_no, layout_client_name;
    RelativeLayout relative_main;
    TextView hint_tv_ac, tv_spinner_account, tv_spinner_ind, tv_hint_indus, hint_tv_reference, tv_spinner_reference;
    LinearLayout Layout_account, indus_linearLayout, linearLayout_reference;
    //String[] indusArray;
    String[] accountList;
    List<ClientAccountType> accountTypeKey;
    String acctype, indusId;
    int reference = 0;
    private TextView status_lable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_view_edit);
        presenter = new Edit_client_pc(this);
        initializelables();
        initializeViews();
        initializeTextInputLayout();
//        get client id from intent.
        if (getIntent().hasExtra("Overview")) {
            presenter.setClientValues((Client) getIntent().getParcelableExtra("Overview"));
            getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.edit_client));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setIndustry();
        setRefrences();
    }

    private void initializelables() {
        name = findViewById(R.id.name);
        name.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.client_name) + " *");

        tv_spinner_account = findViewById(R.id.tv_spinner_account);
        tv_spinner_account.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.account_type));

        gst_no = findViewById(R.id.gst_no);
        // gst_no.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.gst_no));

        tin_no = findViewById(R.id.tin_no);
        // tin_no.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.tin_no));

        if (App_preference.getSharedprefInstance().getCompanySettingsDetails()
                .getGstLabel() != null)
            gst_no.setHint(App_preference.getSharedprefInstance().getCompanySettingsDetails()
                    .getGstLabel());

        if (App_preference.getSharedprefInstance().getCompanySettingsDetails()
                .getTinLabel() != null)
            tin_no.setHint(App_preference.getSharedprefInstance().getCompanySettingsDetails()
                    .getTinLabel());

        status_lable = findViewById(R.id.status_lable);
        status_lable.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.status_radio_btn));

        active = findViewById(R.id.active);
        active.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.active_radio_btn));

        inactive = findViewById(R.id.inactive);
        inactive.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.inactive_radio_btn));

        tv_spinner_ind = findViewById(R.id.tv_spinner_ind);
        tv_spinner_ind.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.industry));

        notes = findViewById(R.id.notes);
        notes.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.notes));

        update_bt = findViewById(R.id.update_bt);
        update_bt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_btn));


        /**Reference View find***/
        linearLayout_reference = findViewById(R.id.linearLayout_reference);
        referenceDp = findViewById(R.id.referenceDp);
        hint_tv_reference = findViewById(R.id.hint_tv_reference);
        tv_spinner_reference = findViewById(R.id.tv_spinner_reference);

        hint_tv_reference.setHint((LanguageController.getInstance().getMobileMsgByKey(AppConstant.reference)));
        tv_spinner_reference.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reference));

    }

    private void setRefrences() {
        AppUtility.spinnerPopUpWindow(referenceDp);

        try {
            final List<ClientRefrenceModel> list = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientRefrenceDao().getRefrenceList();


            referenceDp.setAdapter(new ClientRefrenceAdpter(this, list));

            referenceDp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    hint_tv_reference.setVisibility(View.VISIBLE);
                    hint_tv_reference.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reference));
                    tv_spinner_reference.setText(list.get(position).getRefName());
                    reference = Integer.parseInt(list.get(position).getRefId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void initializeViews() {
        relative_main = findViewById(R.id.relative_main);
        AppUtility.setupUI(relative_main, OverViewEditActivity.this);

        update_bt.setOnClickListener(this);
        industry_spinner = findViewById(R.id.industry_spinner);
        account_spinner = findViewById(R.id.account_spinner);

        presenter.getAccountypelist();

        Layout_account = findViewById(R.id.Layout_account);
        indus_linearLayout = findViewById(R.id.indus_linearLayout);
        hint_tv_ac = findViewById(R.id.hint_tv_ac);


        tv_hint_indus = findViewById(R.id.tv_hint_indus);

        Layout_account.setOnClickListener(this);
        indus_linearLayout.setOnClickListener(this);
        account_spinner.setOnItemSelectedListener(this);
        linearLayout_reference.setOnClickListener(this);
    }


    private void initializeTextInputLayout() {
        layout_client_name = findViewById(R.id.layout_client_name);
        layout_gst_no = findViewById(R.id.layout_gst_no);
        layout_tin_no = findViewById(R.id.layout_tin_no);
        layout_client_notes = findViewById(R.id.layout_client_notes);

        layout_client_name.getEditText().addTextChangedListener(this);
        layout_gst_no.getEditText().addTextChangedListener(this);
        layout_tin_no.getEditText().addTextChangedListener(this);
        layout_client_notes.getEditText().addTextChangedListener(this);
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
    public void setIndustry() {
        final List<ClientIndustryModel> getIndustryList = AppDataBase.getInMemoryDatabase(this).clientIndustryDao().getIndustryList();
        AppUtility.spinnerPopUpWindow(industry_spinner);
        industry_spinner.setAdapter(new ClientIndustryAdpter(this, getIndustryList));

        industry_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_hint_indus.setVisibility(View.VISIBLE);
                tv_hint_indus.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.industry));
                tv_spinner_ind.setText(getIndustryList.get(position).getIndustryName());
                indusId = getIndustryList.get(position).getIndustryId();// + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearLayout_reference:
                referenceDp.performClick();
                break;
            case R.id.Layout_account:
                account_spinner.performClick();
                break;
            case R.id.indus_linearLayout:
                industry_spinner.performClick();
                break;
            case R.id.update_bt:
                if (name.getText().toString().trim().length() > 0 && acctype != null) {
                    presenter.callUpdateClient(name.getText().toString().trim(),
                            acctype, gst_no.getText().toString().trim(),
                            tin_no.getText().toString().trim(),
                            active.isChecked(),
                            indusId,
                            notes.getText().toString().trim(), tv_spinner_ind.getText().toString(), reference + "");
                } else {
                    AppUtility.error_Alert_Dialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_client_name), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                            , new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    return null;
                                }
                            });
                }
                break;
        }
    }

    @Override
    public void updateOverviewUI(Client client) {
        name.setText(client.getNm());
        String acName = (String) presenter.getAccoutTypeName(client.getPymtType());
        if (!client.getPymtType().equals("0")) {
            hint_tv_ac.setVisibility(View.VISIBLE);
            hint_tv_ac.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.account_type));
        }
        acctype = client.getPymtType();
        if (acName != null) {
            tv_spinner_account.setText(acName);
            hint_tv_ac.setVisibility(View.VISIBLE);
            hint_tv_ac.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.account_type));
        }
        gst_no.setText(client.getGstNo());
        tin_no.setText(client.getTinNo());
        if (!(client.getIndustry()).equals("0"))
            tv_hint_indus.setVisibility(View.VISIBLE);
        tv_hint_indus.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.industry));
        tv_spinner_ind.setText(client.getIndustryName());
        indusId = client.getIndustry();
        try {
            if (client.getReferral() != null && !client.getReferral().equals("0")) {
                hint_tv_reference.setVisibility(View.VISIBLE);
                hint_tv_reference.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reference));
                // tv_spinner_reference.setText(AppConstant.clientReferenceList[Integer.parseInt(client.getReferral()) - 1]);
                ClientRefrenceModel model = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientRefrenceDao().getClientByRefrenceId(client.getReferral());
                tv_spinner_reference.setText(model.getRefName());
                reference = Integer.parseInt(client.getReferral());

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        notes.setText(client.getNote());
        if (Integer.parseInt(client.getIsactive()) == 1) {//&& client.getIsactive()!=null
            active.setChecked(true);
        } else if (Integer.parseInt(client.getIsactive()) == 0) {
            inactive.setChecked(true);
        }
    }

    @Override
    public void onClientUpdate(String clientId, boolean result, String msg) {
        EotApp.getAppinstance().showToastmsg(msg);
        if (result) {
            Intent intent = new Intent();
            intent.putExtra("clientId", clientId);
            setResult(ClientDetail.ClientEDIT, intent);
            finish();
        }
    }

    @Override
    public void setAccountTypeList(List<ClientAccountType> accountTypeList) {
        AppUtility.spinnerPopUpWindow(account_spinner);
        accountTypeKey = accountTypeList;
        ArrayList<String> arrayOfAccountType = new ArrayList<String>();
        for (ClientAccountType clientAccountType : accountTypeList)
            arrayOfAccountType.add(clientAccountType.getType());
        accountList = arrayOfAccountType.toArray(new String[arrayOfAccountType.size()]);
        account_spinner.setAdapter(new MySpinnerAdapter(OverViewEditActivity.this, accountList));
        account_spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() >= 1) {
            if (charSequence.hashCode() == name.getText().hashCode())
                layout_client_name.setHintEnabled(true);
            if (charSequence.hashCode() == gst_no.getText().hashCode())
                layout_gst_no.setHintEnabled(true);
            if (charSequence.hashCode() == tin_no.getText().hashCode())
                layout_tin_no.setHintEnabled(true);
            if (charSequence.hashCode() == notes.getText().hashCode())
                layout_client_notes.setHintEnabled(true);
        } else if (charSequence.length() <= 0) {
            if (charSequence.hashCode() == name.getText().hashCode())
                layout_client_name.setHintEnabled(false);
            if (charSequence.hashCode() == gst_no.getText().hashCode())
                layout_gst_no.setHintEnabled(false);
            if (charSequence.hashCode() == tin_no.getText().hashCode())
                layout_tin_no.setHintEnabled(false);
            if (charSequence.hashCode() == notes.getText().hashCode())
                layout_client_notes.setHintEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.account_spinner:
                hint_tv_ac.setVisibility(View.VISIBLE);
                hint_tv_ac.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.account_type));
                tv_spinner_account.setText(accountList[i]);
                acctype = accountTypeKey.get(i).getKey();
                break;
          /*  case R.id.referenceDp:
                hint_tv_reference.setVisibility(View.VISIBLE);
                hint_tv_reference.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reference));
                // tv_spinner_reference.setText(AppConstant.clientReferenceList[i]);
                reference = i + 1;
                break;*/
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
