package com.eot_app.nav_menu.custom_fileds;

import static com.eot_app.nav_menu.audit.audit_list.details.AuditDetailsFragment.AUDITCUSTOMFILED;
import static com.eot_app.nav_menu.jobs.job_detail.detail.DetailFragment.CUSTOMFILED;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.custom_fileds.custom_filed_adpter.CustomFiledQueAdpter;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;
import com.eot_app.nav_menu.custom_fileds.customfield_mvp.CustomFieldList_PC;
import com.eot_app.nav_menu.custom_fileds.customfield_mvp.CustomFieldList_Pi;
import com.eot_app.nav_menu.custom_fileds.customfield_mvp.CustomFieldList_View;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model.Ans_Req;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model.Answer;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.AnswerModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CustomFiledListActivity extends AppCompatActivity implements CustomFieldList_View, View.OnClickListener {
    private final ArrayList<Answer> answerArrayList = new ArrayList<>();
    ArrayList<CustOmFormQuestionsRes> questionList = new ArrayList<>();
    boolean isfilled;
    /***AUDITCUSTOMFIELD use for set Intent result Audit or Job ******/
    private Boolean AUDITCUSTOMFIELD = false;
    private CustomFiledQueAdpter adapter;
    // TODO: Rename and change types of parameters
    private CustomFieldList_Pi custmFormPi;
    private String jobId = "";
    private RecyclerView fieldList;
    private TextView actionbar_title;
    private ImageView backme;
    private Button sbmt_btn;
    private Boolean SAVEANS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_filed_list);
        getSupportActionBar().hide();
        getintentData();
        intializeViews();
    }

    private void getintentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jobId = getIntent().getStringExtra("jobId");
            SAVEANS = getIntent().getBooleanExtra("ansedit", false);
            questionList = getIntent().getExtras().getParcelableArrayList("list");
            if (getIntent().hasExtra("AUDITCUSTOMFIELD")) {
                AUDITCUSTOMFIELD = getIntent().getBooleanExtra("AUDITCUSTOMFIELD", false);
            }

        }
    }

    private void intializeViews() {
        actionbar_title = findViewById(R.id.actionbar_title);
        actionbar_title.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_cutom_field));

        backme = findViewById(R.id.backme);
        backme.setOnClickListener(this);

        sbmt_btn = findViewById(R.id.sbmt_btn);
        sbmt_btn.setOnClickListener(this);
        btnTextSet();


        fieldList = findViewById(R.id.fieldList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        fieldList.setLayoutManager(layoutManager);
        adapter = new CustomFiledQueAdpter(questionList, this, new CustomFiledQueAdpter.CustomFiledCallBack() {
            @Override
            public void CustomFiledsList() {

            }
        });
        fieldList.setAdapter(adapter);
        custmFormPi = new CustomFieldList_PC(this);
    }

    private void btnTextSet() {
        if (!SAVEANS) {
            sbmt_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_btn));
        } else {
            sbmt_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_btn));
        }
    }

    private void showDialogs(String msg) {
        AppUtility.error_Alert_Dialog(this, msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backme:
                this.finish();
                break;
            case R.id.sbmt_btn:
                AppUtility.hideSoftKeyboard(this);
                if (SAVEANS) {
                    if ((App_preference.getSharedprefInstance().getLoginRes().getRights().get(0).getIsEditCustomFormVisible() == 1)) {
                        showDialogs(LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_filed_edit_msg));
                    } else {
                        ansAnsQuesRspncModel();
                        if (!emptyCheckFormValidation()) {
                            emptyAnsFieldError();
                            return;
                        }
                        if (answerArrayList.size() > 0) {
                            Ans_Req ans_req = null;
                            if (AUDITCUSTOMFIELD) {
                                ans_req = new Ans_Req(
                                        answerArrayList, questionList.get(0).getFrmId());
                                ans_req.setAudId(jobId);
                            } else {
                                ans_req = new Ans_Req(
                                        answerArrayList, questionList.get(0).getFrmId(), jobId);
                            }
                            custmFormPi.giveAns(ans_req);
                        }
                    }
                } else {
                    ansAnsQuesRspncModel();
                    if (!emptyCheckFormValidation()) {
                        emptyAnsFieldError();
                        return;
                    }
                    if (answerArrayList.size() > 0) {
                        Ans_Req ans_req = null;
                        if (AUDITCUSTOMFIELD) {
                            ans_req = new Ans_Req(
                                    answerArrayList, questionList.get(0).getFrmId());
                            ans_req.setAudId(jobId);
                        } else {
                            ans_req = new Ans_Req(
                                    answerArrayList, questionList.get(0).getFrmId(), jobId);
                            custmFormPi.giveAns(ans_req);
                        }
                        custmFormPi.giveAns(ans_req);
//                        Ans_Req ans_req = new Ans_Req(
//                                answerArrayList, questionList.get(0).getFrmId(), jobId);
//                        custmFormPi.giveAns(ans_req);
                    }
                }
                Log.e("", "");
                break;
        }
    }

    private boolean emptyCheckFormValidation() {
        boolean isChangeDetected = false;
        if (adapter != null && adapter.getTypeList() != null) {
            ArrayList<CustOmFormQuestionsRes> checkList = adapter.getTypeList();
            for (CustOmFormQuestionsRes qm : checkList) {
                List<AnswerModel> ans = qm.getAns();
                if (ans != null && ans.size() > 0) {
                    AnswerModel model = ans.get(0);
                    if (model != null && !TextUtils.isEmpty(model.getValue()))
                        isChangeDetected = true;
                }
            }
        }
        return isChangeDetected;
    }

    private void emptyAnsFieldError() {
        AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.enter_ans), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }

    public void ansAnsQuesRspncModel() {


        for (int i = 0; i < questionList.size(); i++) {
            String key = "";
            String ans = "";
            ArrayList<AnswerModel> ansArrayList = new ArrayList<>();
            Answer answer = null;
            switch (questionList.get(i).getType()) {
                case "8":
                    if (questionList.get(i).getAns() != null && questionList.get(i).getAns().size() > 0) {
                        AnswerModel answerModel = new AnswerModel(questionList.get(i).getAns().get(0).getKey(), questionList.get(i).getAns().get(0).getValue());
                        ansArrayList.add(answerModel);
                        answer = new Answer(this.questionList.get(i).getQueId(), this.questionList.get(i).getType(), ansArrayList, questionList.get(i).getFrmId());
                        answerArrayList.add(answer);
                    }

                    break;
                case "2":
                case "5":
                case "6":
                case "7":
                case "1":
                    if (questionList.get(i).getAns() != null && questionList.get(i).getAns().size() > 0) {
                        if (questionList.get(i).getType().equals("5")) {
                            if (!TextUtils.isEmpty(questionList.get(i).getAns().get(0).getValue())) {
                                long l = Long.parseLong(questionList.get(i).getAns().get(0).getValue());
                                String date = AppUtility.getDate(l, "dd-MMM-yyyy");
                                ans = date;
                            }
                        } else if (questionList.get(i).getType().equals("6")) {
                            if (!TextUtils.isEmpty(questionList.get(i).getAns().get(0).getValue())) {
                                long l = Long.parseLong(questionList.get(i).getAns().get(0).getValue());
                                String date = AppUtility.getDate(l, AppUtility.dateTimeByAmPmFormate(
                                        "hh:mm a", "kk:mm"));
                                ans = date;
                            }
                        } else if (questionList.get(i).getType().equals("7")) {
                            if (!TextUtils.isEmpty(questionList.get(i).getAns().get(0).getValue())) {
                                long l = Long.parseLong(questionList.get(i).getAns().get(0).getValue());
                                String date = AppUtility.getDate(l, AppUtility.dateTimeByAmPmFormate(
                                        "dd-MMM-yyyy hh:mm a", "dd-MMM-yyyy kk:mm"));
                                ans = date;
                            }
                        } else
                            ans = questionList.get(i).getAns().get(0).getValue();
                        AnswerModel answerModel = new AnswerModel(key, ans);
                        ansArrayList.add(answerModel);
                        answer = new Answer(this.questionList.get(i).getQueId(),
                                this.questionList.get(i).getType(), ansArrayList, questionList.get(i).getFrmId());
                        answerArrayList.add(answer);

                    }
                    break;
                case "4":
                case "3":
                    if (questionList.get(i).getAns() != null && questionList.get(i).getAns().size() > 0) {
                        List<AnswerModel> ans1 = questionList.get(i).getAns();
                        if (ans1 != null)
                            for (AnswerModel am : ans1) {
                                key = am.getKey();
                                ans = am.getValue();
                                AnswerModel answerModel = new AnswerModel(key, ans);
                                ansArrayList.add(answerModel);
                            }
                    }
                    if (ansArrayList.size() > 0) {
                        answer = new Answer(this.questionList.get(i).getQueId(), this.questionList.get(i).getType(), ansArrayList, questionList.get(i).getFrmId());
                        answerArrayList.add(answer);
                    } else {
                        answer = new Answer(this.questionList.get(i).getQueId(),
                                this.questionList.get(i).getType(), ansArrayList, questionList.get(i).getFrmId());
                        answerArrayList.add(answer);
                    }
                    break;
            }

        }

    }

    @Override
    public void onSessionExpire(String msg) {
        AppUtility.alertDialog(this, LanguageController.getInstance()
                        .getMobileMsgByKey(AppConstant.dialog_error_title), msg, LanguageController.getInstance()
                        .getMobileMsgByKey(AppConstant.ok), "",
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        EotApp.getAppinstance().sessionExpired();
                        return null;
                    }
                });
    }

    @Override
    public void onSubmitSuccess() {
        answerArrayList.clear();
        Intent intent = new Intent();
        if (AUDITCUSTOMFIELD) {
            setResult(AUDITCUSTOMFILED, intent);
        } else {
            setResult(CUSTOMFILED, intent);
        }
        this.finish();
    }
}