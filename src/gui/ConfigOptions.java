/*
 * ConfigOptions.java    version 1.0   date 16/12/2015
 * By Rebecca Blundell 91023656 
 */


package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Observable;
import javax.swing.*;

/*
 * A panel used by both the client and server application to specify the
 * configuration options. 
 * 
 * @author Rebecca Blundell 91023656
 */
public class ConfigOptions extends JPanel {
    // All strings are defined in final static declarations

    private static final String DB_LOCATION_LABEL = "Database location: ";
    private static final String SERVER_PORT_LABEL = "Server port: ";
    private static final String DATABASE_EXTENSION = "db";
    private static final String DATABASE_FILE_CHOOSER_DESCRIPTION = "Database files (*." + DATABASE_EXTENSION + ")";
    /**
     * A version number for this class
     */
    private static final long serialVersionUID = 5165L;
    /**
     * An Observable class so interested users of this class can receive
     * automatic updates whenever user options change.
     */
    private ConfigObservable observerConfigOptions = new ConfigObservable();
    // All user modifiable fields are defined here, along with all buttons.
    private JTextField locationField = new JTextField(40);
    private JButton browseButton = new JButton("...");
    private JTextField portNumber = new JTextField(4);
    private String location = null;
    private String port = null;
    private ApplicationMode applicationMode = ApplicationMode.ALONE;

    /**
     * Creates a new instance of ConfigOptions - the panel for configuring
     * database connectivity.
     *
     * @param applicationMode one of <code>ALONE</code>, <code>NETWORK</code>,
     * or <code>SERVER</code>.
     * @see ApplicationMode
     */
    public ConfigOptions(ApplicationMode applicationMode) {
        super();
        this.applicationMode = applicationMode;
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        this.setLayout(gridbag);
        constraints.insets = new Insets(2, 2, 2, 2);

        // Build the Data file location row
        JLabel dbLocationLabel = new JLabel(DB_LOCATION_LABEL);//the label for the file chooser
        gridbag.setConstraints(dbLocationLabel, constraints);
        this.add(dbLocationLabel);

        if (applicationMode == ApplicationMode.NETWORK) {
            constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        } else {
            constraints.gridwidth = GridBagConstraints.RELATIVE;
        }
        locationField.addFocusListener(new ActionHandler());
        locationField.setName(DB_LOCATION_LABEL);
        gridbag.setConstraints(locationField, constraints);
        this.add(locationField);

        if ((applicationMode == ApplicationMode.SERVER)
                || (applicationMode == ApplicationMode.ALONE)) {
            browseButton.addActionListener(new BrowseForDatabase());
            constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
            gridbag.setConstraints(browseButton, constraints);
            this.add(browseButton);
        }


        if ((applicationMode == ApplicationMode.SERVER)
                || (applicationMode == ApplicationMode.NETWORK)) {

            constraints.weightx = 0.0;
            JLabel serverPortLabel = new JLabel(SERVER_PORT_LABEL);
            constraints.gridwidth = 1;
            constraints.anchor = GridBagConstraints.EAST;
            gridbag.setConstraints(serverPortLabel, constraints);
            this.add(serverPortLabel);
            portNumber.addFocusListener(new ActionHandler());
            portNumber.setName(SERVER_PORT_LABEL);
            constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
            constraints.anchor = GridBagConstraints.WEST;
            gridbag.setConstraints(portNumber, constraints);
            this.add(portNumber);
        }
    }

    /**
     * Utility method to inform our observers of any changes the user makes to
     * the parameters on screen.
     *
     * @param updateType Enum specify which field has changed
     * @param payLoad the new data the user just entered.
     */
   
    private void updateObservers(OptionUpdate.Updates updateType,
            Object payLoad) {
        OptionUpdate update = new OptionUpdate(updateType, payLoad);
        observerConfigOptions.setChanged();
        observerConfigOptions.notifyObservers(update);
    }

    /**
     * A class to handle user interactions with the panel. This sends an update
     * to observers if the user changes anything in the panel
     */
    private class ActionHandler implements ActionListener, FocusListener {

        /**
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent ae) {
            //required to be implemented, but not used. 
        }

        /**
         * {@inheritDoc}
         */
        public void focusGained(FocusEvent e) {
            //required to be implemented, but not used. 
        }

        /**
         * {@inheritDoc}
         */
        public void focusLost(FocusEvent e) {
            if (DB_LOCATION_LABEL.equals(e.getComponent().getName())
                    && (!locationField.getText().equals(location))) {
                location = locationField.getText();
                updateObservers(OptionUpdate.Updates.DB_LOCATION_CHANGED,
                        location.trim());
            }

            if (SERVER_PORT_LABEL.equals(e.getComponent().getName())
                    && (!portNumber.getText().equals(port))) {
                port = portNumber.getText();
                updateObservers(OptionUpdate.Updates.PORT_CHANGED, port.trim());
            }
        }
    }

    /**
     * A class that provides the user with the ability to browse for the
     * database rather than forcing them to remember the name.
     */
    private class BrowseForDatabase implements ActionListener {

        /**
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent ae) {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
            chooser.addChoosableFileFilter(
                    new javax.swing.filechooser.FileFilter() {
                        /**
                         * display files ending in ".db" or any other object
                         * (directory or other selectable device).
                         */
                        public boolean accept(File f) {
                            if (f.isFile()) {
                                return f.getName().endsWith(DATABASE_EXTENSION);
                            } else {
                                return true;
                            }
                        }

                        /**
                         * provide a description for the types of files we are
                         * allowing to be selected.
                         */
                        public String getDescription() {
                            return DATABASE_FILE_CHOOSER_DESCRIPTION;
                        }
                    });

            // if the user selected a file, update the file name on screen
            if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(null)) {
                locationField.setText(chooser.getSelectedFile().toString());
                location = locationField.getText();
                updateObservers(OptionUpdate.Updates.DB_LOCATION_CHANGED,
                        location.trim());
            }
        }
    }

    /**
     * Returns the mode the application will be running in (networked, alone or
     * server).
     *
     * @return the mode the application will be running in (networked, alone or
     * server).
     * @see ApplicationMode
     */
    public ApplicationMode getApplicationMode() {
        return this.applicationMode;
    }

    /**
     * Returns the contents of the database location field.
     *
     * @return the contents of the database location field.
     */
    public String getLocationFieldText() {
        return locationField.getText();
    }

    /**
     * Sets the contents of the database location field.
     *
     * @param locationField the contents of the database location field.
     */
    public void setLocationFieldText(String locationField) {
        location = locationField;
        this.locationField.setText(locationField);
    }

    /**
     * Configures whether the location field is enabled or not.
     *
     * @param enabled true if the location field is enabled.
     */
    public void setLocationFieldEnabled(boolean enabled) {
        this.locationField.setEnabled(enabled);
    }

    /**
     * Configures whether the browse button is enabled or not.
     *
     * @param enabled true if the browse button is enabled.
     */
    public void setBrowseButtonEnabled(boolean enabled) {
        this.browseButton.setEnabled(enabled);
    }

    /**
     * Returns the contents of the port number text field.
     *
     * @return the contents of the port number text field.
     */
    public String getPortNumberText() {
        return portNumber.getText();
    }

    /**
     * Sets the contents of the port number text field.
     *
     * @param portNumber the contents of the port number text field.
     */
    public void setPortNumberText(String portNumber) {
        port = portNumber;
        this.portNumber.setText(portNumber);
    }

    /**
     * Configures whether the port number field is enabled or not.
     *
     * @param enabled true if the port number field is enabled.
     */
    public void setPortNumberEnabled(boolean enabled) {
        this.portNumber.setEnabled(enabled);
    }
    
    /**
     * A class that Observers can register themselves
     * with in order to receive updates as things change within this panel.
     */
    private class ConfigObservable extends Observable {

        /**
         * {@inheritDoc}
         */
        @Override
        public void setChanged() {
            super.setChanged();
        }
    }

    /**
     * Returns an instance of the
     * <code>Observable</code> class. Observers can register themselves with
     * this class in order to receive updates as things change within this
     * panel.
     *
     * @return an instance of the <code>Observable</code> class.
     */
    public Observable getObservable() {
        return observerConfigOptions;
    }
}
