package com.example.GUIClass;

public class ElementData {
    int row;
    int col;
    String boardType;

    /**
     * used for returning multiple data at once.  Used for button IDs in the GUI.
     */
    public ElementData(){
        this.col = -1;
        this.row = -1;
        this.boardType = "";
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }
}
