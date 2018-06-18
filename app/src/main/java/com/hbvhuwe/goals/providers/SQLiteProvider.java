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
        String sortOrder = GoalEntry._ID + " DESC";
        Cursor cursor = db.query(GoalEntry.TABLE_NAME, null, null, null, null, null, sortOrder);
        List<Goal> goals = new LinkedList<>();
        while (cursor.moveToNext()) {
            Goal goal = new Goal();
            goal.setId(cursor.getInt(cursor.getColumnIndexOrThrow(GoalEntry._ID)));
            goal.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_TITLE)));
            goal.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_DESC)));
            goal.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_COMPLETED)) != 0);
            goal.setPercent(cursor.getDouble(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_PERCENT)));
            goal.setCreated(cursor.getString(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_CREATED)));
            goals.add(goal);
        }
        cursor.close();
        return goals;
    }

    @Override
    public Goal getGoalById(int goalId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = GoalEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(goalId)};
        String sortOrder = GoalEntry._ID + " DESC";
        Cursor cursor = db.query(GoalEntry.TABLE_NAME, null, selection, selectionArgs, null, null, sortOrder);
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
    public List<Goal> getGoalsCompleted() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = GoalEntry.COLUMN_COMPLETED + " = ?";
        String[] selectionArgs = {"1"};
        Cursor cursor = db.query(GoalEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        List<Goal> goals = new LinkedList<>();
        while (cursor.moveToNext()) {
            Goal goal = new Goal();
            goal.setId(cursor.getInt(cursor.getColumnIndexOrThrow(GoalEntry._ID)));
            goal.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_TITLE)));
            goal.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_DESC)));
            goal.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_COMPLETED)) != 0);
            goal.setPercent(cursor.getDouble(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_PERCENT)));
            goal.setCreated(cursor.getString(cursor.getColumnIndexOrThrow(GoalEntry.COLUMN_CREATED)));
            goals.add(goal);
        }
        cursor.close();
        return goals;
    }

    @Override
    public List<Stage> getStages(int goalId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = StageEntry.COLUMN_GOAL_ID  + " = ?";
        String[] selectionArgs = {String.valueOf(goalId)};
        String sortOrder = StageEntry._ID + " DESC";
        Cursor cursor = db.query(StageEntry.TABLE_NAME, null, selection, selectionArgs, null, null, sortOrder);
        List<Stage> stages = new LinkedList<>();
        while (cursor.moveToNext()) {
            Stage stage = new Stage();
            stage.setStageId(cursor.getInt(cursor.getColumnIndexOrThrow(StageEntry._ID)));
            stage.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(StageEntry.COLUMN_TITLE)));
            stage.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(StageEntry.COLUMN_COMPLETED)) != 0);
            stages.add(stage);
        }
        cursor.close();
        return stages;
    }

    @Override
    public List<Stage> getStagesCompleted(int goalId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = StageEntry.COLUMN_GOAL_ID  + " = ? and " + StageEntry.COLUMN_COMPLETED + " = ?";
        String[] selectionArgs = {String.valueOf(goalId), "1"};
        String sortOrder = StageEntry._ID + " DESC";
        Cursor cursor = db.query(StageEntry.TABLE_NAME, null, selection, selectionArgs, null, null, sortOrder);
        List<Stage> stages = new LinkedList<>();
        while (cursor.moveToNext()) {
            Stage stage = new Stage();
            stage.setStageId(cursor.getInt(cursor.getColumnIndexOrThrow(StageEntry._ID)));
            stage.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(StageEntry.COLUMN_TITLE)));
            stage.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(StageEntry.COLUMN_COMPLETED)) != 0);
            stages.add(stage);
        }
        cursor.close();
        return stages;
    }

    @Override
    public Stage getStageById(int goalId, int stageId) {
        return null;
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

        db.insert(GoalEntry.TABLE_NAME, null, values);
    }

    @Override
    public void addStage(int goalId, Stage stage) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StageEntry.COLUMN_TITLE, stage.getTitle());
        values.put(StageEntry.COLUMN_GOAL_ID, stage.getGoalId());
        values.put(StageEntry.COLUMN_COMPLETED, stage.isCompleted() ? 1 : 0);

        db.insert(StageEntry.TABLE_NAME, null, values);
    }

    @Override
    public void updateGoal(Goal goal) {
        // TODO: 18/06/18 implement goal update
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

        List<Stage> stagesTotal = getStages(goalId);
        List<Stage> stagesCompleted = getStagesCompleted(goalId);

        Goal goal = new Goal();
        goal.setId(goalId);
        goal.setPercent((stagesCompleted.size() / stagesTotal.size()) * 100);

        updateGoal(goal);
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
    }
}
