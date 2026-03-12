package com.npci.faceauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;

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

            if (resultCode == Activity.RESULT_OK && intent != null) {

                JSONObject result = new JSONObject();

                Bundle bundle = intent.getExtras();

                if (bundle != null) {

                    for (String key : bundle.keySet()) {

                        Object value = bundle.get(key);
                        result.put(key, value != null ? value.toString() : "");

                    }

                }

                callback.success(result);

            } else {

                callback.error("FaceAuth cancelled or no result");

            }

        } catch (Exception e) {

            callback.error(e.getMessage());

        }

    }
}
