package com.example.finalproject.dialogs.show_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.finalproject.R;

public class ShowDocumentDialog extends DialogFragment {

    private String docUrl;
    public ShowDocumentDialog(String docUrl) {
        this.docUrl = docUrl;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = getLayoutInflater().inflate(
                R.layout.show_doc_view,
                null,
                false
        );
        WebView wv = v.findViewById(R.id.showDocView);
        if(docUrl!=null) {
            wv.getSettings().setPluginState(WebSettings.PluginState.ON);

            wv.setWebViewClient(new Callback());

            wv.getSettings().setJavaScriptEnabled(true);
            wv.loadUrl(docUrl);
        }
        return new AlertDialog.Builder(getContext())
                .setView(v)
                .setPositiveButton("Close",null)
                .create();
    }
    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }
    }
}
