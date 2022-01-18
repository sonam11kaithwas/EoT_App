package com.eot_app.nav_menu.client.clientlist.client_detail.client_overview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.eot_app.R;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.client_overview.overview_mvp.Client_Overview_view;
import com.eot_app.nav_menu.client.clientlist.client_detail.client_overview.overview_mvp.OverView_pi;
import com.eot_app.nav_menu.client.clientlist.client_detail.client_overview.overview_mvp.Overview_pc;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.nav_menu.jobs.job_detail.detail.DetailFragment;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.client_refrence_db.ClientRefrenceModel;
import com.eot_app.utility.util_interfaces.OnFragmentInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Client_Overview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Client_Overview extends Fragment implements Client_Overview_view, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView cnm, cadr, mob1, mob2, email, accouttype, gst_txt, tin_txt, indus_txt, note_txt, latitude, longitude;
    StringBuffer sb;
    //Button getlatlong;
    private TextView client_txt, adderes_client, clint_details, account_dtls, ac_type, gst_no, tin_no, industry, lbl_notes, txt_refrence, txt_refrence_lable;
    // TODO: Rename and change types of parameters
    private Client client;
    private OverView_pi presenter;
    private CardView lat_lng_card;
    private TextView lat_lng_txt;
    private OnFragmentInteractionListener mListener;
    private View lat_lng_view_lay;
    private TextView lat_lng_txt_lable;

    public Client_Overview() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param client Parameter 1.
     * @return A new instance of fragment Client_Overview.
     */
    // TODO: Rename and change types and number of parameters
    public static Client_Overview newInstance(Client client) {
        Client_Overview fragment = new Client_Overview();
        Bundle args = new Bundle();
        // args.putSerializable(ARG_PARAM1, client);
        args.putParcelable(ARG_PARAM1, client);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // client = (Client) getArguments().getSerializable(ARG_PARAM1);
            client = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_overview2, container, false);
        initializelables(view);
        inlializeView(view);
        presenter = new Overview_pc(this);
        presenter.getClientFromClientId(client.getCltId());
        return view;
    }

    void inlializeView(View view) {
        cnm = view.findViewById(R.id.cnm);
        cadr = view.findViewById(R.id.cadr);
        mob1 = view.findViewById(R.id.mob1);
        mob2 = view.findViewById(R.id.mob2);
        email = view.findViewById(R.id.email);
        accouttype = view.findViewById(R.id.accouttype);
        gst_txt = view.findViewById(R.id.gst_txt);
        tin_txt = view.findViewById(R.id.tin_txt);
        indus_txt = view.findViewById(R.id.indus_txt);
        note_txt = view.findViewById(R.id.note_txt);
        longitude = view.findViewById(R.id.longitude);
        latitude = view.findViewById(R.id.latitude);
        //   getlatlong = view.findViewById(R.id.buttonAccept);

        txt_refrence = view.findViewById(R.id.txt_refrence);
        txt_refrence_lable = view.findViewById(R.id.txt_refrence_lable);
        lat_lng_card = view.findViewById(R.id.lat_lng_card);
        lat_lng_view_lay = view.findViewById(R.id.lat_lng_view_lay);
        lat_lng_txt_lable = view.findViewById(R.id.lat_lng_txt_lable);
        lat_lng_txt = view.findViewById(R.id.lat_lng_txt);
        lat_lng_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.lat_and_lng));

        lat_lng_txt_lable.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.map_on_view));
        lat_lng_view_lay.setOnClickListener(this);


    }

    private void initializelables(View view) {
        client_txt = view.findViewById(R.id.client_txt);
        client_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.client_name));

        adderes_client = view.findViewById(R.id.adderes_client);
        adderes_client.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.address));

        clint_details = view.findViewById(R.id.clint_details);
        clint_details.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.client_details));

        account_dtls = view.findViewById(R.id.account_dtls);
        account_dtls.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.account_details));

        ac_type = view.findViewById(R.id.ac_type);
        ac_type.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.account_type) + " :");

        gst_no = view.findViewById(R.id.gst_no);
        //  gst_no.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.gst_no) + " :");

        tin_no = view.findViewById(R.id.tin_no);
        //   tin_no.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.tin_no) + " :");

        if (App_preference.getSharedprefInstance().getCompanySettingsDetails()
                .getGstLabel() != null)
            gst_no.setText(App_preference.getSharedprefInstance().getCompanySettingsDetails()
                    .getGstLabel() + " :");

        if (App_preference.getSharedprefInstance().getCompanySettingsDetails()
                .getTinLabel() != null)
            tin_no.setText(App_preference.getSharedprefInstance().getCompanySettingsDetails()
                    .getTinLabel() + " :");


        industry = view.findViewById(R.id.industry);
        industry.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.industry) + " :");

        lbl_notes = view.findViewById(R.id.lbl_notes);
        lbl_notes.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.notes));

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
    public void updateUI(Client client, Site_model site, ContactData contact) {
        cnm.setText(client.getNm());
        sb = new StringBuffer();
        if (site != null) {
            if (site.getAdr() != null) {
                sb.append(site.getAdr());
            }
            if (site.getCity() != null && site.getCity().trim().length() > 0) {
                sb.append(", ");
                sb.append(site.getCity());
            }
        } else {
            if (client.getAdr() != null) {
                sb.append(client.getAdr());
            }
            if (client.getCity() != null && client.getCity().trim().length() > 0) {
                sb.append(", ");
                sb.append(client.getCity());
            }
        }
        if (contact != null) {
            mob1.setText(contact.getMob1());
            mob2.setText(contact.getMob2());
            email.setText(contact.getEmail());
        } else {
            mob1.setText(client.getMob1());
            mob2.setText(client.getMob2());
            email.setText(client.getEmail());
        }
        cadr.setText(sb);
        mob1.setOnClickListener(this);
        mob2.setOnClickListener(this);
        email.setOnClickListener(this);
        lat_lng_view_lay.setOnClickListener(this);
        accouttype.setText(presenter.getAccountType(client.getPymtType()));
//        accouttype.setText(presenter.getAccountType(String.valueOf(client.getPymtType())));
        gst_txt.setText(client.getGstNo());
        tin_txt.setText(client.getTinNo());
        //indus_txt.setText(presenter.getIndustryName(Integer.parseInt(client.getIndustry())));
        indus_txt.setText(presenter.getIndustryName((client.getIndustry())));
        note_txt.setText(client.getNote());


        txt_refrence.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reference) + " :");

        try {
            if (client.getReferral() != null && !client.getReferral().equals("0")) {
                //   txt_refrence_lable.setText(AppConstant.clientReferenceList[Integer.parseInt(client.getReferral()) - 1]);
                ClientRefrenceModel model = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientRefrenceDao().getClientByRefrenceId(client.getReferral());
                txt_refrence_lable.setText(model.getRefName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (App_preference.getSharedprefInstance().getCompanySettingsDetails().getIsJobLatLngEnable().equals("0")) {
                lat_lng_card.setVisibility(View.GONE);
            } else {
                if (client.getLat() != null && client.getLng() != null) {
                    lat_lng_card.setVisibility(View.VISIBLE);
                    longitude.setText(client.getLng());
                    latitude.setText(client.getLat());
                } else {
                    lat_lng_card.setVisibility(View.GONE);
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }

    }

    @Override
    public void refreshUI(String clienId) {
        presenter.getClientFromClientId(clienId);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mob1:
                if (mob1.getText().toString().trim().length() > 0) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, DetailFragment.MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    } else if (AppUtility.isvalidPhoneNo(mob1.getText().toString().trim())) {
                        AppUtility.getCallOnNumber(getActivity(), mob1.getText().toString().trim());
                    }
                }
                break;
            case R.id.mob2:
                if (mob2.getText().toString().trim().length() > 0) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, DetailFragment.MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    } else if (AppUtility.isvalidPhoneNo(mob2.getText().toString().trim())) {
                        AppUtility.getCallOnNumber(getActivity(), mob2.getText().toString().trim());

                    }
                }
                break;
            case R.id.email:
                if (email.getText().toString().trim().length() > 0) {
                    AppUtility.sendRequestToJoin(email.getText().toString().trim(), "", "", getActivity());
                }
                break;

            case R.id.lat_lng_view_lay:
                try {
                    String locationdata = "";

                    if (client.getLat() != null && client.getLng() != null) {
                        // locationdata=  "http://maps.google.com/maps?daddr=" + client.getLat() + "," + client.getLng();
                        locationdata = "http://maps.google.com/maps?q=loc:" + client.getLat() + "," + client.getLng();
                    } else {
                        //  locationdata = "http://maps.google.com/maps?daddr=" + sb;
                        locationdata = "http://maps.google.co.in/maps?q=" + sb;
                    }

                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(locationdata));
                        startActivity(intent);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                }

                break;
        }
    }
}
