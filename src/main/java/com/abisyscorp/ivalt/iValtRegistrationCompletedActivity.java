package com.abisyscorp.ivalt;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.abisyscorp.ivalt.ZoomProcessors.AuthenticateProcessor;
import com.abisyscorp.ivalt.ZoomProcessors.Config;
import com.abisyscorp.ivalt.ZoomProcessors.EnrollmentProcessor;
import com.abisyscorp.ivalt.ZoomProcessors.NetworkingHelpers;
import com.abisyscorp.ivalt.ZoomProcessors.Processor;
import com.abisyscorp.ivalt.api.ApiClient;
import com.abisyscorp.ivalt.api.ZoomGlobalState;
import com.abisyscorp.ivalt.biomatricss.BiometricCallback;
import com.abisyscorp.ivalt.biomatricss.BiometricManager;
import com.abisyscorp.ivalt.databinding.ActivityProcessEnrollmentBinding;
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
import com.facetec.sdk.FaceTecIDScanResult;
import com.facetec.sdk.FaceTecOverlayCustomization;
import com.facetec.sdk.FaceTecSDK;
import com.facetec.sdk.FaceTecSDKStatus;
import com.facetec.sdk.FaceTecSessionActivity;
import com.facetec.sdk.FaceTecSessionResult;
import com.facetec.sdk.FaceTecSessionStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

import static com.abisyscorp.ivalt.ZoomProcessors.NetworkingHelpers.OK_HTTP_RESPONSE_CANCELED;


public class iValtRegistrationCompletedActivity extends AppCompatActivity implements View.OnClickListener {

    private static boolean requestInProgress = false;
    boolean success;

    ActivityProcessEnrollmentBinding binding;

    final String TAG = "Processzoomresponse";
    String endpoint = "/liveness-3d";
    SharedPreferences sp;
    Processor latestProcessor;

    public interface Listener {
        void onUploadProgressChanged(long bytesWritten, long totalBytes);
    }

    public static iValtRegistrationCompletedActivity mInstance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = iValtRegistrationCompletedActivity.this;
        binding = DataBindingUtil.setContentView(this,R.layout.activity_process_enrollment);
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        binding.btnSumbit.setOnClickListener(this);
        binding.ivImage.setOnClickListener(this);
        binding.linInfo.setOnClickListener(this);
        //faceMapFunctionility();
        onBiomatricss();


        try {
            if (Constants.getDarkMode(iValtRegistrationCompletedActivity.this)){
                binding.tvAName.setTextColor(Color.WHITE);
                binding.tvAName1.setTextColor(Color.WHITE);
                //binding.tvSuccessText.setTextColor(Color.WHITE);
            }else {
                binding.tvAName.setTextColor(Color.WHITE);
                binding.tvAName1.setTextColor(Color.WHITE);
                //binding.tvSuccessText.setTextColor(Color.WHITE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    BiometricManager mBiometricManager;
    boolean sentToSettings = false;
    final int REQUEST_CODE = 1551;

    void onBiomatricss(){

        mBiometricManager = new BiometricManager.BiometricBuilder(iValtRegistrationCompletedActivity.this)
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
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(iValtRegistrationCompletedActivity.this);
                builder.setCancelable(false);
                builder.setTitle(getString(R.string.needMultiplePermission));
                builder.setMessage(getString(R.string.needMultiplePermissionMsg));
                builder.setPositiveButton(getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", iValtRegistrationCompletedActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        Toast.makeText(iValtRegistrationCompletedActivity.this, getString(R.string.gotoPermission), Toast.LENGTH_SHORT).show();
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.linSuccessText.setVisibility(View.VISIBLE);
                    binding.tvSuccessText.setVisibility(View.GONE);
                    binding.tvSuccessText2.setText("Failed!");
                    binding.tvSuccessText2.setVisibility(View.VISIBLE);
                    binding.tvSuccessText3.setVisibility(View.GONE);
                    binding.btnSumbit.setText("Done");
                }
            });
        }

        @Override
        public void onAuthenticationCancelled() {
            /*
             * The authentication is cancelled by the user.
             */
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.linSuccessText.setVisibility(View.VISIBLE);
                    binding.tvSuccessText.setVisibility(View.GONE);
                    binding.tvSuccessText2.setText("Failed!");
                    binding.tvSuccessText2.setVisibility(View.VISIBLE);
                    binding.tvSuccessText3.setVisibility(View.GONE);
                    binding.btnSumbit.setText("Done");
                }
            });
        }

        @Override
        public void onAuthenticationSuccessful() {
            /*
             * When the fingerprint is has been successfully matched with one of the fingerprints
             * registered on the device, then this callback will be triggered.
             */
            updateResultsLabel("Success");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.linSuccessText.setVisibility(View.VISIBLE);
                    binding.tvSuccessText.setVisibility(View.VISIBLE);
                    binding.tvSuccessText2.setText("Success");
                    binding.tvSuccessText2.setVisibility(View.VISIBLE);
                    binding.tvSuccessText3.setVisibility(View.VISIBLE);
                    binding.btnSumbit.setText("Done");
                    sp.edit().putString("isenroll", "yes").apply();
                    isEnrolled(sp.getString("uPhone", ""));
                }
            });
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.linSuccessText.setVisibility(View.VISIBLE);
                    binding.tvSuccessText.setVisibility(View.GONE);
                    binding.tvSuccessText2.setText("Failed!");
                    binding.tvSuccessText2.setVisibility(View.VISIBLE);
                    binding.tvSuccessText3.setVisibility(View.GONE);
                    binding.btnSumbit.setText("Done");
                }
            });
        }
    };

    public void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(iValtRegistrationCompletedActivity.this);
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

    /*void onBiomatricss(){
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(iValtRegistrationCompletedActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
                binding.linSuccessText.setVisibility(View.VISIBLE);
                binding.tvSuccessText.setVisibility(View.GONE);
                binding.tvSuccessText2.setText("Failed!");
                binding.tvSuccessText2.setVisibility(View.VISIBLE);
                binding.tvSuccessText3.setVisibility(View.GONE);
                binding.btnSumbit.setText("Done");
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                binding.linSuccessText.setVisibility(View.VISIBLE);
                binding.tvSuccessText.setVisibility(View.VISIBLE);
                binding.tvSuccessText2.setText("Success");
                binding.tvSuccessText2.setVisibility(View.VISIBLE);
                binding.tvSuccessText3.setVisibility(View.VISIBLE);
                binding.btnSumbit.setText("Done");
                sp.edit().putString("isenroll", "yes").apply();
                isEnrolled(sp.getString("uPhone", ""));
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
                binding.linSuccessText.setVisibility(View.VISIBLE);
                binding.tvSuccessText.setVisibility(View.GONE);
                binding.tvSuccessText2.setText("Failed!");
                binding.tvSuccessText2.setVisibility(View.VISIBLE);
                binding.tvSuccessText3.setVisibility(View.GONE);
                binding.btnSumbit.setText("Done");
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

        if (!"".equalsIgnoreCase(sp.getString("uPhone", ""))) {
            if (!TextUtils.isEmpty(iValtAuthentication._deviceToken)) {
                onUpdate(sp.getString("uPhone", ""), iValtAuthentication._deviceToken);
            }
        }

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
                    if (sp.getString("is_enrolled","false").equalsIgnoreCase("false")) {
                        onLivenessCheckClick();
                    }else {
                        onLivenessAuthenticate();
                    }
                }
                Log.d("FaceTecSDKSampleApp", ""+FaceTecSDK.getStatus(iValtRegistrationCompletedActivity.this).toString());

                // Displays the FaceTec SDK Status to text field.
                //utils.displayStatus(FaceTecSDK.getStatus(SampleAppActivity.this).toString());
            }
        });


    }

    public void onLivenessAuthenticate(){
        getSessionToken(new SessionTokenCallback() {
            @Override
            public void onSessionTokenReceived(String sessionToken) {
                latestProcessor = new AuthenticateProcessor( sessionToken, iValtRegistrationCompletedActivity.this);
            }
        });
    }

    void isEnrolled(final String mobile){
        binding.loader.setVisibility(View.VISIBLE);
        String url = ApiClient.WEBCARDBASE_URL+ "enrolstatus";
        JSONObject params = new JSONObject();
        try {
            params.put("mobile",mobile);
            params.put("supplier_id", iValtAuthentication._supplier_id);

            Log.d("isEnrolled",mobile);
        } catch (Exception e){
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(iValtRegistrationCompletedActivity.this);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("isEnrolled",response.toString());
                //{"status":"success","message":"Successfully registered!","id":21,"mobile":"7461846199"}
                binding.loader.setVisibility(View.GONE);
                try {
                    if (response.getJSONObject("data").getString("status").equalsIgnoreCase("true")) {
                       Log.d("enrolleddate",response.toString());

                    }else {
                        Constants.showInformation(iValtRegistrationCompletedActivity.this, response.getJSONObject("data").optString("message"));
                    }
                }catch (Exception e){
                    binding.loader.setVisibility(View.GONE);
                    Constants.showInformation(iValtRegistrationCompletedActivity.this,"Error something went wrong");
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.loader.setVisibility(View.GONE);
                Log.d("isEnrolled","Error : "+error.getMessage());
                VolleyLog.e("isEnrolled","Error : "+error.getMessage());
                String strError = "";
                if( error instanceof NetworkError) {
                    strError = "No internet connection!";
                } else if( error instanceof ServerError) {
                    strError = "Invalid code or server error, Try after some time.";
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
                Constants.showInformation(iValtRegistrationCompletedActivity.this,strError);
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

    // When user invokes a ZoOm Liveness Check, first check to make sure the SDK is initialized and ready, then start the ZoOm Activity
    public void onLivenessCheckClick() {
        //Toast.makeText(this, "helleo", Toast.LENGTH_SHORT).show();

        getSessionToken(new SessionTokenCallback() {
            @Override
            public void onSessionTokenReceived(String sessionToken) {
                latestProcessor = new EnrollmentProcessor(sessionToken, iValtRegistrationCompletedActivity.this);
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.linSuccessText.setVisibility(View.VISIBLE);
                    binding.tvSuccessText.setVisibility(View.VISIBLE);
                    binding.tvSuccessText2.setText("Success");
                    binding.tvSuccessText2.setVisibility(View.VISIBLE);
                    binding.tvSuccessText3.setVisibility(View.VISIBLE);
                    binding.btnSumbit.setText("Done");
                    sp.edit().putString("isenroll", "yes").apply();
                    isEnrolled(sp.getString("uPhone", ""));
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.linSuccessText.setVisibility(View.VISIBLE);
                    binding.tvSuccessText.setVisibility(View.GONE);
                    binding.tvSuccessText2.setText("Failed!");
                    binding.tvSuccessText2.setVisibility(View.VISIBLE);
                    binding.tvSuccessText3.setVisibility(View.GONE);
                    binding.btnSumbit.setText("Done");
                }
            });
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

    private void updateResultsLabel(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Constants.showInformation(iValtRegistrationCompletedActivity.this, msg);
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

    void setResultData(String status,String data){
        Intent intent = new Intent("iValtBroadCast");
        intent.putExtra("status",status);
        intent.putExtra("message",data);
        LocalBroadcastManager.getInstance(iValtRegistrationCompletedActivity.this).sendBroadcast(intent);
        finish();//finishing activity
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnSumbit){
            if(binding.tvSuccessText2.getText().toString().equalsIgnoreCase("Failed!") || TextUtils.isEmpty(binding.tvSuccessText2.getText().toString())){
                setResultData("false","Failed");
            }else{
                setResultData("true","Authenticated");
            }
        }

        if (v == binding.ivImage){
            if (sp.getString("is_enrolled","false").equalsIgnoreCase("false")) {
                onLivenessCheckClick();
            }else {
                onLivenessAuthenticate();
            }
        }
        if (v == binding.linInfo){
            showOption();
        }
    }

    public void showOption(){
        AlertDialog.Builder builder = new AlertDialog.Builder(iValtRegistrationCompletedActivity.this);
        View view = LayoutInflater.from(iValtRegistrationCompletedActivity.this).inflate(R.layout.custom_sheetlayout,null);
        TextView tvAbout = view.findViewById(R.id.tvAbout);
        TextView tvFAQ = view.findViewById(R.id.tvFAQ);
        TextView tvContact = view.findViewById(R.id.tvContact);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(iValtRegistrationCompletedActivity.this,AboutUsActivity.class));
            }
        });
        tvFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String url = "https://ivalt.com/faqs-for-android.php";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(iValtRegistrationCompletedActivity.this,ContactActivity.class));
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
        dialog.show();
    }

    public String getIMEI() {
        try {
            //TelephonyManager telephonyManager = (TelephonyManager) activity
            //.getSystemService(Context.TELEPHONY_SERVICE);
            //return telephonyManager.getDeviceId()!=null?telephonyManager.getDeviceId(): UUID.randomUUID().toString();
            return  Settings.Secure.getString(iValtRegistrationCompletedActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }catch (Exception e){
            e.printStackTrace();
            return UUID.randomUUID().toString();
        }
    }

    void onUpdate(final String mobile,String device_token){
        //String url = ApiClient.BASE_URL+"mobile/refresh/token";
        String url = ApiClient.WEBCARDBASE_URL+"mobile/refresh/token";
        JSONObject params = new JSONObject();
        try {
            params.put("mobile",mobile);
            params.put("token",device_token);
            params.put("imei",getIMEI());

        } catch (Exception e){
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(iValtRegistrationCompletedActivity.this);
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
                    strError = "Invalid code or server error, Try after some time.";
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

    public static class ProgressRequestBody extends RequestBody {
        private final RequestBody requestBody;
        private Listener listener;

        public ProgressRequestBody(RequestBody requestBody, Listener listener) {
            this.requestBody = requestBody;
            this.listener = listener;
        }

        @Override
        public MediaType contentType() {
            return requestBody.contentType();
        }

        @Override
        public long contentLength() throws IOException {
            return requestBody.contentLength();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
             ProgressStream progressStream = new ProgressRequestBody.ProgressStream(sink.outputStream(), contentLength());
            BufferedSink progressSink = Okio.buffer(Okio.sink(progressStream));
            requestBody.writeTo(progressSink);
            progressSink.flush();
        }

        protected final class ProgressStream extends OutputStream {
            private final OutputStream stream;
            private long totalBytes;
            private long bytesSent;

            ProgressStream(OutputStream stream, long totalBytes) {
                this.stream = stream;
                this.totalBytes = totalBytes;
            }

            @Override
            public void write(@NonNull byte[] b, int off, int len) throws IOException {
                this.stream.write(b, off, len);
                if(len < b.length) {
                    this.bytesSent += len;
                }
                else {
                    this.bytesSent += b.length;
                }
                listener.onUploadProgressChanged(this.bytesSent, this.totalBytes);
            }

            @Override
            public void write(int b) throws IOException {
                this.stream.write(b);
                this.bytesSent += 1;
                listener.onUploadProgressChanged(this.bytesSent, this.totalBytes);
            }
        }


    }
}
