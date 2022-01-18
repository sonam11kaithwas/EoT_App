package com.eot_app.nav_menu.client_chat_pkg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eot_app.R;
import com.eot_app.UploadDocumentFragment;
import com.eot_app.nav_menu.client_chat_pkg.client_chat_model.ClientChatReqModel;
import com.eot_app.nav_menu.client_chat_pkg.client_chat_mvp.ClientChat_PC;
import com.eot_app.nav_menu.client_chat_pkg.client_chat_mvp.ClientChat_PI;
import com.eot_app.nav_menu.client_chat_pkg.client_chat_mvp.ClientChat_View;
import com.eot_app.nav_menu.jobs.job_controller.ChatController;
import com.eot_app.nav_menu.jobs.job_controller.ChatListnersContainer;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.chat.fire_Base_Model.Chat_Send_Msg_Model;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClientChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClientChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientChatFragment extends UploadDocumentFragment implements View.OnClickListener
        , ClientChat_View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static String CHATFRAGMENTTAG = "ClientChatFragment";
    public static String CLIENTPATH;
    private final String cltId = "";
    private RecyclerView recycler_client_chats;
    private LinearLayoutManager mLayoutManager;
    private ClientChatAdpter clientChatAdpter;
    private EditText msg_send_edt;
    private ImageView file_upload_Img, imag_upload_Img, send_msg_Img;
    private ClientChat_PI clientChatPi;
    private Dialog enterFieldDialog;
    private LinearLayout expanded_image_bg;
    private ImageView expandedImageView;
    private ImageView chat_im_zoom;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Job jobData;
    private OnFragmentInteractionListener mListener;
    private String captureImagePath;

    public ClientChatFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientChatFragment newInstance(String param1, String param2) {
        ClientChatFragment fragment = new ClientChatFragment();
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
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.client_fw_chat));
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_client_chat, container, false);
        initializeView(view);
        loadInitialiMsgs();


        //Get every message callback for textmessages
        FirebaseFirestore.getInstance()
                .collection(CLIENTPATH)     //orderBy("time").
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        final ArrayList<Chat_Send_Msg_Model> reciv_Chat_List2 = new ArrayList<>();
                        try {
                            if (queryDocumentSnapshots != null) {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    Chat_Send_Msg_Model fireBase_req_model2 = document.toObject(Chat_Send_Msg_Model.class);
                                    reciv_Chat_List2.add(fireBase_req_model2);
                                }
                                if (reciv_Chat_List2.size() == 0) {
                                    //emptyListSet();
                                } else {
                                    clientChatAdpter.notifyDataSetChanged();
                                }
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                });

        return view;
    }

    private void initializeView(View view) {
        jobData = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(mParam2);
        /**get chat path**/

        CLIENTPATH = ChatController.getInstance().getClientChatPath(jobData.getJobId());

        Log.e("Path:-------------", "" + CLIENTPATH);
        recycler_client_chats = view.findViewById(R.id.recycler_client_chats);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recycler_client_chats.setLayoutManager(mLayoutManager);


        msg_send_edt = view.findViewById(R.id.msg_send_edt);
        msg_send_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.chat_msg_hint));
        file_upload_Img = view.findViewById(R.id.file_upload_Img);
        imag_upload_Img = view.findViewById(R.id.imag_upload_Img);
        send_msg_Img = view.findViewById(R.id.send_msg_Img);
        file_upload_Img.setOnClickListener(this);
        imag_upload_Img.setOnClickListener(this);
        send_msg_Img.setOnClickListener(this);

        clientChatPi = new ClientChat_PC(this, jobData.getCltId(), jobData.getConId());
    }

    @Override
    public void onDocumentSelected(String path, boolean isImage) {
        if (jobData != null && clientChatPi != null) {
            Uri myUri = Uri.fromFile(new File(path));
            clientChatPi.uploadActualImageOnFireStore(myUri, jobData);
        }
    }

    @Override
    public void onClickContinuarEvent(Uri permisoRequerido) {
        if (jobData != null && clientChatPi != null)
            clientChatPi.uploadActualImageOnFireStore(permisoRequerido, jobData);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ChatController.getInstance().setClientChatState(2, "");
    }

    @Override
    public void onResume() {
        super.onResume();
        ChatController.getInstance().setClientChatState(2, jobData.getJobId());
        ChatListnersContainer.getChatListnerInstance().getCltRemove(jobData.getLabel(), jobData.getJobId());
    }

    @Override
    public void onPause() {
        super.onPause();
//        ChatController.getInstance().setClientChatState(2, "");
    }


    private void loadInitialiMsgs() {
        Query query = FirebaseFirestore.getInstance().collection(CLIENTPATH).orderBy("time");
        FirestoreRecyclerOptions<ClientChatReqModel> response =
                new FirestoreRecyclerOptions.Builder<ClientChatReqModel>()
                        .setQuery(query, ClientChatReqModel.class)
                        .build();

        clientChatAdpter = new ClientChatAdpter(response, this);

        clientChatAdpter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recycler_client_chats.scrollToPosition(recycler_client_chats.getAdapter().getItemCount() - 1);
            }
        });
        clientChatAdpter.notifyDataSetChanged();
        recycler_client_chats.setAdapter(clientChatAdpter);
    }

    @Override
    public void openImage(View thumbView, Drawable bmp, String img_url) {
        //  zoomImageFromThumb(thumbView, bmp);
        getChatImageZoomDialog(thumbView, bmp, img_url);
    }

    private void getChatImageZoomDialog(View thumbView, Drawable bmp, String img_url) {

        enterFieldDialog = new Dialog(getActivity());
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
        view.findViewById(R.id.client_container)
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.file_upload_Img:
                getDocumentsFromGallery();
                break;
            case R.id.imag_upload_Img:
                selectFile(true);
                break;
            case R.id.send_msg_Img:
                if (msg_send_edt.getText().toString().trim().length() > 0) {
                    ClientChatReqModel chatReqModel = new ClientChatReqModel(msg_send_edt.getText().toString().trim(),
                            "", jobData.getLabel(), jobData.getJobId(), "1");
                    msg_send_edt.setText("");
                    clientChatPi.sendCLientFwMessage(chatReqModel);
                    break;
                }
        }
    }

    /***
     * call from image crop fragment **/
    public void callServiceForImage(Uri newUri) {
        clientChatPi.uploadActualImageOnFireStore(newUri, jobData);
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
    public void onStart() {
        super.onStart();
        if (clientChatAdpter != null)
            clientChatAdpter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (clientChatAdpter != null)
            clientChatAdpter.stopListening();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void openUriOnBroWser(String serverUri, String type) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse(serverUri), type);
        try {
            startActivity(i);
        } catch (Exception e) {
            Intent openAnyType = new Intent(Intent.ACTION_VIEW);
            openAnyType.setData(Uri.parse(serverUri));
            startActivity(openAnyType);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
