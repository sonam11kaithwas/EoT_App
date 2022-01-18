package com.eot_app.nav_menu.client.clientlist.client_detail.edit_overview.edit_client_presenter;

import com.eot_app.nav_menu.client.client_db.Client;

/**
 * Created by aplite_pc302 on 6/18/18.
 */

public interface Edit_client_pi {

    void callUpdateClient(String name, String pymtType, String gst_no, String tin_no, boolean active, String industry, String notes, String indusName, String reference);

    void setClientValues(Client client);

    void getAccountypelist();

    CharSequence getAccoutTypeName(String pymtType);

    String getIndustryName(String indusId);
}
