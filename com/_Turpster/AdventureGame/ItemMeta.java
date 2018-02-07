package com._Turpster.AdventureGame;

public class ItemMeta
{
    private String DisplayName;
    private int ItemData;
    
    public ItemMeta(final String DisplayName, final int ItemData) {
        this.DisplayName = DisplayName;
        this.setItemData(ItemData);
    }
    
    public String getDisplayName() {
        return this.DisplayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.DisplayName = displayName;
    }
    
    public int getItemData() {
        return this.ItemData;
    }
    
    public void setItemData(final int itemData) {
        this.ItemData = itemData;
    }
}
