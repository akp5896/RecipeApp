package com.example.recipeapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.recipeapp.IngredientsActivity;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Network.RecipeClient;
import com.example.recipeapp.R;
import com.example.recipeapp.databinding.ItemIngredientsBinding;
import com.roughike.swipeselector.SwipeItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder>{

    private static final String TAG = "INGR ADAPTER";
    Context context;
    List<Ingredient> ingredients;

    public IngredientsAdapter(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredients, parent, false);
        return new IngredientsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemIngredientsBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemIngredientsBinding.bind(itemView);
        }

        public void binding(Ingredient s) {
            RecipeClient.getInstance().getIngredientSubstitute(s.getId(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    JSONObject result = json.jsonObject;
                    try {
                        if(result.getString("status").equals("failure")) {
                            binding.swipeSelector.setItems(new SwipeItem(0, s.getName(), s.getName()));
                            return;
                        }
                        JSONArray subs = result.getJSONArray("substitutes");
                        SwipeItem[] items = new SwipeItem[subs.length() + 1];
                        items[0] = new SwipeItem(0, s.getName(), s.getName());
                        for(int i = 0; i < subs.length(); i++) {
                            items[i + 1] = new SwipeItem(i + 1, s.getName(), subs.getString(i));
                        }
                        binding.swipeSelector.setItems(items);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.i(TAG, "Couldn't retrieve substitutes");
                }
            });
        }
    }
}
