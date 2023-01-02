package com.example.vsiyp.ui.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class TextEditView extends EditText {
    public TextEditView(Context context) {
        super(context);
    }

    public TextEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == 1) {
            if (mShowKeyBord != null) {
                mShowKeyBord.showKeyBord(true);
            }
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public ShowKeyBord mShowKeyBord;

    public void setShowKeyBord(ShowKeyBord mShowKeyBord) {
        this.mShowKeyBord = mShowKeyBord;
    }

    public interface ShowKeyBord {
        void showKeyBord(boolean isShow);
    }
}

