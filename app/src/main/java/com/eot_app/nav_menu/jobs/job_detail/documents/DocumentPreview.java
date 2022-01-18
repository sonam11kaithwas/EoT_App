package com.eot_app.nav_menu.jobs.job_detail.documents;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.language_support.LanguageController;

public class DocumentPreview extends AppCompatActivity {
    TextView tv_title;
    TextView tv_des;
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_preview);

        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.document));

        tv_title = findViewById(R.id.tv_title);
        tv_des = findViewById(R.id.tv_des);
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        GetFileList_Res doc = (GetFileList_Res) getIntent().getSerializableExtra("doc");
        if (doc != null) {
            tv_title.setText(doc.getImage_name());
            webView.loadUrl(App_preference.getSharedprefInstance().getBaseURL() + "" + doc.getAttachFileName());
          /*  Picasso.with(this).load(App_preference.getSharedprefInstance().getBaseURL() + "" + doc.getAttachFileName())
                    .into(img_doc);*/
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }
}
