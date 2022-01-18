package com.eot_app.nav_menu.client.clientlist.client_detail.client_overview.overview_mvp;

/**
 * Created by aplite_pc302 on 6/28/18.
 */

public interface OverView_pi {
    void getClientFromClientId(String clientId);

    String getAccountType(String pymtType);

    String getIndustryName(String indusId);
}
