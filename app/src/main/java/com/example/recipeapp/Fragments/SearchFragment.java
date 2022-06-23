package com.example.recipeapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.recipeapp.Adapters.AutoCompleteAdapter;
import com.example.recipeapp.Adapters.ItemsAdapter;
import com.example.recipeapp.MainActivity;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.R;
import com.example.recipeapp.Network.RecipeClient;
import com.example.recipeapp.databinding.FragmentSearchBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
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



        binding.spinnerCuisine.setOptions(Arrays.asList(getResources().getStringArray(R.array.cuisines)));
        binding.spinnerType.setOptions(Arrays.asList(getResources().getStringArray(R.array.types)));

        //binding.edInclude.setCall((query, handler) -> RecipeClient.getInstance().getIngredientAutocomplete(query, handler));
        //binding.edExclude.setCall((query, handler) -> RecipeClient.getInstance().getIngredientAutocomplete(query, handler));
        binding.edExclude.setAdapter(
                new AutoCompleteAdapter(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        (query, handler) -> RecipeClient.getInstance().getIngredientAutocomplete(query, handler)));
        binding.edInclude.setAdapter(
                new AutoCompleteAdapter(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        (query, handler) -> RecipeClient.getInstance().getIngredientAutocomplete(query, handler)));

        //binding.etTitle.setCall((query, handler) -> RecipeClient.getInstance().getTitleAutocomplete(query, handler));

        binding.etTitle.setAdapter(
                new AutoCompleteAdapter(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        (query, handler) -> RecipeClient.getInstance().getIngredientAutocomplete(query, handler)));

        includedAdapter = new ItemsAdapter(included, R.layout.item);
        binding.rvInclude.setAdapter(includedAdapter);
        binding.rvInclude.setLayoutManager(getFlexboxLayoutManager());

        excludedAdapter = new ItemsAdapter(excluded, R.layout.item);
        binding.rvExclude.setAdapter(excludedAdapter);
        binding.rvExclude.setLayoutManager(getFlexboxLayoutManager());

        binding.btnInclude.setOnClickListener(v -> {
            included.add(binding.edInclude.getText().toString());
            includedAdapter.notifyItemInserted(included.size() - 1);
            binding.edInclude.setText(null);
        });

        binding.btnExclude.setOnClickListener(v -> {
            excluded.add(binding.edExclude.getText().toString());
            excludedAdapter.notifyItemInserted(excluded.size() - 1);
            binding.edExclude.setText(null);
        });

        binding.btnSearch.setOnClickListener(v -> searchListener());
    }

    private void searchListener() {
        String cuisine = null;
        String excludeCuisine = null;
        if(binding.checkboxExcludeCuisine.isChecked()) {
            excludeCuisine = binding.spinnerCuisine.getSelectedItem();
        } else {
            cuisine = binding.spinnerCuisine.getSelectedItem();
        }

        RecipeClient.getInstance().getRecipesWithFilters(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    FeedFragment feedFragment = ((MainActivity) getActivity()).getFeedFragment();
                    List<Recipe> result = Recipe.fromJsonArray(json.jsonObject.getJSONArray("results"));
                    feedFragment.getRecipes().clear();
                    feedFragment.getRecipes().addAll(result);
                    ((MainActivity)(getActivity())).getBinding().bottomNavigation.setSelectedItemId(R.id.feed);

                    Log.i(TAG, "success: " + json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                
                Log.e(TAG, "Recipes search failed: " + response);
            }
        }, binding.etTitle.getText().toString(), cuisine, excludeCuisine,
                String.join(",", included),String.join(",", excluded),
                binding.spinnerType.getSelectedItem(), binding.edTime.getText().toString());
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