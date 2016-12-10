package com.adityawalvekar.impact.impact;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FeedFragment extends Fragment {

    private static int numberOfPosts = 100;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private PostAdapter mPostAdapter;
    private RequestQueue queue;
    private ArrayList<Post> newsFeed = new ArrayList<>();
    private EditText descriptionEditText;
    private Button postButton;
    private SwipeRefreshLayout refreshLayout;
    private SharedPreferences prefs;
    private ProgressBar progressBar;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());
        prefs = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.feedSwipeRefresh);
        progressBar = (ProgressBar) rootView.findViewById(R.id.feedProgressBar);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queue.cancelAll(getActivity());
                getNewsFeed();
            }
        });
        descriptionEditText = (EditText) rootView.findViewById(R.id.postText);
        postButton = (Button) rootView.findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = descriptionEditText.getText().toString();
                if (text.length() <= 0) {
                    Snackbar.make(view, "Description cannot be empty", Snackbar.LENGTH_SHORT).show();
                } else if (text.length() >= 120) {
                    Snackbar.make(view, "Description cannot be more than 120 characters", Snackbar.LENGTH_SHORT).show();
                } else {
                    Date date = new Date();
                    long currentTime = date.getTime();
                    makePost(descriptionEditText.getText().toString(), currentTime, view);
                }
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.feedRecyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        getNewsFeed();
        return rootView;
    }

    private void getNewsFeed() {
        newsFeed.clear();
        mRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        final String URL = "https://impact.adityawalvekar.com/feed";
        queue.add(new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                numberOfPosts = 100;
                try {
                    parseJson(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Response for feed", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                refreshLayout.setRefreshing(false);
                Log.d("Error getting feed", error.toString());
                mRecyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error in retrieving feed", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("username", prefs.getString("username", ""));
                params.put("length", "" + numberOfPosts);
                return params;
            }
        });
    }

    private void parseJson(String response) throws JSONException {
        JSONObject json = new JSONObject(response);
        if (json.has("status") && json.getBoolean("status")) {
            JSONArray array = json.getJSONArray("feed");
            for (int i = 0; i < array.length(); i++) {
                JSONObject post = array.getJSONObject(i);
                switch (post.getInt("event_type")) {
                    case 1:
                        newsFeed.add(new Post(post.getInt("pid"), post.getString("name"), post.getString("desc"), post.getString("date"), post.getBoolean("active"), post.getString("userimage")));
                        break;
                    case 2:
                        newsFeed.add(new Post(post.getInt("pid"), post.getString("name"), post.getString("title"), post.getString("desc"), post.getString("location"), post.getString("date"), post.getBoolean("active"), post.getString("userimage"), post.getString("postimage")));
                        break;
                    case 3:
                        newsFeed.add(new Post(post.getInt("pid"), post.getString("name"), post.getString("userimage"), post.getString("postimage")));
                        break;
                }
            }
        } else {
            Log.d("Error in data", "Status is false");
        }
        mPostAdapter = new PostAdapter(this.getActivity(), newsFeed);
        mRecyclerView.setAdapter(mPostAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public void makePost(final String description, final long currentTime, final View view) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Posting");
        dialog.show();

        queue.add(new StringRequest(Request.Method.POST, "https://impact.adityawalvekar.com/post",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        descriptionEditText.setText("");
                        Snackbar.make(view, "Successfully posted!", Snackbar.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Snackbar.make(view, "There was an error in posting, please try again!", Snackbar.LENGTH_SHORT).show();
                Log.d("FeedFragment", "Error Posting " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
                String userName = sharedPreferences.getString("username", "");
                hashMap.put("username", userName);
                hashMap.put("desc", description);
                hashMap.put("date", String.valueOf(currentTime));
                Log.v("FeedFragment", String.valueOf(currentTime));
                hashMap.put("event_type", String.valueOf(1));
                return hashMap;
            }
        });
    }
}
