/*
 * ConnectionType.java    version 1.0   date 16/12/2015
 * By rjb 
 */

package gui;

/**
 * Specifies the types of connections that can be made to the server. 
 * 
 * @author rjb
 */
public enum ConnectionType {
 /** a Serialized Objects over standard sockets based server. */
    SOCKET,
    /** direct connect - no network involved. */
    DIRECT
}
