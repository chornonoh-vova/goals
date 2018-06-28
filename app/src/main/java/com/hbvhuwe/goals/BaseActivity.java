package com.hbvhuwe.goals;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.hbvhuwe.goals.adapters.BaseAdapter;
import com.hbvhuwe.goals.providers.DataProvider;
import com.hbvhuwe.goals.providers.SQLiteProvider;
import com.hbvhuwe.goals.providers.db.DbHelper;
import com.hbvhuwe.goals.swipe.SwipeHelper;
import com.hbvhuwe.goals.swipe.SwipeListener;

@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity implements SwipeListener {
    protected DataProvider provider;
    protected RecyclerView recyclerView;
    protected BaseAdapter adapter;
    protected CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        provider = new SQLiteProvider(new DbHelper(getApplicationContext()));
    }

    protected void initRecyclerView() {
        ItemTouchHelper.SimpleCallback helper = new SwipeHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(helper).attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    protected void onAdd(final View dialogView, @StringRes int title, @StringRes int message,
                         final DialogInterface.OnClickListener ok) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setView(dialogView)
                .setPositiveButton(R.string.dialog_ok, ok)
                .setNegativeButton(R.string.dialog_cancel, null)
                .create();
        dialog.show();
    }

    protected void emptyTitleError() {
        Snackbar error = Snackbar.make(coordinatorLayout, R.string.dialog_error_message, Snackbar.LENGTH_SHORT);
        error.show();
    }
}
