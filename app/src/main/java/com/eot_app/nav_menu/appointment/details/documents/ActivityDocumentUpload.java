package com.eot_app.nav_menu.appointment.details.documents;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.eot_app.R;
import com.eot_app.nav_menu.appointment.details.AppointmentAttachment;
import com.eot_app.nav_menu.appointment.details.documents.fileattach_mvp.Doc_Attch_Pc;
import com.eot_app.nav_menu.appointment.details.documents.fileattach_mvp.Doc_Attch_Pi;
import com.eot_app.nav_menu.appointment.details.documents.fileattach_mvp.Doc_Attch_View;
import com.eot_app.nav_menu.jobs.job_detail.documents.EditImageDialog;
import com.eot_app.nav_menu.jobs.job_detail.documents.PathUtils;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ActivityDocumentUpload extends AppCompatActivity implements View.OnClickListener,
        Doc_Attch_View {

    AppCompatImageView img_doc;
    AppCompatTextView tv_label_title;
    AppCompatTextView tv_label_desc;
    AppCompatTextView tv_label_optional;

    EditText ed_doc_title;
    EditText ed_doc_desc;

    AppCompatButton button_edit;

    AppCompatButton btn_submit;

    Uri uri;
    EditImageDialog dialog;
    boolean isEdited;
    Doc_Attch_Pi doc_attch_pi;
    private String filePath;
    private boolean isFileImage;
    private String appId;
    private NestedScrollView rl_top;
    private RelativeLayout relative_parent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_save_upload);

        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.document));


        rl_top = findViewById(R.id.rl_top);
        img_doc = findViewById(R.id.doc_img);
        button_edit = findViewById(R.id.button_edit);
        tv_label_title = findViewById(R.id.tv_label_title);
        tv_label_desc = findViewById(R.id.tv_label_des);
        tv_label_optional = findViewById(R.id.tv_label_optional);
        ed_doc_title = findViewById(R.id.et_doc_title);
        ed_doc_desc = findViewById(R.id.et_doc_desc);
        btn_submit = findViewById(R.id.button_submit);
        relative_parent = findViewById(R.id.relative_parent);

        AppUtility.setupUI(relative_parent, ActivityDocumentUpload.this);

        btn_submit.setOnClickListener(this);
        button_edit.setOnClickListener(this);

        tv_label_title.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.doc_name));
        tv_label_desc.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.doc_des_op));
        tv_label_optional.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.optional));
        btn_submit.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_btn));
        button_edit.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.customize));
        doc_attch_pi = new Doc_Attch_Pc(this);

        appId = getIntent().getStringExtra("appId");
        if (getIntent().getBooleanExtra("isImage", false))
            setImage();
        else
            setFile();

        rl_top.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtility.hideSoftKeyboard(ActivityDocumentUpload.this);
                return true;
            }
        });
    }

    private void setFile() {
        isFileImage = false;
        button_edit.setVisibility(View.GONE);
        filePath = getIntent().getStringExtra("uri");

        try {
            ViewGroup.LayoutParams params = img_doc.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = 400;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            img_doc.setMaxHeight(400);
            img_doc.setLayoutParams(params);
        } catch (Exception ex) {

        }


        int fileIcons = getFileIcons(filePath);
        String fileName = getFileName(filePath);

        if (fileName != null)
            ed_doc_title.setText(fileName);
        img_doc.setImageResource(fileIcons);

    }

    private void setImage() {
        uri = getIntent().getParcelableExtra("uri");
        img_doc.setImageURI(uri);
        isFileImage = true;
        // String fileName = getFileName(PathUtils.getPath(this, uri));
        String fileName = getFileName(PathUtils.getRealPath(this, uri));

        if (fileName != null)
            ed_doc_title.setText(fileName);
        button_edit.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_edit:
                imageEditing(uri, "image_capture");
                break;

            case R.id.button_submit:
                if (isFileImage) {
                    if (isEdited)
                        uploadDocuments(uri.getPath());
                    else {
                        //   uploadDocuments(PathUtils.getPath(this, uri));
                        uploadDocuments(PathUtils.getRealPath(this, uri));
                    }
                } else uploadDocuments(filePath);
                break;

        }
    }


    private void uploadDocuments(String imgPath) {
        AppUtility.hideSoftKeyboard(this);
        AppUtility.progressBarShow(this);
        String fileName = ed_doc_title.getText().toString();
        String desc = ed_doc_desc.getText().toString();
        if (TextUtils.isEmpty(desc))
            desc = "";
        if (TextUtils.isEmpty(fileName))
            fileName = getFileName(imgPath);
        doc_attch_pi.uploadDocuments(appId, imgPath, fileName, desc);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }

    //upload image edit highlighting feature for image
    private void imageEditing(Uri urii, String dialogTag) {
        try {
            if (dialog == null || !isEdited) {
                Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), urii);
                // First decode with inJustDecodeBounds=true to check dimensions
                try {
                    photo = EditImageDialog.rotateImageIfRequired(
                            photo, this, urii
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog = new EditImageDialog();
                dialog.setBitmapImage(photo, urii);
                dialog.setOnImageEdited(new EditImageDialog.OnImageEdited() {
                    @Override
                    public void onImageSaved(String path) {
                        isEdited = true;
                        uri = Uri.parse(path);
                        img_doc.setImageURI(Uri.parse(path));
                    }
                });

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                //  dialog.setTargetFragment(this, 101);
                dialog.show(ft, dialogTag);

            } else {
                File file = new File(uri.toString());
                Bitmap bitmap = BitmapFactory.decodeFile(uri.toString());
                dialog.setBitmapImage(bitmap, Uri.fromFile(file));
                dialog.show(getSupportFragmentManager(), dialogTag);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getFileName(String imgPath) {
        String fname = System.currentTimeMillis() + "";
        try {
            File file = new File(imgPath);
            fname = file.getName();
            if (fname.contains(".")) {
                fname = fname.substring(0, fname.lastIndexOf("."));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fname;
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
    public void selectFile() {

    }

    @Override
    public void setList(ArrayList<AppointmentAttachment> getFileList_res) {
        if (getFileList_res != null) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("appointment_refresh"));
            Intent intent = new Intent();
            intent.putExtra("list", getFileList_res);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    @Override
    public void addView() {

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
}
