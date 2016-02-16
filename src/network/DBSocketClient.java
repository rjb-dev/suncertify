/*
 * DBSocketClient.java    version 1.0   date 16/12/2015
 * By rjb
  */


package network;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import db.*;

/**
 * DBSocketClient implements all of the <code>Data</code> methods for the
 * GUI defined in <code>DB</code>.
 * 
 * @author rjb
 * 
 */
public class DBSocketClient implements DB {

    /**
     * The socket client that gets instaniated for the socket connection.
     */
    private Socket socket = null;
    /**
     * The outputstream used to write a serialized object to a socket server.
     */
    private ObjectOutputStream oos = null;
    /**
     * The inputstream used to read a serialized object (a response)
     * from the socket server.
     */
    private ObjectInputStream ois = null;
    /**
     * The ip address of the machine the client is going to attempt a
     * connection.
     */
    private String ip = null;
    /**
     * The port number we will be connecting on.
     */
    private int port = 3000;

    /**
     * Default constructor.
     *
     * @throws UnknownHostException if unable to connect to "localhost".
     * @throws IOException on network error.
     * @throws NumberFormatException if portNumber is not valid (never happens).
     */
    public DBSocketClient()
            throws UnknownHostException, IOException, NumberFormatException {
        this("localhost", "3000");
    }

    /**
     * Constructor that takes in the hostname of the server to connect to.
     *
     * @param hostname The hostname to connect to.
     * @param portNumber the string representation of the port to connect on.
     * @throws UnknownHostException if unable to connect to "localhost".
     * @throws IOException on network error.
     * @throws NumberFormatException if portNumber is not valid.
     */
    public DBSocketClient(String hostname, String portNumber)
            throws UnknownHostException, IOException, NumberFormatException {
        ip = hostname;
        this.port = Integer.parseInt(portNumber);
        this.initialize();
    }

    /**
     * Adds a new record to the database.
     *
     * @param recordArray The String array containing the new record.
     * @throws DuplicateKeyException Indicates that the record number already exists.
     */
    public int create(String[] recordArray) throws DuplicateKeyException {
        DBCommand cmdObj = new DBCommand(SocketCommand.CREATE, recordArray);

        int result = 0;
        try {
            result = getResultFor(cmdObj).getInt();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return result;

    }

    /**
     * Reads a record from the database
     *
     * @param recNo The record number of the record you want to access.
     * @return A String Array containing the fields of the matching record.
     *
     * @throws RecordNotFoundException Indicates there is a problem accessing the required record.
     */
    public String[] read(int recNo) throws RecordNotFoundException {

        DBCommand cmdObj = new DBCommand(SocketCommand.READ, recNo);
        try {
            return getResultFor(cmdObj).getRecordArray();
        } catch (IOException ex) {

            throw new RecordNotFoundException(ex.toString());
        }
    }

    /**
     * Modifies a database record.
     *
     * @param recNo the number of the record to be modified.
     * @param data The String array containing the modified record data.
     * @param lockCookie The appropriate lock for this client to modify the record.
     * @throws RecordNotFoundException if the specified record does not exist or is deleted
     * @throws SecurityException if an incorrect lock is passed in or wraps an IOException if one is thrown.
     */
    public void update(int recNo, String[] data, long lockCookie) throws RecordNotFoundException,
            SecurityException {

        DBCommand cmdObj = new DBCommand(SocketCommand.UPDATE, recNo, data, lockCookie);
        try {
            getResultFor(cmdObj);
        } catch (IOException ioe) {
            throw new SecurityException(ioe.getCause());
        }
    }

    /**
     * Finds records matching the passed in criteria.
     *
     * @param criteria The String array containing the search criteria. An empty
     *     search returns all results.
     */
    public int[] find(String[] criteria) {
        DBCommand cmdObj = new DBCommand(SocketCommand.FIND, criteria);
        try {
            return getResultFor(cmdObj).getIntArray();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            int[] intArray = {0};
            return intArray;
        }
    }  
    
    /**
     * Deletes a database record.
     *
     * @param recNo the number of the record to be deleted.
     * @param lockCookie The appropriate lock for this client to delete the record.
     * @throws RecordNotFoundException if the specified record does not exist or is deleted
     * @throws SecurityException if an incorrect lock is passed in or wraps an IOException if one is thrown.
     */

    public void delete(int recNo, long lockCookie) throws RecordNotFoundException, SecurityException {
        DBCommand cmdObj = new DBCommand(SocketCommand.DELETE, recNo, lockCookie);
        try {
            getResultFor(cmdObj);

        } catch (IOException ioe) {
            throw new SecurityException(ioe.getCause());
        }
    }

    /**
     * Locks a record with a cookie that the client must return when updating 
     * or deleting a record.
     *
     * @param recNo the number of the record to be locked.
     * @return The long lockCookie required to perform an update/delete.
     * @throws RecordNotFoundException if the specified record does not exist or is deleted
     * or wraps an IOException.
     */
    public long lock(int recNo) throws RecordNotFoundException {
        DBCommand cmdObj = new DBCommand(SocketCommand.LOCK, recNo);
        try {
            return getResultFor(cmdObj).getLockCookie();
        } catch (IOException ioe) {
            long noCookie = 0;
            return noCookie;
        }
    }
    /**
     * Removes the lock on the specified record.
     *
     * @param recNo the number of the record to be unlocked.
     * @param The long lockCookie required to unlock the record.
     * @throws RecordNotFoundException if the specified record does not exist or is deleted
     * @throws SecurityException if the supplied lockCookie is incorrect or wraps an IOExcepiton.
     */

    public void unlock(int recNo, long lockCookie) throws RecordNotFoundException,
            SecurityException {
        DBCommand cmdObj = new DBCommand(SocketCommand.UNLOCK, recNo, lockCookie);
        try {
            getResultFor(cmdObj);
        } catch (IOException ioe) {
            throw new SecurityException(ioe.getCause());
        }
    }

    /**
     * Method that does the work of sending the request to the database and
     * getting the response back, doing any necessary conversions.
     *
     * @param command the command to be performed on the database.
     * @return a value object containing the result of the command requested.
     * @throws IOException on network error.
     */
    private DBResult getResultFor(DBCommand command) throws IOException {
        this.initialize();

        try {
            oos.writeObject(command);
            DBResult result = (DBResult) ois.readObject();

            if (result != null) {
                Exception e = result.getException();

                if (!result.isException()) {
                    return result;
                } else if (e instanceof ClassNotFoundException) {
                    throw (ClassNotFoundException) e;
                } else if (e instanceof IOException) {
                    throw (IOException) e;
                } else {
                    // returns the exception.
                    return result;
                }
            } else {
                result = new DBResult(true);//avoids a null result.
                return result;
            }
        } catch (ClassNotFoundException cnfe) {
            IOException ioe = new IOException("problem with unpacking result)");
            ioe.initCause(cnfe);
            throw ioe;
        } finally {
            closeConnections();
        }
    }

    /**
     * Closes the connections once the work is finished.
     *
     * @throws IOException on network error.
     */
    public void finalize() throws java.io.IOException {
        closeConnections();
    }

    /**
     * A method which initializes a socket connection on the specified port.
     *
     * @throws UnknownHostException if the IP address of the host could not be
     *         determined.
     * @throws IOException Thrown if the socket channel cannot be opened.
     */
    private void initialize() throws UnknownHostException, IOException {
        socket = new Socket(ip, port);

        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Closes the socket connection.
     * 
     * @throws IOException Thrown if the close operation fails.
     */
    private void closeConnections() throws IOException {
        oos.close();
        ois.close();
        socket.close();
    }
}

