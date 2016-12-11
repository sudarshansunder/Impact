package com.adityawalvekar.impact.impact;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendsFragment extends Fragment {

    public String JSONResponse;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FriendsAdapter friendsAdapter;
    ArrayList<Friends> mDataSet;
    SearchView searchView;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView1);
        searchView = (SearchView) rootView.findViewById(R.id.friendsSearch);
        mDataSet = new ArrayList<Friends>();
        friendsAdapter = new FriendsAdapter(getContext(), mDataSet);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(friendsAdapter);
        searchView.setQueryHint("Search for someone");
        searchView.setQuery("", true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                mDataSet.clear();
                friendsAdapter.notifyDataSetChanged();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
                String userName = sharedPreferences.getString("username", "");
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                StringRequest request = new StringRequest(Request.Method.POST, "https://impact.adityawalvekar.com/search",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.v("Search Result", response);
                                JSONResponse = response;
                                updateDataSet();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FriendsFragment", "Error searching!");
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
                        String userName = sharedPreferences.getString("username", "");
                        hashMap.put("fullname", query);
                        hashMap.put("username", userName);
                        return hashMap;
                    }
                };
                requestQueue.cancelAll(getActivity());
                requestQueue.add(request);
                friendsAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return rootView;
    }

    public void updateDataSet() {
        try {
            JSONObject jsonObject = new JSONObject(JSONResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("users");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String userName = jsonObject1.getString("username");
                Boolean follows = jsonObject1.getBoolean("follows");
                String userImage = jsonObject1.getString("image");
                String fullName = jsonObject1.getString("fullname");
                Friends friends = new Friends(fullName, userName, userImage, follows);
                mDataSet.add(friends);
            }
            friendsAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            Log.v("FriendsFragment", ex.toString());
        }
    }
}
