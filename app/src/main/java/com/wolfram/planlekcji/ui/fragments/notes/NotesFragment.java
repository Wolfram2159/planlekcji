package com.wolfram.planlekcji.ui.fragments.notes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.ui.bottomSheets.notes.AddImageBottomSheet;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class NotesFragment extends Fragment {

    private NotesFragmentViewModel viewModel;
    private ImageView iv;
    private static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iv = getActivity().findViewById(R.id.notes_iv);

        viewModel = ViewModelProviders.of(getActivity()).get(NotesFragmentViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.notes_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notes_camera:
                onCameraClick();
                return true;
            case R.id.notes_file:
                onFileClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void onCameraClick(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = viewModel.createImageFile();
        } catch (IOException ignored) {

        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(getContext(),
                    "com.example.android.fileprovider",
                    photoFile);
            camera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(camera, REQUEST_TAKE_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        File tempPhoto = new File(viewModel.getCurrentPhotoPath());
        if (resultCode==0 && tempPhoto.exists()){
            tempPhoto.delete();
        }else {
            AddImageBottomSheet addImageBottomSheet = new AddImageBottomSheet();
            addImageBottomSheet.show(getFragmentManager(), "AddImageBottomSheet");
        }
    }

    private void onFileClick(){
        /*Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(viewModel.getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);*/
        //this.sendBroadcast(mediaScanIntent);
    }
}
