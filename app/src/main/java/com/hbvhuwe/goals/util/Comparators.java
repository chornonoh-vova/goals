package com.hbvhuwe.goals.util;

import com.hbvhuwe.goals.model.Goal;
import com.hbvhuwe.goals.model.Model;
import com.hbvhuwe.goals.model.Stage;

import java.util.Comparator;

public final class Comparators {
    public static final Comparator<Goal> BY_GOAL_TITLE_ASC = compareBy(true, true);
    public static final Comparator<Goal> BY_GOAL_TITLE_DESC = compareBy(true, false);
    public static final Comparator<Goal> BY_GOAL_ID_ASC = compareBy(false, true);
    public static final Comparator<Goal> BY_GOAL_ID_DESC = compareBy(false, false);

    public static final Comparator<Stage> BY_STAGE_TITLE_ASC = compareBy(true, true);
    public static final Comparator<Stage> BY_STAGE_TITLE_DESC = compareBy(true, false);
    public static final Comparator<Stage> BY_STAGE_ID_ASC = compareBy(false, true);
    public static final Comparator<Stage> BY_STAGE_ID_DESC = compareBy(false, false);

    @SuppressWarnings("unchecked")
    private static <T extends Model> Comparator<T> compareBy(boolean title, boolean asc) {
        Comparator<Model> res;
        if (title) {
            if (asc) {
                res = new Comparator<Model>() {
                    @Override
                    public int compare(Model o1, Model o2) {
                        return o1.getTitle().compareToIgnoreCase(o2.getTitle());
                    }
                };
            } else {
                res = new Comparator<Model>() {
                    @Override
                    public int compare(Model o1, Model o2) {
                        return o2.getTitle().compareToIgnoreCase(o1.getTitle());
                    }
                };
            }
        } else {
            if (asc) {
                res = new Comparator<Model>() {
                    @Override
                    public int compare(Model o1, Model o2) {
                        return o1.getId() - o2.getId();
                    }
                };
            } else {
                res = new Comparator<Model>() {
                    @Override
                    public int compare(Model o1, Model o2) {
                        return o2.getId() - o1.getId();
                    }
                };
            }
        }

        return (Comparator<T>) res;
    }
}
