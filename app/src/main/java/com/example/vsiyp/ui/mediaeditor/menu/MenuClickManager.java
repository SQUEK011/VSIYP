package com.example.vsiyp.ui.mediaeditor.menu;

import static com.example.vsiyp.ui.mediaeditor.VideoClipsActivity.ACTION_ADD_PICTURE_IN_REQUEST_CODE;
import static com.example.vsiyp.ui.mediaeditor.VideoClipsActivity.ACTION_PIP_VIDEO_ASSET;
import static com.example.vsiyp.ui.mediaeditor.VideoClipsActivity.ACTION_REPLACE_VIDEO_ASSET;

import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.EditorManager;
import com.example.vsiyp.ui.common.utils.CpuUtils;
import com.example.vsiyp.ui.common.utils.LaneSizeCheckUtils;
import com.example.vsiyp.ui.common.utils.SPManager;
import com.example.vsiyp.ui.common.utils.SharedPreferencesUtils;
import com.example.vsiyp.ui.common.utils.ToastUtils;
import com.example.vsiyp.ui.common.utils.ToastWrapper;
import com.example.vsiyp.ui.common.view.loading.LoadingDialogUtils;
import com.example.vsiyp.ui.mediaeditor.VideoClipsActivity;
import com.example.vsiyp.ui.mediaeditor.aibodyseg.BodySegViewModel;
import com.example.vsiyp.ui.mediaeditor.aifun.AIBlockingHintDialog;
import com.example.vsiyp.ui.mediaeditor.aifun.fragment.AiFunFragment;
import com.example.vsiyp.ui.mediaeditor.aisegmentation.SegmentationViewModel;
import com.example.vsiyp.ui.mediaeditor.animation.videoanimation.fragment.AnimationPanelFragment;
import com.example.vsiyp.ui.mediaeditor.audio.activity.AudioPickActivity;
import com.example.vsiyp.ui.mediaeditor.audio.fragment.SoundEffectFragment;
import com.example.vsiyp.ui.mediaeditor.effect.fragment.EffectPanelFragment;
import com.example.vsiyp.ui.mediaeditor.filter.FilterAdjustPanelView;
import com.example.vsiyp.ui.mediaeditor.filter.FilterPanelFragment;

import com.example.vsiyp.ui.mediaeditor.fragment.AudioSpeedFragment;
import com.example.vsiyp.ui.mediaeditor.fragment.CanvasBackgroundFragment;
import com.example.vsiyp.ui.mediaeditor.fragment.GeneralSpeedFragment;
import com.example.vsiyp.ui.mediaeditor.fragment.GraffitiFragment;
import com.example.vsiyp.ui.mediaeditor.fragment.MaskEffectFragment;
import com.example.vsiyp.ui.mediaeditor.fragment.ObjectFragment;
import com.example.vsiyp.ui.mediaeditor.fragment.TransitionPanelFragment;
import com.example.vsiyp.ui.mediaeditor.fragment.TransparencyPanelFragment;
import com.example.vsiyp.ui.mediaeditor.fragment.VideoProportionFragment;
import com.example.vsiyp.ui.mediaeditor.fragment.VolumePanelFragment;
import com.example.vsiyp.ui.mediaeditor.materialedit.MaterialEditViewModel;
import com.example.vsiyp.ui.mediaeditor.pip.PicInPicMixFragment;
import com.example.vsiyp.ui.mediaeditor.sticker.fragment.StickerPanelFragment;
import com.example.vsiyp.ui.mediaeditor.sticker.stickeranimation.fragment.StickerAnimationPanelFragment;
import com.example.vsiyp.ui.mediaeditor.texts.fragment.EditPanelFragment;
import com.example.vsiyp.ui.mediaeditor.trackview.bean.MainViewState;
import com.example.vsiyp.ui.mediaeditor.trackview.viewmodel.EditPreviewViewModel;
import com.example.vsiyp.ui.mediapick.activity.MediaPickActivity;
import com.huawei.hms.videoeditor.sdk.asset.HVEAsset;
import com.huawei.hms.videoeditor.sdk.asset.HVEAudioAsset;
import com.huawei.hms.videoeditor.sdk.asset.HVEImageAsset;
import com.huawei.hms.videoeditor.sdk.asset.HVEVideoAsset;
import com.huawei.hms.videoeditor.sdk.asset.HVEVisibleAsset;
import com.huawei.hms.videoeditor.sdk.effect.HVEEffect;
import com.huawei.hms.videoeditor.sdk.effect.impl.ColorFilterEffect;
import com.huawei.hms.videoeditor.sdk.lane.HVELane;
import com.huawei.hms.videoeditor.sdk.lane.HVEVideoLane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

public class MenuClickManager {
    public final static String AI_FUN = "AI_FUN";

    public final static String AI_FUN_KEY = "AI_FUN_KEY";

    public final static String TIME_LAPSE = "TIME_LAPSE";

    public final static String TIME_LAPSE_KEY = "TIME_LAPSE_KEY";

    public final static String VIDEO_SELECTION = "VIDEO_SELECTION";

    public final static String VIDEO_SELECTION_KEY = "VIDEO_SELECTION_KEY";

    public static final String AI_BODY_SEG = "AI_BODY_SEG";

    public static final String AI_BODY_SEG_KEY = "AI_BODY_SEG_KEY";

    public static final String AI_HEAD_SEG = "AI_HEAD_SEG";

    public static final String AI_HEAD_SEG_KEY = "AI_HEAD_SEG_KEY";

    public static final String AI_SEGMENTATION = "AI_SEGMENTATION";

    public static final String AI_SEGMENTATION_KEY = "AI_SEGMENTATION_KEY";

    private VideoClipsActivity mActivity;

    private EditMenuContentLayout menuContentLayout;

    private MenuControlViewRouter mMenuControlViewRouter;

    private MenuViewModel mMenuViewModel;

    private EditPreviewViewModel editPreviewViewModel;

    private MaterialEditViewModel mMaterialEditViewModel;

    private SegmentationViewModel mSegmentationViewModel;

    private BodySegViewModel mBodySegViewModel;

    private OnAssetDeleteListener mOnAssetDeleteListener;

    private List<Integer> cloudMaterialsIdList;

    private static class MenuClickManagerHolder {
        private static final MenuClickManager INSTANCE = new MenuClickManager();
    }

    public static MenuClickManager getInstance() {
        return MenuClickManagerHolder.INSTANCE;
    }

    public void init(VideoClipsActivity activity, EditMenuContentLayout menuContentLayout, MenuViewModel menuViewModel,
                     EditPreviewViewModel editPreviewViewModel, MaterialEditViewModel materialEditViewModel,
                     SegmentationViewModel mSegmentationViewModel, BodySegViewModel bodySegViewModel) {
        this.mActivity = activity;
        this.mMenuViewModel = menuViewModel;
        this.editPreviewViewModel = editPreviewViewModel;
        this.mMaterialEditViewModel = materialEditViewModel;
        this.mSegmentationViewModel = mSegmentationViewModel;
        this.mBodySegViewModel = bodySegViewModel;
        this.menuContentLayout = menuContentLayout;
        mMenuControlViewRouter =
                new MenuControlViewRouter(mActivity, R.id.fragment_container, menuContentLayout, mMenuViewModel);
        this.cloudMaterialsIdList = getCloudMaterialsIdList();
    }

    public void update(VideoClipsActivity activity, EditMenuContentLayout menuContentLayout,
                       MenuViewModel menuViewModel, EditPreviewViewModel editPreviewViewModel,
                       MaterialEditViewModel materialEditViewModel,
                       SegmentationViewModel mSegmentationViewModel, BodySegViewModel bodySegViewModel) {
        this.mActivity = activity;
        this.mMenuViewModel = menuViewModel;
        this.editPreviewViewModel = editPreviewViewModel;
        this.mMaterialEditViewModel = materialEditViewModel;
        this.mSegmentationViewModel = mSegmentationViewModel;
        this.mBodySegViewModel = bodySegViewModel;
        this.menuContentLayout = menuContentLayout;
        if (mMenuControlViewRouter != null) {
            mMenuControlViewRouter.updateMenuViewModel(mMenuViewModel);
        }
        this.cloudMaterialsIdList = getCloudMaterialsIdList();
    }

    public void setOnAssetDeleteListener(OnAssetDeleteListener listener) {
        mOnAssetDeleteListener = listener;
    }

    public Stack<MenuControlViewRouter.Panel> getViewStack() {
        if (mMenuControlViewRouter == null) {
            return null;
        }
        return mMenuControlViewRouter.getViewStack();
    }

    public void showFragment(int menuId, Fragment fragment) {
        if (mMenuControlViewRouter != null) {
            mMenuControlViewRouter.showFragment(menuId, fragment);
        }
    }

    public boolean popView() {
        if (mMenuControlViewRouter == null) {
            return false;
        }
        boolean isPopView = mMenuControlViewRouter.popView();
        return isPopView;
    }

    public void handlerClickEvent(int id) {
        switch (id) {
            case MainViewState.EDIT_PIP_STATE_ADD:
                if (!LaneSizeCheckUtils.isCanAddPip(EditorManager.getInstance().getEditor(), mActivity)) {
                    ToastWrapper
                            .makeText(mActivity, mActivity.getString(R.string.pip_lane_out_of_size), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                Intent startIntent = new Intent(mActivity, MediaPickActivity.class);
                startIntent.putExtra(MediaPickActivity.ACTION_TYPE, MediaPickActivity.ACTION_ADD_PIP_MEDIA_TYPE);
                mActivity.startActivityForResult(startIntent, ACTION_ADD_PICTURE_IN_REQUEST_CODE);
                break;

            case MainViewState.EDIT_VIDEO_STATE_SPLIT:
                mActivity.showAssetSplitFragment(id);
                break;

            case MainViewState.EDIT_VIDEO_STATE_TRIM:
            case MainViewState.EDIT_VIDEO_OPERATION_TRIM:
                HVEAsset assetTrim = editPreviewViewModel.getMainLaneAsset();
                if (assetTrim == null || !(assetTrim instanceof HVEVideoAsset)) {
                    ToastWrapper.makeText(mActivity, mActivity.getString(R.string.crop_limit), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                mActivity.showAssetCropFragment(id);
                break;
            case MainViewState.EDIT_BACKGROUND_STATE:
                showPanelViewPrepare(id, new CanvasBackgroundFragment());
                break;

            case MainViewState.EDIT_VIDEO_STATE_REPLACE:
                Intent intent = new Intent(mActivity, MediaPickActivity.class);
                HVEAsset asset = editPreviewViewModel.getMainLaneAsset();
                if (asset == null) {
                    return;
                }
                intent.putExtra("isReplaceAsset", true);
                intent.putExtra(MediaPickActivity.ACTION_TYPE, MediaPickActivity.ACTION_REPLACE_MEDIA_TYPE);
                intent.putExtra(MediaPickActivity.DURATION, asset.getDuration());
                mActivity.startActivityForResult(intent, ACTION_REPLACE_VIDEO_ASSET);
                break;

            case MainViewState.EDIT_VIDEO_STATE_COPY:
                mMenuViewModel.copyMainLaneAsset();
                break;

            case MainViewState.EDIT_VIDEO_STATE_DELETE:
            case MainViewState.EDIT_VIDEO_OPERATION_DELETE:
                HVEAsset assetDelete = editPreviewViewModel.getMainLaneAsset();
                if (assetDelete == null) {
                    return;
                }

                if (EditorManager.getInstance().getMainLane() == null
                        || EditorManager.getInstance().getMainLane().getAssets().size() == 1) {
                    ToastWrapper.makeText(mActivity, mActivity.getString(R.string.lastvideo), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                HVEVideoLane hveVideoLane = EditorManager.getInstance().getMainLane();
                if (hveVideoLane == null) {
                    return;
                }
                List<HVEAsset> hveAssetList = hveVideoLane.getAssets();
                if (hveAssetList == null) {
                    return;
                }
                mMenuViewModel.pauseTimeLine();
                mMenuViewModel.deleteVideo(assetDelete);
                mMaterialEditViewModel.clearMaterialEditData();
                if (mOnAssetDeleteListener != null) {
                    mOnAssetDeleteListener.onDelete(assetDelete);
                }
                break;

            case MainViewState.EDIT_VIDEO_STATE_ROTATION:
                HVEAsset hveAsset = editPreviewViewModel.getMainLaneAsset();
                if (hveAsset == null) {
                    return;
                }
                if (hveAsset instanceof HVEVideoAsset || hveAsset instanceof HVEImageAsset) {
                    HVEVisibleAsset visibleAsset = (HVEVisibleAsset) hveAsset;
                    float aRotation = visibleAsset.getRotation();
                    if (aRotation >= 0 && aRotation < 270) {
                        aRotation += 90;
                    } else {
                        aRotation = 0;
                    }
                    visibleAsset.setRotation(aRotation);
                    editPreviewViewModel.getEditor().refresh(editPreviewViewModel.getTimeLine().getCurrentTime());
                    ToastWrapper.makeText(mActivity, String.format(Locale.ROOT,
                                    mActivity.getString(R.string.rotationdegree), String.valueOf(aRotation)), Toast.LENGTH_SHORT)
                            .show(500);
                }
                break;

            case MainViewState.EDIT_VIDEO_STATE_MIRROR:
            case MainViewState.EDIT_VIDEO_OPERATION_MIRROR:
            case MainViewState.EDIT_PIP_OPERATION_MIRROR:
                HVEAsset mirrorAsset = editPreviewViewModel.getSelectedAsset();
                if (mirrorAsset == null) {
                    mirrorAsset = editPreviewViewModel.getMainLaneAsset();
                }
                if (mirrorAsset == null) {
                    return;
                }

                boolean mirrorStatus = mActivity.isMirrorStatus();
                if (mirrorAsset instanceof HVEVideoAsset) {
                    mirrorStatus = ((HVEVideoAsset) mirrorAsset).getHorizontalMirrorState();
                    ((HVEVideoAsset) mirrorAsset).setHorizontalMirrorState(!mirrorStatus);
                }
                if (mirrorAsset instanceof HVEImageAsset) {
                    mirrorStatus = ((HVEImageAsset) mirrorAsset).getHorizontalMirrorState();
                    ((HVEImageAsset) mirrorAsset).setHorizontalMirrorState(!mirrorStatus);
                }
                mirrorStatus = !mirrorStatus;
                mActivity.setMirrorStatus(mirrorStatus);
                if (mirrorStatus) {
                    ToastWrapper.makeText(mActivity, mActivity.getString(R.string.openmirror), Toast.LENGTH_SHORT)
                            .show();
                } else {
                    ToastWrapper.makeText(mActivity, mActivity.getString(R.string.closemirror), Toast.LENGTH_SHORT)
                            .show();
                }

                if (editPreviewViewModel.getTimeLine() != null) {
                    editPreviewViewModel.getEditor().seekTimeLine(editPreviewViewModel.getTimeLine().getCurrentTime());
                }
                break;
            case MainViewState.EDIT_VIDEO_STATE_INVERTED:
                editPreviewViewModel.videoRevert();
                break;
            case MainViewState.EDIT_PIP_OPERATION_ADD:
                Intent startIntent2 = new Intent(mActivity, MediaPickActivity.class);
                startIntent2.putExtra(MediaPickActivity.ACTION_TYPE, MediaPickActivity.ACTION_ADD_PIP_MEDIA_TYPE);
                mActivity.startActivityForResult(startIntent2, ACTION_ADD_PICTURE_IN_REQUEST_CODE);
                break;
            case MainViewState.EDIT_VIDEO_OPERATION_SPLIT:
                mActivity.showAssetSplitFragment(id);
                break;
            case MainViewState.EDIT_VIDEO_OPERATION_COPY:
                mMenuViewModel.copyMainLaneAsset();
                break;
            case MainViewState.EDIT_VIDEO_STATE_VOLUME:
            case MainViewState.EDIT_VIDEO_OPERATION_VOLUME:
            case MainViewState.EDIT_PIP_OPERATION_VOLUME:
            case MainViewState.EDIT_AUDIO_STATE_VOLUME:
                showPanelViewPrepare(id, VolumePanelFragment.newInstance(id));
                break;

            case MainViewState.EDIT_VIDEO_STATE_SPEED:
            case MainViewState.EDIT_VIDEO_OPERATION_SPEED:
            case MainViewState.EDIT_PIP_OPERATION_SPEED:
            case MainViewState.EDIT_AUDIO_STATE_SPEED:
                HVEAsset speedAsset = editPreviewViewModel.getSelectedAsset();
                if (speedAsset == null) {
                    speedAsset = editPreviewViewModel.getMainLaneAsset();
                }
                if (speedAsset instanceof HVEVideoAsset) {
                    showPanelViewPrepare(id, GeneralSpeedFragment.newInstance(id));
                }

                if (speedAsset instanceof HVEAudioAsset) {
                    showPanelViewPrepare(id, AudioSpeedFragment.newInstance(id));
                }
                break;

            case MainViewState.EDIT_VIDEO_OPERATION_ROTATION:
            case MainViewState.EDIT_PIP_OPERATION_ROTATION:
                HVEAsset assets = editPreviewViewModel.getSelectedAsset();
                if (assets == null) {
                    return;
                }
                if (assets instanceof HVEVideoAsset || assets instanceof HVEImageAsset) {
                    HVEVisibleAsset visibleAsset = (HVEVisibleAsset) assets;
                    float rotation = visibleAsset.getRotation();
                    if (rotation >= 0 && rotation < 270) {
                        rotation += 90;
                    } else {
                        rotation = 0;
                    }
                    visibleAsset.setRotation(rotation);
                    if (editPreviewViewModel.getTimeLine() != null) {
                        editPreviewViewModel.getEditor().refresh(editPreviewViewModel.getTimeLine().getCurrentTime());
                    }
                    ToastWrapper.makeText(mActivity, String.format(Locale.ROOT,
                                    mActivity.getString(R.string.rotationdegree), String.valueOf(rotation)), Toast.LENGTH_SHORT)
                            .show(500);
                }

                break;
            case MainViewState.EDIT_VIDEO_OPERATION_TAILORING:
            case MainViewState.EDIT_PIP_OPERATION_CROP:
            case MainViewState.EDIT_VIDEO_STATE_TAILORING:
                mActivity.gotoCropVideoActivity();
                break;

            case MainViewState.EDIT_PIP_OPERATION_ANIMATION:
            case MainViewState.EDIT_VIDEO_STATE_ANIMATION:
            case MainViewState.EDIT_VIDEO_OPERATION_ANIMATION:
                showPanelViewPrepare(id, AnimationPanelFragment.newInstance(id));
                break;

            case MainViewState.EDIT_VIDEO_OPERATION_INVERTED:
                editPreviewViewModel.videoRevert();
                break;

            case MainViewState.EDIT_VIDEO_STATE_MASK:
            case MainViewState.EDIT_VIDEO_OPERATION_MASK:
                showPanelViewPrepare(id, MaskEffectFragment.newInstance(true));
                break;

            case MainViewState.EDIT_VIDEO_OPERATION_REPLACE:
                Intent pipIntent = new Intent(mActivity, MediaPickActivity.class);
                HVEAsset mainAsset = editPreviewViewModel.getMainLaneAsset();
                if (mainAsset == null) {
                    return;
                }
                pipIntent.putExtra("isReplaceAsset", true);
                pipIntent.putExtra(MediaPickActivity.ACTION_TYPE, MediaPickActivity.ACTION_REPLACE_MEDIA_TYPE);
                pipIntent.putExtra(MediaPickActivity.DURATION, mainAsset.getDuration());
                mActivity.startActivityForResult(pipIntent, ACTION_REPLACE_VIDEO_ASSET);
                break;

            case MainViewState.EDIT_VIDEO_OPERATION_FILTER_ADD:
            case MainViewState.EDIT_FILTER_STATE_ADD:
            case MainViewState.EDIT_VIDEO_STATE_FILTER:
            case MainViewState.EDIT_PIP_OPERATION_FILTER:
                boolean isFromAsset = true;
                if (id == MainViewState.EDIT_FILTER_STATE_ADD) {
                    isFromAsset = false;
                }
                if (mActivity != null && !mMenuViewModel.isCanAddEffect(isFromAsset)) {
                    ToastWrapper
                            .makeText(mActivity, mActivity.getResources().getString(R.string.nofilter), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                showPanelViewPrepare(id, FilterPanelFragment.newInstance(isFromAsset));
                break;

            case MainViewState.EDIT_VIDEO_OPERATION_ADJUST:
            case MainViewState.EDIT_FILTER_STATE_ADJUST:
            case MainViewState.EDIT_VIDEO_STATE_ADJUST:
            case MainViewState.EDIT_PIP_OPERATION_ADJUST:
            case MainViewState.EDIT_ADJUST_OPERATION_ADJUST:
                boolean isAsset = true;
                if (id == MainViewState.EDIT_FILTER_STATE_ADJUST || id == MainViewState.EDIT_ADJUST_OPERATION_ADJUST) {
                    isAsset = false;
                }
                if (mActivity != null && !mMenuViewModel.isCanAddEffect(isAsset)) {
                    ToastWrapper
                            .makeText(mActivity, mActivity.getApplication().getResources().getString(R.string.noadjust),
                                    Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                showPanelViewPrepare(id, FilterAdjustPanelView.newInstance(isAsset));
                break;
            case MainViewState.EDIT_STICKER_STATE_ADD_TUYA:
            case MainViewState.EDIT_TEXT_STATE_ADD_ADDTUYA:
                showPanelViewPrepare(id, GraffitiFragment.newInstance(false));
                break;

            case MainViewState.EDIT_STICKER_STATE_ADD_STICKER:
            case MainViewState.EDIT_TEXT_STATE_STICKER:
            case MainViewState.EDIT_STICKER_OPERATION_ADD:
                showPanelViewPrepare(id, StickerPanelFragment.newInstance(false));
                break;

            case MainViewState.EDIT_STICKER_OPERATION_COPY:
                mMenuViewModel.copySticker();
                break;

            case MainViewState.EDIT_STICKER_OPERATION_SPLIT:
                mMenuViewModel.splitSticker();
                break;

            case MainViewState.EDIT_STICKER_OPERATION_REPLACE:
                showPanelViewPrepare(id, StickerPanelFragment.newInstance(true));
                break;

            case MainViewState.EDIT_STICKER_OPERATION_ANIMATION:
                showPanelViewPrepare(id, new StickerAnimationPanelFragment());
                break;

            case MainViewState.EDIT_STICKER_OPERATION_DELETE:
            case MainViewState.EDIT_GRAFFIT_OPERATION_DELETE:
                mMaterialEditViewModel.clearMaterialEditData();
                mMenuViewModel.deleteAsset();
                popView();
                break;

            case MainViewState.EDIT_VIDEO_STATE_TRANSPARENCY:
            case MainViewState.EDIT_VIDEO_OPERATION_TRANSPARENCY:
            case MainViewState.EDIT_PIP_OPERATION_TRANSPARENCY:
                showPanelViewPrepare(id, new TransparencyPanelFragment());
                break;

            case MainViewState.EDIT_AI_OPERATION_DELETE:
                mMenuViewModel.deleteAI();
                popView();
                break;

            case MainViewState.EDIT_TEXT_OPERATION_ADD:
                showPanelViewPrepare(id, EditPanelFragment.newInstance(false, false, true));
                break;

            case MainViewState.EDIT_TEXT_OPERATION_SPLIT:
            case MainViewState.EDIT_TEXT_MODULE_OPERATION_SPLIT:
                mMenuViewModel.splitText();
                break;

            case MainViewState.EDIT_TEXT_STATE_ADD:
                showPanelViewPrepare(id, EditPanelFragment.newInstance(false, false, false));
                break;

            case MainViewState.EDIT_TEXT_OPERATION_EDIT:
                mMaterialEditViewModel.setEditModel(true);
                showPanelViewPrepare(MainViewState.EDIT_TEXT_STATE_ADD,
                        EditPanelFragment.newInstance(false, false, false));
                break;

            case MainViewState.EDIT_TEXT_OPERATION_COPY:
            case MainViewState.EDIT_TEXT_MODULE_OPERATION_COPY:
                mMenuViewModel.copyText();
                break;

            case MainViewState.EDIT_TEXT_OPERATION_ANIMATION:
                showPanelViewPrepare(id, EditPanelFragment.newInstance(false, true, false));
                break;

            case MainViewState.EDIT_TEXT_OPERATION_DELETE:
            case MainViewState.EDIT_TEXT_MODULE_OPERATION_DELETE:
                mMaterialEditViewModel.clearMaterialEditData();
                mMaterialEditViewModel.setEditModel(false);
                mMenuViewModel.deleteText();
                popView();
                break;

            case MainViewState.EDIT_SPECIAL_STATE_ADD:
            case MainViewState.EDIT_SPECIAL_OPERATION_ADD:
                if (mActivity != null && !mMenuViewModel.isCanAddEffect(false)) {
                    ToastWrapper
                            .makeText(mActivity, mActivity.getApplication().getResources().getString(R.string.noeffect),
                                    Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                showPanelViewPrepare(id, EffectPanelFragment.newInstance(false));
                break;

            case MainViewState.EDIT_SPECIAL_OPERATION_REPLACE:
                showPanelViewPrepare(id, EffectPanelFragment.newInstance(true));
                break;

            case MainViewState.EDIT_SPECIAL_OPERATION_OBJECT:
            case MainViewState.EDIT_FILTER_OPERATION_OBJECT:
            case MainViewState.EDIT_ADJUST_OPERATION_OBJECT:
                showPanelViewPrepare(id, new ObjectFragment());
                break;

            case MainViewState.EDIT_SPECIAL_OPERATION_DELETE:
                mMenuViewModel.deleteSpecial();
                popView();
                break;

            case MainViewState.EDIT_FILTER_OPERATION_REPLACE:
                HVEEffect effect = editPreviewViewModel.getSelectedEffect();
                if (effect == null) {
                    return;
                }
                if (effect instanceof ColorFilterEffect) {
                    //showPanelViewPrepare(id, ExclusiveFilterPanelFragment.newInstance());
                } else {
                    showPanelViewPrepare(id, FilterPanelFragment.newInstance(false));
                }

                break;

            case MainViewState.EDIT_ADJUST_OPERATION_REPLACE:
                showPanelViewPrepare(id, FilterAdjustPanelView.newInstance(false));
                break;

            case MainViewState.EDIT_FILTER_OPERATION_DELETE:
            case MainViewState.EDIT_ADJUST_OPERATION_DELETE:
                mMenuViewModel.deleteFilter(id);
                popView();
                break;

            case MainViewState.EDIT_PIP_OPERATION_DELETE:
                mMenuViewModel.deletePip();
                mMaterialEditViewModel.clearMaterialEditData();
                popView();
                break;

            case MainViewState.EDIT_PIP_OPERATION_MIX:
                showPanelViewPrepare(id, new PicInPicMixFragment());
                break;

            case MainViewState.EDIT_PIP_OPERATION_MASK:
                showPanelViewPrepare(id, MaskEffectFragment.newInstance(false));
                break;

            case MainViewState.EDIT_PIP_OPERATION_REPLACE:
                Intent intent2 = new Intent(mActivity, MediaPickActivity.class);
                HVEAsset asset2 = editPreviewViewModel.getSelectedAsset();
                if (asset2 == null) {
                    return;
                }
                intent2.putExtra("isReplaceAsset", true);
                intent2.putExtra(MediaPickActivity.ACTION_TYPE, MediaPickActivity.ACTION_ADD_PIP_MEDIA_TYPE);
                intent2.putExtra(MediaPickActivity.DURATION, asset2.getDuration());
                mActivity.startActivityForResult(intent2, ACTION_PIP_VIDEO_ASSET);
                break;

            case MainViewState.EDIT_PIP_OPERATION_COPY:
                mMenuViewModel.copyOtherLaneAsset();
                break;

            case MainViewState.EDIT_PIP_OPERATION_INVERTED:
                editPreviewViewModel.videoRevert();
                break;
            case MainViewState.EDIT_AUDIO_STATE_HWMUSIC:
            case MainViewState.EDIT_AUDIO_STATE_ADD:
                if (id == MainViewState.EDIT_AUDIO_STATE_ADD) {
                    HVEAsset audioAssets = editPreviewViewModel.getSelectedAsset();
                    if (!(audioAssets instanceof HVEAudioAsset)) {
                        return;
                    }
                }
                if (!LaneSizeCheckUtils.isCanAddAudio(editPreviewViewModel.getEditor(), mActivity)) {
                    ToastUtils.getInstance()
                            .showToast(mActivity, mActivity.getString(R.string.audio_lane_out_of_size), Toast.LENGTH_SHORT);
                    return;
                }
                if (mActivity != null && mMenuViewModel.isCanAddAudio()) {
                    gotoActivityFroResult(AudioPickActivity.class, VideoClipsActivity.ACTION_ADD_AUDIO_REQUEST_CODE);
                } else {
                    if (mActivity != null) {
                        ToastUtils.getInstance()
                                .showToast(mActivity,
                                        mActivity.getApplication().getResources().getString(R.string.audio_music),
                                        Toast.LENGTH_LONG);
                    }
                }
                break;

            case MainViewState.EDIT_AUDIO_STATE_SPLIT:
                HVEAsset cutAudioAsset = editPreviewViewModel.getSelectedAsset();
                long seekTime = editPreviewViewModel.getSeekTime();
                if (cutAudioAsset == null) {
                    break;
                }
                if ((seekTime - cutAudioAsset.getStartTime()) < 100 || (cutAudioAsset.getEndTime() - seekTime) < 100) {
                    ToastUtils.getInstance()
                            .showToast(mActivity, mActivity.getString(R.string.nodivision), Toast.LENGTH_SHORT);
                    break;
                }
                mMenuViewModel.splitAudio();
                break;
            case MainViewState.EDIT_AUDIO_STATE_DELETE:
                mMenuViewModel.deleteAudio();
                popView();
                break;

            case MainViewState.EDIT_AUDIO_STATE_COPY:
                mMenuViewModel.copyAudio();
                break;

            case MainViewState.EDIT_AUDIO_STATE_ACOUSTICS:
                if (!LaneSizeCheckUtils.isCanAddAudio(editPreviewViewModel.getEditor(), mActivity)) {
                    ToastUtils.getInstance()
                            .showToast(mActivity, mActivity.getString(R.string.audio_lane_out_of_size), Toast.LENGTH_SHORT);
                    return;
                }
                if (mActivity != null && mMenuViewModel.isCanAddAudio()) {
                    showPanelViewPrepare(id, SoundEffectFragment.newInstance());
                } else {
                    if (mActivity != null) {
                        ToastUtils.getInstance()
                                .showToast(mActivity,
                                        mActivity.getApplication().getResources().getString(R.string.audio_effect),
                                        Toast.LENGTH_SHORT);
                    }
                }
                break;

            case MainViewState.TRANSITION_PANEL:
                if (mMenuControlViewRouter != null) {
                    mMenuControlViewRouter.removeStackTopFragment();
                    showPanelViewPrepare(id, new TransitionPanelFragment());
                }
                break;

            case MainViewState.EDIT_RATIO_STATE:
            case MainViewState.EDIT_VIDEO_STATE_PROPORTION:
            case MainViewState.EDIT_VIDEO_OPERATION_PROPORTION:
                showPanelViewPrepare(id, new VideoProportionFragment());
                break;
            case MainViewState.EDIT_VIDEO_STATE_AI_HAIR:
            case MainViewState.EDIT_VIDEO_OPERATION_AI_HAIR:
            case MainViewState.EDIT_PIP_OPERATION_AI_HAIR:
                if (mActivity == null || mActivity.isFinishing() || mActivity.isDestroyed()) {
                    return;
                }

                if (isCpu32()) {
                    ToastWrapper.makeText(mActivity, mActivity.getResources().getString(R.string.cpu_limit)).show();
                    return;
                }

                HVEAsset aiHairAsset = editPreviewViewModel.getSelectedAsset();
                if (aiHairAsset == null) {
                    aiHairAsset = editPreviewViewModel.getMainLaneAsset();
                }

                if (!(aiHairAsset instanceof HVEVisibleAsset)) {
                    return;
                }

                if (!isAiCanBeUsed((HVEVisibleAsset) aiHairAsset, HVEEffect.HVEEffectType.HAIR_DYEING)) {
                    ToastWrapper.makeText(mActivity, mActivity.getResources().getString(R.string.ai_limit)).show();
                    return;
                }

                mActivity.runOnUiThread(() -> {
                    LoadingDialogUtils.getInstance()
                            .builder(mActivity, mActivity.getLifecycle())
                            .show(mActivity.getString(R.string.intelligent_processing), true);
                });
                break;

            case MainViewState.EDIT_PIP_OPERATION_AI_SELECTION:
            case MainViewState.EDIT_VIDEO_STATE_AI_SELECTION:
            case MainViewState.EDIT_VIDEO_OPERATION_AI_SELECTION:
                if (mMenuViewModel != null) {
                    mMenuViewModel.setVideoSelectionEnter(id);
                }
                break;

            case MainViewState.EDIT_VIDEO_STATE_TIME_LAPSE:
            case MainViewState.EDIT_VIDEO_OPERATION_TIME_LAPSE:
            case MainViewState.EDIT_PIP_OPERATION_TIME_LAPSE:
                HVEAsset selectedAsset = editPreviewViewModel.getSelectedAsset();
                if (selectedAsset == null) {
                    selectedAsset = editPreviewViewModel.getMainLaneAsset();
                }
                if (selectedAsset == null) {
                    return;
                }
                break;
            case MainViewState.EDIT_VIDEO_STATE_AI_FUN:
            case MainViewState.EDIT_VIDEO_OPERATION_AI_FUN:
            case MainViewState.EDIT_PIP_OPERATION_AI_FUN:
                if (mActivity == null || mActivity.isDestroyed() || mActivity.isFinishing()) {
                    break;
                }

                if (isCpu32()) {
                    ToastWrapper.makeText(mActivity, mActivity.getResources().getString(R.string.cpu_limit)).show();
                    return;
                }

                HVEAsset aiFunAsset = editPreviewViewModel.getSelectedAsset();
                if (aiFunAsset == null) {
                    aiFunAsset = editPreviewViewModel.getMainLaneAsset();
                }

                if (!(aiFunAsset instanceof HVEVisibleAsset)) {
                    return;
                }

                if (!isAiCanBeUsed((HVEVisibleAsset) aiFunAsset, HVEEffect.HVEEffectType.AI_COLOR)
                        && !isAiCanBeUsed((HVEVisibleAsset) aiFunAsset, HVEEffect.HVEEffectType.FACE_REENACT)
                        && !isAiCanBeUsed((HVEVisibleAsset) aiFunAsset, HVEEffect.HVEEffectType.FACE_SMILE)) {
                    ToastWrapper.makeText(mActivity, mActivity.getResources().getString(R.string.ai_limit)).show();
                    return;
                }

                boolean isFirstAiFun = SPManager.get(AI_FUN, mActivity).getBoolean(AI_FUN_KEY, true);
                if (isFirstAiFun) {
                    AIBlockingHintDialog aiFunDialog =
                            new AIBlockingHintDialog(mActivity, mActivity.getString(R.string.cut_second_menu_fun),
                                    mActivity.getString(R.string.fun_description));
                    aiFunDialog.setOnPositiveClickListener(() -> {
                        SPManager.get(AI_FUN, mActivity).put(AI_FUN_KEY, false);
                        MenuClickManager.getInstance().showPanelViewPrepare(id, AiFunFragment.newInstance(id));
                    });
                    aiFunDialog.show();
                } else {
                    MenuClickManager.getInstance().showPanelViewPrepare(id, AiFunFragment.newInstance(id));
                }
                break;
            case MainViewState.EDIT_VIDEO_STATE_AI_SEGMENTATION:
            case MainViewState.EDIT_VIDEO_OPERATION_AI_SEGMENTATION:
            case MainViewState.EDIT_PIP_OPERATION_AI_SEGMENTATION:
                HVEAsset selectSegmentAsset = editPreviewViewModel.getSelectedAsset();
                if (selectSegmentAsset == null) {
                    selectSegmentAsset = editPreviewViewModel.getMainLaneAsset();
                }
                if (!(selectSegmentAsset instanceof HVEVisibleAsset)) {
                    return;
                }
                if (!isAiCanBeUsed((HVEVisibleAsset) selectSegmentAsset, HVEEffect.HVEEffectType.SEGMENTATION)) {
                    ToastWrapper.makeText(mActivity, mActivity.getResources().getString(R.string.ai_limit)).show();
                    return;
                }
                mSegmentationViewModel.setSegmentationEnter(id);
                break;
            case MainViewState.EDIT_VIDEO_STATE_BODY_SEG:
            case MainViewState.EDIT_VIDEO_OPERATION_BODY_SEG:
            case MainViewState.EDIT_PIP_OPERATION_BODY_SEG:
                HVEAsset selectSegAsset = editPreviewViewModel.getSelectedAsset();
                if (selectSegAsset == null) {
                    selectSegAsset = editPreviewViewModel.getMainLaneAsset();
                }
                if (!(selectSegAsset instanceof HVEVisibleAsset)) {
                    return;
                }
                int segPart = SharedPreferencesUtils.getInstance().getIntValue(mActivity, selectSegAsset.getUuid());
                if (!isAiCanBeUsed((HVEVisibleAsset) selectSegAsset, HVEEffect.HVEEffectType.BODY_SEG)
                        || (hasBodySeg((HVEVisibleAsset) selectSegAsset) && segPart == 1)) {
                    ToastWrapper.makeText(mActivity, mActivity.getResources().getString(R.string.ai_limit)).show();
                    return;
                }
                mBodySegViewModel.setBodySegEnter(id);
                SharedPreferencesUtils.getInstance().putIntValue(mActivity, selectSegAsset.getUuid(), 2);
                break;
            case MainViewState.EDIT_VIDEO_STATE_HEAD_SEG:
            case MainViewState.EDIT_VIDEO_OPERATION_HEAD_SEG:
            case MainViewState.EDIT_PIP_OPERATION_HEAD_SEG:
                HVEAsset selectHeadAsset = editPreviewViewModel.getSelectedAsset();
                if (selectHeadAsset == null) {
                    selectHeadAsset = editPreviewViewModel.getMainLaneAsset();
                }
                if (!(selectHeadAsset instanceof HVEVisibleAsset)) {
                    return;
                }
                int headPart = SharedPreferencesUtils.getInstance().getIntValue(mActivity, selectHeadAsset.getUuid());
                if (!isAiCanBeUsed((HVEVisibleAsset) selectHeadAsset, HVEEffect.HVEEffectType.BODY_SEG)
                        || (hasBodySeg((HVEVisibleAsset) selectHeadAsset) && headPart == 2)) {
                    ToastWrapper.makeText(mActivity, mActivity.getResources().getString(R.string.ai_limit)).show();
                    return;
                }
                mBodySegViewModel.setBodySegEnter(id);
                SharedPreferencesUtils.getInstance().putIntValue(mActivity, selectHeadAsset.getUuid(), 1);
                break;
            default:
                break;
        }
    }

    private boolean hasBodySeg(HVEVisibleAsset asset) {
        HVEEffect.HVEEffectType assetType = asset.getAIEffectType();
        return assetType != null && assetType.equals(HVEEffect.HVEEffectType.BODY_SEG);
    }

    public void showPanelViewPrepare(int id, Fragment fragment) {
        mMenuControlViewRouter.showFragment(id, fragment);
    }

    private boolean isAiCanBeUsed(HVEVisibleAsset asset, HVEEffect.HVEEffectType type) {
        HVEEffect.HVEEffectType assetType = asset.getAIEffectType();
        return assetType == null || assetType.equals(type);
    }

    private boolean isCpu32() {
        String deviceType;
        try {
            deviceType = CpuUtils.getArchType();
        } catch (IOException e) {
            return false;
        }
        if (CpuUtils.CPU_ARCHITECTURE_TYPE_32.equals(deviceType)) {
            return true;
        }
        return false;
    }

    private List<Integer> getCloudMaterialsIdList() {
        cloudMaterialsIdList = new ArrayList<>();
        cloudMaterialsIdList.add(MainViewState.EDIT_FILTER_STATE_ADD);
        cloudMaterialsIdList.add(MainViewState.EDIT_VIDEO_STATE_FILTER);
        cloudMaterialsIdList.add(MainViewState.EDIT_PIP_OPERATION_FILTER);
        cloudMaterialsIdList.add(MainViewState.EDIT_STICKER_STATE_ADD_STICKER);
        cloudMaterialsIdList.add(MainViewState.EDIT_TEXT_STATE_STICKER);
        cloudMaterialsIdList.add(MainViewState.EDIT_STICKER_OPERATION_REPLACE);
        cloudMaterialsIdList.add(MainViewState.EDIT_SPECIAL_STATE_ADD);
        cloudMaterialsIdList.add(MainViewState.EDIT_AUDIO_STATE_ACOUSTICS);
        cloudMaterialsIdList.add(MainViewState.EDIT_AUDIO_STATE_HWMUSIC);
        cloudMaterialsIdList.add(MainViewState.EDIT_TEXT_MODULE_OPERATION_REPLACE);
        cloudMaterialsIdList.add(MainViewState.TRANSITION_PANEL);
        cloudMaterialsIdList.add(MainViewState.EDIT_RATIO_STATE);
        cloudMaterialsIdList.add(MainViewState.EDIT_BACKGROUND_STATE);
        cloudMaterialsIdList.add(MainViewState.EDIT_PIP_OPERATION_ANIMATION);
        cloudMaterialsIdList.add(MainViewState.EDIT_VIDEO_STATE_ANIMATION);
        cloudMaterialsIdList.add(MainViewState.EDIT_VIDEO_OPERATION_ANIMATION);
        cloudMaterialsIdList.add(MainViewState.EDIT_TEXT_OPERATION_ANIMATION);
        cloudMaterialsIdList.add(MainViewState.EDIT_STICKER_OPERATION_ANIMATION);
        cloudMaterialsIdList.add(MainViewState.EDIT_SPECIAL_OPERATION_REPLACE);
        cloudMaterialsIdList.add(MainViewState.EDIT_FILTER_OPERATION_REPLACE);
        cloudMaterialsIdList.add(MainViewState.EDIT_TEXT_STATE_ADD_WATER_MARK);
        cloudMaterialsIdList.add(MainViewState.EDIT_VIDEO_STATE_SPEED);
        cloudMaterialsIdList.add(MainViewState.EDIT_PIP_OPERATION_SPEED);
        cloudMaterialsIdList.add(MainViewState.EDIT_VIDEO_OPERATION_SPEED);
        cloudMaterialsIdList.add(MainViewState.EDIT_VIDEO_OPERATION_MASK);
        cloudMaterialsIdList.add(MainViewState.EDIT_VIDEO_STATE_MASK);
        cloudMaterialsIdList.add(MainViewState.EDIT_PIP_OPERATION_MASK);
        cloudMaterialsIdList.add(MainViewState.EDIT_VIDEO_STATE_AI_HAIR);
        cloudMaterialsIdList.add(MainViewState.EDIT_VIDEO_OPERATION_AI_HAIR);
        cloudMaterialsIdList.add(MainViewState.EDIT_PIP_OPERATION_AI_HAIR);
        return cloudMaterialsIdList;
    }

    public List<Integer> getUnableOperateIds(int type) {
        List<Integer> unableMenuId = new ArrayList<>();
        switch (type) {
            case 2:
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_SPEED);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_VOLUME);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_INVERTED);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_AI_SEGMENTATION);
                break;
            case 3:
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_SPLIT);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_SPEED);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_VOLUME);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_ANIMATION);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_FILTER_ADD);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_TAILORING);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_ADJUST);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_COPY);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_REPLACE);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_MASK);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_MIRROR);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_TRANSPARENCY);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_INVERTED);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_AI_HAIR);
                break;
            case 4:
                unableMenuId.add(MainViewState.EDIT_PIP_OPERATION_SPLIT);
                unableMenuId.add(MainViewState.EDIT_PIP_OPERATION_SPEED);
                unableMenuId.add(MainViewState.EDIT_PIP_OPERATION_VOLUME);
                unableMenuId.add(MainViewState.EDIT_PIP_OPERATION_INVERTED);
                break;
            case 5:
                unableMenuId.add(MainViewState.EDIT_PIP_OPERATION_SPLIT);
                break;
            case 6:
                unableMenuId.add(MainViewState.EDIT_TEXT_OPERATION_EDIT);
                break;
            case 7:
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_AI_HAIR);
                break;
            case 8:
                unableMenuId.add(MainViewState.EDIT_VIDEO_STATE_BODY_SEG);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_BODY_SEG);
                unableMenuId.add(MainViewState.EDIT_PIP_OPERATION_BODY_SEG);
                unableMenuId.add(MainViewState.EDIT_VIDEO_STATE_HEAD_SEG);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_HEAD_SEG);
                unableMenuId.add(MainViewState.EDIT_PIP_OPERATION_HEAD_SEG);
                unableMenuId.add(MainViewState.EDIT_VIDEO_STATE_WINGS);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_WINGS);
                unableMenuId.add(MainViewState.EDIT_PIP_OPERATION_WINGS);
                break;
            case 9:
                unableMenuId.add(MainViewState.EDIT_VIDEO_STATE_INVERTED);
                unableMenuId.add(MainViewState.EDIT_VIDEO_OPERATION_INVERTED);
                unableMenuId.add(MainViewState.EDIT_PIP_OPERATION_INVERTED);
                break;
            default:
                break;
        }
        return unableMenuId;
    }

    public void translation(int index) {
        int position = 0;
        switch (index) {
            case MainViewState.EDIT_VIDEO_STATE:
                position = 0;
                break;
            case MainViewState.EDIT_AUDIO_STATE:
                position = 1;
                break;
            case MainViewState.EDIT_STICKER_STATE:
                position = 2;
                break;
            case MainViewState.EDIT_TEXT_STATE:
                position = 3;
                break;
            case MainViewState.EDIT_PIP_STATE:
                position = 4;
                break;
            case MainViewState.EDIT_SPECIAL_STATE:
                position = 5;
                break;
            case MainViewState.EDIT_FILTER_STATE:
                position = 6;
                break;
            default:
                break;
        }
        menuContentLayout.setCurrentFirstMenu(position);
    }

    public HVELane.HVELaneType getLaneType(int id) {
        HVELane.HVELaneType laneType;
        if (id == MainViewState.EDIT_PIP_STATE) {
            laneType = HVELane.HVELaneType.VIDEO;
        } else {
            laneType = HVELane.HVELaneType.STICKER;
        }
        return laneType;
    }

    private void gotoActivityFroResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent(mActivity, cls);
        mActivity.startActivityForResult(intent, requestCode);
    }

    public interface OnAssetDeleteListener {
        void onDelete(HVEAsset asset);
    }

}

