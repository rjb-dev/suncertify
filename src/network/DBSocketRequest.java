/*
 * DBSocketRequest.java    version 1.0   date 16/12/2015
 * By Rebecca Blundell 91023656 
  */


package network;

import java.io.*;
import java.net.*;
import db.*;

/**
 * Interprets the command object and passes the request to the database.
 * 
 * @author Rebecca Blundell 91023656
 */
public class DBSocketRequest extends Thread {

    /**
     * Holds the socket connection to the client.
     */
    private Socket client;
    
    /**
     * The reference to the internal
     * <code>DB</code> instance.
     */
    private DB database;

    /**
     * The Request Constructor.
     *
     * @param dbLocation the location of the data file.
     * @param socketClient The socket end-point that listens for a client
     * request.
     * @throws IOException on network error or on data file access error.
     */
    public DBSocketRequest(String dbLocation, Socket socketClient)
            throws IOException {

        super("DBRequestSocket");
        this.client = socketClient;
        this.database = new Data(dbLocation);
    }

    /**
     * Sets up a new thread to read from and write to the database.
     */
    public void run() {

        try {
            ObjectOutputStream out =
                    new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream in =
                    new ObjectInputStream(client.getInputStream());

            for (;;) {
                DBCommand cmdObj = (DBCommand) in.readObject();
                out.writeObject(execCmdObject(cmdObj));
            }
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Helper method that takes the command object from the client and hands it
     * to the db.
     *
     * @param dbCmd The command object from the socket client.
     * @return Object The return result from the database wrapped in      * a <code>DBResult</code> object.
     */
    private Object execCmdObject(DBCommand dbCmd) {

        DBResult result = null;

        try {

            switch (dbCmd.getCommandId()) {
                case FIND:
                    String[] criteria = dbCmd.getCriteria();
                    result = new DBResult(database.find(criteria));
                    break;

                case READ:
                    int recNo = dbCmd.getRecNo();
                    String[] recordArray = database.read(recNo);
                    result = new DBResult(recordArray);
                    break;

                case CREATE:
                    recordArray = dbCmd.getRecordArray();
                    int intResult = database.create(recordArray);
                    result = new DBResult(intResult);
                    break;

                case UPDATE:
                    long lockCookie = dbCmd.getLockCookie();
                    String[] data = dbCmd.getRecordArray();
                    recNo = dbCmd.getRecNo();
                    database.update(recNo, data, lockCookie);
                    break;

                case LOCK:
                    recNo = dbCmd.getRecNo();
                    result = new DBResult(database.lock(recNo));
                    break;

                case UNLOCK:
                    lockCookie = dbCmd.getLockCookie();
                    recNo = dbCmd.getRecNo();
                    database.unlock(recNo, lockCookie);
                    break;

                case DELETE:
                    lockCookie = dbCmd.getLockCookie();
                    recNo = dbCmd.getRecNo();
                    database.delete(recNo, lockCookie);

                default:

                    break;
            }
        } catch (Exception e) {
            result = new DBResult(e);

        }

        return result;
    }
}
