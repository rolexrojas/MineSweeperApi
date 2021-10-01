package com.deviget.domain;

public class Cell {

        private int columnIndex;
        private int rowElement;
        private Integer rowValue;
        //gameRol: 1 Emtpy, 2 Number, 3 Bomb
        private String  gameRole;
        private boolean seen = false;

        public Cell() {
        }

        public Cell(int columnIndex, int rowElement, Integer rowValue, String gameRole){
            this.columnIndex = columnIndex;
            this.rowElement = rowElement;
            this.rowValue = rowValue;
            this.gameRole = gameRole;
        }

        public int getColumnIndex() {
            return columnIndex;
        }

        public void setColumnIndex(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        public int getRowElement() {
            return rowElement;
        }

        public void setRowElement(int rowElement) {
            this.rowElement = rowElement;
        }

        public Integer getRowValue() {
            return rowValue;
        }

        public void setRowValue(Integer rowValue) {
            this.rowValue = rowValue;
        }

        public String getGameRole() {
            return gameRole;
        }

        public void setGameRole(String gameRole) {
            this.gameRole = gameRole;
        }

        public boolean isSeen() {
            return seen;
        }

        public void setSeen(boolean seen) {
            this.seen = seen;
        }
}
