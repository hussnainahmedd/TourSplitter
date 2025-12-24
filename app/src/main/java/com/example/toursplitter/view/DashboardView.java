
package com.example.toursplitter.view;

import com.example.toursplitter.model.Tour;
import java.util.List;

public interface DashboardView {
    void showProgress();
    void hideProgress();
    void onToursLoaded(List<Tour> tours);
    void onLoadError(String error);
    void showEmptyState();
}