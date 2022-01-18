package com.eot_app.nav_menu.audit.audit_list.equipment.remark;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.chat.img_crop_pkg.ImageCropFragment;
import com.eot_app.nav_menu.jobs.job_detail.customform.MyAttachment;
import com.eot_app.nav_menu.jobs.job_detail.documents.PathUtils;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.MyFormInterFace;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model.Answer;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.AnswerModel;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.QuesRspncModel;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.job_equ_remrk.JobEquRemarkRemarkActivity;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemarkCustomFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemarkCustomFormFragment extends Fragment implements View.OnClickListener, MyAttachment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final static int CAPTURE_IMAGE_GALLARY = 2222;
    private final int CAMERA_CODE = 1002;
    private final int ATTACHFILE_CODE = 1012;
    private final List<MultipartBody.Part> docAns = new ArrayList<>();
    private final List<MultipartBody.Part> signAns = new ArrayList<>();
    private final ArrayList<String> signQueIdArray = new ArrayList<>();
    private final ArrayList<String> docQueIdArray = new ArrayList<>();
    RecyclerView recyclerView;
    ArrayList<QuesRspncModel> question_List;
    ArrayList<Answer> answerArrayList = new ArrayList<>();
    // TODO: Rename and change types of parameters
    Button saveBtn;
    boolean isfilled;
    int position = 0;
    ImageView attchmentView, deleteAttchment;
    Button addAttchment;
    private boolean isMandatoryNotFill;
    private String jobId = "";
    private String queList;
    private OnFragmentInteractionListener mListener;
    //  private QuestionListAdapter qla;
    private RemarkQuestionListAdpter qla;
    private String captureImagePath;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param jobId   Parameter 1.
     * @param queList Parameter 2.
     * @return A new instance of fragment CustomFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RemarkCustomFormFragment newInstance(String jobId, String queList) {
        RemarkCustomFormFragment fragment = new RemarkCustomFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, jobId);
        args.putString(ARG_PARAM2, queList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jobId = getArguments().getString(ARG_PARAM1);
            queList = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_remark_custom_form, container, false);

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<QuesRspncModel>>() {
        }.getType();
        question_List = gson.fromJson(queList, listType);
        saveBtn = view.findViewById(R.id.saveBtn);
        saveBtn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_btn));
        saveBtn.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        qla = new RemarkQuestionListAdpter(question_List, getActivity(), (MyFormInterFace) getActivity(), this);
        recyclerView.setAdapter(qla);
        isFormPreFilled();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        if (jobId.equals("RemarkActivity")) {
            ((RemarkActivity) getActivity()).setTitlesForRemarkForm(false);
        } else if (jobId.equals("JobEquRemarkRemarkActivity")) {
            ((JobEquRemarkRemarkActivity) getActivity()).setTitlesForRemarkForm(false);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveBtn:
                getAnsListSaveBtn();
                /**    if question is mandatory but not fill   ***/
                if (isMandatoryNotFill) {
                    isMandatoryNotFill = false;
                    AppUtility.alertDialog(getActivity(), "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.fill_all_mandatory_questions), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return null;
                        }
                    });
                    return;
                }

                if (jobId.equals("RemarkActivity")) {
                    ((RemarkActivity) getActivity()).getAnsList(answerArrayList);
                } else if (jobId.equals("JobEquRemarkRemarkActivity")) {
                    ((JobEquRemarkRemarkActivity) getActivity()).getAnsList(answerArrayList, signAns, docAns, signQueIdArray, docQueIdArray);
                }
                getActivity().onBackPressed();

                break;
        }
    }

    private void isFormPreFilled() {
        if (question_List != null) {
            for (QuesRspncModel qm : question_List) {
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
        if (question_List != null) {
            for (QuesRspncModel qm : question_List) {
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
        AppUtility.alertDialog(getActivity(), "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.enter_ans), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }

    public void getAnsListSaveBtn() {
        for (int i = 0; i < question_List.size(); i++) {
            String key = "";
            String ans = "";
            ArrayList<AnswerModel> ansArrayList = new ArrayList<>();
            Answer answer = null;
            switch (question_List.get(i).getType()) {

                case "11":
                    if (question_List.get(i).getAns().size() > 0) {
                        ans = question_List.get(i).getAns().get(0).getValue();
                        if (question_List.get(i).getMandatory().equals("1"))
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
                            docQueIdArray.add(question_List.get(i).getQueId());

                            AnswerModel answerModels = new AnswerModel("0", ans);
                            ansArrayList.add(answerModels);
                            answer = new Answer(this.question_List.get(i).getQueId(),
                                    this.question_List.get(i).getType(), ansArrayList, this.question_List.get(i).getFrmId());
                            answerArrayList.add(answer);

                        } else {
                         /*   AnswerModel answerModels = new AnswerModel("0", "");
                            ansArrayList.add(answerModels);
                            answer = new Answer(this.quesRspncModelList.get(i).getQueId(),
                                    this.quesRspncModelList.get(i).getType(), ansArrayList,
                                    this.quesRspncModelList.get(i).getFrmId());
                            answerArrayList.add(answer);*/
                        }

                    } else if (question_List.get(i).getAns().size() == 0)
                        if (question_List.get(i).getMandatory().equals("1"))
                            isMandatoryNotFill = true;
                    break;
                /***case for Signature****/
                case "10":
                    Log.e("", "");
                    if (question_List.get(i).getAns().size() > 0) {
                        ans = question_List.get(i).getAns().get(0).getValue();
                        if (question_List.get(i).getMandatory().equals("1"))
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
                            signQueIdArray.add(question_List.get(i).getQueId());

                            AnswerModel docanswerModels = new AnswerModel("0", ans);
                            ansArrayList.add(docanswerModels);
                            answer = new Answer(this.question_List.get(i).getQueId(),
                                    this.question_List.get(i).getType(), ansArrayList, this.question_List.get(i).getFrmId());
                            answerArrayList.add(answer);
                        } else {
                          /*  AnswerModel answerModels = new AnswerModel("0", "");
                            ansArrayList.add(answerModels);
                            answer = new Answer(this.quesRspncModelList.get(i).getQueId(),
                                    this.quesRspncModelList.get(i).getType(), ansArrayList,
                                    this.quesRspncModelList.get(i).getFrmId());
                            answerArrayList.add(answer);*/
                        }
                    } else if (question_List.get(i).getAns().size() == 0)
                        if (question_List.get(i).getMandatory().equals("1"))
                            isMandatoryNotFill = true;

                    break;


                case "8":
                    if (question_List.get(i).getAns() != null && question_List.get(i).getAns().size() > 0) {
                        ans = question_List.get(i).getAns().get(0).getValue();
                        AnswerModel answerModel = new AnswerModel(question_List.get(i).getAns().get(0).getKey(), question_List.get(i).getAns().get(0).getValue());
                        ansArrayList.add(answerModel);
                        answer = new Answer(this.question_List.get(i).getQueId(), this.question_List.get(i).getType(), ansArrayList, question_List.get(i).getFrmId());
                        answerArrayList.add(answer);
                    }

                    if (question_List.get(i).getMandatory().equals("1"))
                        if (ans.equals("0"))
                            isMandatoryNotFill = true;
                    break;
                case "2":
                case "5":
                case "6":
                case "7":
                case "1":
                    if (question_List.get(i).getAns() != null && question_List.get(i).getAns().size() > 0) {
                        if (question_List.get(i).getType().equals("5")) {
                            if (!TextUtils.isEmpty(question_List.get(i).getAns().get(0).getValue())) {
                                long l = Long.parseLong(question_List.get(i).getAns().get(0).getValue());
                                String date = AppUtility.getDate(l, "dd-MMM-yyyy");
                                ans = date;
                            }
                        } else if (question_List.get(i).getType().equals("6")) {
                            if (!TextUtils.isEmpty(question_List.get(i).getAns().get(0).getValue())) {
                                long l = Long.parseLong(question_List.get(i).getAns().get(0).getValue());
                                String date = AppUtility.getDate(l, "hh:mm a");
                                ans = date;
                            }
                        } else if (question_List.get(i).getType().equals("7")) {
                            if (!TextUtils.isEmpty(question_List.get(i).getAns().get(0).getValue())) {
                                long l = Long.parseLong(question_List.get(i).getAns().get(0).getValue());
                                String date = AppUtility.getDate(l, "dd-MMM-yyyy hh:mm a");
                                ans = date;
                            }
                        } else
                            ans = question_List.get(i).getAns().get(0).getValue();
                        if (question_List.get(i).getMandatory().equals("1"))
                            if (TextUtils.isEmpty(ans))
                                isMandatoryNotFill = true;

                        AnswerModel answerModel = new AnswerModel(key, ans);
                        ansArrayList.add(answerModel);
                        answer = new Answer(this.question_List.get(i).getQueId(),
                                this.question_List.get(i).getType(), ansArrayList);
                        answerArrayList.add(answer);
                    } else if (question_List.get(i).getMandatory().equals("1")) {
                        isMandatoryNotFill = true;
                    }
                    break;
                case "4":
                case "3":
                    if (question_List.get(i).getAns() != null && question_List.get(i).getAns().size() > 0) {
                        List<AnswerModel> ans1 = question_List.get(i).getAns();
                        if (ans1 != null)
                            for (AnswerModel am : ans1) {
                                key = am.getKey();
                                ans = am.getValue();
                                AnswerModel answerModel = new AnswerModel(key, ans);
                                ansArrayList.add(answerModel);
                                if (question_List.get(i).getMandatory().equals("1"))
                                    if (TextUtils.isEmpty(ans))
                                        isMandatoryNotFill = true;
                            }
                    }
                    if (ansArrayList.size() > 0) {
                        answer = new Answer(this.question_List.get(i).getQueId(), this.question_List.get(i).getType(), ansArrayList);
                        answerArrayList.add(answer);
                    } else if (question_List.get(i).getMandatory().equals("1")) {
                        isMandatoryNotFill = true;
                    } else {
                        answer = new Answer(this.question_List.get(i).getQueId(),
                                this.question_List.get(i).getType(), ansArrayList);
                        answerArrayList.add(answer);
                    }
                    break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void selectFileWithoutAttchment(int position, ImageView attchmentView, ImageView deleteAttchment, Button addAttchment) {
        this.deleteAttchment = deleteAttchment;
        this.position = position;
        this.attchmentView = attchmentView;
        this.addAttchment = addAttchment;


        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(R.layout.bottom_image_chooser);
        TextView camera = dialog.findViewById(R.id.camera);
        camera.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.camera));
        TextView gallery = dialog.findViewById(R.id.gallery);
        gallery.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.gallery));
        LinearLayout ll_doc = dialog.findViewById(R.id.driveLayout);
        ll_doc.setVisibility(View.GONE);
        TextView drive_document = dialog.findViewById(R.id.drive_document);
        drive_document.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.document));
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtility.askCameraTakePicture(getActivity())) {
                    takePictureFromCamera();
                }
                dialog.dismiss();
            }

        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtility.askGalaryTakeImagePermiSsion(getActivity())) {
                    getImageFromGallray();
                }
                dialog.dismiss();
            }


        });

        drive_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtility.askGalaryTakeImagePermiSsion(getActivity())) {
                    takeimageFromGalary();//only for drive documents
                }
                dialog.dismiss();
            }
        });
        dialog.show();
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


    /**
     * get image from camera & edit & croping functinallity
     */
    private void takePictureFromCamera() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);// new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("", ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.eot_app.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {

        Calendar calendar = Calendar.getInstance();
        long imageFileName = calendar.getTime().getTime();

        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);// return path

        File directoryPath = new File(storageDir.getPath());
        File image = File.createTempFile(
                String.valueOf(imageFileName),  /* prefix */
                //"__" + question_List.get(position).getQueId() + ".jpg",         /* suffix */
                ".jpg",         /* suffix */
                directoryPath   /* directory */
        );
        captureImagePath = image.getAbsolutePath();
        return new File(image.getPath());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        File file = AppUtility.scaleToActualAspectRatio(captureImagePath, 1024f, 1024f);
                        if (file != null) {
                            imageCroping(Uri.fromFile(new File(captureImagePath)));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    return;
                }
                break;
            case CAPTURE_IMAGE_GALLARY:
                if (data == null) {
                    return;
                } else {
                    Uri uri = data.getData();
                    imageCroping(uri);
                }

                break;
            case ATTACHFILE_CODE:
                if (resultCode == RESULT_OK) {
                    Uri fileUri = data.getData();
                    //  String filePath = PathUtils.getPath(getActivity(), fileUri);
                    String filePath = PathUtils.getRealPath(getActivity(), fileUri);
                    try {
                        String extension = filePath.substring(filePath.lastIndexOf("."));
                        //('jpg','png','jpeg','pdf','doc','docx','xlsx','csv','xls'); supporting extensions
                        if (extension.equals(".jpg") || extension.equals(".png") || extension.equals(".jpeg")) {
                            imageCroping(fileUri);
                        } else {
                            qla.showAttchmentView(position, filePath, attchmentView, deleteAttchment, addAttchment);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                break;
        }

    }

    private void imageCroping(final Uri uri) {
        ImageCropFragment myfragment = ImageCropFragment.newInstance("Uri", uri.toString());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        myfragment.setCallbackListener(new ImageCropFragment.MyDialogInterface() {
            @Override
            public void onClickContinuarEvent(Uri permisoRequerido) {
                String path = "";
                //    path = PathUtils.getPath(getActivity(), uri);
                path = PathUtils.getRealPath(getActivity(), uri);
                if (!path.isEmpty()) {
                    qla.showAttchmentView(position, path, attchmentView, deleteAttchment, addAttchment);
                }
            }
        });
        myfragment.setTargetFragment(this, 101);
        myfragment.show(ft, "FormQueAns_Activity");
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}