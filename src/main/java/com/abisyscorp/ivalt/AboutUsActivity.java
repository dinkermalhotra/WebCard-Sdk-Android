package com.abisyscorp.ivalt;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.abisyscorp.ivalt.databinding.ActivityAboutUsBinding;


public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityAboutUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_about_us);
        binding.frmBack.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        try {

            if (Constants.getDarkMode(AboutUsActivity.this)) {
                binding.tvText.setTextColor(Color.WHITE);
                //binding.relMain.setBackgroundColor(Color.BLACK);
            } else {
                binding.tvText.setTextColor(Color.WHITE);
                //binding.relMain.setBackgroundColor(Color.WHITE);
            }
            binding.tvText2.setText("Version Info: iVALT® v"+Constants.getVersionName(AboutUsActivity.this));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == binding.frmBack){
            AboutUsActivity.this.finish();
        }
        if (v == binding.ivBack){
            AboutUsActivity.this.finish();
        }
    }
}
