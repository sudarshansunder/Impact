package com.adityawalvekar.impact.impact;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vvvro on 12/10/2016.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    ArrayList<Friends> mDataSet;
    Context mContext;

    public FriendsAdapter() {

    }

    public FriendsAdapter(Context myContext, ArrayList<Friends> myDataSet) {
        mContext = myContext;
        mDataSet = myDataSet;
    }

    @Override
    public void onBindViewHolder(final FriendsViewHolder holder, final int position) {
        holder.friendsName.setText(mDataSet.get(position).fullname);
        ImageViewBase64Loader loader = new ImageViewBase64Loader(mContext);
        loader.loadBitmap(mDataSet.get(position).image, holder.friendsImage);
        if (mDataSet.get(holder.getAdapterPosition()).following)
            holder.followButton.setText("Unfollow");
        else
            holder.followButton.setText("Follow");
        holder.followButton.setOnClickListener(null);
        if (mDataSet.get(position).following) {
            holder.followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Value of following for " + mDataSet.get(position).fullname, mDataSet.get(position).following + "");
                    RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                    requestQueue.cancelAll(mContext);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://impact.adityawalvekar.com/unfollow",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("Unfollowing " + mDataSet.get(holder.getAdapterPosition()).fullname, response);
                                    holder.followButton.setText("Follow");
                                    mDataSet.get(holder.getAdapterPosition()).following = false;
                                    notifyDataSetChanged();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v("FriendsAdapter", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            SharedPreferences sharedPreferences = mContext.getSharedPreferences("user_data", Context.MODE_PRIVATE);
                            String userName = sharedPreferences.getString("username", "");
                            hashMap.put("username", userName);
                            hashMap.put("follows", mDataSet.get(position).username);
                            return hashMap;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            });
        } else {
            Log.d("Value of following for " + mDataSet.get(position).fullname, mDataSet.get(position).following + "");
            holder.followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                    requestQueue.cancelAll(mContext);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://impact.adityawalvekar.com/follow",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("Following " + mDataSet.get(holder.getAdapterPosition()).fullname, response);
                                    holder.followButton.setText("Unfollow");
                                    mDataSet.get(holder.getAdapterPosition()).following = true;
                                    notifyDataSetChanged();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v("FriendsAdapter", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            SharedPreferences sharedPreferences = mContext.getSharedPreferences("user_data", Context.MODE_PRIVATE);
                            String userName = sharedPreferences.getString("username", "");
                            hashMap.put("username", userName);
                            hashMap.put("follows", mDataSet.get(position).username);
                            return hashMap;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            });
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

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        TextView friendsName;
        Button followButton;
        CircleImageView friendsImage;

        FriendsViewHolder(View v) {
            super(v);
            friendsName = (TextView) v.findViewById(R.id.friendsName);
            followButton = (Button) v.findViewById(R.id.follow);
            friendsImage = (CircleImageView) v.findViewById(R.id.friendsImage);
        }
    }
}
