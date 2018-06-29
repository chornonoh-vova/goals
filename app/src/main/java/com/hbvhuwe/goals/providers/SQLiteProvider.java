package com.hbvhuwe.goals.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hbvhuwe.goals.model.Goal;
import com.hbvhuwe.goals.model.Stage;
import com.hbvhuwe.goals.providers.db.DbHelper;

import java.util.LinkedList;
import java.util.List;

import static com.hbvhuwe.goals.providers.db.DbContract.GoalEntry;
import static com.hbvhuwe.goals.providers.db.DbContract.StageEntry;

public class SQLiteProvider implements DataProvider {
    public DbHelper mDbHelper;

    public SQLiteProvider(DbHelper mDbHelper) {
        this.mDbHelper = mDbHelper;
    }

    @Override
    public List<Goal> getGoals() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(GoalEntry.TABLE_NAME, null, null, null, null, null, null);
        List<Goal> goals = getGoalsList(cursor);
        cursor.close();
        return goals;
    }

    @Override
    public int getGoalsCount() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(GoalEntry.TABLE_NAME, null, null, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    @Override
    public Goal getGoalById(int goalId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = GoalEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(goalId)};
        Cursor cursor = db.query(GoalEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        Goal goal = null;
        if (cursor.moveToNext()) {
            goal = new Goal();
            goal.setId(cursor.getInt(cursor.getColumnIndexOrThrow(GoalEntry._ID)));
            goal.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_TITLE)));
            goal.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_DESC)));
            goal.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_COMPLETED)) != 0);
            goal.setPercent(cursor.getDouble(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_PERCENT)));
            goal.setCreated(cursor.getString(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_CREATED)));
        }
        cursor.close();
        return goal;
    }

    @Override
    public List<Stage> getStages(int goalId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = StageEntry.COLUMN_GOAL_ID  + " = ?";
        String[] selectionArgs = {String.valueOf(goalId)};
        Cursor cursor = db.query(StageEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        List<Stage> stages = getStagesList(cursor);
        cursor.close();
        return stages;
    }

    @Override
    public int getStagesCount(int goalId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = StageEntry.COLUMN_GOAL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(goalId)};
        Cursor cursor = db.query(StageEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    private int getStagesCompletedCount(int goalId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = StageEntry.COLUMN_GOAL_ID  + " = ? and " + StageEntry.COLUMN_COMPLETED + " = ?";
        String[] selectionArgs = {String.valueOf(goalId), "1"};
        Cursor cursor = db.query(StageEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    @Override
    public void addGoal(Goal goal) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (goal.getId() != -1) {
            values.put(GoalEntry._ID, goal.getId());
        }
        values.put(GoalEntry.COLUMN_TITLE, goal.getTitle());
        values.put(GoalEntry.COLUMN_DESC, goal.getDesc());
        values.put(GoalEntry.COLUMN_COMPLETED, goal.isCompleted() ? 1 : 0);
        values.put(GoalEntry.COLUMN_PERCENT, goal.getPercent());
        values.put(GoalEntry.COLUMN_CREATED, goal.getCreated());

        long count = db.insert(GoalEntry.TABLE_NAME, null, values);
    }

    @Override
    public void addStage(int goalId, Stage stage) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StageEntry.COLUMN_TITLE, stage.getTitle());
        values.put(StageEntry.COLUMN_GOAL_ID, goalId);
        values.put(StageEntry.COLUMN_COMPLETED, stage.isCompleted() ? 1 : 0);

        long count = db.insert(StageEntry.TABLE_NAME, null, values);

        double stagesTotal = getStagesCount(goalId);
        double stagesCompleted = getStagesCompletedCount(goalId);

        updateGoal(goalId, null, null, (stagesCompleted / stagesTotal) * 100d);
    }

    @Override
    public void updateGoal(int goalId, String newTitle, String newDesc, double newProgress) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = GoalEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(goalId) };

        ContentValues values = new ContentValues();
        if (newTitle != null) {
            values.put(GoalEntry.COLUMN_TITLE, newTitle);
        }
        if (newDesc != null) {
            values.put(GoalEntry.COLUMN_DESC, newDesc);
        }
        if (newProgress != -1) {
            if ((int)newProgress == 100) {
                values.put(GoalEntry.COLUMN_COMPLETED, 1);
            } else {
                values.put(GoalEntry.COLUMN_COMPLETED, 0);
            }
            values.put(GoalEntry.COLUMN_PERCENT, newProgress);
        }
        db.update(GoalEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    @Override
    public void checkStage(int goalId, int stageId, boolean check) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = StageEntry.COLUMN_GOAL_ID + " = ? and " + StageEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(goalId), String.valueOf(stageId) };

        ContentValues values = new ContentValues();

        if (check) {
            values.put(StageEntry.COLUMN_COMPLETED, 1);
        } else {
            values.put(StageEntry.COLUMN_COMPLETED, 0);
        }

        db.update(StageEntry.TABLE_NAME, values, selection, selectionArgs);

        double stagesTotal = getStagesCount(goalId);
        double stagesCompleted = getStagesCompletedCount(goalId);

        updateGoal(goalId, null, null, (stagesCompleted / stagesTotal) * 100d);
    }

    @Override
    public void deleteGoalById(int goalId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = GoalEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(goalId) };
        db.delete(GoalEntry.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public void deleteStageById(int goalId, int stageId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = StageEntry.COLUMN_GOAL_ID + " = ? and " + StageEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(goalId), String.valueOf(stageId) };
        db.delete(StageEntry.TABLE_NAME, selection, selectionArgs);

        double stagesTotal = getStagesCount(goalId);
        double stagesCompleted = getStagesCompletedCount(goalId);

        updateGoal(goalId, null, null, (stagesCompleted / stagesTotal) * 100d);
    }

    private List<Goal> getGoalsList(Cursor cursor) {
        List<Goal> goals = new LinkedList<>();
        while (cursor.moveToNext()) {
            goals.add(getGoal(cursor));
        }
        return goals;
    }

    private Goal getGoal(Cursor cursor) {
        Goal goal = new Goal();
        goal.setId(cursor.getInt(cursor.getColumnIndexOrThrow(GoalEntry._ID)));
        goal.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_TITLE)));
        goal.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_DESC)));
        goal.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_COMPLETED)) != 0);
        goal.setPercent(cursor.getDouble(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_PERCENT)));
        goal.setCreated(cursor.getString(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_CREATED)));
        return goal;
    }

    private List<Stage> getStagesList(Cursor cursor) {
        List<Stage> stages = new LinkedList<>();
        while (cursor.moveToNext()) {
            stages.add(getStage(cursor));
        }
        return stages;
    }

    private Stage getStage(Cursor cursor) {
        Stage stage = new Stage();
        stage.setId(cursor.getInt(cursor.getColumnIndexOrThrow(StageEntry._ID)));
        stage.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(StageEntry.COLUMN_TITLE)));
        stage.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(StageEntry.COLUMN_COMPLETED)) != 0);
        return stage;
    }
}
