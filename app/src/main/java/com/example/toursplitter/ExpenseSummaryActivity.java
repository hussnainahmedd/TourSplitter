package com.example.toursplitter;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toursplitter.adapter.BalanceAdapter;
import com.example.toursplitter.data.FirebaseManager;
import com.example.toursplitter.model.Balance;
import com.example.toursplitter.model.Expense;
import com.example.toursplitter.model.Member;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExpenseSummaryActivity extends AppCompatActivity {
    private TextView tvTotalExpenses;
    private RecyclerView rvBalances;
    private ProgressBar progressBar;
    private String tourId;
    private String tourType; // Add this
    private FirebaseManager firebaseManager;
    private BalanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_summary);

        tourId = getIntent().getStringExtra("tourId");
        String tourName = getIntent().getStringExtra("tourName");
        tourType = getIntent().getStringExtra("tourType"); // Add this

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Summary - " + tourName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        firebaseManager = FirebaseManager.getInstance();

        initViews();
        setupRecyclerView();
        calculateBalances();
    }

    private void initViews() {
        tvTotalExpenses = findViewById(R.id.tvTotalExpenses);
        rvBalances = findViewById(R.id.rvBalances);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        adapter = new BalanceAdapter(new ArrayList<>(), getCurrencySymbol()); // Pass currency
        rvBalances.setLayoutManager(new LinearLayoutManager(this));
        rvBalances.setAdapter(adapter);
    }

    private String getCurrencySymbol() {
        if ("local".equalsIgnoreCase(tourType)) {
            return "PKR";
        } else {
            return "$";
        }
    }

    private void calculateBalances() {
        progressBar.setVisibility(View.VISIBLE);

        firebaseManager.getDb().collection("members")
                .whereEqualTo("tourId", tourId)
                .get()
                .addOnSuccessListener(memberSnapshot -> {
                    Map<String, Member> memberMap = new HashMap<>();
                    for (QueryDocumentSnapshot doc : memberSnapshot) {
                        Member member = doc.toObject(Member.class);
                        memberMap.put(member.getMemberId(), member);
                    }

                    loadExpensesAndCalculate(memberMap);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadExpensesAndCalculate(Map<String, Member> memberMap) {
        firebaseManager.getDb().collection("expenses")
                .whereEqualTo("tourId", tourId)
                .get()
                .addOnSuccessListener(expenseSnapshot -> {
                    List<Expense> expenses = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : expenseSnapshot) {
                        Expense expense = doc.toObject(Expense.class);
                        expenses.add(expense);
                    }

                    List<Balance> balances = calculateMemberBalances(expenses, memberMap);
                    double totalExpenses = calculateTotalExpenses(expenses);

                    progressBar.setVisibility(View.GONE);

                    String currency = getCurrencySymbol();
                    if (expenses.isEmpty()) {
                        tvTotalExpenses.setText("Total: " + currency + " 0.00");
                        Toast.makeText(this, "No expenses added yet", Toast.LENGTH_SHORT).show();
                    } else {
                        tvTotalExpenses.setText(String.format(Locale.getDefault(),
                                "Total: %s %.2f", currency, totalExpenses));
                        adapter.updateBalances(balances);
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Failed to load expenses", Toast.LENGTH_SHORT).show();
                });
    }

    private List<Balance> calculateMemberBalances(List<Expense> expenses,
                                                  Map<String, Member> memberMap) {
        Map<String, Double> totalPaid = new HashMap<>();
        Map<String, Double> totalOwed = new HashMap<>();

        for (String memberId : memberMap.keySet()) {
            totalPaid.put(memberId, 0.0);
            totalOwed.put(memberId, 0.0);
        }

        for (Expense expense : expenses) {
            String paidBy = expense.getPaidBy();
            totalPaid.put(paidBy, totalPaid.getOrDefault(paidBy, 0.0) + expense.getAmount());

            List<String> splitAmong = expense.getSplitAmong();
            if (splitAmong != null && !splitAmong.isEmpty()) {
                double perPersonShare = expense.getAmount() / splitAmong.size();
                for (String memberId : splitAmong) {
                    totalOwed.put(memberId, totalOwed.getOrDefault(memberId, 0.0) + perPersonShare);
                }
            }
        }

        List<Balance> balances = new ArrayList<>();
        for (String memberId : memberMap.keySet()) {
            Member member = memberMap.get(memberId);
            double paid = totalPaid.getOrDefault(memberId, 0.0);
            double owed = totalOwed.getOrDefault(memberId, 0.0);
            Balance balance = new Balance(memberId, member.getName(), paid, owed);
            balances.add(balance);
        }

        return balances;
    }

    private double calculateTotalExpenses(List<Expense> expenses) {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}