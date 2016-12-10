package com.adityawalvekar.impact.impact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Post> mDataSet;
    private Context mContext;

    PostAdapter(Context context, ArrayList<Post> myDataSet) {
        mContext = context;
        mDataSet = myDataSet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_view, parent, false);
            return new PostViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_view, parent, false);
            return new EventPostViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            PostViewHolder postViewHolder = (PostViewHolder) holder;
            postViewHolder.userName.setText(mDataSet.get(position).userName);
            postViewHolder.description.setText(mDataSet.get(position).description);
        } else if (holder.getItemViewType() == 2) {
            EventPostViewHolder eventPostViewHolder = (EventPostViewHolder) holder;
            eventPostViewHolder.location.setText(mDataSet.get(position).location);
            eventPostViewHolder.eventTitle.setText(mDataSet.get(position).title);
            eventPostViewHolder.eventCreator.setText(mDataSet.get(position).userName);
            eventPostViewHolder.eventDescription.setText(mDataSet.get(position).description);
        }
    }

    @Override
    public int getItemCount() {
        Log.v("PostAdapter", "Number of items is " + String.valueOf(mDataSet.size()));
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).type;
    }

    private static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView description;

        PostViewHolder(View v) {
            super(v);
            userName = (TextView) v.findViewById(R.id.eventCreator);
            description = (TextView) v.findViewById(R.id.eventDescription);
        }
    }

    static class EventPostViewHolder extends RecyclerView.ViewHolder {
        TextView location;
        TextView eventCreator;
        TextView eventTitle;
        TextView eventDescription;

        EventPostViewHolder(View v) {
            super(v);
            location = (TextView) v.findViewById(R.id.location);
            eventCreator = (TextView) v.findViewById(R.id.eventCreator);
            eventTitle = (TextView) v.findViewById(R.id.eventTitle);
            eventDescription = (TextView) v.findViewById(R.id.eventDescription);
        }
    }
}
