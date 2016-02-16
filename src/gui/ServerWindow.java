/*
 * ServerWindow.java    version 1.0   date 16/12/2015
 * By Rebecca Blundell 91023656 
  */


package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * The graphical user interface the user sees when they start the server
 * application.
 *
 * @author Rebecca Blundell 91023656
 */
public class ServerWindow extends JFrame {
    // All strings are defined in static final declarations at the start of the
    // class. 

    private static final String START_BUTTON_TEXT = "Start server";
    private static final String EXIT_BUTTON_TEXT = "Exit";
    private static final String EXIT_BUTTON_TOOL_TIP = "Stops the server as soon as it is safe to do so";
    private static final String INITIAL_STATUS = "Enter configuration parameters and click \""
            + START_BUTTON_TEXT + "\"";
    /**
     * A version number for this class.
     */
    private static final long serialVersionUID = 5165L;
    // All user modifiable fields are defined here, along with all buttons.
    private ConfigOptions configOptionsPanel =
            new ConfigOptions(ApplicationMode.SERVER);
    private JButton startServerButton = new JButton(START_BUTTON_TEXT);
    private JButton exitButton = new JButton(EXIT_BUTTON_TEXT);
    private JLabel status = new JLabel();

    /**
     * Constructs the standard Server GUI Window and displays it on screen.
     */
    public ServerWindow() {
        super("All About Improvement Server Application");
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setResizable(false);

        // Add the menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
        quitMenuItem.setMnemonic(KeyEvent.VK_Q);
        fileMenu.add(quitMenuItem);

        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        this.setJMenuBar(menuBar);

        this.add(configOptionsPanel, BorderLayout.NORTH);
        this.add(commandOptionsPanel(), BorderLayout.CENTER);

        status.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(status, BorderLayout.CENTER);
        this.add(statusPanel, BorderLayout.SOUTH);

        // load saved configuration
        SavedConfiguration config = SavedConfiguration.getSavedConfiguration();

        // validation for the database location.
        String databaseLocation =
                config.getParameter(SavedConfiguration.DATABASE_LOCATION);
        System.out.println("ServWin Database Location: " + databaseLocation);//debug
        configOptionsPanel.setLocationFieldText(
                (databaseLocation == null) ? "" : databaseLocation);

        // in this case if there is a network, it is always sockets
        String networkType = config.getParameter(SavedConfiguration.NETWORK_TYPE);

        startServerButton.setEnabled(true);
       
        configOptionsPanel.setPortNumberText(
                config.getParameter(SavedConfiguration.SERVER_PORT));

        status.setText(INITIAL_STATUS);

        this.pack();
        // Center on screen
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((d.getWidth() - this.getWidth()) / 2);
        int y = (int) ((d.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        this.setVisible(true);
    }

    /**
     * This private panel provides the buttons the user may click
     *
     * @return a panel containing the mode change buttons.
     */
    private JPanel commandOptionsPanel() {
        JPanel thePanel = new JPanel();
        thePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        startServerButton.addActionListener(new StartServer());
        startServerButton.setEnabled(false);
        thePanel.add(startServerButton);

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
        exitButton.setToolTipText(EXIT_BUTTON_TOOL_TIP);
        thePanel.add(exitButton);

        return thePanel;
    }

    /**
     * A private class that will get called if the "Start" button is called. It
     * will disable any buttons that should no longer be clicked upon, extract
     * the configuration information from the server application, saving it
     * where necessary, and start the server.<p>
     */
    private class StartServer implements ActionListener {

        /**
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent ae) {
            configOptionsPanel.setLocationFieldEnabled(false);
            configOptionsPanel.setPortNumberEnabled(false);
            configOptionsPanel.setBrowseButtonEnabled(false);
            startServerButton.setEnabled(false);

            String port = configOptionsPanel.getPortNumberText();
            String databaseLocation = configOptionsPanel.getLocationFieldText();
            new NetworkStarterSockets(databaseLocation, port, status);

        }
    }
}
