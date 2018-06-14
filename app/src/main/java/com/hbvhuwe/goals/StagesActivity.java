package com.hbvhuwe.goals;

import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.hbvhuwe.goals.adapters.StagesAdapter;
import com.hbvhuwe.goals.model.Goal;
import com.hbvhuwe.goals.model.Stage;
import com.hbvhuwe.goals.providers.DataProvider;
import com.hbvhuwe.goals.providers.SQLiteProvider;
import com.hbvhuwe.goals.providers.db.DbHelper;
import com.hbvhuwe.goals.swipe.StageSwipeListener;

import java.util.Objects;

public class StagesActivity extends AppCompatActivity implements StageSwipeListener,
        StagesAdapter.StageCheckedListener {
    private int goalId;
    private DataProvider provider;

    private EditText goalTitle, goalDesc;
    private ProgressBar goalProgress;
    private RecyclerView stagesList;
    private ImageButton addStage;

    private CoordinatorLayout stagesLayout;

    private StagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stages);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        provider = new SQLiteProvider(new DbHelper(getApplicationContext()));

        goalTitle = findViewById(R.id.goal_title);
        goalDesc = findViewById(R.id.goal_desc);
        goalProgress = findViewById(R.id.goal_progress);
        stagesList = findViewById(R.id.stages_list);
        addStage = findViewById(R.id.add_stage);
        stagesLayout = findViewById(R.id.stages_layout);

        goalTitle.setSelected(false);

        if (savedInstanceState != null) {
            goalId = savedInstanceState.getInt("goalId", 0);
        } else {
            goalId = getIntent().getIntExtra("goalId", 0);
        }

        Goal goal = provider.getGoalById(goalId);
        goalTitle.setText(goal.getTitle());
        goalDesc.setText(goal.getDesc());
        goalProgress.setProgress((int) goal.getPercent());

        initStages();

        addStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initStages() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("goalId", goalId);
    }

    @Override
    public void onChecked(int stageId, boolean isChecked) {

    }

    @Override
    public void onSwipe(final Stage stage, int direction, final int position) {
        provider.deleteStageById(goalId, stage.getStageId());
        adapter.deleteItem(position);

        Snackbar undo = Snackbar.make(stagesLayout, stage.getTitle() + " removed!", Snackbar.LENGTH_LONG);
        undo.setAction(R.string.undo_action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provider.addStage(goalId, stage);
                adapter.addItem(stage, position);
            }
        });
        undo.setActionTextColor(Color.YELLOW);
        undo.show();
    }
}
