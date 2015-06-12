package org.ame.jsforge.event;

import org.ame.jsforge.internal.JSMod;
import org.ame.jsforge.internal.ModLoading.JSModLoader;

import javax.script.ScriptException;
import java.util.HashMap;

/**
 * @author Amelorate
 * Handles events for mods.
 */
public class UniversalEventBus {
	private static UniversalEventBus mainInstance;
	private HashMap<String, EventHandler[]> eventHandlers = new HashMap<>();

	/**
	 * Gets the main instance of the event bus.
	 */
	public static UniversalEventBus getMainInstance() {
		if (mainInstance == null) {
			mainInstance = new UniversalEventBus();
		}
		return mainInstance;
	}

	public void callAll(String event, Object... args) throws ScriptException {
		EventHandler[] handlers = eventHandlers.get(event);
		for (EventHandler handler : handlers) {
			handler.action.call(args);
		}
	}

	/**
	 * @throws EventNotFoundException If the event wasn't found under that mod and name.
	 */
	public Object call(JSMod mod, String event, Object... args) throws ScriptException {
		EventHandler[] handlers = eventHandlers.get(event);
		for (EventHandler handler : handlers) {
			if (mod.equals(handler.mod)) {
				return handler.action.call(args);
			}
		}
		throw new EventNotFoundException("The event " + event + " was not found.");
	}

	public void register(String event, JSMod registering, EventAction action) {
		EventHandler[] oldHandlers = eventHandlers.get(event);
		if (oldHandlers == null) {
			eventHandlers.put(event, new EventHandler[]{new EventHandler(action, registering)});
		}
		else {
			EventHandler[] newHandlers = new EventHandler[oldHandlers.length + 1];
			System.arraycopy(oldHandlers, 0, newHandlers, 0, oldHandlers.length + 1);
			newHandlers[newHandlers.length] = new EventHandler(action, registering);
		}
	}

	public void register(String event, int modID, EventAction action) {
		JSMod mod = JSModLoader.getInstance().jsMods.get(modID);
		register(event, mod, action);
	}

	private class EventHandler {
		public EventHandler(EventAction action, JSMod mod) {
			this.action = action;
			this.mod = mod;
		}

		public EventAction action;
		public JSMod mod;
	}
}
