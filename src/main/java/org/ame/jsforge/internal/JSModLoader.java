package org.ame.jsforge.internal;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
 * Loads mods from the classpath.
 */
public class JSModLoader {
	public static JSModLoader instance;

	public ScriptEngine engine;

	private ArrayList<JSMod> jsMods = new ArrayList<>();
	private boolean alreadyLoadedMods = false;

	public static JSModLoader getInstance() {
		if (instance != null) {
			return instance;
		}
		else {
			instance = new JSModLoader();
			return instance;
		}
	}

	public void loadAllJSMods() throws ScriptException {
		if (alreadyLoadedMods) {
			throw new IllegalArgumentException("All the mods have already been loaded.");
		}
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
		alreadyLoadedMods = true;
	}

	public boolean isValidModFile(URL path) {
		try {
			File file = new File(path.toURI());
			ZipFile modZipFile = new ZipFile(file);
			ZipEntry modInfo = modZipFile.getEntry("modinfo.json");
			InputStream modInfoStream = modZipFile.getInputStream(modInfo);
			JsonParser jsonParser = new JsonParser();
			JsonObject modInfoJson = (JsonObject) jsonParser.parse(new InputStreamReader(modInfoStream));

			if (modInfoJson.get("API_Version").getAsInt() < ForgeScript.API_VERSION) {
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

	public void loadMod(URL path) throws ScriptException {
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(new File(path.toURI()));
		}
		catch (IOException | URISyntaxException e) {
			throw new AssertionError(e);
		}
		JsonObject modInfo = null;
		Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
		while (zipEntries.hasMoreElements()) {
			ZipEntry entry = zipEntries.nextElement();
			System.out.println(entry.getName());
			if (entry.getName().startsWith("src") && entry.getName().endsWith(".js") && !entry.isDirectory()) {
				engine = new ScriptEngineManager().getEngineByName("nashorn");	// I probably need a better way to reset the context.
				InputStreamReader api = new InputStreamReader(this.getClass().getResourceAsStream("/api/main.js"));
				System.out.println(api + " API");
				System.out.println(engine + " engine");
				engine.eval(api);
				engine.eval("var apiVersion = " + ForgeScript.API_VERSION + ";");
				try {
					engine.eval(new InputStreamReader(zipFile.getInputStream(entry)));
				}
				catch (IOException e) {
					throw new AssertionError(e);
				}
			}
			else if (entry.getName().endsWith(".json") && !entry.isDirectory()) {
				try {
					modInfo = (JsonObject) new JsonParser().parse(new InputStreamReader(zipFile.getInputStream(entry)));
				}
				catch (IOException e) {
					throw new AssertionError(e);
				}
			}
		}
		assert modInfo != null;	// Silences idea warning.
		JSMod mod = new JSMod(engine.getContext(), 0, path, modInfo.get("API_Version").getAsInt(), modInfo.get("Mod_Name").getAsString(), modInfo.get("Mod_Version").getAsString());
		jsMods.add(mod);
		engine.eval("var modID = " + jsMods.indexOf(mod) + ";");
		mod.modID = jsMods.indexOf(mod);
	}
}
