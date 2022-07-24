package com.abisyscorp.ivalt;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;


public class PrivacyPolicyActivity extends AppCompatActivity implements View.OnClickListener, OnPageChangeListener, OnLoadCompleteListener {

    FrameLayout frmBack;
    ImageView ivBack;
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        pdfView= findViewById(R.id.pdfView);
        frmBack = findViewById(R.id.frmBack);
        ivBack = findViewById(R.id.ivBack);
        frmBack.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        displayFromAsset("files/ivalt_privacy.pdf");
    }


    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;

        pdfView.fromAsset(assetFileName)
                .defaultPage(pageNumber)
                .enableSwipe(true)

                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onClick(View v) {
        if (v == frmBack){
            PrivacyPolicyActivity.this.finish();
        }

        if (v == ivBack){
            PrivacyPolicyActivity.this.finish();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
