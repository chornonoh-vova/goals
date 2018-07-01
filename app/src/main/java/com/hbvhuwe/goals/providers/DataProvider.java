package com.hbvhuwe.goals.providers;

import com.hbvhuwe.goals.model.Goal;
import com.hbvhuwe.goals.model.Stage;

import java.util.List;

/**
 * Provides data to show in UI
 */
public interface DataProvider {
    /**
     * Get all goals
     * @return list of goals or empty list
     */
    List<Goal> getGoals();

    /**
     * Get all goals count
     *
     * @return number of goals or 0
     */
    int getGoalsCount();

    /**
     * Get goal by id
     * @param goalId id to search
     * @return goal object or null
     */
    Goal getGoalById(int goalId);

    /**
     * Get all stages for specific goals
     * @param goalId id of goal
     * @return list of stages, needed to complete this goal or empty list
     */
    List<Stage> getStages(int goalId);

    /**
     * Get count of all stages for specific goals
     *
     * @param goalId id of goal
     * @return number of stages, needed to compete this goal or 0
     */
    int getStagesCount(int goalId);

    /**
     * Add new goal
     * @param goal to add
     */
    void addGoal(Goal goal);

    /**
     * Add stage to goal
     * @param goalId id of goal to add stage
     * @param stage to add
     */
    void addStage(int goalId, Stage stage);

    /**
     * Update given goal with given parameters
     * @param goalId goal to update, presents always
     * @param newTitle title to update, ignored if null
     * @param newDesc description to update, ignored if null
     * @param newProgress progress to update, ignored if -1
     */
    void updateGoal(int goalId, String newTitle, String newDesc, double newProgress);

    /**
     * Marks given stage in given goal as completed
     * @param goalId goal to update, present always
     * @param stageId stage to check, present always
     * @param check true to mark completed, false otherwise
     */
    void checkStage(int goalId, int stageId, boolean check);

    /**
     * Deletes goal with specified id<br>
     * If there are stages in goal, delete them too
     * @param goalId to delete
     */
    void deleteGoalById(int goalId);

    /**
     * Deletes stage from goal by id
     * @param goalId goal
     * @param stageId to delete
     */
    void deleteStageById(int goalId, int stageId);
}
