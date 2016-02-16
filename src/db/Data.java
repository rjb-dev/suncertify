/*
 * Data.java    version 1.0   date 16/12/2015
 * By rjb 
 */
package db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 * This class implements the provided interface and is a facade that calls
 * methods in the underlying DatabaseAccess and LockerRoom classes.
 *
 * @author rjb
 *
 */
public class Data implements DB {

    /**
     * The class that handles all our physical access to the database.
     */
    private static DatabaseAccess database = null;
    /**
     * The class that handles locking of records.
     */
    private static LockerRoom lockerRoom = new LockerRoom();

    /**
     * Constructor that takes the path of the database as a parameter.
     *
     * @param dbPath the path to the dvd_db directory
     * @throws FileNotFoundException if the database file cannot be found.
     * @throws IOException if the database file cannot be written to.
     */
    public Data(String dbPath) throws FileNotFoundException, IOException {
        database = new DatabaseAccess(dbPath);

    }

    /**
     * Reads a record from the file. Returns an array where each element is a
     * record value.
     *     
* @param recNo The record number to be read from the file
     * @return Array of record values stored in recNo.
     * @throws RecordNotFoundException Indicates there is no record with that
     * recNo.
     */
    public String[] read(int recNo) throws RecordNotFoundException {

        String[] recordArray = database.recordReader(recNo);

        return recordArray;
    }

    /**
     * Modifies the fields of a record. The new value for field n appears in
     * data[n].
     *     
* @param recNo The number of the record to be modified
     * @param data String array containing record fields.
     * @param lockCookie The long value that matches that held by the LockerRoom
     * against this record.
     * @throws RecordNotFoundException Indicates there is no record with that
     * recNo.
     * @throws SecurityException if the record is locked other than with
     * lockCookie.
     */
    public void update(int recNo, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException {

        if (lockerRoom.checkLock(recNo, lockCookie)) {
            database.update(recNo, data);
        } else {
            throw new SecurityException("You do not hold the lock for this record.");
        }
    }

    /**
     * Deletes a record, making the record number and associated storage space
     * available for reuse.
     *     
* @param recNo The number of the record to be modified.
     * @param lockCookie The long value that matches that held by the LockerRoom
     * against this record.
     * @throws RecordNotFoundException Indicates there is no record with that
     * recNo.
     * @throws SecurityException if the record is locked other than with
     * lockCookie.
     */
    public void delete(int recNo, long lockCookie) throws RecordNotFoundException, SecurityException {
        if (lockerRoom.checkLock(recNo, lockCookie)) {
            database.delete(recNo);
        } else {
            throw new SecurityException("You do not hold the lock for this record.");
        }
    }

    /**
     * Returns an array of record numbers that match the specified criteria. A
     * search with an empty string returns all records. Otherwise, the search
     * will return any record that contains all of the passed in strings. The
     * search is not case sensitive and will return any record that contains the
     * required string(s) regardless of whether they are in part or full words.
     *     
* @param criteria String array containing the search criteria.
     * @return int array of record numbers matching the search criteria
     */
    public int[] find(String[] criteria) {

        int[] results = database.findRecord(criteria);
        return results;
    }

    /**
     * Creates a new record in the database (possibly re-using a deleted entry).
     * Inserts the given data, and returns the record number of the new record.
     *     
* @param data String array containing the record to be entered into the
     * database
     * @return The record number of the new entry.
     * @throws DuplicateKeyException Indicates there is already a record with
     * that number //??
     */
    public int create(String[] data) throws DuplicateKeyException {
        int recNo = database.create(data);
        database.getRecordMap(true);
        return recNo;
    }

    /**
     * Locks a record so it can only be updated or deleted by this client.
     * Returned value is a cookie that must be used when the record is unlocked,
     * updated or deleted. If the specified record is already locked by a
     * different client, the lock fails.
     *     
* @param recNo The number of the record to be locked.
     * @return The value of the cookie that must be used when the record is
     * unlocked.
     * @throws RecordNotFoundException Indicates there is no record with that
     * recNo.
     */
    public long lock(int recNo) throws RecordNotFoundException {
        long lockCookie = 0;
        try {
            lockCookie = lockerRoom.getRecordLock(recNo);
            return lockCookie;
        } catch (InterruptedException ie) {
            throw new RecordNotFoundException(ie.getCause().toString());
        }
    }

    /**
     * Releases the lock on a record. Cookie must be the cookie returned when
     * the record was locked, otherwise throws SecurityException.
     *     
     * @param recNo The number of the record to be unlocked.
     * @param cookie The number needed to unlock the record.
     * @throws RecordNotFoundException Indicates there is no record with that
     * recNo.
     * @throws SecurityException Indicates the cookie was not correct.
     */
    public void unlock(int recNo, long cookie) throws RecordNotFoundException,
            SecurityException {

        if (lockerRoom.checkLock(recNo, cookie)) {
            lockerRoom.releaseRecordLock(recNo);
        } else {
            throw new SecurityException("You do not hold the lock for this record.");
        }

    }
}
