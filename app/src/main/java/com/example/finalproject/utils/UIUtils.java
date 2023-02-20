package com.example.finalproject.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;

import com.example.finalproject.BaseActivity;

public class UIUtils {


    public static void openDocumentByUrl(Fragment context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }


    public static void openDateDialog(Context context, SelectDateDialog.ISelectDate onSelectDate) {
        SelectDateDialog dialog = new SelectDateDialog(context,onSelectDate);
        dialog.show();
    }

    public static void openDocumentPicker(Callback<Uri> onDocumentSelected,  BaseActivity baseActivity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        baseActivity.setOnDocumentSelected(onDocumentSelected);
        baseActivity.getDocumentPickerLauncher().launch(intent);
    }
}

