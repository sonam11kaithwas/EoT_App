package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_payment_pkg.payment_mvp;

import android.content.Context;
import android.util.Log;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_List_Req_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_Res_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_payment_pkg.invoice_payment_model.Payment_ReQ_Model;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Payment_pc implements Payment_pi {
    private final PayMent_View payMent_view;

    public Payment_pc(PayMent_View payMent_view) {
        this.payMent_view = payMent_view;
    }

    @Override
    public void payment_invoice_apicall(String amount, String notes, String invId, String refName, String payType,
                                        long paymentDate, Boolean isEmailSendOrNot, String jobId, String isMailSentToClt) {
        try {
            float am = Float.parseFloat(amount);
            String date = AppUtility.getDateWithFormate2(paymentDate, "yyyy-MM-dd");
            Payment_ReQ_Model payment_reQ_model = new Payment_ReQ_Model(am, invId, refName, payType, date, notes, isEmailSendOrNot, jobId, isMailSentToClt);
            JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(payment_reQ_model));

            if (AppUtility.isInternetConnected()) {
                ActivityLogController.saveActivity(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_POST_PAYMENT_INVOICE, ActivityLogController.JOB_MODULE);

                AppUtility.progressBarShow((Context) payMent_view);
                ApiClient.getservices().eotServiceCall(Service_apis.postPaymentInvoice, AppUtility.getApiHeaders(), jsonObject)

                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<JsonObject>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(JsonObject jsonObject) {
                                if (jsonObject.get("success").getAsBoolean()) {
                                    payMent_view.onPaymentSuccess(LanguageController.getInstance().getMobileMsgByKey(AppConstant.payment_recv));
                                } else {
                                    payMent_view.showErrorAlertDialog(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("", e.getMessage());
                                AppUtility.progressBarDissMiss();
                            }

                            @Override
                            public void onComplete() {
                                AppUtility.progressBarDissMiss();
                            }
                        });


            } else {
                EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getInvoiceDetails(String jobId) {
        Inv_List_Req_Model inv_list_req_model = new Inv_List_Req_Model(jobId, 0);
        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(inv_list_req_model));

        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_INVOICE_DETAILS, ActivityLogController.JOB_MODULE);

            AppUtility.progressBarShow((Context) payMent_view);
            ApiClient.getservices().eotServiceCall(Service_apis.getInvoiceDetailMobile, AppUtility.getApiHeaders(), jsonObject)

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                         /*   jsonObject.remove("success");
                            jsonObject.addProperty("success", true);*/
                            if (jsonObject.get("success").getAsBoolean()) {
                                Gson gson = new Gson();
                                Inv_Res_Model invResModel = gson.fromJson(jsonObject.get("data"), Inv_Res_Model.class);
                                payMent_view.setData(invResModel);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                payMent_view.onInvoiceSuccessFalse(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("", e.getMessage());
                            AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });


        } else {
            payMent_view.onInvoiceSuccessFalse(LanguageController.getInstance().getMobileMsgByKey(AppConstant.payment_ntwrk));
        }
    }

    @Override
    public boolean isInputFieldDataValid(String amount, String payType, Inv_Res_Model inv_res_model) {
        if (payType.equals("")) {
            payMent_view.showErrorAlertDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.payment_type));
            return false;
        } else if (amount.equals("")) {
            payMent_view.showErrorAlertDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.please_enter_amount));
            return false;
        } else if (!amount.equals("")) {
            try {
                float am = Float.parseFloat(amount);
                if (am == 0) {
                    payMent_view.showErrorAlertDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.amount_required));
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (inv_res_model == null) {
            payMent_view.showErrorAlertDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.invoice_details));
            return false;
        }
        return true;
    }


}
