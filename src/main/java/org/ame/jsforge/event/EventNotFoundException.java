package org.ame.jsforge.event;

/**
 * @author Amelorate
 * Used if no events were found under that mod and name.
 */
public class EventNotFoundException extends RuntimeException {
	public EventNotFoundException(String message) {
		super(message);
	}
}
