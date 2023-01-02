package com.example.vsiyp.ui.mediaeditor.texts.fragment;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.BaseFragment;
import com.example.vsiyp.ui.mediaeditor.texts.viewmodel.TextEditViewModel;
import com.huawei.hms.videoeditor.sdk.bean.HVEWordStyle;

public class KeyBoardFragment extends BaseFragment {
    private TextEditViewModel textEditViewModel;

    private Context fragmentContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentContext = getContext();
        if (fragmentContext != null && (fragmentContext instanceof ViewModelStoreOwner)) {
            textEditViewModel =
                    new ViewModelProvider((ViewModelStoreOwner) fragmentContext).get(TextEditViewModel.class);
            HVEWordStyle wordStyle = textEditViewModel.getLastWordStyle();
            if (wordStyle == null) {
                textEditViewModel.setDefWordStyle(0);
            }
        }
    }

    @Override
    protected void initViewModelObserve() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.panel_keyboard_edit;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initObject() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected int setViewLayoutEvent() {
        return 0;
    }
}

