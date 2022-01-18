package com.eot_app.nav_menu.jobs.job_detail.detail;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.Log;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.eoteditor.EotEditor;
import com.eot_app.eoteditor.PicassoImageGetter;
import com.eot_app.lat_lng_sync_pck.LatLngSycn_Controller;
import com.eot_app.locations.LocationTracker;
import com.eot_app.locations.OnLocationUpdate;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.custom_fileds.CustomFiledListActivity;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;
import com.eot_app.nav_menu.jobs.job_complation.DocDeleteNotfy;
import com.eot_app.nav_menu.jobs.job_complation.JobCompletionActivity;
import com.eot_app.nav_menu.jobs.job_complation.JobCompletionAdpter;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_db.JtId;
import com.eot_app.nav_menu.jobs.job_detail.JobDetailActivity;
import com.eot_app.nav_menu.jobs.job_detail.customform.CustomFormCompletionCallBack;
import com.eot_app.nav_menu.jobs.job_detail.detail.job_detail_presenter.JobDetail_pc;
import com.eot_app.nav_menu.jobs.job_detail.detail.job_detail_presenter.JobDetail_pi;
import com.eot_app.nav_menu.jobs.job_detail.detail.job_detail_view.JobDetail_view;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.AnswerModel;
import com.eot_app.nav_menu.jobs.job_detail.job_status_pkg.JobStatus_Controller;
import com.eot_app.nav_menu.jobs.job_detail.reschedule.RescheduleActivity;
import com.eot_app.nav_menu.jobs.job_detail.revisit.RevisitActivity;
import com.eot_app.nav_menu.jobs.job_list.JobList;
import com.eot_app.nav_menu.jobs.joboffline_db.JobOverViewNotify;
import com.eot_app.nav_menu.quote.quote_invoice_pkg.Quote_Invoice_Details_Activity;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.CustomLinearLayoutManager;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.TagData;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.eot_app.utility.util_interfaces.OnFragmentInteractionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.hypertrack.hyperlog.HyperLog;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements Html.ImageGetter,
        View.OnClickListener,
        JobDetail_view,
        CustomFormCompletionCallBack, JobOverViewNotify, OnMapReadyCallback, JobCompletionAdpter.FileDesc_Item_Selected
        , NotifyForAttchCount, DocDeleteNotfy {
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1234;
    public static final int CUSTOMFILED = 222;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_COMPLETION_NOTE = 111;
    private static final int REQUEST_RESCHEDULE = 454;
    private static final String TAG = DetailFragment.class.getSimpleName();
    private final LinkedHashMap<String, String> arraystatusvalue = new LinkedHashMap<>();
    private final LinkedHashMap<String, String> arraystatus = new LinkedHashMap<>();
    LocationTracker locationTracker;
    ArrayList<CustOmFormQuestionsRes> questionList = new ArrayList<>();
    long check = System.currentTimeMillis();
    MapView mMapView;
    LinearLayout filter_layout;
    String[] statusArray = new String[arraystatus.size()];
    String[] statusArrayForIds = new String[arraystatusvalue.size()];
    CustomLinearLayoutManager customLayoutManager;
    private FrameLayout frameView;
    private TextView person_name, textViewJobCode, textViewTime, textViewPriority,
            textViewJobStatus, textViewAddress, textViewDescription, textViewContactperson, textViewTitle, textViewInstruction, txtViewHeader, textViewTags, txt_fw_nm_list,
            tv_description, tv_instruction, complation_txt, complation_notes, tv_tag, fw_Nm_List;
    private Button buttonAccept, buttonDecline, buttonView;
    private ImageView imageViewChat, imageViewCall, imageViewEmail;
    private View layout;
    private TextView image_txt;
    private Dialog enterFieldDialog;
    private JobStatusModel jobstatus;
    private JobDetail_pi jobDetail_pi;
    private Spinner new_status_spinner;
    private LinearLayout accept_reject_linear;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private Job mParam2;
    private String param3;
    private View map_loc_txt;
    private RelativeLayout map_layout;
    private OnFragmentInteractionListener mListener;
    //    check if travelling is enable or not
    //    for remove contact detail when permission not allowed from admin
    private CardView contact_card, recur_parent_view, tagcardView;//
    private TextView custom_filed_txt, btnStopRecurView, btnComplationView, custom_filed_form, recur_txt, txt_recur_msg, contact_name_lable, schdule_details_txt, job_status_lable;
    private CardView customField_view, cardView_signature_pad, quotes_details_card;
    private TextView customfiled_btn, signature_pad, quotes_details_txt, quotes_details_number_txt, quotes_details_number;
    private LinearLayout ll_custom_views;
    private Boolean SAVEANS = false;
    private ImageView attachmemt_flag, item_flag, equi_flag, arrow_dp_icon, signature_img;
    private String strAddress = "";
    private Job_Status_Adpter mySpinnerAdapter;
    private RecyclerView recyclerView;
    private ChipGroup chipGroup;
    private CompletionAdpterJobDteails jobCompletionAdpter;
    private TextView site_name;
    private String isMailSentToClt = "1";


    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param3 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param3) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            param3 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void OnItemClick_Document(GetFileList_Res getFileList_res) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(App_preference.getSharedprefInstance().getBaseURL() + "" + getFileList_res.getAttachFileName())));

    }

    @Override
    public void openAttachmentDialog() {

    }

    @Override
    public void updateCOuntAttchment() {
        if (jobDetail_pi != null)
            jobDetail_pi.getAttachFileList(mParam2.getJobId(), App_preference.getSharedprefInstance().getLoginRes().getUsrId()
                    , "6");
    }

    @Override
    public void notifyDocDelete() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("GoogleMap", "1");
        if (mParam2 == null) {
            HyperLog.i(TAG, "Job Not Found In DB");
            return;
        } else {

            try {
                Log.e("Job data", "" + new Gson().toJson(mParam2));
                if (mParam2.getLat() != null && mParam2.getLng() != null) {
                    if (mParam2.getLat().equals("") && mParam2.getLng().equals("")
                            || mParam2.getLat().equals("0") && mParam2.getLng().equals("0")) {
                        mParam2.setLng("0.0");
                        mParam2.setLat("0.0");
                    }
                    if (mParam2.getLat().equals("0.0") && mParam2.getLng().equals("0.0") && map_loc_txt != null) {
                        map_loc_txt.setVisibility(View.VISIBLE);
                        image_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.location_not_found));
                    } else {
                        LatLng latLng = new LatLng(Double.parseDouble(mParam2.getLat()), Double.parseDouble(mParam2.getLng()));
                        googleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                        );
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

                        if (map_loc_txt != null) {
                            map_loc_txt.setVisibility(View.GONE);
                        }
                    }
                } else {
                    mParam2.setLng("0.0");
                    mParam2.setLat("0.0");
                    map_loc_txt.setVisibility(View.VISIBLE);
                }
            } catch (Exception ex) {
                ex.getMessage();
                mParam2.setLng("0.0");
                mParam2.setLat("0.0");
                map_loc_txt.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void updateJobOverViewFlag() {
        try {
            EotApp.getAppinstance().notifyApiObserver(Service_apis.addAppointment);
            mParam2 = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(mParam2.getJobId());
            flagVisibility();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void notifyCustomberSign() {
        mParam2 = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(mParam2.getJobId());
        setUploadSignature();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        layout = inflater.inflate(R.layout.fragment_detail2, container, false);


        mMapView = layout.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately
        Log.e("GoogleMap", "2");


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        initializelables();


        mMapView.getMapAsync(this);


        mySpinnerAdapter = new Job_Status_Adpter(getActivity(), statusArray, new Job_Status_Adpter.MyJobStatusDp() {
            @Override
            public void selectedStatus(int i) {
                Log.e("", "");
                if (statusArray[i].equals(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reschedule))) {
                    if (mParam2.getJobId().equals(mParam2.getTempId())) {
                        showErrorDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_not_sync));
                    } else if (jobstatus != null && jobstatus.getStatus_no().equals(AppConstant.Completed) ||
                            jobstatus.getStatus_no().equals(AppConstant.Closed)) {
                        showErrorDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.close_completed_job_msg));
                    } else {
                        Intent open_reschedule = new Intent(getActivity(), RescheduleActivity.class);
                        open_reschedule.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        String str = new Gson().toJson(mParam2);
                        startActivityForResult(open_reschedule.putExtra("job", str), REQUEST_RESCHEDULE);
                    }
                } else if (statusArray[i].equals(LanguageController.getInstance().getMobileMsgByKey(AppConstant.require_revisit))) {
                    if (mParam2.getJobId().equals(mParam2.getTempId())) {
                        showErrorDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_not_sync));
                    } else if (mParam2.getParentId() != null && mParam2.getParentId().equals("0")) {
                        Intent open_revisit = new Intent(getActivity(), RevisitActivity.class);
                        open_revisit.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        String str = new Gson().toJson(mParam2);
                        startActivity(open_revisit.putExtra("job", str));
                    } else
                        showErrorDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.revisit_msg_validation));
                } else {
                    JobStatusModel statusModel = JobStatus_Controller.getInstance().getStatusObjectById(statusArrayForIds[i]);
                    if (statusModel != null) {
                        HyperLog.i(TAG, "Selected status:" + statusModel.getStatus_name());
                        HyperLog.i(TAG, "onItemSelected:" + "Select status From DropDown");
                        jobstatus.setStatus_name(statusModel.getStatus_name());
                        jobstatus.setStatus_no(statusModel.getStatus_no());
                    } else {
                        return;
                    }
                    if (!jobDetail_pi.isOldStaus(jobstatus.getStatus_no(), mParam2.getJobId())) {//&& (i != -1){
                        AppUtility.alertDialog2(getActivity(), LanguageController.getInstance()
                                .getMobileMsgByKey(AppConstant.status_dialog), LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_status_change), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel), new Callback_AlertDialog() {
                            @Override
                            public void onPossitiveCall() {
                                HyperLog.i(TAG, "Request change status start");
                                ((JobDetailActivity) getActivity()).openFormForEvent(jobstatus.getStatus_no());
                            }

                            @Override
                            public void onNegativeCall() {
                                HyperLog.i(TAG, "onNegativeCall::");
                                jobDetail_pi.setJobCurrentStatus(mParam2.getJobId());
                            }
                        });
                    } else {
                        jobDetail_pi.setJobCurrentStatus(mParam2.getJobId());
                    }
                }


            }
        });
        new_status_spinner.setAdapter(mySpinnerAdapter);


        getViewIds();


        return layout;
    }


    private void initializelables() {

        frameView = layout.findViewById(R.id.frameView);
        person_name = layout.findViewById(R.id.person_name);
        textViewJobCode = layout.findViewById(R.id.textViewJobCode);
        textViewTime = layout.findViewById(R.id.textViewTime);
        textViewPriority = layout.findViewById(R.id.textViewPriority);
        textViewJobStatus = layout.findViewById(R.id.textViewJobStatus);
        textViewAddress = layout.findViewById(R.id.textViewAddress);
        textViewAddress.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_location));

        textViewDescription = layout.findViewById(R.id.textViewDescription);
        textViewDescription.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_desc));

        textViewTags = layout.findViewById(R.id.textViewTags);
        textViewTags.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_tag));

        accept_reject_linear = layout.findViewById(R.id.accept_reject_linear);

        textViewContactperson = layout.findViewById(R.id.textViewContactperson);
        textViewContactperson.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_contact_available));
        textViewTitle = layout.findViewById(R.id.textViewTitle);

        textViewInstruction = layout.findViewById(R.id.textViewInstruction);
        textViewInstruction.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_instr));

        txtViewHeader = layout.findViewById(R.id.txtViewHeader);
        buttonAccept = layout.findViewById(R.id.buttonAccept);
        buttonDecline = layout.findViewById(R.id.buttonDecline);
        buttonView = layout.findViewById(R.id.buttonView);
        buttonView.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.view));


        imageViewChat = layout.findViewById(R.id.imageViewChat);
        imageViewCall = layout.findViewById(R.id.imageViewCall);
        imageViewEmail = layout.findViewById(R.id.imageViewEmail);

        txt_fw_nm_list = layout.findViewById(R.id.txt_fw_nm_list);
        txt_fw_nm_list.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_fw_available));

        contact_card = layout.findViewById(R.id.contact_card);


        tv_description = layout.findViewById(R.id.textView8);
        tv_description.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.description));

        tv_instruction = layout.findViewById(R.id.textView9);
        tv_instruction.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.instr));


        fw_Nm_List = layout.findViewById(R.id.fw_Nm_List);
        fw_Nm_List.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.fieldworkers));

        tv_tag = layout.findViewById(R.id.textView11);
        tv_tag.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_tags));

        complation_txt = layout.findViewById(R.id.complation_txt);
        complation_notes = layout.findViewById(R.id.complation_notes);
        complation_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.completion_note));

        btnComplationView = layout.findViewById(R.id.btnComplationView);


        customField_view = layout.findViewById(R.id.customField_view);
        ll_custom_views = layout.findViewById(R.id.ll_custom_views);

        custom_filed_txt = layout.findViewById(R.id.custom_filed_txt);
        custom_filed_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_cutom_field));
        custom_filed_txt.setOnClickListener(this);


        customfiled_btn = layout.findViewById(R.id.customfiled_btn);

        attachmemt_flag = layout.findViewById(R.id.attachmemt_flag);
        item_flag = layout.findViewById(R.id.item_flag);
        equi_flag = layout.findViewById(R.id.equi_flag);


        recur_parent_view = layout.findViewById(R.id.recur_parent_view);
        recur_txt = layout.findViewById(R.id.recur_txt);
        txt_recur_msg = layout.findViewById(R.id.txt_recur_msg);
        btnStopRecurView = layout.findViewById(R.id.btnStopRecurView);
        btnStopRecurView.setOnClickListener(this);
        btnStopRecurView.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.stop_recur));
        recur_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_recur));

        contact_name_lable = layout.findViewById(R.id.contact_name_lable);
        contact_name_lable.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.contact_details));
        schdule_details_txt = layout.findViewById(R.id.schdule_details_txt);
        schdule_details_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.schedule_details));

        job_status_lable = layout.findViewById(R.id.job_status_lable);
        job_status_lable.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.status_detail) + " : ");

        arrow_dp_icon = layout.findViewById(R.id.arrow_dp_icon);
        arrow_dp_icon.setOnClickListener(this);

        new_status_spinner = layout.findViewById(R.id.new_status_spinner);

        cardView_signature_pad = layout.findViewById(R.id.cardView_signature_pad);
        signature_img = layout.findViewById(R.id.signature_img);

        chipGroup = layout.findViewById(R.id.chipGroup);

        recyclerView = layout.findViewById(R.id.recyclerView);
        signature_pad = layout.findViewById(R.id.signature_pad);
        signature_pad.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.signature_pad));


        map_layout = layout.findViewById(R.id.map_layout);
        map_loc_txt = layout.findViewById(R.id.map_loc_txt);

        try {
            image_txt = layout.findViewById(R.id.image_txt);
        } catch (Exception e) {
            e.getMessage();

        }

        site_name = layout.findViewById(R.id.site_name);
        tagcardView = layout.findViewById(R.id.tagcardView);


        quotes_details_card = layout.findViewById(R.id.quotes_details_card);
        quotes_details_txt = layout.findViewById(R.id.quotes_details_txt);
        quotes_details_number_txt = layout.findViewById(R.id.quotes_details_number_txt);
        quotes_details_number = layout.findViewById(R.id.quotes_details_number);

        quotes_details_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quotes_details));
        quotes_details_number_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quotes_num));


        customLayoutManager = new CustomLinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL
                , false);
        recyclerView.setLayoutManager(customLayoutManager);
        jobCompletionAdpter = new CompletionAdpterJobDteails(new ArrayList<GetFileList_Res>()
                , new CompletionAdpterJobDteails.CallBAckForAttchemnt() {
            @Override
            public void getAttchment() {
                ((JobDetailActivity) getActivity()).getAttchmentFragment();
            }
        });
        recyclerView.setAdapter(jobCompletionAdpter);

//        getViewIds();

        EotApp.getAppinstance().setJobFlagObserver(this);
        EotApp.getAppinstance().setNotifyForAttchCount(this);

    }

    private void addJobServicesInChips(JtId jtildModel) {
        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.job_lable_dynamic_layout, null);
        TextView textView = v.findViewById(R.id.job_lables);
        textView.setText(jtildModel.getTitle());
        chipGroup.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void StopRecurPatternHide() {
        recur_parent_view.setVisibility(View.GONE);
    }

    private void showRecurmsg() {
        try {

            if (App_preference.getSharedprefInstance().getLoginRes().getRights().get(0).getIsRecur().equals("0")
                    && mParam2.getIsRecur() != null && mParam2.getIsRecur().equals("1") && mParam2.getRecurData().size() > 0) {
                recur_parent_view.setVisibility(View.VISIBLE);
                if (mParam2.getRecurType() != null && mParam2.getRecurType().equals("1") && mParam2.getRecurData() != null) {
                    if (mParam2.getRecurData().get(0).getJobRecurModel().getMode() != null
                            && mParam2.getRecurData().get(0).getJobRecurModel().getMode().equals("1")
                            && mParam2.getRecurData().get(0).getJobRecurModel().getEndRecurMode() != null
                            && mParam2.getRecurData().get(0).getJobRecurModel().getEndRecurMode().equals("0")) {
                        txt_recur_msg.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg1) + " " +
                                mParam2.getRecurData().get(0).getJobRecurModel().getInterval() + " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.starting_on) +
                                " " + mParam2.getRecurData().get(0).getJobRecurModel().getStartDate() + " " +
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg2) + " " +
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.infinity));
                    } else {
                        txt_recur_msg.setText
                                (LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg1) + " " +
                                        " " + mParam2.getRecurData().get(0).getJobRecurModel().getInterval() +
                                        " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.starting_on)
                                        + " " + mParam2.getRecurData().get(0).getJobRecurModel().getStartDate() + " " +
                                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg2)
                                        + " " + mParam2.getRecurData().get(0).getJobRecurModel().getOccurences() +
                                        " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg3)
                                        + " " + mParam2.getRecurData().get(0).getJobRecurModel().getEndDate());
                    }
                } else if (mParam2.getRecurType() != null && mParam2.getRecurType().equals("2")
                        && mParam2.getRecurData() != null
                        && mParam2.getRecurData().get(0).getJobRecurModel().getOccur_days() != null
                        && mParam2.getRecurData().get(0).getJobRecurModel().getInterval() != null) {
                    if (mParam2.getRecurData().get(0).getJobRecurModel().getEndRecurMode() != null
                            && mParam2.getRecurData().get(0).getJobRecurModel().getEndRecurMode().equals("0")) {
                        txt_recur_msg.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.weekly_msg1) + " " +
                                mParam2.getRecurData().get(0).getJobRecurModel().getOccur_days() + " " +
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.every) + " " +
                                mParam2.getRecurData().get(0).getJobRecurModel().getInterval() + " " +
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.weeks) + " " +
                                mParam2.getRecurData().get(0).getJobRecurModel().getStartDate() + " " +
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg2)
                                + " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.infinity));
                    } else {
                        txt_recur_msg.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.weekly_msg1) + " " +
                                mParam2.getRecurData().get(0).getJobRecurModel().getOccur_days() + " " +
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.every) + " " +
                                mParam2.getRecurData().get(0).getJobRecurModel().getInterval() + " " +
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.weeks) + " " +
                                mParam2.getRecurData().get(0).getJobRecurModel().getStartDate() + " " +
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg2)
                                + " " + mParam2.getRecurData().get(0).getJobRecurModel().getOccurences() +
                                " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg3) +
                                " " + mParam2.getRecurData().get(0).getJobRecurModel().getEndDate());
                    }

                } else if (mParam2.getRecurType() != null && mParam2.getRecurType().equals("3")
                        && mParam2.getRecurData() != null
                        && mParam2.getRecurData().get(0).getJobRecurModel().getEndRecurMode()
                        != null && mParam2.getRecurData().get(0).getJobRecurModel().getWeek_num() != null
                        && mParam2.getRecurData().get(0).getJobRecurModel().getInterval() != null) {
                    if (mParam2.getRecurData().get(0).getJobRecurModel().getEndRecurMode() != null
                            && mParam2.getRecurData().get(0).getJobRecurModel().getEndRecurMode().equals("0")) {
                        txt_recur_msg.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg1) + " " +
                                mParam2.getRecurData().get(0).getJobRecurModel().getInterval() + " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.starting_on) +
                                " " + mParam2.getRecurData().get(0).getJobRecurModel().getStartDate() + " " +
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg2) + " " +
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.infinity));
                    } else

                        txt_recur_msg.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.weekly_msg1) + " " +
                                mParam2.getRecurData().get(0).getJobRecurModel().getWeek_num() + " "
                                + LanguageController.getInstance().getMobileMsgByKey(AppConstant.every) + " " +

                                mParam2.getRecurData().get(0).getJobRecurModel().getInterval() + " " +
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.months_starting_on) + " "
                                + mParam2.getRecurData().get(0).getJobRecurModel().getStartDate() + " " +

                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg2) + " "
                                + mParam2.getRecurData().get(0).getJobRecurModel().getOccurences() +
                                " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg3) + " " +
                                mParam2.getRecurData().get(0).getJobRecurModel().getEndDate());
                }
            } else {
                recur_parent_view.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void flagVisibility() {
        if (mParam2 != null) {
            if (mParam2.getAttachCount() != null && !mParam2.getAttachCount().isEmpty() && Integer.parseInt(mParam2.getAttachCount()) > 0) {
                attachmemt_flag.setVisibility(View.VISIBLE);
            } else {
                attachmemt_flag.setVisibility(View.GONE);
            }
            if (mParam2.getItemData() != null && mParam2.getItemData().size() > 0) {
                item_flag.setVisibility(View.VISIBLE);
            } else {
                item_flag.setVisibility(View.GONE);
            }
            if (mParam2.getEquArray() != null && mParam2.getEquArray().size() > 0) {
                equi_flag.setVisibility(View.VISIBLE);
            } else {
                equi_flag.setVisibility(View.GONE);
            }
        }
    }


    public void getViewIds() {
        buttonAccept.setOnClickListener(this);
        buttonDecline.setOnClickListener(this);
        textViewAddress.setOnClickListener(this);
        imageViewEmail.setOnClickListener(this);
        buttonView.setOnClickListener(this);
        imageViewChat.setOnClickListener(this);
        imageViewCall.setOnClickListener(this);

        btnComplationView.setOnClickListener(this);

        customfiled_btn.setOnClickListener(this);
        quotes_details_card.setOnClickListener(this);

        jobDetail_pi = new JobDetail_pc(this);


//        getData from
        mParam2 = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(param3);

        try {

            if (mParam2 == null) {
                HyperLog.i(TAG, "Job Not Found In DB");
                return;
            }

            if (mParam2.getStatus() != null && !AppUtility.getJobStatusList().contains(mParam2.getStatus()) && mParam2.getJobId() != null) {
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().deleteJobById(mParam2.getJobId());
                HyperLog.i(TAG, "Job Status Not Found In DB");
                return;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            HyperLog.i(TAG, "Job Not Found In DB");
            return;
        }


        /****custom fileds qiestion list***/
        if (App_preference.getSharedprefInstance().getLoginRes().getIsCustomFieldEnable().equals("1")) {
            jobDetail_pi.getCustomFieldQues(mParam2.getJobId());
        }

        addComplationButtonTxt();

        AppUtility.spinnerPopUpWindow(new_status_spinner);

//get data from other db's
        if (mParam2 != null) {
            setJobDetail();
            jobstatus = new JobStatusModel(mParam2.getStatus(), jobDetail_pi.getStatusName(mParam2.getStatus()), jobDetail_pi.getImg());
            setButtonsUI(jobstatus); //changes


//          this check is use for show/hide contact detail.
            if (jobDetail_pi.checkContactHideOrNot() && mParam2.getStatus().equals(AppConstant.Not_Started)) {
                contact_card.setVisibility(View.GONE);
            }
        }

        new_status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*****Equipm,ent Item & attchment visibility***/
        flagVisibility();

        if (!mParam2.getJobId().equals(mParam2.getTempId()))
            showRecurmsg();
        else recur_parent_view.setVisibility(View.GONE);


        setDataToView();


    }

    @Override
    public void setList(ArrayList<GetFileList_Res> getFileList_res, String isAttachCommpletionNotes) {
        (jobCompletionAdpter).updateFileList(getFileList_res);
    }

    /*** add complation button view ****/
    private void addComplationButtonTxt() {
        HyperLog.i(TAG, "addComplationButtonTxt(M) start");
        HyperLog.i(TAG, mParam2.getComplNote());
        if (TextUtils.isEmpty(mParam2.getComplNote())) {
            btnComplationView.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add));
        } else {
            btnComplationView.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.edit));
            complation_notes.setText(mParam2.getComplNote());

        }
        jobDetail_pi.getAttachFileList(mParam2.getJobId(), App_preference.getSharedprefInstance().getLoginRes().getUsrId()
                , "6");
        HyperLog.i(TAG, "addComplationButtonTxt(M) Stop");
    }

    /***update form list after Ans Submit***/
    public void getUpdateForm() {
        if (jobDetail_pi != null) {
            if (App_preference.getSharedprefInstance().getLoginRes().getIsCustomFieldEnable().equals("1")) {
                jobDetail_pi.getCustomFieldQues(mParam2.getJobId());
            }
        }
    }

    private void setJobDetail() {
        try {
            if (mParam2 != null) {

                try {
                    if (mParam2.getNm() != null && !mParam2.getNm().equals("") && mParam2.getNm().length() > 1)
                        person_name.setText(mParam2.getNm().substring(0, 1).toUpperCase() + mParam2.getNm().substring(1).toLowerCase());
                    else person_name.setText(mParam2.getNm());
                } catch (Exception exception) {
                    person_name.setText(mParam2.getNm());
                    exception.getMessage();

                }

                try {
                    if (App_preference.getSharedprefInstance().getSiteNameShowInSetting()) {
                        site_name.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(mParam2.getSnm())) {
                            site_name.setText(mParam2.getSnm());
                        } else {
                            site_name.setText("");
                            site_name.setVisibility(View.GONE);
                        }
                    } else {
                        site_name.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }


                StringBuffer sb_adr = new StringBuffer();
                if (mParam2.getAdr() != null && !mParam2.getAdr().equals("")) {
                    sb_adr.append(mParam2.getAdr());
                }
                if (!TextUtils.isEmpty(mParam2.getLandmark())) {
                    sb_adr.append(", " + mParam2.getLandmark());
                }

                if (mParam2.getCity() != null && !mParam2.getCity().equals("")) {
                    sb_adr.append(", " + mParam2.getCity());
                }
                if (mParam2.getState() != null && !mParam2.getState().equals("")) {
                    sb_adr.append(", " + SpinnerCountrySite.getStatenameById(mParam2.getCtry(), mParam2.getState()));
                }
                if (mParam2.getCtry() != null && !mParam2.getCtry().equals("")) {
                    sb_adr.append(", " + SpinnerCountrySite.getCountryNameById(mParam2.getCtry()));
                }
                if (!TextUtils.isEmpty(mParam2.getZip())) {
                    sb_adr.append(", " + mParam2.getZip());
                }

                String upperStringAdrs = "";
                try {
                    upperStringAdrs = sb_adr.substring(0, 1).toUpperCase() + sb_adr.substring(1).toLowerCase();
                } catch (Exception exception) {
                    exception.getMessage();
                }

                SpannableStringBuilder builder = new SpannableStringBuilder();
                SpannableString str1 = new SpannableString(upperStringAdrs);
                builder.append(str1);
                SpannableString str2 = new SpannableString("  " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.get_direction));

                str2.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.colorPrimary)),
                        0, str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                builder.append(str2);

                textViewAddress.setText(builder, TextView.BufferType.SPANNABLE);


                strAddress = sb_adr + "";

                try {
                    if (mParam2.getCnm() != null && !mParam2.getCnm().equals("") && mParam2.getCnm().length() > 1) {
                        String upperString = mParam2.getCnm().substring(0, 1).toUpperCase() + mParam2.getCnm().substring(1).toLowerCase();
                        textViewContactperson.setText(upperString);
                    } else {
                        textViewContactperson.setText(mParam2.getCnm());
                    }
                } catch (Exception exception) {
                    textViewContactperson.setText(mParam2.getCnm());
                    exception.getMessage();
                }

            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void showHideRescheduleRevisit(String status) {
        if (status != null) {
            if (status.equals(AppConstant.Not_Started)) {
                if (App_preference.getSharedprefInstance().getLoginRes().getRights().get(0).getIsJobRescheduleOrNot() != 1) {
                    if (!arraystatus.containsKey("0")) {
                        arraystatusvalue.put("0", LanguageController.getInstance().getMobileMsgByKey(AppConstant.reschedule));
                        arraystatus.put("0", LanguageController.getInstance().getMobileMsgByKey(AppConstant.reschedule));
                    }
                } else {
                    if (arraystatus.containsKey("0")) {
                        arraystatusvalue.remove(0);
                        arraystatus.remove(0);
                    }
                }

                if (arraystatus.containsKey("1")) {
                    arraystatus.remove(1);
                    arraystatusvalue.remove(1);
                }


                notifiMyAdpterForStatusDp();

            } else {
                if (App_preference.getSharedprefInstance().getLoginRes().getRights().get(0).getIsJobRescheduleOrNot() != 1) {
                    if (!arraystatus.containsKey("0")) {
                        arraystatus.put("0", LanguageController.getInstance().getMobileMsgByKey(AppConstant.reschedule));
                        arraystatusvalue.put("0", LanguageController.getInstance().getMobileMsgByKey(AppConstant.reschedule));
                    }
                } else {
                    if (arraystatus.containsKey("0")) {
                        arraystatusvalue.remove(0);
                        arraystatus.remove(0);
                    }
                }

                if (App_preference.getSharedprefInstance().getLoginRes().getRights().get(0).getIsJobRevisitOrNot() != 1) {
                    if (!arraystatus.containsKey("1")) {
                        arraystatus.put("1", LanguageController.getInstance().getMobileMsgByKey(AppConstant.require_revisit));
                        arraystatusvalue.put("1", LanguageController.getInstance().getMobileMsgByKey(AppConstant.require_revisit));
                    }

                } else {
                    if (arraystatus.containsKey("1")) {
                        arraystatus.remove(1);
                        arraystatusvalue.remove(1);
                    }
                }

                addJobStatusInList();
            }
        }
    }


    //set job status name on button
    @Override
    synchronized public void setButtonsUI(JobStatusModel model) {

        HyperLog.i(TAG, "setButtonsUI(M) Update UI started");
        try {
            if (mParam2 != null)
                HyperLog.i(TAG, "setButtonsUI(M) " + new Gson().toJson(mParam2));
        } catch (Exception exception) {
            HyperLog.i(TAG, "setButtonsUI(M)" + exception.getMessage());
        }

        String status = model.getStatus_no();
        buttonDecline.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(status))
            HyperLog.i(TAG, "status is null");
        switch (status) {
            case AppConstant.New_On_Hold:
                buttonAccept.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_resume));
                buttonDecline.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_finish));
                textViewJobStatus.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.new_on_hold));
                showHideRescheduleRevisit(AppConstant.New_On_Hold);

                break;
            case AppConstant.Not_Started:
                buttonAccept.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.accept));
                buttonDecline.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reject));
                textViewJobStatus.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.status_new));
                if (App_preference.getSharedprefInstance().getLoginRes().getIsShowRejectStatus() != null
                        && App_preference.getSharedprefInstance().getLoginRes().getIsShowRejectStatus().equals("0")) {
                    buttonDecline.setVisibility(View.VISIBLE);
                } else {
                    buttonDecline.setVisibility(View.GONE);
                }

                showHideRescheduleRevisit(AppConstant.Not_Started);

                break;
            case AppConstant.Accepted:
                //hide travel start button when admin permission deny
                if (App_preference.getSharedprefInstance().getLoginRes() != null &&
                        App_preference.getSharedprefInstance().getLoginRes().getIsHideTravelBtn().equals("1")) {
                    buttonAccept.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_start));
                } else {
                    buttonAccept.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.travel_start));
                }
                buttonDecline.setVisibility(View.GONE);
                textViewJobStatus.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.accepted));

                showHideRescheduleRevisit(AppConstant.Accepted);
                break;
            case AppConstant.Reject:
                textViewJobStatus.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reject));

                showHideRescheduleRevisit(AppConstant.Reject);
                break;
            case AppConstant.Travelling:
                buttonAccept.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.break_key));
                buttonDecline.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.status_tr_fin_st));
                textViewJobStatus.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.travelling));

                showHideRescheduleRevisit(AppConstant.Travelling);
                break;
            case AppConstant.Break:
                buttonAccept.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.resume));
                buttonDecline.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.status_tr_fin_st));
                textViewJobStatus.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.break_key));

                showHideRescheduleRevisit(AppConstant.Break);
                break;
            case AppConstant.In_Progress:
                //   buttonAccept.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.on_hold));
                buttonAccept.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.new_on_hold));
                buttonDecline.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_finish));


                textViewJobStatus.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.In_progress));
                showHideRescheduleRevisit(AppConstant.In_Progress);

                break;
            case AppConstant.Pending:
                buttonAccept.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_resume));
                buttonDecline.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_finish));
                textViewJobStatus.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.on_hold));
//                textViewJobStatus.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.new_on_hold));
                showHideRescheduleRevisit(AppConstant.Pending);
                break;
            case AppConstant.Completed:
                accept_reject_linear.setVisibility(View.GONE);
                textViewJobStatus.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.completed));

                arraystatus.clear();
                arraystatusvalue.clear();
                showHideRescheduleRevisit(AppConstant.Completed);
                break;
            case AppConstant.Cancel:
                buttonAccept.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel));
                buttonDecline.setVisibility(View.GONE);
                textViewJobStatus.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel));
                showHideRescheduleRevisit(AppConstant.Cancel);
                break;
        }

        HyperLog.i(TAG, "setButtonsUI(M) completed.");

    }

    private void addJobStatusInList() {

        /*****Remove Rejected status when permission not allow for rejected status******/
        if (!textViewJobStatus.getText().toString().equals(LanguageController.getInstance().getMobileMsgByKey(AppConstant.completed))) {
            if (App_preference.getSharedprefInstance().getLoginRes().getIsShowRejectStatus() != null
                    && App_preference.getSharedprefInstance().getLoginRes().getIsShowRejectStatus().equals("0")) {
                arraystatusvalue.put("3", "3");
                arraystatus.put("3", LanguageController.getInstance().getMobileMsgByKey(AppConstant.reject));
            }
            arraystatusvalue.put("4", "4");
            arraystatusvalue.put("7", "7");
            arraystatusvalue.put("8", "8");
            arraystatusvalue.put("9", "9");
            arraystatusvalue.put("12", "12");

            arraystatus.put("4", LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel));
            arraystatus.put("7", LanguageController.getInstance().getMobileMsgByKey(AppConstant.In_progress));
            arraystatus.put("8", LanguageController.getInstance().getMobileMsgByKey(AppConstant.on_hold));
            arraystatus.put("9", LanguageController.getInstance().getMobileMsgByKey(AppConstant.completed));
            arraystatus.put("12", LanguageController.getInstance().getMobileMsgByKey(AppConstant.new_on_hold));
        }

        notifiMyAdpterForStatusDp();

    }

    private void notifiMyAdpterForStatusDp() {
        arrow_dp_icon.setVisibility(arraystatus.size() > 0 ? View.VISIBLE : View.GONE);
        int i = 0;
        statusArray = new String[arraystatus.size()];
        for (Map.Entry mapElement : arraystatus.entrySet()) {
            statusArray[i] = ((String) mapElement.getValue());
            i++;
        }
        int j = 0;
        statusArrayForIds = new String[arraystatusvalue.size()];
        for (Map.Entry mapElement : arraystatusvalue.entrySet()) {
            statusArrayForIds[j] = ((String) mapElement.getValue());
            j++;
        }


        if (mySpinnerAdapter != null) {
            mySpinnerAdapter.updtaeList(statusArray);
        }
    }

//    }


    @Override
    public void setResultForChangeInJob(String update, String jobid) {
        Intent intent = new Intent();
        intent.putExtra("JobID", jobid);
        getActivity().setResult(JobList.UPDATE, intent);
    }

    //Reset current job status
    @Override
    public void resetstatus(String status_no) {
        jobstatus.setStatus_name("");
        jobstatus.setStatus_no(status_no);
    }

    public void setDataToView() {

        EotEditor mEditor = layout.findViewById(R.id.editor);
        mEditor.setPlaceholder("Job Description");
        mEditor.setEditorFontSize(13);
        mEditor.setEditorFontColor(Color.GRAY);
        mEditor.setBackgroundColor(Color.TRANSPARENT);
        mEditor.setInputEnabled(false);
        if (!TextUtils.isEmpty(mParam2.getDes()))
            mEditor.setHtml(mParam2.getDes());

        if (TextUtils.isEmpty(mParam2.getInst()))
            layout.findViewById(R.id.cv_instruction).setVisibility(View.GONE);
        else
            layout.findViewById(R.id.cv_instruction).setVisibility(View.VISIBLE);


        if (TextUtils.isEmpty(mParam2.getDes()))
            layout.findViewById(R.id.cv_des).setVisibility(View.GONE);
        else
            layout.findViewById(R.id.cv_des).setVisibility(View.VISIBLE);


        getUpdatedLocation();
        setFwList();
        try {
            StringBuffer StrJt = new StringBuffer();
            List<JtId> jtaray = mParam2.getJtId();
            Iterator<JtId> it = jtaray.iterator();
            if (it.hasNext()) {
                StrJt.append(it.next().getTitle());
            }
            while (it.hasNext()) {
                StrJt.append(", ").append(it.next().getTitle());

            }
            StringBuilder sb = new StringBuilder();
            for (TagData item : mParam2.getTagData()) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append("#" + item.getTnm());
            }
            textViewTags.setText(sb.toString());
            try {
                if (textViewTags != null && textViewTags.getText().toString().equals(""))
                    tagcardView.setVisibility(View.GONE);
                else tagcardView.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.getMessage();
            }

            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString str1 = new SpannableString(
                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_code) + " : ");
            builder.append(str1);

            if (mParam2.getLabel() != null) {
                SpannableString str2 = new SpannableString(mParam2.getLabel());
                str2.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                        0, str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(str2);
            }

            textViewJobCode.setText(builder, TextView.BufferType.SPANNABLE);


            String endtime = "", startDatTime = "";
            if (mParam2.getSchdlStart() != null && !mParam2.getSchdlStart().equals("")) {
                try {
                    String timeFormate = AppUtility.dateTimeByAmPmFormate("dd-MMM-yyyy, hh:mm a", "dd-MMM-yyyy, kk:mm");

                    startDatTime = "Start : " + (AppUtility.getDateWithFormate2((Long.parseLong(mParam2.
                                    getSchdlStart()) * 1000),
                            timeFormate));
                } catch (Exception e) {
                    e.getMessage();
                }

            }
            if (mParam2.getSchdlFinish() != null && !mParam2.getSchdlFinish().equals("")) {
                try {
                    String timeFormate = AppUtility.dateTimeByAmPmFormate("dd-MMM-yyyy, hh:mm a", "dd-MMM-yyyy, kk:mm");
                    endtime = "\n" + "End  : " + (AppUtility.getDateWithFormate2((Long.parseLong(mParam2.
                                    getSchdlFinish()) * 1000),
                            timeFormate));//+" - "
                } catch (Exception e) {
                    e.getMessage();
                }

            }
            textViewTime.setText(startDatTime + endtime);


            if (mParam2.getPrty().equals("1"))
                textViewPriority.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.Low));
            else if (mParam2.getPrty().equals("2")) {
                textViewPriority.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.medium));
            } else
                textViewPriority.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.High));

            if (mParam2.getInst() != null)
                textViewInstruction.setText(mParam2.getInst());


            if (mParam2.getDes() != null) {
                Spannable spannableHtmlWithImageGetter = PicassoImageGetter.getSpannableHtmlWithImageGetter(textViewDescription,
                        mParam2.getDes());

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


            ArrayList<String> titleList = new ArrayList<>();
            if (mParam2.getJtId() != null) {
                for (JtId tempModel : mParam2.getJtId()) {
                    titleList.add(tempModel.getTitle());
                }
            }

            setUploadSignature();

            if (mParam2.getJtId() != null) {
                for (JtId jtildModel : mParam2.getJtId())
                    addJobServicesInChips(jtildModel);
            }
            notifiMyAdpterForStatusDp();


            showQuotesData();

        } catch (Exception e) {
            HyperLog.i("", "setDataToView(M) End  " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showQuotesData() {
        // quotes_details_card
        try {

            if (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsQuoteNoShowOnJob() != null
                    && App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsQuoteNoShowOnJob().equals("0")
                    && mParam2 != null && mParam2.getQuotLabel() != null && !mParam2.getQuotLabel().isEmpty()) {
                quotes_details_card.setVisibility(View.VISIBLE);
                quotes_details_number.setText(mParam2.getQuotLabel());
            } else {
                quotes_details_card.setVisibility(View.GONE);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void setUploadSignature() {
        try {
            if (mParam2 != null && mParam2.getJobId() != null && mParam2.getTempId() != null && mParam2.getJobId().equals(mParam2.getTempId())) {
                cardView_signature_pad.setVisibility(View.GONE);

            } else {
                if (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsJobCardEnableMobile()
                        != null && App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).
                        getIsJobCardEnableMobile().equals("0")) {
                    if (mParam2.getSignature() != null && !mParam2.getSignature().equals("")) {
                        cardView_signature_pad.setVisibility(View.VISIBLE);
                        Picasso.with(getActivity()).load(App_preference.getSharedprefInstance().getBaseURL() +
                                mParam2.getSignature()).placeholder(R.drawable.ic_profile).error(R.drawable.ic_profile)
                                .into(signature_img);
                    } else {
                        cardView_signature_pad.setVisibility(View.GONE);
                    }
                } else {
                    cardView_signature_pad.setVisibility(View.GONE);
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }

    }

    private void setFwList() {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            String kpr = mParam2.getKpr();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("", "");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("", "");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("", "");
        mMapView.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        check = System.currentTimeMillis();
        mMapView.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("", "");
    }


    private void showErrorDialog(String msg) {
        AppUtility.error_Alert_Dialog(getActivity(), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.quotes_details_card:
                reDirectOnQuotesDetails();
                break;
            case R.id.arrow_dp_icon:
                new_status_spinner.performClick();
                break;
            case R.id.btnStopRecurView:
                stopRecurpattern();
                break;
            case R.id.customfiled_btn:
                Intent intent1 = new Intent(getActivity(), CustomFiledListActivity.class);
                intent1.putExtra("jobId", mParam2.getJobId());
                intent1.putExtra("ansedit", SAVEANS);
                intent1.putExtra("AUDITCUSTOMFIELD", false);
                intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent1.putParcelableArrayListExtra("list", questionList);
                startActivityForResult(intent1, CUSTOMFILED);
                break;
            case R.id.btnComplationView:
                showComplationViewDialog();
                break;
            case R.id.buttonAccept:
                setButtonsAction(jobstatus.getStatus_no(), 1);
                break;
            case R.id.buttonDecline:
                if (jobstatus == null || jobstatus.getStatus_no() == null) {
                    mParam2 = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(param3);
                    jobstatus = new JobStatusModel(mParam2.getStatus(), jobDetail_pi.getStatusName(mParam2.getStatus()));
                }
                setButtonsAction(jobstatus.getStatus_no(), 2);
                break;
            case R.id.textViewAddress:
                if (mParam2 != null) {

                    //sample url for source to destination redirection: http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"
                    String locationdata = "";
                    if (!TextUtils.isEmpty(mParam2.getLat()) && !mParam2.getLat().equals("0.0") && !mParam2.getLng().equals("0.0")) {
                        locationdata = "http://maps.google.com/maps?daddr=" + mParam2.getLat() + "," + mParam2.getLng();
                    } else {
                        String completeAddress = mParam2.getAdr() + " " + mParam2.getCity()
                                + " " + SpinnerCountrySite.getStatenameById(mParam2.getCtry(), mParam2.getState()
                                + " " + SpinnerCountrySite.getCountryNameById(mParam2.getCtry())
                        );
                        locationdata = "http://maps.google.com/maps?daddr=" + completeAddress;
                    }
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(locationdata));
                        startActivity(intent);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_location), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return true;
                        }
                    });
                }
                break;
            case R.id.buttonMapView:
                if (mParam2 != null) {

                    //sample url for source to destination redirection: http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"
                    String locationdata = "";
                    if (!TextUtils.isEmpty(mParam2.getLat()) && !mParam2.getLat().equals("0") && !mParam2.getLng().equals("0")) {
                        locationdata = "http://maps.google.com/maps?daddr=" + mParam2.getLat() + "," + mParam2.getLng();
                    } else {
                        String completeAddress = mParam2.getAdr() + " " + mParam2.getCity() + " " + SpinnerCountrySite.getCountryNameById(mParam2.getCtry());
                        //    String searchableAddress = completeAddress.replace(" ", "+");
                        locationdata = "http://maps.google.com/maps?daddr=" + completeAddress;
                    }
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(locationdata));
                        //   intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_location), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return true;
                        }
                    });
                }
                break;
            case R.id.imageViewEmail:
                if (mParam2 != null) {
                    if (!mParam2.getEmail().equals("")) {
                        // frameView.setAlpha(0.3F);
                        getDialogEmail();
                        TextView txtViewSkypeCon1 = enterFieldDialog.findViewById(R.id.txt_email_popup);
                        final SpannableString s1 = new SpannableString(mParam2.getEmail().trim());
                        Linkify.addLinks(s1, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
                        txtViewSkypeCon1.setTextIsSelectable(true);
                        txtViewSkypeCon1.setMovementMethod(LinkMovementMethod.getInstance());
                        if (TextUtils.isEmpty(s1))
                            txtViewSkypeCon1.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_det_email));
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
                break;
            case R.id.buttonView:
                if (!mParam2.getDes().equals("")) {
                    //frameView.setAlpha(0.3F);
                    getDialogDes();
                    try {
                        TextView txtViewDeails = enterFieldDialog.findViewById(R.id.txtViewDeails);
                        txtViewDeails.setVisibility(View.VISIBLE);
                        txtViewDeails.setText(textViewDescription.getText());

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
                if (!mParam2.getInst().equals("")) {
                    //frameView.setAlpha(0.3F);
                    getDialogDes();
                    try {
                        TextView txtViewDeails = enterFieldDialog.findViewById(R.id.txtViewDeails);
                        txtViewDeails.setVisibility(View.VISIBLE);
                        txtViewDeails.setText(mParam2.getInst());

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
            case R.id.imageViewChat:
                if (mParam2 != null) {
                    if (mParam2.getSkype() != null || mParam2.getTwitter() != null) {
                        if (!mParam2.getSkype().equals("") || !mParam2.getTwitter().equals("")) {
                            // frameView.setAlpha(0.3F);
                            getDialog();
                            TextView txtViewSkypeCon1 = enterFieldDialog.findViewById(R.id.txtViewSkypeCon1);
                            txtViewSkypeCon1.setVisibility(View.VISIBLE);
                            txtViewSkypeCon1.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_chat));
                            if (mParam2.getSkype() != null) {
                                txtViewSkypeCon1.setText(mParam2.getSkype());
                            }
                            txtViewSkypeCon1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    openApp(getActivity(), "com.skype.raider", LanguageController.getInstance().getMobileMsgByKey(AppConstant.install_the_skype_app));

                                }
                            });

                            TextView txtViewSkypeCon2 = enterFieldDialog.findViewById(R.id.txtViewSkypeCon2);
                            txtViewSkypeCon2.setVisibility(View.VISIBLE);

                            if (mParam2.getTwitter() != null) {
                                if (TextUtils.isEmpty(mParam2.getTwitter())) {
                                    txtViewSkypeCon2.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_chat));
                                } else {
                                    txtViewSkypeCon2.setText(mParam2.getTwitter());
                                }

                                txtViewSkypeCon2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        openApp(getActivity(), "com.twitter.android", LanguageController.getInstance().getMobileMsgByKey(AppConstant.install_the_twitter_app));
                                    }
                                });
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
                            AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_avail), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    return true;
                                }
                            });
                        }
                    } else {
                        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_avail), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return true;
                            }
                        });
                    }
                }
                break;
            case R.id.imageViewCall:
                if (mParam2 != null) {
                    if (mParam2.getMob1() != null && !mParam2.getMob1().equals("") ||
                            mParam2.getMob2() != null && !mParam2.getMob2().equals("")) {
                        //frameView.setAlpha(0.3F);
                        getDialogCall();
                        try {
                            final TextView txtViewSkypeCon1 = enterFieldDialog.findViewById(R.id.txtViewSkypeCon1);
                            txtViewSkypeCon1.setVisibility(View.VISIBLE);
                            final SpannableString s1 = new SpannableString(mParam2.getMob1().trim());
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
                            final SpannableString s = new SpannableString(mParam2.getMob2().trim());
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

        }

    }

    private void reDirectOnQuotesDetails() {
        if (mParam2 != null && mParam2.getQuotId() != null) {
            Intent quotesinvoiceIntent = new Intent(getActivity(), Quote_Invoice_Details_Activity.class);
            quotesinvoiceIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            quotesinvoiceIntent.putExtra("quotId", mParam2.getQuotId());
            startActivity(quotesinvoiceIntent);
        }
    }


    private void stopRecurpattern() {
        if (AppUtility.isInternetConnected()) {
            AppUtility.alertDialog2(getActivity(),
                    "",
                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.delete_recur_msg),
                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.yes),
                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.no), new Callback_AlertDialog() {
                        @Override
                        public void onPossitiveCall() {
                            jobDetail_pi.stopRecurpattern(mParam2.getJobId());
                        }

                        @Override
                        public void onNegativeCall() {

                        }
                    });
        } else {
            showErrorDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.offline_feature_alert));
        }
    }

    private void copyAddressToClipBoard() {
        if (!TextUtils.isEmpty(textViewAddress.getText().toString())) {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Address Copied", textViewAddress.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity(), "Address Copied", Toast.LENGTH_SHORT).show();
        }
    }

    private void showComplationViewDialog() {
        String jobdata = new Gson().toJson(mParam2);
        Intent intent = new Intent(getActivity(), JobCompletionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("Complation", jobdata);
        startActivityForResult(intent, REQUEST_COMPLETION_NOTE);

    }


    private boolean openApp(Context context, String packageName, String msg) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                showAppInstallDialog(msg);
                return false;
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    void showAppInstallDialog(String msg) {
        EotApp.getAppinstance().showToastmsg(msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

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

    private void setButtonsAction(String status, int btn_id) {
        HyperLog.i("", "setButtonsAction(M) Start");
        if (App_preference.getSharedprefInstance().getLoginRes() != null &&
                App_preference.getSharedprefInstance().getLoginRes().getIsHideTravelBtn().equals("1") && status.equals(AppConstant.Accepted)) {
            //When Permission denny for fw travling start not show travel start status name on button
            //   jobstatus = JobStatus_Controller.getInstance().getStatusByButtonAction(AppConstant.Pending, 1);
            jobstatus = JobStatus_Controller.getInstance().getStatusByButtonAction(AppConstant.New_On_Hold, 1);
        } else {
            jobstatus = JobStatus_Controller.getInstance().getStatusByButtonAction(status, btn_id);
        }
        if (!buttonAccept.getText().toString().toLowerCase().contains("resume")) {
            HyperLog.i("", "Resume states case");
            //  ((JobDetailActivity) getActivity()).openFormForEvent(jobstatus.getStatus_no());
            try {
                HyperLog.i("", "Resume states found");
                ((JobDetailActivity) getActivity()).openFormForEvent(jobstatus.getStatus_no());
            } catch (Exception e) {
                e.getMessage();
                HyperLog.i("", "Resume states Exception handle" + e.getMessage());
                try {
                    if (jobstatus == null) {
                        HyperLog.i("", "Resume Job states not found in json " + e.getMessage());
                        if (mParam2 == null && param3 != null && !param3.equals("")) {
                            HyperLog.i("", "Job Data Not found");
                            HyperLog.i("", "Job Id" + param3);
                            mParam2 = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(param3);
                        } else if (mParam2.getStatus() != null && !mParam2.getStatus().equals("")) {
                            HyperLog.i("", "Find Job status from Json file in Job status resume condition..");
                            jobstatus = new JobStatusModel(mParam2.getStatus(), jobDetail_pi.getStatusName(mParam2.getStatus()));
                        } else {
                            mParam2 = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(param3);
                            jobstatus = new JobStatusModel(mParam2.getStatus(), jobDetail_pi.getStatusName(mParam2.getStatus()));
                        }
                        ((JobDetailActivity) getActivity()).openFormForEvent(jobstatus.getStatus_no());
                    } else {
                        HyperLog.i("", "Job states not null");
                        mParam2 = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(param3);
                        jobstatus = new JobStatusModel(mParam2.getStatus(), jobDetail_pi.getStatusName(mParam2.getStatus()));
                        ((JobDetailActivity) getActivity()).openFormForEvent(jobstatus.getStatus_no());
                    }
                } catch (Exception exception) {
                    exception.getMessage();
                    HyperLog.i("", "Exception" + exception.getMessage());
                    mParam2 = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(param3);
                    jobstatus = new JobStatusModel(mParam2.getStatus(), jobDetail_pi.getStatusName(mParam2.getStatus()));
                    ((JobDetailActivity) getActivity()).openFormForEvent(jobstatus.getStatus_no());
                }


            }

        } else {
            onFormSuccess();
        }
        HyperLog.i("", "setButtonsAction(M) Stop");
    }


    @Override
    public void onFormSuccess() {
        HyperLog.i(TAG, "onFormSuccess(M) Start");
        if (jobstatus != null) {
            if (!jobstatus.getStatus_no().equals(AppConstant.Not_Started)) {
                contact_card.setVisibility(View.VISIBLE);
            }
            if (App_preference.getSharedprefInstance().getLoginRes().getConfirmationTrigger() != null) {
                if (jobstatus != null && jobstatus.getStatus_no() != null && App_preference.getSharedprefInstance().getLoginRes().getConfirmationTrigger().contains(jobstatus.getStatus_no())) {
                    showDialogForSendMailToClt();
                } else {
                    isMailSentToClt = "1";
                    updateStatusApiCall();
                }
            } else {
                isMailSentToClt = "1";
                updateStatusApiCall();
            }

        } else {

        }
        HyperLog.i(TAG, "onFormSuccess(M) Stop");
    }


    synchronized private void updateStatusApiCall() {
        jobDetail_pi.changeJobStatusAlertInvisible(mParam2.getJobId(), mParam2.getType(), jobstatus, LatLngSycn_Controller.getInstance().getLat(), LatLngSycn_Controller.getInstance().getLng(), isMailSentToClt);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //#Eye09693 - {Android} Status entry issue from mobile side
            }
        }, 100);
    }

    private void showDialogForSendMailToClt() {
        AppUtility.alertDialog2(getActivity(),
                "",
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.send_client_mail),
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.yes),
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.no), new Callback_AlertDialog() {
                    @Override
                    public void onPossitiveCall() {
                        isMailSentToClt = "1";
                        updateStatusApiCall();
                    }

                    @Override
                    public void onNegativeCall() {
                        isMailSentToClt = "0";
                        updateStatusApiCall();
                    }
                });
    }

    @Override
    public void onFormError() {
        jobDetail_pi.setJobCurrentStatus(mParam2.getJobId());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_COMPLETION_NOTE) {
            try {
                if (data != null && data.hasExtra("note")) {
                    if (mParam2 != null)
                        mParam2.setComplNote(data.getStringExtra("note"));
                    complation_notes.setText(data.getStringExtra("note"));
                    btnComplationView.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.edit));

                } else
                    btnComplationView.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_RESCHEDULE) {
            try {
                if (data != null && data.hasExtra("stime")) {
                    if (mParam2 != null) {
                        mParam2.setSchdlStart(data.getStringExtra("stime"));
                        mParam2.setSchdlFinish(data.getStringExtra("etime"));
                        String[] formated_date = AppUtility.getFormatedTime(mParam2.getSchdlStart());
                        textViewTime.setText(formated_date[1] + formated_date[2]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CUSTOMFILED) {
            getUpdateForm();
        }
    }

    public void setCompletionNotes(String data) {
        if (mParam2 != null)
            mParam2.setComplNote(data);
        if (complation_notes != null && btnComplationView != null) {
            complation_notes.setText(data);
            btnComplationView.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.edit));
        }
    }

    /**
     * inflate custom view accroding question type
     */

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
                                                        AppUtility.dateTimeByAmPmFormate("hh:mm a", "kk:mm"));
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
                                                String dateConvert = AppUtility.getDate(dateLong, AppUtility.dateTimeByAmPmFormate("dd-MMM-yyyy hh:mm a", "dd-MMM-yyyy kk:mm"));
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

    @Override
    public void sessionExpire(String msg) {
        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.drawable.ic_launcher);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new LoadImage().execute(source, d);
        return d;
    }


    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.d(TAG, "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d(TAG, "onPostExecute drawable " + mDrawable);
            Log.d(TAG, "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                CharSequence t = textViewDescription.getText();
                textViewDescription.setText(t);
            }
        }
    }


}
