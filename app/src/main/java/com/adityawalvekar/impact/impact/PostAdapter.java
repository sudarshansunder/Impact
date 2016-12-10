package com.adityawalvekar.impact.impact;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
            eventPostViewHolder.eventLocation.setText(mDataSet.get(position).location);
            eventPostViewHolder.eventTitle.setText(mDataSet.get(position).title);
            eventPostViewHolder.eventCreator.setText(mDataSet.get(position).userName);
            eventPostViewHolder.eventDescription.setText(mDataSet.get(position).description);
            eventPostViewHolder.eventDateTime.setText("On " + mDataSet.get(position).dateTime.substring(0, 10));
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).type;
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView description;

        PostViewHolder(View v) {
            super(v);
            userName = (TextView) v.findViewById(R.id.eventCreator);
            description = (TextView) v.findViewById(R.id.eventDescription);
        }
    }

    class EventPostViewHolder extends RecyclerView.ViewHolder {
        TextView eventLocation;
        TextView eventCreator;
        TextView eventTitle;
        TextView eventDescription;
        ImageView eventImage;
        TextView eventDateTime;

        EventPostViewHolder(View v) {
            super(v);
            eventLocation = (TextView) v.findViewById(R.id.eventLocation);
            eventCreator = (TextView) v.findViewById(R.id.eventCreator);
            eventTitle = (TextView) v.findViewById(R.id.eventTitle);
            eventDescription = (TextView) v.findViewById(R.id.eventDescription);
            eventImage = (ImageView) v.findViewById(R.id.eventImage);
            eventDateTime = (TextView) v.findViewById(R.id.eventDateTime);
            eventImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext, android.R.style.Theme);
                    View customView = LayoutInflater.from(mContext).inflate(R.layout.fullscreenimage, null, false);
                    builder.setView(customView);
                    customView.setBackground(eventImage.getDrawable());
                    builder.setCancelable(true);
                    final AlertDialog dialog = builder.show();
                    customView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
    }
}
