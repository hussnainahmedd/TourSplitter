package com.example.toursplitter;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.toursplitter.data.FirebaseManager;
import com.example.toursplitter.model.Member;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddExpenseActivity extends AppCompatActivity {
    private EditText etDescription, etAmount;
    private Spinner spPaidBy, spCategory;
    private Button btnAddExpense;
    private ProgressBar progressBar;
    private TextView tvAmountLabel; // Add this
    private String tourId;
    private String tourType; // Add this
    private List<Member> members;
    private FirebaseManager firebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        tourId = getIntent().getStringExtra("tourId");
        String tourName = getIntent().getStringExtra("tourName");
        tourType = getIntent().getStringExtra("tourType"); // Add this

        if (getSupportActionBar() != null) {
            String currency = getCurrencySymbol();
            getSupportActionBar().setTitle("Add Expense (" + currency + ") - " + tourName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        firebaseManager = FirebaseManager.getInstance();
        members = new ArrayList<>();

        initViews();
        setupCategorySpinner();
        updateCurrencyLabel(); // Add this
        loadMembers();

        btnAddExpense.setOnClickListener(v -> addExpense());
    }

    private void initViews() {
        etDescription = findViewById(R.id.etDescription);
        etAmount = findViewById(R.id.etAmount);
        spPaidBy = findViewById(R.id.spPaidBy);
        spCategory = findViewById(R.id.spCategory);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        progressBar = findViewById(R.id.progressBar);
        tvAmountLabel = findViewById(R.id.tvAmountLabel); // Add this if you have it in XML
    }

    private void setupCategorySpinner() {
        String[] categories = {"Food", "Transport", "Accommodation", "Entertainment", "Shopping", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
    }

    private void updateCurrencyLabel() {
        String currency = getCurrencySymbol();
        etAmount.setHint("Amount (" + currency + ")");
        if (tvAmountLabel != null) {
            tvAmountLabel.setText("Amount (" + currency + ")");
        }
    }

    private String getCurrencySymbol() {
        if ("local".equalsIgnoreCase(tourType)) {
            return "PKR";
        } else {
            return "$";
        }
    }

    private void loadMembers() {
        progressBar.setVisibility(View.VISIBLE);
        btnAddExpense.setEnabled(false);

        firebaseManager.getDb().collection("members")
                .whereEqualTo("tourId", tourId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    progressBar.setVisibility(View.GONE);
                    btnAddExpense.setEnabled(true);

                    members.clear();
                    List<String> memberNames = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Member member = doc.toObject(Member.class);
                        members.add(member);
                        memberNames.add(member.getName());
                    }

                    if (members.isEmpty()) {
                        Toast.makeText(this, "Please add members first!", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, memberNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spPaidBy.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnAddExpense.setEnabled(true);
                    Toast.makeText(this, "Failed to load members: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void addExpense() {
        String description = etDescription.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();

        if (description.isEmpty()) {
            etDescription.setError("Description is required");
            etDescription.requestFocus();
            return;
        }

        if (amountStr.isEmpty()) {
            etAmount.setError("Amount is required");
            etAmount.requestFocus();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                etAmount.setError("Amount must be greater than 0");
                etAmount.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            etAmount.setError("Invalid amount");
            etAmount.requestFocus();
            return;
        }

        if (members.isEmpty()) {
            Toast.makeText(this, "Please add members first", Toast.LENGTH_SHORT).show();
            return;
        }

        Member paidByMember = members.get(spPaidBy.getSelectedItemPosition());
        String category = spCategory.getSelectedItem().toString();

        progressBar.setVisibility(View.VISIBLE);
        btnAddExpense.setEnabled(false);

        String expenseId = firebaseManager.getDb().collection("expenses").document().getId();

        List<String> splitAmongIds = new ArrayList<>();
        for (Member m : members) {
            splitAmongIds.add(m.getMemberId());
        }

        Map<String, Object> expenseData = new HashMap<>();
        expenseData.put("expenseId", expenseId);
        expenseData.put("tourId", tourId);
        expenseData.put("description", description);
        expenseData.put("amount", amount);
        expenseData.put("paidBy", paidByMember.getMemberId());
        expenseData.put("paidByName", paidByMember.getName());
        expenseData.put("splitAmong", splitAmongIds);
        expenseData.put("category", category);
        expenseData.put("date", System.currentTimeMillis());

        firebaseManager.getDb().collection("expenses").document(expenseId)
                .set(expenseData)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    String currency = getCurrencySymbol();
                    Toast.makeText(this, "Expense added: " + currency + " " + amount,
                            Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnAddExpense.setEnabled(true);
                    Toast.makeText(this, "Failed to add expense: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}