package com.example.finalproject.view.auth;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.finalproject.R;
import com.example.finalproject.models.User;
import com.example.finalproject.viewmodel.AuthenticationViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;

import java.util.Arrays;
import java.util.List;

public class LoginFragment extends Fragment {

    private AuthenticationViewModel viewModel;

    private Button loginBtn, toRegisterBtn;
    private ImageView googleSignInBtn;
    private EditText emailLogin,passwordLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login,container,false);
    }


    // after view initiated
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(AuthenticationViewModel.class);
        findViews(view);
        attachListeners(view);
    }



    private void findViews(View view) {
        loginBtn = view.findViewById(R.id.loginBtn);
        toRegisterBtn = view.findViewById(R.id.toRegisterBtn);
        googleSignInBtn = view.findViewById(R.id.googleSignInBtn);
        emailLogin = view.findViewById(R.id.email_login);
        passwordLogin = view.findViewById(R.id.password_login);
    }

    private void attachListeners(View view) {
        // google sign in
        googleSignInBtn.setOnClickListener(v -> {
            googleSignInBtn.setEnabled(false);
            signInWithGoogle();
        });
        // login
        loginBtn.setOnClickListener(v -> {
            // @TODO : ProgressDialog
            signInWithEmailAndPassword();
        });
        // to register page navigation action
        toRegisterBtn.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                .navigate(R.id.action_loginFragment_to_registerFragment));
        // login-result observer
        viewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), user -> {
            AuthActivity activity = (AuthActivity) getActivity();
            if(activity==null) return;
            activity.postAuth(user);
        });

        viewModel.getExceptionMableLiveData().observe(getViewLifecycleOwner(), exception -> {
            AuthActivity activity = (AuthActivity) getActivity();
            if(activity==null) return;
            activity.showSnackBar(view,exception.getMessage());
        });
    }

    private void signInWithEmailAndPassword() {
        String email = emailLogin.getText().toString();
        String password = passwordLogin.getText().toString();
        viewModel.login(email, password);
    }


    private void signInWithGoogle() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();


        signInLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        viewModel.loginWithGoogle(result);
        googleSignInBtn.setEnabled(true);
    }

}
