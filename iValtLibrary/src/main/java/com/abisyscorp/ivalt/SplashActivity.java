package com.abisyscorp.ivalt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.abisyscorp.ivalt.api.ApiClient;
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
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
/*import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;*/

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "MyDToken";
    SharedPreferences sp;
    boolean status;
    public String json = "";
    private static final int REQUEST_CODE_CHECK_SETTINGS = 405;
    //complete app pushed


    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };
    private boolean sentToSettings = false;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }

                   Location location = (Location) locationResult.getLastLocation();
                    Log.d("currentlocation","lat"+location.getLatitude()+"////"+"lng"+location.getLongitude());
                    sp.edit().putString("lat",String.valueOf(location.getLatitude())).apply();
                    sp.edit().putString("lng",String.valueOf(location.getLongitude())).apply();
                    sp.edit().putString("address",getAddress(location.getLatitude(),location.getLongitude())).apply();
                    Log.d("currentlocation",getAddress(location.getLatitude(),location.getLongitude())+"///");
                    proceedAfterPermission();

                }
            };
        }catch (Exception e){
            e.printStackTrace();
        }

        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        sp.edit().putString("ff","y").apply();
        //sp.edit().putString("first","yes").apply();

        //getToken();
        //onNewIntent(getIntent());
        Intent bundle = getIntent();
        if (bundle.hasExtra("status")){
            status = bundle.getBooleanExtra("status",false);
            //MyFirebaseMessagingService.isFromNotification = status;
            if (bundle.getStringExtra("dataS")!=null) {
                json = bundle.getStringExtra("dataS");
            }
        }else {
            status = false;
        }
        //sendNotification();
        //Log.d("dddddd", MyFirebaseMessagingService.isFromNotification+"");
        Log.d("dddddd", json+"//");

    }


    public String getAddress(Double latitude,Double longitude){
        String addressStr = "";
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            addressStr = address;
            /*String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();*/
        }catch (Exception e){
            addressStr = "";
            e.printStackTrace();
        }
        return addressStr;
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    protected void enableLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        LocationServices
                .getSettingsClient(this)
                .checkLocationSettings(builder.build())
                .addOnSuccessListener(this, (LocationSettingsResponse response) -> {
                    startLocationUpdates();
                })
                .addOnFailureListener(this, ex -> {
                    if (ex instanceof ResolvableApiException) {
                        // Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),  and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) ex;
                            resolvable.startResolutionForResult(SplashActivity.this, REQUEST_CODE_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_CHECK_SETTINGS == requestCode) {
            if(Activity.RESULT_OK == resultCode){
                //user clicked OK, you can startUpdatingLocation(...);
                startLocationUpdates();
            }else{
                SplashActivity.this.finish();
                //user clicked cancel: informUserImportanceOfLocationAndPresentRequestAgain();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
    }

    public void checkPermissions(){
        if(ActivityCompat.checkSelfPermission(SplashActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SplashActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,permissionsRequired[1])){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setCancelable(false);
                builder.setTitle(getString(R.string.needMultiplePermission));
                builder.setMessage(getString(R.string.needMultiplePermissionMsg));
                builder.setPositiveButton(getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(SplashActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
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
            } else if (sp.getBoolean(permissionsRequired[0],false)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setCancelable(false);
                builder.setTitle(getString(R.string.needMultiplePermission));
                builder.setMessage(getString(R.string.needMultiplePermissionMsg));
                builder.setPositiveButton(getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", SplashActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(SplashActivity.this, getString(R.string.gotoPermission), Toast.LENGTH_SHORT).show();
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
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(SplashActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }
            sp.edit().putBoolean(permissionsRequired[0],true).apply();
        } else {
            //You already have the permission, just go ahead.
            enableLocationSettings();
        }
    }

    private void proceedAfterPermission() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sp.getString("id","").equalsIgnoreCase("")) {
                    //Bundle bundle = new Bundle();
                    //bundle.putBoolean("status", status);
                    startActivity(new Intent(SplashActivity.this, IntroScreenActivity.class));
                }else {
                    //Bundle bundle = new Bundle();
                    //bundle.putBoolean("status", status);
                    if (sp.getString("isenroll","").equalsIgnoreCase("")) {
                        startActivity(new Intent(SplashActivity.this, iValtRegistrationCompletedActivity.class));
                    }else {
                        //startActivity(new Intent(SplashActivity.this, Dashboard.class));
                    }
                }
                SplashActivity.this.finish();
            }
        },3000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                enableLocationSettings();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,permissionsRequired[1])){

                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setCancelable(false);
                builder.setTitle(getString(R.string.needMultiplePermission));
                builder.setMessage(getString(R.string.needMultiplePermissionMsg));
                builder.setPositiveButton(getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(SplashActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
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
            } else {
                Toast.makeText(this, getString(R.string.unableToGetPermission), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setCancelable(false);
                builder.setTitle(getString(R.string.needMultiplePermission));
                builder.setMessage(getString(R.string.unableToGetPermission));
                builder.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });

                builder.show();
            }
        }
    }





//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        Intent bundle = getIntent();
//        if (bundle!=null){
//            status = bundle.getStringExtra("status");
//        }else {
//            status = "no";
//        }
//        Log.d("dddddd",status);
//    }

   /* public void getToken(){
        try {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            if (task.getResult().getToken()!=null) {
                                String token = task.getResult().getToken();
                                sp.edit().putString("token",token).apply();
                                Log.d(TAG,token);
                                if (!"".equalsIgnoreCase(sp.getString("uPhone", ""))) {
                                    //onUpdate(sp.getString("uPhone",""),sp.getString("token",""));
                                }else {
                                    Log.d("empty","empty");
                                }
                            }

                        }
                    });

        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    void onUpdate(){

        String mobile = sp.getString("uPhone","");
        String device_token =  sp.getString("token","");

        if (!"".equalsIgnoreCase(sp.getString("uPhone", ""))) {
            String url = ApiClient.WEBCARDBASE_URL+ "mobile/refresh/token";
            JSONObject params = new JSONObject();
            try {
                params.put("mobile",mobile);
                params.put("token",device_token);

            } catch (Exception e){
                e.printStackTrace();
            }

            RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
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
            });

            requestQueue.add(jor);
        }

    }



    //sendNotification notification = new sendNotification();
    //notification.execute(token);

}
