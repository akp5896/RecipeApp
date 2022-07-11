package com.example.recipeapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recipeapp.Adapters.AutoCompleteAdapter;
import com.example.recipeapp.Adapters.StepsAdapter;
import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.R;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.databinding.FragmentFeedBinding;
import com.example.recipeapp.databinding.FragmentSuggestBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class SuggestFragment extends Fragment {

    FragmentSuggestBinding binding;
    StepsAdapter iHaveAdapter;
    List<String> iHave = new ArrayList<>();

    public SuggestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        binding.edIngredients.setAdapter(
                new AutoCompleteAdapter<Ingredient>(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        (query, callback) -> {
                            Call<List<Ingredient>> call = service.getIngredientAutocomplete(BuildConfig.API_KEY, query, 5);
                            call.enqueue(callback);
                        }));

        iHaveAdapter = new StepsAdapter(iHave, R.layout.item);
        binding.rvIngredients.setAdapter(iHaveAdapter);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        binding.rvIngredients.setLayoutManager(layoutManager);

        binding.btnAdd.setOnClickListener(v -> {
            iHave.add(binding.edIngredients.getText().toString());
            iHaveAdapter.notifyItemInserted(iHave.size() - 1);
            binding.edIngredients.setText(null);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSuggestBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}