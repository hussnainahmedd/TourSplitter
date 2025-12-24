
package com.example.toursplitter.view;

public interface RegisterView {
    void showProgress();
    void hideProgress();
    void onRegisterSuccess();
    void onRegisterError(String error);
    String getName();
    String getEmail();
    String getPassword();
}