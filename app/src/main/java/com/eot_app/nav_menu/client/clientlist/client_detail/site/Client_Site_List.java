package com.eot_app.nav_menu.client.clientlist.client_detail.site;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_View;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_pc;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_pi;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.EditSiteActivity;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.MyListItemSelected;
import com.eot_app.utility.util_interfaces.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Client_Site_List#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Client_Site_List extends Fragment implements MyListItemSelected<Site_model>, Site_View {
    // TODO: Rename parameter arguments, choose names that match

    public static final int SITE_ADD = 31;
    public static final int SITE_EDIT = 32;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView sitelist;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    Site_pi site_pi;
    ImageView imvCross;
    String query;
    EditText edtSearch;
    SwipeRefreshLayout swiperefresh;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public Client_Site_List() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param key    Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Client_Site_List.
     */
    // TODO: Rename and change types and number of parameters
    public static Client_Site_List newInstance(String key, String param2) {
        Client_Site_List fragment = new Client_Site_List();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, key);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_site, container, false);
        initializelables(view);
        initializeview(view);
        return view;
    }

    private void initializelables(View view) {
        edtSearch = view.findViewById(R.id.edtSearch);
        edtSearch.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.search));
    }

    private void initializeview(View view) {
        sitelist = view.findViewById(R.id.sitelist);

        layoutManager = new LinearLayoutManager(getActivity());
        sitelist.setLayoutManager(layoutManager);
        List<Site_model> sitedata = new ArrayList<>();
        adapter = new Adapter_Sitelist(this, sitedata);
        sitelist.setAdapter(adapter);
        site_pi = new Site_pc(this, mParam1);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiperefresh.setRefreshing(true);
                site_pi.siteResponce();
            }
        });
        site_pi.GetSiteDetails();
        imvCross = view.findViewById(R.id.imvCross);
        edtSearch = view.findViewById(R.id.edtSearch);


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
            }

            @Override
            public void afterTextChanged(Editable s) {
                query = edtSearch.getText().toString();
                String searchText = "%" + query + "%";
                site_pi.getsiteList(searchText, mParam1);
            }
        });

        imvCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                site_pi.GetSiteDetails();

            }
        });

        hideKeyBoardOnListTouch();

    }

    @Override
    public void disableSwiprefresh() {
        if (swiperefresh != null && swiperefresh.isRefreshing()) {
            swiperefresh.setRefreshing(false);
        }
    }

    private void hideKeyBoardOnListTouch() {
        sitelist.setOnTouchListener(new View.OnTouchListener() {
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
    public void onMyListitemSeleted(Site_model site_model) {
        Intent in = new Intent(getActivity(), EditSiteActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        in.putExtra("siteEdit", site_model);
        startActivityForResult(in, SITE_EDIT);
    }

    @Override
    public void setSiteData(List<Site_model> siteLists) {
        refreshing();
        ((Adapter_Sitelist) adapter).updateSiteRecord(siteLists);
        disableSwiprefresh();
    }

    private void refreshing() {
        if (swiperefresh.isRefreshing())
            swiperefresh.setRefreshing(false);
    }

    @Override
    public void updateAdapter(int check, String siteId) {
//        ((Adapter_Sitelist) adapter).refreshAdapter(check, siteId);
        site_pi.GetSiteDetailsFromDB();
    }

    @Override
    public void setSearchData(List<Site_model> searchData) {
        ((Adapter_Sitelist) adapter).searchData(searchData);
    }

    @Override
    public void updateFromObserver() {
        site_pi.GetSiteDetailsFromDB();
    }
}
