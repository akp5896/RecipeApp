package com.example.recipeapp.viewmodels;

public class StepViewModel {

    private final String step;
    private final int number;

    public StepViewModel(String step, int number) {
        this.step = step;
        this.number = number;
    }

    public String getStep() {
        return number + ". " + step;
    }
}
