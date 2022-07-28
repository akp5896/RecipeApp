package com.example.recipeapp.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.BR;
import com.example.recipeapp.R;
import com.example.recipeapp.viewmodels.StepViewModel;

import java.util.ArrayList;
import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.BindableViewHolder>{
    private List<StepViewModel> data = new ArrayList<>();

    @NonNull
    @Override
    public BindableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_list,
                parent,
                false
        );
        return new BindableViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull BindableViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    public void updateItems(List<StepViewModel> items) {
        data.addAll(items);
        notifyDataSetChanged();
    }

    public void add(StepViewModel item) {
        data.add(item);
        notifyItemInserted(data.size() - 1);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class BindableViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public BindableViewHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(StepViewModel item) {
            binding.setVariable(BR.itemViewModel, item);
        }
    }
}
