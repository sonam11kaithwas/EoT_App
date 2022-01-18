package com.eot_app;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.eot_app.nav_menu.audit.audit_list.documents.ActivityEditImageDialog;
import com.eot_app.nav_menu.jobs.job_detail.chat.img_crop_pkg.ImageCropFragment;
import com.eot_app.nav_menu.jobs.job_detail.documents.PathUtils;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Sonam kaithwas on 5/5/21.
 */
public class UploadDocumentFragment extends Fragment implements ImageCropFragment.MyDialogInterface {
    private static final int PHOTO_EDIT_CODE = 147;
    private final static int CAPTURE_IMAGE_GALLARY = 222;
    private final int CAMERA_CODE = 100;
    private final int ATTACHFILE_CODE = 102;
    private String captureImagePath;
    private String path = "";


    public void selectFile(boolean hideAttachment) {
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(R.layout.bottom_image_chooser);
        TextView camera = dialog.findViewById(R.id.camera);
        camera.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.camera));
        TextView gallery = dialog.findViewById(R.id.gallery);
        gallery.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.gallery));
        LinearLayout driveLayout = dialog.findViewById(R.id.driveLayout);
        TextView drive_document = dialog.findViewById(R.id.drive_document);
        drive_document.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.document));
        if (hideAttachment) {
            driveLayout.setVisibility(View.GONE);
        }
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtility.askCameraTakePicture(getActivity())) {
                    getPictureFromCamera();
                }
                dialog.dismiss();
            }

        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtility.askGalaryTakeImagePermiSsion(getActivity())) {
                    getImageFromGallery();
                }
                dialog.dismiss();
            }


        });

        drive_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtility.askGalaryTakeImagePermiSsion(getActivity())) {
                    getDocumentsFromGallery();//only for drive documents
                }
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    public void getPictureFromCamera() {
        if (!AppUtility.askCameraTakePicture(getActivity()))
            return;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Eot Directory");

        if (!path.exists()) {
            path.mkdir();
        }

        File imageFile = null;
        try {
            imageFile = createImageFile();
            //File.createTempFile("Your file Name", ".jpg", path);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", imageFile);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        List<ResolveInfo> resInfoList = getActivity().getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;


            getActivity().grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        startActivityForResult(takePictureIntent, CAMERA_CODE); // IMAGE_CAPTURE = 0

     /*   // Ensure that there's a camera activity to handle the intent
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
                try {
                    startActivityForResult(takePictureIntent, CAMERA_CODE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }*/
    }

    public void getImageFromGallery() {
        if (!AppUtility.askGalaryTakeImagePermiSsion(getActivity()))
            return;
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, CAPTURE_IMAGE_GALLARY);
    }

    public void getDocumentsFromGallery() {
        if (!AppUtility.askGalaryTakeImagePermiSsion(getActivity()))
            return;
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
        documentIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        documentIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(documentIntent, ATTACHFILE_CODE);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case CAMERA_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        File file = AppUtility.scaleToActualAspectRatio(captureImagePath, 1024f, 1024f);
                        if (file != null) {
                            imageEditing(Uri.fromFile(new File(captureImagePath)));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    return;
                }
                break;
            case CAPTURE_IMAGE_GALLARY:
            case ATTACHFILE_CODE:
                if (resultCode == RESULT_OK) {
                    Uri galreyImguriUri = data.getData();
                    //     String gallery_image_Path = PathUtils.getPath(getActivity(), galreyImguriUri);
                    String gallery_image_Path = PathUtils.getRealPath(getActivity(), galreyImguriUri);
                    String img_extension = gallery_image_Path.substring(gallery_image_Path.lastIndexOf("."));
                    /******('jpg','png','jpeg','pdf','doc','docx','xlsx','csv','xls'); supporting extensions*/
                    if (img_extension.equals(".jpg") || img_extension.equals(".png") || img_extension.equals(".jpeg")) {
                        imageEditing(data.getData());
                    } else {
                        onDocumentSelected(gallery_image_Path, false);
                    }
                } else {
                    return;
                }
                break;
            case PHOTO_EDIT_CODE:
                if (data != null && data.hasExtra("path")) {
                    String path = data.getStringExtra("path");
                    String name = data.getStringExtra("name");
                    onDocumentSelected(path, true);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /*****upload image edit highlighting feature for image********/
    private void imageEditing(Uri uri) {
        Intent intent = new Intent(getActivity(), ActivityEditImageDialog.class);
        intent.putExtra("uri", uri);
        intent.putExtra("allowOffline", true);
        startActivityForResult(intent, PHOTO_EDIT_CODE);
    }

    @Override
    public void onClickContinuarEvent(Uri permisoRequerido) {
        //   path = PathUtils.getPath(getActivity(), permisoRequerido);
        path = PathUtils.getRealPath(getActivity(), permisoRequerido);
        if (!path.isEmpty()) {
            File file = new File(path);
            if (file != null && file.exists()) {
                onDocumentSelected(path, true);
            }
        }
    }

    public void onDocumentSelected(String path, boolean isImage) {
    }
}
