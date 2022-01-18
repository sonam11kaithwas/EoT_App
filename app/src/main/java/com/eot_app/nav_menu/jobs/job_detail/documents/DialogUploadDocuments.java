package com.eot_app.nav_menu.jobs.job_detail.documents;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import com.eot_app.R;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;
import com.squareup.picasso.Picasso;

import java.io.File;

public class DialogUploadDocuments extends DialogFragment implements View.OnClickListener {

    AppCompatImageView img_doc;
    AppCompatTextView tv_label_title;
    AppCompatTextView tv_label_desc;
    AppCompatTextView tv_label_optional;

    AppCompatEditText ed_doc_title;
    AppCompatEditText ed_doc_desc;

    AppCompatButton btn_submit;
    OnDocumentSubmit onDocumentSubmit;
    private String imgPath;
    private boolean isFileImage;

    public void setIsFileImage(boolean b) {
        this.isFileImage = b;
    }

    public void setOnDocumentSubmit(OnDocumentSubmit onDocumentSubmit) {
        this.onDocumentSubmit = onDocumentSubmit;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog_Alert);

    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_doucment_upload, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        img_doc = view.findViewById(R.id.doc_img);
        tv_label_title = view.findViewById(R.id.tv_label_title);
        tv_label_desc = view.findViewById(R.id.tv_label_des);
        tv_label_optional = view.findViewById(R.id.tv_label_optional);
        ed_doc_title = view.findViewById(R.id.et_doc_title);
        ed_doc_desc = view.findViewById(R.id.et_doc_desc);
        btn_submit = view.findViewById(R.id.button_submit);

        btn_submit.setOnClickListener(this);

        tv_label_title.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.doc_name));
        tv_label_desc.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.doc_des_op));
        tv_label_optional.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.optional));
        btn_submit.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.sub_up));

        if (imgPath != null) {
            String fileName = getFileName();
            if (fileName != null)
                ed_doc_title.setText(fileName);
        }
        if (isFileImage && imgPath != null) {
            Picasso.with(getActivity()).load(new File(imgPath)).into(img_doc);
        } else {
            img_doc.setVisibility(View.GONE);

        }


    }

    private String getFileName() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                uploadDocuments();
                break;
        }
    }

    private void uploadDocuments() {
        if (onDocumentSubmit != null) {
            String fileName = ed_doc_title.getText().toString();
            if (TextUtils.isEmpty(fileName))
                fileName = getFileName();

            onDocumentSubmit.onSubmitDocument(imgPath, fileName, ed_doc_desc.getText().toString());
            AppUtility.hideSoftKeyboard(getActivity());
            dismiss();

        }

    }

    public interface OnDocumentSubmit {
        void onSubmitDocument(String imgPath, String filName, String desc);
    }
}
