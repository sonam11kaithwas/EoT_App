package com.eot_app.nav_menu.client.clientlist.client_detail.contact;

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
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.clientcontact_mvp.Contact_View;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.clientcontact_mvp.Contact_pc;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.clientcontact_mvp.Contact_pi;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.edit_contact.editmodel.Edit_Add_Contact_Activity;
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
 * Use the {@link Client_Contact_List#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Client_Contact_List extends Fragment implements MyListItemSelected<ContactData>, Contact_View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final int EDITCONTACT = 22, ADDCONTACT = 21;
    private static final String ARG_PARAM1 = "param1";
    RecyclerView contactlist;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    Contact_pi contactpi;
    EditText edtSearch;
    String query;
    ImageView imvCross;
    SwipeRefreshLayout swiperefresh;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private OnFragmentInteractionListener mListener;

    public Client_Contact_List() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //@param param1 Parameter 1.
     *
     * @param data Parameter 2.
     * @return A new instance of fragment Client_Contact_List.
     */
    // TODO: Rename and change types and number of parameters
    public static Client_Contact_List newInstance(String data) {
        Client_Contact_List fragment = new Client_Contact_List();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_contact, container, false);
        initializelables(view);
        initializeview(view);
        return view;
    }

    public void getUpdateConbtact() {
        if (contactpi != null) {
            contactpi.contactResponce();
        }
    }

    private void initializelables(View view) {
        edtSearch = view.findViewById(R.id.edtSearch);
        edtSearch.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.search));
    }

    @Override
    public void disableSwiprefresh() {
        if (swiperefresh.isRefreshing()) {
            swiperefresh.setRefreshing(false);
        }
    }

    private void initializeview(View view) {
        imvCross = view.findViewById(R.id.imvCross);
        contactlist = view.findViewById(R.id.contactlist);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        layoutManager = new LinearLayoutManager(getActivity());
        contactlist.setLayoutManager(layoutManager);
        final List<ContactData> contactData = new ArrayList<>();
        adapter = new Adapter_Contactlist(this, contactData);
        contactlist.setAdapter(adapter);
        contactpi = new Contact_pc(this, mParam1);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiperefresh.setRefreshing(true);
                contactpi.contactDetails();
            }
        });
        contactpi.contactDetails();


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
                contactpi.getcontactList(searchText, mParam1);
            }
        });
        imvCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                contactpi.contactDetails();

            }
        });

        hideKeyBoardOnListTouch();

    }


    private void hideKeyBoardOnListTouch() {
        contactlist.setOnTouchListener(new View.OnTouchListener() {
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
    public void onMyListitemSeleted(ContactData contactData) {
        Intent in = new Intent(getActivity(), Edit_Add_Contact_Activity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        in.putExtra("contactEdit", contactData);
        //startActivity(in);
        startActivityForResult(in, EDITCONTACT);
    }

    @Override
    public void setData(List<ContactData> contactData) {
        ((Adapter_Contactlist) adapter).updateRecords(contactData);
        disableSwiprefresh();
    }

    @Override
    public void contactUpdate(int result, String conId) {
        contactpi.getContactListFromDB();
    }

    @Override
    public void setSearchData(List<ContactData> searchData) {
        ((Adapter_Contactlist) adapter).searchData(searchData);
    }

    @Override
    public void updateFromObserver() {
        contactpi.getContactListFromDB();
    }

}
