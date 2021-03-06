package com.wolfram.planlekcji.ui.fragments.notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.common.data.Event;
import com.wolfram.planlekcji.common.mapper.RoomMapper;
import com.wolfram.planlekcji.common.utility.DatabaseUtils;
import com.wolfram.planlekcji.common.utility.UiUtils;
import com.wolfram.planlekcji.database.room.entities.notes.SubjectWithNotesEntity;
import com.wolfram.planlekcji.database.room.entities.notes.image.ImageNoteDisplayEntity;
import com.wolfram.planlekcji.database.room.entities.notes.image.ImageNoteEntity;
import com.wolfram.planlekcji.database.room.entities.notes.text.TextNoteDisplayEntity;
import com.wolfram.planlekcji.ui.activities.CameraActivity;
import com.wolfram.planlekcji.ui.adapters.tree.ImageNoteNode;
import com.wolfram.planlekcji.ui.adapters.tree.TreeAdapter;
import com.wolfram.planlekcji.ui.adapters.tree.TreeNode;
import com.wolfram.planlekcji.database.room.entities.notes.text.TextNoteEntity;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.bottomSheets.notes.image.ModifyImageNoteBottomSheet;
import com.wolfram.planlekcji.ui.bottomSheets.notes.text.ModifyTextNoteBottomSheet;
import com.wolfram.planlekcji.ui.bottomSheets.notes.text.ShowTextNoteBottomSheet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class NotesFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.notes_recycler)
    RecyclerView recycler;
    @BindView(R.id.notes_path)
    TextView path;

    private View fullscreenImage;
    private MaterialButton backBtn;
    private MaterialButton shareBtn;
    private MaterialButton deleteBtn;
    private TextView dateTextView;
    private List<ImageNoteNode> imageList;

    private NotesFragmentViewModel viewModel;
    private TreeAdapter adapter;

    private StfalconImageViewer viewer;

    private final String NO_SUBJECTS = "Create subject first";
    private final int REQUEST_CODE = 1;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        ButterKnife.bind(this, view);

        findFullscreenLayoutViews(inflater, container);

        viewModel = ViewModelProviders.of(getActivity()).get(NotesFragmentViewModel.class);
        setHasOptionsMenu(true);

        setupBackCallback();
        getData();
        setupAdapter();
        return view;
    }

    private void findFullscreenLayoutViews(LayoutInflater inflater, ViewGroup container) {
        fullscreenImage = inflater.inflate(R.layout.fullscreen_image, container, false);
        backBtn = fullscreenImage.findViewById(R.id.edit_panel_back);
        backBtn.setOnClickListener(this);
        shareBtn = fullscreenImage.findViewById(R.id.edit_panel_share);
        shareBtn.setOnClickListener(this);
        deleteBtn = fullscreenImage.findViewById(R.id.edit_panel_delete);
        deleteBtn.setOnClickListener(this);
        dateTextView = fullscreenImage.findViewById(R.id.info_panel_date);
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
                UiUtils.showSnackBar(getActivity(), event.getValue());
                viewModel.callTextNoteMessageReceived();
            }
        });

        LiveData<Event<String>> imageNoteEvent = viewModel.getImageNoteEvent();
        imageNoteEvent.observe(this, event -> {
            if (!event.isUsed()) {
                UiUtils.showSnackBar(getActivity(), event.getValue());
                viewModel.callImageNoteMessageReceived();
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
            public void onImageClick(List<ImageNoteNode> imageNoteNodes, Integer position, ImageView transitionImageView) {
                // TODO: 2019-12-04 fullscreen with editing and deleteing
                imageList = imageNoteNodes;
                ImageNoteNode clickedImage = imageNoteNodes.get(position);
                setDateTextToLayout(clickedImage);
                List<String> imagePathList = getImagePathList(imageNoteNodes);
                if (fullscreenImage.getParent() != null) {
                    ((ViewGroup) fullscreenImage.getParent()).removeAllViews();
                }
                viewer = new StfalconImageViewer.Builder<>(getContext(), imagePathList, (imageView, imageUrl) -> {
                    Glide.with(NotesFragment.this).load(imageUrl).into(imageView);
                }).withStartPosition(position)
                        .withImageChangeListener(imagePosition -> {
                            recycler.post(() -> recycler.smoothScrollToPosition(imagePosition));
                            ImageNoteNode imageNoteNode = imageList.get(imagePosition);
                            setDateTextToLayout(imageNoteNode);
                        })
                        .withOverlayView(fullscreenImage)
                        .allowZooming(true)
                        .show();
            }

            @SuppressLint("SimpleDateFormat")
            private void setDateTextToLayout(ImageNoteNode actualImageNote) {
                Date date = actualImageNote.getDate();
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy HH:mm");
                String dateString = formatter.format(date);
                dateTextView.setText(dateString);
            }

            private List<String> getImagePathList(List<ImageNoteNode> imageNoteNodes) {
                List<String> imagePathList = new ArrayList<>();
                for (ImageNoteNode imageNoteNode : imageNoteNodes) {
                    String photoPath = imageNoteNode.getPhotoPath();
                    imagePathList.add(photoPath);
                }
                return imagePathList;
            }

            @Override
            public void onImageLongClick(ImageNoteDisplayEntity imageNote) {
                ModifyImageNoteBottomSheet bottomSheet = new ModifyImageNoteBottomSheet(imageNote);
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
                else UiUtils.showSnackBar(getActivity(), NO_SUBJECTS);
                return true;
            case R.id.notes_file:
                if (viewModel.getSubjects().size() > 0) onFileClick();
                else UiUtils.showSnackBar(getActivity(), NO_SUBJECTS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onCameraClick() {
        Intent cameraIntent = new Intent(getActivity(), CameraActivity.class);
        String photoName = viewModel.createNewPhotoString();
        cameraIntent.putExtra(CameraActivity.NAME, photoName);
        startActivityForResult(cameraIntent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            String photoPath = viewModel.getPhotoPath();
            if (resultCode == Activity.RESULT_CANCELED) {
                viewModel.deleteImage(photoPath);
            } else if (resultCode == Activity.RESULT_OK) {
                ModifyImageNoteBottomSheet modifyImageNoteBottomSheet = new ModifyImageNoteBottomSheet(photoPath);
                modifyImageNoteBottomSheet.show(getFragmentManager(), CustomBottomSheet.CREATE);
            }
        }
    }

    private void onFileClick() {
        ModifyTextNoteBottomSheet bottomSheet = new ModifyTextNoteBottomSheet();
        bottomSheet.show(getFragmentManager(), CustomBottomSheet.CREATE);
    }

    @Override
    public void onClick(View v) {
        int imagePosition = viewer.currentPosition();
        ImageNoteNode imageNoteNode = imageList.get(imagePosition);
        switch (v.getId()){
            case R.id.edit_panel_back:
                viewer.dismiss();
                break;
            case R.id.edit_panel_share:
                sharePhoto(imageNoteNode);
                break;
            case R.id.edit_panel_delete:
                deletePhoto(imageNoteNode);
                break;
        }
    }

    private void sharePhoto(ImageNoteNode imageNoteNode) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String photoPath = imageNoteNode.getPhotoPath();
        File photoFile = new File(photoPath);
        Uri imageUri = FileProvider.getUriForFile(getContext(),
                "com.example.android.fileprovider",
                photoFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpg");
        startActivity(Intent.createChooser(shareIntent, "Send image..."));
    }

    private void deletePhoto(ImageNoteNode imageNoteNode) {
        ImageNoteDisplayEntity imageNoteDisplayEntity = RoomMapper.convertImageNote(imageNoteNode);
        viewModel.deleteImageNote(imageNoteDisplayEntity);
        viewer.dismiss();
    }
}