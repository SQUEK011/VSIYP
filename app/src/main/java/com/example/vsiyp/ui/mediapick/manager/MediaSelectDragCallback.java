
package com.example.vsiyp.ui.mediapick.manager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class MediaSelectDragCallback extends ItemTouchHelper.Callback {
    private final OnItemMoveCallback mOnItemMoveCallback;

    public MediaSelectDragCallback(OnItemMoveCallback onItemMoveCallback) {
        this.mOnItemMoveCallback = onItemMoveCallback;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = ItemTouchHelper.ACTION_STATE_IDLE;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
        @NonNull RecyclerView.ViewHolder target) {
        if (mOnItemMoveCallback != null) {
            mOnItemMoveCallback.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return false;
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (mOnItemMoveCallback != null) {
            mOnItemMoveCallback.onFinish();
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    public interface OnItemMoveCallback {
        void onItemMove(int fromPosition, int toPosition);

        void onFinish();
    }
}
