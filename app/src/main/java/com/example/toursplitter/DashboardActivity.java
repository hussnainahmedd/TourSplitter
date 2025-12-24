package com.example.toursplitter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toursplitter.adapter.TourAdapter;
import com.example.toursplitter.model.Tour;
import com.example.toursplitter.presenter.DashboardPresenter;
import com.example.toursplitter.view.DashboardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements DashboardView {
    private RecyclerView rvTours;
    private TextView tvEmpty;
    private ProgressBar progressBar;
    private FloatingActionButton fabAddTour;
    private DashboardPresenter presenter;
    private TourAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Tours");
        }

        initViews();
        setupRecyclerView();

        presenter = new DashboardPresenter(this);
        presenter.loadTours();

        fabAddTour.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateTourActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadTours();
    }

    private void initViews() {
        rvTours = findViewById(R.id.rvTours);
        tvEmpty = findViewById(R.id.tvEmpty);
        progressBar = findViewById(R.id.progressBar);
        fabAddTour = findViewById(R.id.fabAddTour);
    }

    private void setupRecyclerView() {
        adapter = new TourAdapter(new ArrayList<>(), tour -> {
            Intent intent = new Intent(this, TourDetailsActivity.class);
            intent.putExtra("tourId", tour.getTourId());
            intent.putExtra("tourName", tour.getName());
            intent.putExtra("tourType", tour.getType()); // Pass tour type
            startActivity(intent);
        });
        rvTours.setLayoutManager(new LinearLayoutManager(this));
        rvTours.setAdapter(adapter);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onToursLoaded(List<Tour> tours) {
        tvEmpty.setVisibility(View.GONE);
        rvTours.setVisibility(View.VISIBLE);
        adapter.updateTours(tours);
    }

    @Override
    public void onLoadError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyState() {
        rvTours.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            presenter.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}