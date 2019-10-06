package com.wolfram.planlekcji.adapters.expanded;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.grade.GradeDisplay;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Wolfram
 * @date 2019-09-26
 */
public class ChildGradeRecyclerViewAdapter extends RecyclerView.Adapter<ChildGradeRecyclerViewAdapter.ViewHolder> {

    private List<GradeDisplay> gradeList;

    ChildGradeRecyclerViewAdapter() {
        this.gradeList = new ArrayList<>();
    }

    void setGradeList(List<GradeDisplay> gradeList) {
        this.gradeList = gradeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.description.setText(gradeList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView description;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.item_grade_description);
        }
    }
}
