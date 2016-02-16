/*
 * DBRTableModel.java    version 1.0   date 16/12/2015
 * By Rebecca Blundell 91023656 
 */

package gui;

import db.*;
import java.util.*;
import javax.swing.table.*;

/**
 ***
 * The custom table model used by the <code>MainWindow</code> instance.
 *
 * @author Rebecca Blundell 91023656
 */


public class DBRTableModel extends AbstractTableModel {
    /**
     * A version number for this class 
     */
    private static final long serialVersionUID = 5165L;

   
    /**
     * An array of <code>String</code> objects representing the table headers.
     */
    private String [] headerNames = {"Record No.", "Name", "Location", "Specialties",
                                    "Staff", "Rate", "Customer ID"};  

    /**
     * Holds all database records displayed in the main table.
     */
    private ArrayList<String[]> dbRecords = new ArrayList<String[]>(5);//5??

    /**
     * Returns the column count of the table.
     *
     * @return An integer indicating the number or columns in the table.
     */
    public int getColumnCount() {
        return this.headerNames.length;
    }

    /**
     * Returns the number of rows in the table.
     *
     * @return An integer indicating the number of rows in the table.
     */
    public int getRowCount() {
        return this.dbRecords.size();
    }

    /**
     * Gets a value from a specified index in the table.
     *
     * @param row An integer representing the row index.
     * @param column An integer representing the column index.
     * @return The object located at the specified row and column.
     */
    public Object getValueAt(int row, int column) {
         return this.dbRecords.get(row)[column];    
    }

    /**
     * Returns the name of a column at a given column index.
     *
     * @param column The specified column index.
     * @return A String containing the column name.
     */
    @Override
    public String getColumnName(int column) {
        return headerNames[column];
    }

    /**
     * Given a row and column index, indicates if a table cell can be edited.
     *
     * @param row Specified row index.
     * @param column Specified column index.
     * @return A boolean indicating if a cell is editable.
     */
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * Adds a row of data to the table.
     */

    public void addDBRecord(String [] recordArray) {
        this.dbRecords.add(recordArray);
    }
    
    /**
     * Adds a record to the table that is passed in as a record object.
     *
     * @param dbr The DBRecord object to add to the table.
     */
    public void addDBRecord(DBRecord dbr) {
        String [] recordArray = {String.valueOf(dbr.getRecNo()), dbr.getName(), 
            dbr.getLocation(), dbr.getSpecialties(), dbr.getStaff(),
                dbr.getRate(), dbr.getCustID()};
        addDBRecord(recordArray);
    }
}

