package com.adityawalvekar.impact.impact;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyEventsFragment extends Fragment {


    private RequestQueue queue;
    private RecyclerView recyclerView;
    private ArrayList<Event> list = new ArrayList<>();

    public MyEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);
        queue = Volley.newRequestQueue(getActivity());
        queue.add(new StringRequest(Request.Method.POST, "https://impact.adityawalvekar.com/attendlistforuser", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("My events response", response);
                try {
                    parseResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("My events error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("username", getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE).getString("username", ""));
                return params;
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.myEventsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void parseResponse(String response) throws JSONException {
        list.clear();
        list = new ArrayList<>();
        JSONObject json = new JSONObject(response);
        int n = json.getInt("count");
        JSONArray events = json.getJSONArray("list");
        for (int i = 0; i < n; i++) {
            JSONObject temp = events.getJSONObject(i);
            list.add(new Event(temp.getString("name"), temp.getString("userimage"), temp.getString("postimage"), temp.getString("date"), temp.getString("location"), temp.getString("title"), temp.getString("desc")));
        }
        MyEventsAdapter adapter = new MyEventsAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
    }

}
