package com.wolfram.planlekcji.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.EventDisplay;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Wolfram
 * @date 2019-08-03
 */
public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.SubjectViewHolder>{

    private OnItemClickListener onItemClickListener;
    private LayoutInflater layoutInflater;
    private List<EventDisplay> eventsList;

    public interface OnItemClickListener {
        void onClick(EventDisplay event, int position);
    }

    public EventsRecyclerViewAdapter(LayoutInflater layoutInflater, OnItemClickListener onItemClickListener) {
        this.layoutInflater = layoutInflater;
        eventsList = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    public void setEventsList(List<EventDisplay> eventsList) {
        this.eventsList = eventsList;
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
        EventDisplay subject = eventsList.get(position);

        holder.subject.setText(
                subject.getName()
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
        return eventsList.size();
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView subject;
        private TextView time;
        private TextView localization;
        private ViewGroup container;
        SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            this.subject = itemView.findViewById(R.id.item_subject);
            this.time = itemView.findViewById(R.id.item_time);
            this.localization = itemView.findViewById(R.id.item_localization);
            this.container = itemView.findViewById(R.id.root_item);
            this.container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            EventDisplay event = eventsList.get(position);
            onItemClickListener.onClick(event, position);
        }
    }
}
