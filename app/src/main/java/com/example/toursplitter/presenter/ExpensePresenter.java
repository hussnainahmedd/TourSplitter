package com.example.toursplitter.presenter;

import android.text.TextUtils;
import com.example.toursplitter.data.FirebaseManager;
import com.example.toursplitter.model.Expense;
import com.example.toursplitter.view.ExpensesView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpensePresenter {
    private ExpensesView view;
    private FirebaseManager firebaseManager;
    private String tourId;

    public ExpensePresenter(ExpensesView view, String tourId) {
        this.view = view;
        this.tourId = tourId;
        this.firebaseManager = FirebaseManager.getInstance();
    }

    public void loadExpenses() {
        view.showProgress();

        firebaseManager.getDb().collection("expenses")
                .whereEqualTo("tourId", tourId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    view.hideProgress();
                    List<Expense> expenses = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Expense expense = doc.toObject(Expense.class);
                        expenses.add(expense);
                    }
                    view.onExpensesLoaded(expenses);
                })
                .addOnFailureListener(e -> {
                    view.hideProgress();
                    view.onError("Failed to load expenses: " + e.getMessage());
                });
    }

    public void addExpense(String description, String amountStr, String paidByMemberId,
                           String paidByName, List<String> splitAmongIds, String category) {

        if (TextUtils.isEmpty(description)) {
            view.onError("Description is required");
            return;
        }

        if (TextUtils.isEmpty(amountStr)) {
            view.onError("Amount is required");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                view.onError("Amount must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            view.onError("Invalid amount");
            return;
        }

        if (splitAmongIds == null || splitAmongIds.isEmpty()) {
            view.onError("Please select members to split with");
            return;
        }

        view.showProgress();

        String expenseId = firebaseManager.getDb().collection("expenses").document().getId();

        Map<String, Object> expenseData = new HashMap<>();
        expenseData.put("expenseId", expenseId);
        expenseData.put("tourId", tourId);
        expenseData.put("description", description);
        expenseData.put("amount", amount);
        expenseData.put("paidBy", paidByMemberId);
        expenseData.put("paidByName", paidByName);
        expenseData.put("splitAmong", splitAmongIds);
        expenseData.put("category", category);
        expenseData.put("date", System.currentTimeMillis());

        firebaseManager.getDb().collection("expenses").document(expenseId)
                .set(expenseData)
                .addOnSuccessListener(aVoid -> {
                    view.hideProgress();
                    view.onExpenseAdded();
                })
                .addOnFailureListener(e -> {
                    view.hideProgress();
                    view.onError("Failed to add expense: " + e.getMessage());
                });
    }
}