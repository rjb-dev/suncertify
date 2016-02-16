/*
 * GuiController.java    version 1.0   date 16/12/2015
 * By Rebecca Blundell 91023656 
 */


package gui;

import java.util.*;
import db.*;

/**
 *
 * Handles all interactions between the GUI layer and the data layer.
 *
 * @author Rebecca Blundell 91023656
 */
public class GuiController {

    /**
     * Holds a reference to the database
     */
    private DB connection;
    /**
     * Lists that populate the combo search boxes.
     */
    List nameSearch = new ArrayList();
    List locSearch = new ArrayList();

    /**
     * Creates a
     * <code>GuiController</code> instance with a specified connection type.
     *
     * @param connectionType the method of connecting the client and database.
     * @param dbLocation the path to the data file, or the network address of
     * the server hosting the data file.
     * @param port the port the network server is listening on.
     * @throws GuiControllerException on communication error with database.
     */
    public GuiController(ConnectionType connectionType,
            String dbLocation, String port)
            throws GuiControllerException {
        try {
            switch (connectionType) {
                case DIRECT:
                    connection = direct.DBConnector.getLocal(dbLocation);
                    break;
                case SOCKET:
                    connection = network.DBConnector.getRemote(dbLocation, port);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid connection type specified");
            }
  
        } catch (java.io.IOException ioe) {
            throw new GuiControllerException(ioe.getCause());
        }
    }

    /**
     * Finds a database record that contains the submitted criteria. An empty
     * search returns all results.
     *
     * @param criteria A String array containing the search items.
     * @return A DBRTableModel containing records matching the criteria.
     * @throws GuiControllerException Indicates a database or network level
     * exception.
     */
    public DBRTableModel findRecord(String[] criteria) throws GuiControllerException {
        DBRTableModel out = new DBRTableModel();
        int[] results;
        try {
            results = this.connection.find(criteria);
            for (int i : results) {
                String[] recordArray = this.connection.read(i);
                out.addDBRecord(recordArray);
                populateComboBoxes(recordArray);
            }
        } catch (RecordNotFoundException rnfe) {
            throw new GuiControllerException(rnfe.getCause());
        }
        return out;
    }

    /**
     * Reads a specific record that matches the passed in record number.
     *
     * @param recNo The record number to read.
     * @return a DBRTableModel containing the record.
     * @throws GuiControllerException Indicates a database or network level
     * exception.
     */
    public DBRTableModel readRecord(int recNo) throws
            GuiControllerException {
        DBRTableModel out = new DBRTableModel();
        try {
            String[] recordArray = this.connection.read(recNo);
            out.addDBRecord(recordArray);
        } catch (RecordNotFoundException rnfe) {
            throw new GuiControllerException(rnfe.getCause());
        }
        return out;
    }

    /**
     * Locks a record for particular use by the client requesting the lock.
     *
     * @param recNo The record number of the record to be locked.
     * @return A long value to be held by the client while the record is
     * modified.
     * @throws GuiControllerException Indicates a database or network level
     * exception.
     */
    public long getLock(int recNo) throws GuiControllerException {
        long lockCookie = 0;
        try {
            lockCookie = this.connection.lock(recNo);
            if (lockCookie != 0) {
                return lockCookie;
            } else {
                throw new GuiControllerException("Could not obtain a lock on the requested record.");
            }
        } catch (RecordNotFoundException rnfe) {
            throw new GuiControllerException(rnfe.getCause());
        }
    }

    /**
     * Creates a new record in the database.
     *
     * @param recordArray A String array containing the record fields.
     * @return an int containing the new record number.
     * @throws GuiControllerException Indicates a database or network level
     * exception.
     */
    public int createRecord(String[] recordArray) throws GuiControllerException {
        int recNo;
        try {
            recNo = connection.create(recordArray);
        } catch (DuplicateKeyException dke) {
            throw new GuiControllerException(dke.getCause());
        }
        return recNo;
    }

    /**
     * Updates an existing database record.
     *
     * @param recNo The record number to be updated
     * @param data A String array containing the record fields.
     * @param lockCookie The correct lock for modifying the record.
     * @throws GuiControllerException Indicates a database or network level
     * exception.
     */
    public void updateRecord(int recNo, String[] data, long lockCookie) throws GuiControllerException {
        try {
            connection.update(recNo, data, lockCookie);
        } catch (RecordNotFoundException rnfe) {
            throw new GuiControllerException("Update failed" + rnfe);
        } catch (SecurityException se) {
            throw new GuiControllerException("Update failed" + se);
        } finally {
            try {
                connection.unlock(recNo, lockCookie);
            } catch (RecordNotFoundException rnfe) {
                throw new GuiControllerException(rnfe.getCause());
            } catch (SecurityException se) {
                throw new GuiControllerException(se.getCause());
            }
        }
    }

    /**
     * Allows a client to book a record
     *
     * @param recNo The record number of the record to be booked.
     * @param dbr A String array containing the record fields.
     * @param lockCookie The correct lock for modifying the record.
     * @return A boolean indicating whether the booking was successful or not.
     * @throws GuiControllerException Indicates a database or network level
     * exception.
     */
    public boolean bookRecord(int recNo, String[] dbr, long lockCookie) throws GuiControllerException {
        boolean booked;
        try {
            connection.update(recNo, dbr, lockCookie);
            booked = true;
        } catch (SecurityException se) {
            booked = false;
            throw new GuiControllerException(se.getCause());

        } catch (RecordNotFoundException rnfe) {
            booked = false;
            throw new GuiControllerException(rnfe.getCause());
        }
        return booked;
    }

    /**
     * Releases a booking
     *
     * @param recNo The record number of the record to be released.
     * @param recordArray an array containing the record values
     * @param lockCookie the cookie proving the client holds the lock for this
     * record.
     * @return A boolean indicating if the return operation was successful.
     * @throws GuiControllerException Indicates a database or network level
     * exception.
     */
    public boolean releaseBooking(int recNo, String[] recordArray, long lockCookie) throws GuiControllerException {
        boolean released = false;
        try {
            connection.update(recNo, recordArray, lockCookie);
            released = true;
        } catch (java.io.IOException e) {
            throw new GuiControllerException(e);
        } finally {
            return released;
        }
    }

    /**
     * Populates the combo boxes used for name and location search.
     *
     * @param recordArray A String array containing the record fields.
     */
    public void populateComboBoxes(String[] recordArray) {
        if (nameSearch.contains(recordArray[1])) {
        } else {
            nameSearch.add(recordArray[1]);
        }
        if (locSearch.contains(recordArray[2])) {
        } else {
            locSearch.add(recordArray[2]);
        }
    }

    /**
     * Deletes a database record.
     *
     * @param recNo The record number to be deleted.
     * @param lockCookie The correct lock for modifying the record.
     * @throws GuiControllerException Indicates a database or network level
     * exception.
     */
    public void delete(int recNo, long lockCookie) throws GuiControllerException {
        try {
            connection.delete(recNo, lockCookie);
        } catch (RecordNotFoundException rnfe) {
            throw new GuiControllerException(rnfe.getCause());
        } catch (SecurityException se) {
            throw new GuiControllerException(se.getCause());
        }
    }
}
