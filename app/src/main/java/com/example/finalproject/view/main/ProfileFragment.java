package com.example.finalproject.view.main;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.models.User;
import com.example.finalproject.utils.Callback;
import com.example.finalproject.utils.UIUtils;
import com.example.finalproject.viewmodel.AppViewModel;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {


    ImageView profileIv;
    Button saveChanges;
    private Uri uploadImage, passportDoc;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        return view;
    }

    private final int GALLERY_REQUEST_CODE = 1;
    private final int GALLERY_PICK_CODE = 1;


    private void openGallery() {
        if(ActivityCompat.checkSelfPermission(getContext(),READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{READ_EXTERNAL_STORAGE},GALLERY_REQUEST_CODE);
        } else {
            Intent i =new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i,GALLERY_PICK_CODE);
        }
    }
    private void openPassportSelect() {
            MainActivity activity = (MainActivity) requireActivity();
            UIUtils.openDocumentPicker(uri ->
            {
                ProfileFragment.this.passportDoc = uri;
                saveChanges.setEnabled(true);
            }, activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      if(resultCode == RESULT_OK && requestCode == GALLERY_PICK_CODE) {
          uploadImage = data.getData();
          profileIv.setImageURI(uploadImage);
          saveChanges.setEnabled(true);
      }
    }


    private void saveChanges() {
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle("Travel book");
        dialog.setMessage("Saving changes...");

        dialog.show();
        if(getActivity()!=null)
        ((MainActivity)getActivity())
                .getViewModel()
                .updateUser(getActivity(), uploadImage, passportDoc, value -> {
            Toast.makeText(getContext(),"Changes saved successfully",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            saveChanges.setEnabled(false);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        saveChanges = view.findViewById(R.id.profile_saveChanges);
        Button passportSelect = view.findViewById(R.id.profile_addPassport);
        Button passportShow = view.findViewById(R.id.profile_currentPassport);

        if(getActivity() != null) {
            TextView helloNameTv = view.findViewById(R.id.helloTv_profile);
            TextView emailTv = view.findViewById(R.id.helloTv_profileEmail);
            profileIv = view.findViewById(R.id.profile_iv);

            profileIv.setOnClickListener(v -> {
                openGallery();
            });
            saveChanges.setOnClickListener(vx -> {
                saveChanges();
            });

            passportShow.setOnClickListener(vy -> {
                showPassport();
            });
            passportSelect.setOnClickListener(v -> {
                openPassportSelect();
            });
            AppViewModel viewModel = ((MainActivity) getActivity()).getViewModel();
            viewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
                emailTv.setText(user.getEmail());
                helloNameTv.setText("Hello " + (user.getName() == null ? user.getEmail().split("@")[0] : user.getName()));
                if(user.getImage() != null && !user.getImage().trim().isEmpty())
                    Picasso.get().load(user.getImage()).into(profileIv);
            });
        }

    }

    private void showPassport() {
        User user = ((MainActivity) getActivity()).getViewModel().getUserLiveData().getValue();
        if(user != null) {
            UIUtils.openDocumentByUrl(getContext(), user.getPassport().getDocumentUrl());
        }
    }
}
