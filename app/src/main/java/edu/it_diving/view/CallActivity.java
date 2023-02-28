package edu.it_diving.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import java.io.FileNotFoundException;
import java.io.IOException;

import edu.it_diving.R;
import edu.it_diving.databinding.CallMainBinding;

import edu.it_diving.databinding.CallMemberLayoutBinding;
import edu.it_diving.model.CallMember;
import edu.it_diving.viewModel.CameraViewModel;
import edu.it_diving.viewModel.MicViewModel;


public class CallActivity extends AppCompatActivity {

    private CallMainBinding binding;
    private final ConstraintSet constraintSetMainCallMemberAtTop = new ConstraintSet();
    private final ConstraintSet constraintSetMainCallMemberAtBottom = new ConstraintSet();

    private boolean mainUserIsAtTop = true;

    private CallMember mainMember, anotherMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Call_Activity", "creating...");

        super.onCreate(savedInstanceState);
        binding = CallMainBinding.inflate(getLayoutInflater());

        mainMember = new CallMember(getString(R.string.you), R.drawable.me);
        anotherMember =
                new CallMember(getString(R.string.test_name), R.drawable.cat);

        mainMember.cameraViewModel = new ViewModelProvider(this).get(CameraViewModel.class);
        mainMember.micViewModel = new ViewModelProvider(this).get(MicViewModel.class);

        binding.msgBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                openSMSApp();
            }
            else
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS},0);
        });
        binding.contactsBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
                chooseContact();
            else
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},0);
        });
        binding.gridBtn.setOnClickListener(v -> swapCallMembersView());
        binding.cameraBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                mainMember.cameraViewModel.changeMode();
            } else
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, 0);
        });
        binding.microBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                mainMember.micViewModel.changeMode();
            } else
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        });
        binding.helloBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.hello);
            builder.setNeutralButton(R.string.hello_answer, (dialog, id) -> dialog.cancel());

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        binding.callEndBtn.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        mainMember.cameraViewModel.getModeContainer().observe(this, this::changeCameraBtnImg);
        mainMember.micViewModel.getModeContainer().observe(this, this::changeMicroBtnImg);

        mainMember.fillView(this, binding.mainMember.callMemberView);
        anotherMember.fillView(this, binding.anotherMember.callMemberView);

        constraintSetMainCallMemberAtTop.clone(binding.getRoot());
        constraintSetMainCallMemberAtBottom.clone(binding.getRoot());
        constraintSetMainCallMemberAtBottom.connect(R.id.main_member, ConstraintSet.TOP,
                R.id.another_member, ConstraintSet.BOTTOM);
        constraintSetMainCallMemberAtBottom.connect(R.id.main_member, ConstraintSet.BOTTOM,
                R.id.lower_panel, ConstraintSet.TOP);
        constraintSetMainCallMemberAtBottom.connect(R.id.another_member, ConstraintSet.BOTTOM,
                R.id.main_member, ConstraintSet.TOP);
        constraintSetMainCallMemberAtBottom.connect(R.id.another_member, ConstraintSet.TOP,
                R.id.upper_panel, ConstraintSet.BOTTOM);

        setContentView(binding.getRoot());

        Log.d("Call_Activity", "created");
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (1):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    try (Cursor c = this.getContentResolver().query(contactData,
                            null, null, null, null)) {

                        if (c.moveToFirst()) {
                            String name = c.getString(c.getColumnIndexOrThrow(
                                    ContactsContract.Contacts.DISPLAY_NAME));
                            anotherMember.setName(name);
                            anotherMember.fillView(this,
                                    binding.anotherMember.callMemberView);
                        }
                    }
                }
                break;
        }
    }

    private void changeCameraBtnImg(boolean mode) {
        if (mode) {
            binding.cameraBtn.setBackground(
                    ContextCompat.getDrawable(this, R.drawable.gray_round_button));
            binding.cameraBtn.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.videocam)
            );
        }

        else {
            binding.cameraBtn.setBackground(
                    ContextCompat.getDrawable(this, R.drawable.white_round_button));
            binding.cameraBtn.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.videocam_off)
            );
        }
    }

    private void changeMicroBtnImg(boolean mode) {
        if (mode) {
            binding.microBtn.setBackground(
                    ContextCompat.getDrawable(this, R.drawable.gray_round_button));
            binding.microBtn.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.mic));
            binding.mainMember.nameTxt
                    .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        else {
            binding.microBtn.setBackground(
                    ContextCompat.getDrawable(this, R.drawable.white_round_button));
            binding.microBtn.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.mic_off));
            binding.mainMember.nameTxt
                    .setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.mic_off_small, 0);
        }
    }

    private void swapCallMembersView() {
        if (mainUserIsAtTop)
            constraintSetMainCallMemberAtBottom.applyTo(binding.getRoot());
        else
            constraintSetMainCallMemberAtTop.applyTo(binding.getRoot());

        mainUserIsAtTop = !mainUserIsAtTop;
    }

    private void chooseContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    private void openSMSApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
        startActivity(intent);
    }

}
