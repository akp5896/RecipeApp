package com.example.recipeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;


public class SpinnerAdapter extends ArrayAdapter<String> {

    private int resourceLayout;
    private Context context;
    List<String> items;
    SearchView search;
    Boolean finished = false;

    public SpinnerAdapter(Context context, int resource, List<String> items, SearchView search) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.context = context;
        this.search = search;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(resourceLayout, null);
        }

        String s = getItem(position);

        if (s != null) {
            TextView tvPosition = v.findViewById(android.R.id.text1);
            if (tvPosition != null) {
                tvPosition.setText(s);
                tvPosition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finished = true;
                        search.setQuery(tvPosition.getText(), false);
                        items.clear();
                        notifyDataSetChanged();
                    }
                });
            }
        }

        return v;
    }

    public Boolean getFinished() {
        Boolean x = finished;
        finished = false;
        return  x;
    }

}
