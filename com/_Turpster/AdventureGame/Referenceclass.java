package com._Turpster.AdventureGame;

import java.util.*;
import java.io.*;

public class Referenceclass
{
    private Formatter formatter;
    private Scanner scanner;
    
    public Referenceclass(final String filename) {
        try {
            this.formatter = new Formatter("test.txt");
            this.scanner = new Scanner(new File(String.valueOf(filename) + ".dat"));
        }
        catch (FileNotFoundException e) {
            System.out.println("could not find file");
        }
    }
    
    public void openFile() {
        try {
            this.scanner = new Scanner(new File("test.txt"));
        }
        catch (FileNotFoundException e) {
            System.out.println("could not find file");
        }
    }
    
    public String getString() {
        while (this.scanner.hasNext()) {
            final String a = this.scanner.next();
            final String b = this.scanner.next();
            final String c = this.scanner.next();
            System.out.printf("%s %s %s \n", a, b, c);
        }
        return null;
    }
    
    public void loadFile() {
        try {
            this.formatter = new Formatter("test.txt");
        }
        catch (Exception e) {
            System.out.println("You have an error");
        }
    }
    
    public void addRecords() {
        this.formatter.format("%s%s%s", "21 ", "Reece", "Scott");
    }
    
    public void closeFile() {
        this.formatter.close();
    }
}
