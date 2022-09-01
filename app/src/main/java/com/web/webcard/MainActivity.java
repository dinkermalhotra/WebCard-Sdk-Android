package com.web.webcard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import com.abisyscorp.ivalt.iValtAuthentication;
import com.abisyscorp.ivalt.model.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String deviceToken = "";
    ProgressBar progressBar;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.progressBar = (ProgressBar) findViewById(R.id.progress);
        iValtAuthentication.onNotificationReceived(this, iValtAuthentication.mMapData);
        findViewById(R.id.tvUSerDetail).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                iValtAuthentication.checkStatus(MainActivity.this, "1", new iValtAuthentication.OnUserDataListener() {
                    public void onResponseData(String status, UserData userData) {
                        if (status.equalsIgnoreCase("true")) {
                            Log.d("userDetailssss", userData.getName());
                            Toast.makeText(MainActivity.this,userData.getName(),Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(MainActivity.this,status,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        findViewById(R.id.tvRemoveError).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                iValtAuthentication.onRemoveUser(MainActivity.this, "1", new iValtAuthentication.OnRemoveUserListener() {
                    public void onRemove(boolean status) {
                        Toast.makeText(MainActivity.this, NotificationCompat.CATEGORY_STATUS + status, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        findViewById(R.id.tvText).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity mainActivity = MainActivity.this;
                iValtAuthentication.launch(mainActivity, "ramakant", "r@gmail.com", mainActivity.deviceToken,"1", "abcc", new iValtAuthentication.OnResultListener() {
                    public void onData(String status, String message) {
                        Toast.makeText(MainActivity.this, "" + status + "====" + message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        findViewById(R.id.tvTextUpdate).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        findViewById(R.id.tvTextLogin).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity mainActivity = MainActivity.this;
                iValtAuthentication.launchLogin(mainActivity, mainActivity.deviceToken, new iValtAuthentication.OnLoginResultListener() {
                    public void onData(String status, UserData userDetail) {
                        Log.d("userData12", "///" + "===" + status);
                        if (userDetail != null) {
                            Toast.makeText(MainActivity.this, "" + /*userDetail.getId()*/ "====" + status, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        findViewById(R.id.tvTextNoti).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                map.put("mobile", "9876255290");
                map.put("type", "website");
                map.put("request_for", "login");
                map.put("website", "website");
                map.put("token", "9876255290");
                iValtAuthentication.onNotificationReceived(MainActivity.this, map);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        getToken();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    /* access modifiers changed from: package-private */
    public void getToken() {
        this.progressBar.setVisibility(View.VISIBLE);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            public void onComplete(Task<String> task) {
                MainActivity.this.progressBar.setVisibility(View.GONE);
                if (!task.isSuccessful()) {
                    Log.w("mytokeenn", "Fetching FCM registration token failed", task.getException());
                    return;
                }
                MainActivity.this.deviceToken = task.getResult();
                Log.d("mytokeenn", MainActivity.this.deviceToken);
            }
        });
    }
}
