/*
 * DBCommand.java    version 1.0   date 16/12/2015
 * By Rebecca Blundell 91023656 
  */


package network;

import db.*;

/**
 * A DBCommand object is an object that stores data as well as the command to be
 * executed.
 *
 * @author Rebecca Blundell 91023656
 */
public class DBCommand implements java.io.Serializable {

    /**
     * A version number for this class.
     */
    private static final long serialVersionUID = 5165L;
    /**
     * The type of action this command object represents. This is the action
     * that the Data class will execute.
     */
    private SocketCommand commandId;
    /**
     * An internal reference to the data passed in the command.
     */
    private DBRecord dbr = null;
    private String[] recordArray;
    private int recNo;
    private long lockCookie;
    private boolean worked;

    /**
     * Default constructor.
     */
    public DBCommand() {
        this(SocketCommand.UNSPECIFIED);
    }

    /**
     * Constructor that requires the type of command to execute as a parameter.
     *
     * @param command The id of the command the server is to perform.
     */
    public DBCommand(SocketCommand command) {
        //calls the next constructor.
        this(command, new DBRecord());
    }

    /**
     * Constructor that requires the type of command and the record object.
     *
     * @param command The id of the command the server is to perform.
     * @param dbr the DBRecord object that the command will process.
     */
    public DBCommand(SocketCommand command, DBRecord dbr) {
        setCommandId(command);
        this.dbr = dbr;
    }

    /**
     * Constructor that requires the type of command, the record number, a
     * string array containing the fields and a lockCookie. Used for the update
     * query.
     *
     * @param command The id of the command the server is to perform.
     * @param recNo The number of the record to update
     * @param data The string array containing the fields of the record to be
     * updated
     * @param lockCookie The lock required to update.
     */
    public DBCommand(SocketCommand command, int recNo, String[] data, long lockCookie) {
        setCommandId(command);
        this.recNo = recNo;
        this.recordArray = data;
        this.lockCookie = lockCookie;
    }

    /**
     * Constructor that requires the type of command, the record number and a
     * lockCookie. Used for delete and unlock.
     *
     * @param command The id of the command the server is to perform.
     * @param recNo The number of the record to update
     * @param lockCookie The lock required to update.
     */
    public DBCommand(SocketCommand command, int recNo, long lockCookie) {
        setCommandId(command);
        this.recNo = recNo;
        this.lockCookie = lockCookie;
    }

    /**
     * Constructor that requires the type of command and a string array
     * containing the record fields. Used for create and find.
     *
     * @param command The id of the command the server is to perform.
     * @param recordArray The string array containing the fields of the record to be
     * updated
     */
    public DBCommand(SocketCommand command, String[] recordArray) {
        setCommandId(command);
        this.recordArray = recordArray;
    }

    /**
     * Constructor that requires the type of command and the record number. Used
     * for read and lock.
     *
     * @param command The id of the command the server is to perform.
     * @param recNo The number of the record to update
     */
    public DBCommand(SocketCommand command, int recNo) {
        setCommandId(command);
        this.recNo = recNo;
    }

    /**
     * Sets the id of the method that this object will call in the
     * <code>DB</code> interface.
     *
     * @param id An integer indicating the method this object will call in the
     * <code>DB</code> interface.
     */
    protected void setCommandId(SocketCommand id) {
        this.commandId = id;
    }

    /**
     * Retrieves the command id specified for this object.
     *
     * @return The integer representing the command id
     */
    protected SocketCommand getCommandId() {
        return commandId;
    }

    /**
     * Gets the
     * <code>DBRecord</code> object contained in this class.
     *
     * @return A handle to the <code>DBRecord</code> member.
     */
    protected DBRecord getDBRecord() {
        return this.dbr;
    }
    /**
     * Gets the
     * <code>recordArray</code> object contained in this class.
     *
     * @return The String [] <code>recordArray</code>.
     */
    protected String[] getRecordArray() {
        return this.recordArray;
    }
    /**
     * Gets the
     * <code>recNo</code> int contained in this class.
     *
     * @return The int, <code>recNo</code>.
     */
    protected int getRecNo() {
        return this.recNo;
    }
    /**
     * Gets the
     * long <code>lockCookie</code>  contained in this class.
     *
     * @return The long <code>lockCookie</code>.
     */
    protected long getLockCookie() {
        return this.lockCookie;
    }
    /**
     * Gets the
     * <code>criteria</code> array contained in this class.
     *
     * @return The String [] <code>recordArray</code>.
     */
    protected String[] getCriteria() {
        return this.recordArray;
    }

    /**
     * Creates a string representing this command.
     *
     * @return a string representing this DBCommand.
     */
    public String toString() {
        return "DBCommand["
                + "SocketCommand: " + commandId + ", "
                + "DBRecord: " + dbr + ", "
                + "Record Array: " + recordArray + ", "
                + "Record number: " + recNo + ", "
                + "Lock cookie: " + lockCookie + ", "
                + "boolean : " + worked + ", "
                + "]";
    }
}
