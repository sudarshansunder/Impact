package com.adityawalvekar.impact.impact;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Post> mDataSet;
    private Context mContext;
    private RequestQueue queue;

    PostAdapter(Context context, ArrayList<Post> myDataSet) {
        mContext = context;
        mDataSet = myDataSet;
        queue = Volley.newRequestQueue(context);
    }

    private Bitmap decodeImage(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_view, parent, false);
            return new PostViewHolder(v);
        } else if (viewType == 2) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_view, parent, false);
            return new EventPostViewHolder(v);
        } else if (viewType == 3) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.donate_view, parent, false);
            return new DonatePostViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            PostViewHolder postViewHolder = (PostViewHolder) holder;
            postViewHolder.userName.setText(mDataSet.get(position).userName);
            postViewHolder.description.setText(mDataSet.get(position).description);
            postViewHolder.dateTime.setText(mDataSet.get(position).dateTime);
            //postViewHolder.userImage.setImageBitmap(decodeImage(mDataSet.get(position).userImage));
            ImageViewBase64Loader imageViewBase64Loader = new ImageViewBase64Loader(mContext);
            imageViewBase64Loader.loadBitmap(mDataSet.get(position).userImage, postViewHolder.userImage);
        } else if (holder.getItemViewType() == 2) {
            EventPostViewHolder eventPostViewHolder = (EventPostViewHolder) holder;
            eventPostViewHolder.eventLocation.setText(mDataSet.get(position).location);
            eventPostViewHolder.eventTitle.setText(mDataSet.get(position).title);
            eventPostViewHolder.eventCreator.setText(mDataSet.get(position).userName);
            eventPostViewHolder.eventDescription.setText(mDataSet.get(position).description);
            eventPostViewHolder.eventDateTime.setText(mDataSet.get(position).dateTime);
            //eventPostViewHolder.userImage.setImageBitmap(decodeImage(mDataSet.get(position).userImage));
            //eventPostViewHolder.eventImage.setImageBitmap(decodeImage(mDataSet.get(position).eventImage));
            ImageViewBase64Loader imageViewBase64Loader = new ImageViewBase64Loader(mContext);
            imageViewBase64Loader.loadBitmap(mDataSet.get(position).userImage, eventPostViewHolder.userImage);
            imageViewBase64Loader.loadBitmap(mDataSet.get(position).eventImage, eventPostViewHolder.eventImage);
        } else if (holder.getItemViewType() == 3) {
            DonatePostViewHolder donatePostViewHolder = (DonatePostViewHolder) holder;
            donatePostViewHolder.userName.setText(mDataSet.get(position).userName);
            //donatePostViewHolder.donateImage.setImageBitmap(decodeImage(mDataSet.get(position).userImage));
            //donatePostViewHolder.donateUserImage.setImageBitmap(decodeImage(mDataSet.get(position).eventImage));
            ImageViewBase64Loader imageViewBase64Loader = new ImageViewBase64Loader(mContext);
            imageViewBase64Loader.loadBitmap(mDataSet.get(position).userImage, donatePostViewHolder.donateImage);
            imageViewBase64Loader.loadBitmap(mDataSet.get(position).eventImage, donatePostViewHolder.donateUserImage);
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
        TextView dateTime;
        ImageView userImage;

        PostViewHolder(View v) {
            super(v);
            userName = (TextView) v.findViewById(R.id.postUserName);
            description = (TextView) v.findViewById(R.id.postDescription);
            dateTime = (TextView) v.findViewById(R.id.dateOfPost);
            userImage = (ImageView) v.findViewById(R.id.postUserImage);
        }
    }

    class EventPostViewHolder extends RecyclerView.ViewHolder {
        TextView eventLocation;
        TextView eventCreator;
        TextView eventTitle;
        TextView eventDescription;
        ImageView eventImage;
        TextView eventDateTime;
        ImageView userImage;
        Button attendEvent;

        EventPostViewHolder(View v) {
            super(v);
            eventLocation = (TextView) v.findViewById(R.id.eventLocation);
            eventCreator = (TextView) v.findViewById(R.id.donatePostUserName);
            eventTitle = (TextView) v.findViewById(R.id.eventTitle);
            eventDescription = (TextView) v.findViewById(R.id.eventDescription);
            eventImage = (ImageView) v.findViewById(R.id.eventImage);
            userImage = (ImageView) v.findViewById(R.id.eventPosterImage);
            eventDateTime = (TextView) v.findViewById(R.id.eventDateTime);
            attendEvent = (Button) v.findViewById(R.id.attendButton);

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

    class DonatePostViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        ImageView donateImage, donateUserImage;

        DonatePostViewHolder(View v) {
            super(v);
            userName = (TextView) v.findViewById(R.id.donatePostUserName);
            donateImage = (ImageView) v.findViewById(R.id.donateCompanyImage);
            donateUserImage = (ImageView) v.findViewById(R.id.donatePostUserImage);
        }
    }
}
