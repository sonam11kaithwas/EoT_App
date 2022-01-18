package com.eot_app.nav_menu.quote.quotes_add_item_pkg.item_add_mvp;

import com.eot_app.nav_menu.quote.add_quotes_pkg.model_pkg.Update_Quote_ReQ;
import com.eot_app.nav_menu.quote.quotes_add_item_pkg.item_model_pkg.AddItem_Model;

public interface QuoteItemAdd_PI {
    void getInventryItemFromServer();

    void getTaxList();

    void initialize_FwList_ServiceTittle_inventoryList();

    void getInventryItemList();

    void getFwList();

    void getJobServiceTittle();

    void apiCallAddQuotesItem(AddItem_Model addItemmodel);

    void callApiUpdateQuotesItem(Update_Quote_ReQ updateItemmodel);

    void loadFromServer(String search);
}
