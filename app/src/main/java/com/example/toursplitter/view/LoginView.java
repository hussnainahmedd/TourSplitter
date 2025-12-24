
package com.example.toursplitter.view;

public interface LoginView {
    void showProgress();
    void hideProgress();
    void onLoginSuccess();
    void onLoginError(String error);
    String getEmail();
    String getPassword();
}