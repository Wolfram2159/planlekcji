package com.wolfram.planlekcji.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.ui.activities.SubjectsViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Wolfram
 * @date 2019-08-03
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>{

    private onDeleted onDeleted;
    private LayoutInflater layoutInflater;
    private List<Subject> subjectsList;
    private SubjectsViewModel viewModel;

    public interface onDeleted{
        void delete(Subject s, int position);
    }

    public SubjectAdapter(LayoutInflater layoutInflater, SubjectsViewModel viewModel, onDeleted onDeleted) {
        this.layoutInflater = layoutInflater;
        subjectsList = new ArrayList<>();
        this.viewModel = viewModel;
        this.onDeleted = onDeleted;
    }

    public void setSubjectsList(List<Subject> subjectsList) {
        this.subjectsList = subjectsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.subject_item, parent, false);
        return new SubjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjectsList.get(position);

        holder.subject.setText(
                subject.getSubject()
        );
        holder.time.setText(
                subject.getTimeString()
        );
        holder.localization.setText(
                subject.getLocalization()
        );
    }

    @Override
    public int getItemCount() {
        return subjectsList.size();
    }

    public void deleteItem(int position){
        Subject subject = subjectsList.get(position);
        subjectsList.remove(position);
        notifyItemRemoved(position);
        viewModel.deleteSubject(subject);
        onDeleted.delete(subject, position);
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView subject;
        private TextView time;
        private TextView localization;
        private ViewGroup container;
        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            this.subject = itemView.findViewById(R.id.item_subject);
            this.time = itemView.findViewById(R.id.item_time);
            this.localization = itemView.findViewById(R.id.item_localization);
            this.container = itemView.findViewById(R.id.root_item);
            this.container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Subject s = subjectsList.get(this.getAdapterPosition());
        }
    }
}
