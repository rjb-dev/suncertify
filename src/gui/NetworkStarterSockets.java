/*
 * NetworkStarterSockets.java    version 1.0   date 16/12/2015
 * By rjb
  */


package gui;

import javax.swing.JLabel;


/**
 * Starts the server that accepts Sockets connections.
 *
 * @author rjb
 */
public class NetworkStarterSockets {

    /**
     * An error code to be passed back to the operating system itself if the
     * port number provided is invalid.
     */
    public static final int ERR_CODE_INVALID_PORT_NUMBER = -1;

    /*
     * Strings that appear in log messages and in the status bar.
     */
    //private static final String INVALID_PORT_NUMBER = "Invalid port number ";
    private static final String SERVER_STARTING = "Starting server.";
    private static final String SERVER_RUNNING = "Server running.";
    /**
     * Our default port - the same as the standard RMI port.
     */
    private int port = java.rmi.registry.Registry.REGISTRY_PORT;

    /**
     * Creates a new instance of NetworkStarterSockets.
     *
     * @param dbLocation the location of the data file on a local hard drive.
     * @param port the port number the socket server will listen on.
     * @param status a label on the server GUI we can update with our status.
     */
    public NetworkStarterSockets(String dbLocation, String port,
            JLabel status) {
        try {
            this.port = Integer.parseInt(port);
            status.setText(SERVER_STARTING);
            network.DBSocketServer.register(dbLocation, this.port);

            status.setText(SERVER_RUNNING);

            // Save our configuration now that it all seems to be working.
            SavedConfiguration config = SavedConfiguration.getSavedConfiguration();

            config.setParameter(SavedConfiguration.DATABASE_LOCATION,
                    dbLocation);

            config.setParameter(SavedConfiguration.SERVER_PORT, port);
            config.setParameter(SavedConfiguration.NETWORK_TYPE,
                    "" + ConnectionType.SOCKET);
        } catch (NumberFormatException nfe) {
            System.exit(ERR_CODE_INVALID_PORT_NUMBER);
        }
    }
}
