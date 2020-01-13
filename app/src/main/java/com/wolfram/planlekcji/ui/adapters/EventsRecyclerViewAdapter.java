package com.wolfram.planlekcji.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.event.EventDisplayEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Wolfram
 * @date 2019-08-03
 */
public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.EventViewHolder>{

    private OnItemClickListener onItemClickListener;
    private List<EventDisplayEntity> eventsList;

    public interface OnItemClickListener {
        void onClick(EventDisplayEntity event);
    }

    public EventsRecyclerViewAdapter() {
        eventsList = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setEventsList(List<EventDisplayEntity> eventsList) {
        this.eventsList = eventsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventDisplayEntity eventDisplay = eventsList.get(position);

        holder.subject.setText(
                eventDisplay.getName()
        );
        holder.time.setText(
                eventDisplay.getTimeString()
        );
        holder.localization.setText(
                eventDisplay.getLocalization()
        );
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView subject;
        private TextView time;
        private TextView localization;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            this.subject = itemView.findViewById(R.id.item_event_name);
            this.time = itemView.findViewById(R.id.item_event_time);
            this.localization = itemView.findViewById(R.id.item_event_localization);
            ViewGroup container = itemView.findViewById(R.id.root_event_item);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            EventDisplayEntity event = eventsList.get(position);
            onItemClickListener.onClick(event);
        }
    }
}
