package com.example.recipeapp.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipeapp.Activities.DetailsActivity;
import com.example.recipeapp.Fragments.RecommendListFragment;
import com.example.recipeapp.Models.Parse.Like;
import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.R;
import com.example.recipeapp.databinding.RecipeItemBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendRecipeAdapter extends RecyclerView.Adapter<RecommendRecipeAdapter.RecipesViewHolder> {

    private static final String TAG = "RECIPES ADAPTER";
    List<Pair<String, Long>> recipes;
    Context context;
    RecommendListFragment fragment;

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new RecipesViewHolder(view);
    }

    public RecommendRecipeAdapter(List<Pair<String, Long>> recipes, Context context, RecommendListFragment fragment) {
        this.recipes = recipes;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipesViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        Long id;

        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(android.R.id.text1);
        }

        public void bind(Pair<String, Long> item) {
            tvName.setText(item.first);
            this.id = item.second;
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailsActivity.class);
                    i.putExtra("id", item.second);
                    i.putExtra(DetailsActivity.RELOAD, true);
                    context.startActivity(i);
                    fragment.dismiss();
                }
            });
        }
    }
}