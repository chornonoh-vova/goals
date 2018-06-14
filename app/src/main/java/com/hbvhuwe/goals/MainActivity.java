package com.hbvhuwe.goals;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;

import com.hbvhuwe.goals.adapters.BaseAdapter;
import com.hbvhuwe.goals.adapters.GoalsAdapter;
import com.hbvhuwe.goals.model.Goal;
import com.hbvhuwe.goals.providers.DataProvider;
import com.hbvhuwe.goals.providers.SQLiteProvider;
import com.hbvhuwe.goals.providers.db.DbHelper;
import com.hbvhuwe.goals.swipe.GoalSwipeListener;
import com.hbvhuwe.goals.swipe.SwipeHelper;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements GoalSwipeListener {

    private DataProvider provider;
    private RecyclerView goalsList;
    private BaseAdapter adapter;
    private CoordinatorLayout coordinatorLayout;

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
        FloatingActionButton addButton = findViewById(R.id.add_goal);
        coordinatorLayout = findViewById(R.id.coordinator_layout);

        initGoals();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdd();
            }
        });
    }

    private void initGoals() {
        adapter = new GoalsAdapter(provider.getGoals());

        goalsList.setLayoutManager(new LinearLayoutManager(this));
        goalsList.setItemAnimator(new DefaultItemAnimator());
        goalsList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        goalsList.setNestedScrollingEnabled(false);
        goalsList.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback helper = new SwipeHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(helper).attachToRecyclerView(goalsList);
    }

    @Override
    public void onSwipe(final Goal goal, int direction, final int position) {
        provider.deleteGoalById(goal.getId());
        adapter.deleteItem(position);

        Snackbar undo = Snackbar.make(coordinatorLayout, goal.getTitle() + " removed!", Snackbar.LENGTH_LONG);
        undo.setAction(R.string.undo_action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provider.addGoal(goal);
                adapter.addItem(goal, position);
            }
        });
        undo.setActionTextColor(Color.YELLOW);
        undo.show();
    }

    public void onAdd() {
        final View dialogView = getLayoutInflater().inflate(R.layout.add_goal_dialog, coordinatorLayout);

        final EditText goalTitle = dialogView.findViewById(R.id.goal_dialog_title);
        final EditText goalDesc = dialogView.findViewById(R.id.goal_dialog_desc);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.goal_dialog_title)
                .setMessage(R.string.goal_dialog_message)
                .setView(dialogView)
                .setPositiveButton(R.string.goal_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Goal goal = new Goal();
                        goal.setId(-1);
                        goal.setTitle(String.valueOf(goalTitle.getText()));
                        goal.setDesc(String.valueOf(goalDesc.getText()));
                        goal.setPercent(0.0d);
                        goal.setCompleted(false);
                        goal.setCreated(new Date().toString());
                        provider.addGoal(goal);
                        initGoals();
                    }
                })
                .setNegativeButton(R.string.goal_dialog_cancel, null)
                .create();
        dialog.show();
    }
}
