/*
 * DatabaseLocationDialog.java    version 1.0   date 16/12/2015
 * By rjb 
 */
package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import javax.swing.*;

/**
 * This class provides a standard dialog box which allows the user to select the
 * location of the database
 *
 * @author rjb
 */
public class DatabaseLocationDialog extends WindowAdapter
        implements ActionListener, Observer {
    /*
     * The strings for the title and buttons in the dialog box. 
     */

    private static final String TITLE = "Please enter database location";
    private static final String CONNECT = "Connect";
    private static final String EXIT = "Exit";

    /*
     * Some values for possible port ranges     
     */
    private static final int LOWEST_PORT = 0;
    private static final int HIGHEST_PORT = 65535;

    /*
     * Dialog box components. 
     */
    private JOptionPane options = null;
    private JDialog dialog = null;
    private JButton connectButton = new JButton(CONNECT);
    private JButton exitButton = new JButton(EXIT);

    /*
     * The common panel that is used by both the client and the server for
     * specifying where the database is.
     */
    private ConfigOptions configOptions = null;

    /*
     * Flags to show whether enough information has been provided for us to
     * start the application.
     */
    private boolean validDb = false;
    private boolean validPort = false;
    private boolean validCnx = false;

    /*
     * Details specified in the configOptions pane detailing where the database
     * is.
     */
    private String location = null;
    private String port = null;
    private ConnectionType networkType = null;

    /**
     * Creates a dialog where the user can specify the location of the database
     * and IP address and port number; or search and select the database on a
     * local drive.
     *
     * @param parent Defines the Component that is to be the parent of this
     * dialog box.
     * @param connectionMode Specifies the type of connection (standalone or
     * networked)
     */
    public DatabaseLocationDialog(Frame parent,
            ApplicationMode connectionMode) {
        configOptions = (new ConfigOptions(connectionMode));
        configOptions.getObservable().addObserver(this);

        // load saved configuration
        SavedConfiguration config = SavedConfiguration.getSavedConfiguration();

        // the port and connection type are irrelevant in standalone mode
        if (connectionMode == ApplicationMode.ALONE) {
            validPort = true;
            validCnx = true;
            networkType = ConnectionType.DIRECT;
            location = config.getParameter(SavedConfiguration.DATABASE_LOCATION);
        } else {
            // otherwise it is a network Socket connection.         
            networkType = ConnectionType.SOCKET;
            validCnx = true;
            port = config.getParameter(SavedConfiguration.SERVER_PORT);
            configOptions.setPortNumberText(port);
            validPort = true;
            location = config.getParameter(SavedConfiguration.SERVER_ADDRESS);
        }

        // validation for database location
        if (location != null) {
            configOptions.setLocationFieldText(location);
            validDb = true;
        }

        options = new JOptionPane(configOptions,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION);

        connectButton.setActionCommand(CONNECT);
        connectButton.addActionListener(this);

        boolean allValid = validDb && validPort && validCnx;
        connectButton.setEnabled(allValid);

        exitButton.setActionCommand(EXIT);
        exitButton.addActionListener(this);

        options.setOptions(new Object[]{connectButton, exitButton});

        dialog = options.createDialog(parent, TITLE);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(this);
        dialog.setVisible(true);

    }

    /**
     * Callback event handler to process situations where the user has closed
     * the window rather than clicking one of the buttons.
     */
    public void windowClosing(WindowEvent we) {
        processCommand(EXIT);
    }

    /**
     * Callback event handler to process clicks on any of the buttons.
     */
    public void actionPerformed(ActionEvent ae) {
        processCommand(ae.getActionCommand());
    }

    /**
     * Common event handling code.
     *
     * @param command a String representing the action that occurred.
     */
    private void processCommand(String command) {
        dialog.setVisible(false);
        if (CONNECT.equals(command)) {
            options.setValue(JOptionPane.OK_OPTION);
        } else {
            options.setValue(JOptionPane.CANCEL_OPTION);
        }
    }

    /**
     * ConfigOptions just sends updates to registered Observers whenever
     * anything changes. The notifications here and validated.
     */
    public void update(Observable o, Object arg) {

        if (!(arg instanceof OptionUpdate)) {

            return;
        }

        OptionUpdate optionUpdate = (OptionUpdate) arg;

        // load saved configuration
        SavedConfiguration config = SavedConfiguration.getSavedConfiguration();

        switch (optionUpdate.getUpdateType()) {
            case DB_LOCATION_CHANGED:
                location = (String) optionUpdate.getPayload();
                if (configOptions.getApplicationMode()
                        == ApplicationMode.ALONE) {
                    File f = new File(location);

                    if (f.exists() && f.canRead() && f.canWrite()) {
                        validDb = true;
                        config.setParameter(SavedConfiguration.DATABASE_LOCATION,
                                location);
                    }
                } else {
                    try {
                        if (location.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
                            String[] quads = location.split("\\.");
                            byte[] address = new byte[quads.length];
                            for (int i = 0; i < quads.length; i++) {
                                address[i] = new Integer(quads[i]).byteValue();
                            }
                            InetAddress.getByAddress(address);
                        } else {
                            InetAddress.getAllByName(location);
                        }
                        validDb = true;
                        config.setParameter(SavedConfiguration.SERVER_ADDRESS,
                                location);
                    } catch (UnknownHostException uhe) {
                        validDb = false;
                    }
                }
                break;
            case PORT_CHANGED:
                port = (String) optionUpdate.getPayload();
                int p = Integer.parseInt(port);

                if (p >= LOWEST_PORT && p < HIGHEST_PORT) {
                    validPort = true;
                    config.setParameter(SavedConfiguration.SERVER_PORT, port);
                } else {
                    validPort = false;
                }
                break;
            default:

                break;
        }

        boolean allValid = validDb && validPort && validCnx;
        connectButton.setEnabled(allValid);
    }

    /**
     * Returns the location of the database, which may be either the path to the
     * local database, or the address of the network server hosting the
     * database.
     *
     * @return the location of the database.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the port number the network server should be listening on for
     * client connections.
     *
     * @return the port number for connecting to the network server.
     */
    public String getPort() {
        return port;
    }

    /**
     * Returns the type of network connection. This is always Socket, but could
     * be changed if the business wanted to add another option.
     *
     * @return the network protocol used to connect to the server.
     */
    public ConnectionType getNetworkType() {
        return networkType;
    }

    /**
     * Let the caller of this dialog know whether the user connected or
     * cancelled.
     *
     * @return true if the user cancelled or closed the window.
     */
    public boolean userCanceled() {
        if (options.getValue() instanceof Integer) {
            int status = ((Integer) options.getValue());//.intValue();
            return status != JOptionPane.OK_OPTION;
        } else {
            return false;
        }
    }
}
