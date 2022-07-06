package com.example.recipeapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewbinding.ViewBinding;

import com.example.recipeapp.MainActivity;
import com.example.recipeapp.Utils.LeftSwipeListener;
import com.example.recipeapp.databinding.FragmentFeedBinding;

public class LeftSwipeDrawerFragment extends Fragment {

    private static final String TAG = "LEFT SWIPE FRAGMENT";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentActivity activity = getActivity();
        if(activity instanceof MainActivity) {
            view.setOnTouchListener(new LeftSwipeListener(getContext(), ((MainActivity)activity).getProfileLayoutBinding()));
        }
        else {
            Log.w(TAG, "Activity does not have drawer. No listener attached.");
        }
    }
}
