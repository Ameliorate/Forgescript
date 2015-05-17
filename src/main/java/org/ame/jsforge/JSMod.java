package org.ame.jsforge;

import javax.script.ScriptContext;
import java.net.URL;

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
}
