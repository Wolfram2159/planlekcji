package com.wolfram.planlekcji.adapters.tree;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.notes.ImageNote;
import com.wolfram.planlekcji.database.room.entities.notes.TextNote;
import com.wolfram.planlekcji.utils.others.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Wolfram
 * @date 2019-10-14
 */
public class TreeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface TreeAdapterListener {
        void onPathChanged(String newPath);
        void onGridChanged(int spanCount);
    }
    public interface TreeAdapterClickListener {
        void onNoteShow(TextNote note);
        void onNoteDelete(TextNote note);
        void onImageClick(List<String> imagePathList, Integer position, ImageView transitionImage);
    }

    public interface TreeAdapterParent{
        int getViewType();
        int getGridSpanCount();
    }

    private TreeNode parent;
    private TreeAdapterListener treeAdapterListener;
    private TreeAdapterClickListener treeAdapterClickListener;

    public void setTreeAdapterClickListener(TreeAdapterClickListener treeAdapterClickListener) {
        this.treeAdapterClickListener = treeAdapterClickListener;
    }

    public void setTreeAdapterListener(TreeAdapterListener treeAdapterListener) {
        this.treeAdapterListener = treeAdapterListener;
        this.treeAdapterListener.onPathChanged(parent.getPath());
    }

    public TreeAdapter(TreeNode parent) {
        this.parent = parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
        notifyDataSetChanged();
    }

    public TreeNode getParent() {
        return parent;
    }

    public void onBackPressed() {
        if (parent.getParent() != null) {
            parent = parent.getParent();
            checkParentInstanceOf();
            treeAdapterListener.onPathChanged(parent.getPath());
            notifyDataSetChanged();
        }
    }

    private void checkParentInstanceOf() {
        int gridSpanCount = parent.getGridSpanCount();
        treeAdapterListener.onGridChanged(gridSpanCount);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case 3:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_image_item, parent, false);
                return new TreeImageNoteVH(v);
            case 2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.directory_item, parent, false);
                return new TreeDirectoryVH(v);
            case 4:
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
                return new TreeSubjectVH(v);
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_text_item, parent, false);
                return new TreeTextNoteVH(v);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TreeNode node = parent.getChildrenList().get(position);
        if (node instanceof Subject) {
            TreeSubjectVH subjectHolder = (TreeSubjectVH) holder;
            subjectHolder.subject_tv.setText(node.getNodeName());
        } else if (node instanceof Directory) {
            TreeDirectoryVH directoryHolder = (TreeDirectoryVH) holder;
            directoryHolder.directory_tv.setText(node.getNodeName());
        } else if (node instanceof ImageNote) {
            TreeImageNoteVH imageHolder = (TreeImageNoteVH) holder;
            File imageFile = new File(((ImageNote) node).getPhotoPath());
            Uri uri = Uri.fromFile(imageFile);
            imageHolder.image.setImageURI(uri);
            imageHolder.date.setText(Utils.getDateString(((ImageNote) node).getDate()));
        } else if (node instanceof TextNote){
            TreeTextNoteVH textHolder = (TreeTextNoteVH) holder;
            textHolder.title.setText(((TextNote) node).getTitle());
            textHolder.date.setText(Utils.getDateString(((TextNote) node).getDate()));
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
            TreeNode newParent = parent.getChildrenList().get(getAdapterPosition());
            if (newParent.getChildrenList() != null) {
                parent = newParent;
                checkParentInstanceOf();
                treeAdapterListener.onPathChanged(parent.getPath());
                notifyDataSetChanged();
            }
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
            TreeNode newParent = parent.getChildrenList().get(getAdapterPosition());
            if (newParent.getChildrenList() != null) {
                parent = newParent;
                checkParentInstanceOf();
                treeAdapterListener.onPathChanged(parent.getPath());
                notifyDataSetChanged();
            }
        }
    }

    public class TreeImageNoteVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        // TODO: 2019-11-17 how to delete imageNote ?
        private ImageView image;
        private TextView date;

        public TreeImageNoteVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.note_image_item_image);
            date = itemView.findViewById(R.id.note_image_item_date);
            itemView.setOnClickListener(this);
        }

        public ImageView getImage() {
            return image;
        }

        @Override
        public void onClick(View view) {
            List<String> pathList = new ArrayList<>();
            for (TreeNode treeNode : parent.getChildrenList()) {
                ImageNote imageNote = (ImageNote) treeNode;
                pathList.add(imageNote.getPhotoPath());
            }
            treeAdapterClickListener.onImageClick(pathList, getAdapterPosition(), image);
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
            TextNote note = (TextNote) parent.getChildrenList().get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.note_text_item_btn_show:
                    treeAdapterClickListener.onNoteShow(note);
                    break;
                case R.id.note_text_item_btn_delete:
                    treeAdapterClickListener.onNoteDelete(note);
                    break;
                default:
                    break;
            }
        }
    }
}
