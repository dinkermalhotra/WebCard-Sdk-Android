package com.abisyscorp.ivalt;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.res.Configuration.UI_MODE_NIGHT_NO;
import static android.content.res.Configuration.UI_MODE_NIGHT_UNDEFINED;

public class Constants {
    /*public static RequestBody toBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }*/



    //new changes...

    public static void showInformation(Context context, String message) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
        TextView textView =  view.findViewById(R.id.tvMessage);
        textView.setText(message);
        toast.setView(view);
        toast.setGravity(80, 0, 200);
        toast.show();
    }

    public static boolean getDarkMode(Context context){
        int nightModeFlags =
                context.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags==Configuration.UI_MODE_NIGHT_YES){
            return true;
        }else if (nightModeFlags==UI_MODE_NIGHT_NO){
            return false;
        }else if (nightModeFlags==UI_MODE_NIGHT_UNDEFINED){
            return false;
        }else {
            return false;
        }
        /*switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                doStuff();
                break;

            case UI_MODE_NIGHT_NO:
                doStuff();
                break;

            case UI_MODE_NIGHT_UNDEFINED:
                doStuff();
                break;
        }*/
    }

    public static String getVersionName(Context context){
        String version = "1.3.2";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
            Log.d("versionname",version);
        } catch (Exception e) {
            version = "1.3.2";
            e.printStackTrace();
        }
        return version;
    }

}
