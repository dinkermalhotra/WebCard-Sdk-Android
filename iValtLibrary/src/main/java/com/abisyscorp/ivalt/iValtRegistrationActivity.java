package com.abisyscorp.ivalt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.abisyscorp.ivalt.countrypicker.CountryPicker;
import com.abisyscorp.ivalt.countrypicker.Theme;
import com.abisyscorp.ivalt.databinding.ActivityLoginmainBinding;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
/*import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;*/
import org.json.JSONException;
import org.json.JSONObject;

import com.abisyscorp.ivalt.api.ApiClient;
//import com.hbb20.CountryCodePicker;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class iValtRegistrationActivity extends AppCompatActivity implements OnClickListener {
    public static String countryCode = "";
    public static String strMobile = "";
    public static String email = "";
    public static String name = "";
    Button btnSumbit;
    ActivityLoginmainBinding binding;
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
    SharedPreferences sp;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loginmain);
        binding.btnSumbit.setOnClickListener(this);
        this.binding.tvCountryCode.setOnClickListener(this);
        this.binding.linCode.setOnClickListener(this);
        this.binding.btnBack.setOnClickListener(this);
        //this.binding.linInfo.setOnClickListener(this);
        //binding.etName.setFocusable(false);
        //binding.etName.setFocusableInTouchMode(false);
        //binding.etEmail.setFocusable(false);
        //binding.etEmail.setFocusableInTouchMode(false);

        binding.etEmail.setText(getIntent().getStringExtra("_email")!=null?getIntent().getStringExtra("_email"):"");
        binding.etName.setText(getIntent().getStringExtra("_name")!=null?getIntent().getStringExtra("_name"):"");
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        SpannableString ss = new SpannableString("I agree with iVALT’s Terms and Conditions");
        //ss.setSpan(new ForegroundColorSpan(Color.BLACK), ss.length()-20, ss.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 20, ss.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ff4057")), 20, ss.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(iValtRegistrationActivity.this, PrivacyPolicyActivity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                //super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
               if (binding.checkbox.isChecked()){
                   binding.checkbox.setChecked(false);
               }else {
                   binding.checkbox.setChecked(true);
               }
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 21, ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ss.setSpan(clickableSpan1, 0, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        binding.tvPolicy.setHighlightColor(Color.TRANSPARENT);
        binding.tvPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvPolicy.setText(ss);

        try {

          /*  if (Constants.getDarkMode(LoginMainActivity.this)){
                binding.tvAName.setTextColor(Color.WHITE);
                binding.tvAName1.setTextColor(Color.WHITE);
            }else {
                binding.tvAName.setTextColor(Color.WHITE);
                binding.tvAName1.setTextColor(Color.WHITE);
            }*/
            /*binding.linAgree.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(iValtRegistrationActivity.this,"clicked",Toast.LENGTH_LONG).show();
                    if (binding.checkbox.isChecked()){
                        binding.checkbox.setChecked(false);
                    } else {
                        binding.checkbox.setChecked(true);
                    }
                }
            });*/
        }catch (Exception e){
            e.printStackTrace();
        }

        initLocation();




    }
    
    void initLocation(){
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
                    //proceedAfterPermission();

                }
            };
        }catch (Exception e){
            e.printStackTrace();
        }

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
                            resolvable.startResolutionForResult(iValtRegistrationActivity.this, REQUEST_CODE_CHECK_SETTINGS);
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
                iValtRegistrationActivity.this.finish();
                //user clicked cancel: informUserImportanceOfLocationAndPresentRequestAgain();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
        //getToken();
    }
    private static final String TAG = "MyDToken";
    /*public void getToken(){
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

    public void checkPermissions(){
        if(ActivityCompat.checkSelfPermission(iValtRegistrationActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(iValtRegistrationActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(iValtRegistrationActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(iValtRegistrationActivity.this,permissionsRequired[1])){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(iValtRegistrationActivity.this);
                builder.setCancelable(false);
                builder.setTitle(getString(R.string.needMultiplePermission));
                builder.setMessage(getString(R.string.needMultiplePermissionMsg));
                builder.setPositiveButton(getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(iValtRegistrationActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(iValtRegistrationActivity.this);
                builder.setCancelable(false);
                builder.setTitle(getString(R.string.needMultiplePermission));
                builder.setMessage(getString(R.string.needMultiplePermissionMsg));
                builder.setPositiveButton(getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", iValtRegistrationActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(iValtRegistrationActivity.this, getString(R.string.gotoPermission), Toast.LENGTH_SHORT).show();
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
                ActivityCompat.requestPermissions(iValtRegistrationActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }
            sp.edit().putBoolean(permissionsRequired[0],true).apply();
        } else {
            //You already have the permission, just go ahead.
            enableLocationSettings();
        }
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
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(iValtRegistrationActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(iValtRegistrationActivity.this,permissionsRequired[1])){

                AlertDialog.Builder builder = new AlertDialog.Builder(iValtRegistrationActivity.this);
                builder.setCancelable(false);
                builder.setTitle(getString(R.string.needMultiplePermission));
                builder.setMessage(getString(R.string.needMultiplePermissionMsg));
                builder.setPositiveButton(getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(iValtRegistrationActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(iValtRegistrationActivity.this);
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



    //^([\w-]|(?<!\.)\.)+[a-zA-Z0-9]@[a-zA-Z0-9]([\w\-]+)((\.([a-zA-Z]){2,9})+)$
    //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z?-]+\\.+[a-z]+";

    public Boolean isValidEmail(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void onClick(View v) {
        if (v == this.binding.btnSumbit) {
            if (TextUtils.isEmpty(this.binding.tvCountryCode.getText().toString())) {
                Constants.showInformation(this, "Please select country code");
            } else if (TextUtils.isEmpty(this.binding.etNumber.getText().toString())) {
                Constants.showInformation(this, "Please enter mobile number");
            } else if (this.binding.etNumber.getText().toString().length() < 10) {
                Constants.showInformation(this, "mobile number is not valid");
            }else if (TextUtils.isEmpty(binding.etName.getText().toString())) {
                Constants.showInformation(this, "Please enter name");
            }else if (TextUtils.isEmpty(binding.etEmail.getText().toString().toLowerCase())) {
                Constants.showInformation(this, "Please enter email id");
            }else if (!binding.etEmail.getText().toString().toLowerCase().matches(emailPattern)) {
                Constants.showInformation(this, "Please enter valid email id");
            } else if (!binding.checkbox.isChecked()){
                Constants.showInformation(this, "Please agree to iValt's terms and conditions");
            }else {
                email = binding.etEmail.getText().toString().toLowerCase();
                countryCode = this.binding.tvCountryCode.getText().toString();
                strMobile = this.binding.etNumber.getText().toString();
                name = this.binding.etName.getText().toString();
                String number = countryCode+strMobile;
                Log.d("inputmobile", number);
                sendOtp(number);
            }
        }
        if (v == this.binding.tvCountryCode) {
            showCountryList();
        }
        if (v == this.binding.linCode) {
            showCountryList();
        }
       /* if (v == binding.linInfo){
            showOption();
        }*/
        if (v == binding.btnBack){
            iValtRegistrationActivity.this.finish();
        }
    }

    public void showOption(){
        AlertDialog.Builder builder = new AlertDialog.Builder(iValtRegistrationActivity.this);
        View view = LayoutInflater.from(iValtRegistrationActivity.this).inflate(R.layout.custom_sheetlayout,null);
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
                startActivity(new Intent(iValtRegistrationActivity.this,AboutUsActivity.class));
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
                startActivity(new Intent(iValtRegistrationActivity.this,ContactActivity.class));
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

    public void sendOtp(String mobile) {
        this.binding.loader.setVisibility(View.VISIBLE);
        String url = ApiClient.BASE_IVURLWP+"send/sms";
        JSONObject params = new JSONObject();
        try {
            params.put("mobile", mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(iValtRegistrationActivity.this);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("sendotpresponse", response.toString());
                iValtRegistrationActivity.this.binding.loader.setVisibility(View.GONE);
                try {
                    if(response.has("data")) {
                        JSONObject data = response.getJSONObject("data");
                        if (data.getString("status").equalsIgnoreCase("true")) {
                            Constants.showInformation(iValtRegistrationActivity.this, data.optString("message"));
                            iValtRegistrationActivity.this.startActivity(new Intent(iValtRegistrationActivity.this, iValtOtpActivity.class));
                            iValtRegistrationActivity.this.finish();
                        }else {
                            Constants.showInformation(iValtRegistrationActivity.this, data.optString("message"));
                        }
                    }else {
                        Constants.showInformation(iValtRegistrationActivity.this, response.optString("message") );
                    }
                  /*  if (response.getString("status").equalsIgnoreCase("success")) {

                    } else {

                    }*/
                } catch (Exception e) {
                    iValtRegistrationActivity.this.binding.loader.setVisibility(View.GONE);
                    Constants.showInformation(iValtRegistrationActivity.this, "Error something went wrong");
                    e.printStackTrace();
                }
            }
        },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.loader.setVisibility(View.GONE);
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
                Constants.showInformation(iValtRegistrationActivity.this,strError);
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

    CountryPicker picker;
    public void showCountryList() {

        picker = CountryPicker.newInstance("Select Country", Theme.LIGHT);  // Set Dialog Title and Theme
        picker.setListener((name, code, dialCode, flagDrawableResID) -> {

            String cCode = ""+dialCode;
            //Toast.makeText(iValtRegistrationActivity.this, ""+picker.getSelectedCountryCodeWithPlus(), Toast.LENGTH_SHORT).show();
            iValtRegistrationActivity.this.binding.tvCountryCode.setText(cCode);
            binding.ivImageFloag.setImageResource(flagDrawableResID);

            picker.dismiss();

        });

        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");







      /*  final CountryPicker picker = new CountryPicker();
        picker.setListener(new CountryPickerListener() {
            public void onSelectCountry(String s, String s1, String s2, int i) {
                iValtRegistrationActivity.this.binding.tvCountryCode.setText(s2);
                binding.ivImageFloag.setImageResource(i);
                picker.dismiss();
            }
        });
        picker.show(getSupportFragmentManager(), "Select Country");*/
    }
}
