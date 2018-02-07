package com._Turpster.AdventureGame;

public enum ItemName
{
    sword("sword", 0);
    
    private ItemName(final String s, final int n) {
    }
    
    public String name(final ItemName IN) {
        switch (IN) {
            case sword: {
                return "Generic Sword";
            }
            default: {
                return null;
            }
        }
    }
}
