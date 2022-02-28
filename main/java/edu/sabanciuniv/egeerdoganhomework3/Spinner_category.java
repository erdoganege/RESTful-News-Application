package edu.sabanciuniv.egeerdoganhomework3;

import androidx.annotation.NonNull;

public class Spinner_category {

    private String category;
    private int id;

    public Spinner_category() {
    }

    public Spinner_category(String category, int id) {
        this.category = category;
        this.id = id;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return category;
    }
}
