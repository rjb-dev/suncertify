/*
 * DuplicateKeyException.java    version 1.0   date 16/12/2015
 * By Rebecca Blundell 91023656 
 */

package db;

/**
 *
 * @author Rebecca Blundell 91023656
 */
public class DuplicateKeyException extends Exception{

    public DuplicateKeyException(){
        super();
    }
    
    public DuplicateKeyException(String s){
        super(s);
    }
}
