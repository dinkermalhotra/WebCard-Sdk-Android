package com.abisyscorp.ivalt;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.abisyscorp.ivalt.ZoomProcessors.AuthenticateProcessor;
import com.abisyscorp.ivalt.ZoomProcessors.Config;
import com.abisyscorp.ivalt.ZoomProcessors.NetworkingHelpers;
import com.abisyscorp.ivalt.ZoomProcessors.Processor;
import com.abisyscorp.ivalt.api.ApiClient;
import com.abisyscorp.ivalt.api.ZoomGlobalState;
import com.abisyscorp.ivalt.biomatricss.BiometricCallback;
import com.abisyscorp.ivalt.biomatricss.BiometricManager;
import com.abisyscorp.ivalt.databinding.ActivityLoginAuthenticateBinding;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.abisyscorp.ivalt.ZoomProcessors.NetworkingHelpers.OK_HTTP_RESPONSE_CANCELED;

public class iValtLoginActivity extends AppCompatActivity {

    ActivityLoginAuthenticateBinding binding;
    final String TAG = "loginnzoomresponse";
    SharedPreferences sp;
    String endpoint = "/liveness-3d";
    private static boolean requestInProgress = false;
    boolean success;
    Processor latestProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login_authenticate);
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        //faceMapFunctionility();
        //authentication
        callApi();

       /* try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }



    void callApi(){
        binding.loader.setVisibility(View.VISIBLE);
        String url = ApiClient.WEBCARDBASE_URL+"registered-user-details";
        JSONObject params = new JSONObject();
        try {
            params.put("imei",getIMEI(iValtLoginActivity.this));

            Log.d("verifyusererresp",new Gson().toJson(params));
        } catch (Exception e){
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(iValtLoginActivity.this);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("verifyusererresp",response.toString());
                closeProgress();
                //{"status":"success","message":"Successfully registered!","id":21,"mobile":"7461846199"}
                try {
                    if (response.getJSONObject("data").getString("status").equalsIgnoreCase("true")) {
                        JSONObject data = response.getJSONObject("data");
                        JSONObject details = data.getJSONObject("details");
                        JSONObject uData = details.getJSONObject("user");
                        SharedPreferences sp = iValtLoginActivity.this.getSharedPreferences("sp",Context.MODE_PRIVATE);
                        sp.edit().putString("id",uData.optString("id")).apply();
                        //faceMapFunctionility();
                        checkBiomatrics();

                    }else {
                        checkBiomatrics();
                       //faceMapFunctionility();
                        //setResultData("false","");
                        //Constants.showInformation(LoginAuthenticateActivity.this, response.getString("message") != null ? response.getString("message") : "");
                        //LoginAuthenticateActivity.this.finish();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    //faceMapFunctionility();
                    checkBiomatrics();
                    //setResultData("","Error something went wrong");
                    //LoginAuthenticateActivity.this.finish();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeProgress();
                //faceMapFunctionility();
                checkBiomatrics();
                Log.d("verifyusererresp","Error : "+error.getMessage());
                VolleyLog.e("verifyusererresp","Error : "+error.getMessage());
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
                VolleyLog.e("verifyusererresp","Error : "+strError);
                //LoginAuthenticateActivity.this.finish();
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
    }
    

    void checkBiomatrics(){
        onBiomatricss();
    }

    BiometricManager mBiometricManager;
    boolean sentToSettings = false;
    final int REQUEST_CODE = 1551;

    void onBiomatricss(){

        mBiometricManager = new com.abisyscorp.ivalt.biomatricss.BiometricManager.BiometricBuilder(iValtLoginActivity.this)
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
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(iValtLoginActivity.this);
                builder.setCancelable(false);
                builder.setTitle(getString(R.string.needMultiplePermission));
                builder.setMessage(getString(R.string.needMultiplePermissionMsg));
                builder.setPositiveButton(getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", iValtLoginActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        Toast.makeText(iValtLoginActivity.this, getString(R.string.gotoPermission), Toast.LENGTH_SHORT).show();
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
            setResultData("false", "");
        }

        @Override
        public void onAuthenticationCancelled() {
            /*
             * The authentication is cancelled by the user.
             */
            updateResultsLabel("Authentication unsuccessful, Try again.");
            setResultData("false", "");
        }

        @Override
        public void onAuthenticationSuccessful() {
            /*
             * When the fingerprint is has been successfully matched with one of the fingerprints
             * registered on the device, then this callback will be triggered.
             */
            updateResultsLabel("Success");
            onVerified();

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
            setResultData("false", "");
        }
    };

    public void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(iValtLoginActivity.this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void faceMapFunctionility(){

        Log.d("dddddd1",MyFirebaseMessagingService.mobile+"\n"+MyFirebaseMessagingService.type+"\n"+MyFirebaseMessagingService.requestFor+"\n"+MyFirebaseMessagingService.website+"\n"+MyFirebaseMessagingService.token);
        binding.loader.setVisibility(View.VISIBLE);
        FaceTecCustomization zoomCustomization = new FaceTecCustomization();
        FaceTecOverlayCustomization overlayCustomization = new FaceTecOverlayCustomization();
        overlayCustomization.showBrandingImage = false;
        overlayCustomization.backgroundColor = android.R.color.transparent;
        zoomCustomization.setOverlayCustomization(overlayCustomization);

        Config.initializeFaceTecSDKFromAutogeneratedConfig(this, new FaceTecSDK.InitializeCallback() {
            @Override
            public void onCompletion(final boolean successful) {
                closeProgress();
                if(successful) {
                    Log.d("FaceTecSDKSampleApp", "Initialization Successful.");
                    closeProgress();
                    onLivenessAuthenticate();
                }
                Log.d("FaceTecSDKSampleApp", ""+FaceTecSDK.getStatus(iValtLoginActivity.this).toString());

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



    public void onLivenessAuthenticate(){
        //Toast.makeText(this, "helleo", Toast.LENGTH_SHORT).show();
        getSessionToken(new SessionTokenCallback() {
            @Override
            public void onSessionTokenReceived(String sessionToken) {
                latestProcessor = new AuthenticateProcessor( sessionToken, iValtLoginActivity.this);
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
            updateResultsLabel("Success");
            onVerified();
        } else {

            updateResultsLabel("Authentication unsuccessful, Try again.");
            setResultData("false", "");
            // Reset the enrollment identifier.
        }
    }

    public String getIMEI(Activity activity) {
        try {
            //TelephonyManager telephonyManager = (TelephonyManager) activity
            //.getSystemService(Context.TELEPHONY_SERVICE);
            //return telephonyManager.getDeviceId()!=null?telephonyManager.getDeviceId(): UUID.randomUUID().toString();
            return  Settings.Secure.getString(iValtLoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }catch (Exception e){
            e.printStackTrace();
            return UUID.randomUUID().toString();
        }
    }

    void onVerified(){
        binding.loader.setVisibility(View.VISIBLE);
        String url = ApiClient.WEBCARDBASE_URL+"registered-user-details";
        JSONObject params = new JSONObject();
        try {
            params.put("imei",getIMEI(iValtLoginActivity.this));

            Log.d("verifyusererresp",new Gson().toJson(params));
        } catch (Exception e){
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(iValtLoginActivity.this);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("verifyusererresp",response.toString());
                //{"status":"success","message":"Successfully registered!","id":21,"mobile":"7461846199"}
                binding.loader.setVisibility(View.GONE);
                try {
                    if (response.getJSONObject("data").getString("status").equalsIgnoreCase("true")) {
                        JSONObject data = response.getJSONObject("data");
                        JSONObject details = data.getJSONObject("details");
                        JSONObject uData = details.getJSONObject("user");
                        Log.d("loginData",uData.toString());
                        sp.edit().putString("id",uData.optString("id")).apply();
                        sp.edit().putString("uPhone",uData.optString("mobile")).apply();
                        sp.edit().putString("uName",uData.optString("name")).apply();
                        sp.edit().putString("country_code",uData.optString("country_code")).apply();
                        Log.d("daaaaa",sp.getString("uPhone",""));
                        Log.d("daaaaa",sp.getString("uName",""));
                        Log.d("daaaaa",sp.getString("country_code",""));
                        setResultData("true",uData.toString());
                        //LoginAuthenticateActivity.this.finish();
                        updateDeviceToken(uData.optString("mobile"),iValtAuthentication._deviceToken);
                    }else {
                        setResultData("false","");
                        //Constants.showInformation(LoginAuthenticateActivity.this, response.getString("message") != null ? response.getString("message") : "");
                        //LoginAuthenticateActivity.this.finish();
                    }
                }catch (JSONException e){
                    binding.loader.setVisibility(View.GONE);
                    Constants.showInformation(iValtLoginActivity.this,"Error something went wrong");
                    e.printStackTrace();
                    setResultData("false","");
                    //setResultData("","Error something went wrong");
                    //LoginAuthenticateActivity.this.finish();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.loader.setVisibility(View.GONE);
                Log.d("verifyusererresp","Error : "+error.getMessage());
                VolleyLog.e("verifyusererresp","Error : "+error.getMessage());
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
                //Constants.showInformation(iValtLoginActivity.this,strError);
                //LoginAuthenticateActivity.this.finish();
                setResultData("false",strError);
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
    }

    void setResultData(String status,String data){
        Intent intent = new Intent("iValtBroadLogin");
        intent.putExtra("status",status);
        intent.putExtra("userData",data);
        LocalBroadcastManager.getInstance(iValtLoginActivity.this).sendBroadcast(intent);
        finish();//finishing activity
    }

    public String getIMEI() {
        try {
            //TelephonyManager telephonyManager = (TelephonyManager) activity
            //.getSystemService(Context.TELEPHONY_SERVICE);
            //return telephonyManager.getDeviceId()!=null?telephonyManager.getDeviceId(): UUID.randomUUID().toString();
            return  Settings.Secure.getString(iValtLoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }catch (Exception e){
            e.printStackTrace();
            return UUID.randomUUID().toString();
        }
    }

    public void updateDeviceToken(String mobile,String deviceToken){


        if (!"".equalsIgnoreCase(mobile)) {
            String url = ApiClient.WEBCARDBASE_URL+"mobile/refresh/token";
            JSONObject params = new JSONObject();
            try {
                params.put("mobile",mobile);
                params.put("token",deviceToken);
                params.put("imei",getIMEI());
                Log.d("updateotp",params.toString());
            } catch (Exception e){
                e.printStackTrace();
            }

            RequestQueue requestQueue = Volley.newRequestQueue(iValtLoginActivity.this);
            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("updateotp",response.toString());

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("verifyregisterresp","Error : "+error.getMessage());
                    VolleyLog.e("verifyregisterresp","Error : "+error.getMessage());
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
                    Log.d("voError",strError);
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
        }

    }



    private void updateResultsLabel(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Constants.showInformation(iValtLoginActivity.this, msg);
            }
        });
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


    private void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.loader.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (Exception e){
                    binding.loader.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        });
    }

}