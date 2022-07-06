package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.recipeapp.Adapters.AutoCompleteAdapter;
import com.example.recipeapp.Adapters.CheckboxAdapter;
import com.example.recipeapp.Adapters.StepsAdapter;
import com.example.recipeapp.CustomViews.MultipleSpinnerItem;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.Settings;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.databinding.ActivityEditPreferencesBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;

public class EditPreferencesActivity extends AppCompatActivity {

    public static final String DIET = "diet";
    public static final String CUISINES = "cuisines";
    public static final String INTOLERANCES = "intolerances";
    public static final String BANNED = "banned";
    ActivityEditPreferencesBinding binding;
    StepsAdapter banAdapter;
    List<String> banned = new ArrayList<>();

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

        binding.spinnerDiet.setSelection(getDietIndex());

        ArrayList<MultipleSpinnerItem> cuisinesCheckboxItems = new ArrayList<>();
        String[] cuisines = getResources().getStringArray(R.array.cuisines);
        for (String cuisine : cuisines) {
            MultipleSpinnerItem cuisineItem = new MultipleSpinnerItem();
            cuisineItem.setTitle(cuisine);
            cuisineItem.setSelected(Settings.getCuisines().contains(cuisine));
            cuisinesCheckboxItems.add(cuisineItem);
        }

        CheckboxAdapter cuisineAdapter = new CheckboxAdapter(this, 0,
                cuisinesCheckboxItems);
        binding.spinnerCuisine.setAdapter(cuisineAdapter);

        ArrayList<MultipleSpinnerItem> intoleranceCheckboxItems = new ArrayList<>();
        String[] intolerances = getResources().getStringArray(R.array.intolerances);
        for (String intolerance : intolerances) {
            MultipleSpinnerItem intoleranceItem = new MultipleSpinnerItem();
            intoleranceItem.setTitle(intolerance);
            intoleranceItem.setSelected(Settings.containsIntolerance(intolerance));
            intoleranceCheckboxItems.add(intoleranceItem);
        }

        CheckboxAdapter intoleranceAdapter = new CheckboxAdapter(this, 0,
                intoleranceCheckboxItems);
        binding.spinnerIntolerances.setAdapter(intoleranceAdapter);
        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        binding.edBan.setAdapter(
                new AutoCompleteAdapter<Ingredient>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        (query, callback) -> {
                            Call<List<Ingredient>> call = service.getIngredientAutocomplete(BuildConfig.API_KEY, query, 5);
                            call.enqueue(callback);
                        }));
        banned.addAll(Settings.getBanned());
        banAdapter = new StepsAdapter(banned, R.layout.item);
        binding.rvBan.setAdapter(banAdapter);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        binding.rvBan.setLayoutManager(layoutManager);

        binding.btnAdd.setOnClickListener(v -> {
            banned.add(binding.edBan.getText().toString());
            banAdapter.notifyItemInserted(banned.size() - 1);
            binding.edBan.setText(null);
        });

        binding.btnSave.setOnClickListener(v -> {
            SharedPreferences pref =
                    PreferenceManager.getDefaultSharedPreferences(EditPreferencesActivity.this);
            SharedPreferences.Editor edit = pref.edit();

            edit.putString(DIET, binding.spinnerDiet.getSelectedItem().toString());
            edit.putStringSet(CUISINES, cuisineAdapter.getChecked());
            edit.putStringSet(INTOLERANCES, intoleranceAdapter.getChecked());
            edit.putStringSet(BANNED, new HashSet<>(banned));
            edit.apply();
            Settings.setDiet(binding.spinnerDiet.getSelectedItem().toString());
            Settings.setCuisines(cuisineAdapter.getChecked());
            Settings.setIntolerances(intoleranceAdapter.getChecked());
            Settings.setBanned(new HashSet<>(banned));
            finish();
        });
    }

    private int getDietIndex() {
        if(Settings.getDiet() == null) {
            return 0;
        }
        return Arrays.asList(getResources().getStringArray(R.array.diets)).indexOf(Settings.getDiet());
    }
}