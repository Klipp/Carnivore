package org.rsg.carnivore.cache;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.rsg.carnivore.CarnivorePacket;
import org.rsg.carnivore.Constants;
//import org.rsg.lib.Log;
import org.rsg.lib.time.TimeUtilities;

public class SpeedometerCache extends java.util.Stack<TimestampedObject> {
	private static final long serialVersionUID = Constants.VERSION;
	public final int ONE_SECOND = 1000;
	public final int FRAME_DEFAULT = 1 * ONE_SECOND;
	public final int frame; //how long (in milliseconds) that things should stay in the cache
	
	public SpeedometerCache() {
		super();
		this.frame = FRAME_DEFAULT;
	}

	public void pruneToFrame() {
		try {
			Iterator<?> iterator = iterator();
			while (iterator.hasNext()) {
				TimestampedObject to = (TimestampedObject) iterator.next();
				if(to.timestamp < (TimeUtilities.currentTime() - frame)) {
					iterator.remove();
				}
			}
		} catch (ConcurrentModificationException e) {
//			Log.debug("["+this.getClass().getName()+"] pruneToFrame ConcurrentModificationException");
			try {
				Thread.sleep(100);
				pruneToFrame(); //recursion -- should be ok if it happens rarely 
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}

	public void add(CarnivorePacket p) {
		this.add(new TimestampedObject(p));
	}
	
	public String toString() {
		String s = "";
		for(int i = 0; i < this.size(); i++) {
			s = s + "|";
		}
		return "Cache(" + this.size() + ")" + s;
	}
}
