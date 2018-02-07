package com._Turpster.AdventureGame;

public class EntityMeta
{
    private String DisplayName;
    
    public EntityMeta(final String DisplayName) {
        this.setDisplayName(DisplayName);
    }
    
    public String getDisplayName() {
        return this.DisplayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.DisplayName = displayName;
    }
}
