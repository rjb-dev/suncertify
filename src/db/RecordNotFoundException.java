/*
 * RecordNotFoundException.java    version 1.0   date 16/12/2015
 * By rjb 
 */

package db;
import java.io.IOException;

/**
 * 
 * @author rjb
 */
public class RecordNotFoundException extends IOException {
    
    public RecordNotFoundException(){
        super();
    }
    
    public RecordNotFoundException(String s){
        super(s);
    }

}
