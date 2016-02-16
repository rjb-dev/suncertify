/*
 * ApplicationMode.java    version 1.0   date 16/12/2015
 * By rjb 
 */


package gui;

/**
 * Specifies the modes the application can run in.
 * 
 * @author rjb
 */
public enum ApplicationMode {

    /**
     * Application will be a standalone client - no network access.
     */
    ALONE,
    /**
     * A networked client via Sockets. This is used when the user has not
     * specified any command line parameters.
     */
    NETWORK,
    /**
     * The server application.
     */
    SERVER
}
