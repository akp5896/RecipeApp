package com.example.recipeapp.Utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.core.view.GravityCompat;

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
                    binding.drawerLayout.openDrawer(GravityCompat.START);
                    Toast.makeText(context, "right2left swipe", Toast.LENGTH_SHORT).show ();
                }   
                break;
        }
        return true;
    }
}
