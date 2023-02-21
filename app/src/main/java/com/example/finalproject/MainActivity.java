package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.finalproject.models.Travel;
import com.example.finalproject.models.User;
import com.example.finalproject.repositories.TravelRepository;
import com.example.finalproject.utils.DatabaseCallback;
import com.example.finalproject.view.auth.AuthActivity;
import com.example.finalproject.viewmodel.AppViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;


public class MainActivity extends BaseActivity {

    public static final String USER_ARG = "user";

    private AppBarConfiguration appBarConfiguration;
    private NavigationView navigationView;
    private NavController navController;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;

    private AppViewModel viewModel;

    public AppViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            if(firebaseAuth.getCurrentUser() == null) {
                startActivity(new Intent(MainActivity.this, AuthActivity.class));
                Toast.makeText(this,"Logged out successfully!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel = new ViewModelProvider(this).get(AppViewModel.class);

        navigationView = findViewById(R.id.nav_MAIN_view);
        drawer = findViewById(R.id.drawer);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_MAIN_host);
        navController = navHostFragment.getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(drawer)
                .build();
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);

        toggle = new ActionBarDrawerToggle(this, drawer, R.string.close, R.string.open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        handleWelcome();
    }

    private void handleWelcome() {
        Intent previousScreenIntent = getIntent();
        String userAsJson = previousScreenIntent.getStringExtra(USER_ARG);
        User user = new Gson().fromJson(userAsJson, User.class);
        viewModel.setUser(user);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
