package com.example.toursplitter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TourDetailsActivity extends AppCompatActivity {
    private String tourId;
    private String tourName;
    private String tourType; // Add this
    private Button btnManageMembers, btnAddExpense, btnViewSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_details);

        tourId = getIntent().getStringExtra("tourId");
        tourName = getIntent().getStringExtra("tourName");
        tourType = getIntent().getStringExtra("tourType"); // Add this

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(tourName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnManageMembers = findViewById(R.id.btnManageMembers);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnViewSummary = findViewById(R.id.btnViewSummary);
    }

    private void setupClickListeners() {
        btnManageMembers.setOnClickListener(v -> {
            Intent intent = new Intent(TourDetailsActivity.this, ManageMembersActivity.class);
            intent.putExtra("tourId", tourId);
            intent.putExtra("tourName", tourName);
            intent.putExtra("tourType", tourType); // Add this
            startActivity(intent);
        });

        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(TourDetailsActivity.this, AddExpenseActivity.class);
            intent.putExtra("tourId", tourId);
            intent.putExtra("tourName", tourName);
            intent.putExtra("tourType", tourType); // Add this
            startActivity(intent);
        });

        btnViewSummary.setOnClickListener(v -> {
            Intent intent = new Intent(TourDetailsActivity.this, ExpenseSummaryActivity.class);
            intent.putExtra("tourId", tourId);
            intent.putExtra("tourName", tourName);
            intent.putExtra("tourType", tourType); // Add this
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}