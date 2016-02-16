/*
 * SocketCommand.java    version 1.0   date 16/12/2015
 * By Rebecca Blundell 91023656 rjb
  */


package network;

/**
 * 
 * 
 *The enumerated list of possible commands we can send from the client to the
 * server.
 * 
 * @author rjb
 */
public enum SocketCommand {
/** indicates that the command object has not been set. */
    UNSPECIFIED,
    /** request will be performing a Find action. */
    FIND,
    /** request will be reading a record. */
    READ,
    /** adding a lock to the lockmap. */
    LOCK,
    /** removing a lock from the lockmap */
    UNLOCK,
    /** updating status of a DVD. */
    UPDATE,
    /** creating a new DB record. */
    CREATE,
    /** delete a DVD record. */
    DELETE,
    
}
