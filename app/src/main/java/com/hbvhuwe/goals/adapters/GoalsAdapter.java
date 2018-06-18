package com.hbvhuwe.goals.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbvhuwe.goals.R;
import com.hbvhuwe.goals.StagesActivity;
import com.hbvhuwe.goals.model.Goal;

import java.util.List;

public class GoalsAdapter extends BaseAdapter<GoalsAdapter.ViewHolder, Goal> {

    public GoalsAdapter(List<Goal> dataset) {
        this.dataset = dataset;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Goal goal;

        TextView goalTitle, goalCreated;
        ImageView goalDesc, goalDone;
        TextView goalProgress;
        public ConstraintLayout viewForeground;
        public RelativeLayout viewBackground;

        public ViewHolder(final View itemView) {
            super(itemView);

            goalTitle = itemView.findViewById(R.id.goal_item_title);
            goalDesc = itemView.findViewById(R.id.goal_item_desc);
            goalCreated = itemView.findViewById(R.id.goal_item_created);
            goalDone = itemView.findViewById(R.id.goal_item_done);
            goalProgress = itemView.findViewById(R.id.goal_item_progress);
            viewBackground = itemView.findViewById(R.id.goal_item_background);
            viewForeground = itemView.findViewById(R.id.goal_item_foreground);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), StagesActivity.class);
                    intent.putExtra("goalId", goal.getId());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_goals_list, parent,  false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.goal = dataset.get(position);
        holder.goalTitle.setText(holder.goal.getTitle());
        if (holder.goal.getDesc().isEmpty()) {
            holder.goalDesc.setVisibility(View.GONE);
        }
        holder.goalCreated.setText(holder.goal.getCreated());
        holder.goalProgress.setText((int)holder.goal.getPercent() + " %");
        if (!holder.goal.isCompleted()) {
            holder.goalDone.setVisibility(View.INVISIBLE);
        }
    }
}
