package com.example.vsiyp.ui.mediaeditor.materialedit;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.huawei.hms.videoeditor.sdk.asset.HVEAsset;
import com.huawei.hms.videoeditor.sdk.asset.HVEVisibleAsset;
import com.huawei.hms.videoeditor.sdk.asset.HVEWordAsset;

import java.util.ArrayList;
import java.util.List;

public class MaterialEditViewModel extends AndroidViewModel {

    private static final String TAG = "MaterialViewModel";

    private final List<MaterialEditData> mSelectMaterialList = new ArrayList<>();

    public MutableLiveData<List<MaterialEditData>> mSelectMaterials = new MutableLiveData<>();

    public final MutableLiveData<Boolean> isMaterialLayoutShow = new MutableLiveData<>(true);

    public MutableLiveData<MaterialEditData> mStickerEdit = new MutableLiveData<>();

    public MutableLiveData<MaterialEditData> mTextDefaultEdit = new MutableLiveData<>();

    public MutableLiveData<MaterialEditData> mTextTemplateEdit = new MutableLiveData<>();

    public MutableLiveData<MaterialEditData> mMaterialCopy = new MutableLiveData<>();

    public MutableLiveData<MaterialEditData> mMaterialDelete = new MutableLiveData<>();

    private final MutableLiveData<String> mText = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isStickerEditState = new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> isTextEditState = new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> isTextTemplateEditState = new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> isTextTrailerEditState = new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> refreshState = new MutableLiveData<>();

    public MaterialEditViewModel(@NonNull Application application) {
        super(application);
    }

    public void clearMaterialEditData() {
        mSelectMaterialList.clear();
        mSelectMaterials.postValue(mSelectMaterialList);
    }

    public void addMaterialEditData(MaterialEditData data) {
        if (data != null && data.getAsset() != null) {
            mSelectMaterialList.clear();
            mSelectMaterialList.add(data);
            mSelectMaterials.postValue(mSelectMaterialList);
        }
    }

    public void addMaterialEditDataList(List<MaterialEditData> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            mSelectMaterialList.clear();
            mSelectMaterialList.addAll(dataList);
            mSelectMaterials.postValue(mSelectMaterialList);
        }
    }

    public MutableLiveData<List<MaterialEditData>> getSelectMaterials() {
        return mSelectMaterials;
    }

    public HVEVisibleAsset getSelectAsset() {
        if (mSelectMaterialList.isEmpty()) {
            return null;
        }
        return mSelectMaterialList.get(0).getAsset();
    }

    public List<MaterialEditData> getMaterialList() {
        return mSelectMaterialList;
    }

    public MutableLiveData<Boolean> getIsMaterialEditShow() {
        return isMaterialLayoutShow;
    }

    public void setMaterialEditShow(boolean isShow) {
        isMaterialLayoutShow.postValue(isShow);
    }

    private boolean isEditModel = false;

    public boolean isEditModel() {
        return isEditModel;
    }

    public void setEditModel(boolean editModel) {
        isEditModel = editModel;
    }

    private int mCurrentFirstMenuId = -1;

    public int getCurrentFirstMenuId() {
        return mCurrentFirstMenuId;
    }

    public void setCurrentFirstMenuId(int currentFirstMenuId) {
        mCurrentFirstMenuId = currentFirstMenuId;
    }

    public MutableLiveData<MaterialEditData> getStickerEdit() {
        return mStickerEdit;
    }

    public void setStickerEdit(MaterialEditData stickerEdit) {
        mStickerEdit.postValue(stickerEdit);
    }

    public MutableLiveData<MaterialEditData> getTextDefaultEdit() {
        return mTextDefaultEdit;
    }

    public void setTextDefaultEdit(MaterialEditData textDefaultEdit) {
        mTextDefaultEdit.postValue(textDefaultEdit);
    }

    public MutableLiveData<MaterialEditData> getTextTemplateEdit() {
        return mTextTemplateEdit;
    }

    public void setTextTemplateEdit(MaterialEditData textTemplateEdit) {
        mTextTemplateEdit.postValue(textTemplateEdit);
    }

    public MutableLiveData<MaterialEditData> getMaterialCopy() {
        return mMaterialCopy;
    }

    public void setMaterialCopy(MaterialEditData materialCopy) {
        mMaterialCopy.postValue(materialCopy);
    }

    public MutableLiveData<MaterialEditData> getMaterialDelete() {
        return mMaterialDelete;
    }

    public void setMaterialDelete(MaterialEditData materialDelete) {
        mMaterialDelete.postValue(materialDelete);
    }

    public MutableLiveData<Boolean> getIsStickerEditState() {
        return isStickerEditState;
    }

    public void setIsStickerEditState(Boolean isStickerEditState) {
        this.isStickerEditState.postValue(isStickerEditState);
    }

    public MutableLiveData<Boolean> getIsTextEditState() {
        return isTextEditState;
    }

    public void setIsTextEditState(Boolean isTextEditState) {
        this.isTextEditState.postValue(isTextEditState);
    }

    public MutableLiveData<Boolean> getIsIsTextTemplateEditState() {
        return isTextTemplateEditState;
    }

    public void setIsTextTemplateEditState(Boolean isTextTemplateEditState) {
        this.isTextTemplateEditState.postValue(isTextTemplateEditState);
    }

    public MutableLiveData<Boolean> getIsTextTrailerEditState() {
        return isTextTrailerEditState;
    }

    public void setIsTextTrailerEditState(Boolean isTextTrailerEditState) {
        this.isTextTrailerEditState.postValue(isTextTrailerEditState);
    }

    public MutableLiveData<String> getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText.postValue(mText);
    }

    public int setTextNotPost(String text, HVEAsset asset) {
        if (asset == null) {
            return -1;
        }
        if (!(asset instanceof HVEWordAsset)) {
            return -1;
        }
        if (TextUtils.isEmpty(text) || text.length() == 0) {
            setText(text);
            return 0;
        }

        int count = ((HVEWordAsset) asset).setText(text);
        if (count < 0 || count > text.length()) {
            return -1;
        }

        String curText = text.substring(0, count);
        setText(curText);
        return count;
    }

    public MutableLiveData<Boolean> getRefreshState() {
        return refreshState;
    }

    public void refresh() {
        refreshState.postValue(true);
    }
}

