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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.custom.mapper.RoomMapper;
import com.wolfram.planlekcji.ui.adapters.tree.DirectoryNode;
import com.wolfram.planlekcji.ui.adapters.tree.ImageNoteNode;
import com.wolfram.planlekcji.ui.adapters.tree.RootNode;
import com.wolfram.planlekcji.ui.adapters.tree.SubjectNode;
import com.wolfram.planlekcji.ui.adapters.tree.TextNoteNode;
import com.wolfram.planlekcji.ui.adapters.tree.TreeAdapter;
import com.wolfram.planlekcji.ui.adapters.tree.TreeNode;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.notes.ImageNote;
import com.wolfram.planlekcji.database.room.entities.notes.TextNote;
import com.wolfram.planlekcji.ui.bottomSheets.notes.AddImageNoteBottomSheet;
import com.wolfram.planlekcji.ui.bottomSheets.notes.AddTextNoteBottomSheet;
import com.wolfram.planlekcji.ui.bottomSheets.notes.ModifyImageNoteBottomSheet;
import com.wolfram.planlekcji.ui.bottomSheets.notes.ShowTextNoteBottomSheet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotesFragment extends Fragment {

    private NotesFragmentViewModel viewModel;
    private RecyclerView recycler;
    private TreeAdapter adapter;
    private TextView path;

    private StfalconImageViewer viewer;

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

        // TODO: 2019-12-25 check if everything working properly, like editing, deleting etc
        TreeNode rootNode = viewModel.getParentOfTree(this, new NotesFragmentViewModel.TreeObserver() {
            @Override
            public TreeNode getParent() {
                return adapter.getParent();
            }

            @Override
            public void setParent(TreeNode parent) {
                adapter.setParent(parent);
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        adapter = new TreeAdapter(rootNode, Glide.with(this));

        adapter.setTreeAdapterListener(new TreeAdapter.TreeAdapterListener() {
            @Override
            public void onPathChanged(String newPath) {
                Animation inAnim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
                inAnim.setDuration(800);
                path.setText(newPath);
                path.startAnimation(inAnim);
            }

            @Override
            public void onGridChanged(int spanCount) {
                layoutManager.setSpanCount(spanCount);
            }
        });

        adapter.setTreeAdapterClickListener(new TreeAdapter.TreeAdapterClickListener() {
            @Override
            public void onNoteShow(TextNoteNode note) {
                TextNote textNote = RoomMapper.convertTextNote(note);
                ShowTextNoteBottomSheet bottomSheet = new ShowTextNoteBottomSheet();
                viewModel.setTextNote(textNote);
                bottomSheet.show(getFragmentManager(), "ShowTextBottomSheet");
            }

            @Override
            public void onNoteDelete(TextNoteNode note) {
                TextNote textNote = RoomMapper.convertTextNote(note);
                viewModel.deleteTextNote(textNote);
            }

            @Override
            public void onImageClick(List<String> imagePathList, Integer position, ImageView transitionImageView) {
                // TODO: 2019-12-04 fullscreen with editing and deleteing
                /*View fullscreen = getLayoutInflater().inflate(R.layout.fullscreen_image, null);
                Button btn = fullscreen.findViewById(R.id.fullscreen_btn);*/
                viewer = new StfalconImageViewer.Builder<>(getContext(), imagePathList, (imageView, imageUrl) -> {
                    Glide.with(NotesFragment.this).load(imageUrl).into(imageView);
                }).withStartPosition(position)
                        .withImageChangeListener(imagePosition -> recycler.post(() -> recycler.smoothScrollToPosition(imagePosition)))
                        //.withOverlayView(fullscreen)
                        .show();
                /*btn.setOnClickListener(view -> {
                    viewer.dismiss();
                });*/
            }

            @Override
            public void onImageLongClick(ImageNoteNode imageNoteNode) {
                ImageNote imageNote = RoomMapper.convertImageNote(imageNoteNode);
                viewModel.setImageNote(imageNote);
                ModifyImageNoteBottomSheet bottomSheet = new ModifyImageNoteBottomSheet();
                bottomSheet.show(getFragmentManager(), "ModifyImageNote");
            }
        });

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

    private void onCameraClick() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = viewModel.createImageFile();
        } catch (IOException ex) {
            ex.printStackTrace();
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
        if (resultCode == 0) {
            viewModel.deleteImage();
        } else {
            AddImageNoteBottomSheet addImageNoteBottomSheet = new AddImageNoteBottomSheet();
            addImageNoteBottomSheet.show(getFragmentManager(), "AddImageNoteBottomSheet");
        }
    }

    private void onFileClick() {
        AddTextNoteBottomSheet bottomSheet = new AddTextNoteBottomSheet();
        bottomSheet.show(getFragmentManager(), "AddTextNoteBottomSheet");
    }
}