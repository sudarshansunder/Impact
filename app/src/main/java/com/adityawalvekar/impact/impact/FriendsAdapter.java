package com.adityawalvekar.impact.impact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vvvro on 12/10/2016.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    ArrayList<Friends> mDataSet;
    Context mContext;

    public FriendsAdapter(){

    }

    public FriendsAdapter(Context myContext, ArrayList<Friends> myDataSet){
        mContext = myContext;
        mDataSet = myDataSet;
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder{
        TextView friendsName;
        Button followButton;
        CircleImageView friendsImage;
        FriendsViewHolder(View v){
            super(v);
            friendsName = (TextView) v.findViewById(R.id.friendsName);
            followButton = (Button) v.findViewById(R.id.follow);
            friendsImage = (CircleImageView) v.findViewById(R.id.friendsImage);
        }
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        holder.friendsName.setText(mDataSet.get(position).fullname);
        if(mDataSet.get(position).following==true){
            holder.followButton.setText("Following");
            holder.followButton.setEnabled(false);
        }else{
            holder.followButton.setText("Follow");
            holder.followButton.setEnabled(true);
        }
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.person_follow_view, parent, false);
        FriendsViewHolder viewHolder = new FriendsViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
