package com.hbvhuwe.goals;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.hbvhuwe.goals.adapters.GoalsAdapter;
import com.hbvhuwe.goals.model.Goal;
import com.hbvhuwe.goals.providers.DataProvider;
import com.hbvhuwe.goals.providers.SQLiteProvider;
import com.hbvhuwe.goals.providers.db.DbHelper;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements RecyclerViewGoalClickListener {

    private DataProvider provider;
    private RecyclerView goalsList;
    private GoalsAdapter adapter;

    private FloatingActionButton addButton;

    @Override
    protected void onDestroy() {
        ((SQLiteProvider)provider).mDbHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        provider = new SQLiteProvider(new DbHelper(getApplicationContext()));

        goalsList = findViewById(R.id.goals_list);
        addButton = findViewById(R.id.add_button);

        initGoals();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdd();
            }
        });
    }

    private void initGoals() {
        goalsList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GoalsAdapter(provider.getGoals(), this);
        goalsList.setAdapter(adapter);
        goalsList.setNestedScrollingEnabled(false);
    }

    @Override
    public void onDelete(Goal goal, int position) {
        provider.deleteGoalById(goal.getId());
        initGoals();
    }

    public void onAdd() {
        final View dialogView = getLayoutInflater().inflate(R.layout.add_dialog, null);

        final EditText goalTitle = dialogView.findViewById(R.id.goal_title);
        final EditText goalDesc = dialogView.findViewById(R.id.goal_desc);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add new goal")
                .setMessage("What is your task?")
                .setView(dialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Goal goal = new Goal();
                        goal.setTitle(String.valueOf(goalTitle.getText()));
                        goal.setDesc(String.valueOf(goalDesc.getText()));
                        goal.setPercent(0.0d);
                        goal.setCompleted(false);
                        goal.setCreated(new Date().toString());
                        provider.addGoal(goal);
                        initGoals();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}
