
package com.example.toursplitter.presenter;

import com.example.toursplitter.data.FirebaseManager;
import com.example.toursplitter.model.Tour;
import com.example.toursplitter.view.DashboardView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class DashboardPresenter {
    private DashboardView view;
    private FirebaseManager firebaseManager;

    public DashboardPresenter(DashboardView view) {
        this.view = view;
        this.firebaseManager = FirebaseManager.getInstance();
    }

    public void loadTours() {
        if (firebaseManager.getCurrentUser() == null) {
            view.onLoadError("User not logged in");
            return;
        }

        view.showProgress();
        String userId = firebaseManager.getCurrentUser().getUid();

        firebaseManager.getDb().collection("tours")
                .whereEqualTo("createdBy", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    view.hideProgress();
                    List<Tour> tours = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Tour tour = doc.toObject(Tour.class);
                        tours.add(tour);
                    }

                    if (tours.isEmpty()) {
                        view.showEmptyState();
                    } else {
                        view.onToursLoaded(tours);
                    }
                })
                .addOnFailureListener(e -> {
                    view.hideProgress();
                    view.onLoadError("Failed to load tours: " + e.getMessage());
                });
    }

    public void signOut() {
        firebaseManager.signOut();
    }
}