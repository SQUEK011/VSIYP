package com.example.vsiyp.ui.common.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;

import java.util.List;

public class ValueItemAdapter extends RecyclerView.Adapter<ValueItemAdapter.ValueViewHolder> {
    private final Context mCtx;

    private int[] resStringId;

    private List<Integer> imageList;

    private int mSelectPosition = 0;

    private OnEditItemClickListener mOnItemClickListener;

    public void setData(int[] data, List<Integer> list) {
        this.resStringId = data.clone();
        this.imageList = list;
        notifyDataSetChanged();
    }

    public ValueItemAdapter(Context context, int[] data, List<Integer> list) {
        this.mCtx = context;
        this.resStringId = data.clone();
        this.imageList = list;
    }

    public void setmSelectPosition(int mSelectPosition) {
        this.mSelectPosition = mSelectPosition;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ValueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.value_item, parent, false);
        return new ValueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ValueViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (imageList == null || imageList.size() <= 0) {
            return;
        }
        Integer imageInt = imageList.get(position);
        int i = imageInt.intValue();
        holder.image.setBackgroundResource(i);
        holder.proportion.setText(resStringId[position]);
        holder.proportion.setSelected(mSelectPosition == position);
        if (position == mSelectPosition && mSelectPosition == 0) {
            holder.image.setBackgroundResource(R.drawable.ic_value_item_1_selected);
        } else if (position == mSelectPosition && mSelectPosition == 1) {
            holder.image.setBackgroundResource(R.drawable.ic_value_item_2_selected);
        } else if (position == mSelectPosition && mSelectPosition == 2) {
            holder.image.setBackgroundResource(R.drawable.ic_value_item_3_selected);
        } else if (position == mSelectPosition && mSelectPosition == 3) {
            holder.image.setBackgroundResource(R.drawable.ic_value_item_4_selected);
        } else if (position == mSelectPosition && mSelectPosition == 4) {
            holder.image.setBackgroundResource(R.drawable.ic_value_item_5_selected);
        } else if (position == mSelectPosition && mSelectPosition == 5) {
            holder.image.setBackgroundResource(R.drawable.ic_value_item_6_selected);
        } else if (position == mSelectPosition && mSelectPosition == 6) {
            holder.image.setBackgroundResource(R.drawable.ic_value_item_7_selected);
        } else if (position == mSelectPosition && mSelectPosition == 7) {
            holder.image.setBackgroundResource(R.drawable.ic_value_item_8_selected);
        } else if (position == mSelectPosition && mSelectPosition == 8) {
            holder.image.setBackgroundResource(R.drawable.ic_value_item_9_selected);
        } else if (position == mSelectPosition && mSelectPosition == 9) {
            holder.image.setBackgroundResource(R.drawable.ic_value_item_10_selected);
        } else if (position == mSelectPosition && mSelectPosition == 10) {
            holder.image.setBackgroundResource(R.drawable.ic_value_item_11_selected);
        } else if (position == mSelectPosition && mSelectPosition == 11) {
            holder.image.setBackgroundResource(R.drawable.ic_value_item_12_selected);
        }
        holder.layoutParent.setOnClickListener(new OnClickRepeatedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectPosition != position) {
                    changeSelected(position);
                }
            }
        }));

        holder.layoutParent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return mOnItemClickListener.onItemLongClick(holder, resStringId[position], position);
            }
        });

        mOnItemClickListener.onItemClick(holder, resStringId[mSelectPosition], mSelectPosition);
    }

    @Override
    public int getItemCount() {
        if (imageList == null) {
            return 0;
        }
        return imageList.size();
    }

    public class ValueViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;

        private TextView proportion;

        private ConstraintLayout layoutParent;

        public ValueViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_value_item);
            proportion = itemView.findViewById(R.id.name_value_item);
            layoutParent = itemView.findViewById(R.id.layout_value_item);
        }
    }

    public void setOnItemClickListener(OnEditItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void changeSelected(int position) {
        mSelectPosition = position;
        notifyDataSetChanged();
    }
}

