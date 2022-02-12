package com.github.et118.El_Macho.Commands;

public class CommandInfo {
    private String[] prefixes;
    private String name;
    private String description;
    private String category;
    private boolean enabled;

    public CommandInfo(String[] prefixes, String name, String description, String category, boolean enabled) {
        this.prefixes = prefixes;
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

    public String[] getPrefixes() {return this.prefixes;}
}
