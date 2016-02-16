/*
 * GuiControllerException.java    version 1.0   date 16/12/2015
 * By Rebecca Blundell 91023656 
 */
package gui;

/**
 *
 * Holds all exceptions that may occur in the
 * <code>GuiController</code>.
 *
 * @author Rebecca Blundell 91023656
 */
public class GuiControllerException extends Exception {

    /**
     * A version number for this class.
     */
    private static final long serialVersionUID = 5165L;

    /**
     * Creates a default
     * <code>GuiControllerException</code> instance.
     */
    public GuiControllerException() {
    }

    /**
     * Creates a
     * <code>GuiControllerException</code> instance and chains an exception.
     *
     * @param e The exception to wrap and chain.
     */
    public GuiControllerException(Throwable e) {
        super(e);
    }

    public GuiControllerException(String s) {
        super(s);
    }
}
