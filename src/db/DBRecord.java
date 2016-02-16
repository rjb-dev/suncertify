/*
 * DBRecord.java    version 1.0   date 16/12/2015
 * By rjb 
 */


package db;



/**
 *This class creates an object from a database record
 * 
 * @author rjb
 */
public class DBRecord {

  
    /**
     * The size size of the Flag field for the record.
     */
    static final int FLAG_LENGTH = 2;

    /**
     * The maximum size of the Name field for the record.
     */
    static final int NAME_LENGTH = 32;
    
    /**
     * The maximum size of the Location field for the record.
     */
    static final int LOCATION_LENGTH = 64;
    
    /**
     * The maximum size of the specialties field for the record.
     */
    static final int SPECIALTIES_LENGTH = 64;
    
    /**
     * The maximum size of the staff field for the record.
     */
    static final int STAFF_LENGTH = 6;
    
    /**
     * The maximum size of the rate field for the record.
     */
    static final int RATE_LENGTH = 8;
    
    /**
     * The maximum size of the customer ID field for the record.
     */
    static final int CUSTID_LENGTH = 8;
    
     /**
     * The size of a complete record in the database. Calculated by adding all
     * the previous fields together with the 2 byte flag. Knowing this makes it easy to work with
     * an entire block of data at a time when reading and writing database records.
     */
    static final int RECORD_LENGTH = FLAG_LENGTH
                                   + NAME_LENGTH
                                   + LOCATION_LENGTH
                                   + SPECIALTIES_LENGTH
                                   + STAFF_LENGTH
                                   + RATE_LENGTH
                                   + CUSTID_LENGTH;
 
    /**
    * Stores the record number
    */
    private int recNo = 0;
    
    
    /**
    * Stores the contractor's name
    */
    private String name = "";
    
    /**
    * Stores the contractor's location
    */
    private String location = "";
    
    /**
    * Stores the contractor's name and location which together are a primary key
    */
    private String nameLoc = "";
    
    /**
    * Stores the contractor's specialties
    */
    private String specialties = "";
    
    /**
    * Stores the contractor's staff numbers
    */    
    private String staff = "";
    
    /**
    * Stores the contractor's hourly rate
    */    
    private String rate = "";
    
    /**
    * Stores the contractor's custID, i.e the customer who has booked them
    */    
    private String custID = "";

    
   /**
     * Creates an instance of this object with default values.
     */
    public DBRecord() {
    }
    
    /**
     * Creates an instance of this object with a specified list of
     * initial values. Assumes no one has booked the contractor yet
     *
     * @param name Holds the name of the contractor.
     * @param location Holds the contractor's location.
     * @param specialties Holds the types of work the contractor does.
     * @param staff Holds the number of staff the contractor has available
     * @param rate Holds the hourly rate for work.
     * 
     */
    public DBRecord(String name, String location, 
            String specialties, String staff, String rate) {
        this(name, location, specialties, staff, rate, "        ");
    }
    
    
    /**
     * Creates an instance of this object with a specified list of
     * initial values including the customer ID for the customer making an
     * initial booking.
     *
     * @param name Holds the name of the contractor.
     * @param location Holds the contractor's location.
     * @param specialties Holds the types of work the contractor does.
     * @param staff Holds the number of staff the contractor has available
     * @param rate Holds the hourly rate for work.
     * @param custID Holds the customer who has booked the record's ID number.
     */
    public DBRecord(String name, String location, 
            String specialties, String staff, String rate, String custID) {
        
        this.name = name;
        this.location = location;
        this.specialties = specialties;
        this.staff = staff;
        this.rate = rate;
        this.custID = custID;      
        this.nameLoc = name + location;
        }
    
    /**
     *  Returns the record number
     * 
     * @return recNo An <code>int</code> record number.
     */
    public int getRecNo(){
        return this.recNo;
    }
    /**
     * Sets the record number
     * 
     * @param recNo An <code>int</code> 
     */
    public void setRecNo(int recNo){
        this.recNo = recNo;
    }
    /**
     * Returns the name of the contractor
     * 
     * @return name A <code>String</code> containing the name of the contractor.
     */
    public String getName(){
        return this.name;
    }
    /**
     * Sets the name of the contractor
     * 
     * @param name A <code>String</code> containing the name of the contractor.
     */
    public void setName(String name){
         this.name = name;      
    }
    /**
     * Returns the location of the contractor
     * 
     * @return A <code>String</code> containing the location of the contractor
     */
    public String getLocation(){
        return this.location;
    }
        
     /**
     * Sets the location of the contractor
     * 
     * @param location A <code>String</code> containing the contractor's location
     */
    public void setLocation(String location){
        this.location = location;
    }
    
    /**
     * Returns the specialties of the contractor in a comma separated list
     * 
     * @return A <code>String</code> containing the specialties of the contractor
     * in a comma separated list
     */
    public String getSpecialties(){
        return this.specialties;
    }
        
     /**
     * Sets the specialties of the contractor
     * 
     * @param specialties A <code>String</code> containing the contractor's specialties
      * in a comma separated list.
     */
    public void setSpecialties(String specialties){
        this.specialties = specialties;
    }
    
     /**
     * Returns the number of staff of the contractor
     * 
     * @return A <code>String</code> containing the number of available staff of the contractor
     */
    public String getStaff(){
        return this.staff;
    }
    
    /**
     * Sets the available staff of the contractor
     * 
     * @param staff A <code>String</code> containing the number of available staff
     */
    public void setStaff(String staff){
        this.staff = staff;
    }
    
    
     /**
     * Returns the hourly rate of the contractors
     * 
     * @return A <code>String</code> containing the hourly rate of the contractor
     */
    public String getRate(){
        return this.rate;
    }
    
    /**
     * Sets the hourly rate of the contractor
     * 
     * @param rate A <code>String</code> containing the hourly rate of the contractors
     */
    public void setRate(String rate){
        this.rate = rate;
    }
    
     /**
     * Returns the customer number that has booked the contractor
     * 
     * @return A <code>String</code> containing the customer ID of the person
      * who has booked the contractor
     */
    public String getCustID(){
        return this.custID;
    }
    
    /**
     * Sets the available staff of the contractor
     * 
     * @param custID A <code>String<code> containing the number of available staff
     */
    public void setCustID(String custID){
        this.custID = custID;
    }
                                       
}
