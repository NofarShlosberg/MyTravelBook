package com.example.finalproject.view.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.finalproject.R;
import com.example.finalproject.models.User;
import com.example.finalproject.viewmodel.AuthenticationViewModel;

public class RegisterFragment extends Fragment {

    private AuthenticationViewModel viewModel;

    private Button registerBtn, toLoginBtn;
    private TextView emailRegister,passwordRegister,nameRegister;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register,container,false);
    }

    // after view initiated
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(AuthenticationViewModel.class);

        findViews(view);
        attachListeners(view);
    }

    private void attachListeners(View view) {
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

        // return to login screen
        toLoginBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
        // register action
        registerBtn.setOnClickListener(v -> {
            // @TODO : ProgressDialog

            AuthActivity activity = (AuthActivity) getActivity();
            String name = nameRegister.getText().toString();
           if(name.isEmpty()) {
               activity.showSnackBar(view, "Please fill your name at the name field");
               nameRegister.requestFocus();
               return;
           }
            if(emailRegister.getText().toString().isEmpty()) {
                activity.showSnackBar(view, "Please fill your email at the email field");
                emailRegister.requestFocus();
                return;
            }
            if(passwordRegister.getText().toString().isEmpty()) {
                activity.showSnackBar(view, "Please fill your password at the password field");
                passwordRegister.requestFocus();
                return;
            }

           viewModel.register(new User(name,
                   emailRegister.getText().toString()), passwordRegister.getText().toString());
        });
    }

    private void findViews(View view) {
        toLoginBtn = view.findViewById(R.id.toLoginBtn);
        registerBtn = view.findViewById(R.id.registerBtn);
        emailRegister = view.findViewById(R.id.email_register);
        passwordRegister = view.findViewById(R.id.password_register);
        nameRegister = view.findViewById(R.id.name_register);
    }


}
