package com.example.GUIClass;

import java.util.Scanner;

public class ElementIDParser {
   ElementData d;

    public ElementIDParser(){
        d = new ElementData();
    }

    /**
     * parses the ID of a GUI element (like `Upper;3;9`) and returns a custom data package of the constituent data it contains
     * @return the data extracted from the string ID
     */
    public ElementData parseID(String s){
        Scanner sc = new Scanner(s);
        sc.useDelimiter(";");
        d.setBoardType(sc.next());
        d.setRow(sc.nextInt());
        d.setCol(sc.nextInt());
        return d;
    }
}
