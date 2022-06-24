package com.example.recipeapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.DetailsActivity;
import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.R;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.databinding.ItemBinding;
import com.example.recipeapp.databinding.RecipeItemBinding;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

    List<Recipe> recipes;
    Context context;


    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipesViewHolder(view);
    }

    public RecipesAdapter(List<Recipe> recipes, Context context) {
        this.recipes = recipes;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipesViewHolder extends RecyclerView.ViewHolder{

        RecipeItemBinding binding;

        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecipeItemBinding.bind(itemView);
        }

        public void bind(Recipe item) {
            binding.tvTitle.setText(item.getTitle());
            Glide.with(context).load(item.getImage()).into(binding.ivImage);
            binding.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailsActivity.class);
                    i.putExtra("recipe", Parcels.wrap(item));
                    context.startActivity(i);
                }
            });

            binding.ivHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show();
                    RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
                    Call<Taste> call = service.getTasteById(item.getId(), BuildConfig.API_KEY);
                    call.enqueue(new Callback<Taste>() {
                        @Override
                        public void onResponse(Call<Taste> call, Response<Taste> response) {
                            Preferences preferences = (Preferences) ParseUser.getCurrentUser().getParseObject("preferences");
                            preferences.updatePreferences(item, response.body());
                            preferences.saveInBackground();
                        }

                        @Override
                        public void onFailure(Call<Taste> call, Throwable t) {

                        }
                    });
                }
            });
        }
    }
}

