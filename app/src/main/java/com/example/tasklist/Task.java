package com.example.tasklist;

public class Task {
    private String title;
    private String description;
    private String date;
    private Long taskId;

    public Task(Long id, String title, String description, String date) {
        this.taskId = id;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public Long getTaskId() { return taskId; }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
