package org.ame.jsforge;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import javax.script.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * @author Amelorate
 * The main class for ForgeScript. Ducttapes everything together.
 */
@Mod(modid = ForgeScript.MODID, version = ForgeScript.VERSION)
public class ForgeScript {
	public static final String MODID = "forgescript";
	public static final String VERSION = "0.0";
	public static final int API_VERSION = 0;

	private static ScriptEngine engine;
	private static ArrayList<ScriptContext> modContexts = new ArrayList<>();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) throws ScriptException {
		loadAllJSMods();
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

	private void loadAllJSMods() throws ScriptException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		URL[] classPath = ((URLClassLoader)classLoader).getURLs();
		for(URL url: classPath) {
			if (url.getPath().contains("mods")) {
				if (isValidModFile(url)) {
					loadMod(url);
				}
				else {
					System.out.println();
				}
			}
		}
	}

	private boolean isValidModFile(URL path) {
		try {
			File file = new File(path.toURI());
			ZipFile modZipFile = new ZipFile(file);
			ZipEntry modInfo = modZipFile.getEntry("modinfo.json");
			InputStream modInfoStream = modZipFile.getInputStream(modInfo);
			JsonParser jsonParser = new JsonParser();
			JsonObject modInfoJson = (JsonObject) jsonParser.parse(new InputStreamReader(modInfoStream));

			if (modInfoJson.get("API_Version").getAsInt() < API_VERSION) {
				System.out.println("Bad Version");
				return false;
			}
			return true;
		}
		catch (ZipException | ClassCastException | NullPointerException e) {
			System.out.println("Not a js mod:" + path.getFile());
			return false;
		}
		catch (URISyntaxException | IOException e) {
			throw new AssertionError(e);
		}
	}

	private void loadMod(URL path) throws ScriptException {
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(new File(path.toURI()));
		}
		catch (IOException | URISyntaxException e) {
			throw new AssertionError(e);
		}
		Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
		while (zipEntries.hasMoreElements()) {
			ZipEntry entry = zipEntries.nextElement();
			System.out.println(entry.getName());
			if (entry.getName().startsWith("src") && !entry.isDirectory()) {
				engine = new ScriptEngineManager().getEngineByName("nashorn");	// I probably need a better way to reset the context.
				InputStreamReader api = new InputStreamReader(this.getClass().getResourceAsStream("/api/main.js"));
				engine.eval(api);
				engine.eval("var apiVersion = " + API_VERSION + ";");
				try {
					engine.eval(new InputStreamReader(zipFile.getInputStream(entry)));
				}
				catch (IOException e) {
					throw new AssertionError(e);
				}
				ScriptContext context = engine.getContext();
				modContexts.add(context);
				engine.eval("var modID = " + modContexts.indexOf(context) + ";");
			}
		}
	}

	/**
	 * Invoke a method on every mod.
	 */
	private void invokeAll(String function, boolean ignoreNoSuchMethodExceptions, Object... args) throws ScriptException, NoSuchMethodException {
		for (ScriptContext context : modContexts) {
			engine.setContext(context);
			try {
				((Invocable) engine).invokeFunction(function, args);
			}
			catch (NoSuchMethodException e) {
				if (!ignoreNoSuchMethodExceptions) {
					throw e;
				}
			}
		}
	}
}
