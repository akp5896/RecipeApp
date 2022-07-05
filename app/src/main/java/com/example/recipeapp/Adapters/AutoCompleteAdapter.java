package com.example.recipeapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.recipeapp.Retrofit.RetrofitAutocomplete;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutoCompleteAdapter<T extends RetrofitAutocomplete> extends ArrayAdapter<String> implements Filterable {
    private static final String TAG = "AutoAdapter";

    public interface NetworkCall<T extends RetrofitAutocomplete> {
        void makeCall(String query, Callback<List<T>> callback);
    }

    private ArrayList<String> data;
    private NetworkCall<T> call;

    public AutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource, NetworkCall<T> call) {
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
                        call.makeCall(constraint.toString(), new Callback<List<T>>() {
                            @Override
                            public void onResponse(Call<List<T>> call, Response<List<T>> response) {
                                ArrayList<String> suggestions = new ArrayList<>();
                                List<T> body = response.body();
                                for(int i = 0; i < body.size(); i++) {
                                    suggestions.add(body.get(i).getName());
                                }
                                results.values = suggestions;
                                results.count = suggestions.size();
                                data = suggestions;
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<List<T>> call, Throwable t) {
                                Log.e(TAG, "query failed" + t);
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

