package com.abisyscorp.ivalt;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.abisyscorp.ivalt.ZoomProcessors.AuthenticateProcessor;
import com.abisyscorp.ivalt.ZoomProcessors.Config;
import com.abisyscorp.ivalt.ZoomProcessors.NetworkingHelpers;
import com.abisyscorp.ivalt.ZoomProcessors.Processor;
import com.abisyscorp.ivalt.api.ApiClient;
import com.abisyscorp.ivalt.api.ZoomGlobalState;
import com.abisyscorp.ivalt.biomatricss.BiometricCallback;
import com.abisyscorp.ivalt.biomatricss.BiometricManager;
import com.abisyscorp.ivalt.databinding.ActivityGlobalauthBinding;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facetec.sdk.FaceTecCustomization;
import com.facetec.sdk.FaceTecFaceScanProcessor;
import com.facetec.sdk.FaceTecFaceScanResultCallback;
import com.facetec.sdk.FaceTecOverlayCustomization;
import com.facetec.sdk.FaceTecSDK;
import com.facetec.sdk.FaceTecSDKStatus;
import com.facetec.sdk.FaceTecSessionActivity;
import com.facetec.sdk.FaceTecSessionResult;
import com.facetec.sdk.FaceTecSessionStatus;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.abisyscorp.ivalt.ZoomProcessors.NetworkingHelpers.OK_HTTP_RESPONSE_CANCELED;


public class iValtGlobalAuthActivity extends AppCompatActivity {

    SharedPreferences sp;
    private static boolean requestInProgress = false;
    final String TAG = "Myzoomresponse";
    String endpoint = "/liveness-3d";
    boolean success;
    Processor latestProcessor;

    ActivityGlobalauthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_globalauth);
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        //faceMapFunctionility();
        checkBiomatrics();
       /* try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }


    void checkBiomatrics(){

        try {
            if (iValtAuthentication.mMapData!=null) {

                Map<String,String> mMap =  iValtAuthentication.mMapData;
                Log.d("fcmresponsee",new Gson().toJson(mMap));
                if (mMap.containsKey("message")) {
                    String messageMap = mMap.get("message");
                    JSONObject jData = new JSONObject(messageMap);

                    if (jData.has("mobile")) {
                        MyFirebaseMessagingService.mobile = jData.optString("mobile");
                    }
                    Log.d("rdddddd1", MyFirebaseMessagingService.mobile);

                    if (jData.has("type")) {
                        MyFirebaseMessagingService.type = jData.optString("type");
                    }
                    if (jData.has("request_for")) {
                        MyFirebaseMessagingService.requestFor = jData.optString("request_for");
                    }
                    if (jData.has("website")) {
                        MyFirebaseMessagingService.website = jData.optString("website");
                    }
                    if (jData.has("token")) {
                        MyFirebaseMessagingService.token = jData.optString("token");
                    }
                    if (iValtAuthentication.mMapData.containsKey("public_key")) {
                        MyFirebaseMessagingService.public_key = iValtAuthentication.mMapData.get("public_key");
                    }

                } else {

                    if (iValtAuthentication.mMapData.containsKey("mobile")) {
                        MyFirebaseMessagingService.mobile = iValtAuthentication.mMapData.get("mobile");
                    }
                    Log.d("rdddddd1", MyFirebaseMessagingService.mobile);

                    if (iValtAuthentication.mMapData.containsKey("type")) {
                        MyFirebaseMessagingService.type = iValtAuthentication.mMapData.get("type");
                    }
                    if (iValtAuthentication.mMapData.containsKey("request_for")) {
                        MyFirebaseMessagingService.requestFor = iValtAuthentication.mMapData.get("request_for");
                    }
                    if (iValtAuthentication.mMapData.containsKey("website")) {
                        MyFirebaseMessagingService.website = iValtAuthentication.mMapData.get("website");
                    }
                    if (iValtAuthentication.mMapData.containsKey("token")) {
                        MyFirebaseMessagingService.token = iValtAuthentication.mMapData.get("token");
                    }
                    if (iValtAuthentication.mMapData.containsKey("public_key")) {
                        MyFirebaseMessagingService.public_key = iValtAuthentication.mMapData.get("public_key");
                    }

                    //Toast.makeText(this, "datat"+new Gson().toJson(iValtAuthentication.mMapData), Toast.LENGTH_SHORT).show();

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        onBiomatricss();
    }

    BiometricManager mBiometricManager;
    boolean sentToSettings = false;
    final int REQUEST_CODE = 1551;

    void onBiomatricss(){

        mBiometricManager = new BiometricManager.BiometricBuilder(iValtGlobalAuthActivity.this)
                .setTitle("Biometric login for iValt")
                .setSubtitle("Log in using your biometric credential")
                .setDescription("")
                .setNegativeButtonText("Cancel")
                .build();

        mBiometricManager.authenticate(biometricCallback);

    }

    BiometricCallback biometricCallback = new BiometricCallback() {
        @Override
        public void onSdkVersionNotSupported() {
            updateResultsLabel("Device does not support Biometric authentication");
            /*
             *  Will be called if the device sdk version does not support Biometric authentication
             */
        }



        @Override
        public void onBiometricAuthenticationNotSupported() {
            /*
             *  Will be called if the device does not contain any fingerprint sensors
             */
            updateResultsLabel("Device does not contain any fingerprint sensors");
        }

        @Override
        public void onBiometricAuthenticationNotAvailable() {
            updateResultsLabel("Your Face ID/Touch ID is not configured.");
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                    enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                    startActivityForResult(enrollIntent, REQUEST_CODE);
                }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    startActivity(intent);
                }
            }catch (Exception e){
                e.printStackTrace();
                showAlert();
            }
            /*
             *  The device does not have any biometrics registered in the device.
             */
        }

        @Override
        public void onBiometricAuthenticationPermissionNotGranted() {
            updateResultsLabel("USE_BIOMETRIC permission is not granted to the app");
            try {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(iValtGlobalAuthActivity.this);
                builder.setCancelable(false);
                builder.setTitle(getString(R.string.needMultiplePermission));
                builder.setMessage(getString(R.string.needMultiplePermissionMsg));
                builder.setPositiveButton(getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", iValtGlobalAuthActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        Toast.makeText(iValtGlobalAuthActivity.this, getString(R.string.gotoPermission), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                builder.show();
            }catch (Exception e){
                e.printStackTrace();
            }
            /*
             *  android.permission.USE_BIOMETRIC permission is not granted to the app
             */
        }

        @Override
        public void onBiometricAuthenticationInternalError(String error) {
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            /*
             *  This method is called if one of the fields such as the title, subtitle,
             * description or the negative button text is empty
             */
        }

        @Override
        public void onAuthenticationFailed() {
            /*
             * When the fingerprint doesn’t match with any of the fingerprints registered on the device,
             * then this callback will be triggered.
             */
            updateResultsLabel("Authentication unsuccessful, Try again.");
            iValtGlobalAuthActivity.this.finish();
        }

        @Override
        public void onAuthenticationCancelled() {
            /*
             * The authentication is cancelled by the user.
             */
            updateResultsLabel("Authentication unsuccessful, Try again.");
            iValtGlobalAuthActivity.this.finish();
        }

        @Override
        public void onAuthenticationSuccessful() {
            /*
             * When the fingerprint is has been successfully matched with one of the fingerprints
             * registered on the device, then this callback will be triggered.
             */
            updateResultsLabel("Authentication succeeded!");

            if (MyFirebaseMessagingService.type.equalsIgnoreCase("global")) {
                globalReg("true");
            } else {
                if (MyFirebaseMessagingService.type.equalsIgnoreCase("wordpress") && MyFirebaseMessagingService.requestFor.equalsIgnoreCase("login")) {
                    loginUser();
                } else {
                    uploadRegister();
                }
            }

        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            /*
             * This method is called when a non-fatal error has occurred during the authentication
             * process. The callback will be provided with an help code to identify the cause of the
             * error, along with a help message.
             */
        }

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            /*
             * When an unrecoverable error has been encountered and the authentication process has
             * completed without success, then this callback will be triggered. The callback is provided
             * with an error code to identify the cause of the error, along with the error message.
             */
            updateResultsLabel("Authentication unsuccessful, Try again.");
            iValtGlobalAuthActivity.this.finish();
        }
    };

    public void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(iValtGlobalAuthActivity.this);
        builder.setMessage("Your Face ID/Touch ID is not configured.");
        builder.setPositiveButton("Ok",(dialog, which) -> {
            dialog.dismiss();
        }).setNegativeButton("Setting", ((dialog, which) -> {
            dialog.dismiss();
            try {
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        })).show();
    }


  /*  void onBiometricsProcess(){
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                biometricPrompt.authenticate(promptInfo);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                try {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                        startActivityForResult(enrollIntent, REQUEST_CODE);
                    }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                        startActivity(intent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    showAlert();
                }
                break;
        }
    }


    void onBiomatricss(){
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(iValtGlobalAuthActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
                updateResultsLabel("Authentication unsuccessful, Try again.");
                iValtGlobalAuthActivity.this.finish();

            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();

                if (MyFirebaseMessagingService.type.equalsIgnoreCase("global")) {
                    globalReg("true");
                } else {
                    if (MyFirebaseMessagingService.type.equalsIgnoreCase("wordpress") && MyFirebaseMessagingService.requestFor.equalsIgnoreCase("login")) {
                        loginUser();
                    } else {
                        uploadRegister();
                    }
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
                updateResultsLabel("Authentication unsuccessful, Try again.");
                iValtGlobalAuthActivity.this.finish();

            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText(" ")
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
    }*/




    public void faceMapFunctionility(){

        try {
            if (iValtAuthentication.mMapData!=null) {

                Map<String,String> mMap =  iValtAuthentication.mMapData;
                Log.d("fcmresponsee",new Gson().toJson(mMap));
                if (mMap.containsKey("message")) {
                    String messageMap = mMap.get("message");
                    JSONObject jData = new JSONObject(messageMap);

                    if (jData.has("mobile")) {
                        MyFirebaseMessagingService.mobile = jData.optString("mobile");
                    }
                    Log.d("rdddddd1", MyFirebaseMessagingService.mobile);

                    if (jData.has("type")) {
                        MyFirebaseMessagingService.type = jData.optString("type");
                    }
                    if (jData.has("request_for")) {
                        MyFirebaseMessagingService.requestFor = jData.optString("request_for");
                    }
                    if (jData.has("website")) {
                        MyFirebaseMessagingService.website = jData.optString("website");
                    }
                    if (jData.has("token")) {
                        MyFirebaseMessagingService.token = jData.optString("token");
                    }

                } else {

                    if (iValtAuthentication.mMapData.containsKey("mobile")) {
                        MyFirebaseMessagingService.mobile = iValtAuthentication.mMapData.get("mobile");
                    }
                    Log.d("rdddddd1", MyFirebaseMessagingService.mobile);

                    if (iValtAuthentication.mMapData.containsKey("type")) {
                        MyFirebaseMessagingService.type = iValtAuthentication.mMapData.get("type");
                    }
                    if (iValtAuthentication.mMapData.containsKey("request_for")) {
                        MyFirebaseMessagingService.requestFor = iValtAuthentication.mMapData.get("request_for");
                    }
                    if (iValtAuthentication.mMapData.containsKey("website")) {
                        MyFirebaseMessagingService.website = iValtAuthentication.mMapData.get("website");
                    }
                    if (iValtAuthentication.mMapData.containsKey("token")) {
                        MyFirebaseMessagingService.token = iValtAuthentication.mMapData.get("token");
                    }

                    //Toast.makeText(this, "datat"+new Gson().toJson(iValtAuthentication.mMapData), Toast.LENGTH_SHORT).show();

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.d("dddddd1",MyFirebaseMessagingService.mobile+"\n"+MyFirebaseMessagingService.type+"\n"+MyFirebaseMessagingService.requestFor+"\n"+MyFirebaseMessagingService.website+"\n"+MyFirebaseMessagingService.token);
        binding.loader.setVisibility(View.VISIBLE);
        FaceTecCustomization zoomCustomization = new FaceTecCustomization();
        FaceTecOverlayCustomization overlayCustomization = new FaceTecOverlayCustomization();
        overlayCustomization.showBrandingImage = false;
        overlayCustomization.backgroundColor = android.R.color.transparent;
        zoomCustomization.setOverlayCustomization(overlayCustomization);

        // Set the encryption key, preload ZoOm resources, and initialize to validate the license.  update results label with result.
        //ZoomSDK.setFacemapEncryptionKey(publicKey);
        //FaceTecSDK.preload(Dashboard.this);

        showProgress();
        Config.initializeFaceTecSDKFromAutogeneratedConfig(iValtGlobalAuthActivity.this, new FaceTecSDK.InitializeCallback() {
            @Override
            public void onCompletion(final boolean successful) {
                closeProgress();
                if(successful) {
                    Log.d("FaceTecSDKSampleApp", "Initialization Successful.");
                    //closeProgress();
                    sp.edit().putBoolean("isFromNotification",false).commit();
                    onLivenessCheckClick();
                }
                Log.d("FaceTecSDKSampleApp", ""+FaceTecSDK.getStatus(iValtGlobalAuthActivity.this).toString());

                // Displays the FaceTec SDK Status to text field.
                //utils.displayStatus(FaceTecSDK.getStatus(SampleAppActivity.this).toString());
            }
        });

    }

    interface SessionTokenCallback {
        void onSessionTokenReceived(String sessionToken);
    }

    public void getSessionToken(final SessionTokenCallback sessionTokenCallback) {
        //utils.showSessionTokenConnectionText();

        // Do the network call and handle result
        okhttp3.Request request = new okhttp3.Request.Builder()
                .header("X-Device-Key", Config.DeviceKeyIdentifier)
                .header("User-Agent", FaceTecSDK.createFaceTecAPIUserAgentString(""))
                .url(Config.BaseURL + "/session-token")
                .get()
                .build();
        showProgress();

        NetworkingHelpers.getApiClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                closeProgress();
                e.printStackTrace();
                Log.d("FaceTecSDKSampleApp", "Exception raised while attempting HTTPS call.");

                // If this comes from HTTPS cancel call, don't set the sub code to NETWORK_ERROR.
                if(!e.getMessage().equals(NetworkingHelpers.OK_HTTP_RESPONSE_CANCELED)) {
                    //utils.handleErrorGettingServerSessionToken();
                    updateResultsLabel("Session could not be started due to an unexpected issue during the network request.");
                }
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                closeProgress();
                String responseString = response.body().string();
                response.body().close();
                try {
                    JSONObject responseJSON = new JSONObject(responseString);
                    if(responseJSON.has("sessionToken")) {
                        //utils.hideSessionTokenConnectionText();
                        sessionTokenCallback.onSessionTokenReceived(responseJSON.getString("sessionToken"));
                    }
                    else {
                        updateResultsLabel("Session could not be started due to an unexpected issue during the network request.");
                    }
                }
                catch(JSONException e) {
                    e.printStackTrace();
                    Log.d("FaceTecSDKSampleApp", "Exception raised while attempting to parse JSON result.");
                    //utils.handleErrorGettingServerSessionToken();
                    updateResultsLabel("Session could not be started due to an unexpected issue during the network request.");
                }
            }
        });
    }

    public void onLivenessCheckClick() {
        //Toast.makeText(this, "helleo", Toast.LENGTH_SHORT).show();
        getSessionToken(new SessionTokenCallback() {
            @Override
            public void onSessionTokenReceived(String sessionToken) {
                latestProcessor = new AuthenticateProcessor( sessionToken, iValtGlobalAuthActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (latestProcessor == null) {
            return;
        }

        // At this point, you have already handled all results in your Processor code.
        if (this.latestProcessor.isSuccess()) {
            if (MyFirebaseMessagingService.type.equalsIgnoreCase("global")) {
                globalReg("true");
            } else {
                if (MyFirebaseMessagingService.type.equalsIgnoreCase("wordpress") && MyFirebaseMessagingService.requestFor.equalsIgnoreCase("login")) {
                    loginUser();
                } else {
                    uploadRegister();
                }
            }
        }else {
            updateResultsLabel("Authentication unsuccessful, Try again.");
            iValtGlobalAuthActivity.this.finish();
        }
    }

    private void closeProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.loader.setVisibility(View.GONE);
                        }
                    });
                } catch (Exception e){
                    binding.loader.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        });
    }

    private void showProgress(){
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.loader.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e){
            binding.loader.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    // In order to provide better diagnostics and debugging for initial integrations,
    // we by default collect some additional data in order to pinpoint problems if there are support issues raised.


    // Updates a label in the UI with some text
    private void updateResultsLabel(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Constants.showInformation(iValtGlobalAuthActivity.this, msg);
            }
        });
    }


    private void loginUser() {
        Log.d(TAG+"yyyy","calllogin");
        showProgress();
        String url = ApiClient.BASE_IVURLWP+"confirm-login-auth";
        //String url = ApiClient.BASE_IVURLWP+"check-user-enrollment";
        JSONObject params = new JSONObject();
        try {
            params.put("mobile",MyFirebaseMessagingService.mobile);
            params.put("token",MyFirebaseMessagingService.token);
            params.put("domain",MyFirebaseMessagingService.website);
            params.put("country_code",sp.getString("country_code",""));
            Log.d(TAG+"patamL",params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(iValtGlobalAuthActivity.this);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG+"login",response.toString());
                closeProgress();
                iValtAuthentication.mMapData.clear();
                if (response.has("status")){
                    try {
                        if (response.getJSONObject("data").getString("status").equalsIgnoreCase("true")){
                            updateResultsLabel(response.getJSONObject("data").optString("message"));
                        }else {
                            updateResultsLabel(response.getJSONObject("data").optString("message"));
                        }
                        iValtGlobalAuthActivity.this.finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeProgress();
                iValtAuthentication.mMapData.clear();
                VolleyLog.e("VolleyError","Error : "+error.getMessage());
                String strError = "";
                if( error instanceof NetworkError) {
                    strError = "No internet connection!";
                } else if( error instanceof ServerError) {
                    strError = "Server error, Try after some time.";
                } else if( error instanceof AuthFailureError) {
                    strError = "AuthFailure, Try after some time.";
                } else if( error instanceof ParseError) {
                    strError = "Parser error, Try after some time.";
                } else if( error instanceof NoConnectionError) {
                    strError = "No internet connection!.";
                } else if( error instanceof TimeoutError) {
                    strError = "Timeout error.";
                }else {
                    strError = "Error : " + error.getMessage();
                }
                try {
                    final String finalStrError = strError;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Constants.showInformation(iValtGlobalAuthActivity.this, finalStrError);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                iValtGlobalAuthActivity.this.finish();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(ApiClient.headerParamName,ApiClient.X_API_KEY);
                return params;
            }
        };

        requestQueue.add(jor);
        //MyFirebaseMessagingService.isFromNotification = false;
        MyFirebaseMessagingService.type = "";
        MyFirebaseMessagingService.requestFor = "";
        MyFirebaseMessagingService.type = "";
        MyFirebaseMessagingService.requestFor = "";
    }


    private void uploadRegister() {
        Log.d(TAG+"yyyy","callregister");
        showProgress();
        String url = ApiClient.BASE_IVURLWP+"register/confirmation";
        JSONObject params = new JSONObject();
        try {
            params.put("mobile",MyFirebaseMessagingService.mobile);
            params.put("token",MyFirebaseMessagingService.token);
            params.put("domain",MyFirebaseMessagingService.website);
            params.put("country_code",sp.getString("country_code",""));
            Log.d(TAG+"patamR",params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(iValtGlobalAuthActivity.this);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG+"confrim",response.toString());
                closeProgress();
                iValtAuthentication.mMapData.clear();
                if (response.has("data")){
                    try {
                        if (response.getJSONObject("data").getString("status").equalsIgnoreCase("true")){
                            updateResultsLabel(response.getJSONObject("data").optString("message"));
                        }else {
                            updateResultsLabel(response.getJSONObject("data").optString("message"));
                        }
                        iValtGlobalAuthActivity.this.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeProgress();
                iValtAuthentication.mMapData.clear();
                VolleyLog.e("VolleyError","Error : "+error.getMessage());
                String strError = "";
                if( error instanceof NetworkError) {
                    strError = "No internet connection!";
                } else if( error instanceof ServerError) {
                    strError = "Server error, Try after some time.";
                } else if( error instanceof AuthFailureError) {
                    strError = "AuthFailure, Try after some time.";
                } else if( error instanceof ParseError) {
                    strError = "Parser error, Try after some time.";
                } else if( error instanceof NoConnectionError) {
                    strError = "No internet connection!.";
                } else if( error instanceof TimeoutError) {
                    strError = "Timeout error.";
                }else {
                    strError = "Error : " + error.getMessage();
                }
                try {
                    final String finalStrError = strError;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Constants.showInformation(iValtGlobalAuthActivity.this, finalStrError);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                iValtGlobalAuthActivity.this.finish();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(ApiClient.headerParamName,ApiClient.X_API_KEY);
                return params;
            }
        };
        //{
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params  = new HashMap<>();
//                params.put("mobile",MyFirebaseMessagingService.mobile);
//                params.put("token",MyFirebaseMessagingService.token);
//                params.put("domain",MyFirebaseMessagingService.website);
//                return params;
//            }
//        };

        requestQueue.add(jor);
        //MyFirebaseMessagingService.isFromNotification = false;
        MyFirebaseMessagingService.type = "";
        MyFirebaseMessagingService.requestFor = "";
        MyFirebaseMessagingService.type = "";
        MyFirebaseMessagingService.requestFor = "";
    }


    private void globalReg(String status) {
        Log.d(TAG+"yyyy","callglobal");
        showProgress();
        Log.d(TAG+"yyyy","callglobal");
        //https://baldevkrishan.com/ivalt_v1/public/webcard/send/global/notification
        String url = ApiClient.BASE_IVURL+"global/authenticate";//?mobile="+sp.getString("uPhone", "")+"&status="+status+"&latitude="+sp.getString("lat","")+"&longitude="+sp.getString("lng","")+"&address="+sp.getString("address","");
        String rurl = url.replace(" ","%20");

        Log.d(TAG+"yyyy",rurl);
        JSONObject params = new JSONObject();
        try {
            params.put("mobile",sp.getString("uPhone", ""));
            params.put("status",status);
            params.put("country_code",sp.getString("country_code",""));
          /*  params.put("latitude",sp.getString("lat",""));
            params.put("longitude",sp.getString("lng",""));
            params.put("longitude",sp.getString("lng",""));
            params.put("address",sp.getString("address",""))*/;
            /*params.put("public_key",MyFirebaseMessagingService.public_key);*/

            //params.put("auth_status",status);
            Log.d(TAG+"global",params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(iValtGlobalAuthActivity.this);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, rurl, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG+"global",response.toString());
                closeProgress();
                iValtGlobalAuthActivity.this.finish();
                if (response.has("status")){
                    try {
                        if (response.getJSONObject("data").getString("status").equalsIgnoreCase("true")){
                            //updateResultsLabel(response.getString("message"));
                        }else {
                            //updateResultsLabel(response.getString("message"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeProgress();
                iValtGlobalAuthActivity.this.finish();
                VolleyLog.e("VolleyErrorglobal","Error : "+error.getMessage());
                String strError = "";
                if( error instanceof NetworkError) {
                    strError = "No internet connection!";
                } else if( error instanceof ServerError) {
                    strError = "Server error, Try after some time.";
                } else if( error instanceof AuthFailureError) {
                    strError = "AuthFailure, Try after some time.";
                } else if( error instanceof ParseError) {
                    strError = "Parser error, Try after some time.";
                } else if( error instanceof NoConnectionError) {
                    strError = "No internet connection!.";
                } else if( error instanceof TimeoutError) {
                    strError = "Timeout error.";
                }else {
                    strError = "Error : " + error.getMessage();
                }
                try {
                    final String finalStrError = strError;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Constants.showInformation(iValtGlobalAuthActivity.this, finalStrError);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }

                iValtGlobalAuthActivity.this.finish();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(ApiClient.headerParamName,ApiClient.X_API_KEY);
                return params;
            }
        };

        requestQueue.add(jor);

        //MyFirebaseMessagingService.isFromNotification = false;
        MyFirebaseMessagingService.type = "";
        MyFirebaseMessagingService.requestFor = "";
        MyFirebaseMessagingService.type = "";
        MyFirebaseMessagingService.requestFor = "";

    }




}
