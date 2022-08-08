package com.abisyscorp.ivalt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import com.abisyscorp.ivalt.api.ApiClient;
import com.abisyscorp.ivalt.databinding.ActivityOtpBinding;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class iValtOtpActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityOtpBinding binding;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        binding.btnSumbit.setOnClickListener(this);
        binding.linInfo.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);
        binding.btnResend.setOnClickListener(this);

        //Toast.makeText(iValtOtpActivity.this, iValtRegistrationActivity.countryCode+"//", Toast.LENGTH_SHORT).show();

        SpannableString ssNumber = new SpannableString("We have send you the verification code on "+iValtRegistrationActivity.countryCode+""+iValtRegistrationActivity.strMobile);
        //ss.setSpan(new ForegroundColorSpan(Color.BLACK), ss.length()-20, ss.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ssNumber.setSpan(new StyleSpan(Typeface.BOLD), ssNumber.length() - (iValtRegistrationActivity.countryCode+""+iValtRegistrationActivity.strMobile).length(), ssNumber.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ssNumber.setSpan(new ForegroundColorSpan(Color.parseColor("#021633")), ssNumber.length() - (iValtRegistrationActivity.countryCode+""+iValtRegistrationActivity.strMobile).length(), ssNumber.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        binding.tvAName1.setText(ssNumber);

        SpannableString ss = new SpannableString("Didn’t receive code? Resend");
        //ss.setSpan(new ForegroundColorSpan(Color.BLACK), ss.length()-20, ss.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), ss.length()-7, ss.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ff4057")), ss.length()-7, ss.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        binding.btnResend.setText(ss);

        binding.etOtp1.addTextChangedListener(new GenericTextWatcher(binding.etOtp1));
        binding.etOtp2.addTextChangedListener(new GenericTextWatcher(binding.etOtp2));
        binding.etOtp3.addTextChangedListener(new GenericTextWatcher(binding.etOtp3));
        binding.etOtp4.addTextChangedListener(new GenericTextWatcher(binding.etOtp4));

    }

    public void showOption(){
        AlertDialog.Builder builder = new AlertDialog.Builder(iValtOtpActivity.this);
        View view = LayoutInflater.from(iValtOtpActivity.this).inflate(R.layout.custom_sheetlayout,null);
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
                startActivity(new Intent(iValtOtpActivity.this,AboutUsActivity.class));
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
                startActivity(new Intent(iValtOtpActivity.this,ContactActivity.class));
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

    @Override
    public void onClick(View v) {
        if (v == binding.btnSumbit){

            String code = binding.etOtp1.getText().toString()+binding.etOtp2.getText().toString()+binding.etOtp3.getText().toString()+binding.etOtp4.getText().toString();
            if (TextUtils.isEmpty(code)){
                //showTimer();
                binding.etOtp1.setBackgroundResource(R.drawable.stroke_round_red);
                binding.etOtp2.setBackgroundResource(R.drawable.stroke_round_red);
                binding.etOtp3.setBackgroundResource(R.drawable.stroke_round_red);
                binding.etOtp4.setBackgroundResource(R.drawable.stroke_round_red);
                binding.errMsg.setVisibility(View.VISIBLE);
            } else {
                String deviceToken = Settings.Secure.getString(iValtOtpActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID); ;
                onVerified(iValtRegistrationActivity.strMobile,code, iValtRegistrationActivity.name, iValtRegistrationActivity.countryCode);
            }
        }
        if (v == binding.linInfo){
            showOption();
        }

        if (v == binding.btnBack){
            iValtOtpActivity.this.finish();
        }

        if (v == binding.btnResend){
            String number = iValtRegistrationActivity.countryCode+iValtRegistrationActivity.strMobile;
            Log.d("inputmobile", number);
            binding.etOtp1.setBackgroundResource(R.drawable.stroke_round);
            binding.etOtp2.setBackgroundResource(R.drawable.stroke_round);
            binding.etOtp3.setBackgroundResource(R.drawable.stroke_round);
            binding.etOtp4.setBackgroundResource(R.drawable.stroke_round);
            sendOtp(number);
        }
    }

    public class GenericTextWatcher implements TextWatcher
    {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
           /* binding.etOtp1.setBackgroundResource(R.drawable.stroke_round);
            binding.etOtp2.setBackgroundResource(R.drawable.stroke_round);
            binding.etOtp3.setBackgroundResource(R.drawable.stroke_round);
            binding.etOtp4.setBackgroundResource(R.drawable.stroke_round);*/
            String text = editable.toString();
            int id = view.getId();
            if (id == R.id.etOtp1) {
                if (text.length() == 1)
                    binding.etOtp2.requestFocus();
            } else if (id == R.id.etOtp2) {
                if (text.length() == 1)
                    binding.etOtp3.requestFocus();
                else if (text.length() == 0)
                    binding.etOtp1.requestFocus();
            } else if (id == R.id.etOtp3) {
                if (text.length() == 1)
                    binding.etOtp4.requestFocus();
                else if (text.length() == 0)
                    binding.etOtp2.requestFocus();
            } else if (id == R.id.etOtp4) {
                if (text.length() == 0)
                    binding.etOtp3.requestFocus();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
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
        RequestQueue requestQueue = Volley.newRequestQueue(iValtOtpActivity.this);
        JsonObjectRequest jor = new JsonObjectRequest(1, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("sendotpresponse", response.toString());
                iValtOtpActivity.this.binding.loader.setVisibility(View.GONE);
                try {
                    if(response.has("data")) {
                        JSONObject data = response.getJSONObject("data");
                        if (data.getString("status").equalsIgnoreCase("true")) {
                            Constants.showInformation(iValtOtpActivity.this, data.optString("message") );
                            showTimer();
                        }else {
                            Constants.showInformation(iValtOtpActivity.this, data.optString("message") );
                        }
                    }
                } catch (Exception e) {
                    iValtOtpActivity.this.binding.loader.setVisibility(View.GONE);
                    Constants.showInformation(iValtOtpActivity.this, "Error something went wrong");
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
                Constants.showInformation(iValtOtpActivity.this,strError);
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


    public String getIMEI(Activity activity) {
        try {
            //TelephonyManager telephonyManager = (TelephonyManager) activity
                    //.getSystemService(Context.TELEPHONY_SERVICE);
            //return telephonyManager.getDeviceId()!=null?telephonyManager.getDeviceId(): UUID.randomUUID().toString();
            return  Settings.Secure.getString(iValtOtpActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }catch (Exception e){
            e.printStackTrace();
            return UUID.randomUUID().toString();
        }
    }

    void onVerified(final String mobile, String user_code,String name, String country_code){

        binding.etOtp1.setBackgroundResource(R.drawable.stroke_round);
        binding.etOtp2.setBackgroundResource(R.drawable.stroke_round);
        binding.etOtp3.setBackgroundResource(R.drawable.stroke_round);
        binding.etOtp4.setBackgroundResource(R.drawable.stroke_round);

        binding.loader.setVisibility(View.VISIBLE);
        binding.errMsg.setVisibility(View.GONE);
        //String url = ApiClient.BASE_URL+"confirm/register";
        String url = ApiClient.WEBCARDBASE_URL+"register/mobile/user";
        JSONObject params = new JSONObject();
        try {
            params.put("mobile",mobile);
            params.put("user_code",user_code);
            params.put("name",name);
            params.put("imei",getIMEI(iValtOtpActivity.this));
            params.put("country_code",country_code);
            params.put("device_token",iValtAuthentication._deviceToken);
            params.put("platform","android");
            params.put("email", iValtRegistrationActivity.email);
            params.put("supplier_id", iValtAuthentication._supplier_id);
            params.put("supplier", iValtAuthentication._supplier);

            Log.d("registrationParams",params.toString());
        } catch (Exception e){
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(iValtOtpActivity.this);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("verifyregisterresp",response.toString());
                //{"status":"success","message":"Successfully registered!","id":21,"mobile":"7461846199"}
                binding.loader.setVisibility(View.GONE);
                try {

                    //{"data":{"status":true,"message":"User registered successfully.","details":{"id":264,"mobile":"6280227476","enrollmentStatus":0,"is_enrolled":0}},"error":null,"debug":{"timestamp":"2022-07-23T06:07:43.276078Z","activityId":"8P1ZA9WRE2BL3rY9LeEw6ZjDQRj63AVi"}}
                    if (response.has("data")) {
                        JSONObject data = response.getJSONObject("data");
                        if (data.getString("status").equalsIgnoreCase("true")) {
                            JSONObject details = data.getJSONObject("details");
                            binding.etOtp1.setBackgroundResource(R.drawable.stroke_round);
                            binding.etOtp2.setBackgroundResource(R.drawable.stroke_round);
                            binding.etOtp3.setBackgroundResource(R.drawable.stroke_round);
                            binding.etOtp4.setBackgroundResource(R.drawable.stroke_round);
                            binding.errMsg.setVisibility(View.GONE);
                            if (details.has("id")){
                                String id = details.getString("id");
                                sp.edit().putString("id",id).apply();
                                sp.edit().putString("uPhone",mobile).apply();
                                sp.edit().putString("uName",name).apply();
                                sp.edit().putString("country_code",country_code).apply();
                                sp.edit().putString("status","no").apply();
                                sp.edit().putString("hasoptp","yes").apply();
                                sp.edit().putString("email", iValtRegistrationActivity.email).apply();
                                Constants.showInformation(iValtOtpActivity.this, data.optString("message") );
                                //Bundle bundle = new Bundle();
                                //bundle.putBoolean("status",false);
                                //isEnrolled(mobile);
                                sp.edit().putString("is_enrolled","true").apply();
                                Intent intent = new Intent(iValtOtpActivity.this, iValtRegistrationCompletedActivity.class);
                                startActivity(intent);
                                iValtOtpActivity.this.finish();
                                //startActivity(new Intent(OtpActivity.this,ProcessEnrollmentActivity.class));
                                //OtpActivity.this.finish();
                            }
                        }else {
                            binding.etOtp1.setBackgroundResource(R.drawable.stroke_round_red);
                            binding.etOtp2.setBackgroundResource(R.drawable.stroke_round_red);
                            binding.etOtp3.setBackgroundResource(R.drawable.stroke_round_red);
                            binding.etOtp4.setBackgroundResource(R.drawable.stroke_round_red);
                            binding.errMsg.setVisibility(View.VISIBLE);
                            Constants.showInformation(iValtOtpActivity.this, data.optString("message") );
                        }
                    }else {
                        binding.etOtp1.setBackgroundResource(R.drawable.stroke_round_red);
                        binding.etOtp2.setBackgroundResource(R.drawable.stroke_round_red);
                        binding.etOtp3.setBackgroundResource(R.drawable.stroke_round_red);
                        binding.etOtp4.setBackgroundResource(R.drawable.stroke_round_red);
                        binding.errMsg.setVisibility(View.VISIBLE);
                        Constants.showInformation(iValtOtpActivity.this, response.optString("message") );
                    }

                }catch (Exception e){
                    binding.loader.setVisibility(View.GONE);
                    binding.errMsg.setVisibility(View.GONE);
                    Constants.showInformation(iValtOtpActivity.this,"Error something went wrong");
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.loader.setVisibility(View.GONE);
                binding.errMsg.setVisibility(View.VISIBLE);
                binding.etOtp1.setBackgroundResource(R.drawable.stroke_round_red);
                binding.etOtp2.setBackgroundResource(R.drawable.stroke_round_red);
                binding.etOtp3.setBackgroundResource(R.drawable.stroke_round_red);
                binding.etOtp4.setBackgroundResource(R.drawable.stroke_round_red);
                //showTimer();
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
                Constants.showInformation(iValtOtpActivity.this,strError);
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

    public void showTimer(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.etOtp1.setBackgroundResource(R.drawable.stroke_round);
                binding.etOtp2.setBackgroundResource(R.drawable.stroke_round);
                binding.etOtp3.setBackgroundResource(R.drawable.stroke_round);
                binding.etOtp4.setBackgroundResource(R.drawable.stroke_round);
                binding.errMsg.setVisibility(View.GONE);
            }
        },2000);
    }

    void isEnrolled(final String mobile){
        binding.loader.setVisibility(View.VISIBLE);
        String url = ApiClient.WEBCARDBASE_URL+"check-user-enrollment";
        //String url = ApiClient.WEBCARDBASE_URL+"is_enrolled";
        JSONObject params = new JSONObject();
        try {
            params.put("supplier_id", iValtAuthentication._supplier_id);

            Log.d("isEnrolled",mobile);
        } catch (Exception e){
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(iValtOtpActivity.this);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("isEnrolled",response.toString());
                //{"status":"success","message":"Successfully registered!","id":21,"mobile":"7461846199"}
                binding.loader.setVisibility(View.GONE);
                try {
                    if (response.getString("status").equalsIgnoreCase("success")) {
                        String is_enrolled= response.getString("enrollmentStatus");


                    } else {
                        iValtOtpActivity.this.finish();
                        Constants.showInformation(iValtOtpActivity.this, response.optString("message") );
                    }
                }catch (Exception e){
                    binding.loader.setVisibility(View.GONE);
                    Constants.showInformation(iValtOtpActivity.this,"Error something went wrong");
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.loader.setVisibility(View.GONE);
                Log.d("isEnrolled","Error : "+error.getMessage());
                VolleyLog.e("isEnrolled","Error : "+error.getMessage());
                NetworkResponse response = error.networkResponse;
                Log.d("removeUser", response.statusCode + "//");
                String json = "";
                String strError = "";
                switch (response.statusCode) {
                    case 404:
                        strError = "Page not Found";
                        break;
                    case 500:
                        strError = "Server error, Try after some time.";
                        break;
                    case 422:
                        json = new String(response.data);
                        try {
                            JSONObject jData = new JSONObject(json);
                            Log.d("errorMessage",jData.toString());
                            if (jData.getString("status").equalsIgnoreCase("success")) {
                                String is_enrolled= jData.getString("enrollmentStatus");
                                sp.edit().putString("is_enrolled",is_enrolled).apply();
                                Intent intent = new Intent(iValtOtpActivity.this, iValtRegistrationCompletedActivity.class);
                                startActivity(intent);
                                iValtOtpActivity.this.finish();

                            }else {
                                iValtOtpActivity.this.finish();
                                Constants.showInformation(iValtOtpActivity.this, jData.getString("message") != null ? jData.getString("message") : "");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case 400:
                        json = new String(response.data);
                        try {
                            JSONObject jData = new JSONObject(json);
                            Log.d("errorMessage",jData.toString());
                            if (jData.getString("status").equalsIgnoreCase("success")) {
                                String is_enrolled= jData.getString("enrollmentStatus");
                                sp.edit().putString("is_enrolled",is_enrolled).apply();
                                Intent intent = new Intent(iValtOtpActivity.this, iValtRegistrationCompletedActivity.class);
                                startActivity(intent);
                                iValtOtpActivity.this.finish();

                            }else {
                                iValtOtpActivity.this.finish();
                                Constants.showInformation(iValtOtpActivity.this, jData.getString("message") != null ? jData.getString("message") : "");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    default:
                }

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
                Constants.showInformation(iValtOtpActivity.this,strError);
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




//    public void onVerfiyOrRegister(String mobile,String user_code,String country_code,String device_token){
//        binding.loader.setVisibility(View.VISIBLE);
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        apiInterface.verifyAndRegister(Constants.toBody(mobile),Constants.toBody(user_code),Constants.toBody(country_code),Constants.toBody(device_token)).enqueue(new Callback<SendSmsResponse>() {
//            @Override
//            public void onResponse(Call<SendSmsResponse> call, Response<SendSmsResponse> response) {
//                binding.loader.setVisibility(View.GONE);
//                if (response.isSuccessful()){
//                    if (response.body().getStatus().equalsIgnoreCase("success")) {
//                        Constants.showInformation(OtpActivity.this, response.body().getMessage() != null ? response.body().getMessage() : "");
//                    }else {
//                        Constants.showInformation(OtpActivity.this, response.body().getMessage() != null ? response.body().getMessage() : "");
//                    }
//                } else {
//                    Constants.showInformation(OtpActivity.this,"Error something went wrong");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SendSmsResponse> call, Throwable t) {
//                binding.loader.setVisibility(View.GONE);
//                Constants.showInformation(OtpActivity.this,t.getMessage()!=null ? t.getMessage() : "Error");
//            }
//        });
//    }

}
