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
import com.wolfram.planlekcji.common.data.Event;
import com.wolfram.planlekcji.common.mapper.RoomMapper;
import com.wolfram.planlekcji.common.others.SnackbarUtils;
import com.wolfram.planlekcji.database.room.entities.notes.SubjectWithNotesEntity;
import com.wolfram.planlekcji.database.room.entities.notes.TextNoteDisplayEntity;
import com.wolfram.planlekcji.ui.adapters.tree.ImageNoteNode;
import com.wolfram.planlekcji.ui.adapters.tree.TreeAdapter;
import com.wolfram.planlekcji.ui.adapters.tree.TreeNode;
import com.wolfram.planlekcji.database.room.entities.notes.ImageNoteEntity;
import com.wolfram.planlekcji.database.room.entities.notes.TextNoteEntity;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.bottomSheets.notes.AddImageNoteBottomSheet;
import com.wolfram.planlekcji.ui.bottomSheets.notes.ModifyTextNoteBottomSheet;
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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesFragment extends Fragment {

    @BindView(R.id.notes_recycler)
    RecyclerView recycler;
    @BindView(R.id.notes_path)
    TextView path;

    private NotesFragmentViewModel viewModel;
    private TreeAdapter adapter;

    private StfalconImageViewer viewer;

    private static final int REQUEST_TAKE_PHOTO = 1;
    private final String NO_SUBJECTS = "Create subject first";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        ButterKnife.bind(this, view);

        viewModel = ViewModelProviders.of(getActivity()).get(NotesFragmentViewModel.class);
        setHasOptionsMenu(true);

        setupBackCallback();
        getData();
        setupAdapter();
        return view;
    }

    private void setupBackCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                adapter.onBackPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void getData() {
        NotesFragmentViewModel.ParentSetter parentSetter = parent -> adapter.setParent(parent);
        viewModel.setParentSetter(parentSetter);

        LiveData<List<SubjectWithNotesEntity>> subjectWithNotesList = viewModel.getSubjectWithNotesList();
        subjectWithNotesList.observe(this, viewModel::setSubjectsWithNotes);

        LiveData<Event<String>> textNoteEvent = viewModel.getTextNoteEvent();
        textNoteEvent.observe(this, event -> {
            if (!event.isUsed()) {
                SnackbarUtils.showSnackBar(getActivity(), event.getValue());
                viewModel.callMessageReceived();
            }
        });
    }

    private void setupAdapter() {
        TreeNode initialRoot = viewModel.getActualParent();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), initialRoot.getGridSpanCount());
        adapter = new TreeAdapter(initialRoot, Glide.with(this));

        TreeAdapter.TreeChangedListener adapterListener = new TreeAdapter.TreeChangedListener() {
            @Override
            public void onParentChanged(TreeNode parent) {
                String newPath = parent.getPath();
                Animation inAnim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
                inAnim.setDuration(800);
                path.setText(newPath);
                path.startAnimation(inAnim);
                viewModel.setActualParent(parent);
            }

            @Override
            public void onGridChanged(int spanCount) {
                layoutManager.setSpanCount(spanCount);
            }
        };

        TreeAdapter.TreeAdapterClickListener clickListener = new TreeAdapter.TreeAdapterClickListener() {
            @Override
            public void onNoteShow(TextNoteDisplayEntity note) {
                ShowTextNoteBottomSheet bottomSheet = new ShowTextNoteBottomSheet(note);
                bottomSheet.show(getFragmentManager(), CustomBottomSheet.ACTION);
            }

            @Override
            public void onNoteDelete(TextNoteEntity textNote) {
                viewModel.deleteTextNote(textNote);
            }

            @Override
            public void onImageClick(List<String> imagePathList, Integer position, ImageView transitionImageView) {
                // TODO: 2019-12-04 fullscreen with editing and deleteing
                viewer = new StfalconImageViewer.Builder<>(getContext(), imagePathList, (imageView, imageUrl) -> {
                    Glide.with(NotesFragment.this).load(imageUrl).into(imageView);
                }).withStartPosition(position)
                        .withImageChangeListener(imagePosition -> recycler.post(() -> recycler.smoothScrollToPosition(imagePosition)))
                        //.withOverlayView(fullscreen)
                        .show();
            }

            @Override
            public void onImageLongClick(ImageNoteNode imageNoteNode) {
                ImageNoteEntity imageNote = RoomMapper.convertImageNote(imageNoteNode);
                viewModel.setImageNote(imageNote);
                ModifyImageNoteBottomSheet bottomSheet = new ModifyImageNoteBottomSheet();
                bottomSheet.show(getFragmentManager(), CustomBottomSheet.MODIFY);
            }
        };

        adapter.setTreeChangedListener(adapterListener);
        adapter.setTreeAdapterClickListener(clickListener);
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
                if (viewModel.getSubjects().size() > 0) onCameraClick();
                else SnackbarUtils.showSnackBar(getActivity(), NO_SUBJECTS);
                return true;
            case R.id.notes_file:
                if (viewModel.getSubjects().size() > 0) onFileClick();
                else SnackbarUtils.showSnackBar(getActivity(), NO_SUBJECTS);
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
            addImageNoteBottomSheet.show(getFragmentManager(), CustomBottomSheet.CREATE);
        }
    }

    private void onFileClick() {
        ModifyTextNoteBottomSheet bottomSheet = new ModifyTextNoteBottomSheet();
        bottomSheet.show(getFragmentManager(), CustomBottomSheet.CREATE);
    }
}