package com.gabrielmatheus.apphub.todo;

import java.io.Serializable;

public class Task implements Serializable {
    public String title;
    public String dueDate;
    public String priority;
    public boolean isDone;

    public Task(String title, String dueDate, String priority) {
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
        this.isDone = false;
    }
}
