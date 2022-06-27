package com.example.recipeapp.Adapters;

import android.app.Notification;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.recipeapp.Network.RecipeClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    public interface NetworkCall {
        void makeCall(String query, JsonHttpResponseHandler handler);
    }

    private ArrayList<String> data;
    private NetworkCall call;

    public AutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource, NetworkCall call) {
        super(context, resource);
        this.data = new ArrayList<>();
        this.call = call;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    try {
                        call.makeCall(constraint.toString(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                try {
                                    ArrayList<String> suggestions = new ArrayList<>();
                                    JSONArray response = json.jsonArray;
                                    for (int i = 0; i < response.length(); i++) {
                                        suggestions.add(response.getJSONObject(i).getString("name"));
                                    }
                                    results.values = suggestions;
                                    results.count = suggestions.size();
                                    data = suggestions;
                                    notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("AutoAdapter", "query failed");
                            }
                        });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else notifyDataSetInvalidated();
            }
        };
    }
}

