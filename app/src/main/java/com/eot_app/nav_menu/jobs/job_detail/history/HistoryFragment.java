package com.eot_app.nav_menu.jobs.job_detail.history;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.MyListItemSelected;
import com.eot_app.utility.util_interfaces.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment implements MyListItemSelected<History>, history_listview {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    history_pi history_pi;
    history_pc history_pc;
    RecyclerView historylist;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    View nodoc_linear;
    TextView nolist_txt;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        historylist = view.findViewById(R.id.recycleHistoryList);

        nodoc_linear = view.findViewById(R.id.elist);
        nolist_txt = view.findViewById(R.id.nolist_txt);
        history_pi = new history_pc(this);
        history_pi.getJobId(mParam2);

        layoutManager = new LinearLayoutManager(getActivity());
        historylist.setLayoutManager(layoutManager);
        List<History> hisdata = new ArrayList<>();

        adapter = new HistoryAdapter(hisdata, this);
        historylist.setAdapter(adapter);

        history_pi.getHistoryList();

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void setdata(List<History> data) {
        ((HistoryAdapter) adapter).updateRecords(data);
        if (adapter.getItemCount() <= 0) {
            nodoc_linear.setVisibility(View.VISIBLE);
            nolist_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.hint_empty));
        } else {
            nodoc_linear.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMyListitemSeleted(History history) {

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
    public void netwrkDialog() {
        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert)
                , LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_not_fount),
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        adapter.notifyDataSetChanged();
                        return null;
                    }
                });
    }
}
