package com.hbvhuwe.goals.util;

import com.hbvhuwe.goals.model.Goal;
import com.hbvhuwe.goals.model.Model;
import com.hbvhuwe.goals.model.Stage;

import java.util.Comparator;

public final class Comparators {
    private static final Comparator BY_DEFAULT = new Comparator<Model>() {
        @Override
        public int compare(Model o1, Model o2) {
            return o2.getId() - o1.getId();
        }
    };

    public static final Comparator<Goal> BY_GOAL_DEFAULT = BY_DEFAULT;
    public static final Comparator<Goal> BY_GOAL_TITLE_ACS = compareGoalBy(true, true);
    public static final Comparator<Goal> BY_GOAL_TITLE_DESC = compareGoalBy(true, false);
    public static final Comparator<Goal> BY_GOAL_DATE_ASC = compareGoalBy(false, true);
    public static final Comparator<Goal> BY_GOAL_DATE_DESC = BY_GOAL_DEFAULT;

    public static final Comparator<Stage> BY_STAGE_DEFAULT = BY_DEFAULT;
    public static final Comparator<Stage> BY_STAGE_TITLE_ASC = compareStageBy(true, true);
    public static final Comparator<Stage> BY_STAGE_TITLE_DESC = compareStageBy(true, false);
    public static final Comparator<Stage> BY_STAGE_DATE_ASC = compareStageBy(false, true);
    public static final Comparator<Stage> BY_STAGE_DATE_DESC = BY_STAGE_DEFAULT;

    private static Comparator<Goal> compareGoalBy(boolean titleordate, final boolean order) {
        Comparator<Goal> res = BY_GOAL_DEFAULT;
        if (titleordate) {
            res = new Comparator<Goal>() {
                @Override
                public int compare(Goal o1, Goal o2) {
                    if (order) {
                        return o1.getTitle().compareTo(o2.getTitle());
                    } else {
                        return o2.getTitle().compareTo(o1.getTitle());
                    }
                }
            };
        } else {
            if (order) {
                res = new Comparator<Goal>() {
                    @Override
                    public int compare(Goal o1, Goal o2) {
                        return o1.getId() - o2.getId();
                    }
                };
            }
        }

        return res;
    }

    private static Comparator<Stage> compareStageBy(boolean titleorid, final boolean order) {
        Comparator<Stage> res = BY_STAGE_DEFAULT;
        if (titleorid) {
            res = new Comparator<Stage>() {
                @Override
                public int compare(Stage o1, Stage o2) {
                    if (order) {
                        return o1.getTitle().compareTo(o2.getTitle());
                    } else {
                        return o2.getTitle().compareTo(o1.getTitle());
                    }
                }
            };
        } else {
            if (order) {
                res = new Comparator<Stage>() {
                    @Override
                    public int compare(Stage o1, Stage o2) {
                        return o1.getId() - o2.getId();
                    }
                };
            }
        }
        return res;
    }
}
