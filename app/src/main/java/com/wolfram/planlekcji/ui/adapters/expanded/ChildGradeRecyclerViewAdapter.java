package com.wolfram.planlekcji.ui.adapters.expanded;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.grade.GradeDisplayEntity;
import com.wolfram.planlekcji.common.others.DateUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Wolfram
 * @date 2019-09-26
 */
public class ChildGradeRecyclerViewAdapter extends RecyclerView.Adapter<ChildGradeRecyclerViewAdapter.ViewHolder> {

    public interface OnChildItemClickListener {
        void onClick(GradeDisplayEntity g);
    }

    private List<GradeDisplayEntity> gradeList;
    private OnChildItemClickListener onChildItemClickListener;

    ChildGradeRecyclerViewAdapter() {
        this.gradeList = new ArrayList<>();
    }

    void setGradeList(List<GradeDisplayEntity> gradeList) {
        this.gradeList = gradeList;
        notifyDataSetChanged();
    }

    void setOnChildItemClickListener(OnChildItemClickListener onChildItemClickListener) {
        this.onChildItemClickListener = onChildItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_child_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GradeDisplayEntity g = gradeList.get(position);
        holder.description.setText(g.getDescription());
        String date = DateUtils.getDateString(g.getDate());
        holder.date.setText(date);
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView description;
        private TextView date;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.item_grade_description);
            date = itemView.findViewById(R.id.item_grade_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            GradeDisplayEntity g = gradeList.get(position);
            onChildItemClickListener.onClick(g);
        }
    }
}
