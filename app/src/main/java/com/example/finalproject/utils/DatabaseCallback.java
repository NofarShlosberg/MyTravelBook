package com.example.finalproject.utils;

public interface DatabaseCallback<T> extends Callback<T> {
    void onDatabaseException(Exception e);
}
