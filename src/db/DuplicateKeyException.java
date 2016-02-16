/*
 * DuplicateKeyException.java    version 1.0   date 16/12/2015
 * By rjb 
 */

package db;

/**
 *
 * @author rjb
 */
public class DuplicateKeyException extends Exception{

    public DuplicateKeyException(){
        super();
    }
    
    public DuplicateKeyException(String s){
        super(s);
    }
}
