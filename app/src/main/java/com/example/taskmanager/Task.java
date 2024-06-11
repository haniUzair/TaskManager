package com.example.taskmanager;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private String id;
    private String title;
    private String date;
    private String startTime;
    private String endTime;
    private String description;
    private List<String> categories;

    public Task() {
        // Default constructor required for calls to DataSnapshot.getValue(Task.class)
    }

    public Task(String id, String title, String date, String startTime, String endTime, String description) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.categories = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void addCategory(String category) {
        categories.add(category);
    }
}



