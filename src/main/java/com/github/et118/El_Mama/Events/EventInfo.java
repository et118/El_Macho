package com.github.et118.El_Mama.Events;

public class EventInfo {
    private String name;
    private String description;
    private String category;
    private boolean enabled;
    public EventInfo(String name, String description, String category, boolean enabled) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
