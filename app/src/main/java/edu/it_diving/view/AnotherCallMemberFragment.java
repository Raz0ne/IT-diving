package edu.it_diving.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import edu.it_diving.viewModel.AnotherCallMember;

public class AnotherCallMemberFragment extends CallMemberFragment {

    private AnotherCallMember callMember;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = super.onCreateView(inflater, container, savedInstanceState);
        callMember.nameViewModel.getNameContainer().observe(getViewLifecycleOwner(),
                this::changeNameText);

        return v;
    }

    public void setCallMember(AnotherCallMember callMember) {
        this.callMember = callMember;
        super.setCallMember(callMember);
    }

    private void changeNameText(String newName) {
        binding.nameTxt.setText(newName);
    }
}
