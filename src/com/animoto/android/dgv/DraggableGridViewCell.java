package com.animoto.android.dgv;

public interface DraggableGridViewCell {

    //Should return the position of the cell in the underlying data.
    public int getPositionInData() throws CellDataNotSetException;

    public class CellDataNotSetException extends Exception {
        public CellDataNotSetException(String msg) {
            super(msg);
        }
    }

    public boolean changeDataForCell(int number);

}
