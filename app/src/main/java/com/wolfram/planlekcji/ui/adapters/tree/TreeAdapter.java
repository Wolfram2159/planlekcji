package com.wolfram.planlekcji.ui.adapters.tree;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.common.mapper.RoomMapper;
import com.wolfram.planlekcji.common.others.DateUtils;
import com.wolfram.planlekcji.database.room.entities.notes.TextNoteDisplayEntity;
import com.wolfram.planlekcji.database.room.entities.notes.TextNoteEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Wolfram
 * @date 2019-10-14
 */
public class TreeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface TreeChangedListener {
        void onParentChanged(TreeNode parent);
        void onGridChanged(int spanCount);
    }

    public interface TreeAdapterClickListener {
        void onNoteShow(TextNoteDisplayEntity note);
        void onNoteDelete(TextNoteEntity note);
        void onImageClick(List<String> imagePathList, Integer position, ImageView transitionImage);
        void onImageLongClick(ImageNoteNode imageNote);
    }

    public interface TreeAdapterParent {
        int getViewType();
        int getGridSpanCount();
    }

    private TreeNode parent;
    private TreeChangedListener treeChangedListener;
    private TreeAdapterClickListener treeAdapterClickListener;
    private RequestManager glide;
    private static final int TEXT_NOTE_NODE_VIEW_TYPE = 1;
    private static final int DIRECTORY_NODE_VIEW_TYPE = 2;
    private static final int IMAGE_NOTE_NODE_VIEW_TYPE = 3;
    private static final int SUBJECT_NODE_VIEW_TYPE = 4;

    public TreeAdapter(TreeNode parent, RequestManager glide) {
        this.parent = parent;
        this.glide = glide;
    }

    public void setTreeAdapterClickListener(TreeAdapterClickListener treeAdapterClickListener) {
        this.treeAdapterClickListener = treeAdapterClickListener;
    }

    public void setTreeChangedListener(TreeChangedListener treeChangedListener) {
        this.treeChangedListener = treeChangedListener;
        this.treeChangedListener.onParentChanged(parent);
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
        notifyDataSetChanged();
    }

    public void onBackPressed() {
        if (parent.getParent() != null) {
            parent = parent.getParent();
            checkParentInstanceOf();
            treeChangedListener.onParentChanged(parent);
            notifyDataSetChanged();
        }
    }

    private void checkParentInstanceOf() {
        int gridSpanCount = parent.getGridSpanCount();
        treeChangedListener.onGridChanged(gridSpanCount);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case IMAGE_NOTE_NODE_VIEW_TYPE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_image_item, parent, false);
                return new TreeImageNoteVH(v);
            case DIRECTORY_NODE_VIEW_TYPE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.directory_item, parent, false);
                return new TreeDirectoryVH(v);
            case SUBJECT_NODE_VIEW_TYPE:
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
                return new TreeSubjectVH(v);
            case TEXT_NOTE_NODE_VIEW_TYPE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_text_item, parent, false);
                return new TreeTextNoteVH(v);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TreeNode node = parent.getChildrenList().get(position);
        if (node instanceof SubjectNode) {
            TreeSubjectVH subjectHolder = (TreeSubjectVH) holder;
            subjectHolder.subject_tv.setText(node.getNodeName());
        } else if (node instanceof DirectoryNode) {
            TreeDirectoryVH directoryHolder = (TreeDirectoryVH) holder;
            directoryHolder.directory_tv.setText(node.getNodeName());
        } else if (node instanceof ImageNoteNode) {
            TreeImageNoteVH imageHolder = (TreeImageNoteVH) holder;
            String photoPath = ((ImageNoteNode) node).getPhotoPath();
            glide
                    .load(photoPath)
                    .centerCrop()
                    .into(imageHolder.image);
            imageHolder.date.setText(DateUtils.getDateString(((ImageNoteNode) node).getDate()));
        } else if (node instanceof TextNoteNode) {
            TreeTextNoteVH textHolder = (TreeTextNoteVH) holder;
            textHolder.title.setText(((TextNoteNode) node).getTitle());
            textHolder.date.setText(DateUtils.getDateString(((TextNoteNode) node).getDate()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        TreeNode node = parent.getChildrenList().get(position);
        return node.getViewType();
    }

    @Override
    public int getItemCount() {
        return parent.getChildrenList().size();
    }

    public class TreeSubjectVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView subject_tv;

        public TreeSubjectVH(@NonNull View itemView) {
            super(itemView);
            subject_tv = itemView.findViewById(R.id.item_subject_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPos = getAdapterPosition();
            setNewParent(adapterPos);
        }
    }

    public class TreeDirectoryVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView directory_tv;

        public TreeDirectoryVH(@NonNull View itemView) {
            super(itemView);
            directory_tv = itemView.findViewById(R.id.item_directory_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPos = getAdapterPosition();
            setNewParent(adapterPos);
        }
    }

    private void setNewParent(int adapterPosition) {
        TreeNode newParent = parent.getChildrenList().get(adapterPosition);
        if (newParent.getChildrenList() != null) {
            parent = newParent;
            checkParentInstanceOf();
            treeChangedListener.onParentChanged(parent);
            notifyDataSetChanged();
        }
    }

    public class TreeImageNoteVH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // TODO: 2019-11-17 how to delete imageNote ?
        private ImageView image;
        private TextView date;

        public TreeImageNoteVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.note_image_item_image);
            date = itemView.findViewById(R.id.note_image_item_date);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            List<String> pathList = new ArrayList<>();
            for (TreeNode treeNode : parent.getChildrenList()) {
                ImageNoteNode imageNote = (ImageNoteNode) treeNode;
                pathList.add(imageNote.getPhotoPath());
            }
            treeAdapterClickListener.onImageClick(pathList, getAdapterPosition(), image);
        }

        @Override
        public boolean onLongClick(View view) {
            ImageNoteNode imageNote = (ImageNoteNode) parent.getChildrenList().get(getAdapterPosition());
            treeAdapterClickListener.onImageLongClick(imageNote);
            return true;
        }
    }

    public class TreeTextNoteVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView date;

        public TreeTextNoteVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.note_text_item_title);
            date = itemView.findViewById(R.id.note_text_item_date);
            MaterialButton show = itemView.findViewById(R.id.note_text_item_btn_show);
            MaterialButton delete = itemView.findViewById(R.id.note_text_item_btn_delete);
            show.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            TextNoteNode note = (TextNoteNode) parent.getChildrenList().get(getAdapterPosition());
            TextNoteDisplayEntity textNoteEntity = RoomMapper.convertTextNote(note);
            switch (view.getId()) {
                case R.id.note_text_item_btn_show:
                    treeAdapterClickListener.onNoteShow(textNoteEntity);
                    break;
                case R.id.note_text_item_btn_delete:
                    treeAdapterClickListener.onNoteDelete(textNoteEntity);
                    break;
                default:
                    break;
            }
        }
    }
}
