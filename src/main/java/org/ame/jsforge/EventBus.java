package org.ame.jsforge;

import org.ame.jsforge.internal.Internal;
import org.ame.jsforge.internal.JSMod;
import org.ame.jsforge.internal.JSModLoader;

import javax.script.ScriptException;
import java.util.HashMap;

/**
 * @author Amelorate
 * Handles events for mods.
 */
public class EventBus {
	private static HashMap<String, JSMod[]> eventHandlers = new HashMap<>();

	public static void callAll(String event, Object... args) throws ScriptException {
		JSMod[] handlers = eventHandlers.get(event);
		for (JSMod mod : handlers) {
			call(mod, event, args);
		}
	}

	public static Object call(JSMod mod, String event, Object... args) throws ScriptException {
		return mod.invoke("Event.internal.call", true, event, args);
	}

	@Internal
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

	@Internal
	public static void register(String event, int modID) {
		JSMod mod = JSModLoader.getInstance().jsMods.get(modID);
		register(event, mod);
	}
}
