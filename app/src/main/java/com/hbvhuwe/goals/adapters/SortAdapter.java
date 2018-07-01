package com.hbvhuwe.goals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbvhuwe.goals.R;

public class SortAdapter extends BaseAdapter {
    private Context context;
    private int[] drawables;
    private String[] names;
    private LayoutInflater inflater;

    public SortAdapter(Context appContext) {
        this.context = appContext;
        names = context.getResources().getStringArray(R.array.sort_entries);
        drawables = new int[]{R.drawable.ic_arrow_down,
                R.drawable.ic_arrow_up,
                R.drawable.ic_arrow_down,
                R.drawable.ic_arrow_up};
        inflater = LayoutInflater.from(appContext);
    }

    @Override
    public int getCount() {
        return drawables.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.sort_widget_item, null);
        ImageView itemImage = convertView.findViewById(R.id.sort_item_image);
        TextView itemText = convertView.findViewById(R.id.sort_item_text);
        itemImage.setImageResource(drawables[position]);
        itemText.setText(names[position]);
        return convertView;
    }
}
