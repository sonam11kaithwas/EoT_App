package com.eot_app.nav_menu.client.clientlist.client_detail.client_overview.overview_mvp;

import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.settings.clientindustry_db.ClientIndustryModel;

import java.util.List;

/**
 * Created by aplite_pc302 on 6/28/18.
 */

public class Overview_pc implements OverView_pi {
    Client_Overview_view view;

    public Overview_pc(Client_Overview_view view) {
        this.view = view;
    }

    @Override
    public void getClientFromClientId(String clientId) {
        Client client = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().getClientFromId(Integer.parseInt(clientId));
        Site_model site = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getDefaultFromCltId(clientId);
        ContactData contact = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().getDefaultConFromCltId(clientId);
        view.updateUI(client, site, contact);
    }

    @Override
    public String getAccountType(String pymtType) {
        String paymentType = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientAccount().getNameByID(pymtType);
        return paymentType;
    }

    @Override
    public String getIndustryName(String indusId) {
        if (indusId != null) {
            List<ClientIndustryModel> industryList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientIndustryDao().getIndustryList();
            for (ClientIndustryModel industryModel : industryList) {
                if (indusId.equals(industryModel.getIndustryId())) {
                    return industryModel.getIndustryName();
                }
            }
        }
        return "";
    }
}
