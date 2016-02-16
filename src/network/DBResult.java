/*
 * DBResult.java    version 1.0   date 16/12/2015
 * By rjb 
  */


package network;



/**
 * <code>DBResult</code> is a wrapper for the return values from the
 * <code>DB</code> methods. The server will send this object to the socket
 * client which will inspect it for exceptions and valid return results.
 *
 * @author rjb
 */
public class DBResult implements java.io.Serializable {

    /**
     * A version number for this class.
     */
    private static final long serialVersionUID = 5165L;
    /**
     * The exception member. When an exception occurs, the original exception is
     * returned in the result.
     */
    private Exception exception = null;
    /**
     * The boolean result returned to the socket client.
     */
    private boolean booleanResult = false;
    /**
     * The boolean String array result returned to the socket client.
     */
    private String[] recordArray;
    /**
     * The int result returned to the socket client.
     */
    private int intResult;
    /**
     * The int array result returned to the socket client.
     */
    private int[] intArray;
    /**
     * The cookie result returned to the socket client.
     */
    private long lockCookie;

    /**
     * Constructor for the result returned by create
     *
     * @param intResult takes the recNo for a newly created record.
     */

    public DBResult(int intResult) {
        this.intResult = intResult;
    }

    /**
     * Constructor for the result returned by read
     *
     * @param recordArray takes the record fields as a String array.
     *
     */
    public DBResult(String[] recordArray) {
        this.recordArray = recordArray;
    }

     /**
     * Constructor for the result returned by lock
     *
     * @param lockCookie takes the lock as a long.
     *
     */    
    public DBResult(long lockCookie) {
        this.lockCookie = lockCookie;
    }

    /**
     * Constructor for the integer array result returned by find.
     *
     * @param intArray takes the array of recNos returned by find.
     *
     */
    public DBResult(int[] intArray) {
        this.intArray = intArray;
    }

    /**
     * Constructor for the boolean result returned by DBSocketClient 
     * to avoid a NullPointerException being thrown there. This result
     * is not necessary, but it avoids the problem I was having with my
     * delete method returning a null result.
     *
     * @param retVal a boolean
     */
    public DBResult(boolean retVal) {
        this.booleanResult = retVal;
    }

    
    /**
     * Constructor which takes in an exception being returned by the database.
     *
     * @param e The <code>Exception</code> being returned.
     */
    public DBResult(Exception e) {
        this.exception = e;
    }

    /**
     * Returns the boolean value of this result.
     *
     * @return The boolean value of the query result.
     */
    public boolean getBoolean() {
        return booleanResult;
    }

    
    /**
     * Returns the int value of this result.
     *
     * @return The int value of the query result.
     */
    public int getInt() {
        return intResult;
    }

    /**
     * Returns the
     * <code>Exception</code> value of this result.
     *
     * @return The <code>Exception</code> object of the query result.
     */
    public Exception getException() {
        return this.exception;
    }

      /**
     * Returns the String array value of this result.
     *
     * @return The string array value of the query result.
     */
    public String[] getRecordArray() {
        return this.recordArray;
    }
  /**
     * Returns the int array value of this result.
     *
     * @return The int array value of the query result.
     */
    public int[] getIntArray() {
        return this.intArray;
    }

    /**
     * Returns the long value of this result.
     *
     * @return The long value of the query result.
     */
    public long getLockCookie() {
        return this.lockCookie;
    }

   
    /**
     * Indicates if this object is a wrapper for an
     * <code>Exception</code>.
     *
     * @return true if an exception has occurred, false if everything is ok.
     */
    public boolean isException() {
        return exception != null;
    }

    /**
     * Creates a string representing this result.
     *
     * @return a string representing this DvdCommand.
     */
    public String toString() {
        return "DBResult:["
                + "String []: " + recordArray + "; "
                + "Exception: " + exception + "; "
                + "booleanResult: " + booleanResult + "; "
                + "int []: " + intArray + "; "
                + "int: " + intResult + "; "
                + "lockCookie: " + lockCookie + "; "
                + "]";
    }
}
