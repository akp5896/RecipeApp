package com.example.recipeapp.DiffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.example.recipeapp.viewmodels.ReviewItemViewModel;

import java.util.List;
import java.util.Objects;

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
        ReviewItemViewModel oldModel = oldList.get(oldItemPosition);
        ReviewItemViewModel newModel = newList.get(newItemPosition);
        return Objects.equals(oldModel.authorName, newModel.authorName) && Objects.equals(oldModel.body, newModel.body);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Since reviews are immutable
        return true;
    }
}
