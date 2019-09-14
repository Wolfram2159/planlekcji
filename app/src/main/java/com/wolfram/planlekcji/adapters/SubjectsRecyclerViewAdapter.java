package com.wolfram.planlekcji.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Wolfram
 * @date 2019-09-14
 */
public class SubjectsRecyclerViewAdapter extends RecyclerView.Adapter<SubjectsRecyclerViewAdapter.SubjectsViewHolder> {

    private List<Subject> subjectList;
    private LayoutInflater layoutInflater;

    public SubjectsRecyclerViewAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        this.subjectList = new ArrayList<>();
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.subject_item, parent, false);
        return new SubjectsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectsViewHolder holder, int position) {
        Subject subject = subjectList.get(position);

        holder.textView.setText(subject.getName());
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class SubjectsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textView;

        public SubjectsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.item_subject_name);
            ViewGroup container = itemView.findViewById(R.id.root_subject_item);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
