package com.wolfram.planlekcji.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.event.EventDisplay;

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
    private LayoutInflater layoutInflater;
    private List<EventDisplay> eventsList;

    public interface OnItemClickListener {
        void onClick(EventDisplay event);
    }

    public EventsRecyclerViewAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        eventsList = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setEventsList(List<EventDisplay> eventsList) {
        this.eventsList = eventsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
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
            EventDisplay event = eventsList.get(position);
            onItemClickListener.onClick(event);
        }
    }
}
