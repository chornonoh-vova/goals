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

    void updateGoal(int goalId, String newTitle, String newDesc, double newProgress);

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
