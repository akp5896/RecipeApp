package com.example.recipeapp.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.recipeapp.Adapters.SpinnerAdapter;
import com.example.recipeapp.R;
import com.example.recipeapp.Network.RecipeClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class NetworkAutocompleteView extends FrameLayout {

    private static final long NETWORK_TIME_DELTA = 1000;
    private static final String TAG = "AUTOCOMPLETE";
    long lastClickTime = 0;

    private SearchView search;
    private MyListView suggestions;
    private SpinnerAdapter adapter;
    private List<String> response = new ArrayList<>();
    NetworkCall call;

    public interface NetworkCall {
        void makeCall(String query, JsonHttpResponseHandler handler);
    }

    public NetworkAutocompleteView(@NonNull Context context) {
        super(context);
        initView(context);
    }
    public NetworkAutocompleteView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NetworkAutocompleteView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public NetworkAutocompleteView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public String getText() {
        return search.getQuery().toString();
    }

    public void setText(CharSequence seq) {
        search.setQuery(seq, false);
    }

    public void setCall(NetworkCall call) {
        this.call = call;
    }

    private void initView(Context context) {
        View v = inflate(getContext(), R.layout.network_automcomplete, this);
        search = v.findViewById(R.id.search_bar);
        suggestions = v.findViewById(R.id.suggestions);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                response.clear();
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(adapter != null && adapter.getFinished())
                    return true;
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime > NETWORK_TIME_DELTA){
                    call.makeCall(search.getQuery().toString(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "Autocomplete Success! " + json);
                            try {
                                JSONArray results = json.jsonArray;
                                response.clear();
                                for(int i = 0; i < results.length(); i++) {

                                    response.add(results.getJSONObject(i).getString("name"));
                                }
                                adapter = new SpinnerAdapter(context,android.R.layout.simple_list_item_1, response, search);
                                suggestions.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.i(TAG, "Automcomplete fail! " + response);
                        }
                    });
                }
                lastClickTime = clickTime;
                return true;
            }
        });
    }
}
