package com.abisyscorp.ivalt;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/*import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;*/

//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 *
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 *
 * <intent-filter>
 *   <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */
public class MyFirebaseMessagingService {
    public static String type = "",requestFor="",token="",mobile="",website="",public_key="";
    //iValtAuthentication.mMapData =
    /*extends
} FirebaseMessagingService {
    public static String type = "",requestFor="",token="",mobile="",website="";

    private static final String TAG = "MyFirebaseMsgService";
    //public static boolean isFromNotification = false;
    //public static String type = "",requestFor="",token="",mobile="",website="";
    SharedPreferences sp;
    String str = "";
    String title = "";
    public static int zoombasicActivity =0;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size()>0) {
                        //{mobile=9569000579, user_id=15, type=wordpress, token=MyIKdONz68mYiwVHDlqI, request_for=login, website=http://baldevkrishan.com}
            sp.edit().putBoolean("isFromNotification", true).commit();
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().toString());
            iValtAuthentication.onNotificationReceived(this,remoteMessage.getData());
             iValtAuthentication.mMapData = remoteMessage.getData()


            try {
                str = remoteMessage.getData().toString();

                String data = "";
                data = str.replace("{message=","").replace("}}","}");
                JSONObject jData = new JSONObject(data);
                Log.d("dddddd1", jData.toString());
                //str = str.replace(" ", "");
                //str = str.replace("=", "\":\"");
                //str = str.replace(",", "\",\"");
                //str = str.replace("{", "{\"");
                //str = str.replace("}", "\"}");
                Log.d(TAG, str);
                if(zoombasicActivity==1)
                {
                    Intent intent=new Intent("f_Filter");
                    intent.putExtra("status",true);
                    intent.putExtra("dataS",str);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    return;
                }else {
                    if (jData!=null){
                        if (jData.has("title")){
                            title = jData.getString("title");
                        }else {
                            title = "New Wordpress Login Request";
                        }
                    }else {
                        title = "New Wordpress Login Request";
                    }
                    sendNotification(title, str);
                }

                //sendNotification("message"+remoteMessage.getData().toString(), str);
                //New Wordpress Login Request

                //Log.d(TAG,mobile+"\n"+type+"\n"+requestFor+"\n"+website+"\n"+token);
//                sendNotification(str);
                //array('title' => 'Wordpress Login Request', 'body' =>   'New wordpress login request' ,'sound'=>'Default','image'=>'Notification Image'),

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            sp.edit().putBoolean("isFromNotification", true).commit();
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            str = remoteMessage.getNotification().getBody();

//        }

            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
        }



    }
    // [END receive_message]


    // [START on_new_token]

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }



    private void sendNotification(String messageBody,String json) {
        //Bundle bundle = new Bundle();
        //bundle.putBoolean("status",true);

        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("status",true);
        intent.putExtra("dataS",json);


        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent pendingIntent =
//                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}
*/
}