package com.eot_app.nav_menu.client.clientlist.client_detail.edit_overview.edit_client_view;

import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.utility.settings.clientaccount_db.ClientAccountType;

import java.util.List;

/**
 * Created by aplite_pc302 on 6/18/18.
 */

public interface Edit_client_view {
    void updateOverviewUI(Client client);

    void onClientUpdate(String clientId, boolean result, String msg);

    // void setAccountTypeList(List<ClientAccountType> accountList);
    void setAccountTypeList(List<ClientAccountType> accountList);

    void setIndustry();
}
