package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.recipeapp.Adapters.CheckboxAdapter;
import com.example.recipeapp.CustomViews.StateVO;
import com.example.recipeapp.databinding.ActivityEditPreferencesBinding;

import java.util.ArrayList;

public class EditPreferencesActivity extends AppCompatActivity {

    ActivityEditPreferencesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditPreferencesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.spinnerDiet.setAdapter(
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        getResources().getStringArray(R.array.diets)
                )
        );

        ArrayList<StateVO> cuisinesVO = new ArrayList<>();
        String[] cuisines = getResources().getStringArray(R.array.cuisines);
        for (String cuisine : cuisines) {
            StateVO stateVO = new StateVO();
            stateVO.setTitle(cuisine);
            stateVO.setSelected(false);
            cuisinesVO.add(stateVO);
        }

        CheckboxAdapter cuisineAdapter = new CheckboxAdapter(this, 0,
                cuisinesVO);
        binding.spinnerCuisine.setAdapter(cuisineAdapter);

        ArrayList<StateVO> intoleranceVO = new ArrayList<>();
        String[] intolerances = getResources().getStringArray(R.array.intolerances);
        for (String intolerance : intolerances) {
            StateVO stateVO = new StateVO();
            stateVO.setTitle(intolerance);
            stateVO.setSelected(false);
            intoleranceVO.add(stateVO);
        }

        CheckboxAdapter intoleranceAdapter = new CheckboxAdapter(this, 0,
                intoleranceVO);
        binding.spinnerIntolerances.setAdapter(intoleranceAdapter);

    }
}