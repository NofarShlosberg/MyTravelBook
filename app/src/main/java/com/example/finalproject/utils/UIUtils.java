package com.example.finalproject.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.example.finalproject.BaseActivity;

public class UIUtils {


    public static void openDocumentByUrl(Context context, String url) {
        if(url == null || url.trim().isEmpty()) {
            Toast.makeText(context,"There is no document to be found here",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent shareIntent = new Intent(Intent.ACTION_VIEW);
        shareIntent.setDataAndType(Uri.parse(url), "application/pdf");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(shareIntent);
        }
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

