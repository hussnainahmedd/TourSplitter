
package com.example.toursplitter.view;

import com.example.toursplitter.model.Member;
import java.util.List;

public interface MembersView {
    void showProgress();
    void hideProgress();
    void onMembersLoaded(List<Member> members);
    void onMemberAdded();
    void onError(String error);
}