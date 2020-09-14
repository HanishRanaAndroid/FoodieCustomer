package com.valle.foodieapp.listeners;

import com.valle.foodieapp.models.OrderPlacedModel;

public interface WishlistListener {

    void OnClickItem(OrderPlacedModel.responseData.orders_InfoData orders_infoData, String wishlisted);

    void OnRepeatOrder(OrderPlacedModel.responseData.orders_InfoData orders_infoData);
}
