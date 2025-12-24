package com.example.toursplitter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.toursplitter.presenter.CreateTourPresenter;
import com.example.toursplitter.view.CreateTourView;

public class CreateTourActivity extends AppCompatActivity implements CreateTourView {
    private EditText etTourName;
    private RadioGroup rgTourType;
    private RadioButton rbLocal, rbInternational;
    private Button btnCreateTour;
    private ProgressBar progressBar;
    private CreateTourPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tour);

        initViews();
        presenter = new CreateTourPresenter(this);

        btnCreateTour.setOnClickListener(v -> presenter.createTour());
    }

    private void initViews() {
        etTourName = findViewById(R.id.etTourName);
        rgTourType = findViewById(R.id.rgTourType);
        rbLocal = findViewById(R.id.rbLocal);
        rbInternational = findViewById(R.id.rbInternational);
        btnCreateTour = findViewById(R.id.btnCreateTour);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        btnCreateTour.setEnabled(false);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        btnCreateTour.setEnabled(true);
    }

    @Override
    public void onTourCreated() {
        Toast.makeText(this, "Tour created successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onCreateError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public String getTourName() {
        return etTourName.getText().toString().trim();
    }

    @Override
    public String getTourType() {
        int selectedId = rgTourType.getCheckedRadioButtonId();
        if (selectedId == R.id.rbLocal) {
            return "local";
        } else if (selectedId == R.id.rbInternational) {
            return "international";
        }
        return "";
    }
}