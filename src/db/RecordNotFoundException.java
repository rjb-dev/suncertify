/*
 * RecordNotFoundException.java    version 1.0   date 16/12/2015
 * By Rebecca Blundell 91023656 
 */

package db;
import java.io.IOException;

/**
 * 
 * @author Rebecca Blundell 91023656
 */
public class RecordNotFoundException extends IOException {
    
    public RecordNotFoundException(){
        super();
    }
    
    public RecordNotFoundException(String s){
        super(s);
    }

}
