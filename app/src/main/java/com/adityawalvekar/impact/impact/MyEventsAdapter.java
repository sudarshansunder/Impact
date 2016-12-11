package com.adityawalvekar.impact.impact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyEventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Event> events;

    public MyEventsAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (events.size() == 0) {
            return new EmptyViewHolder(LayoutInflater.from(context).inflate(R.layout.no_event, parent, false));
        } else {
            return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.event_view, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((EventViewHolder) holder).poster.setText(events.get(position).username);
        ((EventViewHolder) holder).title.setText(events.get(position).title);
        ((EventViewHolder) holder).desc.setText(events.get(position).desc);
        ((EventViewHolder) holder).location.setText(events.get(position).location);
        ((EventViewHolder) holder).date.setText(events.get(position).date);
        ImageViewBase64Loader loader = new ImageViewBase64Loader(context);
        loader.loadBitmap(events.get(position).userImage, ((EventViewHolder) holder).userImage);
        loader.loadBitmap(events.get(position).eventImage, ((EventViewHolder) holder).eventImage);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        TextView poster, date, title, desc, location;
        ImageView userImage, eventImage;
        Button attendButton;
        TextView postDate;

        public EventViewHolder(View itemView) {
            super(itemView);
            poster = (TextView) itemView.findViewById(R.id.donatePostUserName);
            date = (TextView) itemView.findViewById(R.id.eventDateTime);
            title = (TextView) itemView.findViewById(R.id.eventTitle);
            desc = (TextView) itemView.findViewById(R.id.eventDescription);
            location = (TextView) itemView.findViewById(R.id.eventLocation);
            postDate = (TextView) itemView.findViewById(R.id.dateOfPost);
            postDate.setVisibility(View.INVISIBLE);
            userImage = (ImageView) itemView.findViewById(R.id.eventPosterImage);
            eventImage = (ImageView) itemView.findViewById(R.id.eventImage);
            attendButton = (Button) itemView.findViewById(R.id.attendButton);
            attendButton.setVisibility(View.INVISIBLE);

        }
    }


}
