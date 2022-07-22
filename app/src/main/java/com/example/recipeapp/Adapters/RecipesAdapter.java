package com.example.recipeapp.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipeapp.Activities.DetailsActivity;
import com.example.recipeapp.Activities.MainActivity;
import com.example.recipeapp.Models.Parse.Like;
import com.example.recipeapp.Models.Parse.ParseRecipe;
import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.DiffUtil.RecipeDiffUtilCallback;
import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.R;
import com.example.recipeapp.databinding.RecipeItemBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

    private static final String TAG = "RECIPES ADAPTER";
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

    public void updateList(List<Recipe> recipes) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new RecipeDiffUtilCallback(this.recipes, recipes));
        this.recipes.clear();
        this.recipes.addAll(recipes);
        diffResult.dispatchUpdatesTo(this);
    }
    class RecipesViewHolder extends RecyclerView.ViewHolder{

        RecipeItemBinding binding;

        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecipeItemBinding.bind(itemView);
        }

        public void bind(Recipe item) {
            binding.tvTitle.setText(item.getTitle());
            Glide.with(context).load(item.getImage()).error(R.drawable.ic_launcher_background).into(binding.ivImage);
            binding.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailsActivity.class);
                    i.putExtra(DetailsActivity.RECIPE, Parcels.wrap(item));
                    context.startActivity(i);
                }
            });

            binding.ivHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, R.string.liked, Toast.LENGTH_SHORT).show();
                    if(context instanceof MainActivity) {
                        getLocation(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                Like like = new Like();
                                like.setLikeTo(item.getId());
                                like.setTitle(item.getTitle());
                                like.setLocation(new ParseGeoPoint(location.getLatitude(), location.getLongitude()));
                                like.saveInBackground(e1 -> {
                                    if (e1 != null) {
                                        Log.w(TAG, "Like not saved" + e1);
                                    }
                                });
                            }
                        });
                    }
                    item.getTaste(new Callback<Taste>() {
                        @Override
                        public void onResponse(Call<Taste> call, Response<Taste> response) {
                            Preferences preferences = (Preferences) ParseUser.getCurrentUser().getParseObject(Preferences.KEY_PREFERENCES);
                            preferences.updatePreferences(item, response.body());
                            preferences.saveInBackground();
                        }

                        @Override
                        public void onFailure(Call<Taste> call, Throwable t) {
                            Log.e(TAG, "Failure : " + t);
                        }
                    });
                }
            });
        }
    }

    private void getLocation(OnSuccessListener<? super Location> onSuccess) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    37);
        }
        ((MainActivity) context).fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) context, onSuccess)
                .addOnFailureListener(e -> Log.e(TAG, "error" + e));
    }
}

