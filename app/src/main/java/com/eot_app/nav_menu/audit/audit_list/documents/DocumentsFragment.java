package com.eot_app.nav_menu.audit.audit_list.documents;


import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.nav_menu.audit.audit_list.documents.doc_model.GetFileList_Res;
import com.eot_app.nav_menu.audit.audit_list.documents.fileattach_mvp.Doc_Attch_Pc;
import com.eot_app.nav_menu.audit.audit_list.documents.fileattach_mvp.Doc_Attch_Pi;
import com.eot_app.nav_menu.audit.audit_list.documents.fileattach_mvp.Doc_Attch_View;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.language_support.LanguageController;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Callable;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.ViewType;

public class DocumentsFragment extends Fragment implements Doc_Attch_View, DocumentListAdapter.FileDesc_Item_Selected, OnPhotoEditorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final static int CAPTURE_IMAGE_CAMERA = 111;
    private final static int CAPTURE_IMAGE_GALLARY = 222;
    private final static int IMAGE_CROP = 333;
    private static final int PHOTO_EDIT_CODE = 147;
    private final int CAMERA_CODE = 100;
    private final int ATTACHFILE_CODE = 101;
    RecyclerView fileupload_rc;
    Doc_Attch_Pi doc_attch_pi;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout nodoc_linear;
    String captureImagePath;
    TextView noDocList;
    private DocumentListAdapter documentListAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String jobId;
    private EditText edtSearch;
    private ImageView imvCross;
    private String query = "";
    private ArrayList<GetFileList_Res> fileList_res = new ArrayList<>();
    // private EditImageDialog currentDialog = null;

    private SwipeRefreshLayout swipeRefreshLayout;


    public DocumentsFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DocumentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DocumentsFragment newInstance(String param1, String param2) {
        DocumentsFragment fragment = new DocumentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            jobId = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_documents, container, false);
        setHasOptionsMenu(true);
        initiliazeView(view);
        //  storageUserPermission();
        layoutManager = new GridLayoutManager(getActivity(), 2);
        fileupload_rc.setLayoutManager(layoutManager);
        initializeAdapter();
        doc_attch_pi = new Doc_Attch_Pc(this);
        doc_attch_pi.getAttachFileList(jobId);
        return view;
    }

    private void initializeAdapter() {
        ArrayList<GetFileList_Res> getFileList_res = new ArrayList<>();
        documentListAdapter = new DocumentListAdapter(this, getFileList_res);
        fileupload_rc.setAdapter(documentListAdapter);
    }

    private void initiliazeView(View view) {
        fileupload_rc = view.findViewById(R.id.fileupload_rc);
        nodoc_linear = view.findViewById(R.id.nodoc_linear);
        noDocList = view.findViewById(R.id.noDocList);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        noDocList.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_document_msg));
        FloatingActionButton fab = view.findViewById(R.id.doc_att);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFile();
            }
        });

        imvCross = view.findViewById(R.id.imvCross);
        edtSearch = view.findViewById(R.id.edtSearch);

        edtSearch.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.search));

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                query = edtSearch.getText().toString();
                if (query != null) {
                    if (query.length() >= 1) {
                        imvCross.setVisibility(View.VISIBLE);
                    } else {
                        imvCross.setVisibility(View.GONE);
                    }
                }

                query = edtSearch.getText().toString();
                DocumentsFragment.this.documentListAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (documentListAdapter != null) {
                    documentListAdapter.clearAllItems();
                }
                doc_attch_pi.getAttachFileList(jobId);

            }
        });

        imvCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
            }
        });

    }


    public void refreshDocuments() {
        if (doc_attch_pi != null) {
            if (documentListAdapter != null)
                documentListAdapter.clearAllItems();
            doc_attch_pi.getAttachFileList(jobId);
        }
    }

    void filter(String text) {
        ArrayList<GetFileList_Res> temp = new ArrayList();
        for (GetFileList_Res d : fileList_res) {
            if (d.getAttachFileActualName().toLowerCase().startsWith(text)) {
                temp.add(d);
            }
        }
        documentListAdapter.updateFileList(temp);
    }


    @Override
    public void onPause() {
        edtSearch.setText("");
        super.onPause();
    }


    @Override
    public void setList(ArrayList<GetFileList_Res> getFileList_res) {
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
        this.fileList_res = getFileList_res;
        if (getFileList_res.size() > 0) {
            nodoc_linear.setVisibility(View.GONE);
            fileupload_rc.setVisibility(View.VISIBLE);
            (documentListAdapter).updateFileList(getFileList_res);
        } else if (fileupload_rc.getAdapter() != null && fileupload_rc.getAdapter().getItemCount() == 0) {
            nodoc_linear.setVisibility(View.VISIBLE);
            fileupload_rc.setVisibility(View.GONE);
        }
    }

    @Override
    public void addView() {
        nodoc_linear.setVisibility(View.VISIBLE);
        fileupload_rc.setVisibility(View.GONE);
    }

    @Override
    public void selectFile() {
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
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
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                    // imageEditing(Uri.fromFile(new File(captureImagePath)));
                    try {
//                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        Bitmap photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                                Uri.fromFile(new File(captureImagePath)));
//get uri from current created path
                        File file = AppUtility.scaleToActualAspectRatio(captureImagePath, 1024f, 1024f);
                        if (file != null)
                            imageEditing(Uri.fromFile(file), "camera_capture");
                        //   imageEditing(Uri.fromFile(new File(captureImagePath)), "camera_capture");

                       /* EditImageDialog dialog = new EditImageDialog();
                        dialog.setBitmapImage(photo, Uri.fromFile(new File(captureImagePath)));
                        currentDialog = dialog;
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
//                        dialog.setArguments(bundle);
                        dialog.setTargetFragment(this, 101);
                        dialog.show(ft, "camera_capture");*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    return;
                }

                break;

            case CAPTURE_IMAGE_GALLARY:
                if (resultCode == RESULT_OK) {
                    Uri galreyImguriUri = data.getData();
                    //   String gallery_image_Path = com.eot_app.nav_menu.jobs.job_detail.documents.PathUtils.getPath(getActivity(), galreyImguriUri);
                    String gallery_image_Path = com.eot_app.nav_menu.jobs.job_detail.documents.PathUtils.getRealPath(getActivity(), galreyImguriUri);
                    String img_extension = gallery_image_Path.substring(gallery_image_Path.lastIndexOf("."));
                    /******('jpg','png','jpeg','pdf','doc','docx','xlsx','csv','xls'); supporting extensions*/
                    if (img_extension.equals(".jpg") || img_extension.equals(".png") || img_extension.equals(".jpeg")) {
                        imageEditing(data.getData(), "gallery_pick");
                    } else {
                        uploadFileDialog(gallery_image_Path);
                    }
                } else {
                    return;
                }
                break;
            case ATTACHFILE_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri fileUri = data.getData();
                        //  String filePath = com.eot_app.nav_menu.jobs.job_detail.documents.PathUtils.getPath(getActivity(), fileUri);
                        String filePath = com.eot_app.nav_menu.jobs.job_detail.documents.PathUtils.getRealPath(getActivity(), fileUri);
                        String extension = filePath.substring(filePath.lastIndexOf("."));
                        //('jpg','png','jpeg','pdf','doc','docx','xlsx','csv','xls'); supporting extensions
                        if (extension.equals(".jpg") || extension.equals(".png") || extension.equals(".jpeg")) {
                            imageEditing(data.getData(), "drive_document");
                        } else {
                            uploadFileDialog(filePath);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    return;
                }
                break;

            case PHOTO_EDIT_CODE:
                if (data != null && data.hasExtra("path")) {
                    String path = data.getStringExtra("path");
                    String name = data.getStringExtra("name");

                    uploadFile(path, name);
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //upload image edit highlighting feature for image
    private void imageEditing(Uri uri, String dialogTag) {
        Intent intent = new Intent(getActivity(), ActivityEditImageDialog.class);
        intent.putExtra("uri", uri);
        startActivityForResult(intent, PHOTO_EDIT_CODE);
    }


    //upload file dialog
    private void uploadFileDialog(final String selectedFilePath) {
        if (selectedFilePath == null) {
            return;
        }
        final File file = new File(selectedFilePath);
        String fname = file.getName();
        if (fname.contains(".")) {
            fname = fname.substring(0, fname.lastIndexOf("."));
        }
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View mView = layoutInflaterAndroid.inflate(R.layout.doc_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final TextView file_upload_title = mView.findViewById(R.id.file_upload_title);
        file_upload_title.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.do_you_want_change_name_image));
        final EditText dialog_title = mView.findViewById(R.id.dialog_title);
        dialog_title.setText(fname);

        builder.setView(mView)
                .setCancelable(false)
                .setPositiveButton(AppConstant.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        doc_attch_pi.uploadDocuments(jobId, selectedFilePath, dialog_title.getText().toString());
                    }
                })
                .setNegativeButton(AppConstant.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        dialog.show();
    }

    @Override
    public void OnItemClick_Document(GetFileList_Res getFileList_res) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(App_preference.getSharedprefInstance().getBaseURL() + "" + getFileList_res.getAttachFileName())));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e("", "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("", "");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSessionExpire(String msg) {
       /* AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "",
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        EotApp.getAppinstance().sessionExpired();
                        return null;
                    }
                });*/
    }

    public void uploadFile(String imagePath, String imageName) {
        doc_attch_pi.uploadDocuments(jobId, imagePath, imageName);
    }

    @Override
    public void fileExtensionNotSupport(String msg) {
        AppUtility.alertDialog(getActivity(), "", msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }

    private void diaolgShow(String msg) {
        AppUtility.alertDialog(getActivity(), "", msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode) {

    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onRemoveViewListener(int numberOfAddedViews) {

    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {

    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {

    }
}
