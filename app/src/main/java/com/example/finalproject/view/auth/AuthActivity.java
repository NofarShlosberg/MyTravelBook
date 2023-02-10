package com.example.finalproject.view.auth;

import static com.example.finalproject.MainActivity.USER_ARG;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.BaseActivity;
import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.models.User;
import com.google.gson.Gson;

public class AuthActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }

    public void postAuth(User user) {
        Intent toMainActivityIntent = new Intent(this, MainActivity.class);
        String userAsJson = new Gson().toJson(user);
        toMainActivityIntent.putExtra(USER_ARG, userAsJson);
        startActivity(toMainActivityIntent);
        finish();
    }
}
