package com.hbvhuwe.goals.swipe;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.hbvhuwe.goals.adapters.GoalsAdapter;
import com.hbvhuwe.goals.adapters.StagesAdapter;

public class SwipeHelper extends ItemTouchHelper.SimpleCallback {
    private SwipeListener listener;

    public SwipeHelper(int dragDirs, int swipeDirs, SwipeListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            View foregroundView = null;
            if (viewHolder instanceof GoalsAdapter.ViewHolder) {
                foregroundView = ((GoalsAdapter.ViewHolder) viewHolder).viewForeground;
            } else if (viewHolder instanceof StagesAdapter.ViewHolder) {
                foregroundView = ((StagesAdapter.ViewHolder) viewHolder).viewForeground;
            }
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = null;
        if (viewHolder instanceof GoalsAdapter.ViewHolder) {
            foregroundView = ((GoalsAdapter.ViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof StagesAdapter.ViewHolder) {
            foregroundView = ((StagesAdapter.ViewHolder) viewHolder).viewForeground;
        }
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = null;
        if (viewHolder instanceof GoalsAdapter.ViewHolder) {
            foregroundView = ((GoalsAdapter.ViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof StagesAdapter.ViewHolder) {
            foregroundView = ((StagesAdapter.ViewHolder) viewHolder).viewForeground;
        }
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        View foregroundView = null;
        if (viewHolder instanceof GoalsAdapter.ViewHolder) {
            foregroundView = ((GoalsAdapter.ViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof StagesAdapter.ViewHolder) {
            foregroundView = ((StagesAdapter.ViewHolder) viewHolder).viewForeground;
        }
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (viewHolder instanceof GoalsAdapter.ViewHolder) {
            ((GoalSwipeListener) listener).onSwipe(((GoalsAdapter.ViewHolder) viewHolder).goal,
                    direction, viewHolder.getAdapterPosition());
        } else if (viewHolder instanceof StagesAdapter.ViewHolder) {
            ((StageSwipeListener) listener).onSwipe(((StagesAdapter.ViewHolder) viewHolder).stage,
                    direction, viewHolder.getAdapterPosition());
        }
    }
}
