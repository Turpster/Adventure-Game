package com._Turpster.AdventureGame;

import java.awt.*;
import javax.imageio.*;
import java.io.*;

public enum CharacterType implements Serializable
{
    Dorotheos_Spyro("Dorotheos_Spyro", 0), 
    Amor_Ceadda("Amor_Ceadda", 1), 
    Gallus_\u00c6thelno\u00f0("Gallus_\u00c6thelno\u00f0", 2), 
    Zenon_Ninian("Zenon_Ninian", 3), 
    Sarosano("Sarosano", 4);
    
    private CharacterType(final String s, final int n) {
    }
    
    static Image getCharacterAvatar(final CharacterType CT) {
        try {
            return ImageIO.read(new File(String.valueOf(CT.toString()) + ".png"));
        }
        catch (IOException e) {
            return null;
        }
    }
    
    static String getCharacterName(final CharacterType CT) {
        if (CT == CharacterType.Dorotheos_Spyro) {
            return "Spyro";
        }
        if (CT == CharacterType.Amor_Ceadda) {
            return "Amor_Ceadda";
        }
        if (CT == CharacterType.Gallus_\u00c6thelno\u00f0) {
            return "'Gul'";
        }
        if (CT == CharacterType.Zenon_Ninian) {
            return "Zenon Ninian";
        }
        if (CT == CharacterType.Sarosano) {
            return "Sarosano";
        }
        return null;
    }
    
    static CharacterType getCharacter(final String character) {
        if (character.equalsIgnoreCase("Spyro")) {
            return CharacterType.Dorotheos_Spyro;
        }
        if (character.equalsIgnoreCase("Dorotheos_Spyro")) {
            return CharacterType.Dorotheos_Spyro;
        }
        if (character.equalsIgnoreCase("Amor Ceadda")) {
            return CharacterType.Amor_Ceadda;
        }
        if (character.equalsIgnoreCase("Gul")) {
            return CharacterType.Gallus_\u00c6thelno\u00f0;
        }
        if (character.equalsIgnoreCase("Gallus_AtheLnoo")) {
            return CharacterType.Gallus_\u00c6thelno\u00f0;
        }
        if (character.equalsIgnoreCase("Zenon_Ninian")) {
            return CharacterType.Zenon_Ninian;
        }
        if (character.equalsIgnoreCase("Sarosano")) {
            return CharacterType.Sarosano;
        }
        return null;
    }
}
