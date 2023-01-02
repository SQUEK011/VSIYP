package com.example.vsiyp.ui.mediaeditor.menu;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditItemViewModel extends ViewModel {

    private MutableLiveData<EditMenuBean> itemsFirstSelected = new MutableLiveData<>();

    private MutableLiveData<EditMenuBean> itemsSecondSelected = new MutableLiveData<>();

    private MutableLiveData<Boolean> isShowSecondItem = new MutableLiveData<>();

    public EditItemViewModel() {

    }

    public MutableLiveData<EditMenuBean> getItemsFirstSelected() {
        return itemsFirstSelected;
    }

    public MutableLiveData<EditMenuBean> getItemsSecondSelected() {
        return itemsSecondSelected;
    }

    public void setIsShowSecondItem(boolean isShowSecondItem) {
        this.isShowSecondItem.postValue(isShowSecondItem);
    }

    public MutableLiveData<Boolean> getIsShowSecondItem() {
        return isShowSecondItem;
    }
}

