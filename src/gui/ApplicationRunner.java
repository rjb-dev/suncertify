/*
 * ApplicationRunner.java    version 1.0   date 16/12/2015
 * By rjb 
 */


package gui;


import java.awt.*;
import javax.swing.*;

/**
 *
 * This class will check the command line arguments
 * and then call the classes to start the application in the correct mode.
 *
 * @author rjb
 */
public class ApplicationRunner {

    /**
     * The method that launches the All About Improvement application.
     *
     * @param args Holds the command line inputs
     */
    public static void main(String[] args) {
        
        ApplicationRunner app = new ApplicationRunner(args);
    }

    /**
     * Sets the default Swing look and feel, then instantiates the main
     * application window.
     *
     * @param args the command line arguments, which may be one of "alone",
     * "server" or no argument.
     */
    public ApplicationRunner(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException uex) {
            uex.printStackTrace();
        } catch (ClassNotFoundException cex) {
            cex.printStackTrace();
        } catch (InstantiationException iex) {
            iex.printStackTrace();
        } catch (IllegalAccessException iaex) {
           iaex.printStackTrace();
        }
         

        if (args.length == 0 || "alone".equalsIgnoreCase(args[0])) {
            // Create an instance of the main application window
            new MainWindow(args);
        } else if ("server".equalsIgnoreCase(args[0])) {
            new ServerWindow();
        } else {
            System.err.println("Command line options may be one of:");
            System.err.println("\"server\" - starts server application");
            System.err.println("\"alone\"  - starts non-networked client");
            System.err.println("\"\"       - (no command line option): "
                               + "networked client will start");
        }
    }

    /**
     * Prompts the user with an error message in an alert window.
     *
     * @param msg The message displayed in the error window.
     */
    public static void handleException(String msg) {
        JOptionPane alert = new JOptionPane(msg,
                                            JOptionPane.ERROR_MESSAGE,
                                            JOptionPane.DEFAULT_OPTION);
        JDialog dialog = alert.createDialog(null, "Alert");

        // Center on screen
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((d.getWidth() - dialog.getWidth()) / 2);
        int y = (int) ((d.getHeight() - dialog.getHeight()) / 2);
        dialog.setLocation(x, y);

        dialog.setVisible(true);
    }


}
