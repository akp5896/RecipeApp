package com.example.recipeapp.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.SearchView;
import com.example.recipeapp.Adapters.SpinnerAdapter;
import com.example.recipeapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        adapter = new SpinnerAdapter(context, android.R.layout.simple_list_item_1, current, search);
        suggestions.setAdapter(adapter);

        search.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    current.clear();
                    adapter.notifyDataSetChanged();
                }
                else {
                    newQueryTextHandler(search.getQuery().toString());
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
                return newQueryTextHandler(newText);
            }
        });
    }

    public String getSelectedItem() {
        return search.getQuery().toString();
    }

    private boolean newQueryTextHandler(String newText) {
        current.clear();
        List<String> newArray = new ArrayList<>(options);
        newArray.removeIf(
                x -> !x.
                        toUpperCase(Locale.ROOT)
                        .startsWith(newText
                                .toUpperCase(Locale.ROOT)));
        if(newArray.size() == 0) {
            newArray = new ArrayList<>(options);
            newArray.removeIf(x -> !x.toUpperCase(Locale.ROOT).contains(newText.toUpperCase(Locale.ROOT)));
        }
        current.addAll(newArray);
        adapter.notifyDataSetChanged();
        return true;
    }


    public void setOptions(List<String> options) {
        this.options = options;
    }
}
