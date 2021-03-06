package org.ame.jsforge.internal;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.ame.jsforge.internal.ModLoading.JSModLoader;

import javax.script.ScriptException;

/**
 * @author Amelorate
 * The main class for ForgeScript. Ducttapes everything together.
 */
@Mod(modid = ForgeScript.MODID, version = ForgeScript.VERSION)
public class ForgeScript {
	public static final String MODID = "forgescript";
	public static final String VERSION = "0.0";
	public static final int API_VERSION = 0;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) throws ScriptException {
		JSModLoader.getInstance().loadAllJSMods();
		try {
			invokeAll("preInit", true, event);
		}
		catch (NoSuchMethodException ignored) {}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) throws ScriptException{
		try {
			invokeAll("init", true, event);
		}
		catch (NoSuchMethodException ignored) {}
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) throws ScriptException{
		try {
			invokeAll("postInit", true, event);
		}
		catch (NoSuchMethodException ignored) {}
	}

	/**
	 * Invoke a method on every mod.
	 */
	public void invokeAll(String invoking, boolean ignoreNoSuchMethodExceptions, Object... args) throws ScriptException, NoSuchMethodException {
		for (JSMod mod : JSModLoader.getInstance().jsMods) {
			mod.invoke(invoking, ignoreNoSuchMethodExceptions, args);
		}
	}

	public Object invoke(String invoking, int id, boolean ignoreNoSuchMethodExceptions, Object... args) throws ScriptException, NoSuchMethodException {
		return JSModLoader.getInstance().jsMods.get(id).invoke(invoking, ignoreNoSuchMethodExceptions, args);
	}
}
