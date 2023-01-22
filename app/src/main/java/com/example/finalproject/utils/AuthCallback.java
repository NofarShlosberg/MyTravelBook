package com.example.finalproject.utils;

import com.google.firebase.auth.AuthResult;

public interface AuthCallback extends Callback<AuthResult> {
    void onException(Exception e);
}
