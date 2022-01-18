package com.eot_app.nav_menu.jobs.job_detail.documents;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.eot_app.R;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.SaveSettings;


public class EditImageDialog extends DialogFragment implements View.OnClickListener, CropImageView.OnSetImageUriCompleteListener, CropImageView.OnGetCroppedImageCompleteListener {
    public static final String FIXED_ASPECT_RATIO = "extra_fixed_aspect_ratio";
    public static final String EXTRA_ASPECT_RATIO_X = "extra_aspect_ratio_x";
    public static final String EXTRA_ASPECT_RATIO_Y = "extra_aspect_ratio_y";
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 100;
    OnImageEdited onImageEdited;
    private Bitmap imageBitmap;
    private PhotoEditorView photo_editor_view;
    private PhotoEditor photoEditor;
    private EditText edt_file_name;
    //  private EditText edt_desc;
    private File file;
    private ImageView image_paint, image_back, image_undo, image_save, image_green, image_red, image_blue, image_crop;
    private String defaultFileName = "";
    private boolean colorButtonFlag = false;
    //  private DocumentsFragment documentsFragment;
    private CropImageView cropImageView;
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;
    private boolean isFixedAspectRatio = false;
    private TextView cancel_txt, crop_txt;
    private ConstraintLayout constraintLayout_editImage;
    private RelativeLayout cropImageViewLayout;
    private Uri uri;

    public EditImageDialog() {

    }

    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {

        if (selectedImage.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    public void setOnImageEdited(OnImageEdited onImageEdited) {
        this.onImageEdited = onImageEdited;
    }

    public void setBitmapImage(Bitmap bitmap, Uri uri) {
        this.imageBitmap = bitmap;
        this.uri = uri;
    }

    private void saveImage(final String imageName) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(getActivity());
            try {
                SaveSettings saveSettings = new SaveSettings.Builder()
                        .setClearViewsEnabled(true)
                        .setTransparencyEnabled(true)
                        .build();

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (file.exists()) {
                    photoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                        @Override
                        public void onSuccess(@NonNull String imagePath) {
                            AppUtility.progressBarDissMiss();
                            onImageEdited.onImageSaved(imagePath);
                            dismiss();
                            Log.d("TAG", "success");
                        }

                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("TAG", "failed");
                            AppUtility.progressBarDissMiss();
                        }
                    });
                } else {
                    Log.d("TAG", "file does't exist");
                    AppUtility.progressBarDissMiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return null;
                }
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.edit_image_dialog_job_layout, container, false);
        findViews(view);

        return view;
    }

    private void getCropImageRatios() {
        isFixedAspectRatio = getActivity().getIntent().getBooleanExtra(FIXED_ASPECT_RATIO, false);
        mAspectRatioX = getActivity().getIntent().getIntExtra(EXTRA_ASPECT_RATIO_X, DEFAULT_ASPECT_RATIO_VALUES);
        mAspectRatioY = getActivity().getIntent().getIntExtra(EXTRA_ASPECT_RATIO_Y, DEFAULT_ASPECT_RATIO_VALUES);

        hideEditImageView();

        // Initialize components of the app
        // If you want to fix the aspect ratio, set it to 'true'

        cropImageView.setFixedAspectRatio(isFixedAspectRatio);

        /**load bitmap from uri */
        cropImageView.setImageUriAsync(uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        cropImageView.setOnSetImageUriCompleteListener(this);
        cropImageView.setOnGetCroppedImageCompleteListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        cropImageView.setOnSetImageUriCompleteListener(null);
        cropImageView.setOnGetCroppedImageCompleteListener(null);
    }

    private void findViews(View view) {

        image_back = view.findViewById(R.id.image_back);
        image_back.setOnClickListener(this);
        image_paint = view.findViewById(R.id.image_paint);
        image_paint.setOnClickListener(this);
        image_undo = view.findViewById(R.id.image_undo);
        image_undo.setOnClickListener(this);
        image_save = view.findViewById(R.id.image_save);
        image_save.setOnClickListener(this);
        image_green = view.findViewById(R.id.image_green);
        image_blue = view.findViewById(R.id.image_blue);
        image_red = view.findViewById(R.id.image_red);

        image_crop = view.findViewById(R.id.image_crop);
        image_crop.setOnClickListener(this);

        constraintLayout_editImage = view.findViewById(R.id.constraintLayout_editImage);

        intializedCropviews(view);


        image_green.setOnClickListener(this);
        image_blue.setOnClickListener(this);
        image_red.setOnClickListener(this);

        edt_file_name = view.findViewById(R.id.edt_file_name);
        // edt_desc = view.findViewById(R.id.edt_des);
        edt_file_name.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.enter_file_name));
        photo_editor_view = view.findViewById(R.id.photo_editor_view);
        photo_editor_view.getSource().setImageBitmap(imageBitmap);


        photoEditor = new PhotoEditor.Builder(getContext(), photo_editor_view)
                .setPinchTextScalable(true)
                .build();

        // file = new File(Environment.getExternalStorageDirectory()
        file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                + File.separator + "image_"
                + System.currentTimeMillis() + ".png");
        try {
            file.createNewFile();
            String fname = file.getName();
            if (fname.contains(".")) {
                fname = fname.substring(0, fname.lastIndexOf("."));
                defaultFileName = fname;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void intializedCropviews(View view) {
        cropImageViewLayout = view.findViewById(R.id.cropImageViewLayout);
        cropImageView = view.findViewById(R.id.cropImageView);

        cancel_txt = view.findViewById(R.id.cancel_txt);
        cancel_txt.setOnClickListener(this);
        crop_txt = view.findViewById(R.id.crop_txt);
        crop_txt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                this.dismiss();
                break;
            case R.id.image_paint:
                if (!colorButtonFlag) {
                    showColorButton();
                    photoEditor.setBrushDrawingMode(true);
                } else {
                    hideColorButton();
                }
                image_undo.setVisibility(View.VISIBLE);
                break;
            case R.id.image_save:
                //  AppUtility.hideSoftKeyboard(getActivity());
                String fileName = edt_file_name.getText().toString();
                // String des = edt_desc.getText().toString();
                if (!fileName.equals("")) {
                    defaultFileName = fileName;
                }
                saveImage(defaultFileName);
                break;
            case R.id.image_undo:
                photoEditor.undo();
                break;
            case R.id.image_red:
                photoEditor.setBrushColor(getResources().getColor(R.color.red_color));
                image_paint.setColorFilter(getResources().getColor(R.color.red_color));
                break;
            case R.id.image_green:
                photoEditor.setBrushColor(getResources().getColor(R.color.color_green));
                image_paint.setColorFilter(getResources().getColor(R.color.color_green));
                break;
            case R.id.image_blue:
                photoEditor.setBrushColor(getResources().getColor(R.color.color_blue));
                image_paint.setColorFilter(getResources().getColor(R.color.color_blue));
                break;
            case R.id.image_crop:
                getCropImageRatios();
                break;
            case R.id.cancel_txt:
                cropiExit();
                break;
            case R.id.crop_txt:
                cropImageView.getCroppedImageAsync(cropImageView.getCropShape(), 0, 0);
                break;
        }
    }

    private void cropiExit() {
        image_crop.setVisibility(View.VISIBLE);
        cropImageView.setVisibility(View.GONE);
        photo_editor_view.setVisibility(View.VISIBLE);
        cancel_txt.setVisibility(View.GONE);
        crop_txt.setVisibility(View.GONE);
        image_paint.setVisibility(View.VISIBLE);
        constraintLayout_editImage.setVisibility(View.VISIBLE);
    }


    private void hideEditImageView() {
        cropImageViewLayout.setVisibility(View.VISIBLE);
        cancel_txt.setVisibility(View.VISIBLE);
        crop_txt.setVisibility(View.VISIBLE);
        cropImageView.setVisibility(View.VISIBLE);
        image_crop.setVisibility(View.GONE);
        image_paint.setVisibility(View.GONE);
        photo_editor_view.setVisibility(View.GONE);
        constraintLayout_editImage.setVisibility(View.GONE);
    }

    private void showColorButton() {
        colorButtonFlag = true;
        image_red.setVisibility(View.VISIBLE);
        image_green.setVisibility(View.VISIBLE);
        image_blue.setVisibility(View.VISIBLE);
    }

    private void hideColorButton() {
        colorButtonFlag = false;
        image_red.setVisibility(View.GONE);
        image_green.setVisibility(View.GONE);
        image_blue.setVisibility(View.GONE);
    }

    // Saves the state upon rotating the screen/restarting the activity
    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    // Restores the state upon rotating the screen/restarting the activity
    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        if (bundle != null) {
            mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
            mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        try {
            InputMethodManager imm =
                    (InputMethodManager) edt_file_name.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            // if (imm.isActive())
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception ex) {
            Log.e("TAG :", ex.getMessage());
        }
        //  AppUtility.hideSoftKeyboard(getActivity());
        super.onDismiss(dialog);
    }

    /**
     * after image croping
     **/
    @Override
    public void onGetCroppedImageComplete(CropImageView view, final Bitmap bitmap, Exception error) {
        if (error == null) {
            imageBitmap = bitmap;
            getVisibleViews();
            //getBitmapToUri(bitmap);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    photo_editor_view.getSource().setImageBitmap(bitmap);

                }
            });
           /* try {
             //   String path = saveToInternalStorage(getActivity(), bitmap);
                getVisibleViews();
                //getBitmapToUri(bitmap);
                photo_editor_view.getSource().setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                cropiExit();
            }*/
        } else {
            cropiExit();
        }
    }

    private void getBitmapToUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Title", null);
        this.uri = Uri.parse(path);
    }

    private void getVisibleViews() {
        cropImageView.setVisibility(View.GONE);
        cropImageViewLayout.setVisibility(View.GONE);
        image_crop.setVisibility(View.VISIBLE);
        photo_editor_view.setVisibility(View.VISIBLE);
        constraintLayout_editImage.setVisibility(View.VISIBLE);
        image_paint.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            //Toast.makeText(mCropImageView.getContext(), "Image load successful", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(mCropImageView.getContext(), "Image load failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
            Toast.makeText(cropImageView.getContext(), "Unable to load image", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Save crop image
     **/
    private String saveToInternalStorage(Context context, Bitmap bitmapImage) throws IOException {
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "image.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            //Bitmap scaledBitmap = getCompressedBitmap(bitmapImage);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 70, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath();
    }


    public interface OnImageEdited {
        void onImageSaved(String path);
    }
}
