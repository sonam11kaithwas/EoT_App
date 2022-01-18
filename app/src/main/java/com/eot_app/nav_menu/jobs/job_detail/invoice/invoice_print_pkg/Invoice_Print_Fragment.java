package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_print_pkg;


import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_Client_Address_model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_Res_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ItemData;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Item_Details;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ShippingItem;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.ZoomLayout;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;

public class Invoice_Print_Fragment extends Fragment implements Invoice_Print_View {
    TextView txtName, txtAddress, txtCity, txtCountry, txtEmailId, txtInvoiceBillTo, txtBillToName, txtBillToAddress, txtBillToCountry, txtBillToPhone, txtInvoiceCode, txtPoNumber,
            txtInvoiceDate, txtDueDate, txtInvoiceTotal, txtAdditionalDiscount, txtTotalAmount, txtPaid, txtRemainingAmount, txtRateLabel, txtAmountLabel;
    ImageView imagePersonIcon;
    LinearLayout linearInvoice;
    ZoomLayout zoomLayout;
    double invoiceTotalAmount = 0;
    private LinearLayout linearUserDetail, linearInvoiceDetail;
    private Inv_Res_Model invoiceDetails;
    private TableLayout tableLayout;
    private Invoice_Print_pc invoice_print_pc;

    public Invoice_Print_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invoice_print, container, false);
        invoice_print_pc = new Invoice_Print_pc(this);
        findViews(view);
        FrameLayout.LayoutParams userDetailParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        userDetailParams.gravity = Gravity.RIGHT;

        FrameLayout.LayoutParams invoiceDetailParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        invoiceDetailParams.gravity = Gravity.LEFT;

        Bundle bundle = getArguments();
        int position = bundle.getInt("position");
        String convertedObject = bundle.getString("invoiceDetails");
        invoiceDetails = new Gson().fromJson(convertedObject, Inv_Res_Model.class);

        if (position == 1) {
            linearUserDetail.setLayoutParams(userDetailParams);
            linearInvoiceDetail.setLayoutParams(invoiceDetailParams);
        }
        setItemListData();
        setInvoiceDetails();
        return view;
    }

    private void findViews(View view) {
        zoomLayout = view.findViewById(R.id.zoomLayout);
        linearInvoice = view.findViewById(R.id.linearInvoice);
        linearUserDetail = view.findViewById(R.id.linearUserDetail);
        linearInvoiceDetail = view.findViewById(R.id.linearInvoiceDetail);
        imagePersonIcon = view.findViewById(R.id.imagePersonIcon);
        txtName = view.findViewById(R.id.txtName);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtCity = view.findViewById(R.id.txtCity);
        txtCountry = view.findViewById(R.id.txtCountry);
        txtEmailId = view.findViewById(R.id.txtEmailId);
        txtInvoiceBillTo = view.findViewById(R.id.txtInvoiceBillTo);
        txtBillToName = view.findViewById(R.id.txtBillToName);
        txtBillToAddress = view.findViewById(R.id.txtBillToAddress);
        txtBillToCountry = view.findViewById(R.id.txtBillToCountry);
        txtBillToPhone = view.findViewById(R.id.txtBillToPhone);
        txtInvoiceCode = view.findViewById(R.id.txtInvoiceCode);
        txtPoNumber = view.findViewById(R.id.txtPoNumber);
        txtInvoiceDate = view.findViewById(R.id.txtInvoiceDate);
        txtDueDate = view.findViewById(R.id.txtDueDate);
        txtInvoiceTotal = view.findViewById(R.id.txtInvoiceTotal);
        txtAdditionalDiscount = view.findViewById(R.id.txtAdditionalDiscount);
        txtTotalAmount = view.findViewById(R.id.txtTotalAmount);
        txtPaid = view.findViewById(R.id.txtPaid);
        txtRemainingAmount = view.findViewById(R.id.txtRemainingAmount);
        tableLayout = view.findViewById(R.id.tableLayout);
        txtRateLabel = view.findViewById(R.id.txtRateLabel);
        txtAmountLabel = view.findViewById(R.id.txtAmountLabel);

    }

    private void setInvoiceDetails() {
        if (invoiceDetails != null) {
            txtRateLabel.setText("Rate " + "(" + (invoiceDetails.getCurSym()) + ")");
            txtAmountLabel.setText("Amount " + "(" + (invoiceDetails.getCurSym()) + ")");
            double remainingAmount, totalAmount = 0, paidAmount = 0;
            try {
                Item_Details item_details = new Gson().fromJson(invoiceDetails.getPro(), Item_Details.class);
                if (item_details != null) {

                    if (item_details.getAdr() != null && !item_details.getAdr().equals("")) {
                        txtAddress.setText(item_details.getAdr());
                        txtAddress.setVisibility(View.VISIBLE);
                    }
                    if (item_details.getCmpemail() != null && !item_details.getCmpemail().equals("")) {
                        txtEmailId.setText("E: -" + item_details.getCmpemail());
                        txtEmailId.setVisibility(View.VISIBLE);
                    }
                    if (item_details.getCmpnm() != null && !item_details.getCmpnm().equals("")) {
                        txtName.setText(item_details.getCmpnm());
                        txtName.setVisibility(View.VISIBLE);
                    }
                    if (item_details.getCity() != null && !item_details.getCity().equals("")) {
                        txtCity.setText(item_details.getCity());
                        txtCity.setVisibility(View.VISIBLE);
                    }
                    if (item_details.getCountry() != null && !item_details.getCountry().equals("")) {
                        txtCountry.setText(item_details.getCountry());
                        txtCountry.setVisibility(View.VISIBLE);
                    }

                }
                if (!invoiceDetails.getInv_client_address().equals("")) {
                    Inv_Client_Address_model client_address_model = new Gson().fromJson(invoiceDetails.getInv_client_address(), Inv_Client_Address_model.class);
                    if (client_address_model != null) {
                        if (client_address_model.getNm() != null && !client_address_model.getNm().equals("")) {
                            txtBillToName.setVisibility(View.VISIBLE);
                            txtBillToName.setText(client_address_model.getNm());
                        }
                        if (client_address_model.getAdr() != null && !client_address_model.getNm().equals("")) {
                            if (!client_address_model.getCity().equals("")) {
                                txtBillToAddress.setVisibility(View.VISIBLE);
                                txtBillToAddress.setText(client_address_model.getAdr() + " " + client_address_model.getCity());
                            } else {
                                txtBillToAddress.setVisibility(View.VISIBLE);
                                txtBillToAddress.setText(client_address_model.getAdr());
                            }
                        }
                        if (client_address_model.getCountry() != null && !client_address_model.getCountry().equals("")) {
                            txtBillToCountry.setVisibility(View.VISIBLE);
                            txtBillToCountry.setText(client_address_model.getCountry());
                        }
                        if (client_address_model.getMob() != null && !client_address_model.getMob().equals("")) {
                            txtBillToPhone.setVisibility(View.VISIBLE);
                            txtBillToPhone.setText("P: -" + client_address_model.getMob());
                        }
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }


            if (invoiceDetails.getCode() != null && !invoiceDetails.getCode().equals("")) {
                txtInvoiceCode.setText(invoiceDetails.getCode());
            }
            if (invoiceDetails.getPono() != null && !invoiceDetails.getPono().equals("")) {
                txtPoNumber.setText(invoiceDetails.getPono());
            }

            if (invoiceDetails.getDuedate() != null && !invoiceDetails.getDuedate().equals("")) {
                long dateInMilli = Long.parseLong(invoiceDetails.getDuedate());
                txtDueDate.setText(AppUtility.getDateWithFormate(dateInMilli, "dd-MMM-yyyy"));
            }
            if (invoiceDetails.getInvDate() != null && !invoiceDetails.getInvDate().equals("")) {
                long dateInMilli = Long.parseLong(invoiceDetails.getInvDate());
                txtInvoiceDate.setText(AppUtility.getDateWithFormate(dateInMilli, "dd-MMM-yyyy"));
            }

            if (invoiceDetails.getDiscount() != null && !invoiceDetails.getDiscount().equals("")) {
                txtAdditionalDiscount.setText(invoiceDetails.getCurSym() + " " + AppUtility.getRoundoff_amount(invoiceDetails.getDiscount()));
            }

            txtInvoiceTotal.setText(invoiceDetails.getCurSym() + " " + AppUtility.getRoundoff_amount(String.valueOf(invoiceTotalAmount)));

            if (invoiceDetails.getTotal() != null && !invoiceDetails.getTotal().equals("")) {
                txtTotalAmount.setText(invoiceDetails.getCurSym() + " " + AppUtility.getRoundoff_amount(invoiceDetails.getTotal()));
                try {
                    totalAmount = Double.parseDouble(invoiceDetails.getTotal());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            if (invoiceDetails.getPaid() != null && !invoiceDetails.getPaid().equals("")) {
                txtPaid.setText(invoiceDetails.getCurSym() + " " + AppUtility.getRoundoff_amount(invoiceDetails.getPaid()));
                try {
                    paidAmount = Double.parseDouble(invoiceDetails.getPaid());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            remainingAmount = totalAmount - paidAmount;
            txtRemainingAmount.setText(invoiceDetails.getCurSym() + " " + AppUtility.getRoundoff_amount(String.valueOf(remainingAmount)));

            if (invoiceDetails.getShippingItem().size() > 0) {
                TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tableLayoutParams.topMargin = (int) getContext().getResources().getDimension(R.dimen.table_row_margin);
                for (int i = 0; i < invoiceDetails.getShippingItem().size(); i++) {
                    ShippingItem shippingItem = invoiceDetails.getShippingItem().get(i);
                    View tableView = LayoutInflater.from(getContext()).inflate(R.layout.shipping_item_layout, null);
                    tableView.setLayoutParams(tableLayoutParams);
                    TextView txtLabel = tableView.findViewById(R.id.txtLabel);
                    TextView txtValue = tableView.findViewById(R.id.txtValue);
                    txtLabel.setText(shippingItem.getInm());
                    txtValue.setText(invoiceDetails.getCurSym() + " " + AppUtility.getRoundoff_amount(shippingItem.getRate()));
                    tableLayout.addView(tableView, i + 2);
                }
            }

        }
    }

    private void setItemListData() {
        if (invoiceDetails != null) {
            if (invoiceDetails.getItemData().size() > 0) {
                for (int i = 0; i < invoiceDetails.getItemData().size(); i++) {
                    ItemData itemData = invoiceDetails.getItemData().get(i);
                    View itemView = LayoutInflater.from(getContext()).inflate(R.layout.invoice_item_layout, null);
                    TextView txtSrNo = itemView.findViewById(R.id.txtSrNo);
                    TextView txtItemFieldWorker = itemView.findViewById(R.id.txtItemFieldWorker);
                    TextView txtDescription = itemView.findViewById(R.id.txtDescription);
                    TextView txtQtyHr = itemView.findViewById(R.id.txtQtyHr);
                    TextView txtRate = itemView.findViewById(R.id.txtRate);
                    TextView txtDiscount = itemView.findViewById(R.id.txtDiscount);
                    TextView txtTax = itemView.findViewById(R.id.txtTax);
                    TextView txtAmount = itemView.findViewById(R.id.txtAmount);


                    txtSrNo.setText("" + (i + 1));
                    if (itemData.getInm() != null && !itemData.getInm().equals("")) {
                        txtItemFieldWorker.setText(itemData.getInm());
                    }
                    if (itemData.getQty() != null && !itemData.getQty().equals("")) {
                        txtQtyHr.setText(itemData.getQty());
                    }
                    if (itemData.getRate() != null && !itemData.getRate().equals("")) {
                        txtRate.setText(AppUtility.getRoundoff_amount(itemData.getRate()));
                    }
                    if (itemData.getDiscount() != null && !itemData.getDiscount().equals("")) {
                        txtDiscount.setText(itemData.getDiscount());
                    }

                    if (itemData.getDes() != null && !itemData.getDes().equals("")) {
                        txtDescription.setText(itemData.getDes());
                    }

                    float totalTax = 0.0f;
                    if (itemData.getTax() != null) {
                        for (Tax hm : itemData.getTax()) {
                            // String taxRate = hm.getRate();
//                            if (taxRate != null) {
//                                try {
//                                    totalTax = totalTax + (Float.parseFloat(taxRate));
//                                } catch (NumberFormatException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
                        }
                        txtTax.setText("" + totalTax + "%");

                        String totalAmount = AppUtility.getCalculatedAmount(itemData.getQty(), itemData.getRate(), itemData.getDiscount(), itemData.getTax(), invoiceDetails.getTaxCalculationType());
                        invoiceTotalAmount = invoiceTotalAmount + Double.parseDouble(totalAmount);
                        txtAmount.setText(AppUtility.getRoundoff_amount(totalAmount));
                    }
                    linearInvoice.addView(itemView);
                }
            }
        }
    }

    public void convertZoomLayoutToPdf() {
        zoomLayout.exitScaleAndTranslation();
        invoice_print_pc.createPdf(invoice_print_pc.loadBitmapFromView(zoomLayout));
    }

    @Override
    public void onPdfCreated(final File filePath, String fileName) {

        AppUtility.alertDialog2(getContext(), "", "Pdf generated successfully!\n\n" + filePath.toString(), "open", "no", new Callback_AlertDialog() {
            @Override
            public void onPossitiveCall() {
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                // pdfIntent.setDataAndType(Uri.fromFile(filePath),"application/pdf");
                pdfIntent.setDataAndType(FileProvider.getUriForFile(getContext(), "com.eot_app.provider", filePath), "application/pdf");
                pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    startActivity(pdfIntent);
                } catch (Exception e) {
                    EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.file_cant_open));//"can't open this type of file!"
                }
            }

            @Override
            public void onNegativeCall() {

            }
        });
       /* Toast.makeText(getContext(), "PDF is created!!!", Toast.LENGTH_SHORT).show();
        getActivity().finish();*/
    }
}
