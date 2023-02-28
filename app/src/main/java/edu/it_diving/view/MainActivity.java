package edu.it_diving.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import edu.it_diving.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Main_Activity", "creating...");

        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());


        binding.callBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CallActivity.class);
            callActivityResultLauncher.launch(intent);
        });

        setContentView(binding.getRoot());

        Log.d("Main_Activity", "created");
    }

    ActivityResultLauncher<Intent> callActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    finishAndRemoveTask();
                }
            });
}