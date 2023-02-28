package edu.it_diving.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import edu.it_diving.R;
import edu.it_diving.viewModel.MainCallMember;

public class MainCallMemberFragment extends CallMemberFragment {
    private MainCallMember callMember;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        MutableLiveData<Boolean> x = callMember.micViewModel.getModeContainer();
        x.observe(getViewLifecycleOwner(),
                this::changeMicroDrawableRight);

        return v;
    }

    public void setCallMember(MainCallMember callMember) {
        this.callMember = callMember;
        super.setCallMember(callMember);
    }

    private void changeMicroDrawableRight(boolean mode) {
        if (mode)
            binding.nameTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    0, 0);
        else
            binding.nameTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.mic_off_small, 0);
    }
}
