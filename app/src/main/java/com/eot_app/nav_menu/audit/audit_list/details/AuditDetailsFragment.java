package com.eot_app.nav_menu.audit.audit_list.details;

import static com.eot_app.nav_menu.jobs.job_detail.detail.DetailFragment.MY_PERMISSIONS_REQUEST_CALL_PHONE;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.eot_app.R;
import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.eoteditor.EotEditor;
import com.eot_app.eoteditor.PicassoImageGetter;
import com.eot_app.lat_lng_sync_pck.LatLngSycn_Controller;
import com.eot_app.locations.LocationTracker;
import com.eot_app.locations.OnLocationUpdate;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditStatusRequest;
import com.eot_app.nav_menu.audit.audit_list.details.audit_detail_mvp.AuditDetail_PC;
import com.eot_app.nav_menu.audit.audit_list.details.audit_detail_mvp.AuditDetails_View;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.custom_fileds.CustomFiledListActivity;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.AnswerModel;
import com.eot_app.nav_menu.jobs.job_detail.job_status_pkg.JobStatus_Controller;
import com.eot_app.nav_menu.jobs.joboffline_db.JobOverViewNotify;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.eot_app.utility.util_interfaces.MySpinnerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Mahendra Dabi on 7/11/19.
 */
public class AuditDetailsFragment extends Fragment implements View.OnClickListener, AuditDetails_View
        , JobOverViewNotify {

    public static final int AUDITCUSTOMFILED = 333;
    private final String[] arraystatus = {"Start", "On Hold", "Completed"};//"Job Break",
    LanguageController langInstance;
    //status
    ImageView status_img;
    TextView status;
    LocationTracker locationTracker;
    long check = System.currentTimeMillis();
    private FrameLayout frameView;
    private AuditDetail_PC auditDetail_pc;
    private ImageView imageViewChat, imageViewCall, imageViewEmail, imageView5;
    private View layout;
    private Button button_resume, button_start, button_on_hold, button_completed, buttonView, buttonMapView, buttonView1;
    private Spinner status_spinner;
    private TextView status_label, textViewJobCode, textViewTime, textViewPriority, textViewJobStatus, textViewAddress, textViewDescription, textViewContactperson, textViewTitle, textViewInstruction, txtViewHeader, txt_fw_nm_list, tv_location, tv_description, tv_instruction, tv_contact_name, fw_Nm_List;
    //custom dialog for instruction and details
    private Dialog enterFieldDialog;
    private LinearLayout audit_status_relative;
    private AuditList_Res audit;
    private LinearLayout ll_status;
    private int selectedAuditPosition = -1;
    private TextView tv_end_label, tv_start_label, tv_end_date, tv_start_date;
    private TextView tv_end_time, tv_start_time;
    private boolean isSpinnerActive;
    private ImageView attachmemt_flag, equi_flag;
    private CardView customField_view, des_card, card_instr;
    private TextView custom_filed_txt;
    private LinearLayout ll_custom_views;
    private Button customfiled_btn;
    private Boolean SAVEANS = false;
    private ArrayList<CustOmFormQuestionsRes> questionList = new ArrayList<>();

    public static AuditDetailsFragment getInstance(AuditList_Res audit, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        /**Important**/
        bundle.putParcelable("audit", audit);
        AuditDetailsFragment fragment = new AuditDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_audit_details, container, false);
        initializelables();
        setDataToView();
        EotApp.getAppinstance().setJobFlagObserver(this);
        return layout;
    }

    @Override
    public void updateJobOverViewFlag() {
        try {
            EotApp.getAppinstance().notifyApiObserver(Service_apis.addAppointment);
            audit = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().getAuditById(audit.getAudId());
            getArguments().putParcelable("audit", audit);
            flagVisibility();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void flagVisibility() {
        if (audit != null) {
            if (audit.getAttachCount() != null && !audit.getAttachCount().isEmpty() && Integer.parseInt(audit.getAttachCount()) > 0) {
                attachmemt_flag.setVisibility(View.VISIBLE);
            } else {
                attachmemt_flag.setVisibility(View.GONE);
            }

            if (audit.getEquArray() != null && audit.getEquArray().size() > 0) {
                equi_flag.setVisibility(View.VISIBLE);
            } else {
                equi_flag.setVisibility(View.GONE);
            }
        }
    }

    private void initializelables() {

        auditDetail_pc = new AuditDetail_PC(this);
        langInstance = LanguageController.getInstance();

        status_img = layout.findViewById(R.id.status_img);
        status = layout.findViewById(R.id.status);
        ll_status = layout.findViewById(R.id.ll_status);

        tv_start_label = layout.findViewById(R.id.tv_start_label);
        tv_end_label = layout.findViewById(R.id.tv_end_label);

        tv_start_date = layout.findViewById(R.id.tv_start_date);
        tv_end_date = layout.findViewById(R.id.tv_end_date);

        tv_start_time = layout.findViewById(R.id.tv_start_time);
        tv_end_time = layout.findViewById(R.id.tv_end_time);


        tv_start_label.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.start));
        tv_end_label.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.end));


        button_start = layout.findViewById(R.id.button_start);
        button_on_hold = layout.findViewById(R.id.button_on_hold);
        button_completed = layout.findViewById(R.id.button_completed);
        button_resume = layout.findViewById(R.id.button_resume);

        button_start.setText(langInstance.getMobileMsgByKey(AppConstant.start));
        button_completed.setText(langInstance.getMobileMsgByKey(AppConstant.completed));
//        button_on_hold.setText(langInstance.getMobileMsgByKey(AppConstant.on_hold));
        button_on_hold.setText(langInstance.getMobileMsgByKey(AppConstant.new_on_hold));
        button_resume.setText(langInstance.getMobileMsgByKey(AppConstant.resume));


        audit_status_relative = layout.findViewById(R.id.audit_status_relative);
        status_label = layout.findViewById(R.id.status_label);

        status_label.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.status_radio_btn));

        tv_start_date = layout.findViewById(R.id.tv_start_date);
        tv_end_date = layout.findViewById(R.id.tv_end_date);

        frameView = layout.findViewById(R.id.frameView);
        textViewJobCode = layout.findViewById(R.id.textViewJobCode);
        textViewTime = layout.findViewById(R.id.textViewTime);
        textViewPriority = layout.findViewById(R.id.textViewPriority);
        textViewJobStatus = layout.findViewById(R.id.textViewJobStatus);
        textViewAddress = layout.findViewById(R.id.textViewAddress);
        textViewAddress.setText(langInstance.getMobileMsgByKey(AppConstant.no_location));

        textViewDescription = layout.findViewById(R.id.textViewDescription);
        //   textViewDescription.setText(langInstance.getMobileMsgByKey(AppConstant.no_desc));

        textViewContactperson = layout.findViewById(R.id.textViewContactperson);
        textViewTitle = layout.findViewById(R.id.textViewTitle);

        textViewInstruction = layout.findViewById(R.id.textViewInstruction);
        textViewInstruction.setText(langInstance.getMobileMsgByKey(AppConstant.no_instr));

        txtViewHeader = layout.findViewById(R.id.txtViewHeader);
        buttonView = layout.findViewById(R.id.buttonView);
        buttonView.setText(langInstance.getMobileMsgByKey(AppConstant.view));

        buttonView1 = layout.findViewById(R.id.buttonView1);
        buttonView1.setText(langInstance.getMobileMsgByKey(AppConstant.view));

        buttonMapView = layout.findViewById(R.id.buttonMapView);
        buttonMapView.setText(langInstance.getMobileMsgByKey(AppConstant.map));

        imageViewChat = layout.findViewById(R.id.imageViewChat);
        imageViewCall = layout.findViewById(R.id.imageViewCall);
        imageViewEmail = layout.findViewById(R.id.imageViewEmail);
        imageView5 = layout.findViewById(R.id.imageView5);

        status_spinner = layout.findViewById(R.id.status_spinner);


        txt_fw_nm_list = layout.findViewById(R.id.txt_fw_nm_list);
        txt_fw_nm_list.setText(langInstance.getMobileMsgByKey(AppConstant.no_auditor_available));


        tv_location = layout.findViewById(R.id.textView6);
        tv_location.setText(langInstance.getMobileMsgByKey(AppConstant.location));

        tv_description = layout.findViewById(R.id.textView8);
        tv_description.setText(langInstance.getMobileMsgByKey(AppConstant.description));

        tv_instruction = layout.findViewById(R.id.textView9);
        tv_instruction.setText(langInstance.getMobileMsgByKey(AppConstant.instr));

        tv_contact_name = layout.findViewById(R.id.textView10);
        tv_contact_name.setText(langInstance.getMobileMsgByKey(AppConstant.contact_name));

        fw_Nm_List = layout.findViewById(R.id.fw_Nm_List);
        fw_Nm_List.setText(langInstance.getMobileMsgByKey(AppConstant.auditors));


        /*custsom field view's*/
        customField_view = layout.findViewById(R.id.customField_view);
        custom_filed_txt = layout.findViewById(R.id.custom_filed_txt);
        custom_filed_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_cutom_field));
        //custom_filed_txt.setOnClickListener(this);
        ll_custom_views = layout.findViewById(R.id.ll_custom_views);
        customfiled_btn = layout.findViewById(R.id.customfiled_btn);
        customfiled_btn.setOnClickListener(this);


        //listeners on view
        buttonMapView.setOnClickListener(this);
        buttonView.setOnClickListener(this);
        buttonView1.setOnClickListener(this);


        imageViewCall.setOnClickListener(this);
        imageViewEmail.setOnClickListener(this);
        imageViewChat.setOnClickListener(this);
        audit_status_relative.setOnClickListener(this);

        //listners for status button
        button_start.setOnClickListener(this);
        button_on_hold.setOnClickListener(this);
        button_resume.setOnClickListener(this);
        button_completed.setOnClickListener(this);

        attachmemt_flag = layout.findViewById(R.id.attachmemt_flag);
        equi_flag = layout.findViewById(R.id.equi_flag);
        des_card = layout.findViewById(R.id.des_card);
        card_instr = layout.findViewById(R.id.card_instr);

    }

    private void addCustomFieldText() {
        boolean btnText = false;
        try {
            for (CustOmFormQuestionsRes model : questionList) {
                if (model.getAns().size() > 0 && !model.getAns().get(0).getValue().equals("")) {
                    btnText = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

        try {
            if (!btnText) {
                customfiled_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add));
                SAVEANS = false;
            } else {
                customfiled_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.edit));
                SAVEANS = true;
            }
        } catch (Exception ex) {
            ex.getMessage();
        }

    }

    /*****Custom Filed's Data List*****/
    @Override
    public void setCustomFiledList(ArrayList<CustOmFormQuestionsRes> questionList) {

        try {
            if (questionList.size() > 0) {
                this.questionList = questionList;
                customField_view.setVisibility(View.VISIBLE);
                ll_custom_views.removeAllViews();
                for (CustOmFormQuestionsRes res : questionList) {
                    View customView = LayoutInflater.from(getActivity()).inflate(R.layout.item_custom_field_job_overview, null, false);
                    TextView textView = customView.findViewById(R.id.custom_filed_form);
                    AppCompatCheckBox checkBox = customView.findViewById(R.id.checkbox);
                    textView.setText(res.getDes() + " : ");

                    List<AnswerModel> ans = res.getAns();
                    if (ans != null && ans.size() > 0) {
                        for (AnswerModel model : ans) {
                            if (!TextUtils.isEmpty(model.getValue())) {
                                switch (res.getType()) {
                                    case "5"://date type
                                        try {
                                            if (!(model.getValue()).equals("")) {
                                                String[] dateConvert = AppUtility.getFormatedTime(model.getValue());
                                                String s = dateConvert[0];
                                                String[] date = s.split(",");
                                                textView.append(date[1].trim().replace(" ", "-"));

                                                //  holder.tvDate.setText(date[1].trim().replace(" ", "-"));
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                        break;
                                    case "6"://Time type
                                        try {
                                            if (!(model.equals(""))) {
                                                String time = AppUtility.getDateWithFormate2((Long.parseLong(model.
                                                                getValue()) * 1000),

                                                        AppUtility.dateTimeByAmPmFormate(
                                                                "hh:mm a", "kk:mm"));
                                                textView.append(time);
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                        break;
                                    case "7": //Date Time Type
                                        try {
                                            if (!model.getValue().equals("")) {
                                                Long dateLong = Long.parseLong(model.getValue());
                                                String dateConvert = AppUtility.getDate(dateLong,
                                                        AppUtility.dateTimeByAmPmFormate(
                                                                "dd-MMM-yyyy hh:mm a",
                                                                "dd-MMM-yyyy kk:mm"));
                                                textView.append(dateConvert);
                                            }
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                        break;

                                    //single checkbox
                                    case "8":
                                        checkBox.setVisibility(View.VISIBLE);
                                        if (!TextUtils.isEmpty(model.getValue())) {
                                            checkBox.setChecked(model.getValue().equals("1"));
                                        } else checkBox.setChecked(false);
                                        textView.setText(res.getDes());
                                        break;

                                    default:
                                        textView.append(model.getValue() + " ");


                                }
                            }
                        }

                    } else if (res.getType().equals("8")) {
                        textView.setText(res.getDes());
                        checkBox.setVisibility(View.VISIBLE);
                    }

                    ll_custom_views.addView(customView);
                }
                addCustomFieldText();
            } else {
                customField_view.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSessionExpire(String message) {
        showDialog(message);
    }

    private void showDialog(String message) {
        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), message, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }

    private void setDataToView() {
        ActivityLogController
                .saveActivity(
                        ActivityLogController.AUDIT_MODULE,
                        ActivityLogController.AUDIT_DETAILS,
                        ActivityLogController.AUDIT_MODULE
                );
        Bundle bundle = getArguments();
        getUpdatedLocation();
        if (bundle != null) {
            selectedAuditPosition = bundle.getInt("position");
            // this.audit = (AuditList_Res) bundle.getSerializable("audit");
            this.audit = bundle.getParcelable("audit");

            if (!TextUtils.isEmpty(this.audit.getAdr())) {
                StringBuilder addres = new StringBuilder();
                addres.append(this.audit.getAdr());
                if (!TextUtils.isEmpty(this.audit.getLandmark()))
                    addres.append(", " + this.audit.getLandmark());

                if (!TextUtils.isEmpty(this.audit.getCity()))
                    addres.append(", " + this.audit.getCity());

                if (!TextUtils.isEmpty(this.audit.getState())
                        && !TextUtils.isEmpty(this.audit.getCtry())) {
                    try {
                        String stateName = SpinnerCountrySite.getStatenameById(this.audit.getCtry(), this.audit.getState());
                        if (!TextUtils.isEmpty(stateName))
                            addres.append(", " + stateName);
                        String countryId = SpinnerCountrySite.getCountryNameById(this.audit.getCtry());
                        if (!TextUtils.isEmpty(countryId))
                            addres.append(", " + countryId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (!TextUtils.isEmpty(this.audit.getZip())) {
                    addres.append(", " + this.audit.getZip());
                }

                textViewAddress.setText(addres.toString());
            }

//            textViewDescription.setText(this.audit.getDes());
            try {
                if (this.audit.getInst() != null && !this.audit.getInst().equals("")) {
                    card_instr.setVisibility(View.VISIBLE);
                    textViewInstruction.setText(this.audit.getInst());
                } else card_instr.setVisibility(View.GONE);
            } catch (Exception exception) {
                exception.getMessage();
            }

            setAuditorName(this.audit.getKpr());

            if (TextUtils.isEmpty(audit.getCnm()))
                textViewContactperson.setText(langInstance.getMobileMsgByKey(AppConstant.no_contact_available));
            else
                textViewContactperson.setText(audit.getCnm());

            if (this.audit.getLabel() != null)
                textViewJobCode.setText(getSpannebleFormat(LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_code), this.audit.getLabel()), TextView.BufferType.SPANNABLE);
            else
                textViewJobCode.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_code) + "");
            updateStatusButtons(this.audit.getStatus());

            String dateFormat = "dd MMM yyyy";
            String timeFormat = AppUtility.dateTimeByAmPmFormate("hh:mm a", "kk:mm");

            if (!TextUtils.isEmpty(this.audit.getSchdlStart())) {
                long startDateLong = Long.parseLong(this.audit.getSchdlStart());
                String startDate = AppUtility.getDateWithFormate2(startDateLong * 1000, dateFormat);
                tv_start_date.setText(startDate);
                String startTime = AppUtility.getDateWithFormate2(startDateLong * 1000, timeFormat);
                tv_start_time.setText(startTime);
                // tv_start_date.setText(getSpannebleFormat(LanguageController.getInstance().getMobileMsgByKey(AppConstant.start), startDate), TextView.BufferType.SPANNABLE);

            }

            if (!TextUtils.isEmpty(this.audit.getSchdlFinish())) {
                long endDateLong = Long.parseLong(this.audit.getSchdlFinish());

                String endDate = AppUtility.getDateWithFormate2(endDateLong * 1000, dateFormat);
                tv_end_date.setText(endDate);

                String endTime = AppUtility.getDateWithFormate2(endDateLong * 1000, timeFormat);
                tv_end_time.setText(endTime);

            }


            AppUtility.spinnerPopUpWindow(status_spinner);
            status_spinner.setSelected(false);
            status_spinner.setAdapter(new MySpinnerAdapter(getActivity(), arraystatus));
            status_spinner.post(new Runnable() {
                @Override
                public void run() {
                    status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                            if (System.currentTimeMillis() - check < 1000) {
                                return;
                            }
                            if (position == 1) {
                                if (audit.getStatus() != null && audit.getStatus().equals("8"))
                                    return;
                            }
                            AppUtility.alertDialog2(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.status_dialog), LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_status_change), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel), new Callback_AlertDialog() {
                                @Override
                                public void onPossitiveCall() {

                                    createStatusChangeRequest(position);
                                }

                                @Override
                                public void onNegativeCall() {
                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            });


            /****custom fileds qiestion list***/
            if (App_preference.getSharedprefInstance().getLoginRes().getIsCustomFieldEnable().equals("1")) {
                auditDetail_pc.getCustomFieldQues(audit.getAudId());
            }

            try {
                EotEditor mEditor = layout.findViewById(R.id.editor);
                mEditor.setPlaceholder("Job Description");
                mEditor.setEditorFontSize(13);
                mEditor.setEditorFontColor(Color.GRAY);
                mEditor.setBackgroundColor(Color.TRANSPARENT);
                mEditor.setInputEnabled(false);
                if (!TextUtils.isEmpty(this.audit.getDes())) {
                    des_card.setVisibility(View.VISIBLE);
                    mEditor.setHtml(this.audit.getDes());
                } else des_card.setVisibility(View.GONE);


                if (this.audit.getDes() != null) {
                    Spannable spannableHtmlWithImageGetter = PicassoImageGetter.getSpannableHtmlWithImageGetter(textViewDescription,
                            this.audit.getDes());

                    PicassoImageGetter.setClickListenerOnHtmlImageGetter(spannableHtmlWithImageGetter, new PicassoImageGetter.Callback() {
                        @Override
                        public void onImageClick(String imageUrl) {
                            if (!TextUtils.isEmpty(imageUrl)) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl)));
                            }
                        }
                    });
                    textViewDescription.setText(spannableHtmlWithImageGetter);
                    textViewDescription.setMovementMethod(LinkMovementMethod.getInstance());
                }


            } catch (Exception e) {
                e.getMessage();
            }


        }


        /*****Equipm,ent Item & attchment visibility***/
        flagVisibility();
    }

    @Override
    public void onResume() {
        super.onResume();
        check = System.currentTimeMillis();
    }

    // status button for audit for current audit status
    private void updateStatusButtons(String status) {
        if (!TextUtils.isEmpty(status)) {
            if (status.equals("1")) {
                button_start.setVisibility(View.VISIBLE);
                button_resume.setVisibility(View.GONE);
                button_on_hold.setVisibility(View.GONE);
                button_completed.setVisibility(View.GONE);
                audit_status_relative.setVisibility(View.GONE);
            } else if (status.equals("7")) {
                button_start.setVisibility(View.GONE);
                button_resume.setVisibility(View.GONE);
                button_on_hold.setVisibility(View.VISIBLE);
                button_completed.setVisibility(View.VISIBLE);
                audit_status_relative.setVisibility(View.VISIBLE);

            } else if (status.equals("8")) {
                button_start.setVisibility(View.GONE);
                button_on_hold.setVisibility(View.GONE);
                button_resume.setVisibility(View.VISIBLE);
                button_completed.setVisibility(View.VISIBLE);
                audit_status_relative.setVisibility(View.VISIBLE);

            } else if (status.equals("9")) {
                button_start.setVisibility(View.GONE);
                button_resume.setVisibility(View.GONE);
                button_on_hold.setVisibility(View.GONE);
                button_completed.setVisibility(View.GONE);
                audit_status_relative.setVisibility(View.GONE);
            }
//            else if (status.equals("8")) {
//                button_start.setVisibility(View.GONE);
//                button_on_hold.setVisibility(View.GONE);
//                button_resume.setVisibility(View.VISIBLE);
//                button_completed.setVisibility(View.VISIBLE);
//                audit_status_relative.setVisibility(View.VISIBLE);
//
//            }

            setViewByAuditStatus(Integer.parseInt(status));


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 145) {
            if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (locationTracker != null) {
                    locationTracker.getCurrentLocation();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void getUpdatedLocation() {
        locationTracker = new LocationTracker(getActivity(), new OnLocationUpdate() {
            @Override
            public void OnContinue(boolean isLocationUpdated, boolean isPermissionAllowed) {
                if (isPermissionAllowed) {
                    locationTracker.getCurrentLocation();
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            145);
                }
            }
        });

    }

    private void createStatusChangeRequest(int position) {

        AuditStatusRequest request = new AuditStatusRequest();
        request.setAudId(this.audit.getAudId() + "");
        request.setUsrId(App_preference.getSharedprefInstance().getLoginRes().getUsrId());

        //set status flag start on-hold Resume and complete position
        if (position == 0)
            request.setStatus("7");
        else if (position == 1)
            //  request.setStatus("12");
            request.setStatus("8");
        else if (position == 2)
            request.setStatus("9");

        //device type
        request.setDevice_Type("1");

        request.setDateTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
        request.setLat(LatLngSycn_Controller.getInstance().getLat());
        request.setLng(LatLngSycn_Controller.getInstance().getLng());
        request.setType("2");

        if (!this.audit.getStatus().equals(request.getStatus()))
            auditDetail_pc.updateAuditStatus(request);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customfiled_btn:
                Intent intent1 = new Intent(getActivity(), CustomFiledListActivity.class);
                intent1.putExtra("jobId", audit.getAudId());
                intent1.putExtra("ansedit", SAVEANS);
                intent1.putExtra("AUDITCUSTOMFIELD", true);
                intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent1.putParcelableArrayListExtra("list", questionList);
                startActivityForResult(intent1, AUDITCUSTOMFILED);
                break;

            case R.id.button_start:
//                createStatusChangeRequest(0);
//                break;
            case R.id.button_resume:
                createStatusChangeRequest(0);
                break;

            case R.id.button_on_hold:
//                if (button_on_hold.getText().toString().equals(LanguageController.getInstance().getMobileMsgByKey(AppConstant.on_hold))) {
//                    createStatusChangeRequest(2);
//                    break;
//                } else {
                createStatusChangeRequest(1);
                break;
            //}
            case R.id.button_completed:
                createStatusChangeRequest(2);
                break;
            case R.id.audit_status_relative:
                status_spinner.performClick();
                break;
            case R.id.buttonMapView:
                drawRoute();
                break;

            case R.id.buttonView:
                if (!this.audit.getDes().equals("")) {
                    //frameView.setAlpha(0.3F);
                    getDialogDes();
                    try {
                        TextView txtViewDeails = enterFieldDialog.findViewById(R.id.txtViewDeails);
                        txtViewDeails.setVisibility(View.VISIBLE);
                        txtViewDeails.setText(this.audit.getDes());

                        TextView txtViewHeader = enterFieldDialog.findViewById(R.id.txtViewHeader);
                        txtViewHeader.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.description));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    TextView okBtn = enterFieldDialog.findViewById(R.id.btnClose);
                    okBtn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok));
                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //frameView.setAlpha(1.0F);
                            enterFieldDialog.dismiss();
                        }
                    });
                    enterFieldDialog.show();
                } else {
                    AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.desc_no), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return true;
                        }
                    });
                }
                break;


            case R.id.buttonView1:
                if (!this.audit.getInst().equals("")) {
                    //frameView.setAlpha(0.3F);
                    getDialogDes();
                    try {
                        TextView txtViewDeails = enterFieldDialog.findViewById(R.id.txtViewDeails);
                        txtViewDeails.setVisibility(View.VISIBLE);
                        txtViewDeails.setText(this.audit.getInst());

                        TextView txtViewHeader = enterFieldDialog.findViewById(R.id.txtViewHeader);
                        txtViewHeader.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.instr));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    TextView okBtn = enterFieldDialog.findViewById(R.id.btnClose);
                    okBtn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok));
                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //frameView.setAlpha(1.0F);
                            enterFieldDialog.dismiss();
                        }
                    });
                    enterFieldDialog.show();
                } else {
                    AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_inst), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return true;
                        }
                    });
                }

                break;
            case R.id.imageViewCall:
                if (this.audit != null) {
                    if (!this.audit.getMob1().equals("") || !this.audit.getMob2().equals("")) {
                        //frameView.setAlpha(0.3F);
                        getDialogCall();
                        try {
                            final TextView txtViewSkypeCon1 = enterFieldDialog.findViewById(R.id.txtViewSkypeCon1);
                            txtViewSkypeCon1.setVisibility(View.VISIBLE);
                            final SpannableString s1 = new SpannableString(this.audit.getMob1().trim());
                            Linkify.addLinks(txtViewSkypeCon1, Linkify.ALL);
                            if (TextUtils.isEmpty(s1))
                                txtViewSkypeCon1.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_contact_available));
                            else
                                txtViewSkypeCon1.setText(s1);

                            txtViewSkypeCon1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                                            != PackageManager.PERMISSION_GRANTED) {

                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                                    } else if (AppUtility.isvalidPhoneNo(txtViewSkypeCon1.getText().toString())) {
                                        AppUtility.getCallOnNumber(getActivity(), txtViewSkypeCon1.getText().toString());

                                    }
                                }
                            });

                            final TextView txtViewSkypeCon2 = enterFieldDialog.findViewById(R.id.txtViewSkypeCon2);
                            txtViewSkypeCon2.setVisibility(View.VISIBLE);
                            Linkify.addLinks(txtViewSkypeCon2, Linkify.ALL);
                            final SpannableString s = new SpannableString(this.audit.getMob2().trim());
                            if (TextUtils.isEmpty(s))
                                txtViewSkypeCon2.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_contact_available));
                            else
                                txtViewSkypeCon2.setText(s);
                            txtViewSkypeCon2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                                            != PackageManager.PERMISSION_GRANTED) {

                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                                    } else if (AppUtility.isvalidPhoneNo(txtViewSkypeCon2.getText().toString())) {
                                        AppUtility.getCallOnNumber(getActivity(), txtViewSkypeCon2.getText().toString());

                                    }
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        TextView okBtn = enterFieldDialog.findViewById(R.id.btnClose);
                        okBtn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok));
                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // frameView.setAlpha(1.0F);
                                enterFieldDialog.dismiss();
                            }
                        });
                        enterFieldDialog.show();
                    } else {
                        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_contact_available), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return true;
                            }
                        });
                    }
                }
                break;


            case R.id.imageViewEmail:

                if (audit != null) {
                    if (!audit.getEmail().equals("")) {
                        // frameView.setAlpha(0.3F);
                        getDialogEmail();
                        TextView txtViewSkypeCon1 = enterFieldDialog.findViewById(R.id.txt_email_popup);
                        final SpannableString s1 = new SpannableString(audit.getEmail().trim());
                        Linkify.addLinks(s1, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
                        txtViewSkypeCon1.setTextIsSelectable(true);
                        txtViewSkypeCon1.setMovementMethod(LinkMovementMethod.getInstance());
                        if (TextUtils.isEmpty(s1))
                            txtViewSkypeCon1.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_email_msg));
                        else
                            txtViewSkypeCon1.setText(s1);
                        enterFieldDialog.show();

                        TextView okBtn = enterFieldDialog.findViewById(R.id.btnClose);
                        okBtn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok));

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                enterFieldDialog.dismiss();
                            }
                        });

                    } else {
                        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_det_email), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return true;
                            }
                        });
                    }
                }

              /*  AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_email_msg), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return true;
                    }
                });*/
                break;

            case R.id.imageViewChat:
                AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert),
                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_chat),
                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.no), "", new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return true;
                            }
                        });
                break;


        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUDITCUSTOMFILED) {
            getUpdateForm();
        }
    }


    /***update form list after Ans Submit***/
    public void getUpdateForm() {
        if (auditDetail_pc != null) {
            if (App_preference.getSharedprefInstance().getLoginRes().getIsCustomFieldEnable().equals("1")) {
                auditDetail_pc.getCustomFieldQues(audit.getAudId());
            }
        }
    }

    /***
     * redirecting to google map to draw route
     * between user current location to audit location
     *
     ***/
    private void drawRoute() {
        if (!TextUtils.isEmpty(this.audit.getLat()) && !TextUtils.isEmpty(this.audit.getLng())) {
            // String geoUri = "http://maps.google.com/maps?q=loc:" + this.audit.getLat() + "," + this.audit.getLng();
            try {
                String geoUri = "http://maps.google.com/maps?daddr=" + this.audit.getLat() + "," + this.audit.getLng();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!TextUtils.isEmpty(textViewAddress.getText().toString())) {
            try {

                String completeAddress = audit.getAdr() + " " + audit.getCity() + " " + SpinnerCountrySite.getCountryNameById(audit.getCtry());

                String geoUri = "http://maps.google.com/maps?daddr=" + completeAddress;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_location), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return true;
                }
            });
        }
    }


    private void getDialog() {
        try {
            if (enterFieldDialog != null)
                if (enterFieldDialog.isShowing())
                    enterFieldDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (enterFieldDialog != null)
                enterFieldDialog = null;
            enterFieldDialog = new Dialog(getActivity());
            enterFieldDialog.setCancelable(false);
            enterFieldDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            enterFieldDialog.setContentView(R.layout.popup_details);

            Window window = enterFieldDialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
            window.setAttributes(wlp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDialogDes() {
        try {
            if (enterFieldDialog != null)
                if (enterFieldDialog.isShowing())
                    enterFieldDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (enterFieldDialog != null)
                enterFieldDialog = null;
            enterFieldDialog = new Dialog(getActivity());
            enterFieldDialog.setCancelable(false);
            enterFieldDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            enterFieldDialog.setContentView(R.layout.popup_descriprion);

            TextView txtViewHeader = enterFieldDialog.findViewById(R.id.txtViewHeader);
            txtViewHeader.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.description));

            Window window = enterFieldDialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
            window.setAttributes(wlp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getDialogEmail() {
        try {
            if (enterFieldDialog != null)
                if (enterFieldDialog.isShowing())
                    enterFieldDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (enterFieldDialog != null)
                enterFieldDialog = null;
            enterFieldDialog = new Dialog(getActivity());
            enterFieldDialog.setCancelable(false);
            enterFieldDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            enterFieldDialog.setContentView(R.layout.popup_emai_layout);

            Window window = enterFieldDialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
            window.setAttributes(wlp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getDialogCall() {
        try {
            if (enterFieldDialog != null)
                if (enterFieldDialog.isShowing())
                    enterFieldDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (enterFieldDialog != null)
                enterFieldDialog = null;
            enterFieldDialog = new Dialog(getActivity());
            enterFieldDialog.setCancelable(false);
            enterFieldDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            enterFieldDialog.setContentView(R.layout.popup_call);

            Window window = enterFieldDialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
            window.setAttributes(wlp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*get spanneble string with label bold  and value normal*/
    private SpannableStringBuilder getSpannebleFormat(String lableName, String value) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString str1 = new SpannableString(lableName + " ");
        str1.setSpan(new ForegroundColorSpan(Color.parseColor("#737D7E")), 0, str1.length(), 0);
        builder.append(str1);

        SpannableString str2 = new SpannableString(value);
        str2.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(str2);

        return builder;
    }

    @Override
    public void statusChanged(int status) {
        if (status > 0) {
            this.audit.setStatus(status + "");
            Intent intent = new Intent();
            updateStatusButtons(status + "");
            intent.putExtra("status", status);
            intent.putExtra("position", selectedAuditPosition);
            getActivity().setResult(Activity.RESULT_OK, intent);
        }


    }


    /**/
    private void setAuditorName(String names) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            String kpr = names;
            String[] kprList = kpr.split(",");
            for (String id : kprList) {
                FieldWorker fieldWorker = AppDataBase.getInMemoryDatabase(getActivity()).fieldWorkerModel().getFieldWorkerByID(id);
                if (fieldWorker != null) {
                    stringBuffer.append(fieldWorker.getName())
                            .append(fieldWorker.getLnm())
                            .append(", ");
                }

            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 2);
            String paramIds = stringBuffer.toString();
            txt_fw_nm_list.setText(paramIds);
        } catch (Exception e) {
            e.printStackTrace();
            txt_fw_nm_list.setText("Not available");
        }
    }


    private void setViewByAuditStatus(int statusValue) {
        try {
            JobStatusModel jobStatusObject = JobStatus_Controller.getInstance().getStatusObjectById(String.valueOf(statusValue));
            if (jobStatusObject != null) {
                if (statusValue == 8) {
                    status.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.new_on_hold));
                    status_img.setImageDrawable(getActivity().getDrawable(R.drawable.ic_pendng_task));
                } else {
                    status.setText(jobStatusObject.getStatus_name());
                    int id = getResources().getIdentifier(jobStatusObject.getImg(), "drawable", getActivity().getPackageName());
                    status_img.setImageResource(id);
                }
                ll_status.setBackgroundResource(R.color.white);
                switchDefaultColor(EotApp.getAppinstance().getResources().getColor(R.color.txt_color));


                if (jobStatusObject.getStatus_no().equals("7")) {
                    ll_status.setBackgroundResource(R.color.in_progress);
                    switchDefaultColor(EotApp.getAppinstance().getResources().getColor(R.color.white));
                }
            } else {
                if (audit != null && audit.getAudId() != null)
                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().deletAuditById(audit.getAudId());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchDefaultColor(int color) {
        status.setTextColor(color);
        // st.setTextColor(color);
        // job_am_pm.setTextColor(color);
        status.setTextColor(color);
    }


}
