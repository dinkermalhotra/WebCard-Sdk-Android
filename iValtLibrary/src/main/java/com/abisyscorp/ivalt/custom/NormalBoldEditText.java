package com.abisyscorp.ivalt.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by ayushsingla on 19/12/16.
 */

public class NormalBoldEditText extends AppCompatEditText {
    public NormalBoldEditText(Context context) {
        super(context);
        init(context);
    }

    public NormalBoldEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NormalBoldEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-SemiBold.otf");
        setTypeface(font);
    }

}
