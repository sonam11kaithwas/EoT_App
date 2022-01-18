package com.eot_app.nav_menu.client.clientlist.client_detail.edit_overview.edit_client_presenter;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.activitylog.LogModel;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.edit_overview.edit_client_model.EditClient_resquest;
import com.eot_app.nav_menu.client.clientlist.client_detail.edit_overview.edit_client_view.Edit_client_view;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.db.OfflineDataController;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.clientaccount_db.ClientAccountType;
import com.eot_app.utility.settings.clientindustry_db.ClientIndustryModel;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by aplite_pc302 on 6/18/18.
 */

public class Edit_client_pc implements Edit_client_pi {
    private final Edit_client_view edit_client_view;
    private int cltId;

    public Edit_client_pc(Edit_client_view edit_client_view) {
        this.edit_client_view = edit_client_view;
    }

    @Override
    public void callUpdateClient(String name, String pymtType, String gst_no, String tin_no, boolean active,
                                 String industry, String notes, String indusnmame, String refrence) {
        int active_no = active ? 1 : 0;
        Client client = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().getClientFromId(cltId);
        client.setNm(name);
        client.setPymtType(pymtType);
        client.setGstNo(gst_no);
        client.setTinNo(tin_no);
        client.setIndustry(String.valueOf(industry));
        client.setNote(notes);
        client.setIsactive(String.valueOf(active_no));
        client.setIndustryName(indusnmame);
        client.setReferral(refrence);

        EditClient_resquest editClient_resquest = new EditClient_resquest(client.getCltId(),
                name, pymtType, gst_no, tin_no,
                industry, notes, active_no, client.getLat(), client.getLng()
                , client.getReferral());
        String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
        Gson gson = new Gson();
        String data = gson.toJson(editClient_resquest);
        OfflineDataController.getInstance().addInOfflineDB(Service_apis.updateClient, data, dateTime);
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().update(client);
        edit_client_view.onClientUpdate(client.getCltId(), true, LanguageController.getInstance().getMobileMsgByKey(AppConstant.clt_updated));

        LogModel logModel = ActivityLogController
                .getObj(ActivityLogController.CLIENT_MODULE,
                        ActivityLogController.UPDATE_CLIENT,
                        ActivityLogController.CLIENT_MODULE);
        ActivityLogController.saveOfflineTable(logModel);
    }

    @Override
    public void setClientValues(Client client) {
        cltId = Integer.parseInt(client.getCltId());
        Client client1 = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().getClientFromId(Integer.parseInt(client.getCltId()));
        edit_client_view.updateOverviewUI(client1);
    }

    @Override
    public void getAccountypelist() {
        List<ClientAccountType> accountTypeList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientAccount().getAccountList();
        edit_client_view.setAccountTypeList(accountTypeList);
    }

    @Override
    public CharSequence getAccoutTypeName(String pymtType) {
        String paymentType = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientAccount().getNameByID(pymtType);
        return paymentType;
    }

    @Override
    public String getIndustryName(String indusId) {
        List<ClientIndustryModel> industryList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientIndustryDao().getIndustryList();
//        if (indusId != 0) {
        for (ClientIndustryModel industryModel : industryList) {
            if (indusId.equals(industryModel.getIndustryId())) {
                return industryModel.getIndustryName();
            }
        }
        return "";
//        if (indusId.equals("0")) {
//            return null;
//        } else {
//            int inst = Integer.parseInt(indusId);
//            inst = inst - 1;
//            String[] indusArray = AppConstant.Industries;
//            return indusArray[inst];
//        }
    }
}
