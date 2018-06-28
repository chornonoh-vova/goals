package com.hbvhuwe.goals.swipe;

import com.hbvhuwe.goals.model.Model;

public interface SwipeListener {
    void onSwipe(final Model model, int direction, int position);
}
