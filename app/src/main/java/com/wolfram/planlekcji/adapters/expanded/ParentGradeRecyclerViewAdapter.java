package com.wolfram.planlekcji.adapters.expanded;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.grade.GradeDisplay;
import com.wolfram.planlekcji.database.room.entities.grade.GradeGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Wolfram
 * @date 2019-09-26
 */

public class ParentGradeRecyclerViewAdapter extends RecyclerView.Adapter<ParentGradeRecyclerViewAdapter.ViewHolder> {

    private List<GradeGroup> gradeGroups;

    public ParentGradeRecyclerViewAdapter() {
        gradeGroups = new ArrayList<>();
    }


    public void setGradeGroups(List<GradeGroup> gradeGroups) {
        this.gradeGroups = gradeGroups;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(gradeGroups.get(position).getTitle());
        holder.setGradesList(gradeGroups.get(position).getGradeList());
    }

    @Override
    public int getItemCount() {
        return gradeGroups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private RecyclerView recyclerView;
        private ChildGradeRecyclerViewAdapter adapter;

        void setGradesList(List<GradeDisplay> gradesList) {
            adapter.setGradeList(gradesList);
        }

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_subject_name);
            recyclerView = itemView.findViewById(R.id.child_recycler);
            LinearLayoutManager layoutManager = new LinearLayoutManager(title.getContext());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new ChildGradeRecyclerViewAdapter();
            recyclerView.setVisibility(View.GONE);
            recyclerView.setAdapter(adapter);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (recyclerView.getVisibility()) {
                case View.VISIBLE:
                    recyclerView.setVisibility(View.GONE);
                    notifyItemChanged(getAdapterPosition());
                    break;
                case View.INVISIBLE:
                    break;
                case View.GONE:
                    recyclerView.setVisibility(View.VISIBLE);
                    notifyItemChanged(getAdapterPosition());
                    break;
            }
        }
    }
}
