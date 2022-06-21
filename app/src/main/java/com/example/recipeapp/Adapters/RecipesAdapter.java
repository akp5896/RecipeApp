package com.example.recipeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.R;
import com.example.recipeapp.databinding.ItemBinding;
import com.example.recipeapp.databinding.RecipeItemBinding;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    List<Recipe> recipes;
    Context context;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(view);
    }

    public RecipesAdapter(List<Recipe> recipes, Context context) {
        this.recipes = recipes;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RecipeItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecipeItemBinding.bind(itemView);
        }

        public void bind(Recipe item) {
            binding.tvTitle.setText(item.getTitle());
            Glide.with(context).load(item.getImage()).into(binding.ivImage);
        }
    }
}

