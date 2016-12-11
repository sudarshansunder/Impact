package com.adityawalvekar.impact.impact;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout username, password;
    private Button login, register;
    private RequestQueue queue;
    private SharedPreferences prefs;
    private ProgressDialog dialog;

    private boolean validEmailId(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validPassword(String password) {
        return password.length() > 6 && password.length() < 32;
    }

    private boolean validLogin() {
        boolean e = username.getError() == null;
        boolean p = password.getError() == null;
        return e && p;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        linkViews();
        queue = Volley.newRequestQueue(this);
        prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        if (prefs.getBoolean("auth", false)) {
            Log.d("Auth", "User already logged in");
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        username.setErrorEnabled(true);
        password.setErrorEnabled(true);
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
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validLogin()) {
                    //TODO Logic to login user
                    loginUser();
                } else {
                    Snackbar.make(view, "Enter valid email id and password!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });
    }

    private void linkViews() {
        username = (TextInputLayout) findViewById(R.id.email_login);
        password = (TextInputLayout) findViewById(R.id.password_login);
        login = (Button) findViewById(R.id.email_login_button);
        register = (Button) findViewById(R.id.email_register_button);
    }

    private void loginUser() {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging you in");
        dialog.show();
        final String URL = "https://impact.adityawalvekar.com/login";
        queue.add(new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("Login response", response);
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.has("status") && res.getBoolean("status")) {
                        //TODO Login in the user
                        prefs.edit()
                                .putBoolean("auth", true)
                                .putString("name", res.getString("fullname"))
                                .putString("username", username.getEditText().getText().toString())
                                .putString("image", res.getString("picture"))
                                .apply();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else if (res.has("status") && !res.getBoolean("status")) {
                        if (res.getInt("code") == 401) {
                            password.setError("Invalid credentials");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.d("Login error", error.toString());
                Log.d("Login error body", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", username.getEditText().getText().toString());
                params.put("password", password.getEditText().getText().toString());
                params.put("token", FirebaseInstanceId.getInstance().getToken());
                return params;
            }
        });
    }

}
