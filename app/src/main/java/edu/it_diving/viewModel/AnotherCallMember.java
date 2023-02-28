package edu.it_diving.viewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

public class AnotherCallMember extends  CallMember {

    public NameViewModel nameViewModel;

    public AnotherCallMember(Context context, @NonNull String name, int icon) {
        super(name, icon);

        nameViewModel = new ViewModelProvider((ViewModelStoreOwner) context)
                .get(NameViewModel.class);
    }
}
