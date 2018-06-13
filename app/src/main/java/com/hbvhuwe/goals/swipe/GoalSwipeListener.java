package com.hbvhuwe.goals.swipe;

import com.hbvhuwe.goals.model.Goal;

public interface GoalSwipeListener extends SwipeListener {
    void onSwipe(final Goal goal, int direction, int position);
}
