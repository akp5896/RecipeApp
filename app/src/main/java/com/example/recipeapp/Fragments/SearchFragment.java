package com.example.recipeapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.recipeapp.ItemsAdapter;
import com.example.recipeapp.R;
import com.example.recipeapp.databinding.FragmentSearchBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;

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

        List<String> items;
        items = new ArrayList<String>();
        items.add("CucumberCucumber");
        items.add("Cucumber");
        items.add("Cucumber");
        items.add("Tomato");
        items.add("Cheese");
        items.add("Milk");
        items.add("Potato");
        items.add("Cucumber");
        items.add("Beer");
        items.add("Tomato");
        items.add("Cheese");
        items.add("Milk");
        items.add("Potato");
        items.add("Cucumber");
        items.add("Beer");

        ItemsAdapter adapter = new ItemsAdapter(items);
        binding.rvInclude.setAdapter(adapter);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        binding.rvInclude.setLayoutManager(layoutManager);

        ItemsAdapter adapter2 = new ItemsAdapter(items);
        binding.rvExclude.setAdapter(adapter2);
        FlexboxLayoutManager layoutManager2 = new FlexboxLayoutManager(getContext());
        layoutManager2.setFlexDirection(FlexDirection.ROW);
        layoutManager2.setFlexWrap(FlexWrap.WRAP);
        binding.rvExclude.setLayoutManager(layoutManager2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}