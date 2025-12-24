
package com.example.toursplitter.presenter;

import android.text.TextUtils;
import com.example.toursplitter.data.FirebaseManager;
import com.example.toursplitter.model.User;
import com.example.toursplitter.view.RegisterView;
import java.util.HashMap;
import java.util.Map;

public class RegisterPresenter {
    private RegisterView view;
    private FirebaseManager firebaseManager;

    public RegisterPresenter(RegisterView view) {
        this.view = view;
        this.firebaseManager = FirebaseManager.getInstance();
    }

    public void register() {
        String name = view.getName();
        String email = view.getEmail();
        String password = view.getPassword();

        if (TextUtils.isEmpty(name)) {
            view.onRegisterError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            view.onRegisterError("Email is required");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.onRegisterError("Invalid email format");
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            view.onRegisterError("Password must be at least 6 characters");
            return;
        }

        view.showProgress();

        firebaseManager.getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && firebaseManager.getCurrentUser() != null) {
                        String userId = firebaseManager.getCurrentUser().getUid();
                        saveUserToFirestore(userId, name, email);
                    } else {
                        view.hideProgress();
                        String error = task.getException() != null ?
                                task.getException().getMessage() : "Registration failed";
                        view.onRegisterError(error);
                    }
                });
    }

    private void saveUserToFirestore(String userId, String name, String email) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", userId);
        userData.put("name", name);
        userData.put("email", email);
        userData.put("createdAt", System.currentTimeMillis());

        firebaseManager.getDb().collection("users").document(userId)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    view.hideProgress();
                    view.onRegisterSuccess();
                })
                .addOnFailureListener(e -> {
                    view.hideProgress();
                    view.onRegisterError("Failed to save user data: " + e.getMessage());
                });
    }
}