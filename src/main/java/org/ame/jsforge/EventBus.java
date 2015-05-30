package org.ame.jsforge;

import org.ame.jsforge.internal.JSMod;

import java.util.HashMap;

/**
 * @author Amelorate
 * Handles events for mods.
 */
public class EventBus {
	private static HashMap<String, JSMod[]> eventHandlers = new HashMap<>();

	public static void callAll(String event, Object... args) {
		// TODO: Implement callAll.
	}

	public static Object call(JSMod mod, String event, Object... args) {
		// TODO: Implement call.
		return null;
	}

	public static void register(String event, JSMod registering) {
		JSMod[] oldHandlers = eventHandlers.get(event);
		if (oldHandlers == null) {
			eventHandlers.put(event, new JSMod[]{registering});
		}
		else {
			JSMod[] newHandlers = new JSMod[oldHandlers.length + 1];
			System.arraycopy(oldHandlers, 0, newHandlers, 0, oldHandlers.length + 1);
			newHandlers[newHandlers.length] = registering;
		}
	}
}
