package com.hbvhuwe.goals.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<VH> {
    protected List<T> dataset;

    @Override
    public final int getItemCount() {
        return dataset.size();
    }

    public final void deleteItem(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    public final void addItem(T item, int position) {
        dataset.add(position, item);
        notifyItemInserted(position);
    }
}
