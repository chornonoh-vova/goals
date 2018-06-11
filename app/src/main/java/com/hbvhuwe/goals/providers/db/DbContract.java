package com.hbvhuwe.goals.providers.db;

import android.provider.BaseColumns;

public final class DbContract {
    private DbContract() {}

    public static class GoalEntry implements BaseColumns {
        public static final String TABLE_NAME = "goal";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESC = "desc";
        public static final String COLUMN_COMPLETED = "completed";
        public static final String COLUMN_PERCENT = "percent";
        public static final String COLUMN_CREATED = "created";
    }

    public static class StageEntry implements BaseColumns {
        public static final String TABLE_NAME = "stage";
        public static final String COLUMN_GOAL_ID = "goalId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_COMPLETED = "completed";
    }

    static final String SQL_CREATE_GOALS =
            "CREATE TABLE " + GoalEntry.TABLE_NAME + " ( " +
            GoalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            GoalEntry.COLUMN_TITLE + " TEXT, " +
            GoalEntry.COLUMN_DESC + " TEXT, " +
            GoalEntry.COLUMN_COMPLETED + " INTEGER DEFAULT 0, " +
            GoalEntry.COLUMN_PERCENT + " REAL DEFAULT 0.0, " +
            GoalEntry.COLUMN_CREATED + " TEXT NOT NULL);";

    static final String SQL_CREATE_STAGES = "CREATE TABLE " + StageEntry.TABLE_NAME + " ( " +
            StageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            StageEntry.COLUMN_GOAL_ID + " INTEGER REFERENCES " +
            GoalEntry.TABLE_NAME + "(" + GoalEntry._ID + ")" + " ON DELETE CASCADE, " +
            StageEntry.COLUMN_TITLE + " TEXT, " +
            StageEntry.COLUMN_COMPLETED + " INTEGER DEFAULT 0)";

    static final String SQL_DROP_GOALS =
            "DROP TABLE IF EXISTS " + GoalEntry.TABLE_NAME;

    static final String SQL_DROP_STAGES =
            "DROP TABLE IF EXISTS " + StageEntry.TABLE_NAME;
}
