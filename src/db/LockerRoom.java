/*
 * LockerRoom.java    version 1.0   date 16/12/2015
 * By Rebecca Blundell 91023656 
 */

package db;

 import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;
import java.util.Random;
/**
 ***
 * Handles <b>logically</b> reserving and releasing a named object.
 * <br />
 * This should only be used by the Data class.
 * 
 * @author Rebecca Blundell 91023656
 */
public class LockerRoom {
   
    /** Length of time we will wait for a lock. */
    private static final int TIMEOUT = 5 * 1000;
    /**
     * A map to keep track of the locked DBRecords. 
     */
    private static Map<Integer, Long> locker
            = new HashMap<Integer, Long>();

    /**
     * A mutual exclusion lock limiting access to the collection.
     */
    private static Lock lock = new ReentrantLock();

    /**
     * A <code>Condition</code> that can be used to signal threads waiting for
     * the <code>lock</code> that it is now available.
     */
    private static Condition lockReleased  = lock.newCondition();
   
    long getCookie(){
        Date cookieTime = new Date();
        long myDough = cookieTime.hashCode();
        Random bakeCookie = new Random(myDough);
        long lockCookie = bakeCookie.nextLong();
        return lockCookie;
    }
    
    boolean checkLock(int recNo, long lockCookie){
        return locker.get(recNo)==lockCookie;
    }

    /**
     * Lock the requested record. 
     * If the lock doesn't succeed this method waits for 5 seconds.
     *
     * @param recNo The record number of the record to be locked.
     * @return lockCookie which needs to be provided by the client for modifying the supplied recNo.
     * @throws InterruptedException Indicates the thread is interrupted.
     */
    long getRecordLock(int recNo)
            throws InterruptedException {
        lock.lock();
        long lockCookie = 0L;
        try {
            long endTimeMSec = System.currentTimeMillis() + TIMEOUT;
            while (locker.containsKey(recNo)) {
                long timeLeftMSec = endTimeMSec - System.currentTimeMillis();
                if (!lockReleased.await(timeLeftMSec, TimeUnit.MILLISECONDS)) {
                 return lockCookie;   
                }
            }
            lockCookie = getCookie();
            locker.put(recNo, lockCookie);
            return lockCookie;
        } finally {
            // ensure lock is always released
            lock.unlock();
        }
    }

    /**
     * Unlock the requested record. This method is only called after it's proven the 
     * requesting client holds the lock, which is why it is not checked here.
     *
     * @param recNo The recNo of the record to be released.
     * 
     */
    void releaseRecordLock(int recNo) {
        lock.lock();
        try {
            locker.remove(recNo);
            lockReleased.signal();
          } finally {
            lock.unlock();
        }
    }
}




