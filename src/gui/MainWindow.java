/*
 * MainWindow.java    version 1.0   date 16/12/2015
 * By rjb
  */


package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.TableColumn;

/**
 *This class is the main GUI window that provides access to
 * all required functionality.
 * 
 * @author rjb 
 *
 */
public class MainWindow extends JFrame {

    private static final long serialVersionUID = 5165L;
    /**
     * The internal reference to the GUI controller.
     */
    private GuiController controller;
    /**
     * The main application window instance.
     */
    private JFrame mainWindow;
    /**
     * The
     * <code>JTable</code> that displays the records held by the system.
     */
    private JTable mainTable = new JTable();
    /**
     * The text field that contains the user defined search String.
     */
    private JTextField txtSearch = new JTextField(20);
    /**
     * The internal reference to the the currently displayed table data.
     */
    private DBRTableModel tableData;
    boolean cboNameSelected = false;
    boolean cboLocSelected = false;
    /**
     * Colours used in GUI
     */
    Color myRose = new Color(209, 94, 136);
    Color myAqua = new Color(197, 238, 240);
    Color myChoc = new Color(54, 33, 4);
    /**
     * Form for adding/updating records
     */
    JOptionPane createForm = new JOptionPane();
    JPanel panCreate = new JPanel(new GridLayout(0, 1));
    JTextField txtName = new JTextField();
    JTextField txtLoc = new JTextField();
    JTextField txtSpec = new JTextField();
    JTextField txtStaff = new JTextField();
    JTextField txtRate = new JTextField();

    /**
     * The main gui window.
     *
     * @param args an argument specifying whether we are starting a networked
     * client (argument missing) or a standalone client (argument = "alone").
     */
    public MainWindow(String[] args) {
        super("All About Improvement");
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);

        ApplicationMode connectionType = (args.length == 0)
                ? ApplicationMode.NETWORK
                : ApplicationMode.ALONE;

        // find out where our database is
        DatabaseLocationDialog dbLocation =
                new DatabaseLocationDialog(this, connectionType);

        if (dbLocation.userCanceled()) {
            System.exit(0);
        }

        try {
            controller = new GuiController(dbLocation.getNetworkType(),
                    dbLocation.getLocation(),
                    dbLocation.getPort());
        } catch (GuiControllerException gce) {
            ApplicationRunner.handleException(
                    "Failed to connect to the database");
        }

        //add components to create/update form
        panCreate.add(new JLabel("Name: "));
        panCreate.add(txtName);
        panCreate.add(new JLabel("Location: "));
        panCreate.add(txtLoc);
        panCreate.add(new JLabel("Specialties (separate with a comma)"));
        panCreate.add(txtSpec);
        panCreate.add(new JLabel("Number of Staff:"));
        panCreate.add(txtStaff);
        panCreate.add(new JLabel("Rate:"));
        panCreate.add(txtRate);
        panCreate.add(new JLabel("To modify or add a customer number you need to use the \"Book\" or \"Release\" buttons"));

        //get an initial list of all records from the database.
        try {
            String[] allRecords = {""};
            tableData = controller.findRecord(allRecords);
            setupTable();
        } catch (GuiControllerException gce) {
            ApplicationRunner.handleException(
                    "Failed to acquire an initial list of records."
                    + "\nPlease check the DB connection.");
        }
        this.add(new TradieScreen());
        this.pack();
        this.setSize(900, 500);

        // Center on screen
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((d.getWidth() - this.getWidth()) / 2);
        int y = (int) ((d.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        this.setVisible(true);
    }

    /**
     * Uses
     * <code>tableData</code> to refresh the contents of the
     * <code>mainTable</code>.
     */
    private void setupTable() {
        mainTable.setModel(this.tableData);
        setColumnWidths();
    }

    private void refreshTable() {
        String[] criteria = {""};
        try {
            tableData = controller.findRecord(criteria);
        } catch (GuiControllerException gce) {
            gce.printStackTrace();
        }
        setupTable();
    }

    //set preferred coloumn widths
    private void setColumnWidths() {
        TableColumn column = null;
        for (int i = 0; i < 7; i++) {
            column = mainTable.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(10);
                    break;
                case 1:
                    column.setPreferredWidth(100);
                    break;
                case 2:
                    column.setPreferredWidth(4);
                    break;
                case 3:
                    column.setPreferredWidth(150);
                    break;
                case 4:
                case 5:
                case 6:
                    column.setPreferredWidth(15);
                    break;
                default:
                    column.setPreferredWidth(50);
                    break;
            }
        }
    }

    /**
     * All the items that belong within the main panel of the client
     * application.
     */
    private class TradieScreen extends JPanel {

        /**
         * A version number for this class
         */
        private static final long serialVersionUID = 5165L;

        /**
         * Constructs the main panel for the GUI.
         */
        public TradieScreen() {
            this.setLayout(new BorderLayout());

            // Setup table and add to centre
            JScrollPane tableScroll = new JScrollPane(mainTable);
            tableScroll.setSize(500, 250);
            mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            mainTable.setToolTipText("Select a record to update, book or release a booking.");
            mainTable.setBackground(myAqua);
            mainTable.setForeground(myChoc);
            mainTable.setSelectionBackground(myRose);
            mainTable.setRowHeight(24);
            mainTable.setFont(new Font("Arial", Font.PLAIN, 12));
            this.add(tableScroll, BorderLayout.CENTER);

            //Setup search panel and add to top
            JPanel panSearch = new JPanel(new FlowLayout(BoxLayout.LINE_AXIS));
            panSearch.setPreferredSize(new Dimension(300, 80));
            panSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(myRose, 4, true),
                    "Find a Tradie", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 16), myChoc));
            panSearch.setName("Search");
            JLabel lblSearch = new JLabel("Use the drop-down boxes to search by business name or location. "
                    + "You can also search by entering a keyword into the text box and clicking the Search button.");
            panSearch.add(lblSearch);
            panSearch.setBackground(myAqua);

            String[] contractors = new String[controller.nameSearch.size()];
            controller.nameSearch.toArray(contractors);
            JComboBox cboName = new JComboBox(contractors);
            cboName.addActionListener(new nameChoose());
            panSearch.add(cboName);

            String[] locations = new String[controller.locSearch.size()];
            controller.locSearch.toArray(locations);
            JComboBox cboLoc = new JComboBox(locations);
            cboLoc.addActionListener(new locChoose());
            panSearch.add(cboLoc);

            panSearch.add(txtSearch);

            JButton btnSearch = new JButton("Search");
            btnSearch.addActionListener(new keywordSearch());
            panSearch.add(btnSearch);

            //setup modify frame with add, update and delete buttons
            JPanel panModify = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panModify.setBackground(myRose);

            JButton btnAdd = new JButton("Add Record");
            btnAdd.setBackground(myRose);
            btnAdd.addActionListener(new createRecord());
            panModify.add(btnAdd);

            JButton btnUpdate = new JButton("Update Record");
            btnUpdate.setBackground(myRose);
            btnUpdate.addActionListener(new updateRecord());
            panModify.add(btnUpdate);

            JButton btnDelete = new JButton("Delete Record");
            btnDelete.setBackground(myRose);
            btnDelete.addActionListener(new deleteRecord());
            panModify.add(btnDelete);

            // Setup hiring panel
            JPanel hiringPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            hiringPanel.setBackground(myAqua);

            JButton btnBook = new JButton("Book Tradie");
            btnBook.setBackground(myAqua);
            btnBook.setForeground(myChoc);
            btnBook.setFont(new Font("Arial", Font.PLAIN, 14));
            btnBook.setToolTipText(
                    "Book the contractor selected in the above table.");
            btnBook.addActionListener(new book());
            btnBook.setRequestFocusEnabled(false);
            hiringPanel.add(btnBook);

            JButton btnRelease = new JButton("Release booking");
            btnRelease.setBackground(myAqua);
            btnRelease.setFont(new Font("Arial", Font.PLAIN, 14));
            btnRelease.setToolTipText(
                    "Release the booking selected in the above table.");
            btnRelease.addActionListener(new ReleaseBooking());
            btnRelease.setRequestFocusEnabled(false);
            hiringPanel.add(btnRelease);

            // create bottom panel to contain modify and hiring panel.
            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.add(panModify, BorderLayout.NORTH);
            bottomPanel.add(hiringPanel, BorderLayout.SOUTH);

            // Add the bottom panel to the main window
            this.add(bottomPanel, BorderLayout.SOUTH);
            this.add(panSearch, BorderLayout.NORTH);
        }
    }

    /**
     * Sets up the nameChoose combo box
     */
    private class nameChoose implements ActionListener {

        /**
         * Handles the actionPerformed event for the nameChoose combo box
         *
         * @param ae
         */
        public void actionPerformed(ActionEvent ae) {
            JComboBox cb = (JComboBox) ae.getSource();
            cboNameSelected = true;
            String selected = (String) cb.getSelectedItem();

            if (cboLocSelected == false) {
                String[] criteria = {selected};
                try {
                    tableData = controller.findRecord(criteria);
                } catch (GuiControllerException gce) {

                }
            } else {
                String locSelected = (String) tableData.getValueAt(1, 2);
                String[] criteria = {selected, locSelected};
                cboLocSelected = false;
                cboNameSelected = false;
                try {
                    tableData = controller.findRecord(criteria);
                } catch (GuiControllerException gce) {
                    
                }
            }
            setupTable();
        }
    }

    /**
     * Sets up the locChoose combo box
     */
    private class locChoose implements ActionListener {

        /**
         * Handles the actionPerformed event for the nameChoose combo box
         *
         * @param ae
         */
        public void actionPerformed(ActionEvent ae) {
            cboLocSelected = true;
            JComboBox cb = (JComboBox) ae.getSource();
            String selected = (String) cb.getSelectedItem();

            if (cboNameSelected == false) {
                String[] criteria = {selected};
                try {
                    tableData = controller.findRecord(criteria);
                } catch (GuiControllerException gce) {

                }
            } else {
                String nameSelected = (String) tableData.getValueAt(1, 1);

                String[] criteria = {selected, nameSelected};
                cboNameSelected = false;
                cboLocSelected = false;
                try {
                    tableData = controller.findRecord(criteria);
                } catch (GuiControllerException gce) {

                }
            }
            setupTable();
        }
    }

    /**
     * Handles the keyword search
     */
    private class keywordSearch implements ActionListener {

        /**
         * Handles the actionPerformed event for the search button
         *
         * @param ae
         */
        public void actionPerformed(ActionEvent ae) {

            String search = txtSearch.getText();

            if (search.contains(",")) {
                String[] criteria = search.split("\\s*,\\s*");
                try {
                    tableData = controller.findRecord(criteria);//do a complicated search
                } catch (GuiControllerException gce) {
                    gce.printStackTrace();
                }
            } else {
                search = search.trim();
                String[] criteria = {search};
                try {
                    tableData = controller.findRecord(criteria);
                } catch (GuiControllerException gce) {
                    gce.printStackTrace();
                }
            }
            setupTable();
        }
    }

    /**
     * Handles making a booking
     */
    private class book implements ActionListener {//???

        private static final String UNAVAILABLE = "Unable to book - another customer may have reserved this tradie.";

        /**
         * book Handles the actionPerformed event for the book button.
         *
         * @param ae The event initiated by the book button.
         */
        public void actionPerformed(ActionEvent ae) {

            long lockCookie = 0;
            String custID = "";
            int recNo = 0;
            String[] recordArray = new String[tableData.getColumnCount() - 1];
            boolean booked = false;
            int index = mainTable.getSelectedRow();

            if ((index >= 0) && (index <= mainTable.getRowCount()) && tableData.getValueAt(index, 6).equals("")) {
                String recString = (tableData.getValueAt(index, 0)).toString();
                recNo = Integer.parseInt(recString);

                try {
                    lockCookie = controller.getLock(recNo);
                    if (lockCookie != 0) {

                        JOptionPane inputCustID = new JOptionPane();
                        inputCustID.setVisible(true);
                        custID = (String) JOptionPane.showInputDialog(
                                mainWindow, "Please enter your ID number",
                                "Customer ID Request", JOptionPane.QUESTION_MESSAGE);

                    } else {
                        ApplicationRunner.handleException(UNAVAILABLE);
                    }
                } catch (GuiControllerException gce) {
                    ApplicationRunner.handleException(gce.getMessage());
                }

                for (int i = 1; i < tableData.getColumnCount() - 1; i++) {
                    recordArray[i - 1] = (tableData.getValueAt(index, i)).toString();
                }
                recordArray[5] = custID;
                try {
                    booked = controller.bookRecord(recNo, recordArray, lockCookie);

                    if (booked) {
                    } else {
                        ApplicationRunner.handleException(UNAVAILABLE);
                    }
                } catch (GuiControllerException gce) {

                    ApplicationRunner.handleException(gce.getMessage());
                }

            } else {
                JOptionPane.showMessageDialog(mainWindow, "You have not selected a bookable record", "Tradie unavailable", JOptionPane.WARNING_MESSAGE);
            }
            refreshTable();
        }
    }

    /**
     *
     * Handles releasing a record.
     */
    private class ReleaseBooking implements ActionListener {

        private static final String RETURN_FAILURE_MSG = "Release operation failed.";

        /**
         * Handles the actionPerformed event for the release button
         *
         * @param ae The event initiated by the release button.
         */
        public void actionPerformed(ActionEvent ae) {

            int recNo;
            long lockCookie = 0;
            int index = mainTable.getSelectedRow();
            String[] recordArray = new String[mainTable.getColumnCount() - 1];

            if ((index >= 0) && (index <= mainTable.getRowCount())) {
                String recString = (mainTable.getValueAt(index, 0)).toString();
                recNo = Integer.parseInt(recString);

                try {
                    lockCookie = controller.getLock(recNo);
                } catch (GuiControllerException gce) {
                    ApplicationRunner.handleException("Failed to acquire a lock for the selected record");
                }
                if (lockCookie != 0) {
                    String custID = "";

                    for (int i = 1; i < mainTable.getColumnCount() - 1; i++) {
                        recordArray[i - 1] = (mainTable.getValueAt(index, i)).toString();
                    }
                    recordArray[5] = custID;

                    try {
                        controller.releaseBooking(recNo, recordArray, lockCookie);

                    } catch (GuiControllerException gce) {
                        ApplicationRunner.handleException(RETURN_FAILURE_MSG);
                    }
                }
            }
            refreshTable();
        }
    }

    /**
     *
     * Handles creating a record.
     */
    public class createRecord implements ActionListener {

        /**
         * Handles the actionPerformed event for the Add Record button
         *
         * @param ae The event initiated by the Add Record button.
         */
        public void actionPerformed(ActionEvent ae) {

            createForm.setVisible(true);
            int result = JOptionPane.showConfirmDialog(null, panCreate, "Add a Record", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String name = txtName.getText();
                    String loc = txtLoc.getText();
                    String spec = txtSpec.getText();
                    String staff = txtStaff.getText();
                    String rate = txtRate.getText();
                    String[] recordArray = {name, loc, spec, staff, rate};
                    int recNo = controller.createRecord(recordArray);
                    JOptionPane.showMessageDialog(null, "The new record number is " + recNo,
                            "Record creation successful", JOptionPane.INFORMATION_MESSAGE);
                } catch (GuiControllerException gce) {
                    ApplicationRunner.handleException("Record creation was not successful." + gce.getMessage());
                }
            }
            refreshTable();
        }
    }

    /**
     *
     * Handles updating a record.
     */
    private class updateRecord implements ActionListener {

        /**
         * Handles the actionPerformed event for the Update Record button
         *
         * @param ae The event initiated by the Update Record button.
         */
        public void actionPerformed(ActionEvent ae) {

            int index = mainTable.getSelectedRow();

            //if the row index is valid, get the record number for that record.
            if ((index >= 0) && (index <= mainTable.getRowCount())) {
                int recNo = Integer.parseInt((mainTable.getValueAt(index, 0)).toString());
                txtName.setText((mainTable.getValueAt(index, 1)).toString());
                txtLoc.setText((mainTable.getValueAt(index, 2)).toString());
                txtSpec.setText((mainTable.getValueAt(index, 3)).toString());
                txtStaff.setText((mainTable.getValueAt(index, 4)).toString());
                txtRate.setText((mainTable.getValueAt(index, 5)).toString());
                createForm.setVisible(true);

                int result = JOptionPane.showConfirmDialog(null, panCreate, "Update Record",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {

                    try {
                        String name = txtName.getText();
                        String loc = txtLoc.getText();
                        String spec = txtSpec.getText();
                        String staff = txtStaff.getText();
                        String rate = txtRate.getText();
                        String[] recordArray = {name, loc, spec, staff, rate};
                        long lockCookie = controller.getLock(recNo);
                        controller.updateRecord(recNo, recordArray, lockCookie);

                    } catch (GuiControllerException gce) {
                        ApplicationRunner.handleException(gce.getMessage());
                    }
                }

            } else {
                JOptionPane.showMessageDialog(null, "You need to select a record before clicking Update Record",
                        "No Record Selected", JOptionPane.WARNING_MESSAGE);
            }
            refreshTable();
            txtName.setText("");
            txtLoc.setText("");
            txtSpec.setText("");
            txtStaff.setText("");
            txtRate.setText("");
        }
    }

    /**
     *
     * Handles deleting a record.
     */
    private class deleteRecord implements ActionListener {

        /**
         * Handles the actionPerformed event for the Delete Record button
         *
         * @param ae The event initiated by the Delete Record button.
         */
        public void actionPerformed(ActionEvent e) {

            int index = mainTable.getSelectedRow();
            int recNo;
            long lockCookie = 0;

            if ((index >= 0) && (index <= mainTable.getRowCount())) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete? Deletion can not be undone.", "Delete is permanent.", JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    recNo = Integer.parseInt(mainTable.getValueAt(index, 0).toString());
                    try {
                        lockCookie = controller.getLock(recNo);
                        controller.delete(recNo, lockCookie);
                    } catch (GuiControllerException gce) {
                        ApplicationRunner.handleException(gce.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No records have been changed.", "Delete Cancelled", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "You need to select a record before clicking Delete Record", "No Record Selected", JOptionPane.WARNING_MESSAGE);
            }
            refreshTable();
        }
    }
}
