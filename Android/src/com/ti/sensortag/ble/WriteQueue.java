package com.ti.sensortag.ble;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import android.util.Log;

/**
 * Class for queuing the BLE write operations writeDescriptor and writeCharacteristc.
 * 
 * This class is needed because you cannot have two writes waiting for callbacks.
 * e.g. you cannot call writeDescriptor, and then writeDescriptor again before 
 * the onDescriptorWrite callback is received.
 * */
public class WriteQueue {

    private static final String TAG = "WriteQueue";
    /**
     * The runnable at head is the current runnable waiting for a callback.
     * An empty list implies that no callbacks are expected.
     * */
    private final LinkedList<Runnable> writes = new LinkedList<Runnable>();

    public synchronized void queueRunnable(Runnable write) {
	if(writes.isEmpty()){
	    new Thread(write).start();
	}

	writes.addLast(write);

	Log.v(TAG, "Queue size: " + writes.size());
    }

    /**
     * This method is called when a callback is returned, allowing us to issue a new write.
     * 
     * @throws NoSuchElementException The queue should never have to be asked to deque if it is empty because
     * elements are only popped after callbacks.
     * */
    public synchronized void issue() {
	if(writes.isEmpty()){
	    Log.w(TAG, "That was weird, no runnable waiting for callback, yet we recieved a callback." +
		    " It could be because we flushed the queue.");
	}
	else {
	    writes.removeFirst();
	}

	if(!writes.isEmpty()){
	    Runnable write = writes.peekFirst();
	    new Thread(write).start();
	}
	Log.v(TAG, "Queue size: " + writes.size());
    }

    /**
     * If issue was called for every queue runnable call then the above code would work great.
     * But in the real world callbacks are lost, which will cause 
     * all the other runnables waiting in line to wait forever.
     * 
     * Flush is called to cancel all waiting runnables.
     */
    public synchronized void flush() {
	Log.v(TAG, "Flushed queue of size: " + writes.size());
	writes.clear();
    }
}
