package com.eot_app.nav_menu.jobs.job_detail.job_equipment.job_equ_remrk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.UploadDocumentActivity;
import com.eot_app.lat_lng_sync_pck.LatLngSycn_Controller;
import com.eot_app.locations.LocationTracker;
import com.eot_app.locations.OnLocationUpdate;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.EquipmentStatus;
import com.eot_app.nav_menu.audit.audit_list.equipment.remark.RemarkCustomFormFragment;
import com.eot_app.nav_menu.audit.audit_list.equipment.remark.RemarkQuestionListAdpter;
import com.eot_app.nav_menu.audit.audit_list.equipment.remark.remark_mvp.RemarkRequest;
import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.chat.img_crop_pkg.ImageCropFragment;
import com.eot_app.nav_menu.jobs.job_detail.customform.MyAttachment;
import com.eot_app.nav_menu.jobs.job_detail.customform.cstm_form_model.CustomFormList_Res;
import com.eot_app.nav_menu.jobs.job_detail.documents.DocumentListAdapter;
import com.eot_app.nav_menu.jobs.job_detail.documents.PathUtils;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.MyFormInterFace;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model.Ans_Req;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model.Answer;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.form_ques_mvp.Que_View;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.form_ques_mvp.Que_pi;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.form_ques_mvp.Qus_pc;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.AnswerModel;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.QuesGetModel;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.QuesRspncModel;
import com.eot_app.nav_menu.jobs.job_detail.job_audit_remark_attchment_pkg.SelectedImageActivity;
import com.eot_app.nav_menu.jobs.job_detail.job_audit_remark_attchment_pkg.model_pkg.JobAuditSingleAttchReqModel;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.job_equ_remrk.job_equ_mvp.JobEquRemark_PC;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.job_equ_remrk.job_equ_mvp.JobEquRemark_PI;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.job_equ_remrk.job_equ_mvp.JobEquRemark_View;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.MySpinnerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public
class JobEquRemarkRemarkActivity extends UploadDocumentActivity implements
        View.OnClickListener, JobEquRemark_View, ImageCropFragment.MyDialogInterface, DocumentListAdapter.FileDesc_Item_Selected
        , Que_View, MyFormInterFace, MyAttachment, RadioGroup.OnCheckedChangeListener {
    public static final int SINGLEATTCHMENT = 365;

    private final ArrayList<Answer> answerArrayList = new ArrayList<>();
    private final List<MultipartBody.Part> docAns = new ArrayList<>();
    private final List<MultipartBody.Part> signAns = new ArrayList<>();
    private final ArrayList<String> signQueIdArray = new ArrayList<>();
    private final ArrayList<String> docQueIdArray = new ArrayList<>();
    LocationTracker locationTracker;
    String path = "", jobId = "";
    FragmentTransaction ft;
    RemarkCustomFormFragment myfragment;
    int position = 0;
    String titleNm = "";
    int type = 1;
    private ArrayList<CustomFormList_Res> customFormLists = new ArrayList<>();
    private EquArrayModel equipment;
    private Button button_submit;
    private AppCompatTextView tv_label_condition, tv_label_remark, tv_label_attachment, click_here_txt, upload_lable, tv_label_customForm_que;
    private EditText edit_remarks;
    private Spinner status_spinner;
    private TextView status_label;
    private LinearLayout audit_status_relative;
    private TextView tv_equipment_name, tv_location;
    private JobEquRemark_PI jobEquimPi;
    private int selectedCondition = -1;
    private boolean REMARK_SUBMIT = false;

    private RecyclerView recyclerView_attachment, recyclerView_customForm;
    private LinearLayout upload_lable_layout, formLayout;
    private List<EquipmentStatus> equipmentStatusList = new ArrayList<>();
    private Que_pi queAns_pi;
    private List<QuesRspncModel> quesRspncModelList = new ArrayList<>();
    private RemarkQuestionListAdpter questionListAdapter;
    private boolean isMandatoryNotFill;
    private ImageView attchmentView, deleteAttchment;
    private Button addAttchment;
    private boolean isTagSet = false;
    private RadioGroup rediogrp;
    private RadioButton radio_before, radio_after;
    private RelativeLayout image_with_tag;
    private TextView image_txt, chip_txt;
    private ImageView deleteChip;
    private View chip_layout;
    private DocumentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_equ_remark);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        listeners();
        setLanguage();

        setData();

        getCustomFormSLists();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_before:
                isTagSet = true;
                image_txt.setVisibility(View.VISIBLE);
                image_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.before));
                rediogrp.setVisibility(View.GONE);
                chip_layout.setVisibility(View.VISIBLE);
                chip_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.before));

                break;
            case R.id.radio_after:
                isTagSet = true;
                image_txt.setVisibility(View.VISIBLE);
                image_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.after));
                rediogrp.setVisibility(View.GONE);
                chip_layout.setVisibility(View.VISIBLE);
                chip_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.after));
                break;
        }

    }

    @Override
    public void getAnsId(String ansId) {
        if (customFormLists.size() > 0) {
            QuesGetModel quesGetModel = new QuesGetModel(ansId, customFormLists.get(0).getFrmId(),
                    ""
                    , equipment.getAudId(), equipment.getEquId());
            queAns_pi.getQuestions(quesGetModel);
        }
    }

    private void getCustomFormSLists() {
        Job auditmodel = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(equipment.getAudId());
        if (auditmodel != null) {
            ArrayList<String> jTitleId = null;
            try {
                jTitleId = new ArrayList<>();
                if (auditmodel.getEquArray() != null) {
                    jTitleId.add(equipment.getEcId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            jobEquimPi.getCustomFormList(auditmodel, jTitleId);
        }
    }

    @Override
    public void questionlist(List<QuesRspncModel> quesRspncModelList) {
        Log.e("", "");
        this.quesRspncModelList = quesRspncModelList;
        if (quesRspncModelList.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView_customForm.setLayoutManager(linearLayoutManager);
            questionListAdapter = new RemarkQuestionListAdpter((ArrayList<QuesRspncModel>) quesRspncModelList, this,
                    this, this);
            recyclerView_customForm.setAdapter(questionListAdapter);
            isFormPreFilled();
        }
    }

    private void isFormPreFilled() {
        if (questionListAdapter != null && questionListAdapter.getTypeList() != null) {
            ArrayList<QuesRspncModel> checkList = questionListAdapter.getTypeList();
            for (QuesRspncModel qm : checkList) {
                List<AnswerModel> ans = qm.getAns();
                if (ans != null && ans.size() > 0) {
                    AnswerModel model = ans.get(0);
                    if (model != null && !TextUtils.isEmpty(model.getValue())) {
                        //  isfilled = true;
                    }
                }
            }
        }
    }

    @Override
    public void onSubmitSuccess(String msg) {

    }

    @Override
    public void addfragmentDynamically(List<QuesRspncModel> quesRspncModelList) {
        Log.e("", "");
        if (!quesRspncModelList.isEmpty()) {
            String queList = new Gson().toJson(quesRspncModelList);
            ft = getSupportFragmentManager().beginTransaction();
            myfragment = RemarkCustomFormFragment.newInstance("JobEquRemarkRemarkActivity", queList);
            ft.add(R.id.framlayout, myfragment, "Fragment add successFully.....").addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack("close");
            ft.commit();
            setTitlesForRemarkForm(true);
        }
    }

    private void removeTagData() {
        radio_after.setChecked(false);
        radio_before.setChecked(false);
        isTagSet = false;
        image_txt.setVisibility(View.GONE);
        image_txt.setText("");
        rediogrp.setVisibility(View.VISIBLE);
        chip_layout.setVisibility(View.GONE);
        chip_txt.setText("");

    }

    public void setTitlesForRemarkForm(boolean actionBar) {
        if (actionBar) {
            if (customFormLists != null && customFormLists.size() > 0)
                setTitle(customFormLists.get(0).getFrmnm());
        } else {
            setTitles();
        }
    }

    @Override
    public void showOfflineAlert(String msg) {

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

    private void createEquipmentForJobAudit(RemarkRequest remarkRequest) {
        jobEquimPi.addNewRemark(remarkRequest, path, docAns, docQueIdArray, signAns, signQueIdArray);
    }

    private void processImageAndUpload(RemarkRequest remarkRequest) {
        path = saveBitMap(image_with_tag);
        createEquipmentForJobAudit(remarkRequest);
    }

    @Override
    public void finishMuAvtivity() {

    }

    @Override
    public void formNotFound() {
        formLayout.setVisibility(View.GONE);
    }

    @Override
    public void finishErroroccur() {

    }

    private void initViews() {
        upload_lable_layout = findViewById(R.id.upload_lable_layout);
        tv_label_condition = findViewById(R.id.tv_label_condition);
        tv_label_remark = findViewById(R.id.tv_label_remark);
        edit_remarks = findViewById(R.id.edit_remarks);
        button_submit = findViewById(R.id.button_submit);
        status_spinner = findViewById(R.id.status_spinner);
        audit_status_relative = findViewById(R.id.audit_status_relative);
        status_label = findViewById(R.id.status_label);
        recyclerView_attachment = findViewById(R.id.recyclerView_attachment);

        tv_equipment_name = findViewById(R.id.tv_equipment_name);
        tv_location = findViewById(R.id.tv_location);

        tv_label_attachment = findViewById(R.id.tv_label_attachment);
        click_here_txt = findViewById(R.id.click_here_txt);
        upload_lable = findViewById(R.id.upload_lable);

        AppUtility.spinnerPopUpWindow(status_spinner);
        status_spinner.setSelected(false);


        equipmentStatusList = App_preference.getSharedprefInstance().getLoginRes().getEquipmentStatus();
        if (equipmentStatusList != null) {
            String[] statusList = new String[equipmentStatusList.size()];
            int i = 0;
            for (EquipmentStatus status : equipmentStatusList) {
                statusList[i] = status.getStatusText();
                i++;
            }
            status_spinner.setAdapter(new MySpinnerAdapter(this, statusList));
        }


        formLayout = findViewById(R.id.formLayout);
        recyclerView_customForm = findViewById(R.id.recyclerView_customForm);
        tv_label_customForm_que = findViewById(R.id.tv_label_customForm_que);

        audit_status_relative.setOnClickListener(this);
        button_submit.setOnClickListener(this);

        upload_lable_layout.setOnClickListener(this);

        jobEquimPi = new JobEquRemark_PC(this);


        image_with_tag = findViewById(R.id.image_with_tag);
        image_txt = findViewById(R.id.image_txt);

        radio_before = findViewById(R.id.radio_before);
        radio_before.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.before));
        radio_before.setChecked(false);

        radio_after = findViewById(R.id.radio_after);
        radio_after.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.after));
        radio_after.setChecked(false);


        chip_txt = findViewById(R.id.chip_txt);
        deleteChip = findViewById(R.id.deleteChip);
        chip_layout = findViewById(R.id.chip_layout);
        deleteChip.setOnClickListener(this);
        rediogrp = findViewById(R.id.rediogrp);
        rediogrp.setOnCheckedChangeListener(this);

    }

    /**
     * Set Form Question's
     *****/
    public void setList(ArrayList<CustomFormList_Res> customFormLists) {
        this.customFormLists = customFormLists;
        formLayout.setVisibility(View.VISIBLE);
        if (customFormLists.size() > 0) {

            queAns_pi = new Qus_pc(this);
            QuesGetModel quesGetModel = new QuesGetModel("-1", customFormLists.get(0).getFrmId(),
                    "", equipment.getAudId(), equipment.getEquId());

            queAns_pi.getQuestions(quesGetModel);
        }
    }

    @Override
    public void onClickContinuarEvent(Uri uri) {
        String path = "";
        path = PathUtils.getRealPath(this, uri);
        if (!path.isEmpty()) {
            if (type == 1) {
                uploadRemarkAttchment(path);
            } else if (type == 2) {
                questionListAdapter.showAttchmentView(position, path, attchmentView, deleteAttchment, addAttchment);

            }
        }
    }

//    /*Picker for attachemnt*/
//    private void selectAttachment() {
//        final BottomSheetDialog dialog = new BottomSheetDialog(JobEquRemarkRemarkActivity.this);
//        dialog.setContentView(R.layout.bottom_image_chooser);
//        TextView camera = dialog.findViewById(R.id.camera);
//        camera.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.camera));
//        TextView gallery = dialog.findViewById(R.id.gallery);
//        gallery.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.gallery));
//        TextView drive_document = dialog.findViewById(R.id.drive_document);
//        drive_document.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.document));
//        camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (AppUtility.askCameraTakePicture(JobEquRemarkRemarkActivity.this)) {
//                    takePictureFromCamera();
//                }
//                dialog.dismiss();
//            }
//
//        });
//
//        gallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (AppUtility.askGalaryTakeImagePermiSsion(JobEquRemarkRemarkActivity.this)) {
//                    getImageFromGallray();
//                }
//                dialog.dismiss();
//            }
//
//
//        });
//
//        drive_document.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (AppUtility.askGalaryTakeImagePermiSsion(JobEquRemarkRemarkActivity.this)) {
//                    takeimageFromGalary();//only for drive documents
//                }
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }
//
//    private void getImageFromGallray() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(galleryIntent, CAPTURE_IMAGE_GALLARY);
//    }

    private void setLanguage() {
        tv_label_condition.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.condition) + " *");
        tv_label_remark.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.remark));
        edit_remarks.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.remark_msg));
        button_submit.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.submit_btn));
        status_label.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.condition));
        tv_label_attachment.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.attachment));
        upload_lable.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_upload));
        click_here_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.doc_here));
    }

    private void listeners() {
        status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCondition = position;

                String selectedValue = equipmentStatusList.get(position).getStatusText();
                status_label.setText(selectedValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

//    private void takePictureFromCamera() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                Log.e("", ex.getMessage());
//
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.eot_app.provider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                try {
//                    startActivityForResult(takePictureIntent, CAMERA_CODE);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//    private void takeimageFromGalary() {
//        //allow upload file extension
//        String[] mimeTypes = {"image/jpeg", "image/jpg", "image/png",
//                "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",//.doc & .docx
//                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",//.xls & .xlsx
//                "application/pdf",//pdf
//                "text/csv", "text/plain"//csv
//        };
//
///**only for document uploading */
//        Intent documentIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        documentIntent.addCategory(Intent.CATEGORY_OPENABLE);
//        documentIntent.setType("*/*");
//        documentIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//        documentIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//        startActivityForResult(documentIntent, ATTACHFILE_CODE);
//    }

//    private File createImageFile() throws IOException {
//        Calendar calendar = Calendar.getInstance();
//        long imageFileName = calendar.getTime().getTime();
//
//        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);// return path
//
//        File directoryPath = new File(storageDir.getPath());
//        File image = File.createTempFile(
//                String.valueOf(imageFileName),  /* prefix */
//                ".jpg",         /* suffix */
//                directoryPath   /* directory */
//        );
//        captureImagePath = image.getAbsolutePath();
//        return new File(image.getPath());
//    }

//
//    private void imageEditing(Uri uri, String dialogTag) {
//        Intent intent = new Intent(JobEquRemarkRemarkActivity.this, ActivityEditImageDialog.class);
//        intent.putExtra("uri", uri);
//        intent.putExtra("allowOffline", true);
//        startActivityForResult(intent, PHOTO_EDIT_CODE);
//    }

//    private void imageCroping(final Uri uri) {
//        try {
//            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    ImageCropFragment myfragment = ImageCropFragment.newInstance("Uri", uri.toString());
//                    myfragment.setCallbackListener(JobEquRemarkRemarkActivity.this);
//                    myfragment.setCallbackListener(new ImageCropFragment.MyDialogInterface() {
//                        @Override
//                        public void onClickContinuarEvent(Uri permisoRequerido) {
//                            path = PathUtils.getRealPath(getApplicationContext(), uri);
//                            if (!path.isEmpty()) {
//                                File file = new File(path);
//                                if (file != null && file.exists()) {
//                                    img_attachment.setVisibility(View.VISIBLE);
//                                    Picasso.with(getApplicationContext()).load(file).into(img_attachment);
//                                    removeTagData();
//                                    uploadRemarkAttchment();
//                                }
//                            }
//                        }
//                    });
//                    myfragment.show(getSupportFragmentManager().beginTransaction(), "AddExpenseActivity");
//                }
//            }, 100);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//    }

    private void uploadRemarkAttchment(String path) {
        if (jobId != null && equipment.getEquId() != null) {
            JobAuditSingleAttchReqModel model = new JobAuditSingleAttchReqModel(jobId, equipment.getEquId(),
                    button_submit.getText().toString(), titleNm, path, "1");
            String str = new Gson().toJson(model);
            Intent intent = new Intent(this, SelectedImageActivity.class);
            intent.putExtra("attchment", str);
            startActivityForResult(intent, SINGLEATTCHMENT);
        }

    }

//    private int getFileIcons(String serverFilePath) {
//        int resId = 0;
//        String ext = serverFilePath.substring((serverFilePath.lastIndexOf(".")) + 1);
//
//        if (!ext.isEmpty()) {
//            if (ext.equals("doc") || ext.equals("docx")) {
//                resId = R.drawable.word;
//            } else if (ext.equals("pdf")) {
//                resId = R.drawable.pdf;
//            } else if (ext.equals("xlsx") || ext.equals("xls")) {
//                resId = R.drawable.excel;
//
//            } else if (ext.equals("csv")) {
//                resId = R.drawable.csv;
//            } else {
//                resId = R.drawable.doc;
//            }
//        }
//        return resId;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deleteChip:
                isTagSet = false;
                image_txt.setText("");
                image_txt.setVisibility(View.GONE);
                chip_layout.setVisibility(View.GONE);
                rediogrp.setVisibility(View.VISIBLE);
                radio_after.setChecked(false);
                radio_before.setChecked(false);
                break;
            case R.id.upload_lable_layout:
                //      uploadRemarkAttchment();
                type = 1;
                selectFile(false);

                //      selectAttachment();
                break;
            case R.id.audit_status_relative:
                status_spinner.performClick();
                break;

            case R.id.button_submit:
                if (selectedCondition == -1) {
                    AppUtility.alertDialog((this), LanguageController.getInstance().
                                    getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey
                                    (AppConstant.status_required), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                            "", new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    return null;
                                }
                            });
                } else {
                    ansAnsQuesRspncModel();

                    /**    if question is mandatory but not fill   ***/
                    if (isMandatoryNotFill) {
                        isMandatoryNotFill = false;
                        AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.fill_all_mandatory_questions), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return null;
                            }
                        });
                        return;
                    }


                    String fomrId = "";
                    if (customFormLists.size() > 0) {
                        fomrId = customFormLists.get(0).getFrmId();
                    }
                    if (answerArrayList.size() > 0) {
                        Ans_Req ans_req = new Ans_Req(App_preference.getSharedprefInstance().getLoginRes().getUsrId(),
                                answerArrayList, fomrId, equipment.getAudId());
                        createNewRemarkRequest(ans_req);
                    } else {
                        createNewRemarkRequest(new Ans_Req());
                    }
                }

                break;
        }
    }


    /**
     * get Form Answer's
     *****/
    public void ansAnsQuesRspncModel() {
        for (int i = 0; i < quesRspncModelList.size(); i++) {
            String key = "";
            String ans = "";
            ArrayList<AnswerModel> ansArrayList = new ArrayList<>();
            Answer answer = null;
            switch (quesRspncModelList.get(i).getType()) {
                case "11":
                    if (quesRspncModelList.get(i).getAns().size() > 0) {
                        ans = quesRspncModelList.get(i).getAns().get(0).getValue();
                        if (quesRspncModelList.get(i).getMandatory().equals("1"))
                            if (TextUtils.isEmpty(ans))
                                isMandatoryNotFill = true;

                        String mimeType = "";
                        MultipartBody.Part body = null;
                        File file = new File(ans);

                        if (file != null && file.exists()) {
                            mimeType = URLConnection.guessContentTypeFromName(file.getName());
                            if (mimeType == null) {
                                mimeType = file.getName();
                            }
                            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
                            // MultipartBody.Part is used to send also the actual file name
                            body = MultipartBody.Part.createFormData("docAns[]", file.getName()
                                    , requestFile);//ans.substring(ans.lastIndexOf(".")
                            docAns.add(body);
                            docQueIdArray.add(quesRspncModelList.get(i).getQueId());

                            AnswerModel answerModels = new AnswerModel("0", ans);
                            ansArrayList.add(answerModels);
                            answer = new Answer(this.quesRspncModelList.get(i).getQueId(),
                                    this.quesRspncModelList.get(i).getType(), ansArrayList, this.quesRspncModelList.get(i).getFrmId());
                            answerArrayList.add(answer);

                        } else {
                         /*   AnswerModel answerModels = new AnswerModel("0", "");
                            ansArrayList.add(answerModels);
                            answer = new Answer(this.quesRspncModelList.get(i).getQueId(),
                                    this.quesRspncModelList.get(i).getType(), ansArrayList,
                                    this.quesRspncModelList.get(i).getFrmId());
                            answerArrayList.add(answer);*/
                        }

                    } else if (quesRspncModelList.get(i).getAns().size() == 0)
                        if (quesRspncModelList.get(i).getMandatory().equals("1"))
                            isMandatoryNotFill = true;
                    break;
                /***case for Signature****/
                case "10":
                    Log.e("", "");
                    if (quesRspncModelList.get(i).getAns().size() > 0) {
                        ans = quesRspncModelList.get(i).getAns().get(0).getValue();
                        if (quesRspncModelList.get(i).getMandatory().equals("1"))
                            if (TextUtils.isEmpty(ans))
                                isMandatoryNotFill = true;

                        String mimeType = "";
                        MultipartBody.Part body = null;
                        File file = new File(ans);

                        if (file != null && file.exists()) {
                            mimeType = URLConnection.guessContentTypeFromName(file.getName());
                            if (mimeType == null) {
                                mimeType = file.getName();
                            }
                            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
                            // MultipartBody.Part is used to send also the actual file name
                            body = MultipartBody.Part.createFormData("signAns[]", file.getName()
                                    , requestFile);//ans.substring(ans.lastIndexOf(".")
                            signAns.add(body);
                            signQueIdArray.add(quesRspncModelList.get(i).getQueId());

                            AnswerModel docanswerModels = new AnswerModel("0", ans);
                            ansArrayList.add(docanswerModels);
                            answer = new Answer(this.quesRspncModelList.get(i).getQueId(),
                                    this.quesRspncModelList.get(i).getType(), ansArrayList, this.quesRspncModelList.get(i).getFrmId());
                            answerArrayList.add(answer);
                        } else {
                          /*  AnswerModel answerModels = new AnswerModel("0", "");
                            ansArrayList.add(answerModels);
                            answer = new Answer(this.quesRspncModelList.get(i).getQueId(),
                                    this.quesRspncModelList.get(i).getType(), ansArrayList,
                                    this.quesRspncModelList.get(i).getFrmId());
                            answerArrayList.add(answer);*/
                        }
                    } else if (quesRspncModelList.get(i).getAns().size() == 0)
                        if (quesRspncModelList.get(i).getMandatory().equals("1"))
                            isMandatoryNotFill = true;

                    break;


                case "8":
                    if (quesRspncModelList.get(i).getAns() != null && quesRspncModelList.get(i).getAns().size() > 0) {
                        ans = quesRspncModelList.get(i).getAns().get(0).getValue();
                        AnswerModel answerModel = new AnswerModel(quesRspncModelList.get(i).getAns().get(0).getKey(), quesRspncModelList.get(i).getAns().get(0).getValue());
                        ansArrayList.add(answerModel);
                        answer = new Answer(this.quesRspncModelList.get(i).getQueId(),
                                this.quesRspncModelList.get(i).getType(), ansArrayList, quesRspncModelList.get(i).getFrmId());
                        answerArrayList.add(answer);
                    }

                    if (quesRspncModelList.get(i).getMandatory().equals("1"))
                        if (ans.equals("0"))
                            isMandatoryNotFill = true;
                    break;
                case "2":
                case "5":
                case "6":
                case "7":
                case "1":
                    if (quesRspncModelList.get(i).getAns() != null && quesRspncModelList.get(i).getAns().size() > 0) {
                        if (quesRspncModelList.get(i).getType().equals("5")) {
                            if (!TextUtils.isEmpty(quesRspncModelList.get(i).getAns().get(0).getValue())) {
                                long l = Long.parseLong(quesRspncModelList.get(i).getAns().get(0).getValue());
                                String date = AppUtility.getDate(l, "dd-MMM-yyyy");
                                ans = date;
                            }
                        } else if (quesRspncModelList.get(i).getType().equals("6")) {
                            if (!TextUtils.isEmpty(quesRspncModelList.get(i).getAns().get(0).getValue())) {
                                long l = Long.parseLong(quesRspncModelList.get(i).getAns().get(0).getValue());
                                String date = AppUtility.getDate(l,
                                        AppUtility.dateTimeByAmPmFormate("hh:mm a", "kk:mm"));
                                ans = date;
                            }
                        } else if (quesRspncModelList.get(i).getType().equals("7")) {
                            if (!TextUtils.isEmpty(quesRspncModelList.get(i).getAns().get(0).getValue())) {
                                long l = Long.parseLong(quesRspncModelList.get(i).getAns().get(0).getValue());
                                String date = AppUtility.getDate(l, AppUtility.dateTimeByAmPmFormate(
                                        "dd-MMM-yyyy hh:mm a", "dd-MMM-yyyy kk:mm"));
                                ans = date;
                            }
                        } else
                            ans = quesRspncModelList.get(i).getAns().get(0).getValue();
                        if (quesRspncModelList.get(i).getMandatory().equals("1"))
                            if (TextUtils.isEmpty(ans))
                                isMandatoryNotFill = true;

                        AnswerModel answerModel = new AnswerModel(key, ans);
                        ansArrayList.add(answerModel);
                        answer = new Answer(this.quesRspncModelList.get(i).getQueId(),
                                this.quesRspncModelList.get(i).getType(), ansArrayList,
                                customFormLists.get(0).getFrmId());
                        answerArrayList.add(answer);

                    } else if (quesRspncModelList.get(i).getMandatory().equals("1")) {
                        isMandatoryNotFill = true;
                    }
                    break;
                case "4":
                case "3":
                    if (quesRspncModelList.get(i).getAns() != null && quesRspncModelList.get(i).getAns().size() > 0) {
                        List<AnswerModel> ans1 = quesRspncModelList.get(i).getAns();
                        if (ans1 != null)
                            for (AnswerModel am : ans1) {
                                key = am.getKey();
                                ans = am.getValue();
                                AnswerModel answerModel = new AnswerModel(key, ans);
                                ansArrayList.add(answerModel);
                                if (quesRspncModelList.get(i).getMandatory().equals("1"))
                                    if (TextUtils.isEmpty(ans))
                                        isMandatoryNotFill = true;
                            }
                    }
                    if (ansArrayList.size() > 0) {
                        answer = new Answer(this.quesRspncModelList.get(i).getQueId(),
                                this.quesRspncModelList.get(i).getType(), ansArrayList
                                , customFormLists.get(0).getFrmId());
                        answerArrayList.add(answer);
                    } else if (quesRspncModelList.get(i).getMandatory().equals("1")) {
                        isMandatoryNotFill = true;
                    } else {
                        answer = new Answer(this.quesRspncModelList.get(i).getQueId(),
                                this.quesRspncModelList.get(i).getType(), ansArrayList, customFormLists.get(0).getFrmId());
                        answerArrayList.add(answer);
                    }
                    break;
            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 145) {
            if (grantResults != null && grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (locationTracker != null) {
                    locationTracker.getCurrentLocation();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void getUpdatedLocation() {
        locationTracker = new LocationTracker(this, new OnLocationUpdate() {
            @Override
            public void OnContinue(boolean isLocationUpdated, boolean isPermissionAllowed) {
                if (isPermissionAllowed) {
                    locationTracker.getCurrentLocation();
                } else {
                    ActivityCompat.requestPermissions(JobEquRemarkRemarkActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            145);
                }
            }
        });

    }

    private void createNewRemarkRequest(Ans_Req ans_req) {
        RemarkRequest remarkRequest = new RemarkRequest();
        remarkRequest.setAnswerArray(ans_req);
        remarkRequest.setAudId(equipment.getAudId());
        remarkRequest.setEquId(equipment.getEquId());
        remarkRequest.setRemark(edit_remarks.getText().toString());
        remarkRequest.setIsJob("1");
        remarkRequest.setStatus(selectedCondition + 1 + "");

        remarkRequest.setUsrId(App_preference.getSharedprefInstance().getLoginRes().getUsrId());

        if (LatLngSycn_Controller.getInstance().getLat() == null)
            remarkRequest.setLat("0.0");
        else remarkRequest.setLat(LatLngSycn_Controller.getInstance().getLat());

        if (LatLngSycn_Controller.getInstance().getLng() == null)
            remarkRequest.setLng("0.0");
        else remarkRequest.setLng(LatLngSycn_Controller.getInstance().getLng());


        remarkRequest.setStatus(getSelectedStatusId(selectedCondition));

        if (isTagSet) {
            processImageAndUpload(remarkRequest);
        } else {
            createEquipmentForJobAudit(remarkRequest);
        }

    }

    private String getSelectedStatusId(int position) {
        String esId = "";
        if (equipmentStatusList != null)
            esId = equipmentStatusList.get(position).getEsId();

        return esId;

    }

    private int getSelectedStatusPosition(String esID) {
        int position = -1;
        if (equipmentStatusList != null) {
            for (int i = 0; i < equipmentStatusList.size(); i++) {
                if (equipmentStatusList.get(i).getEsId().equals(esID)) {
                    return i;
                }
            }
        }
        return position;

    }

    private void setData() {

        getUpdatedLocation();
        Intent intent = getIntent();

        //equipment = intent.getParcelableExtra("equipment");
        if (intent.hasExtra("equipment")) {
            String strEquipment = intent.getExtras().getString("equipment");
            equipment = new Gson().fromJson(strEquipment, EquArrayModel.class);
        }

        intent.getExtras().get("");
        try {
            if (getIntent().hasExtra("jobId")) {
                jobId = intent.getExtras().getString("jobId");
            }
        } catch (Exception ex) {
            ex.getMessage();
        }

        if (equipment != null) {
            setTitles();
            tv_equipment_name.setText(equipment.getEqunm());
            tv_location.setText(equipment.getLocation());

//            if (TextUtils.isEmpty(equipment.getStatus())
//                    || equipment.getStatus().equals("0"))
//                return;

            if (!TextUtils.isEmpty(equipment.getRemark()))
                edit_remarks.setText(equipment.getRemark());


            if (!TextUtils.isEmpty(equipment.getStatus()) && TextUtils.isDigitsOnly(equipment.getStatus())) {
                int selectedStatusPosition = getSelectedStatusPosition(equipment.getStatus());
                if (selectedStatusPosition > -1)
                    status_spinner.setSelection(selectedStatusPosition);
            }

            if (equipment != null && equipment.getAttachments() != null) {
                setAttachments(equipment.getAttachments());
                button_submit.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_btn));
                REMARK_SUBMIT = true;
            }

        }

    }


    private void setAttachments(ArrayList<GetFileList_Res> attachments) {
        if (equipment != null) {
            if (attachments != null) {
                if (attachments.size() > 0) {
                    tv_label_attachment.setVisibility(View.VISIBLE);
                } else {
                    tv_label_attachment.setVisibility(View.GONE);
                }
                if (adapter == null) {
                    recyclerView_attachment.setLayoutManager(new GridLayoutManager(this, 2));
                    adapter = new DocumentListAdapter(this, attachments, jobId);
                    recyclerView_attachment.setAdapter(adapter);
                } else {
                    adapter.updateFileList(attachments);
                }
            }
        }
    }


    private void setTitles() {
        if (equipment.getRemark().equals("") && equipment.getStatus().equals("") && equipment.getAttachments() != null && equipment.getAttachments().size() == 0) {
            setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_remark));
            titleNm = (LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_remark));
        } else {
            if (equipment != null && equipment.getEqunm() != null) {
                setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.remark_on) + " " + equipment.getEqunm());
                titleNm = LanguageController.getInstance().getMobileMsgByKey(AppConstant.remark_on) + " " + equipment.getEqunm();
            } else {
                setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_remark));
                titleNm = LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_remark);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {
            case SINGLEATTCHMENT: {
                if (data.hasExtra("code")) {
                    String barcode = data.getStringExtra("code");
                    ArrayList<GetFileList_Res> equipmentList = new ArrayList<>();
                    try {
                        Type listType = new TypeToken<List<GetFileList_Res>>() {
                        }.getType();
                        equipmentList = new Gson().fromJson(barcode, listType);
                        if (equipment != null && equipment.getAttachments() != null && equipment.getAttachments().size() > 0) {
                            equipmentList.addAll(equipment.getAttachments());
                        }
                        if (equipmentList.size() > 0) {
                            setAttachments(equipmentList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRemarkUpdate(String message) {
        String remark_msg = !REMARK_SUBMIT ? LanguageController.getInstance().getMobileMsgByKey(AppConstant.euipment_remark_submit) : LanguageController.getInstance().getMobileMsgByKey(AppConstant.euipment_remark_update);
        EotApp.getAppinstance().showToastmsg(remark_msg);
        Intent intent = new Intent();
        intent.putExtra("auditId", equipment.getAudId());
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    public void onErrorMsg(String msg) {
        AppUtility.error_Alert_Dialog(this, msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    @Override
    public void onSessionExpire(String message) {
        AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), message, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }

    @Override
    public void OnItemClick_Document(GetFileList_Res getFileList_res) {
        if (getFileList_res != null)
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(App_preference.getSharedprefInstance().getBaseURL() + "" + getFileList_res.getAttachFileName())));

    }

    /**
     * get Sub Question's Answer's List
     ******/
    public void getAnsList
    (ArrayList<Answer> answerArray, List<MultipartBody.Part> signAns,
     List<MultipartBody.Part> docAns, ArrayList<String> signQueIdArray, ArrayList<String> docQueIdArray) {
        Log.e("Size--->>", ">>>>>" + answerArray.size());
        if (customFormLists != null && customFormLists.size() > 0)
            for (Answer ans : answerArray) {
                ans.setFrmId(customFormLists.get(0).getFrmId());
            }
        this.signQueIdArray.addAll(signQueIdArray);
        this.docQueIdArray.addAll(docQueIdArray);
        this.signAns.addAll(signAns);
        this.docAns.addAll(docAns);
        answerArrayList.addAll(answerArray);
    }

    @Override
    public void selectFileWithoutAttchment(int position, ImageView attchmentView, ImageView
            deleteAttchment, Button addAttchment) {
        this.deleteAttchment = deleteAttchment;
        this.position = position;
        this.attchmentView = attchmentView;
        this.addAttchment = addAttchment;
        type = 2;
        selectFile(true);
    }

    @Override
    public void onDocumentSelected(String path, boolean isImage) {
        super.onDocumentSelected(path, isImage);
        if (type == 1) {
            uploadRemarkAttchment(path);
        } else if (type == 2) {
            if (questionListAdapter != null)
                questionListAdapter.showAttchmentView(position, path, attchmentView, deleteAttchment, addAttchment);
        }
    }
}