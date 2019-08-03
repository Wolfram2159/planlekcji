package com.wolfram.planlekcji.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Wolfram
 * @date 2019-08-03
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>{

    private LayoutInflater layoutInflater;
    private List<Subject> subjectsList;

    public SubjectAdapter(LayoutInflater layoutInflater, List<Subject> subjectsList) {
        this.layoutInflater = layoutInflater;
        this.subjectsList = subjectsList;
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
