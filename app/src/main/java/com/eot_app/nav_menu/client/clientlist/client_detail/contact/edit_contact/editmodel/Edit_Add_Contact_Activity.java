package com.eot_app.nav_menu.client.clientlist.client_detail.contact.edit_contact.editmodel;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.eot_app.R;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.Client_Contact_List;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.SiteListAdpter;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.PrefixEditText;
import com.eot_app.utility.language_support.LanguageController;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class Edit_Add_Contact_Activity extends AppCompatActivity implements View.OnClickListener, EditContact_View, TextWatcher {
    private final boolean selfContact = false;
    TextInputLayout input_layout_name, input_layout_con_email, input_layout_mobile, input_layout_Alternate, input_layout_Fax, input_layout_skype,
            input_layout_twitter;//, contact_lat_layout, contact_lng_layout;
    EditText con_name, con_email, con_mob, con_alternate, con_fax, con_skype, con_twitter;//, edt_lng, edt_lat;
    Button update_bt;
    String key;
    ContactData contactData;
    EditContact_Pi editContact_pi;
    CheckBox active;
    RelativeLayout relative_main;
    private TextView link_site, tv_hint_site, site_set;
    private ImageView site_img;
    private Spinner site_title_spinner;
    private Set<String> jtIdList = new HashSet<>();
    private ConstraintLayout siteConstraintLayout;
    private SiteListAdpter myAdapter;
    private PrefixEditText con_mob_ctry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contactnew);
        relative_main = findViewById(R.id.relative_main);
        AppUtility.setupUI(relative_main, Edit_Add_Contact_Activity.this);

        initializelables();
        findInputTextLayout();
        textInputClick();


        editContact_pi = new EditContact_Pc(this);
        update_bt = findViewById(R.id.update_bt);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (getIntent().hasExtra("contactEdit")) {
            contactData = bundle.getParcelable("contactEdit");
            // contactData = (ContactData) bundle.getSerializable("contactEdit");
        } else {
            key = (String) bundle.get("contactAdd");
            appendCtryCode();
        }


        if (contactData == null) {
            getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_contacts_screen_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            update_bt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.create_contact));
            editContact_pi.getSiteFromdb(key);
        } else {
            getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.edit_contact));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            update_bt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_btn));
            key = contactData.getCltId();
            editContact_pi.getSiteFromdb(key);
            editContact_pi.setEditContactData(contactData);

        }

//        editContact_pi = new EditContact_Pc(this);

        update_bt.setOnClickListener(this);

    }

    private void appendCtryCode() {
        try {
            if (App_preference.getSharedprefInstance().getLoginRes().getCtryCode() != null) {
                con_mob.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
                con_alternate.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

    private void initializelables() {
        con_name = findViewById(R.id.con_name);
        con_name.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.contact_name) + " *");

        con_email = findViewById(R.id.con_email);
        con_email.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.email));// + " *"

        con_mob = findViewById(R.id.con_mob);
        con_mob.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.mob_no));//+ " *"

        con_alternate = findViewById(R.id.con_alternate);
        con_alternate.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.alt_mobile_number));

        con_fax = findViewById(R.id.con_fax);
        con_fax.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.fax));

        con_skype = findViewById(R.id.skype);
        con_skype.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.skype));

        con_twitter = findViewById(R.id.twitter);
        con_twitter.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.twitter));

        active = findViewById(R.id.active);
        active.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.contact_chkBox));

        link_site = findViewById(R.id.link_site);
        link_site.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.link_site));


        tv_hint_site = findViewById(R.id.tv_hint_site);
        tv_hint_site.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.select_site));

        site_set = findViewById(R.id.site_set);
        site_set.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.select_site));

        siteConstraintLayout = findViewById(R.id.siteConstraintLayout);
        siteConstraintLayout.setOnClickListener(this);

        site_title_spinner = findViewById(R.id.site_title_spinner);
    }


    public void findInputTextLayout() {
        input_layout_name = findViewById(R.id.input_layout_name);
        input_layout_con_email = findViewById(R.id.input_layout_con_email);
        input_layout_mobile = findViewById(R.id.input_layout_mobile);
        input_layout_Alternate = findViewById(R.id.input_layout_Alternate);
        input_layout_Fax = findViewById(R.id.input_layout_Fax);
        input_layout_skype = findViewById(R.id.input_layout_skype);
        input_layout_twitter = findViewById(R.id.input_layout_twitter);


    }


    @Override
    public void setSiteList(List<Site_model> siteList) {
        AppUtility.spinnerPopUpWindow(site_title_spinner);

        if (contactData != null) {
            /** update/edit quotes*/

            myAdapter = new SiteListAdpter(this, 0, siteList, new SiteListAdpter.OnItemClickListener() {
                @Override
                public void onItemClick(Set<String> data, Set<String> title_ids) {
                    jtIdList = title_ids;
                    Log.e("TAG :", "" + jtIdList);
                    contactSiteSelected(data);
                }
            }, contactData.getSiteId());
        } else {
            /** add quotes*/
            myAdapter = new SiteListAdpter(this, 0, siteList, new SiteListAdpter.OnItemClickListener() {
                @Override
                public void onItemClick(Set<String> data, Set<String> title_ids) {
                    jtIdList = title_ids;
                    contactSiteSelected(data);
                    Log.e("TAG :", "" + jtIdList);
                }
            });
        }

        site_title_spinner.setAdapter(myAdapter);
    }


    private void contactSiteSelected(Set<String> data) {
        if (data.size() >= 4) {//show count when more than 4 services selected
            site_set.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_selected) + " " + data.size());
        } else {
            site_set.setText(data.toString().replace("[", "").replace("]", ""));
        }
        if (data.size() > 0) {
            tv_hint_site.setVisibility(View.VISIBLE);

        } else {
            tv_hint_site.setVisibility(View.INVISIBLE);

        }

    }

    public void textInputClick() {
        input_layout_name.getEditText().addTextChangedListener(this);
        input_layout_con_email.getEditText().addTextChangedListener(this);
        input_layout_mobile.getEditText().addTextChangedListener(this);
        input_layout_Alternate.getEditText().addTextChangedListener(this);
        input_layout_Fax.getEditText().addTextChangedListener(this);
        input_layout_skype.getEditText().addTextChangedListener(this);
        input_layout_twitter.getEditText().addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.siteConstraintLayout:
                site_title_spinner.performClick();
                break;
            case R.id.update_bt:
                String nm, email, fax, skype, twitter, alternate, mob;
                nm = con_name.getText().toString().trim();
                email = con_email.getText().toString().trim();
                mob = con_mob.getText().toString().trim();
                alternate = con_alternate.getText().toString().trim();
                fax = con_fax.getText().toString().trim();
                skype = con_skype.getText().toString().trim();
                twitter = con_twitter.getText().toString().trim();
                int def = active.isChecked() ? 1 : 0;
                if (editContact_pi.checkValidation(nm, email, mob, alternate)) {
                    if (contactData == null) {
                        /*if (nm.equalsIgnoreCase("self")) {
                            setNameError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.self_validation_msg));
                            return;
                        }*/
                        ClientContactAddEdit_Model addcontact = new ClientContactAddEdit_Model(App_preference.getSharedprefInstance().getLoginRes().getCompId(), key, nm, email, mob, alternate, fax, skype,
                                twitter, def, 0, jtIdList);

                        editContact_pi.AddNewClientContact(addcontact);
                    } else {
                     /*   if (!selfContact) {
                            if (nm.equalsIgnoreCase("self")) {
                                setNameError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.self_validation_msg));
                                return;
                            }
                        }*/
                        ClientContactEdit_Model contactEdit = new ClientContactEdit_Model(contactData.getCltId(), contactData.getConId(), nm, email, mob, alternate, fax, twitter, skype, def, 0
                                , jtIdList);
                        editContact_pi.EditClientContact(contactEdit, contactData.getCltId());
                    }
                }
        }
    }

    @Override
    public void EditContactviewUI(ContactData contactData) {
        if (contactData != null) {
            con_name.setText(contactData.getCnm());
            con_email.setText(contactData.getEmail());
            if (contactData.getMob1() != null && !contactData.getMob1().equals(""))
                con_mob.setText(contactData.getMob1());
            else
                con_mob.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
            if (contactData.getMob2() != null && !contactData.getMob2().equals(""))
                con_alternate.setText(contactData.getMob2());
            else
                con_alternate.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
            con_skype.setText(contactData.getSkype());
            con_twitter.setText(contactData.getTwitter());
            con_fax.setText(contactData.getFax());
            if (contactData.getDef().equals("1")) {
                active.setChecked(true);
                active.setEnabled(false);
            }
            /***Eye04013 ***/
            /**Eye08211 */
         /*   if (contactData.getCnm().equalsIgnoreCase("self")) {
                con_name.setEnabled(false);
                selfContact = true;
            }*/


            Set<String> data = new HashSet<>();
            try {
                if (contactData.getSiteId() != null) {
                    for (SiteId jtId : contactData.getSiteId()) {
                        data.add(jtId.getSnm());
                        jtIdList.add(jtId.getSiteId());
                    }
                }
            } catch (Exception ex) {
                ex.getMessage();
            }

//        quote_title_spinner.performClick();
            contactSiteSelected(data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showErrorDialog(String msg) {
        AppUtility.error_Alert_Dialog(this, msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });

    }

    @Override
    public void setNameError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void setEmailError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void setMobError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void setAlterNateError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void setResultForChangeInContact(String edit, String conId) {
        Intent intent = new Intent();
        intent.putExtra("conId", conId);
        if (edit.equals("EditContact"))
            EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.contact_updated_successfully));
        setResult(Client_Contact_List.EDITCONTACT, intent);
//        if (edit.equals("NewContact"))
//            setResult(Client_Contact_List.ADDCONTACT, intent);
        finish();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() >= 1) {
            if (charSequence.hashCode() == con_name.getText().hashCode())
                input_layout_name.setHintEnabled(true);
            if (charSequence.hashCode() == con_email.getText().hashCode())
                input_layout_con_email.setHintEnabled(true);
            if (charSequence.hashCode() == con_mob.getText().hashCode()) {
                input_layout_mobile.setHintEnabled(true);
                //    con_mob.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode() + " ");
            }
            if (charSequence.hashCode() == con_alternate.getText().hashCode())
                input_layout_Alternate.setHintEnabled(true);
            if (charSequence.hashCode() == con_fax.getText().hashCode())
                input_layout_Fax.setHintEnabled(true);
            if (charSequence.hashCode() == con_skype.getText().hashCode())
                input_layout_skype.setHintEnabled(true);
            if (charSequence.hashCode() == con_twitter.getText().hashCode())
                input_layout_twitter.setHintEnabled(true);

        } else if (charSequence.length() <= 0) {
            if (charSequence.hashCode() == con_name.getText().hashCode())
                input_layout_name.setHintEnabled(false);
            if (charSequence.hashCode() == con_email.getText().hashCode())
                input_layout_con_email.setHintEnabled(false);
            if (charSequence.hashCode() == con_mob.getText().hashCode()) {
                // con_mob.setTag("");
                input_layout_mobile.setHintEnabled(false);
            }
            if (charSequence.hashCode() == con_alternate.getText().hashCode())
                input_layout_Alternate.setHintEnabled(false);
            if (charSequence.hashCode() == con_fax.getText().hashCode())
                input_layout_Fax.setHintEnabled(false);
            if (charSequence.hashCode() == con_skype.getText().hashCode())
                input_layout_skype.setHintEnabled(false);
            if (charSequence.hashCode() == con_twitter.getText().hashCode())
                input_layout_twitter.setHintEnabled(false);
           /* if (charSequence.hashCode() == edt_lat.getText().hashCode())
                contact_lat_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_lng.getText().hashCode())
                contact_lng_layout.setHintEnabled(false);*/
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
