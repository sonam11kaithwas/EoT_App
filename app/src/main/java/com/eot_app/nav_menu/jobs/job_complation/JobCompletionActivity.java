package com.eot_app.nav_menu.jobs.job_complation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_complation.complat_mvp.Compl_PC;
import com.eot_app.nav_menu.jobs.job_complation.complat_mvp.Compl_PI;
import com.eot_app.nav_menu.jobs.job_complation.complat_mvp.Compla_View;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_db.JtId;
import com.eot_app.nav_menu.jobs.job_detail.documents.ActivityDocumentSaveUpload;
import com.eot_app.nav_menu.jobs.job_detail.documents.EditImageDialog;
import com.eot_app.nav_menu.jobs.job_detail.documents.PathUtils;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;
import com.eot_app.nav_menu.jobs.job_detail.documents.fileattach_mvp.Doc_Attch_Pc;
import com.eot_app.nav_menu.jobs.job_detail.documents.fileattach_mvp.Doc_Attch_Pi;
import com.eot_app.nav_menu.jobs.job_detail.documents.fileattach_mvp.Doc_Attch_View;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.setting_db.JobTitle;
import com.eot_app.utility.settings.setting_db.Suggestion;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.eot_app.utility.util_interfaces.JoBServSuggAdpter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

public class JobCompletionActivity extends AppCompatActivity implements View.OnClickListener
        , TextWatcher, Compla_View, Doc_Attch_View, JobCompletionAdpter.FileDesc_Item_Selected {
    private final static int CAPTURE_IMAGE_GALLARY = 222;
    private static final int DOUCMENT_UPLOAD_CODE = 156;
    private final int CAMERA_CODE = 100;
    private final int ATTACHFILE_CODE = 101;
    private final String JOBCOMPLATIONTYPE = "";
    private final StringBuffer desc = new StringBuffer();
    private final List<Suggestion> suggestionList = new ArrayList<>();
    Job jobData;
    JoBServSuggAdpter suggestionAdapter;
    String captureImagePath;
    RecyclerView.LayoutManager layoutManager;
    Doc_Attch_Pi doc_attch_pi;
    String[] suggestionsArray = new String[suggestionList.size()];
    private TextView cancel_txt, complHeader, save_txt, tv_label_des;
    private EditText compedt;
    private Compl_PI complPi;
    private RecyclerView recyclerView;
    private JobCompletionAdpter jobCompletionAdpter;
    private String jobId;
    private EditImageDialog currentDialog = null;
    private ArrayList<GetFileList_Res> fileList_res = new ArrayList<>();
    private ProgressBar progressBar;
    private ImageView suggestion_img;
    private Spinner job_suggestion_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_completion);
        getSupportActionBar().hide();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (getIntent().hasExtra("Complation")) {
                String jobDataStr = bundle.getString("Complation");
                jobData = new Gson().fromJson(jobDataStr, Job.class);
                jobId = jobData.getJobId();
            }
        }

        initializeMyViews();

    }

    private void initializeAdapter() {
        ArrayList<GetFileList_Res> getFileList_res = new ArrayList<>();
        jobCompletionAdpter = new JobCompletionAdpter(this, getFileList_res, this, jobId
                , new JobCompletionAdpter.RemoveAttchment() {
            @Override
            public void removeAttchment(String jaId) {
                Log.e("", "");
                if (complPi != null) {
                    showDialogForRemoveAttch(jaId);
                }
            }
        });
        recyclerView.setAdapter(jobCompletionAdpter);
    }

    private void showDialogForRemoveAttch(final String jaId) {
        AppUtility.alertDialog2(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.are_you_sure),
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.doc_remove),
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel), new Callback_AlertDialog() {
                    @Override
                    public void onPossitiveCall() {
                        try {
                            complPi.removeUploadAttchment(jaId);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNegativeCall() {

                    }
                });
    }

    @Override
    public void uploadDocDelete(String msg) {
        doc_attch_pi.getAttachFileList(jobId, App_preference.getSharedprefInstance().getLoginRes().getUsrId(), "6");
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
    public void sessionexpire(String msg) {
        AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title),
                msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        EotApp.getAppinstance().sessionExpired();
                        return null;
                    }
                });
    }

    private void initializeMyViews() {
        tv_label_des = findViewById(R.id.tv_label_des);
        tv_label_des.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.completion_note));
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this, 2);
//        CustomLinearLayoutManager customLayoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.VERTICAL
//                , false);

        recyclerView.setLayoutManager(layoutManager);
        initializeAdapter();
        doc_attch_pi = new Doc_Attch_Pc(this);
        doc_attch_pi.getAttachFileList(jobId, App_preference.getSharedprefInstance().getLoginRes().getUsrId(), "6");

        cancel_txt = findViewById(R.id.cancel_txt);
        complHeader = findViewById(R.id.complHeader);
        save_txt = findViewById(R.id.save_txt);

        cancel_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel));
        complHeader.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.completion_note));

        //    compleLayout = findViewById(R.id.compleLayout);
        compedt = findViewById(R.id.compedt);
        compedt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.completion_note));
        //    compleLayout.getEditText().addTextChangedListener(this);

        suggestion_img = findViewById(R.id.suggestion_img);
        job_suggestion_spinner = findViewById(R.id.job_suggestion_spinner);
        suggestion_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (suggestionList != null && suggestionList.size() > 0)
                    job_suggestion_spinner.performClick();
                else {
                    AppUtility.alertDialog(JobCompletionActivity.this,
                            "", LanguageController.getInstance()
                                    .getMobileMsgByKey(AppConstant.no_suggesstion)
                            , LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    return null;
                                }
                            });
                }

            }
        });
        viewClickListner();

        setComplationView();

        complPi = new Compl_PC(this);

        filterJobServices();

        if (AppUtility.isInternetConnected()) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void filterJobServices() {
        try {
            if (jobData != null && jobData.getJtId() != null && jobData.getJtId().size() > 0) {
                if (suggestionList != null && suggestionList.size() > 0) {
                    suggestionList.clear();
                }

                for (JtId jtId : jobData.getJtId()) {
                    JobTitle jobTitle1 = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobTitleModel().getJobTitleByid(jtId.getJtId());
                    if (jobTitle1 != null)
                        suggestionList.addAll(jobTitle1.getSuggestionList());
                }

                List<String> tempList = new ArrayList<>();
//                suggestionsArray = new String[suggestionList.size()];
                for (int i = 0; i < suggestionList.size(); i++) {
                    try {
                        if (suggestionList.get(i).getComplNoteSugg() != null &&
                                suggestionList.get(i).getComplNoteSugg().length() > 0) {
                            tempList.add(suggestionList.get(i).getComplNoteSugg());
                            //     suggestionsArray[i] = suggestionList.get(i).getComplNoteSugg();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    suggestionsArray = new String[tempList.size()];
                    for (int i = 0; i < tempList.size(); i++) {
                        suggestionsArray[i] = tempList.get(i);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }


                try {
                    if (suggestionAdapter == null) {
                        AppUtility.spinnerPopUpWindow(job_suggestion_spinner);
                        suggestionAdapter = new JoBServSuggAdpter(JobCompletionActivity.this, suggestionsArray
                                , new JoBServSuggAdpter.SelectedService() {
                            @Override
                            public void getSerNm(String nm) {
                                setSelectedSuggeston(nm);
                            }
                        });
                        job_suggestion_spinner.setAdapter(suggestionAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception exception) {
            exception.getMessage();
        }

    }


    private void setSelectedSuggeston(String nm) {
        try {
            String str = "";
            if (compedt.getText().toString().trim().length() > 0) {
                str = compedt.getText().toString().trim() + "\n" + nm;
            } else {
                str = nm;
            }
            compedt.setText(str);
        } catch (Exception e) {
            e.getMessage();
        }

    }

    private void setComplationView() {
        if (TextUtils.isEmpty(jobData.getComplNote())) {
            save_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_btn));
        } else {
            save_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.update));
            compedt.setText(jobData.getComplNote());
        }
    }

    private void viewClickListner() {
        cancel_txt.setOnClickListener(this);
        save_txt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_txt:
                AppUtility.hideSoftKeyboard(this);
                this.finish();
                break;
            case R.id.save_txt:
                AppUtility.hideSoftKeyboard(this);
                complPi.addEditJobComplation(jobData.getJobId(), compedt.getText().toString().trim());
                break;
        }
    }


    @Override
    public void selectFile() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.bottom_image_chooser);
        TextView camera = dialog.findViewById(R.id.camera);
        camera.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.camera));
        TextView gallery = dialog.findViewById(R.id.gallery);
        gallery.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.gallery));
        TextView drive_document = dialog.findViewById(R.id.drive_document);
        drive_document.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.document));
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtility.askCameraTakePicture(JobCompletionActivity.this)) {
                    takePictureFromCamera();
                }
                dialog.dismiss();
            }

        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtility.askGalaryTakeImagePermiSsion(JobCompletionActivity.this)) {
                    getImageFromGallray();
                }
                dialog.dismiss();
            }


        });

        drive_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtility.askGalaryTakeImagePermiSsion(JobCompletionActivity.this)) {
                    takeimageFromGalary();//only for drive documents
                }
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void OnItemClick_Document(GetFileList_Res getFileList_res) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(App_preference.getSharedprefInstance().getBaseURL() + "" + getFileList_res.getAttachFileName())));
    }

    @Override
    public void openAttachmentDialog() {
        selectFile();
    }

    @Override
    public void setList(ArrayList<GetFileList_Res> getFileList_res, String isAttachCommpletionNotes) {
        progressBar.setVisibility(View.GONE);
        if (currentDialog != null) {
            currentDialog.dismiss();
            currentDialog = null;
        }
        this.fileList_res = getFileList_res;

        if (jobCompletionAdpter != null)
            (jobCompletionAdpter).updateFileList(getFileList_res);

    }

    @Override
    public void addView() {
        // recyclerView.setVisibility(View.VISIBLE);
        //  nolist_linear.setVisibility(View.GONE);
    }

    @Override
    public void onSessionExpire(String msg) {
        AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "",
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        EotApp.getAppinstance().sessionExpired();
                        return null;
                    }
                });
    }

    @Override
    public void fileExtensionNotSupport(String msg) {

    }

    @Override
    public void onDocumentUpdate(String msg, boolean isSuccess) {

    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * get image from camera & edit & croping functinallity
     */
    private void takePictureFromCamera() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);// new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                startActivityForResult(takePictureIntent, CAMERA_CODE);
            }
        }
    }

    private void getImageFromGallray() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, CAPTURE_IMAGE_GALLARY);
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
        documentIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(documentIntent, ATTACHFILE_CODE);
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


    @Override
    public void updateDetailJob(String note) {
        Intent intent = new Intent();
        if (jobData != null) jobData.setComplNote(note);
        intent.putExtra("note", note);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
      /*  if (charSequence.length() >= 1) {
            if (charSequence.hashCode() == compedt.getText().hashCode())
                compleLayout.setHintEnabled(true);
        } else if (charSequence.length() <= 0) {
            *//**Floating hint Disable after text enter**//*
            if (charSequence.hashCode() == compedt.getText().hashCode())
                compleLayout.setHintEnabled(false);
        }*/
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DOUCMENT_UPLOAD_CODE:
                if (resultCode == RESULT_OK)
                    if (doc_attch_pi != null) {
                        ArrayList<GetFileList_Res> updateList = (ArrayList<GetFileList_Res>) data.getSerializableExtra("list");
                        String isAttach = data.getStringExtra("isAttach");
                        if (isAttach.equals("1")) {
                            if (updateList != null) {
                                updateList.addAll(fileList_res);
                                desc.append(compedt.getText().toString().trim());
                                compedt.getText().clear();
                                if (!TextUtils.isEmpty(desc))
                                    desc.append(System.lineSeparator());
                                desc.append(updateList.get(0).getDes());
                                compedt.setText(desc);
                                desc.setLength(0);
                                setList(updateList, "");
                            }
                        }
                    }
                break;
            case CAMERA_CODE:

                if (resultCode == RESULT_OK) {
                    try {
                        File file = AppUtility.scaleToActualAspectRatio(captureImagePath, 1024f, 1024f);
                        if (file != null) {
                            imageEditing(Uri.fromFile(file), true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    return;
                }

                break;

            case CAPTURE_IMAGE_GALLARY:
            case ATTACHFILE_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri galreyImguriUri = data.getData();
                        // String gallery_image_Path = PathUtils.getPath(this, galreyImguriUri);
                        String gallery_image_Path = PathUtils.getRealPath(this, galreyImguriUri);
                        String img_extension = gallery_image_Path.substring(gallery_image_Path.lastIndexOf("."));
                        //('jpg','png','jpeg','pdf','doc','docx','xlsx','csv','xls'); supporting extensions
                        if (img_extension.equals(".jpg") || img_extension.equals(".png") || img_extension.equals(".jpeg")) {
                            imageEditing(data.getData(), true);

                        } else {
                            uploadFileDialog(gallery_image_Path);

                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                } else {
                    return;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //upload image edit highlighting feature for image
    private void imageEditing(Uri uri, boolean isImage) {
        try {
            Intent intent = new Intent(this, ActivityDocumentSaveUpload.class);
            intent.putExtra("uri", uri);
            intent.putExtra("isImage", true);
            intent.putExtra("jobid", jobId);
            intent.putExtra("SAVEASCOMPLETION", true);
            startActivityForResult(intent, DOUCMENT_UPLOAD_CODE);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //upload file dialog
    private void uploadFileDialog(final String selectedFilePath) {
        Intent intent = new Intent(this, ActivityDocumentSaveUpload.class);
        intent.putExtra("uri", selectedFilePath);
        intent.putExtra("isImage", false);
        intent.putExtra("jobid", jobId);
        startActivityForResult(intent, DOUCMENT_UPLOAD_CODE);

    }

    public void setUpdatedDesc(String desc) {
        StringBuffer data = new StringBuffer(compedt.getText().toString().trim());
        if (!TextUtils.isEmpty(desc))
            data.append(System.lineSeparator());
        data.append(desc);
        compedt.setText(data);
        data.setLength(0);


    }
}
