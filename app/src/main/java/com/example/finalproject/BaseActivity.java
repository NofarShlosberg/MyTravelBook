package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.utils.Callback;
import com.google.android.material.snackbar.Snackbar;

public abstract class BaseActivity extends AppCompatActivity {
    private Callback<Uri> onDocumentSelected;
    protected ActivityResultLauncher<Intent> documentPickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Uri selectedFileUri = result.getData().getData();
                if(onDocumentSelected != null) {
                    onDocumentSelected.consume(selectedFileUri);
                }
            }
        }
    });

    public ActivityResultLauncher<Intent> getDocumentPickerLauncher() {
        return documentPickerLauncher;
    }

    public void setOnDocumentSelected(Callback<Uri> onDocumentSelected) {
        this.onDocumentSelected = onDocumentSelected;
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showSnackBar(View v, String message) {
        Snackbar.make(v,message,Snackbar.LENGTH_SHORT).show();
    }
}
