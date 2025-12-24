
package com.example.toursplitter.view;

import com.example.toursplitter.model.Balance;
import java.util.List;

public interface SummaryView {
    void showProgress();
    void hideProgress();
    void onBalancesCalculated(List<Balance> balances, double totalExpenses);
    void onError(String error);
}
