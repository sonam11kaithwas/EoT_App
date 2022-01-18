package com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.UploadDocumentActivity;
import com.eot_app.nav_menu.jobs.job_detail.chat.img_crop_pkg.ImageCropFragment;
import com.eot_app.nav_menu.jobs.job_detail.customform.CustomFormFragment;
import com.eot_app.nav_menu.jobs.job_detail.customform.cstm_form_model.CustomFormList_Res;
import com.eot_app.nav_menu.jobs.job_detail.documents.PathUtils;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model.Ans_Req;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model.Answer;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.form_ques_mvp.Que_View;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.form_ques_mvp.Que_pi;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.form_ques_mvp.Qus_pc;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.AnswerModel;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.QuesGetModel;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.QuesRspncModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FormQueAns_Activity extends UploadDocumentActivity implements View.OnClickListener, Que_View, MyFormInterFace, ImageCropFragment.MyDialogInterface {
    private final static int CAPTURE_IMAGE_GALLARY = 222;
    public static boolean isFullLoad = false;
    private final ArrayList<Answer> answerArrayList = new ArrayList<>();
    private final int CAMERA_CODE = 100;
    private final int ATTACHFILE_CODE = 101;
    private final List<MultipartBody.Part> docAns = new ArrayList<>();
    private final List<MultipartBody.Part> signAns = new ArrayList<>();
    private final ArrayList<String> signQueIdArray = new ArrayList<>();
    private final ArrayList<String> docQueIdArray = new ArrayList<>();
    boolean isfilled;
    int position = 0;
    private ImageView attchmentView, deleteAttchment;
    private Button addAttchment;
    private RelativeLayout client_container;
    private CustomFormList_Res customFormList;
    private Que_pi queAns_pi;
    private FrameLayout frameLayout;
    private FragmentTransaction ft;
    private Fragment myfragment;
    private QuesGetModel quesGetModel;
    private RecyclerView recycleView_parent;
    private TextView actionbar_title;
    private QuestionListAdapter questionListAdapter;
    private List<QuesRspncModel> quesRspncModelList = new ArrayList<>();
    private String jobId;
    private View em_layout;
    private TextView nolist_txt;
    private RelativeLayout rl;
    private int title_count = 1;
    private Button skip_btn, sbmt_btn;
    private boolean isMandatoryNotFill;
    private LinearLayout linearLayout2;
    private String captureImagePath;


    @Override
    public void onSessionExpire(String msg) {
        AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }

    @Override
    public void showOfflineAlert(String msg) {
        AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                setResult(RESULT_OK);
                finish();
                return null;
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_que_ans);
        getSupportActionBar().hide();
        customFormList = getIntent().getParcelableExtra("formId");
        jobId = getIntent().getStringExtra("jobId");
        initializeView();
    }

    private void initializeView() {
        client_container = findViewById(R.id.client_container);
        linearLayout2 = findViewById(R.id.linearLayout2);

        em_layout = findViewById(R.id.em_layout);
        nolist_txt = findViewById(R.id.nolist_txt);
        rl = findViewById(R.id.rl);
        sbmt_btn = findViewById(R.id.sbmt_btn);
        sbmt_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.submit_btn));
        sbmt_btn.setOnClickListener(this);
        skip_btn = findViewById(R.id.skip_btn);
        skip_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.skip));
        if (customFormList.getMandatory().equals("1")) {
            skip_btn.setEnabled(false);
            skip_btn.setBackgroundColor(getResources().getColor(R.color.hintcolor));
        } else {
            skip_btn.setBackground(getResources().getDrawable(R.drawable.submit_btn));
            skip_btn.setOnClickListener(this);
        }
        findViewById(R.id.backme).setOnClickListener(this);
        actionbar_title = findViewById(R.id.actionbar_title);
        actionbar_title.setText("1" + "  " + customFormList.getFrmnm());

        recycleView_parent = findViewById(R.id.recycleView_parent);
        frameLayout = findViewById(R.id.framlayout);
        queAns_pi = new Qus_pc(this);
        quesGetModel = new QuesGetModel("-1", customFormList.getFrmId(), App_preference.getSharedprefInstance().getLoginRes().getUsrId(), jobId);
        queAns_pi.getQuestions(quesGetModel);
    }

    @Override
    public void questionlist(List<QuesRspncModel> quesRspncModelList) {
        /*******/

        this.quesRspncModelList = quesRspncModelList;
        if (quesRspncModelList.size() > 0) {
            em_layout.setVisibility(View.GONE);
            rl.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recycleView_parent.setLayoutManager(linearLayoutManager);
            questionListAdapter = new QuestionListAdapter((ArrayList<QuesRspncModel>) quesRspncModelList, this,
                    this);
            recycleView_parent.setAdapter(questionListAdapter);
            isFormPreFilled();
        } else {
            em_layout.setVisibility(View.VISIBLE);
            rl.setVisibility(View.GONE);
            nolist_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_form_list));
        }
    }

    @Override
    public void onSubmitSuccess(String msg) {
        EotApp.getAppinstance().showToastmsg(msg);
        setResult(RESULT_OK);
        answerArrayList.clear();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sbmt_btn:
//                if keyboard open close it first
                AppUtility.hideSoftKeyboard(this);


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
                if (!isfilled)
                    if (!emptyCheckFormValidation()) {
                        emptyAnsFieldError();
                        return;
                    }
                if (answerArrayList.size() > 0) {
                    Ans_Req ans_req = new Ans_Req(quesGetModel.getUsrId(), answerArrayList,
                            customFormList.getFrmId(), jobId);
                    queAns_pi.addAnswerWithAttachments(ans_req, signAns, docAns, signQueIdArray, docQueIdArray);
                }
                break;
            case R.id.skip_btn:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.backme:
                onBackPressed();
                break;
        }
    }

    private void isFormPreFilled() {
        if (questionListAdapter != null && questionListAdapter.getTypeList() != null) {
            ArrayList<QuesRspncModel> checkList = questionListAdapter.getTypeList();
            for (QuesRspncModel qm : checkList) {
                List<AnswerModel> ans = qm.getAns();
                if (ans != null && ans.size() > 0) {
                    AnswerModel model = ans.get(0);
                    if (model != null && !TextUtils.isEmpty(model.getValue()))
                        isfilled = true;
                }
            }
        }
    }

    private boolean emptyCheckFormValidation() {
        boolean isChangeDetected = false;
        if (questionListAdapter != null && questionListAdapter.getTypeList() != null) {
            ArrayList<QuesRspncModel> checkList = questionListAdapter.getTypeList();
            for (QuesRspncModel qm : checkList) {
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

    @Override
    public void getAnsId(String ansId) {
        quesGetModel = new QuesGetModel(ansId, customFormList.getFrmId(), App_preference.getSharedprefInstance().getLoginRes().getUsrId(), jobId);
        queAns_pi.getQuestions(quesGetModel);
    }


    @Override
    public void addfragmentDynamically(List<QuesRspncModel> quesRspncModelList) {
        if (!quesRspncModelList.isEmpty()) {
            isFullLoad = false;
            String queList = new Gson().toJson(quesRspncModelList);
            ft = getSupportFragmentManager().beginTransaction();
            myfragment = CustomFormFragment.newInstance("NO", queList);
            ft.add(R.id.framlayout, myfragment, "Fragment add successFully.....").addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack("close");
            try {
                ft.commit();
            } catch (Exception exception) {
                ft.commitAllowingStateLoss();
                exception.printStackTrace();
            }
            updatePageCount(true);//count increase
        }

    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();

        if (view != null) {

            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        closeKeyboard();
        int fragments = getFragmentManager().getBackStackEntryCount();
        if (fragments > 0) {
            getFragmentManager().popBackStack();
        } else if (customFormList.getMandatory().equals("1")) {
            setResult(RESULT_CANCELED);
            super.onBackPressed();
        } else {
            setResult(RESULT_OK);
            super.onBackPressed();
        }

    }

    public void updatePageCount(boolean update) {
        if (update) {
            title_count += 1;
            actionbar_title.setText("" + title_count + "  " + customFormList.getFrmnm());
        } else {
            title_count -= 1;
            actionbar_title.setText("" + title_count + "  " + customFormList.getFrmnm());
        }
    }


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
                        answer = new Answer(this.quesRspncModelList.get(i).getQueId(), this.quesRspncModelList.get(i).getType(), ansArrayList, quesRspncModelList.get(i).getFrmId());
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
                                this.quesRspncModelList.get(i).getType(), ansArrayList, this.quesRspncModelList.get(i).getFrmId());
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
                                this.quesRspncModelList.get(i).getType(), ansArrayList,
                                this.quesRspncModelList.get(i).getFrmId());
                        answerArrayList.add(answer);
                    } else if (quesRspncModelList.get(i).getMandatory().equals("1")) {
                        isMandatoryNotFill = true;
                    } else {
                        answer = new Answer(this.quesRspncModelList.get(i).getQueId(),
                                this.quesRspncModelList.get(i).getType(), ansArrayList
                                , this.quesRspncModelList.get(i).getFrmId());
                        answerArrayList.add(answer);
                    }
                    break;
            }
        }
    }

    @Override
    public void finishMuAvtivity() {
        setResult(RESULT_CANCELED);
        answerArrayList.clear();
        this.finish();
    }

    public void getAnsList(ArrayList<Answer> answerArray, List<MultipartBody.Part> signAns,
                           List<MultipartBody.Part> docAns, ArrayList<String> signQueIdArray, ArrayList<String> docQueIdArray) {
        Log.e("Size--->>", ">>>>>" + answerArray.size());
        linearLayout2.setVisibility(View.VISIBLE);
        this.signQueIdArray.addAll(signQueIdArray);
        this.docQueIdArray.addAll(docQueIdArray);
        this.signAns.addAll(signAns);
        this.docAns.addAll(docAns);
        answerArrayList.addAll(answerArray);
    }

    private void emptyAnsFieldError() {
        AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.enter_ans), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }


    public void selectFile(int position, ImageView attchmentView, ImageView deleteAttchment, Button addAttchment) {//, ImageView attchmentView, ImageView item_View
        this.deleteAttchment = deleteAttchment;
        this.position = position;
        this.attchmentView = attchmentView;
        this.addAttchment = addAttchment;


        //parent class function in UploadDocumentActivity called for image and attachment selection
        selectFile(true);

    }

    @Override
    public void onClickContinuarEvent(Uri uri) {
        String path = "";
        //    path = PathUtils.getPath(this, uri);
        path = PathUtils.getRealPath(this, uri);
        if (!path.isEmpty()) {
            questionListAdapter.showAttchmentView(position, path, attchmentView, deleteAttchment, addAttchment);//, attchmentView,deleteAttchment
        }
    }

    @Override
    public void onDocumentSelected(String path, boolean isImage) {
        super.onDocumentSelected(path, isImage);
        if (questionListAdapter != null)
            questionListAdapter.showAttchmentView(position, path, attchmentView, deleteAttchment, addAttchment);


    }
}

