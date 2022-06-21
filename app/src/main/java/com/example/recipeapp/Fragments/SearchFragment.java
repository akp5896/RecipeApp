package com.example.recipeapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.recipeapp.ItemsAdapter;
import com.example.recipeapp.MainActivity;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.R;
import com.example.recipeapp.RecipeClient;
import com.example.recipeapp.databinding.FragmentSearchBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private static final String TAG = "Search fragment";
    FragmentSearchBinding binding;
    List<String> included = new ArrayList<>();
    List<String> excluded = new ArrayList<>();
    ItemsAdapter includedAdapter;
    ItemsAdapter excludedAdapter;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<CharSequence> cuisineAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.cuisines,
                        android.R.layout.simple_spinner_item);
        cuisineAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCuisine.setAdapter(cuisineAdapter);
        ArrayAdapter<CharSequence> typesAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.types,
                        android.R.layout.simple_spinner_item);
        typesAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerType.setAdapter(typesAdapter);

        includedAdapter = new ItemsAdapter(included);
        binding.rvInclude.setAdapter(includedAdapter);
        binding.rvInclude.setLayoutManager(getFlexboxLayoutManager());

        excludedAdapter = new ItemsAdapter(excluded);
        binding.rvExclude.setAdapter(excludedAdapter);
        binding.rvExclude.setLayoutManager(getFlexboxLayoutManager());

        binding.btnInclude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                included.add(binding.edInclude.getText().toString());
                includedAdapter.notifyItemInserted(included.size() - 1);
                binding.edInclude.setText(null);
            }
        });

        binding.btnExclude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluded.add(binding.edExclude.getText().toString());
                excludedAdapter.notifyItemInserted(excluded.size() - 1);
                binding.edExclude.setText(null);
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchListener();
            }
        });
    }

    private void searchListener() {
        String cuisine = null;
        String excludeCuisine = null;
        if(binding.checkboxExcludeCuisine.isChecked()) {
            excludeCuisine = binding.spinnerCuisine.getSelectedItem().toString();
        } else {
            cuisine = binding.spinnerCuisine.getSelectedItem().toString();
        }
        RecipeClient.getInstance().getRecipesWithFilters(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    List<Recipe> result = Recipe.fromJsonArray(json.jsonObject.getJSONArray("results"));
                    Log.i(TAG, "success: " + json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                
                Log.e(TAG, "Recipies search failed: " + response);
            }
        }, binding.etTitle.getText().toString(), cuisine, excludeCuisine,
                String.join(",", included),String.join(",", excluded),
                binding.spinnerType.getSelectedItem().toString(), binding.edTime.getText().toString());
    }

    @NonNull
    private FlexboxLayoutManager getFlexboxLayoutManager() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        return layoutManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}