package com.example.recipeapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

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
        MutableLiveData<List<T>> makeCall(String query);
    }

    private ArrayList<String> data;
    private NetworkCall<T> call;
    private Context context;

    public AutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource, NetworkCall<T> call) {
        super(context, resource);
        this.data = new ArrayList<>();
        this.call = call;
        this.context = context;
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
                        MutableLiveData<List<T>> listMutableLiveData = call.makeCall(constraint.toString());
                        ((AppCompatActivity)context).runOnUiThread(
                            () -> listMutableLiveData.observe((AppCompatActivity)context, body -> {
                            ArrayList<String> suggestions = new ArrayList<>();
                            for(int i = 0; i < body.size(); i++) {
                                suggestions.add(body.get(i).getName());
                            }
                            results.values = suggestions;
                            results.count = suggestions.size();
                            data = suggestions;
                            notifyDataSetChanged();
                        }));
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

