package com.example.recipeapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.R;
import com.example.recipeapp.Repositories.IngredientsRepository;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.Retrofit.SubEnvelope;
import com.example.recipeapp.databinding.ItemIngredientsBinding;
import com.roughike.swipeselector.SwipeItem;

import java.util.ConcurrentModificationException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>{

    private static final String TAG = "INGR ADAPTER";
    List<Ingredient> ingredients;
    Context context;

    public IngredientsAdapter(List<Ingredient> ingredients, Context context) {
        this.ingredients = ingredients;
        this.context = context;
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
            binding.swipeSelector.setItems(new SwipeItem(0, s.getName(), s.getName()));
            IngredientsRepository.getRepository().getIngredientSubstitutes(s.getId()).observe((AppCompatActivity)context, listSubEnvelope -> {
                if(listSubEnvelope.status.equals(RecipeApi.FAILURE)) {
                    binding.swipeSelector.setItems(new SwipeItem(0, s.getName(), s.getName()));
                    return;
                }
                List<String> subs = listSubEnvelope.results;
                SwipeItem[] items = new SwipeItem[subs.size() + 1];
                items[0] = new SwipeItem(0, s.getName(), s.getName());
                for(int i = 0; i < subs.size(); i++) {
                    items[i + 1] = new SwipeItem(i + 1, s.getName(), subs.get(i));
                }
                binding.swipeSelector.setItems(items);
            });
        }
    }
}
