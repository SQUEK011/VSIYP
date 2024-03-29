package com.example.vsiyp.ui.mediaeditor.menu;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.adapter.comment.RCommandAdapter;
import com.example.vsiyp.ui.common.adapter.comment.RViewHolder;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.FoldScreenUtil;
import com.example.vsiyp.ui.common.utils.LanguageUtils;
import com.example.vsiyp.ui.common.utils.LocalResourceUtil;
import com.example.vsiyp.ui.common.utils.SizeUtils;
import com.huawei.hms.videoeditor.sdk.util.SmartLog;

import java.util.List;

public class MenuAdapter extends RCommandAdapter<EditMenuBean> {

    private static final String TAG = "MenuAdapter";

    private final ConstraintLayout.LayoutParams contentParams;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public MenuAdapter(Context context, List<EditMenuBean> list, int layoutId) {
        super(context, list, layoutId);
        int itemWidth = (int) (SizeUtils.screenWidth(mContext) / 6.5f);
        int itemHeight = LanguageUtils.isZh() ? SizeUtils.dp2Px(mContext, 56) : SizeUtils.dp2Px(mContext, 64);
        if (FoldScreenUtil.isFoldable() && FoldScreenUtil.isFoldableScreenExpand(context)) {
            itemWidth = (int) (SizeUtils.screenWidth(mContext) / 12.5f);
        }
        contentParams = new ConstraintLayout.LayoutParams(itemWidth, itemHeight);
    }

    @Override
    protected void convert(RViewHolder holder, EditMenuBean editMenuBean, int dataPosition, int position) {
        if (contentParams == null) {
            SmartLog.e(TAG, "contentParams is null");
            return;
        }

        holder.itemView.setLayoutParams(contentParams);

        holder.itemView.setEnabled(editMenuBean.isEnable());
        holder.setViewAlpha(R.id.iv_icon, editMenuBean.isEnable() ? 1f : 0.45f);
        holder.setViewAlpha(R.id.tv_name, editMenuBean.isEnable() ? 1f : 0.45f);

        int imageResourceId = LocalResourceUtil.getDrawableId(mContext, editMenuBean.getDrawableName());
        if (imageResourceId != 0) {
            holder.setImageResource(R.id.iv_icon, imageResourceId);
        } else {
            holder.setImageResource(R.id.iv_icon, R.drawable.logo);
        }

        int stringResourceId = LocalResourceUtil.getStringId(mContext, editMenuBean.getName());
        if (stringResourceId != 0) {
            String name = mContext.getString(LocalResourceUtil.getStringId(mContext, editMenuBean.getName()));
            SmartLog.d(TAG, "name:" + name + " name-length:" + name.length());
            holder.setText(R.id.tv_name, LocalResourceUtil.getStringId(mContext, editMenuBean.getName()));
        } else {
            holder.setText(R.id.tv_name, R.string.app_name);
        }

        holder.itemView.setTag(R.id.editMenuTag, editMenuBean);
        holder.itemView.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (editMenuBean.isEnable() && mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(position, dataPosition);
            }
        }));
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int dataPosition);
    }

}

