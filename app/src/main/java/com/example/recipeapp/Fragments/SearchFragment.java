package com.example.recipeapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.recipeapp.Adapters.AutoCompleteAdapter;
import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Adapters.IngredientFilterAdapter;
import com.example.recipeapp.Activities.MainActivity;
import com.example.recipeapp.Models.API.ApiCallParams;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Models.API.RecipeTitle;
import com.example.recipeapp.R;
import com.example.recipeapp.Retrofit.Envelope;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.Utils.LeftSwipeListener;
import com.example.recipeapp.databinding.FragmentSearchBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends LeftSwipeDrawerFragment {

    private static final String TAG = "Search fragment";
    FragmentSearchBinding binding;
    List<String> included = new ArrayList<>();
    List<String> excluded = new ArrayList<>();
    IngredientFilterAdapter includedAdapter;
    IngredientFilterAdapter excludedAdapter;

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


        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        binding.edExclude.setAdapter(
                new AutoCompleteAdapter<Ingredient>(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        (query, callback) -> {
                            Call<List<Ingredient>> call = service.getIngredientAutocomplete(BuildConfig.API_KEY, query, 5);
                            call.enqueue(callback);
                        }));
        binding.edInclude.setAdapter(
                new AutoCompleteAdapter<Ingredient>(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        (query, callback) -> {
                            Call<List<Ingredient>> call = service.getIngredientAutocomplete(BuildConfig.API_KEY, query, 5);
                            call.enqueue(callback);
                        }));

        binding.etTitle.setAdapter(
                new AutoCompleteAdapter<RecipeTitle>(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        (query, callback) -> {
                            Call<List<RecipeTitle>> call = service.getTitleAutocomplete(BuildConfig.API_KEY, query, 5);
                            call.enqueue(callback);
                        }));

        includedAdapter = new IngredientFilterAdapter(included, R.layout.item);
        binding.rvInclude.setAdapter(includedAdapter);
        binding.rvInclude.setLayoutManager(getFlexboxLayoutManager());

        excludedAdapter = new IngredientFilterAdapter(excluded, R.layout.item);
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
            excludeCuisine = putWithEmptyCheck(binding.spinnerCuisine.getSelectedItem());
        } else {
            cuisine = putWithEmptyCheck(binding.spinnerCuisine.getSelectedItem());
        }

        ApiCallParams params = new ApiCallParams(putWithEmptyCheck(binding.etTitle.getText()), cuisine, excludeCuisine,
                putWithEmptyCheck(String.join(",", included)), putWithEmptyCheck(String.join(",", excluded)),
                putWithEmptyCheck(binding.spinnerType.getSelectedItem()), putWithEmptyCheck(binding.edTime.getText().toString()));

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPlaceholder, SearchFeedFragment.newInstance(params)).commit();
    }

    private String putWithEmptyCheck(CharSequence chars) {
        if(chars == null || chars.toString().equals(""))
            return null;
        return chars.toString();
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