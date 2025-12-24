
package com.example.toursplitter.presenter;

import android.text.TextUtils;
import com.example.toursplitter.data.FirebaseManager;
import com.example.toursplitter.model.Member;
import com.example.toursplitter.view.MembersView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MembersPresenter {
    private MembersView view;
    private FirebaseManager firebaseManager;
    private String tourId;

    public MembersPresenter(MembersView view, String tourId) {
        this.view = view;
        this.tourId = tourId;
        this.firebaseManager = FirebaseManager.getInstance();
    }

    public void loadMembers() {
        view.showProgress();

        firebaseManager.getDb().collection("members")
                .whereEqualTo("tourId", tourId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    view.hideProgress();
                    List<Member> members = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Member member = doc.toObject(Member.class);
                        members.add(member);
                    }
                    view.onMembersLoaded(members);
                })
                .addOnFailureListener(e -> {
                    view.hideProgress();
                    view.onError("Failed to load members: " + e.getMessage());
                });
    }

    public void addMember(String name, String email) {
        if (TextUtils.isEmpty(name)) {
            view.onError("Member name is required");
            return;
        }

        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.onError("Valid email is required");
            return;
        }

        view.showProgress();

        String memberId = firebaseManager.getDb().collection("members").document().getId();

        Map<String, Object> memberData = new HashMap<>();
        memberData.put("memberId", memberId);
        memberData.put("tourId", tourId);
        memberData.put("name", name);
        memberData.put("email", email);
        memberData.put("joinedAt", System.currentTimeMillis());

        firebaseManager.getDb().collection("members").document(memberId)
                .set(memberData)
                .addOnSuccessListener(aVoid -> {
                    view.hideProgress();
                    view.onMemberAdded();
                    loadMembers();
                })
                .addOnFailureListener(e -> {
                    view.hideProgress();
                    view.onError("Failed to add member: " + e.getMessage());
                });
    }
}