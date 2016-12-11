package com.adityawalvekar.impact.impact;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseToken extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Firebase token", "Refreshed token: " + refreshedToken);
    }
}
