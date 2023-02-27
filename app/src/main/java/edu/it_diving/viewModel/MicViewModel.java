package edu.it_diving.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MicViewModel extends ViewModel {

    private boolean mode = false;

    private final MutableLiveData<Boolean> observableContainer = new MutableLiveData<Boolean>();

    public MutableLiveData<Boolean> getModeContainer() {
        return observableContainer;
    }

    public boolean getMode() {
        return mode;
    }

    public void changeMode() {
        mode = !mode;
        observableContainer.setValue(mode);
    }
}
