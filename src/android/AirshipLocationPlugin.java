/* Copyright Urban Airship and Contributors */

package com.urbanairship.cordova;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.urbanairship.UAirship;
import com.urbanairship.util.HelperActivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The Airship Location Cordova plugin.
 */
public class AirshipLocationPlugin extends CordovaPlugin {

    /**
     * These actions are only available after takeOff.
     */
    private final static List<String> AIRSHIP_LOCATION_ACTIONS = Arrays.asList("setLocationEnabled", "setBackgroundLocationEnabled", "isLocationEnabled", "isBackgroundLocationEnabled", "recordCurrentLocation");

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    private Context context;

    @Override
    public void initialize(@NonNull CordovaInterface cordova, @NonNull CordovaWebView webView) {
        super.initialize(cordova, webView);
        PluginLogger.info("Initializing android Airship location cordova plugin.");
        context = cordova.getActivity().getApplicationContext();
    }

    /**
     * To extend the plugin, add the actions to either {@link #AIRSHIP_LOCATION_ACTIONS} or {#link #GLOBAL_ACTIONS} and
     * then define the method with the signature `void <CORDOVA_ACTION>(JSONArray data, final
     * CallbackContext callbackContext)` and it will automatically be called. All methods will be
     * ( executed in the ExecutorService. Any exceptions thrown by the actions are automatically caught
     * and the callbackContext will return an error result.
     */
    @Override
    public boolean execute(final String action, final JSONArray data, final CallbackContext callbackContext) {
        final boolean isAirshipAction = AIRSHIP_LOCATION_ACTIONS.contains(action);

        if (!isAirshipAction) {
            PluginLogger.debug("Invalid action: %s", action);
            return false;
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    PluginLogger.debug("Plugin Execute: %s", action);
                    Method method = AirshipLocationPlugin.class.getDeclaredMethod(action, JSONArray.class, CallbackContext.class);
                    method.invoke(AirshipLocationPlugin.this, data, callbackContext);
                } catch (Exception e) {
                    PluginLogger.error(e, "Action failed to execute: %s", action);
                    callbackContext.error("Action " + action + " failed with exception: " + e.getMessage());
                }
            }
        });

        return true;
    }

    /**
     * Enables or disables Urban Airship location services.
     * <p/>
     * Expected arguments: Boolean
     *
     * @param data The call data.
     * @param callbackContext The callback context.
     */
    void setLocationEnabled(@NonNull JSONArray data, @NonNull final CallbackContext callbackContext) throws JSONException {
        boolean enabled = data.getBoolean(0);

        if (enabled && shouldRequestPermissions()) {
            RequestPermissionsTask task = new RequestPermissionsTask(context, new RequestPermissionsTask.Callback() {
                @Override
                public void onResult(boolean enabled) {
                    UAirship.shared().getLocationManager().setLocationUpdatesEnabled(enabled);
                }
            });
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            UAirship.shared().getLocationManager().setLocationUpdatesEnabled(enabled);
            callbackContext.success();
        }
    }

    /**
     * Determines if we should request permissions
     *
     * @return {@code true} if permissions should be requested, otherwise {@code false}.
     */
    private boolean shouldRequestPermissions() {
        if (Build.VERSION.SDK_INT < 23) {
            return false;
        }

        Context context = UAirship.getApplicationContext();
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED;
    }

    private static class RequestPermissionsTask extends AsyncTask<String[], Void, Boolean> {

        @SuppressLint("StaticFieldLeak")
        private final Context context;
        private final Callback callback;

        public interface Callback {
            void onResult(boolean enabled);
        }

        RequestPermissionsTask(@NonNull Context context, @NonNull Callback callback) {
            this.context = context.getApplicationContext();
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(String[]... strings) {
            int[] result = HelperActivity.requestPermissions(context, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
            for (int element : result) {
                if (element == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (callback != null) {
                callback.onResult(result);
            }
        }
    }

    /**
     * Checks if location is enabled or not.
     *
     * @param data The call data.
     * @param callbackContext The callback context.
     */
    void isLocationEnabled(@NonNull JSONArray data, @NonNull CallbackContext callbackContext) {
        int value = UAirship.shared().getLocationManager().isLocationUpdatesEnabled() ? 1 : 0;
        callbackContext.success(value);
    }

    /**
     * Enables are disables background location. Background location requires location to be enabled.
     * <p/>
     * Expected arguments: Boolean
     *
     * @param data The call data.
     * @param callbackContext The callback context.
     */
    void setBackgroundLocationEnabled(@NonNull JSONArray data, @NonNull CallbackContext callbackContext) throws JSONException {
        boolean enabled = data.getBoolean(0);
        UAirship.shared().getLocationManager().setBackgroundLocationAllowed(enabled);
        callbackContext.success();
    }

    /**
     * Checks if background location is enabled or not.
     *
     * @param data The call data.
     * @param callbackContext The callback context.
     */
    void isBackgroundLocationEnabled(@NonNull JSONArray data, @NonNull CallbackContext callbackContext) {
        int value = UAirship.shared().getLocationManager().isBackgroundLocationAllowed() ? 1 : 0;
        callbackContext.success(value);
    }
}
