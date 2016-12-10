package com.adityawalvekar.impact.impact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vvvro on 12/10/2016.
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Post> mDataSet;
    private Context mContext;

    public PostAdapter(Context context, ArrayList<Post> myDataSet) {
        mContext = context;
        mDataSet = myDataSet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_view, parent, false);
            PostAdapter.ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_view, parent, false);
            ViewHolder1 viewHolder = new ViewHolder1(v);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            PostAdapter.ViewHolder viewHolder = (PostAdapter.ViewHolder) holder;
            viewHolder.userName.setText(mDataSet.get(position).userName);
            viewHolder.description.setText(mDataSet.get(position).description);
        } else if (holder.getItemViewType() == 2) {
            PostAdapter.ViewHolder1 viewHolder1 = (PostAdapter.ViewHolder1) holder;
            viewHolder1.location.setText(mDataSet.get(position).location);
            viewHolder1.eventTitle.setText(mDataSet.get(position).title);
            viewHolder1.eventCreator.setText(mDataSet.get(position).userName);
            viewHolder1.eventDescription.setText(mDataSet.get(position).description);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView description;

        public ViewHolder(View v) {
            super(v);
            userName = (TextView) v.findViewById(R.id.userName);
            description = (TextView) v.findViewById(R.id.description);
        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        public TextView location;
        public TextView eventCreator;
        public TextView eventTitle;
        public TextView eventDescription;

        public ViewHolder1(View v) {
            super(v);
            location = (TextView) v.findViewById(R.id.location);
            eventCreator = (TextView) v.findViewById(R.id.eventCreator);
            eventTitle = (TextView) v.findViewById(R.id.eventTitle);
            eventDescription = (TextView) v.findViewById(R.id.eventDescription);
        }
    }
}
