package com.abisyscorp.ivalt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.abisyscorp.ivalt.api.ApiClient;
import com.abisyscorp.ivalt.databinding.ActivityContactBinding;
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

import org.json.JSONObject;

import java.util.regex.Pattern;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityContactBinding binding;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contact);
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        binding.etMobile.setText(sp.getString("uPhone", ""));
        binding.etEmail.setText(sp.getString("email",""));
        binding.frmBack.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.btnSend.setOnClickListener(this);
        if (Constants.getDarkMode(ContactActivity.this)) {
            binding.etEmail.setTextColor(Color.WHITE);
            binding.etMessage.setTextColor(Color.WHITE);
            binding.etMobile.setTextColor(Color.WHITE);
            binding.tvEmail.setTextColor(Color.WHITE);
            binding.tvMessage.setTextColor(Color.WHITE);
            binding.tvMobile.setTextColor(Color.WHITE);
            //binding.relMain.setBackgroundColor(Color.BLACK);
        } else {
            binding.etEmail.setTextColor(Color.WHITE);
            binding.etMessage.setTextColor(Color.WHITE);
            binding.etMobile.setTextColor(Color.WHITE);
            binding.tvEmail.setTextColor(Color.WHITE);
            binding.tvMessage.setTextColor(Color.WHITE);
            binding.tvMobile.setTextColor(Color.WHITE);
            //binding.relMain.setBackgroundColor(Color.WHITE);
        }
    }

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z?-]+\\.+[a-z]+";

    @Override
    public void onClick(View v) {
        if (v == binding.frmBack){
            ContactActivity.this.finish();
        }
        if (v == binding.ivBack){
            ContactActivity.this.finish();
        }
        if (v == binding.btnSend){
            String message = binding.etMessage.getText().toString();
            String email = binding.etEmail.getText().toString();
            String mobile = binding.etMobile.getText().toString();
            if (TextUtils.isEmpty(email.toLowerCase())){
                Constants.showInformation(ContactActivity.this, "Please enter email");
            } else if (!binding.etEmail.getText().toString().toLowerCase().matches(emailPattern)){
                Constants.showInformation(ContactActivity.this, "Please enter valid email");
            }else if (TextUtils.isEmpty(mobile)){
                Constants.showInformation(ContactActivity.this, "Please enter mobile number");
            }else if (this.binding.etMobile.getText().toString().length() < 10) {
                Constants.showInformation(this, "mobile number is not valid");
            }else if (TextUtils.isEmpty(message)){
                Constants.showInformation(ContactActivity.this, "Please enter message");
            }else {
                onSend(mobile, message, email);
            }
        }
    }

    public boolean checkEmail(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    void onSend(final String mobile, String message, String email){
        binding.loader.setVisibility(View.VISIBLE);
        String url = ApiClient.BASE_IVURL+"contact/support";
        JSONObject params = new JSONObject();
        try {
            params.put("mobile",mobile);
            params.put("message",message);
            params.put("email",email);

            Log.d("contactusresp",mobile+" // "+message+" // "+" // "+email);
        } catch (Exception e){
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(ContactActivity.this);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("contactusresp",response.toString());
                //{"status":"success","message":"Successfully registered!","id":21,"mobile":"7461846199"}
                binding.loader.setVisibility(View.GONE);
                try {
                    if (response.getString("status").equalsIgnoreCase("success")) {
                        binding.etMobile.setText("");
                        binding.etEmail.setText("");
                        binding.etMessage.setText("");
                        Constants.showInformation(ContactActivity.this, response.getString("message") != null ? response.getString("message") : "");
                    }else {
                        Constants.showInformation(ContactActivity.this, response.getString("message") != null ? response.getString("message") : "");
                    }
                }catch (Exception e){
                    binding.loader.setVisibility(View.GONE);
                    Constants.showInformation(ContactActivity.this,"Error something went wrong");
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.loader.setVisibility(View.GONE);
                Log.d("contactusresp","Error : "+error.getMessage());
                VolleyLog.e("contactusresp","Error : "+error.getMessage());
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
                Constants.showInformation(ContactActivity.this,strError);
            }
        });

        requestQueue.add(jor);
    }
}
