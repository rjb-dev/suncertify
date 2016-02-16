/*
 * OptionUpdate.java    version 1.0   date 16/12/2015
 * By Rebecca Blundell 91023656 
  */

package gui;

/**
 * This class is for transferring information about changes to
 * <code>ConfigOptions</code> panel to any interested observers.
 * 
 * @author Rebecca Blundell 91023656
 *
 */
public class OptionUpdate {
    /**
     * The enumerated list of possible updates that can be sent from the
     * ConfigOptions panel. Only one of the following options can possibly be
     * passed with this value object.
     */
    public enum Updates {

        /**
         * The user has specified the location of the data file or the address
         * of the server.
         */
        DB_LOCATION_CHANGED,
        /**
         * The user has changed the port number the server is expected to be
         * listening on.
         */
        PORT_CHANGED;
    }

     /*
     * The values that will be transfered.
     */
    private Updates updateType = null;
    private Object payload = null;

    /**
     * Empty constructor.
     */
    public OptionUpdate() {
    }

    /**
     * A constructor for the update type and any relevant information.
     *
     * @param updateType the type of update that has occurred.
     * @param payload any relevant information that we would like to pass at
     * the same time.
     */
    public OptionUpdate(Updates updateType, Object payload) {
        this.updateType = updateType;
        this.payload = payload;
    }

    /**
     * Sets the type of update that has occurred.
     *
     * @param updateType the type of update that has occurred.
     */
    public void setUpdateType(Updates updateType) {
        this.updateType = updateType;
    }

    /**
     * Gets the type of update that has occurred.
     *
     * @return the type of update that has occurred.
     */
    public Updates getUpdateType() {
        return this.updateType;
    }

    /**
     * Sets any information considered relevant to this update.
     *
     * @param payload any relevant information.
     */
    public void getPayload(Object payload) {
        this.payload = payload;
    }

    /**
     * Gets any information considered relevant to this update.
     *
     * @return any relevant information.
     */
    public Object getPayload() {
        return payload;
    }
}



