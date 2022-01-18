package com.eot_app.nav_menu.quote.quotes_list_pkg;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.nav_menu.quote.quote_invoice_pkg.Quote_Invoice_Details_Activity;
import com.eot_app.nav_menu.quote.quotes_list_pkg.qoute_model_pkg.Quote_ReS;
import com.eot_app.nav_menu.quote.quotes_list_pkg.quotes_list_mvp.QuotesList_Pc;
import com.eot_app.nav_menu.quote.quotes_list_pkg.quotes_list_mvp.QuotesList_Pi;
import com.eot_app.nav_menu.quote.quotes_list_pkg.quotes_list_mvp.QuotesList_View;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.MyListItemSelected;
import com.eot_app.utility.util_interfaces.OnFragmentInteractionListener;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Quotes_List extends Fragment implements View.OnClickListener, QuotesList_View, MyListItemSelected<Quote_ReS> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private QuotesList_Pi quotesList_pi;
    private RecyclerView.LayoutManager layoutManager;
    private QuoteList_Adpter quoteListAdpter;
    private RecyclerView quoteList_RecyclerView;
    private SwipeRefreshLayout swiperefresh;
    private OnFragmentInteractionListener mListener;
    private QuotesFilter quotesFilter;
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().isEmpty()) {
                quotesFilter.setSearch(s.toString());
                quotesList_pi.reuestTogetNewRecords(quotesFilter);
            }
        }
    };
    private EditText edtSearch;
    private LinearLayout quotes_search_view, filter_tags_linear;
    private ChipGroup chipGroup;
    private Button date_time_clear_btn;
    private View em_layout;
    private TextView nolist_txt;

    public Quotes_List() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Quotes_List.
     */
    // TODO: Rename and change types and number of parameters
    public static Quotes_List newInstance(String param1, String param2) {
        Quotes_List fragment = new Quotes_List();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quotes__list, container, false);
        initializeViews(view);
        return view;
    }

    /**
     * add dynamically chips for advance filter
     */
    @Override
    public void chipAdd(QuotesFilter quotesreceiver) {
        filter_tags_linear.setVisibility(View.VISIBLE);
        quotesFilter.setStatus(quotesreceiver.getStatus());
        quotesFilter.setDtf(quotesreceiver.getDtf());
        quotesFilter.setDtt(quotesreceiver.getDtt());
        quotesFilter.setSearch(edtSearch.getText().toString());

        chipGroup.removeAllViews();
        chipGroup.setVisibility(View.VISIBLE);
        for (String status : this.quotesFilter.getStatus()) {
            addFilterName(status, "status");
        }
//        need to change it
        if (!quotesreceiver.getDtf().isEmpty() || !quotesreceiver.getDtt().isEmpty()) {
            String datechip = "";
            if (quotesreceiver.getDateFliterNm().isEmpty()) {

                datechip = quotesreceiver.getDtf().split(" ")[0]
                        + " To " +
                        quotesreceiver.getDtt().split(" ")[0];
            } else {
                datechip = quotesreceiver.getDateFliterNm();
            }
            addFilterName(datechip, "date_time");
        }
    }

    private void addFilterName(String chipItem_name, final String chipItem_tag) {
        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.chip_layout, null);
        TextView textView = v.findViewById(R.id.chip_txt);
        if (chipItem_tag.equals("status")) {
            textView.setText(getStatusNameById(chipItem_name));
        } else {
            textView.setText(chipItem_name);
        }
        textView.setTag(chipItem_tag);
        ImageView deleteMember = v.findViewById(R.id.deleteChip);
        deleteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView label = ((TextView) ((LinearLayout) view.getParent()).getChildAt(0));
                removeFilterOnChipItemClick(label.getText(), label.getTag());
                chipGroup.removeView((LinearLayout) view.getParent());

            }
        });
        chipGroup.addView(v, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }


    //remove filter chip
    private void removeFilterOnChipItemClick(CharSequence text, Object tag) {
        if (tag.equals("date_time")) {
            quotesFilter.setDtf("");
            quotesFilter.setDtt("");
        } else if (tag.equals("status")) {
            switch (text.toString()) {
                case AppConstant.status_new_key:
                    quotesFilter.getStatus().remove(AppConstant.QuoteNew);
                    break;
                case AppConstant.approved:
                    quotesFilter.getStatus().remove(AppConstant.QuoteAproved);
                    break;
                case AppConstant.status_onhold:
                    quotesFilter.getStatus().remove(AppConstant.QuoteOnHold);
                    break;
                case AppConstant.status_reje:
                    quotesFilter.getStatus().remove(AppConstant.QuoteReject);
                    break;
            }
        }
        if (quotesFilter.isAdvansedFilterEmpty()) {
            filter_tags_linear.setVisibility(View.GONE);
        }
        quotesList_pi.reuestTogetNewRecords(quotesFilter);
    }

    private String getStatusNameById(String chipItem_name) {
        switch (chipItem_name) {
            case AppConstant.QuoteNew:
                return AppConstant.status_new_key;

            case AppConstant.QuoteAproved:
                return AppConstant.approved;

            case AppConstant.QuoteOnHold:
                return AppConstant.status_onhold;

            case AppConstant.QuoteReject:
                return AppConstant.status_reje;
        }
        return "";
    }

    private void initializeViews(View view) {
        quoteList_RecyclerView = view.findViewById(R.id.quoteList_RecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        quoteList_RecyclerView.setLayoutManager(layoutManager);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        chipGroup = view.findViewById(R.id.chipGroup);
        date_time_clear_btn = view.findViewById(R.id.filter_list_btn);
        date_time_clear_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reset));
        date_time_clear_btn.setOnClickListener(this);

        quotes_search_view = view.findViewById(R.id.quotes_search_view);
        filter_tags_linear = view.findViewById(R.id.filter_tags_linear);

        edtSearch = view.findViewById(R.id.edtSearch);
        edtSearch.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.search_quote_code_client_name_address));

        em_layout = view.findViewById(R.id.em_layout);
        nolist_txt = view.findViewById(R.id.nolist_txt);

        List<Quote_ReS> quoteList = new ArrayList<>();
        quoteListAdpter = new QuoteList_Adpter(getActivity(), quoteList, this);
        quoteList_RecyclerView.setAdapter(quoteListAdpter);
        quotesList_pi = new QuotesList_Pc(this);

        view.findViewById(R.id.search_btn).setOnClickListener(this);

        quotesFilter = new QuotesFilter();
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                quoteListAdpter.notifyDataSetChanged();
                quotesList_pi.reuestTogetNewRecords(quotesFilter);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        edtSearch.addTextChangedListener(textWatcher);
        quotesList_pi.reuestTogetNewRecords(quotesFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        edtSearch.removeTextChangedListener(textWatcher);
        edtSearch.setText("");
    }

    @Override
    public void onMyListitemSeleted(Quote_ReS quote_reS) {
        if (AppUtility.isInternetConnected()) {
            Intent quotesinvoiceIntent = new Intent(getActivity(), Quote_Invoice_Details_Activity.class);
            quotesinvoiceIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            quotesinvoiceIntent.putExtra("quotId", quote_reS.getQuotId());
            startActivity(quotesinvoiceIntent);
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
    public void setQuoteList(List<Quote_ReS> quoteList) {
        try {
            if (swiperefresh.isRefreshing()) {
                swiperefresh.setRefreshing(false);
            }
            if (quoteList.size() > 0) {
                quoteListAdpter.updateRecords(quoteList);
                em_layout.setVisibility(View.GONE);
                quoteList_RecyclerView.setVisibility(View.VISIBLE);
            } else {
                em_layout.setVisibility(View.VISIBLE);
                quoteList_RecyclerView.setVisibility(View.GONE);
                nolist_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_quotes_found));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void disableSwipeReferesh() {
        if (swiperefresh.isRefreshing()) {
            swiperefresh.setRefreshing(false);
        }
    }

    @Override
    public void refereshQuotesList() {
        quotesList_pi.getQuotesDataList();
        // edtSearch.setText("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn:
                if (!TextUtils.isEmpty(edtSearch.getText().toString())) {
                    quotesFilter.setSearch(edtSearch.getText().toString());
                    quotesList_pi.reuestTogetNewRecords(quotesFilter);
                }
                break;
            case R.id.filter_list_btn:
                removeAllFilter();
                hideFilterView();
                break;
        }
    }

    private void hideFilterView() {
        quotes_search_view.setVisibility(View.GONE);
        filter_tags_linear.setVisibility(View.GONE);
        /******/
        quotesList_pi.reuestTogetNewRecords(quotesFilter);
    }

    private void removeAllFilter() {
        quotesFilter.clearAllvalues();
        // quotesList_pi.reuestTogetNewRecords(quotesFilter);
        // edtSearch.setText("");
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
    public void setSearchVisibility(boolean b) {
        quotes_search_view.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSessionExpired(String message) {
        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), message, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }
}
