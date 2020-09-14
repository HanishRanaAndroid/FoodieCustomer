package com.valle.foodieapp.listeners;

import com.valle.foodieapp.models.CartModel;

public interface UpdateCartListener {

    void updateCart(CartModel.responseData.ItemsList itemsList,String quantity);

    void RemoveItem(CartModel.responseData.ItemsList itemsList);

}
