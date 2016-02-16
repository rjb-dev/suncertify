/*
 * DBConnector.java    version 1.0   date 16/12/2015
 * By Rebecca Blundell 91023656 
  */

package network;

import java.io.*;
import java.net.UnknownHostException;
import db.*;
/**
 * A DBConnector is used by the GUI controller to make a connection to the server.
 * 
 * @author Rebecca Blundell 91023656
 */
public class DBConnector {
/**
     * It has private constructor so it can't be instantiated.
     */
    private DBConnector() {
    }

    /**
     * static method to create a connection to the database.
     *
     * @param hostname The ip or address of the host machine.
     * @param port the socket port the server is listening on.
     * @return a connection to the database.
     * @throws UnknownHostException when the host is unreachable or cannot be
     * resolved
     * @throws IOException if a communication error occurs trying to connect
     * to the host.
     */
    public static DB getRemote(String hostname, String port)
            throws UnknownHostException, IOException {
        return new DBSocketClient(hostname, port);
    }
}
