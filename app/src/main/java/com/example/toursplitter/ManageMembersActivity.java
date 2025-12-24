package com.example.toursplitter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toursplitter.adapter.MemberAdapter;
import com.example.toursplitter.model.Member;
import com.example.toursplitter.presenter.MembersPresenter;
import com.example.toursplitter.view.MembersView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ManageMembersActivity extends AppCompatActivity implements MembersView {
    private RecyclerView rvMembers;
    private ProgressBar progressBar;
    private FloatingActionButton fabAddMember;
    private MembersPresenter presenter;
    private MemberAdapter adapter;
    private String tourId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_members);

        tourId = getIntent().getStringExtra("tourId");
        String tourName = getIntent().getStringExtra("tourName");
        setTitle("Members - " + tourName);

        initViews();
        setupRecyclerView();

        presenter = new MembersPresenter(this, tourId);
        presenter.loadMembers();

        fabAddMember.setOnClickListener(v -> showAddMemberDialog());
    }

    private void initViews() {
        rvMembers = findViewById(R.id.rvMembers);
        progressBar = findViewById(R.id.progressBar);
        fabAddMember = findViewById(R.id.fabAddMember);
    }

    private void setupRecyclerView() {
        adapter = new MemberAdapter(new ArrayList<>());
        rvMembers.setLayoutManager(new LinearLayoutManager(this));
        rvMembers.setAdapter(adapter);
    }

    private void showAddMemberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_member, null);

        EditText etMemberName = dialogView.findViewById(R.id.etMemberName);
        EditText etMemberEmail = dialogView.findViewById(R.id.etMemberEmail);

        builder.setView(dialogView)
                .setTitle("Add Member")
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = etMemberName.getText().toString().trim();
                    String email = etMemberEmail.getText().toString().trim();
                    presenter.addMember(name, email);
                })
                .setNegativeButton("Cancel", null)
                .show();
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
    public void onMembersLoaded(List<Member> members) {
        adapter.updateMembers(members);
    }

    @Override
    public void onMemberAdded() {
        Toast.makeText(this, "Member added successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}