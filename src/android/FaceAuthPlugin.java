package com.npci.faceauth;

import android.app.Activity;
import android.content.Intent;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;

// NPCI activity inside the AAR
import org.npci.upi.security.pinactivitycomponent.GetCredential;

public class FaceAuthPlugin extends CordovaPlugin {

    private static final int FACEAUTH_REQUEST = 9001;
    private CallbackContext callback;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        if ("startAuth".equals(action)) {

            this.callback = callbackContext;

            Activity activity = cordova.getActivity();

            try {

                JSONObject payload = args.getJSONObject(0);

                Intent intent = new Intent(activity, GetCredential.class);

                intent.putExtra("request", payload.toString());

                cordova.startActivityForResult(this, intent, FACEAUTH_REQUEST);

                return true;

            } catch (Exception e) {

                callbackContext.error(e.getMessage());
                return false;

            }

        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode != FACEAUTH_REQUEST) return;

        try {

            JSONObject result = new JSONObject();

            if (intent != null && intent.getExtras() != null) {

                for (String key : intent.getExtras().keySet()) {

                    Object val = intent.getExtras().get(key);
                    result.put(key, val == null ? JSONObject.NULL : val.toString());

                }

            }

            callback.success(result);

        } catch (Exception e) {

            callback.error(e.getMessage());

        }

    }

}