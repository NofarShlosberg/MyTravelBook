package com.example.finalproject.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.finalproject.BaseActivity;

public class UIUtils {

    public static void openDateDialog(Context context, SelectDateDialog.ISelectDate onSelectDate) {
        SelectDateDialog dialog = new SelectDateDialog(context,onSelectDate);
        dialog.show();
    }

    public static void openDocumentPicker(Callback<Uri> onDocumentSelected,  BaseActivity baseActivity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("application/pdf");
        baseActivity.setOnDocumentSelected(onDocumentSelected);
        baseActivity.getDocumentPickerLauncher().launch(intent);
    }
}
