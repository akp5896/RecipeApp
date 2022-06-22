package com.example.recipeapp.CustomViews;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.R;
import com.example.recipeapp.RecipeClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class NetworkAutocomplete extends FrameLayout {

    private static final long NETWORK_TIME_DELTA = 1000;
    private static final String TAG = "AUTOCOMPLETE";
    long lastClickTime = 0;

    private EditText search;
    private MyListView suggestions;
    private ArrayAdapter adapter;
    private List<String> response = new ArrayList<>();

    public NetworkAutocomplete(@NonNull Context context) {
        super(context);
        initView(context);
    }
    public NetworkAutocomplete(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NetworkAutocomplete(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public NetworkAutocomplete(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View v = inflate(getContext(), R.layout.network_automcomplete, this);
        search = v.findViewById(R.id.search_bar);
        suggestions = v.findViewById(R.id.suggestions);


        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime > NETWORK_TIME_DELTA){
                    RecipeClient.getInstance()
                            .getIngredientAutocomplete(search.getText().toString(), new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Headers headers, JSON json) {
                                    Log.i(TAG, "Autocomplete Success! " + json);
                                    try {
                                        JSONArray results = json.jsonArray;
                                        response.clear();
                                        for(int i = 0; i < results.length(); i++) {

                                            response.add(results.getJSONObject(i).getString("name"));
                                        }
                                        adapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1, response);
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

            }
        });
    }
}
