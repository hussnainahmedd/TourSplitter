
package com.example.toursplitter.presenter;

import android.text.TextUtils;
import com.example.toursplitter.data.FirebaseManager;
import com.example.toursplitter.model.Tour;
import com.example.toursplitter.view.CreateTourView;
import java.util.HashMap;
import java.util.Map;

public class CreateTourPresenter {
    private CreateTourView view;
    private FirebaseManager firebaseManager;

    public CreateTourPresenter(CreateTourView view) {
        this.view = view;
        this.firebaseManager = FirebaseManager.getInstance();
    }

    public void createTour() {
        String name = view.getTourName();
        String type = view.getTourType();

        if (TextUtils.isEmpty(name)) {
            view.onCreateError("Tour name is required");
            return;
        }

        if (TextUtils.isEmpty(type)) {
            view.onCreateError("Please select tour type");
            return;
        }

        if (firebaseManager.getCurrentUser() == null) {
            view.onCreateError("User not logged in");
            return;
        }

        view.showProgress();

        String tourId = firebaseManager.getDb().collection("tours").document().getId();
        String userId = firebaseManager.getCurrentUser().getUid();

        Map<String, Object> tourData = new HashMap<>();
        tourData.put("tourId", tourId);
        tourData.put("name", name);
        tourData.put("type", type);
        tourData.put("createdBy", userId);
        tourData.put("createdAt", System.currentTimeMillis());

        firebaseManager.getDb().collection("tours").document(tourId)
                .set(tourData)
                .addOnSuccessListener(aVoid -> {
                    view.hideProgress();
                    view.onTourCreated();
                })
                .addOnFailureListener(e -> {
                    view.hideProgress();
                    view.onCreateError("Failed to create tour: " + e.getMessage());
                });
    }
}