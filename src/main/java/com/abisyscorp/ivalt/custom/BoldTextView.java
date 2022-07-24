package com.abisyscorp.ivalt.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by ayushsingla on 19/12/16.
 */

public class BoldTextView extends AppCompatTextView {
    public BoldTextView(Context context) {
        super(context);
        init(context);
    }

    public BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-SemiBold.otf");
        setTypeface(font);
    }

}
