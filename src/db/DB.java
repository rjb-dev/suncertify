/*
 * DB.java    version 1.0   date 16/12/2015
 * By rjb 
 */

package db;


/**
 *The supplied interface for the SJD Project.
 * 
 * @author rjb
 */
public interface DB {
 /**
* Reads a record from the file. Returns an array where each 
* element is a record value.
* 
* @param recNo The record number to be read from the file
* @return Array of record values stored in recNo.
* @throws RecordNotFoundException Indicates there is no record with that recNo.
*/
public String[] read(int recNo) throws RecordNotFoundException;

/**
* Modifies the fields of a record. The new value for field n
* appears in data[n]. Throws SecurityException if the record
* is locked with a cookie other than lockCookie.
*
* @param recNo The number of the record to be modified
* @param data New value for a field.
* @param lockCookie Locks record while in use. //??
* @throws RecordNotFoundException Indicates there is no record with that recNo.
* @throws SecurityException if the record is locked other than with lockCookie.
*/
public void update (int recNo, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException;

/**
* Deletes a record, making the record number and associated storage space available for reuse.
* Throws SecurityException if the record is locked with a cookie other than lockCookie.
* 
* @param recNo The number of the record to be modified.
* @param lockCookie Locks record while in use. //??
* @throws RecordNotFoundException Indicates there is no record with that recNo.
* @throws SecurityException if the record is locked other than with lockCookie.
*/
public void delete(int recNo, long lockCookie) throws RecordNotFoundException,
SecurityException;

/**
* Returns an array of record numbers that match the specified criteria.
* Field n in the database file is described by criteria [n].
* A null value in criteria[n] matches any field value. A non-null value
* in criteria[n] matches any field value that begins with criteria[n].
* (For example, "Fred" matches "Fred" or "Freddy".)
*
* @param criteria The search criteria.
* @return Array of record numbers matching the search criteria
*/
public int[] find (String[] criteria);

/**
* Creates a new record in the database (possibly re-using a deleted entry).
* Inserts the given data, and returns the record number of the new record.
* 
* @param data Takes the data to be entered into the database
* @return The record number of the new entry.
* @throws DuplicateKeyException Indicates there is already a record with that number //??
*/
public int create(String[] data) throws DuplicateKeyException;

/**
* Locks a record so it can only be updated or deleted by this client.
* Returned value is a cookie that must be used when the record is unlocked,
* updated or deleted. If the specified record is already locked by a 
* different client, the current thread gives up the CPU and consumes no
* CPU cycles until the record is unlocked.
*
* @param recNo The number of the record to be locked.
* @return The value of the cookie that must be used when the record is unlocked.
* @throws RecordNotFoundException Indicates there is no record with that recNo.
*/
public long lock(int recNo) throws RecordNotFoundException;

/**
* Releases the lock on a record. Cookie must be the cookie returned
* when the record was locked, otherwise throws SecurityException.
* 
* @param recNo The number of the record to be unlocked.
* @param cookie The number needed to unlock the record.
* @throws RecordNotFoundException Indicates there is no record with that recNo.
* @throws SecurityException Indicates the cookie was not correct.
*/
public void unlock(int recNo, long cookie) throws RecordNotFoundException,
SecurityException;

}
