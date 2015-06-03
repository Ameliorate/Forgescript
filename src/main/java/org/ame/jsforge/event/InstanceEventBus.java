package org.ame.jsforge.event;

import java.util.HashMap;

/**
 * @author Amelorate
 * Allows having events like UnivercalEventBus without requiring unnesasary args.
 */
public class InstanceEventBus {
	private HashMap<String, EventAction> subscribers = new HashMap<>();

	public Object call(String event, Object... args) {
		return subscribers.get(event).call(args);
	}

	public void register(String event, EventAction action) {
		subscribers.put(event, action);
	}
}
