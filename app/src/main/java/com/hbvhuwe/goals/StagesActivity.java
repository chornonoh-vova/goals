package com.hbvhuwe.goals;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbvhuwe.goals.adapters.StagesAdapter;
import com.hbvhuwe.goals.model.Goal;
import com.hbvhuwe.goals.model.Model;
import com.hbvhuwe.goals.model.Stage;

import java.util.Objects;

public class StagesActivity extends BaseActivity implements StagesAdapter.StageCheckedListener {
    private int goalId;

    private EditText goalTitle, goalDesc;
    private TextView goalCreated;
    private ProgressBar goalProgress;

    private Toolbar toolbar;

    private RelativeLayout editingView;
    private ImageButton doneEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stages);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.stages_title_text);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        goalTitle = findViewById(R.id.goal_title);
        goalDesc = findViewById(R.id.goal_desc);
        goalCreated = findViewById(R.id.goal_created);
        goalProgress = findViewById(R.id.goal_progress);
        recyclerView = findViewById(R.id.stages_list);
        ImageButton addStage = findViewById(R.id.add_stage);
        editingView = findViewById(R.id.stages_editing_toolbar);
        doneEditing = findViewById(R.id.done_editing);
        ImageButton closeEditing = findViewById(R.id.close_editing);

        coordinatorLayout = findViewById(R.id.stages_layout);

        goalTitle.setSelected(false);

        if (savedInstanceState != null) {
            goalId = savedInstanceState.getInt("goalId", 0);
        } else {
            goalId = getIntent().getIntExtra("goalId", 0);
        }

        initGoal();

        initRecyclerView();


        closeEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(false, false);
                goalTitle.clearFocus();
                goalDesc.clearFocus();
            }
        });

        goalTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    updateGoal(true, false);
                }
            }
        });

        goalDesc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    updateGoal(false, true);
                }
            }
        });

        addStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdd();
            }
        });
    }

    private void initGoal() {
        Goal goal = provider.getGoalById(goalId);
        goalTitle.setText(goal.getTitle());
        goalDesc.setText(goal.getDesc());
        goalProgress.setProgress((int) goal.getPercent());
        goalCreated.setText(goal.getCreated());
    }

    @Override
    protected void initRecyclerView() {
        adapter = new StagesAdapter(provider.getStages(goalId), this);
        if (provider.getStages(goalId).isEmpty()) {
            findViewById(R.id.stages_list_empty).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.stages_list_empty).setVisibility(View.GONE);
        }

        super.initRecyclerView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("goalId", goalId);
    }

    @Override
    public void onChecked(int stageId, boolean isChecked) {
        provider.checkStage(goalId, stageId, isChecked);
        initGoal();
    }

    @Override
    public void onSwipe(final Model model, int direction, final int position) {
        provider.deleteStageById(goalId, model.getId());
        adapter.deleteItem(position);

        Snackbar undo = Snackbar.make(coordinatorLayout, model.getTitle() + " removed!", Snackbar.LENGTH_LONG);
        undo.setAction(R.string.undo_action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provider.addStage(goalId, (Stage) model);
                adapter.addItem(model, position);

                initGoal();
                initRecyclerView();
            }
        });
        undo.setActionTextColor(Color.YELLOW);
        undo.show();

        initGoal();
        initRecyclerView();
    }

    private void onAdd() {
        @SuppressLint("InflateParams") final View dialogView = getLayoutInflater().inflate(R.layout.add_stage_dialog, null);
        final EditText stageTitle = dialogView.findViewById(R.id.stage_dialog_title);

        super.onAdd(dialogView, R.string.stage_dialog_title, R.string.stage_dialog_message,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Stage stage = new Stage();
                        stage.setTitle(stageTitle.getText().toString().trim());
                        stage.setGoalId(goalId);
                        stage.setCompleted(false);
                        if (stage.getTitle().isEmpty()) {
                            emptyTitleError();
                            return;
                        }
                        provider.addStage(goalId, stage);
                        initRecyclerView();
                    }
                });
    }

    private void update(boolean updateTitle, boolean updateDesc) {
        if (updateTitle) {
            provider.updateGoal(goalId, goalTitle.getText().toString().trim(), null, -1);
            goalTitle.clearFocus();
        }

        if (updateDesc) {
            provider.updateGoal(goalId, null, goalDesc.getText().toString().trim(), -1);
            goalDesc.clearFocus();
        }

        initGoal();

        toolbar.setTitle(R.string.stages_title_text);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        editingView.setVisibility(View.GONE);
    }

    private void updateGoal(final boolean updateTitle, final boolean updateDesc) {
        toolbar.setTitle("");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        editingView.setVisibility(View.VISIBLE);
        doneEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(updateTitle, updateDesc);
            }
        });
    }
}
