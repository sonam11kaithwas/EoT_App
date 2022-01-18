package com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist;

import java.util.List;

/**
 * Created by ubuntu on 8/6/18.
 */

public interface Site_View {
    void setSiteData(List<Site_model> siteLists);

    void updateAdapter(int check, String siteedit);

    void setSearchData(List<Site_model> searchData);

    void updateFromObserver();

    void disableSwiprefresh();

}
