package com.eot_app.nav_menu.admin_fw_chat_pkg.chat_pkg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eot_app.R;
import com.eot_app.nav_menu.admin_fw_chat_pkg.chat_pkg.chat_mvp.SingleChat_PC;
import com.eot_app.nav_menu.admin_fw_chat_pkg.chat_pkg.chat_mvp.SingleChat_PI;
import com.eot_app.nav_menu.admin_fw_chat_pkg.chat_pkg.chat_mvp.SingleChat_View;
import com.eot_app.nav_menu.admin_fw_chat_pkg.chat_pkg.model_pkg.SingleChatModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.chat_single_pkg.AdminChatController;
import com.eot_app.nav_menu.admin_fw_chat_pkg.chatmember_list_pkg.chatmember_model.ChatUserListResModel;
import com.eot_app.nav_menu.jobs.job_detail.chat.img_crop_pkg.ImageCropFragment;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity implements
        SingleChat_View, View.OnClickListener {
    private final int CAMERA_CODE = 100;
    private final int GALLERY_CODE = 101;
    private final int PDF_CODE = 102;
    public RecyclerView recycler_chats;
    // private String userId;
    ChatUserListResModel chatMember;
    private String PATH = "";
    private String captureImagePath;
    // TODO: Rename and change types of parameters
    private TextView nolist_txt;
    private EditText msg_send_edt;
    private ImageView imag_upload, send_msg, file_upload;
    private ChatUserListAdpter myChatAdapter;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private LinearLayout expanded_image_bg;
    private ImageView expandedImageView;
    private LinearLayoutManager mLayoutManager;
    private com.eot_app.utility.util_interfaces.OnFragmentInteractionListener mListener;
    private Dialog enterFieldDialog;
    // private String captureImagePath;
    private ImageView chat_im_zoom;
    private SingleChat_PI singleChatPi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (getIntent().hasExtra("chatMember")) {
                String convertObject = bundle.getString("chatMember");
                chatMember = new Gson().fromJson(convertObject, ChatUserListResModel.class);
            }

        }
        String usrStatus;
        if (chatMember.getUserStatus().equals("0")) {
            usrStatus = "Offline";
        } else {
            usrStatus = "Online";
        }
        setTitle(chatMember.getFnm() + " " + chatMember.getLnm() + "   " + usrStatus);//+ "\n" + "\nOnline"
        initializeView();

        loadMessagesList();


        //Get every message callback for textmessages
        FirebaseFirestore.getInstance()
                .collection(PATH).     //orderBy("time").
                addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                final ArrayList<SingleChatModel> reciv_Chat_List2 = new ArrayList<>();
                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Log.d("TAG", document.getId() + " => " + document.getData());
                        SingleChatModel fireBase_req_model2 = document.toObject(SingleChatModel.class);
                        reciv_Chat_List2.add(fireBase_req_model2);
                    }
                    if (reciv_Chat_List2.size() == 0) {

                    } else {
                        myChatAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void loadMessagesList() {
        Query query = FirebaseFirestore.getInstance().collection(PATH).orderBy("time");
        FirestoreRecyclerOptions<SingleChatModel> response =
                new FirestoreRecyclerOptions.Builder<SingleChatModel>()
                        .setQuery(query, SingleChatModel.class)
                        .build();

        myChatAdapter = new ChatUserListAdpter(response, this);

        myChatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recycler_chats.scrollToPosition(recycler_chats.getAdapter().getItemCount() - 1);
            }
        });
        //  myChatAdapter.startListening();
        myChatAdapter.notifyDataSetChanged();
        recycler_chats.setAdapter(myChatAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        myChatAdapter.stopListening();
    }

    private void initializeView() {

        /**get chat path**/
        PATH = AdminChatController.getAdminChatInstance().getAdminGrpChatPath(chatMember.getUsrId());
        Log.e("Path:-------------", "" + PATH);
        singleChatPi = new SingleChat_PC(this, PATH, chatMember.getUsrId());

        recycler_chats = findViewById(R.id.recycler_client_chats);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        recycler_chats.setLayoutManager(mLayoutManager);

        recycler_chats.setItemAnimator(new DefaultItemAnimator());
        recycler_chats.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtility.hideSoftKeyboard(ChatActivity.this);
                return false;
            }
        });
        imag_upload = findViewById(R.id.imag_upload_Img);
        imag_upload.setOnClickListener(this);

        msg_send_edt = findViewById(R.id.msg_send_edt);
        msg_send_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.chat_msg_hint));
        send_msg = findViewById(R.id.send_msg_Img);

        send_msg.setOnClickListener(this);

        nolist_txt = findViewById(R.id.nolist_txt);

        file_upload = findViewById(R.id.file_upload_Img);
        file_upload.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        myChatAdapter.startListening();
    }

    @Override
    public void openImage(View thumbView, Drawable bmp, String img_url) {
        //  zoomImageFromThumb(thumbView, bmp);
        getChatImageZoomDialog(thumbView, bmp, img_url);
    }

    /***
     * call from image crop fragment **/
    public void callServiceForImage(Uri newUri) {
        //fireBase_pi.uploadActualImageOnFireStore(newUri, jobData);
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
            case GALLERY_CODE:
                if (data == null) {
                    return;
                } else {
                    Uri uri = data.getData();
                    imageCroping(uri);
                    singleChatPi.uploadActualImageOnFireStore(uri);
                }
                break;
            case PDF_CODE:
                if (data != null) {
                    Uri uri = data.getData();
                    singleChatPi.uploadActualImageOnFireStore(uri);
                } else {
                    return;
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
//    }

    private void imageCroping(final Uri uri) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageCropFragment myfragment = ImageCropFragment.newInstance("Uri", uri.toString());
                myfragment.show(getSupportFragmentManager().beginTransaction(), "ChatActivity");
            }
        }, 1);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState, outPersistentState);
    }


    public void openUriOnBroWser(String serverUri, String type) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse(serverUri), type);
        //i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        try {
            startActivity(i);
        } catch (Exception e) {
            Intent openAnyType = new Intent(Intent.ACTION_VIEW);
            openAnyType.setData(Uri.parse(serverUri));
            startActivity(openAnyType);
//            EotApp.getAppinstance().showToastmsg("can't open this type of file!");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imag_upload_Img:
                sendImageForChat();
                break;

            case R.id.send_msg_Img:
                if (msg_send_edt.getText().toString().trim().length() > 0) {
                    SingleChatModel chatModel = new SingleChatModel(msg_send_edt.getText().toString().trim()
                            , "", AppUtility.getDateByMiliseconds(), "1");
                    msg_send_edt.setText("");
                    singleChatPi.sendChatMessages(chatModel);
                }
                break;

            case R.id.file_upload_Img:
                if (AppUtility.askStoragePerMission(this)) {
                    uploadPDF_File();
                }
                break;
        }
    }

    private void uploadPDF_File() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(
                Intent.createChooser(intent, "Select a File to Upload"),
                PDF_CODE);
    }

    private void sendImageForChat() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.bottom_image_chooser);
        TextView camera = dialog.findViewById(R.id.camera);
        camera.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.camera));
        TextView gallery = dialog.findViewById(R.id.gallery);
        gallery.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.gallery));
        LinearLayout driveLayout = dialog.findViewById(R.id.driveLayout);
        driveLayout.setVisibility(View.GONE);

        camera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (AppUtility.askCameraTakePicture(ChatActivity.this)) {
                    takePictureFromCamera();
                }
                dialog.dismiss();
            }
        });


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtility.askGalaryTakeImagePermiSsion(ChatActivity.this)) {
                    takeimageFromGalary();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void takeimageFromGalary() {
        Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT, null);
        galleryintent.putExtra("key", "gallery");
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent, GALLERY_CODE);
    }

    private void takePictureFromCamera() {
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);// new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("", ex.getMessage());

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.eot_app.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                try {
                    startActivityForResult(takePictureIntent, CAMERA_CODE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        Calendar calendar = Calendar.getInstance();
        long imageFileName = calendar.getTime().getTime();

        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);// return path

        File directoryPath = new File(storageDir.getPath());
        File image = File.createTempFile(
                String.valueOf(imageFileName),  /* prefix */
                ".jpg",         /* suffix */
                directoryPath   /* directory */
        );
        captureImagePath = image.getAbsolutePath();
        return new File(image.getPath());
    }

    private void getChatImageZoomDialog(View thumbView, Drawable bmp, String img_url) {

        enterFieldDialog = new Dialog(this);
        enterFieldDialog.setCancelable(false);
        enterFieldDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        enterFieldDialog.setContentView(R.layout.chat_image_zoom_out_in_layout);

        Window window = enterFieldDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);

        expanded_image_bg = enterFieldDialog.findViewById(R.id.expanded_image_bg);
        expandedImageView = enterFieldDialog.findViewById(R.id.expanded_image);
        chat_im_zoom = enterFieldDialog.findViewById(R.id.chat_im_zoom);
        enterFieldDialog.show();
        zoomImageFromThumb(thumbView, bmp, img_url);

    }

    private void zoomImageFromThumb(final View thumbView, Drawable imageResId, String img_url) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.

        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.



      /*  expandedImageView = (ImageView) view.findViewById(
                R.id.expanded_image);
        expanded_image_bg = view.findViewById(R.id.expanded_image_bg);*/
//        expandedImageView.setImageBitmap(imageResId);

        Glide.with(this).load(img_url)
                .placeholder(imageResId)
                .override(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//220
                .into(expandedImageView);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expanded_image_bg.setVisibility(View.VISIBLE);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                    //enterFieldDialog.dismiss();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expanded_image_bg.setVisibility(View.GONE);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                        enterFieldDialog.dismiss();//dialog close
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expanded_image_bg.setVisibility(View.GONE);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });


        chat_im_zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thumbView.setAlpha(1f);
                expanded_image_bg.setVisibility(View.GONE);
                expandedImageView.setVisibility(View.GONE);
                mCurrentAnimator = null;
                enterFieldDialog.dismiss();
            }
        });


    }
}
