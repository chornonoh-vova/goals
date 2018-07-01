package com.hbvhuwe.goals;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.hbvhuwe.goals.adapters.GoalsAdapter;
import com.hbvhuwe.goals.model.Goal;
import com.hbvhuwe.goals.model.Model;
import com.hbvhuwe.goals.providers.SQLiteProvider;
import com.hbvhuwe.goals.providers.db.DbHelper;
import com.hbvhuwe.goals.util.Comparators;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity {
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

        recyclerView = findViewById(R.id.goals_list);
        FloatingActionButton addButton = findViewById(R.id.add_goal);
        coordinatorLayout = findViewById(R.id.main_layout);

        //initRecyclerView();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdd();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }

    @Override
    protected void initRecyclerView() {
        List<Goal> list = provider.getGoals();
        Collections.sort(list, Comparators.BY_GOAL_ID_DESC);

        adapter = new GoalsAdapter(list);
        if (provider.getGoalsCount() == 0) {
            findViewById(R.id.goals_list_empty).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.goals_list_empty).setVisibility(View.INVISIBLE);
        }

        super.initRecyclerView();
    }

    @Override
    public void onSwipe(final Model model, int direction, final int position) {
        provider.deleteGoalById(model.getId());
        adapter.deleteItem(position);

        Snackbar undo = Snackbar.make(coordinatorLayout, model.getTitle() + " removed!", Snackbar.LENGTH_LONG);
        undo.setAction(R.string.undo_action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provider.addGoal((Goal) model);
                adapter.addItem(model, position);
                initRecyclerView();
            }
        });
        undo.setActionTextColor(Color.YELLOW);
        undo.show();
        initRecyclerView();
    }

    private void onAdd() {
        @SuppressLint("InflateParams")
        final View dialogView = getLayoutInflater().inflate(R.layout.add_goal_dialog, null);

        final EditText goalTitle = dialogView.findViewById(R.id.goal_dialog_title);
        final EditText goalDesc = dialogView.findViewById(R.id.goal_dialog_desc);

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");

        super.onAdd(dialogView, R.string.goal_dialog_title, R.string.goal_dialog_message,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Goal goal = new Goal();
                        goal.setId(-1);
                        goal.setTitle(String.valueOf(goalTitle.getText()).trim());
                        goal.setDesc(String.valueOf(goalDesc.getText()).trim());
                        goal.setPercent(0.0d);
                        goal.setCompleted(false);
                        goal.setCreated(dateFormat.format(new Date()));
                        if (goal.getTitle().isEmpty()) {
                            emptyError();
                            return;
                        }
                        provider.addGoal(goal);
                        initRecyclerView();
                    }
                });
    }
}
