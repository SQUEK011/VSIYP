package com.example.vsiyp.ui.mediaeditor.audio.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class SoundEffectMarqueTextView extends TextView {
    public SoundEffectMarqueTextView(Context context) {
        super(context);
    }

    public SoundEffectMarqueTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SoundEffectMarqueTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SoundEffectMarqueTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
