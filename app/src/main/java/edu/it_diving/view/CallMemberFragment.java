package edu.it_diving.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import edu.it_diving.databinding.FragmentCallMemberBinding;
import edu.it_diving.viewModel.CallMember;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class CallMemberFragment extends Fragment {

    private Context context;

    protected FragmentCallMemberBinding binding;
    private CallMember callMember;

    public static CallMemberFragment newInstance() {
        return new CallMemberFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCallMemberBinding.inflate(inflater, container, false);

        showCallMember();

        return binding.getRoot();
    }

    public void setCallMember(CallMember callMember) {
        this.callMember = callMember;
    }

    public void showCallMember() {
        if (callMember.getName().length() >= 48) {
            String limitedName = callMember.getName()
                    .substring(0, 48) + "...";
            binding.nameTxt.setText(limitedName);
        }
        else
            binding.nameTxt.setText(callMember.getName());

        if (callMember.getIconBitmap() == null) {
            binding.iconImg.setImageResource(callMember.getIcon());

            Glide.with(context).load(callMember.getIcon())
                    .apply(RequestOptions.bitmapTransform(
                            new BlurTransformation(100, 3))).into(binding.bgImg);
        }
        else {
            Glide.with(context).load(callMember.getIconBitmap()).into(binding.iconImg);

            Glide.with(context).load(callMember.getIconBitmap())
                    .apply(RequestOptions.bitmapTransform(
                            new BlurTransformation(100, 3))).into(binding.bgImg);
        }
    }

}