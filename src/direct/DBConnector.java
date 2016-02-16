/*
 * DBConnector.java    version 1.0   date 16/12/2015
 * By rjb 
  */


package direct;

import db.*;
import java.io.*;

/**
 * A  DBConnector is used in cases where the GUI controller wants to make a
 * connection to the data file. In this case, that connection is a direct
 * connection.
 * 
 * @author rjb
 */
public class DBConnector {

    /**
     * It has private constructor so it can't be instantiated.
     */
    private DBConnector() {
    }

    /**
     * Static method that gets a database handle.
     * The DB is a local object.
     *
     * @param dbLocation the path to the database.
     * @return A <code>DB</code> instance.
     * @throws IOException Thrown if an <code>IOException</code> is 
     * thrown trying to access the Data class.
     */
    public static DB getLocal(String dbLocation)
            throws IOException {
        return new Data(dbLocation);
    }
}
