package com.example.recipeapp.DiffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.example.recipeapp.viewmodels.ReviewItemViewModel;

import java.util.List;

public class ReviewDiffUtilCallback extends DiffUtil.Callback {
    private List<ReviewItemViewModel> oldList;
    private List<ReviewItemViewModel> newList;

    public ReviewDiffUtilCallback(List<ReviewItemViewModel> oldList, List<ReviewItemViewModel> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Since reviews are immutable
        return true;
    }
}
