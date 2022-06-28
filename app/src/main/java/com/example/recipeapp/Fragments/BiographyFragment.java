package com.example.recipeapp.Fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.recipeapp.databinding.FragmentBiographyBinding;
import com.example.recipeapp.databinding.ProfileLayoutBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class BiographyFragment extends DialogFragment {
    public static final String BIO_TAG = "fragment_compose_biography";
    FragmentBiographyBinding binding;
    private String text;
    ProfileLayoutBinding headerBinding;

    public BiographyFragment() {
    }
    public static BiographyFragment newInstance(String text, ProfileLayoutBinding headerBinding) {
        BiographyFragment fragment = new BiographyFragment();
        fragment.text = text;
        fragment.headerBinding = headerBinding;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.etEditBio.setText(text);
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                String bio = binding.etEditBio.getText().toString();
                user.put("bio", bio);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        headerBinding.tvBio.setText(bio);
                        dismiss();
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBiographyBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        getDialog().getWindow().setLayout((6 * width) / 7, (3 * height) / 5);
        super.onStart();
    }
}