package com.adityawalvekar.impact.impact;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout username, password, name, confirmPassword;
    private Button register;
    private RequestQueue queue;
    private SharedPreferences prefs;
    private ImageView profileImage;
    private String base64;
    private ProgressDialog dialog;

    private boolean validEmailId(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validPassword(String password) {
        return password.length() >= 6 && password.length() <= 32;
    }

    private boolean validPasswordConfirm(String confirm) {
        return confirm.equals(password.getEditText().getText().toString());
    }

    private boolean validName(String name) {
        return !name.isEmpty();
    }

    private boolean validLogin() {
        boolean e = username.getError() == null;
        boolean p = password.getError() == null;
        boolean cp = confirmPassword.getError() == null;
        boolean n = name.getError() == null;
        return e && p && cp && n;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        linkViews();
        prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        queue = Volley.newRequestQueue(this);
        username.setErrorEnabled(true);
        password.setErrorEnabled(true);
        name.setErrorEnabled(true);
        confirmPassword.setErrorEnabled(true);
        username.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validEmailId(charSequence.toString())) {
                    username.setError(null);
                } else {
                    username.setError("Invalid email id");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validPassword(charSequence.toString())) {
                    password.setError(null);
                } else {
                    password.setError("Password must be between 6 and 32 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validPasswordConfirm(charSequence.toString())) {
                    confirmPassword.setError(null);
                } else {
                    confirmPassword.setError("Passwords must match");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validName(charSequence.toString())) {
                    name.setError(null);
                } else {
                    name.setError("Name cannot be empty");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validLogin()) {
                    //TODO Logic to register user
                    registerUser();
                } else {
                    Snackbar.make(view, "Enter valid email id and password!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 6969);
            }
        });
    }

    private void linkViews() {
        username = (TextInputLayout) findViewById(R.id.email_reg);
        password = (TextInputLayout) findViewById(R.id.password_reg);
        confirmPassword = (TextInputLayout) findViewById(R.id.password_confirm);
        name = (TextInputLayout) findViewById(R.id.name_reg);
        register = (Button) findViewById(R.id.email_register);
        profileImage = (ImageView) findViewById(R.id.profile_picture_login);
    }

    private void registerUser() {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating Account");
        dialog.show();


        final String URL = "https://impact.adityawalvekar.com/register";
        queue.add(new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("Register Response", response);
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.has("status") && res.getBoolean("status")) {
                        //TODO Register the user
                        prefs.edit()
                                .putBoolean("auth", true)
                                .putString("name", name.getEditText().getText().toString())
                                .putString("username", username.getEditText().getText().toString())
                                .putString("image", res.getString("picture"))
                                .apply();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.d("Register error", error.toString());
                if (error.getMessage() != null)
                    Log.d("Register error body", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", username.getEditText().getText().toString());
                params.put("password", password.getEditText().getText().toString());
                params.put("fullname", name.getEditText().getText().toString());
                params.put("picture", base64);
                return params;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 6969 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, bao);
                byte[] ba = bao.toByteArray();
                base64 = Base64.encodeToString(ba, Base64.DEFAULT);
                // Log.d(TAG, String.valueOf(bitmap));
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
