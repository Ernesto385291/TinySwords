package main;

public class Objective {
    private String description;
    private boolean completed;
    private int currentProgress;
    private int targetProgress;
    
    public Objective(String description, int targetProgress) {
        this.description = description;
        this.targetProgress = targetProgress;
        this.currentProgress = 0;
        this.completed = false;
    }
    
    public void updateProgress(int amount) {
        currentProgress += amount;
        if(currentProgress >= targetProgress) {
            completed = true;
        }
    }
    
    public String getDescription() {
        return description + " (" + currentProgress + "/" + targetProgress + ")";
    }
    
    public boolean isCompleted() {
        return completed;
    }
} 