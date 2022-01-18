package com.eot_app.nav_menu.jobs.job_detail.feedback;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.feedback.feedback_mvp.FeedBack_View;
import com.eot_app.nav_menu.jobs.job_detail.feedback.feedback_mvp.Feedback_pc;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.SignatureView;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.OnFragmentInteractionListener;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedbackFragment extends Fragment implements FeedBack_View, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String tempDir;
    public String current = null;
    ImageView im_smile, im_mute, im_sad;
    LinearLayout mContent;
    SignatureView mSignature;
    View mView;
    File mypath;
    TextView mClear, textView5, textView2, txt_des, textView4, clear_txt;
    Button mGetSign;
    String getPath;
    EditText desNotes;
    String DesNotes;
    int setSmilie = 3;
    FrameLayout frameNEw;
    ConstraintLayout rel1;
    Feedback_pc feedback_pc;
    View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //private OnFragmentInteractionListener mListener;

    public FeedbackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedbackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSessionExpire(String msg) {
        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }

    @Override
    public void onfeedbackSendSuccessfully() {
        setSmilie = 3;
        im_smile.setSelected(true);
        im_mute.setSelected(false);
        im_sad.setSelected(false);
        mSignature.clear();
        desNotes.setText("");

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feedback2, container, false);
        initializelables();
        feedback_pc = new Feedback_pc(this);
        AppUtility.setupUI(rel1, getActivity());
        tempDir = Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.external_dir) + "/";
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        File directory = cw.getDir(getResources().getString(R.string.external_dir), Context.MODE_PRIVATE);
        current = "eot_" + AppUtility.getDateByMiliseconds() + ".png";
        mypath = new File(directory, current);
        getPath = mypath.getAbsolutePath();
        mSignature = new SignatureView(getActivity());
        // mSignature.setBackgroundColor(Color.WHITE);
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mView = mContent;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        im_smile.setOnClickListener(this);
        im_sad.setOnClickListener(this);
        im_mute.setOnClickListener(this);
        im_mute.setOnClickListener(this);


        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Cleared");
                mSignature.clear();
                //mGetSign.setEnabled(false);
            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetSign.setEnabled(false);
                AppUtility.hideSoftKeyboard(getActivity());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        DesNotes = desNotes.getText().toString();
                        Log.v("log_tag", "sign Saved");
                        Log.e("CANVAS", mSignature.isSignatureEmpty() + "");
                        File mfile = mSignature.exportFile(getPath, current);
                        if (mSignature.isSignatureEmpty()) {
                            mfile = null;
                        }
                        feedback_pc.UploadSignToServer(mfile, DesNotes, String.valueOf(setSmilie), mParam2);
                        mGetSign.setEnabled(true);
                    }
                }, 300);


            }
        });

        return view;
    }

    private void initializelables() {
        rel1 = view.findViewById(R.id.rel1);
        textView5 = view.findViewById(R.id.textView5);
        textView5.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.feed_head));

        textView2 = view.findViewById(R.id.textView2);
        textView2.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.feed_sub_head));

        txt_des = view.findViewById(R.id.txt_des);
        txt_des.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.description));

        textView4 = view.findViewById(R.id.textView4);
        textView4.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.sign));

        clear_txt = view.findViewById(R.id.clear_txt);
        clear_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.clear));

        desNotes = view.findViewById(R.id.editText);
        frameNEw = view.findViewById(R.id.frameNEw);
        mContent = view.findViewById(R.id.signt);
        mClear = view.findViewById(R.id.clear_txt);
        mGetSign = view.findViewById(R.id.button);
        mGetSign.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.submit_btn));
        im_mute = view.findViewById(R.id.im_mute);
        im_sad = view.findViewById(R.id.im_sad);
        im_smile = view.findViewById(R.id.im_smile);
        im_smile.setSelected(true);
    }

    //
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        Log.e("", "");
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //  mListener = null;
    }

    @Override
    public void onDestroy() {
        Log.e("", "");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.e("", "");
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_smile:
                setSmilie = 3;
                im_smile.setSelected(true);
                im_mute.setSelected(false);
                im_sad.setSelected(false);
                break;
            case R.id.im_mute:
                setSmilie = 2;
                im_smile.setSelected(false);
                im_mute.setSelected(true);
                im_sad.setSelected(false);
                break;
            case R.id.im_sad:
                setSmilie = 1;
                im_smile.setSelected(false);
                im_mute.setSelected(false);
                im_sad.setSelected(true);
                break;
        }
    }
}
