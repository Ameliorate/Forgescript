package org.ame.jsforge.internal;

import org.ame.jsforge.internal.ModLoading.JSModLoader;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import java.net.URL;
import java.util.HashMap;

/**
 * @author Amelorate
 * Contains information relating to a mod.
 */
public class JSMod {
	public JSMod(ScriptContext context, int modID, URL path, int apiTarget, String name, String modVersion) {
		this.context = context;
		this.modID = modID;
		this.path = path;
		this.apiTarget = apiTarget;
		this.name = name;
		this.modVersion = modVersion;
	}

	public ScriptContext context;
	public int modID;
	public URL path;
	public int apiTarget;
	public String name;
	public String modVersion;
	public HashMap<String, URL> resources = new HashMap<>();

	public Object invoke(String invoking, boolean ignoreNoSuchMethodExceptions, Object... args) throws ScriptException {
		JSModLoader.getInstance().engine.setContext(context);
		try {
			return ((Invocable) JSModLoader.getInstance().engine).invokeFunction(invoking, args);
		}
		catch (NoSuchMethodException e) {
			if (!ignoreNoSuchMethodExceptions) {
				throw new UncheckedException(e);
			}
			else {
				return null;
			}
		}

	}
}
