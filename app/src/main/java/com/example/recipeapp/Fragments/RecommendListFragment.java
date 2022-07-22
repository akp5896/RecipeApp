package com.example.recipeapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recipeapp.Adapters.RecommendRecipeAdapter;
import com.example.recipeapp.R;
import com.example.recipeapp.databinding.FragmentRecommendListBinding;

import java.util.List;

public class RecommendListFragment extends DialogFragment {

    List<Pair<String, Long>> recipes;
    FragmentRecommendListBinding binding;

    public RecommendListFragment() {
        // Required empty public constructor
    }

    public static RecommendListFragment newInstance(List<Pair<String, Long>> recipes) {
        RecommendListFragment fragment = new RecommendListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.recipes = recipes;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvRecipes.setAdapter(new RecommendRecipeAdapter(recipes, getContext(), this));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecommendListBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        getDialog().getWindow().setLayout((6 * width) / 7, (3 * height) / 5);
        super.onStart();
    }
}