package com.valle.foodieapp.listeners;

public interface PayUMoneyTransactionListener {
    void onSucessfullTransaction(String payuResponse,String merchantResponse);
    void onFailureOfTransaction();
}
