package com.hbvhuwe.goals.swipe;

import com.hbvhuwe.goals.model.Stage;

public interface StageSwipeListener extends SwipeListener {
    void onSwipe(final Stage stage, int direction, int position);
}
