package com.example.recipeapp.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.R;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    List<String> items;
    int layoutId = R.layout.item;

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View todoView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new StepsViewHolder(todoView);
    }

    public StepsAdapter(List<String> items, int layoutId)
    {
        this.items = items;
        this.layoutId = layoutId;
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        String item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    // General ViewHolder used to display simple string lists, for example, in autocomplete
    class StepsViewHolder extends RecyclerView.ViewHolder{

        TextView tvText;
        ConstraintLayout layout;

        public StepsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.text);
            layout = itemView.findViewById(R.id.layout);
        }

        public void bind(String item) {
            tvText.setText(item);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = getAdapterPosition();
                    items.remove(i);
                    notifyItemRemoved(i);
                }
            });
        }
    }
}
