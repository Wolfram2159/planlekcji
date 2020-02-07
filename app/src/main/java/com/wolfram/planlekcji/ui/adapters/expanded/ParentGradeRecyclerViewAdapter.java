package com.wolfram.planlekcji.ui.adapters.expanded;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.common.data.Group;
import com.wolfram.planlekcji.database.room.entities.grade.GradeDisplayEntity;
import com.wolfram.planlekcji.database.room.entities.grade.GradeEntity;
import com.wolfram.planlekcji.database.room.entities.grade.GradeGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * @author Wolfram
 * @date 2019-09-26
 */

public class ParentGradeRecyclerViewAdapter extends RecyclerView.Adapter<ParentGradeRecyclerViewAdapter.ViewHolder> {

    private List<? extends Group<GradeEntity>> gradeGroups;
    private ChildGradeRecyclerViewAdapter.OnChildItemClickListener onChildItemClickListener;

    public ParentGradeRecyclerViewAdapter() {
        gradeGroups = new ArrayList<>();
    }

    public void setGradeGroups(List<? extends Group<GradeEntity>> gradeGroups) {
        this.gradeGroups = gradeGroups;
        notifyDataSetChanged();
    }

    public void setOnChildItemClickListener(ChildGradeRecyclerViewAdapter.OnChildItemClickListener onChildItemClickListener) {
        this.onChildItemClickListener = onChildItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_parent_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(gradeGroups.get(position).getTitle());
        holder.setGradesList(gradeGroups.get(position).getList());
    }

    @Override
    public int getItemCount() {
        return gradeGroups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_subject_name)
        TextView title;
        @BindView(R.id.child_recycler)
        RecyclerView recyclerView;
        @BindView(R.id.item_subject_arrow)
        ImageView arrow;
        @BindView(R.id.parent_item_main_constraint_layout)
        View root;
        private ChildGradeRecyclerViewAdapter adapter;

        void setGradesList(@Nullable List<GradeEntity> gradesList) {
            adapter.setGradeList(gradesList);
        }

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(title.getContext());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new ChildGradeRecyclerViewAdapter();
            adapter.setOnChildItemClickListener(onChildItemClickListener);
            recyclerView.setVisibility(View.GONE);
            recyclerView.setAdapter(adapter);
            root.setOnClickListener(this);
        }

        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }

        @Override
        public void onClick(View view) {
            switch (recyclerView.getVisibility()) {
                case View.VISIBLE:
                    animateCollapse();
                    recyclerView.setVisibility(View.GONE);
                    notifyItemChanged(getAdapterPosition());
                    break;
                case View.INVISIBLE:
                    break;
                case View.GONE:
                    animateExpand();
                    recyclerView.setVisibility(View.VISIBLE);
                    notifyItemChanged(getAdapterPosition());
                    break;
            }
        }
    }
}
