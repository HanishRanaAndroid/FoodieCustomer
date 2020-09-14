package com.valle.foodieapp.listeners;

import com.valle.foodieapp.models.MenuItemListModel;

public interface AddToCartListner {
    void addTocart(MenuItemListModel.responseData.ItemsData itemsData);
}
