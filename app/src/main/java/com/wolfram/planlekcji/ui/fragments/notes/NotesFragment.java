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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.adapters.tree.Directory;
import com.wolfram.planlekcji.adapters.tree.TreeAdapter;
import com.wolfram.planlekcji.adapters.tree.TreeNode;
import com.wolfram.planlekcji.ui.bottomSheets.notes.AddImageBottomSheet;

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
    private OnBackPressedCallback callback;
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

        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                adapter.onBackPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
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
        // TODO: 2019-10-14 how to save Tree, not creating always new Tree
        TreeNode root = new Directory();
        //TreeNode child = new Subject(1, "SubjectTest");
        TreeNode child2 = new Directory();
        TreeNode child3 = new Directory();
        TreeNode child4 = new Directory();
        TreeNode child5 = new Directory();
        TreeNode child6 = new Directory();
        TreeNode child7 = new Directory();
        //root.addChildren(child, "SubjectTest");
        root.addChildren(child7, "SubjectTest");
        root.addChildren(child2, "Directory");
        root.addChildren(child3, "Directory1");
        root.addChildren(child6, "SubjectTest1");
        child6.addChildren(child4, "Directory3");
        child6.addChildren(child5, "SubjectTest3");
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
        /*Log.e("parent", "" + (child.getParent()==null) + "");
        Log.e("parent", "" + (node.getParent()==null) + "");*/

        /*Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(viewModel.getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);*/
        //this.sendBroadcast(mediaScanIntent);
    }
}
