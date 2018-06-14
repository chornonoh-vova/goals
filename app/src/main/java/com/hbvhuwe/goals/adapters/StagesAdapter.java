package com.hbvhuwe.goals.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.hbvhuwe.goals.R;
import com.hbvhuwe.goals.model.Stage;

import java.util.List;

public class StagesAdapter extends BaseAdapter<StagesAdapter.ViewHolder, Stage> {
    private final StageCheckedListener listener;

    public StagesAdapter(List<Stage> dataset, StageCheckedListener listener) {
        this.dataset = dataset;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Stage stage;
        CheckBox stageTitle;

        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        public ViewHolder(View itemView) {
            super(itemView);
            stageTitle = itemView.findViewById(R.id.stage_item_check_box);
            viewBackground = itemView.findViewById(R.id.stage_item_background);
            viewForeground = itemView.findViewById(R.id.stage_item_foreground);

            stageTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    listener.onChecked(stage.getStageId(), isChecked);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stages_list_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.stage = dataset.get(position);
        holder.stageTitle.setText(holder.stage.getTitle());
        holder.stageTitle.setChecked(holder.stage.isCompleted());
    }

    public interface StageCheckedListener {
        void onChecked(int stageId, boolean isChecked);
    }
}
