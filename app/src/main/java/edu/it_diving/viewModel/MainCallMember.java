package edu.it_diving.viewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

public class MainCallMember extends CallMember {
    public CameraViewModel cameraViewModel;
    public MicViewModel micViewModel;

    public MainCallMember(Context context, @NonNull String name, int icon) {
        super(name, icon);

        cameraViewModel = new ViewModelProvider((ViewModelStoreOwner) context)
                .get(CameraViewModel.class);
        micViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(MicViewModel.class);
    }
}
