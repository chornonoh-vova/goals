package com.hbvhuwe.goals.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hbvhuwe.goals.R;
import com.hbvhuwe.goals.RecyclerViewGoalClickListener;
import com.hbvhuwe.goals.model.Goal;
import com.hbvhuwe.goals.providers.DataProvider;

import java.util.List;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {

    private List<Goal> dataset;
    private static RecyclerViewGoalClickListener listener;

    public GoalsAdapter(List<Goal> dataset, RecyclerViewGoalClickListener listener) {
        this.dataset = dataset;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Goal goal;

        TextView goalTitle;
        TextView goalDesc;
        TextView goalCreated;
        ProgressBar goalProgress;
        Button actionArchive;
        Button actionDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            goalTitle = itemView.findViewById(R.id.goal_item_title);
            goalDesc = itemView.findViewById(R.id.goal_item_desc);
            goalCreated = itemView.findViewById(R.id.goal_item_created);
            goalProgress = itemView.findViewById(R.id.goal_item_progress);
            actionArchive = itemView.findViewById(R.id.goal_action_archive);
            actionDelete = itemView.findViewById(R.id.goal_action_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 06/06/18 switch to details
                }
            });

            actionDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDelete(goal, getLayoutPosition());
                }
            });
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goal_list_item, parent,  false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.goal = dataset.get(position);
        holder.goalTitle.setText(holder.goal.getTitle());
        holder.goalDesc.setText(holder.goal.getDesc());
        holder.goalCreated.setText(holder.goal.getCreated());
        holder.goalProgress.setProgress((int) holder.goal.getPercent());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
