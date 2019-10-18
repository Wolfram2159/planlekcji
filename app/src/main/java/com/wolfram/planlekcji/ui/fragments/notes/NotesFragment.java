package com.wolfram.planlekcji.ui.fragments.notes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.adapters.tree.Directory;
import com.wolfram.planlekcji.adapters.tree.Root;
import com.wolfram.planlekcji.adapters.tree.TreeAdapter;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.notes.Note;
import com.wolfram.planlekcji.ui.bottomSheets.notes.AddImageBottomSheet;
import com.wolfram.planlekcji.utils.others.Utils;

import java.io.File;
import java.io.IOException;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotesFragment extends Fragment {

    private NotesFragmentViewModel viewModel;
    private RecyclerView recycler;
    private TreeAdapter adapter;
    private TextView path;

    private static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(NotesFragmentViewModel.class);
        setHasOptionsMenu(true);

        recycler = getActivity().findViewById(R.id.notes_recycler);
        path = getActivity().findViewById(R.id.notes_path);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                adapter.onBackPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // TODO: 2019-10-14 how to save Tree, not creating always new Tree
        Root root = new Root();
        viewModel.getSubjects().observe(this, subjects -> {
            for (Subject subject : subjects) {
                Directory pictures = new Directory();
                Directory documents = new Directory();
                subject.addChildren(pictures, "Pictures");
                subject.addChildren(documents, "Documents");
                root.addChildren(subject, subject.getName());

                viewModel.getNotesFromSubject(subject.getId()).observe(this, notes -> {
                    for (Note note : notes) {
                        String nodeName = Utils.getDateString(note.getDate());
                        if (note.getFilePath() != null){
                            documents.addChildren(note, "File");
                        }else if (note.getPhotoPath() != null){
                            pictures.addChildren(note, "Photo");
                        }
                    }
                    adapter.setParent(root);
                });
            }
            adapter.setParent(root);
        });

        viewModel.getNotes().observe(this, notes -> {
            Log.e("a",notes.size() + "");
        });

        adapter = new TreeAdapter(root);

        adapter.setOnPathChangedListener(newPath -> {
            Animation inAnim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
            inAnim.setDuration(800);
            path.setText(newPath);
            path.startAnimation(inAnim);
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
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
        /*Log.e("parent", "" + (child.getParent()==null) + "");
        Log.e("parent", "" + (node.getParent()==null) + "");*/

        /*Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(viewModel.getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);*/
        //this.sendBroadcast(mediaScanIntent);
    }
}
