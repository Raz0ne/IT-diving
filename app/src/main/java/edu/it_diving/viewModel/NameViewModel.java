package edu.it_diving.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NameViewModel extends ViewModel {

    private String name;

    private final MutableLiveData<String> observableContainer = new MutableLiveData<String>();

    public MutableLiveData<String> getNameContainer() {
        return observableContainer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        observableContainer.setValue(this.name);
    }
}
