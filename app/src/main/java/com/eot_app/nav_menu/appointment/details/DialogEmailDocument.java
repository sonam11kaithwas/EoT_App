package com.eot_app.nav_menu.appointment.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg.Invoice_Email_Activity;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.language_support.LanguageController;
import com.hypertrack.hyperlog.HyperLog;

/**
 * Created by Mahendra Dabi on 08-08-2020.
 */
public class DialogEmailDocument extends DialogFragment {
    AppCompatButton button_send_email, doc_img;
    AppCompatImageView img_close;
    AppCompatTextView tv_title;
    WebView webView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog_Alert);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_email_document, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        /*       *//*start *//*
        Uri pdfPath = Uri.parse(App_preference.getSharedprefInstance().getBaseURL() + "" + getArguments().getString("pdfPath"));
        webView = view.findViewById(R.id.webview);
       String doc = "<iframe src='http://docs.google.com/gview?embedded=true&url=" + pdfPath + "' width='100%' height='100%' style='border: none;'></iframe>";
       //String doc ="http://docs.google.com/gview?embedded=true&url="+pdfPath;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setAllowFileAccess(true);
        webView.loadData(doc , "text/html",  "UTF-8");



        *//*close*/
        button_send_email = view.findViewById(R.id.button_send_email);
        img_close = view.findViewById(R.id.img_close);
        tv_title = view.findViewById(R.id.tv_title);
        doc_img = view.findViewById(R.id.doc_img);

        tv_title.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.view_and_send));
        button_send_email.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.send_email));
        doc_img.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.view));


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        doc_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(App_preference.getSharedprefInstance().getBaseURL() + "" + getArguments().getString("pdfPath"))));
                } catch (Exception ex) {

                }
            }
        });

        button_send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HyperLog.i("", "send invoice intent called");
                Bundle arguments = getArguments();
                Intent emailIntent = new Intent(getActivity(), Invoice_Email_Activity.class);
                emailIntent.putExtra("appId", arguments.getString("appId"));
                emailIntent.putExtra("pdfPath", arguments.getString("pdfPath"));
                startActivity(emailIntent);
                dismiss();

            }
        });
    }

}
