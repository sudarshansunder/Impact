package com.adityawalvekar.impact.impact;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ButtonBarLayout;
import android.text.Editable;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventCreatorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventCreatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventCreatorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView imageView;
    String base64 = "";
    EditText eventDateEditText;

    final Calendar calendar = Calendar.getInstance();

    private OnFragmentInteractionListener mListener;

    public EventCreatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventCreatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventCreatorFragment newInstance(String param1, String param2) {
        EventCreatorFragment fragment = new EventCreatorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_event_creator, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.eventCreatorImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 6969);
            }
        });
        base64 = new String();
        Button button = (Button) rootView.findViewById(R.id.createEventButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText eventNameEditText = (EditText) rootView.findViewById(R.id.eventCreatorName);
                EditText eventDescriptionEditText = (EditText) rootView.findViewById(R.id.eventCreatorDescription);
                EditText eventAddressEditText = (EditText) rootView.findViewById(R.id.eventCreatorAddress);
                if(eventNameEditText.getText().toString().matches("")||eventDescriptionEditText.getText().toString().matches("")||eventAddressEditText.getText().toString().matches("")) {
                    Toast.makeText(getActivity(),"Please enter data in all the fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                Editable editable1 = eventNameEditText.getText();
                Editable editable2 = eventDescriptionEditText.getText();
                Editable editable3 = eventAddressEditText.getText();
                String eventName = editable1.toString();
                String eventDescription = editable2.toString();
                String eventAddress = editable3.toString();
                if(eventDateEditText.getText().toString().matches("")) {
                    Toast.makeText(getActivity(),"Please choose a Date",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(base64.matches("")) {
                    Toast.makeText(getActivity(),"Please pick an Image",Toast.LENGTH_SHORT).show();
                    return;
                }
                long currTime = calendar.getTime().getTime();
                createEvent(eventName, eventDescription, eventAddress, base64, currTime);
            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                updateLabel();
            }
        };
        eventDateEditText = (EditText) rootView.findViewById(R.id.eventDate);
        eventDateEditText.setInputType(InputType.TYPE_NULL);
        eventDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),date,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        return rootView;
    }

    private void updateLabel(){
        String myFormat = "MM/dd/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
        eventDateEditText.setText(simpleDateFormat.format(calendar.getTime()));
    }

    public void createEvent(final String eventName, final String eventDescription, final String eventAddress, String img, final long currTime){
        RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://impact.adityawalvekar.com/event", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("EventCreatorFragment","Error creating event");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String,String>();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_date",MODE_PRIVATE);
                String userName = sharedPreferences.getString("username", "");
                hashMap.put("username", userName);
                hashMap.put("title", eventName);
                hashMap.put("desc", eventDescription);
                hashMap.put("picture", base64);
                hashMap.put("location", eventAddress);
                hashMap.put("date", String.valueOf(currTime));
                hashMap.put("event_type", String.valueOf(2));
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 6969 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, bao);
                byte[] ba = bao.toByteArray();
                base64 = Base64.encodeToString(ba, Base64.DEFAULT);
                // Log.d(TAG, String.valueOf(bitmap));
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
