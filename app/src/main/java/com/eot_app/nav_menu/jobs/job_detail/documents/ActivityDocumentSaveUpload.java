package com.eot_app.nav_menu.jobs.job_detail.documents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentTransaction;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;
import com.eot_app.nav_menu.jobs.job_detail.documents.fileattach_mvp.Doc_Attch_Pc;
import com.eot_app.nav_menu.jobs.job_detail.documents.fileattach_mvp.Doc_Attch_Pi;
import com.eot_app.nav_menu.jobs.job_detail.documents.fileattach_mvp.Doc_Attch_View;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.CompressImageInBack;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.OnImageCompressed;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ActivityDocumentSaveUpload extends AppCompatActivity implements View.OnClickListener,
        Doc_Attch_View, RadioGroup.OnCheckedChangeListener {

    AppCompatImageView img_doc;
    AppCompatTextView tv_label_title;
    AppCompatTextView tv_label_desc;

    EditText ed_doc_title;
    EditText ed_doc_desc;

    TextView button_edit;

    AppCompatButton btn_submit;

    Uri uri;
    EditImageDialog dialog;
    boolean isEdited;
    Doc_Attch_Pi doc_attch_pi;
    CompressImageInBack compressImageInBack = null;
    private String filePath;
    private boolean isFileImage, isTagSet = false;
    private String jobId;
    private ScrollView scrollView;
    private RadioGroup rediogrp;
    private RadioButton radio_before, radio_after;
    private TextView image_txt;//, remove_txt;
    private CheckBox completion_checkbox;
    private RelativeLayout image_with_tag;
    private String isFromCmpletion, type;
    private RelativeLayout ll_layout;
    private TextView desc_txt, chip_txt;
    private ImageView deleteChip;
    private View chip_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_doucment_save_upload);

        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.document));
        desc_txt = findViewById(R.id.desc_txt);
        ll_layout = findViewById(R.id.ll_layout);
        AppUtility.setupUI(ll_layout, ActivityDocumentSaveUpload.this);

        image_with_tag = findViewById(R.id.image_with_tag);

        completion_checkbox = findViewById(R.id.completion_checkbox);
        completion_checkbox.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_as_completion_notes));
        completion_checkbox.setOnClickListener(this);

        image_txt = findViewById(R.id.image_txt);

//        remove_txt = findViewById(R.id.remove_txt);
//        remove_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.remove_the_image_tag));
//        remove_txt.setOnClickListener(this);

        rediogrp = findViewById(R.id.rediogrp);
        rediogrp.setOnCheckedChangeListener(this);

        radio_before = findViewById(R.id.radio_before);
        radio_before.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.before));
        radio_before.setChecked(false);

        radio_after = findViewById(R.id.radio_after);
        radio_after.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.after));
        radio_after.setChecked(false);


        scrollView = findViewById(R.id.scrollView);
        img_doc = findViewById(R.id.doc_img);
        button_edit = findViewById(R.id.button_edit);
        tv_label_title = findViewById(R.id.tv_label_title);
        tv_label_desc = findViewById(R.id.tv_label_des);
        ed_doc_title = findViewById(R.id.et_doc_title);
        ed_doc_desc = findViewById(R.id.et_doc_desc);
        btn_submit = findViewById(R.id.button_submit);

        btn_submit.setOnClickListener(this);
        button_edit.setOnClickListener(this);

        desc_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_desc_append_complition));
        tv_label_title.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.doc_name));
        tv_label_desc.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.doc_des_op) + LanguageController.getInstance().getMobileMsgByKey(AppConstant.optional));
        btn_submit.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_btn));
        button_edit.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.customize));

        chip_txt = findViewById(R.id.chip_txt);
        deleteChip = findViewById(R.id.deleteChip);
        chip_layout = findViewById(R.id.chip_layout);
        deleteChip.setOnClickListener(this);

        doc_attch_pi = new Doc_Attch_Pc(this);

        jobId = getIntent().getStringExtra("jobid");
        if (getIntent().getBooleanExtra("isImage", false))
            setImage();
        else
            setFile();

        if (getIntent().getBooleanExtra("SAVEASCOMPLETION", false)) {
            saveAsCompletionNotes();
        }
    }


    private void saveAsCompletionNotes() {
        completion_checkbox.setChecked(true);
        //completion_checkbox.setClickable(false);
        completion_checkbox.setEnabled(false);
        desc_txt.setVisibility(View.VISIBLE);
    }

    private void setFile() {
        isFileImage = false;
        button_edit.setVisibility(View.GONE);
        rediogrp.setVisibility(View.GONE);
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
        //  img_doc.setImageURI(uri);
        new CompressImageInBack(this, new OnImageCompressed() {
            @Override
            public void onImageCompressed(Bitmap bitmap) {
                if (bitmap != null) {
                    img_doc.setImageBitmap(bitmap);

                }
            }
        }, uri).compressImageInBckg();//.execute(uri);


        isFileImage = true;
        // String fileName = getFileName(PathUtils.getPath(this, uri));
        String fileName = getFileName(PathUtils.getRealPath(this, uri));

        if (fileName != null)
            ed_doc_title.setText(fileName);
        button_edit.setVisibility(View.VISIBLE);
        rediogrp.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.completion_checkbox:
                if (completion_checkbox.isChecked())
                    desc_txt.setVisibility(View.VISIBLE);
                else
                    desc_txt.setVisibility(View.GONE);
                break;
            case R.id.deleteChip:
                isTagSet = false;
                image_txt.setText("");
                image_txt.setVisibility(View.GONE);
                chip_layout.setVisibility(View.GONE);
                rediogrp.setVisibility(View.VISIBLE);
                radio_after.setChecked(false);
                radio_before.setChecked(false);
                break;

            case R.id.button_edit:
                imageEditing(uri, "image_capture");
                break;

            case R.id.button_submit:

                processImageAndUpload();

                break;
        }
    }

    private void processImageAndUpload() {

        if (completion_checkbox.isChecked()) {
            isFromCmpletion = "1";
            type = "6";
        } else {
            isFromCmpletion = "0";
            type = "2";
        }

        if (isFileImage) {
            if (isEdited) {
                String path = saveBitMap(image_with_tag);
                uploadDocuments(path, isFromCmpletion);
            } else {
                if (isTagSet) {
                    String path = saveBitMap(image_with_tag);
                    uploadDocuments(path, isFromCmpletion);
                } else {
                    compressImageInBack = new CompressImageInBack(this, new OnImageCompressed() {
                        @Override
                        public void onImageCompressed(Bitmap bitmap) {
                            String savedImagePath = compressImageInBack.getSavedImagePath();
                            if (savedImagePath != null) {
                                uploadDocuments(savedImagePath, isFromCmpletion);
                            }
                        }
                    }, uri);
                    compressImageInBack.setSaveBitmap(true);
                    //  compressImageInBack.execute(uri);
                    compressImageInBack.compressImageInBckg();
                }
            }
        } else {
            uploadDocuments(filePath, isFromCmpletion);
        }
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


    private void uploadDocuments(String imgPath, String isFromCmpletion) {
        AppUtility.hideSoftKeyboard(this);
        AppUtility.progressBarShow(this);
        String fileName = ed_doc_title.getText().toString();
        String desc = ed_doc_desc.getText().toString();
        if (TextUtils.isEmpty(desc))
            desc = "";
        if (TextUtils.isEmpty(fileName))
            fileName = getFileName(imgPath);
        doc_attch_pi.uploadDocuments(jobId, imgPath, fileName, desc, type, isFromCmpletion);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            closeKeyboard();
        onBackPressed();

        return true;
    }

    //upload image edit highlighting feature for image
    private void imageEditing(final Uri urii, final String dialogTag) {
        try {
            if (dialog == null || !isEdited) {

                new CompressImageInBack(this, new OnImageCompressed() {
                    @Override
                    public void onImageCompressed(Bitmap photo) {
                        dialog = new EditImageDialog();
                        dialog.setBitmapImage(photo, urii);
                        dialog.setOnImageEdited(new EditImageDialog.OnImageEdited() {
                            @Override
                            public void onImageSaved(String path) {
                                isEdited = true;
                                uri = Uri.parse(path);
                                img_doc.setImageBitmap(BitmapFactory.decodeFile(path));
                                //  img_doc.setImageURI(Uri.parse(path));
                            }
                        });

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        //  dialog.setTargetFragment(this, 101);
                        dialog.show(ft, dialogTag);
                    }
                }, urii).compressImageInBckg();//.execute(urii);


            } else {
                File file = new File(uri.toString());
                Bitmap bitmap = BitmapFactory.decodeFile(uri.toString());
                dialog.setBitmapImage(bitmap, Uri.fromFile(file));
                dialog.show(getSupportFragmentManager(), dialogTag);
            }


        } catch (Exception e) {
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
    public void setList(ArrayList<GetFileList_Res> getFileList_res, String isAttachCompletionNotes) {
        if (getFileList_res != null) {
            Intent intent = new Intent();
            intent.putExtra("list", getFileList_res);
            intent.putExtra("isAttach", isAttachCompletionNotes);
            setResult(RESULT_OK, intent);
        }
        finish();
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
        if (!TextUtils.isEmpty(msg))
            showErrorDialog(LanguageController.getInstance().getServerMsgByKey(msg));
    }

    @Override
    public void onDocumentUpdate(String msg, boolean isSuccess) {

    }

    @Override
    public void hideProgressBar() {

    }

    private void showErrorDialog(String msg) {
        AppUtility.error_Alert_Dialog(ActivityDocumentSaveUpload.this, msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.radio_before:
                isTagSet = true;
                image_txt.setVisibility(View.VISIBLE);
                image_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.before));
                radioGroup.setVisibility(View.GONE);
                //  remove_txt.setVisibility(View.VISIBLE);
                chip_layout.setVisibility(View.VISIBLE);
                chip_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.before));
                break;
            case R.id.radio_after:
                isTagSet = true;
                image_txt.setVisibility(View.VISIBLE);
                image_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.after));
                radioGroup.setVisibility(View.GONE);
                //   remove_txt.setVisibility(View.VISIBLE);
                chip_layout.setVisibility(View.VISIBLE);
                chip_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.after));
                break;

        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
