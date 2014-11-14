/**
 * <p>
 * <u><b>Copyright Notice</b></u>
 * </p><p>
 * The copyright in this document is the property of
 * Bath Institute of Medical Engineering.
 * </p><p>
 * Without the written consent of Bath Institute of Medical Engineering
 * given by Contract or otherwise the document must not be copied, reprinted or
 * reproduced in any material form, either wholly or in part, and the contents
 * of the document or any method or technique available there from, must not be
 * disclosed to any other person whomsoever.
 *  </p><p>
 *  <b><i>Copyright 2013-2014 Bath Institute of Medical Engineering.</i></b>
 * --------------------------------------------------------------------------
 *
 */
package com.projectnocturne.sensortag;

import android.util.Log;

import com.projectnocturne.NocturneApplication;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Class for queueing the BLE write operations writeDescriptor and
 * writeCharacteristc.
 * <p/>
 * This class is needed because you cannot have two writes waiting for
 * callbacks. e.g. you cannot call writeDescriptor, and then writeDescriptor
 * again before the onDescriptorWrite callback is received.
 */
public class WriteQueue {

    private static final String LOG_TAG = WriteQueue.class.getSimpleName() + "::";

    /**
     * The runnable at head is the current runnable waiting for a callback. An
     * empty list implies that no callbacks are expected.
     */
    private final LinkedList<SensorTagWriteRunnable> writes = new LinkedList<SensorTagWriteRunnable>();

    /**
     * If issue was called for every queue runnable call then the above code
     * would work great. But in the real world callbacks are lost, which will
     * cause all the other runnables waiting in line to wait forever.
     * <p/>
     * Flush is called to cancel all waiting runnables.
     */
    public synchronized void flush() {
        NocturneApplication.logMessage(Log.VERBOSE, LOG_TAG + "Flushed queue of size: " + writes.size());
        writes.clear();
    }

    /**
     * This method is called when a callback is returned, allowing us to issue a
     * new write.
     *
     * @throws NoSuchElementException The queue should never have to be asked to dequeue if it is
     *                                empty because elements are only popped after callbacks.
     */
    public synchronized void issue() {
        NocturneApplication.logMessage(Log.INFO, LOG_TAG + "issue()");
        if (writes.isEmpty()) {
            // Log.w(LOG_TAG,
            // "That was weird, no runnable waiting for callback, yet we received a callback."
            // + " It could be because we flushed the queue.");
        } else {
            writes.removeFirst();
        }

        if (!writes.isEmpty()) {
            final SensorTagWriteRunnable write = writes.peekFirst();
            NocturneApplication.logMessage(Log.INFO, LOG_TAG + "issue() executing [" + write.name + "]");
            new Thread(write).start();
        }
        NocturneApplication.logMessage(Log.VERBOSE, LOG_TAG + "Queue size: " + writes.size());
    }

    public synchronized void queueRunnable(final SensorTagWriteRunnable write) {
        if (writes.isEmpty()) {
            NocturneApplication.logMessage(Log.INFO, LOG_TAG + "queueRunnable() executing [" + write.name + "]");
            new Thread(write).start();
        }
        NocturneApplication.logMessage(Log.INFO, LOG_TAG + "queueRunnable() adding [" + write.name + "] to queue");
        writes.addLast(write);

        NocturneApplication.logMessage(Log.VERBOSE, LOG_TAG + "Queue size: " + writes.size());
    }
}
