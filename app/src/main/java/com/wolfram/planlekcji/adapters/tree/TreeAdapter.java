package com.wolfram.planlekcji.adapters.tree;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Wolfram
 * @date 2019-10-14
 */
public class TreeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnPathChangedListener{
        void onPathChanged(String newPath);
    }

    private TreeNode parent;
    private OnPathChangedListener onPathChangedListener;

    public void setOnPathChangedListener(OnPathChangedListener onPathChangedListener) {
        this.onPathChangedListener = onPathChangedListener;
        this.onPathChangedListener.onPathChanged(parent.getPath());
    }

    public TreeAdapter(TreeNode parent) {
        this.parent = parent;
        //onPathChangedListener.onPathChanged(parent.getPath());
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
        notifyDataSetChanged();
    }

    public void onBackPressed(){
        if (parent.getParent() != null){
            parent = parent.getParent();
            onPathChangedListener.onPathChanged(parent.getPath());
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
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
        }
    }

    @Override
    public int getItemViewType(int position) {
        TreeNode node = parent.getChildrenList().get(position);
        if (node instanceof Subject) {
            return 1;
        } else if (node instanceof Directory) {
            return 2;
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
                onPathChangedListener.onPathChanged(parent.getPath());
                notifyDataSetChanged();
            }
        }
    }

    public class TreeDirectoryVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView directory_tv;

        public TreeDirectoryVH(@NonNull View itemView) {
            super(itemView);
            directory_tv = itemView.findViewById(R.id.directory_item_node_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            TreeNode newParent = parent.getChildrenList().get(getAdapterPosition());
            if (newParent.getChildrenList() != null) {
                parent = newParent;
                onPathChangedListener.onPathChanged(parent.getPath());
                notifyDataSetChanged();
            }
        }
    }
}
