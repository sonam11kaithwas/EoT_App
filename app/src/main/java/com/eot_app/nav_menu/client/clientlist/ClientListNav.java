package com.eot_app.nav_menu.client.clientlist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.ClientDetail;
import com.eot_app.nav_menu.client.clientlist.client_presenter.ClientList_View;
import com.eot_app.nav_menu.client.clientlist.client_presenter.Client_pc;
import com.eot_app.nav_menu.client.clientlist.client_presenter.Client_pi;
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
 * Use the {@link ClientListNav#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientListNav extends Fragment implements MyListItemSelected<Client>, ClientList_View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView clientlist;
    RecyclerView.LayoutManager layoutManager;
    Adapter_clientlist adapter;
    Client_pi client_pi;
    EditText edtSearch;
    ImageView imvCross;
    SwipeRefreshLayout swiperefresh;
    String query;
    View em_layout;
    TextView nolist_txt;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ClientListNav() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientListNav.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientListNav newInstance(String param1, String param2) {
        ClientListNav fragment = new ClientListNav();
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
//        activity_client_list
        View view = inflater.inflate(R.layout.activity_client_list, container, false);

        initializelables(view);
        initializeview(view);
        return view;
    }

    private void initializelables(View view) {
        edtSearch = view.findViewById(R.id.edtSearch);
        edtSearch.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.search));
    }


    private void initializeview(View view) {
        Log.e("TAG : ", "initialize client");
        em_layout = view.findViewById(R.id.em_layout);
        nolist_txt = view.findViewById(R.id.nolist_txt);
        imvCross = view.findViewById(R.id.imvCross);

        clientlist = view.findViewById(R.id.clientlist);
        layoutManager = new LinearLayoutManager(getActivity());

        clientlist.setLayoutManager(layoutManager);


        final List<Client> data = new ArrayList<>();
        swiperefresh = view.findViewById(R.id.swiperefresh);
        adapter = new Adapter_clientlist(ClientListNav.this, data);
        clientlist.setAdapter(adapter);


        client_pi = new Client_pc(this);
        /** Featch Client list From DB*/
        client_pi.getClientList();


        /**  use this method because we update every time client and inside it.  */
        client_pi.getSIteList();
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
/**                 for update get info of client,site & contact update.  */
                client_pi.getSIteList();
            }
        });


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtSearch.getText().toString().equals("")) {
                    client_pi.getClientList();
                }
//                adapter.getFilter().filter(s.toString().toLowerCase());
                query = edtSearch.getText().toString();
                if (query.length() > 0) {
                    if (query.length() >= 1) {
                        imvCross.setVisibility(View.VISIBLE);
                    } else {
                        imvCross.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                query = edtSearch.getText().toString();
                String searchText = "%" + query + "%";
                if (query.trim().length() > 0)
                    client_pi.getclientListfromDB(searchText);
            }
        });
        imvCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                client_pi.getClientList();
            }
        });


        hideKeyBoardOnListTouch();
    }

    private void hideKeyBoardOnListTouch() {
        clientlist.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtility.hideSoftKeyboard(getActivity());
                return false;
            }
        });

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
    public void setdata(List<Client> data) {
        adapter.updateRecords(data);
        disableSwiprefresh();
        if (adapter.getItemCount() <= 0) {
            em_layout.setVisibility(View.VISIBLE);
            nolist_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.empty_client_list));
        } else {
            em_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void disableSwiprefresh() {
        if (swiperefresh.isRefreshing()) {
            swiperefresh.setRefreshing(false);
        }
    }

    @Override
    public void onMyListitemSeleted(Client client) {
        Intent in = new Intent(getActivity(), ClientDetail.class);
        in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        in.putExtra("data", client);
        System.out.println(client);
        startActivity(in);
    }

    @Override
    public void refreshList(String cltId) {
        client_pi.getSIteList();
    }

    @Override
    public void setSearchData(List<Client> searchData) {
        adapter.searchData(searchData);
    }

    @Override
    public void updateFromApiObserver() {
        if (client_pi != null) {
            client_pi.getClientList();
            client_pi.getSIteList();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
    public void onResume() {
        super.onResume();
        edtSearch.setText("");
        client_pi.getClientList();
    }
}

