package com.example.finalproject;

import static com.example.finalproject.MainActivity.USER_ARG;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.models.User;
import com.example.finalproject.repositories.UserRepository;
import com.example.finalproject.utils.DatabaseCallback;
import com.example.finalproject.view.auth.AuthActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logoApp);
        logo.setAlpha(0f);

        logo.animate()
                .setDuration(2000)
                .alpha(1f)
                .start();

        // @TODO : Start at AuthActivity
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser() != null) { // remember user
                    UserRepository repository = new UserRepository();
                    repository.getUser(new DatabaseCallback<User>() {
                        @Override
                        public void onDatabaseException(Exception e) {
                            // No internet
                        }

                        @Override
                        public void consume(User user) {
                            String userAsJson = new Gson().toJson(user);
                            Intent intent= new Intent(SplashActivity.this , MainActivity.class);
                            intent.putExtra(USER_ARG, userAsJson);
                            startActivity(intent);
                        }
                    });
                    return;
                }
                startActivity(new Intent(SplashActivity.this , AuthActivity.class));
            }
        },2000);

    }
}
