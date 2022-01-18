package com.eot_app.nav_menu.jobs.job_detail.chat.img_crop_pkg;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.eot_app.R;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.UserToUserChatActivity;
import com.eot_app.nav_menu.client_chat_pkg.ClientChatFragment;
import com.eot_app.nav_menu.jobs.job_detail.chat.ChatFragment;
import com.eot_app.nav_menu.jobs.job_detail.customform.CustomFormFragment;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;


public class ImageCropFragment extends DialogFragment implements View.OnClickListener,
        CropImageView.OnGetCroppedImageCompleteListener, CropImageView.OnSetImageUriCompleteListener {
    public static final String FIXED_ASPECT_RATIO = "extra_fixed_aspect_ratio";
    public static final String EXTRA_ASPECT_RATIO_X = "extra_aspect_ratio_x";
    public static final String EXTRA_ASPECT_RATIO_Y = "extra_aspect_ratio_y";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 100;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private Uri imageUri;
    private ImageView back_arrow_img, image_crop, imageView, sendImageView;
    private CropImageView cropImageView;
    private boolean isFixedAspectRatio = false;
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;
    private TextView cancel_img_crop_txt, crop_img_txt;
    private RelativeLayout cropImageViewLayout;
    private ConstraintLayout sendImageViewLayout;
    private MyDialogInterface callbackListener;


    public ImageCropFragment() {
        // Required empty public constructor
    }


    public static ImageCropFragment newInstance(String param1, String param2) {
        ImageCropFragment fragment = new ImageCropFragment();
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
            imageUri = Uri.parse(getArguments().getString(ARG_PARAM2));
        }
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_crop, container, false);
        MyViewinitialize(view);
        return view;
    }

    private void MyViewinitialize(View view) {
        back_arrow_img = view.findViewById(R.id.back_arrow_img);
        back_arrow_img.setOnClickListener(this);

        image_crop = view.findViewById(R.id.image_crop);
        image_crop.setOnClickListener(this);

        cropImageView = view.findViewById(R.id.cropImageView);

        imageView = view.findViewById(R.id.imageView);
        imageView.setImageURI(imageUri);

        cancel_img_crop_txt = view.findViewById(R.id.cancel_img_crop_txt);
        cancel_img_crop_txt.setOnClickListener(this);
        crop_img_txt = view.findViewById(R.id.crop_img_txt);
        crop_img_txt.setOnClickListener(this);

        cropImageViewLayout = view.findViewById(R.id.cropImageViewLayout);

        sendImageViewLayout = view.findViewById(R.id.sendImageViewLayout);

        sendImageView = view.findViewById(R.id.sendImageView);
        sendImageView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_arrow_img:
                this.dismiss();
                break;
            case R.id.image_crop:
                getCropImageRatios();
                break;
            case R.id.cancel_img_crop_txt:
                cropiExit();
                break;
            case R.id.crop_img_txt:
                cropImageView.getCroppedImageAsync(cropImageView.getCropShape(), 0, 0);
                break;
            case R.id.sendImageView:
                if (getTag().equals("ClientChatFragment")) {
                    ClientChatFragment clientChat = (ClientChatFragment) getTargetFragment();
                    clientChat.callServiceForImage(imageUri);
                } else if (getTag().equals("ChatFragment")) {
                    ChatFragment chatFragment = (ChatFragment) getTargetFragment();
                    chatFragment.callServiceForImage(imageUri);
                } else if (getTag().equals("UserToUserChatActivity")) {
                    callbackListener.onClickContinuarEvent(imageUri);
                } else if (getTag().equals("AddExpenseActivity")) {
                    callbackListener.onClickContinuarEvent(imageUri);
                } else if (getTag().equals("AddJobEquipMentActivity")) {
                    callbackListener.onClickContinuarEvent(imageUri);
                } else if (getTag().equals("FormQueAns_Activity")) {
                    callbackListener.onClickContinuarEvent(imageUri);
                } else if (getTag().equals("CustomFormFragment")) {
                    CustomFormFragment chatFragment = (CustomFormFragment) getTargetFragment();
                    chatFragment.callServiceForImage(imageUri);
                }
                this.dismiss();
                break;
        }
    }


    private void cropiExit() {
        image_crop.setVisibility(View.VISIBLE);
        sendImageViewLayout.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        cropImageViewLayout.setVisibility(View.GONE);
    }

    private void getCropImageRatios() {
        isFixedAspectRatio = getActivity().getIntent().getBooleanExtra(FIXED_ASPECT_RATIO, false);
        mAspectRatioX = getActivity().getIntent().getIntExtra(EXTRA_ASPECT_RATIO_X, DEFAULT_ASPECT_RATIO_VALUES);
        mAspectRatioY = getActivity().getIntent().getIntExtra(EXTRA_ASPECT_RATIO_Y, DEFAULT_ASPECT_RATIO_VALUES);

        cropImageView.setFixedAspectRatio(isFixedAspectRatio);

        hideViewfromCroping();

        /**load bitmap from uri */
        cropImageView.setImageUriAsync(imageUri);
    }

    private void hideViewfromCroping() {
        imageView.setVisibility(View.GONE);
        sendImageViewLayout.setVisibility(View.GONE);
        image_crop.setVisibility(View.GONE);
        cropImageViewLayout.setVisibility(View.VISIBLE);
    }

    /***after call this image croping complete**/
    @Override
    public void onGetCroppedImageComplete(CropImageView view, Bitmap bitmap, Exception error) {
        if (error == null) {
            getBitmapToUri(bitmap);
            getViewsAfterCroping(bitmap);
        } else {
            cropiExit();
        }
    }


    private void getBitmapToUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Title", null);
        this.imageUri = Uri.parse(path);
    }

    private void getViewsAfterCroping(Bitmap bitmap) {
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(bitmap);
        cropImageViewLayout.setVisibility(View.GONE);
        image_crop.setVisibility(View.VISIBLE);
        sendImageViewLayout.setVisibility(View.VISIBLE);
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

    @Override
    public void onPause() {
        super.onPause();
        Log.e("", "");
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            imageUri = uri;
        } else {
            Toast.makeText(cropImageView.getContext(), "Unable to load image", Toast.LENGTH_LONG).show();
        }
    }

    public void setInstance(UserToUserChatActivity runnable) {
        this.callbackListener = runnable;
    }


    public void setCallbackListener(MyDialogInterface callbackListener) {
        this.callbackListener = callbackListener;
    }

    public interface MyDialogInterface {
        void onClickContinuarEvent(Uri permisoRequerido);
    }
}
