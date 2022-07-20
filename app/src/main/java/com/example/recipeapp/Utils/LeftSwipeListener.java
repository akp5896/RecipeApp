package com.example.recipeapp.Utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.recipeapp.databinding.ProfileLayoutBinding;

public class LeftSwipeListener implements View.OnTouchListener {
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    Context context;
    ProfileLayoutBinding binding;

    public LeftSwipeListener(Context context, ProfileLayoutBinding binding) {
        this.context = context;
        this.binding = binding;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x2 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x1 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    binding.drawerLayout.setVisibility(View.VISIBLE);
                    binding.drawerLayout.openDrawer(GravityCompat.START);
                    binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                        @Override
                        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                        }

                        @Override
                        public void onDrawerOpened(@NonNull View drawerView) {

                        }

                        @Override
                        public void onDrawerClosed(@NonNull View drawerView) {
                            binding.drawerLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onDrawerStateChanged(int newState) {

                        }
                    });
                }
                break;
        }
        return true;
    }
}
