package com.example.recipeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipeapp.CustomViews.StateVO;
import com.example.recipeapp.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CheckboxAdapter extends ArrayAdapter<StateVO> {
    private final Context context;
    private final ArrayList<StateVO> listState;
    private final HashSet<String> checked;

    public CheckboxAdapter(Context context, int resource, List<StateVO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.listState = (ArrayList<StateVO>) objects;
        this.checked = new HashSet<>();
    }

    public HashSet<String> getChecked() {
        return checked;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {
        final CheckboxViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(context);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new CheckboxViewHolder();
            holder.textView = (TextView) convertView
                    .findViewById(R.id.text);
            holder.checkBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (CheckboxViewHolder) convertView.getTag();
        }

        holder.textView.setText(listState.get(position).getTitle());

        holder.checkBox.setTag(position);
        holder.checkBox.setChecked(listState.get(position).isSelected());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checked.add(listState.get(position).getTitle());
                } else {
                    checked.remove(listState.get(position).getTitle());
                }
                Toast.makeText(context, "Checked" + listState.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    private class CheckboxViewHolder {
        private TextView textView;
        private CheckBox checkBox;
    }
}