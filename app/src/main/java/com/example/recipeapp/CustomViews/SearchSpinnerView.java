package com.example.recipeapp.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.recipeapp.Adapters.ItemsAdapter;
import com.example.recipeapp.R;

import java.util.ArrayList;
import java.util.List;

public class SearchSpinnerView extends FrameLayout {

    SearchView search;
    ListView suggestions;
    List<String> options;
    List<String> current;

    ArrayAdapter<String> adapter;

    public SearchSpinnerView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public SearchSpinnerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SearchSpinnerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public SearchSpinnerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View v = inflate(getContext(), R.layout.search_spinner, this);
        search = v.findViewById(R.id.search_bar);
        suggestions = v.findViewById(R.id.suggestions);

        options = new ArrayList<>();

        current = new ArrayList<>();
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, current);
        suggestions.setAdapter(adapter);

        search.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    current.clear();
                    adapter.notifyDataSetChanged();
                }
                else {
                    current.addAll(options);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                current.clear();
                List<String> newArray = new ArrayList<>(options);
                newArray.removeIf(x -> !x.startsWith(newText));
                current.addAll(newArray);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }


    public void setOptions(List<String> options) {
        this.options = options;
    }
}
