package com.abisyscorp.ivalt;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.abisyscorp.ivalt.databinding.ActivityIntroScreenBinding;


public class IntroScreenActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityIntroScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_intro_screen);
        binding.btnStart.setOnClickListener(this);
        binding.linInfo.setOnClickListener(this);

        try {
            if (Constants.getDarkMode(IntroScreenActivity.this)){
                binding.tvAName.setTextColor(Color.WHITE);
                binding.tvAName1.setTextColor(Color.WHITE);
                binding.tvAName2.setTextColor(Color.WHITE);
            }else {
                binding.tvAName.setTextColor(Color.WHITE);
                binding.tvAName1.setTextColor(Color.WHITE);
                binding.tvAName2.setTextColor(Color.WHITE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        if (v == binding.btnStart){
            startActivity(new Intent(IntroScreenActivity.this, iValtRegistrationActivity.class));
        }
        if (v == binding.linInfo){
            showOption();
        }
    }

    public void showOption(){
        AlertDialog.Builder builder = new AlertDialog.Builder(IntroScreenActivity.this);
        View view = LayoutInflater.from(IntroScreenActivity.this).inflate(R.layout.custom_sheetlayout,null);
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
                startActivity(new Intent(IntroScreenActivity.this,AboutUsActivity.class));
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
                startActivity(new Intent(IntroScreenActivity.this,ContactActivity.class));
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
}
