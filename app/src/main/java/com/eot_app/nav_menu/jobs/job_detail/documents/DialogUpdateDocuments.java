package com.eot_app.nav_menu.jobs.job_detail.documents;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;
import com.eot_app.nav_menu.jobs.job_detail.documents.fileattach_mvp.Doc_Attch_Pc;
import com.eot_app.nav_menu.jobs.job_detail.documents.fileattach_mvp.Doc_Attch_Pi;
import com.eot_app.nav_menu.jobs.job_detail.documents.fileattach_mvp.Doc_Attch_View;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class DialogUpdateDocuments extends DialogFragment implements View.OnClickListener, Doc_Attch_View {

    AppCompatImageView img_doc;
    AppCompatTextView tv_doc_name;
    AppCompatTextView tv_label_desc;
    AppCompatTextView tv_label_optional;
    AppCompatEditText ed_doc_desc;
    AppCompatButton btn_submit;
    Doc_Attch_Pi doc_attch_pi;
    OnDocumentUpdate onDocumentUpdate;
    String type;
    private String imgPath;
    private String desc;
    private boolean isFileImage;
    private String fileName;
    private String docId;
    private boolean isFromClientHistory = false;
    private TextView desc_txt;
    private String isAddAttachAsCompletionNote;
    private String jobId;

    public void setIsFileImage(boolean b) {
        this.isFileImage = b;
    }

    public void setOnDocumentUpdate(OnDocumentUpdate onDocumentUpdate) {
        this.onDocumentUpdate = onDocumentUpdate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog_Alert);

    }


    public void setFromClientHistory(boolean fromClientHistory) {
        isFromClientHistory = fromClientHistory;
    }

    public void setImgPath(String docId, String imgPath, String fileName, String desc, String type, String jobId) {
        this.docId = docId;
        this.imgPath = imgPath;
        this.fileName = fileName;
        this.desc = desc;
        this.type = type;
        this.jobId = jobId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_doucment_update, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        desc_txt = view.findViewById(R.id.desc_txt);
        desc_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.modifyed_desc_append_complition));
        img_doc = view.findViewById(R.id.doc_img);
        tv_doc_name = view.findViewById(R.id.tv_doc_name);
        tv_label_desc = view.findViewById(R.id.tv_label_des);
        tv_label_optional = view.findViewById(R.id.tv_label_optional);
        ed_doc_desc = view.findViewById(R.id.et_doc_desc);


        btn_submit = view.findViewById(R.id.button_submit);

        btn_submit.setOnClickListener(this);
        img_doc.setOnClickListener(this);
        tv_doc_name.setOnClickListener(this);

        setData();

        ed_doc_desc.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!TextUtils.isEmpty(type) && type.equals("6") && s.length() > 0) {
                    desc_txt.setVisibility(View.VISIBLE);
                } else if (s.length() == 0)
                    desc_txt.setVisibility(View.GONE);
            }
        });

        if (isFromClientHistory) {
            btn_submit.setVisibility(View.GONE);
            ed_doc_desc.setEnabled(false);
        } else {
            btn_submit.setVisibility(View.VISIBLE);
            ed_doc_desc.setEnabled(true);
        }
    }

    private void setData() {
        btn_submit.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_btn));
        tv_label_desc.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.doc_des_op));
        tv_label_optional.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.optional));


        if (desc != null)
            ed_doc_desc.setText(desc);
        if (fileName != null)
            tv_doc_name.setText(fileName);
        if (isFileImage && imgPath != null)
            Picasso.with(getActivity()).load(App_preference.getSharedprefInstance().getBaseURL() + "" + imgPath).into(img_doc);
        else {
            setImageIcon();
        }

        doc_attch_pi = new Doc_Attch_Pc(this);

    }

    private void setImageIcon() {
        if (imgPath != null) {
            try {
                int fileIcons = getFileIcons(imgPath);
                if (fileIcons > 0) {
                    img_doc.setImageResource(fileIcons);
                    img_doc.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                if (type.equals("6"))
                    isAddAttachAsCompletionNote = "1";
                else
                    isAddAttachAsCompletionNote = "0";

                uploadDocuments(isAddAttachAsCompletionNote);
                break;
            case R.id.doc_img:
            case R.id.tv_doc_name:
                openFileExternalBrowser();
                break;
        }
    }

    private void openFileExternalBrowser() {
        if (!TextUtils.isEmpty(imgPath))
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(App_preference.getSharedprefInstance().getBaseURL() + "" + imgPath)));

    }

    private void uploadDocuments(String isAddAttachAsCompletionNote) {
        if (docId != null) {
            AppUtility.hideSoftKeyboard(getActivity());
            String updateDesc = "";
            if (!TextUtils.isEmpty(ed_doc_desc.getText().toString()))
                updateDesc = ed_doc_desc.getText().toString();

            if (desc.contentEquals(updateDesc)) {
                dismiss();
            } else {
                AppUtility.progressBarShow(getActivity());
                doc_attch_pi.updateDocuments(docId, updateDesc, isAddAttachAsCompletionNote, jobId);
            }
        }


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
    public void onDocumentUpdate(String msg, boolean isSuccess) {
        if (isSuccess && onDocumentUpdate != null) {
            onDocumentUpdate.onUpdateDes(ed_doc_desc.getText().toString());
            dismiss();

        } else if (msg != null)
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void selectFile() {

    }

    @Override
    public void setList(ArrayList<GetFileList_Res> getFileList_res, String isAttch) {

    }

    @Override
    public void addView() {

    }

    @Override
    public void onSessionExpire(String msg) {
        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "",
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
    public void onDismiss(DialogInterface dialog) {
        try {
            if (ed_doc_desc.isFocused()) {
                InputMethodManager imm = (InputMethodManager) ed_doc_desc.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDismiss(dialog);

    }

    public interface OnDocumentUpdate {
        void onUpdateDes(String desc);
    }
}
