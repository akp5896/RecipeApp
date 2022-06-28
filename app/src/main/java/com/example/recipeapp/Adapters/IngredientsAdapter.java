package com.example.recipeapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.R;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.Retrofit.SubEnvelope;
import com.example.recipeapp.databinding.ItemIngredientsBinding;
import com.roughike.swipeselector.SwipeItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>{

    private static final String TAG = "INGR ADAPTER";
    List<Ingredient> ingredients;

    public IngredientsAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredients, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        holder.bindIngredientsSubstitute(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }


    public class IngredientsViewHolder extends RecyclerView.ViewHolder{
        ItemIngredientsBinding binding;
        public IngredientsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemIngredientsBinding.bind(itemView);
        }

        public void bindIngredientsSubstitute(Ingredient s) {
            RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
            Call<SubEnvelope<List<String>>> call = service.getIngredientSubstitute(s.getId(), BuildConfig.API_KEY);
            call.enqueue(new Callback<SubEnvelope<List<String>>>() {
                @Override
                public void onResponse(Call<SubEnvelope<List<String>>> call, Response<SubEnvelope<List<String>>> response) {
                    SubEnvelope<List<String>> x = response.body();
                    if(x.status.equals(RecipeApi.FAILURE)) {
                        binding.swipeSelector.setItems(new SwipeItem(0, s.getName(), s.getName()));
                        return;
                    }
                    List<String> subs = x.results;
                    SwipeItem[] items = new SwipeItem[subs.size() + 1];
                    items[0] = new SwipeItem(0, s.getName(), s.getName());
                    for(int i = 0; i < subs.size(); i++) {
                        items[i + 1] = new SwipeItem(i + 1, s.getName(), subs.get(i));
                    }
                    binding.swipeSelector.setItems(items);
                }

                @Override
                public void onFailure(Call<SubEnvelope<List<String>>> call, Throwable t) {
                    Log.i(TAG, "Couldn't retrieve substitutes");
                }
            });

        }
    }
}
