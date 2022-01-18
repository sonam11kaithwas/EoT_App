package com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip;

import static com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.AddEditInvoiceItemActivity2.EQUIPMENTCONVERT;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.FileProvider;

import com.eot_app.R;
import com.eot_app.UploadDocumentActivity;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.audit.audit_list.documents.ActivityEditImageDialog;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.EquipmentStatus;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterAdapterSites;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterCountry;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterStates;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.nav_menu.jobs.job_detail.chat.img_crop_pkg.ImageCropFragment;
import com.eot_app.nav_menu.jobs.job_detail.documents.PathUtils;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.adpter_pkg.BrandAdapter;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.adpter_pkg.CateAdpter;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.adpter_pkg.GrpAdpter;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.adpter_pkg.SiteAdpter;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.clientEqu.ClientEquRes;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.model_pkg.AddEquReq;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.model_pkg.BrandData;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.model_pkg.GetCatgData;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.model_pkg.GetgrpData;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.mvp.AddJobEqu_Pc;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.mvp.AddJobEqu_Pi;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.mvp.AddJobEqu_View;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.CompressImageInBack;
import com.eot_app.utility.Country;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.States;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.MySpinnerAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

public class AddJobEquipMentActivity extends UploadDocumentActivity implements TextWatcher, AddJobEqu_View, View.OnClickListener
        , RadioGroup.OnCheckedChangeListener, CheckBox.OnCheckedChangeListener, ImageCropFragment.MyDialogInterface
        , Spinner.OnItemSelectedListener {
    private final static int CAPTURE_IMAGE_GALLARY = 222;
    private static final int PHOTO_EDIT_CODE = 147;
    private final int CAMERA_CODE = 100;
    private final int ATTACHFILE_CODE = 1021;
    String path = "";
    String type = "2";
    int onClicked = 0;
    String ClientNm;
    CompressImageInBack compressImageInBack = null;
    private String brandId = "", siteId = "", status = "", invId;
    private CheckBox ch_equ_as_part;
    private TextInputLayout auto_barnd_layout, equ_layout, equ_model_layout, equ_supplier_layout, equ_serial_layout, job_country_layout, job_state_layout, equ_city_layout, equ_zip_layout, quote_notes_layout, catery_layout, grp_layout, equ_adrs_layout, client_site_layout, custom_filed_header_1, custom_filed_header_2;//equ_brand_layout
    private Button add_edit_item_Btn;
    private TextView manuf_date_lable, warnty_date_lable, purchase_date_lable, status_type, txt_date, txt_date_warnty, txt_date_purchase, tv_label_attachment, upload_lable, click_here_txt, hint_status_txt, status_txt;
    /**
     * select date from picker & concanate current time
     */
    private final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            String dateselect = "";
            try {
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);//hh:mm:ss a
                Date startDate = formatter.parse(selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear);
                dateselect = formatter.format(startDate);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 5, 0, -20);


                if (view.getTag().equals("Manufacture")) {
                    Log.e("", "");
                    setManufactureViews(dateselect, params);
                } else if (view.getTag().equals("Warranty")) {
                    Log.e("", "");
                    setWarrntyViews(dateselect, params);
                } else if (view.getTag().equals("Purchase")) {
                    Log.e("", "");
                    setPurchaseViews(dateselect, params);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };
    private RelativeLayout image_with_tag;
    private boolean isFileImage;
    private boolean isTagSet = false;
    private RadioGroup rediogrpForTag, rediogrp;
    private RadioButton radio_before, radio_after;
    private View client_row, site_row;
    private Spinner status_Dp;
    private EditText edt_equ, edt_equ_model, edt_equ_supplier, edt_equ_serial, edt_equ_adrs, edt_equ_city, edt_equ_zip, quote_notes_edt;//edt_equ_brand
    private AutoCompleteTextView auto_country, auto_states, auto_catery, auto_grp, auto_brand, auto_client_site;
    private String ctry, state;
    private EditText auto_client;
    private AppCompatImageView img_attachment;
    private TextView image_txt, remove_txt;
    private AddJobEqu_Pi addJobEqu_pi;
    private RadioButton radio_owner, radio_serv_prov;
    private CheckBox checkbox_barCode;
    private String isBarcodeGenerate = "0", isPart = "0";
    private String egId = "";
    private String ecId = "";
    private String jobId = "";
    private LinearLayout manuf_date_layout, date_purchase_layout, date_warnty_layout, linearLayout_status;
    private String captureImagePath;
    private LinearLayout lay;
    private Job job;
    private InvoiceItemDataModel updateItemDataModel;
    private List<GetCatgData> GetCatgDataList;
    private List<GetgrpData> GetgrpDataList;
    private List<BrandData> BrandDataList;
    private List<ClientEquRes> clientEquResList;
    private List<Site_model> clientSiteList = new ArrayList<>();
    private List<EquipmentStatus> equipmentStatusList;
    private RelativeLayout add_quote_item_layout;
    private TextInputLayout client_layout;
    private String cltId;
    private EditText custom_filed_txt_1, custom_filed_txt_2;
    /**
     * add equipment from audit list
     */
    private AuditList_Res audit;

    private void setPurchaseViews(String dateselect, LinearLayout.LayoutParams params) {
        purchase_date_lable.setText(dateselect);
        txt_date_purchase.setLayoutParams(params);
        txt_date_purchase.setVisibility(View.VISIBLE);
        txt_date_purchase.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.purchase_date));
    }

    private void setWarrntyViews(String dateselect, LinearLayout.LayoutParams params) {
        warnty_date_lable.setText(dateselect);
        txt_date_warnty.setLayoutParams(params);
        txt_date_warnty.setVisibility(View.VISIBLE);
        txt_date_warnty.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.warranty_expiry_date));
    }

    private void setManufactureViews(String dateselect, LinearLayout.LayoutParams params) {
        manuf_date_lable.setText(dateselect);
        txt_date.setLayoutParams(params);
        txt_date.setVisibility(View.VISIBLE);
        txt_date.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.manufacture_date));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job_equip_ment);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_add_equipment));

        initializeViewS();
    }


    private void initializeViewS() {

        custom_filed_header_1 = findViewById(R.id.custom_filed_header_1);
        custom_filed_header_2 = findViewById(R.id.custom_filed_header_2);
        custom_filed_txt_1 = findViewById(R.id.custom_filed_txt_1);
        custom_filed_txt_2 = findViewById(R.id.custom_filed_txt_2);
        if (App_preference.getSharedprefInstance().getCompanySettingsDetails().getEqupExtraField1Label() != null)
            custom_filed_txt_1.setHint(App_preference.getSharedprefInstance().getCompanySettingsDetails().getEqupExtraField1Label());

        if (App_preference.getSharedprefInstance().getCompanySettingsDetails().getEqupExtraField2Label() != null)
            custom_filed_txt_2.setHint(App_preference.getSharedprefInstance().getCompanySettingsDetails().getEqupExtraField2Label());


        site_row = findViewById(R.id.site_row);
        client_row = findViewById(R.id.client_row);
        client_layout = findViewById(R.id.client_layout);
        auto_client = findViewById(R.id.auto_client);
        auto_client.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.client_mand) + " *");

        add_quote_item_layout = findViewById(R.id.add_quote_item_layout);
        AppUtility.setupUI(add_quote_item_layout, AddJobEquipMentActivity.this);

        equ_layout = findViewById(R.id.equ_layout);
        equ_model_layout = findViewById(R.id.equ_model_layout);
        equ_supplier_layout = findViewById(R.id.equ_supplier_layout);
        // equ_brand_layout = findViewById(R.id.equ_brand_layout);
        equ_serial_layout = findViewById(R.id.equ_serial_layout);
        job_country_layout = findViewById(R.id.job_country_layout);
        job_state_layout = findViewById(R.id.job_state_layout);

        auto_country = findViewById(R.id.auto_country);
        auto_country.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.country));

        auto_states = findViewById(R.id.auto_states);
        auto_states.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.state));

        equ_city_layout = findViewById(R.id.equ_city_layout);
        equ_zip_layout = findViewById(R.id.equ_zip_layout);
        quote_notes_layout = findViewById(R.id.quote_notes_layout);
        equ_adrs_layout = findViewById(R.id.equ_adrs_layout);

        edt_equ = findViewById(R.id.edt_equ);
        edt_equ.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.equipment) + " *");

        edt_equ_model = findViewById(R.id.edt_equ_model);
        edt_equ_model.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.model_no));

        edt_equ_supplier = findViewById(R.id.edt_equ_supplier);
        edt_equ_supplier.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.supplier));
        //  edt_equ_brand = findViewById(R.id.edt_equ_brand);
        edt_equ_serial = findViewById(R.id.edt_equ_serial);
        edt_equ_serial.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.serial_no));

        edt_equ_adrs = findViewById(R.id.edt_equ_adrs);
        edt_equ_adrs.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.address));

        edt_equ_city = findViewById(R.id.edt_equ_city);
        edt_equ_city.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.city));

        edt_equ_zip = findViewById(R.id.edt_equ_zip);
        edt_equ_zip.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.zip));

        quote_notes_edt = findViewById(R.id.quote_notes_edt);
        quote_notes_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.notes));

        add_edit_item_Btn = findViewById(R.id.add_edit_item_Btn);
        add_edit_item_Btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_add_equipment));

        manuf_date_lable = findViewById(R.id.manuf_date_lable);
        manuf_date_lable.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.manufacture_date));

        warnty_date_lable = findViewById(R.id.warnty_date_lable);
        warnty_date_lable.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.warranty_expiry_date));

        purchase_date_lable = findViewById(R.id.purchase_date_lable);
        purchase_date_lable.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.purchase_date));

        catery_layout = findViewById(R.id.catery_layout);


        auto_catery = findViewById(R.id.auto_catery);
        auto_catery.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.equipment_category));

        grp_layout = findViewById(R.id.grp_layout);
        auto_grp = findViewById(R.id.auto_grp);
        auto_grp.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.equipment_group));

        auto_barnd_layout = findViewById(R.id.auto_barnd_layout);

        auto_brand = findViewById(R.id.auto_brand);
        auto_brand.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.brand));


        radio_owner = findViewById(R.id.radio_owner);
        radio_owner.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.owner));

        radio_serv_prov = findViewById(R.id.radio_serv_prov);
        radio_serv_prov.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.serv_prov));

        checkbox_barCode = findViewById(R.id.checkbox_barCode);
        checkbox_barCode.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.gen_bar_code));

        status_type = findViewById(R.id.type);
        status_type.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.type));

        txt_date = findViewById(R.id.txt_date);
        txt_date_warnty = findViewById(R.id.txt_date_warnty);
        txt_date_purchase = findViewById(R.id.txt_date_purchase);

        manuf_date_layout = findViewById(R.id.manuf_date_layout);
        date_purchase_layout = findViewById(R.id.date_purchase_layout);
        date_warnty_layout = findViewById(R.id.date_warnty_layout);

        tv_label_attachment = findViewById(R.id.tv_label_attachment);
        tv_label_attachment.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_images));

        lay = findViewById(R.id.lay);

        upload_lable = findViewById(R.id.upload_lable);
        upload_lable.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_upload));

        click_here_txt = findViewById(R.id.click_here_txt);
        click_here_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.doc_here));

        img_attachment = findViewById(R.id.img_attachment);
        ch_equ_as_part = findViewById(R.id.ch_equ_as_part);
        ch_equ_as_part.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.equ_as_a_part));

        client_site_layout = findViewById(R.id.client_site_layout);

        auto_client_site = findViewById(R.id.auto_client_site);
        auto_client_site.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.site_name));

        status_Dp = findViewById(R.id.status_Dp);
        hint_status_txt = findViewById(R.id.hint_status_txt);
        hint_status_txt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.status_radio_btn));

        status_txt = findViewById(R.id.status_txt);
        status_txt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.status_radio_btn));

        linearLayout_status = findViewById(R.id.linearLayout_status);


        image_with_tag = findViewById(R.id.image_with_tag);
        image_txt = findViewById(R.id.image_txt);

        remove_txt = findViewById(R.id.remove_txt);
        remove_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.remove_the_image_tag));
        remove_txt.setOnClickListener(this);


        rediogrp = findViewById(R.id.rediogrp);
        rediogrp.setOnCheckedChangeListener(this);

        radio_before = findViewById(R.id.radio_before);
        radio_before.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.before));
        radio_before.setChecked(false);

        radio_after = findViewById(R.id.radio_after);
        radio_after.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.after));
        radio_after.setChecked(false);

        rediogrpForTag = findViewById(R.id.rediogrpForTag);
        rediogrpForTag.setOnCheckedChangeListener(this);


        addJobEqu_pi = new AddJobEqu_Pc(this);
        addJobEqu_pi.getCageryList();
        addJobEqu_pi.getGrpList();
        addJobEqu_pi.getCountryList();
        addJobEqu_pi.getBrandList();
        addJobEqu_pi.getEquStatusList();


        /**get Item Data For Item to equipment convert*****/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (getIntent().hasExtra("InvoiceItemDataModel")) {
                jobId = bundle.getString("edit_jobId");
                invId = bundle.getString("invId");
                //updateItemDataModel = bundle.getParcelable("InvoiceItemDataModel");
                String string = bundle.getString("objectStr");
                updateItemDataModel = new Gson().fromJson(string, InvoiceItemDataModel.class);
                setItemDefaultData();
            } else if (getIntent().hasExtra("auditId")) {
                cltId = getIntent().getStringExtra("cltId");

                audit = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().getAuditsEquipmentList(getIntent().getStringExtra("auditId"));
                if (audit != null) {
                    siteId = audit.getSiteId();
                    if (audit.getCltId().equals("0")) {
                        setCompanySettingAdrs();
                    } else {
                        addJobEqu_pi.getClientSiteListServer(cltId);
                        setAuditdata(audit);
                    }
                }


            } else {
                /**add new Equipment***/
                if (getIntent().hasExtra("JobId")) {
                    jobId = getIntent().getStringExtra("JobId");
                    cltId = getIntent().getStringExtra("cltId");
                    job = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(jobId);
                    addJobEqu_pi.getClientSiteListServer(cltId);
                    siteId = job.getSiteId();
                    setJobData(job);

                }
            }
            job = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(jobId);
        }


        setTextViews();
    }

    private void setJobData(Job job) {
        try {
            if (job != null) {
                auto_client.setText(job.getNm());
                auto_country.setText(SpinnerCountrySite.getCountryNameById(job.getCtry()));
                auto_states.setText(SpinnerCountrySite.getStatenameById(job.getCtry(), job.getState()));
                edt_equ_city.setText(job.getCity());
                edt_equ_adrs.setText(job.getAdr());
                edt_equ_zip.setText(job.getZip());
                auto_client_site.setText(job.getSnm());

                client_site_layout.setHintEnabled(true);
                client_layout.setHintEnabled(true);
                job_country_layout.setHintEnabled(true);
                client_layout.setHintEnabled(true);
                job_state_layout.setHintEnabled(true);
                equ_adrs_layout.setHintEnabled(true);
                equ_city_layout.setHintEnabled(true);
                equ_zip_layout.setHintEnabled(true);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void setAuditdata(AuditList_Res audit) {
        if (audit != null) {
            auto_client.setText(audit.getNm());
            auto_country.setText(SpinnerCountrySite.getCountryNameById(audit.getCtry()));
            auto_states.setText(SpinnerCountrySite.getStatenameById(audit.getCtry(), audit.getState()));
            edt_equ_city.setText(audit.getCity());
            edt_equ_adrs.setText(audit.getAdr());
            edt_equ_zip.setText(audit.getZip());
            auto_client_site.setText(audit.getSnm());

            client_site_layout.setHintEnabled(true);
            client_layout.setHintEnabled(true);
            job_country_layout.setHintEnabled(true);
            client_layout.setHintEnabled(true);
            job_state_layout.setHintEnabled(true);
            equ_adrs_layout.setHintEnabled(true);
            equ_city_layout.setHintEnabled(true);
            equ_zip_layout.setHintEnabled(true);
        }
    }

    private void setItemDefaultData() {
        try {
            if (updateItemDataModel != null) {
                edt_equ.setText(updateItemDataModel.getInm());
                edt_equ_model.setText(updateItemDataModel.getPno());
                edt_equ_supplier.setText(updateItemDataModel.getSupplierCost());
                edt_equ_serial.setText(updateItemDataModel.getSerialNo());


                if (jobId != null) {
                    job = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(jobId);
                    if (job != null) {
                        if (job.getCltId() != null)
                            addJobEqu_pi.getClientSiteListServer(job.getCltId());
                        if (job.getSiteId() != null)
                            siteId = job.getSiteId();
                        setJobData(job);
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void setTextViews() {

        // edt_equ_brand.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.brand));

        textChangeListner();

        emptyBrandCheck();
        emptyGroupCheck();
        emptyCatryCheck();
    }

    private void emptyGroupCheck() {
        auto_grp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                auto_grp.requestFocus();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (GetgrpDataList != null && GetgrpDataList.size() > 0) {
                        if (event.getRawX() >= (auto_grp.getRight() - auto_grp.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            if (onClicked == 0) {
                                hideKeybaord(auto_grp);
                                auto_grp.showDropDown();
                                onClicked = 1;
                                return true;
                            } else {
                                hideKeybaord(auto_grp);
                                auto_grp.dismissDropDown();
                                onClicked = 0;
                                return true;
                            }

                        } else {
                            auto_grp.showDropDown();
                            showSoftKeyboard(auto_grp);
                            return true;
                        }
                    } else {
                        auto_grp.setInputType(InputType.TYPE_NULL);
                        AppUtility.alertDialog(AddJobEquipMentActivity.this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_empty_equipment_Group), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return null;
                            }
                        });
                    }
                }

                return false;
            }
        });
    }

    private void emptyCatryCheck() {
        auto_catery.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                auto_catery.requestFocus();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (GetCatgDataList != null && GetCatgDataList.size() > 0) {
                        if (event.getRawX() >= (auto_catery.getRight() - auto_catery.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            if (onClicked == 0) {
                                hideKeybaord(auto_catery);
                                auto_catery.showDropDown();
                                onClicked = 1;
                                return true;
                            } else {
                                hideKeybaord(auto_catery);
                                auto_catery.dismissDropDown();
                                onClicked = 0;
                                return true;
                            }

                        } else {
                            auto_catery.showDropDown();
                            showSoftKeyboard(auto_catery);
                            return true;
                        }
                    } else {
                        auto_catery.setInputType(InputType.TYPE_NULL);
                        AppUtility.alertDialog(AddJobEquipMentActivity.this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_empty_equipment_Category), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return null;
                            }
                        });
                    }
                }

                return false;
            }
        });
    }

    private void emptyBrandCheck() {
        auto_brand.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                auto_brand.requestFocus();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (BrandDataList != null && BrandDataList.size() > 0) {
                        if (event.getRawX() >= (auto_brand.getRight() - auto_brand.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            if (onClicked == 0) {
                                auto_brand.showDropDown();
                                onClicked = 1;
                                return true;
                            } else {
                                hideKeybaord(auto_brand);
                                auto_brand.dismissDropDown();
                                onClicked = 0;
                                return true;
                            }
                        } else {
                            auto_brand.showDropDown();
                            showSoftKeyboard(auto_brand);
                            return true;
                        }

                    } else {
                        auto_brand.setInputType(InputType.TYPE_NULL);
                        AppUtility.alertDialog(AddJobEquipMentActivity.this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_empty_equipment_Brand), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return null;
                            }
                        });
                    }
                }

                return false;
            }

        });
    }

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    public void showSoftKeyboard(View view) {
        if (view.isFocused()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void textChangeListner() {
        equ_layout.getEditText().addTextChangedListener(this);
        equ_model_layout.getEditText().addTextChangedListener(this);
        equ_supplier_layout.getEditText().addTextChangedListener(this);
        //  equ_brand_layout.getEditText().addTextChangedListener(this);
        equ_serial_layout.getEditText().addTextChangedListener(this);
        equ_city_layout.getEditText().addTextChangedListener(this);
        equ_zip_layout.getEditText().addTextChangedListener(this);
        quote_notes_layout.getEditText().addTextChangedListener(this);
        equ_adrs_layout.getEditText().addTextChangedListener(this);
        custom_filed_header_1.getEditText().addTextChangedListener(this);
        custom_filed_header_2.getEditText().addTextChangedListener(this);

        catery_layout.setOnClickListener(this);
        grp_layout.setOnClickListener(this);

        auto_catery.setOnClickListener(this);
        auto_grp.setOnClickListener(this);
        auto_brand.setOnClickListener(this);


        manuf_date_layout.setOnClickListener(this);
        date_purchase_layout.setOnClickListener(this);
        date_warnty_layout.setOnClickListener(this);

        add_edit_item_Btn.setOnClickListener(this);

        rediogrp.setOnCheckedChangeListener(this);
        checkbox_barCode.setOnCheckedChangeListener(this);
        ch_equ_as_part.setOnCheckedChangeListener(this);
//        upload_lable.setOnClickListener(this);
        lay.setOnClickListener(this);
        client_site_layout.setOnClickListener(this);

        auto_client_site.setOnClickListener(this);

        status_Dp.setOnItemSelectedListener(this);
        linearLayout_status.setOnClickListener(this);

    }


    @Override
    public void setEquStatusList(final List<EquipmentStatus> equipmentStatusList) {
        if (equipmentStatusList != null && equipmentStatusList.size() > 0) {
            AppUtility.spinnerPopUpWindow(status_Dp);
            this.equipmentStatusList = equipmentStatusList;
            if (equipmentStatusList != null) {
                String[] statusList = new String[equipmentStatusList.size()];
                int i = 0;
                for (EquipmentStatus status : equipmentStatusList) {
                    statusList[i] = status.getStatusText();
                    i++;
                    if (status.getStatusText().equals("Deployed")) {
                        /***set Default Equipment Status DEPLOYED****/
                        setDefaultEquStatus(status);
                    }
                }

                status_Dp.setAdapter(new MySpinnerAdapter(this, statusList));
            }
        }
    }


    private void setDefaultEquStatus(EquipmentStatus statusModel) {
        hint_status_txt.setVisibility(View.VISIBLE);
        status = statusModel.getEsId();
        String selectedValue = statusModel.getStatusText();
        status_txt.setText(selectedValue);
    }

    private void setEquStatus(int pos) {
        hint_status_txt.setVisibility(View.VISIBLE);
        status = equipmentStatusList.get(pos).getEsId();
        String selectedValue = equipmentStatusList.get(pos).getStatusText();
        status_txt.setText(selectedValue);
    }

    @Override
    public void setClientSiteList(List<Site_model> siteModelList) {
        if (siteModelList != null && siteModelList.size() > 0) {
            AppUtility.autocompletetextviewPopUpWindow(auto_client_site);
            this.clientSiteList = siteModelList;
            auto_client_site.setText(siteModelList.get(0).getSnm());
            siteId = siteModelList.get(0).getSiteId();
            client_site_layout.setHintEnabled(true);

            FilterAdapterSites filterSites = new FilterAdapterSites(this,
                    R.layout.custom_adpter_item_layout_new, (ArrayList<Site_model>) siteModelList);
            auto_client_site.setAdapter(filterSites);
            auto_client_site.setThreshold(1);
            auto_client_site.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    siteId = ((Site_model) adapterView.getItemAtPosition(i)).getSiteId();
                    client_site_layout.setHintEnabled(true);
                }
            });

            auto_client_site.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() >= 1) {
                        client_site_layout.setHintEnabled(true);
                    } else if (charSequence.length() <= 0) {
                        client_site_layout.setHintEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    @Override
    public void setClientSiteListServer(List<ClientEquRes> clientEquRes) {
        this.clientEquResList = clientEquRes;

        final SiteAdpter siteAdpter = new SiteAdpter(this, R.layout.custom_adpter_item_layout_new,
                (ArrayList<ClientEquRes>) clientEquRes);
        auto_client_site.setAdapter(siteAdpter);
        auto_client_site.setThreshold(1);
        auto_client_site.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                siteId = ((ClientEquRes) adapterView.getItemAtPosition(i)).getSiteId();
                client_site_layout.setHintEnabled(true);
            }
        });

        auto_client_site.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    client_site_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    client_site_layout.setHintEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.status_Dp:
                setEquStatus(i);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void removeTagData() {
        isTagSet = false;
        image_txt.setVisibility(View.GONE);
        remove_txt.setVisibility(View.GONE);
        rediogrpForTag.setVisibility(View.VISIBLE);
        radio_after.setChecked(false);
        radio_before.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remove_txt:
                removeTagData();
                break;
            case R.id.client_site_layout:
                auto_client_site.showDropDown();
                break;
            case R.id.linearLayout_status:
                status_Dp.performClick();
                break;
            case R.id.lay:
                selectFile(true);
                break;
            case R.id.add_edit_item_Btn:
                if (isTagSet) {
                    processImageAndUpload();
                } else {
                    createEquipmentForJobAudit();
                }
                break;
            case R.id.auto_catery:
                if (GetCatgDataList != null && GetCatgDataList.size() > 0)
                    auto_catery.showDropDown();
                break;
            case R.id.auto_client_site:
                if (clientSiteList != null && clientSiteList.size() > 0)
                    auto_client_site.showDropDown();
                break;
            case R.id.auto_grp:
                if (GetgrpDataList != null && GetgrpDataList.size() > 0)
                    auto_grp.showDropDown();
                break;
            case R.id.auto_brand:
                if (BrandDataList != null && BrandDataList.size() > 0)
                    auto_brand.showDropDown();
                break;
            case R.id.manuf_date_layout:
                SelectStartDate("Manufacture");
                break;
            case R.id.date_purchase_layout:
                SelectStartDate("Purchase");
                break;
            case R.id.date_warnty_layout:
                SelectStartDate("Warranty");
                break;

        }
    }

    private void createEquipmentForJobAudit() {
        add_edit_item_Btn.setEnabled(false);
        createEqquipmentRequest();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                add_edit_item_Btn.setEnabled(true);
            }
        }, 500);
    }

    public void createEqquipmentRequest() {

        String countryname, statename;
        countryname = auto_country.getText().toString();
        statename = auto_states.getText().toString();
        ctry = addJobEqu_pi.cntryId(countryname);
        state = addJobEqu_pi.statId(ctry, statename);

        if (updateItemDataModel == null) {
            if (addJobEqu_pi.RequiredFields(ctry, state, edt_equ.getText().toString().trim())) {
                if (audit != null) {
                    addJobEqu_pi.addNewEquipment(
                            new AddEquReq(type, egId, ecId, edt_equ_zip.getText().toString().trim(),
                                    edt_equ_city.getText().toString().trim(), edt_equ_adrs.getText().toString().trim(),
                                    ctry, state, isBarcodeGenerate,
                                    quote_notes_edt.getText().toString().trim(),
                                    purchase_date_lable.getText().toString().trim(),
                                    manuf_date_lable.getText().toString().trim(), warnty_date_lable.getText().toString().trim(),
                                    edt_equ_supplier.getText().toString().trim(), edt_equ_serial.getText().toString().trim(),
                                    edt_equ_model.getText().toString().trim(),
                                    brandId, edt_equ.getText().toString().trim(), audit.getAudId(), audit.getCltId(),
                                    audit.getContrId(), siteId, isPart, status, custom_filed_txt_1.getText().toString().trim(), custom_filed_txt_2.getText().toString().trim())
                            , path);
                } else {
                    addJobEqu_pi.addNewEquipment(
                            new AddEquReq(type, egId, ecId, edt_equ_zip.getText().toString().trim(),
                                    edt_equ_city.getText().toString().trim(), edt_equ_adrs.getText().toString().trim(),
                                    ctry, state, isBarcodeGenerate,
                                    quote_notes_edt.getText().toString().trim(),
                                    purchase_date_lable.getText().toString().trim(),
                                    manuf_date_lable.getText().toString().trim(), warnty_date_lable.getText().toString().trim(),
                                    edt_equ_supplier.getText().toString().trim(), edt_equ_serial.getText().toString().trim(),
                                    edt_equ_model.getText().toString().trim(),
                                    brandId, edt_equ.getText().toString().trim(), jobId, job.getCltId(),
                                    job.getContrId(), siteId, isPart, status, custom_filed_txt_1.getText().toString().trim(), custom_filed_txt_2.getText().toString().trim())
                            , path);
                }
            }
        } else {
            /*****Convert item to EQUIPMENT*****/
            if (addJobEqu_pi.RequiredFields(ctry, state, edt_equ.getText().toString().trim())) {
                addJobEqu_pi.convertItemToequip(new AddEquReq(type, egId, ecId, edt_equ_zip.getText().toString().trim(),
                        edt_equ_city.getText().toString().trim(), edt_equ_adrs.getText().toString().trim(),
                        ctry, state, isBarcodeGenerate,
                        quote_notes_edt.getText().toString().trim(),
                        purchase_date_lable.getText().toString().trim(),
                        manuf_date_lable.getText().toString().trim(), warnty_date_lable.getText().toString().trim(),
                        edt_equ_supplier.getText().toString().trim(), edt_equ_serial.getText().toString().trim(),
                        edt_equ_model.getText().toString().trim(),
                        brandId, edt_equ.getText().toString().trim(), jobId, job.getCltId(),
                        job.getContrId(), updateItemDataModel.getItemId(),
                        updateItemDataModel.getRate(), siteId, isPart, status, invId, custom_filed_txt_1.getText().toString().trim(), custom_filed_txt_2.getText().toString().trim()), path);
            }
        }


    }


    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void sessionExpire(String msg) {
        AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title)
                , msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "",
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        EotApp.getAppinstance().sessionExpired();
                        return null;
                    }
                });
    }

    @Override
    public void addExpenseSuccesFully(String msg) {
        EotApp.getAppinstance().showToastmsg(msg);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        /****Refresh list for Appoinment*****/
        finish();
    }

    @Override
    public void convertEquipment(String msg) {
        EotApp.getAppinstance().showToastmsg(msg);
        Intent intent = new Intent();
        setResult(EQUIPMENTCONVERT, intent);
        finish();
    }


    @Override
    public void setCountryError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void setStateError(String msg) {
        showErrorDialog(msg);
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

    private void setCompanySettingAdrs() {
        client_layout.setVisibility(View.GONE);
        client_row.setVisibility(View.GONE);
        client_site_layout.setVisibility(View.GONE);
        site_row.setVisibility(View.GONE);

        auto_country.setText(SpinnerCountrySite.getCountryNameById(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry()));
        /***Important ******/
        //   addJobEqu_pi.getStateList((App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry()));
        auto_states.setText(SpinnerCountrySite.getStatenameById(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry(), App_preference.getSharedprefInstance().getCompanySettingsDetails().getState()));
        edt_equ_city.setText(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCity());
        edt_equ_city.setText(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCity());


        //state = (App_preference.getSharedprefInstance().getCompanySettingsDetails().getState());
        // ctry = (App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry());

        job_country_layout.setHintEnabled(true);
        job_state_layout.setHintEnabled(true);
        equ_adrs_layout.setHintEnabled(true);
        equ_city_layout.setHintEnabled(true);
        equ_zip_layout.setHintEnabled(true);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkbox_barCode:
                isBarcodeGenerate = buttonView.isChecked() ? "1" : "0";
                break;
            case R.id.ch_equ_as_part:
                isPart = buttonView.isChecked() ? "1" : "0";
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_before:
                isTagSet = true;
                image_txt.setVisibility(View.VISIBLE);
                image_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.before));
                rediogrpForTag.setVisibility(View.GONE);
                remove_txt.setVisibility(View.VISIBLE);
                break;
            case R.id.radio_after:
                isTagSet = true;
                image_txt.setVisibility(View.VISIBLE);
                image_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.after));
                rediogrpForTag.setVisibility(View.GONE);
                remove_txt.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return true;
    }


    @Override
    public void setCategList(List<GetCatgData> cateList) {
        this.GetCatgDataList = cateList;

        final CateAdpter countryAdapter = new CateAdpter(this, R.layout.custom_adpter_item_layout_new,
                (ArrayList<GetCatgData>) cateList);
        auto_catery.setAdapter(countryAdapter);
        auto_catery.setThreshold(1);
        auto_catery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ecId = ((GetCatgData) adapterView.getItemAtPosition(i)).getEcId();
                // catery_layout.setHintEnabled(true);
            }
        });

        auto_catery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    catery_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    catery_layout.setHintEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void setBrandList(List<BrandData> brandList) {
        this.BrandDataList = brandList;

        final BrandAdapter countryAdapter = new BrandAdapter(this, R.layout.custom_adpter_item_layout_new,
                (ArrayList<BrandData>) brandList);
        auto_brand.setAdapter(countryAdapter);
        auto_brand.setThreshold(1);
        auto_brand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //     egId = ((GetgrpData) adapterView.getItemAtPosition(i)).getEgId();
//                addJobEqu_pi.getStateList(((Country) adapterView.getItemAtPosition(i)).getId());
                brandId = ((BrandData) adapterView.getItemAtPosition(i)).getEbId();
                auto_barnd_layout.setHintEnabled(true);
            }
        });

        auto_brand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    auto_barnd_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    auto_barnd_layout.setHintEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void setGrpList(List<GetgrpData> cateList) {
        this.GetgrpDataList = cateList;

        final GrpAdpter countryAdapter = new GrpAdpter(this, R.layout.custom_adpter_item_layout_new,
                (ArrayList<GetgrpData>) cateList);
        auto_grp.setAdapter(countryAdapter);
        auto_grp.setThreshold(1);
        auto_grp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                egId = ((GetgrpData) adapterView.getItemAtPosition(i)).getEgId();
//                addJobEqu_pi.getStateList(((Country) adapterView.getItemAtPosition(i)).getId());
                grp_layout.setHintEnabled(true);
            }
        });

        auto_grp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    grp_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    grp_layout.setHintEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void setCountryList(List<Country> countryList) {
//        AppUtility.autocompletetextviewPopUpWindow(job_country_layout);
        final FilterCountry countryAdapter = new FilterCountry(this, R.layout.custom_adapter_item_layout, (ArrayList<Country>) countryList);
        auto_country.setAdapter(countryAdapter);
        auto_country.setThreshold(1);
        auto_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ctry = ((Country) adapterView.getItemAtPosition(i)).getId();
                addJobEqu_pi.getStateList(((Country) adapterView.getItemAtPosition(i)).getId());
                job_country_layout.setHintEnabled(true);
            }
        });

        auto_country.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    job_country_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    job_country_layout.setHintEnabled(false);
                }
                auto_states.setText("");
                ctry = "";
                state = "";
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void setStateList(List<States> statesList) {
        //AppUtility.autocompletetextviewPopUpWindow(job_state_layout);
        FilterStates stateAdapter = new FilterStates(this, R.layout.custom_adapter_item_layout, (ArrayList<States>) statesList);
        auto_states.setAdapter(stateAdapter);
        auto_states.setThreshold(0);
        auto_states.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                state = ((States) adapterView.getItemAtPosition(i)).getId();
                job_state_layout.setHintEnabled(true);
            }
        });


        auto_states.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    job_state_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    job_state_layout.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (charSequence.length() >= 1) {
            if (charSequence.hashCode() == edt_equ.getText().hashCode())
                equ_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_equ_model.getText().hashCode())
                equ_model_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_equ_supplier.getText().hashCode())
                equ_supplier_layout.setHintEnabled(true);
//            if (charSequence.hashCode() == edt_equ_brand.getText().hashCode())
//                equ_brand_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_equ_serial.getText().hashCode())
                equ_serial_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_equ_city.getText().hashCode())
                equ_city_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_equ_zip.getText().hashCode())
                equ_zip_layout.setHintEnabled(true);
            if (charSequence.hashCode() == quote_notes_edt.getText().hashCode())
                quote_notes_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_equ_adrs.getText().hashCode())
                equ_adrs_layout.setHintEnabled(true);
            if (charSequence.hashCode() == custom_filed_txt_1.getText().hashCode())
                custom_filed_header_1.setHintEnabled(true);
            if (charSequence.hashCode() == custom_filed_txt_2.getText().hashCode())
                custom_filed_header_2.setHintEnabled(true);
        } else if (charSequence.length() <= 0) {
            /**Floating hint Disable after text enter**/
            if (charSequence.hashCode() == edt_equ.getText().hashCode())
                equ_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_equ_model.getText().hashCode())
                equ_model_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_equ_supplier.getText().hashCode())
                equ_supplier_layout.setHintEnabled(false);
//            if (charSequence.hashCode() == edt_equ_brand.getText().hashCode())
//                equ_brand_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_equ_serial.getText().hashCode())
                equ_serial_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_equ_city.getText().hashCode())
                equ_city_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_equ_zip.getText().hashCode())
                equ_zip_layout.setHintEnabled(false);
            if (charSequence.hashCode() == quote_notes_edt.getText().hashCode())
                quote_notes_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_equ_adrs.getText().hashCode())
                equ_adrs_layout.setHintEnabled(false);
            if (charSequence.hashCode() == custom_filed_txt_1.getText().hashCode())
                custom_filed_header_1.setHintEnabled(true);
            if (charSequence.hashCode() == custom_filed_txt_2.getText().hashCode())
                custom_filed_header_2.setHintEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void SelectStartDate(String tag) {
        Calendar myCalendar = Calendar.getInstance();
        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddJobEquipMentActivity.this, datePickerListener, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setTag(tag);
        datePickerDialog.show();
    }

    @Override
    public void setEquReqError(String msg) {
        showDialogs(msg);
    }

    private void showDialogs(String msg) {
        AppUtility.error_Alert_Dialog(this, msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    /*Picker for attachemnt*/
    private void selectAttachment() {
        final BottomSheetDialog dialog = new BottomSheetDialog(AddJobEquipMentActivity.this);
        dialog.setContentView(R.layout.bottom_image_chooser);
        TextView camera = dialog.findViewById(R.id.camera);
        camera.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.camera));
        TextView gallery = dialog.findViewById(R.id.gallery);
        gallery.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.gallery));
        LinearLayout driveLayout = dialog.findViewById(R.id.driveLayout);
        TextView drive_document = dialog.findViewById(R.id.drive_document);
        drive_document.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.document));
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtility.askCameraTakePicture(AddJobEquipMentActivity.this)) {
                    takePictureFromCamera();
                }
                dialog.dismiss();

            }

        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtility.askGalaryTakeImagePermiSsion(AddJobEquipMentActivity.this)) {
                    getImageFromGallray();
                }
                dialog.dismiss();

            }


        });

        drive_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtility.askGalaryTakeImagePermiSsion(AddJobEquipMentActivity.this)) {
                    takeimageFromGalary();//only for drive documents
                }
                dialog.dismiss();
            }
        });

        //    driveLayout.setVisibility(View.GONE);
        dialog.show();

    }


    private void takeimageFromGalary() {
        //allow upload file extension
        String[] mimeTypes = {"image/jpeg", "image/jpg", "image/png",
                "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",//.doc & .docx
                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",//.xls & .xlsx
                "application/pdf",//pdf
                "text/csv", "text/plain"//csv
        };

/**only for document uploading */
        Intent documentIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        documentIntent.addCategory(Intent.CATEGORY_OPENABLE);
        documentIntent.setType("*/*");
        documentIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        documentIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(documentIntent, ATTACHFILE_CODE);
    }


    private void getImageFromGallray() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, CAPTURE_IMAGE_GALLARY);
    }

    private void takePictureFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("", ex.getMessage());

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.eot_app.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                try {
                    startActivityForResult(takePictureIntent, CAMERA_CODE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    private File createImageFile() throws IOException {
        Calendar calendar = Calendar.getInstance();
        long imageFileName = calendar.getTime().getTime();

        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);// return path

        File directoryPath = new File(storageDir.getPath());
        File image = File.createTempFile(
                String.valueOf(imageFileName),  /* prefix */
                ".jpg",         /* suffix */
                directoryPath   /* directory */
        );
        captureImagePath = image.getAbsolutePath();
        return new File(image.getPath());
    }


    private void imageEditing(Uri uri, String dialogTag) {
        Intent intent = new Intent(AddJobEquipMentActivity.this, ActivityEditImageDialog.class);
        intent.putExtra("uri", uri);
        intent.putExtra("allowOffline", true);
        startActivityForResult(intent, PHOTO_EDIT_CODE);
    }

    private void imageCroping(final Uri uri) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageCropFragment myfragment = ImageCropFragment.newInstance("Uri", uri.toString());
                myfragment.setCallbackListener(AddJobEquipMentActivity.this);
                myfragment.show(getSupportFragmentManager().beginTransaction(), "AddJobEquipMentActivity");
            }
        }, 100);
    }


    private int getFileIcons(String serverFilePath) {
        int resId = 0;
        String ext = serverFilePath.substring((serverFilePath.lastIndexOf(".")) + 1);

        if (!ext.isEmpty()) {
            if (ext.equals("doc") || ext.equals("docx")) {
                resId = R.drawable.word;
            } else if (ext.equals("pdf")) {
                resId = R.drawable.pdf;
            } else if (ext.equals("xlsx") || ext.equals("xls")) {
                resId = R.drawable.excel;

            } else if (ext.equals("csv")) {
                resId = R.drawable.csv;
            } else {
                resId = R.drawable.doc;
            }
        }
        return resId;
    }


    @Override
    public void onClickContinuarEvent(Uri uri) {
        //  path = PathUtils.getPath(this, uri);
        path = PathUtils.getRealPath(this, uri);
        if (!path.isEmpty()) {
            File file = new File(path);
            if (file != null && file.exists()) {
                img_attachment.setVisibility(View.VISIBLE);
                Picasso.with(this).load(file).into(img_attachment);
                image_txt.setText("");
                remove_txt.setVisibility(View.GONE);
                radio_after.setChecked(false);
                radio_before.setChecked(false);
                rediogrpForTag.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onDocumentSelected(String paths, boolean isImage) {
        //   super.onDocumentSelected(paths, isImage);
        try {
            //   if (!path.isEmpty()) {
            path = paths;
            final String ext = path.substring(path.lastIndexOf("."));
            img_attachment.setVisibility(View.VISIBLE);

            if (ext.equals(".doc") || ext.equals(".docx")) {
                img_attachment.setImageResource(R.drawable.word);
                img_attachment.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else if (ext.equals(".pdf")) {
                img_attachment.setImageResource(R.drawable.pdf);
                img_attachment.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else if (ext.equals(".xlsx") || ext.equals(".xls")) {
                img_attachment.setImageResource(R.drawable.excel);
                img_attachment.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else if (ext.equals(".csv")) {
                img_attachment.setImageResource(R.drawable.csv);
                img_attachment.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                File file = new File(path);
                if (file != null && file.exists()) {
                    img_attachment.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(file).into(img_attachment);
                }
                img_attachment.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                rediogrpForTag.setVisibility(View.VISIBLE);
                radio_after.setChecked(false);
                radio_before.setChecked(false);
                image_txt.setText("");
                remove_txt.setVisibility(View.GONE);
                rediogrpForTag.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processImageAndUpload() {
        path = saveBitMap(image_with_tag);
        createEquipmentForJobAudit();
    }

    private String saveBitMap(View drawView) {

        File pictureFileDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }

        return filename;
    }

    private Bitmap getBitmapFromView(View view) {

        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }

        view.draw(canvas);
        return returnedBitmap;
    }


}