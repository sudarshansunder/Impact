package com.adityawalvekar.impact.impact;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class EventCreatorFragment extends Fragment {

    final Calendar calendar = Calendar.getInstance();
    ImageView imageView;
    String base64 = "";
    EditText eventDateTextInputLayout;

    public EventCreatorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_event_creator, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.eventCreatorImage);
        ImageViewBitmapLoader imageViewBitmapLoader = new ImageViewBitmapLoader(getContext());
        imageViewBitmapLoader.loadBitmap(R.drawable.sunset, imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 6969);
            }
        });
        base64 = "";
        Button button = (Button) rootView.findViewById(R.id.createEventButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout eventNameTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.eventCreatorName);
                TextInputLayout eventDescriptionTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.eventCreatorDescription);
                TextInputLayout eventAddressTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.eventCreatorAddress);
                if (eventNameTextInputLayout.getEditText().getText().toString().matches("") || eventDescriptionTextInputLayout.getEditText().getText().toString().matches("") || eventAddressTextInputLayout.getEditText().getText().toString().matches("")) {
                    Snackbar.make(view, "Please enter data in all the fields", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                Editable editable1 = eventNameTextInputLayout.getEditText().getText();
                Editable editable2 = eventDescriptionTextInputLayout.getEditText().getText();
                Editable editable3 = eventAddressTextInputLayout.getEditText().getText();
                String eventName = editable1.toString();
                String eventDescription = editable2.toString();
                String eventAddress = editable3.toString();
                if (eventDateTextInputLayout.getText().toString().matches("")) {
                    Snackbar.make(view, "Please choose a Date", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (base64.matches("")) {
                    Snackbar.make(view, "Please pick an Image", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                long currTime = calendar.getTime().getTime();
                createEvent(eventName, eventDescription, eventAddress, base64, currTime, view);
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
        eventDateTextInputLayout = (EditText) rootView.findViewById(R.id.eventDate);
        eventDateTextInputLayout.setInputType(InputType.TYPE_NULL);
        eventDateTextInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        return rootView;
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
        eventDateTextInputLayout.setText(simpleDateFormat.format(calendar.getTime()));
    }

    public void createEvent(final String eventName, final String eventDescription, final String eventAddress, String img, final long currTime, final View view) {
        RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://impact.adityawalvekar.com/event", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Snackbar.make(view, "Event was Created", Snackbar.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("EventCreatorFragment", "Error creating event");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_data", MODE_PRIVATE);
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
}
