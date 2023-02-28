package edu.it_diving.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import edu.it_diving.R;
import edu.it_diving.databinding.CallMainBinding;

import edu.it_diving.viewModel.AnotherCallMember;
import edu.it_diving.viewModel.MainCallMember;


public class CallActivity extends AppCompatActivity {

    private CallMainBinding binding;
    private final ConstraintSet constraintSetMainCallMemberAtTop = new ConstraintSet();
    private final ConstraintSet constraintSetMainCallMemberAtBottom = new ConstraintSet();

    private boolean mainUserIsAtTop = true;

    private MainCallMember mainMember;
    private AnotherCallMember anotherMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Call_Activity", "creating...");

        super.onCreate(savedInstanceState);
        binding = CallMainBinding.inflate(getLayoutInflater());

        mainMember = new MainCallMember(this, getString(R.string.you), R.drawable.me);
        anotherMember = new AnotherCallMember(this, getString(R.string.test_name),
                R.drawable.cat);

        MainCallMemberFragment mainMemberFragment = binding.mainMember.getFragment();
        mainMemberFragment.setCallMember(mainMember);
        AnotherCallMemberFragment anotherMemberFragment = binding.anotherMember.getFragment();
        anotherMemberFragment.setCallMember(anotherMember);

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
                changeCameraBtnImg(mainMember.cameraViewModel.getMode());
            } else
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, 0);
        });
        binding.micBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                mainMember.micViewModel.changeMode();
                changeMicBtnImg(mainMember.micViewModel.getMode());
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
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    try (Cursor c = this.getContentResolver().query(contactData,
                            null, null, null, null)) {

                        if (c.moveToFirst()) {
                            String name = c.getString(c.getColumnIndexOrThrow(
                                    ContactsContract.Contacts.DISPLAY_NAME));

                            anotherMember.nameViewModel.setName(name);
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

    private void changeMicBtnImg(boolean mode) {
        if (mode) {
            binding.micBtn.setBackground(
                    ContextCompat.getDrawable(this, R.drawable.gray_round_button));
            binding.micBtn.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.mic));
        }

        else {
            binding.micBtn.setBackground(
                    ContextCompat.getDrawable(this, R.drawable.white_round_button));
            binding.micBtn.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.mic_off));
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
