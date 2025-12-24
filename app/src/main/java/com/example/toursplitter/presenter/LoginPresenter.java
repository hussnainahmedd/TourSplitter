
package com.example.toursplitter.presenter;

import android.text.TextUtils;
import com.example.toursplitter.data.FirebaseManager;
import com.example.toursplitter.model.User;
import com.example.toursplitter.view.LoginView;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter {
    private LoginView view;
    private FirebaseManager firebaseManager;

    public LoginPresenter(LoginView view) {
        this.view = view;
        this.firebaseManager = FirebaseManager.getInstance();
    }

    public void login() {
        String email = view.getEmail();
        String password = view.getPassword();

        if (TextUtils.isEmpty(email)) {
            view.onLoginError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            view.onLoginError("Password is required");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.onLoginError("Invalid email format");
            return;
        }

        view.showProgress();

        firebaseManager.getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    view.hideProgress();
                    if (task.isSuccessful()) {
                        view.onLoginSuccess();
                    } else {
                        String error = task.getException() != null ?
                                task.getException().getMessage() : "Login failed";
                        view.onLoginError(error);
                    }
                });
    }

    public boolean isUserLoggedIn() {
        return firebaseManager.isUserLoggedIn();
    }
}