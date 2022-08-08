package com.abisyscorp.ivalt;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.abisyscorp.ivalt.api.ApiClient;
import com.abisyscorp.ivalt.model.UserData;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class iValtAuthentication {

    public static String _supplier_id = "",_supplier="",_deviceToken;
    public static final int CALLBACK_LOGIN = 1989;
    public static final int CALLBACK_REGISTER = 1990;
    public static  Map<String,String> mMapData;

    public interface OnResultListener{
        void onData(String status,String message);
    }

    public interface OnLoginResultListener{
        void onData(String status, UserData userDetail);
    }

    public interface OnRemoveUserListener{
        void onRemove(boolean status);
    }

    public interface OnUserDataListener{
        void onResponseData(String status,UserData userData);
    }


    public static void launch(Context mContext,String _name,String _email,String deviceToken,String supplier_id,String supplier,OnResultListener listener){
        _supplier_id = supplier_id;
        _supplier = supplier;
        _deviceToken = deviceToken;
        //ProcessEnrollmentActivity.mInstance.initView(listener);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(new BroadcastReceiver() {
                                                                         @Override
                                                                         public void onReceive(Context context, Intent intent) {
                                                                            String status =  intent.getStringExtra("status");
                                                                            String message =  intent.getStringExtra("message");

                                                                             listener.onData(status,message);
                                                                             LocalBroadcastManager.getInstance(mContext).unregisterReceiver(this);
                                                                         }
                                                                     },
                new IntentFilter("iValtBroadCast"));
        mContext.startActivity(new Intent(mContext, iValtRegistrationActivity.class)
                .putExtra("_name",_name)
                .putExtra("_email",_email)
        );
    }

    public static void launchLogin(Activity mContext,String deviceToken,OnLoginResultListener listener){
        LocalBroadcastManager.getInstance(mContext).registerReceiver(new BroadcastReceiver() {
                                                                         @Override
                                                                         public void onReceive(Context context, Intent intent) {
                                                                             String status =  intent.getStringExtra("status");
                                                                             String userData =  intent.getStringExtra("userData");
                                                                             if (status.equalsIgnoreCase("true")){
                                                                                 try {
                                                                                     UserData userObj = new Gson().fromJson(userData,UserData.class);
                                                                                     listener.onData(status,userObj);
                                                                                 }catch (Exception e){
                                                                                     listener.onData(status,null);
                                                                                     e.printStackTrace();
                                                                                 }

                                                                             }else {
                                                                                 listener.onData(status,null);
                                                                             }


                                                                             LocalBroadcastManager.getInstance(mContext).unregisterReceiver(this);
                                                                         }
                                                                     },
                new IntentFilter("iValtBroadLogin"));
        _deviceToken = deviceToken;

        mContext.startActivity(new Intent(mContext, iValtLoginActivity.class));

    }

    public static void onNotificationReceived(Context mContext,Map<String,String> data){
        mMapData = data;
        //Toast.makeText(mContext.getApplicationContext(), "///"+new Gson().toJson(mMapData),Toast.LENGTH_LONG).show();
        if ((mMapData !=null?mMapData.size():0) > 0) {
            Intent intent = new Intent(mContext, iValtGlobalAuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    //sms
    //global auth
    //registration cponfrimation


    public static void onRemoveUser(Context context,String supplier_id,OnRemoveUserListener listener){

        String url = ApiClient.WEBCARDBASE_URL+ "remove-user";
        JSONObject params = new JSONObject();
        try {
            params.put("supplier_id",supplier_id);
        } catch (Exception e){
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("removeUser",response.toString());
                try {
                    if (response.getJSONObject("data").getString("status").equalsIgnoreCase("true")){
                        listener.onRemove(true);
                    }else {
                        listener.onRemove(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("removeUser","Error : "+error.getMessage());
                VolleyLog.e("removeUser","Error : "+error.getMessage());

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
                            if (jData.getString("status").equalsIgnoreCase("error")) {
                                listener.onRemove(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case 400:
                        json = new String(response.data);
                        try {
                            JSONObject jData = new JSONObject(json);
                            if (jData.getString("status").equalsIgnoreCase("error")) {
                                listener.onRemove(false);
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
                    strError = "server error, Try after some time.";
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
                Log.d("removeUser",strError);
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

    public static String getIMEI(Context context) {
        try {
            //TelephonyManager telephonyManager = (TelephonyManager) activity
            //.getSystemService(Context.TELEPHONY_SERVICE);
            //return telephonyManager.getDeviceId()!=null?telephonyManager.getDeviceId(): UUID.randomUUID().toString();
            return  Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }catch (Exception e){
            e.printStackTrace();
            return UUID.randomUUID().toString();
        }
    }



    public static void checkStatus(Context context,String supplier_id,OnUserDataListener listener){

        String url = ApiClient.BASE_IVURL+ "user/details";
        JSONObject params = new JSONObject();
        try {
            params.put("imei",getIMEI(context));
            params.put("supplier_id",supplier_id);
        } catch (Exception e){
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("userDetails",response.toString());
                try {
                    if (response.getJSONObject("data").getString("status").equalsIgnoreCase("true")){
                        JSONObject data = response.getJSONObject("data");
                        JSONObject details = data.getJSONObject("details");
                        JSONObject uData = details.getJSONObject("user");
                        UserData userData = new Gson().fromJson(uData.toString(),UserData.class);
                        listener.onResponseData(response.getJSONObject("data").getString("status"),userData);
                    }else {
                        listener.onResponseData(response.getJSONObject("data").getString("status"),null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("userDetails","Error : "+error.getMessage());
                        VolleyLog.e("userDetails","Error : "+error.getMessage());

                        NetworkResponse response = error.networkResponse;
                        Log.d("userDetails", response.statusCode + "//");
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
                                    if (jData.getString("status").equalsIgnoreCase("error")) {
                                        listener.onResponseData("false",null);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;

                            case 400:
                                json = new String(response.data);
                                try {
                                    JSONObject jData = new JSONObject(json);
                                    if (jData.getString("status").equalsIgnoreCase("error")) {
                                        listener.onResponseData("false",null);
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
                            strError = "server error, Try after some time.";
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
                        Log.d("removeUser",strError);
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
