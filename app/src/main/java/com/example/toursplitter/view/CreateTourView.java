
package com.example.toursplitter.view;

public interface CreateTourView {
    void showProgress();
    void hideProgress();
    void onTourCreated();
    void onCreateError(String error);
    String getTourName();
    String getTourType();
}