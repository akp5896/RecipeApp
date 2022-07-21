package com.example.recipeapp.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.BR;
import com.example.recipeapp.DiffUtil.ReviewDiffUtilCallback;
import com.example.recipeapp.R;
import com.example.recipeapp.viewmodels.ReviewItemViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.BindableViewHolder> {

    private final LinkedList<ReviewItemViewModel> data = new LinkedList<>();
    MutableLiveData<ReviewItemViewModel> itemClicked;

    @NonNull
    @Override
    public BindableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.review_item,
                parent,
                false
        );
        return new BindableViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindableViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    public void updateItems(List<ReviewItemViewModel> items) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ReviewDiffUtilCallback(data, items));
        data.clear();
        data.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }

    public void addAtTheBeginning(ReviewItemViewModel item) {
        data.addFirst(item);
        notifyItemInserted(0);
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

        public void bind(ReviewItemViewModel item) {
            binding.setVariable(BR.itemViewModel, item);
        }
    }
}
