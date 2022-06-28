package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.recipeapp.Adapters.AutoCompleteAdapter;
import com.example.recipeapp.Adapters.CheckboxAdapter;
import com.example.recipeapp.Adapters.ItemsAdapter;
import com.example.recipeapp.CustomViews.StateVO;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.databinding.ActivityEditPreferencesBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;

public class EditPreferencesActivity extends AppCompatActivity {

    public static final String DIET = "diet";
    public static final String CUISINES = "cuisines";
    public static final String INTOLERANCES = "intolerances";
    public static final String BANNED = "banned";
    ActivityEditPreferencesBinding binding;
    ItemsAdapter banAdapter;
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
        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        binding.edBan.setAdapter(
                new AutoCompleteAdapter<Ingredient>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        (query, callback) -> {
                            Call<List<Ingredient>> call = service.getIngredientAutocomplete(BuildConfig.API_KEY, query, 5);
                            call.enqueue(callback);
                        }));

        banAdapter = new ItemsAdapter(banned, R.layout.item);
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
        });
    }
}