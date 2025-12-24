
package com.example.toursplitter.view;

import com.example.toursplitter.model.Expense;
import java.util.List;

public interface ExpensesView {
    void showProgress();
    void hideProgress();
    void onExpensesLoaded(List<Expense> expenses);
    void onExpenseAdded();
    void onError(String error);
}