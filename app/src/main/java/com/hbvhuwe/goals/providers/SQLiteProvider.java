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
        String sortOrder = GoalEntry.COLUMN_CREATED + " DESC";
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
        String sortOrder = GoalEntry.COLUMN_CREATED + " DESC";
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
        String sortOrder = GoalEntry.COLUMN_CREATED + " DESC";
        Cursor cursor = db.query(GoalEntry.TABLE_NAME, null, selection, selectionArgs, null, null, sortOrder);
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
        return null;
    }

    @Override
    public Stage getStageById(int goalId, int stageId) {
        return null;
    }

    @Override
    public void addGoal(Goal goal) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GoalEntry.COLUMN_TITLE, goal.getTitle());
        values.put(GoalEntry.COLUMN_DESC, goal.getDesc());
        values.put(GoalEntry.COLUMN_COMPLETED, goal.isCompleted() ? 1 : 0);
        values.put(GoalEntry.COLUMN_PERCENT, goal.getPercent());
        values.put(GoalEntry.COLUMN_CREATED, goal.getCreated());

        db.insert(GoalEntry.TABLE_NAME, null, values);
    }

    @Override
    public void addStage(int goalId, Stage stage) {

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

    }
}
