package com.wolfram.planlekcji.adapters.tree;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.notes.Note;
import com.wolfram.planlekcji.utils.others.Utils;

import java.io.File;

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

    private TreeNode parent;
    private TreeAdapterListener treeAdapterListener;

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
        if (parent instanceof Subject) {
            treeAdapterListener.onGridChanged(2);
        } else if (parent instanceof Root) {
            treeAdapterListener.onGridChanged(1);
        } else if (parent instanceof Directory) {
            treeAdapterListener.onGridChanged(3);
        }
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
            case 1:
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
                return new TreeSubjectVH(v);
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
        } else if (node instanceof Note){
            TreeImageNoteVH imageHolder = (TreeImageNoteVH) holder;
            File imageFile = new File(((Note) node).getPhotoPath());
            Uri uri = Uri.fromFile(imageFile);
            imageHolder.image.setImageURI(uri);
            imageHolder.date.setText(Utils.getDateString(((Note) node).getDate()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        TreeNode node = parent.getChildrenList().get(position);
        if (node instanceof Subject) {
            return 1;
        } else if (node instanceof Directory) {
            return 2;
        } else if(node instanceof Note){
            return 3;
        } else {
            return super.getItemViewType(position);
        }
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
    public class TreeImageNoteVH extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView image;
        private TextView date;

        public TreeImageNoteVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.note_item_image);
            date = itemView.findViewById(R.id.note_item_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //show big photo on full screen
        }
    }
}
