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
import java.util.List;
import java.util.Map;

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
    public void onBindViewHolder(final FriendsViewHolder holder, final int position) {
        holder.friendsName.setText(mDataSet.get(position).fullname);
        if(mDataSet.get(position).following==true){
            holder.followButton.setText("UNFOLLOW");
            holder.followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                    requestQueue.cancelAll(mContext);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://impact.adityawalvekar.com/unfollow",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    holder.followButton.setText("FOLLOW");
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v("FriendsAdapter",error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String,String> hashMap = new HashMap<String, String>();
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
        }else{
            holder.followButton.setText("FOLLOW");
            holder.followButton.setEnabled(true);
            holder.followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                    requestQueue.cancelAll(mContext);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://impact.adityawalvekar.com/follow",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    holder.followButton.setText("UNFOLLOW");;
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v("FriendsAdapter",error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String,String> hashMap = new HashMap<String, String>();
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
}
